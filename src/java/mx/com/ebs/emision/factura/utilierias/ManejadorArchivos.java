/**
 * 
 */
package mx.com.ebs.emision.factura.utilierias;

import java.io.File;

/**
 * @author Tyranitar
 *
 */
public class ManejadorArchivos {
	
	public static void main(String loquesea[]){
		String path = "D:\\Emision_Facturas\\Librerias\\librerias en WebLogicInteraciones\\";
		File directorio = new File(path);
		String [] ficheros = directorio.list();
		
		for (int i = 0; i < ficheros.length; i++) {
		    try {
		        System.out.println("" + ficheros[i]);
		       
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}
		System.out.println("test");
	}

}
