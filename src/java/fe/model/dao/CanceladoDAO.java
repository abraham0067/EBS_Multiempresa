package fe.model.dao;

import fe.db.MCancelados;
import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;

public class CanceladoDAO implements Serializable {

    private HibernateUtilApl hibManagerRO;
    private HibernateUtilEmi hibManagerSU;

    public CanceladoDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
        hibManagerSU = new HibernateUtilEmi();
    }

    public MCancelados buscarCancelados_IdCfd(int id) {
        MCancelados obj = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MCancelados.class);
            cr.add(Restrictions.isNotNull("cfd.id"));
            cr.add(Restrictions.eq("cfd.id", id));
            obj = (MCancelados) cr.uniqueResult();
            if (obj != null && obj.getCfd() != null) {
                obj.getCfd().getUuid();
            }

            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return obj;
    }

    public boolean GuardarActualizacionCancelado(MCancelados cancel) {
        boolean val = false;
        try {
            hibManagerSU.initTransaction();
            hibManagerSU.getSession().saveOrUpdate(cancel);
            hibManagerSU.getTransaction().commit();
            val = true;
        } catch (HibernateException ex) {
            ex.printStackTrace();
        } finally {
            hibManagerSU.closeSession();
        }
        return val;
    }
}
