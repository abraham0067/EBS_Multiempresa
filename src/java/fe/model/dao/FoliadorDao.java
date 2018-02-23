package fe.model.dao;

import fe.db.MCfd;
import fe.db.MEmpresa;
import fe.db.MFolios;
import fe.db.MLlave;
import fe.db.MLogFE;
import fe.db.MPlantilla;
import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;
import lombok.Synchronized;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import java.util.List;

/**
 * Created by eflores on 07/11/2017.
 * Dao para obtener datos de timbrado directo
 */
public class FoliadorDao {
    private static HibernateUtilApl hibManagerRO;
    private static HibernateUtilEmi hibManagerSU;

    public FoliadorDao() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
        hibManagerSU = new HibernateUtilEmi();
    }
    /**
     * Salvar un registro en la base de datos
     *
     * @param obj
     */
    public Object saveNewObject(Object obj) {
        try {
            hibManagerSU.initTransaction();
            hibManagerSU.getSession().saveOrUpdate(obj);
            hibManagerSU.getTransaction().commit();
        } catch (Exception e) {
            hibManagerSU.getTransaction().rollback();
            e.printStackTrace();
        }
        return obj;

    }
}
