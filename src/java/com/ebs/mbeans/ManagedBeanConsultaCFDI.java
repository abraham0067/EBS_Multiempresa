/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.mbeans;

import com.ebs.CancelacionCFDI.CancelaCFDITest;
import com.ebs.CancelacionCFDI.CancelaCFDI;
import com.ebs.catalogos.TiposComprobante;
import com.ebs.catalogos.TiposDocumento;
import com.ebs.model.LazyCfdiDataModel;
import com.ebs.vistas.VistaCfdiOtro;
import fe.db.MAcceso;
import fe.db.MArchivosCfd;
import fe.db.MCfd;
import fe.db.MConfig;
import fe.db.MEmpresa;
import fe.db.MOtro;
import fe.db.MapearCfdArchi;
import fe.model.dao.*;
import fe.model.util.CrearZIPFacturas;
import fe.model.util.Material;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;

import lombok.Getter;
import lombok.Setter;
import org.joda.time.Days;
import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import org.primefaces.component.fileupload.FileUpload;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.LazyDataModel;
import sun.util.resources.cldr.mg.LocaleNames_mg;

public class ManagedBeanConsultaCFDI implements Serializable {

    // TODO: 31/10/2017 cambiar por un parametro de configuracion
    private int DATES_LIMITS_CANCEL = 30;

    private Integer[] idsEmpresasAsignadas;///Separados por comas
    //Parametros
    private String strEstatus;


    @Getter
    @Setter
    private int empresaIdFiltro = -1;
    /*
            opciones.add(new SelectItem("numeroFactura", "Numero Factura"));
            opciones.add(new SelectItem("folioErp", "Folio ERP"));
            //Si no es un cliente
            if (!cuentaUsuario.getPerfil().getTipoUser().toUpperCase().equals("CLIENTE")) {
                ///(ATRIBUTOCLASE|VALOR EN LA VISTA)
                opciones.add(new SelectItem("rfc", "RFC"));
                opciones.add(new SelectItem("serie", "Serie"));
                opciones.add(new SelectItem("noCliente", "No. Cliente"));
                opciones.add(new SelectItem("razonSocial", "Razón Social"));
     */
    @Getter
    @Setter
    private String numeroFactura;
    @Getter
    @Setter
    private String folioErp;
    @Getter
    @Setter
    private String rfc;
    @Getter
    @Setter
    private String serie;
    @Getter
    @Setter
    private String noCliente;
    @Getter
    @Setter
    private boolean esClienteEmpresa;

    @Getter
    @Setter
    private String razonSocial;

    @Getter
    @Setter
    private Date datDesde;//Fecha Inicial
    @Getter
    @Setter
    private Date datHasta;//Fecha Final


    @Getter
    @Setter
    private String   numPolizaSeguro;//Parametro de busqueda de bupa mexico


    private String contentName;
    private String archivoSubFileName;
    private String archivoSubContentType;
    private List<MArchivosCfd> archivosCFDI;
    private List<MapearCfdArchi> listMap;//USADO EN LA CANCELACION
    private List<MEmpresa> listEmpresas;
    private EmpresaDAO daoEmp;
    private ArchivosCfdDAO daoArch;
    private ConfigDAO DAOCong;
    private String ids;
    private int asig = 0;
    private CfdiDAO daoCFDI;
    private MaterialDAO matDao;
    private MCfd cfd;
    @Getter
    @Setter
    private List<Integer> listCFDS;
    @Getter
    @Setter
    private List<Integer> listCFDSAux;
    //private List<MCfd> selectedCFDS;//Donde se guardaran los cfdi extraidos del listmap
    @Getter
    @Setter
    private List<Integer> selectedCFDSIds;//Donde se guardaran los cfdi extraidos del listmap
    private MCfd selectedMCFD;//FACTURA axiliar

    @Getter
    @Setter
    private LazyDataModel<VistaCfdiOtro> listMapMCA;
    @Getter
    @Setter
    private List<VistaCfdiOtro> listMapMCASelecteds;

    private SimpleDateFormat sdfDateFormatter;
    private FileUpload archivo;//Cambiar a multiples archivos
    //Materiales en un FACTURA
    private Material materialData;
    private String NumCajas;
    private String Materialtxt;
    private String Recibotxt;
    private String ReciboFechatxt;

    //Datos de sesion
    private MAcceso activeUser;
    private HttpServletRequest httpServletRequest;
    private FacesContext faceContext;
    private String appContext;

    //VARIABLE TO DEBUG
    private boolean debug = true;

    private final String pswCancelacion = "DqOKzq1gFHIPNtYY0Z1Vr79uIyA=";
    //SUMA HASTA CANCELACION
    private final String numPerfilCancelacion = "134217728";
    private boolean cancelar = false;

    private LogAccesoDAO daoLog;

    /**
     * Creates a new instance of ManagedBeanConsultaCFDI
     */
    public ManagedBeanConsultaCFDI() {

        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        activeUser = (MAcceso) httpSession.getAttribute("macceso");
        ///empresa = activeUser.getEmpresa();
        appContext = httpServletRequest.getContextPath();
        numeroFactura = "";
        folioErp = "";
        rfc = "";
        serie = "";
        razonSocial = "";
        numPolizaSeguro = "";
        esClienteEmpresa = false;
    }

    @PostConstruct
    public void init() {
        selectedCFDSIds = new ArrayList();
        listMapMCASelecteds = new ArrayList();
        DAOCong = new ConfigDAO();
        daoArch = new ArchivosCfdDAO();
        daoEmp = new EmpresaDAO();
        daoCFDI = new CfdiDAO();
        matDao = new MaterialDAO();
        sdfDateFormatter = new SimpleDateFormat("yyyy-MM-dd HHmmss");
        strEstatus = "-1";
        numeroFactura = "";
        folioErp = "";
        rfc = "";
        serie = "";
        razonSocial = "";
        numPolizaSeguro = "";

        daoLog = new LogAccesoDAO();
        //cargamos los cfdi por default
        cargarEmpresasUsuario();
        cargarCFDI();
    }


    public void cargarEmpresasUsuario() {
        ///Cargamos ids empresas asociadas al usuario
        List<Integer> lista = new ArrayList<>();

        noCliente = activeUser.getCliente();
        String tipoUser = activeUser.getPerfil().getTipoUser();
        if(tipoUser.equalsIgnoreCase("CLIENTE")  &&   (noCliente != null && !noCliente.isEmpty())){
            esClienteEmpresa = true;
        } else {
            esClienteEmpresa = false;
            noCliente = "";
        }

        activeUser.getEmpresas().forEach(emp -> {
            lista.add(emp.getId());
        });
        idsEmpresasAsignadas = lista.toArray(new Integer[lista.size()]);

        //BUSCA EN LOS PERFILES SI EL USUARIO PUEDE CANCELAR ARCHIVOS
        this.cancelar = ((activeUser.getPerfil().getPerfil() & Long.parseLong(numPerfilCancelacion) ) == Long.parseLong(numPerfilCancelacion) ) ? true : false;

    }

    /**
     * Se encarga de buscar los registros usando los parametros solicitados.
     * Responde al boton buscar de la pagina de facturas FACTURA.
     */
    public void cargarCFDI() {
        Integer[] idsBusqueda = {-1};
        if (empresaIdFiltro > 0) {
            idsBusqueda = new Integer[1];
            idsBusqueda[0] = empresaIdFiltro;
        } else {
            idsBusqueda = idsEmpresasAsignadas;
        }

        if (esClienteEmpresa) {//Si el Usuario es un cliente
            System.out.println("Buscando cliente cliente");
            if (validacionCliente()) {
                listMapMCA = new LazyCfdiDataModel(
                        esClienteEmpresa,
                        activeUser.getId(),
                        idsBusqueda,
                        numeroFactura,
                        folioErp,
                        rfc,
                        serie,
                        noCliente,
                        razonSocial,
                        datDesde,
                        datHasta,
                        activeUser.getCliente(),
                        numPolizaSeguro);

            }
        } else//otro usuario
            if (validacionOtro()) {
                System.out.println("Buscando cliente EBS");
                listMapMCA = new LazyCfdiDataModel(
                        esClienteEmpresa,
                        activeUser.getId(),
                        idsBusqueda,
                        numeroFactura,
                        folioErp,
                        rfc,
                        serie,
                        noCliente,
                        razonSocial,
                        datDesde,
                        datHasta,
                        activeUser.getCliente(),
                        strEstatus,
                        numPolizaSeguro);
            }
    }

    /**
     * Extrae unicamente los MCFD del listmap de archivos con cada MCFD
     */
    private void obtenerCFDIdeListMapSelecteds() {
        if (selectedCFDSIds != null) {
            selectedCFDSIds.clear();
        } else {
            selectedCFDSIds = new ArrayList();
        }
        for (VistaCfdiOtro tmp : listMapMCASelecteds) {
            selectedCFDSIds.add(tmp.getId());
        }
    }

    public void extraerFacturas() {
        obtenerCFDIdeListMapSelecteds();//Extraemos los FACTURA de la lista mapeada
        int i = 0;
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        String contentType = "application/zip";
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HHmmss");
        if (selectedCFDSIds.size() > 0) {

            this.listCFDSAux = selectedCFDSIds;
            if (listCFDSAux != null && !listCFDSAux.isEmpty()) {
                try {
                    ec.responseReset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
                    ec.setResponseContentType(contentType); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ExternalContext#getMimeType() for auto-detection based on filename.
                    ec.setResponseHeader("Content-Disposition", "attachment; filename=\"CFDIS_" + sf.format(new Date()) + ".zip\"");
                    OutputStream output = ec.getResponseOutputStream();
                    //Now you can write the InputStream of the file to the above OutputStream the usual way.
                    //...
                    CrearZIPFacturas crear = new CrearZIPFacturas();
                    crear.ZipCfdis(listCFDSAux, output);
                    fc.responseComplete(); // Important! Otherwise JSF will attempt to render the response which obviously will fail since it's already written with a file and closed.
                    //FacesContext.getCurrentInstance().responseComplete(); //Equal
                } catch (Exception e) {
                    e.printStackTrace(System.out);
                }
            } else {
                FacesContext.getCurrentInstance().addMessage("formContent", new FacesMessage(FacesMessage.SEVERITY_INFO, "No existe facturas con los parametros solicitados, favor de rectificarlos.", "Info"));
            }
        } else {
            fc.addMessage("formConsultasCFD", new FacesMessage(FacesMessage.SEVERITY_INFO, "Seleccione al menos una factura para descargar.", "Info"));
        }
    }

    /**
     * Descarga un reporte en formato zip. Responde al boton de extraer reportes
     * en facturas FACTURA.
     */
    public void extraerReporte() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HHmmss");
        List<MCfd> tmp;
        String contentType = "application/vnd.sealed-xls";
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        int totalRow = 0;
        int currentRow = 0;
        int pageSize = 100;//Rows per request

        Integer[] idsBusqueda;
        if (empresaIdFiltro > 0) {
            idsBusqueda = new Integer[1];
            idsBusqueda[0] = empresaIdFiltro;
        } else {
            idsBusqueda = idsEmpresasAsignadas;///Todas las empresas asignadas
        }

        if (validacionOtro()) {
            //First rows test
            tmp = daoCFDI.ListaParametrosExportLazy(
                    activeUser.getId(),
                    idsBusqueda,
                    numeroFactura,
                    folioErp,
                    rfc,
                    serie,
                    noCliente,
                    razonSocial,
                    datDesde,
                    datHasta,
                    Integer.parseInt(strEstatus),
                    numPolizaSeguro,
                    currentRow,
                    pageSize);
            totalRow = daoCFDI.rowCount;
            // TODO: 28/08/2017 PONER UN LIMITE MAXIMO PARA EL NUMERO DE REGISTROS DEL REPORTE
            if (tmp != null && !tmp.isEmpty()) {
                try {
                    try {
                        HSSFWorkbook libro = new HSSFWorkbook();
                        Sheet hoja = libro.createSheet("Facturas");
                        //COLUMNS NAME
                        Row cabecera = hoja.createRow(0);
                        cabecera.createCell(0).setCellValue("NUMERO FACTURA");
                        cabecera.createCell(1).setCellValue("FOLIO ERP");
                        cabecera.createCell(2).setCellValue("NO CLIENTE");
                        cabecera.createCell(3).setCellValue("RAZON SOCIAL");
                        cabecera.createCell(4).setCellValue("R.F.C.");
                        cabecera.createCell(5).setCellValue("FECHA");
                        cabecera.createCell(6).setCellValue("SUBTOTAL");
                        cabecera.createCell(7).setCellValue("IVA");
                        cabecera.createCell(8).setCellValue("TOTAL");
                        cabecera.createCell(9).setCellValue("MONEDA");
                        cabecera.createCell(10).setCellValue("TIPO CAMBIO");
                        cabecera.createCell(11).setCellValue("ESTATUS");
                        cabecera.createCell(12).setCellValue("UUID");
                        //END COLUMNS NAME

                        while (currentRow < Integer.MAX_VALUE && currentRow < totalRow && tmp != null) {
                            for (MCfd cfd : tmp) {
                                Row fila = hoja.createRow(currentRow + 1);
                                Cell celda = fila.createCell(0);
                                celda.setCellValue(cfd.getNumeroFactura());
                                Cell celda1 = fila.createCell(1);
                                celda1.setCellValue(cfd.getFolioErp());
                                Cell celda2 = fila.createCell(2);
                                MOtro otro = daoCFDI.Otro(cfd.getId());
                                if (otro != null) {
                                    celda2.setCellValue(otro.getParam5() != null ? otro.getParam5() : "");
                                } else {
                                    celda2.setCellValue("");
                                }
                                otro = null;
                                Cell celda3 = fila.createCell(3);
                                celda3.setCellValue(cfd.getRazonSocial());
                                Cell celda4 = fila.createCell(4);
                                celda4.setCellValue(cfd.getRfc());
                                Cell celda5 = fila.createCell(5);
                                celda5.setCellValue(cfd.FECHA());
                                Cell celda6 = fila.createCell(6);
                                celda6.setCellValue(cfd.getSubTotalML());
                                Cell celda7 = fila.createCell(7);
                                celda7.setCellValue(cfd.getIva());
                                Cell celda8 = fila.createCell(8);
                                celda8.setCellValue(cfd.getTotal());
                                Cell celda9 = fila.createCell(9);
                                celda9.setCellValue(cfd.getMoneda());
                                Cell celda10 = fila.createCell(10);
                                celda10.setCellValue(cfd.getTipoCambio());
                                Cell celda11 = fila.createCell(11);
                                celda11.setCellValue(cfd.ESTATUS());
                                Cell celda12 = fila.createCell(12);
                                celda12.setCellValue(cfd.getUuid());
                                currentRow++;
                            }
                            //GET NEXT ROWS 
                            tmp = daoCFDI.ListaParametrosExportLazy(
                                    activeUser.getId(),
                                    idsBusqueda,
                                    numeroFactura,
                                    folioErp,
                                    rfc,
                                    serie,
                                    noCliente,
                                    razonSocial,
                                    datDesde,
                                    datHasta,
                                    Integer.parseInt(strEstatus),
                                    numPolizaSeguro,
                                    currentRow,
                                    pageSize);
                        }
                        //TODO change download way to use primefaces component instead direct outputstream
                        ec.responseReset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
                        ec.setResponseContentType(contentType); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ExternalContext#getMimeType() for auto-detection based on filename.
                        ec.setResponseHeader("Content-Disposition", "attachment; filename=\"CFDIS_" + sf.format(new Date()) + ".xls\"");
                        OutputStream output = ec.getResponseOutputStream();
                        libro.write(output);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace(System.out);
                        fc.responseComplete();
                    }
                    // Now you can write the InputStream of the file to the above OutputStream the usual way.
                    // ...
                    fc.responseComplete(); // Important! Otherwise JSF will attempt to render the response which obviously will fail since it's already written with a file and closed.
                } catch (Exception e) {
                    e.printStackTrace(System.out);
                }
            } else {
                fc.addMessage("formContent", new FacesMessage(FacesMessage.SEVERITY_INFO, "No existen facturas con los parametros solicitados, favor de rectificarlos.", "Info"));
                //Show a message
            }

        } else {
            fc.addMessage("formContent", new FacesMessage(FacesMessage.SEVERITY_INFO, "Parametros especificados incorrectos.", "Info"));
        }
    }

    /**
     * Carga todos los archivos asociados a una factura para mostrarlos en una
     * nueva pagina.
     */
    public void extraerListaArchivos() {
        obtenerCFDIdeListMapSelecteds();
        try {
            if (selectedCFDSIds != null) {
                if (selectedCFDSIds.size() == 1) {
                    cfd = daoCFDI.BuscarId(selectedCFDSIds.get(0));
                    selectedMCFD = cfd;
                    if (cfd != null) {
                        //Carga la lista de archivos para una factura
                        this.archivosCFDI = this.daoArch.BuscarArchivoCfd_cfdId(this.selectedMCFD.getId());
                        FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/facturas/facturasAdjuntarArchivos.xhtml");
                    } else {
                        FacesContext.getCurrentInstance().addMessage("formOperations", new FacesMessage(FacesMessage.SEVERITY_INFO, "No se encontro la factura.", "Info"));
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage("formOperations", new FacesMessage(FacesMessage.SEVERITY_INFO, "Solo debe seleccionar un elemento.", "Info"));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage("formOperations", new FacesMessage(FacesMessage.SEVERITY_INFO, "Es necesario seleccionar una factura.", "Info"));
            }
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }

    /**
     * Asigna archivos a una factura
     *
     * @param event
     */
    public void uploadArchivos(FileUploadEvent event) {
        //UploadedFile file = event.getFile();
        if (validarArc()) {
            this.cfd = daoCFDI.BuscarId(this.selectedMCFD.getId());
            MConfig pathArchivos = DAOCong.BuscarConfigDatoClasificacion("ARCHIVOS", "DIVERSOS_ARCHIVOS");
            if (pathArchivos != null && pathArchivos.getRuta() != null) {
                String pathgen = pathArchivos.getRuta();
                pathgen = pathgen + this.selectedMCFD.getUuid();
                File directorio = new File(pathgen);
                directorio.mkdirs();
                this.archivoSubFileName = event.getFile().getFileName();
                this.archivoSubContentType = event.getFile().getContentType();
                MArchivosCfd mArchivo = new MArchivosCfd();
                mArchivo.setCfd(this.selectedMCFD);
                mArchivo.setRuta(pathgen + "/" + this.archivoSubFileName);
                mArchivo.setNombre(this.archivoSubFileName);
                mArchivo.setUsuario(activeUser.getUsuario());
                if (daoArch.GuardarActualizaArchivoCfd(mArchivo)) {
                    try {
                        OutputStream out2 = new FileOutputStream(
                                pathgen + "/" + getArchivoSubFileName());
                        out2.write(event.getFile().getContents());
                        out2.close();
                        //Update the content
                        FacesContext.getCurrentInstance().addMessage("formUpload", new FacesMessage(FacesMessage.SEVERITY_INFO, "El archivo ha sido asignado.", "Info"));
                        this.archivosCFDI = this.daoArch.BuscarArchivoCfd_cfdId(this.selectedMCFD.getId());
                    } catch (Exception ex) {
                        FacesContext.getCurrentInstance().addMessage("formUpload", new FacesMessage(FacesMessage.SEVERITY_ERROR, "El archivo no se pudo guardar.", "Info"));
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage("formUpload", new FacesMessage(FacesMessage.SEVERITY_ERROR, "El archivo no se pudo guardar.", "Info"));
                }
            }
        }
    }

    /**
     * Responde al boton cancelar en facturasCFDI.xhtml
     */
    public void cancelar() {
        obtenerCFDIdeListMapSelecteds();
        boolean canCancel;
        //CancelarCFDI_ServiceLocator serviceLocator;
        //CancelarCFDI_PortType port;
        MConfig config;
        MConfig diasConfig = DAOCong.BuscarConfigDatoClasificacion("DIAS_LIMITE", "CANCELACIONES");
        if (diasConfig != null) {
            if (!diasConfig.getValor().isEmpty()) {
                try {
                    DATES_LIMITS_CANCEL = Integer.parseInt(diasConfig.getValor());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        for (Integer tmpId: selectedCFDSIds) {
            MCfd tmp = daoCFDI.BuscarId(tmpId);
            canCancel = true;
            MOtro otroTemp = daoCFDI.Otro(tmp.getId());
            if (otroTemp != null) {
                if (otroTemp.getParam20() != null && otroTemp.getParam20().contains("3.3")) {
                    //do dates validation
                    Calendar actualTime = Calendar.getInstance();
                    Calendar invoiceTime = Calendar.getInstance();
                    invoiceTime.setTime(tmp.getFecha());
                    LocalDateTime actualLocalDate = LocalDateTime.fromCalendarFields(actualTime);
                    LocalDateTime invoiceLocalDate = LocalDateTime.fromCalendarFields(invoiceTime);
                    System.out.println("Actual time : {" + actualLocalDate);
                    System.out.println("Inovice time: {" + invoiceLocalDate);
                    int datesBetween = Days.daysBetween(invoiceLocalDate, actualLocalDate).getDays();
                    if (datesBetween >= DATES_LIMITS_CANCEL) {
                        canCancel = false;
                        FacesContext.getCurrentInstance().addMessage(null,
                                new FacesMessage(FacesMessage.SEVERITY_WARN,
                                        "No podemos cancelar ese documento ya que ya han pasado mas de " + DATES_LIMITS_CANCEL + " desde la fecha de timbrado.", "Error"));
                    }
                }
            }
            if (canCancel) {
                if (tmp.getId() > 0) {
//                cfd = getDaoCFDI().BuscarId(tmp.getId());
                    cfd = tmp;
                    if (cfd != null) {
                        try {
                            /*serviceLocator = new CancelarCFDI_ServiceLocator();
                            port = serviceLocator.getCancelarCFDIPort();*/
                            config = DAOCong.BuscarConfigDatoClasificacion("AMBIENTE", "SERVIDOR");
                            byte[] acuse = null;
                            String folio2 =cfd.getFolioErp2();
                            String rfcEmpresa = daoEmp.getRfcEmpresaById(cfd.getIdEmpresa());
                            System.out.println("-----CANCELACION DESDE EL PORTAL DETECTADA------");
                            System.out.println("rfcEmpresa: " + rfcEmpresa);
                            System.out.println("CANCELANDO: " + cfd.getUuid());

                            if (config != null && config.getDato() != null) {
                                if (config.getValor() != null && config.getValor().trim().equalsIgnoreCase("Desarrollo")) {
                                    System.out.print("Cancelando->Desarrollo");
                                    CancelaCFDITest cancela = new CancelaCFDITest();

                                    //CANCELA FACTURAS CON UUID´S ANTERIORES
                                    if(cfd.getUuid().startsWith("PRUEBA")){
                                        if(folio2 != null && !folio2.isEmpty())
                                            acuse = cancela.cancelaTestFolioErp2(rfcEmpresa, cfd.getSerieErp(), cfd.getFolioErp(), folio2, pswCancelacion);
                                        else
                                            acuse = cancela.cancelaTest(rfcEmpresa, cfd.getSerieErp(), cfd.getFolioErp(), pswCancelacion);
                                    }else
                                        acuse = cancela.cancelaTestUuid(rfcEmpresa, cfd.getUuid(), pswCancelacion );


                                } else {
                                    System.out.print("Cancelando->Produccion");
                                    acuse = new CancelaCFDI().cancelaUuid(rfcEmpresa, cfd.getUuid(), pswCancelacion);
                                }

                            } else{
                                System.out.print("Cancelando->Produccion");
                                acuse = new CancelaCFDI().cancelaUuid(rfcEmpresa, cfd.getUuid(), pswCancelacion);
                            }

                            /*
                            if (config != null && config.getDato() != null) {

                                if (config.getValor() != null && config.getValor().trim().equalsIgnoreCase("Desarrollo")) {
                                    System.out.print("Cancelando->Desarrollo");

                                    CancelaCFDITest cancela = new CancelaCFDITest();

                                    if(folio2 != null && !folio2.isEmpty())
                                        acuse = cancela.cancelaTestFolioErp2((daoEmp.getRfcEmpresaById(cfd.getIdEmpresa())), cfd.getSerieErp(), cfd.getFolioErp(), folio2, "DqOKzq1gFHIPNtYY0Z1Vr79uIyA=");
                                    else
                                        acuse = cancela.cancelaTest((daoEmp.getRfcEmpresaById(cfd.getIdEmpresa())), cfd.getSerieErp(), cfd.getFolioErp(), "DqOKzq1gFHIPNtYY0Z1Vr79uIyA=");

                                    //acuse = port.cancelaTest((daoEmp.getRfcEmpresaById(cfd.getIdEmpresa())),
                                     //       cfd.getSerieErp(), cfd.getFolioErp(), "DqOKzq1gFHIPNtYY0Z1Vr79uIyA=");



                                } else {
                                    System.out.print("Cancelando->Produccion");
                                    CancelaCFDI cancela = new CancelaCFDI();

                                    if(folio2 != null && !folio2.isEmpty())
                                        acuse = cancela.cancelaFolioErp2((daoEmp.getRfcEmpresaById(cfd.getIdEmpresa())), cfd.getSerieErp(), cfd.getFolioErp(), folio2, "DqOKzq1gFHIPNtYY0Z1Vr79uIyA=");
                                    else
                                        acuse = cancela.cancela((daoEmp.getRfcEmpresaById(cfd.getIdEmpresa())), cfd.getSerieErp(), cfd.getFolioErp(), "DqOKzq1gFHIPNtYY0Z1Vr79uIyA=");


                                    //acuse = port.cancelaUuid((daoEmp.getRfcEmpresaById(cfd.getIdEmpresa())),
                                      //      cfd.getUuid(), "DqOKzq1gFHIPNtYY0Z1Vr79uIyA=");
                                }

                            } else {
                                System.out.print("Cancelando->Produccion");
                                CancelaCFDI cancela = new CancelaCFDI();

                                if(folio2 != null && !folio2.isEmpty())
                                    acuse = cancela.cancelaFolioErp2((daoEmp.getRfcEmpresaById(cfd.getIdEmpresa())), cfd.getSerieErp(), cfd.getFolioErp(), folio2, "DqOKzq1gFHIPNtYY0Z1Vr79uIyA=");
                                else
                                    acuse = cancela.cancela((daoEmp.getRfcEmpresaById(cfd.getIdEmpresa())), cfd.getSerieErp(), cfd.getFolioErp(), "DqOKzq1gFHIPNtYY0Z1Vr79uIyA=");
                                //acuse = port.cancelaUuid((daoEmp.getRfcEmpresaById(cfd.getIdEmpresa())), cfd.getUuid(),
                                  //      "DqOKzq1gFHIPNtYY0Z1Vr79uIyA=");

                            }*/
                            String sAcuse = new String(acuse);
                            //System.out.println("sAcuse = " + sAcuse);
                            if (sAcuse.contains("<Error") || sAcuse.trim().length() <= 70) {
                                System.out.println("Factura no cancelada");

                                daoLog.guardaRegistro(activeUser,"El usuario '" + activeUser.getUsuario() +"' intento cancelar la factura con UUID: " + cfd.getUuid());

                                acuse = null;
                            }

                            if (acuse != null) {
                                String Statusacuse = new String(acuse);
                                this.cargarCFDI();
                                int inicio = Statusacuse.indexOf("<EstatusUUID>");
                                if (inicio > 0) {
                                    inicio = inicio + 13;
                                    int fin = Statusacuse.indexOf("</EstatusUUID>");
                                    if (fin > 0) {
                                        String EstatusUUID = Statusacuse.substring(inicio, fin);
                                        if (EstatusUUID != null && !EstatusUUID.trim().equals("")) {
                                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, metodoErroreCancel(EstatusUUID), "Error"));
                                        } else {
                                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "No fue posible recuperar el error.", "Error"));
                                        }

                                        if(EstatusUUID.equals("201") || EstatusUUID.equals("202"))
                                            daoLog.guardaRegistro(activeUser,"El usuario '" + activeUser.getUsuario() +"' cancelo la factura con UUID: " + cfd.getUuid());
                                    }
                                } else {
                                    inicio = Statusacuse.indexOf("CodEstatus=");
                                    if (inicio > 0) {
                                        inicio = inicio + 11;
                                        int fin = inicio + 5;
                                        String EstatusUUID = Statusacuse.substring(inicio, fin);
                                        if (EstatusUUID != null && !EstatusUUID.trim().equals("")) {
                                            FacesContext.getCurrentInstance().addMessage("formOperations", new FacesMessage(FacesMessage.SEVERITY_ERROR, metodoErroreCancel(EstatusUUID), "Error"));
                                        } else {
                                            FacesContext.getCurrentInstance().addMessage("formOperations", new FacesMessage(FacesMessage.SEVERITY_ERROR, "No fue posible recuperar el error.", "Error"));
                                        }
                                    }
                                }
                                this.setListEmpresas(daoEmp.ListaEmpresasPadres(activeUser.getId()));
                            } else {
                                FacesContext.getCurrentInstance().addMessage("formOperations", new FacesMessage(FacesMessage.SEVERITY_ERROR, "La factura no pudo ser cancelada.", "Error"));
                                FacesContext.getCurrentInstance().addMessage("formOperations", new FacesMessage(FacesMessage.SEVERITY_ERROR, sAcuse, "Error"));
                                cargarCFDI();
                            }
                        } catch (Exception e) {
                            e.printStackTrace(System.out);
                        }
                    } else {
                        FacesContext.getCurrentInstance().addMessage("formOperations", new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se encontro la factura, favor de verificarlo.", "Info"));
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage("formOperations", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Es necesario seleccionar una factura.", "Info"));
                }
            }
        }
    }

    /**
     * Limpia los filtros del formulario de busqueda
     */
    public void limpiarFiltros() {
        selectedCFDSIds = new ArrayList();
        listMapMCASelecteds = new ArrayList();
        datDesde = null;
        datHasta = null;
        strEstatus = "-1";
        numeroFactura = "";
        folioErp = "";
        rfc = "";
        serie = "";
        if(!esClienteEmpresa) {
            noCliente = "";
        }
        razonSocial = "";
        empresaIdFiltro = -1;
        numPolizaSeguro = "";
        //cargamos los cfdi por default
        cargarCFDI();
    }

    public boolean validacionCliente() {
//        System.out.println("VALIDACION CLIENTES");
        if (datDesde != null && datHasta != null) {
            if (datHasta.before(datDesde)) {
                FacesContext.getCurrentInstance().addMessage("formContent", new FacesMessage(FacesMessage.SEVERITY_INFO, "La fecha de inicio debe ser anterior.", "Info"));
//                System.out.println("false");
                return false;
            }
        }

        return true;
    }

    public boolean validacionOtro() {
        boolean res = true;
        if (datDesde != null && datHasta != null && !datDesde.equals("") && !datHasta.equals("")) {
            if (datHasta.before(datDesde)) {
                FacesContext.getCurrentInstance().addMessage("formContent", new FacesMessage(FacesMessage.SEVERITY_INFO, "La fecha de inicio debe ser anterior.", "Info"));
                res = false;
            }
        }

        return res;
    }

    /**
     * Guarda el nuevo material en el FACTURA
     */
    public void guardarMaterialCFDI() {
        try {
            //Creamos nun nuevo material
            if (debug) {

            }

            String msg = matDao.insertDetalleAdenda(materialData);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, "Error"));
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            redirectConsultaCFDI();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public String getSubstring(String tmp) {
        String res;
        if (tmp != null) {
            res = (tmp.length() > 100) ? tmp.substring(0, 100) + "..." : tmp;
        } else {
            res = "-";
        }
        return res;
    }

    /**
     * Redirecciona a ala pagina de consulta de CFDIs
     */
    public void redirectConsultaCFDI() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/facturas/facturasCFDI.xhtml");
        } catch (IOException e1) {
            System.out.println(">>>>>>>>>>IOException on redirectConsultaCFDI()" + e1.getMessage());
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "A ocurrido un error al intentar redireccionar.", "Error"));
        }
    }

    /**
     * Redirecciona a la pagina de modificacion de materiales de un FACTURA
     */
    public void redirectAgregarMaterial() {
        obtenerCFDIdeListMapSelecteds();
        try {
            //Validamos que hay seleccionado una y solo una factura
            if (selectedCFDSIds.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Es necesario seleccionar una factura.", "Error"));
            } else if (selectedCFDSIds.size() > 1) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Debe seleccionar solo una factura.", "Error"));
            } else {
                selectedMCFD = daoCFDI.BuscarId(selectedCFDSIds.get(0));//Asignamos el FACTURA al tmp
                materialData = new Material();
                materialData.setNumFactura(selectedMCFD.getId());
                FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/facturas/facturasMateriales.xhtml");
            }
        } catch (IOException e1) {
            System.out.println(">>>>>>>>>>IOException on redirectAgregarMaterial()" + e1.getMessage());
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "A ocurrido un problema, lamentamos los inconvenientes.", "Error"));
        }
    }

    public boolean validarArc() {
        boolean val = true;
        if (this.selectedMCFD.getId() <= 0) {
            System.out.println("error" + "Es necesario seleccionar una factura");
            val = false;
        }
        return val;
    }

    public String metodoErroreCancel(String EstatusUUID) {
        String resp = null;
        if (EstatusUUID.contains("201") || EstatusUUID.contains("202")) {
            resp = "La factura se cancelo exitosamente";
        } else if (EstatusUUID.contains("203")) {
            resp = "UUID No encontrado o no corresponde en el emisor";
        } else if (EstatusUUID.contains("204")) {
            resp = "UUID No aplicable para cancelación";
        } else if (EstatusUUID.contains("205")) {
            resp = "UUID No existe";
        } else if (EstatusUUID.contains("301")) {
            resp = "XML mal formado";
        } else if (EstatusUUID.contains("302")) {
            resp = "Sello mal formado o inválido";
        } else if (EstatusUUID.contains("303")) {
            resp = "Sello no corresponde al emisor";
        } else if (EstatusUUID.contains("304")) {
            resp = "Certificado revocado o caduco";
        } else {
            resp = "La factura no se cancelo ";
        }
        return resp;
    }


    public void updateArchivosList() {
        System.out.println(">>>>>>>>>> Uploading file list");
        this.archivosCFDI = this.daoArch.BuscarArchivoCfd_cfdId(this.selectedMCFD.getId());
    }

    /**
     * @return the activeUser
     */
    public MAcceso getActiveUser() {
        return activeUser;
    }

    /**
     * @param activeUser the activeUser to set
     */
    public void setActiveUser(MAcceso activeUser) {
        this.activeUser = activeUser;
    }

    /**
     * @return the cfd
     */
    public MCfd getCfd() {
        return cfd;
    }

    /**
     * @param cfd the cfd to set
     */
    public void setCfd(MCfd cfd) {
        this.cfd = cfd;
    }

    /**
     * @return the strEstatus
     */
    public String getStrEstatus() {
        return strEstatus;
    }

    /**
     * @param strEstatus the strEstatus to set
     */
    public void setStrEstatus(String strEstatus) {
        this.strEstatus = strEstatus;
    }

    /**
     * @return the ids
     */
    public String getIds() {
        return ids;
    }

    /**
     * @param ids the ids to set
     */
    public void setIds(String ids) {
        this.ids = ids;
    }

    /**
     * @return the contentName
     */
    public String getContentName() {
        return contentName;
    }

    /**
     * @param contentName the contentName to set
     */
    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    /**
     * @return the archivoSubFileName
     */
    public String getArchivoSubFileName() {
        return archivoSubFileName;
    }

    /**
     * @param archivoSubFileName the archivoSubFileName to set
     */
    public void setArchivoSubFileName(String archivoSubFileName) {
        this.archivoSubFileName = archivoSubFileName;
    }

    /**
     * @return the archivoSubContentType
     */
    public String getArchivoSubContentType() {
        return archivoSubContentType;
    }

    /**
     * @param archivoSubContentType the archivoSubContentType to set
     */
    public void setArchivoSubContentType(String archivoSubContentType) {
        this.archivoSubContentType = archivoSubContentType;
    }

    /**
     * @return the archivosCFDI
     */
    public List<MArchivosCfd> getArchivosCFDI() {
        return archivosCFDI;
    }

    /**
     * @param archivosCFDI the archivosCFDI to set
     */
    public void setArchivosCFDI(List<MArchivosCfd> archivosCFDI) {
        this.archivosCFDI = archivosCFDI;
    }

    /**
     * @return the listMap
     */
    public List<MapearCfdArchi> getListMap() {
        return listMap;
    }

    /**
     * @param listMap the listMap to set
     */
    public void setListMap(List<MapearCfdArchi> listMap) {
        this.listMap = listMap;
    }

    /**
     * @return the asig
     */
    public int getAsig() {
        return asig;
    }

    /**
     * @param asig the asig to set
     */
    public void setAsig(int asig) {
        this.asig = asig;
    }

    /**
     * @return the sdfDateFormatter
     */
    public SimpleDateFormat getSdfDateFormatter() {
        return sdfDateFormatter;
    }

    /**
     * @param sdfDateFormatter the sdfDateFormatter to set
     */
    public void setSdfDateFormatter(SimpleDateFormat sdfDateFormatter) {
        this.sdfDateFormatter = sdfDateFormatter;
    }

    /**
     * @return the selectedMCFD
     */
    public MCfd getSelectedMCFD() {
        return selectedMCFD;
    }

    /**
     * @param selectedMCFD the selectedMCFD to set
     */
    public void setSelectedMCFD(MCfd selectedMCFD) {
        this.selectedMCFD = selectedMCFD;
    }

    /**
     * @return the archivo
     */
    public FileUpload getArchivo() {
        return archivo;
    }

    /**
     * @param archivo the archivo to set
     */
    public void setArchivo(FileUpload archivo) {
        this.archivo = archivo;
    }

    /**
     * @return the listEmpresas
     */
    public List<MEmpresa> getListEmpresas() {
        return listEmpresas;
    }

    /**
     * @param listEmpresas the listEmpresas to set
     */
    public void setListEmpresas(List<MEmpresa> listEmpresas) {
        this.listEmpresas = listEmpresas;
    }



    /**
     * @return the materialData
     */
    public Material getMaterialData() {
        return materialData;
    }

    /**
     * @return the NumCajas
     */
    public String getNumCajas() {
        return NumCajas;
    }

    /**
     * @return the Materialtxt
     */
    public String getMaterialtxt() {
        return Materialtxt;
    }

    /**
     * @return the Recibotxt
     */
    public String getRecibotxt() {
        return Recibotxt;
    }

    /**
     * @return the ReciboFechatxt
     */
    public String getReciboFechatxt() {
        return ReciboFechatxt;
    }

    /**
     * @param materialData the materialData to set
     */
    public void setMaterialData(Material materialData) {
        this.materialData = materialData;
    }

    /**
     * @param NumCajas the NumCajas to set
     */
    public void setNumCajas(String NumCajas) {
        this.NumCajas = NumCajas;
    }

    /**
     * @param Materialtxt the Materialtxt to set
     */
    public void setMaterialtxt(String Materialtxt) {
        this.Materialtxt = Materialtxt;
    }

    /**
     * @param Recibotxt the Recibotxt to set
     */
    public void setRecibotxt(String Recibotxt) {
        this.Recibotxt = Recibotxt;
    }

    /**
     * @param ReciboFechatxt the ReciboFechatxt to set
     */
    public void setReciboFechatxt(String ReciboFechatxt) {
        this.ReciboFechatxt = ReciboFechatxt;
    }

    /**
     * @return the matDao
     */
    public MaterialDAO getMatDao() {
        return matDao;
    }

    /**
     * @param matDao the matDao to set
     */
    public void setMatDao(MaterialDAO matDao) {
        this.matDao = matDao;
    }

    /**
     * @param httpServletRequest the httpServletRequest to set
     */
    public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    /**
     * @param faceContext the faceContext to set
     */
    public void setFaceContext(FacesContext faceContext) {
        this.faceContext = faceContext;
    }

    public boolean isCancelar() {
        return cancelar;
    }

    public void setCancelar(boolean cancelar) {
        this.cancelar = cancelar;
    }
}
