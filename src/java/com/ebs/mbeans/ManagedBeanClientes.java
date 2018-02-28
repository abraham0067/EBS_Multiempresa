/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.mbeans;

import fe.db.*;
import fe.model.dao.*;

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
 * @author Eduardo C. Flores Ambrosio <Eduardo at EB&S>
 */
public class ManagedBeanClientes implements Serializable {

    //Modelos
    private MAcceso mAcceso;

    //Contexto
    private HttpServletRequest httpServletRequest;
    private FacesContext facesContext;
    private FacesMessage facesMessage;
    private String appContext;


    private int idEmpresaSelect;
    private String tipoBusqueda;
    private String paramBusqueda;
    private int currentOperation;
    private MDireccion direccion;
    private List<MReceptor> receptores;
    private List<MCliente> clientes;
    private MReceptor receptorSelected;
    private List<MEmpresa> empresas;
    //DAOS
    private EmpresaDAO daoEmpresa;
    private ReceptorDAO daoReceptor;
    private ClienteDAO daoCliente;
    private DireccionDAO daoDireccion;
    private LogAccesoDAO daoLog;

    /**
     * Creates a new instance of ManagedBeanClientes
     */
    public ManagedBeanClientes() {

    }

    @PostConstruct
    public void init() {
        facesContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        mAcceso = (MAcceso) httpSession.getAttribute("macceso");
        appContext = httpServletRequest.getContextPath();
        daoLog = new LogAccesoDAO();
        daoDireccion = new DireccionDAO();
        daoReceptor = new ReceptorDAO();
        daoCliente = new ClienteDAO();
        daoEmpresa = new EmpresaDAO();
        paramBusqueda = "";
        tipoBusqueda = "Ninguno";
        idEmpresaSelect = -1;
    }

    public void buscarClientes() {
        if(daoReceptor.BusquedaRFC().equals("BMS030731PC4"))
            clientes = daoCliente.BusquedaParam(mAcceso.getId(), idEmpresaSelect, tipoBusqueda, paramBusqueda);
        else
            receptores = daoReceptor.BusquedaParam(mAcceso.getId(), idEmpresaSelect, tipoBusqueda, paramBusqueda);

        empresas = daoEmpresa.ListaEmpresasPadres(mAcceso.getId());
    }

    public void redirectCreacionCliente() {
        currentOperation = 0;
        resetC();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/admin/catalogos/cliente.xhtml");
        } catch (IOException e1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "A ocurrido un error al redireccionar.", "Error"));
        }

    }

    public void redirectModificarCliente() {
        currentOperation = 1;
        if (receptorSelected != null) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/admin/catalogos/cliente.xhtml");
            } catch (IOException e1) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "A ocurrido un error al redireccionar.", "Error"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Debes seleccionar un receptor primero.", "Error"));
        }
    }

    public void redirectConsultaClientes() {
        try {
            idEmpresaSelect = -1;
            receptorSelected = null;
            buscarClientes();
            FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/admin/catalogos/catClientes.xhtml");
        } catch (IOException e1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "A ocurrido un error al redireccionar.", "Error"));
        }

    }

    public void borrarCliente() {
        if (daoReceptor.BorrarReceptor(receptorSelected)) {
            if (receptorSelected.getDireccion() != null) {
                daoDireccion.BorrarDireccion(receptorSelected.getDireccion());
                receptores.remove(receptorSelected);
            }
            daoLog.guardaRegistro(mAcceso, "Borro al receptor: " + receptorSelected.getRfcOrigen() + " - " + receptorSelected.getRazonSocial());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "El receptor ha sido eliminado de forma correcta.", "Error"));
        }

    }

    public void guardarCambiosCliente() {
        //Clean the strings on class
        if (currentOperation == 0) {
            receptorSelected.setEmpresa(daoEmpresa.BuscarEmpresaId(idEmpresaSelect));
            daoDireccion.GuardarActualizaDireccion(receptorSelected.getDireccion());
            if (daoReceptor.GuardarActualizar(receptorSelected)) {
                daoLog.guardaRegistro(mAcceso,
                        "Registro al receptorSelected.setEmpresa(daoEmpresa.BuscarEmpresaId(idEmpresaSelect))receptor: " +
                                receptorSelected.getRfcOrigen().trim().toUpperCase() + " - " + receptorSelected.getRazonSocial());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "El receptor se registro correctamente.", "Error"));
                resetC();
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "A ocurrido un error al intentar registrar el receptor.", "Error"));
            }

        } else if (currentOperation == 1) {
            daoDireccion.GuardarActualizaDireccion(receptorSelected.getDireccion());
            if (daoReceptor.GuardarActualizar(receptorSelected)) {
                daoLog.guardaRegistro(mAcceso, "Modifico al receptor: " + receptorSelected.getRfcOrigen().trim().toUpperCase() + " - " + receptorSelected.getRazonSocial());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "El receptor se modifico correctamente.", "Error"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "A ocurrido un error al intentar modificar el receptor.", "Error"));
            }
        }
    }

    public void resetC() {
        receptorSelected = null;
        receptorSelected = new MReceptor();
        receptorSelected.setDireccion(new MDireccion());
        idEmpresaSelect = -1;
    }

    /**
     * @return the mAcceso
     */
    public MAcceso getmAcceso() {
        return mAcceso;
    }

    /**
     * @return the facesMessage
     */
    public FacesMessage getFacesMessage() {
        return facesMessage;
    }

    /**
     * @return the currentOperation
     */
    public int isCurrentOperation() {
        return currentOperation;
    }

    /**
     * @param mAcceso the mAcceso to set
     */
    public void setmAcceso(MAcceso mAcceso) {
        this.mAcceso = mAcceso;
    }

    /**
     * @param facesMessage the facesMessage to set
     */
    public void setFacesMessage(FacesMessage facesMessage) {
        this.facesMessage = facesMessage;
    }

    /**
     * @param currentOperation the currentOperation to set
     */
    public void setCurrentOperation(int currentOperation) {
        this.currentOperation = currentOperation;
    }

    /**
     * @return the receptores
     */
    public List<MReceptor> getReceptores() {
        return receptores;
    }

    /**
     * @return the empresas
     */
    public List<MEmpresa> getEmpresas() {
        return empresas;
    }

    /**
     * @param receptores the receptores to set
     */
    public void setReceptores(List<MReceptor> receptores) {
        this.receptores = receptores;
    }

    /**
     * @param empresas the empresas to set
     */
    public void setEmpresas(List<MEmpresa> empresas) {
        this.empresas = empresas;
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

    /**
     * @return the idEmpresaSelect
     */
    public int getIdEmpresaSelect() {
        return idEmpresaSelect;
    }

    /**
     * @param idEmpresaSelect the idEmpresaSelect to set
     */
    public void setIdEmpresaSelect(int idEmpresaSelect) {
        this.idEmpresaSelect = idEmpresaSelect;
    }

    /**
     * @return the receptorSelected
     */
    public MReceptor getReceptorSelected() {
        return receptorSelected;
    }

    /**
     * @param receptorSelected the receptorSelected to set
     */
    public void setReceptorSelected(MReceptor receptorSelected) {
        this.receptorSelected = receptorSelected;
    }

    /**
     * @return the currentOperation
     */
    public int getCurrentOperation() {
        return currentOperation;
    }

    /**
     * @return the direccion
     */
    public MDireccion getDireccion() {
        return direccion;
    }

    /**
     * @param direccion the direccion to set
     */
    public void setDireccion(MDireccion direccion) {
        this.direccion = direccion;
    }



    public List<MCliente> getClientes() { return clientes;}

    public void setClientes(List<MCliente> clientes) { this.clientes = clientes; }
}
