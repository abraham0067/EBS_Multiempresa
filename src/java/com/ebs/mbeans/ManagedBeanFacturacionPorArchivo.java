package com.ebs.mbeans;

import com.ebs.LeerExcel.LeerDatosExcel;
import fe.db.MAcceso;
import fe.db.MConfig;
import fe.db.MEmpresa;
import fe.db.MEmpresaMTimbre;
import fe.model.dao.ConfigDAO;
import fe.model.dao.EmpresaDAO;
import fe.model.dao.EmpresaTimbreDAO;
import fe.model.dao.LogAccesoDAO;
import fe.net.ClienteFacturaManual;
import fe.sat.v33.ComprobanteData;
import lombok.Getter;
import lombok.Setter;
import mx.com.ebs.emision.factura.utilierias.PintarLog;
import org.jdom.Document;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpSession;

@ManagedBean
@SessionScoped
public class ManagedBeanFacturacionPorArchivo implements Serializable {
    private int idEmpresaUsuario;
    //Contexto
    private HttpServletRequest httpServletRequest;
    private FacesContext facesContext;
    private String appContext;

    //DAOS
    private MAcceso mAcceso;
    private LogAccesoDAO daoLogs;
    private EmpresaDAO daoEmpresas;
    private EmpresaTimbreDAO daoEmpTimp;
    private ConfigDAO daoConfig;

    private String ambiente = "DESARROLLO";
    private static final String UUIDREGEXPATT = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";
    private static boolean DEBUG = false;
    private UploadedFile uploadedFile;

    @Setter
    @Getter
    private String nombreArchivo;
    @Setter
    @Getter
    private int idEmpresa;//ID de la empresa emisora

    @Setter
    @Getter
    private boolean deshabilitaBotonGeneraFActura;
    private ArrayList<String> respuestas;
    @Setter
    @Getter
    private MEmpresa empresaEmisora;
    /**
     * Creates a new instance of ManagedBeanPlantillas
     */
    public ManagedBeanFacturacionPorArchivo() {
    }

    @PostConstruct
    public void init() {
        facesContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        mAcceso = (MAcceso) httpSession.getAttribute("macceso");
        appContext = httpServletRequest.getContextPath();
        idEmpresaUsuario = mAcceso.getEmpresa().getId();
        daoEmpresas = new EmpresaDAO();
        daoLogs = new LogAccesoDAO();

        daoEmpTimp = new EmpresaTimbreDAO();
        daoConfig = new ConfigDAO();

        MConfig ambObj = daoConfig.BuscarConfigDatoClasificacion("AMBIENTE", "SERVIDOR");
        if (ambObj != null) {
            if (ambObj.getValor() != null && !ambObj.getValor().isEmpty()) {
                ambiente = ambObj.getValor().toUpperCase();
            }
        }
        MConfig debugObj = daoConfig.BuscarConfigDatoClasificacion("DEBUG", "SERVIDOR");
        if (debugObj != null) {
            if (debugObj.getValor() != null && !debugObj.getValor().isEmpty()) {
                // TODO: 14/09/2017 AGREGAR UN PARAMETRO PARA CONTROLAR EL DEBUG
                if (debugObj.getValor().equalsIgnoreCase("SI") || debugObj.getValor().equalsIgnoreCase("1")
                        || debugObj.getValor().equalsIgnoreCase("YES")) {
                    DEBUG = true;
                } else {
                    DEBUG = false;
                }
            }
        }

        idEmpresa = -1;
        respuestas = new ArrayList<>();
        uploadedFile = null;
        deshabilitaBotonGeneraFActura = true;
    }

    public void cargaArchivo(FileUploadEvent event) {
        generaMensajes("", event.getFile().getFileName() + " ha sido cargado.");
        uploadedFile = event.getFile();
        nombreArchivo = "ARCHIVO CARGADO: "+uploadedFile.getFileName();
        deshabilitaBotonGeneraFActura = false;
    }

    public void generarFactura() {
        try {
            if (uploadedFile != null) {
                FacesContext.getCurrentInstance().addMessage("frmManual", new FacesMessage(FacesMessage.SEVERITY_INFO, "Generando facturacion por archivo excel", ""));

                System.out.println("COMIENZA LA GENERACION:  " + uploadedFile.getFileName());
                PintarLog.println("Apunto de llamar al servicio de factura automatica desde el servidor");

                empresaEmisora = daoEmpresas.BuscarEmpresaId(this.idEmpresa);
                System.out.println("empresaEmisora = " + empresaEmisora.getRfcOrigen());

                MEmpresaMTimbre m = daoEmpTimp.ObtenerClaveWSEmpresaTimbre(idEmpresaUsuario);
                LeerDatosExcel genComprobanteData = new LeerDatosExcel(uploadedFile.getInputstream());
                ComprobanteData comprobanteData = genComprobanteData.getComprobanteData();
                String respuestaServicio = new ClienteFacturaManual().exeGenFactura(comprobanteData, m.getClaveWS(), ambiente, DEBUG);

                if (respuestaServicio != null) {
                    if (checkRespuestaServicio(respuestaServicio)) {
                        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "La factura se genero correctamente.", ""));
                    } else {
                        //Show all to user
                        for (String mssg : respuestas)
                            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, mssg, ""));
                    }
                }
            } else
                FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Se debe agregar un archivo antes de generar las facturas"));

        }catch(Exception e){
            e.printStackTrace(System.out);
            FacesContext.getCurrentInstance().addMessage("ERROR", new FacesMessage(FacesMessage.SEVERITY_ERROR, "", e.getMessage()));
        }finally{
            reset();
        }
    }


    private String respuestaServicioTimbrado(byte[] xml, byte[] xmlp){
        String respuesta = null;
        String claveWs = null;
        try {
            Document doc = new SAXBuilder().build(new ByteArrayInputStream(xml));
            Namespace ns = doc.getRootElement().getNamespace();
            String rfc = doc.getRootElement().getChild("Emisor",ns ).getAttributeValue("Rfc");

            claveWs = daoEmpTimp.ObtenerClaveWSEmpresaTimbre(rfc);
        } catch (Exception e){
            System.out.println("Xml mal formado: "+ e.getMessage());
        }

        if(claveWs != null )
            respuesta = new ClienteFacturaManual().exeGenFactura(xml, xmlp, claveWs, ambiente, DEBUG);
        else
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo obtener las claves de acceso para el timbrado del emisor.", "Error"));

        return respuesta;
    }

    private void reset(){
        idEmpresa = -1;
        uploadedFile = null;
        nombreArchivo = null;
        deshabilitaBotonGeneraFActura = true;
    }

    private void generaMensajes(String resultado, String mensaje){
        FacesMessage message = new FacesMessage(resultado, mensaje);
        FacesContext.getCurrentInstance().addMessage("", message);
    }

    private boolean checkRespuestaServicio(String arg) {
        respuestas.clear();
        boolean res = true;
        if (arg != null && !arg.isEmpty() && !arg.contains("null") && !arg.contains("NULL") && !arg.contains("Null")) {
            String uuidNuevo = "";
            Pattern p1 = Pattern.compile(UUIDREGEXPATT);
            Matcher matcher1 = p1.matcher(arg);
            if (matcher1.find()) {
                uuidNuevo = matcher1.group(0);
            } else {
                uuidNuevo = "";
            }
            if (uuidNuevo != null && !uuidNuevo.isEmpty() && uuidNuevo.length() == 36) {

                if (uuidNuevo.matches(UUIDREGEXPATT)) {
                    System.out.println("UUID->" + uuidNuevo);
                    //ok
                } else {
                    res = false;
                    respuestas.add("El UUID obtenido no es valido" + uuidNuevo);
                }
            } else {
                res = false;
                // TODO: 19/07/2017  BUSCAR CODIGO DE ERROR EN EL XML DE RESPUESTA
                if (arg.length() > 400) {
                    respuestas.add("{" + arg.substring(127, 391) + "}"); //Obtenemo un substring
                } else {
                    respuestas.add(arg);
                }
            }
        } else {
            respuestas.add("Ocurrio un error al procesar la factura, no se obtuvo respuesta del servidor");
            res = false;
        }
        return res;
    }



    private static  String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<cfdi:Comprobante xmlns:cfdi=\"http://www.sat.gob.mx/cfd/3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv33.xsd\" Version=\"3.3\" Serie=\"EBSF\" Folio=\"1666\" Fecha=\"2018-04-27T11:55:10\" Sello=\"QI2VXeKqnT8WisvBlJVZ8TzIDp90bkPYZtl22PDDuzrRf3DyRDuPfgNx0Tpt3+NqFQ4l9MT8pz8ebsjP1uUnkqr2oa/U4E3jo67hzmB8Zy1vHtO4Xh668I6q7nkekui++yqUJAgEUJlk73tEoWgdgT8nSJ92q52ANmTuZ5fyDEYoIQudsuWINTjnAJsfMiB4riSE+j0HhrhDkyZxkI+xsC8RXepVdQroTnbirXG8bOrh4RbvbPQK5xwPRmsMcc0B/FYlY95MX54YzwjKE4pWfb33IRMoAVwHdpQUY4AEl7rSIAgBd8ViWBjayrtq9nIIDJGT9t3FAJ4TMhCgR00ocg==\" FormaPago=\"99\" NoCertificado=\"00001000000407263084\" Certificado=\"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\" SubTotal=\"2367.00\" Moneda=\"USD\" TipoCambio=\"18.8838\" Total=\"2745.72\" TipoDeComprobante=\"I\" MetodoPago=\"PPD\" LugarExpedicion=\"11590\">\n" +
            "  <cfdi:Emisor Rfc=\"EB&amp;060116TK9\" Nombre=\"ELECTRONIC BILLS &amp; SERVICES S A DE C V\" RegimenFiscal=\"601\" />\n" +
            "  <cfdi:Receptor Rfc=\"CIS010913C63\" Nombre=\"COMPUTER INFOMATION SYSTEMS SA DE CV\" UsoCFDI=\"G01\"/>\n" +
            "  <cfdi:Conceptos>\n" +
            "    <cfdi:Concepto ClaveProdServ=\"43232406\" NoIdentificacion=\"1\" Cantidad=\"1\" ClaveUnidad=\"ACT\" Unidad=\"Servicio\" Descripcion=\"ACTUALIZACION PROCESO DE VALIDACION VIGENCIA VIA ESCANEO QR; ANALISIS Y EXTRACCION DE REPORTES DE LA INFORMACION ALMACENADA EN TABLAS, NUEVO PPORTAL DE AGENTES.\" ValorUnitario=\"2367.00\" Importe=\"2367.00\">\n" +
            "      <cfdi:Impuestos>\n" +
            "        <cfdi:Traslados>\n" +
            "          <cfdi:Traslado Base=\"2367.00\" Impuesto=\"002\" TasaOCuota=\"0.160000\" TipoFactor=\"Tasa\" Importe=\"378.72\"/>\n" +
            "        </cfdi:Traslados>\n" +
            "      </cfdi:Impuestos>\n" +
            "    </cfdi:Concepto>\n" +
            "  </cfdi:Conceptos>\n" +
            "  <cfdi:Impuestos TotalImpuestosTrasladados=\"378.72\">\n" +
            "    <cfdi:Traslados>\n" +
            "      <cfdi:Traslado Impuesto=\"002\" TasaOCuota=\"0.160000\" TipoFactor=\"Tasa\" Importe=\"378.72\"/>\n" +
            "    </cfdi:Traslados>\n" +
            "  </cfdi:Impuestos>\n" +
            "</cfdi:Comprobante>";
    private static String xmlp = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<cfdi:Comprobante xmlns:cfdi=\"http://www.sat.gob.mx/cfd/3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" print=\"print\" xsi:schemaLocation=\"http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv33.xsd\" Version=\"3.3\" Serie=\"EBSF\" Folio=\"1666\" Fecha=\"2018-04-27T11:55:10\" Sello=\"QI2VXeKqnT8WisvBlJVZ8TzIDp90bkPYZtl22PDDuzrRf3DyRDuPfgNx0Tpt3+NqFQ4l9MT8pz8ebsjP1uUnkqr2oa/U4E3jo67hzmB8Zy1vHtO4Xh668I6q7nkekui++yqUJAgEUJlk73tEoWgdgT8nSJ92q52ANmTuZ5fyDEYoIQudsuWINTjnAJsfMiB4riSE+j0HhrhDkyZxkI+xsC8RXepVdQroTnbirXG8bOrh4RbvbPQK5xwPRmsMcc0B/FYlY95MX54YzwjKE4pWfb33IRMoAVwHdpQUY4AEl7rSIAgBd8ViWBjayrtq9nIIDJGT9t3FAJ4TMhCgR00ocg==\" FormaPago=\"99\" NoCertificado=\"00001000000407263084\" Certificado=\"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\" SubTotal=\"2367.00\" Moneda=\"USD\" TipoCambio=\"18.8838\" Total=\"2745.72\" TipoDeComprobante=\"I\" MetodoPago=\"PPD\" LugarExpedicion=\"11590\" CatFormaDePago=\"Por definir\" CatLugarExpedicion=\"DIF\" CatMetodoDePago=\"Pago en parcialidades o diferido\" CatMoneda=\"Dolar americano\" CatTipoDeComprobante=\"Ingreso\" FolioErp=\"1666\">\n" +
            "  <cfdi:Emisor Rfc=\"EB&amp;060116TK9\" Nombre=\"ELECTRONIC BILLS &amp; SERVICES S A DE C V\" RegimenFiscal=\"601\" CatRegimenFiscal=\"General de Ley Personas Morales\">\n" +
            "    <cfdi:DomicilioFiscal calle=\"EUCKEN\" noExterior=\"16\" noInterior=\"PISO 6\" colonia=\"ANZURES\" localidad=\"MEXICO\" referencia=\"Ninguna\" municipio=\"MIGUEL HIDALGO\" estado=\"CIUDAD DE MÃ?XICO\" pais=\"MÃ?XICO\" codigoPostal=\"11590\"/>\n" +
            "  </cfdi:Emisor>\n" +
            "  <cfdi:Receptor Rfc=\"CIS010913C63\" Nombre=\"COMPUTER INFOMATION SYSTEMS SA DE CV\" UsoCFDI=\"G01\" CatUsoCFDI=\"Adquisición de mercancias\">\n" +
            "    <cfdi:Domicilio calle=\"MANZANAS\" noExterior=\"8\" noInterior=\"101 B\" colonia=\"TLACOQUEMECATL DEL VALLE\" referencia=\"Ninguna\" municipio=\"BENITO JUAREZ\" estado=\"CIUDAD DE MEXICO\" pais=\"MEXICO\" codigoPostal=\"03200\"/>\n" +
            "  </cfdi:Receptor>\n" +
            "  <cfdi:Conceptos>\n" +
            "    <cfdi:Concepto ClaveProdServ=\"43232406\" NoIdentificacion=\"1\" Cantidad=\"1\" ClaveUnidad=\"ACT\" Unidad=\"Servicio\" Descripcion=\"ACTUALIZACION PROCESO DE VALIDACION VIGENCIA VIA ESCANEO QR; ANALISIS Y EXTRACCION DE REPORTES DE LA INFORMACION ALMACENADA EN TABLAS, NUEVO PPORTAL DE AGENTES.\" ValorUnitario=\"2367.00\" Importe=\"2367.00\" CatClaveProdServ=\"Software de pruebas de programas\" CatClaveUnidad=\"Actividad\" posicion=\"0\">\n" +
            "      <cfdi:Impuestos>\n" +
            "        <cfdi:Traslados>\n" +
            "          <cfdi:Traslado Base=\"2367.00\" Impuesto=\"002\" TasaOCuota=\"0.160000\" TipoFactor=\"Tasa\" Importe=\"378.72\" CatImpuesto=\"IVA\"/>\n" +
            "        </cfdi:Traslados>\n" +
            "      </cfdi:Impuestos>\n" +
            "      <cfdi:Param0/>\n" +
            "    </cfdi:Concepto>\n" +
            "    <cfdi:Comment9999 posicion=\"9999\"/>\n" +
            "  </cfdi:Conceptos>\n" +
            "  <cfdi:Impuestos TotalImpuestosTrasladados=\"378.72\">\n" +
            "    <cfdi:Traslados>\n" +
            "      <cfdi:Traslado Impuesto=\"002\" TasaOCuota=\"0.160000\" TipoFactor=\"Tasa\" Importe=\"378.72\" CatImpuesto=\"IVA\"/>\n" +
            "    </cfdi:Traslados>\n" +
            "  </cfdi:Impuestos>\n" +
            "  <cfdi:AdditionalData plantilla=\"CFDIV33\">\n" +
            "    <cfdi:Entrega/>\n" +
            "    <cfdi:Params>\n" +
            "      <cfdi:Param0/>\n" +
            "    </cfdi:Params>\n" +
            "    <cfdi:Personals/>\n" +
            "    <cfdi:DirFiscal calle=\"\" noExterior=\"\" noInterior=\"\" colonia=\"\" localidad=\"\" municipio=\"\" estado=\"\" pais=\"\" cp=\"\"/>\n" +
            "  </cfdi:AdditionalData>\n" +
            "  <cfdi:DatosCompra/>\n" +
            "</cfdi:Comprobante>\n" +
            "\n" +
            "\n";

}
