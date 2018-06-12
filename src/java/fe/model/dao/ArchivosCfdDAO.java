package fe.model.dao;

import fe.db.MArchivosCfd;
import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.List;

public class ArchivosCfdDAO implements Serializable {

    private HibernateUtilApl hibManagerRO;
    private HibernateUtilEmi hibManagerSU;

    public ArchivosCfdDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
        hibManagerSU = new HibernateUtilEmi();//Save Update interface

    }

    public boolean GuardarActualizaArchivoCfd(MArchivosCfd archivosCfd) {
        boolean valor = true;
        try {
            hibManagerSU.initTransaction();
            hibManagerSU.getSession().saveOrUpdate(archivosCfd);
            hibManagerSU.getTransaction().commit();
        } catch (HibernateException ex) {
            valor = false;
            hibManagerSU.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            hibManagerSU.closeSession();
        }

        return valor;
    }

    public boolean BorrarArchivoCfd(MArchivosCfd archivoCfd) {
        boolean valor = true;
        try {
            hibManagerSU.initTransaction();
            hibManagerSU.getSession().delete(archivoCfd);
            hibManagerSU.getTransaction().commit();
        } catch (HibernateException ex) {
            valor = false;
            hibManagerSU.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            hibManagerSU.closeSession();
        }
        return valor;
    }

    public MArchivosCfd BuscarArchivoCfdId(int id) {
        MArchivosCfd archivoCfd = null;
        try {
            hibManagerRO.initTransaction();
            archivoCfd = (MArchivosCfd) hibManagerRO.getSession().get(MArchivosCfd.class, id);
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace(System.out);
            hibManagerRO.getTransaction().rollback();
        } finally {
            hibManagerRO.closeSession();
        }

        return archivoCfd;
    }

    @SuppressWarnings("unchecked")
    public List<MArchivosCfd> BuscarArchivoCfd_cfdId(int id) {
        List<MArchivosCfd> archivoCfd = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MArchivosCfd.class);
            cr.add(Restrictions.eq("cfd.id", id));
            archivoCfd = cr.list();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace(System.out);
            hibManagerRO.getTransaction().rollback();
        } finally {
            hibManagerRO.closeSession();
        }

        return archivoCfd;
    }

}
