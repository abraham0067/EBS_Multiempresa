package fe.model.dao;

import fe.db.MArchivosCfdProforma;
import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.List;

public class ArchivosProformaDAO implements Serializable {

    private HibernateUtilApl hibManagerRO;
    private HibernateUtilEmi hibManagerSU;

    public ArchivosProformaDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
        hibManagerSU = new HibernateUtilEmi();//Save Update interface

    }

    public boolean GuardarActualizaArchivoCfd(MArchivosCfdProforma archivosCfd) {
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

    public boolean BorrarArchivoCfd(MArchivosCfdProforma archivoCfd) {
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

    public MArchivosCfdProforma BuscarArchivoCfdId(int id) {
        MArchivosCfdProforma archivoCfd = null;
        try {
            hibManagerRO.initTransaction();
            archivoCfd = (MArchivosCfdProforma) hibManagerRO.getSession().get(MArchivosCfdProforma.class, id);
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
    public List<MArchivosCfdProforma> BuscarArchivoCfd_cfdId(int id) {
        List<MArchivosCfdProforma> archivoCfd = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MArchivosCfdProforma.class);
            cr.add(Restrictions.eq("cfdId", id));
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
