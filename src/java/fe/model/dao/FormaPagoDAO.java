/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.model.dao;

import fe.db.MCformapago;
import fe.model.util.hibernateutil.HibernateUtilApl;

import java.io.Serializable;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Eduardo C. Flores Ambrosio <EB&S>
 */
public class FormaPagoDAO implements Serializable {
    private HibernateUtilApl hibManagerRO;

    
    public FormaPagoDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
    }
    
    public List<MCformapago> getAll(){
        List<MCformapago> res = null;
        try{
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MCformapago.class);
            cr.addOrder(Order.asc("codigo"));
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
    public MCformapago getByCode(String code){
        MCformapago res = null;
        try{
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MCformapago.class);
            cr.add(Restrictions.ilike("clave", code, MatchMode.EXACT));
            res = (MCformapago)cr.uniqueResult();
            hibManagerRO.getTransaction().commit();
        } catch(HibernateException e){
            hibManagerRO.getTransaction().rollback();
            e.printStackTrace();
        } finally {
                hibManagerRO.closeSession();
        }
         return res;
    }
    
}
