package fe.model.dao;

import java.io.*;
import org.hibernate.*;
import org.hibernate.criterion.*;
import fe.db.*;
import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;
import java.util.*;

public class RetencionDAO {

    private HibernateUtilApl hibManagerRO;
    private HibernateUtilEmi hibManagerSU;
    public int rowCount = 0;

    public RetencionDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
        hibManagerSU = new HibernateUtilEmi();
    }

    public boolean GuardarActualizacionRetencionCFD(final McfdRetencion cfdi) {
        boolean val = false;
        try {
            hibManagerSU.initTransaction();
            hibManagerSU.getSession().saveOrUpdate((Object) cfdi);
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

    public boolean GuardarActualizacionRetencionXML(final MCfdXmlRetencion xml) {
        boolean val = false;
        try {
            hibManagerSU.initTransaction();
            hibManagerSU.getSession().saveOrUpdate((Object) xml);
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

    public McfdRetencion BuscarId(final int id) {
        McfdRetencion cfd = null;
        try {
            hibManagerRO.initTransaction();
            cfd = (McfdRetencion) hibManagerRO.getSession().get((Class) McfdRetencion.class, (Serializable) id);
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return cfd;
    }

    public List<McfdRetencion> BuscarUUID(final String uuid, final Integer empresa) {
        List<McfdRetencion> cfd = null;
        try {
            hibManagerRO.initTransaction();
            final Criteria cr = hibManagerRO.getSession().createCriteria((Class) McfdRetencion.class);
            cr.add((Criterion) Restrictions.eq("uuid", (Object) uuid));
            cr.add((Criterion) Restrictions.eq("MEmpresa.id", (Object) empresa));
            cr.add((Criterion) Restrictions.eq("edoDocumento", (Object) 1));
            cfd = (List<McfdRetencion>) cr.list();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return cfd;
    }

    public List<McfdRetencion> ListMcfd(final int idUser) {
        List<McfdRetencion> listacfd = null;
        try {
            hibManagerRO.initTransaction();
            final MAcceso usuario = (MAcceso) hibManagerRO.getSession().get((Class) MAcceso.class, (Serializable) idUser);
            if (usuario.getEmpresas() != null && !usuario.getEmpresas().isEmpty()) {
                final Integer[] idsemp = new Integer[usuario.getEmpresas().size()];
                for (int z = 0; z < usuario.getEmpresas().size(); ++z) {
                    idsemp[z] = usuario.getEmpresas().get(z).getId();
                }
                final Criteria cr = hibManagerRO.getSession().createCriteria((Class) McfdRetencion.class);
                cr.add(Restrictions.in("MEmpresa.id", (Object[]) idsemp));
                cr.addOrder(Order.desc("fecha"));
                cr.setMaxResults(300);
                listacfd = (List<McfdRetencion>) cr.list();
            }
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return listacfd;
    }

    public List<McfdRetencion> ListMcfdCliente(final int idUser) {
        List<McfdRetencion> listacfd = null;
        try {
            hibManagerRO.initTransaction();
            final MAcceso usuario = (MAcceso) hibManagerRO.getSession().get((Class) MAcceso.class, (Serializable) idUser);
            if (usuario.getCliente() != null && !usuario.getCliente().trim().equals("") && usuario.getEmpresas() != null && !usuario.getEmpresas().isEmpty()) {
                final Integer[] idsemp = new Integer[usuario.getEmpresas().size()];
                for (int z = 0; z < usuario.getEmpresas().size(); ++z) {
                    System.out.println("Empresa" + usuario.getEmpresas().get(z).getId());
                    idsemp[z] = usuario.getEmpresas().get(z).getId();
                }
                final Criteria cr = this.hibManagerRO.getSession().createCriteria((Class) McfdRetencion.class);
                cr.add(Restrictions.in("MEmpresa.id", (Object[]) idsemp));
                final Criteria motro = this.hibManagerRO.getSession().createCriteria((Class) MOtroRetencion.class);
                motro.add((Criterion) Restrictions.eq("param5", (Object) usuario.getCliente()));
                final List<MOtroRetencion> otros = (List<MOtroRetencion>) motro.list();
                if (otros != null && !otros.isEmpty()) {
                    System.out.println("ids otros");
                    final Integer[] idcfds = new Integer[otros.size()];
                    for (int z2 = 0; z2 < otros.size(); ++z2) {
                        idcfds[z2] = otros.get(z2).getMcfdRetencion().getId();
                    }
                    cr.add(Restrictions.in("id", (Object[]) idcfds));
                    cr.addOrder(Order.desc("fecha"));
                    cr.setMaxResults(300);
                } else {
                    System.out.println("No hay");
                    cr.setMaxResults(0);
                }
                listacfd = (List<McfdRetencion>) cr.list();
            }
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return listacfd;
    }

    public List<McfdRetencion> ListaParametros(final int idUser, final int idEmpresa, final String columna, final String value, Date fechaDesde, Date fechaHasta, final int estatus,
            int first, int pageSize) {
        List<McfdRetencion> listacfd = new ArrayList<McfdRetencion>();
        try {
            hibManagerRO.initTransaction();
            boolean estan = true;
            Criteria cr = hibManagerRO.getSession().createCriteria((Class) McfdRetencion.class);
            MAcceso usuario = (MAcceso) hibManagerRO.getSession().get((Class) MAcceso.class, (Serializable) idUser);
            String[] sector = null;
            if (usuario.getSector() != null && !"".equals(usuario.getSector().trim())) {
                sector = usuario.getSector().split(",");
            }
            if (idEmpresa <= 0) {
                if (usuario.getEmpresas() != null && !usuario.getEmpresas().isEmpty()) {
                    final Integer[] idsemp = new Integer[usuario.getEmpresas().size()];
                    for (int z = 0; z < usuario.getEmpresas().size(); ++z) {
                        idsemp[z] = usuario.getEmpresas().get(z).getId();
                    }
                    cr.add(Restrictions.in("MEmpresa.id", (Object[]) idsemp));
                }
            } else {
                System.out.println("MEmpresa. id " + idEmpresa);
                cr.add((Criterion) Restrictions.eq("MEmpresa.id", (Object) idEmpresa));
            }
            if (estatus >= 0) {
                cr.add((Criterion) Restrictions.eq("estadoDocumento", (Object) estatus));
            }
            boolean entroOt = false;
            final Criteria motroRetencion = hibManagerRO.getSession().createCriteria((Class) MOtroRetencion.class);
            if (sector != null && sector.length > 0) {
                motroRetencion.add(Restrictions.in("param7", (Object[]) sector));
                entroOt = true;
            }
            System.out.println("columna 1" + columna + " value" + value);
            if (columna != null && !columna.isEmpty() && !columna.trim().equals("-1") && value != null && !"".equals(value.trim())) {
                if (columna.contains("param")) {
                    System.out.println("columna 2" + columna + " value" + value);
                    motroRetencion.add((Criterion) Restrictions.eq(columna.trim(), (Object) value.trim()));
                    entroOt = true;
                } else {
                    System.out.println("columna 3" + columna + " value" + value);
                    cr.add(Restrictions.ilike(columna, (Object) ("%" + value + "%")));
                }
            }
            if (entroOt) {
                final List<MOtroRetencion> otros = (List<MOtroRetencion>) motroRetencion.list();
                if (otros != null && !otros.isEmpty()) {
                    final Integer[] idcfds = new Integer[otros.size()];
                    for (int z2 = 0; z2 < otros.size(); ++z2) {
                        idcfds[z2] = otros.get(z2).getMcfdRetencion().getId();
                    }
                    cr.add(Restrictions.in("id", (Object[]) idcfds));
                } else {
                    estan = false;
                }
            }
            if (fechaDesde != null && fechaHasta != null) {
                final Calendar dia = Calendar.getInstance();
                dia.setTime(fechaDesde);
                dia.set(10, 0);
                dia.set(12, 0);
                dia.set(13, 0);
                fechaDesde = dia.getTime();
                final Calendar diaF = Calendar.getInstance();
                diaF.setTime(fechaHasta);
                diaF.set(10, 23);
                diaF.set(12, 59);
                diaF.set(13, 59);
                fechaHasta = diaF.getTime();
                cr.add((Criterion) Expression.ge("fecha", (Object) fechaDesde));
                cr.add((Criterion) Expression.le("fecha", (Object) fechaHasta));
            }
            if (estan) {
                cr.addOrder(Order.desc("fecha"));
            }

            //paginacion
            cr.setProjection(Projections.rowCount());
            rowCount = (Integer) cr.uniqueResult();
            //Resets
            cr.setProjection(null);
            cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            //Pagination
            cr.setFirstResult(first);
            cr.setMaxResults(pageSize);

            listacfd = (List<McfdRetencion>) cr.list();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.out);
            System.out.println(ex.getMessage());
        } finally {
            hibManagerRO.closeSession();
        }
        return listacfd;
    }

    public List<McfdRetencion> ListaParametrosClientes(final int idUser, final int idEmpresa, final String columna, final String value, Date fechaDesde, Date fechaHasta, final String cliente,
            int first, int pageSize) {
        System.out.println("ENTER.........................");
        List<McfdRetencion> listacfd = null;
        try {
            System.out.println("cliente = " + cliente);
            if (cliente != null && !cliente.trim().equals("")) {
                hibManagerRO.initTransaction();
                boolean estan = true;
                final Criteria cr = hibManagerRO.getSession().createCriteria((Class) McfdRetencion.class);
                final MAcceso usuario = (MAcceso) hibManagerRO.getSession().get((Class) MAcceso.class, (Serializable) idUser);
                String[] sector = null;
                if (usuario.getSector() != null && !"".equals(usuario.getSector().trim())) {
                    sector = usuario.getSector().split(",");
                }
                if (idEmpresa <= 0) {
                    if (usuario.getEmpresas() != null && !usuario.getEmpresas().isEmpty()) {
                        final Integer[] idsemp = new Integer[usuario.getEmpresas().size()];
                        for (int z = 0; z < usuario.getEmpresas().size(); ++z) {
                            idsemp[z] = usuario.getEmpresas().get(z).getId();
                        }
                        cr.add(Restrictions.in("MEmpresa.id", (Object[]) idsemp));
                    }
                } else {
                    cr.add((Criterion) Restrictions.eq("MEmpresa.id", (Object) idEmpresa));
                }
                final Criteria motro = hibManagerRO.getSession().createCriteria((Class) MOtroRetencion.class);
                motro.add((Criterion) Restrictions.eq("param5", (Object) cliente));
                if (sector != null && sector.length > 0) {
                    motro.add(Restrictions.in("param7", (Object[]) sector));
                }
                if (columna != null && !columna.isEmpty() && !columna.trim().equals("-1") && value != null && !"".equals(value.trim())) {
                    if (columna.contains("param")) {
                        motro.add((Criterion) Restrictions.eq(columna.trim(), (Object) value.trim()));
                    } else {
                        cr.add(Restrictions.ilike(columna, (Object) ("%" + value + "%")));
                    }
                }
                final List<MOtroRetencion> otros = (List<MOtroRetencion>) motro.list();
                if (otros != null && !otros.isEmpty()) {
                    final Integer[] idcfds = new Integer[otros.size()];
                    for (int z2 = 0; z2 < otros.size(); ++z2) {
                        idcfds[z2] = otros.get(z2).getMcfdRetencion().getId();
                    }
                    cr.add(Restrictions.in("id", (Object[]) idcfds));
                } else {
                    estan = false;
                }
                if (fechaDesde != null && fechaHasta != null) {
                    final Calendar dia = Calendar.getInstance();
                    dia.setTime(fechaDesde);
                    dia.set(10, 0);
                    dia.set(12, 0);
                    dia.set(13, 0);
                    fechaDesde = dia.getTime();
                    final Calendar diaF = Calendar.getInstance();
                    diaF.setTime(fechaHasta);
                    diaF.set(10, 23);
                    diaF.set(12, 59);
                    diaF.set(13, 59);
                    fechaHasta = diaF.getTime();
                    cr.add((Criterion) Expression.ge("fecha", (Object) fechaDesde));
                    cr.add((Criterion) Expression.le("fecha", (Object) fechaHasta));
                }
                if (estan) {
                    cr.addOrder(Order.desc("fecha"));
                }

                //paginacion
                cr.setProjection(Projections.rowCount());
                rowCount = (Integer) cr.uniqueResult();
                //Reset
                cr.setProjection(null);
                cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
                //Pagination
                cr.setFirstResult(first);
                cr.setMaxResults(pageSize);

                listacfd = (List<McfdRetencion>) cr.list();
                hibManagerRO.getTransaction().commit();
            }
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return listacfd;
    }

    public List<McfdRetencion> ListaParametrosExportR(final int idUser, final int idEmpresa, final String columna, final String value, Date fechaDesde, Date fechaHasta, final int estatus) {
        List<McfdRetencion> listacfd = null;
        try {
            hibManagerRO.initTransaction();
            boolean estan = true;
            final Criteria cr = hibManagerRO.getSession().createCriteria((Class) McfdRetencion.class);
            final MAcceso usuario = (MAcceso) hibManagerRO.getSession().get((Class) MAcceso.class, (Serializable) idUser);
            String[] sector = null;
            if (usuario.getSector() != null && !"".equals(usuario.getSector().trim())) {
                sector = usuario.getSector().split(",");
            }
            if (idEmpresa <= 0) {
                if (usuario.getEmpresas() != null && !usuario.getEmpresas().isEmpty()) {
                    final Integer[] idsemp = new Integer[usuario.getEmpresas().size()];
                    for (int z = 0; z < usuario.getEmpresas().size(); ++z) {
                        idsemp[z] = usuario.getEmpresas().get(z).getId();
                    }
                    cr.add(Restrictions.in("MEmpresa.id", (Object[]) idsemp));
                }
            } else {
                cr.add((Criterion) Restrictions.eq("MEmpresa.id", (Object) idEmpresa));
            }
            if (estatus >= 0) {
                cr.add((Criterion) Restrictions.eq("estadoDocumento", (Object) estatus));
            }
            boolean entroOt = false;
            Criteria motro = hibManagerRO.getSession().createCriteria((Class) MOtroRetencion.class);
            if (sector != null && sector.length > 0) {
                motro.add(Restrictions.in("param7", (Object[]) sector));
                entroOt = true;
            }
            if (columna != null && !columna.isEmpty() && !columna.trim().equals("-1") && value != null && !"".equals(value.trim())) {
                if (columna.contains("param")) {
                    entroOt = true;
                    motro.add((Criterion) Restrictions.eq(columna.trim(), (Object) value.trim()));
                } else {
                    cr.add(Restrictions.ilike(columna, (Object) ("%" + value + "%")));
                }
            }
            if (entroOt) {
                final List<MOtroRetencion> otros = (List<MOtroRetencion>) motro.list();
                if (otros != null && !otros.isEmpty()) {
                    final Integer[] idcfds = new Integer[otros.size()];
                    for (int z2 = 0; z2 < otros.size(); ++z2) {
                        idcfds[z2] = otros.get(z2).getMcfdRetencion().getId();
                    }
                    cr.add(Restrictions.in("id", (Object[]) idcfds));
                } else {
                    estan = false;
                    cr.setMaxResults(0);
                }
            } else {
                motro = null;
            }
            if (fechaDesde != null && fechaHasta != null) {
                final Calendar dia = Calendar.getInstance();
                dia.setTime(fechaDesde);
                dia.set(10, 0);
                dia.set(12, 0);
                dia.set(13, 0);
                fechaDesde = dia.getTime();
                final Calendar diaF = Calendar.getInstance();
                diaF.setTime(fechaHasta);
                diaF.set(10, 23);
                diaF.set(12, 59);
                diaF.set(13, 59);
                fechaHasta = diaF.getTime();
                cr.add((Criterion) Expression.ge("fecha", (Object) fechaDesde));
                cr.add((Criterion) Expression.le("fecha", (Object) fechaHasta));
            }
            if (estan) {
                cr.addOrder(Order.desc("fecha"));
            } else {
                cr.setMaxResults(0);
            }
            listacfd = (List<McfdRetencion>) cr.list();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return listacfd;
    }

    public List<McfdRetencion> ListaSelec(final Integer[] ids) {
        List<McfdRetencion> cfdis = null;
        try {
            hibManagerRO.initTransaction();
            final Criteria cr = hibManagerRO.getSession().createCriteria((Class) McfdRetencion.class);
            cr.add(Restrictions.in("id", (Object[]) ids));
            cfdis = (List<McfdRetencion>) cr.list();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.out);
        } finally {
            hibManagerRO.closeSession();
        }
        return cfdis;
    }

    public MOtroRetencion Otro(final int id) {
        MOtroRetencion otro = null;
        try {
            hibManagerRO.initTransaction();
            final Criteria cr = hibManagerRO.getSession().createCriteria((Class) MOtroRetencion.class);
            cr.add((Criterion) Restrictions.eq("mcfdRetencion.id", (Object) id));
            otro = (MOtroRetencion) cr.uniqueResult();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace(System.out);
        } finally {
            hibManagerRO.closeSession();
        }
        return otro;
    }

    public List<MapearRetencionArchi> ListaArchivos(final List<McfdRetencion> cfds) {
        final List<MapearRetencionArchi> mcfdRetencion = new ArrayList<MapearRetencionArchi>();
        if (cfds != null && cfds.size() > 0) {
            try {
                for (int i = 0; i < cfds.size(); ++i) {
                    MapearRetencionArchi map = new MapearRetencionArchi();
                    map.setRetencion(cfds.get(i));
                    mcfdRetencion.add(map);
                    map = null;
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
            }
        }
        return mcfdRetencion;
    }

    public Map<Integer, Object> getObjectsMap(final int IdRetencion) {
        final Map<Integer, Object> obj = new HashMap<Integer, Object>();
        try {
            hibManagerRO.initTransaction();
            final McfdRetencion cfd = (McfdRetencion) hibManagerRO.getSession().get((Class) McfdRetencion.class, (Serializable) IdRetencion);
            obj.put(1, cfd.getMEmpresa());
            obj.put(2, cfd.getMPlantilla());
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return obj;
    }
}
