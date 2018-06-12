package fe.model.dao;

import fe.db.MConfig;
import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.List;

public class ConfigDAO implements Serializable {

    private HibernateUtilApl hibManagerRO;
    private HibernateUtilEmi hibManagerSU;

    public ConfigDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
        hibManagerSU = new HibernateUtilEmi();
    }

    public boolean GuardarActualizaMconfig(MConfig config) {
        boolean valor = true;
        try {
            hibManagerSU.initTransaction();
            hibManagerSU.getSession().saveOrUpdate(config);
            hibManagerSU.getTransaction().commit();
        } catch (HibernateException ex) {
            valor = false;
            hibManagerSU.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            hibManagerSU.closeSession();
        }

        return valor;
    }

    public boolean BorrarConfig(MConfig config) {
        boolean valor = true;
        try {
            hibManagerSU.initTransaction();
            hibManagerSU.getSession().delete(config);
            hibManagerSU.getTransaction().commit();
        } catch (HibernateException ex) {
            valor = false;
            hibManagerSU.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            hibManagerSU.closeSession();
        }
        return valor;
    }

    public MConfig BuscarConfigId(Integer id) {
        MConfig config = null;
        try {
            hibManagerRO.initTransaction();
            config = (MConfig) hibManagerRO.getSession().get(MConfig.class, id);
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {

            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            hibManagerRO.closeSession();
        }
        return config;
    }

    public MConfig BuscarConfigDatoClasificacion(String dato, String Clasificacion) {
        MConfig config = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MConfig.class);
            cr.add(Restrictions.ilike("dato", dato.trim()));
            cr.add(Restrictions.ilike("clasificacion", Clasificacion));
            config = (MConfig) cr.uniqueResult();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {

            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            hibManagerRO.closeSession();
        }
        return config;
    }

    @SuppressWarnings("unchecked")
    public List<MConfig> ListaParametro() {
        List<MConfig> parametros = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MConfig.class);
            cr.addOrder(Order.asc("dato"));
            parametros = cr.list();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            hibManagerRO.closeSession();
        }

        return parametros;
    }
}
