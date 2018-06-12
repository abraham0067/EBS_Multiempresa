/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.mbeans;

import fe.db.MAcceso;
import fe.db.MEmpresa;
import fe.db.MPac;
import fe.db.MPacMEmpresa;
import fe.model.dao.EmpresaDAO;
import fe.model.dao.LogAccesoDAO;
import fe.model.dao.PacDAO;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Eduardo C. Flores Ambrosio <Eduardo at EB&S>
 */
public class ManagedBeanPacEmpresas implements Serializable {

    private static final int ESTATUS_DESACTIVADO = 0;
    private static final int ESTATUS_ACTIVADO = 1;
    private static final int ESTATUS_NO_INCLUIR_EN_RULETA = 2;

    private MAcceso mAcceso;
    private int idEmpresaUsuario;//Sesion
    private int idEmpresaCreation;//som
    private int idPacSelected;//som
    //Contexto
    private HttpServletRequest httpServletRequest;
    private FacesContext facesContext;
    private String appContext;


    private int idEmpresaSelected = -1;
    //Modelos
    private List<MPacMEmpresa> listPacsEmpresa;
    private MPacMEmpresa pacEmpresaSelected;
    private List<MEmpresa> empresas;
    private List<MPac> pacs;
    private int currentOperation = -1;
    private boolean booStatus = false;
    private LogAccesoDAO daoLog;
    private EmpresaDAO daoEmp;
    private PacDAO daoPac;

    /**
     * Creates a new instance of ManagedBeanPacEmpresas
     */
    public ManagedBeanPacEmpresas() {

    }

    @PostConstruct
    public void init() {
        facesContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        mAcceso = (MAcceso) httpSession.getAttribute("macceso");
        idEmpresaUsuario = mAcceso.getEmpresa().getId();
        appContext = httpServletRequest.getContextPath();
        daoLog = new LogAccesoDAO();
        daoEmp = new EmpresaDAO();
        daoPac = new PacDAO();
        pacEmpresaSelected = new MPacMEmpresa();
        //PRECARGA DE DATOS
        ///this.listPacsEmpresa = daoPac.ListaPacEmpresasId(idEmpresaUsuario);
    }

    /**
     * Redirecciona a la pagina de asignacion de pacs
     */
    public void redirectCreacionPacEmp() {
        currentOperation = 0;
        resetC();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/admin/pacemp/pacEmpresa.xhtml");
        } catch (IOException e1) {
            e1.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "A ocurrido un error al redireccionar.", "Error"));
        }
    }

    /**
     * Redirecciona a la pagina de modificacion de pacEmpresas
     */
    public void redirectModificacionPacEmp() {
        if (pacEmpresaSelected != null) {
            currentOperation = 1;
            if (pacEmpresaSelected.getEstatus() == ESTATUS_DESACTIVADO || pacEmpresaSelected.getEstatus() == ESTATUS_NO_INCLUIR_EN_RULETA) {
                booStatus = false;
            } else {
                booStatus = true;
            }

            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/admin/pacemp/pacEmpresa.xhtml");
            } catch (IOException e1) {
                e1.printStackTrace();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "A ocurrido un error al redireccionar.", "Error"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Debe seleccionar un PAC.", "Error"));
        }
    }

    /**
     * Redirecciona a la pagoma de cpmsultas
     */
    public void redirectConsulta() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/admin/pacemp/adminPacEmpresa.xhtml");
        } catch (IOException e1) {
            e1.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "A ocurrido un error al redireccionar.", "Error"));
        }

    }

    public void buscarPacs() {
        pacEmpresaSelected = null;
        if(idEmpresaSelected>0){
            this.listPacsEmpresa = daoPac.ListaPacEmpresasId(idEmpresaSelected);
        } else {
            this.listPacsEmpresa = new ArrayList<>();
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Por favor seleccione una empresa.", "Info"));
        }
    }

    public void eliminarPac() {
        if (pacEmpresaSelected != null) {
            if(pacEmpresaSelected.getEstatus() != ESTATUS_ACTIVADO) {
                if (daoPac.BorrarObjeto(pacEmpresaSelected)) {
                    daoLog.guardaRegistro(mAcceso, "Borror el pac: " + pacEmpresaSelected.getUsuario() + " de la empresa: "
                            + pacEmpresaSelected.getEmpresa().getRfcOrigen());
                    this.listPacsEmpresa.remove(pacEmpresaSelected);
                    pacEmpresaSelected = null;
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "El PAC se ha eliminado correctamente.", "Error"));
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "No se pudo eliminar el PAC.", "Error"));
                }
            } else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "No se puede eliminar un PAC activo.", "Error"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Debes seleccionar un PAC para eliminar.", "Error"));
        }
    }

    public void activarPACSeleccionado(){
        if (pacEmpresaSelected != null && idEmpresaSelected > 0) {
            List<MPacMEmpresa> pacs = daoPac.ListaPacEmpresasId(idEmpresaSelected);
            for (MPacMEmpresa tmp : pacs) {
                if(tmp.getId() != pacEmpresaSelected.getId()){
                    tmp.setEstatus(ESTATUS_NO_INCLUIR_EN_RULETA);
                    daoPac.actualizarObjeto(tmp);
                } else {
                    pacEmpresaSelected.setEstatus(ESTATUS_ACTIVADO);
                    daoPac.actualizarObjeto(pacEmpresaSelected);
                }
            }
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "El PAC ha sido activado, el resto de los PACS fueron desactivados(Solo para la empresa seleccionada).", "Info"));
            buscarPacs();
        } else{
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Debes seleccionar un PAC.", "Info"));
        }
    }

    public void resetC() {       
        this.idEmpresaCreation = -1;
        this.idPacSelected = -1;
        this.booStatus = false;
        pacEmpresaSelected = null;
        pacEmpresaSelected = new MPacMEmpresa();
        empresas= daoEmp.ListaEmpresasPadres(mAcceso.getId());
        pacs= daoPac.ListaPac();
    }

    /**
     *
     */
    public void guardarPacEmpresa() {
        if (currentOperation == 0) {
            if (validarCreacion()) {

                pacEmpresaSelected.setEmpresa(daoEmp.BuscarEmpresaId(idEmpresaCreation));
                pacEmpresaSelected.setPac(daoPac.BuscarPacId(idPacSelected));
                pacEmpresaSelected.setEstatus(ESTATUS_NO_INCLUIR_EN_RULETA);
                if (pacEmpresaSelected != null) {
                    if (daoPac.GuardarOActualizar(pacEmpresaSelected)) {
                        try {
                            daoLog.comienzaAccion(mAcceso, "Se registro el pac_Empresa= " +
                                    pacEmpresaSelected.getUsuario() + " para la empresa " + pacEmpresaSelected.getEmpresa().RFC_Empresa());
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "El PAC fue registrado correctamente.", "Error"));
                            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                            buscarPacs();
                            redirectConsulta();
                        } catch (Exception e1) {
                            e1.printStackTrace(System.out);
                        }
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "No se pudo hacer el registro.", "Error"));
                    }
                }
            }
            //Modificacion
        } else if (currentOperation == 1) {
            if (daoPac.GuardarOActualizar(pacEmpresaSelected)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "El PAC se ha guardado correctamente.", "Error"));
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                buscarPacs();
                redirectConsulta();
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "No se han podido realizar los cambios.", "Error"));
            }

        }

    }

    public boolean validarCreacion() {
        boolean res = true;
        MPacMEmpresa tmp = daoPac.BuscarEmpidPacid(idEmpresaCreation, idPacSelected);
        if (tmp != null) {
            res = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Ya existe un registro con estos datos.", "Error"));
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
     * @return the idEmpresaUsuario
     */
    public int getIdEmpresaUsuario() {
        return idEmpresaUsuario;
    }

    /**
     * @return the idEmpresaCreation
     */
    public int getIdEmpresaCreation() {
        return idEmpresaCreation;
    }

    /**
     * @return the listPacsEmpresa
     */
    public List<MPacMEmpresa> getListPacsEmpresa() {
        return listPacsEmpresa;
    }


    /**
     * @param mAcceso the mAcceso to set
     */
    public void setmAcceso(MAcceso mAcceso) {
        this.mAcceso = mAcceso;
    }

    /**
     * @param idEmpresaUsuario the idEmpresaUsuario to set
     */
    public void setIdEmpresaUsuario(int idEmpresaUsuario) {
        this.idEmpresaUsuario = idEmpresaUsuario;
    }

    /**
     * @param idEmpresaCreation the idEmpresaCreation to set
     */
    public void setIdEmpresaCreation(int idEmpresaCreation) {
        this.idEmpresaCreation = idEmpresaCreation;
    }

    /**
     * @param listPacsEmpresa the listPacsEmpresa to set
     */
    public void setListPacsEmpresa(List<MPacMEmpresa> listPacsEmpresa) {
        this.listPacsEmpresa = listPacsEmpresa;
    }

    /**
     * @return the empresas
     */
    public List<MEmpresa> getEmpresas() {
        return empresas;
    }

    /**
     * @return the pacs
     */
    public List<MPac> getPacs() {
        return pacs;
    }

    /**
     * @param empresas the empresas to set
     */
    public void setEmpresas(List<MEmpresa> empresas) {
        this.empresas = empresas;
    }

    /**
     * @param pacs the pacs to set
     */
    public void setPacs(List<MPac> pacs) {
        this.pacs = pacs;
    }

    /**
     * @return the pacEmpresaSelected
     */
    public MPacMEmpresa getPacEmpresaSelected() {
        return pacEmpresaSelected;
    }

    /**
     * @param pacEmpresaSelected the pacEmpresaSelected to set
     */
    public void setPacEmpresaSelected(MPacMEmpresa pacEmpresaSelected) {
        this.pacEmpresaSelected = pacEmpresaSelected;
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
     * @return the idPacSelected
     */
    public int getIdPacSelected() {
        return idPacSelected;
    }

    /**
     * @param idPacSelected the idPacSelected to set
     */
    public void setIdPacSelected(int idPacSelected) {
        this.idPacSelected = idPacSelected;
    }


    public int getIdEmpresaSelected() {
        return this.idEmpresaSelected;
    }

    public boolean isBooStatus() {
        return this.booStatus;
    }

    public void setIdEmpresaSelected(int idEmpresaSelected) {
        this.idEmpresaSelected = idEmpresaSelected;
    }

    public void setBooStatus(boolean booStatus) {
        this.booStatus = booStatus;
    }
}
