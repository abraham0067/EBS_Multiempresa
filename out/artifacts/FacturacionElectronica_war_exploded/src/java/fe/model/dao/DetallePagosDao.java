package fe.model.dao;

import fe.db.MPagos;
import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.List;

/**
 * Created by eflores on 02/10/2017.
 */
public class DetallePagosDao implements Serializable {

    public int rowCount = 0;
    private HibernateUtilApl hibManagerRO;
    private HibernateUtilEmi hibManagerSU;

    public DetallePagosDao() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
        hibManagerSU = new HibernateUtilEmi();
    }

    public List<MPagos> getPagosPendientesByIdEmp(int idEmp){
        List<MPagos> res = null;
        try{
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MPagos.class);
            cr.add(Restrictions.eq("empId", idEmp));
            cr.add(Restrictions.eq("pagado",false));
            cr.addOrder(Order.desc("fecha"));
            cr.setMaxResults(5);
            res = cr.list();
            hibManagerRO.getTransaction().commit();
        } catch(HibernateException e){
            hibManagerRO.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return res;
    }

    public MPagos getPagoPendienteByUuid(String uuid){
        MPagos res = null;
        try{
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MPagos.class);
            cr.add(Restrictions.eq("uuid", uuid));
            res = (MPagos) cr.uniqueResult();
            hibManagerRO.getTransaction().commit();
        } catch(HibernateException e){
            hibManagerRO.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return res;
    }

    public boolean savePago(MPagos pago){
        boolean val = false;
        try {
            hibManagerSU.initTransaction();
            hibManagerSU.getSession().update(pago);
            hibManagerSU.getTransaction().commit();
            val = true;
        } catch (HibernateException ex) {
            ex.printStackTrace();
            hibManagerSU.getTransaction().rollback();
        } finally {
            hibManagerSU.closeSession();
        }
        return val;
    }



    /*
        public boolean GuardarActualizacionCFDXML(MCfdXml cfdixml) {
        boolean val = false;
        try {
            hibManagerSU.initTransaction();
            hibManagerSU.getSession().saveOrUpdate(cfdixml);
            hibManagerSU.getTransaction().commit();
            val = true;
        } catch (HibernateException ex) {
            ex.printStackTrace();
        } finally {
            hibManagerSU.closeSession();
        }
        return val;
    }
    public MPagos getPagoPendienteBySerieAndFolio(String serie,String folio){
        MPagos res = null;
        try{
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MPagos.class);
            cr.add(Restrictions.eq("serie", serie));
            cr.add(Restrictions.eq("folio", folio));
            res = (MPagos) cr.uniqueResult();
            hibManagerRO.getTransaction().commit();
        } catch(HibernateException e){
            e.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return res;
    }
    */




}
