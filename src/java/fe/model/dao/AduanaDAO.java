/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.model.dao;

import fe.db.MCaduana;
import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;
import java.io.Serializable;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;

/**
 *
 * @author Eduardo C. Flores Ambrosio <EB&S>
 */
public class AduanaDAO implements Serializable {
    private HibernateUtilApl hibManagerRO;
    //private HibernateUtilEmi hibManagerSU;

    public AduanaDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
    }
    
    public List<MCaduana> getAll(){
        List<MCaduana> res = null;
        try{
        hibManagerRO.initTransaction();
        Criteria cr = hibManagerRO.getSession().createCriteria(MCaduana.class);
        res = cr.list();
        hibManagerRO.getTransaction().commit();
        } catch(HibernateException e){
            hibManagerRO.getTransaction().commit();
            e.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
         return res;       
    }
}
