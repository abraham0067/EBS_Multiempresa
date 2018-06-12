/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.model.dao;

import fe.db.MCunidades;
import fe.model.util.hibernateutil.HibernateUtilApl;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Eduardo C. Flores Ambrosio <EB&S>
 */
public class ClaveUnidadDAO implements Serializable {
    private HibernateUtilApl hibManagerRO;
    private static final int MAXROWS = 100;
    public ClaveUnidadDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
    }
    
    public List<MCunidades> getLImitRows(String arg){
        List<MCunidades> res = null;
        try{
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MCunidades.class);
            cr.add(Restrictions.or(
                    Restrictions.ilike("nombre", arg, MatchMode.ANYWHERE),
                    Restrictions.ilike("clave", arg, MatchMode.START)
            ));
            cr.addOrder(Order.asc("nombre"));
            cr.setMaxResults(MAXROWS);
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
