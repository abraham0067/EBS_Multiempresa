/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.model.dao;

import fe.db.MCpedimentoAduana;
import fe.model.util.hibernateutil.HibernateUtilApl;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Eduardo C. Flores Ambrosio <EB&S>
 */
public class PedimentosDAO implements Serializable {
    private HibernateUtilApl hibManagerRO;

    public PedimentosDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
    }
    
    public List<MCpedimentoAduana> get50(String patente){
        List<MCpedimentoAduana> res = null;
        try{
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MCpedimentoAduana.class);
            cr.add(Restrictions.ilike("patente", patente, MatchMode.ANYWHERE));
            cr.setMaxResults(50);
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
