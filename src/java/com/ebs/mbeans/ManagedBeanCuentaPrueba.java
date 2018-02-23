/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.mbeans;

import fe.db.MAcceso;
import fe.db.MConfig;
import fe.db.MDireccion;
import fe.db.MEmpresa;
import fe.db.MPerfil;
import fe.model.dao.ConfigDAO;
import fe.model.dao.DireccionDAO;
import fe.model.dao.EmpresaDAO;
import fe.model.dao.PerfilDAO;
import fe.model.dao.UsuarioDAO;
import fe.model.util.ControlPasword;
import fe.model.util.SendMail;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

/**
 *
 * @author Eduardo C. Flores Ambrosio <Eduardo at EB&S>
 */
public class ManagedBeanCuentaPrueba implements Serializable {

    private MEmpresa empresa;
    private MDireccion direccion;
    private MAcceso acceso;
    private MPerfil perfil;
    private DireccionDAO daoDir;
    private EmpresaDAO daoEmp;
    private UsuarioDAO daoUser;
    private PerfilDAO daoPerfil;
    private SendMail enviomail;
    private ControlPasword contPass;
    private ConfigDAO daoParam;
    private String appContext;



    /**
     * Creates a new instance of ManagedBeanAutoRegistro
     */
    public ManagedBeanCuentaPrueba() {

    }

    @PostConstruct
    public void init() {
        empresa = new MEmpresa();
        direccion = new MDireccion();
        daoDir = new DireccionDAO();
        daoEmp = new EmpresaDAO();
        daoUser = new UsuarioDAO();
        daoPerfil = new PerfilDAO();
        enviomail = new SendMail();
        contPass = new ControlPasword();
        daoParam = new ConfigDAO();
        HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        appContext = httpServletRequest.getContextPath();

    }

    /**
     * Registro de una cuenta de prueba, limite de tiempo no definido
     * 
     */
    public void registrar() {
//        this.empresa.escapeSqlAndHtmlCharacters();
//        this.direccion.escapeSqlAndHtmlCharacters();
        System.out.println("Cuenta de prueba");
        if (daoEmp.BuscarEmpresaRFC(empresa.getRfcOrigen().trim()) == null) {
            if (daoDir.GuardarActualizaDireccion(direccion)) {
                empresa.setDireccion(direccion);
                if (daoEmp.GuardarOActualizaEmpresa(empresa)) {
                    perfil = new MPerfil(20, 1L, "Administrador", empresa);
                    if (daoPerfil.guardarActualizarPerfil(perfil)) {
                        String clave = contPass.GeneraccionPass();
                        String ClaveFinal = contPass.getEncriptPW(empresa.getRfcOrigen().trim(), clave);
                        List<MEmpresa> empresas = new ArrayList<MEmpresa>();
                        empresas.add(empresa);
                        acceso = new MAcceso(empresa.getRfcOrigen()
                                .trim(), ClaveFinal, perfil,
                                empresa.getCorreoContacto(),
                                empresa.getNombreContacto(), 3, 0,
                                1, empresa.getRfcOrigen(), empresa,
                                MAcceso.Nivel.EXTERNO);
                        acceso.setEmpresas(empresas);
                        if (daoUser.guardarActualizarUsuario(acceso)) {
                            String idDatos = empresa.getRfcOrigen() + "," + empresa.getId() + "," + acceso.getId();
                            String Url = null;
                            MConfig paramUrl = daoParam.BuscarConfigDatoClasificacion("ACTIVAR_USUARIO", "URL_DEL_PORTAL");
                            if (paramUrl != null) {
                                Url = paramUrl.getValor().trim();
                                Url = Url + "login.action?id=" + encrypt(idDatos);
                                String mensaje = "Para terminar su registro a Facturación Electrónica pulse el link:  "
                                        + Url + " \n para realizar su confirmación. Y acceda al portal  con el siguiente: "
                                        + "\n\nUsuario: " + empresa.getRfcOrigen()
                                        + "\n\nPassword: "
                                        + clave
                                        + "\n\n Saludos.";
                                String[] mails = empresa.getCorreoContacto().split(";");
                                if (enviomail.sendEmail("Confirmación del Auto-Registro en Electronic Bills & Services", mensaje, mails)) {
                                    Limpiar();
                                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Se ah enviado un correo para confirmar su registro, y pueda acceder al sistema.", "Info"));
                                    //result = "salir";Redirect login
                                } else {
                                    Limpiar();
                                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No se logro enviar el correo favor de comunicarse con el proveedor, solicitando su usuario.", "Info"));
                                    //result = "salir";Redirect login

                                }
                            } else {
                                Limpiar();
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No se logro enviar el correo favor de comunicarse con el proveedor, solicitando su usuario.", "Info"));
                                //result = "salir";Redirect login
                            }
                        } else {
                            daoPerfil.BorrarPerfil(perfil);
                            daoEmp.BorrarEmpresa(empresa);
                            daoDir.BorrarDireccion(direccion);
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Lo sentimos, en estos momentos se pudo registrar la empresa, favor de intentarlo mas tarde.", "Info"));
                        }

                    } else {
                        daoEmp.BorrarEmpresa(empresa);
                        daoDir.BorrarDireccion(direccion);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Lo sentimos, en estos momentos se pudo registrar la empresa, favor de intentarlo mas tarde.", "Info"));
                    }
                } else {
                    daoDir.BorrarDireccion(direccion);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Lo sentimos, en estos momentos se pudo registrar la empresa, favor de intentarlo mas tarde.", "Info"));
                }

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Lo sentimos, en estos momentos se pudo registrar la empresa, favor de intentarlo mas tarde.", "Info"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "La empresa ya esta registrada, si tiene problemas de acceder, favor de llamar al provedor.", "Info"));
        }

    }

    public void Limpiar() {
        perfil = null;
        acceso = null;
        empresa = null;
        direccion = null;
    }

    private String encrypt(String cadena) {
        StandardPBEStringEncryptor s = new StandardPBEStringEncryptor();
        s.setPassword("uniquekey");
        return s.encrypt(cadena);
    }

    /**
     * Redirecciona a la pagina de inicio de sesion
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
     * @return the direccion
     */
    public MDireccion getDireccion() {
        return direccion;
    }

    /**
     * @return the acceso
     */
    public MAcceso getAcceso() {
        return acceso;
    }

    /**
     * @return the perfil
     */
    public MPerfil getPerfil() {
        return perfil;
    }

    /**
     * @param direccion the direccion to set
     */
    public void setDireccion(MDireccion direccion) {
        this.direccion = direccion;
    }

    /**
     * @param acceso the acceso to set
     */
    public void setAcceso(MAcceso acceso) {
        this.acceso = acceso;
    }

    /**
     * @param perfil the perfil to set
     */
    public void setPerfil(MPerfil perfil) {
        this.perfil = perfil;
    }

    /**
     * @return the empresa
     */
    public MEmpresa getEmpresa() {
        return empresa;
    }

    /**
     * @param empresa the empresa to set
     */
    public void setEmpresa(MEmpresa empresa) {
        this.empresa = empresa;
    }

}
