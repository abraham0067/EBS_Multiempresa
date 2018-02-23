package fe.model.dao;

import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;

import java.io.Serializable;
import java.util.List;

/**
 * Created by eflores on 19/10/2017.
 */
public class DaoGenerico implements Serializable {

    private HibernateUtilApl hibManagerRO;
    private HibernateUtilEmi hibManagerSU;

    public DaoGenerico() {
        hibManagerRO =new HibernateUtilApl();//Read only pool
        hibManagerSU =new HibernateUtilEmi();
    }


    public List<Object> getRows(Class<?> classType, int numberOfRows){
        List<Object> rows = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(classType);
            cr.setMaxResults(numberOfRows);
            rows = cr.list();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.out);
        } finally {
            hibManagerRO.closeSession();
        }
        return rows;
    }



}
