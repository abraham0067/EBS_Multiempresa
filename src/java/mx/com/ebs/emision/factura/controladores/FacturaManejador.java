/**
 *
 */
package mx.com.ebs.emision.factura.controladores;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mx.com.ebs.emision.factura.utilierias.PintarLog;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import fe.db.MCfd;
import fe.db.MCfdXml;
import fe.db.MCfdXmlRetencion;
import fe.db.MOtro;
import fe.db.MOtroRetencion;
import fe.db.MPlantilla;
import fe.db.McfdRetencion;
import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilCustom;
import fe.model.util.hibernateutil.HibernateUtilEmi;
import org.hibernate.criterion.Criterion;

/**
 * @author Amiranda Controlador de la tabla Factura
 */
@SuppressWarnings("deprecation")
public class FacturaManejador {

    private HibernateUtilApl hibManagerRO;
    private HibernateUtilEmi hibManagerSU;
    private HibernateUtilCustom hibManagerCustCorp;//Ejecuta actualizaciones
    private HibernateUtilCustom hibManagerCustAseg;//Ejecuta actualizaciones

    public FacturaManejador() {
        hibManagerRO = new HibernateUtilApl();//Read only interface

    }

    /**
     * Metodo encargado de consultar todas las facturas que se encuentran en BD
     * tomando como base los parametros establecidoss return List<MCfd>: La
     * lista de facturas cargadas en un objeto de tipo MCfd
     *
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<MCfd> consultaDinamica(String rfc, String razonSocial, String serieErp, String numeroFactura) throws Exception {
        hibManagerRO.initTransaction();
        Criteria crF = hibManagerRO.getSession().createCriteria(MCfd.class);
        List<MCfd> listaFacturas = new ArrayList();
        try {
            if (!"".equalsIgnoreCase(rfc)) {
                crF.add(Restrictions.sqlRestriction("this_.RFC            like '%" + rfc + "%'"));
            }
            if (!"".equalsIgnoreCase(razonSocial)) {
                crF.add(Restrictions.sqlRestriction("this_.RAZON_SOCIAL   like '%" + razonSocial + "%'"));
            }
            if (!"".equalsIgnoreCase(serieErp)) {
                crF.add(Restrictions.sqlRestriction("this_.SERIE_ERP      like '%" + serieErp + "%'"));
            }
            if (!"".equalsIgnoreCase(numeroFactura)) {
                crF.add(Restrictions.sqlRestriction("this_.NUMERO_FACTURA like '%" + numeroFactura + "%'"));
            }
            crF.setMaxResults(100); // solo las ultimas 100
            listaFacturas = crF.list();

            hibManagerRO.getTransaction().commit();
        } catch (Exception e) {
            hibManagerRO.getTransaction().rollback();
            throw new Exception("Error al consultar las facturas: " + e);
        } finally {
            try {
                hibManagerRO.closeSession();
            } catch (Exception e) {
                PintarLog.println("al cerrar la conexion", e);
            }
        }
        return listaFacturas;
    }

    /**
     * Metodo encargado de consultar todas las facturas que se encuentran en BD
     * ligadas a los parametros especificados y el id de la Empresa return
     * List<MCfd>: La lista de facturas cargadas en un objeto de tipo MCfd
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<MCfd> consultaDinamica(String rfc, String razonSocial, String serieErp, String numeroFactura, String idEmpresa, String noCliente, Date fechaDesde, Date fechaHasta) throws Exception {
        hibManagerRO.initTransaction();
        Criteria crF = hibManagerRO.getSession().createCriteria(MCfd.class);
        List<MCfd> listaFacturas = new ArrayList();
        //Por el momento, asignamos el id de la empresa como 2
        PintarLog.println("idEmpresa: " + idEmpresa);
        try {
            if (!"".equalsIgnoreCase(rfc)) {
                crF.add(Restrictions.ilike("rfc", rfc));
            }
            System.out.println(" rfcffssss " + rfc);
            if (!"".equalsIgnoreCase(razonSocial)) {
                crF.add(Restrictions.ilike("razonSocial", razonSocial));
            }
            if (!"".equalsIgnoreCase(serieErp)) {
                crF.add(Restrictions.ilike("serieErp", serieErp));
            }
            if (!"".equalsIgnoreCase(numeroFactura)) {
                crF.add(Restrictions.ilike("numeroFactura", numeroFactura));
            }
            if (!"".equalsIgnoreCase(noCliente)) {
                crF.add(Restrictions.ilike("noCliente", noCliente));
            }
            /*
	        if(!"".equalsIgnoreCase(rfc))          crF.add(Restrictions.sqlRestriction("this_.RFC  like '%" + rfc           + "%'"));
	        if(!"".equalsIgnoreCase(razonSocial))  crF.add(Restrictions.sqlRestriction("this_.RAZON_SOCIAL   like '%" + razonSocial   + "%'"));
	        if(!"".equalsIgnoreCase(serieErp))     crF.add(Restrictions.sqlRestriction("this_.SERIE_ERP      like '%" + serieErp      + "%'"));
	        if(!"".equalsIgnoreCase(numeroFactura))crF.add(Restrictions.sqlRestriction("this_.NUMERO_FACTURA like '%" + numeroFactura + "%'"));	        
	        if(!"".equalsIgnoreCase(noCliente))    crF.add(Restrictions.sqlRestriction("this_.NUMERO_CLIENTE like '%" + noCliente     + "%'"));
             */
            if (fechaDesde != null && fechaHasta != null) {
                crF.add(Expression.ge("fecha", fechaDesde));
                crF.add(Expression.le("fecha", fechaHasta));
            }
            crF.add(Restrictions.sqlRestriction("this_.EMPRESA_ID     =      " + idEmpresa));

            if ("".equalsIgnoreCase(rfc) && "".equalsIgnoreCase(razonSocial) && "".equalsIgnoreCase(serieErp) && "".equalsIgnoreCase(numeroFactura) && "".equalsIgnoreCase(noCliente) && fechaDesde == null && fechaHasta == null) {
                crF.setMaxResults(100); // solo las ultimas 100
                listaFacturas = crF.addOrder(Order.desc("id")).list();
            } else {
                listaFacturas = crF.addOrder(Order.desc("id")).list();
            }

            hibManagerRO.getTransaction().commit();

        } catch (Exception e) {
            hibManagerRO.getTransaction().rollback();
            throw new Exception("Error al consultar las facturas: " + e);
        } finally {
            try {
                hibManagerRO.closeSession();
            } catch (Exception e) {
                PintarLog.println("al cerrar la conexion", e);
            }
        }
        return listaFacturas;
    }

    public MOtro ObtenerMOtroPorCFDIXML(int idCfd) {
        PintarLog.println("idFactura a consultar: " + idCfd);
        MOtro otro = null;
        try {
            hibManagerRO.initTransaction();
            Criteria crF = hibManagerRO.getSession().createCriteria(MOtro.class);
            crF.add(Restrictions.sqlRestriction("this_.CFD_ID = " + idCfd + ""));
            crF.add(Restrictions.isNotNull("param1"));
            crF.add(Restrictions.eq("param0", "0"));
            otro = (MOtro) crF.uniqueResult();
            hibManagerRO.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace(System.out);
            hibManagerRO.getTransaction().rollback();
        } finally {
            try {
                hibManagerRO.closeSession();
            } catch (Exception e) {
                PintarLog.println("al cerrar la conexion", e);
            }
        }
        return otro;
    }

    public MOtro ObtenerMOtroPorCFDIPDF(int idCfd) {
        PintarLog.println("idFactura a consultar: " + idCfd);
        MOtro otro = null;
        try {
            hibManagerRO.initTransaction();
            Criteria crF = hibManagerRO.getSession().createCriteria(MOtro.class);
            crF.add(Restrictions.sqlRestriction("this_.CFD_ID = " + idCfd + ""));
            crF.add(Restrictions.isNotNull("param2"));
            crF.add(Restrictions.or(Restrictions.eq("param0", "0"), Restrictions.ilike("param2", "%pdf")));
            //crF.add(Restrictions.eq("param0", "0"));
            otro = (MOtro) crF.uniqueResult();
            hibManagerRO.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace(System.out);
            hibManagerRO.getTransaction().rollback();
        } finally {
            try {
                hibManagerRO.closeSession();
            } catch (Exception e) {
                PintarLog.println("al cerrar la conexion", e);
            }
        }
        return otro;
    }

    /**
     * Metodo encargado de consultar los archivos de uan factura return MCfdXml:
     * La lista de facturas cargadas en un objeto de tipo
     *
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<MCfdXml> consultaStreamFactura(int idFactura) throws Exception {
        PintarLog.println("idFactura a consultar: " + idFactura);
        List<MCfdXml> listaFacturas = new ArrayList<MCfdXml>();
        try {
            hibManagerRO.initTransaction();
            Criteria crF = hibManagerRO.getSession().createCriteria(MCfdXml.class);
            crF.add(Restrictions.sqlRestriction("this_.CFD_ID = " + idFactura + ""));
            listaFacturas = crF.list();
            hibManagerRO.getTransaction().commit();
        } catch (Exception e) {
            hibManagerRO.getTransaction().rollback();
            throw new Exception("Error al obtener la lista de los folios: " + e);
        } finally {
            try {
                hibManagerRO.closeSession();
            } catch (Exception e) {
                PintarLog.println("al cerrar la conexion", e);
            }
        }
        return listaFacturas;
    }

    /*
     * Metodo encargado de obtener la lista de xml para exportar 
     */
    //public List<MCfdXml> DescargaXML(String tipoBusqueda, String valor, Date FechaInicio, Date FechaFin) throws Exception {
    public List<MCfdXml> DescargaXML(String tipoBusqueda, String valor) throws Exception {
        System.out.println("DescargaXML");
        List<MCfdXml> listaFacturas = new ArrayList<MCfdXml>();
        try {
            hibManagerRO.initTransaction();
            Criteria crF = hibManagerRO.getSession().createCriteria(MCfdXml.class).setFetchMode("cfd", FetchMode.JOIN);
            listaFacturas = crF.list();
            hibManagerRO.getTransaction().commit();
        } catch (Exception e) {
            hibManagerRO.getTransaction().rollback();
            throw new Exception("Error al obtener la lista de XML: " + e);
        } finally {
            try {
                hibManagerRO.closeSession();
            } catch (Exception e) {
                PintarLog.println("al cerrar la conexion", e);
            }
        }
        return listaFacturas;
    }

    /**
     * Metodo encargado de consultar los archivos de una factura return MCfdXml:
     * La lista de facturas cargadas en un objeto de tipo
     *
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public MCfd consultaDatosFactura(int idFactura) throws Exception {
        MCfd mcfd = new MCfd();
        PintarLog.println("idFactura a consultar: " + idFactura);
        try {
            hibManagerRO.initTransaction();
            Criteria crF = hibManagerRO.getSession().createCriteria(MCfd.class);
            crF.add(Restrictions.sqlRestriction("this_.ID = " + idFactura + ""));
            List<MCfd> listaFacturas = crF.list();
            for (int i = 0; i < listaFacturas.size(); i++) {
                mcfd = listaFacturas.get(i);
            }
            hibManagerRO.getTransaction().commit();
        } catch (Exception e) {
            hibManagerRO.getTransaction().rollback();
            throw new Exception("Error al obtener la lista de los folios: " + e);
        } finally {
            try {
                hibManagerRO.closeSession();
            } catch (Exception e) {
                PintarLog.println("al cerrar la conexion", e);
            }
        }
        return mcfd;
    }

    private MCfdXml ObtenUUID(int id_cfd) {
        MCfdXml cfd = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MCfdXml.class);
            cr.add(Restrictions.eq("cfd.id", id_cfd));
            cfd = (MCfdXml) cr.uniqueResult();
            hibManagerRO.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            hibManagerRO.closeSession();
        }
        return cfd;
    }

    public MOtroRetencion ObtenerMOtroPorCFDIPDFRetencion(final int idCfd) {
        PintarLog.println("idFactura a consultar: " + idCfd);
        MOtroRetencion otro = null;
        try {
            hibManagerRO.initTransaction();
            final Criteria crF = hibManagerRO.getSession().createCriteria((Class) MOtroRetencion.class);
            crF.add((Criterion) Restrictions.eq("mcfdRetencion.id", (Object) idCfd));
            crF.add(Restrictions.isNotNull("param2"));
            crF.add((Criterion) Restrictions.or((Criterion) Restrictions.eq("param0", (Object) "0"), Restrictions.ilike("param2", (Object) "%pdf")));
            crF.add((Criterion) Restrictions.eq("param0", (Object) "0"));
            otro = (MOtroRetencion) crF.uniqueResult();
            hibManagerRO.getTransaction().commit();
        } catch (Exception e) {
            hibManagerRO.getTransaction().rollback();
            try {
                hibManagerRO.closeSession();
            } catch (Exception e1) {
                PintarLog.println("al cerrar la conexion", e1);
            }
        } finally {
            try {
                hibManagerRO.closeSession();
            } catch (Exception e2) {
                PintarLog.println("al cerrar la conexion", e2);
            }
        }
        return otro;
    }

    public MOtroRetencion ObtenerMOtroPorCFDIXMLRetencion(final int idCfd) {
        PintarLog.println("idFactura a consultar: " + idCfd);
        MOtroRetencion otro = null;
        try {
            hibManagerRO.initTransaction();
            final Criteria crF = hibManagerRO.getSession().createCriteria((Class) MOtroRetencion.class);
            crF.add((Criterion) Restrictions.eq("mcfdRetencion.id", (Object) idCfd));
            crF.add(Restrictions.isNotNull("param1"));
            crF.add((Criterion) Restrictions.eq("param0", (Object) "0"));
            otro = (MOtroRetencion) crF.uniqueResult();
            hibManagerRO.getTransaction().commit();
        } catch (Exception e) {
            hibManagerRO.getTransaction().rollback();
            try {
                hibManagerRO.closeSession();
            } catch (Exception e1) {
                PintarLog.println("al cerrar la conexion", e1);
            }
        } finally {
            try {
                hibManagerRO.closeSession();
            } catch (Exception e2) {
                PintarLog.println("al cerrar la conexion", e2);
            }
        }
        return otro;
    }

    public List<MCfdXmlRetencion> consultaStreamFacturaRetencion(final int idFactura) throws Exception {
        PintarLog.println("idFactura a consultar: " + idFactura);
        List<MCfdXmlRetencion> listaFacturas = new ArrayList<MCfdXmlRetencion>();
        try {
            hibManagerRO.initTransaction();
            final Criteria crF = hibManagerRO.getSession().createCriteria((Class) MCfdXmlRetencion.class);
            crF.add((Criterion) Restrictions.eq("mcfdRetencion.id", (Object) idFactura));
            listaFacturas = (List<MCfdXmlRetencion>) crF.list();
            hibManagerRO.getTransaction().commit();
        } catch (Exception e) {
            hibManagerRO.getTransaction().rollback();
            throw new Exception("Error al obtener la lista de los folios: " + e);
        } finally {
            try {
                hibManagerRO.closeSession();
            } catch (Exception e2) {
                PintarLog.println("al cerrar la conexion", e2);
            }
        }
        return listaFacturas;
    }

    public McfdRetencion consultaDatosFacturaRetencion(final int idFactura) throws Exception {
        McfdRetencion mcfd = new McfdRetencion();
        PintarLog.println("idFactura a consultar: " + idFactura);
        try {
            hibManagerRO.initTransaction();
            final Criteria crF = hibManagerRO.getSession().createCriteria((Class) McfdRetencion.class);
            crF.add((Criterion) Restrictions.eq("id", (Object) idFactura));
            final List<McfdRetencion> listaFacturas = (List<McfdRetencion>) crF.list();
            for (int i = 0; i < listaFacturas.size(); ++i) {
                mcfd = listaFacturas.get(i);
            }
            MPlantilla p = new MPlantilla();
            if (mcfd.getMPlantilla() == null) {
                try {
                    System.out.println("No se encontro plantilla asignada para esta empresa, enlazando plantilla generica...");
                    final Criteria crP = hibManagerRO.getSession().createCriteria((Class) MPlantilla.class);
                    crP.add((Criterion) Restrictions.eq("nombre", (Object) "Retencion"));
                    crP.add((Criterion) Restrictions.eq("MServicio.id", (Object) 3));
                    crP.add((Criterion) Restrictions.eq("MEmpresa.id", (Object) 2));
                    p = (MPlantilla) crP.uniqueResult();
                    if (p != null) {
                        mcfd.setMPlantilla(p);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            hibManagerRO.getTransaction().commit();
        } catch (Exception e2) {
            hibManagerRO.getTransaction().rollback();
            throw new Exception("Error al obtener la lista de los folios: " + e2);
        } finally {
            try {
                hibManagerRO.closeSession();
            } catch (Exception e3) {
                PintarLog.println("al cerrar la conexion", e3);
            }
        }
        return mcfd;
    }

}
