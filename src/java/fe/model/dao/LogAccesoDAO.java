/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.model.dao;

import fe.db.MAcceso;
import fe.db.MLogAcceso;
import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;
import java.io.Serializable;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Lily
 */
@SuppressWarnings("deprecation")
public class LogAccesoDAO implements Serializable {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public int rowCount = 0;
    private HibernateUtilApl hibManagerRO;
    private HibernateUtilEmi hibManagerSU;

    public LogAccesoDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
        hibManagerSU = new HibernateUtilEmi();
    }

    public boolean guardaRegistro(MAcceso a, String mensaje) {
        boolean guardo = false;
        try {
            if (hibManagerSU.initTransaction()) {
                MLogAcceso logAcces = new MLogAcceso(null, a, Calendar.getInstance().getTime(), mensaje);
                hibManagerSU.getSession().save(logAcces);
                hibManagerSU.getTransaction().commit();
                guardo = true;
            }
        } catch (HibernateException e) {
            hibManagerSU.getTransaction().rollback();
            e.printStackTrace(System.err);
        } finally {
            hibManagerSU.closeSession();
        }
        return guardo;
    }

    @SuppressWarnings("unused")
    public boolean comienzaAccion(Map<String, Object> sesion, String Mensaje) throws Exception {
        boolean valor = false;
        MAcceso acc = (MAcceso) sesion.get("acceso");
        if (guardaRegistro(acc, Mensaje)) {
            return true;
        } else {
            return false;
        }
    }

    @SuppressWarnings("unused")
    public boolean comienzaAccion(MAcceso acc, String Mensaje) throws Exception {
        if (guardaRegistro(acc, Mensaje)) {
            return true;
        } else {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public List<MLogAcceso> ListaLogAcceso(Integer idacceso) {
        List<MLogAcceso> ListaLog = null;
        try {
            hibManagerRO.initTransaction();
            MAcceso acceso = (MAcceso) hibManagerRO.getSession().get(MAcceso.class, idacceso);
            if (acceso.getEmpresas() != null && !acceso.getEmpresas().isEmpty()) {
                Integer[] idem = new Integer[acceso.getEmpresas().size()];
                for (int i = 0; i < acceso.getEmpresas().size(); i++) {
                    idem[i] = acceso.getEmpresas().get(i).getId();
                }
                Criteria cr = hibManagerRO.getSession().createCriteria(MAcceso.class);
                cr.add(Restrictions.in("empresa.id", idem));
                List<MAcceso> Listaacces = cr.list();
                if (Listaacces != null) {
                    Integer[] idsac = new Integer[Listaacces.size()];
                    for (int j = 0; j < Listaacces.size(); j++) {
                        idsac[j] = Listaacces.get(j).getId();
                    }
                    Criteria crlo = hibManagerRO.getSession().createCriteria(MLogAcceso.class);
                    crlo.add(Restrictions.in("acceso.id", idsac));
                    crlo.addOrder(Order.desc("fecha"));
                    crlo.setMaxResults(100);
                    ListaLog = crlo.list();
                }

            }
            hibManagerRO.getTransaction().commit();

        } catch (HibernateException e) {
            hibManagerRO.getTransaction().rollback();
            e.printStackTrace(System.err);
        } finally {
            hibManagerRO.closeSession();
        }
        return ListaLog;
    }

    @SuppressWarnings("unchecked")
    public List<MLogAcceso> ListaLog100Acceso() {
        List<MLogAcceso> ListaLog = null;
        try {
            hibManagerRO.initTransaction();
            Criteria crlo = hibManagerRO.getSession().createCriteria(MLogAcceso.class);
            crlo.addOrder(Order.desc("fecha"));
            crlo.setMaxResults(100);
            ListaLog = crlo.list();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException e) {
            hibManagerRO.getTransaction().rollback();
            e.printStackTrace(System.err);
        } finally {
            hibManagerRO.closeSession();
        }
        return ListaLog;
    }

    @SuppressWarnings("unchecked")
    public List<MLogAcceso> ListaDeTodoLogdefecha(Integer idacceso,
            int idempresa, String columna, String value, Date fecha, int first, int pageSize)
            throws Exception {
        List<MLogAcceso> ListlogAcc = null;
        try {
            hibManagerRO.initTransaction();
            Criteria logs = hibManagerRO.getSession().createCriteria(MLogAcceso.class);
            if (idempresa <= 0) {
                MAcceso acceso = (MAcceso) hibManagerRO.getSession().get(MAcceso.class, idacceso);
                if (acceso.getEmpresas() != null
                        && !acceso.getEmpresas().isEmpty()) {
                    Integer[] idem = new Integer[acceso.getEmpresas().size()];
                    for (int i = 0; i < acceso.getEmpresas().size(); i++) {
                        idem[i] = acceso.getEmpresas().get(i).getId();
                    }
                    Criteria cr = hibManagerRO.getSession().createCriteria(MAcceso.class);
                    cr.add(Restrictions.in("empresa.id", idem));
                    if (columna.trim().equals("Usuario")) {
                        cr.add(Restrictions.ilike("usuario", "%" + value + "%"));
                    }
                    List<MAcceso> Listaacces = cr.list();
                    if (Listaacces != null) {
                        Integer[] idsac = new Integer[Listaacces.size()];
                        for (int j = 0; j < Listaacces.size(); j++) {
                            idsac[j] = Listaacces.get(j).getId();
                        }
                        logs.add(Restrictions.in("acceso.id", idsac));
                    }
                }
            } else {
                Criteria cr = hibManagerRO.getSession().createCriteria(MAcceso.class);
                cr.add(Restrictions.eq("empresa.id", idempresa));
                if (columna.trim().equals("Usuario")) {
                    cr.add(Restrictions.ilike("usuario", "%" + value + "%"));
                }
                List<MAcceso> Listaacces = cr.list();
                if (Listaacces != null) {
                    Integer[] idsac = new Integer[Listaacces.size()];
                    for (int j = 0; j < Listaacces.size(); j++) {
                        idsac[j] = Listaacces.get(j).getId();
                    }
                    logs.add(Restrictions.in("acceso.id", idsac));
                }
            }
            if (!columna.trim().equals("Todos")
                    && !columna.trim().equals("Ninguno")) {
//				System.out.println("columna" + columna);
                if (!columna.trim().equals("Usuario")) {
                    logs.add(Restrictions.ilike(columna.trim(), "%" + value
                            + "%"));
                }
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
                logs.add(Expression.ge("fecha", FechaI));
                logs.add(Expression.le("fecha", FechaF));
            }
            logs.addOrder(Order.desc("fecha"));

            //paginacion
            logs.setProjection(Projections.rowCount());
            rowCount = (Integer) logs.uniqueResult();
            //Reset
            logs.setProjection(null);
            logs.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

            //Pagination
            logs.setFirstResult(first);
            logs.setMaxResults(pageSize);

            ListlogAcc = logs.list();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException e) {
            hibManagerRO.getTransaction().rollback();
            e.printStackTrace(System.err);
        } finally {
            hibManagerRO.closeSession();
        }
        return ListlogAcc;
    }

}
