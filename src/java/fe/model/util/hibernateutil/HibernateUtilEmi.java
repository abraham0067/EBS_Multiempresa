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
public class HibernateUtilEmi implements Serializable{

    private Session session;
    private Transaction transaction;
    private static SessionFactory sessionFactory;
    private static final String fileName = "hibernate.cfg.xml";

    //Inicializador de instancia, para atributos no estaticos
    //Se ejecuta una vez para cada instancia creada
    {
    
    }
    
    //Inicializador estatico, para atributos estaticos de la clase
    //Solo se ejecuta una vez para todas las intancias
    static {
        try {
            //Session factory for hibernate 3
            AnnotationConfiguration configuration = new AnnotationConfiguration();
            configuration.configure("/"+fileName);
            sessionFactory = configuration.buildSessionFactory();
        } catch (HibernateException ex) {
            ex.printStackTrace(System.out);
            System.out.println("Initial session factory creation failed"+  ex);
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
