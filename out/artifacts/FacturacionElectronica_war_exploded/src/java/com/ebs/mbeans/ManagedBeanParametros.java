/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.mbeans;

import fe.db.MAcceso;
import fe.db.MConfig;
import fe.model.dao.ConfigDAO;
import fe.model.dao.LogAccesoDAO;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
/**
 *
 * @author Eduardo C. Flores Ambrosio <Eduardo at EB&S>
 */
public class ManagedBeanParametros implements Serializable {

    private MAcceso mAcceso;
    //Contexto
    private HttpServletRequest httpServletRequest;
    private FacesContext facesContext;
    private String appContext;


    private List<MConfig> listaParametros;    
    private MConfig configSelected;
    private ConfigDAO daoConfig;
    private LogAccesoDAO daoLog;
    private int currentOperation = -1;
    /*
     * Creates a new instance of ManagedBeanParametros
     */

    public ManagedBeanParametros() {
    }

    @PostConstruct
    public void init() {
        facesContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        mAcceso = (MAcceso) httpSession.getAttribute("macceso");
        appContext = httpServletRequest.getContextPath();
        daoLog = new LogAccesoDAO();
        daoConfig = new ConfigDAO();
        this.buscarParametros();
    }

    public void buscarParametros() {
        this.listaParametros = daoConfig.ListaParametro();
    }
    /**
     * Redirecciona a la pagina de creacion de un nuevo parametro
     */
    public void redirectNuevoParametro() {
        currentOperation = 0;
        this.reset();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/admin/parametros/parametro.xhtml");
        } catch (IOException e1) {
            e1.printStackTrace();
            FacesContext.getCurrentInstance().addMessage("frmControls", new FacesMessage(FacesMessage.SEVERITY_ERROR, "A ocurrido un error al redireccionar.", "Error"));
        }
    }
    /**
     * Redirecciona a la pagina de modificacion de un parametro
     */
    public void redirectModificacionParametro() {
        currentOperation = 1;
        if (configSelected != null) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/admin/parametros/parametro.xhtml");
            } catch (IOException e1) {
                e1.printStackTrace();
                FacesContext.getCurrentInstance().addMessage("frmControls", new FacesMessage(FacesMessage.SEVERITY_ERROR, "A ocurrido un error al redireccionar.", "Error"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage("frmControls", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Primero selecciona un parametro.", "Error"));
        }
    }
    /**
     * Redirecciona a la pagina de consulta de todos los parametros disponibles
     */
    public void redirectConsultaParametros() {
        currentOperation = -1;
        configSelected = null;
        try {
            buscarParametros();
            FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/admin/parametros/adminParametros.xhtml");
        } catch (IOException e1) {
            e1.printStackTrace();
            FacesContext.getCurrentInstance().addMessage("frmControls", new FacesMessage(FacesMessage.SEVERITY_ERROR, "A ocurrido un error al redireccionar.", "Error"));
        }
    }
    
    /**
     * Limpia los datos
     */
    public void reset() {
        configSelected = null;
        configSelected = new MConfig();
    }
    
    /**
     * Realiza las operaciones de guardado o modificacion segun corresponda
     */
    public void guardarParametro() {
//        this.configSelected.escapeSqlAndHtmlCharacters();
        if (currentOperation == 0) {
            if (daoConfig.GuardarActualizaMconfig(configSelected)) {
                daoLog.guardaRegistro(mAcceso, "Registro el Parametro " + configSelected.getDato() + ", clasificación " + configSelected.getClasificacion());
                this.buscarParametros();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "El parametro se creo correctamente.", "Error"));
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                this.redirectConsultaParametros();
            } else {
                FacesContext.getCurrentInstance().addMessage("frmControls", new FacesMessage(FacesMessage.SEVERITY_ERROR, "A ocurrido un error al intentar modificar el parametro.", "Error"));
            }
        } else if (currentOperation == 1) {
            if (daoConfig.GuardarActualizaMconfig(configSelected)) {
                daoLog.guardaRegistro(mAcceso, "Modifico el Parametro " + configSelected.getDato() + ", clasificación " + configSelected.getClasificacion());
                FacesContext.getCurrentInstance().addMessage("frmControls", new FacesMessage(FacesMessage.SEVERITY_INFO, "El parametro se modifico correctamente.", "Error"));
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                this.redirectConsultaParametros();
            } else {
                FacesContext.getCurrentInstance().addMessage("frmControls", new FacesMessage(FacesMessage.SEVERITY_ERROR, "A ocurrido un error al intentar modificar el parametro.", "Error"));
            }
        }
    }
    
    /**
     * Borra el parametro seleccionado
     */
    public void borrarParametro() {
        if (configSelected != null) {
            if (daoConfig.BorrarConfig(configSelected)) {
                daoLog.guardaRegistro(mAcceso, "Borro el parametro: " + configSelected.getDato() + " clasificación:" + configSelected.getClasificacion());
                listaParametros.remove(configSelected);
                configSelected = null;
                FacesContext.getCurrentInstance().addMessage("frmControls", new FacesMessage(FacesMessage.SEVERITY_INFO, "El parametro ha sido eliminado.", "Error"));
            } else {
                FacesContext.getCurrentInstance().addMessage("frmControls", new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo eliminar el parametro.", "Error"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage("frmControls", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe seleccionar un parametro.", "Error"));
        }
    }    
    /**
     * @return the listaParametros
     */
    public List<MConfig> getListaParametros() {
        return listaParametros;
    }

    /**
     * @param listaParametros the listaParametros to set
     */
    public void setListaParametros(List<MConfig> listaParametros) {
        this.listaParametros = listaParametros;
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
     * @return the configSelected
     */
    public MConfig getConfigSelected() {
        return configSelected;
    }

    /**
     * @param configSelected the configSelected to set
     */
    public void setConfigSelected(MConfig configSelected) {
        this.configSelected = configSelected;
    }

    /**
     * @return the mAcceso
     */
    public MAcceso getmAcceso() {
        return mAcceso;
    }

    /**
     * @param mAcceso the mAcceso to set
     */
    public void setmAcceso(MAcceso mAcceso) {
        this.mAcceso = mAcceso;
    }

}
