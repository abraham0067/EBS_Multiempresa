package fe.db;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
//import org.apache.commons.lang.StringEscapeUtils;


@SuppressWarnings({ "serial", "unused" })
@Entity
@Table(name = "M_INVOICE")
public class MInvoice implements Serializable {
	@Id	
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", nullable = false)
	private int id = 0;
	@Column(name = "TIPO_DOCUMENTO", length = 50, nullable = true)
	private String tipoDocumento = "FACTURA";
	@Column(name = "SERIE_ERP", length = 50, nullable = true)
	private String serieErp = "";
	@Column(name = "FOLIO_ERP", length = 40, nullable = true)
	private String folioErp = "";
	@Column(name = "NUMERO_CLIENTE", length = 40, nullable = true)
	private String noCliente = "";
	@Column(name = "RFC", length = 40, nullable = true)
	private String rfc = "XAXX010101000";
	@Column(name = "RAZON_SOCIAL", length = 200, nullable = true)
	private String razonSocial = "";
	@Column(name = "IMPORTE_IVA", nullable = true, precision = 40, scale = 2)
	private double iva = 0d;
	@Column(name = "IMPORTE_TOTAL", nullable = true, precision = 40, scale = 2)
	private double total = 0d;
	@Column(name = "FLAG", nullable = true)
	private Integer flag = 0;
	@Column(name = "SERIE", length = 30, nullable = true)
	private String serie = "";
	@Column(name = "FOLIO", length = 30, nullable = true)
	private double folio = 0;
	@Column(name = "NUMERO_FACTURA", length = 30, nullable = false)
	private String numeroFactura = "";
	@Column(name = "FECHA", nullable = true)
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	private Date fecha;
	@Column(name = "NO_APROBACION", length = 40, nullable = true)
	private String noAprobacion;
	@Column(name = "STATUS", nullable = false)
	private int estatusDoc = 0;
	@Column(name = "STATUS_ENVIO", nullable = true)
	private Integer estatusMail = 0;
	@Column(name = "FECHA_CANCELACION", nullable = true)
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	private Date fechaCancelacion;
	@Column(name = "EBS_APROBACION", length = 20, nullable = true)
	private String ebsAprovacion;
	@Column(name = "FILENAME", length = 200, nullable = true)
	private String nombreArchivo;
	@Column(name = "FACTURA_SAP", length = 50, nullable = true)
	private String facturaSap;
	@Column(name = "AREA_NEGOCIO", length = 50, nullable = true)
	private String areaNegocio;
	@Column(name = "REGION", length = 70, nullable = true)
	private String region;
	@Column(name = "IMPORTE", precision = 25, scale = 2, nullable = true)
	private double importe = 0d;
	@Column(name = "MONEDA_DOC", length = 50, nullable = true)
	private String moneda = "MXN";
	@Column(name = "SECTOR", length = 50, nullable = true)
	private String sector;
	@Column(name = "COBRA", length = 50, nullable = true)
	private String cobra;
	@Column(name = "PEDIDO", length =50, nullable = true)
	private String pedido;
	@Column(name = "CONTACTO", length = 100, nullable = true)
	private String contacto;
	@Column(name = "NO_PARCIALIDADES", length = 25, nullable = true)
	private String noParcialidades;
	@Column(name = "STATUS_PRINT", nullable = true)
	private Integer estatusPrint = 0;
	@Column(name = "FECHA_SAP", nullable = true)
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	private Date fechaSAP;
	@Column(name = "TIPO_CAMBIO", nullable = true, precision = 25, scale = 4)
	private double tipoCambio = 1d;
	@Column(name = "MONTO_PESOS", nullable = true, precision = 25, scale = 2)
	private double montoPesos;
	@Column(name = "IMPUESTO_PESOS", nullable = true, precision = 25, scale = 2)
	private Double impuestoPesos;	
	@Column(name = "ADJUNTOS", nullable = true)
	private Integer adjuntos = 0;
	@Column(name = "STATUS_PRINT2", length = 10, nullable = true)
	private String estatusPrint2;
	@ManyToOne
	private MEmpresa empresa;
	@ManyToOne
	private MPlantilla plantilla;

	public MInvoice() {
		super();
	}

	/**
	 * @return the tipoDocumento
	 */
	public String getTipoDocumento() {
		return tipoDocumento;
		
	}

	/**
	 * @param tipoDocumento
	 *            the tipoDocumento to set
	 */
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	/**
	 * @return the serieErp
	 */
	public String getSerieErp() {
		return serieErp;
	}

	/**
	 * @param serieErp
	 *            the serieErp to set
	 */
	public void setSerieErp(String serieErp) {
		this.serieErp = serieErp;
	}

	/**
	 * @return the folioErp
	 */
	public String getFolioErp() {
		return folioErp;
	}

	/**
	 * @param folioErp
	 *            the folioErp to set
	 */
	public void setFolioErp(String folioErp) {
		this.folioErp = folioErp;
	}

	/**
	 * @return the noCliente
	 */
	public String getNoCliente() {
		return noCliente;
	}

	/**
	 * @param noCliente
	 *            the noCliente to set
	 */
	public void setNoCliente(String noCliente) {
		this.noCliente = noCliente;
	}

	/**
	 * @return the rfc
	 */
	public String getRfc() {
		return rfc;
	}

	/**
	 * @param rfc
	 *            the rfc to set
	 */
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	/**
	 * @return the razonSocial
	 */
	public String getRazonSocial() {
		return razonSocial;
	}

	/**
	 * @param razonSocial
	 *            the razonSocial to set
	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	/**
	 * @return the iva
	 */
	public double getIva() {
		return iva;
	}

	/**
	 * @param iva
	 *            the iva to set
	 */
	public void setIva(double iva) {
		this.iva = iva;
	}

	/**
	 * @return the total
	 */
	public double getTotal() {
		return total;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal(double total) {
		this.total = total;
	}

	

	/**
	 * @return the serie
	 */
	public String getSerie() {
		return serie;
	}

	/**
	 * @param serie
	 *            the serie to set
	 */
	public void setSerie(String serie) {
		this.serie = serie;
	}

	/**
	 * @return the folio
	 */
	public double getFolio() {
		return folio;
	}

	/**
	 * @param folio
	 *            the folio to set
	 */
	public void setFolio(double folio) {
		this.folio = folio;
	}

	/**
	 * @return the numeroFactura
	 */
	public String getNumeroFactura() {
		return numeroFactura;
	}

	/**
	 * @param numeroFactura
	 *            the numeroFactura to set
	 */
	public void setNumeroFactura(String numeroFactura) {
		this.numeroFactura = numeroFactura;
	}

	/**
	 * @return the fecha
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * @param fecha
	 *            the fecha to set
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	

	


	/**
	 * @return the fechaCancelacion
	 */
	public Date getFechaCancelacion() {
		return fechaCancelacion;
	}

	/**
	 * @param fechaCancelacion
	 *            the fechaCancelacion to set
	 */
	public void setFechaCancelacion(Date fechaCancelacion) {
		this.fechaCancelacion = fechaCancelacion;
	}

	/**
	 * @return the ebsAprovacion
	 */
	public String getEbsAprovacion() {
		return ebsAprovacion;
	}

	/**
	 * @param ebsAprovacion
	 *            the ebsAprovacion to set
	 */
	public void setEbsAprovacion(String ebsAprovacion) {
		this.ebsAprovacion = ebsAprovacion;
	}

	/**
	 * @return the nombreArchivo
	 */
	public String getNombreArchivo() {
		return nombreArchivo;
	}

	/**
	 * @param nombreArchivo
	 *            the nombreArchivo to set
	 */
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	/**
	 * @return the facturaSap
	 */
	public String getFacturaSap() {
		return facturaSap;
	}

	/**
	 * @param facturaSap
	 *            the facturaSap to set
	 */
	public void setFacturaSap(String facturaSap) {
		this.facturaSap = facturaSap;
	}

	/**
	 * @return the areaNegocio
	 */
	public String getAreaNegocio() {
		return areaNegocio;
	}

	/**
	 * @param areaNegocio
	 *            the areaNegocio to set
	 */
	public void setAreaNegocio(String areaNegocio) {
		this.areaNegocio = areaNegocio;
	}

	/**
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * @param region
	 *            the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}

	/**
	 * @return the importe
	 */
	public double getImporte() {
		return importe;
	}

	/**
	 * @param importe
	 *            the importe to set
	 */
	public void setImporte(double importe) {
		this.importe = importe;
	}

	/**
	 * @return the moneda
	 */
	public String getMoneda() {
		return moneda;
	}

	/**
	 * @param moneda
	 *            the moneda to set
	 */
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	/**
	 * @return the sector
	 */
	public String getSector() {
		return sector;
	}

	/**
	 * @param sector
	 *            the sector to set
	 */
	public void setSector(String sector) {
		this.sector = sector;
	}

	/**
	 * @return the cobra
	 */
	public String getCobra() {
		return cobra;
	}

	/**
	 * @param cobra
	 *            the cobra to set
	 */
	public void setCobra(String cobra) {
		this.cobra = cobra;
	}

	/**
	 * @return the pedido
	 */
	public String getPedido() {
		return pedido;
	}

	/**
	 * @param pedido
	 *            the pedido to set
	 */
	public void setPedido(String pedido) {
		this.pedido = pedido;
	}

	/**
	 * @return the contacto
	 */
	public String getContacto() {
		return contacto;
	}

	/**
	 * @param contacto
	 *            the contacto to set
	 */
	public void setContacto(String contacto) {
		this.contacto = contacto;
	}

	/**
	 * @return the noParcialidades
	 */
	public String getNoParcialidades() {
		return noParcialidades;
	}

	/**
	 * @param noParcialidades
	 *            the noParcialidades to set
	 */
	public void setNoParcialidades(String noParcialidades) {
		this.noParcialidades = noParcialidades;
	}

	

	/**
	 * @return the fechaSAP
	 */
	public Date getFechaSAP() {
		return fechaSAP;
	}

	/**
	 * @param fechaSAP
	 *            the fechaSAP to set
	 */
	public void setFechaSAP(Date fechaSAP) {
		this.fechaSAP = fechaSAP;
	}

	/**
	 * @return the tipoCambio
	 */
	public double getTipoCambio() {
		return tipoCambio;
	}

	/**
	 * @param tipoCambio
	 *            the tipoCambio to set
	 */
	public void setTipoCambio(double tipoCambio) {
		this.tipoCambio = tipoCambio;
	}

	/**
	 * @return the montoPesos
	 */
	public double getMontoPesos() {
		return montoPesos;
	}

	/**
	 * @param montoPesos
	 *            the montoPesos to set
	 */
	public void setMontoPesos(double montoPesos) {
		this.montoPesos = montoPesos;
	}

	

	/**
	 * @return the estatusPrint2
	 */
	public String getEstatusPrint2() {
		return estatusPrint2;
	}

	/**
	 * @param estatusPrint2
	 *            the estatusPrint2 to set
	 */
	public void setEstatusPrint2(String estatusPrint2) {
		this.estatusPrint2 = estatusPrint2;
	}

	/**
	 * @return the empresa
	 */
	public MEmpresa getEmpresa() {
		return empresa;
	}

	/**
	 * @param empresa
	 *            the empresa to set
	 */
	public void setEmpresa(MEmpresa empresa) {
		this.empresa = empresa;
	}

	/**
	 * @return the plantilla
	 */
	public MPlantilla getPlantilla() {
		return plantilla;
	}

	/**
	 * @param plantilla
	 *            the plantilla to set
	 */
	public void setPlantilla(MPlantilla plantilla) {
		this.plantilla = plantilla;
	}

	public String FECHA() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(fecha);
	}

	public String Total() {
		String cantidad = "";
		Locale locMEX = new Locale("sp_MX");
		NumberFormat nf = NumberFormat.getInstance(locMEX);
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		cantidad = nf.format(total);

		return cantidad;

	}

	public String ESTATUS() {
		String res = "";
		if (estatusDoc == 1) {
			res = "GENERADA";
		} else {
			res = "CANCELADA";
		}
		return res;
	}

	public String IVA() {
		String cantidad = "";
		Locale locMEX = new Locale("sp_MX");
		NumberFormat nf = NumberFormat.getInstance(locMEX);
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		cantidad = nf.format(iva);

		return cantidad;

	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the noAprobacion
	 */
	public String getNoAprobacion() {
		return noAprobacion;
	}

	/**
	 * @param noAprobacion the noAprobacion to set
	 */
	public void setNoAprobacion(String noAprobacion) {
		this.noAprobacion = noAprobacion;
	}

	/**
	 * @return the adjuntos
	 */
	public Integer getAdjuntos() {
		return adjuntos;
	}

	/**
	 * @param adjuntos the adjuntos to set
	 */
	public void setAdjuntos(Integer adjuntos) {
		this.adjuntos = adjuntos;
	}

	/**
	 * @return the flag
	 */
	public Integer getFlag() {
		return flag;
	}

	/**
	 * @param flag the flag to set
	 */
	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	/**
	 * @return the estatusDoc
	 */
	public int getEstatusDoc() {
		return estatusDoc;
	}

	/**
	 * @param estatusDoc the estatusDoc to set
	 */
	public void setEstatusDoc(int estatusDoc) {
		this.estatusDoc = estatusDoc;
	}

	/**
	 * @return the estatusMail
	 */
	public Integer getEstatusMail() {
		return estatusMail;
	}

	/**
	 * @param estatusMail the estatusMail to set
	 */
	public void setEstatusMail(Integer estatusMail) {
		this.estatusMail = estatusMail;
	}

	/**
	 * @return the estatusPrint
	 */
	public Integer getEstatusPrint() {
		return estatusPrint;
	}

	/**
	 * @param estatusPrint the estatusPrint to set
	 */
	public void setEstatusPrint(Integer estatusPrint) {
		this.estatusPrint = estatusPrint;
	}

	/**
	 * @return the impuestoPesos
	 */
	public Double getImpuestoPesos() {
		return impuestoPesos;
	}

	/**
	 * @param impuestoPesos the impuestoPesos to set
	 */
	public void setImpuestoPesos(Double impuestoPesos) {
		this.impuestoPesos = impuestoPesos;
	}
        
//        public void escapeSqlAndHtmlCharacters(){
//            this.tipoDocumento=(this.tipoDocumento != null)? StringEscapeUtils.escapeSql(this.tipoDocumento) : "";
//            this.serieErp=(this.serieErp != null) ? StringEscapeUtils.escapeSql(this.serieErp) : "";
//            this.folioErp=(this.folioErp != null) ? StringEscapeUtils.escapeSql(this.folioErp) : "";
//            this.noCliente=(this.noCliente != null) ? StringEscapeUtils.escapeSql(this.noCliente) : "";
//            this.rfc=(this.rfc != null)? StringEscapeUtils.escapeSql(this.rfc) : "";
//            this.razonSocial=(this.razonSocial != null)? StringEscapeUtils.escapeSql(this.razonSocial) : "";
//            this.serie=(this.serie != null)? StringEscapeUtils.escapeSql(this.serie) : "";
//            this.numeroFactura=(this.numeroFactura != null)? StringEscapeUtils.escapeSql(this.numeroFactura) : "";
//            this.noAprobacion=(this.noAprobacion != null) ? StringEscapeUtils.escapeSql(this.noAprobacion) : "";
//            this.ebsAprovacion=(this.ebsAprovacion != null)? StringEscapeUtils.escapeSql(this.ebsAprovacion) : "";
//            this.nombreArchivo=(this.nombreArchivo != null)? StringEscapeUtils.escapeSql(this.nombreArchivo) : "";
//            this.facturaSap=(this.facturaSap != null)?StringEscapeUtils.escapeSql(this.facturaSap) : "";
//            this.areaNegocio=(this.areaNegocio != null) ? StringEscapeUtils.escapeSql(this.areaNegocio) : "";
//            this.region=(this.region != null) ? StringEscapeUtils.escapeSql(this.region) : "";
//            this.moneda=(this.moneda != null) ? StringEscapeUtils.escapeSql(this.moneda) : "";
//            this.sector=(this.sector != null) ? StringEscapeUtils.escapeSql(this.sector) : "";
//            this.cobra=(this.cobra != null)? StringEscapeUtils.escapeSql(this.cobra) : "" ;
//            this.pedido=(this.pedido != null) ? StringEscapeUtils.escapeSql(this.pedido) : "";
//            this.contacto=(this.contacto != null) ? StringEscapeUtils.escapeSql(this.contacto) : "";
//            this.noParcialidades=(this.noParcialidades != null)?StringEscapeUtils.escapeSql(this.noParcialidades) : "";
//            this.estatusPrint2=(this.estatusPrint2 != null)?StringEscapeUtils.escapeSql(this.estatusPrint2): "";
//            
//            this.tipoDocumento=(this.tipoDocumento != null)? StringEscapeUtils.escapeHtml(this.tipoDocumento) : "";
//            this.serieErp=(this.serieErp != null) ? StringEscapeUtils.escapeHtml(this.serieErp) : "";
//            this.folioErp=(this.folioErp != null) ? StringEscapeUtils.escapeHtml(this.folioErp) : "";
//            this.noCliente=(this.noCliente != null) ? StringEscapeUtils.escapeHtml(this.noCliente) : "";
//            this.rfc=(this.rfc != null)? StringEscapeUtils.escapeHtml(this.rfc) : "";
//            this.razonSocial=(this.razonSocial != null)? StringEscapeUtils.escapeHtml(this.razonSocial) : "";
//            this.serie=(this.serie != null)? StringEscapeUtils.escapeHtml(this.serie) : "";
//            this.numeroFactura=(this.numeroFactura != null)? StringEscapeUtils.escapeHtml(this.numeroFactura) : "";
//            this.noAprobacion=(this.noAprobacion != null) ? StringEscapeUtils.escapeHtml(this.noAprobacion) : "";
//            this.ebsAprovacion=(this.ebsAprovacion != null)? StringEscapeUtils.escapeHtml(this.ebsAprovacion) : "";
//            this.nombreArchivo=(this.nombreArchivo != null)? StringEscapeUtils.escapeHtml(this.nombreArchivo) : "";
//            this.facturaSap=(this.facturaSap != null)?StringEscapeUtils.escapeHtml(this.facturaSap) : "";
//            this.areaNegocio=(this.areaNegocio != null) ? StringEscapeUtils.escapeHtml(this.areaNegocio) : "";
//            this.region=(this.region != null) ? StringEscapeUtils.escapeHtml(this.region) : "";
//            this.moneda=(this.moneda != null) ? StringEscapeUtils.escapeHtml(this.moneda) : "";
//            this.sector=(this.sector != null) ? StringEscapeUtils.escapeHtml(this.sector) : "";
//            this.cobra=(this.cobra != null)? StringEscapeUtils.escapeHtml(this.cobra) : "" ;
//            this.pedido=(this.pedido != null) ? StringEscapeUtils.escapeHtml(this.pedido) : "";
//            this.contacto=(this.contacto != null) ? StringEscapeUtils.escapeHtml(this.contacto) : "";
//            this.noParcialidades=(this.noParcialidades != null)?StringEscapeUtils.escapeHtml(this.noParcialidades) : "";
//            this.estatusPrint2=(this.estatusPrint2 != null)?StringEscapeUtils.escapeHtml(this.estatusPrint2): "";
//        }
}

