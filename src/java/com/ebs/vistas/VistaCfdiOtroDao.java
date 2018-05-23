package com.ebs.vistas;

import fe.db.MAcceso;
import fe.model.util.hibernateutil.HibernateUtilApl;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by eflores on 22/01/2018.
 */
public class VistaCfdiOtroDao {
    public int rowCount = 0;
    private HibernateUtilApl hibManagerRO;

    public VistaCfdiOtroDao() {
        hibManagerRO = new HibernateUtilApl();
    }


    public List<VistaCfdiOtro> ListaParametros(int idUser, Integer[] idsEmpresas,
                                               String numeroFactura,
                                               String folioErp,
                                               String rfc,
                                               String serie,
                                               String noCliente,
                                               String razonSocial, Date fechaDesde, Date fechaHasta, int estatus,
                                               String numPolizaSeguro,
                                               String UUID,
                                               int first, int pageSize) {

        List<VistaCfdiOtro> listacfd = null;
        try {
            hibManagerRO.initTransaction();
            // Verificar si aparte de la empresa se buscaran por otros
            // parametros
            Criteria cr = hibManagerRO.getSession().createCriteria(VistaCfdiOtro.class);
            MAcceso usuario = (MAcceso) hibManagerRO.getSession().get(MAcceso.class, idUser);

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

                String[] numeroFact = numeroFactura.trim().split(",");
                //cr.add(Restrictions.eq("numeroFactura", numeroFactura.trim()));

                cr.add(Restrictions.in("numeroFactura", numeroFact));
                //cr.add(Restrictions.eq("numeroFactura", numeroFactura.trim()));
            }

            if (numPolizaSeguro != null && !numPolizaSeguro.isEmpty()) {
                cr.add(Restrictions.ilike("noPolizaSeguro", numPolizaSeguro.trim(), MatchMode.ANYWHERE));
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

                String[] noClientes = noCliente.trim().split(",");

                if(noClientes.length >= 2){
                    cr.add(Restrictions.in("noCliente",noClientes));
                }else{
                    cr.add(Restrictions.like("noCliente", "%" + noCliente.trim() + "%", MatchMode.ANYWHERE));
                }

            }
            if (razonSocial != null && !razonSocial.isEmpty()) {
                cr.add(Restrictions.like("razonSocial", "%" + razonSocial.trim() + "%", MatchMode.ANYWHERE));
            }
            if (UUID != null && !UUID.isEmpty()) {
                //System.out.println(UUID.trim());
                cr.add(Restrictions.like("uuid", "%" + UUID.trim() + "%", MatchMode.ANYWHERE));
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
            System.out.println("rowCount = " + rowCount);


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
            System.out.println("Resultados en la lista: " + listacfd.size());
            hibManagerRO.closeSession();
        }

        return listacfd;
    }


    public List<VistaCfdiOtro> ListaParametrosClientes(int idUser, Integer[] idsEmpresas, String numeroFactura,
                                                       String folioErp,
                                                       String rfc,
                                                       String serie,
                                                       String noCliente,///Inmutable
                                                       String razonSocial,
                                                       Date fechaDesde, Date fechaHasta,
                                                       String numPolizaSeguro,
                                                       String UUID,
                                                       int first, int pageSize) {
        List<VistaCfdiOtro> listacfd = null;
        try {

            hibManagerRO.initTransaction();
            boolean estan = true;
            Criteria cr = hibManagerRO.getSession().createCriteria(VistaCfdiOtro.class);
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
                String[] numeroFact = numeroFactura.trim().split(",");
                //cr.add(Restrictions.eq("numeroFactura", numeroFactura.trim()));

                cr.add(Restrictions.in("numeroFactura", numeroFact));
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

                String[] noClientes = noCliente.trim().split(",");

                if(noClientes.length >= 2){
                    cr.add(Restrictions.in("noCliente",noClientes));
                }else{
                    cr.add(Restrictions.like("noCliente", "%" + noCliente.trim() + "%", MatchMode.ANYWHERE));
                }

            }
            if (razonSocial != null && !razonSocial.isEmpty()) {
                cr.add(Restrictions.like("razonSocial", razonSocial.trim(), MatchMode.ANYWHERE));
            }
            if (numPolizaSeguro != null && !numPolizaSeguro.isEmpty()) {
                cr.add(Restrictions.ilike("noPolizaSeguro", numPolizaSeguro.trim(), MatchMode.ANYWHERE));
            }

            if (UUID != null && !UUID.isEmpty()) {
                System.out.println(UUID.trim());
                cr.add(Restrictions.like("uuid", "%" + UUID.trim() + "%", MatchMode.ANYWHERE));
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
            System.out.println("Resultados en la lista: " + listacfd.size());
            hibManagerRO.closeSession();
        }

        return listacfd;
    }

}
