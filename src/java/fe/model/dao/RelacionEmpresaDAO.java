package fe.model.dao;

import fe.model.util.hibernateutil.HibernateUtilEmi;
import org.hibernate.HibernateException;

import java.io.Serializable;

public class RelacionEmpresaDAO implements Serializable {

    private HibernateUtilEmi hibManagerSU;

    public RelacionEmpresaDAO() {
        hibManagerSU = new HibernateUtilEmi();
    }

    public boolean GuardarRelacionEmpresas(int empresa, int subempresa) {
        boolean val = false;
        try {
            String hql = "INSERT INTO M_RELACION_EMPRESAS (ID_SUBEMPRESA, ID_EMPRESA)  VALUE( :subempresa, :empresa) ";
            hibManagerSU.initTransaction();
            hibManagerSU.getSession().createSQLQuery(hql)
                    .setInteger("subempresa", subempresa)
                    .setInteger("empresa", empresa)
                    .executeUpdate();
            hibManagerSU.getTransaction().commit();
            val = true;
        } catch (HibernateException ex) {
            hibManagerSU.getTransaction().rollback();
            ex.printStackTrace();
            System.out.println("Error al iniciar la transaccion:" + ex.getMessage());
        } finally {
            hibManagerSU.closeSession();
        }
        return val;

    }

}
