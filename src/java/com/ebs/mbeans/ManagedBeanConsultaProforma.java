/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.mbeans;

import com.ebs.model.LazyProformaDataModel;
import fe.db.MAcceso;
import fe.db.MCfdProforma;
import fe.db.MEmpresa;
import fe.db.MOtroProforma;
import fe.model.dao.ConfigDAO;
import fe.model.dao.ProformaDao;
import lombok.Getter;
import lombok.Setter;
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

public class ManagedBeanConsultaProforma implements Serializable {

    private static final int ESTADO_DOCUMENTO_CANCELADO = 0;
    private static final int ESTADO_DOCUMENTO_GENERADO = 1;
    private static final int ESTADO_DOCUMENTO_OK = 2;

    @Getter
    @Setter
    private int empresaIdFiltro = -1;
    private Integer[] idsEmpresasAsignadas;
    @Getter
    @Setter
    private String strEstatus;
    @Getter
    @Setter
    private String tipBusq;//-1 indica que no existe el tipo de busqueda
    @Getter
    @Setter
    private boolean booTipoBusqueda;//Bandera para activar o desactivar la casilla del parametro
    @Getter
    @Setter
    private String paramBusq;//Parametro de busqueda
    @Getter
    @Setter
    private Date datDesde;//Fecha Inicial
    @Getter
    @Setter
    private Date datHasta;//Fecha Final

    private String contentName;
    private String archivoSubFileName;
    private String archivoSubContentType;
    @Getter
    @Setter
    private List<MCfdProforma> archivosCFDI;
    @Getter
    @Setter
    private List<MCfdProforma> listMap;//USADO EN LA CANCELACION
    private ConfigDAO DAOCong;
    private ProformaDao daoCFDI;
    ;
    @Getter
    @Setter
    private LazyDataModel<MCfdProforma> listMapMCA;
    @Getter
    @Setter
    private List<MCfdProforma> listMapMCASelecteds;
    private SimpleDateFormat sdfDateFormatter;
    private FileUpload archivo;//Cambiar a multiples archivos

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
    public ManagedBeanConsultaProforma() {
        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        activeUser = (MAcceso) httpSession.getAttribute("macceso");
        paramBusq = "";
        appContext = httpServletRequest.getContextPath();

    }

    @PostConstruct
    public void init() {
        listMapMCASelecteds = new ArrayList();
        DAOCong = new ConfigDAO();
        daoCFDI = new ProformaDao();
        sdfDateFormatter = new SimpleDateFormat("yyyy-MM-dd HHmmss");
        strEstatus = "-1";
        tipBusq = "-1";//-1 indica que no existe el tipo de busqueda
        booTipoBusqueda = false;//Bandera para activar o desactivar la casilla del parametro
        paramBusq = "";//Parametro de busqueda

        cargarEmpresasUsuario();
        //cargamos los cfdi por default
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
                listMapMCA = new LazyProformaDataModel(
                        true,
                        activeUser.getId(),
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
                listMapMCA = new LazyProformaDataModel(
                        false,
                        activeUser.getId(),
                        idsBusqueda,
                        tipBusq,
                        paramBusq,
                        datDesde,
                        datHasta,
                        activeUser.getCliente(),
                        strEstatus
                );
            }
    }

    public void extraerFacturas() {
        int i = 0;
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        String contentType = "application/zip";
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HHmmss");
        if (listMapMCASelecteds.size() > 0) {
            try {
                ec.responseReset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
                ec.setResponseContentType(contentType); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ExternalContext#getMimeType() for auto-detection based on filename.
                ec.setResponseHeader("Content-Disposition", "attachment; filename=\"CFDIS_" + sf.format(new Date()) + ".zip\"");
                OutputStream output = ec.getResponseOutputStream();
                //Now you can write the InputStream of the file to the above OutputStream the usual way.
                //...
                // TODO: 24/10/2017 HACER COMPLATIBLE EL ZIPEO CON LAS FACTURAS DE PROFORMA
                //CrearZIPFacturas crear = new CrearZIPFacturas();
                //crear.ZipCfdis(listCFDSAux, output);
                fc.responseComplete(); // Important! Otherwise JSF will attempt to render the response which obviously will fail since it's already written with a file and closed.
                //FacesContext.getCurrentInstance().responseComplete(); //Equal
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        } else {
            fc.addMessage("formConsultasCFD", new FacesMessage(FacesMessage.SEVERITY_INFO, "Seleccione al menos una factura para descargar.", "Info"));
        }
    }

    /**
     * Cancelar el CFDI Proforma poniendo el estatus en 0
     */
    public void cancelar() {
        FacesContext fc = FacesContext.getCurrentInstance();
        if (!listMapMCASelecteds.isEmpty()) {
            for (MCfdProforma temp : listMapMCASelecteds) {
                if (temp.getEstadoDocumento() != ESTADO_DOCUMENTO_CANCELADO) {
                    temp.setEstadoDocumento(ESTADO_DOCUMENTO_CANCELADO);///Cambio de estatus a cancelado
                    if (daoCFDI.actualiza(temp)) {

                    }
                }
            }
            listMapMCASelecteds.clear();
            cargarCFDI();
            fc.addMessage("formConsultasCFD", new FacesMessage(FacesMessage.SEVERITY_INFO, "Factura(s) Proforma cancelada(s).", "Info"));
        } else {
            fc.addMessage("formConsultasCFD", new FacesMessage(FacesMessage.SEVERITY_WARN, "Seleccione al menos una factura para cancelar.", "Info"));
        }
    }

    /**
     * Descarga un reporte en formato zip. Responde al boton de extraer reportes
     * en facturas FACTURA.
     */
    public void extraerReporte() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HHmmss");
        List<MCfdProforma> tmp;
        String contentType = "application/vnd.sealed-xls";
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        Integer[] idsBusqueda = {-1};
        if (empresaIdFiltro > 0) {
            idsBusqueda = new Integer[1];
            idsBusqueda[0] = empresaIdFiltro;
        } else {
            idsBusqueda = idsEmpresasAsignadas;
        }

        int totalRow = 0;
        int currentRow = 0;
        int pageSize = 100;//Rows per request

        if (validacionOtro()) {
            //First rows test
            tmp = daoCFDI.ListaParametrosExportLazy(
                    activeUser.getId(),
                    idsBusqueda,
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
                            for (MCfdProforma cfd : tmp) {
                                Row fila = hoja.createRow(currentRow + 1);
                                Cell celda = fila.createCell(0);
                                celda.setCellValue(cfd.getNumeroFactura());
                                Cell celda1 = fila.createCell(1);
                                celda1.setCellValue(cfd.getFolioErp());
                                Cell celda2 = fila.createCell(2);
                                MOtroProforma otro = daoCFDI.Otro(cfd.getId());
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
                                    idsBusqueda,
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
        listMapMCASelecteds = new ArrayList();
        datDesde = null;
        datHasta = null;
        strEstatus = "-1";
        tipBusq = "-1";//-1 indica que no existe el tipo de busqueda
        booTipoBusqueda = false;//Bandera para activar o desactivar la casilla del parametro
        paramBusq = "";//Parametro de busqueda
        empresaIdFiltro = -1;
        //cargamos los cfdi por default
        cargarCFDI();
    }

    public boolean validacionCliente() {
        if (datDesde != null && datHasta != null) {
            if (datHasta.before(datDesde)) {
                FacesContext.getCurrentInstance().addMessage("formContent", new FacesMessage(FacesMessage.SEVERITY_INFO, "La fecha de unicio debe ser anterior.", "Info"));
                return false;
            }
        }
        return true;
    }

    public boolean validacionOtro() {
        boolean res = true;
        if (datDesde != null && datHasta != null && !datDesde.equals("") && !datHasta.equals("")) {
            if (datHasta.before(datDesde)) {
                FacesContext.getCurrentInstance().addMessage("formContent",
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "La fecha de inicio debe ser anterior.", "Info"));
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


    public String getSubstring(String tmp) {
        String res;
        if (tmp != null) {
            res = (tmp.length() > 100) ? tmp.substring(0, 100) + "..." : tmp;
        } else {
            res = "-";
        }
        return res;
    }
}
