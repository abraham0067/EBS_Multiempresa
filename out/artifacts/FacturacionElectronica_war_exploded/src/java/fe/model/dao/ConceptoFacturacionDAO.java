package fe.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import fe.db.MAcceso;
import fe.db.MConceptosFacturacion;
import fe.db.MAcceso.Nivel;
import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;
import java.io.Serializable;

public class ConceptoFacturacionDAO implements Serializable {

    private HibernateUtilApl hibManagerRO;
    private HibernateUtilEmi hibManagerSU;

    public ConceptoFacturacionDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
        hibManagerSU = new HibernateUtilEmi();
    }

    public boolean GuardarOActualizar(MConceptosFacturacion concepto) {
        boolean valor = true;
        try {
            hibManagerSU.initTransaction();
            hibManagerSU.getSession().saveOrUpdate(concepto);
            hibManagerSU.getTransaction().commit();
        } catch (HibernateException ex) {
            valor = false;
            hibManagerSU.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            hibManagerSU.closeSession();
        }

        return valor;
    }

    public boolean BorrarConcepto(MConceptosFacturacion concepto) {
        boolean valor = true;
        try {
            hibManagerSU.initTransaction();
            hibManagerSU.getSession().delete(concepto);
            hibManagerSU.getTransaction().commit();
        } catch (HibernateException ex) {
            valor = false;
            hibManagerSU.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            hibManagerSU.closeSession();
        }

        return valor;
    }

    public MConceptosFacturacion BuscarConceptoId(int idconcepto) {
        MConceptosFacturacion concepto = null;
        try {
            hibManagerRO.initTransaction();
            concepto = (MConceptosFacturacion) hibManagerRO.getSession().get(
                    MConceptosFacturacion.class, idconcepto);
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return concepto;
    }

    public MConceptosFacturacion BuscarConceptoEmpresa(String Concepto,
            int idempresa) {
        MConceptosFacturacion concepto = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MConceptosFacturacion.class);
            cr.add(Restrictions.ilike("conceptofacturacion", Concepto));
            cr.add(Restrictions.eq("empresa.id", idempresa));
            concepto = (MConceptosFacturacion) cr.uniqueResult();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return concepto;
    }


    public List<MConceptosFacturacion> BuscaConceptos(int iduser,
            int idEmpresa, String tipoBus, String Parametro) {
        List<MConceptosFacturacion> conceptos = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MConceptosFacturacion.class);
//			System.out.println(idEmpresa + "kjjk");
            if (idEmpresa > 0) {
//				System.out.println(idEmpresa + "aqui ");
                cr.add(Restrictions.eq("empresa.id", idEmpresa));
                if (!tipoBus.equals("Todos")
                        && !tipoBus.trim().equals("Ninguno")) {
                    cr.add(Restrictions.ilike(tipoBus.trim(),
                            "%" + Parametro.trim() + "%"));
                }
                cr.addOrder(Order.asc("claveconcepto"));
                cr.setMaxResults(100);
                conceptos = cr.list();
            } else {
                MAcceso acceso = (MAcceso) hibManagerRO.getSession().get(MAcceso.class, iduser);
                if (acceso.getEmpresas() != null
                        && !acceso.getEmpresas().isEmpty()) {
                    cr.add(Restrictions.in("empresa", acceso.getEmpresas()));
                    if (!tipoBus.equals("Todos")
                            && !tipoBus.trim().equals("Ninguno")) {
                        cr.add(Restrictions.ilike(tipoBus.trim(), "%"
                                + Parametro.trim() + "%"));
                    }
                    cr.addOrder(Order.asc("claveconcepto"));
                    conceptos = cr.list();
                } else if (acceso.getNivel() == Nivel.INTERNO) {
                    if (!tipoBus.equals("Todos")
                            && !tipoBus.trim().equals("Ninguno")) {
                        cr.add(Restrictions.ilike(tipoBus.trim(), "%"
                                + Parametro.trim() + "%"));
                        cr.addOrder(Order.asc("claveconcepto"));
                        conceptos = cr.list();
                    }
                }
            }
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return conceptos;
    }


    public List<MConceptosFacturacion> ListaConceptosIdEmpresa(int idEmpresa) {
        List<MConceptosFacturacion> conceptos = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MConceptosFacturacion.class);
            cr.add(Restrictions.eq("empresa.id", idEmpresa));
            cr.setMaxResults(200);
            conceptos = cr.list();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return conceptos;
    }


    public List<MConceptosFacturacion> BusConceptoIdEmpresa(String concepto,
            int idEmpresa) {
        List<MConceptosFacturacion> conceptos = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MConceptosFacturacion.class);
            cr.add(Restrictions.eq("empresa.id", idEmpresa));
            cr.add(Restrictions.ilike("conceptofacturacion", concepto.trim()
                    + "%"));
            conceptos = cr.list();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return conceptos;

    }
}
