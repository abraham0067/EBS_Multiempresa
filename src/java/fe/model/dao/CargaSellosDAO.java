package fe.model.dao;

import java.text.SimpleDateFormat;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import fe.db.MConfig;
import fe.model.util.hibernateutil.HibernateUtilApl;
import java.io.Serializable;

public class CargaSellosDAO implements Serializable {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private HibernateUtilApl hibManagerRO;

    public CargaSellosDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
    }

    @SuppressWarnings("unchecked")
    public List<MConfig> ObtenerListaDatos() {
        List<MConfig> listaParam = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MConfig.class);
            cr.add(Restrictions.ilike("clasificacion", "SELLOS"));
            listaParam = cr.list();
            hibManagerRO.getTransaction().commit();
        } catch (HibernateException e) {
            hibManagerRO.getTransaction().rollback();
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            hibManagerRO.closeSession();
        }

        return listaParam;

    }

}
