/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.mbeans;

import fe.db.EmpresaTimbre_Timbre;
import fe.db.Empresa_EmpresaTimbre;
import fe.db.MAcceso;
import fe.db.MDireccion;
import fe.db.MEmpresa;
import fe.db.MEmpresaMTimbre;
import fe.db.MPerfil;
import fe.db.MTimbre;
import fe.model.action.AuxClassDomain;
import fe.model.dao.DireccionDAO;
import fe.model.dao.EmpresaDAO;
import fe.model.dao.LogAccesoDAO;
import fe.model.dao.LoginDAO;
import fe.model.dao.PerfilDAO;
import fe.model.dao.RelacionEmpresaDAO;
import fe.model.dao.TimbreDAO;
import fe.model.dao.UsuarioDAO;
import fe.model.util.ControlPasword;
import fe.model.util.SendMail;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 * Administracion de empresas
 *
 * @author Eduardo C. Flores Ambrosio <Eduardo at EB&S>
 */
public class ManagedBeanEmpresas implements Serializable {

    //Modelos
    private MAcceso mAcceso;

    //Contexto
    private HttpServletRequest httpServletRequest;
    private FacesContext facesContext;
    private String appContext;


    //Modelos, consulta de empresas
    private List<MEmpresa> empresas;
    private List<MEmpresa> empresasSelect;
    private List<MEmpresa> empresasAux;
    private List<EmpresaTimbre_Timbre> empsTimpsTimps;
    private List<MTimbre> timbres;//Timbre de la empresa seleccionada
    private List<Empresa_EmpresaTimbre> emp_EmpTimp;//Lista de todas las empresas creadass
    private List<Empresa_EmpresaTimbre> emp_EmpTimpAux;//Para la gestion de timbres
    private List<Empresa_EmpresaTimbre> emp_EmpTimpSelecteds;//Aux selection
    private MEmpresaMTimbre empresaTimbre;//Para la compra de timbres
    private MTimbre timbre;
    private String paramBusqueda;
    private String tipoBusqueda;
    private boolean flagTipoBusqueda;
    //DAOS
    private EmpresaDAO daoEmpresas;
    private LogAccesoDAO daoLogs;
    private LoginDAO daoLogin;
    private DireccionDAO daoDireccion;
    private RelacionEmpresaDAO daoRelacion;
    private PerfilDAO daoPerfil;
    private UsuarioDAO daoUser;
    private TimbreDAO daoTimbre;

    private ControlPasword ctrlPass;
    private SimpleDateFormat sdf;
    private SendMail sendMail;

    //Creacion de nueva empresa
    private UploadedFile ufLogo;
    private File fileLogo;
    private int idEmpresaPadre;
    private MEmpresa empresaPadreSelect;
    private MEmpresa nuevaEmpresa;
    private MEmpresa empresaPadre;
    private MEmpresa empresaAux;
    private List<MEmpresa> empresasPadre;
    private MDireccion direccion;
    private int generarUsuario;//USADO EN FE_MULTI
    private boolean boCrearUsuario;//CAMBIADO A BOOLEAN
    private boolean interno = false;
    //    private boolean mostrarTimbres = false;
//    private boolean registrarTimbres = false;
    private int idEmpresa = -1;//El idEmpresa de la empresa seleccionada tanto para la creacion y modificacion
    private Empresa_EmpresaTimbre empTimSelected;//Empresa seleccionada
    private int idDireccion;
    private int idPadre;
    private long idEmpTim = 0;
    private String nombreLogo;
    private MEmpresa empresa;
    //Bandera para indicar el tipo de operacion que se esta realizando
    //0-Creacion de una nueva empresa
    //1-Modificacion de una existente
    private int currentOperation = 0;

    private static final int TIMEOUTPERFIL = 30;//Minutos
    private static final int HEIGHTLOGO = 50;
    private static final int WIDTHLOGO = 150;
    private static final int OFFSETTOLERANCE = 10;

    /**
     * Creates a new instance of ManagedBeanEmpresas
     */
    public ManagedBeanEmpresas() {

    }

    @PostConstruct
    public void init() {
        facesContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        mAcceso = (MAcceso) httpSession.getAttribute("macceso");
        daoRelacion = new RelacionEmpresaDAO();
        daoEmpresas = new EmpresaDAO();
        daoLogs = new LogAccesoDAO();
        daoLogin = new LoginDAO();
        daoDireccion = new DireccionDAO();
        sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        daoPerfil = new PerfilDAO();
        daoUser = new UsuarioDAO();
        daoTimbre = new TimbreDAO();
        ctrlPass = new ControlPasword();
        sendMail = new SendMail();
        nuevaEmpresa = new MEmpresa();
        direccion = new MDireccion();
        timbre = new MTimbre();
        empresa = new MEmpresa();
        empresaTimbre = new MEmpresaMTimbre();
        if (mAcceso.getNivel() == MAcceso.Nivel.INTERNO) {
            setInterno(true);
        } else {
            setInterno(false);
        }
        //timbres = new ArrayList();
        appContext = httpServletRequest.getContextPath();
        tipoBusqueda  = "Todos";
        paramBusqueda  = "";
        buscarEmpresas();
    }

    /**
     * Obtiene las empresas padre y redirecciona para registrar una nueva
     * empresa
     */
    public void redirectNuevaEmpresa() {
        this.reset();
        currentOperation = 0;//Creacion
        this.setEmpresasPadre(daoEmpresas.ListaEmpresasPadres(mAcceso.getId()));
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/admin/empresas/nuevaEmpresa.xhtml");
        } catch (IOException e1) {
            System.out.println(">>>>>>>>>>IOException on redirectNuevaEmpresa()" + e1.getMessage());
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "A ocurrido un error al redireccionar.", "Error"));
        }
    }

    public void redirectModificarEmpresa() {
        currentOperation = 1;//Modificacion
        if (empTimSelected != null) {
            this.setEmpresasPadre(daoEmpresas.ListaEmpresasPadres(mAcceso.getId()));
            //nuevaEmpresa = daoEmpresas.BuscarEmpresaId(empTimSelected.getEmpresa().getId());//HIBERNATE
            nuevaEmpresa = empTimSelected.getEmpresa();//PRIMEFACES
            idEmpresa = nuevaEmpresa.getId();
            idDireccion = nuevaEmpresa.getDireccion().getId();
            direccion = nuevaEmpresa.getDireccion();
            empresaPadre = daoEmpresas.BuscarEmpresaPadre(empTimSelected.getEmpresa().getId());
            idEmpresaPadre = (empresaPadre != null) ? empresaPadre.getId() : -1;
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/admin/empresas/nuevaEmpresa.xhtml");
            } catch (IOException e1) {
                System.out.println(">>>>>>>>>>IOException on redirectConsultarEmpresas()" + e1.getMessage());
                FacesContext.getCurrentInstance().addMessage("frmAdminEmpresas", new FacesMessage(FacesMessage.SEVERITY_ERROR, "A ocurrido un error al redireccionar.", "Error"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage("frmAdminEmpresas", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debes seleccionar una empresa para modificar.", "Error"));
        }

    }

    public void redirectConsultarEmpresas() {
        this.reset();
//        System.out.println("Redireccionando consulta de empresas");
        if (mAcceso != null) {
            this.setEmpresasPadre(daoEmpresas.ListaEmpresasPadres(mAcceso.getId()));

            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/admin/administracionEmpresas.xhtml");
            } catch (IOException e1) {
                System.out.println(">>>>>>>>>>IOException on redirectConsultarEmpresas()" + e1.getMessage());
                FacesContext.getCurrentInstance().addMessage("frmAdminEmpresas", new FacesMessage(FacesMessage.SEVERITY_ERROR, "A ocurrido un error al redireccionar.", "Error"));
            }
        }
    }

    /**
     * Registra una nueva empresa
     */
    public void registrarNuevaEmpresa() {
        if (ufLogo != null) {
            System.out.println("Logo" + ufLogo.getFileName());
        }
        String respuesta = "";
        if (validar()) {
            if (getIdEmpresa() > 0) {
                MEmpresa emp = daoEmpresas.BuscarEmpresaId(getIdEmpresa());
                emp.setRfcOrigen(nuevaEmpresa.getRfcOrigen());
                emp.setRazonSocial(nuevaEmpresa.getRazonSocial());
                emp.setNombreContacto(nuevaEmpresa.getNombreContacto());
                emp.setCelularContacto(nuevaEmpresa.getCelularContacto());
                emp.setCorreoContacto(nuevaEmpresa.getCorreoContacto());
                emp.setTelefonoContacto(nuevaEmpresa.getTelefonoContacto());

                if (nuevaEmpresa.getColorWeb()!= null && nuevaEmpresa.getColorWeb().trim().equals("-1")) {
                    nuevaEmpresa.setColorWeb(null);
                }
                if (interno) {
                    emp.setEstatusEmpresa(nuevaEmpresa.getEstatusEmpresa());
                    emp.setTipo_Plantilla(nuevaEmpresa.getTipo_Plantilla());
                }
                if (ufLogo != null && ufLogo.getContents().length > 0) {
                    nombreLogo = emp.getRfcOrigen() + sdf.format(new Date()) + ".png";
                    java.net.URL path = AuxClassDomain.class.getProtectionDomain().getCodeSource().getLocation();
                    String rutaFinal = daoLogin.ObtenerRuta(path.getPath(), "FacturacionElectronica") + "/resources/logos/";//Ruta usando la carpeta de recursos
                    //String rutaFinal = daoLogin.ObtenerRuta(path.getPath(), "FacturacionElectronica") + "/Dweb/Imagen/";
                    rutaFinal = rutaFinal.substring(1, rutaFinal.length());
                    File fileToCreate = new File(rutaFinal, nombreLogo);
                    try {
                        //FileUtils.copyFile(this.logo, fileToCreate);
                        FileUtils.copyInputStreamToFile(this.getUfLogo().getInputstream(), fileToCreate);
                    } catch (IOException e) {
                        System.out.println(">>>>>>>>>>IOException on registrarNuevaEmpresa() " + e.getMessage());
                    }
                    emp.setLogo(nombreLogo);
                }

                if (idDireccion > 0) {
                    getDireccion().setId(idDireccion);
                    if (daoDireccion.GuardarActualizaDireccion(getDireccion())) {

                    }
                }

                if (daoEmpresas.GuardarOActualizaEmpresa(emp)) {
//                    System.out.println("La empresa se modifico correctamente");
                    FacesContext.getCurrentInstance().addMessage("frmAddEmpresa", new FacesMessage(FacesMessage.SEVERITY_INFO, "La empresa se modifico correctamente.", "Info"));
                    String mensaje = "Modifico la empresa " + emp.RFC_Empresa();
                    daoLogs.guardaRegistro(mAcceso, mensaje);
                } else {
                    FacesContext.getCurrentInstance().addMessage("frmAddEmpresa", new FacesMessage(FacesMessage.SEVERITY_WARN, "La empresa no se logro modificar.", "Info"));
//                    System.out.println("La empresa no se logro registrar");
                }
            } else {//Creacion de una nueva empresa
//                System.out.println("Creando empresa");
                if (getUfLogo() != null  && ufLogo.getContents().length > 0) {
                    nombreLogo = nuevaEmpresa.getRfcOrigen() + sdf.format(new Date()) + ".png";
                    //java.net.URL path = LoginAction.class.getProtectionDomain().getCodeSource().getLocation();
                    // TODO: 21/08/2017 GUARDAR LAS IMAGENES EN UN PATH DIFERENTE QUE DONDE SE DEPLOYA EL WAR,cargarlas usando una llamada a un APIREST
                    java.net.URL path = AuxClassDomain.class.getProtectionDomain().getCodeSource().getLocation();
                    String rutaFinal = daoLogin.ObtenerRuta(path.getPath(), "FacturacionElectronica") + "/resources/logos/";//Ruta usando la carpeta de recursos
                    //String rutaFinal = daoLogin.ObtenerRuta(path.getPath(), "FacturacionElectronica") + "/Dweb/Imagen/";
                    rutaFinal = rutaFinal.substring(1, rutaFinal.length());
                    File fileToCreate = new File(rutaFinal, nombreLogo);
                    try {
                        FileUtils.copyInputStreamToFile(this.getUfLogo().getInputstream(), fileToCreate);
                    } catch (IOException e) {

                        System.out.println(">>>>>>>>>>IOException on registrarNuevaEmpresa() " + e.getMessage());
                    }
                } else {
                    nombreLogo = null;
                }
                nuevaEmpresa.setLogo(nombreLogo);
                if (daoDireccion.GuardarActualizaDireccion(getDireccion())) {
                    nuevaEmpresa.setDireccion(getDireccion());
                }
                nuevaEmpresa.setTipoRegistro(0);
                if (nuevaEmpresa.getColorWeb().trim().equals("-1")) {
                    nuevaEmpresa.setColorWeb(null);
                }
                if (mAcceso.getNivel() == MAcceso.Nivel.INTERNO) {
                    nuevaEmpresa.setEstatusEmpresa(1);
                } else {
                    nuevaEmpresa.setEstatusEmpresa(2);
                }
                nuevaEmpresa.setImpSaldoDisponible(0d);
                if (daoEmpresas.GuardarOActualizaEmpresa(nuevaEmpresa)) {
                    String mensaje = "Registro la empresa" + nuevaEmpresa.RFC_Empresa();
                    daoLogs.guardaRegistro(mAcceso, mensaje);
                    mAcceso = daoUser.BuscarUsuarioId(mAcceso.getId());
                    if (mAcceso != null) {
                        if (mAcceso.getNivel() == MAcceso.Nivel.INTERNO) {
                            if (idPadre > 0) {
                                daoRelacion.GuardarRelacionEmpresas(idPadre, nuevaEmpresa.getId());
                            }
                        } else {
                            daoRelacion.GuardarRelacionEmpresas(mAcceso.getEmpresa().getId(), nuevaEmpresa.getId());
                        }
                    }
                    if (boCrearUsuario) {
                        MPerfil perfil = new MPerfil(TIMEOUTPERFIL, 1L, "Administrador", nuevaEmpresa);
                        mensaje = null;
                        if (daoPerfil.guardarActualizarPerfil(perfil)) {
                            mensaje = "Registro del perfil: " + perfil.getTipoUser() + " de la empresa:" + nuevaEmpresa.RFC_Empresa();
                            daoLogs.guardaRegistro(mAcceso, mensaje);
                            String clave = ctrlPass.GeneraccionPass();
                            String passFinal = ctrlPass.getEncriptPW(nuevaEmpresa.getRfcOrigen().trim(), clave);
                            empresas = null;
                            empresas = new ArrayList<>();
                            empresas.add(nuevaEmpresa);
                            MAcceso access = new MAcceso(nuevaEmpresa
                                    .getRfcOrigen().trim(),
                                    passFinal, perfil,
                                    nuevaEmpresa.getCorreoContacto(),
                                    nuevaEmpresa.getNombreContacto(),
                                    new Integer(1), new Date(),
                                    new Integer(0), new Date(),
                                    new Date(), 1,
                                    nuevaEmpresa.getRfcOrigen(), null,
                                    nuevaEmpresa, empresas,
                                    MAcceso.Nivel.EXTERNO);
                            if (daoUser.guardarActualizarUsuario(access)) {
                                mensaje = "Registro del usuario"
                                        + access.getUsuario()
                                        + " de la empresa: "
                                        + nuevaEmpresa.RFC_Empresa();
                                daoLogs.guardaRegistro(mAcceso, mensaje);
                                sendMail = new SendMail();
                                String asunto = "Se le envian su datos del usuario que se genero para la empresa  \t\r"
                                        + nuevaEmpresa.RFC_Empresa();
                                mensaje = "Buen dia Sr(a). "
                                        + access.getNombre()
                                        + "\n\n\n"
                                        + "Se le envia su usuario y password \n\n\nUsuario:"
                                        + access.getUsuario()
                                        + " \n\nPassword:" + clave
                                        + "\n\n\nSaludos.";
                                String[] mail = access.getEmail().split(";");
                                if (!sendMail.sendEmail(asunto, mensaje, mail)) {
//                                    System.out.println("No se pudo enviar el correo");
                                    FacesContext.getCurrentInstance().addMessage("frmAddEmpresa", new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo enviar por correo, por que es incorrecto favor de rectificarlo.", "Info"));
                                }
                            }
                        }
                    }
                    //Reset the data and notify
                    this.reset();
                    this.buscarEmpresas();
                    FacesContext.getCurrentInstance().addMessage("frmAddEmpresa", new FacesMessage(FacesMessage.SEVERITY_INFO, "La empresa se ha registrado correctamente.", "Info"));
                } else {
//                    System.out.println("La empresa no se logro registrar correctametne");
                    FacesContext.getCurrentInstance().addMessage("frmAddEmpresa", new FacesMessage(FacesMessage.SEVERITY_ERROR, "La empresa no se logro registra correctamente.", "Info"));
                    if (interno) {
                        setEmpresas(daoEmpresas.ListaEmpresasPadres(mAcceso.getId()));
                        emp_EmpTimp = daoEmpresas.ListaEmpEmpTim(empresas);
                    }
                }
            }
        } else {
            respuesta = "nuevo";
        }
        if (respuesta.equals("nuevo")) {
            if (interno) {
                setEmpresas(daoEmpresas.ListaEmpresasPadres(mAcceso.getId()));
                emp_EmpTimp = daoEmpresas.ListaEmpEmpTim(empresas);
            }
            if (getIdEmpresa() > 0) {
                nuevaEmpresa = daoEmpresas.BuscarEmpresaId(getIdEmpresa());
                idDireccion = nuevaEmpresa.getDireccion().getId();
            } else {
                setIdEmpresa(-1);
            }
        }
    }

    /**
     * Elimina una empresa
     */
    public void borrarEmpresa() {
        empresaAux = daoEmpresas.BuscarEmpresaId(this.empTimSelected.getEmpresa().getId());
        if (daoEmpresas.BorrarEmpresa(this.empTimSelected.getEmpresa())) {
            emp_EmpTimp.remove(empTimSelected);
            this.emp_EmpTimp.remove(empTimSelected);
            empTimSelected = null;
            FacesContext.getCurrentInstance().addMessage("frmAdminEmpresas", new FacesMessage(FacesMessage.SEVERITY_INFO, "La empresa fue borrada correctamente.", "Info"));
        } else {
            FacesContext.getCurrentInstance().addMessage("frmAdminEmpresas", new FacesMessage(FacesMessage.SEVERITY_ERROR, "La empresa  no se puede eliminar ya que tiene dependencias (Perfiles).", "Info"));
        }
    }

    /**
     * Carga los timbres correspondientes a una empresa
     */
    public void cargarTimbres() {
        if (empTimSelected != null) {
            idEmpresa = empTimSelected.getEmpresa().getId();
            empresas = new ArrayList<MEmpresa>();
            empresas.add(daoEmpresas.BuscarEmpresaId(idEmpresa));
            emp_EmpTimpAux = daoEmpresas.ListaEmpEmpTim(empresas);
            this.idPadre = idEmpresa;
            empsTimpsTimps = daoTimbre.ListaTimbrePorEmpresa(idEmpresa);
            if (empsTimpsTimps != null && empsTimpsTimps.size() > 0) {
                timbres = empsTimpsTimps.get(0).getTimbres();
            }
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/admin/empresas/administracionTimbres.xhtml");
            } catch (IOException ex) {
                System.out.println(">>>>>>>>>>IOException on cargarTimbres()");
                Logger.getLogger(ManagedBeanEmpresas.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            FacesContext.getCurrentInstance().addMessage("frmAdminEmpresas", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debes seleccionar una empresa.", "Error"));
        }
    }

    public void agregarComprarTimbres() {
        idEmpTim = empTimSelected.getEmpresaTimbre().getId();
        if (idEmpTim > 0) {
            empresaTimbre = daoTimbre.BuscarEmpresaTimbreID(idEmpTim);
        }
        empresa = daoEmpresas.BuscarEmpresaId(empTimSelected.getEmpresa().getId());
        empresas = new ArrayList<MEmpresa>();
        empresas.add(empresa);
        emp_EmpTimpAux = daoEmpresas.ListaEmpEmpTim(empresas);
        empresaTimbre.setRfc(empresa.getRfcOrigen());
    }

    /**
     * Comprar folios
     */
    public void comprarFolios() {
        empresa = daoEmpresas.BuscarEmpresaId(idPadre);
        if (idEmpTim > 0) {
            MEmpresaMTimbre auxemptim = null;
            auxemptim = daoTimbre.BuscarEmpresaTimbreID(idEmpTim);
            auxemptim.setEstatus(empresaTimbre.getEstatus());
            auxemptim.setClaveWS(empresaTimbre.getClaveWS());
            auxemptim.setTipoCte(empresaTimbre.getTipoCte());
            if (daoTimbre.GuardarActualizaEmpTim(auxemptim)) {
                timbre.setEmpresatimbre(auxemptim);
                timbre.setUsuario(mAcceso.getUsuario());
                if (timbre != null && daoTimbre.GuardarActualizaTimbre(timbre)) {
                    auxemptim.setFolios((auxemptim.getFolios() + timbre.getFolioscomprados()));
                    if (daoTimbre.GuardarActualizaEmpTim(auxemptim)) {
                        daoLogs.guardaRegistro(mAcceso, "Agrega Timbres a la Empresa " + auxemptim.getEmpresa().RFC_Empresa());
                    }
                }
            }
        } else {
            empresaTimbre.setRfc(empresa.getRfcOrigen());
            empresaTimbre.setEmpresa(empresa);
            if (daoTimbre.GuardarActualizaEmpTim(empresaTimbre)) {
                timbre.setEmpresatimbre(empresaTimbre);
                timbre.setUsuario(mAcceso.getUsuario());
                if (timbre != null && daoTimbre.GuardarActualizaTimbre(timbre)) {
                    empresaTimbre.setFolios((empresaTimbre.getFolios() + timbre.getFolioscomprados()));
                    if (daoTimbre.GuardarActualizaEmpTim(empresaTimbre)) {
                        daoLogs.guardaRegistro(mAcceso, "Agrega Timbres a la Empresa " + empresaTimbre.getEmpresa().RFC_Empresa());
                    }
                }
            }
        }
        //ACTUALIZAMOS LOS OBJETOS CON LOS NUEVOS DATOS
        //mostrarTimbres = true;
        if (empTimSelected != null) {
//            mostrarTimbres = true;
            idEmpresa = empTimSelected.getEmpresa().getId();
            empresas = new ArrayList<MEmpresa>();
            empresas.add(daoEmpresas.BuscarEmpresaId(idEmpresa));
            emp_EmpTimpAux = daoEmpresas.ListaEmpEmpTim(empresas);
            emp_EmpTimp = daoEmpresas.ListaEmpEmpTim(empresas);
            this.idPadre = idEmpresa;
            empsTimpsTimps = daoTimbre.ListaTimbrePorEmpresa(idEmpresa);
            if (empsTimpsTimps != null && empsTimpsTimps.size() > 0) {
                timbres = empsTimpsTimps.get(0).getTimbres();
            }
        }

    }

    /**
     * Reset the data
     */
    public void reset() {
        nuevaEmpresa = null;
        nuevaEmpresa = new MEmpresa();
        empresaAux = null;
        empresaAux = new MEmpresa();
        direccion = null;
        direccion = new MDireccion();
        boCrearUsuario = false;
        ufLogo = null;
        //ufLogo = new DefaultUploadedFile();
        idEmpresa = -1;
        idEmpresaPadre = -1;
        idDireccion = -1;
        if (timbres != null) {
            timbres.clear();
        }
        if (emp_EmpTimpAux != null) {
            emp_EmpTimpAux.clear();
        }
        empresaTimbre = null;
        empresaTimbre = new MEmpresaMTimbre();
        timbre = null;
        timbre = new MTimbre();

    }

    /**
     * @return the idEmpresa
     */
    public int getIdEmpresa() {
        return idEmpresa;
    }

    /**
     * @return the idDireccion
     */
    public int getIdDireccion() {
        return idDireccion;
    }

    /**
     * @return the idPadre
     */
    public int getIdPadre() {
        return idPadre;
    }

    /**
     * @param idEmpresa the idEmpresa to set
     */
    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    /**
     * @param idDireccion the idDireccion to set
     */
    public void setIdDireccion(int idDireccion) {
        this.idDireccion = idDireccion;
    }

    /**
     * @param idPadre the idPadre to set
     */
    public void setIdPadre(int idPadre) {
        this.idPadre = idPadre;
    }

    /**
     * Metodo de validacion para la creacion de una nueva empresa.
     *
     * @return
     */
    public boolean validar() {
        boolean val = true;
        if ((nuevaEmpresa.getRfcOrigen() == null) || ("".equals(nuevaEmpresa.getRfcOrigen()))) {
            FacesContext.getCurrentInstance().addMessage("frmAdminEmpresas", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Es necesario insertar el RFC origen.", "Info"));
            val = false;
        } else {
            //Nueva empresa
            if (idEmpresa <= 0) {
                //Validamos si no existe ese RFC
                if (daoEmpresas.BuscarEmpresaRFC(nuevaEmpresa.getRfcOrigen().trim()) != null) {
                    FacesContext.getCurrentInstance().addMessage("frmAdminEmpresas", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "La empresa  ya existe, no se puede registrar.", "Info"));
                    val = false;
                }
            }
        }
        if ((nuevaEmpresa.getRazonSocial() == null)
                || ("".equals(nuevaEmpresa.getRazonSocial()))) {
            FacesContext.getCurrentInstance().addMessage("frmAdminEmpresas", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Es necesario insertar la razon social.", "Info"));
            val = false;
        }
        if (nuevaEmpresa.getCorreoContacto() == null
                || ("".equals(nuevaEmpresa.getCorreoContacto()))) {
            FacesContext.getCurrentInstance().addMessage("frmAdminEmpresas", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Es necesario insertar el Correo del contacto.", "Info"));
            val = false;
        }
        if (nuevaEmpresa.getNombreContacto() == null
                || ("".equals(nuevaEmpresa.getNombreContacto()))) {
            FacesContext.getCurrentInstance().addMessage("frmAdminEmpresas", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Es necesario insertar el Nombre del contacto.", "Info"));
            val = false;
        }
        if (direccion.getCalle() == null
                || "".equals(direccion.getCalle().trim())) {
            FacesContext.getCurrentInstance().addMessage("frmAdminEmpresas", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Es necesario insertar la Calle.", "Info"));
            val = false;
        }
        if (direccion.getPais() == null
                || ("".equals(direccion.getPais().trim()))) {
            FacesContext.getCurrentInstance().addMessage("frmAdminEmpresas", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Es necesario el país..", "Info"));
            val = false;
        }
        if(ufLogo != null && ufLogo.getContents().length > 0){
            try {
                BufferedImage logo = ImageIO.read(new ByteArrayInputStream(ufLogo.getContents()));
                if(logo.getHeight()>HEIGHTLOGO+OFFSETTOLERANCE || logo.getHeight()< HEIGHTLOGO - OFFSETTOLERANCE){
                    FacesContext.getCurrentInstance().addMessage("frmAdminEmpresas", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "El logo de la empresa debe de tener una dimentsion de 150*50(WxH)", "Info"));
                    val = false;
                }else if (logo.getWidth() > WIDTHLOGO + OFFSETTOLERANCE || logo.getWidth() < WIDTHLOGO - OFFSETTOLERANCE ){
                    FacesContext.getCurrentInstance().addMessage("frmAdminEmpresas", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "El logo de la empresa debe de tener una dimentsion de 150*50(WxH)", "Info"));
                    val = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return val;
    }

    /**
     * Busca registros de empresas en la base de datos
     */
    public void buscarEmpresas() {
        if (validarCampoBusqueda()) {
            setEmpresas(daoEmpresas.BusquedaEmpresas(mAcceso.getId(), tipoBusqueda.trim(), paramBusqueda.trim()));
            setEmp_EmpTimp(daoEmpresas.ListaEmpEmpTim(getEmpresas()));
        }
    }

    //Valida que el parametro de busqueda  no se encuentre vacio
    public boolean validarCampoBusqueda() {
        if (!tipoBusqueda.equals("Todos") || tipoBusqueda.equals("-1")) {
            if (paramBusqueda.equals("") || paramBusqueda == null) {
                FacesContext.getCurrentInstance().addMessage("frmAdminEmpresas", new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "Debe proporcionar un parametro de busqueda.", "Info"));
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }

    }

    public void modificarCampoBusqueda() {
        if (tipoBusqueda.equalsIgnoreCase("") || tipoBusqueda.equalsIgnoreCase("Todos") || tipoBusqueda == null) {
            flagTipoBusqueda = true;
            paramBusqueda = "";
        } else {
            flagTipoBusqueda = false;
        }
    }

    /**
     * Guarda el logo de la empresa automaticamente para su posterior almacenaje
     * en un directorio
     *
     * @param event
     */
    public void cargarImagenEmpresa(FileUploadEvent event) {
        this.setUfLogo(event.getFile());
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
     * @return the empresasSelect
     */
    public List<MEmpresa> getEmpresasSelect() {
        return empresasSelect;
    }

    /**
     * @return the empresasAux
     */
    public List<MEmpresa> getEmpresasAux() {
        return empresasAux;
    }

    /**
     * @return the paramBusqueda
     */
    public String getParamBusqueda() {
        return paramBusqueda;
    }

    /**
     * @return the tipoBusqueda
     */
    public String getTipoBusqueda() {
        return tipoBusqueda;
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
     * @param empresasSelect the empresasSelect to set
     */
    public void setEmpresasSelect(List<MEmpresa> empresasSelect) {
        this.empresasSelect = empresasSelect;
    }

    /**
     * @param empresasAux the empresasAux to set
     */
    public void setEmpresasAux(List<MEmpresa> empresasAux) {
        this.empresasAux = empresasAux;
    }

    /**
     * @param paramBusqueda the paramBusqueda to set
     */
    public void setParamBusqueda(String paramBusqueda) {
        this.paramBusqueda = paramBusqueda;
    }

    /**
     * @param tipoBusqueda the tipoBusqueda to set
     */
    public void setTipoBusqueda(String tipoBusqueda) {
        this.tipoBusqueda = tipoBusqueda;
    }

    /**
     * @return the empsTimpsTimps
     */
    public List<EmpresaTimbre_Timbre> getEmpsTimpsTimps() {
        return empsTimpsTimps;
    }

    /**
     * @return the emp_EmpTimp
     */
    public List<Empresa_EmpresaTimbre> getEmp_EmpTimp() {
        return emp_EmpTimp;
    }

    /**
     * @param empsTimpsTimps the empsTimpsTimps to set
     */
    public void setEmpsTimpsTimps(List<EmpresaTimbre_Timbre> empsTimpsTimps) {
        this.empsTimpsTimps = empsTimpsTimps;
    }

    /**
     * @param emp_EmpTimp the emp_EmpTimp to set
     */
    public void setEmp_EmpTimp(List<Empresa_EmpresaTimbre> emp_EmpTimp) {
        this.emp_EmpTimp = emp_EmpTimp;
    }

    /**
     * @return the empresaTimbre
     */
    public MEmpresaMTimbre getEmpresaTimbre() {
        return empresaTimbre;
    }

    /**
     * @param EmpresaTimbre the empresaTimbre to set
     */
    public void setEmpresaTimbre(MEmpresaMTimbre EmpresaTimbre) {
        this.empresaTimbre = EmpresaTimbre;
    }

    /**
     * @return the emp_EmpTimpSelecteds
     */
    public List<Empresa_EmpresaTimbre> getEmp_EmpTimpSelecteds() {
        return emp_EmpTimpSelecteds;
    }

    /**
     * @param emp_EmpTimpSelecteds the emp_EmpTimpSelecteds to set
     */
    public void setEmp_EmpTimpSelecteds(List<Empresa_EmpresaTimbre> emp_EmpTimpSelecteds) {
        this.emp_EmpTimpSelecteds = emp_EmpTimpSelecteds;
    }

    /**
     * @return the flagTipoBusqueda
     */
    public boolean isFlagTipoBusqueda() {
        return flagTipoBusqueda;
    }

    /**
     * @param flagTipoBusqueda the flagTipoBusqueda to set
     */
    public void setFlagTipoBusqueda(boolean flagTipoBusqueda) {
        this.flagTipoBusqueda = flagTipoBusqueda;
    }

    /**
     * @return the empTimSelected
     */
    public Empresa_EmpresaTimbre getEmpTimSelected() {
        return empTimSelected;
    }

    /**
     * @param empTimSelected the empTimSelected to set
     */
    public void setEmpTimSelected(Empresa_EmpresaTimbre empTimSelected) {
        this.empTimSelected = empTimSelected;
    }

    /**
     * @return the idEmpresaPadre
     */
    public int getIdEmpresaPadre() {
        return idEmpresaPadre;
    }

    /**
     * @return the empresaPadreSelect
     */
    public MEmpresa getEmpresaPadreSelect() {
        return empresaPadreSelect;
    }

    /**
     * @return the empresasPadre
     */
    public List<MEmpresa> getEmpresasPadre() {
        return empresasPadre;
    }

    /**
     * @param idEmpresaPadre the idEmpresaPadre to set
     */
    public void setIdEmpresaPadre(int idEmpresaPadre) {
        this.idEmpresaPadre = idEmpresaPadre;
    }

    /**
     * @param empresaPadreSelect the empresaPadreSelect to set
     */
    public void setEmpresaPadreSelect(MEmpresa empresaPadreSelect) {
        this.empresaPadreSelect = empresaPadreSelect;
    }

    /**
     * @param empresasPadre the empresasPadre to set
     */
    public void setEmpresasPadre(List<MEmpresa> empresasPadre) {
        this.empresasPadre = empresasPadre;
    }

    /**
     * @return the nuevaEmpresa
     */
    public MEmpresa getNuevaEmpresa() {
        return nuevaEmpresa;
    }

    /**
     * @param nuevaEmpresa the nuevaEmpresa to set
     */
    public void setNuevaEmpresa(MEmpresa nuevaEmpresa) {
        this.nuevaEmpresa = nuevaEmpresa;
    }

    /**
     * @return the direccion
     */
    public MDireccion getDireccion() {
        return direccion;
    }

    /**
     * @param direccion the direccion to set
     */
    public void setDireccion(MDireccion direccion) {
        this.direccion = direccion;
    }

    /**
     * @return the boCrearUsuario
     */
    public boolean isBoCrearUsuario() {
        return boCrearUsuario;
    }

    /**
     * @param boCrearUsuario the boCrearUsuario to set
     */
    public void setBoCrearUsuario(boolean boCrearUsuario) {
        this.boCrearUsuario = boCrearUsuario;
    }

    /**
     * @return the interno
     */
    public boolean isInterno() {
        return interno;
    }

    /**
     * @param interno the interno to set
     */
    public void setInterno(boolean interno) {
        this.interno = interno;
    }

    /**
     * @return the fileLogo
     */
    public File getFileLogo() {
        return fileLogo;
    }

    /**
     * @param fileLogo the fileLogo to set
     */
    public void setFileLogo(File fileLogo) {
        this.fileLogo = fileLogo;
    }

    /**
     * @return the empresaAux
     */
    public MEmpresa getEmpresaAux() {
        return empresaAux;
    }

    /**
     * @param empresaAux the empresaAux to set
     */
    public void setEmpresaAux(MEmpresa empresaAux) {
        this.empresaAux = empresaAux;
    }

    /**
     * @return the ufLogo
     */
    public UploadedFile getUfLogo() {
        return ufLogo;
    }

    /**
     * @param ufLogo the ufLogo to set
     */
    public void setUfLogo(UploadedFile ufLogo) {
//        System.out.println("Setting logo");
        this.ufLogo = ufLogo;
    }

    /**
     * @return the empresaPadre
     */
    public MEmpresa getEmpresaPadre() {
        return empresaPadre;
    }

    /**
     * @param empresaPadre the empresaPadre to set
     */
    public void setEmpresaPadre(MEmpresa empresaPadre) {
        this.empresaPadre = empresaPadre;
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
     * @return the ctrlPass
     */
    public ControlPasword getCtrlPass() {
        return ctrlPass;
    }

    /**
     * @return the sdf
     */
    public SimpleDateFormat getSdf() {
        return sdf;
    }

    /**
     * @return the sendMail
     */
    public SendMail getSendMail() {
        return sendMail;
    }

    /**
     * @return the generarUsuario
     */
    public int getGenerarUsuario() {
        return generarUsuario;
    }

    /**
     * @return the nombreLogo
     */
    public String getNombreLogo() {
        return nombreLogo;
    }

    /**
     * @param ctrlPass the ctrlPass to set
     */
    public void setCtrlPass(ControlPasword ctrlPass) {
        this.ctrlPass = ctrlPass;
    }

    /**
     * @param sdf the sdf to set
     */
    public void setSdf(SimpleDateFormat sdf) {
        this.sdf = sdf;
    }

    /**
     * @param sendMail the sendMail to set
     */
    public void setSendMail(SendMail sendMail) {
        this.sendMail = sendMail;
    }

    /**
     * @param generarUsuario the generarUsuario to set
     */
    public void setGenerarUsuario(int generarUsuario) {
        this.generarUsuario = generarUsuario;
    }

    /**
     * @param nombreLogo the nombreLogo to set
     */
    public void setNombreLogo(String nombreLogo) {
        this.nombreLogo = nombreLogo;
    }

    /**
     * @return the timbres
     */
    public List<MTimbre> getTimbres() {
        return timbres;
    }

    /**
     * @param timbres the timbres to set
     */
    public void setTimbres(List<MTimbre> timbres) {
        this.timbres = timbres;
    }

    /**
     * @return the emp_EmpTimpAux
     */
    public List<Empresa_EmpresaTimbre> getEmp_EmpTimpAux() {
        return emp_EmpTimpAux;
    }

    /**
     * @param emp_EmpTimpAux the emp_EmpTimpAux to set
     */
    public void setEmp_EmpTimpAux(List<Empresa_EmpresaTimbre> emp_EmpTimpAux) {
        this.emp_EmpTimpAux = emp_EmpTimpAux;
    }

    /**
     * @return the timbre
     */
    public MTimbre getTimbre() {
        return timbre;
    }

    /**
     * @param timbre the timbre to set
     */
    public void setTimbre(MTimbre timbre) {
        this.timbre = timbre;
    }

    /**
     * @return the idEmpTim
     */
    public long getIdEmpTim() {
        return idEmpTim;
    }

    /**
     * @param idEmpTim the idEmpTim to set
     */
    public void setIdEmpTim(long idEmpTim) {
        this.idEmpTim = idEmpTim;
    }
}
