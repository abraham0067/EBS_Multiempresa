/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.model.dao;

import fe.db.MCmetodoPago;
import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;
import java.io.Serializable;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;

/**
 *
 * @author Eduardo C. Flores Ambrosio <EB&S>
 */
public class MetodoPagoDAO implements Serializable {
 private HibernateUtilApl hibManagerRO;
    private HibernateUtilEmi hibManagerSU;

    
    public MetodoPagoDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
    }
    
    public List<MCmetodoPago> getAll(){
        List<MCmetodoPago> res = null;
        try{
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MCmetodoPago.class);
            cr.addOrder(Order.desc("clave"));
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
}
