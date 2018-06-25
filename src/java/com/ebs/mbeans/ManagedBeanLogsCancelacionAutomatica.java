
package com.ebs.mbeans;

import com.ebs.model.LazyLogsAppDataModel;
import fe.db.MAcceso;
import fe.db.MLogApp;
import fe.model.dao.LogAPPDAO;
import org.primefaces.model.LazyDataModel;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Eduardo C. Flores Ambrosio <Eduardo at EB&S>
 */
public class ManagedBeanLogsCancelacionAutomatica implements Serializable {

    private MAcceso mAcceso;
    //Contexto
    private HttpServletRequest httpServletRequest;
    private FacesContext facesContext;
    private String appContext;
    private LazyDataModel<MLogApp> listaLogApp;//Log de aplicacion
    private MLogApp logApp;
    private Date fecha;

    ///Usados en la busqueda de logs de acceso
    private String tipoBusqueda;
    private String paramBusqueda;

    ///Campos de busqueda para logs de proceso de facturacion
    private String serie;
    private String folioErp;

    private int idEmpresaSelected;
    private LogAPPDAO daoApp;

    private String tipoServicio;

    /**
     * Creates a new instance of ManagedBeanLogs
     */
    public ManagedBeanLogsCancelacionAutomatica() {}

    @PostConstruct
    public void init() {
        facesContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        mAcceso = (MAcceso) httpSession.getAttribute("macceso");
        appContext = httpServletRequest.getContextPath();
        //fecha = new Date();
        daoApp = new LogAPPDAO();
        idEmpresaSelected = 0;
        serie = "";
        folioErp = "";
        tipoServicio = "-1";
        buscarLogsApp();///Preload data on render
    }

    public void buscarLogsApp() {
        try {
            //Lazy loading
            listaLogApp = new LazyLogsAppDataModel(mAcceso.getId(), idEmpresaSelected, serie, folioErp, tipoServicio, fecha);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void limpiarFiltros() {
        serie = "";
        folioErp = "";
        fecha = null;
        idEmpresaSelected = -1;
        tipoServicio = "-1";
        buscarLogsApp();
    }


    /**
     * Valida la busqueda para el log de acceso
     *
     * @return
     */
    public boolean validarBusqueda1() {
        boolean res = true;
        if (!tipoBusqueda.equals("Ninguno") && !tipoBusqueda.equals("Todos")) {
            if (paramBusqueda == null || paramBusqueda.equals("".trim())) {
                res = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Falta especificar el parametro de busqueda.", "Error"));
            }
        }
        return res;
    }


    /**
     * @return the mAcceso
     */
    public MAcceso getmAcceso() {
        return mAcceso;
    }

    /**
     * @return the fecha
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @return the tipoBusqueda
     */
    public String getTipoBusqueda() {
        return tipoBusqueda;
    }

    /**
     * @return the paramBusqueda
     */
    public String getParamBusqueda() {
        return paramBusqueda;
    }

    /**
     * @param mAcceso the mAcceso to set
     */
    public void setmAcceso(MAcceso mAcceso) {
        this.mAcceso = mAcceso;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * @param tipoBusqueda the tipoBusqueda to set
     */
    public void setTipoBusqueda(String tipoBusqueda) {
        this.tipoBusqueda = tipoBusqueda;
    }

    /**
     * @param paramBusqueda the paramBusqueda to set
     */
    public void setParamBusqueda(String paramBusqueda) {
        this.paramBusqueda = paramBusqueda;
    }

    /**
     * @return the idEmpresaSelected
     */
    public int getIdEmpresaSelected() {
        return idEmpresaSelected;
    }

    /**
     * @param idEmpresaSelected the idEmpresaSelected to set
     */
    public void setIdEmpresaSelected(int idEmpresaSelected) {
        this.idEmpresaSelected = idEmpresaSelected;
    }

    public LazyDataModel<MLogApp> getListaLogApp() {
        return listaLogApp;
    }

    public void setListaLogApp(LazyDataModel<MLogApp> listaLogApp) {
        this.listaLogApp = listaLogApp;
    }

    /**
     * @return the logApp
     */
    public MLogApp getLogApp() {
        return logApp;
    }

    /**
     * @param logApp the logApp to set
     */
    public void setLogApp(MLogApp logApp) {
        this.logApp = logApp;
    }

    public String getSerie() {
        return this.serie;
    }

    public String getFolioErp() {
        return this.folioErp;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public void setFolioErp(String folioErp) {
        this.folioErp = folioErp;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }
}
