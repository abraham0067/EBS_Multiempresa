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
import java.util.Date;
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
public class ManagedBeanControlUser implements Serializable {

    private MAcceso acceso;
    private String passant;
    private String newpass;
    private String confinewpass;
    private String usuario;
    private String Tipouser;
    private Integer idAcceso;
    private ControlAcceso ctrlAcceso;
    private UsuarioDAO daoUser;
    private LogAccesoDAO daoLog;
    private ControlPasword ctrlPass;
    private Date fecha;
    private String mensaje;
    
    private HttpServletRequest httpServletRequest;
    private FacesContext faceContext;
    private String appContext;


    /**
     * Creates a new instance of ManagedBeanControlUser
     */
    public ManagedBeanControlUser() {
    }

    @PostConstruct
    public void init() {
        //Create daos and get session data
        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        acceso = (MAcceso) httpSession.getAttribute("macceso");
        idAcceso = acceso.getId();
        appContext = httpServletRequest.getContextPath();
    }

    public void guardaCambioContrasenia() {
        ctrlAcceso = new ControlAcceso();
        daoUser = new UsuarioDAO();
        daoLog = new LogAccesoDAO();
        ctrlPass = new ControlPasword();
        if ((acceso == null || acceso.getUsuario() == null || "".equals(acceso.getUsuario().trim())) && idAcceso != null && idAcceso > 0) {
            acceso = daoUser.BuscarUsuarioId(idAcceso);
        }


        //Comprobar si el password dummie enviado es el correcto
        String pwant = ctrlAcceso.getEncriptPW(acceso.getUsuario(), passant.trim());
        if (acceso.getClave().trim().equals(pwant)) {
            String respuesta = ctrlPass.ValidaPassPasado(newpass, passant);
            if (respuesta.equalsIgnoreCase("Correcto")) {
                acceso.setCambiaClave(0);
                acceso.setClave(ctrlAcceso.getEncriptPW(acceso.getUsuario(), newpass));
                if (daoUser.guardarActualizarUsuario(acceso)) {
                    daoLog.guardaRegistro(acceso, "El usuario modifico su propio password:"+acceso.getUsuario());
                    setUsuario(acceso.getUsuario());
                    cleanSession();
                    redirectPrincipal();
                    //Guardar en la sesion los datos del logeo y redirigir a la pagina principal
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se logro actualizar su password, intentelo mas tarde.", "Error!"));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, respuesta, "Error!"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Verifique su password anterior.", "Error!"));
        }
    }
    
    public void cleanSession(){
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        httpSession.removeAttribute("macceso");//Perfil
        httpSession.removeAttribute("usernick");//Nombre Usuario
        httpSession.removeAttribute("logeado");//Bandera
    }
    public void setSession(){
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        httpSession.setAttribute("macceso", this.acceso);//Perfil
        httpSession.setAttribute("usernick", this.acceso.getUsuario());//Nombre Usuario
        httpSession.setAttribute("logeado", true);//Bandera
    }

    public void redirectPrincipal() {
        try {
            daoLog.guardaRegistro(acceso, "Ingreso al sistema el usuario: " + this.acceso.getUsuario());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Exelente, su usuario ha sido activado. " +
                    "En adelante acceda con su nueva contraseña.", ""));
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/main/principal.xhtml");
        } catch (Exception ex) {
            System.out.println("Exception on login() " + ex.getMessage());
            ex.printStackTrace(System.err);
        }
    }

    public void redirectLoginImmediate() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/login/login.xhtml");
        } catch (Exception ex) {
            System.out.println("Exception on login() " + ex.getMessage());
            ex.printStackTrace(System.err);
        }
    }


    /**
     * @return the acceso
     */
    public MAcceso getAcceso() {
        return acceso;
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
     * @return the confinewpass
     */
    public String getConfinewpass() {
        return confinewpass;
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
     * @param acceso the acceso to set
     */
    public void setAcceso(MAcceso acceso) {
        this.acceso = acceso;
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
     * @param confinewpass the confinewpass to set
     */
    public void setConfinewpass(String confinewpass) {
        this.confinewpass = confinewpass;
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
     * @return the idAcceso
     */
    public Integer getIdAcceso() {
        return idAcceso;
    }

    /**
     * @param idAcceso the idAcceso to set
     */
    public void setIdAcceso(Integer idAcceso) {
        this.idAcceso = idAcceso;
    }

    /**
     * @return the mensaje
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * @param mensaje the mensaje to set
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
