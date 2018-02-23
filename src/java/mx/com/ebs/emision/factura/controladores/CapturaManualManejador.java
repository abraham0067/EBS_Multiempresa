package mx.com.ebs.emision.factura.controladores;

import java.sql.CallableStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mx.com.ebs.emision.factura.utilierias.PintarLog;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import fe.db.MCfd;
import fe.db.MConceptosFacturacion;
import fe.db.MDireccion;
import fe.db.MEmpresa;
import fe.db.MFolios;
import fe.db.MReceptor;
import fe.model.util.hibernateutil.HibernateUtilApl;//Readonly
import fe.model.util.hibernateutil.HibernateUtilEmi;
import fe.model.util.hibernateutil.HibernateUtilCustom;
import java.sql.Connection;
import java.sql.SQLException;
import org.hibernate.jdbc.Work;


/**
 * @author Tyranitar
 *
 */
@SuppressWarnings("unchecked")
public class CapturaManualManejador {
    private HibernateUtilApl hibManagerRO;
    private HibernateUtilEmi hibManagerSU;
    private HibernateUtilCustom hibManagerCust;
    public CapturaManualManejador() {
            hibManagerRO = new HibernateUtilApl();//Read only interface
            hibManagerSU = new HibernateUtilEmi();
    }

   
    /**
     * Metodo encargado de obtener un folio de la tabla m_folios
     *
     * @return String : el numero de folio
	     *
     */
    public List<MFolios> obtenNumeroDeFolio() throws Exception {
        List<MFolios> listaFolios = new ArrayList<MFolios>();
        try {
            hibManagerRO.initTransaction();
            Criteria crF = hibManagerRO.getSession().createCriteria(MFolios.class);
            listaFolios = crF.list();
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
        return listaFolios;
    }

    /**
     * Metodo encargado de obtener un folio de la tabla m_folios
     *
     * @return List<MEmpresa>: la lista delas empresas
	     *
     */
    public List<MEmpresa> consultaEmpresas() {

        List<MEmpresa> listaEmpresas = new ArrayList<MEmpresa>();
        try {
            hibManagerRO.initTransaction();
            Criteria crF = hibManagerRO.getSession().createCriteria(MEmpresa.class).addOrder(Order.asc("razonSocial"));;
            listaEmpresas = crF.list();
            hibManagerRO.getTransaction().commit();

        } catch (Exception e) {
            hibManagerRO.getTransaction().rollback();
            PintarLog.println("Error al consultar la lista de empresas: " + e);
        } finally {
            try {
                hibManagerRO.closeSession();
            } catch (Exception e) {
                PintarLog.println("al cerrar la conexion", e);
            }
        }
        return listaEmpresas;
    }

    /**
     * Metodo encargado de obtener un folio de la tabla m_folios
     *
     * @return List<MEmpresa>: la lista delas empresas
     * @throws Exception 
	     *
     */
    public List<MEmpresa> consultaEmpresas(String idEmpresa) throws Exception {
        hibManagerRO.initTransaction();
        List<MEmpresa> listaEmpresas = new ArrayList<MEmpresa>();
        try {
            Criteria crF = hibManagerRO.getSession().createCriteria(MEmpresa.class);
            crF.add(Restrictions.sqlRestriction("this_.ID = " + idEmpresa + ""));
            listaEmpresas = crF.list();
            hibManagerRO.getTransaction().commit();
        } catch (Exception e) {
            hibManagerRO.getTransaction().rollback();
            PintarLog.println("Error al consultar la empresa correspondiente al id:" + idEmpresa + " " + e);
            throw new Exception("Error al consultar la empresa correspondiente al id:" + idEmpresa + " " + e);
        } finally {
            try {
                hibManagerRO.closeSession();
            } catch (Exception e) {
                PintarLog.println("al cerrar la conexion", e);
            }
        }
        if (listaEmpresas == null) {
            listaEmpresas = new ArrayList<MEmpresa>();
        }
        return listaEmpresas;
    }

    /**
     * Metodo encargado de obtener la lista de folios disponible
     *
     * @return List<MEmpresa>: la lista delas empresas
	     *
     */
    public ArrayList<MFolios> consultaSeries() {
        ArrayList<MFolios> series = new ArrayList<MFolios>();

        try {
            hibManagerRO.initTransaction();
            Criteria fol = hibManagerRO.getSession().createCriteria(MCfd.class).addOrder(Order.asc("serie"));
            series = (ArrayList<MFolios>) fol.list();

        } catch (Exception e) {
            hibManagerRO.getTransaction().rollback();
            PintarLog.println("Error al consultar la lista de series: " + e);
            e.printStackTrace();
        } finally {
            try {
                hibManagerRO.getTransaction().commit();
                hibManagerRO.closeSession();
            } catch (Exception e) {
                PintarLog.println("al cerrar la conexion", e);
            }
        }
        return series;
    }

    /**
     * Metodo encargado de obtener la lista de folios disponible
     *
     * @param idEmpresa
     * @return List<MEmpresa>: la lista delas empresas
	     *
     */
    /*public ArrayList<MFolios> consultaSeries(String rfcEmpresa)  {
	        ArrayList<MFolios> series = new ArrayList<MFolios>();
	        
	        try{
	        	initTran();
	        	 Criteria criteria = session.createCriteria(MFolios.class);   
	        	 criteria.add(Restrictions.eq("rfcOrigen", rfcEmpresa));
	        	 criteria.add(Restrictions.or(Restrictions.eq("mailAviso2", "MANUAL"), Restrictions.eq("mailAviso2", "AMBOS") ));
	        	 criteria.addOrder(Order.asc("serie"));	        	 
			    series = (ArrayList<MFolios>) criteria.list();
		        transaction.commit();        
		        
	        }catch(Exception e){
	        	transaction.rollback();
	        	PintarLog.println("Error al consultar la lista de series: "+e);
	        	e.printStackTrace();
	        }finally{
	        	try{
		        	 HibernateUtil.close(session);
		        	}catch(Exception e){
		        		PintarLog.println("al cerrar la conexion", e);
		        	}
		        }
	        return series;
	    }*/
    public ArrayList<MFolios> consultaSeries(int idempresa) {
        ArrayList<MFolios> series = new ArrayList<MFolios>();

        try {
            hibManagerRO.initTransaction();
            Criteria criteria = hibManagerRO.getSession().createCriteria(MFolios.class);
            criteria.add(Restrictions.eq("empresa.id", idempresa));
            criteria.addOrder(Order.asc("serie"));
            series = (ArrayList<MFolios>) criteria.list();
            hibManagerRO.getTransaction().commit();

        } catch (Exception e) {
            hibManagerRO.getTransaction().rollback();
            PintarLog.println("Error al consultar la lista de series: " + e);
            e.printStackTrace();
        } finally {
            try {
                hibManagerRO.closeSession();
            } catch (Exception e) {
                PintarLog.println("al cerrar la conexion", e);
            }
        }
        return series;
    }

    /**
     * Metodo encargado de obtener la lista de folios disponible
     *
     * @param RFC
     * @return List<MFolios>: la lista delas empresas
	     *
     */
    public List<MFolios> ListFolios(String RFC) {
        List<MFolios> ListFol = null;
        hibManagerRO.initTransaction();
        Criteria fol = hibManagerRO.getSession().createCriteria(MFolios.class);
        fol.add(Restrictions.eq("rfcOrigen", RFC));
        fol.addOrder(Order.asc("serie"));
        ListFol = fol.list();
        hibManagerRO.getTransaction().commit();
        hibManagerRO.closeSession();
        return ListFol;
    }

    /**
     * Metodo encargado de obtener la lista de folios disponible
     *
     * @return List<MReceptor>: la lista delas empresas
     *
     */
    public MReceptor consultaDireccionReceptor(String rfcReceptor) {
        ArrayList<MReceptor> dirReceptor = new ArrayList<MReceptor>();
        MReceptor mReceptor = new MReceptor();

        try {
            hibManagerRO.initTransaction();
            Criteria criteria = hibManagerRO.getSession().createCriteria(MReceptor.class);
            criteria.add(Restrictions.eq("rfcOrigen", rfcReceptor));
            dirReceptor = (ArrayList<MReceptor>) criteria.list();
            if (dirReceptor != null && dirReceptor.size() > 0) {
                mReceptor = dirReceptor.get(0);
            }
            hibManagerRO.getTransaction().commit();

        } catch (Exception e) {
            hibManagerRO.getTransaction().rollback();
            PintarLog.println("Error al consultar la direccion del receptor [" + rfcReceptor + "]: " + e);
        } finally {
            try {
                hibManagerRO.closeSession();
            } catch (Exception e) {

                PintarLog.println("al cerrar la conexion", e);
            }
        }

        return mReceptor;
    }

    public Object[] consultaDireccionReceptorUnionBDS(String rfcReceptor, String tipo) {

        ArrayList<MReceptor> dirReceptor = new ArrayList<MReceptor>();
        MReceptor mReceptor = new MReceptor();
        String Razon_social = null, id_domicilio = null, calle = null,
                colonia = null, codigo_postal = null, cve_estado, cve_pais,
                pais = null, estado = null, num_exterior = null, num_interior = null, municipio = null, id_cliente = null;
        try {
            hibManagerRO.initTransaction();
            Criteria criteria = hibManagerRO.getSession().createCriteria(MReceptor.class);
            criteria.add(Restrictions.eq("rfcOrigen", rfcReceptor));
            dirReceptor = (ArrayList<MReceptor>) criteria.list();
            if (dirReceptor != null && dirReceptor.size() > 0) {
                mReceptor = dirReceptor.get(0);
            }
            if ((mReceptor == null || (mReceptor != null && (mReceptor.getRfcOrigen() == null || "".equals(mReceptor.getRfcOrigen().trim()))))) {
                try {
                    hibManagerCust = new HibernateUtilCustom("BIN931011519_hibernate.cfg.xml");
                    hibManagerCust.initTransaction();
                    String sqlRFCF = "SELECT   ( nombre_persona|| ' '||apellido_paterno|| ' '||apellido_materno ) as RAZON_SOCIAL,"
                            + " (trim(rfc.siglas_rfc) || ''|| trim(rfc.f_rfc) || ''|| trim(rfc.homoclave_rfc)) AS RFC, persona.id_domicilio, persona.id_persona as id_cliente "
                            + " FROM persona_fisica, persona, rfc  "
                            + " WHERE persona.sit_persona = 'AC'"
                            + " AND (persona.id_persona(+) = persona_fisica.id_persona)"
                            + " AND (persona_fisica.id_persona = rfc.id_persona(+))"
                            + " AND (trim(rfc.siglas_rfc) || ''|| trim(rfc.f_rfc) || ''|| trim(rfc.homoclave_rfc)) LIKE :rfcReceptor  AND ROWNUM = 1";

                    String sqlRFCM = "SELECT nom_razon_social, (trim(rfc.siglas_rfc) || ''|| trim(rfc.f_rfc) || ''|| trim(rfc.homoclave_rfc)) AS RFC,"
                            + " persona.id_domicilio, persona.id_persona as id_cliente  "
                            + " FROM persona_moral, persona, rfc "
                            + " WHERE  persona.sit_persona = 'AC'"
                            + " AND (persona.id_persona = persona_moral.id_persona)"
                            + " AND (persona_moral.id_persona = rfc.id_persona(+))"
                            + " AND (trim(rfc.siglas_rfc) || ''|| trim(rfc.f_rfc) || ''|| trim(rfc.homoclave_rfc)) LIKE :rfcReceptor AND  ROWNUM = 1";
                    String sqlDireccion = " select d.calle,"
                            + "d.colonia,d.codigo_postal,d.cve_pais,d.cve_estado, d.cve_ciudad,d.num_exterior,"
                            + "d.num_interior,p.desc_poblacion from domicilio d, poblacion p where "
                            + " p.cve_poblacion=d.cve_pobla and d.id_domicilio=";
                    String sqlPoblacion = "select desc_poblacion from poblacion where cve_poblacion= ?";
                    Object[] objeto = null;
                    if (tipo.trim().equals("F")) {
                        objeto = (Object[]) hibManagerCust.getSession().createSQLQuery(sqlRFCF)
                                .setParameter("rfcReceptor", rfcReceptor).uniqueResult();
                    } else if (tipo.trim().equals("M")) {
                        objeto = (Object[]) hibManagerCust.getSession().createSQLQuery(sqlRFCM)
                                .setParameter("rfcReceptor", rfcReceptor).uniqueResult();
                    }
                    if (objeto != null && objeto.length >= 3) {

                        Razon_social = objeto[0].toString();
                        id_domicilio = objeto[2].toString();
                        id_cliente = objeto[3].toString();

                        if (id_domicilio != null && !"".equals(id_domicilio.trim())) {
                            sqlDireccion = sqlDireccion + id_domicilio;//No es necesario parametrizar este query

                            Object[] objeto2 = (Object[]) hibManagerCust.getSession().createSQLQuery(sqlDireccion).uniqueResult();

                            if (objeto2 != null && objeto2.length >= 8) {
                                calle = (String) objeto2[0];
                                colonia = (String) objeto2[1];
                                codigo_postal = (String) objeto2[2];
                                cve_pais = (String) objeto2[3];
                                cve_estado = (String) objeto2[4];
                                num_exterior = (String) objeto2[6];
                                num_interior = (String) objeto2[7];
                                municipio = (String) objeto2[8];

                                if (cve_estado != null && !"".equals(cve_estado.trim())) {

                                    Object objeto3 = hibManagerCust.getSession().createSQLQuery(sqlPoblacion).setString(0, cve_estado).uniqueResult();
                                    if (objeto3 != null) {
                                        estado = (String) objeto3;
                                    }
                                }
                                if (cve_pais != null && !"".equals(cve_pais.trim())) {

                                    Object objeto3 = hibManagerCust.getSession().createSQLQuery(sqlPoblacion).setString(0, cve_pais).uniqueResult();
                                    if (objeto3 != null) {
                                        pais = (String) objeto3;
                                    }

                                }

                            }
                        }

                    }
                    hibManagerCust.getTransaction().commit();
                    hibManagerCust.closeSession();
                    if (Razon_social != null && !"".equals(Razon_social.trim())) {
                        MDireccion direc = new MDireccion(calle, num_exterior, num_interior, "", colonia, municipio, municipio, estado, codigo_postal, pais);
                        hibManagerSU.initTransaction();
                        hibManagerSU.getSession().saveOrUpdate(direc);
                        mReceptor = new MReceptor(rfcReceptor, Razon_social, direc);
                        hibManagerSU.getSession().saveOrUpdate(mReceptor);
                        hibManagerSU.getTransaction().commit();
                        hibManagerSU.closeSession();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            hibManagerCust.getTransaction().commit();
        } catch (Exception e) {
            hibManagerCust.getTransaction().rollback();
            PintarLog.println("Error al consultar la direccion del receptor [" + rfcReceptor + "]: " + e);
        } finally {
            try {
                hibManagerCust.closeSession();
            } catch (Exception e) {

                PintarLog.println("al cerrar la conexion", e);
            }
        }

        Object[] objetos = {mReceptor, id_cliente};

        return objetos;
    }

    /**
     * Metodo encargado guardar los datos del Receptor
     *
     * @param MReceptor : los datos del receptor
	     *
     */
    public void almacenaDatosReceptor(MReceptor receptor) {
        try {
            hibManagerSU.initTransaction();
            hibManagerSU.getSession().saveOrUpdate(receptor.getDireccion());
            hibManagerSU.getSession().saveOrUpdate(receptor);
            hibManagerSU.getTransaction().commit();
        } catch (Exception e) {
            hibManagerSU.getTransaction().rollback();
            PintarLog.println("Error al almacenar los datos del receptor: " + e);
        } finally {
            try {
                hibManagerSU.closeSession();
            } catch (Exception e) {
                PintarLog.println("al cerrar la conexion", e);
            }
        }
    }

    /**
     * Metodo encargado de actualizar los datos del Receptor
     *
     * @param MReceptor : los datos del receptor
	     *
     */
    public void actualizaDatosReceptor(MReceptor receptor) {
        try {
            hibManagerSU.initTransaction();
            hibManagerSU.getSession().update(receptor.getDireccion());
            hibManagerSU.getSession().update(receptor);
            hibManagerSU.getTransaction().commit();
        } catch (Exception e) {
            hibManagerSU.getTransaction().rollback();
            PintarLog.println("Error al almacenar los datos del receptor: " + e);
        } finally {
            try {
                hibManagerSU.closeSession();
            } catch (Exception e) {
                PintarLog.println("al cerrar la conexion", e);
            }
        }
    }

    public String[] ValiadaOrdenCentro(String OrdenInterna, String centroBenefico) {
        String resp = "No encontrado", clave = "";
        try {
            hibManagerCust = new HibernateUtilCustom("BIN931011519_hibernate.cfg.xml");
            hibManagerCust.initTransaction();
            String SQL = "";
            if (OrdenInterna != null && !"".equals(OrdenInterna.trim())) {
                SQL = "SELECT orden, texto, f_alta_reg_sap, oi  FROM (SELECT   orden, texto, f_alta_reg_sap, INSTR (orden, 'OIR') oi "
                        + " FROM iz_orden_interna  WHERE 2 = 2 AND orden LIKE :ordenInterna ORDER BY 4 DESC) WHERE ROWNUM = 1";

            } else if (centroBenefico != null && !"".equals(centroBenefico.trim())) {

                SQL = " SELECT centro_beneficio,sociedad_CO  FROM ("
                        + " SELECT centro_beneficio,sociedad_CO, f_alta_reg_sap FROM IZ_CENTRO_BENEF "
                        + " WHERE 2 = 2 AND CENTRO_BENEFICIO LIKE :centroBenefico ORDER BY 3 DESC  ) WHERE ROWNUM = 1";

            }
            if (SQL != null && !"".equals(SQL.trim())) {
                Object[] objeto = null;
                if(OrdenInterna != null && !"".equals(OrdenInterna.trim())){
                    objeto = (Object[]) hibManagerCust.getSession().createSQLQuery(SQL)
                            .setParameter("ordenInterna", "%" + OrdenInterna).uniqueResult();
                } else if(centroBenefico != null && !"".equals(centroBenefico.trim())){
                    objeto = (Object[]) hibManagerCust.getSession().createSQLQuery(SQL)
                            .setParameter("centorBenefico", "%" + centroBenefico).uniqueResult();
                }
                
                if (objeto != null && objeto.length >= 2) {
                    clave = (String) objeto[0];
                    resp = (String) objeto[1];

                }

                hibManagerCust.getTransaction().commit();
            }
        } catch (Exception e) {
            hibManagerCust.getTransaction().rollback();
        } finally {
            try {
                hibManagerCust.closeSession();
            } catch (Exception e) {
                PintarLog.println("al cerrar la conexion", e);
            }
        }
        String[] datos = {clave, resp};
        return datos;
    }

    public String[] ObtId_emp_id_mon(String RFC, String razonSocial, String moneda) {
        String id_empresa = "", id_moneda = "";
        try {
            hibManagerCust = new HibernateUtilCustom("BIN931011519_hibernate.cfg.xml");
            hibManagerCust.initTransaction();
            String sql = "SELECT persona.id_persona as id_empresa "
                    + " FROM persona_moral, persona, rfc "
                    + " WHERE  persona.sit_persona = 'AC' AND (persona.id_persona = persona_moral.id_persona) AND (persona_moral.id_persona = rfc.id_persona(+)) "
                    + " AND (rfc.siglas_rfc || ''|| rfc.f_rfc || ''|| rfc.homoclave_rfc) LIKE :rfc AND nom_razon_social  LIKE :razonSocial  AND  ROWNUM = 1 ";

            String sqlm = "SELECT  CVE_MONEDA FROM moneda WHERE DESC_C_ID_SEC= :moneda AND  ROWNUM = 1 ";
            Object objeto = hibManagerCust.getSession().createSQLQuery(sql)
                    .setParameter("rfc", RFC)
                    .setParameter("razonSocial", razonSocial).uniqueResult();
            Object objeto2 = hibManagerCust.getSession().createSQLQuery(sqlm)
                    .setParameter("moneda", moneda).uniqueResult();
            hibManagerCust.getTransaction().commit();
            hibManagerCust.closeSession();
            if (objeto != null) {
                id_empresa = "" + objeto;
            }
            if (objeto2 != null) {
                id_moneda = "" + objeto2;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String[] claves = {id_empresa, id_moneda};

        return claves;
    }

    public String ObtenCveConcepto(String cptofac) {
        String cveCpto = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MConceptosFacturacion.class);
            cr.add(Restrictions.eq("conceptofacturacion", cptofac));
            MConceptosFacturacion cpto = (MConceptosFacturacion) cr.uniqueResult();
            if (cpto != null) {
                cveCpto = cpto.getClaveconcepto();
            }
            hibManagerRO.getTransaction().commit();
        } catch (Exception e) {
            hibManagerRO.getTransaction().rollback();
            PintarLog.println("Error al almacendar los datos del receptor: " + e);
        } finally {
            try {
                hibManagerRO.closeSession();
            } catch (Exception e) {
                PintarLog.println("al cerrar la conexion", e);
            }
        }
        return cveCpto;
    }

    @SuppressWarnings("unused")
    private MCfd ObtenFacturaGenerada(int id) {
        MCfd cfd = null;
        try {
            hibManagerRO.initTransaction();
            cfd = (MCfd) hibManagerRO.getSession().get(MCfd.class, id);
            hibManagerRO.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            hibManagerRO.closeSession();
        }
        return cfd;
    }

    public String obtenerId_cliente(String Rfc) {
        String id_cliente = "";
        try {
            hibManagerCust = new HibernateUtilCustom("BIN931011519_hibernate.cfg.xml");
            hibManagerCust.initTransaction();
            String sql = "SELECT persona.id_persona  FROM persona, rfc  "
                    + " WHERE  persona.sit_persona = 'AC'  AND (persona.id_persona = rfc.id_persona(+)) "
                    + " AND (rfc.siglas_rfc || ''|| rfc.f_rfc || ''|| rfc.homoclave_rfc)  LIKE :rfc and  ROWNUM = 1 ";
            Object objeto2 = hibManagerCust.getSession().createSQLQuery(sql)
                    .setParameter("rfc", Rfc).uniqueResult();
            hibManagerCust.getTransaction().commit();
            hibManagerCust.closeSession();
            if (objeto2 != null) {
                id_cliente = "" + objeto2;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id_cliente;
    }

    @SuppressWarnings("deprecation")
    public boolean InsertaSAT(int ID_EMPRESA, int ID_AREA_FACT, String NUM_FACTURA, double IMP_FACTURA,
            double IMP_IVA_FACT, String CVE_OPERACION, String TX_FACTURA, double IMP_PAGADO, double IMP_IVA_PAGADO, double IMP_IMPAGADO,
            double IMP_IVA_IMPAGADO, int CVE_MONEDA, String TX_REFERENCIA, double ID_CONTRA_DV, Date F_ALTA, Date F_PAGO, Date F_PROG_PAGO, String CVE_USUAR_ALTA,
            String CVE_USUAR_PAGO, String SIT_FACTURA, double ID_CLIENTE, String CVE_MOTIVO, String B_IMPRESO,
            String DESC_FISCAL, String CVE_REFERENCIA, double ID_FACT_ORIGEN, Date F_CANCEL, String OBSERVACIONES,
            String ORDEN, String TX_ORDEN, String CTRO_BENEF, String SOC_CO) {
        boolean res = false;
        int ID_FACTURA = -1;
        try {
            hibManagerCust = new HibernateUtilCustom("BIN931011519_hibernate.cfg.xml");
            hibManagerCust.initTransaction();
            System.out.println("Obtiene conexion");
            try {
    	        CallableStatement cstmt = hibManagerCust.getSession().connection().prepareCall("{call PKGFACTU.stpFolio(?, ?,?)}");
                        cstmt.setInt(1, ID_EMPRESA);
                        cstmt.setInt(2, 1);
                        cstmt.registerOutParameter(3, java.sql.Types.INTEGER);  
                        cstmt.execute();
                        System.out.println("ID_factura : " + cstmt.getInt(3));
    	        ID_FACTURA= cstmt.getInt(3);
                        cstmt.close();
                    }
    	     catch (Exception e) {
    	    	   System.out.println("Error.:."+e.getMessage());
                e.printStackTrace();
            }

            if (ID_FACTURA > 0) {
                String sql = "INSERT INTO CF_FACTURA "
                        + " (ID_FACTURA, ID_EMPRESA, ID_AREA_FACT, NUM_FACTURA, IMP_FACTURA, IMP_IVA_FACT, CVE_OPERACION, "
                        + "TX_FACTURA, IMP_PAGADO, IMP_IVA_PAGADO, IMP_IMPAGADO, IMP_IVA_IMPAGADO, CVE_MONEDA, "
                        + "TX_REFERENCIA, ID_CONTRA_DV, F_ALTA, F_PAGO, F_PROG_PAGO, CVE_USUAR_ALTA, CVE_USUAR_PAGO, "
                        + "SIT_FACTURA, ID_CLIENTE, CVE_MOTIVO, B_IMPRESO, DESC_FISCAL, CVE_REFERENCIA, ID_FACT_ORIGEN, "
                        + "F_CANCEL, OBSERVACIONES, ORDEN, TX_ORDEN, CTRO_BENEF, SOC_CO)"
                        + " values (?, ?, ?,?,?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

                hibManagerCust.getSession().createSQLQuery(sql).setInteger(0, ID_FACTURA).setInteger(1, ID_EMPRESA).setInteger(2, ID_AREA_FACT)
                        .setString(3, NUM_FACTURA).setDouble(4, IMP_FACTURA).setDouble(5, IMP_IVA_FACT).setString(6, CVE_OPERACION)
                        .setString(7, TX_FACTURA).setDouble(8, IMP_PAGADO).setDouble(9, IMP_IVA_PAGADO).setDouble(10, IMP_IMPAGADO).setDouble(11, IMP_IVA_IMPAGADO).setInteger(12, CVE_MONEDA)
                        .setString(13, TX_REFERENCIA).setDouble(14, ID_CONTRA_DV).setDate(15, F_ALTA).setDate(16, F_PAGO).setDate(17, F_PROG_PAGO).setString(18, CVE_USUAR_ALTA).setString(19, CVE_USUAR_PAGO)
                        .setString(20, SIT_FACTURA).setDouble(21, ID_CLIENTE).setString(22, CVE_MOTIVO).setString(23, B_IMPRESO).setString(24, DESC_FISCAL).setString(25, CVE_REFERENCIA).setDouble(26, ID_FACT_ORIGEN)
                        .setDate(27, F_CANCEL).setString(28, OBSERVACIONES).setString(29, ORDEN).setString(30, TX_ORDEN).setString(31, CTRO_BENEF).setString(32, SOC_CO).executeUpdate();
                res = true;

            }
        } catch (Exception e) {

            System.out.println("Error" + e.getMessage());
        } finally {
            hibManagerCust.getTransaction().commit();
            hibManagerCust.closeSession();
        }

        return res;
    }

    public MCfd ObtenFacturaGeneradaUUID(String uuid) {
        MCfd cfd = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MCfd.class);
            cr.add(Restrictions.eq("uuid", uuid));
            cfd = (MCfd) cr.uniqueResult();

        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            hibManagerRO.getTransaction().commit();
            hibManagerRO.closeSession();
        }
        return cfd;
    }

}
