package fe.model.dao;

import fe.db.CServiciosFacturacion;
import fe.db.MServiciosFacturacion;

import java.util.List;

public class ServiciosDisponibles {

    private CServiciosFacturacion cServiciosFacturacion;
    private MServiciosFacturacion mServiciosFacturacion;
    private List<MServiciosFacturacion> listMserv;
    private ServiciosFacturacionDAO serviciosFacturacionDAO;

    public ServiciosDisponibles(){

        serviciosFacturacionDAO = new ServiciosFacturacionDAO();
    }

    public boolean servicioAsignado(int idEmpresa, String servicio){
        boolean servicioAsignado = false;

        listMserv = serviciosFacturacionDAO.getServiciosFacturacion(idEmpresa);

        for (int i = 0; i < listMserv.size(); i++){
           // System.out.println("asignado" + listMserv.get(i).getCServiciosFacturacion().getServicio());
            if(listMserv.get(i).getCServiciosFacturacion().getServicio().equalsIgnoreCase(servicio)){
                servicioAsignado = true;
                i = listMserv.size();
            }
        }

        return servicioAsignado;
    }

}
