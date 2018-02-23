/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.mbeans;

import com.ebs.CancelacionRetencion.CancelaRetencion;
import com.ebs.CancelacionRetencion.CancelaRetencionTest;
import com.ebs.fe.wsgi.util.Zipper;
import com.opensymphony.xwork2.ActionSupport;
import com.ebs.model.LazyMapearRetencionArchiDataModel;
import fe.db.MAcceso;
import fe.db.MArchivosCfd;
import fe.db.MConfig;
import fe.db.MEmpresa;
import fe.db.MapearRetencionArchi;
import fe.db.McfdRetencion;
import fe.model.dao.ArchivosCfdDAO;
import fe.model.dao.ConfigDAO;
import fe.model.dao.EmpresaDAO;
import fe.model.dao.MaterialDAO;
import fe.model.dao.RetencionDAO;
import fe.model.util.CrearZIPFacturas;
import fe.model.util.Material;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import fe.pki.PKI;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Eduardo C. Flores Ambrosio <EB&S>
 */
public class ManagedBeanRetenciones implements Serializable {

    private MAcceso usuario;
    private FacesContext context;
    private HttpServletRequest httpRequest;
    private String appContext;


    private List<McfdRetencion> ListCFds;
    private LazyDataModel<MapearRetencionArchi> listMapRetenciones;
    private List<MapearRetencionArchi> selectedRetenciones;
    private List<MEmpresa> ListEmpresa;
    private List<MArchivosCfd> ListArchivos;
    private MArchivosCfd archCfd;
    private StreamedContent scFile;
    private McfdRetencion cfd;
    private String tipoBus;
    private String parametroBus;
    private String idSeleccionado;
    private Date fechaDesde;
    private Date fechaHasta;
    private int idEmpresa;
    private int idcfd;
    private int idarchid;
    private int asig;

    private EmpresaDAO daoEmp;
    private RetencionDAO daoReten;
    private ArchivosCfdDAO daoArchi;
    private ConfigDAO daoConfig;
    private MaterialDAO matDao;

    private boolean cliente;
    private InputStream zip;
    private long contentLength;
    private String contentName;
    private String archivoSubFileName;
    private String archivoSubContentType;
    private SimpleDateFormat sf;
    private int busestatus;
    private Material materialData;
    private String NumCajas;
    private String Materialtxt;
    private String Recibotxt;
    private String ReciboFechatxt;
    private int idAux;

    @Getter @Setter
    private UploadedFile ufRetencionesFile;

    private String claveWebService = "samaujYHh3voRwBSM5ajw29Wd6Y=";

    /**
     * Creates a new instance of ManagedBeanRetenciones
     */
    public ManagedBeanRetenciones() {

    }

    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance();
        httpRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession s = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        usuario = (MAcceso) s.getAttribute("macceso");
        appContext = httpRequest.getContextPath();
        daoEmp = new EmpresaDAO();
        daoArchi = new ArchivosCfdDAO();
        daoConfig = new ConfigDAO();
        daoReten = new RetencionDAO();
        matDao = new MaterialDAO();
        if (this.usuario.getPerfil().getTipoUser().toUpperCase().equals("CLIENTE")) {
            this.cliente = true;
        }
        idEmpresa = usuario.getEmpresa().getId();
        busestatus = -1;
        sf = new SimpleDateFormat("yyyy-MM-dd HHmmss");
    }

    /**
     * Busqueda de retenciones
     */
    public void buscar() {
        if (cliente) {
            //Busqueda cliente
            listMapRetenciones = new LazyMapearRetencionArchiDataModel(true, usuario.getId(), idEmpresa, tipoBus, parametroBus, fechaDesde, fechaHasta, usuario.getCliente());
        } else {
            //Busqueda usuarios internos
            listMapRetenciones = new LazyMapearRetencionArchiDataModel(usuario.getId(), idEmpresa, tipoBus, parametroBus, fechaDesde, fechaHasta, busestatus);
        }
    }

    public void extraerFacturas() {
        scFile = null;
        if (selectedRetenciones != null && selectedRetenciones.size() > 0) {
            Integer[] lista = new Integer[selectedRetenciones.size()];
            int i = 0;
            for (MapearRetencionArchi tmp : selectedRetenciones) {
                lista[i] = tmp.getRetencion().getId();
                i++;
            }
            ListCFds = daoReten.ListaSelec(lista);
            if (ListCFds != null && !ListCFds.isEmpty()) {
                CrearZIPFacturas zipper = new CrearZIPFacturas();
                byte[] res;
                try {
                    res = zipper.ZipRetenciones((List) ListCFds);
                    String name = "CFDIS_" + this.sf.format(new Date()) + ".zip";
                    if (res != null) {
                        scFile = new DefaultStreamedContent(new ByteArrayInputStream(res), "application/zip", name);
                        selectedRetenciones.clear();
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo descargar el archivo.", "--"));
                    }
                } catch (Exception ex) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo descargar el archivo.", "--"));
                    Logger.getLogger(ManagedBeanRetenciones.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No existen facturas con los parametros solicitados.", "--"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Por favor seleccione la(s) factura(s) que desea descargar.", "--"));
        }
    }

    public void cargaRetenciones(){
/*
            if (ufRetencionesFile != null) {
                HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                String localPort = String.valueOf(request.getLocalPort());
                String prefix = FilenameUtils.getBaseName(ufRetencionesFile.getFileName());
                String fileTypeInput = FilenameUtils.getExtension(ufRetencionesFile.getFileName());
                byte[] bytesFile =  ufRetencionesFile.getContents();
                try {
                    PKI pki = new PKI();
                    // TODO: 15/01/2018 BUSCAR SEGURIDADUTIL
                    byte [] cypredBytes  = pki.cipher_bytes(Zipper.compress("cfdi", bytesFile),,PKI.AES_128,PKI.ENCRYPT_MODE)
                    byte[] y = null;
                    byte[] response = null;

                    if (fileTypeInput.toUpperCase().endsWith(".TXT")){
                        response =  ClienteInvoiceRetencion.generate(cypredBytes, ModoRetencion.TXT, claveWebService);
                    } else{
                        response =  ClienteInvoiceRetencion.generate(cypredBytes, ModoRetencion.XML, claveWebService);
                    }


                    pki.decrypt_bytes(Zipper.compress("cfdi", bytesFile),,PKI.DECRYPT_MODE)
                    y = su.descifraZip(response);

                } catch (IOException e) {
                    e.printStackTrace(System.out);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Ha ocurrido un error al intentar subir el archivo para la cancelacion.", "Detail"));
                } catch (IllegalStateException e) {
                    e.printStackTrace(System.out);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Ha ocurrido un error al intentar subir el archivo de texto para la cancelacion.", "Detail"));
                } finally {
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Debe seleccionar un archivo txt con los contratos para su cancelacion.", "Detail"));
            }
            */
    }


    /**
     * Descarga el reporte en formato xls
     *
     * @return
     */
    public void extraerReporte() {
        scFile = null;
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        String contentType = "application/vnd.sealed-xls";
        if (validar()) {
            ListCFds = daoReten.ListaParametrosExportR(usuario.getId(), idEmpresa, tipoBus, parametroBus, fechaDesde, fechaHasta, busestatus);
            if (ListCFds != null && !ListCFds.isEmpty()) {
                try {
                    ec.responseReset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
                    ec.setResponseContentType(contentType); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ExternalContext#getMimeType() for auto-detection based on filename.
                    ec.setResponseHeader("Content-Disposition", "attachment; filename=\"CFDIS_" + sf.format(new Date()) + ".xls\"");
                    OutputStream output = ec.getResponseOutputStream();
                    CrearZIPFacturas crear = new CrearZIPFacturas();
                    crear.ReporteCVSRetenciones(ListCFds, output);
                    // Now you can write the InputStream of the file to the above OutputStream the usual way.
                    // ...
                    fc.responseComplete(); // Important! Otherwise JSF will attempt to render the response which obviously will fail since it's already written with a file and closed.
                } catch (Exception e) {
                            e.printStackTrace(System.out);
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No existen facturas con los parametros seleccionados.", "--"));
            }
        }
    }

    /**
     * Valida la entrada de parametros
     *
     * @return
     */
    private boolean validar() {
        boolean res = true;
        if (this.fechaDesde != null && this.fechaHasta != null && !this.fechaDesde.equals("") && !this.fechaHasta.equals("")) {
            if (this.fechaHasta.before(this.fechaDesde)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "La fecha Hasta debe ser mayor que la fecha Desde", "--"));
                res = false;
            }
            if (this.tipoBus.trim().equals("-1") && this.fechaHasta == null && this.fechaDesde == null && this.idEmpresa <= 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Es necesario insertar algun tipo de busqueda", "--"));
                res = false;
            }
            if (!this.tipoBus.trim().equals("-1") && (this.parametroBus == null || "".equals(this.parametroBus.trim()))) {
                res = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Es necesario insertar el valor de busqueda", "--"));
            }
        }

        return res;
    }

    private boolean validaCliente() {
        boolean res = true;
        if (this.fechaDesde != null && this.fechaHasta != null && this.fechaHasta.before(this.fechaDesde)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "La fecha Hasta debe ser mayor que la fecha Desde", "--"));
            res = false;
        }
        return res;
    }

    /**
     * Redirige a la pagina de asignacion de archivos
     */
    public void redirectAsignarArchivo() {
        obtenerMcfdMap();
        if (selectedRetenciones.size() == 1) {
            cfd = selectedRetenciones.get(0).getRetencion();
            ListArchivos = (List<MArchivosCfd>) daoArchi.BuscarArchivoCfd_cfdId(selectedRetenciones.get(0).getRetencion().getId());
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/facturas/facturasAdjuntarArchivos.xhtml");

            } catch (IOException e) {
                e.printStackTrace(System.out);
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Solo debe seleccionar un registro", "--"));
        }

    }

    public void redirectMaterial() {
        obtenerMcfdMap();
        if (selectedRetenciones != null && !selectedRetenciones.isEmpty()) {
            if (selectedRetenciones.size() == 1) {
                cfd = selectedRetenciones.get(0).getRetencion();
                materialData = new Material();
                materialData.setNumFactura(busestatus);
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/facturas/facturasMateriales.xhtml");
                } catch (IOException e) {
                    e.printStackTrace(System.out);
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Seleccione solo un registro.", "--"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Por favor seleccione un registro.", "--"));
        }

    }

    public void cargarArchivos(FileUploadEvent event) {
//        System.out.println("Cargando archivos");
        //UploadedFile file = event.getFile();
        if (validarArch()) {
            MConfig pathArchivos = daoConfig.BuscarConfigDatoClasificacion("ARCHIVOS", "DIVERSOS_ARCHIVOS");
            if (pathArchivos != null && pathArchivos.getRuta() != null) {
                String pathgen = pathArchivos.getRuta();
                pathgen = pathgen + this.cfd.getUuid();
                File directorio = new File(pathgen);
                if (directorio.mkdirs()) {
                }
                this.archivoSubFileName = event.getFile().getFileName();
                this.archivoSubContentType = event.getFile().getContentType();
                MArchivosCfd mArchivo = new MArchivosCfd();
                //mArchivo.setCfd(this.cfd);
                mArchivo.setRuta(pathgen + "/" + this.archivoSubFileName);
                mArchivo.setNombre(this.archivoSubFileName);
                mArchivo.setUsuario(usuario.getUsuario());
                if (daoArchi.GuardarActualizaArchivoCfd(mArchivo)) {
                    try {
                        OutputStream out2 = new FileOutputStream(
                                pathgen + "/" + archivoSubFileName);
                        out2.write(event.getFile().getContents());
                        out2.close();
                        //Update the content
                        FacesContext.getCurrentInstance().addMessage("formUpload", new FacesMessage(FacesMessage.SEVERITY_INFO, "El archivo ha sido asignado.", "Info"));
                        this.archCfd = this.daoArchi.BuscarArchivoCfdId(this.cfd.getId());

                    } catch (Exception ex) {
                        FacesContext.getCurrentInstance().addMessage("formUpload", new FacesMessage(FacesMessage.SEVERITY_ERROR, "El archivo no se pudo guardar.", "Info"));
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage("formUpload", new FacesMessage(FacesMessage.SEVERITY_ERROR, "El archivo no se pudo guardar.", "Info"));
                }
            }
        }
    }

    public boolean validarArch() {
        boolean res = true;
        if (cfd == null) {
            res = false;
        }
        return res;
    }

    /**
     * Guarda el registro de un material
     */
    public void guardarMaterial() {
        String mss = matDao.insertDetalleAdenda(materialData);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, mss, "--"));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        redirectConsulta();
    }

    private void redirectConsulta() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/facturas/retenciones.xhtml");
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }

    private void obtenerMcfdMap() {
        ListCFds = new ArrayList<McfdRetencion>();
        if (selectedRetenciones != null && !selectedRetenciones.isEmpty()) {
            for (MapearRetencionArchi tmp : selectedRetenciones) {
                ListCFds.add(tmp.getRetencion());

            }
        }
    }

    /**
     * Cancelacion de una retencion
     */
    public void cancelarRetencion() {
        if (selectedRetenciones != null && !selectedRetenciones.isEmpty()) {
            for (MapearRetencionArchi tmp : selectedRetenciones) {
                if (tmp != null) {
                    try {
                        MConfig config = this.daoConfig.BuscarConfigDatoClasificacion("AMBIENTE", "SERVIDOR");
                        byte[] acuse = null;
                        if (config != null && config.getDato() != null) {
                            if (config.getValor() != null && config.getValor().trim().equalsIgnoreCase("Desarrollo")) {
                                //Los datos se actualizan en la base de datos de desarrollo(106)
                                new CancelaRetencionTest();
                                acuse = CancelaRetencionTest.cancelaRetencionTest(tmp.getRetencion().getMEmpresa().getRfcOrigen(), "", tmp.getRetencion().getFolioErp(), "DqOKzq1gFHIPNtYY0Z1Vr79uIyA=");

                            } else {
                                //Datos actualizados en la base de datos de produccion (107)
                                new CancelaRetencion();
                                acuse = CancelaRetencion.cancelaUuidRetencion(tmp.getRetencion().getMEmpresa().getRfcOrigen(), tmp.getRetencion().getUuid(), "DqOKzq1gFHIPNtYY0Z1Vr79uIyA=");
                            }
                        } else {
                            //Los datos se actualizan en la base de datos de desarrollo(106)
                            new CancelaRetencion();
                            acuse = CancelaRetencion.cancelaUuidRetencion(tmp.getRetencion().getMEmpresa().getRfcOrigen(), tmp.getRetencion().getUuid(), "DqOKzq1gFHIPNtYY0Z1Vr79uIyA=");
                        }
                        
                        if (new String(acuse).trim().length() < 70) {
                            acuse = null;
                        }
                        if (acuse != null && acuse.length > 100) {
                            String Statusacuse = new String(acuse);
                            int inicio = Statusacuse.indexOf("<EstatusUUID>");
                            if (inicio > 0) {
                                inicio += 13;
                                final int fin = Statusacuse.indexOf("</EstatusUUID>");
                                if (fin > 0) {
                                    final String EstatusUUID = Statusacuse.substring(inicio, fin);
                                    if (EstatusUUID != null && !EstatusUUID.trim().equals("")) {
                                        String mensaje = this.getMessageByCode(EstatusUUID);
                                        if (mensaje.contains("La factura se cancelo exitosamente")) {
                                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "La retencion ha sido cancelada.", "--"));
                                            buscar();
                                        }
                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, mensaje, "--"));
                                    } else {
                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "No fue posible recuperar el mensaje de respuesta.", "--"));
                                    }
                                }
                            } else {
                                inicio = Statusacuse.indexOf("CodEstatus=");
                                if (inicio > 0) {
                                    inicio += 11;
                                    int fin = inicio + 5;
                                    String EstatusUUID = Statusacuse.substring(inicio, fin);
                                    if (EstatusUUID != null && !EstatusUUID.trim().equals("")) {
                                        String mensaje = this.getMessageByCode(EstatusUUID);
                                        if (mensaje.contains("La factura se cancelo exitosamente")) {
                                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "La retencion ha sido cancelada.", "--"));
                                            buscar();
                                        }
                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, mensaje, "--"));
                                    } else {
                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "No fue posible recuperar el mensaje de respuesta.", "--"));
                                    }
                                }
                            }
                            //this.setListEmpresa((List<MEmpresa>)this.DAOEmp.ListaEmpresasPadres(this.UsuarioOpera.getId()));
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "La factura no se pudo cancelar.", "--"));
                            if (this.cliente) {
                                if (this.validaCliente()) {
                                    buscar();
                                }
                            } else if (this.validar()) {
                                buscar();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace(System.out);
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No se encontro la factura.", "--"));
                }
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Por favor seleccione al menos una factura.", "--"));
        }

    }

    public String getMessageByCode(final String EstatusUUID) {
        String resp = null;
        if (EstatusUUID.contains("1201") || EstatusUUID.contains("1202")) {
            resp = "La factura se cancelo exitosamente";
        } else if (EstatusUUID.contains("1203")) {
            resp = "UUID No encontrado o no corresponde en el emisor";
        } else if (EstatusUUID.contains("1205")) {
            resp = "UUID No existe";
        } else if (EstatusUUID.contains("1300")) {
            resp = "Autenticación no valida";
        } else if (EstatusUUID.contains("1301")) {
            resp = "XML mal formado";
        } else if (EstatusUUID.contains("1302")) {
            resp = "Estructura de folio invalida";
        } else if (EstatusUUID.contains("1303")) {
            resp = "Estructura de RFC no valida";
        } else if (EstatusUUID.contains("1304")) {
            resp = "Estructura de fecha no valida";
        } else if (EstatusUUID.contains("1305")) {
            resp = "Certificado no corresponde al emisor";
        } else if (EstatusUUID.contains("1306")) {
            resp = "Certificado no vigente";
        } else if (EstatusUUID.contains("1307")) {
            resp = "Uso de FIEL no permitido";
        } else if (EstatusUUID.contains("1308")) {
            resp = "Certificado revocado o caduco";
        } else if (EstatusUUID.contains("1309")) {
            resp = "Firma mal formada o invalida";
        } else if (EstatusUUID.contains("201") || EstatusUUID.contains("202")) {
            resp = "La factura se cancelo exitosamente";
        } else if (EstatusUUID.contains("203")) {
            resp = "UUID No encontrado o no corresponde en el emisor";
        } else if (EstatusUUID.contains("204")) {
            resp = "UUID No aplicable para cancelaci\u00f3n";
        } else if (EstatusUUID.contains("205")) {
            resp = "UUID No existe";
        } else if (EstatusUUID.contains("301")) {
            resp = "XML mal formado";
        } else if (EstatusUUID.contains("302")) {
            resp = "Sello mal formado o inv\u00e1lido";
        } else if (EstatusUUID.contains("303")) {
            resp = "Sello no corresponde al emisor";
        } else if (EstatusUUID.contains("304")) {
            resp = "Certificado revocado o caduco";
        } else {
            resp = "Código no encontrado.";
        }
        return resp;
    }

    public boolean isCliente() {
        return cliente;
    }

    public void setCliente(boolean cliente) {
        this.cliente = cliente;
    }

    public LazyDataModel<MapearRetencionArchi> getListMapRetenciones() {
        return listMapRetenciones;
    }

    public void setListMapRetenciones(LazyDataModel<MapearRetencionArchi> listMapRetenciones) {
        this.listMapRetenciones = listMapRetenciones;
    }

    public List<MapearRetencionArchi> getSelectedRetenciones() {
        return selectedRetenciones;
    }

    public void setSelectedRetenciones(List<MapearRetencionArchi> selectedRetenciones) {
        this.selectedRetenciones = selectedRetenciones;
    }

    public StreamedContent getScFile() {
        return scFile;
    }

    public void setScFile(StreamedContent scFile) {
        this.scFile = scFile;
    }

    public String getTipoBus() {
        return tipoBus;
    }

    public void setTipoBus(String tipoBus) {
        this.tipoBus = tipoBus;
    }

    public String getParametroBus() {
        return parametroBus;
    }

    public void setParametroBus(String parametroBus) {
        this.parametroBus = parametroBus;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public int getBusestatus() {
        return busestatus;
    }

    public void setBusestatus(int busestatus) {
        this.busestatus = busestatus;
    }

}
