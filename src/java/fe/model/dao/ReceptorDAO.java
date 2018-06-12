package fe.model.dao;

import fe.db.MAcceso;
import fe.db.MAcceso.Nivel;
import fe.db.MEmpresa;
import fe.db.MReceptor;
import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.List;

public class ReceptorDAO implements Serializable {

    private HibernateUtilApl hibManagerRO;
    private HibernateUtilEmi hibManagerSU;

    public ReceptorDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
        hibManagerSU = new HibernateUtilEmi();
    }

    public boolean GuardarActualizar(MReceptor receptor) {
        boolean val = false;
        try {
            hibManagerSU.initTransaction();
            hibManagerSU.getSession().saveOrUpdate(receptor);
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

    @SuppressWarnings({"unchecked"})
    public List<MReceptor> ListaReceptoresporUser(int idUser) {
        List<MReceptor> receptores = null;
        try {
            hibManagerRO.initTransaction();
            MAcceso usuario = (MAcceso) hibManagerRO.getSession().get(MAcceso.class, idUser);
            if (usuario.getEmpresas() != null
                    && !usuario.getEmpresas().isEmpty()) {
                Integer[] idemps = new Integer[usuario.getEmpresas().size()];
                for (int i = 0; i < usuario.getEmpresas().size(); i++) {
                    idemps[i] = usuario.getEmpresas().get(i).getId();
                }
                Criteria cr = hibManagerRO.getSession().createCriteria(MReceptor.class);
                cr.add(Restrictions.in("empresa.id", idemps));
                receptores = cr.list();
                hibManagerRO.getTransaction().commit();
            } else if (usuario.getNivel() == Nivel.INTERNO) {
                Criteria cr = hibManagerRO.getSession().createCriteria(MReceptor.class);
                cr.setMaxResults(100);
                receptores = cr.list();
                hibManagerRO.getTransaction().commit();
            }
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            hibManagerRO.closeSession();
        }

        return receptores;
    }

    @SuppressWarnings("unchecked")
    public List<MReceptor> ListaReceptoresporIdemp(int Idempresa) {
        List<MReceptor> receptores = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MReceptor.class);
            cr.add(Restrictions.eq("empresa.id", Idempresa));
            cr.addOrder(Order.asc("rfcOrigen"));
            receptores = cr.list();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            hibManagerRO.closeSession();
        }

        return receptores;
    }

    public MReceptor BuscarReceptorIdr(int id) {
        MReceptor receptor = null;
        try {
            hibManagerRO.initTransaction();
            receptor = (MReceptor) hibManagerRO.getSession().get(MReceptor.class, id);
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            hibManagerRO.closeSession();
        }

        return receptor;
    }

    @SuppressWarnings("unchecked")
    public List<MReceptor> BusquedaParam(Integer idacceso, int idempresa, String columna, String value) {
        List<MReceptor> listlReceptor = null;

        try {
            hibManagerRO.initTransaction();
            Criteria rp = hibManagerRO.getSession().createCriteria(MReceptor.class);

            if (idempresa <= 0) {
                MAcceso acceso = (MAcceso) hibManagerRO.getSession().get(MAcceso.class, idacceso);
                if (acceso.getEmpresas() != null && !acceso.getEmpresas().isEmpty()) {
                    Integer[] idem = new Integer[acceso.getEmpresas().size()];
                    for (int i = 0; i < acceso.getEmpresas().size(); i++)
                        idem[i] = acceso.getEmpresas().get(i).getId();

                    rp.add(Restrictions.in("empresa.id", idem));
                    if (!columna.trim().equals("Todos") && !columna.trim().equals("Ninguno") && (!value.trim().equals("")))
                        rp.add(Restrictions.ilike(columna.trim(), "%" + value + "%"));


                } else if (acceso.getNivel() == Nivel.INTERNO) {
                    if (!columna.trim().equals("Todos") && !columna.trim().equals("Ninguno") && (!value.trim().equals("")))
                        rp.add(Restrictions.ilike(columna.trim(), "%" + value + "%"));
                }
            } else {
                rp.add(Restrictions.eq("empresa.id", idempresa));
                if (!columna.trim().equals("Todos")
                        && !columna.trim().equals("Ninguno")
                        && (!value.trim().equals(""))) {
                    rp.add(Restrictions.ilike(columna.trim(), "%" + value + "%"));
                }
            }

            listlReceptor = rp.list();

            hibManagerRO.getTransaction().commit();
        } catch (HibernateException e) {
            hibManagerRO.getTransaction().rollback();
            e.printStackTrace(System.err);
        } finally {
            hibManagerRO.closeSession();
        }
        return listlReceptor;
    }

    public String BusquedaRFC(int idempresa) {
        String rfc = null;
        try {
            hibManagerRO.initTransaction();

            MEmpresa empresa = (MEmpresa) hibManagerRO.getSession().get(MEmpresa.class, idempresa);
            if(empresa != null)
                rfc = empresa.getRfcOrigen();

            hibManagerRO.getTransaction().commit();
        } catch (HibernateException e) {
            hibManagerRO.getTransaction().rollback();
            e.printStackTrace(System.err);
        } finally {
            hibManagerRO.closeSession();
        }
        return rfc;
    }

    public boolean BorrarReceptor(MReceptor receptor) {
        boolean val = false;
        try {
            hibManagerSU.initTransaction();
            hibManagerSU.getSession().delete(receptor);
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

    public MReceptor BuscarReceptorRfc_IdEmpresa(String rfc, int empresa) {
        MReceptor receptor = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MReceptor.class);
            cr.add(Restrictions.eq("rfcOrigen", rfc.trim().toUpperCase()));
            cr.add(Restrictions.eq("empresa.id", empresa));
            receptor = (MReceptor) cr.uniqueResult();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            hibManagerRO.closeSession();
        }

        return receptor;
    }

    public MReceptor BuscarReceptorRfc(String rfc) {
        MReceptor receptor = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MReceptor.class);
            cr.add(Restrictions.eq("rfcOrigen", rfc.trim().toUpperCase()));
            receptor = (MReceptor) cr.uniqueResult();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            hibManagerRO.closeSession();
        }

        return receptor;
    }

}
