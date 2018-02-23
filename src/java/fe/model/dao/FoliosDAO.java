package fe.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import fe.db.MAcceso;
import fe.db.MFolios;
import fe.db.MAcceso.Nivel;
import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;
import java.io.Serializable;

public class FoliosDAO implements Serializable {

    private HibernateUtilApl hibManagerRO;
    private HibernateUtilEmi hibManagerSU;

    public FoliosDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
        hibManagerSU = new HibernateUtilEmi();

    }

    @SuppressWarnings("unchecked")
    public List<MFolios> ListaFoliosxUser(int idUser) {
        List<MFolios> Folios = null;
        try {
            hibManagerRO.initTransaction();
            MAcceso acceso = (MAcceso) hibManagerRO.getSession().get(MAcceso.class, idUser);
            if (acceso.getEmpresas() != null && !acceso.getEmpresas().isEmpty()) {
                Integer[] idemp = new Integer[acceso.getEmpresas().size()];
                for (int i = 0; i < acceso.getEmpresas().size(); i++) {
                    idemp[i] = acceso.getEmpresas().get(i).getId();
                }
                Criteria cr = hibManagerRO.getSession().createCriteria(MFolios.class);
                cr.add(Restrictions.in("empresa.id", idemp));
                Folios = cr.list();
                hibManagerRO.getTransaction().commit();
            } else if (acceso.getNivel() == Nivel.INTERNO) {
                Criteria cr = hibManagerRO.getSession().createCriteria(MFolios.class);
                cr.addOrder(Order.desc("fecha"));
                cr.setMaxResults(100);
                Folios = cr.list();
                hibManagerRO.getTransaction().commit();
            }
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return Folios;
    }

    @SuppressWarnings("unchecked")
    public List<MFolios> ListaFoliosidEmpresa(int idEmpresa) {
        List<MFolios> Folios = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MFolios.class);
            cr.add(Restrictions.eq("estatus", 1));
            cr.add(Restrictions.eq("empresa.id", idEmpresa));
            Folios = cr.list();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return Folios;
    }

    public MFolios BuscarFolioId(int idFolio) {
        MFolios folio = null;
        try {
            hibManagerRO.initTransaction();
            folio = (MFolios) hibManagerRO.getSession().get(MFolios.class, idFolio);
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return folio;
    }

    public MFolios BuscarFolioSerieIdempresa(String serie, int idempresa) {
        MFolios folio = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MFolios.class);
            cr.add(Restrictions.eq("empresa.id", idempresa));
            cr.add(Restrictions.ilike("serie", serie));
            cr.add(Restrictions.eq("estatus", 1));
            folio = (MFolios) cr.uniqueResult();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return folio;
    }

    @SuppressWarnings("unchecked")
    public List<MFolios> BusquedaParam(Integer idacceso, int idempresa,
            String columna, String value) {
        List<MFolios> ListFolios = null;
        try {
            hibManagerRO.initTransaction();
            Criteria rp = hibManagerRO.getSession().createCriteria(MFolios.class);
            if (idempresa <= 0) {
                MAcceso acceso = (MAcceso) hibManagerRO.getSession().get(MAcceso.class, idacceso);
                if (acceso.getEmpresas() != null
                        && !acceso.getEmpresas().isEmpty()) {
                    Integer[] idem = new Integer[acceso.getEmpresas().size()];
                    for (int i = 0; i < acceso.getEmpresas().size(); i++) {
                        idem[i] = acceso.getEmpresas().get(i).getId();
                    }
                    rp.add(Restrictions.in("empresa.id", idem));
                    if (!columna.trim().equals("Todos")
                            && !columna.trim().equals("Ninguno")
                            && (!value.trim().equals(""))) {
                        rp.add(Restrictions.ilike(columna.trim(), "%" + value
                                + "%"));
                    }
                } else if (acceso.getNivel() == Nivel.INTERNO) {
                    if (!columna.trim().equals("Todos")
                            && !columna.trim().equals("Ninguno")
                            && (!value.trim().equals(""))) {
                        rp.add(Restrictions.ilike(columna.trim(), "%" + value
                                + "%"));
                    }
                }
            } else {
                if (!columna.trim().equals("Todos")
                        && !columna.trim().equals("Ninguno")
                        && (!value.trim().equals(""))) {
                    rp.add(Restrictions.ilike(columna.trim(), "%" + value + "%"));
                }
                rp.add(Restrictions.eq("empresa.id", idempresa));
            }

            ListFolios = rp.list();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException e) {
            hibManagerRO.getTransaction().rollback();
            e.printStackTrace(System.err);
        } finally {
            hibManagerRO.closeSession();
        }
        return ListFolios;
    }

    public boolean GuardarActualizarFolio(MFolios folio) {
        boolean val = false;
        try {
            hibManagerSU.initTransaction();
            hibManagerSU.getSession().saveOrUpdate(folio);
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

    public boolean BorrarFolio(MFolios folio) {
        boolean val = false;
        try {
            hibManagerSU.initTransaction();
            hibManagerSU.getSession().delete(folio);
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
}
