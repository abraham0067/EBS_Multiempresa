package com.ebs.mbeans;

import fe.db.MAcceso;
import fe.db.MEmpresa;
import fe.db.MCliente;
import fe.model.dao.EmpresaDAO;
import fe.model.dao.LogAccesoDAO;
import fe.model.dao.ClienteDAO;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.CellEditEvent;

public class ManagedBeanAgentes implements Serializable {

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
    private List<MCliente> clientes;
    private MCliente clienteSelected;
    private List<MEmpresa> empresas;
    //DAOS
    private EmpresaDAO daoEmpresa;
    private ClienteDAO daoCliente;
    private LogAccesoDAO daoLog;

    /**
     * Creates a new instance of ManagedBeanAgentes
     */
    public ManagedBeanAgentes() {

    }

    @PostConstruct
    public void init() {
        facesContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        mAcceso = (MAcceso) httpSession.getAttribute("macceso");
        appContext = httpServletRequest.getContextPath();
        daoLog = new LogAccesoDAO();
        daoCliente = new ClienteDAO();
        daoEmpresa = new EmpresaDAO();
        paramBusqueda = "";
        tipoBusqueda = "Ninguno";
        idEmpresaSelect = -1;
    }

    public void buscarAgentes() {
        empresas = daoEmpresa.ListaEmpresasPadres(mAcceso.getId());
        if(idEmpresaSelect >= 0)
            clientes = daoCliente.BusquedaParam(mAcceso.getId(), idEmpresaSelect, tipoBusqueda.trim(), paramBusqueda.trim());
        else
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe seleccionar primero la empresa", ""));
    }

    public void redirectCreacionAgente() {
        currentOperation = 0;
        resetC();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/admin/catalogos/agente.xhtml");
        } catch (IOException e1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "A ocurrido un error al redireccionar.", "Error"));
        }

    }

    public void redirectModificarAgente() {
        currentOperation = 1;
        if (clienteSelected != null) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/admin/catalogos/agente.xhtml");
            } catch (IOException e1) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "A ocurrido un error al redireccionar.", "Error"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Debes seleccionar un receptor primero.", "Error"));
        }
    }

    public void redirectConsultaAgentes() {
        try {
            idEmpresaSelect = -1;
            clienteSelected = null;
            clientes = null;
            buscarAgentes();
            FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/admin/catalogos/catAgentes.xhtml");
        } catch (IOException e1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "A ocurrido un error al redireccionar.", "Error"));
        }

    }

    public void borrarAgente() {
        if (daoCliente.BorrarCliente(clienteSelected)) {
            daoLog.guardaRegistro(mAcceso, "Borro al receptor: " + clienteSelected.getRfc()+ " - " + clienteSelected.getRazonSocial());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "El receptor ha sido eliminado de forma correcta.", ""));
        }

    }

    public void guardarCambiosAgente() {
        //Clean the strings on class
        if (currentOperation == 0) {
            if(daoCliente.BuscarNoCliente(idEmpresaSelect, clienteSelected.getNoCliente())){
                clienteSelected.setEmpresa(daoEmpresa.BuscarEmpresaId(idEmpresaSelect));
                if (daoCliente.GuardarActualizar(clienteSelected)) {
                    daoLog.guardaRegistro(mAcceso,
                            "Registro al clienteSelected.setEmpresa(daoEmpresa.BuscarEmpresaId(idEmpresaSelect))receptor: " +
                                    clienteSelected.getRfc().trim().toUpperCase() + " - " + clienteSelected.getRazonSocial());
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "El agente se registro correctamente.", "Error"));
                    resetC();
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "A ocurrido un error al intentar registrar el agente.", "Error"));
                }
            }else
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "El numero de cliente proporcionado ya se encuentra en uso.", ""));

        } else if (currentOperation == 1) {
            System.out.println("GUARDA2");
            if (daoCliente.GuardarActualizar(clienteSelected)) {
                daoLog.guardaRegistro(mAcceso, "Modifico al receptor: " + clienteSelected.getRfc().trim().toUpperCase() + " - " + clienteSelected.getRazonSocial());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "El agente se modifico correctamente.", "Error"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "A ocurrido un error al intentar modificar el agente.", "Error"));
            }
        }
    }

    private void resetC() {
        clienteSelected = null;
        clienteSelected = new MCliente();
        idEmpresaSelect = -1;
    }

    public void onRowEdit(RowEditEvent event) {
        MCliente cliente = (MCliente) event.getObject();
        //ALMACENA EL CAMBIO
        daoCliente.GuardarActualizar(cliente);

        FacesMessage msg = new FacesMessage("Agente Editado", cliente.getNoCliente());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edicion Cancelada",  ((MCliente) event.getObject()).getNoCliente());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if(newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Atributo cambiado", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
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
     * @return the clientes
     */
    public List<MCliente> getClientes() {
        return clientes;
    }

    /**
     * @return the empresas
     */
    public List<MEmpresa> getEmpresas() {
        return empresas;
    }

    /**
     * @param clientes the clientes to set
     */
    public void setReceptores(List<MCliente> clientes) {
        this.clientes = clientes;
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
     * @return the clienteSelected
     */
    public MCliente getClienteSelected() {
        return clienteSelected;
    }

    /**
     * @param clienteSelected the clienteSelected to set
     */
    public void setClienteSelected(MCliente clienteSelected) {
        this.clienteSelected = clienteSelected;
    }

    /**
     * @return the currentOperation
     */
    public int getCurrentOperation() {
        return currentOperation;
    }



}
