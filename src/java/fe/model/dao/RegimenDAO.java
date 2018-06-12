/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.model.dao;

import fe.db.MCregimenFiscal;
import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Eduardo C. Flores Ambrosio <EB&S>
 */
public class RegimenDAO implements Serializable {
 private HibernateUtilApl hibManagerRO;
    private HibernateUtilEmi hibManagerSU;

    
    public RegimenDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
    }
    
    public List<MCregimenFiscal> getAll(){
        List<MCregimenFiscal> res = null;
        try{
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MCregimenFiscal.class);
            res = (List<MCregimenFiscal>) cr.list();
            hibManagerRO.getTransaction().commit();
        } catch(HibernateException e){
            hibManagerRO.getTransaction().rollback();
            e.printStackTrace();
        } finally {
                hibManagerRO.closeSession();
        }
         return res;       
    }
    
    public List<MCregimenFiscal> getRegimenFisicas(){
        List<MCregimenFiscal> res = null;
        try{
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MCregimenFiscal.class);
            cr.add(Restrictions.eq("fisica", 1));
            res = (List<MCregimenFiscal>) cr.list();
            hibManagerRO.getTransaction().commit();
        } catch(HibernateException e){
            hibManagerRO.getTransaction().rollback();

            e.printStackTrace();
        } finally {
                hibManagerRO.closeSession();
        }
         return res;       
    }       
    public List<MCregimenFiscal> getRegimenMoral(){
        List<MCregimenFiscal> res = null;
        try{
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MCregimenFiscal.class);
            cr.add(Restrictions.eq("moral", 1));
            res = (List<MCregimenFiscal>) cr.list();
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
