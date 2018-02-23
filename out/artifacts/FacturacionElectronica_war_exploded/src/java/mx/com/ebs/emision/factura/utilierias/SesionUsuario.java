/**
 * 
 */
package mx.com.ebs.emision.factura.utilierias;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import fe.db.MAcceso;
import javax.faces.context.FacesContext;

/**
 * @author Tyranitar
 * Clase que contiene metodos encargados del manejo de los Objetos de la sessi�n, parametros y/o atributos
 */
public class SesionUsuario {
	private HttpSession         session                ;
	
	public SesionUsuario(){
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		setSession(request.getSession(true));	
	}

	
	/**
	 * @return the mAcceso
	 */
	public MAcceso getMAcceso() {		
		return (MAcceso) this.getSession().getAttribute("macceso");
	}

	/**
	 * @return the session
	 */
	protected HttpSession getSession() {
		return session;
	}

	/**
	 * @param session the session to set
	 */
	protected void setSession(HttpSession session) {
		this.session = session;
	}
	
	
	

}
