package fe.model.dao;

import fe.db.MEmpresaMTimbre;
import fe.model.util.hibernateutil.HibernateUtilApl;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;

public class EmpresaTimbreDAO implements Serializable {

    private HibernateUtilApl hibManagerRO;//Read only pool

    public EmpresaTimbreDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface    
    }

    public MEmpresaMTimbre ObtenerClaveWSEmpresaTimbre(int idempresa) {
        MEmpresaMTimbre emptimbre = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MEmpresaMTimbre.class);
            cr.add(Restrictions.eq("empresa.id", idempresa));
            emptimbre = (MEmpresaMTimbre) cr.uniqueResult();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return emptimbre;
    }

    public String ObtenerClaveWSEmpresaTimbre(String rfcEmpresa) {
        String claveWs = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MEmpresaMTimbre.class);
            cr.add(Restrictions.eq("rfc", rfcEmpresa));

            MEmpresaMTimbre empresaTimbre = (MEmpresaMTimbre) cr.uniqueResult();
            if(empresaTimbre != null)
                claveWs = empresaTimbre.getClaveWS();

            hibManagerRO.getTransaction().commit();
        } catch (HibernateException ex) {
            hibManagerRO.getTransaction().rollback();
            ex.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }
        return claveWs;
    }
}
