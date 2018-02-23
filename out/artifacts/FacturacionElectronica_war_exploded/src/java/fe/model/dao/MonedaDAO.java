/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.model.dao;

import fe.db.MCmoneda;
import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;
import java.io.Serializable;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 *blib c
 * @author Eduardo C. Flores Ambrosio <EB&S>
 */
public class MonedaDAO implements Serializable {
    private HibernateUtilApl hibManagerRO;
    
    public MonedaDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
    }
    
    public List<MCmoneda> getAll(){
        List<MCmoneda> res = null;
        try{
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MCmoneda.class);
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
    
    public List<MCmoneda> get25(String arg){
        List<MCmoneda> res = null;
        try{
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MCmoneda.class);
            cr.add(Restrictions.ilike("clave", arg, MatchMode.ANYWHERE));
            cr.setMaxResults(25);
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
    public MCmoneda getUniqueByCode(String code){
        MCmoneda res = null;
        try{
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MCmoneda.class);
            cr.add(Restrictions.ilike("clave", code, MatchMode.EXACT));
            res = (MCmoneda) cr.uniqueResult();
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
