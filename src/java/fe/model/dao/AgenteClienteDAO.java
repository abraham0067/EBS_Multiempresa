package fe.model.dao;

import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;
import org.hibernate.HibernateException;
import org.hibernate.Query;

import java.util.List;

public class AgenteClienteDAO {

    private HibernateUtilApl hibManagerRO;
    private HibernateUtilEmi hibManagerSU;

    public AgenteClienteDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
        hibManagerSU = new HibernateUtilEmi();
    }

    public Integer BuscarAgente(int idCliente) {
        Integer agente = null;
        try {
            hibManagerRO.initTransaction();
            String sql = "SELECT m.agente  FROM MAgenteCliente m WHERE m.accesoAgenteId =:idCliente";
            Query cfd =  hibManagerRO.getSession().createQuery(sql);
            cfd.setParameter("idCliente", idCliente);
            cfd.setMaxResults(1);

            agente = (Integer) cfd.uniqueResult();

            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return agente;
    }

    public List<Integer> BuscarClientesDeAgente(int idCliente) {
        List<Integer> agente = null;
        try {
            hibManagerRO.initTransaction();
            String sql = "SELECT m.agente  FROM MAgenteCliente m WHERE m.accesoAgenteId =:idCliente";
            Query cfd =  hibManagerRO.getSession().createQuery(sql);
            cfd.setParameter("idCliente", idCliente);

            agente = cfd.list();

            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return agente;
    }

}
