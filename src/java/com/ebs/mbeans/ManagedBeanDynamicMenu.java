package com.ebs.mbeans;

import fe.db.MAcceso;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import fe.model.dao.ServiciosDisponibles;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import com.ebs.menu.*;
import org.apache.commons.io.IOUtils;
import org.primefaces.model.menu.*;

/**
 * @author Eduardo C. Flores Ambrosio
 */
@ManagedBean(name = "managedBeanDynamicMenu")
@RequestScoped
public class ManagedBeanDynamicMenu implements Serializable {

    //Barra de menu principal
    private MenuModel menubar;
    private FacesContext context;
    private HttpServletRequest httpRequest;
    private List<SelectItem> opciones;
    //Información del Usuario
    private MAcceso cuentaUsuario;
    private static String fileName = "WebMenu.xml";
    private static String fileNameCte = "WebMenuCte.xml";
    private static String menuStringInter;
    private static String menuStringCte;
    private long perfilu = 0l;
    private ServiciosDisponibles serviciosDisponibles;
    private  boolean servDisp;
    static {
        //Init static attributes
        ManagedBeanDynamicMenu.staticInit();
    }
    /**
     * Creates a new instance of DinamicMenuContent
     */
    public ManagedBeanDynamicMenu() {

    }

    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance();
        httpRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession s = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        menubar = new DefaultMenuModel();
        opciones = new ArrayList<>();
        cuentaUsuario = (MAcceso) s.getAttribute("macceso");
        MenuBuilder builder;
        if(cuentaUsuario.getNivel() == MAcceso.Nivel.INTERNO){
            builder = new MenuBuilder(menuStringInter);///Menu interno solo para admingral
        }else{
            builder = new MenuBuilder(menuStringCte);///Menu interno para todos los demas usuarios
        }
        WebMenu fullMenu = builder.getFullMenu();
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        perfilu = (Long) ((s.getAttribute("perfil") != null) ? s.getAttribute("perfil") : cuentaUsuario.getPerfil().getPerfil());
        buildMenu(fullMenu);//try to gen menu bar binding
        //Opciones disponibles para el cliente
        opciones.add(new SelectItem("numeroFactura", "Numero Factura"));
        opciones.add(new SelectItem("folioErp", "Folio ERP"));
        //Si no es un cliente
        if (!cuentaUsuario.getPerfil().getTipoUser().toUpperCase().equals("CLIENTE")) {
            ///(ATRIBUTOCLASE|VALOR EN LA VISTA)
            opciones.add(new SelectItem("rfc", "RFC"));
            opciones.add(new SelectItem("serie", "Serie"));
            opciones.add(new SelectItem("noCliente", "No. Cliente"));
            opciones.add(new SelectItem("razonSocial", "Razón Social"));
        }

    }

    private static void staticInit(){
        System.out.println("Static Reading Configuration files");
        menuStringInter = getFileWithUtil(fileName);
        menuStringCte = getFileWithUtil(fileNameCte);
    }

    private void buildMenu(WebMenu obj) {
        menubar = new DefaultMenuModel();//Main container
        serviciosDisponibles = new ServiciosDisponibles();
        servDisp = serviciosDisponibles.servicioAsignado(cuentaUsuario.getEmpresa().getId(), "Complemento de pago manual");
        for (SubMenu sbm : obj.getMenus()) {
            menubar.addElement(buildSubMenu(sbm));
        }
    }

    /**
     * Creates recursively each menu
     *
     * @param subm
     * @return
     */
    private DefaultSubMenu buildSubMenu(SubMenu subm) {

        if (subm.getItems().isEmpty() && subm.getMenus().isEmpty()) {
            return null;
        }
        // TODO: 14/09/2017 AGREGAR SOLO LOS SUBMENUS CUANDO ESTOS NO ESTEN VACIOS
        DefaultSubMenu current = new DefaultSubMenu();
        current.setIcon(subm.getIcon());
        current.setLabel(subm.getTitle());

       // System.out.println("flag: " + servDisp);

        //TODO  AGREGAR LOS ICONOS Y SUBMENUS BASADOS EN EL VALOR DE LA POSICION DE LA ENTRADA, ACTUALMENTE DEPENDE DEL ORDEN DE CODIGO
        for (com.ebs.menu.MenuItem item : subm.getItems()) {
            //System.out.println("item.getTitle() = " + item.getTitle());
            //SI ES CANCELACION OMITE PEGAR EL MENU YA QUE ES UN BOTON
            if(item.getTitle().equals("Cancelacion"))
                continue;

            DefaultMenuItem it = new DefaultMenuItem(item.getTitle());
            it.setUrl(item.getUrl());
            if (item.getPerfilValue() == null) {
                current.addElement(it);//add when perfil property not found
            } else {
                if ((perfilu & item.getPerfilValue()) == item.getPerfilValue())//Only add if have rights

                    if(item.getTitle().equalsIgnoreCase("Complemento de pago manual")){
                        if(servDisp){
                            current.addElement(it);
                        }
                    }else{
                        current.addElement(it);
                    }


            }

        }
        if (subm.getMenus() != null) {
            for (SubMenu sb : subm.getMenus()) {
                current.addElement(buildSubMenu(sb));
            }
        }
        return current;
    }

    /**
     * Carga de archivos desde la carpeta que esta marcada como resources
     * @param fileName
     * @return
     */
    private static  String getFileWithUtil(String fileName) {
        String result = "";
        ClassLoader classLoader = ManagedBeanDynamicMenu.class.getClassLoader();
        try {
            result = IOUtils.toString(classLoader.getResourceAsStream(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    public MenuModel getMenubar() {
        return menubar;
    }

    public void setMenubar(MenuModel menubar) {
        this.menubar = menubar;
    }

    public List<SelectItem> getOpciones() {
        return opciones;
    }

    public void setOpciones(List<SelectItem> opciones) {
        this.opciones = opciones;
    }
}
