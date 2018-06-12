/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.model.dao;

import fe.db.MCfdXml;
import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author HpAbraham
 */
public class XmlDao {

    private HibernateUtilApl hibManagerRO;
    private HibernateUtilEmi hibManagerSU;

    public XmlDao() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
        hibManagerSU = new HibernateUtilEmi();
    }

    public MCfdXml getXml_XmlP(int cfd_id) {
        MCfdXml mc = null;

        try {
            if (cfd_id != 0) {
                hibManagerRO.initTransaction();
                Criteria cr = hibManagerRO.getSession().createCriteria(MCfdXml.class);
                cr.add(Restrictions.eq("cfd.id", cfd_id));
                mc = (MCfdXml) cr.uniqueResult();
                hibManagerRO.getTransaction().commit();
            }
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return mc;
    }

    public boolean guardarActualizar(MCfdXml cfd) {
        boolean valor = false;

        try {
            hibManagerSU.initTransaction();
            hibManagerSU.getSession().saveOrUpdate(cfd);
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

}
