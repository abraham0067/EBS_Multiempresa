package fe.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import fe.db.MPerfil;
import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;
import java.io.Serializable;

public class PerfilDAO implements Serializable {

    private HibernateUtilApl hibManagerRO;
    private HibernateUtilEmi hibManagerSU;

    public PerfilDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
        hibManagerSU = new HibernateUtilEmi();
    }

    public boolean guardarActualizarPerfil(MPerfil objPerfil) {
        boolean ok = true;
        try {
            hibManagerSU.initTransaction();
            hibManagerSU.getSession().saveOrUpdate(objPerfil);
            hibManagerSU.getTransaction().commit();
        } catch (HibernateException ex) {
            ok = false;
            hibManagerSU.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            hibManagerSU.closeSession();
        }
        return ok;
    }

    public boolean BorrarPerfil(MPerfil perfil) {
        boolean valor = false;
        try {
            hibManagerSU.initTransaction();
            hibManagerSU.getSession().delete(perfil);
            hibManagerSU.getTransaction().commit();
            valor = true;
        } catch (HibernateException ex) {
            hibManagerSU.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            hibManagerSU.closeSession();
        }
        return valor;
    }

    @SuppressWarnings("unchecked")
    public List<MPerfil> ListaPerfiles(int idEmpresa) {
        List<MPerfil> Perfiles = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MPerfil.class);
            cr.add(Restrictions.isNotNull("empresa.id"));
            cr.add(Restrictions.eq("empresa.id", idEmpresa));
            Perfiles = cr.list();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            hibManagerRO.closeSession();
        }

        return Perfiles;
    }

    public MPerfil BuscarPerfil(Integer id) {
        MPerfil perfil = null;
        try {
            hibManagerRO.initTransaction();
            perfil = (MPerfil) hibManagerRO.getSession().get(MPerfil.class, id);
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            hibManagerRO.closeSession();
        }

        return perfil;
    }

    public MPerfil BuscarPerfil(Integer idEmpresa, String tipoUser) {
        MPerfil perfil = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MPerfil.class);
            cr.add(Restrictions.eq("empresa.id", idEmpresa));
            cr.add(Restrictions.ilike("tipoUser", tipoUser));
            perfil = (MPerfil) cr.uniqueResult();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            hibManagerRO.closeSession();
        }
        return perfil;
    }

}
