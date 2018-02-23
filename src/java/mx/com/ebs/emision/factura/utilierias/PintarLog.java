package mx.com.ebs.emision.factura.utilierias;

/**
 * @autor       Amiranda amiranda@ebs.com.mx
 * @fecha       14/02/2011
 * @version     1.0
 * @descripcion [ Clase encargada de Pintar Mensajes Log dentro del Servidor   ]
 */
public class PintarLog {
 	
	/**
	 * Metodo encargado de pintar traces en el Log de
	 * la aplicacion
	 * @param msg : Mensaje a pintar en el log
	 * */
	public static void println(String msg){		
		System.out.println("Emision Facturas [ "+msg+" ]");		
	}
	
	/**
	 * Metodo encargado de pintar Excepciones en el Log de
	 * la aplicacion.
	 * @param Exception  mensage de excepcion a pintar en el log
	 * @throws Exception 
	 * */
	public static void println(Exception e) {		
		System.out.println("Emision Facturas---  Error... [ "+e.getLocalizedMessage()+" ]");		
	}
	
	/**
	 * Metodo encargado de pintar Excepciones en el Log de
	 * la aplicacion.
	 * @param Exception  mensage de excepcion a pintar en el log
	 * @throws Exception 
	 * */
	public static void println(String mensaje,Exception ex) {		
		System.out.println("Emision Facturas Error---  "+mensaje+"... [ "+ex.getLocalizedMessage()+" ]");		
	}
}