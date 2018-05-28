package fe.model.dao;

import fe.db.MServicios;
import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;
import org.hibernate.HibernateException;
import org.hibernate.Query;

import java.util.List;
import java.util.stream.Stream;

public class ServiciosDAO {
    private HibernateUtilApl hibManagerRO;
    private HibernateUtilEmi hibManagerSU;

    public ServiciosDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
        hibManagerSU = new HibernateUtilEmi();
    }

    public boolean GuardarActualizarServicio(MServicios servicio) {
        boolean val = false;
        try {
            hibManagerSU.initTransaction();
            hibManagerSU.getSession().saveOrUpdate(servicio);
            hibManagerSU.getTransaction().commit();
            val = true;
        } catch (HibernateException ex) {
            hibManagerSU.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerSU.closeSession();
        }
        return val;
    }

    public Integer[] BuscarServicio(int empresa_id) {
        Integer[] servicio = null;
        try {
            hibManagerRO.initTransaction();
            String sql = "SELECT m.servicio_empresa  FROM MServicios m WHERE m.mEmpresaByEmpresaId.id =:empresa_id";
            Query cfd =  hibManagerRO.getSession().createQuery(sql);
            cfd.setParameter("empresa_id", empresa_id);

            List listIds = cfd.list();

            if(listIds != null && listIds.size()>0) {
                servicio = (Integer[]) listIds.stream().toArray(size -> new Integer[size]);
                //servicio = (Integer[])listIds.toArray(new Integer[listIds.size()]);
            }
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return servicio;
    }

}