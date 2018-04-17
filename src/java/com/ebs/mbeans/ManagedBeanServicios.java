package com.ebs.mbeans;

import fe.db.CServiciosFacturacion;
import fe.db.MServicio;
import fe.db.MServiciosFacturacion;
import fe.model.dao.ServiciosFacturacionDAO;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.HibernateException;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.fieldset.Fieldset;
import org.primefaces.component.inputswitch.InputSwitch;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.panelgrid.PanelGrid;
import org.primefaces.context.PrimeFacesContext;
import org.primefaces.context.RequestContext;

import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlForm;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.sun.faces.el.ELUtils.createValueExpression;

public class ManagedBeanServicios implements Serializable {

    @Getter
    @Setter
    List<CServiciosFacturacion> servicios;
    @Getter
    @Setter
    List<MServiciosFacturacion> mServicios;
    ServiciosFacturacionDAO serviciosDao;

    List<CServiciosFacturacion> auxServicios;

    List<MServiciosFacturacion> auxMServicios;

    @Getter
    @Setter
    private int idEmpresaSelect;
    @Getter
    @Setter
    private int idServicioSelect;
    @Getter
    @Setter
    HtmlForm serviciosForm;
    @Getter
    @Setter
    private boolean[] valuesServicios;

    @Getter
    @Setter
    private String[] valuesServiciosDelete;

    private List<Integer> servId;

    @Getter
    @Setter
    private String nombreServicio;


    public ManagedBeanServicios() {
        serviciosDao = new ServiciosFacturacionDAO();
        idEmpresaSelect = -1;
        this.nombreServicio = null;
        clear();
        auxServicios = null;
        servicios = serviciosDao.getServicios();
    }

    public void asignarServicio() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("id: " + idServicioSelect));
    }

    public void mostrarServicios() {

        OutputLabel label1;
        InputSwitch inputSwitch;
        servicios.clear();
        servicios = serviciosDao.getServicios();

        mServicios = serviciosDao.getServiciosFacturacion(idEmpresaSelect);

        if (servicios == null) {
            serviciosForm = new HtmlForm();
        } else {
            serviciosForm.getChildren().clear();

            valuesServicios = new boolean[servicios.size()];

            PanelGrid pg = null;
            Fieldset fs = null;

            OutputLabel ol1;
            OutputLabel ol2;
            InputSwitch is3;
            OutputLabel ol4;
            OutputLabel ol3;

            pg = new PanelGrid();
            pg.setColumnClasses("ui-grid-col-3,ui-grid-col-3,ui-grid-col-2,ui-grid-col-4");
            pg.setId("pg");
            pg.setColumns(4);
            pg.setLayout("grid");
            pg.setStyleClass("ui-panelgrid-blank");
            pg.setStyle("width: 100%;");
            serviciosForm.getChildren().add(pg);
            //Fieldset
            fs = new Fieldset();
            fs.setLegend("Servicios");
            fs.setToggleable(false);
            fs.setCollapsed(false);
            fs.getChildren().add(pg);
            serviciosForm.getChildren().add(fs);

            for (int i = 0; i < servicios.size(); i++) {
                label1 = new OutputLabel();
                inputSwitch = new InputSwitch();
                ol1 = new OutputLabel();
                ol2 = new OutputLabel();


                label1.setValue(servicios.get(i).getServicio());
                inputSwitch.setOffLabel("No");
                inputSwitch.setOnLabel("Si");
                inputSwitch.setValueExpression("value", createValueExpression("#{managedBeanServicios.valuesServicios['" + i + "']}", String.class));
                valuesServicios[i] = false;
                boolean flag = false;
                for (int j = 0; j < mServicios.size(); j++) {

                    if (servicios.get(i).getId().intValue() == mServicios.get(j).getCServiciosFacturacion().getId().intValue()) {
                        flag = true;
                        j = mServicios.size();
                        valuesServicios[i] = flag;
                        inputSwitch.setValue(flag);
                    }
                    //boolean flag = servicios.get(i).getId().intValue() == mServicios.get(j).getCServiciosFacturacion().getId().intValue() ? true : false;

                }
                pg.getChildren().add(ol1);
                pg.getChildren().add(label1);
                pg.getChildren().add(inputSwitch);
                pg.getChildren().add(ol2);
            }

            ol1 = new OutputLabel();
            ol2 = new OutputLabel();
            ol4 = new OutputLabel();
            ol3 = new OutputLabel();

            CommandButton cb = new CommandButton();
            cb.setValue("Guardar cambios");
            cb.setProcess("@this,@form");
            cb.setUpdate("formAdminServ:msg,@form");
            cb.setActionExpression(FacesContext.
                    getCurrentInstance().
                    getApplication().
                    getExpressionFactory().
                    createMethodExpression(
                            FacesContext.getCurrentInstance().getELContext(),
                            "#{managedBeanServicios.modificarServicio()}",
                            String.class,
                            new Class[0]));
            pg.getChildren().add(ol1);
            pg.getChildren().add(ol2);
            pg.getChildren().add(ol3);
            pg.getChildren().add(ol4);

            pg.getChildren().add(ol1);
            pg.getChildren().add(ol3);
            pg.getChildren().add(cb);
            pg.getChildren().add(ol2);


        }
    }

    public void clear() {
        if (serviciosForm == null) {
            serviciosForm = new HtmlForm();
        } else {
            serviciosForm.getChildren().clear();

        }
    }



    public void listServicios() {

        auxServicios = new ArrayList<>();

        for (int i = 0; i < valuesServicios.length; i++) {

            if (valuesServicios[i]) {
                auxServicios.add(servicios.get(i));
            }
        }

    }

    public void agregarServicio() {
        boolean resOperation = false;

        if (nombreServicio != null) {

            CServiciosFacturacion serv = null;
            serv = serviciosDao.buscarServicio(nombreServicio);

            if (serv == null) {

                serv = new CServiciosFacturacion();
                serv.setServicio(nombreServicio);
                boolean flag = serviciosDao.agregarServicio(serv);
                if (flag) {
                    resOperation = true;
                    FacesContext.getCurrentInstance().addMessage("formAdminServ", new FacesMessage("Servicio agregado exitosamente."));
                    this.nombreServicio = null;
                    clear();
                    idEmpresaSelect = -1;
                    servicios.clear();
                    servicios = serviciosDao.getServicios();
                } else {
                    resOperation = false;
                    FacesContext.getCurrentInstance().addMessage("formAdminServ", new FacesMessage("Error al intentar agregar el servicio."));
                }


            } else {
                FacesContext.getCurrentInstance().addMessage("formAdminServ", new FacesMessage("El servicio ya existe."));
            }

        } else {
            FacesContext.getCurrentInstance().addMessage("formAdminServ", new FacesMessage("El nombre del servicio no puede ser vacío."));
        }

        RequestContext.getCurrentInstance().addCallbackParam("resOperation", resOperation);
    }

    public void modificarServicio() {

        listServicios();
        MServiciosFacturacion mServ = null;
        String campo = "idEmpresa";
        String idCampo = "id";
        if (idEmpresaSelect > 0) {
            //System.out.println("res: " + serviciosDao.borrarServicio(idEmpresaSelect));
            if (serviciosDao.borrarServicio(idEmpresaSelect)) {

                for (int i = 0; i < auxServicios.size(); i++) {
                    mServ = new MServiciosFacturacion();
                    mServ.setIdEmpresa(idEmpresaSelect);
                    mServ.setCServiciosFacturacion(auxServicios.get(i));
                    serviciosDao.actualizarEmpresaServicios(mServ);
                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Servicios actualizados."));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error al modificar los servicios."));
            }

        } else {

        }

    }

    public void serviciosIds() {

        mServicios = new ArrayList<MServiciosFacturacion>();
        mServicios.clear();
        for (int i = 0; i < valuesServiciosDelete.length; i++) {
            for (int j = 0; j < servicios.size(); j++) {
                int aux = Integer.parseInt(valuesServiciosDelete[i]);
                if (aux == servicios.get(j).getId().intValue()) {
                    MServiciosFacturacion mServicio = new MServiciosFacturacion();
                    mServicio.setCServiciosFacturacion(servicios.get(j));
                    mServicios.add(mServicio);
                    j = servicios.size();
                }
            }
        }

    }

    public void borrarServicio() {

        serviciosIds();

        boolean flag = false;
        int idService = 0;
        boolean status = false;

        if (mServicios.size() > 0) {

            for (int i = 0; i < mServicios.size(); i++) {

                idService = mServicios.get(i).getCServiciosFacturacion().getId();
                try{
                    serviciosDao.borrarServicioCatalogo(idService);
                    status = true;
                    FacesContext.getCurrentInstance().addMessage("formAdminServ", new FacesMessage("Servicio " + mServicios.get(i).getCServiciosFacturacion().getServicio() + " eliminado con éxito."));
                }catch(HibernateException ex){
                    status = false;
                    FacesContext.getCurrentInstance().addMessage("formAdminServ", new FacesMessage("Error al eliminar " + mServicios.get(i).getCServiciosFacturacion().getServicio()
                            + "\n" + ex.getMessage()));
                }
            }
            servicios.clear();
            servicios = serviciosDao.getServicios();

            clear();
            this.mServicios.clear();
            idEmpresaSelect = -1;


        } else {
            FacesContext.getCurrentInstance().addMessage("formAdminServ", new FacesMessage("Es necesario seleccionar al menos un servicio."));
        }

        RequestContext.getCurrentInstance().addCallbackParam("resDelete", status);
    }
}
