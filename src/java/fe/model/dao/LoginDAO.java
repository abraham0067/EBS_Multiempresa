/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.model.dao;

import fe.db.MAcceso;
import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import java.io.Serializable;

/**
 *
 * @author Lily
 */
public class LoginDAO implements Serializable {

    private HibernateUtilApl hibManagerRO;
    private HibernateUtilEmi hibManagerSU;

    public LoginDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
        hibManagerSU = new HibernateUtilEmi();
    }

    public MAcceso buscarAccesoPorLogin(String usuario) throws Exception {
        MAcceso a = null;
        if (usuario != null && !"".equals(usuario.trim())) {
            try {
                if (hibManagerRO.initTransaction()) {
                    Criteria ct = hibManagerRO.getSession().createCriteria(MAcceso.class);
                    ct.add(Restrictions.isNotNull("usuario"));
                    ct.add(Restrictions.eq("usuario", usuario.trim()));
                    a = (MAcceso) ct.uniqueResult();
                    hibManagerRO.getTransaction().commit();
                }
            } catch (HibernateException ex) {
                hibManagerRO.getTransaction().rollback();
                ex.printStackTrace(System.err);
            } finally {
                hibManagerRO.closeSession();
            }
        }
        return a;
    }

    public MAcceso buscarAccesoPorLoginPass(String usuario, String pass)
            throws Exception {
        MAcceso a = null;
        try {
            if (hibManagerRO.initTransaction()) {
                /*
				 * MPerfil perfil= (MPerfil) session.get(MPerfil.class,1);
				 * MAcceso acceso= new
				 * MAcceso("admingral","samaujYHh3voRwBSM5ajw29Wd6Y=",perfil);
				 * session.saveOrUpdate(acceso);
                 */

                Criteria ct = hibManagerRO.getSession().createCriteria(MAcceso.class);
                ct.add(Restrictions.eq("usuario", usuario));
                a = (MAcceso) ct.uniqueResult();
                /*
				 * if (a.getPerfil().getEmpresas() != null &&
				 * !a.getPerfil().getEmpresas().isEmpty()) { if
				 * (a.getPerfil().getEmpresas().get(0) != null) {
				 * 
				 * }
				 * 
				 * }
                 */
                hibManagerRO.getTransaction().commit();
            }
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            hibManagerRO.closeSession();
        }
        return a;
    }

    public MAcceso buscarAccesoPorId(Integer id) throws HibernateException {
        MAcceso a = null;
        try {
            hibManagerRO.initTransaction();
            a = (MAcceso) hibManagerRO.getSession().get(MAcceso.class, id);
            if (a != null) {
                if (a.getEmpresas() != null && !a.getEmpresas().isEmpty()) {

                }
            }
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            hibManagerRO.closeSession();
        }

        return a;

    }

    public boolean guardarActualizarAcceso(MAcceso action) {
        boolean ok = true;
        try {
            hibManagerRO.initTransaction();
            // session.update(action.getPerfil());
            hibManagerRO.getSession().saveOrUpdate(action);
            hibManagerRO.getTransaction().commit();

        } catch (HibernateException ex) {
            ok = false;
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            hibManagerRO.closeSession();
        }
        return ok;
    }

    public String ObtenerRuta(String url) {
        String p[] = url.split("/");
        String rutaFinal = "/";
        for (int i = 0; i < p.length; i++) {
            if (!"".equals(p[i].trim())) {
                if (p[i].trim().equals("WEB-INF")) {
                    rutaFinal = rutaFinal + p[i].trim();
                    break;
                } else {
                    rutaFinal = rutaFinal + p[i].trim() + "/";
                }
            }
        }
        return rutaFinal;
    }

    public String ObtenerRuta(String url, String directorio) {
        String p[] = url.split("/");
        String rutaFinal = "/";
        for (int i = 0; i < p.length; i++) {

            if (!"".equals(p[i].trim())) {
                if ((p[i].trim()).equals(directorio)) {
                    rutaFinal = rutaFinal + p[i].trim();
                    break;
                } else {
                    rutaFinal = rutaFinal + p[i].trim() + "/";
                }
            }
        }
        return rutaFinal;
    }

    public String obtenerRutaDeDirectorioAnterior(String url, String directorio) {
        String p[] = url.split("/");
        String rutaFinal = "/";
        for (int i = 0; i < p.length; i++) {

            if (!"".equals(p[i].trim())) {
                // PintarLog.println("p[i].trim() "+p[i].trim());
                if ((p[i].trim()).equals(directorio)) {
                    // rutaFinal = rutaFinal + p[i].trim();
                    break;
                } else {
                    rutaFinal = rutaFinal + p[i].trim() + "/";
                }
            }
        }
        return rutaFinal;
    }

    public MAcceso buscarAccesoRfcUser(String RFC, String usuario)
            throws Exception {
        MAcceso a = null;
        try {
            if (hibManagerRO.initTransaction()) {
                Criteria ct = hibManagerRO.getSession().createCriteria(MAcceso.class);
                ct.add(Restrictions.eq("usuario", usuario));
                ct.add(Restrictions.isNotNull("rfc"));
                ct.add(Restrictions.eq("rfc", RFC));
                a = (MAcceso) ct.uniqueResult();
                hibManagerRO.getTransaction().commit();
            }
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.err);
        } finally {
            hibManagerRO.closeSession();
        }
        return a;

    }

    public boolean EliminaAcceso(MAcceso acceso) {
        boolean val = true;
        try {
            hibManagerSU.initTransaction();
            hibManagerSU.getSession().delete(acceso);
            hibManagerSU.getTransaction().commit();
            val = true;
        } catch (HibernateException ex) {
            hibManagerSU.getTransaction().rollback();
            val = false;
        } finally {
            hibManagerSU.closeSession();
        }
        return val;
    }

    public String decrypt(String cadena) {
        StandardPBEStringEncryptor s = new StandardPBEStringEncryptor();
        s.setPassword("uniquekey");
        String devuelve = "";
        try {
            devuelve = s.decrypt(cadena);
        } catch (HibernateException e) {
        }
        return devuelve;
    }

}
