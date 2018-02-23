package fe.model.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import fe.db.EmpresaTimbre_Timbre;
import fe.db.MEmpresaMTimbre;
import fe.db.MTimbre;
import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;
import java.io.Serializable;

public class TimbreDAO implements Serializable{
    
    private HibernateUtilApl hibManagerRO;
    private HibernateUtilEmi hibManagerSU;

    public TimbreDAO() {
        hibManagerRO = new HibernateUtilApl();//Read only interface
        hibManagerSU = new HibernateUtilEmi();
    }


	public boolean GuardarActualizaEmpTim(MEmpresaMTimbre empresaTimbre) {
		boolean valor = true;
		try {
			hibManagerSU.initTransaction();
			hibManagerSU.getSession().saveOrUpdate(empresaTimbre);
			hibManagerSU.getTransaction().commit();
		} catch (HibernateException ex) {
			valor = false;
			hibManagerSU.getTransaction().rollback();
			ex.printStackTrace(System.err);
		} finally {
			hibManagerSU.closeSession();
		}

		return valor;
	}

	public boolean GuardarActualizaTimbre(MTimbre Timbre) {
		boolean valor = true;
		try {
			hibManagerSU.initTransaction();
			hibManagerSU.getSession().saveOrUpdate(Timbre);
			hibManagerSU.getTransaction().commit();
		} catch (HibernateException ex) {
			valor = false;
			hibManagerSU.getTransaction().rollback();
			ex.printStackTrace(System.err);
		} finally {
			hibManagerSU.closeSession();
		}

		return valor;
	}

	public boolean BorrarTimbre(MTimbre Timbre) {
		boolean valor = true;
		try {
			hibManagerSU.initTransaction();
			hibManagerSU.getSession().delete(Timbre);
			hibManagerSU.getTransaction().commit();
		} catch (HibernateException ex) {
			valor = false;
			hibManagerSU.getTransaction().rollback();
			ex.printStackTrace(System.err);
		} finally {
			hibManagerSU.closeSession();
		}

		return valor;
	}

	public boolean BorrarEmpresaTimbre(MEmpresaMTimbre EmpresaTimbre) {
		boolean valor = true;
		try {
			hibManagerSU.initTransaction();
			hibManagerSU.getSession().delete(EmpresaTimbre);
			hibManagerSU.getTransaction().commit();
		} catch (HibernateException ex) {
			valor = false;
			hibManagerSU.getTransaction().rollback();
			ex.printStackTrace(System.err);
		} finally {
			hibManagerSU.closeSession();
		}

		return valor;
	}

	@SuppressWarnings("unchecked")
	public List<EmpresaTimbre_Timbre> ListaTimbrePorEmpresa(int idempresa) {
		List<EmpresaTimbre_Timbre> empresaTimbre_Timbres = new ArrayList<EmpresaTimbre_Timbre>();

		try {
			hibManagerRO.initTransaction();
			Criteria cr = hibManagerRO.getSession().createCriteria(MEmpresaMTimbre.class);
			cr.add(Restrictions.eq("empresa.id", idempresa));
			List<MEmpresaMTimbre> listaEmpTimp = cr.list();
			if (listaEmpTimp != null && !listaEmpTimp.isEmpty()) {
				for (int i = 0; i < listaEmpTimp.size(); i++) {
					Criteria cr2 = hibManagerRO.getSession().createCriteria(MTimbre.class);
					cr2.add(Restrictions.eq("empresatimbre",listaEmpTimp.get(i)));
					List<MTimbre> timbres = null;
					timbres = cr2.list();
					empresaTimbre_Timbres.add(new EmpresaTimbre_Timbre(listaEmpTimp.get(i), timbres));
				}
			}
			hibManagerRO.getTransaction().commit();
		} catch (HibernateException ex) {
			hibManagerRO.getTransaction().rollback();
			ex.printStackTrace(System.err);
		} finally {
			hibManagerRO.closeSession();
		}

		return empresaTimbre_Timbres;
	}

	public MTimbre BuscarTimbreID(long idtimbre) {
		MTimbre timbre = null;
		try {
			hibManagerRO.initTransaction();
			timbre = (MTimbre) hibManagerRO.getSession().get(MTimbre.class, idtimbre);
			hibManagerRO.getTransaction().commit();
		} catch (HibernateException ex) {
			hibManagerRO.getTransaction().rollback();
			ex.printStackTrace(System.err);
		} finally {
			hibManagerRO.closeSession();
		}
		return timbre;
	}

	public MEmpresaMTimbre BuscarEmpresaTimbreID(long idemptimbre) {
		MEmpresaMTimbre Emptimbre = null;
		try {
			hibManagerRO.initTransaction();
			Emptimbre = (MEmpresaMTimbre) hibManagerRO.getSession().get(MEmpresaMTimbre.class,
					idemptimbre);
			hibManagerRO.getTransaction().commit();
		} catch (HibernateException ex) {
			hibManagerRO.getTransaction().rollback();
			ex.printStackTrace(System.err);
		} finally {
			hibManagerRO.closeSession();
		}
		return Emptimbre;
	}

}
