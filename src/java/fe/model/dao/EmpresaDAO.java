/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.model.dao;

import fe.db.Empresa_EmpresaTimbre;
import fe.db.MAcceso;
import fe.db.MAcceso.Nivel;
import fe.db.MEmpresa;
import fe.db.MEmpresaMTimbre;
import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lily
 */
public class EmpresaDAO implements Serializable {

    private HibernateUtilApl hibManagerRO;
    private HibernateUtilEmi hibManagerSU;

    public EmpresaDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
        hibManagerSU = new HibernateUtilEmi();
    }

    public boolean BorrarEmpresa(MEmpresa empresa) {
        boolean valor = true;
        try {
            hibManagerSU.initTransaction();
            hibManagerSU.getSession().delete(empresa);
            hibManagerSU.getSession().delete(empresa.getDireccion());
            hibManagerSU.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerSU.getTransaction().rollback();
            valor = false;
            ex.printStackTrace(System.out);
        } finally {
            hibManagerSU.closeSession();
        }
        return valor;
    }

    public boolean GuardarOActualizaEmpresa(MEmpresa empresa) {
        boolean valor = true;
        try {
            hibManagerSU.initTransaction();
            hibManagerSU.getSession().saveOrUpdate(empresa);
            hibManagerSU.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerSU.getTransaction().rollback();
            valor = false;
            ex.printStackTrace(System.err);
        } finally {
            hibManagerSU.closeSession();
        }
        return valor;
    }

    public String getRfcEmpresaById(Integer id) {

        hibManagerRO.initTransaction();
        ///Single column return
        String rfcEmisor = (String) hibManagerRO.getSession().createQuery("SELECT emps.rfcOrigen from MEmpresa emps " +
                "where emps.id  = :id").setInteger("id", id)
                .setMaxResults(1)
                .uniqueResult();
        hibManagerRO.getTransaction().commit();

        return rfcEmisor;

    }

    /*
	 * Busca una empresa mediante su RFC
     */
    public MEmpresa BuscarEmpresaRFC(String RFC) {
        MEmpresa empresa = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MEmpresa.class);
            cr.add(Restrictions.isNotNull("rfcOrigen"));
            cr.add(Restrictions.ilike("rfcOrigen", RFC.trim()));
            empresa = (MEmpresa) cr.uniqueResult();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            hibManagerRO.closeSession();
        }
        return empresa;
    }

    /*
	 * Busca una empresa mediante su id
     */
    public MEmpresa BuscarEmpresaId(int id) {
        MEmpresa empresa = null;
        try {
            hibManagerRO.initTransaction();
            empresa = (MEmpresa) hibManagerRO.getSession().get(MEmpresa.class, id);
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            hibManagerRO.closeSession();
        }
        return empresa;
    }

    /*
	 * Lista de empresas que tiene asignado un usuario, en caso de ser interno
	 * se muestran 100
     */
    @SuppressWarnings("unchecked")
    public List<MEmpresa> ListaEmpresasPadres(Integer idUser) {
        List<MEmpresa> listaEmpresas = new ArrayList<MEmpresa>();
        try {
            hibManagerRO.initTransaction();
            MAcceso usuario = (MAcceso) hibManagerRO.getSession().get(MAcceso.class, idUser);
            if (usuario.getEmpresas() != null && !usuario.getEmpresas().isEmpty()) {
                //System.out.println();
                //System.out.println("Tiene empresas asignadas");
                for (int i = 0; i < usuario.getEmpresas().size(); i++) {
                    if (usuario.getEmpresas().get(i).getEstatusEmpresa() == 1) {
                        listaEmpresas.add(usuario.getEmpresas().get(i));
                    }
                }
            } else if (usuario.getNivel() == Nivel.INTERNO) {
                Criteria cr = hibManagerRO.getSession().createCriteria(MEmpresa.class);
                cr.add(Restrictions.eq("estatusEmpresa", 1));
                cr.addOrder(Order.asc("rfcOrigen"));
                listaEmpresas = cr.list();
            }
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            hibManagerRO.closeSession();
        }
        return listaEmpresas;

    }

    /**
     * Lista de empresas asignadas a un usuario externo
     */
    public List<MEmpresa> ListaAsignadas(Integer idUser) {
        List<MEmpresa> listaEmpresas = null;
        try {
            hibManagerRO.initTransaction();
            MAcceso usuario = (MAcceso) hibManagerRO.getSession().get(MAcceso.class, idUser);
            if (usuario.getEmpresas() != null
                    && !usuario.getEmpresas().isEmpty()) {
                listaEmpresas = usuario.getEmpresas();
            }
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            hibManagerRO.closeSession();
        }
        return listaEmpresas;

    }


    public List<MEmpresa> ListaPosiblesEmp(Integer idPadre, Integer[] asig) {
        List<MEmpresa> listaEmpresas = new ArrayList<MEmpresa>();
        try {
            hibManagerRO.initTransaction();
            if (asig != null && asig.length > 0) {
                String nota = null;
                boolean existe = false;
                for (int h = 0; h < asig.length; h++) {

                    if (asig[h].intValue() == idPadre.intValue()) {
                        existe = true;
                    }
                    if (h == 0) {
                        nota = "" + asig[h];
                    } else {
                        nota = nota + "," + asig[h];
                    }
                }
                String hql ="SELECT ID_SUBEMPRESA FROM M_RELACION_EMPRESAS WHERE  ID_EMPRESA = :idPadre AND   NOT ( ID_SUBEMPRESA IN  ( :nota ))";
                List<Integer> listem = hibManagerRO.getSession().createSQLQuery(hql)
                        .setInteger("idPadre", idPadre).setString("nota", nota).list();
                MEmpresa padre = (MEmpresa) hibManagerRO.getSession()
                        .get(MEmpresa.class, idPadre);
                if (listem != null && !listem.isEmpty()) {
                    Criteria cr = hibManagerRO.getSession().createCriteria(MEmpresa.class);
                    cr.add(Restrictions.in("id", listem));
                    cr.add(Restrictions.eq("estatusEmpresa", 1));
                    cr.addOrder(Order.asc("rfcOrigen"));
                    listaEmpresas = cr.list();
                    if (!existe) {
                        listaEmpresas.add(padre);
                    }
                } else if (!existe) {
                    listaEmpresas.add(padre);
                }
            } else {
                String hql = "SELECT ID_SUBEMPRESA FROM M_RELACION_EMPRESAS WHERE  ID_EMPRESA = :idPadre";
                List<Integer> listem = hibManagerRO.getSession().createSQLQuery(hql)
                        .setInteger("idPadre", idPadre).list();
                MEmpresa padre = (MEmpresa) hibManagerRO.getSession()
                        .get(MEmpresa.class, idPadre);
                if (listem != null && !listem.isEmpty()) {
                    Criteria cr = hibManagerRO.getSession().createCriteria(MEmpresa.class);
                    cr.add(Restrictions.in("id", listem));
                    cr.addOrder(Order.asc("rfcOrigen"));
                    cr.add(Restrictions.eq("estatusEmpresa", 1));
                    listaEmpresas = cr.list();
                    listaEmpresas.add(padre);
                } else {
                    listaEmpresas.add(padre);
                }
            }
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            hibManagerRO.closeSession();
        }
        return listaEmpresas;

    }

    @SuppressWarnings("unchecked")
    public List<MEmpresa> BusquedaEmpresas(Integer IdUsuario, String TipoBus,
            String paramBus) {
        List<MEmpresa> empresas = null;
        try {
            hibManagerRO.initTransaction();
            MAcceso usuario = (MAcceso) hibManagerRO.getSession().get(MAcceso.class, IdUsuario);
            if (usuario != null && usuario.getEmpresas() != null
                    && !usuario.getEmpresas().isEmpty()) {
                Integer[] idEmps = new Integer[usuario.getEmpresas().size()];
                for (int i = 0; i < usuario.getEmpresas().size(); i++) {
                    idEmps[i] = usuario.getEmpresas().get(i).getId();
                }
                Criteria cr = hibManagerRO.getSession().createCriteria(MEmpresa.class);
                cr.add(Restrictions.in("id", idEmps));
                if (!TipoBus.trim().equals("Todos")) {
                    cr.add(Restrictions.ilike(TipoBus, paramBus.trim() + "%"));
                }
                cr.addOrder(Order.asc("rfcOrigen"));
                cr.setMaxResults(100);
                empresas = cr.list();

            } else if (usuario.getNivel() == Nivel.INTERNO) {
                Criteria cr = hibManagerRO.getSession().createCriteria(MEmpresa.class);
                if (!TipoBus.trim().equals("Todos")) {
                    cr.add(Restrictions.ilike(TipoBus, paramBus.trim() + "%"));
                }
                cr.addOrder(Order.asc("rfcOrigen"));
                cr.setMaxResults(100);
                empresas = cr.list();
            }
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }

        return empresas;
    }

    /*
	 * IDS de empresas Seleccionadas
     */
    @SuppressWarnings("unchecked")
    public List<MEmpresa> ListaEmpresasSeleccionadas(Integer[] asig) {
        List<MEmpresa> listaEmpresas = new ArrayList<MEmpresa>();
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MEmpresa.class);
            cr.add(Restrictions.eq("estatusEmpresa", 1));
            cr.add(Restrictions.in("id", asig));
            cr.addOrder(Order.asc("rfcOrigen"));
            listaEmpresas = cr.list();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            hibManagerRO.closeSession();
        }
        return listaEmpresas;

    }

    public MEmpresa BuscarEmpresaPadre(int idHijo) {
        MEmpresa padre = null;
        try {
            hibManagerRO.initTransaction();
            String hql = "SELECT ID_EMPRESA FROM M_RELACION_EMPRESAS WHERE ID_SUBEMPRESA= :idHijo";
            Integer id = (Integer) hibManagerRO.getSession().createSQLQuery(hql).
                    setInteger("idHijo", idHijo).uniqueResult();
            if (id != null && id.intValue() > 0) {
                padre = (MEmpresa) hibManagerRO.getSession().get(MEmpresa.class, id.intValue());
            }
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return padre;
    }

    public List<Empresa_EmpresaTimbre> ListaEmpEmpTim(List<MEmpresa> empresas) {
        List<Empresa_EmpresaTimbre> nuevaList = null;
        if (empresas != null && !empresas.isEmpty()) {
            try {
                nuevaList = new ArrayList<Empresa_EmpresaTimbre>();
                hibManagerRO.initTransaction();
                for (int i = 0; i < empresas.size(); i++) {
                    Criteria cr = hibManagerRO.getSession().createCriteria(MEmpresaMTimbre.class);
                    cr.add(Restrictions.eq("empresa", empresas.get(i)));
                    MEmpresaMTimbre empTim = (MEmpresaMTimbre) cr.uniqueResult();
                    nuevaList.add(new Empresa_EmpresaTimbre(empresas.get(i), empTim));
                }
                hibManagerRO.getTransaction().commit();
            } catch (HibernateException ex) {
                hibManagerRO.getTransaction().rollback();
                ex.printStackTrace();
            } finally {
                hibManagerRO.closeSession();
            }
        }
        return nuevaList;
    }

}
