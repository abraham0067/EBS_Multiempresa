
package fe.model.dao;

import fe.db.MTdocsFactman;
import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;

import java.io.Serializable;
import java.util.List;

/**
 * Created by eflores on 06/09/2017.
 */
public class TipoDocsFactManDao implements Serializable{
    private HibernateUtilApl hibManagerRO;

    public TipoDocsFactManDao() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
    }

    public List<MTdocsFactman> getAll() {
        List<MTdocsFactman> res = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MTdocsFactman.class);
            cr.setMaxResults(15);
            cr.addOrder(Order.asc("id"));
            res = cr.list();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException e) {
            hibManagerRO.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return res;
    }



}
