package fe.model.util;


/**
 * @autor       Tyranitar amiranda@ebs.com.mx
 * @fecha       14/02/2011
 * @version     1.0
 * @descripcion [ Clase que contiene rutinas para validar informacion de variables    ]
 */
public class Limpiador {
	 
	/** Metodo encargado de limpiar cadenas */
	public static String cleanString(String string) {
		if (string == null || "null".equalsIgnoreCase(string)) {
			string = "";
		}
		return string.trim();
	}
 
	/** Metodo encargado de limpiar cadenas que contienen caracteres de tipo int 
	 * @throws Exception */
	public static int cleanStringToIntegers(String string, String nameField) throws Exception {
		int number = -1;

		if (string == null || "null".equalsIgnoreCase(string)
				|| "".equalsIgnoreCase(string)) {
			string = "0";

		}
		try {
			number = Integer.parseInt(string.trim());
		} catch (Exception e) {
			throw new Exception ("Al convertir a entero, el valor ["+string+"] del campo: "+nameField+" ");
		}
		return number;
	}
	
	/** Metodo encargado de limpiar cadenas que contienen caracteres de tipo int 
	 * @throws Exception */
	public static int cleanStringToIntegers(String string) throws Exception {
		int number = -1;

		if (string == null || "null".equalsIgnoreCase(string)
				|| "".equalsIgnoreCase(string)) {
			string = "0";

		}
		try {
			number = Integer.parseInt(string.trim());
		} catch (Exception e) {
			throw new Exception ("Al convertir a entero, el valor ["+string+"]");
		}
		return number;
	}

	/**
	 * Metodo encargado de limpiar cadenas que contienen caracteres de tipo
	 * double
	 */
	public static double cleanStringToDoubles(String string) {
		if (string == null || "null".equalsIgnoreCase(string)
				|| "".equalsIgnoreCase(string)) {
			string = "0.0";

		}
		return Double.parseDouble(string.trim());
	}
}
