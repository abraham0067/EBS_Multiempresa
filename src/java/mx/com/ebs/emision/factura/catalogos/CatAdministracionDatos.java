/**
 * 
 */
package mx.com.ebs.emision.factura.catalogos;

import java.util.ArrayList;
import java.util.List;

import fe.db.MReceptor;
import mx.com.ebs.emision.factura.controladores.AdministracionReceptorManejador;
import mx.com.ebs.emision.factura.utilierias.PintarLog;
import mx.com.ebs.emision.factura.utilierias.SesionUsuario;

/**
 * @author Tyranitar
 *
 */
public class CatAdministracionDatos {
	public List<MReceptor>        catReceptor            = new ArrayList<MReceptor>();
	private SesionUsuario       sesionUsuario  = new SesionUsuario();
	/**
	 * Constructor encargado de inicializar los catalogos que se alojan en esta clase
	 */
	public CatAdministracionDatos(){
		try {
			catReceptor =  new AdministracionReceptorManejador().consultaReceptor();
		} catch (Exception e) {
			PintarLog.println(e);
		}
	}


	/**
	 * @return the catReceptor
	 */
	public List<MReceptor> getCatReceptor() {
		return catReceptor;
	}


	/**
	 * @param catReceptor the catReceptor to set
	 */
	public void setCatReceptor(List<MReceptor> catReceptor) {
		this.catReceptor = catReceptor;
	}
	
	
}
