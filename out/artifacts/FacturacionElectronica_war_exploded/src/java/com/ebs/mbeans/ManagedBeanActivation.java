/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.mbeans;

import fe.db.MAcceso;
import fe.db.MConfig;
import fe.model.dao.ConfigDAO;
import fe.model.dao.DireccionDAO;
import fe.model.dao.EmpresaDAO;
import fe.model.dao.PerfilDAO;
import fe.model.dao.UsuarioDAO;
import fe.model.util.ControlPasword;
import fe.model.util.SendMail;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

/**
 *
 * @author Eduardo C. Flores Ambrosio <EB&S>
 */
@ManagedBean
@RequestScoped
public class ManagedBeanActivation implements Serializable {

    @ManagedProperty(value = "#{param.key}")
    private String key;
    private boolean valid;
    private boolean keyExpired;
    private UsuarioDAO daoUser;
    private EmpresaDAO daoEmp;
    private ConfigDAO daoConfig;
    private MConfig expirationTimeLimit;
    private DireccionDAO daoDir;
    private PerfilDAO daoPerfil;
    private SendMail enviomail;
    private ControlPasword contPass;

    /**
     * Creates a new instance of ManagedBeanActivation
     */
    public ManagedBeanActivation() {

    }

    @PostConstruct
    public void init() {
        daoUser = new UsuarioDAO();
        daoEmp = new EmpresaDAO();
        contPass = new ControlPasword();
        expirationTimeLimit = daoConfig.BuscarConfigDatoClasificacion("EXPIRATION_TIME", "REGISTRO");
        if (expirationTimeLimit == null) {
            //Create the parameter
        }
        //Check if key is expired and valid
        valid = checkKey(key);       
    }

    /**
     * Check verification key in database
     *
     * @param key
     * @return
     */
    private boolean checkKey(String key) {
        boolean res = false;
        //Find User

        MAcceso user = daoUser.buscarUserByActivationKey(key);
        //Get fecha de registro
        Calendar cal = Calendar.getInstance();
        cal.setTime(user.getFechaRegistro());
        cal.add(Calendar.MINUTE, cal.MINUTE + Integer.getInteger(expirationTimeLimit.getValor())); // 60 min
        if (cal.before(new Date())) {
            //Activation key still valid
            //Update user
            if (user != null) {
                user.getEmpresa().setEstatusEmpresa(1);//Activate empresa
                daoEmp.GuardarOActualizaEmpresa(user.getEmpresa());
                user.setEstatus(1);//Activate user
                user.setVerificationKey(null);
                if (daoUser.guardarActualizarUsuario(user)) {
                    res = true;
                }
            }
        } else {
            //Activation key expired
            keyExpired = true;
        }

        return res;
    }

    /**
     * If key is expired we sent new link
     * @param user 
     */
    public void sendEmailAgain() {
        MAcceso user = daoUser.buscarUserByActivationKey(key);
        //Encode the query parameter in UTF-8 for HTTP GET REQUEST
        String verificationKey = null;
        String clave = contPass.GeneraccionPass();//New key
        String claveFinal = contPass.getEncriptPW(user.getEmpresa().getCorreoContacto().trim(), clave);//Email and pass
        verificationKey = generateB64Sha256(user.getEmpresa().getCorreoContacto() + "." + clave);
        user.setVerificationKey(verificationKey);//Set verification key
        user.setClave(claveFinal);
        user.setFechaRegistro(new Date());//Changes the new reg date
        if (daoUser.guardarActualizarUsuario(user)) {//Persist user
            String Url = null;
            //Se ha creado un nuevo registro en la tabla de configuracion para la nueva url del portal V2
            MConfig paramUrl = daoConfig.BuscarConfigDatoClasificacion("ACTIVAR_USUARIO_V2", "URL_DEL_PORTAL");
            if (paramUrl != null) {
                Url = paramUrl.getValor().trim();
                //Enconde only the query parameter
                //Save in database the activation key not encoded
                //http://localhost/FacturacionElectronica/activation.xhtml?querystring (Example URL)
                try {
                    Url += "activation.xhtml?key=" + URLEncoder.encode(verificationKey, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    //DO OTHER ENCODING
                }
                String mensaje = "Para terminar su registro a Facturación Electrónica pulse el link:  "
                        + Url + " \n para realizar su confirmación. Y acceda al portal  con el siguiente: "
                        + "\n\nUsuario: " + user.getEmpresa().getCorreoContacto()
                        + "\n\nPassword: "
                        + clave
                        + "\n\n Saludos.";
                String[] mails = user.getEmpresa().getCorreoContacto().split(";");
                if (enviomail.sendEmail("Confirmación del Auto-Registro en Electronic Bills & Services", mensaje, mails)) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Se ha enviado un nuevo correo  para confirmar su registro.", "Info"));
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "No se logro enviar el correo favor de comunicarse con el proveedor solicitando su usuario.", "Info"));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "No se logro enviar el correo favor de comunicarse con el proveedor solicitando su usuario.", "Info"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Lo sentimos, en estos momentos estamos presentando problemas, intentelo más tarde.", "Info"));
        }

    }

    private String generateB64Sha256(String text) {
        return Base64.encodeBase64String(DigestUtils.sha256(text));
    }

    public String encrypt(String cadena) {
        StandardPBEStringEncryptor s = new StandardPBEStringEncryptor();
        s.setPassword("uniquekey");
        return s.encrypt(cadena);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isKeyExpired() {
        return keyExpired;
    }

    public void setKeyExpired(boolean keyExpired) {
        this.keyExpired = keyExpired;
    }

}
