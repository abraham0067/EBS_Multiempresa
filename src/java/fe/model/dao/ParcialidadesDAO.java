package fe.model.dao;

import fe.db.MParcialidades;
import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.*;

import java.io.Serializable;
import java.util.List;

public class ParcialidadesDAO implements Serializable {

    private HibernateUtilApl hibManagerRO;
    private HibernateUtilEmi hibManagerSU;

    public ParcialidadesDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
        hibManagerSU = new HibernateUtilEmi();
    }

    public boolean GuardarActualizacionPar(MParcialidades parcialidades) {
        boolean val = false;
        try {
            hibManagerSU.initTransaction();
            hibManagerSU.getSession().saveOrUpdate(parcialidades);
            hibManagerSU.getTransaction().commit();
            val = true;
        } catch (HibernateException ex) {
            hibManagerSU.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerSU.closeSession();
        }
        return val;
    }

    public MParcialidades BuscarId(int id) {
        MParcialidades parcialidades = null;
        try {
            hibManagerRO.initTransaction();
            parcialidades = (MParcialidades) hibManagerRO.getSession().get(MParcialidades.class, id);
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return parcialidades;
    }

    public List<MParcialidades> BusParDeIdCFD(int idCfd) {
        List<MParcialidades> parcialidades = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MParcialidades.class);
            cr.add(Restrictions.eq("cfd.id", idCfd));
            cr.add(Restrictions.eq("estatus", 1));
            cr.addOrder(Order.desc("numParcialidad"));
            parcialidades = cr.list();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return parcialidades;
    }

    public MParcialidades BusParMax_IdCFD(int idCfd) {
        MParcialidades parcialidades = null;
        try {
            ActualizarEstatusPar(idCfd);
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MParcialidades.class);
            cr.add(Restrictions.eq("cfd.id", idCfd));
            cr.add(Restrictions.eq("estatus", 1));
            cr.addOrder(Order.desc("numParcialidad"));
            cr.setMaxResults(1);
            parcialidades = (MParcialidades) cr.uniqueResult();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return parcialidades;
    }

    public void ActualizarEstatusPar(int idCfd) {

        try {
            // TODO: 07/08/2017 CORREGIR EJECUCION DEL QUERY
            hibManagerSU.initTransaction();
            String hql = "UPDATE M_PARCIALIDADES SET ESTATUS=0 "
                    + "WHERE PARCIALIDAD_ID IN ( "
                    + "SELECT ID FROM M_CFD WHERE "
                    + "ESTADO_DOCUMENTO=0 AND ID IN (SELECT PARCIALIDAD_ID FROM M_PARCIALIDADES "
                    + "WHERE ESTATUS=1 AND cfd_ID= :idCfd ))";
            int actual = hibManagerSU.getSession().createSQLQuery(hql)
                    .setInteger("idCfd", idCfd).executeUpdate();
            hibManagerSU.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerSU.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerSU.closeSession();
        }

    }

    public double BuscaTotalParcialidad(final Integer idOrigen) {
        double total = 0.0;
        try {
            if (hibManagerRO.initTransaction()) {
                Criteria cr = this.hibManagerRO.getSession().createCriteria((Class) MParcialidades.class);
                cr.add((Criterion) Restrictions.eq("MCfd.id", idOrigen));
                cr.add((Criterion) Restrictions.eq("estatus", 1));
                cr.setProjection((Projection) Projections.sum("total"));
                total = (Double) cr.uniqueResult();
                hibManagerRO.getTransaction().commit();
            }
        } catch (HibernateException e) {
            total = 0.0;
            hibManagerRO.getTransaction().rollback();
            e.printStackTrace(System.err);
        }finally {
            try {
                hibManagerRO.closeSession();
            } catch (Exception ex) {
                    }
            try {
                hibManagerRO.closeSession();
            }
            catch (Exception ex2) {}
        return total;
        }
    }

    }
