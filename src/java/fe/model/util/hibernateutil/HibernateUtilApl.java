/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.model.util.hibernateutil;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;

import java.io.Serializable;

/**
 *
 * @author Eduardo C. Flores Ambrosio <EB&S>
 */
public class HibernateUtilApl implements Serializable{

    private static SessionFactory sessionFactory;
    private Session session;
    private Transaction transaction;
    private static final String fileName = "hibernate.apl.cfg.xml";

    //Inicializador de instancia
    {

    }

    //Inicializador estatico
    static {
        try {
            AnnotationConfiguration an = new AnnotationConfiguration();
            an.configure("/"+fileName);
            sessionFactory = an.buildSessionFactory();
        } catch (HibernateException ex) {
            ex.printStackTrace(System.out);
            System.out.println("Initial session factory creation failed" + ex);
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
