/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.model.dao;

/**
 *
 * @author Lily
 */

import fe.db.MAcceso;
import fe.db.MAcceso.Nivel;
import fe.db.MLogApp;
import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.*;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SuppressWarnings("deprecation")
public class LogAPPDAO implements Serializable {

    public int rowCount = 0;
    private HibernateUtilApl hibManagerRO;
    private HibernateUtilEmi hibManagerSU;

    public LogAPPDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
        hibManagerSU = new HibernateUtilEmi();
    }

    @SuppressWarnings("unchecked")
    public List<MLogApp> ListaLogApp(Integer iduser) {
        List<MLogApp> ListLogs = null;
        try {
            hibManagerRO.initTransaction();
//			System.out.println("id::: " + iduser);
            MAcceso acceso = (MAcceso) hibManagerRO.getSession().get(MAcceso.class, iduser);
            if (acceso != null && acceso.getEmpresas() != null
                    && !acceso.getEmpresas().isEmpty()) {
                Integer[] idemps = new Integer[acceso.getEmpresas().size()];
                for (int i = 0; i < acceso.getEmpresas().size(); i++) {
                    idemps[i] = acceso.getEmpresas().get(i).getId();
                }
                Criteria cr = hibManagerRO.getSession().createCriteria(MLogApp.class);
                cr.add(Restrictions.isNotNull("empresa"));
                cr.add(Restrictions.in("empresa.id", idemps));
                cr.addOrder(Order.desc("fecha"));
                cr.setMaxResults(100);
                ListLogs = cr.list();
            } else if (acceso.getNivel() == Nivel.INTERNO) {
                Criteria cr = hibManagerRO.getSession().createCriteria(MLogApp.class);
                cr.addOrder(Order.desc("fecha"));
                cr.setMaxResults(100);
                ListLogs = cr.list();
            }
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return ListLogs;
    }

    /*@SuppressWarnings("unchecked")
    public List<MLogApp> BusquedaListaLogsApp(Integer iduser, int idempresa, Date fecha) {
        List<MLogApp> ListLogs = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MLogApp.class);
            cr.add(Restrictions.isNotNull("empresa"));
            if (idempresa > 0) {
                cr.add(Restrictions.eq("empresa.id", idempresa));
            } else {
                MAcceso acceso = (MAcceso) hibManagerRO.getSession().get(MAcceso.class, iduser);
                if (acceso != null && acceso.getEmpresas() != null
                        && !acceso.getEmpresas().isEmpty()) {
                    Integer[] idemps = new Integer[acceso.getEmpresas().size()];
                    for (int i = 0; i < acceso.getEmpresas().size(); i++) {
                        idemps[i] = acceso.getEmpresas().get(i).getId();
                    }
                    cr.add(Restrictions.in("empresa.id", idemps));
                }
            }
            //System.out.println("fecha"+ fecha);
            if (fecha != null) {
                Calendar dia = Calendar.getInstance();
                dia.setTime(fecha);
                dia.set(Calendar.HOUR, 0);
                dia.set(Calendar.MINUTE, 0);
                dia.set(Calendar.SECOND, 0);
                Date FechaI = dia.getTime();
                //System.out.println("fechaI"+ FechaI);
                dia.set(Calendar.HOUR, 23);
                dia.set(Calendar.MINUTE, 59);
                dia.set(Calendar.SECOND, 59);
                Date FechaF = dia.getTime();
                //System.out.println("fechaI"+ FechaF);
                cr.add(Expression.ge("fecha", FechaI));
                cr.add(Expression.le("fecha", FechaF));
            }

            cr.addOrder(Order.desc("fecha"));
            cr.setMaxResults(100);
            ListLogs = cr.list();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return ListLogs;
    }*/


    @SuppressWarnings("unchecked")
    public List<MLogApp> BusquedaPorParametros(Integer iduser, int idempresa, String serie, String folioErp, Date fecha, int first, int pageSize) {
        Integer[]idempresas = {idempresa};
        return BusquedaPorParametros(iduser, idempresas, serie, folioErp,"N", fecha, first, pageSize);
    }

    @SuppressWarnings("unchecked")
    public List<MLogApp> BusquedaPorParametros(Integer iduser, Integer[] idempresa, String serie, String folioErp, String tipoServicio,  Date fecha, int first, int pageSize) {
        List<MLogApp> ListLogs = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MLogApp.class);
            cr.add(Restrictions.isNotNull("empresa"));

            MAcceso usuario = (MAcceso) hibManagerRO.getSession().get(MAcceso.class, iduser);
            if (idempresa == null || idempresa.length == 0) {
                if (usuario.getEmpresas() != null && !usuario.getEmpresas().isEmpty()) {
                    Integer[] idsemp = new Integer[usuario.getEmpresas().size()];
                    for (int z = 0; z < usuario.getEmpresas().size(); z++) {
                        idsemp[z] = usuario.getEmpresas().get(z).getId();
                    }
                    cr.add(Restrictions.in("empresa.id", idsemp));
                }
            } else {
                cr.add(Restrictions.in("empresa.id", idempresa));
            }

//            if (idempresa > 0) {
//                cr.add(Restrictions.eq("empresa.id", idempresa));
//            } else {
//                //if(tipoServicio.equals("N")){
//                    MAcceso acceso = (MAcceso) hibManagerRO.getSession().get(MAcceso.class, iduser);
//                    if (acceso != null && acceso.getEmpresas() != null
//                            && !acceso.getEmpresas().isEmpty()) {
//                        Integer[] idemps = new Integer[acceso.getEmpresas().size()];
//                        for (int i = 0; i < acceso.getEmpresas().size(); i++) {
//                            idemps[i] = acceso.getEmpresas().get(i).getId();
//                        }
//                        cr.add(Restrictions.in("empresa.id", idemps));
//                    }
//                //}
//            }



            if(serie!= null && !serie.isEmpty()){
                cr.add(Restrictions.isNotNull("serie"));
                cr.add(Restrictions.eq("serie", serie.trim()));
            }

            if(folioErp!= null && !folioErp.isEmpty()){
                cr.add(Restrictions.isNotNull("folio"));
                cr.add(Restrictions.eq("folio", folioErp.trim()));
            }
            if (fecha != null) {
                Calendar dia = Calendar.getInstance();
                dia.setTime(fecha);
                dia.set(Calendar.HOUR, 0);
                dia.set(Calendar.MINUTE, 0);
                dia.set(Calendar.SECOND, 0);
                Date FechaI = dia.getTime();
                dia.set(Calendar.HOUR, 23);
                dia.set(Calendar.MINUTE, 59);
                dia.set(Calendar.SECOND, 59);
                Date FechaF = dia.getTime();
                cr.add(Expression.ge("fecha", FechaI));
                cr.add(Expression.le("fecha", FechaF));
            }

            if(!tipoServicio.equals("N")) {
                cr.add(Restrictions.eq("origen",2));
                if(!tipoServicio.equals("-1"))
                    cr.add(Restrictions.eq("estatus", Integer.valueOf(tipoServicio)));
            }

            cr.addOrder(Order.desc("fecha"));
            //paginacion
            cr.setProjection(Projections.rowCount());
            rowCount = (Integer) cr.uniqueResult();
            //Reset
            cr.setProjection(null);
            cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

            //Pagination
            cr.setFirstResult(first);
            cr.setMaxResults(pageSize);

            ListLogs = cr.list();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return ListLogs;
    }


}
