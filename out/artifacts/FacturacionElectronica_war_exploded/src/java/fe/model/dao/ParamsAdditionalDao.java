package fe.model.dao;

import fe.db.MAddfieldsCpto;
import fe.db.MAddfieldsInvoice;
import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.List;

/**
 * Created by eflores on 31/08/2017.
 */
public class ParamsAdditionalDao implements Serializable{

    private HibernateUtilApl hibManagerRO;
    private HibernateUtilEmi hibManagerSU;

    public ParamsAdditionalDao() {
        hibManagerRO= new HibernateUtilApl();
        //hibManagerSU = new HibernateUtilEmi();
    }


    public List<MAddfieldsInvoice> getAllAdditionalForCfdiByEmp(int idEmp){
        List<MAddfieldsInvoice> data = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MAddfieldsInvoice.class);
            cr.add(Restrictions.eq("idEmp", idEmp));
            cr.addOrder(Order.asc("ordenMuestra"));
            data = cr.list();
            hibManagerRO.getTransaction().commit();
        }catch (HibernateException e){
            hibManagerRO.getTransaction().rollback();
            e.printStackTrace(System.out);
        }
        return data;
    }

    public List<MAddfieldsCpto> getAllAdditionalForCptoByEmp(int idEmp){
        List<MAddfieldsCpto> data = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MAddfieldsCpto.class);
            cr.add(Restrictions.eq("idEmp", idEmp));
            cr.addOrder(Order.asc("ordenMuestra"));
            data = cr.list();
            hibManagerRO.getTransaction().commit();
        }catch (HibernateException e){
            hibManagerRO.getTransaction().rollback();
            e.printStackTrace();
        }
        return data;
    }
}
