package fe.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import fe.db.MAcceso;
import fe.db.MLogAcceso;
import fe.db.MAcceso.Nivel;
import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;

import java.io.Serializable;

public class UsuarioDAO implements Serializable {

    private HibernateUtilApl hibManagerRO;
    private HibernateUtilEmi hibManagerSU;

    public UsuarioDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
        hibManagerSU = new HibernateUtilEmi();
    }

    public boolean BorrarUsuario(Integer idacceso) {
        boolean val = true;
        try {
            MAcceso temmp = BuscarUsuarioId(idacceso);
            hibManagerSU.initTransaction();
            temmp.setEmpresas(null);//Requerido debido al tipo de fecth en el MACCESO java
            hibManagerSU.getSession().saveOrUpdate(temmp);
            hibManagerSU.getSession().delete(temmp);
            hibManagerSU.getSession().flush();
            hibManagerSU.getTransaction().commit();
            val = true;
        } catch (HibernateException ex) {
            hibManagerSU.getTransaction().rollback();
            System.out.println(ex.getMessage());
            val = false;
        } finally {
            try {
                hibManagerSU.closeSession();
            } catch (Exception e) {
            }
        }
        return val;
    }

    public boolean guardarActualizarUsuario(MAcceso user) {
        boolean ok = true;
        try {
            hibManagerSU.initTransaction();
            hibManagerSU.getSession().saveOrUpdate(user);
            hibManagerSU.getSession().getTransaction().commit();
        } catch (HibernateException ex) {
            ok = false;
            hibManagerSU.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            try {
                hibManagerSU.closeSession();
            } catch (Exception e) {
            }
        }
        return ok;
    }

    public MAcceso BuscarUsuarioUser(String usuario) {
        MAcceso acceso = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MAcceso.class);
            cr.add(Restrictions.isNotNull("usuario"));
            cr.add(Restrictions.ilike("usuario", usuario.trim()));
            acceso = (MAcceso) cr.uniqueResult();
            if (acceso != null && acceso.getEmpresas() != null
                    && !acceso.getEmpresas().isEmpty()) {

            }
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            try {
                hibManagerRO.closeSession();
            } catch (Exception e) {
            }
        }
        return acceso;
    }

    public MAcceso BuscarUsuarioId(Integer id) {
        MAcceso acceso = null;
        try {
            hibManagerRO.initTransaction();
            acceso = (MAcceso) hibManagerRO.getSession().get(MAcceso.class, id);
            if (acceso.getEmpresas() != null && !acceso.getEmpresas().isEmpty()) {
            }
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();

            ex.printStackTrace(System.err);
        } finally {
            try {
                hibManagerRO.closeSession();
            } catch (Exception e) {
            }
        }

        return acceso;
    }


    @SuppressWarnings("unchecked")
    public List<MAcceso> ListaUsuarios(Integer idUser, int idempresa, String parametro, String value) {
        List<MAcceso> listaAccesos = null;
        try {
            hibManagerRO.initTransaction();
            MAcceso usuario = (MAcceso) hibManagerRO.getSession().get(MAcceso.class, idUser);
            if (idempresa > 0) {
                Criteria cr = hibManagerRO.getSession().createCriteria(MAcceso.class);
                cr.add(Restrictions.eq("empresa.id", idempresa));
                if (usuario.getNivel() == Nivel.EXTERNO) {
                    cr.add(Restrictions.eq("nivel", Nivel.EXTERNO));
                }
                cr.addOrder(Order.asc("usuario"));
                if (!parametro.trim().equals("Todos") && !parametro.trim().equals("Ninguno") && !value.trim().equals("")) {
                    cr.add(Restrictions.ilike(parametro.trim(), "%" + value.trim() + "%"));
                }
                cr.setMaxResults(100);
                listaAccesos = cr.list();
            } else if (usuario.getEmpresas() != null && !usuario.getEmpresas().isEmpty()) {
                Integer[] ids = new Integer[usuario.getEmpresas().size()];
                for (int i = 0; i < usuario.getEmpresas().size(); i++) {
                    ids[i] = usuario.getEmpresas().get(i).getId();
                }
                Criteria cr = hibManagerRO.getSession().createCriteria(MAcceso.class);
                cr.add(Restrictions.in("empresa.id", ids));
                if (usuario.getNivel() == Nivel.EXTERNO) {
                    cr.add(Restrictions.eq("nivel", Nivel.EXTERNO));
                }
                cr.addOrder(Order.asc("usuario"));
                if (!parametro.trim().equals("Todos") && !parametro.trim().equals("Ninguno")) {
                    cr.add(Restrictions.ilike(parametro.trim(), "%" + value.trim() + "%"));
                }
                cr.setMaxResults(100);
                listaAccesos = cr.list();
            } else if (usuario.getNivel() == Nivel.INTERNO) {
                Criteria cr = hibManagerRO.getSession().createCriteria(MAcceso.class);
                if (!parametro.trim().equals("Todos") && !parametro.trim().equals("Ninguno")) {
                    cr.add(Restrictions.ilike(parametro.trim(), "%" + value.trim() + "%"));
                }
                cr.addOrder(Order.asc("usuario"));
                cr.setMaxResults(100);
                listaAccesos = cr.list();
            }
            hibManagerRO.getTransaction().commit();

        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            try {
                hibManagerRO.closeSession();
            } catch (Exception e) {
            }
        }

        return listaAccesos;
    }

    public MAcceso buscarUserByActivationKey(String activationKey) {

        MAcceso res = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MAcceso.class);
            cr.add(Restrictions.eq("verificationKey", activationKey));
            res = (MAcceso) cr.uniqueResult();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            try {
                hibManagerRO.closeSession();
            } catch (Exception e) {

            }
        }
        return res;
    }

}
