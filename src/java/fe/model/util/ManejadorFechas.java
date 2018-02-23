/**
 * 
 */
package fe.model.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @autor       Tyranitar amiranda@ebs.com.mx
 * @fecha       23/02/2011
 * @version     1.0
 * @descripcion [ Clase que contiene metodos para manejar fechas  ]
 */
public class ManejadorFechas {
	
	 
	/**
	 * Obtiene la fecha en formado diamesanio
	 * @return String Cadena con la fecha en formato predefinido
	 * */
	public static String obtenFecha() {
		Date fecha = new Date();
		SimpleDateFormat formato = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		String fechaHoy = formato.format(fecha);
		return fechaHoy;
	}
	
	/**
	 * Obtiene la fecha en formado anio - mes - dia  hora:minuto
	 * @return String Cadena con la fecha en formato predefinido
	 * */
	public static String obtenFechaRegistro() {
		Date fecha = new Date();
		SimpleDateFormat formato = new SimpleDateFormat("yyyyMMdd HH:mm");
		String fechaHoy = formato.format(fecha);
		return fechaHoy;
	}
	
	/**
	 * Obtiene la fecha en formado anio - mes - dia  hora:minuto
	 * @return String Cadena con la fecha en formato predefinido
	 * */
	public static String obtenFechaFormatoSAT() {
		Date fecha = new Date();
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		String fechaHoy = formato.format(fecha);
		return fechaHoy;
	}
	
	public static String obtenFechaFormatoISO() {
		Date fecha = new Date();
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fechaHoy = formato.format(fecha);
		return fechaHoy;
	}
	/**
	 * 
	 * */
	public static String obtenIdEvent(){
        Date fecha=new Date();
        SimpleDateFormat formato = new SimpleDateFormat("yyyyMddHHmmsssss");
        String fechaHoy=formato.format(fecha);
        return fechaHoy;
    }  
	
	/**
	 * Metodo encargado de convertir una cadena con formato DATE a un objeto Date
	 * @throws ParseException 
	 * 
	 * */
	public static Date convierteCadenaAdate(String cadena) throws ParseException{		
		Date fecha = null;		
		SimpleDateFormat formatoDeCadena = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		fecha = formatoDeCadena.parse(cadena);		
		return fecha;
	}
	
	public static Date obtenFechaactual() throws ParseException{
		return  convierteCadenaAdate(obtenFechaRegistro());
	}
	/**
	 * Obtiene la diferencia entre dos fechas
	 * @param fecha1: La fecha a validar
	 * @param fecha2: La fecha a validar
	 * @param opcion: Dato numerico que determina que tipo de retorno generar 1, Horas, 2 Minutos 
	 * @return String Cadena con la fecha en formato predefinido
	 * @throws Exception 
	 * */
	public static int obtenDiferencia(Date fecha1, Date fecha2, int opcion) throws Exception{
		   Date fechaMayor = null;
		   Date fechaMenor = null;
		   
		 
		   /* Verificamos cual es la mayor de las dos fechas, para no tener sorpresas al momento
		    * de realizar la resta.
		    */
		   if (fecha1.compareTo(fecha2) > 0){
		    fechaMayor = fecha1;
		    fechaMenor = fecha2;
		   }else{
		    fechaMayor = fecha2;
		    fechaMenor = fecha1;
		   }
		 
		  //los milisegundos
		   long diferenciaMils = fechaMayor.getTime() - fechaMenor.getTime();
		 
		   //obtenemos los segundos
		   long segundos = diferenciaMils / 1000;
		 
		   //obtenemos las horas
		   long horas = segundos / 3600;
		 
		   //obtenemos los dias
		   long dias = horas / 24;
		   //restamos las horas para continuar con minutos
		   segundos -= horas*3600;
		 
		   //igual que el paso anterior
		   long minutos = segundos /60;
		   segundos -= minutos*60;
		 
		   
		   if(opcion==1){
			   return Integer.parseInt((""+horas));
		   }else if(opcion==2){
			   return Integer.parseInt((""+minutos));
		   }else if(opcion==3){
			   return Integer.parseInt((""+dias));
		   }else{		   
			   throw new Exception("La opcion "+opcion+" en el metodo que obtiene diferiencia de fechas no esta definida");
		   }
		}
}