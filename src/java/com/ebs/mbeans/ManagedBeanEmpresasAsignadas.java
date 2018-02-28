/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.mbeans;

import fe.db.MAcceso;
import fe.db.MEmpresa;
import fe.model.dao.EmpresaDAO;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Eduardo C. Flores Ambrosio <Eduardo at EB&S>
 */
public class ManagedBeanEmpresasAsignadas implements Serializable{
    private List<MEmpresa> empresasAsignadas;
    private MEmpresa empresaSeleccionada;
    private int idEmpresa;
    private EmpresaDAO daoEmpresa;
    
    private MAcceso mAcceso;
    private final HttpServletRequest httpServletRequest;
    private final FacesContext faceContext;
    private String appContext;

    /**
     * Creates a new instance of ManagedBeanEmpresasAsignadas
     */
    public ManagedBeanEmpresasAsignadas() {
        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        mAcceso = (MAcceso)httpSession.getAttribute("macceso");
        appContext = httpServletRequest.getContextPath();
    }
    @PostConstruct
    public void init(){
        daoEmpresa = new EmpresaDAO();
    }
    /**
     * Cambia la empresa de la que se esta haciendo uso como parametro para obtener
     * los registros
     */
    public void cambiarEmpresaEnSesion(){
        //System.out.println("Cambiando de empresa en sesion");
        //System.out.println("IdEmpresa "+ idEmpresa);
        HttpSession httpSessionx = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        httpSessionx.removeAttribute("macceso");
        httpSessionx.removeAttribute("usernick");
        httpSessionx.invalidate();
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        empresaSeleccionada=daoEmpresa.BuscarEmpresaId(getIdEmpresa());
        if(empresaSeleccionada != null){
            mAcceso.setEmpresa(empresaSeleccionada);
            //System.out.println("Empresa Seleccionada"+empresaSeleccionada.getId()+"-"+empresaSeleccionada.getRazonSocial());
        }
        httpSessionx = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        httpSessionx.setAttribute("macceso", mAcceso);
        httpSessionx.setAttribute("usernick", mAcceso.getUsuario());
        httpSessionx.setAttribute("logeado", true);
        if(empresaSeleccionada != null){
            //System.out.println("Empresa id del objeto "+empresaSeleccionada.RFC_Empresa());
            httpSessionx.setAttribute("empresa", empresaSeleccionada);
        }
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/main/principal.xhtml");
        } catch (IOException ex) {
            System.out.println("IOException on cambiarEmpresaEnSesion()");
            Logger.getLogger(ManagedBeanEmpresasAsignadas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Obtiene todas las empresas padres
     * @return 
     */
    public List<MEmpresa> getEmpresasAsignadas() {
        empresasAsignadas = daoEmpresa.ListaEmpresasPadres(mAcceso.getId());
        if(empresasAsignadas == null){
            empresasAsignadas = new ArrayList();
        }
        return empresasAsignadas;
    }


    public MEmpresa getEmpresaSeleccionada() {
        return empresaSeleccionada;
    }

    public void setEmpresaSeleccionada(MEmpresa empresaSeleccionada) {
        //System.out.println("Set empresaSeleccionada"+empresaSeleccionada.RFC_Empresa());
        this.empresaSeleccionada = empresaSeleccionada;
    }

    public MAcceso getmAcceso() {
        return mAcceso;
    }

    public void setmAcceso(MAcceso mAcceso) {
        this.mAcceso = mAcceso;
    }

    /**
     * @param empresasAsignadas the empresasAsignadas to set
     */
    public void setEmpresasAsignadas(List<MEmpresa> empresasAsignadas) {
        this.empresasAsignadas = empresasAsignadas;
    }

    /**
     * @return the idEmpresa
     */
    public int getIdEmpresa() {
        return idEmpresa;
    }

    /**
     * @param idEmpresa the idEmpresa to set
     */
    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }
    
}
