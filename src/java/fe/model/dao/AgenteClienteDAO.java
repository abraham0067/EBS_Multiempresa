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

    public String BuscarAgente(int idCliente) {
        String agente = null;
        try {
            hibManagerRO.initTransaction();
            String sql = "SELECT m.agente  FROM MAgenteCliente m WHERE m.mAccesoByAccesoAgenteId.id =:idCliente";
            Query cfd =  hibManagerRO.getSession().createQuery(sql);
            cfd.setParameter("idCliente", idCliente);
            cfd.setMaxResults(1);

            agente = (String)cfd.uniqueResult();

            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return agente;
    }

    public List<String> BuscarClientesDeAgente(String idAgente) {
        List<String> noCliente = null;
        try {
            hibManagerRO.initTransaction();
            String sql = "SELECT m.numeroCliente FROM MAgenteCliente m WHERE m.agente =:idAgente";
            Query cfd =  hibManagerRO.getSession().createQuery(sql);
            cfd.setParameter("idAgente", idAgente);

            noCliente = cfd.list();

            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return noCliente;
    }

}
