package fe.model.dao;

import fe.db.MCfdXml;
import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;

/**
 * Created by eflores on 21/08/2017.
 */
public class CfdiXmlDAO  implements Serializable{
    private HibernateUtilApl hibManagerRO;
    private HibernateUtilEmi hibManagerSU;
    public int rowCount = 0;

    public CfdiXmlDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
        hibManagerSU = new HibernateUtilEmi();
    }

    public MCfdXml getCfdiXmlByCfdiId(int id) {
        MCfdXml tmp = null;
        try {
            hibManagerRO.initTransaction();
            Criteria crF = hibManagerRO.getSession().createCriteria(MCfdXml.class);
            crF.add(Restrictions.sqlRestriction("this_.CFD_ID = " + id + ""));
            tmp = (MCfdXml) crF.uniqueResult();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException e) {
            hibManagerRO.getTransaction().rollback();
        } finally {
            try {
                hibManagerRO.closeSession();
            } catch (Exception e) {
            }
        }
        return tmp;
    }



}
