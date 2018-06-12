/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.mbeans;

import fe.db.MAcceso;
import fe.db.MConfig;
import fe.model.dao.ConfigDAO;
import fe.model.dao.ControlAcceso;
import fe.model.dao.LogAccesoDAO;
import fe.model.dao.LoginDAO;
import fe.model.dao.UsuarioDAO;
import fe.model.util.ControlPasword;
import fe.model.util.SendMail;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;

/**
 *
 * @author Eduardo
 */
@ManagedBean(name = "managedBeanLogin")
@RequestScoped
public class ManagedBeanLogin implements Serializable {
    private int  numberOfActiveUsers =  0;

    //Datos del usuario
    private String userNick;
    private String password;
    private String rfcUsuario;
    private String emailUsuario;
    //Bandera
    private boolean logged;
    //Modelos
    private MAcceso mAcceso;
    //DAOs
    private LoginDAO daoLogin;
    private LogAccesoDAO logAcceso;
    private UsuarioDAO daoUser;
    private ControlAcceso ctrAcceso;
    private ControlPasword ctrlPassword;
    private SendMail mailHandler;
    private ConfigDAO daoConfig;
    //Contexto
    private HttpServletRequest httpServletRequest;
    private FacesContext facesContext;
    private boolean cambiarClave;//Para indicar que se tiene que cambiar la clave por una solicitud de recuperacion
    private String appContext;

    private String mensaje;
    private Date fchActual;
    private boolean isProduction = false;

    /**
     * Creates a new instance of ManagedBeanLogin
     */
    public ManagedBeanLogin() {
        HttpSession ss = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        //ss.setMaxInactiveInterval(4800);
    }

    @PostConstruct
    public void init() {
        logAcceso = new LogAccesoDAO();//Crear los DAOS en el postcontructu mejora el performance
        daoUser = new UsuarioDAO();
        daoLogin = new LoginDAO();
        mailHandler = new SendMail();
        ctrlPassword = new ControlPasword();
        daoConfig = new ConfigDAO();
        //Checking if is production stage
        MConfig ambiente = daoConfig.BuscarConfigDatoClasificacion("AMBIENTE", "SERVIDOR");
        if (ambiente != null && (
                          ambiente.getValor().equalsIgnoreCase("PRODUCCION")
                        ||ambiente.getValor().equalsIgnoreCase("PROD")
        )){
            isProduction = true;
        }

        httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        appContext = httpServletRequest.getContextPath();
    }

    /**
     * Manejo de eventos para un commandbutton
     * @param actionEvent 
     */
    public void buttonAction(ActionEvent actionEvent) {
        try {
            login();
        } catch (IOException ex) {
            Logger.getLogger(ManagedBeanLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void preRenderTask(){
        //Check if user need change her password
    }
    
    /**
     * Comprueba la existencia de las credenciales del usuario
     */
    public void login() throws IOException {
        mensaje = "";
        facesContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        try {
            //daoLogin.buscarAccesoPorLogin(userNick);
            mAcceso = daoUser.BuscarUsuarioUser(userNick);
            if (mAcceso != null) {
                ctrAcceso = new ControlAcceso(this, httpServletRequest);
                if (ctrAcceso.validaUsusario(password)) {
                    this.logged = true;
                    fchActual = new Date();
                    HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                    httpSession.setMaxInactiveInterval(60 * mAcceso.getPerfil().getTimeOut());//SOBRE ESCRIBE EL VALOR QUE ESTA EN WEB.XML
                    httpSession.setAttribute("macceso", this.mAcceso);
                    httpSession.setAttribute("usernick", this.userNick);//Nombre Usuario
                    httpSession.setAttribute("logeado", this.logged);//Bandera
                    httpSession.setAttribute("perfil", (mAcceso.getPerfil().getPerfil() == null)?
                            0 : mAcceso.getPerfil().getPerfil());//En caso de que el perfil sea nulo evitamos que el usuario pueda hacer cosas en la aplicacion
                    numberOfActiveUsers++;
                    System.out.println("Number of active users:{"+numberOfActiveUsers+ "}");
                    try {
                        logAcceso.guardaRegistro(mAcceso, "Ingreso al sistema el usuario: " + userNick);
                        FacesContext.getCurrentInstance().getExternalContext().redirect( httpServletRequest.getContextPath() + "/main/principal.xhtml");
                    } catch (Exception ex) {
                        ex.printStackTrace(System.out);
                    }
                } else {
                    
                    if (!cambiarClave) {
                        if(mAcceso.getEstatus() !=0)
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                                    "Usuario o contraseña erroneos.", null));
                    } else {
                        redirectChangePasword();
                    }
                }
            } else {
                // A ocurrido un  error al autenticarse, enviar un mensaje al usuario
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "El usuario no existe.", null));
            }
        } catch (Exception e1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Ha ocurrido un error interno, contacte con el proveedor."
                    + "de nuevo.", null));
            e1.printStackTrace();
        }
    }

    /**
     * Metodo de Prueba para comprobar el login, no usa base de datos.
     *
     * @return
     */
    public String loginUser() {
        RequestContext rc = RequestContext.getCurrentInstance();
        boolean loggedIn = false;
        if (userNick != null && userNick.equals("name") && password != null && password.equals("pass")) {
            loggedIn = true;
            rc.addCallbackParam("loggedIn", loggedIn);
            //FacesContext.getCurrentInstance().getExternalContext().redirect(appContext +"/main/principal.xhtml");
            return "/main/principal.xhtml";
        } else {
            rc.addCallbackParam("loggedIn", loggedIn);
            return "login.xhtml";
        }
    }

    /**
     * Redirige a la pagina de recuperacion de credenciales
     */
    public void redirectPassRecovery() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/recovery/passwordRecovery.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ManagedBeanLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Redirige a la pagina de login
     */
    public void redirectLoginPage() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/login/login.xhtml");
        } catch (IOException ex) {
            System.out.println("Error redirecting");
            Logger.getLogger(ManagedBeanLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Redirecciona a la pagina de autoregistro
     */
    public void redirectAutoregistroPage() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/login/autoRegistro.xhtml");
        } catch (IOException ex) {
            System.out.println("Error redirecting");
            Logger.getLogger(ManagedBeanLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Redirecciona a la pagina de autoregistro
     */
    public void loginUsingEmpresaDemo() {
//        try {
            //Login and redirect
            //FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/login/trial.xhtml");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Not yet supported.", "Error!"));
//        } catch (IOException ex) {
//            System.out.println("Error redirecting");
//            Logger.getLogger(ManagedBeanLogin.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    /**
     * Redirecciona a la pagina de cambio de password primera vez
     *
     */
    public void redirectChangePasword() {
        try {
            HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            httpSession.setAttribute("macceso", this.mAcceso);//Perfil
            httpSession.setAttribute("usernick", this.userNick);//Nombre Usuario
            httpSession.setAttribute("logeado", this.logged);//Bandera
            FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/login/cambioClave.xhtml");
        } catch (IOException ex) {
            System.out.println("Error redirecting");
            Logger.getLogger(ManagedBeanLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metodo para recuperar el password
     */
    public void passwordRecovery() {
        mailHandler = new SendMail();
        mAcceso = daoUser.BuscarUsuarioUser(rfcUsuario);
        if (mAcceso != null) {
            String PassGenerico = ctrlPassword.GeneraccionPass();
            mAcceso.setClave(ctrlPassword.getEncriptPW(mAcceso.getUsuario(), PassGenerico));
            ///Obligamos al usuario a cambiar la clave cuando intente ingresar
            mAcceso.setCambiaClave(1);
            if (daoUser.guardarActualizarUsuario(mAcceso)) {
                String asunto = "Envio de Password";
                String mensaje = "Se le envia su password de reposición y su usuario \n\nUsuario:"
                        + mAcceso.getUsuario()
                        + "\n \nPassword:"
                        + PassGenerico + "\n\n\nSaludos.";
                String[] mails = mAcceso.getEmail().split(";");
                if (mailHandler.sendEmail(asunto, mensaje, mails)) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                            FacesMessage.SEVERITY_INFO,
                            "Le hemos enviado un correo de recuperación.", "Error!"));
                    rfcUsuario = "";
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                            FacesMessage.SEVERITY_WARN,
                            "No hemos podido enviarle un correo de recuperación, intentelo más tarde.", "Error!"));
                }
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "El usuario no existe, si no esta seguro de su usuario, contacte al proveedor.",
                    "Error!"));
            rfcUsuario = "";
        }
    }

    /**
     * Invalida la sesion del usuario
     *
     * @return
     */
    public void logout() {
        //Invalidate the session and remove the atributes
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        httpSession.removeAttribute("macceso");
        httpSession.removeAttribute("usernick");
        httpSession.removeAttribute("logeado");
        httpSession.removeAttribute("empresa");
        httpSession.removeAttribute("perfil");
        //Remove the cookies
//        Map<String, Object> cookies = FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap();
//        Iterator<Map.Entry<String, Object>> entries = cookies.entrySet().iterator();
//        while (entries.hasNext()) {
//            Map.Entry entry = (Map.Entry) entries.next();
//        }
        httpSession.invalidate();
        if (numberOfActiveUsers > 0){
            numberOfActiveUsers--;
        }

        System.out.println("Number of active users:{"+numberOfActiveUsers+ "}");
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/login/login.xhtml");
            //return "/login/login.xhtml";
        } catch (IOException ex) {
            Logger.getLogger(ManagedBeanLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void crearCuentaDePrueba(){
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "NOT SUPPORTED", "--"));
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        //password = .escapeSql(password);
        //password = .escapeHtml(password);
        this.password = password;
    }

    public String navigate() {
        return "passwordForget.xhtml";
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public MAcceso getmAcceso() {
        return mAcceso;
    }

    public void setmAcceso(MAcceso mAcceso) {
        this.mAcceso = mAcceso;
    }

    public ControlAcceso getCtrAcceso() {
        return ctrAcceso;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Boolean isCambiarClave() {
        return cambiarClave;
    }

    public void setCambiarClave(Boolean cambiarClave) {
        this.cambiarClave = cambiarClave;
    }

    public Date getFchActual() {
        return fchActual;
    }

    public void setFchActual(Date fchActual) {
        this.fchActual = fchActual;
    }

    public boolean guardarActualizarAcceso(MAcceso tmp) {
        return daoUser.guardarActualizarUsuario(tmp);
    }

    /**
     * @return the rfcUsuario
     */
    public String getRfcUsuario() {
        return rfcUsuario;
    }

    /**
     * @return the emailUsuario
     */
    public String getEmailUsuario() {
        return emailUsuario;
    }

    /**
     * @param rfcUsuario the rfcUsuario to set
     */
    public void setRfcUsuario(String rfcUsuario) {
        //rfcUsuario = .escapeSql(rfcUsuario);
        //rfcUsuario = .escapeHtml(rfcUsuario); 
        this.rfcUsuario = rfcUsuario;
    }

    /**
     * @param emailUsuario the emailUsuario to set
     */
    public void setEmailUsuario(String emailUsuario) {
        //emailUsuario = .escapeSql(emailUsuario);
        //emailUsuario = .escapeHtml(emailUsuario);
        this.emailUsuario = emailUsuario;
    }

    public boolean isIsProduction() {
        return isProduction;
    }

    public void setIsProduction(boolean isProduction) {
        this.isProduction = isProduction;
    }

    public int getNumberOfActiveUsers() {
        return numberOfActiveUsers;
    }

}