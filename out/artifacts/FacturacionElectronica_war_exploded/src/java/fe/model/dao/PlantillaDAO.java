package fe.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import fe.db.MEmpresa;
import fe.db.MPlantilla;
import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;
import java.io.Serializable;

public class PlantillaDAO implements Serializable {

    private HibernateUtilApl hibManagerRO;
    private HibernateUtilEmi hibManagerSU;

    public PlantillaDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
        hibManagerSU = new HibernateUtilEmi();
    }

    public boolean GuardarOActualizar(MPlantilla plantilla) {
        boolean ok = true;
        try {
            hibManagerSU.initTransaction();
            hibManagerSU.getSession().saveOrUpdate(plantilla);
            hibManagerSU.getTransaction().commit();
        } catch (HibernateException ex) {
            ok = false;
            hibManagerSU.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            hibManagerSU.closeSession();
        }
        return ok;
    }

    public List<MPlantilla> ListaPlantillaEmpresa(int idempresa) {
        List<MPlantilla> plantillas = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MPlantilla.class);
            //cr.add(Restrictions.eq("estatus", 1));
            cr.add(Restrictions.eq("empresa.id", idempresa));
            cr.setMaxResults(100);
            cr.addOrder(Order.asc("nombre"));
            plantillas = cr.list();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            hibManagerRO.closeSession();
        }

        return plantillas;
    }

    public boolean existPlantilla(int idempresa, String plantilla) {
        int count = 0;
        boolean res = false;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MPlantilla.class);
            cr.add(Restrictions.eq("empresa.id", idempresa));
            cr.add(Restrictions.eq("nombre", plantilla));
            cr.setProjection(Projections.rowCount());
            count = (Integer) cr.uniqueResult();
            if(count>0)
                res = true;
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            hibManagerRO.closeSession();
        }
        return res;
    }

    public MPlantilla BuscarPlantilla(int id) {
        MPlantilla plantilla = null;
        try {
            hibManagerRO.initTransaction();
            plantilla = (MPlantilla) hibManagerRO.getSession().get(MPlantilla.class, id);
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            hibManagerRO.closeSession();
        }

        return plantilla;
    }

    public boolean BorrarPlantilla(MPlantilla plantilla) {
        boolean var = false;
        try {
            hibManagerSU.initTransaction();
            hibManagerSU.getSession().delete(plantilla);
            var = true;
            /**
             * Criteria cr = session.createCriteria(MEmpresa.class);
             * cr.add(Restrictions.eq("plantilla.id", plantilla.getId()));
             * List<MEmpresa> empresas= cr.list(); if(empresas==null ||
             * empresas.isEmpty()){
             *
             *
             * }*
             */
            hibManagerSU.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerSU.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            hibManagerSU.closeSession();
        }
        return var;

    }

    public boolean ActualizarCFD(MPlantilla plantilla, MEmpresa empresa) {
        boolean var = false;
        try {
            String hql = "Update M_CFD set plantilla_id= :idPlantilla where empresa_id= :idEmpresa and plantilla_id is null";
            hibManagerSU.initTransaction();
            hibManagerSU.getSession().createSQLQuery(hql)
                    .setInteger("idPlantilla", plantilla.getId())
                    .setInteger("idEmpresa", empresa.getId()).executeUpdate();
            var = true;
            hibManagerSU.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerSU.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            hibManagerSU.closeSession();
        }
        return var;

    }

}
