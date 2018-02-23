/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.model.dao;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import fe.db.MConfig;
import fe.model.util.hibernateutil.HibernateUtilApl;
import java.io.Serializable;

/**
 *
 * @author Lily
 */
public class ProcesoDAO implements Serializable {

    private HibernateUtilApl hibManagerRO;

    public ProcesoDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
    }

    public MConfig ObtnerVAlue(String dato) throws Exception {
        MConfig m = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MConfig.class);
            cr.add(Restrictions.ilike("dato", dato + "%"));
            cr.add(Restrictions.ilike("clasificacion", "PROCESOS"));
            m = (MConfig) cr.uniqueResult();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException he) {
            hibManagerRO.getTransaction().rollback();
            he.printStackTrace(System.err);
        } finally {
            hibManagerRO.closeSession();
        }
        return m;
    }
}
