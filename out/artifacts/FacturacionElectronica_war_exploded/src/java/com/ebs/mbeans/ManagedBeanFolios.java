/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.mbeans;

import fe.db.MAcceso;
import fe.db.MFolios;
import fe.model.dao.EmpresaDAO;
import fe.model.dao.FoliosDAO;
import fe.model.dao.LogAccesoDAO;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 *
 * @author Eduardo C. Flores Ambrosio <Eduardo at EB&S>
 */
public class ManagedBeanFolios implements Serializable {

    private MAcceso mAcceso;
    private int idEmpresaSelect;//som 

    private String tipoBusqueda;
    private String paramBusqueda;
    //Contexto
    private HttpServletRequest httpServletRequest;
    private FacesContext facesContext;
    private String appContext;


    private List<MFolios> folios;
    private MFolios folio;

    //DAOS
    private EmpresaDAO daoEmp;
    private FoliosDAO daoFolio;
    private LogAccesoDAO daoLog;

    private int currentOperation;

    /**
     * Creates a new instance of ManagedBeanFolios
     */
    public ManagedBeanFolios() {

    }

    @PostConstruct
    public void init() {
        facesContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        mAcceso = (MAcceso) httpSession.getAttribute("macceso");
        appContext = httpServletRequest.getContextPath();
        daoEmp = new EmpresaDAO();
        daoFolio = new FoliosDAO();
        daoLog = new LogAccesoDAO();
        //folios = daoFolio.BusquedaParam(mAcceso.getId(), idEmpresaSelect, tipoBusqueda, paramBusqueda);
    }

    public void buscarFolios() {
        if(validaBusqueda()){
            folios = daoFolio.BusquedaParam(mAcceso.getId(), idEmpresaSelect, tipoBusqueda, paramBusqueda);
        }
    }

    public void borrarFolio() {
        if (folio != null) {
            if (daoFolio.BorrarFolio(folio)) {
                daoLog.guardaRegistro(mAcceso, "Elimino el folio: " + folio.getSerie());
                folios.clear();
                folios = daoFolio.ListaFoliosxUser(mAcceso.getId());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "El folio se elimino correctamente.", "Error"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debes seleccionar un folio.", "Error"));
        }
    }

    public void redirectConsultaFolios() {
        try {
            folio = null;
            FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/admin/catalogos/catFolios.xhtml");
        } catch (IOException e1) {
            System.out.println(">>>>>>>>>>IOException on redirectConsultarEmpresas()" + e1.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "A ocurrido un error al redireccionar.", "Error"));
        }
    }

    public void redirectModificacionFolio() {
        if (folio != null) {
            currentOperation = 1;
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/admin/catalogos/folio.xhtml");
            } catch (IOException e1) {
                System.out.println(">>>>>>>>>>IOException on redirectConsultarEmpresas()" + e1.getMessage());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "A ocurrido un error al redireccionar.", "Error"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Debes seleccionar un folio para su modificación.", "Error"));
        }
    }

    public void redirectCreacionFolio() {
        currentOperation = 0;
        resetC();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/admin/catalogos/folio.xhtml");
        } catch (IOException e1) {
            System.out.println(">>>>>>>>>>IOException on redirectConsultarEmpresas()" + e1.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "A ocurrido un error al redireccionar.", "Error"));
        }
    }

    public void guardarFolio() {
//        this.folio.escapeSqlAndHtmlCharacters();
        if (currentOperation == 0) {
            if (validar()) {
                MFolios folioAnterior = daoFolio.BuscarFolioSerieIdempresa(folio.getSerie(), idEmpresaSelect);
                if (folioAnterior != null) {
                    folioAnterior.setEstatus(2);
                    daoFolio.GuardarActualizarFolio(folioAnterior);
                }
                folio.setEmpresa(daoEmp.BuscarEmpresaId(idEmpresaSelect));
                if (daoFolio.GuardarActualizarFolio(folio)) {
                    daoLog.guardaRegistro(mAcceso, "Creo el folio: " + folio.getSerie() + " id: " + folio.getIdFolio());
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "El folio se registro correctamente.", "Error"));
                    folios = daoFolio.ListaFoliosxUser(mAcceso.getId());
                }
            }
        } else if (currentOperation == 1) {
            if (validar()) {
                if (daoFolio.GuardarActualizarFolio(folio)) {
                    daoLog.guardaRegistro(mAcceso, "Modifico el folio: " + folio.getSerie() + " id: " + folio.getIdFolio());
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "El folio se modifico correctamente.", "Error"));
                    folios = daoFolio.ListaFoliosxUser(mAcceso.getId());
                }
            }

        }
    }

    public void resetC() {
        idEmpresaSelect = -1;
        folio = null;
        folio = new MFolios();
    }

    public boolean validar() {
        boolean res = true;
        if (folio.getRango().intValue() < 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Es necesario insertar el rango.", "Error"));
            res = false;
        } else if (folio.getAsignados() < 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "El numero de asignados debe ser mayor o igual a 0 .", "Error"));
            res = false;
        }
        return res;
    }
    public boolean validaBusqueda(){
        boolean res = true;
        if(tipoBusqueda != null && tipoBusqueda.equals("Ninguno") && idEmpresaSelect <= 0){
            res = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falta algún parametro de busqueda .", "Error"));
        }
        if(!tipoBusqueda.equals("Todos") && !tipoBusqueda.equals("Ninguno")){
            if(paramBusqueda != null && paramBusqueda.equals("")){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falta el parametro de busqueda .", "Error"));
                res = false;
            }
        }
        return res;
    }
    /**
     * @return the folios
     */
    public List<MFolios> getFolios() {
        return folios;
    }

    /**
     * @param folios the folios to set
     */
    public void setFolios(List<MFolios> folios) {
        this.folios = folios;
    }

    /**
     * @return the mAcceso
     */
    public MAcceso getmAcceso() {
        return mAcceso;
    }

    /**
     * @return the idEmpresaSelect
     */
    public int getIdEmpresaSelect() {
        return idEmpresaSelect;
    }

    /**
     * @param mAcceso the mAcceso to set
     */
    public void setmAcceso(MAcceso mAcceso) {
        this.mAcceso = mAcceso;
    }

    /**
     * @param idEmpresaSelect the idEmpresaSelect to set
     */
    public void setIdEmpresaSelect(int idEmpresaSelect) {
        this.idEmpresaSelect = idEmpresaSelect;
    }

    /**
     * @return the folio
     */
    public MFolios getFolio() {
        return folio;
    }

    /**
     * @param folio the folio to set
     */
    public void setFolio(MFolios folio) {
        this.folio = folio;
    }

    /**
     * @return the currentOperation
     */
    public int getCurrentOperation() {
        return currentOperation;
    }

    /**
     * @param currentOperation the currentOperation to set
     */
    public void setCurrentOperation(int currentOperation) {
        this.currentOperation = currentOperation;
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
}
