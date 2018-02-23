/**
 * 
 */
package mx.com.ebs.emision.factura.catalogos;

import java.util.ArrayList;
import java.util.List;

import mx.com.ebs.emision.factura.utilierias.PintarLog;
import mx.com.ebs.emision.factura.utilierias.SesionUsuario;
import fe.db.MEmpresa;
import fe.model.dao.EmpresaDAO;

/**
 * @author Tyranitar
 * Consulta las socideades ligadas al usuario
 */
public class CatConsultaFacturas {
	private List<MEmpresa>      listSociedades = new ArrayList<MEmpresa>();  ;       // lista de Emisores tomados de la base de datos
	private SesionUsuario       sesionUsuario ;	
	public CatConsultaFacturas(){		
	sesionUsuario  = new SesionUsuario();
	EmpresaDAO DAOEmp= new EmpresaDAO();
		try {
			setListSociedades(DAOEmp.ListaEmpresasPadres(sesionUsuario.getMAcceso().getId()));
		} catch (Exception e) {
			PintarLog.println("Error al consultar las empresas del perfil----: "+e);
		}   	    
	}
	
	/**
	 * @return the listSociedades
	 */
	public List<MEmpresa> getListSociedades() {
		if(this.listSociedades==null)listSociedades= new ArrayList<MEmpresa>(); 
		return listSociedades;
	}

	/**
	 * @param listSociedades the listSociedades to set
	 */
	public void setListSociedades(List<MEmpresa> listSociedades) {
		this.listSociedades = listSociedades;
	}
	
	
}
