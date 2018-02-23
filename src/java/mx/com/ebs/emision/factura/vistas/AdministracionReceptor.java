package mx.com.ebs.emision.factura.vistas;

import mx.com.ebs.emision.factura.catalogos.CatAdministracionDatos;
import mx.com.ebs.emision.factura.controladores.CapturaManualManejador;
import mx.com.ebs.emision.factura.utilierias.Limpiador;
import mx.com.ebs.emision.factura.utilierias.PintarLog;

import com.opensymphony.xwork2.ActionSupport;

import fe.db.MReceptor;

public class AdministracionReceptor extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
	
	public  CatAdministracionDatos catAdministracionDatos = new CatAdministracionDatos();
	public  String                 mensajeCorrecto        = "";
	public  String                 mensajeError           = "";
	private String                 receptorSeleccionado   ;
	private MReceptor              receptor               = new MReceptor();
	

	
	/**
	 * Metodo encargado de consultar los datos del receptor seleccionado
	 * @return String : succes o error , segun sea el caso
	 * */
	public String consultaReceptor() throws Exception{		
	// Iniciamos proceso de consulta de Receptores
		try{
			receptor = new CapturaManualManejador().consultaDireccionReceptor(Limpiador.cleanString(receptorSeleccionado));
		}catch(Exception e){
			PintarLog.println("Error al consultar la información del receptor. "+e);
			mensajeError = "Error al consultar la información del receptor. "+e;
			return ERROR;
			
		}		
		mensajeCorrecto="";
		return SUCCESS;
	}
		
	/**
	 * Metodo encargado de actualizar los datos del receptor seleccionado
	 * @return String : succes o error , segun sea el caso
	 * */
	public String actualizarReceptor() throws Exception{		
	// Iniciamos proceso de actualizacion de Receptores
		try{
			
			if(receptor!=null && !"".equalsIgnoreCase(Limpiador.cleanString(receptor.getRfcOrigen()))){
				MReceptor              receptorOriginal               = new MReceptor();
				receptorOriginal=new CapturaManualManejador().consultaDireccionReceptor(Limpiador.cleanString(receptorSeleccionado));
				receptorOriginal.setRazonSocial(Limpiador.cleanString(receptor.getRazonSocial()));
				receptorOriginal.setRfcOrigen(Limpiador.cleanString(receptor.getRfcOrigen()));
				receptorOriginal.setDireccion(receptor.getDireccion());
				new CapturaManualManejador().almacenaDatosReceptor(receptorOriginal);
			}
		}catch(Exception e){
			PintarLog.println("Error al actualizar la información del receptor. "+e);
			mensajeError = "Error al actualizar la información del receptor. "+e;
			return ERROR;			
		}		
		mensajeCorrecto="Se actualizo correctamente la información del proveedor ["+receptor.getRazonSocial()+"]";
		return SUCCESS;
	}


	/**
	 * @return the receptor
	 */
	public MReceptor getReceptor() {
		return receptor;
	}


	/**
	 * @param receptor the receptor to set
	 */
	public void setReceptor(MReceptor receptor) {
		this.receptor = receptor;
	}


	/**
	 * @return the receptorSeleccionado
	 */
	public String getReceptorSeleccionado() {
		return receptorSeleccionado;
	}


	/**
	 * @param receptorSeleccionado the receptorSeleccionado to set
	 */
	public void setReceptorSeleccionado(String receptorSeleccionado) {
		this.receptorSeleccionado = receptorSeleccionado;
	}
	
	
	
}
