/**
 * 
 */
package mx.com.ebs.emision.factura.catalogos;

import mx.com.ebs.emision.factura.vo.catalogos.CatalogosBean;

/**
 * @author Amiranda
 *
 */
public class Catalogo {

	public CatalogosBean generaElementoLista(String clave, String descripcion){
		return new CatalogosBean(clave,descripcion);
	}

}
