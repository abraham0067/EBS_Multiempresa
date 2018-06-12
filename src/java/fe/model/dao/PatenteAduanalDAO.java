/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.model.dao;

import fe.db.MCpatentesAduanales;
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
public class PatenteAduanalDAO implements Serializable {
    private HibernateUtilApl hibManagerRO;

    public PatenteAduanalDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
    }
    
    public List<MCpatentesAduanales> get50(String patente){
        List<MCpatentesAduanales> res = null;
        try{
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MCpatentesAduanales.class);
            cr.add(Restrictions.ilike("patente", patente, MatchMode.START));
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
