package fe.model.dao;

import fe.db.MImpuestos;
import fe.model.util.hibernateutil.HibernateUtilApl;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.List;

/**
 * Created by eflores on 18/09/2017.
 */
public class ImpuestosFactManDao implements Serializable {
    private HibernateUtilApl hibManagerRO;

    public ImpuestosFactManDao() {
        this.hibManagerRO = new HibernateUtilApl();
    }

    /**
     * ...
     *
     * @param impuesto   clave del impuesto
     * @param tipoFactor tipo de factor del impuesto
     * @return tasas pertenecientes al impuesto
     */
    public List<MImpuestos> getImpuestosTrasladosByImpuestoAndTipoFactor(
            String impuesto,
            String tipoFactor
    ) {
        List<MImpuestos> res = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MImpuestos.class);
            cr.add(Restrictions.ilike("claveImpuesto", impuesto, MatchMode.EXACT));
            cr.add(Restrictions.ilike("tipoFactor", tipoFactor, MatchMode.EXACT));
            cr.add(Restrictions.eq("aplicableTras", true));
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

    public List<MImpuestos> getImpuestosRetenidosByImpuestoAndTipoFactor(
            String impuesto,
            String tipoFactor
    ) {
        List<MImpuestos> res = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MImpuestos.class);
            cr.add(Restrictions.ilike("claveImpuesto", impuesto, MatchMode.EXACT));
            cr.add(Restrictions.ilike("tipoFactor", tipoFactor, MatchMode.EXACT));
            cr.add(Restrictions.eq("aplicableRets", true));
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
