/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.model.dao;

import fe.db.MCprodserv;
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
public class ProductosDAO implements Serializable {
    private HibernateUtilApl hibManagerRO;
    private static final int MAXROWS = 100;
    private static final String ONLYNUMBERS_REGEX = "\\d+";

    public ProductosDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
    }
    
    public List<MCprodserv> getLimitedRows(String value)
    {

        List<MCprodserv> res =  null;
        try{
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MCprodserv.class);
            if(value.matches(ONLYNUMBERS_REGEX)){
                cr.add(Restrictions.ilike("clave",value, MatchMode.START));
                cr.addOrder(Order.asc("clave"));
            } else {
                cr.add(Restrictions.ilike("descripcion",value, MatchMode.START));
                cr.addOrder(Order.asc("descripcion"));
            }
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
