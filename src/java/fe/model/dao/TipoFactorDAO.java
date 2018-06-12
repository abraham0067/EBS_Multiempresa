/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.model.dao;

import fe.db.MCtipoFactor;
import fe.model.util.hibernateutil.HibernateUtilApl;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Eduardo C. Flores Ambrosio <EB&S>
 */
public class TipoFactorDAO implements Serializable {
    private HibernateUtilApl hibManagerRO;
    
    public TipoFactorDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
    }
    
    public List<MCtipoFactor> getAll(){
        List<MCtipoFactor> res = null;
        try{
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MCtipoFactor.class);
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
