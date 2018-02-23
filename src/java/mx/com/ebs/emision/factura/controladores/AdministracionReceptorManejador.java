package mx.com.ebs.emision.factura.controladores;

import java.util.ArrayList;
import java.util.List;

import mx.com.ebs.emision.factura.utilierias.PintarLog;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import fe.db.MReceptor;
import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;

public class AdministracionReceptorManejador {
    private HibernateUtilApl hibManagerRO;
    public AdministracionReceptorManejador() {
        hibManagerRO = new HibernateUtilApl();//Read only pool
    }
	
     
	    
	    
	/**
	 * Metodo encargado de obtener todos los receptores que se encuentren registrados en 
	 * la tabla MReceptor
	 * @return List<MReceptor> : la lista de objetos MReceptor obtenida en la consulta
	 * */
	@SuppressWarnings("unchecked")
	public List<MReceptor> consultaReceptor() throws Exception{
		List<MReceptor> receptores = new ArrayList<MReceptor>(); 
		
		try{
	    	hibManagerRO.initTransaction();
	        Criteria crF = hibManagerRO.getSession().createCriteria(MReceptor.class);
	        crF.addOrder(Order.asc("razonSocial"));
	        receptores   = crF.list();	       
	        hibManagerRO.getTransaction().commit();
    	}catch(Exception e){
        	hibManagerRO.getTransaction().rollback();
        	PintarLog.println("Error al obtener la lista Receptores: "+e);
        }finally{
        	try{
	        	 hibManagerRO.closeSession();
	        }catch(Exception e){
	        	PintarLog.println("al cerrar la conexion", e);
	       	}
	  }
        return receptores;
    }		
	
	
	@SuppressWarnings("unchecked")
	public List<MReceptor> consultaReceptoresXEmpresa(int idempresa) throws Exception{
		List<MReceptor> receptores = new ArrayList<MReceptor>(); 
		
		try{
	    	hibManagerRO.initTransaction();
	        Criteria crF = hibManagerRO.getSession().createCriteria(MReceptor.class);
	        crF.add(Restrictions.eq("empresa.id",idempresa));
	        crF.addOrder(Order.asc("razonSocial"));	        
	        receptores   = crF.list();	       
	        hibManagerRO.getTransaction().commit();
    	}catch(Exception e){
        	hibManagerRO.getTransaction().rollback();
        	PintarLog.println("Error al obtener la lista Receptores: "+e);
        }finally{
        	try{
	        	 hibManagerRO.closeSession();
	        }catch(Exception e){
	        	PintarLog.println("al cerrar la conexion", e);
	       	}
	  }
        return receptores;
    }		
	


}
