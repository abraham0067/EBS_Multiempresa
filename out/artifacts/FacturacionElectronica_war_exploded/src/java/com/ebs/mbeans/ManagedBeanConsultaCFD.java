/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.mbeans;

import fe.db.MAcceso;
import fe.db.MEmpresa;
import fe.db.MInvoice;
import fe.model.dao.CfdDAO;
import fe.model.dao.ConfigDAO;
import fe.model.dao.EmpresaDAO;
import fe.model.util.CrearZIPFacturasCFD;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 *
 * @author Eduardo C. Flores Ambrosio <Eduardo at EB&S>
 */
public class ManagedBeanConsultaCFD implements Serializable{

    //Parametros

    private MEmpresa empresa;
    private String strEstatus;
    private String tipBusq;//-1 indica que no existe el tipo de busqueda
    private boolean booTipoBusqueda;//Bandera para activar o desactivar la casilla del parametro
    private String paramBusq;//Parametro de busqueda
    private Date datDesde;//Fecha Inicial
    private Date datHasta;//Fecha Final

    private List<MInvoice> listCFDS;
    private List<MInvoice> listCFDSAux;
    private List<MInvoice> listSelectsCFDS;
    private MInvoice selectedCFD;
    private SimpleDateFormat sdfUtil;

    //DAOs
    private EmpresaDAO daoEmp;
    private CfdDAO daoCFD;
    private ConfigDAO daoConfig;

    //Datos de sesion
    private MAcceso mAcceso;
    private final HttpServletRequest httpServletRequest;
    private final FacesContext faceContext;
    private String appContext;


    /**
     * Creates a new instance of ManagedBeanConsultaCFD
     */
    public ManagedBeanConsultaCFD() {
        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        mAcceso = (MAcceso) httpSession.getAttribute("macceso");
        empresa = mAcceso.getEmpresa();
        appContext = httpServletRequest.getContextPath();

    }

    @PostConstruct
    public void init() {
        daoConfig = new ConfigDAO();
        daoCFD = new CfdDAO();
        daoEmp = new EmpresaDAO();
        sdfUtil = new SimpleDateFormat("yyyy-MM-dd HHmmss");
    }

    public void cargarCFD() {
        if (mAcceso.getPerfil().getTipoUser().toUpperCase().equals("CLIENTE")) {//Si el Usuario es un cliente
            if (validacionCliente()) {
                this.setListCFDS(daoCFD.ListaParametrosClientes(mAcceso.getId(), empresa.getId(), tipBusq, paramBusq, datDesde, datHasta, mAcceso.getCliente()));
            }else{
                FacesContext.getCurrentInstance().addMessage("formConsultasCFD", new FacesMessage(FacesMessage.SEVERITY_WARN, "Se debe especificar un parametro de busqueda.", "Info"));
            }
        } else {//otro usuario
            if (validacionOtro()) {
                this.listCFDS = daoCFD.ListaParametros(mAcceso.getId(), empresa.getId(), tipBusq, paramBusq, datDesde, datHasta, Integer.parseInt(strEstatus));
            }else{
                FacesContext.getCurrentInstance().addMessage("formConsultasCFD", new FacesMessage(FacesMessage.SEVERITY_WARN, "Se debe especificar un parametro de busqueda.", "Info"));
            }
        }

    }

    public boolean validacionCliente() {
        if (datDesde != null && datHasta != null) {
            if (datHasta.before(datDesde)) {
                return false;
            }
        }
        return true;
    }
    
    public boolean validacionOtro() {
        boolean res = true;
        if (datDesde != null && datHasta != null && !datDesde.equals("")
                && !datHasta.equals("")) {
            if (datHasta.before(datDesde)) {
                res = false;
            }
        }
        if (tipBusq.trim().equals("-1") && datHasta == null
                && datDesde == null && getEmpresa().getId() <= 0) {
            res = false;
        }
        return res;
    }

    public void extraerFacturas() {
        int i = 0;
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        String contentType = "application/zip";
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HHmmss");
        if (listSelectsCFDS.size() > 0) {
            Integer[] idsin = new Integer[listSelectsCFDS.size()];
            for (MInvoice tmp : listSelectsCFDS) {
                idsin[i] = tmp.getId();
                i++;
            }
            this.listCFDSAux = daoCFD.ListaSelec(idsin);

            if (listCFDSAux != null && !listCFDSAux.isEmpty()) {
                CrearZIPFacturasCFD crear = new CrearZIPFacturasCFD();
                try {
                    ec.responseReset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
                    ec.setResponseContentType(contentType); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ExternalContext#getMimeType() for auto-detection based on filename.
                    ec.setResponseHeader("Content-Disposition", "attachment; filename=\"CFDIS_" + sf.format(new Date()) + ".zip\"");
                    OutputStream output = ec.getResponseOutputStream();
                    //Now you can write the InputStream of the file to the above OutputStream the usual way.
                    //...
                    byte[] archivoZip = crear.ZipCfdis(listCFDSAux);
                    output.write(archivoZip);
                    output.flush();
                    fc.responseComplete(); // Important! Otherwise JSF will attempt to render the response which obviously will fail since it's already written with a file and closed.
                    //FacesContext.getCurrentInstance().responseComplete();
                } catch (Exception e) {
                    e.printStackTrace(System.out);
                }
            }
        }

    }

    /**
     * Activa desactica la casilla de parametro de busqueda
     */
    public void cambiarValor() {
        if (tipBusq.equalsIgnoreCase("-1") || tipBusq == null) {
            booTipoBusqueda = false;
            paramBusq = "";
        } else {
            booTipoBusqueda = true;
        }
    }

    /**
     * @return the empresa
     */
    public MEmpresa getEmpresa() {
        return empresa;
    }

    /**
     * @return the strEstatus
     */
    public String getStrEstatus() {
        return strEstatus;
    }

    /**
     * @return the tipBusq
     */
    public String getTipBusq() {
        return tipBusq;
    }

    /**
     * @return the booTipoBusqueda
     */
    public boolean isBooTipoBusqueda() {
        return booTipoBusqueda;
    }

    /**
     * @return the paramBusq
     */
    public String getParamBusq() {
        return paramBusq;
    }

    /**
     * @return the datDesde
     */
    public Date getDatDesde() {
        return datDesde;
    }

    /**
     * @return the datHasta
     */
    public Date getDatHasta() {
        return datHasta;
    }

    /**
     * @param empresa the empresa to set
     */
    public void setEmpresa(MEmpresa empresa) {
        this.empresa = empresa;
    }

    /**
     * @param strEstatus the strEstatus to set
     */
    public void setStrEstatus(String strEstatus) {
        this.strEstatus = strEstatus;
    }

    /**
     * @param tipBusq the tipBusq to set
     */
    public void setTipBusq(String tipBusq) {
        this.tipBusq = tipBusq;
    }

    /**
     * @param booTipoBusqueda the booTipoBusqueda to set
     */
    public void setBooTipoBusqueda(boolean booTipoBusqueda) {
        this.booTipoBusqueda = booTipoBusqueda;
    }

    /**
     * @param paramBusq the paramBusq to set
     */
    public void setParamBusq(String paramBusq) {

        this.paramBusq = paramBusq;
    }

    /**
     * @param datDesde the datDesde to set
     */
    public void setDatDesde(Date datDesde) {
        this.datDesde = datDesde;
    }

    /**
     * @param datHasta the datHasta to set
     */
    public void setDatHasta(Date datHasta) {
        this.datHasta = datHasta;
    }

    /**
     * @return the listCFDS
     */
    public List<MInvoice> getListCFDS() {
        return listCFDS;
    }

    /**
     * @return the listCFDSAux
     */
    public List<MInvoice> getListCFDSAux() {
        return listCFDSAux;
    }

    /**
     * @return the listSelectsCFDS
     */
    public List<MInvoice> getListSelectsCFDS() {
        return listSelectsCFDS;
    }

    /**
     * @return the selectedCFD
     */
    public MInvoice getSelectedCFD() {
        return selectedCFD;
    }

    /**
     * @return the mAcceso
     */
    public MAcceso getmAcceso() {
        return mAcceso;
    }

    /**
     * @param listCFDS the listCFDS to set
     */
    public void setListCFDS(List<MInvoice> listCFDS) {
        this.listCFDS = listCFDS;
    }

    /**
     * @param listCFDSAux the listCFDSAux to set
     */
    public void setListCFDSAux(List<MInvoice> listCFDSAux) {
        this.listCFDSAux = listCFDSAux;
    }

    /**
     * @param listSelectsCFDS the listSelectsCFDS to set
     */
    public void setListSelectsCFDS(List<MInvoice> listSelectsCFDS) {
        this.listSelectsCFDS = listSelectsCFDS;
    }

    /**
     * @param selectedCFD the selectedCFD to set
     */
    public void setSelectedCFD(MInvoice selectedCFD) {
        this.selectedCFD = selectedCFD;
    }

    /**
     * @param mAcceso the mAcceso to set
     */
    public void setmAcceso(MAcceso mAcceso) {
        this.mAcceso = mAcceso;
    }

    /**
     * @return the sdfUtil
     */
    public SimpleDateFormat getSdfUtil() {
        return sdfUtil;
    }

    /**
     * @param sdfUtil the sdfUtil to set
     */
    public void setSdfUtil(SimpleDateFormat sdfUtil) {
        this.sdfUtil = sdfUtil;
    }

}
