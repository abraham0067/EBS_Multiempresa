package fe.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import fe.db.MPac;
import fe.db.MPacMEmpresa;
import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;
import java.io.Serializable;

public class PacDAO implements Serializable {

    private HibernateUtilApl hibManagerRO;
    private HibernateUtilEmi hibManagerSU;

    public PacDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
        hibManagerSU = new HibernateUtilEmi();
    }

    public boolean GuardarOActualizar(Object obj) {
        boolean val = false;
        try {
            hibManagerSU.initTransaction();
            hibManagerSU.getSession().saveOrUpdate(obj);
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

    public boolean actualizarObjeto(Object obj) {
        boolean val = false;
        try {
            hibManagerSU.initTransaction();
            hibManagerSU.getSession().update(obj);
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

    public boolean BorrarObjeto(Object obj) {
        boolean val = false;
        try {
            hibManagerSU.initTransaction();
            hibManagerSU.getSession().delete(obj);
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

    @SuppressWarnings("unchecked")
    public List<MPac> ListaPac() {
        List<MPac> pacs = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MPac.class);
            cr.addOrder(Order.asc("nombre"));
            cr.setMaxResults(100);
            pacs = cr.list();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }

        return pacs;

    }

    public MPac BuscarPacId(long id) {
        MPac pac = null;
        try {
            hibManagerRO.initTransaction();
            pac = (MPac) hibManagerRO.getSession().get(MPac.class, id);
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return pac;
    }

    public MPac BuscarPacNombre(String nombre) {
        MPac pac = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MPac.class);
            cr.add(Restrictions.ilike("nombre", nombre));
            pac = (MPac) cr.uniqueResult();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return pac;
    }

    /**
     * --------------------------------------------------------------------------------------------*
     */

    public MPacMEmpresa BuscarPacEmpresaId(long id) {
        MPacMEmpresa pac = null;
        try {
            hibManagerRO.initTransaction();
            pac = (MPacMEmpresa) hibManagerRO.getSession().get(MPacMEmpresa.class, id);
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return pac;
    }

    public MPacMEmpresa BuscarEmpidPacid(int idempresa, long id_pac) {
        MPacMEmpresa pacs = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MPacMEmpresa.class);
            cr.add(Restrictions.eq("empresa.id", idempresa));
            cr.add(Restrictions.eq("pac.id", id_pac));
            cr.addOrder(Order.asc("fechaRegistro"));
            pacs = (MPacMEmpresa) cr.uniqueResult();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }

        return pacs;

    }

    @SuppressWarnings("unchecked")
    public List<MPacMEmpresa> ListaPacEmpresasId(int idempresa) {
        List<MPacMEmpresa> pacs = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MPacMEmpresa.class);
            cr.add(Restrictions.eq("empresa.id", idempresa));
            cr.addOrder(Order.asc("fechaRegistro"));
            cr.setMaxResults(100);
            pacs = cr.list();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }

        return pacs;

    }

    public boolean ActualizarStatus(int idempresa, long idpac) {
        boolean val = false;

        try {
            hibManagerSU.initTransaction();
            String hql = "UPDATE M_PAC_M_EMPRESA SET ESTATUS=0 WHERE empresa_id= :idEmpresa and pac_id != :idPac";
            hibManagerSU.getSession().createSQLQuery(hql)
                    .setInteger("idEmpresa", idempresa)
                    .setLong("idPac", idpac)
                    .executeUpdate();
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

}
