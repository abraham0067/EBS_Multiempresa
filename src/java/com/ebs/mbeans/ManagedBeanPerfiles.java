/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.mbeans;

import static com.sun.faces.el.ELUtils.createValueExpression;

import fe.db.MAcceso;
import fe.db.MAcceso.Nivel;
import fe.db.MEmpresa;
import fe.db.MPerfil;
import fe.db.MenuPerfiles;
import fe.model.dao.EmpresaDAO;
import fe.model.dao.LogAccesoDAO;
import fe.model.dao.LoginDAO;
import fe.model.dao.PerfilDAO;
//import fe.web.webPerfil;
import com.ebs.menu.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.IOUtils;
import org.primefaces.context.RequestContext;

import java.io.Serializable;
import javax.faces.component.html.HtmlForm;
import javax.swing.text.StyledEditorKit;

import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.fieldset.Fieldset;
import org.primefaces.component.growl.Growl;
import org.primefaces.component.inputswitch.InputSwitch;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.panelgrid.PanelGrid;
import org.primefaces.model.menu.Submenu;

/**
 * @author Eduardo C. Flores Ambrosio <Eduardo at EB&S>
 */
public class ManagedBeanPerfiles implements Serializable {

    //Modelos
    private MAcceso mAcceso;

    //Contexto
    private HttpServletRequest httpServletRequest;
    private FacesContext facesContext;
    private String appContext;


    private long x;
    private static String fileName = "WebMenu.xml";
    private static String fileNameCte = "WebMenuCte.xml";

    private List<MPerfil> perfiles;
    private Iterator<MenuPerfiles> dibujaPerfiles;

    private int idEmpresaSelect;//Para el registro de un nuevo perfil
    private Integer idPerfil;
    @Getter @Setter private int empresaIdFiltro;

    private MPerfil perfil;
    private MPerfil nuevoPerfil;
    private EmpresaDAO daoEmp;
    private PerfilDAO daoPerfil;
    private LoginDAO daoLogin;
    private LogAccesoDAO daoLog;

    private HtmlForm form;//Binding form
    private boolean[] values;
    private List<SubMenu> menuPerfiles;
    private String nombrePerfil;
    private static WebMenu fullMenu;

    /**
     * Creates a new instance of ManagedBeanPerfiles
     */
    public ManagedBeanPerfiles() {

    }

    /**
     * Post construct
     */
    @PostConstruct
    public void init() {
        facesContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        mAcceso = (MAcceso) httpSession.getAttribute("macceso");
        appContext = httpServletRequest.getContextPath();
        idEmpresaSelect = -1;
        perfil = new MPerfil();
        nuevoPerfil = new MPerfil();
        daoEmp = new EmpresaDAO();
        daoPerfil = new PerfilDAO();
        daoLogin = new LoginDAO();
        daoLog = new LogAccesoDAO();
        resetPerfil();
    }

    /**
     * Obtine los perfiles registrados de la empresa
     */
    public void cargarPerfiles() {
        clearForm();
        idPerfil = -1;
        this.perfiles = new ArrayList<>();
        if (empresaIdFiltro > 0 ) {
            this.perfiles = daoPerfil.ListaPerfiles(empresaIdFiltro);
        }

    }

    public void modificarPerfil() {
        //imprimirValores();
        if (idPerfil != null && idPerfil > 0) {
            MPerfil modperfil = daoPerfil.BuscarPerfil(idPerfil);
            modperfil.setPerfil(calculaLongPerfil());
            /*if (perfil.getTimeOut().intValue() < 0) {
             perfil.setTimeOut(0);
             }
             modperfil.setTimeOut(perfil.getTimeOut());
             */
            if (daoPerfil.guardarActualizarPerfil(modperfil)) {
                daoLog.guardaRegistro(mAcceso, "Modifico el perfil: " + modperfil.getTipoUser());
                this.cargarPerfiles();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "El perfil se modifico correctamente.", "Info"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Debes seleccionar un perfil primero.", "Info"));
        }
    }

    public void clearForm(){
        if (form == null) {
            form = new HtmlForm();
        } else {
            //Clear the form first
            form.getChildren().clear();
        }
    }

    public void populateForm() {

        if (form == null) {
            form = new HtmlForm();
        } else {
            //Clear the form first
            form.getChildren().clear();
            //Add the growl
            Growl messages = new Growl();
            messages.setId("messagesIn");
            messages.setShowSummary(true);
            messages.setShowDetail(false);
            messages.setLife(16000);
            form.getChildren().add(messages);

            //add the fieldset and panelgrid for every submenu
            PanelGrid pg = null;
            Fieldset fs = null;

            OutputLabel ol1;
            OutputLabel ol2;
            InputSwitch is3;
            OutputLabel ol4;

            if (menuPerfiles != null) {
                int todos = 0;
                //Solo se sumarizan los menu items de cada submenu
                for (SubMenu tmp1 : menuPerfiles) {
                    for(MenuItem tmp2:tmp1.getItems())
                        todos++;
                }
                values  = new boolean[todos];
                int i = 0;
                for (SubMenu tmp1 : menuPerfiles) {

                    pg = new PanelGrid();
                    pg.setColumnClasses("ui-grid-col-3,ui-grid-col-3,ui-grid-col-2,ui-grid-col-4");
                    pg.setId("pg" + tmp1.getName());
                    pg.setColumns(4);
                    pg.setLayout("grid");
                    pg.setStyleClass("ui-panelgrid-blank");
                    pg.setStyle("width: 100%;");
                    form.getChildren().add(pg);
                    //Fieldset
                    fs = new Fieldset();
                    fs.setLegend(tmp1.getTitle());
                    fs.setToggleable(false);
                    fs.setCollapsed(false);
                    fs.getChildren().add(pg);
                    form.getChildren().add(fs);
                    values[i] = true;
                    for(MenuItem tmp2: tmp1.getItems()){
                        //Creates a inputswitch
                        //Add elements
                        ol1 = new OutputLabel();
                        ol2 = new OutputLabel();
                        is3 = new InputSwitch();
                        ol4 = new OutputLabel();

                        //Add data
                        ol2.setValue(tmp2.getTitle());
                        is3.setValueExpression("value", createValueExpression("#{managedBeanPerfiles.values['" + i + "']}", String.class));
                        is3.setOffLabel("No");
                        is3.setOnLabel("Si");
                        values[i] = ((perfil.getPerfil() & tmp2.getPerfilValue()) == tmp2.getPerfilValue())? true:false;
                        is3.setValue(values[i]);//Para prueba


                        pg.getChildren().add(ol1);
                        pg.getChildren().add(ol2);
                        pg.getChildren().add(is3);
                        pg.getChildren().add(ol4);
                        i++;
                    }
                }
                //Create components
                ol1 = new OutputLabel();
                ol2 = new OutputLabel();
                ol4 = new OutputLabel();
                CommandButton cbSave = new CommandButton();
                cbSave.setValue("Guardar cambios");
                cbSave.setProcess("@form,@this");
                cbSave.setUpdate("frmAdminPerfiles:messages,frmAdminPerfiles:pgPerfil,@form");
                cbSave.setActionExpression(FacesContext.
                        getCurrentInstance().
                        getApplication().
                        getExpressionFactory().
                        createMethodExpression(
                                FacesContext.getCurrentInstance().getELContext(),
                                "#{managedBeanPerfiles.modificarPerfil()}",
                                String.class,
                                new Class[0]));
                //Add components to form
                pg.getChildren().add(ol1);
                pg.getChildren().add(cbSave);
                pg.getChildren().add(ol2);
                pg.getChildren().add(ol4);
            }
        }
    }

    /**
     * Calculo del perfil, se hace una operacion or sobre los valores
     * esto permite usar el mismo valor del perfil en diferentes elementos y aun asi obtener
     * un valor correcto
     * @return
     */
    public long calculaLongPerfil() {
        long perfil1 = 0;
        int i = 0;
        boolean tieneElementoActivo = false;
        for (SubMenu sbm:menuPerfiles){
            tieneElementoActivo = false;
            for(MenuItem mi:sbm.getItems()){
                if(values[i]){//If activated
                    perfil1 |= mi.getPerfilValue();
                    tieneElementoActivo = true;
                }
                i++;
            }

            ///Agregamos cuando el elemento padre tiene un subelemento activodd
            if(tieneElementoActivo)
                perfil1 |= sbm.getPerfilValue();//valor del perfil del submenu
            /*if(values[i]){<
                perfil1+= sbm.getPerfilValue();
            }*/
            //i++;
        }
    return perfil1;
    }

    public void registrarPerfil() {
        boolean resOperation = false;
        if (idEmpresaSelect > 0) {
            if (daoPerfil.BuscarPerfil(idEmpresaSelect, nombrePerfil) == null) {
                MEmpresa empresa = daoEmp.BuscarEmpresaId(idEmpresaSelect);
                nuevoPerfil = new MPerfil();
                nuevoPerfil.setEmpresa(empresa);
                nuevoPerfil.setPerfil(0l);
                nuevoPerfil.setTimeOut(20);//20 minutos
                nuevoPerfil.setTipoUser(nombrePerfil);
                if (daoPerfil.guardarActualizarPerfil(nuevoPerfil)) {
                    daoLog.guardaRegistro(mAcceso, "Registro el perfil: " + nuevoPerfil.getTipoUser());
                    this.resetPerfil();
                    this.cargarPerfiles();
                    FacesContext.getCurrentInstance().addMessage("frmAdminPerfiles", new FacesMessage(FacesMessage.SEVERITY_INFO, "El perfil se registro correctamente.", "Info"));
                    resOperation = true;
                }
            } else {
                FacesContext.getCurrentInstance().addMessage("frmAdminPerfiles", new FacesMessage(FacesMessage.SEVERITY_ERROR, "El perfil que intenta registrar ya existe.", "Info"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage("frmAdminPerfiles", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debes seleccionar la empresa.", "Info"));
        }
        RequestContext.getCurrentInstance().addCallbackParam("resOperation", resOperation);
    }

    private String getFileWithUtil(String fileName) {
        String result = "";
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            result = IOUtils.toString(classLoader.getResourceAsStream(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * Hace la actualizacion de la interfaz de acuerdo a perfil seleccionado
     */
    public void cambiarPerfil() {
        if(idPerfil <= 0){
            clearForm();
            return;
        }

        if (idPerfil > 0) {
            if (perfiles != null && !perfiles.isEmpty()) {
                if (idPerfil > 0) {
                    perfil = daoPerfil.BuscarPerfil(idPerfil);
                    if(perfil!= null){
                        if(perfil.getPerfil() == null){
                            perfil.setPerfil(0L);
                        }
                    }
                }
                populateForm();
            }
        }


        if (idPerfil > 0) {
            perfil = daoPerfil.BuscarPerfil(idPerfil);
            if(perfil!= null){
                if(perfil.getPerfil() == null){
                    perfil.setPerfil(0L);
                }
            }
            //Dinamic generation
            if (getPerfiles() != null && !getPerfiles().isEmpty()) {
                WebMenu fullMenu = null;
                if (mAcceso.getNivel() == Nivel.EXTERNO) {
                    String menuAsXml = getFileWithUtil(fileNameCte);
                    MenuBuilder builder = new MenuBuilder(menuAsXml);
                    fullMenu = builder.getMenuAsList();
                    menuPerfiles = fullMenu.getMenus();
                } else {
                    String menuAsXml = getFileWithUtil(fileName);
                    MenuBuilder builder = new MenuBuilder(menuAsXml);
                    fullMenu = builder.getMenuAsList();
                    menuPerfiles = fullMenu.getMenus();
                }

                if (idPerfil > 0) {
                    perfil = daoPerfil.BuscarPerfil(idPerfil);
                }

                populateForm();
            }
        }
    }

    public void resetPerfil() {
        nombrePerfil = "";
        idEmpresaSelect = -1;
    }

    public void borrarPerfil() {
        if (idPerfil > 0) {
            perfil = daoPerfil.BuscarPerfil(idPerfil);
            if (daoPerfil.BorrarPerfil(perfil)) {
                this.cargarPerfiles();//Leemos nuevamente los perfiles
                FacesContext.getCurrentInstance().addMessage("frmAdminPerfiles", new FacesMessage(FacesMessage.SEVERITY_INFO, "El perfil se borro satisfactoriamente.", "Info"));
                perfil = null;
            } else {
                FacesContext.getCurrentInstance().addMessage("frmAdminPerfiles", new FacesMessage(FacesMessage.SEVERITY_INFO, "El perfil no se logro borrar ya que tiene dependencias.", "Info"));
            }
        } else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"Por favor seleccione el perfil que desea eliminar",""));
        }
    }

    /**
     * Reset the atributes
     */
    public void reset() {
        if (perfiles != null) {
            perfiles.clear();
        }
    }

    /**
     * @return the perfiles
     */
    public List<MPerfil> getPerfiles() {
        return perfiles;
    }

    /**
     * @return the dibujaPerfiles
     */
    public Iterator<MenuPerfiles> getDibujaPerfiles() {
        return dibujaPerfiles;
    }

    /**
     * @return the idPerfil
     */
    public Integer getIdPerfil() {
        return idPerfil;
    }

    /**
     * @return the perfil
     */
    public MPerfil getPerfil() {
        return perfil;
    }

    /**
     * @param perfiles the perfiles to set
     */
    public void setPerfiles(List<MPerfil> perfiles) {
        this.perfiles = perfiles;
    }

    /**
     * @param dibujaPerfiles the dibujaPerfiles to set
     */
    public void setDibujaPerfiles(Iterator<MenuPerfiles> dibujaPerfiles) {
        this.dibujaPerfiles = dibujaPerfiles;
    }

    /**
     * @param idPerfil the idPerfil to set
     */
    public void setIdPerfil(Integer idPerfil) {
        this.idPerfil = idPerfil;
    }

    /**
     * @param perfil the perfil to set
     */
    public void setPerfil(MPerfil perfil) {
        this.perfil = perfil;
    }

    /**
     * @return the x
     */
    public long getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(long x) {
        this.x = x;
    }

    /**
     * @return the nuevoPerfil
     */
    public MPerfil getNuevoPerfil() {
        return nuevoPerfil;
    }

    /**
     * @param nuevoPerfil the nuevoPerfil to set
     */
    public void setNuevoPerfil(MPerfil nuevoPerfil) {
        this.nuevoPerfil = nuevoPerfil;
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

    public HtmlForm getForm() {
        return form;
    }

    public void setForm(HtmlForm form) {
        this.form = form;
    }

    public String getNombrePerfil() {
        return nombrePerfil;
    }

    public void setNombrePerfil(String nombrePerfil) {
        this.nombrePerfil = nombrePerfil;
    }

    public boolean[] getValues() {
        return values;
    }

    public void setValues(boolean[] values) {
        this.values = values;
    }

}
