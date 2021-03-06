/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.mbeans;

import com.ebs.catalogos.TiposComprobante;
import com.ebs.catalogos.TiposDocumento;
import com.ebs.clienteFEWS.ClienteFEWS;
import com.ebs.model.LazyMPagoDataModel;
import com.ebs.util.ZipperFacturasCfdi33;
import fe.db.MAcceso;
import fe.db.MArchivosCfd;
import fe.db.MCfdPagos;
import fe.db.MEmpresa;
import fe.db.MOtroPagos;
import fe.model.dao.ArchivosPagosDAO;
import fe.model.dao.AutoPagosDao;
import fe.model.dao.ConfigDAO;
import fe.model.dao.EmpresaDAO;
import fe.model.dao.MaterialDAO;
import fe.model.util.Material;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.primefaces.component.fileupload.FileUpload;
import org.primefaces.model.LazyDataModel;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ManagedBeanConsultaPagos implements Serializable {

    //Parametros
    
    
    private int empresaIdFiltro = -1;
    private Integer[] idsEmpresasAsignadas;

    
    
    private String strEstatus;
    
    
    private String tipBusq;//-1 indica que no existe el tipo de busqueda
    
    
    private boolean booTipoBusqueda;//Bandera para activar o desactivar la casilla del parametro
    
    
    private String paramBusq;//Parametro de busqueda
    
    
    private Date datDesde;//Fecha Inicial
    
    
    private Date datHasta;//Fecha Final

    private String contentName;
    private String archivoSubFileName;
    private String archivoSubContentType;
    
    
    private List<MArchivosCfd> archivosCFDI;
    
    
    private List<MCfdPagos> listMap;//USADO EN LA CANCELACION
    
    
    private List<MEmpresa> listEmpresas;
    private EmpresaDAO daoEmp;
    private ConfigDAO DAOCong;
    private String ids;
    private int asig = 0;
    private AutoPagosDao daoCFDI;
    private ArchivosPagosDAO daoArch;
    private MaterialDAO matDao;
    private MCfdPagos cfd;
    
    
    private List<MCfdPagos> listCFDS;
    /*
    
    private List<MCfdPagos> listCFDSAux;*/
    
    
    private List<Integer> listCFDSAux;
    /*
    
    private List<MCfdPagos> selectedCFDS;//Donde se guardaran los cfdi extraidos del listmap*/
    
    
    private List<Integer> selectedCFDS;//Donde se guardaran los cfdi extraidos del listmap
    
    
    private MCfdPagos selectedMCFD;//FACTURA axiliar
    //private List<MapearCfdArchi> listMapMCA;//Usaoo en el View
    
    
    private LazyDataModel<MCfdPagos> listMapMCA;
    
    
    private List<MCfdPagos> listMapMCASelecteds;
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

    /**
     * Creates a new instance of ManagedBeanConsultaCFDI
     */
    public ManagedBeanConsultaPagos() {
        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        activeUser = (MAcceso) httpSession.getAttribute("macceso");
        paramBusq = "";
        appContext = httpServletRequest.getContextPath();
    }

    @PostConstruct
    public void init() {
        selectedCFDS = new ArrayList();
        listMapMCASelecteds = new ArrayList();
        DAOCong = new ConfigDAO();
        daoArch = new ArchivosPagosDAO();
        daoEmp = new EmpresaDAO();
        daoCFDI = new AutoPagosDao();
        matDao = new MaterialDAO();
        sdfDateFormatter = new SimpleDateFormat("yyyy-MM-dd HHmmss");
        strEstatus = "-1";
        tipBusq = "-1";//-1 indica que no existe el tipo de busqueda
        booTipoBusqueda = false;//Bandera para activar o desactivar la casilla del parametro
        paramBusq = "";//Parametro de busqueda
        //cargamos los cfdi por default
        //cargamos los cfdi por default
        cargarEmpresasUsuario();
        cargarCFDI();
    }

    public void cargarEmpresasUsuario() {
        ///Cargamos ids empresas asociadas al usuario
        List<Integer> lista = new ArrayList<>();
        activeUser.getEmpresas().forEach(emp -> {
            lista.add(emp.getId());
        });
        idsEmpresasAsignadas = lista.toArray(new Integer[lista.size()]);
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



        if (activeUser.getPerfil().getTipoUser().toUpperCase().equals("CLIENTE")) {//Si el Usuario es un cliente
            if (validacionCliente()) {
                listMapMCA = new LazyMPagoDataModel(
                        true,
                        activeUser,
                        idsBusqueda,
                        tipBusq,
                        paramBusq,
                        datDesde,
                        datHasta,
                        activeUser.getCliente());
            } else {
                FacesContext.getCurrentInstance().addMessage("formConsultasCFD", new FacesMessage(FacesMessage.SEVERITY_WARN, "Debe especificar un parametro de busqueda.", "Info"));
            }
        } else//otro usuario
            if (validacionOtro()) {
                listMapMCA = new LazyMPagoDataModel(
                        false,
                        activeUser,
                        idsBusqueda,
                        tipBusq,
                        paramBusq,
                        datDesde,
                        datHasta,
                        activeUser.getCliente(),
                        strEstatus);
            } else {
                FacesContext.getCurrentInstance().addMessage("formConsultasCFD", new FacesMessage(FacesMessage.SEVERITY_WARN, "Debe especificar un parametros de busqueda.", "Info"));
            }
    }

    /**
     * Extrae unicamente los MCFD del listmap de archivos con cada MCFD
     */
    private void obtenerCFDIdeListMapSelecteds() {
        if (selectedCFDS != null) {
            selectedCFDS.clear();
        } else {
            selectedCFDS = new ArrayList();
        }
        for (MCfdPagos tmp : this.listMapMCASelecteds) {
            selectedCFDS.add(tmp.getId());
        }
    }
    
    /**
     * Descarga de un zip con los pdfs y los xmls
     */
    public void extraerFacturas() {
        obtenerCFDIdeListMapSelecteds();//Extraemos los FACTURA de la lista mapeada
        int i = 0;
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        String contentType = "application/zip";
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HHmmss");
        if (selectedCFDS.size() > 0) {
            this.listCFDSAux = selectedCFDS;
            if (listCFDSAux != null && !listCFDSAux.isEmpty()) {
                try {
                    ec.responseReset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
                    ec.setResponseContentType(contentType); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ExternalContext#getMimeType() for auto-detection based on filename.
                    ec.setResponseHeader("Content-Disposition", "attachment; filename=\"CFDIS_" + sf.format(new Date()) + ".zip\"");
                    OutputStream output = ec.getResponseOutputStream();
                    //Now you can write the InputStream of the file to the above OutputStream the usual way.
                    //...
                    //ZipperFacturasCfdi33 zipperFacturasCfdi33 = new ZipperFacturasCfdi33();
                    //zipperFacturasCfdi33.ZipCfdisPagos(listCFDSAux, output);
                    ClienteFEWS clienteFEWS = new ClienteFEWS();
                    byte[] zipPagos = clienteFEWS.zip(listCFDSAux, "ZIP_PAGOS");
                    output.write(zipPagos);
                    fc.responseComplete(); // Important! Otherwise JSF will attempt to render the response which obviously will fail since it's already written with a file and closed.
                    FacesContext.getCurrentInstance().responseComplete(); //Equal
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
        List<MCfdPagos> tmp;
        String contentType = "application/vnd.sealed-xls";
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        int totalRow = 0;
        int currentRow = 0;
        int pageSize = 100;//Rows per request

        if (validacionOtro()) {
            //First rows test
            tmp = daoCFDI.ListaParametrosExportLazy(
                    activeUser.getId(),
                    empresaIdFiltro,
                    tipBusq,
                    paramBusq,
                    datDesde,
                    datHasta,
                    Integer.parseInt(strEstatus),
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
                            for (MCfdPagos cfd : tmp) {
                                Row fila = hoja.createRow(currentRow + 1);
                                Cell celda = fila.createCell(0);
                                celda.setCellValue(cfd.getNumeroFactura());
                                Cell celda1 = fila.createCell(1);
                                celda1.setCellValue(cfd.getFolioErp());
                                Cell celda2 = fila.createCell(2);
                                MOtroPagos otro = daoCFDI.Otro(cfd.getId());
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
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                celda5.setCellValue(sdf.format(cfd.getFecha()));
                                Cell celda6 = fila.createCell(6);
                                celda6.setCellValue(cfd.getSubtotalMl());
                                Cell celda7 = fila.createCell(7);
                                celda7.setCellValue(cfd.getIva());
                                Cell celda8 = fila.createCell(8);
                                celda8.setCellValue(cfd.getTotal());
                                Cell celda9 = fila.createCell(9);
                                celda9.setCellValue(cfd.getMoneda());
                                Cell celda10 = fila.createCell(10);
                                celda10.setCellValue(cfd.getTipoCambio());
                                Cell celda11 = fila.createCell(11);
                                if (cfd.getEstadoDocumento() == 1) {
                                    celda11.setCellValue("GENERADA");
                                } else {
                                    celda11.setCellValue("CANCELADA");
                                }

                                Cell celda12 = fila.createCell(12);
                                celda12.setCellValue(cfd.getUuid());
                                currentRow++;
                            }
                            //GET NEXT ROWS 
                            tmp = daoCFDI.ListaParametrosExportLazy(
                                    activeUser.getId(),
                                    empresaIdFiltro,
                                    tipBusq,
                                    paramBusq,
                                    datDesde,
                                    datHasta,
                                    Integer.parseInt(strEstatus),
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
     * Limpia los filtros del formulario de busqueda
     */
    public void limpiarFiltros() {
        selectedCFDS = new ArrayList();
        listMapMCASelecteds = new ArrayList();
        datDesde = null;
        datHasta = null;
        strEstatus = "-1";
        tipBusq = "-1";//-1 indica que no existe el tipo de busqueda
        booTipoBusqueda = false;//Bandera para activar o desactivar la casilla del parametro
        paramBusq = "";//Parametro de busqueda
        //cargamos los cfdi por default
        cargarCFDI();
    }

    public boolean validacionCliente() {
//        System.out.println("VALIDACION CLIENTES");
        if (datDesde != null && datHasta != null) {
            if (datHasta.before(datDesde)) {
                FacesContext.getCurrentInstance().addMessage("formContent", new FacesMessage(FacesMessage.SEVERITY_INFO, "La fecha de unicio debe ser anterior.", "Info"));
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
        if ((tipBusq != null && !tipBusq.trim().equals("-1")) && (paramBusq == null || paramBusq.isEmpty())) {
            FacesContext.getCurrentInstance().addMessage("formContent",
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Debe especificar el parametro de busqueda.", "Info"));
            res = false;
        }
        return res;
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
     * Activa desactica la casilla del parametro de busqueda
     */
    public void cambiarValor() {
        //System.out.println("Cambiando valor");
        if (tipBusq == null || tipBusq.equalsIgnoreCase("-1")) {
            booTipoBusqueda = false;
            paramBusq = "";
        } else {
            booTipoBusqueda = true;
        }
    }

    public int getEmpresaIdFiltro() {
        return empresaIdFiltro;
    }

    public void setEmpresaIdFiltro(int empresaIdFiltro) {
        this.empresaIdFiltro = empresaIdFiltro;
    }

    public Integer[] getIdsEmpresasAsignadas() {
        return idsEmpresasAsignadas;
    }

    public void setIdsEmpresasAsignadas(Integer[] idsEmpresasAsignadas) {
        this.idsEmpresasAsignadas = idsEmpresasAsignadas;
    }

    public String getStrEstatus() {
        return strEstatus;
    }

    public void setStrEstatus(String strEstatus) {
        this.strEstatus = strEstatus;
    }

    public String getTipBusq() {
        return tipBusq;
    }

    public void setTipBusq(String tipBusq) {
        this.tipBusq = tipBusq;
    }

    public boolean isBooTipoBusqueda() {
        return booTipoBusqueda;
    }

    public void setBooTipoBusqueda(boolean booTipoBusqueda) {
        this.booTipoBusqueda = booTipoBusqueda;
    }

    public String getParamBusq() {
        return paramBusq;
    }

    public void setParamBusq(String paramBusq) {
        this.paramBusq = paramBusq;
    }

    public Date getDatDesde() {
        return datDesde;
    }

    public void setDatDesde(Date datDesde) {
        this.datDesde = datDesde;
    }

    public Date getDatHasta() {
        return datHasta;
    }

    public void setDatHasta(Date datHasta) {
        this.datHasta = datHasta;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getArchivoSubFileName() {
        return archivoSubFileName;
    }

    public void setArchivoSubFileName(String archivoSubFileName) {
        this.archivoSubFileName = archivoSubFileName;
    }

    public String getArchivoSubContentType() {
        return archivoSubContentType;
    }

    public void setArchivoSubContentType(String archivoSubContentType) {
        this.archivoSubContentType = archivoSubContentType;
    }

    public List<MArchivosCfd> getArchivosCFDI() {
        return archivosCFDI;
    }

    public void setArchivosCFDI(List<MArchivosCfd> archivosCFDI) {
        this.archivosCFDI = archivosCFDI;
    }

    public List<MCfdPagos> getListMap() {
        return listMap;
    }

    public void setListMap(List<MCfdPagos> listMap) {
        this.listMap = listMap;
    }

    public List<MEmpresa> getListEmpresas() {
        return listEmpresas;
    }

    public void setListEmpresas(List<MEmpresa> listEmpresas) {
        this.listEmpresas = listEmpresas;
    }

    public EmpresaDAO getDaoEmp() {
        return daoEmp;
    }

    public void setDaoEmp(EmpresaDAO daoEmp) {
        this.daoEmp = daoEmp;
    }

    public ConfigDAO getDAOCong() {
        return DAOCong;
    }

    public void setDAOCong(ConfigDAO DAOCong) {
        this.DAOCong = DAOCong;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public int getAsig() {
        return asig;
    }

    public void setAsig(int asig) {
        this.asig = asig;
    }

    public AutoPagosDao getDaoCFDI() {
        return daoCFDI;
    }

    public void setDaoCFDI(AutoPagosDao daoCFDI) {
        this.daoCFDI = daoCFDI;
    }

    public ArchivosPagosDAO getDaoArch() {
        return daoArch;
    }

    public void setDaoArch(ArchivosPagosDAO daoArch) {
        this.daoArch = daoArch;
    }

    public MaterialDAO getMatDao() {
        return matDao;
    }

    public void setMatDao(MaterialDAO matDao) {
        this.matDao = matDao;
    }

    public MCfdPagos getCfd() {
        return cfd;
    }

    public void setCfd(MCfdPagos cfd) {
        this.cfd = cfd;
    }

    public List<MCfdPagos> getListCFDS() {
        return listCFDS;
    }

    public void setListCFDS(List<MCfdPagos> listCFDS) {
        this.listCFDS = listCFDS;
    }

    public List<Integer> getListCFDSAux() {
        return listCFDSAux;
    }

    public void setListCFDSAux(List<Integer> listCFDSAux) {
        this.listCFDSAux = listCFDSAux;
    }

    public List<Integer> getSelectedCFDS() {
        return selectedCFDS;
    }

    public void setSelectedCFDS(List<Integer> selectedCFDS) {
        this.selectedCFDS = selectedCFDS;
    }

    public MCfdPagos getSelectedMCFD() {
        return selectedMCFD;
    }

    public void setSelectedMCFD(MCfdPagos selectedMCFD) {
        this.selectedMCFD = selectedMCFD;
    }

    public LazyDataModel<MCfdPagos> getListMapMCA() {
        return listMapMCA;
    }

    public void setListMapMCA(LazyDataModel<MCfdPagos> listMapMCA) {
        this.listMapMCA = listMapMCA;
    }

    public List<MCfdPagos> getListMapMCASelecteds() {
        return listMapMCASelecteds;
    }

    public void setListMapMCASelecteds(List<MCfdPagos> listMapMCASelecteds) {
        this.listMapMCASelecteds = listMapMCASelecteds;
    }

    public SimpleDateFormat getSdfDateFormatter() {
        return sdfDateFormatter;
    }

    public void setSdfDateFormatter(SimpleDateFormat sdfDateFormatter) {
        this.sdfDateFormatter = sdfDateFormatter;
    }

    public FileUpload getArchivo() {
        return archivo;
    }

    public void setArchivo(FileUpload archivo) {
        this.archivo = archivo;
    }

    public Material getMaterialData() {
        return materialData;
    }

    public void setMaterialData(Material materialData) {
        this.materialData = materialData;
    }

    public String getNumCajas() {
        return NumCajas;
    }

    public void setNumCajas(String numCajas) {
        NumCajas = numCajas;
    }

    public String getMaterialtxt() {
        return Materialtxt;
    }

    public void setMaterialtxt(String materialtxt) {
        Materialtxt = materialtxt;
    }

    public String getRecibotxt() {
        return Recibotxt;
    }

    public void setRecibotxt(String recibotxt) {
        Recibotxt = recibotxt;
    }

    public String getReciboFechatxt() {
        return ReciboFechatxt;
    }

    public void setReciboFechatxt(String reciboFechatxt) {
        ReciboFechatxt = reciboFechatxt;
    }

    public MAcceso getActiveUser() {
        return activeUser;
    }

    public void setActiveUser(MAcceso activeUser) {
        this.activeUser = activeUser;
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public FacesContext getFaceContext() {
        return faceContext;
    }

    public void setFaceContext(FacesContext faceContext) {
        this.faceContext = faceContext;
    }

    public String getAppContext() {
        return appContext;
    }

    public void setAppContext(String appContext) {
        this.appContext = appContext;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
