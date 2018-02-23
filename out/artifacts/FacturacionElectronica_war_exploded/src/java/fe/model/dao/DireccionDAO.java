package fe.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import fe.db.MDireccion;
import fe.db.MEmpresa;
import fe.db.MReceptor;
import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;
import java.io.Serializable;

public class DireccionDAO implements Serializable {

    private HibernateUtilApl hibManagerRO;
    private HibernateUtilEmi hibManagerSU;

    public DireccionDAO() {
        hibManagerRO =new HibernateUtilApl();//Read only pool
        hibManagerSU =new HibernateUtilEmi();
    }

    public boolean BorrarDireccion(MDireccion direccion) {
        boolean valor = false;
        try {
            hibManagerSU.initTransaction();
            hibManagerRO.initTransaction();
            Criteria cres = hibManagerRO.getSession().createCriteria(MEmpresa.class);
            cres.add(Restrictions.eq("direccion", direccion));
            List<MEmpresa> listEmpresa = cres.list();

            Criteria crrs = hibManagerRO.getSession().createCriteria(MReceptor.class);
            crrs.add(Restrictions.eq("direccion", direccion));
            List<MReceptor> listReceptor = crrs.list();
            if ((listEmpresa == null || listEmpresa.isEmpty()) && (listReceptor == null || listReceptor.isEmpty())) {
                hibManagerSU.getSession().delete(direccion);
                valor = true;
            }
            hibManagerRO.getTransaction().commit();
            hibManagerSU.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerSU.getTransaction().rollback();
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.out);
        } finally {
            hibManagerRO.closeSession();
            hibManagerSU.closeSession();
        }
        return valor;
    }

    public boolean GuardarActualizaDireccion(MDireccion direccion) {
        boolean valor = true;
        try {
            hibManagerSU.initTransaction();
            hibManagerSU.getSession().saveOrUpdate(direccion);
            hibManagerSU.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerSU.getTransaction().rollback();
            valor = false;
            ex.printStackTrace(System.out);
        } finally {

            hibManagerSU.closeSession();
        }
        return valor;
    }

    @SuppressWarnings({"unchecked"})
    public List<MDireccion> ListaDirecciones() {
        List<MDireccion> direcciones = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MDireccion.class);
            direcciones = cr.list();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.out);
        } finally {
            hibManagerRO.closeSession();
        }
        return direcciones;
    }

    public MDireccion BuscarDireccion(int id) {
        MDireccion direccion = null;
        try {
            hibManagerRO.initTransaction();
            direccion = (MDireccion) hibManagerRO.getSession().get(MDireccion.class, id);
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.out);
        } finally {
            hibManagerRO.closeSession();
        }

        return direccion;
    }

    @SuppressWarnings("unchecked")
    public List<MDireccion> BuscarDireccion(String columna, String value) {
        List<MDireccion> direcciones = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MDireccion.class);
            cr.add(Restrictions.ilike(columna.trim(), "%" + value.trim() + "%"));
            direcciones = cr.list();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }

        return direcciones;

    }
}
