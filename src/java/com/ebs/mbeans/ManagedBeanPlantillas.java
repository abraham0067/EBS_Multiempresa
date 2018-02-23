/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.mbeans;

import fe.db.MAcceso;
import fe.db.MEmpresa;
import fe.db.MPlantilla;
import fe.model.dao.EmpresaDAO;
import fe.model.dao.LogAccesoDAO;
import fe.model.dao.PlantillaDAO;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 * @author Eduardo C. Flores Ambrosio <Eduardo at EB&S>
 */
@ManagedBean
@SessionScoped
public class ManagedBeanPlantillas implements Serializable {

    private MAcceso mAcceso;
    private int idEmpresaUsuario;
    private int idEmpresaSelect;
    private MEmpresa empresaPlantilla;
    private DefaultStreamedContent scPlantilla;
    private SimpleDateFormat sdf;

    //Contexto
    private HttpServletRequest httpServletRequest;
    private FacesContext facesContext;
    private String appContext;


    //Modelos, consulta de empresas
    private List<MEmpresa> empresas;
    private List<MPlantilla> plantillas;
    private MPlantilla plantillaSelected;
    private boolean activarPlantilla;
    private UploadedFile fuPlantilla;
    private UploadedFile fuImgPlantilla;
    private int currentOperation = -1;//0 creacion 1 Modificacion

    //DAOS
    private EmpresaDAO daoEmpresas;
    private PlantillaDAO daoPlantillas;
    private LogAccesoDAO daoLogs;

    /**
     * Creates a new instance of ManagedBeanPlantillas
     */
    public ManagedBeanPlantillas() {
    }

    @PostConstruct
    public void init() {
        facesContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        mAcceso = (MAcceso) httpSession.getAttribute("macceso");
        appContext = httpServletRequest.getContextPath();
        idEmpresaUsuario = mAcceso.getEmpresa().getId();
        daoEmpresas = new EmpresaDAO();
        daoLogs = new LogAccesoDAO();
        daoPlantillas = new PlantillaDAO();
    }

    /**
     * Borra la plantilla seleccionada
     */
    public void borrarPlantilla() {
        if (plantillaSelected != null) {
            if (daoPlantillas.BorrarPlantilla(plantillaSelected)) {
                daoLogs.guardaRegistro(mAcceso, "Elimino la plantilla " + plantillaSelected.getNombre().trim() + " con id " + plantillaSelected.getId());
                plantillas.remove(plantillaSelected);
                plantillaSelected = null;
                FacesContext.getCurrentInstance().addMessage("frmAdminPlantillas", new FacesMessage(FacesMessage.SEVERITY_INFO, "La plantilla se elimino correctamente.", "Error"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage("frmAdminPlantillas", new FacesMessage(FacesMessage.SEVERITY_WARN, "Debes seleccionar una plantilla para eliminar.", "Error"));
        }
    }

    /**
     * Redirecciona a la pagina de creacion de una nueva plantilla
     */
    public void redirectNuevaPlantilla() {
        currentOperation = 0;
        reset();//Clear
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/admin/plantillas/plantilla.xhtml");
        } catch (IOException e1) {
            FacesContext.getCurrentInstance().addMessage("frmAdminPlantillas", new FacesMessage(FacesMessage.SEVERITY_ERROR, "A ocurrido un error al redireccionar.", "Error"));
        }
    }

    /**
     * Redirecciona a la pagina de modificacion de datos de una plantilla
     */
    public void redirectModificarPlantilla() {
        if (plantillaSelected != null) {
            currentOperation = 1;
            if (plantillaSelected.getEstatus() == 1) {
                activarPlantilla = true;
            } else if (plantillaSelected.getEstatus() == 2) {
                activarPlantilla = false;
            }
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/admin/plantillas/plantilla.xhtml");
            } catch (IOException e1) {
                e1.printStackTrace();
                FacesContext.getCurrentInstance().addMessage("frmAdminPlantillas", new FacesMessage(FacesMessage.SEVERITY_ERROR, "A ocurrido un error al redireccionar.", "Error"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Selecciona un plantilla.", "Error"));
        }
    }

    /**
     * Limpia los datos para la creacion de una nueva plantilla
     */
    public void reset() {
        plantillaSelected = null;
        plantillaSelected = new MPlantilla();
        plantillaSelected.setVersion(1.0f);
        activarPlantilla = false;
    }

    public void resetForCon() {
        plantillaSelected = null;
    }

    /**
     * Redirecciona a la pagina de consulta de plantillas disponibles
     */
    public void redirectConsultarPlantillas() {
        currentOperation = -1;
        resetForCon();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/admin/plantillas/adminPlantillas.xhtml");
        } catch (IOException e1) {
            e1.printStackTrace();
            FacesContext.getCurrentInstance().addMessage("frmPlantilla", new FacesMessage(FacesMessage.SEVERITY_ERROR, "A ocurrido un error al redireccionar.", "Error"));
        }
    }

    public void guardarPlantilla() {
        if (currentOperation == 0) {
            if(registrarPlantilla()){
                redirectConsultarPlantillas();
            }
        } else if (currentOperation == 1) {
            if(modificarPlantilla()){
                redirectConsultarPlantillas();
            }
        }
    }

    /**
     * Guarda las modificaciones realizadas a una plantilla
     */
    public boolean modificarPlantilla() {
        boolean res = false;

        if (activarPlantilla) {
            plantillaSelected.setEstatus(1);
        } else {
            plantillaSelected.setEstatus(0);
        }

        if (fuPlantilla != null && fuPlantilla.getContents() != null && fuPlantilla.getContents().length > 0) {
            plantillaSelected.setPlantilla(fuPlantilla.getContents());
        }

        if (fuImgPlantilla != null && fuImgPlantilla.getContents() != null && fuImgPlantilla.getContents().length > 0) {
            plantillaSelected.setImagen(fuImgPlantilla.getContents());
        }

        if (daoPlantillas.GuardarOActualizar(plantillaSelected)) {
            daoLogs.guardaRegistro(mAcceso, "Se modifico la plantilla " + plantillaSelected.getNombre().trim() + " con id " + plantillaSelected.getId());
            FacesContext.getCurrentInstance().addMessage("frmPlantilla", new FacesMessage(FacesMessage.SEVERITY_INFO, "La plantilla se ha modificado correctamente.", "Error"));
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            res = true;
        } else {
            FacesContext.getCurrentInstance().addMessage("frmPlantilla", new FacesMessage(FacesMessage.SEVERITY_ERROR, "A ocurrido un error en la modificación.", "Error"));
        }

        return res;
    }

    /**
     * Guarda una nueva plantilla
     */
    public boolean registrarPlantilla() {
        boolean res = false;

        plantillaSelected.setEmpresa(daoEmpresas.BuscarEmpresaId(idEmpresaSelect));

        if (fuPlantilla != null && fuPlantilla.getContents() != null && fuPlantilla.getContents().length > 0) {
            plantillaSelected.setPlantilla(fuPlantilla.getContents());
        }

        if (fuImgPlantilla != null && fuImgPlantilla.getContents() != null && fuImgPlantilla.getContents().length > 0) {
            plantillaSelected.setImagen(fuImgPlantilla.getContents());
        }

        if (activarPlantilla) {
            plantillaSelected.setEstatus(1);
        } else {
            plantillaSelected.setEstatus(0);
        }

        if (daoPlantillas.existPlantilla(idEmpresaSelect,plantillaSelected.getNombre()) == false) {
            if (daoPlantillas.GuardarOActualizar(plantillaSelected)) {
                daoLogs.guardaRegistro(mAcceso, "Se registro la plantilla " + plantillaSelected.getNombre().trim() + " con id " + plantillaSelected.getId());
                buscarPlantillas();
                FacesContext.getCurrentInstance().addMessage("frmPlantilla",
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "La plantilla se ha registrado correctamente.", "Error"));
                FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
                res = true;
            } else {
                FacesContext.getCurrentInstance().addMessage("frmPlantilla",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "A ocurrido un error.", "Error"));
            }
        }else{
            FacesContext.getCurrentInstance().addMessage("frmPlantilla",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Al parecer ya existe una plantilla con el mismo nombre, el nombre debe ser unico.", "Error"));
        }
        return res;
    }

    /**
     * Descarga in zip de la plantilla
     *
     * @return
     */
    public void descargarZipPlantilla(int idTemp) {
        scPlantilla = null;
        sdf = new SimpleDateFormat("dd_MMMMM_yyyyy");
        MPlantilla plant = daoPlantillas.BuscarPlantilla(idTemp);
        if (plant != null) {
            String name = "plantilla" + ".zip";
            if (plant != null) {
                byte[] stream = plant.getPlantilla();
                name = plant.getNombre().replace(" ","_") + "-" + plant.getVersion() + "_" +  sdf.format(new Date()) + ".zip";
                name = name.trim();
                if (stream != null) {
                    setScPlantilla(new DefaultStreamedContent(new ByteArrayInputStream(stream), "application/zip", name));
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "No existe el archivo  en el registro.", "Detail"));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "No se pudo descargar el archivo comprimido.", "Detail"));
            }
        }
    }

    /**
     * Busca todas las plantillas disponibles para el usuario y le empresa
     * actual
     */
    public void buscarPlantillas() {
//        System.out.println("Buscando");
        if (plantillas != null) {
            plantillas.clear();
        }
        this.empresaPlantilla = daoEmpresas.BuscarEmpresaId(idEmpresaSelect);
        this.plantillas = daoPlantillas.ListaPlantillaEmpresa(idEmpresaSelect);
    }

    /**
     * @return the mAcceso
     */
    public MAcceso getmAcceso() {
        return mAcceso;
    }

    /**
     * @return the empresas
     */
    public List<MEmpresa> getEmpresas() {
        return empresas;
    }

    /**
     * @return the plantillas
     */
    public List<MPlantilla> getPlantillas() {
        return plantillas;
    }

    /**
     * @param mAcceso the mAcceso to set
     */
    public void setmAcceso(MAcceso mAcceso) {
        this.mAcceso = mAcceso;
    }

    /**
     * @param empresas the empresas to set
     */
    public void setEmpresas(List<MEmpresa> empresas) {
        this.empresas = empresas;
    }

    /**
     * @param plantillas the plantillas to set
     */
    public void setPlantillas(List<MPlantilla> plantillas) {
        this.plantillas = plantillas;
    }

    /**
     * @return the idEmpresaUsuario
     */
    public int getIdEmpresaUsuario() {
        return idEmpresaUsuario;
    }

    /**
     * @param idEmpresaUsuario the idEmpresaUsuario to set
     */
    public void setIdEmpresaUsuario(int idEmpresaUsuario) {
        this.idEmpresaUsuario = idEmpresaUsuario;
    }

    /**
     * @return the empresaPlantilla
     */
    public MEmpresa getEmpresaPlantilla() {
        return empresaPlantilla;
    }

    /**
     * @param empresaPlantilla the empresaPlantilla to set
     */
    public void setEmpresaPlantilla(MEmpresa empresaPlantilla) {
        this.empresaPlantilla = empresaPlantilla;
    }

    /**
     * @return the plantillaSelected
     */
    public MPlantilla getPlantillaSelected() {
        return plantillaSelected;
    }

    /**
     * @param plantillaSelected the plantillaSelected to set
     */
    public void setPlantillaSelected(MPlantilla plantillaSelected) {
        this.plantillaSelected = plantillaSelected;
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
     * @return the activarPlantilla
     */
    public boolean isActivarPlantilla() {
        return activarPlantilla;
    }

    /**
     * @return the fuPlantilla
     */
    public UploadedFile getFuPlantilla() {
        return fuPlantilla;
    }

    /**
     * @return the fuImgPlantilla
     */
    public UploadedFile getFuImgPlantilla() {
        return fuImgPlantilla;
    }

    /**
     * @param activarPlantilla the activarPlantilla to set
     */
    public void setActivarPlantilla(boolean activarPlantilla) {
        this.activarPlantilla = activarPlantilla;
    }

    /**
     * @param fuPlantilla the fuPlantilla to set
     */
    public void setFuPlantilla(UploadedFile fuPlantilla) {
        this.fuPlantilla = fuPlantilla;
    }

    /**
     * @param fuImgPlantilla the fuImgPlantilla to set
     */
    public void setFuImgPlantilla(UploadedFile fuImgPlantilla) {
        this.fuImgPlantilla = fuImgPlantilla;
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
     * @return the scPlantilla
     */
    public DefaultStreamedContent getScPlantilla() {
        return scPlantilla;
    }

    /**
     * @param scPlantilla the scPlantilla to set
     */
    public void setScPlantilla(DefaultStreamedContent scPlantilla) {
        this.scPlantilla = scPlantilla;
    }
}
