package fe.model.dao;

import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;

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
