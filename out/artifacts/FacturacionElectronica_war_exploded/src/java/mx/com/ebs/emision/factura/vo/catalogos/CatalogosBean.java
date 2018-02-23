
package mx.com.ebs.emision.factura.vo.catalogos;

import java.io.Serializable;

/**
 * @author Amiranda
 * Clase que contiene la estructura basica para creacion de un catalogo/lista 
 *
 */
public class CatalogosBean implements Serializable{
	
	private String clave;
	private String descripcion;
	
	public CatalogosBean(){	}
	
	public CatalogosBean(String clave, String descripcion){
		this.clave = clave;
		this.descripcion = descripcion;
	}
	/**
	 * @return the clave
	 */
	public String getClave() {
		return clave;
	}
	/**
	 * @param clave the clave to set
	 */
	public void setClave(String clave) {
		this.clave = clave;
	}
	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
	

}
