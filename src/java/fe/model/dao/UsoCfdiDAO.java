/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.model.dao;

import fe.db.MCusoComprobantes;
import fe.model.util.hibernateutil.HibernateUtilApl;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Eduardo C. Flores Ambrosio <EB&S>
 */
public class UsoCfdiDAO implements Serializable {
    private HibernateUtilApl hibManagerRO;

    
    public UsoCfdiDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
    }
    
    public List<MCusoComprobantes> getAll(){
        List<MCusoComprobantes> res = null;
        try{
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MCusoComprobantes.class);
            res = cr.list();
            hibManagerRO.getTransaction().commit();
        } catch(HibernateException e){
            e.printStackTrace();
            hibManagerRO.getTransaction().rollback();
        } finally {
                hibManagerRO.closeSession();
        }
         return res;       
    }
    public List<MCusoComprobantes> getForFisicas(){
        List<MCusoComprobantes> res = null;
        try{
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MCusoComprobantes.class);
            cr.add(Restrictions.eq("fisica",1));
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
    public List<MCusoComprobantes> getForMorales(){
        List<MCusoComprobantes> res = null;
        try{
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MCusoComprobantes.class);
            cr.add(Restrictions.eq("moral",1));
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
