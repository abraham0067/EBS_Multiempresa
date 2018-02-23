/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.mbeans;

import fe.db.MAcceso;
import fe.db.MPac;
import fe.model.dao.EmpresaDAO;
import fe.model.dao.LogAccesoDAO;
import fe.model.dao.PacDAO;
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
public class ManagedBeanPacs implements Serializable {

    private MAcceso mAcceso;
    //Contexto
    private HttpServletRequest httpServletRequest;
    private FacesContext facesContext;
    private String appContext;

    private int currentOperation;//0-Creacion 1-Modificacion

    private List<MPac> pacs;
    private MPac pac;
    private LogAccesoDAO daoLog;
    private EmpresaDAO daoEmp;
    private PacDAO daoPac;

    /**
     * Creates a new instance of ManagedBeanPacs
     */
    public ManagedBeanPacs() {
    }

    @PostConstruct
    public void init() {
        facesContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        mAcceso = (MAcceso) httpSession.getAttribute("macceso");
        appContext = httpServletRequest.getContextPath();
        daoLog = new LogAccesoDAO();
        daoEmp = new EmpresaDAO();
        daoPac = new PacDAO();
        this.buscarPacs();
    }

    /**
     * Busca los pacs registrados
     */
    public void buscarPacs() {
        pacs = daoPac.ListaPac();
    }

    /**
     * Redirecciona a la pagina de modificacion de un pac
     */
    public void redirectModicarPac() {
        if (pac != null) {
            try {
                currentOperation = 1;
                FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/admin/catalogos/pac.xhtml");
            } catch (IOException e1) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "A ocurrido un error al redireccionar.", ""));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Seleccione un PAC.", ""));
        }
    }

    /**
     * Redirecciona a la pagina de registro de un pac
     */
    public void redirectRegistrarPac() {
        try {
            currentOperation = 0;
            resetC();
            FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/admin/catalogos/pac.xhtml");
        } catch (IOException e1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "A ocurrido un error al redireccionar.", ""));
        }
    }

    /**
     * Redirecciona a la pagina de consulta de pacs
     */
    public void redirectConsultaPacs() {
        try {
            this.buscarPacs();
            pac = null;
            FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/admin/catalogos/catPacs.xhtml");
        } catch (IOException e1) {
            e1.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "A ocurrido un error al redireccionar.", ""));
        }

    }

    /**
     * Limpia datos
     */
    public void resetC() {
        pac = null;
        pac = new MPac();
    }

    /**
     * Guarda un nuevo o actualiza un pac existente
     */
    public void guardarPac() {
//        this.pac.escapeSqlAndHtmlCharacters();
        if (currentOperation == 0) {
            if (pac != null && daoPac.GuardarOActualizar(pac)) {
                try {
                    daoLog.comienzaAccion(mAcceso, "Se registro el pac= " + pac.getNombre());
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "El pac se ha registrado correctamente.", ""));
                    FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                    redirectConsultaPacs();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo registrar el pac.", ""));
            }
        } else if (currentOperation == 1) {
            if (pac != null && daoPac.GuardarOActualizar(pac)) {
                try {
                    daoLog.comienzaAccion(mAcceso, "Se modifico el pac= " + pac.getNombre());
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "El pac se ha modificado correctamente.", ""));
                    FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                    redirectConsultaPacs();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo modificar el pac.", ""));
            }
        }
    }

    /**
     * Elimina un pac
     */
    public void borrarPac() {
        if (pac != null) {
            if (daoPac.BorrarObjeto(pac)) {
                daoLog.guardaRegistro(mAcceso, "Borror el pac:" + pac.getNombre());
                this.buscarPacs();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "El pac se elimino correctamente.", ""));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "No se pudo eliminar el PAC.", ""));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Debe seleccionar un pac.", "Error"));
        }
    }

    /**
     * @return the mAcceso
     */
    public MAcceso getmAcceso() {
        return mAcceso;
    }

    /**
     * @return the currentOperation
     */
    public int getCurrentOperation() {
        return currentOperation;
    }

    /**
     * @param mAcceso the mAcceso to set
     */
    public void setmAcceso(MAcceso mAcceso) {
        this.mAcceso = mAcceso;
    }

    /**
     * @param currentOperation the currentOperation to set
     */
    public void setCurrentOperation(int currentOperation) {
        this.currentOperation = currentOperation;
    }

    /**
     * @return the pacs
     */
    public List<MPac> getPacs() {
        return pacs;
    }

    /**
     * @param pacs the pacs to set
     */
    public void setPacs(List<MPac> pacs) {
        this.pacs = pacs;
    }

    /**
     * @return the pac
     */
    public MPac getPac() {
        return pac;
    }

    /**
     * @param pac the pac to set
     */
    public void setPac(MPac pac) {
        this.pac = pac;
    }

}
