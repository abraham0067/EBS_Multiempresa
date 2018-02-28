package fe.model.dao;

import fe.db.MCliente;
import fe.db.MEmpresa;
import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;
import org.hibernate.HibernateException;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import java.io.Serializable;
import java.util.List;
import fe.db.MAcceso.Nivel;
import fe.db.MAcceso;

public class ClienteDAO implements Serializable {

    private HibernateUtilApl hibManagerRO;
    private HibernateUtilEmi hibManagerSU;

    public ClienteDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
        hibManagerSU = new HibernateUtilEmi();
    }

    public boolean GuardarActualizar(MCliente cliente) {
        boolean val = false;
        try {
            hibManagerSU.initTransaction();
            hibManagerSU.getSession().saveOrUpdate(cliente);
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
    public List<MCliente> ListaClientesporUser(int idUser) {
        List<MCliente> clientes = null;
        try {
            hibManagerRO.initTransaction();
            MAcceso usuario = (MAcceso) hibManagerRO.getSession().get(MAcceso.class, idUser);
            if (usuario.getEmpresas() != null
                    && !usuario.getEmpresas().isEmpty()) {
                Integer[] idemps = new Integer[usuario.getEmpresas().size()];
                for (int i = 0; i < usuario.getEmpresas().size(); i++) {
                    idemps[i] = usuario.getEmpresas().get(i).getId();
                }
                Criteria cr = hibManagerRO.getSession().createCriteria(MCliente.class);
                cr.add(Restrictions.in("empresa.id", idemps));
                clientes = cr.list();
                hibManagerRO.getTransaction().commit();
            } else if (usuario.getNivel() == Nivel.INTERNO) {
                Criteria cr = hibManagerRO.getSession().createCriteria(MCliente.class);
                cr.setMaxResults(100);
                clientes = cr.list();
                hibManagerRO.getTransaction().commit();
            }
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            hibManagerRO.closeSession();
        }

        return clientes;
    }

    @SuppressWarnings("unchecked")
    public List<MCliente> ListaClientesporIdemp(int Idempresa) {
        List<MCliente> clientes = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MCliente.class);
            cr.add(Restrictions.eq("empresa.id", Idempresa));
            cr.addOrder(Order.asc("rfcOrigen"));
            clientes = cr.list();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            hibManagerRO.closeSession();
        }

        return clientes;
    }

    public MCliente BuscarMClienteIdr(int id) {
        MCliente cliente = null;
        try {
            hibManagerRO.initTransaction();
            cliente = (MCliente) hibManagerRO.getSession().get(MCliente.class, id);
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            hibManagerRO.closeSession();
        }

        return cliente;
    }

    @SuppressWarnings("unchecked")
    public List<MCliente> BusquedaParam(Integer idacceso, int idempresa, String columna, String value) {
        List<MCliente> lCliente = null;

        try {
            hibManagerRO.initTransaction();
            Criteria rp = hibManagerRO.getSession().createCriteria(MCliente.class);

            if (idempresa <= 0) {
                MAcceso acceso = (MAcceso) hibManagerRO.getSession().get(MAcceso.class, idacceso);
                if (acceso.getEmpresas() != null && !acceso.getEmpresas().isEmpty()) {
                    Integer[] idem = new Integer[acceso.getEmpresas().size()];
                    for (int i = 0; i < acceso.getEmpresas().size(); i++) {
                        idem[i] = acceso.getEmpresas().get(i).getId();
                    }
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

            //rp.setMaxResults(10);
            lCliente = rp.list();

            hibManagerRO.getTransaction().commit();
        } catch (HibernateException e) {
            hibManagerRO.getTransaction().rollback();
            e.printStackTrace(System.err);
        } finally {
            hibManagerRO.closeSession();
        }
        return lCliente;
    }

    public String BusquedaRFC(int idempresa) {
        String rfc = null;
        try {
            hibManagerRO.initTransaction();
            MEmpresa empresa = (MEmpresa) hibManagerRO.getSession().get(MEmpresa.class, idempresa);
            if (empresa != null)
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

    public boolean BorrarCliente(MCliente cliente) {
        boolean val = false;
        try {
            hibManagerSU.initTransaction();
            hibManagerSU.getSession().delete(cliente);
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

    public MCliente BuscarMClienteRfc_IdEmpresa(String rfc, int empresa) {
        MCliente cliente = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MCliente.class);
            cr.add(Restrictions.eq("rfcOrigen", rfc.trim().toUpperCase()));
            cr.add(Restrictions.eq("empresa.id", empresa));
            cliente = (MCliente) cr.uniqueResult();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            hibManagerRO.closeSession();
        }

        return cliente;
    }
}