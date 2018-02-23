/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.model.dao;

import fe.db.MJars;
import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Eduardo C. Flores Ambrosio <Eduardo at EB&S>
 */
public class JarsDAO {

    private HibernateUtilApl hibManagerRO;
    private HibernateUtilEmi hibManagerSU;

    public JarsDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
    }

    /**
     * Buscar un jar en la base de datos
     *
     * @param id
     * @return
     */
    public MJars buscarJar(String id) {
        MJars jar = null;
        try {
            hibManagerRO.initTransaction();
            jar = (MJars) hibManagerRO.getSession().get(MJars.class, id);
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return jar;
    }

    /**
     * Busca un jar de una empresa
     *
     * @param idEmpresa
     * @return
     */
    public MJars buscarJarEmpresa(int idEmpresa) {
        MJars jar = null;
        try {
            hibManagerRO.initTransaction();
            jar = (MJars) hibManagerRO.getSession().get(MJars.class, idEmpresa);
            Criteria cr = hibManagerRO.getSession().createCriteria(MJars.class);
            cr.add(Restrictions.eq("idEmpresa", idEmpresa));
            jar = (MJars) cr.uniqueResult();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return jar;
    }

}
