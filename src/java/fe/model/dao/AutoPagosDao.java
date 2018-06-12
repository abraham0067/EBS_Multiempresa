package fe.model.dao;

import fe.db.MAcceso;
import fe.db.MCfdPagos;
import fe.db.MCfdXmlPagos;
import fe.db.MOtroPagos;
import fe.model.util.hibernateutil.HibernateUtilApl;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.*;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class AutoPagosDao implements Serializable {
    private HibernateUtilApl hibManagerRO;
    public int rowCount = 0;

    public AutoPagosDao() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
    }


    public MCfdPagos BuscarId(int id) {
        MCfdPagos cfd = null;
        try {
            hibManagerRO.initTransaction();
            cfd = (MCfdPagos) hibManagerRO.getSession().get(MCfdPagos.class, id);
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            ex.printStackTrace();
            hibManagerRO.getTransaction().rollback();
        } finally {
            hibManagerRO.closeSession();
        }
        return cfd;
    }

    public MCfdXmlPagos findXmlByCfdiId(int id) {
        MCfdXmlPagos xml = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MCfdXmlPagos.class);
            cr.add(Restrictions.eq("cfdId", id));
            xml = (MCfdXmlPagos) cr.uniqueResult();
            hibManagerRO.getTransaction().commit();
        } catch (org.hibernate.NonUniqueResultException ex) {
            ex.printStackTrace();
            hibManagerRO.getTransaction().rollback();
            System.out.println(">>>>>>>>>> Existen dos registros de MOtroPagos para el FACTURA: " + id);
            return null;
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.out);
        } finally {
            hibManagerRO.closeSession();
        }
        return xml;
    }

    public List<MCfdPagos> ListaParametros(MAcceso usuario, Integer[] idsEmpresas, String columna,
                                           String value, Date fechaDesde, Date fechaHasta, int estatus,
                                           int first, int pageSize) {
        List<MCfdPagos> listacfd = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MCfdPagos.class);

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


            if (columna != null
                    && !columna.isEmpty()
                    && !columna.trim().equals("-1")
                    && value != null
                    && !"".equals(value.trim())) {
                if (columna.contains("param")) {

                } else {
                    if (columna.equals("folioErp")) {
                        String[] folios = value.trim().split(",");
                        cr.add(Restrictions.in(columna.trim(), folios));
                    } else if (columna.equals("serie")) {
                        String[] serie = value.trim().split(",");
                        cr.add(Restrictions.in(columna.trim(), serie));
                    } else {
                        cr.add(Restrictions.eq(columna.trim(), value.trim()));
                    }
                }
            }

            if (fechaDesde != null) {
                Calendar dia = Calendar.getInstance();
                dia.setTime(fechaDesde);
                dia.set(Calendar.HOUR, 0);
                dia.set(Calendar.MINUTE, 0);
                dia.set(Calendar.SECOND, 0);
                fechaDesde = dia.getTime();
                cr.add(Expression.le("fecha", fechaDesde));
            }

            if (fechaHasta != null) {
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

            ///ROW COUNT
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

    public List<MCfdPagos> ListaParametrosClientes(MAcceso usuario, Integer[] idsEmpresas, String columna, String value,
                                                   Date fechaDesde, Date fechaHasta, String cliente,
                                                   int first, int pageSize) {
        List<MCfdPagos> listacfd = null;
        try {
            if (cliente != null && !cliente.trim().equals("")) {

                hibManagerRO.initTransaction();
                boolean estan = true;
                Criteria cr = hibManagerRO.getSession().createCriteria(MCfdPagos.class);
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
                        cr.add(Restrictions.in("idEmpresa", idsemp));
                    }
                } else {
                    cr.add(Restrictions.in("idEmpresa", idsEmpresas));
                }

                Criteria motro = hibManagerRO.getSession().createCriteria(MOtroPagos.class);
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
                        } else if (columna.equals("serie")) {
                            String[] series = value.trim().split(",");
                            cr.add(Restrictions.in(columna.trim(), series));
                        } else {
                            cr.add(Restrictions.eq(columna.trim(), value.trim()));
                        }
                    }
                }

                List<MOtroPagos> otros = motro.list();
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

                if (fechaHasta != null) {
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

                ///Retrieve only required data
                /*
                cr.setProjection(Projections.projectionList()
                        .add(Projections.property("id"), "id")
                        .add(Projections.property("razonSocial"), "razonSocial")
                        .add(Projections.property("rfc"), "rfc")
                        .add(Projections.property("empresaId"), "empresaId")
                        .add(Projections.property("plantillaId"), "plantillaId")
                        .add(Projections.property("serie"), "serie")
                        .add(Projections.property("folio"), "folio")
                        .add(Projections.property("serieErp"), "serieErp")
                        .add(Projections.property("folioErp"), "folioErp")
                        .add(Projections.property("moneda"), "moneda")
                        .add(Projections.property("fecha"), "fecha")
                        .add(Projections.property("tipoCambio"), "tipoCambio")
                        ///.add(Projections.property("subtotal"), "subtotal")
                        ///.add(Projections.property("subtotalMl"), "subtotalMl")
                        .add(Projections.property("total"), "total")
                        .add(Projections.property("estadoDocumento"), "estadoDocumento")
                        .add(Projections.property("numeroFactura"), "numeroFactura"))
                        .setResultTransformer(Transformers.aliasToBean(MCfdPagos.class));
                        */

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

    public List<MCfdPagos> ListaParametrosExportLazy(int idUser, int idEmpresa,
                                                     String columna, String value,
                                                     Date fechaDesde, Date fechaHasta,
                                                     int estatus, int first, int pageSize) {
        List<MCfdPagos> listacfd = null;
        try {
            hibManagerRO.initTransaction();
            // Verificar si aparte de la empresa se buscaran por otros
            // parametros
            Criteria cr = hibManagerRO.getSession().createCriteria(MCfdPagos.class);
            MAcceso usuario = (MAcceso) hibManagerRO.getSession().get(MAcceso.class, idUser);

            if (idEmpresa <= 0) {
                if (usuario.getEmpresas() != null && !usuario.getEmpresas().isEmpty()) {
                    Integer[] idsemp = new Integer[usuario.getEmpresas().size()];
                    for (int z = 0; z < usuario.getEmpresas().size(); z++) {
                        idsemp[z] = usuario.getEmpresas().get(z).getId();
                    }
                    cr.add(Restrictions.in("empresaId", idsemp));
                }
            } else {
                cr.add(Restrictions.eq("empresaId", idEmpresa));
            }

            if (estatus >= 0) {
                cr.add(Restrictions.eq("estadoDocumento", estatus));
            }


            if (columna != null
                    && !columna.isEmpty()
                    && !columna.trim().equals("-1")
                    && value != null
                    && !"".equals(value.trim())) {
                if (columna.contains("param")) {

                } else {
                    if (columna.equals("folioErp")) {
                        String[] folios = value.trim().split(",");
                        cr.add(Restrictions.in(columna.trim(), folios));
                    }else if (columna.equals("serie")) {
                        String[] series = value.trim().split(",");
                        cr.add(Restrictions.in(columna.trim(), series));
                    } else {
                        cr.add(Restrictions.eq(columna.trim(), value.trim()));
                    }
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

            if (fechaHasta != null) {
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

    public MOtroPagos Otro(int id) {
        MOtroPagos otro = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MOtroPagos.class);
            cr.add(Restrictions.eq("cfdId", id));
            otro = (MOtroPagos) cr.uniqueResult();
            hibManagerRO.getTransaction().commit();
        } catch (org.hibernate.NonUniqueResultException ex) {
            ex.printStackTrace();
            hibManagerRO.getTransaction().rollback();
            System.out.println(">>>>>>>>>> Existen dos registros de MOtroPagos para el FACTURA: " + id);
            return null;
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.out);
        } finally {
            hibManagerRO.closeSession();
        }
        return otro;
    }
}
