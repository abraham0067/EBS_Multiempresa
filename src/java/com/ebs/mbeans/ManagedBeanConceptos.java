/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.mbeans;

import fe.db.MAcceso;
import fe.db.MConceptosFacturacion;
import fe.model.dao.ConceptoFacturacionDAO;
import fe.model.dao.EmpresaDAO;
import fe.model.dao.LogAccesoDAO;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
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
public class ManagedBeanConceptos implements Serializable {

    private MAcceso mAcceso;
    private int idEmpresaSelect;//som 

    private String tipoBusqueda;
    private String paramBusqueda;
    //Contexto
    private HttpServletRequest httpServletRequest;
    private FacesContext facesContext;
    private String appContext;


    private int currentOperation;
    private List<MConceptosFacturacion> conceptos;
    private MConceptosFacturacion concepto;

    //DAOS
    private EmpresaDAO daoEmp;
    private ConceptoFacturacionDAO daoConcepto;
    private LogAccesoDAO daoLog;

    /**
     * Creates a new instance of ManagedBeanConceptos
     */
    public ManagedBeanConceptos() {

    }

    @PostConstruct
    public void init() {
        facesContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        mAcceso = (MAcceso) httpSession.getAttribute("macceso");
        appContext = httpServletRequest.getContextPath();
        daoEmp = new EmpresaDAO();
        daoConcepto = new ConceptoFacturacionDAO();
        daoLog = new LogAccesoDAO();
    }

    public void buscarConceptos() {
        if (validarBusqueda()) {
            conceptos = daoConcepto.BuscaConceptos(mAcceso.getId(), idEmpresaSelect, tipoBusqueda, paramBusqueda);
        }
    }

    public void guardarConcepto() {
//        this.concepto.escapeSqlAndHtmlCharacters();
        if(currentOperation == 0){
            if(validarCreacion()){
                concepto.setEmpresa(daoEmp.BuscarEmpresaId(idEmpresaSelect));
                if(daoConcepto.GuardarOActualizar(concepto)){
                    daoLog.guardaRegistro(mAcceso, "Registro el concepto "+concepto.getClaveconcepto()+" de la empresa "+ concepto.getEmpresa().getRfcOrigen());
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "El concepto se registro correctamente.", "Error"));
                    FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                    redirectConsultaConceptos();
                }
            }
        } else if(currentOperation == 1){
            if(validarCreacion()){
                if(daoConcepto.GuardarOActualizar(concepto)){
                    daoLog.guardaRegistro(mAcceso, "Modifico el concepto "+concepto.getClaveconcepto()+" de la empresa "+ concepto.getEmpresa().getRfcOrigen());
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "El concepto se modifico correctamente.", "Error"));
                    FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                    redirectConsultaConceptos();
                }
            }
        }
    }

    public void redirectConsultaConceptos() {
        try {
            concepto = null;
            idEmpresaSelect = -1;
            conceptos = new ArrayList<>();
            FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/admin/catalogos/catConceptos.xhtml");
        } catch (IOException e1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "A ocurrido un error al redireccionar.", "Error"));
        }
    }

    public void redirectCreacionConcepto() {
        try {
            currentOperation = 0;
            resetC();
            FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/admin/catalogos/concepto.xhtml");
        } catch (IOException e1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "A ocurrido un error al redireccionar.", "Error"));
        }
    }

    public void redirectModificacionConcepto() {
        if(concepto != null){
            currentOperation = 1;
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/admin/catalogos/concepto.xhtml");
            } catch (IOException e1) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "A ocurrido un error al redireccionar.", "Error"));
            }
        } else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Por favor seleccione un concepto para modificar.", "Error"));
        }

    }

    public void borrarConcepto() {
        if (concepto != null) {
            if (daoConcepto.BorrarConcepto(concepto)) {
                daoLog.guardaRegistro(mAcceso, "Borro el concepto " + concepto.getClaveconcepto() + " de la empresa " + concepto.getEmpresa().getRfcOrigen());
                concepto = null;
                buscarConceptos();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "El concepto se borro exitosamente.", "Error"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debes seleccionar un concepto.", "Error"));
        }

    }

    public boolean validarBusqueda() {
        boolean res = true;
        if (idEmpresaSelect < 0) {
            res = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debes seleccionar una empresa.", "Error"));
        }
        if (!tipoBusqueda.equals("Ninguno") && !tipoBusqueda.equals("Todos")) {
            if (paramBusqueda == null || paramBusqueda.equals("")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debes ingresar el parametro de busqueda.", "Error"));
                res = false;
            }
        }
        return res;
    }

    public boolean validarCreacion() {
        boolean res = true;
        
        if(concepto.getConceptofacturacion() != null && !concepto.getConceptofacturacion().equals("") ){
             MConceptosFacturacion ctofe= daoConcepto.BuscarConceptoEmpresa(concepto.getConceptofacturacion().trim(), idEmpresaSelect);
             if(ctofe != null){
                if(currentOperation == 1){
                    if(concepto.getId() != ctofe.getId()){
                        res =false;
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "El concepto ya existe favor de modificarlo.", "Error"));
                    }
                } else if(currentOperation == 0){
                    res =false;
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "El concepto ya existe favor de modificarlo.", "Error"));
                }
             }
        }
        return res;
    }

    public void resetC() {
        concepto = null;
        concepto = new MConceptosFacturacion();
        idEmpresaSelect = -1;
        

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
     * @param idEmpresaSelect the idEmpresaSelect to set
     */
    public void setIdEmpresaSelect(int idEmpresaSelect) {
        this.idEmpresaSelect = idEmpresaSelect;
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
     * @return the conceptos
     */
    public List<MConceptosFacturacion> getConceptos() {
        return conceptos;
    }

    /**
     * @return the concepto
     */
    public MConceptosFacturacion getConcepto() {
        return concepto;
    }

    /**
     * @param conceptos the conceptos to set
     */
    public void setConceptos(List<MConceptosFacturacion> conceptos) {
        this.conceptos = conceptos;
    }

    /**
     * @param concepto the concepto to set
     */
    public void setConcepto(MConceptosFacturacion concepto) {
        this.concepto = concepto;
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

}
