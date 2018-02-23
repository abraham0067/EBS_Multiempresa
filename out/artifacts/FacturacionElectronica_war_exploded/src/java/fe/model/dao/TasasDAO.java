/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.model.dao;

import fe.db.MCcuotasImpuestos;
import fe.model.util.hibernateutil.HibernateUtilApl;

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
public class TasasDAO implements Serializable {

    private static final int MAX_RESULST = 13;
    private HibernateUtilApl hibManagerRO;

    public TasasDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
    }

    public List<MCcuotasImpuestos> getAll() {
        List<MCcuotasImpuestos> res = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MCcuotasImpuestos.class);
            res = (List<MCcuotasImpuestos>) cr.list();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException e) {
            hibManagerRO.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return res;
    }

    public List<MCcuotasImpuestos> getByTipoAndImpuestoAndFactor(String tipo, String imp, String factor) {
        List<MCcuotasImpuestos> res = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MCcuotasImpuestos.class);
            cr.add(Restrictions.ilike("tipo", tipo, MatchMode.EXACT));
            cr.add(Restrictions.ilike("impuesto", imp, MatchMode.EXACT));
            cr.add(Restrictions.ilike("factor", factor, MatchMode.EXACT));
            ///PARA PREVENIR MOSTRAR MUCHAS TASAS EN CASO DE QUE EXISTA UN ERROR EN BD
            cr.setMaxResults(MAX_RESULST);
            res = (List<MCcuotasImpuestos>) cr.list();
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
