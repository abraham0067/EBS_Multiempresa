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
import mx.com.ebs.emision.factura.utilierias.PintarLog;
import org.jdom.Document;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManagedBeanComplementoPagoArchivo implements Serializable {

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

    private String nombreArchivo;
    private int idEmpresa;//ID de la empresa emisora

    private boolean deshabilitaBotonGeneraFActura;
    private ArrayList<String> respuestas;
    private MEmpresa empresaEmisora;
    /**
     * Creates a new instance of ManagedBeanPlantillas
     */
    public ManagedBeanComplementoPagoArchivo() {
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

                MEmpresaMTimbre m = daoEmpTimp.ObtenerClaveWSEmpresaTimbre(idEmpresa);


                String respuestaServicio = null;
                LeerDatosExcel genComprobanteData = new LeerDatosExcel(uploadedFile.getInputstream());
                ComprobanteData comprobanteData = genComprobanteData.getComprobanteData();
                // comprobanteData = agregarDatosComprobante(comprobanteData);
                System.out.println("emisor: " + comprobanteData.getEmisor().getRfc());
                if(genComprobanteData.getRFC().equalsIgnoreCase(empresaEmisora.getRfcOrigen())){
                    respuestaServicio = new ClienteFacturaManual().exeGenFactura(comprobanteData, m.getClaveWS(), ambiente, DEBUG);
                }else{
                    respuestaServicio = null;
                    FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "El RFC del emisor no es un RFC valido.", ""));
                }


                if (respuestaServicio != null) {
                    if (checkRespuestaServicio(respuestaServicio)) {
                        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "La factura se genero correctamente.", ""));
                    } else {
                        //Show all to user
                        for (String mssg : respuestas)
                            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, mssg, ""));
                    }
                }
            } else {
                FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Se debe agregar un archivo antes de generar la factura"));
            }
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

    public String getNombreArchivo() {
        return this.nombreArchivo;
    }

    public int getIdEmpresa() {
        return this.idEmpresa;
    }

    public boolean isDeshabilitaBotonGeneraFActura() {
        return this.deshabilitaBotonGeneraFActura;
    }

    public MEmpresa getEmpresaEmisora() {
        return this.empresaEmisora;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public void setDeshabilitaBotonGeneraFActura(boolean deshabilitaBotonGeneraFActura) {
        this.deshabilitaBotonGeneraFActura = deshabilitaBotonGeneraFActura;
    }

    public void setEmpresaEmisora(MEmpresa empresaEmisora) {
        this.empresaEmisora = empresaEmisora;
    }


}
