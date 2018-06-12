/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.mbeans;

import fe.db.MAcceso;
import fe.db.MAcceso.Nivel;
import fe.db.MConfig;
import fe.db.MEmpresa;
import fe.db.MPerfil;
import fe.model.dao.*;
import fe.model.util.ControlPasword;
import fe.model.util.SendMail;
import org.primefaces.model.DualListModel;

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
 * Managed bean para la administracion de los usuarios, funciona para usuarios
 * logeados de nivelNuevoUsuario 0 o nivelNuevoUsuario 1
 *
 * @author Eduardo C. Flores Ambrosio <Eduardo at EB&S>
 */
public class ManagedBeanUsuarios implements Serializable {

    private final int NONE = -1;
    private final int CREACION = 0;
    private final int MODIFICACION = 1;

    private final int NIVEL_INTERNO = 0;
    private final int NIVEL_EXTERNO = 1;
    private MAcceso usuarioAcceso;
    //Modelos

    //Contexto
    private HttpServletRequest httpServletRequest;
    private FacesContext facesContext;
    private String appContext;


    private List<MAcceso> usuarios;
    private List<MPerfil> perfiles;
    private List<MEmpresa> empresasTarget;//Empresas disponibless
    private List<MEmpresa> empresasSource;//Empresas seleccionadas
    private List<MEmpresa> empresasPadres;//Todas las empresas
    private MAcceso usuarioSelected;
    private MEmpresa empresaPadre;
    private MPerfil perfil;
    private DualListModel<MEmpresa> allEmpresas;
    private MAcceso usuarioAccesoAux;
    private int empresaIdFiltro;
    private int idUsuario;
    private int idEmpPadre;
    private int idPerfil;
    private int nivelNuevoUsuario = -1;///Solo para usuarios logeados como internos
    private String paramBusqueda;
    private String tipoBus;
    private String clave;
    private String passGenericoTmp;
    private int estatus;
    private boolean bolFlag;
    private ControlPasword ctrlpass;
    private SendMail mailManager;
    //Bandera para indicar el tipo de operacion que se esta realizando
    //0-Creacion de una nueva empresa
    //1-Modificacion de una existente
    private int currentOperation = NONE;

    //DAOS
    private EmpresaDAO daoEmpresa;
    private UsuarioDAO daoUsuario;
    private LogAccesoDAO daoLog;
    private PerfilDAO daoPerfil;
    private ConfigDAO daoCon;

    private static String URL_PORTAL = "http://localhost:8080/FacturacionElectronica/";

    /**
     * Creates a new instance of ManagedBeanUsuarios
     */
    public ManagedBeanUsuarios() {

    }

    public static String getURL_PORTAL() {
        return ManagedBeanUsuarios.URL_PORTAL;
    }

    @PostConstruct
    public void init() {
        facesContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        usuarioAcceso = (MAcceso) httpSession.getAttribute("macceso");
        appContext = httpServletRequest.getContextPath();
        idUsuario = usuarioAcceso.getId();
        nivelNuevoUsuario = (usuarioAcceso.getNivel() == Nivel.INTERNO) ? 0 : 1;
        daoEmpresa = new EmpresaDAO();
        daoUsuario = new UsuarioDAO();
        daoLog = new LogAccesoDAO();
        tipoBus = "Ninguno";
        usuarioAccesoAux = new MAcceso();

        daoPerfil = new PerfilDAO();
        ctrlpass = new ControlPasword();
        mailManager = new SendMail();
        daoCon = new ConfigDAO();
        bolFlag = true;
        //
        empresasPadres = daoEmpresa.ListaEmpresasPadres(idUsuario);
        empresasTarget = new ArrayList();
        empresasSource = new ArrayList();
        allEmpresas = new DualListModel<MEmpresa>(empresasTarget, empresasSource);

        MConfig cnfg1 = daoCon.BuscarConfigDatoClasificacion("ACTIVAR_USUARIO_V2", "URL_DEL_PORTAL");
        ManagedBeanUsuarios.URL_PORTAL = (cnfg1.getValor() != null) ? cnfg1.getValor() : ManagedBeanUsuarios.URL_PORTAL;
    }

    /**
     * Registrar un nuevo usuario en la base de datos, Este metodo es usado para
     * todos los usuarios logoeados con nivel de externo
     * Los usuarios externos solo pueden ver usuarios externos
     */
    public void salvarOActualizarUsuarioExterno() {
        String PWD_DUMIE;
        boolean resOperacion = false;
        String mensaje = "";
        String asunto = "";
        if (validaNuevoRegistro()) {
            if (currentOperation == CREACION) {//Creacion
                ///TRIM USER NICK AND EMAIL
                usuarioAccesoAux.setUsuario(usuarioAccesoAux.getUsuario().trim());
                usuarioAccesoAux.setEmail(usuarioAccesoAux.getEmail().trim());

                PWD_DUMIE = ctrlpass.GeneraccionPass();
                usuarioAccesoAux.setEmpresa(daoEmpresa.BuscarEmpresaId(idEmpPadre));
                usuarioAccesoAux.setPerfil(daoPerfil.BuscarPerfil(idPerfil));
                usuarioAccesoAux.setNivel(MAcceso.Nivel.EXTERNO);
                if (allEmpresas.getTarget().size() > 0) {
                    usuarioAccesoAux.setEmpresas(allEmpresas.getTarget());
                } else {
                    usuarioAccesoAux.setEmpresas(null);
                }
                usuarioAccesoAux.setClave(ctrlpass.getEncriptPW(
                        usuarioAccesoAux.getUsuario(),
                        PWD_DUMIE));
                usuarioAccesoAux.setEstatus(estatus);
                if (daoUsuario.guardarActualizarUsuario(usuarioAccesoAux)) {
                    mensaje = "Registro al usuario="
                            + usuarioAccesoAux.getUsuario() + " con id="
                            + usuarioAccesoAux.getId();
                    daoLog.guardaRegistro(usuarioAcceso, mensaje);
                    asunto = "Envio de password para el acceso al sistema de Facturación Electrónica";
                    mensaje = "¡Buen Dia! Sr(a)."
                            + usuarioAccesoAux.getNombre()
                            + "\n\nSe le envia sus datos para acceder al sistema\n\n\n\nUsuario:"
                            + usuarioAccesoAux.getUsuario()
                            + " \n\nPassword: " + PWD_DUMIE
                            + "\n\n\nUrl: " + ManagedBeanUsuarios.URL_PORTAL
                            + "\n\n\nSaludos.";
                    String[] mail = usuarioAccesoAux.getEmail().split(";");
                    if (!mailManager.sendEmail(asunto, mensaje, mail)) {
                        FacesContext.getCurrentInstance().addMessage("frmAddUsuario",
                                new FacesMessage(FacesMessage.SEVERITY_INFO,
                                        "No se pudo enviar el correo electronico, favor de rectificarlo.",
                                        "Info"));
                    }
                    ///Necesario???
                    this.empresasPadres = daoEmpresa.ListaEmpresasPadres(usuarioAcceso.getId());
                    FacesContext.getCurrentInstance().addMessage("frmAddUsuario",
                            new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "El usuario se registro correctamente.",
                                    "Info"));
                    resOperacion = true;
                    this.resetCreacion();
                }
            } else if (currentOperation == MODIFICACION) {//Modificacion
                usuarioAccesoAux.setEmail(usuarioAccesoAux.getEmail().trim());

                usuarioAccesoAux.setPerfil(daoPerfil.BuscarPerfil(idPerfil));
                usuarioAccesoAux.setEstatus(estatus);
                usuarioAccesoAux.setNivel(Nivel.EXTERNO);
                if (estatus == 1) {
                    usuarioAccesoAux.setIntentos(0);
                }
                if (allEmpresas.getTarget() != null) {
                    usuarioAccesoAux.setEmpresas(allEmpresas.getTarget());
                } else {
                    usuarioAccesoAux.setEmpresas(null);
                }
                ///Solo se modifica cuando se a ingresado una clave valida
                if (clave != null && !clave.equals("")) {
                    usuarioAccesoAux.setClave(ctrlpass.getEncriptPW(usuarioAccesoAux.getUsuario(), clave.trim()));
                }
                if (daoUsuario.guardarActualizarUsuario(usuarioAccesoAux)) {
                    mensaje = "Modifico al usuario=" + usuarioAccesoAux.getUsuario() + " con id=" + usuarioAccesoAux.getId();
                    daoLog.guardaRegistro(usuarioAcceso, mensaje);
                    limpiar();
                    FacesContext.getCurrentInstance().addMessage("frmAdminUsuarios",
                            new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "El usuario se modifico correctamente.", "Info"));
                } else {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Se ha producido un error al intentar crear el usuario", "Error"));
                }
            }
        }
        if (resOperacion) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            redirectConsultaUsuarios();
        }

    }

    /**
     * Para usuarios internos que ven el xhtml de admin para esos usuarios
     * Los usuarios internos son los usuarios que pertenecen a ebs
     */
    public void salvarOActualizarUsuarioInterno() {
        String PWD_DUMIE;
        boolean resOperacion = false;
        String mensaje = "";
        String asunto = "";
//        this.usuarioAccesoAux.escapeSqlAndHtmlCharacters();
        if (validaNuevoRegistro()) {
            if (currentOperation == CREACION) {///Creacion
                ///TRIM USERNICK
                usuarioAccesoAux.setUsuario(usuarioAccesoAux.getUsuario().trim());
                usuarioAccesoAux.setEmail(usuarioAccesoAux.getEmail().trim());

                PWD_DUMIE = ctrlpass.GeneraccionPass();
                usuarioAccesoAux.setEmpresa(daoEmpresa.BuscarEmpresaId(idEmpPadre));
                usuarioAccesoAux.setPerfil(daoPerfil.BuscarPerfil(idPerfil));
                if (nivelNuevoUsuario == 1) {//EXTERNO
                    ///Si el usuario que estamos registrando es externo agregamos las empresas que se seleccioanron
                    if (allEmpresas.getTarget().size() > 0) {
                        usuarioAccesoAux.setEmpresas(allEmpresas.getTarget());
                    } else {
                        usuarioAccesoAux.setEmpresas(null);
                    }
                    usuarioAccesoAux.setNivel(Nivel.EXTERNO);
                } else {//INTERNO
                    /// si es interno ponemos en null las empresas
                    usuarioAccesoAux.setNivel(Nivel.INTERNO);
                    if (allEmpresas.getTarget().size() > 0) {
                        usuarioAccesoAux.setEmpresas(allEmpresas.getTarget());
                    } else {
                        usuarioAccesoAux.setEmpresas(null);
                    }
                }
                //Generamos un password dummie
                // TODO: 11/09/2017 reemplazar el password enviado al usuario por UN TOKEN, PASAR POR GET EL CODIGO AL XHTML
                usuarioAccesoAux.setClave(ctrlpass.getEncriptPW(usuarioAccesoAux.getUsuario(), PWD_DUMIE));
                usuarioAccesoAux.setEstatus(estatus);
                if (daoUsuario.guardarActualizarUsuario(usuarioAccesoAux)) {
                    mensaje = "Registro del usuario=" + usuarioAccesoAux.getUsuario() + " con id=" + usuarioAccesoAux.getId();
                    daoLog.guardaRegistro(usuarioAcceso, mensaje);
                    asunto = "Envio de password para el acceso al sistema de Facturación Electrónica";
                    mensaje = "¡Buen Dia! Sr(a)."
                            + usuarioAccesoAux.getNombre()
                            + "\n\nSe le envia sus datos para acceder al sistema\n\n\n\nUsuario:"
                            + usuarioAccesoAux.getUsuario()
                            + " \n\nPassword: " + PWD_DUMIE
                            + "\n\n\nUrl: " + ManagedBeanUsuarios.URL_PORTAL
                            + "\n\n\nSaludos.";
                    String[] mail = usuarioAccesoAux.getEmail().split(";");
                    if (!mailManager.sendEmail(asunto, mensaje, mail)) {
                        FacesContext.getCurrentInstance().addMessage("frmAddUsuario", new FacesMessage(
                                FacesMessage.SEVERITY_INFO, "No se pudo enviar el correo electronico, favor de rectificarlo.", "Info"));
                    }
                    ///Necesario???
                    this.empresasPadres = daoEmpresa.ListaEmpresasPadres(usuarioAcceso.getId());
                    FacesContext.getCurrentInstance().addMessage("frmAddUsuario", new FacesMessage(
                            FacesMessage.SEVERITY_INFO, "El usuario se registro correctamente.", "Info"));
                    resOperacion = true;
                }
            } else if (currentOperation == MODIFICACION) {
                usuarioAccesoAux.setEmail(usuarioAccesoAux.getEmail().trim());
                usuarioAccesoAux.setPerfil(daoPerfil.BuscarPerfil(idPerfil));
                usuarioAccesoAux.setEstatus(estatus);
                if (estatus == 1) {
                    usuarioAccesoAux.setIntentos(0);
                }
                if (nivelNuevoUsuario == 0) {
                    usuarioAccesoAux.setNivel(MAcceso.Nivel.INTERNO);
                    if (allEmpresas.getTarget().size() > 0) {
                        usuarioAccesoAux.setEmpresas(allEmpresas.getTarget());
                    } else {
                        usuarioAccesoAux.setEmpresas(null);
                    }
                } else {
                    usuarioAccesoAux.setNivel(MAcceso.Nivel.EXTERNO);
                    if (allEmpresas.getTarget().size() > 0) {
                        usuarioAccesoAux.setEmpresas(allEmpresas.getTarget());
                    } else {
                        usuarioAccesoAux.setEmpresas(null);
                    }
                }
                ///Solo se modifica cuando se a ingresado una clave valida
                if (clave != null && !clave.equals("")) {
                    usuarioAccesoAux.setClave(ctrlpass.getEncriptPW(usuarioAccesoAux.getUsuario(), clave.trim()));

                }
                if (daoUsuario.guardarActualizarUsuario(usuarioAccesoAux)) {
                    mensaje = "Modifico al usuario=" + usuarioAccesoAux.getUsuario() + " con id=" + usuarioAccesoAux.getId();
                    daoLog.guardaRegistro(usuarioAcceso, mensaje);
                    limpiar();
                    FacesContext.getCurrentInstance().addMessage("frmAdminUsuarios",
                            new FacesMessage(
                                    FacesMessage.SEVERITY_INFO,
                                    "El usuario se modifico correctamente.",
                                    "Info"));
                    resOperacion = true;
                } else {
                    FacesContext.getCurrentInstance().addMessage("frmAdminUsuarios", new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, "Se ha producido un error al intentar crear el usuario",
                            "Info"));
                }
            }
        }

        if (resOperacion) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            redirectConsultaUsuarios();
        }
    }

    /**
     * Redirecciona a la pagina de creacion de un usuario nuevo externo
     * dependiendo del nivelNuevoUsuario del usuario que esta en sesion
     */
    public void redirectCreacionUsuario() {
        this.currentOperation = CREACION;//Creacion
        this.resetCreacion();//
        try {
            if (usuarioAcceso.getNivel() == Nivel.EXTERNO) {
                empresasPadres = (daoEmpresa.ListaEmpresasPadres(usuarioAcceso.getId()));
                FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/admin/usuarios/nuevoUsuarioCliente.xhtml");
            }
            if (usuarioAcceso.getNivel() == Nivel.INTERNO) {
                FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/admin/usuarios/nuevoUsuarioInterno.xhtml");
            }
        } catch (IOException e1) {
            e1.printStackTrace();
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "A ocurrido un error al redireccionar.", "Error"));
        }
    }

    public void redirectModificacionUsuario() {
        if (usuarioSelected != null) {
            this.currentOperation = MODIFICACION;//Indicamos que estamos modificando
            this.resetModificacion();
            usuarioAccesoAux = daoUsuario.BuscarUsuarioId(usuarioSelected.getId());//Map data
            this.empresasTarget = daoEmpresa.ListaAsignadas(usuarioAccesoAux.getId());
            Integer[] idsemp = null;
            if (empresasTarget != null) {
                idsemp = new Integer[empresasTarget.size()];
                for (int i = 0; i < empresasTarget.size(); i++) {
                    idsemp[i] = empresasTarget.get(i).getId();
                }
            } else {
                empresasTarget = new ArrayList<MEmpresa>();
            }
            this.empresasSource = daoEmpresa.ListaPosiblesEmp(usuarioAccesoAux.getEmpresa().getId(), idsemp);
            if (empresasSource == null) {
                empresasSource = new ArrayList<MEmpresa>();
            }
            allEmpresas = new DualListModel<MEmpresa>(empresasSource, empresasTarget);
            idEmpPadre = usuarioAccesoAux.getEmpresa().getId();
            this.perfiles = daoPerfil.ListaPerfiles(idEmpPadre);
            idPerfil = usuarioAccesoAux.getPerfil().getId();
            estatus = usuarioAccesoAux.getEstatus();
            if (usuarioAccesoAux.getNivel() == Nivel.INTERNO) {
                nivelNuevoUsuario = 0;//Interno
            } else {
                nivelNuevoUsuario = 1;//Externo
            }
            try {
                if (usuarioAcceso.getNivel() == Nivel.EXTERNO) {
                    FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/admin/usuarios/nuevoUsuarioCliente.xhtml");
                }
                if (usuarioAcceso.getNivel() == Nivel.INTERNO) {
                    FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/admin/usuarios/nuevoUsuarioInterno.xhtml");
                }
            } catch (IOException e1) {
                e1.printStackTrace();
                FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "A ocurrido un error al redireccionar.", "Error"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, "Debes seleccionar un usuario.", "Error"));
        }
    }

    /**
     * Redirecciona a la pagina de consulta de usuarios
     */
    public void redirectConsultaUsuarios() {
        currentOperation = NONE;
        nivelNuevoUsuario = -1;
        paramBusqueda  = "";
        tipoBus = "Todos";
        usuarioSelected = null;
        empresasPadres = daoEmpresa.ListaEmpresasPadres(usuarioAcceso.getId());
        usuarios = daoUsuario.ListaUsuarios(usuarioAcceso.getId(), empresaIdFiltro, "Todos", "");
        try {
            if (usuarioAcceso.getNivel() == MAcceso.Nivel.EXTERNO) {
                FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/admin/usuarios/adminUsuariosExterno.xhtml");
            } else if (usuarioAcceso.getNivel() == MAcceso.Nivel.INTERNO) {
                FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/admin/usuarios/adminUsuariosInterno.xhtml");
            }
        } catch (IOException e1) {
            e1.printStackTrace();
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "A ocurrido un error al redireccionar.", "Error"));
        }
    }

    /**
     * Muestra la lista de usuarios registrados
     */
    public void cargarListaUsuarios() {
        if (usuarios != null) {
            usuarios.clear();
        }
        if (validarBusqueda()) {
            empresasPadres = daoEmpresa.ListaEmpresasPadres(usuarioAcceso.getId());
            usuarios = daoUsuario.ListaUsuarios(usuarioAcceso.getId(), empresaIdFiltro, tipoBus, paramBusqueda);
        } else {
            empresasPadres = daoEmpresa.ListaEmpresasPadres(usuarioAcceso.getId());
        }
    }

    public void eliminarUsuario() {
        if (usuarioSelected.getId().intValue() != usuarioAcceso.getId().intValue()) {
            if (daoUsuario.BorrarUsuario(usuarioSelected.getId())) {
                FacesContext.getCurrentInstance().addMessage("frmAdminUsuarios", new FacesMessage(FacesMessage.SEVERITY_INFO, "El usuario se elimino correctamente.", "Info"));
                String mensaje = "Elimino al usuario="
                        + usuarioSelected.getUsuario() + " con id="
                        + usuarioSelected.getId();
                daoLog.guardaRegistro(usuarioAcceso, mensaje);
                usuarios.remove(usuarioSelected);
            } else {
                FacesContext.getCurrentInstance().addMessage("frmAdminUsuarios", new FacesMessage(FacesMessage.SEVERITY_ERROR, "El usuario  no se elimino correctamente.", "Info"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage("frmAdminUsuarios", new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se puede eliminar el usuario: " + usuarioAcceso.getUsuario() + ", ya que es el que se esta ultilizando.", "Info"));
        }
    }

    public boolean validarBusqueda() {
        boolean res = true;
        if (!tipoBus.trim().equals("Ninguno") && !tipoBus.trim().equals("Todos")) {
            if (paramBusqueda == null || "".equals(paramBusqueda.trim())) {
                res = false;
                FacesContext.getCurrentInstance().addMessage("frmAdminUsuarios", new FacesMessage(FacesMessage.SEVERITY_WARN, "Es necesario insertar el valor de busqueda.", "Info"));
            }
        } else{
            paramBusqueda = "";
        }
        return res;
    }

    public void limpiar() {

    }

    public void respuestaSUCCES() {

    }

    public void respuestaINPUT() {

    }

    public void enviarPassword() {
        if (usuarioSelected != null) {
            passGenericoTmp = ctrlpass.GeneraccionPass();
            usuarioSelected.setClave(ctrlpass.getEncriptPW(usuarioSelected.getUsuario(), passGenericoTmp));
            usuarioSelected.setCambiaClave(1);
            if (daoUsuario.guardarActualizarUsuario(usuarioSelected)) {
                String asunto = "Envio de Password de reposición";
                String mensaje = "Buen dia Sr(a). "
                        + usuarioSelected.getNombre()
                        + " Se le envia su password de reposicion y su usuario \n\n Usuario: "
                        + usuarioSelected.getUsuario() + " \n Password: "
                        + passGenericoTmp
                        + "\n\n\nUrl:" + ManagedBeanUsuarios.URL_PORTAL
                        + "\n\n\nSaludos.";
                String[] mail = usuarioSelected.getEmail().split(";");
                if (!mailManager.sendEmail(asunto, mensaje, mail)) {
                    FacesContext.getCurrentInstance().addMessage("",
                            new FacesMessage(FacesMessage.SEVERITY_WARN,
                                    "Tuvimos problemas al enviar el password de reposición.", "Info"));
                } else {
                    FacesContext.getCurrentInstance().addMessage("",
                            new FacesMessage(FacesMessage.SEVERITY_WARN,
                                    "Se ha enviado un correo al usuario con su password de reposición.", "Info"));
                }
                daoLog.guardaRegistro(usuarioAcceso,
                        "Modifico el password al usuario= " + usuarioSelected.getUsuario() + "  con id= " + usuarioSelected.getId());
            }
        } else {
            FacesContext.getCurrentInstance().addMessage("",
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Debes seleccionar un usuario.", "Error"));
        }

    }

    public void cambiarBandera() {
        if (!tipoBus.trim().equals("Ninguno") && !tipoBus.trim().equals("Todos")) {
            bolFlag = false;
        } else {
            bolFlag = true;
        }
    }

    public void cambiarNivel() {
        if (nivelNuevoUsuario >= 0) {
            if (nivelNuevoUsuario == 0) {//INTERNO
                MConfig con = daoCon.BuscarConfigDatoClasificacion("EMPRESA_PRINCIPAL", "RFC_DE_EMPRESA");
                if (con != null) {
                    this.empresaPadre = daoEmpresa.BuscarEmpresaRFC(con.getValor());
                    this.idEmpPadre = this.empresaPadre.getId();
                    this.perfiles = daoPerfil.ListaPerfiles(idEmpPadre);//Empresa principal
                } else {
                    FacesContext.getCurrentInstance().addMessage("frmAddUsuario", new FacesMessage(FacesMessage.SEVERITY_WARN, "Debe insertar primero el parametro EMPRESA_PRINCIPAL y su RFC para poder crear usuario internos.", "Info"));
                }
            } else {//EXTERNO
                actualizarDatos();
            }
        } else {
            resetCreacion();
            FacesContext.getCurrentInstance().addMessage("frmAddUsuario", new FacesMessage(FacesMessage.SEVERITY_WARN, "Seleccione un nivelNuevoUsuario.", "Info"));
        }

    }


    public boolean validaNuevoRegistro() {
        boolean res = true;
        ///El nombre de usuario debe de ser unico
        MAcceso tmpUsu = daoUsuario.BuscarUsuarioUser(usuarioAccesoAux.getUsuario().trim());
        if (tmpUsu != null && currentOperation == CREACION) {
            FacesContext.getCurrentInstance().addMessage("frmAddUsuario", new FacesMessage(FacesMessage.SEVERITY_ERROR, "El usuario ya existe.", "Info"));
            res = false;
        }
        if (allEmpresas.getTarget().isEmpty() && currentOperation == CREACION) {
            //Validar solo cuando el nivel de usuario que se va a registrar es externo(cliente)
            if (nivelNuevoUsuario == 1) {
                FacesContext.getCurrentInstance().addMessage("frmAddUsuario", new FacesMessage(FacesMessage.SEVERITY_WARN, "Debes asignar al menos una empresa.", "Info"));
                res = false;
            }
        }
        return res;
    }

    public void actualizarDatos() {
        this.empresasPadres = daoEmpresa.ListaEmpresasPadres(idUsuario);
        if (empresasSource == null) {
            empresasSource = new ArrayList<MEmpresa>();
        }
        if (empresasTarget == null) {
            empresasTarget = new ArrayList<MEmpresa>();
        }
        if (this.idEmpPadre > 0) {
            this.perfiles = daoPerfil.ListaPerfiles(idEmpPadre);
            this.empresasSource = daoEmpresa.ListaPosiblesEmp(idEmpPadre, null);
        }
        allEmpresas = new DualListModel<MEmpresa>(empresasSource, empresasTarget);
    }

    /**
     * Borra los datos del formulario de creacion de usuarios
     */
    public void resetCreacion() {
        this.idEmpPadre = -1;
        idPerfil = -1;
        usuarioAccesoAux = new MAcceso();
        clave = "";
        estatus = -1;
        nivelNuevoUsuario = -1;
        if (perfiles != null) {
            perfiles.clear();
        }
        //empresasPadres = daoEmpresa.ListaEmpresasPadres(idUsuario);
        empresasSource = new ArrayList<MEmpresa>();
        empresasTarget = new ArrayList<MEmpresa>();
        if (allEmpresas != null) {
            allEmpresas = null;
            allEmpresas = new DualListModel<MEmpresa>(empresasSource, empresasTarget);
        }

    }

    /**
     * Borra los datos para el formulario de modificaion de usuarios
     */
    public void resetModificacion() {
        usuarioAccesoAux = null;
        empresasTarget = null;
        empresasSource = null;
    }

    public int getNONE() {
        return this.NONE;
    }

    public int getCREACION() {
        return this.CREACION;
    }

    public int getMODIFICACION() {
        return this.MODIFICACION;
    }

    public int getNIVEL_INTERNO() {
        return this.NIVEL_INTERNO;
    }

    public int getNIVEL_EXTERNO() {
        return this.NIVEL_EXTERNO;
    }

    public MAcceso getUsuarioAcceso() {
        return this.usuarioAcceso;
    }

    public List<MAcceso> getUsuarios() {
        return this.usuarios;
    }

    public List<MPerfil> getPerfiles() {
        return this.perfiles;
    }

    public List<MEmpresa> getEmpresasTarget() {
        return this.empresasTarget;
    }

    public List<MEmpresa> getEmpresasSource() {
        return this.empresasSource;
    }

    public List<MEmpresa> getEmpresasPadres() {
        return this.empresasPadres;
    }

    public MAcceso getUsuarioSelected() {
        return this.usuarioSelected;
    }

    public MEmpresa getEmpresaPadre() {
        return this.empresaPadre;
    }

    public MPerfil getPerfil() {
        return this.perfil;
    }

    public DualListModel<MEmpresa> getAllEmpresas() {
        return this.allEmpresas;
    }

    public MAcceso getUsuarioAccesoAux() {
        return this.usuarioAccesoAux;
    }

    public int getEmpresaIdFiltro() {
        return this.empresaIdFiltro;
    }

    public int getIdUsuario() {
        return this.idUsuario;
    }

    public int getIdEmpPadre() {
        return this.idEmpPadre;
    }

    public int getIdPerfil() {
        return this.idPerfil;
    }

    public int getNivelNuevoUsuario() {
        return this.nivelNuevoUsuario;
    }

    public String getParamBusqueda() {
        return this.paramBusqueda;
    }

    public String getTipoBus() {
        return this.tipoBus;
    }

    public String getClave() {
        return this.clave;
    }

    public String getPassGenericoTmp() {
        return this.passGenericoTmp;
    }

    public int getEstatus() {
        return this.estatus;
    }

    public boolean isBolFlag() {
        return this.bolFlag;
    }

    public ControlPasword getCtrlpass() {
        return this.ctrlpass;
    }

    public SendMail getMailManager() {
        return this.mailManager;
    }

    public int getCurrentOperation() {
        return this.currentOperation;
    }

    public void setUsuarios(List<MAcceso> usuarios) {
        this.usuarios = usuarios;
    }

    public void setPerfiles(List<MPerfil> perfiles) {
        this.perfiles = perfiles;
    }

    public void setEmpresasTarget(List<MEmpresa> empresasTarget) {
        this.empresasTarget = empresasTarget;
    }

    public void setEmpresasSource(List<MEmpresa> empresasSource) {
        this.empresasSource = empresasSource;
    }

    public void setEmpresasPadres(List<MEmpresa> empresasPadres) {
        this.empresasPadres = empresasPadres;
    }

    public void setUsuarioSelected(MAcceso usuarioSelected) {
        this.usuarioSelected = usuarioSelected;
    }

    public void setEmpresaPadre(MEmpresa empresaPadre) {
        this.empresaPadre = empresaPadre;
    }

    public void setPerfil(MPerfil perfil) {
        this.perfil = perfil;
    }

    public void setAllEmpresas(DualListModel<MEmpresa> allEmpresas) {
        this.allEmpresas = allEmpresas;
    }

    public void setUsuarioAccesoAux(MAcceso usuarioAccesoAux) {
        this.usuarioAccesoAux = usuarioAccesoAux;
    }

    public void setEmpresaIdFiltro(int empresaIdFiltro) {
        this.empresaIdFiltro = empresaIdFiltro;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setIdEmpPadre(int idEmpPadre) {
        this.idEmpPadre = idEmpPadre;
    }

    public void setIdPerfil(int idPerfil) {
        this.idPerfil = idPerfil;
    }

    public void setNivelNuevoUsuario(int nivelNuevoUsuario) {
        this.nivelNuevoUsuario = nivelNuevoUsuario;
    }

    public void setParamBusqueda(String paramBusqueda) {
        this.paramBusqueda = paramBusqueda;
    }

    public void setTipoBus(String tipoBus) {
        this.tipoBus = tipoBus;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public void setPassGenericoTmp(String passGenericoTmp) {
        this.passGenericoTmp = passGenericoTmp;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public void setBolFlag(boolean bolFlag) {
        this.bolFlag = bolFlag;
    }

    public void setCurrentOperation(int currentOperation) {
        this.currentOperation = currentOperation;
    }
}
