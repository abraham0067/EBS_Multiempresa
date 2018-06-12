package fe.model.dao;

import fe.db.CServiciosFacturacion;
import fe.db.MServiciosFacturacion;
import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;

public class ServiciosFacturacionDAO {

    private HibernateUtilApl hibernateUtilApl;
    private HibernateUtilEmi hibernateUtilEmi;

    public ServiciosFacturacionDAO() {
        hibernateUtilApl = new HibernateUtilApl();
        hibernateUtilEmi = new HibernateUtilEmi();
    }


    public List<CServiciosFacturacion> getServicios() {

        List<CServiciosFacturacion> listServicios = new ArrayList<>();

        try {
            hibernateUtilApl.initTransaction();
            Criteria cr = hibernateUtilApl.getSession().createCriteria(CServiciosFacturacion.class);
            cr.addOrder(Order.asc("servicio"));
            listServicios = cr.list();

        } catch (HibernateException ex) {
            hibernateUtilApl.getTransaction().rollback();
        } finally {
            hibernateUtilApl.closeSession();
        }

        return listServicios;
    }

    public List<MServiciosFacturacion> getServiciosFacturacion(int id) {
        List<MServiciosFacturacion> mServicios = new ArrayList<>();

        try {
            hibernateUtilApl.initTransaction();

            Criteria cr = hibernateUtilApl.getSession().createCriteria(MServiciosFacturacion.class);
            cr.add(Restrictions.eq("idEmpresa", id));
            mServicios = cr.list();

        } catch (HibernateException ex) {
            hibernateUtilApl.getTransaction().rollback();
        } finally {
            hibernateUtilApl.closeSession();
        }

        return mServicios;
    }

    public boolean actualizarEmpresaServicios(MServiciosFacturacion mServiciosFacturacion) {

        boolean res = true;

        try {
            hibernateUtilEmi.initTransaction();
            hibernateUtilEmi.getSession().saveOrUpdate(mServiciosFacturacion);
            hibernateUtilEmi.getTransaction().commit();

        } catch (HibernateException ex) {
            res = false;
            hibernateUtilEmi.getTransaction().rollback();
        } finally {
            hibernateUtilEmi.closeSession();
        }

        return res;

    }

    public boolean borrarServicio(int id) {
        boolean flag = true;

        hibernateUtilEmi.initTransaction();

        try {
            String hql = "DELETE FROM MServiciosFacturacion WHERE idEmpresa = :id";
            Query query = hibernateUtilEmi.getSession().createQuery(hql);
            query.setInteger("id", id);
            int rowCount = query.executeUpdate();
            hibernateUtilEmi.getTransaction().commit();
        } catch (HibernateException ex) {
            ex.printStackTrace();
            hibernateUtilEmi.getTransaction().rollback();
            flag = false;
        } finally {
            hibernateUtilEmi.closeSession();
        }

        return flag;
    }

    public boolean borrarMServicio(int idServicios){

        boolean flag = true;
        try{
            hibernateUtilEmi.initTransaction();
            String hql = "DELETE FROM MServiciosFacturacion WHERE idEmpresa = :id";
            Query query = hibernateUtilEmi.getSession().createQuery(hql);
            query.setInteger("id", idServicios);
            int rowCount = query.executeUpdate();
            hibernateUtilEmi.getTransaction().commit();

        }catch(HibernateException ex){
            hibernateUtilEmi.getTransaction().rollback();
            flag = false;
        }finally {
            hibernateUtilEmi.closeSession();
        }

        return flag;

    }

    public boolean agregarServicio(CServiciosFacturacion cServiciosFacturacion) {

        boolean flag = true;
        try {
            hibernateUtilEmi.initTransaction();
            hibernateUtilEmi.getSession().saveOrUpdate(cServiciosFacturacion);
            hibernateUtilEmi.getTransaction().commit();
        } catch (HibernateException ex) {
            flag = false;
            hibernateUtilEmi.getTransaction().rollback();
        } finally {
            hibernateUtilEmi.closeSession();
        }
        return flag;
    }

    public CServiciosFacturacion buscarServicio(String nombre) {

        CServiciosFacturacion cServiciosFacturacion1 = null;
        try {
            hibernateUtilApl.initTransaction();
            Criteria cr = hibernateUtilApl.getSession().createCriteria(CServiciosFacturacion.class).add(Restrictions.eq("servicio", nombre));
            cServiciosFacturacion1 = (CServiciosFacturacion) cr.uniqueResult();
            hibernateUtilApl.getTransaction().commit();
        } catch (HibernateException ex) {
            hibernateUtilApl.getTransaction().rollback();
        } finally {
            hibernateUtilApl.closeSession();
        }

        return cServiciosFacturacion1;
    }

    public void borrarServicioCatalogo(int id) throws HibernateException {
        boolean flag = true;

        hibernateUtilEmi.initTransaction();

        try {
            String hql = "DELETE FROM CServiciosFacturacion WHERE id = :id";
            Query query = hibernateUtilEmi.getSession().createQuery(hql);
            query.setInteger("id", id);
            int rowCount = query.executeUpdate();
            hibernateUtilEmi.getTransaction().commit();
        } finally {
            hibernateUtilEmi.closeSession();
        }
    }


}
