/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.model.dao;

import fe.db.MCimpuesto;
import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;
import java.io.Serializable;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Eduardo C. Flores Ambrosio <EB&S>
 */
public class ImpuestoDAO implements Serializable {

    private HibernateUtilApl hibManagerRO;

    public ImpuestoDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
    }

    public List<MCimpuesto> getAll() {
        List<MCimpuesto> res = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MCimpuesto.class);
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

    public List<MCimpuesto> getImpestosForTraslados() {
        List<MCimpuesto> res = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MCimpuesto.class);
            cr.add(Restrictions.eq("traslado", 1));
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

    public List<MCimpuesto> getImpestosForRetenciones() {
        List<MCimpuesto> res = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MCimpuesto.class);
            cr.add(Restrictions.eq("retencion", 1));
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
