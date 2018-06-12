package fe.model.dao;

import fe.db.MAcceso;
import fe.db.MInvoice;
import fe.model.util.hibernateutil.HibernateUtilApl;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SuppressWarnings("deprecation")
public class CfdDAO implements Serializable {

    private HibernateUtilApl hibManagerRO;

    public CfdDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
    }

    public List<MInvoice> ListMcfd(int idUser) {
        List<MInvoice> listacfd = null;
        try {
            hibManagerRO.initTransaction();
            MAcceso usuario = (MAcceso) hibManagerRO.getSession().get(MAcceso.class, idUser);
            if (usuario.getEmpresas() != null
                    && !usuario.getEmpresas().isEmpty()) {
                Integer[] idsemp = new Integer[usuario.getEmpresas().size()];
                for (int z = 0; z < usuario.getEmpresas().size(); z++) {
                    idsemp[z] = usuario.getEmpresas().get(z).getId();
                }
                Criteria cr = hibManagerRO.getSession().createCriteria(MInvoice.class);
                cr.add(Restrictions.in("empresa.id", idsemp));
                cr.addOrder(Order.desc("fecha"));
                cr.setMaxResults(300);
                listacfd = cr.list();
            }
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return listacfd;
    }

    @SuppressWarnings({"unchecked"})
    public List<MInvoice> ListaParametros(int idUser, int idEmpresa,
            String columna, String value, Date fechaDesde, Date fechaHasta,
            int estatus) {
        List<MInvoice> listacfd = null;
        try {
            hibManagerRO.initTransaction();
            // Verificar si aparte de la empresa se buscaran por otros
            // parametros
            boolean estan = true;
            Criteria cr = hibManagerRO.getSession().createCriteria(MInvoice.class);
            if (idEmpresa <= 0) {
                MAcceso usuario = (MAcceso) hibManagerRO.getSession().get(MAcceso.class, idUser);
                if (usuario.getEmpresas() != null
                        && !usuario.getEmpresas().isEmpty()) {
                    Integer[] idsemp = new Integer[usuario.getEmpresas().size()];
                    for (int z = 0; z < usuario.getEmpresas().size(); z++) {
                        idsemp[z] = usuario.getEmpresas().get(z).getId();
                    }
                    cr.add(Restrictions.in("empresa.id", idsemp));
                }
            } else {
//				System.out.println("empresa. id " + idEmpresa);
                cr.add(Restrictions.eq("empresa.id", idEmpresa));
            }
            if (estatus >= 0) {
                cr.add(Restrictions.eq("estatusDoc", estatus));
            }
//			System.out.println("columna 1" + columna + " value" + value);
            if (columna != null && !columna.isEmpty()
                    && !columna.trim().equals("-1") && value != null
                    && !"".equals(value.trim())) {
//				System.out.println("columna 3" + columna + " value" + value);
                cr.add(Restrictions.isNotNull(columna));
                cr.add(Restrictions.ilike(columna, "%" + value + "%"));

            }

            if (fechaDesde != null && fechaHasta != null) {
                Calendar dia = Calendar.getInstance();
                dia.setTime(fechaDesde);
                dia.set(Calendar.HOUR, 0);
                dia.set(Calendar.MINUTE, 0);
                dia.set(Calendar.SECOND, 0);
                fechaDesde = dia.getTime();
                Calendar diaF = Calendar.getInstance();
                diaF.setTime(fechaHasta);
                diaF.set(Calendar.HOUR, 23);
                diaF.set(Calendar.MINUTE, 59);
                diaF.set(Calendar.SECOND, 59);
                fechaHasta = diaF.getTime();
                cr.add(Expression.ge("fecha", fechaDesde));
                cr.add(Expression.le("fecha", fechaHasta));
            }
            if (estan) {
                cr.addOrder(Order.desc("fecha"));
                cr.setMaxResults(300);
            } else {
                cr.setMaxResults(0);
            }
            listacfd = cr.list();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return listacfd;
    }

    @SuppressWarnings("unchecked")
    public List<MInvoice> ListaParametrosClientes(int idUser, int idEmpresa,
            String columna, String value, Date fechaDesde, Date fechaHasta,
            String cliente) {
        List<MInvoice> listacfd = null;
        try {
            if (cliente != null && !cliente.trim().equals("")) {
                hibManagerRO.initTransaction();
                // Verificar si aparte de la empresa se buscaran por otros
                // parametros
                boolean estan = true;
                Criteria cr = hibManagerRO.getSession().createCriteria(MInvoice.class);
                if (idEmpresa <= 0) {
                    MAcceso usuario = (MAcceso) hibManagerRO.getSession().get(MAcceso.class,
                            idUser);
                    if (usuario.getEmpresas() != null
                            && !usuario.getEmpresas().isEmpty()) {
                        Integer[] idsemp = new Integer[usuario.getEmpresas()
                                .size()];
                        for (int z = 0; z < usuario.getEmpresas().size(); z++) {
                            idsemp[z] = usuario.getEmpresas().get(z).getId();
                        }
                        cr.add(Restrictions.in("empresa.id", idsemp));
                    }
                } else {
                    // System.out.println("empresa. id " + idEmpresa);
                    cr.add(Restrictions.eq("empresa.id", idEmpresa));
                }
                cr.add(Restrictions.eq("noCliente", cliente));

                if (columna != null && !columna.isEmpty()
                        && !columna.trim().equals("-1") && value != null
                        && !"".equals(value.trim())) {
                    cr.add(Restrictions.ilike(columna, "%" + value + "%"));

                }

                if (fechaDesde != null && fechaHasta != null) {
                    Calendar dia = Calendar.getInstance();
                    dia.setTime(fechaDesde);
                    dia.set(Calendar.HOUR, 0);
                    dia.set(Calendar.MINUTE, 0);
                    dia.set(Calendar.SECOND, 0);
                    fechaDesde = dia.getTime();
                    Calendar diaF = Calendar.getInstance();
                    diaF.setTime(fechaHasta);
                    diaF.set(Calendar.HOUR, 23);
                    diaF.set(Calendar.MINUTE, 59);
                    diaF.set(Calendar.SECOND, 59);
                    fechaHasta = diaF.getTime();
                    cr.add(Expression.ge("fecha", fechaDesde));
                    cr.add(Expression.le("fecha", fechaHasta));
                }
                if (estan) {
                    cr.addOrder(Order.desc("fecha"));
                    cr.setMaxResults(300);
                }
                listacfd = cr.list();
                hibManagerRO.getTransaction().commit();
            }
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return listacfd;
    }

    @SuppressWarnings("unchecked")
    public List<MInvoice> ListaSelec(Integer[] ids) {
        List<MInvoice> cfds = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MInvoice.class);
            cr.add(Restrictions.in("id", ids));
            cfds = cr.list();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            ex.printStackTrace(System.out);
            hibManagerRO.getTransaction().rollback();
        } finally {
            hibManagerRO.closeSession();
        }

        return cfds;
    }

    public MInvoice BuscarId(Integer id) {
        MInvoice cfds = null;
        try {
            hibManagerRO.initTransaction();
            cfds = (MInvoice) hibManagerRO.getSession().get(MInvoice.class, id);
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.out);
        } finally {
            hibManagerRO.closeSession();
        }

        return cfds;
    }

    @SuppressWarnings("unchecked")
    public List<MInvoice> BuscarFolioSerie(String folioERP, String serieERP,
            Integer empresa) {
        List<MInvoice> cfd = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MInvoice.class);
            cr.add(Restrictions.eq("folioErp", folioERP));
            if (serieERP != null && !serieERP.trim().equals("")) {
                cr.add(Restrictions.eq("serieErp", serieERP));
            } else {
                cr.add(Restrictions.isNull("serieErp"));
            }
            cr.add(Restrictions.eq("empresa.id", empresa));
            cr.add(Restrictions.eq("estatusDoc", 1));
            cfd = cr.list();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return cfd;
    }

}
