/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.mbeans;

import fe.db.MAcceso;
import fe.model.dao.ControlAcceso;
import fe.model.dao.LogAccesoDAO;
import fe.model.dao.UsuarioDAO;
import fe.model.util.ControlPasword;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 *
 * @author Eduardo C. Flores Ambrosio <Eduardo at EB&S>
 */
public class ManagedBeanCambioPassword implements Serializable {

    private static long serialVersionUID = 1L;
    private String passant;
    private String newpass;//La validacion de la igualdad de los 2 pass se hace en el xhtml
    private Integer id_acceso;
    private String usuario;
    private String Tipouser;
    private ControlAcceso CtrlAcceso;
    private UsuarioDAO DAOUser;
    private LogAccesoDAO DAOLog;
    private ControlPasword CtrlPass;
    private Date fecha;

    //Bandera
    private boolean logged;
    //Modelos
    private MAcceso usuarioAcceso;
    //Contexto
    private HttpServletRequest httpServletRequest;
    private FacesContext facesContext;
    private FacesMessage facesMessage;
    private String appContext;

    private int currentOperation = -1;

    /**
     * Creates a new instance of ManagedBeanCambioPassword
     */
    public ManagedBeanCambioPassword() {

    }

    @PostConstruct
    public void init() {
        facesContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        usuarioAcceso = (MAcceso) httpSession.getAttribute("macceso");//Sesion del usuario
        appContext = httpServletRequest.getContextPath();
        CtrlAcceso = new ControlAcceso();
        DAOUser = new UsuarioDAO();
        DAOLog = new LogAccesoDAO();
        CtrlPass = new ControlPasword();
    }

    /**
     * Se guarda el nuevo password en la base de dato
     */
    public void guardarNuevoPassword() {
        fecha = new Date();
        try {
                String pwant = CtrlAcceso.getEncriptPW(usuarioAcceso.getUsuario(), passant.trim());
                if (usuarioAcceso.getClave().trim().equals(pwant)) {
                    String respuesta = CtrlPass.ValidaPassPasado(newpass, passant);//Valida el passsword
                    respuesta = "Correcto";//TO WORKK WITHOUR VALIDATION
                    if (respuesta.equals("Correcto")) {
                        String pw = CtrlAcceso.getEncriptPW(usuarioAcceso.getUsuario(), newpass);
                        if (usuarioAcceso.getCambiaClave() == 1) {
                            usuarioAcceso.setCambiaClave(0);
                        }
                        usuarioAcceso.setClave(pw);
                        if (DAOUser.guardarActualizarUsuario(usuarioAcceso)) {
                            DAOLog.guardaRegistro(usuarioAcceso, "Modifico su password");
                            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, "Su contraseña ha sido cambiada,.", "Info"));
                            limpiarSesion();
                            setUsuario(usuarioAcceso.getUsuario());
                        } else {
                            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se logro actualizar su password, intentelo mas tarde", "Info"));
                        }
                    } else {
                        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, respuesta, "Info"));
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Verifique su password anterior ¡Es incorrecto!", "Info"));
                }
            if (usuarioAcceso == null) {
                //limpiar la sessionFacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocurrio un problema en el servidor.", "Info"));
                limpiarSesion();//Limpiamos la session
                newpass = null;
                usuario = null;
            }

        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocurrio un problema en el servidor.", "Info"));
        }

    }

    /**
     * Redirecciona a la pagina de inicio del sistema
     */
    public void redirectIndex() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(appContext +"/main/principal.xhtml");
        } catch (IOException e1) {
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "A ocurrido un error al redireccionar.", "Error"));
        }
    }

    /**
     * Limpia el contenido de la sesion
     */
    public void limpiarSesion() {
        
    }

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @return the passant
     */
    public String getPassant() {
        
        return passant;
    }

    /**
     * @return the newpass
     */
    public String getNewpass() {
        return newpass;
    }

    /**
     * @return the id_acceso
     */
    public Integer getId_acceso() {
        return id_acceso;
    }

    /**
     * @return the usuario
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * @return the Tipouser
     */
    public String getTipouser() {
        return Tipouser;
    }

    /**
     * @return the fecha
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @return the logged
     */
    public boolean isLogged() {
        return logged;
    }

    /**
     * @return the usuarioAcceso
     */
    public MAcceso getUsuarioAcceso() {
        return usuarioAcceso;
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
    public int getCurrentOperation() {
        return currentOperation;
    }

    /**
     * @param aSerialVersionUID the serialVersionUID to set
     */
    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
    }

    /**
     * @param passant the passant to set
     */
    public void setPassant(String passant) {
        this.passant = passant;
    }

    /**
     * @param newpass the newpass to set
     */
    public void setNewpass(String newpass) {
        this.newpass = newpass;
    }
    /**
     * @param id_acceso the id_acceso to set
     */
    public void setId_acceso(Integer id_acceso) {
        this.id_acceso = id_acceso;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * @param Tipouser the Tipouser to set
     */
    public void setTipouser(String Tipouser) {
        this.Tipouser = Tipouser;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * @param logged the logged to set
     */
    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    /**
     * @param usuarioAcceso the usuarioAcceso to set
     */
    public void setUsuarioAcceso(MAcceso usuarioAcceso) {
        this.usuarioAcceso = usuarioAcceso;
    }

    /**
     * @param currentOperation the currentOperation to set
     */
    public void setCurrentOperation(int currentOperation) {
        this.currentOperation = currentOperation;
    }

}
