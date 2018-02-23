/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.model.dao;
import fe.db.MCtipoRelacionCfdi;
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
public class TipoRelacionDAO implements Serializable {
    private HibernateUtilApl hibManagerRO;
    
    public TipoRelacionDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
    }
    
    public List<MCtipoRelacionCfdi> getAll(){
        List<MCtipoRelacionCfdi> res = null;
        try{
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MCtipoRelacionCfdi.class);
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
