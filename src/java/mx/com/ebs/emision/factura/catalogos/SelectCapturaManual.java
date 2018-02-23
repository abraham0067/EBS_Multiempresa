/**
 * 
 */
package mx.com.ebs.emision.factura.catalogos;


/**
 * @author Amiranda
 * Objeto que permite almacenar los valores de las selecciones de las listas contenidas en la pagina captura Manual
 * 
 */
public class SelectCapturaManual {
	
	private String emisor;
	private String tipoDocumento;
	private String metodoPago;
	private String serie;
	private String moneda;
	private String descuentos;
	private String tasa;
	private String ivaRetenido;
	private String ieps;
	private String isrRetenido;
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("================ Datos del objeto "+SelectCapturaManual.class+"==================" );
		sb.append("\nemisor:"+emisor +"\n");
		sb.append("tipoDocumento:"+tipoDocumento +"\n");
		sb.append("metodoPago:"+metodoPago +"\n");
		sb.append("serie:"+serie +"\n");
		sb.append("moneda:"+moneda +"\n");
		sb.append("descuentos:"+descuentos+"\n");
		sb.append("tasa:"+tasa +"\n");
		sb.append("ivaRetenido:"+ivaRetenido +"\n");
		sb.append("ieps:"+ieps +"\n");
		sb.append("isrRetenido:"+isrRetenido +"\n");		
		return sb.toString();
	}
	
	/**
	 * @return the tipoDocumento
	 */
	public String getTipoDocumento() {
		return tipoDocumento;
	}

	/**
	 * @param tipoDocumento the tipoDocumento to set
	 */
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	/**
	 * @return the emisor
	 */
	public String getEmisor() {
		return emisor;
	}

	/**
	 * @param emisor the emisor to set
	 */
	public void setEmisor(String emisor) {
		this.emisor = emisor;
	}

	/**
	 * @return the metodoPago
	 */
	public String getMetodoPago() {
		return metodoPago;
	}

	/**
	 * @param metodoPago the metodoPago to set
	 */
	public void setMetodoPago(String metodoPago) {
		this.metodoPago = metodoPago;
	}

	/**
	 * @return the serie
	 */
	public String getSerie() {
		return serie;
	}

	/**
	 * @param serie the serie to set
	 */
	public void setSerie(String serie) {
		this.serie = serie;
	}

	/**
	 * @return the moneda
	 */
	public String getMoneda() {
		return moneda;
	}

	/**
	 * @param moneda the moneda to set
	 */
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	/**
	 * @return the descuentos
	 */
	public String getDescuentos() {
		return descuentos;
	}

	/**
	 * @param descuentos the descuentos to set
	 */
	public void setDescuentos(String descuentos) {
		this.descuentos = descuentos;
	}

	/**
	 * @return the tasa
	 */
	public String getTasa() {
		return tasa;
	}

	/**
	 * @param tasa the tasa to set
	 */
	public void setTasa(String tasa) {
		this.tasa = tasa;
	}

	/**
	 * @return the ivaRetenido
	 */
	public String getIvaRetenido() {
		return ivaRetenido;
	}

	/**
	 * @param ivaRetenido the ivaRetenido to set
	 */
	public void setIvaRetenido(String ivaRetenido) {
		this.ivaRetenido = ivaRetenido;
	}

	/**
	 * @return the ieps
	 */
	public String getIeps() {
		return ieps;
	}

	/**
	 * @param ieps the ieps to set
	 */
	public void setIeps(String ieps) {
		this.ieps = ieps;
	}

	/**
	 * @return the isrRetenido
	 */
	public String getIsrRetenido() {
		return isrRetenido;
	}

	/**
	 * @param isrRetenido the isrRetenido to set
	 */
	public void setIsrRetenido(String isrRetenido) {
		this.isrRetenido = isrRetenido;
	}
	
	
	
	
	
	
	

}
