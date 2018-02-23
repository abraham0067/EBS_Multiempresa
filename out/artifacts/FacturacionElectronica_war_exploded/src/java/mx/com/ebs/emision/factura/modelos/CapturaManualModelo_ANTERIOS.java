/**
 * 
 */
package mx.com.ebs.emision.factura.modelos;

import mx.com.ebs.emision.factura.catalogos.SelectCapturaManual;
import mx.com.ebs.emision.factura.utilierias.Limpiador;
import mx.com.ebs.emision.factura.utilierias.ManejadorFechas;
import fe.db.MEmpresa;
import fe.db.MReceptor;
import fe.sat.AdditionalData;
import fe.sat.CommentFEData;
import fe.sat.ComprobanteData;
import fe.sat.Direccion12Data;
import fe.sat.DireccionFiscal12Data;
import fe.sat.Emisor12Data;
import fe.sat.Receptor12Data;
import fe.sat.RetencionData;
import fe.sat.TrasladoData;

/**
 * @author Amiranda
 * Modelo de la forma captura Manual
 */
public class CapturaManualModelo_ANTERIOS {
// ============== TRASLADOS IVA ===========================
	private String trasladoImporteIVA ="0.0";
// ========================================================	

// ============== TRASLADOS IEPS ==========================
	private String trasladoTasaISR    ="0.0";
	private String trasladoImporteISR ="0.0";
// ========================================================	
	
// ============== RETENCIONES IVA =========================
	private String retencionImporteIVA ="0.0";
// ========================================================	

// ============== RETENCIONES ISR =========================
	private String retencionImporteISR ="0.0";
// ========================================================	

	
	/**
	 * Metodo encargado de cargar los datos faltanes del comprobante
	 * */
	public ComprobanteData cargaDatosComprobante(ComprobanteData comprobanteData, SelectCapturaManual selecionCapturaManual, MEmpresa emisor,MReceptor receptor,String comentarios, String comentariosAddenda){
		//if(emisor.getDireccion()==null)
			
		//==================================== CARGA DE COMENTARIOS ======================================================
		CommentFEData[] arrComentarios = new CommentFEData[1];
		CommentFEData       comentario = new CommentFEData();		             
		comentario.setComment(comentarios);
		comentario.setPosition("9999");
		             arrComentarios[0] = comentario;
		comprobanteData.setComments9999(arrComentarios);
		//================================================================================================================
		//==================================== CARGA DE INFORMACION ADICIONAL ============================================
		comprobanteData.getDatosComprobante().setFormaDePago  (selecionCapturaManual.getMetodoPago());// Checar esta inconguencia		
		comprobanteData.getDatosComprobante().setTipoDocumento(selecionCapturaManual.getTipoDocumento());
		comprobanteData.getDatosComprobante().setSerie        (selecionCapturaManual.getSerie());
		comprobanteData.getDatosComprobante().setMoneda       (selecionCapturaManual.getMoneda());
		comprobanteData.getDatosComprobante().setDescuento    (0.0);// se manda el valor no la seleccion
		// ===============================================================================================================
		
		// ================================== CARGA DE TRASLADOS =========================================================
		TrasladoData[] ArrTrasladoData = new TrasladoData[2];
		TrasladoData   trasladosIVA    = new TrasladoData();
		TrasladoData   trasladosISR    = new TrasladoData();
		
		
		trasladosIVA.setDescripcionImpuesto("IVA");
		trasladosIVA.setTasa(Limpiador.cleanStringToDoubles(selecionCapturaManual.getTasa()));
		trasladosIVA.setImporteImpuesto(Limpiador.cleanStringToDoubles(trasladoImporteIVA));
		
		trasladosISR.setDescripcionImpuesto("IEPS");
		trasladosISR.setTasa(Limpiador.cleanStringToDoubles(trasladoTasaISR));
		trasladosISR.setImporteImpuesto(Limpiador.cleanStringToDoubles(trasladoImporteISR));
		
		ArrTrasladoData[0]=trasladosIVA;
		ArrTrasladoData[1]=trasladosISR;
		
		comprobanteData.getImpuestos().setTraslados(ArrTrasladoData);
		// =================== ================================================= =========================================
		
		// ======================================================== CARGA DE RETENCIONES =======================================================
		RetencionData[] arrRetencionData = new RetencionData[2]; 
		RetencionData   retencionesIVA   = new RetencionData();
		RetencionData   retencionesISR   = new RetencionData();
		
		
		retencionesIVA.setDescripcionImpuesto("IVA");
		retencionesIVA.setImporteImpuesto(Limpiador.cleanStringToDoubles(retencionImporteIVA));
		
		retencionesISR.setDescripcionImpuesto("ISR");
		retencionesISR.setImporteImpuesto(Limpiador.cleanStringToDoubles(retencionImporteISR));
		
		arrRetencionData[0]=retencionesIVA;
		arrRetencionData[1]=retencionesISR;
		
		comprobanteData.getImpuestos().setRetenciones(arrRetencionData);
		// ===============================================================================================================================================================================
		
		// =================== Agragando datos que no se capturan en la pantalla =========================================================================================================		
		
		// ======================================  1---- Datos del Emisor  ===============================================================================================================
		Emisor12Data emisor12Data = new Emisor12Data();
		DireccionFiscal12Data direccionFiscal12 = new  DireccionFiscal12Data();
		direccionFiscal12.setCalle(Limpiador.cleanString(emisor.getDireccion().getCalle()));
		direccionFiscal12.setColonia(Limpiador.cleanString(emisor.getDireccion().getColonia()));
		direccionFiscal12.setCP(Limpiador.cleanString(emisor.getDireccion().getCp()));
		direccionFiscal12.setEstado(Limpiador.cleanString(emisor.getDireccion().getEstado()));
		direccionFiscal12.setLocalidad(Limpiador.cleanString(emisor.getDireccion().getLocalidad()));
		direccionFiscal12.setMunicipio(Limpiador.cleanString(emisor.getDireccion().getMunicipio()));
		direccionFiscal12.setNoExterior(Limpiador.cleanString(emisor.getDireccion().getNoExterior()));
		direccionFiscal12.setNoInterior(Limpiador.cleanString(emisor.getDireccion().getNoInterior()));
		direccionFiscal12.setPais(Limpiador.cleanString(emisor.getDireccion().getPais()));
		direccionFiscal12.setReferencia("Ninguna");
		emisor12Data.setRFCEmisor(Limpiador.cleanString(emisor.getRfcOrigen()));
		emisor12Data.setRegimen(new String[]{"Régimen General de Ley Personas Morales"});
		String[] llaveCompuestaEmisor = selecionCapturaManual.getEmisor().split("\\|");	
		emisor12Data.setNombreEmisor(Limpiador.cleanString(llaveCompuestaEmisor[1]));// <---------------- Se carga el nombre del emisor
		emisor12Data.setDireccionFiscalEmisor(direccionFiscal12);
		comprobanteData.setEmisor(emisor12Data);
		// ===============================================================================================================================================================================
		
		// ======================================  1---- Datos del Receptor  ===============================================================================================================
		Receptor12Data receptor12Data = new Receptor12Data();
		Direccion12Data direccionReceptorFiscal12 = new  Direccion12Data();
		direccionReceptorFiscal12.setCalle(Limpiador.cleanString(receptor.getDireccion().getCalle()));
		direccionReceptorFiscal12.setColonia(Limpiador.cleanString(receptor.getDireccion().getColonia()));
		direccionReceptorFiscal12.setCP(Limpiador.cleanString(receptor.getDireccion().getCp()));
		direccionReceptorFiscal12.setEstado(Limpiador.cleanString(receptor.getDireccion().getEstado()));
		direccionReceptorFiscal12.setLocalidad(Limpiador.cleanString(receptor.getDireccion().getLocalidad()));
		direccionReceptorFiscal12.setMunicipio(Limpiador.cleanString(receptor.getDireccion().getMunicipio()));
		direccionReceptorFiscal12.setNoExterior(Limpiador.cleanString(receptor.getDireccion().getNoExterior()));
		direccionReceptorFiscal12.setNoInterior(Limpiador.cleanString(receptor.getDireccion().getNoInterior()));
		direccionReceptorFiscal12.setPais(Limpiador.cleanString(receptor.getDireccion().getPais()));
		direccionReceptorFiscal12.setReferencia("Ninguna");
		receptor12Data.setRFCReceptor(Limpiador.cleanString(receptor.getRfcOrigen()));				
		receptor12Data.setNombreReceptor(Limpiador.cleanString(receptor.getRazonSocial()));// <---------------- Se carga el nombre del emisor
		receptor12Data.setDireccionReceptor(direccionReceptorFiscal12);
		comprobanteData.setReceptor(receptor12Data);
		// ===============================================================================================================================================================================
		
		// =========================================== Datos dummy para que las validaciones iniciales del proceso de captura manual, no genere errores de datos==========================
		comprobanteData.getDatosComprobante().setFolio("1");
		comprobanteData.getDatosComprobante().setFecha(ManejadorFechas.obtenFechaFormatoISO());
		comprobanteData.getDatosComprobante().setSello("qQSvHZPzRB7eT6f9dJs3GepVl82eq4aOiI4b2hK+YSTcaJXgrDm8GfmZyUMnJ5XLs/TZDtAeafF5W1oyEygqva5fA3Ga5agq9HKHMEZx2qCOgOB+97C26StVKUdGD3jcPCh64AEgXLdCftIPwiRXP6eAr0IeeaujjVdEobvuxyo=");
		comprobanteData.getDatosComprobante().setNoCertificado("00001000000103168809");//Mas de 20 caracteres
		comprobanteData.getDatosComprobante().setLugarExpedicion("");		
		// ================================================================================================================================================================================
		
		// ======================================== 2--Datos de la addenta ================================================================================================================
		
		CommentFEData[] arrAddenda = new CommentFEData[1];
		CommentFEData       add = new CommentFEData();		    
		add.setComment(comentariosAddenda);
		add.setPosition("0");
		             arrAddenda[0] =add;
		comprobanteData.setComments0(arrAddenda );
		//NotaInteraccionesData notaInteraccionesData = new NotaInteraccionesData();
		//AddendaData                         addenda = new AddendaData();		
		//notaInteraccionesData.setNotas(new String[] {comentariosAddenda});		
		//addenda.otraAddendaData=notaInteraccionesData;
		//comprobanteData.setAddenda(new Addenda[]{addenda});
		// =================================================================================================================================================================================
		
		// ===========================================  Datos adicionales  Orden Interna, Centro Beneficio==================================================================================
		AdditionalData additionalData = new AdditionalData() ;
		additionalData.setParam(new String[]{"Orden Interna ","Centro Beneficio"});
		comprobanteData.setAdditional(additionalData);
		// =================================================================================================================================================================================

		// ==================================================================================================================================================================================
		return comprobanteData;
	}


	/**
	 * @return the trasladoImporteIVA
	 */
	public String getTrasladoImporteIVA() {
		return trasladoImporteIVA;
	}


	/**
	 * @param trasladoImporteIVA the trasladoImporteIVA to set
	 */
	public void setTrasladoImporteIVA(String trasladoImporteIVA) {
		this.trasladoImporteIVA = trasladoImporteIVA;
	}


	/**
	 * @return the trasladoTasaISR
	 */
	public String getTrasladoTasaISR() {
		return trasladoTasaISR;
	}


	/**
	 * @param trasladoTasaISR the trasladoTasaISR to set
	 */
	public void setTrasladoTasaISR(String trasladoTasaISR) {
		this.trasladoTasaISR = trasladoTasaISR;
	}


	/**
	 * @return the trasladoImporteISR
	 */
	public String getTrasladoImporteISR() {
		return trasladoImporteISR;
	}


	/**
	 * @param trasladoImporteISR the trasladoImporteISR to set
	 */
	public void setTrasladoImporteISR(String trasladoImporteISR) {
		this.trasladoImporteISR = trasladoImporteISR;
	}


	/**
	 * @return the retencionImporteIVA
	 */
	public String getRetencionImporteIVA() {
		return retencionImporteIVA;
	}


	/**
	 * @param retencionImporteIVA the retencionImporteIVA to set
	 */
	public void setRetencionImporteIVA(String retencionImporteIVA) {
		this.retencionImporteIVA = retencionImporteIVA;
	}


	/**
	 * @return the retencionImporteISR
	 */
	public String getRetencionImporteISR() {
		return retencionImporteISR;
	}


	/**
	 * @param retencionImporteISR the retencionImporteISR to set
	 */
	public void setRetencionImporteISR(String retencionImporteISR) {
		this.retencionImporteISR = retencionImporteISR;
	}

}
