/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.model.util.hibernateutil;

import java.io.Serializable;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 *
 * @author Eduardo C. Flores Ambrosio <EB&S>
 */
public class HibernateUtilCustom implements Serializable{

    private Session session;
    private Transaction transaction;
    private static SessionFactory sessionFactory;
    private static String fileName = "hibernate.cfg.xml";
    
    public HibernateUtilCustom(String fileName) {
        HibernateUtilCustom.fileName = fileName;
        try {
            AnnotationConfiguration configuration = new AnnotationConfiguration();
            configuration.configure("/"+fileName);
            sessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            ex.printStackTrace(System.out);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public boolean initTransaction() {
        boolean result = false;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            result = true;
        } catch (HibernateException ex1) {
            result = false;
            System.out.println("Error al iniciar la transaccion:" + ex1.getMessage());
        }
        return result;
    }
    
    public Session getSession() {
        return session;
    }

    public void closeSession() {
        try {
            session.close();
        } catch (Exception e) {
        }
    }

    public Transaction getTransaction() {
        return transaction;
    }
    

}
