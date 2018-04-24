package fe.model.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.ebs.catalogos.TiposDocumento;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import fe.db.MAcceso;
import fe.db.MArchivosCfd;
import fe.db.MCfd;
import fe.db.MCfdXml;
import fe.db.MOtro;
import fe.db.MapearCfdArchi;
import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;

import java.io.Serializable;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;


public class CfdiDAO implements Serializable {
    private HibernateUtilApl hibManagerRO;
    private HibernateUtilEmi hibManagerSU;
    public int rowCount = 0;

    public CfdiDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
    }

    public MCfd BuscarId(int id) {
        MCfd cfd = null;
        try {
            hibManagerRO.initTransaction();
            cfd = (MCfd) hibManagerRO.getSession().get(MCfd.class, id);
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            ex.printStackTrace();
            hibManagerRO.getTransaction().rollback();
        } finally {
            hibManagerRO.closeSession();
        }
        return cfd;
    }

    public List<MCfd> ListaParametros(int idUser, Integer[] idsEmpresas,
                                      String numeroFactura,
                                      String folioErp,
                                      String rfc,
                                      String serie,
                                      String noCliente,
                                      String razonSocial, Date fechaDesde, Date fechaHasta, int estatus,
                                      String numPolizaSeguro,
                                      int first, int pageSize) {


        List<MCfd> listacfd = null;
        try {
            hibManagerRO.initTransaction();
            // Verificar si aparte de la empresa se buscaran por otros
            // parametros
            Criteria cr = hibManagerRO.getSession().createCriteria(MCfd.class);
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
                    cr.add(Restrictions.in("idEmpresa", idsemp));
                }
            } else {
                cr.add(Restrictions.in("idEmpresa", idsEmpresas));
            }

            if (estatus >= 0) {
                cr.add(Restrictions.eq("edoDocumento", estatus));
            }

            if (numeroFactura != null && !numeroFactura.isEmpty()) {
                cr.add(Restrictions.eq("numeroFactura", numeroFactura.trim()));
            }
            /*
            if (numPolizaSeguro != null && !numPolizaSeguro.isEmpty()) {
                cr.add(Restrictions.eq("otro.noPolizaSeguro", numPolizaSeguro.trim()));
            }*/
            if (folioErp != null && !folioErp.isEmpty()) {
                String[] folios = folioErp.trim().split(",");
                cr.add(Restrictions.in("folioErp", folios));
            }
            if (rfc != null && !rfc.isEmpty()) {
                cr.add(Restrictions.eq("rfc", rfc.trim()));
            }
            if (serie != null && !serie.isEmpty()) {
                String[] series = serie.trim().split(",");
                cr.add(Restrictions.in("serie", series));
            }
            if (noCliente != null && !noCliente.isEmpty()) {
                cr.add(Restrictions.eq("noCliente", noCliente.trim()));
            }
            if (razonSocial != null && !razonSocial.isEmpty()) {
                cr.add(Restrictions.like("razonSocial", "%" + razonSocial.trim() + "%", MatchMode.ANYWHERE));
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

    public List<MCfd> ListaParametrosClientes(int idUser, Integer[] idsEmpresas, String numeroFactura,
                                              String folioErp,
                                              String rfc,
                                              String serie,
                                              String noCliente,
                                              String razonSocial,
                                              Date fechaDesde, Date fechaHasta, String cliente,
                                              String numPolizaSeguro,
                                              int first, int pageSize) {
        List<MCfd> listacfd = null;
        try {
            if (cliente != null && !cliente.trim().equals("")) {
                hibManagerRO.initTransaction();
                boolean estan = true;
                Criteria cr = hibManagerRO.getSession().createCriteria(MCfd.class);
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
                        cr.add(Restrictions.in("idEmpresa", idsemp));
                    }
                } else {
                    cr.add(Restrictions.in("idEmpresa", idsEmpresas));
                }

                if (numeroFactura != null && !numeroFactura.isEmpty()) {
                    cr.add(Restrictions.eq("numeroFactura", numeroFactura.trim()));
                }
                if (folioErp != null && !folioErp.isEmpty()) {
                    String[] folios = folioErp.trim().split(",");
                    cr.add(Restrictions.in("folioErp", folios));
                }
                if (rfc != null && !rfc.isEmpty()) {
                    cr.add(Restrictions.eq("rfc", rfc.trim()));
                }
                if (serie != null && !serie.isEmpty()) {
                    String[] series = serie.trim().split(",");
                    cr.add(Restrictions.in("serie", series));
                }
                if (noCliente != null && !noCliente.isEmpty()) {
                    cr.add(Restrictions.eq("noCliente", noCliente.trim()));
                }
                if (razonSocial != null && !razonSocial.isEmpty()) {
                    cr.add(Restrictions.like("razonSocial", razonSocial.trim(), MatchMode.ANYWHERE));
                }
/*
                if (numPolizaSeguro != null && !numPolizaSeguro.isEmpty()) {
                    cr.add(Restrictions.eq("otro.noPolizaSeguro", numPolizaSeguro.trim()));
                }
                */

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

    public List<MCfd> ListaParametrosExportLazy(int idUser, Integer[] idsEmpresas,
                                                String numeroFactura,
                                                String folioErp,
                                                String rfc,
                                                String serie,
                                                String noCliente,
                                                String razonSocial,
                                                Date fechaDesde, Date fechaHasta,
                                                int estatus,
                                                String numPolizaSeguro,
                                                int first, int pageSize) {
        List<MCfd> listacfd = null;
        try {
            hibManagerRO.initTransaction();
            // Verificar si aparte de la empresa se buscaran por otros
            // parametros
            boolean estan = true;
            Criteria cr = hibManagerRO.getSession().createCriteria(MCfd.class);
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
                    cr.add(Restrictions.in("idEmpresa", idsemp));
                }
            } else {
                cr.add(Restrictions.in("idEmpresa", idsEmpresas));
            }


            if (estatus >= 0) {
                cr.add(Restrictions.eq("edoDocumento", estatus));
            }


            if (numeroFactura != null && !numeroFactura.isEmpty()) {
                cr.add(Restrictions.eq("numeroFactura", numeroFactura.trim()));
            }
/*
            if (numPolizaSeguro != null && !numPolizaSeguro.isEmpty()) {
                cr.add(Restrictions.eq("otro.noPolizaSeguro", numPolizaSeguro.trim()));
            }
            */

            if (folioErp != null && !folioErp.isEmpty()) {
                String[] folios = folioErp.trim().split(",");
                cr.add(Restrictions.in("folioErp", folios));
            }
            if (rfc != null && !rfc.isEmpty()) {
                cr.add(Restrictions.eq("rfc", rfc.trim()));
            }
            if (serie != null && !serie.isEmpty()) {
                String[] series = serie.trim().split(",");
                cr.add(Restrictions.in("serie", series));
            }
            if (noCliente != null && !noCliente.isEmpty()) {
                cr.add(Restrictions.eq("noCliente", noCliente.trim()));
            }
            if (razonSocial != null && !razonSocial.isEmpty()) {
                cr.add(Restrictions.like("razonSocial", razonSocial.trim(), MatchMode.ANYWHERE));
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

    public MOtro Otro(int id) {
        MOtro otro = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MOtro.class);
            cr.add(Restrictions.eq("cfd.id", id));
            otro = (MOtro) cr.uniqueResult();
            hibManagerRO.getTransaction().commit();
        } catch (org.hibernate.NonUniqueResultException ex) {
            ex.printStackTrace();
            hibManagerRO.getTransaction().rollback();
            System.out.println(">>>>>>>>>> Existen dos registros de MOtro para el FACTURA: " + id);
            return null;
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.out);
        } finally {
            hibManagerRO.closeSession();
        }
        return otro;
    }

    public List<MapearCfdArchi> ListaArchivos(List<MCfd> cfds) {
        List<MapearCfdArchi> mcfdsContainers = new ArrayList<MapearCfdArchi>();

        for (MCfd cfd : cfds) {
            MapearCfdArchi map = new MapearCfdArchi();
            map.setCfd(cfd);
            mcfdsContainers.add(map);
        }

        for (MapearCfdArchi mcfdsContainer : mcfdsContainers) {
            try {
                hibManagerRO.initTransaction();
                for (int i = 0; i < cfds.size(); i++) {
                    List<MArchivosCfd> existe = null;
                    Criteria cr = hibManagerRO.getSession().createCriteria(MArchivosCfd.class);
                    cr.add(Restrictions.eq("cfd.id", mcfdsContainer.getCfd().getId()));
                    existe = cr.list();
                    if (existe != null && !existe.isEmpty()) {
                        mcfdsContainer.setExiste(1);
                    }
                }
                hibManagerRO.getTransaction().commit();
            } catch (HibernateException ex) {
                hibManagerRO.getTransaction().rollback();
                ex.printStackTrace(System.out);
            } finally {
                hibManagerRO.closeSession();
            }

            try {
                hibManagerRO.initTransaction();
                for (int i = 0; i < cfds.size(); i++) {
                    MOtro otro = null;
                    Criteria cr = hibManagerRO.getSession().createCriteria(MOtro.class);
                    cr.add(Restrictions.eq("cfd.id", mcfdsContainer.getCfd().getId()));
                    otro = (MOtro) cr.setMaxResults(1).uniqueResult();
                    if (otro != null) {
                        mcfdsContainer.setNoPolizaSeguro(otro.getNoPolizaSeguro());
                    }
                }
                hibManagerRO.getTransaction().commit();
            } catch (HibernateException ex) {
                hibManagerRO.getTransaction().rollback();
                ex.printStackTrace(System.out);
            } finally {
                hibManagerRO.closeSession();
            }
        }

        return mcfdsContainers;

    }

    public MCfd findByUUID(final String uuid, final Integer empresa) {
        MCfd cfd = null;
        try {
            if (hibManagerRO.initTransaction()) {
                final Criteria cr = this.hibManagerRO.getSession().createCriteria(MCfd.class);
                cr.add(Restrictions.eq("uuid", uuid));
                if (empresa.intValue() > 0) {
                    System.out.println("id_empresa: " + empresa.intValue());
                    cr.add(Restrictions.eq("idEmpresa", empresa));
                }
                cr.add(Restrictions.eq("edoDocumento", 1));
                cfd = (MCfd) cr.uniqueResult();
                this.hibManagerRO.getTransaction().commit();
            }
        } catch (HibernateException ex) {
            this.hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            try {
                hibManagerRO.getSession().close();
            } catch (HibernateException ex2) {
            }
            try {
                hibManagerRO.closeSession();
            } catch (HibernateException ex3) {
            }
        }
        return cfd;
    }
}
