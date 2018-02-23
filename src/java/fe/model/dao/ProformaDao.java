package fe.model.dao;

import fe.db.MAcceso;
import fe.db.MCfdProforma;
import fe.db.MCfdXmlProforma;
import fe.db.MOtroProforma;
import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ProformaDao implements Serializable {

    private HibernateUtilApl hibManagerRO;
    private HibernateUtilEmi hibManagerSU;
    public int rowCount = 0;

    public ProformaDao() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
        hibManagerSU = new HibernateUtilEmi();
    }

    public boolean actualiza(MCfdProforma cfdi) {
        boolean val = false;
        try {
            hibManagerSU.initTransaction();
            hibManagerSU.getSession().saveOrUpdate(cfdi);
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

    public MCfdProforma BuscarId(int id) {
        MCfdProforma cfd = null;
        try {
            hibManagerRO.initTransaction();
            cfd = (MCfdProforma) hibManagerRO.getSession().get(MCfdProforma.class, id);
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            ex.printStackTrace();
            hibManagerRO.getTransaction().rollback();
        } finally {
            hibManagerRO.closeSession();
        }
        return cfd;
    }




    public List<MCfdProforma> ListaParametros(int idUser, Integer[] idsEmpresas,
                                              String columna, String value,
                                              Date fechaDesde, Date fechaHasta, int estatus,
                                              int first, int pageSize) {
        List<MCfdProforma> listacfd = null;
        try {
            hibManagerRO.initTransaction();
            boolean estan = true;
            Criteria cr = hibManagerRO.getSession().createCriteria(MCfdProforma.class);
            MAcceso usuario = (MAcceso) hibManagerRO.getSession().get(MAcceso.class, idUser);
            String[] sector = null;

            if (usuario.getSector() != null && !"".equals(usuario.getSector().trim())) {
                sector = usuario.getSector().split(",");
            }


            if (idsEmpresas == null || idsEmpresas.length == 0) {
                if (usuario.getEmpresas() != null && !usuario.getEmpresas().isEmpty()) {
                    Integer[] idsemp = new Integer[usuario.getEmpresas().size()];
                    for (int z = 0; z < usuario.getEmpresas().size(); z++) {
                        idsemp[z] = usuario.getEmpresas().get(z).getId();
                    }
                    cr.add(Restrictions.in("empresaId", idsemp));
                }
            } else {
                cr.add(Restrictions.in("empresaId", idsEmpresas));
            }


            if (estatus >= 0) {
                cr.add(Restrictions.eq("es", estatus));
            }

            boolean entroOt = false;
            Criteria motro = hibManagerRO.getSession().createCriteria(MOtroProforma.class);
            if (sector != null && sector.length > 0) {
                motro.add(Restrictions.in("param7", sector));
                entroOt = true;
            }


            if (columna != null
                    && !columna.isEmpty()
                    && !columna.trim().equals("-1")
                    && value != null
                    && !"".equals(value.trim())) {
                if (columna.contains("param")) {
                    motro.add(Restrictions.eq(columna.trim(), value.trim()));
                    entroOt = true;
                } else {
                    if (columna.equals("folioErp")) {
                        String[] folios = value.trim().split(",");
                        cr.add(Restrictions.in(columna.trim(), folios));
                    } else {
                        cr.add(Restrictions.eq(columna.trim(), value.trim()));
                    }
                }
            }

            if (entroOt) {
                List<MOtroProforma> otros = motro.list();
                if (otros != null && !otros.isEmpty()) {
                    Integer[] idcfds = new Integer[otros.size()];
                    for (int z = 0; z < otros.size(); z++) {
                        idcfds[z] = otros.get(z).getCfdId();
                    }
                    cr.add(Restrictions.in("id", idcfds));

                }
            }

            if (fechaDesde != null) {
                Calendar dia = Calendar.getInstance();
                dia.setTime(fechaDesde);
                dia.set(Calendar.HOUR, 0);
                dia.set(Calendar.MINUTE, 0);
                dia.set(Calendar.SECOND, 0);
                fechaDesde = dia.getTime();
                cr.add(Expression.ge("fecha", fechaDesde));
            }

            if(fechaHasta != null){
                Calendar diaF = Calendar.getInstance();
                diaF.setTime(fechaHasta);
                diaF.set(Calendar.HOUR, 23);
                diaF.set(Calendar.MINUTE, 59);
                diaF.set(Calendar.SECOND, 59);
                fechaHasta = diaF.getTime();
                cr.add(Expression.le("fecha", fechaHasta));
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


    public List<MCfdProforma> ListaParametrosClientes(int idUser, Integer[] idsEmpresas, String columna, String value,
                                                      Date fechaDesde, Date fechaHasta, String cliente,
                                                      int first, int pageSize) {
        List<MCfdProforma> listacfd = null;
        try {
            if (cliente != null && !cliente.trim().equals("")) {
                hibManagerRO.initTransaction();
                boolean estan = true;
                Criteria cr = hibManagerRO.getSession().createCriteria(MCfdProforma.class);
                MAcceso usuario = (MAcceso) hibManagerRO.getSession().get(MAcceso.class, idUser);
                String[] sector = null;
                if (usuario.getSector() != null && !"".equals(usuario.getSector().trim())) {
                    sector = usuario.getSector().split(",");
                }

                if (idsEmpresas == null || idsEmpresas.length == 0) {
                    if (usuario.getEmpresas() != null && !usuario.getEmpresas().isEmpty()) {
                        Integer[] idsemp = new Integer[usuario.getEmpresas().size()];
                        for (int z = 0; z < usuario.getEmpresas().size(); z++) {
                            idsemp[z] = usuario.getEmpresas().get(z).getId();
                        }
                        cr.add(Restrictions.in("empresaId", idsemp));
                    }
                } else {
                    cr.add(Restrictions.in("empresaId", idsEmpresas));
                }


                Criteria motro = hibManagerRO.getSession().createCriteria(MOtroProforma.class);
                motro.add(Restrictions.eq("param5", cliente));
                if (sector != null && sector.length > 0) {
                    motro.add(Restrictions.in("param7", sector));
                }

                if (columna != null
                        && !columna.isEmpty()
                        && !columna.trim().equals("-1")
                        && value != null
                        && !"".equals(value.trim())) {
                    if (columna.contains("param")) {
                        motro.add(Restrictions.eq(columna.trim(), value.trim()));
                    } else {
                        if (columna.equals("folioErp")) {
                            String[] folios = value.trim().split(",");
                            cr.add(Restrictions.in(columna.trim(), folios));
                        } else {
                            cr.add(Restrictions.eq(columna.trim(), value.trim()));
                        }
                    }
                }

                List<MOtroProforma> otros = motro.list();
                if (otros != null && !otros.isEmpty()) {
                    Integer[] idcfds = new Integer[otros.size()];
                    for (int z = 0; z < otros.size(); z++) {
                        idcfds[z] = otros.get(z).getCfdId();
                    }
                    cr.add(Restrictions.in("id", idcfds));
                } else {
                    estan = false;
                }

                if (fechaDesde != null) {
                    Calendar dia = Calendar.getInstance();
                    dia.setTime(fechaDesde);
                    dia.set(Calendar.HOUR, 0);
                    dia.set(Calendar.MINUTE, 0);
                    dia.set(Calendar.SECOND, 0);
                    fechaDesde = dia.getTime();
                    cr.add(Expression.ge("fecha", fechaDesde));
                }

                if(fechaHasta != null){
                    Calendar diaF = Calendar.getInstance();
                    diaF.setTime(fechaHasta);
                    diaF.set(Calendar.HOUR, 23);
                    diaF.set(Calendar.MINUTE, 59);
                    diaF.set(Calendar.SECOND, 59);
                    fechaHasta = diaF.getTime();
                    cr.add(Expression.le("fecha", fechaHasta));
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


    public List<MCfdProforma> ListaParametrosExportLazy(int idUser, Integer[] idsEmpresas,
                                                        String columna, String value,
                                                        Date fechaDesde, Date fechaHasta,
                                                        int estatus, int first, int pageSize) {
        List<MCfdProforma> listacfd = null;
        try {
            hibManagerRO.initTransaction();

            boolean estan = true;
            Criteria cr = hibManagerRO.getSession().createCriteria(MCfdProforma.class);
            MAcceso usuario = (MAcceso) hibManagerRO.getSession().get(MAcceso.class, idUser);
            String[] sector = null;
            if (usuario.getSector() != null && !"".equals(usuario.getSector().trim())) {
                sector = usuario.getSector().split(",");
            }

            if (idsEmpresas == null || idsEmpresas.length == 0) {
                if (usuario.getEmpresas() != null && !usuario.getEmpresas().isEmpty()) {
                    Integer[] idsemp = new Integer[usuario.getEmpresas().size()];
                    for (int z = 0; z < usuario.getEmpresas().size(); z++) {
                        idsemp[z] = usuario.getEmpresas().get(z).getId();
                    }
                    cr.add(Restrictions.in("empresaId", idsemp));
                }
            } else {
                cr.add(Restrictions.in("empresaId", idsEmpresas));
            }

            if (estatus >= 0) {
                cr.add(Restrictions.eq("estadoDocumento", estatus));
            }

            boolean entroOt = false;
            Criteria motro = hibManagerRO.getSession().createCriteria(MOtroProforma.class);
            if (sector != null && sector.length > 0) {
                motro.add(Restrictions.in("param7", sector));
                entroOt = true;
            }

            if (columna != null && !columna.isEmpty()
                    && !columna.trim().equals("-1") && value != null
                    && !"".equals(value.trim())) {
                if (columna.contains("param")) {
                    entroOt = true;
                    motro.add(Restrictions.eq(columna.trim(), value.trim()));
                } else {
                    if (columna.equals("folioErp")) {
                        String[] folios = value.trim().split(",");
                        cr.add(Restrictions.in(columna.trim(), folios));
                    } else {
                        cr.add(Restrictions.eq(columna.trim(), value.trim()));
                    }
                }
            }

            if (entroOt) {
                List<MOtroProforma> otros = motro.list();
                if (otros != null && !otros.isEmpty()) {
                    Integer[] idcfds = new Integer[otros.size()];
                    for (int z = 0; z < otros.size(); z++) {
                        idcfds[z] = otros.get(z).getCfdId();
                    }
                    cr.add(Restrictions.in("id", idcfds));

                } else {
                    estan = false;
                    cr.setMaxResults(0);
                }
            } else {
                motro = null;
            }


            if (fechaDesde != null) {
                Calendar dia = Calendar.getInstance();
                dia.setTime(fechaDesde);
                dia.set(Calendar.HOUR, 0);
                dia.set(Calendar.MINUTE, 0);
                dia.set(Calendar.SECOND, 0);
                fechaDesde = dia.getTime();
                cr.add(Expression.ge("fecha", fechaDesde));
            }

            if(fechaHasta != null){
                Calendar diaF = Calendar.getInstance();
                diaF.setTime(fechaHasta);
                diaF.set(Calendar.HOUR, 23);
                diaF.set(Calendar.MINUTE, 59);
                diaF.set(Calendar.SECOND, 59);
                fechaHasta = diaF.getTime();
                cr.add(Expression.le("fecha", fechaHasta));
            }
            cr.addOrder(Order.desc("fecha"));

            //paginacion row count
            cr.setProjection(Projections.rowCount());
            rowCount = (Integer) cr.uniqueResult();
            //Reset
            cr.setProjection(null);
            cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

            //Pagination
            cr.setFirstResult(first);
            cr.setMaxResults(pageSize);

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

    public MOtroProforma Otro(int id) {
        MOtroProforma otro = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MOtroProforma.class);
            cr.add(Restrictions.eq("cfdId", id));
            otro = (MOtroProforma) cr.uniqueResult();
            hibManagerRO.getTransaction().commit();
        } catch (org.hibernate.NonUniqueResultException ex) {
            ex.printStackTrace();
            hibManagerRO.getTransaction().rollback();
            System.out.println(">>>>>>>>>> Existen dos registros de MOtroProforma para el FACTURA: " + id);
            return null;
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.out);
        } finally {
            hibManagerRO.closeSession();
        }
        return otro;
    }

    public MCfdXmlProforma findXmlByCfdiId(int id) {
        MCfdXmlProforma xml = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MCfdXmlProforma.class);
            cr.add(Restrictions.eq("cfdId", id));
            xml = (MCfdXmlProforma) cr.uniqueResult();
            hibManagerRO.getTransaction().commit();
        } catch (org.hibernate.NonUniqueResultException ex) {
            ex.printStackTrace();
            hibManagerRO.getTransaction().rollback();
            System.out.println(">>>>>>>>>> Existen dos registros de MOtroProforma para el FACTURA: " + id);
            return null;
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.out);
        } finally {
            hibManagerRO.closeSession();
        }
        return xml;
    }
}
