/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.model.dao;

import fe.db.MCcodigopostal;
import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueResultException;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 * @author Eduardo C. Flores Ambrosio <EB&S>
 */
public class CodigoPostalDAO implements Serializable {
    private HibernateUtilApl hibManagerRO;
    private HibernateUtilEmi hibManagerSU;

    public CodigoPostalDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
    }

    public List<MCcodigopostal> get50(String clave) {
        List<MCcodigopostal> res = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MCcodigopostal.class);
            cr.add(Restrictions.ilike("codigoPostal", clave, MatchMode.ANYWHERE));
            cr.setMaxResults(50);
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

    public boolean checkExist(String clave) {
        MCcodigopostal res = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MCcodigopostal.class);
            cr.add(Restrictions.ilike("codigoPostal", clave, MatchMode.EXACT));
            res = (MCcodigopostal) cr.uniqueResult();
            if (res != null)
                System.out.println("res = " + res.getCodigoPostal());
            hibManagerRO.getTransaction().commit();
        } catch (NonUniqueResultException e) {
            hibManagerRO.getTransaction().rollback();
            e.printStackTrace();
        } catch (HibernateException e) {
            hibManagerRO.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return res != null ? true : false;
    }

    public MCcodigopostal getCP(String clave) {
        MCcodigopostal res = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MCcodigopostal.class);
            cr.add(Restrictions.ilike("codigoPostal", clave, MatchMode.EXACT));
            res = (MCcodigopostal) cr.uniqueResult();
            hibManagerRO.getTransaction().commit();
        } catch (NonUniqueResultException e) {
            hibManagerRO.getTransaction().rollback();
            e.printStackTrace();
        } catch (HibernateException e) {
            hibManagerRO.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return res;
    }

}
