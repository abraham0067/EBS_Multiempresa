package fe.db;

import fe.model.util.CifraTexto;
import java.io.*;
import javax.persistence.*;
import java.util.*;
import java.text.*;
//import org.apache.commons.lang.StringEscapeUtils;

@Entity
@Table(name = "MCFD_RETENCION", catalog = "FACCORP_APL")
public class McfdRetencion implements Serializable
{
    private Integer id;
    private MEmpresa MEmpresa;
    private MPlantilla MPlantilla;
    private Integer anoAprobacion;
    private int estadoDocumento;
    private int estatusDoc;
    private int estatusEnt;
    private int estatusFtp;
    private int estatusMail;
    private int estatusPrint;
    private String facturaOrigen;
    private Date fecha;
    private Date fechaCancelacion;
    private Date fechaOrigen;
    private Double folio;
    private String folioErp;
    private int formaPago;
    private double iva;
    private double ivaMl;
    private String moneda;
    private Integer noAprobacion;
    private String numeroCliente;
    private String numeroFactura;
    private int parcialidad;
    private String razonSocial;
    private String rfc;
    private String nacionalidad;
    private String serie;
    private String serieErp;
    private int sistema;
    private double subtotal;
    private double subtotalMl;
    private Integer sucursal;
    private String tablaXml;
    private double tipoCambio;
    private int tipoComprobante;
    private String tipoDocumento;
    private int tipoFiscal;
    private double total;
    private double totalMl;
    private String uno;
    private String uuid;
    private Set<MOtroRetencion> MOtroRetencions;
    private Set<MCfdXmlRetencion> MCfdXmlRetencions;
    
    public McfdRetencion() {
        this.MOtroRetencions = new HashSet<MOtroRetencion>(0);
        this.MCfdXmlRetencions = new HashSet<MCfdXmlRetencion>(0);
    }
    
    public McfdRetencion(final int estadoDocumento, final int estatusDoc, final int estatusEnt, final int estatusFtp, final int estatusMail, final int estatusPrint, final Date fecha, final String folioErp, final int formaPago, final double iva, final double ivaMl, final int parcialidad, final String rfc, final String nacionalidad, final int sistema, final double subtotal, final double subtotalMl, final double tipoCambio, final int tipoComprobante, final String tipoDocumento, final int tipoFiscal, final double total, final double totalMl) {
        this.MOtroRetencions = new HashSet<MOtroRetencion>(0);
        this.MCfdXmlRetencions = new HashSet<MCfdXmlRetencion>(0);
        this.estadoDocumento = estadoDocumento;
        this.estatusDoc = estatusDoc;
        this.estatusEnt = estatusEnt;
        this.estatusFtp = estatusFtp;
        this.estatusMail = estatusMail;
        this.estatusPrint = estatusPrint;
        this.fecha = fecha;
        this.folioErp = folioErp;
        this.formaPago = formaPago;
        this.iva = iva;
        this.ivaMl = ivaMl;
        this.parcialidad = parcialidad;
        this.rfc = rfc;
        this.nacionalidad = nacionalidad;
        this.sistema = sistema;
        this.subtotal = subtotal;
        this.subtotalMl = subtotalMl;
        this.tipoCambio = tipoCambio;
        this.tipoComprobante = tipoComprobante;
        this.tipoDocumento = tipoDocumento;
        this.tipoFiscal = tipoFiscal;
        this.total = total;
        this.totalMl = totalMl;
    }
    
    public McfdRetencion(final MEmpresa MEmpresa, final MPlantilla MPlantilla, final Integer anoAprobacion, final int estadoDocumento, final int estatusDoc, final int estatusEnt, final int estatusFtp, final int estatusMail, final int estatusPrint, final String facturaOrigen, final Date fecha, final Date fechaCancelacion, final Date fechaOrigen, final Double folio, final String folioErp, final int formaPago, final double iva, final double ivaMl, final String moneda, final Integer noAprobacion, final String numeroCliente, final String numeroFactura, final int parcialidad, final String razonSocial, final String rfc, final String serie, final String serieErp, final int sistema, final double subtotal, final double subtotalMl, final Integer sucursal, final String tablaXml, final double tipoCambio, final int tipoComprobante, final String tipoDocumento, final int tipoFiscal, final double total, final double totalMl, final String uno, final String uuid, final Set<MOtroRetencion> MOtroRetencions, final Set<MCfdXmlRetencion> MCfdXmlRetencions) {
        this.MOtroRetencions = new HashSet<MOtroRetencion>(0);
        this.MCfdXmlRetencions = new HashSet<MCfdXmlRetencion>(0);
        this.MEmpresa = MEmpresa;
        this.MPlantilla = MPlantilla;
        this.anoAprobacion = anoAprobacion;
        this.estadoDocumento = estadoDocumento;
        this.estatusDoc = estatusDoc;
        this.estatusEnt = estatusEnt;
        this.estatusFtp = estatusFtp;
        this.estatusMail = estatusMail;
        this.estatusPrint = estatusPrint;
        this.facturaOrigen = facturaOrigen;
        this.fecha = fecha;
        this.fechaCancelacion = fechaCancelacion;
        this.fechaOrigen = fechaOrigen;
        this.folio = folio;
        this.folioErp = folioErp;
        this.formaPago = formaPago;
        this.iva = iva;
        this.ivaMl = ivaMl;
        this.moneda = moneda;
        this.noAprobacion = noAprobacion;
        this.numeroCliente = numeroCliente;
        this.numeroFactura = numeroFactura;
        this.parcialidad = parcialidad;
        this.razonSocial = razonSocial;
        this.rfc = rfc;
        this.serie = serie;
        this.serieErp = serieErp;
        this.sistema = sistema;
        this.subtotal = subtotal;
        this.subtotalMl = subtotalMl;
        this.sucursal = sucursal;
        this.tablaXml = tablaXml;
        this.tipoCambio = tipoCambio;
        this.tipoComprobante = tipoComprobante;
        this.tipoDocumento = tipoDocumento;
        this.tipoFiscal = tipoFiscal;
        this.total = total;
        this.totalMl = totalMl;
        this.uno = uno;
        this.uuid = uuid;
        this.MOtroRetencions = MOtroRetencions;
        this.MCfdXmlRetencions = MCfdXmlRetencions;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "empresa_ID")
    public MEmpresa getMEmpresa() {
        return this.MEmpresa;
    }
    
    public void setMEmpresa(final MEmpresa MEmpresa) {
        this.MEmpresa = MEmpresa;
    }
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "plantilla_ID")
    public MPlantilla getMPlantilla() {
        return this.MPlantilla;
    }
    
    public void setMPlantilla(final MPlantilla MPlantilla) {
        this.MPlantilla = MPlantilla;
    }
    
    @Column(name = "ANO_APROBACION")
    public Integer getAnoAprobacion() {
        return this.anoAprobacion;
    }
    
    public void setAnoAprobacion(final Integer anoAprobacion) {
        this.anoAprobacion = anoAprobacion;
    }
    
    @Column(name = "ESTADO_DOCUMENTO", nullable = false)
    public int getEstadoDocumento() {
        return this.estadoDocumento;
    }
    
    public void setEstadoDocumento(final int estadoDocumento) {
        this.estadoDocumento = estadoDocumento;
    }
    
    @Column(name = "ESTATUS_DOC", nullable = false)
    public int getEstatusDoc() {
        return this.estatusDoc;
    }
    
    public void setEstatusDoc(final int estatusDoc) {
        this.estatusDoc = estatusDoc;
    }
    
    @Column(name = "ESTATUS_ENT", nullable = false)
    public int getEstatusEnt() {
        return this.estatusEnt;
    }
    
    public void setEstatusEnt(final int estatusEnt) {
        this.estatusEnt = estatusEnt;
    }
    
    @Column(name = "ESTATUS_FTP", nullable = false)
    public int getEstatusFtp() {
        return this.estatusFtp;
    }
    
    public void setEstatusFtp(final int estatusFtp) {
        this.estatusFtp = estatusFtp;
    }
    
    @Column(name = "ESTATUS_MAIL", nullable = false)
    public int getEstatusMail() {
        return this.estatusMail;
    }
    
    public void setEstatusMail(final int estatusMail) {
        this.estatusMail = estatusMail;
    }
    
    @Column(name = "ESTATUS_PRINT", nullable = false)
    public int getEstatusPrint() {
        return this.estatusPrint;
    }
    
    public void setEstatusPrint(final int estatusPrint) {
        this.estatusPrint = estatusPrint;
    }
    
    @Column(name = "FACTURA_ORIGEN", length = 70)
    public String getFacturaOrigen() {
        return this.facturaOrigen;
    }
    
    public void setFacturaOrigen(final String facturaOrigen) {
        this.facturaOrigen = facturaOrigen;
    }
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA", nullable = false, length = 19)
    public Date getFecha() {
        return this.fecha;
    }
    
    public void setFecha(final Date fecha) {
        this.fecha = fecha;
    }
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA_CANCELACION", length = 19)
    public Date getFechaCancelacion() {
        return this.fechaCancelacion;
    }
    
    public void setFechaCancelacion(final Date fechaCancelacion) {
        this.fechaCancelacion = fechaCancelacion;
    }
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA_ORIGEN", length = 19)
    public Date getFechaOrigen() {
        return this.fechaOrigen;
    }
    
    public void setFechaOrigen(final Date fechaOrigen) {
        this.fechaOrigen = fechaOrigen;
    }
    
    @Column(name = "FOLIO", precision = 22, scale = 0)
    public Double getFolio() {
        return this.folio;
    }
    
    public void setFolio(final Double folio) {
        this.folio = folio;
    }
    
    @Column(name = "FOLIO_ERP", nullable = false, length = 30)
    public String getFolioErp() {
        return this.folioErp;
    }
    
    public void setFolioErp(final String folioErp) {
        this.folioErp = folioErp;
    }
    
    @Column(name = "FORMA_PAGO", nullable = false)
    public int getFormaPago() {
        return this.formaPago;
    }
    
    public void setFormaPago(final int formaPago) {
        this.formaPago = formaPago;
    }
    
    @Column(name = "iva", nullable = false, precision = 22, scale = 0)
    public double getIva() {
        return this.iva;
    }
    
    public void setIva(final double iva) {
        this.iva = iva;
    }
    
    @Column(name = "IVA_ML", nullable = false, precision = 22, scale = 0)
    public double getIvaMl() {
        return this.ivaMl;
    }
    
    public void setIvaMl(final double ivaMl) {
        this.ivaMl = ivaMl;
    }
    
    @Column(name = "MONEDA", length = 45)
    public String getMoneda() {
        return this.moneda;
    }
    
    public void setMoneda(final String moneda) {
        this.moneda = moneda;
    }
    
    @Column(name = "NO_APROBACION")
    public Integer getNoAprobacion() {
        return this.noAprobacion;
    }
    
    public void setNoAprobacion(final Integer noAprobacion) {
        this.noAprobacion = noAprobacion;
    }
    
    @Column(name = "NUMERO_CLIENTE", length = 45)
    public String getNumeroCliente() {
        return this.numeroCliente;
    }
    
    public void setNumeroCliente(final String numeroCliente) {
        this.numeroCliente = numeroCliente;
    }
    
    @Column(name = "NUMERO_FACTURA", length = 70)
    public String getNumeroFactura() {
        return this.numeroFactura;
    }
    
    public void setNumeroFactura(final String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }
    
    @Column(name = "PARCIALIDAD", nullable = false)
    public int getParcialidad() {
        return this.parcialidad;
    }
    
    public void setParcialidad(final int parcialidad) {
        this.parcialidad = parcialidad;
    }
    
    @Column(name = "RAZON_SOCIAL", length = 256)
    public String getRazonSocial() {
        return this.razonSocial;
    }
    
    public void setRazonSocial(final String razonSocial) {
        this.razonSocial = razonSocial;
    }
    
    @Column(name = "RFC", nullable = false, length = 15)
    public String getRfc() {
        return this.rfc;
    }
    
    public void setRfc(final String rfc) {
        this.rfc = rfc;
    }
    
    @Column(name = "NACIONALIDAD", nullable = false, length = 15)
    public String getNacionalidad() {
        return this.nacionalidad;
    }
    
    public void setNacionalidad(final String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }
    
    @Column(name = "SERIE", length = 25)
    public String getSerie() {
        return this.serie;
    }
    
    public void setSerie(final String serie) {
        this.serie = serie;
    }
    
    @Column(name = "SERIE_ERP", length = 30)
    public String getSerieErp() {
        return this.serieErp;
    }
    
    public void setSerieErp(final String serieErp) {
        this.serieErp = serieErp;
    }
    
    @Column(name = "SISTEMA", nullable = false)
    public int getSistema() {
        return this.sistema;
    }
    
    public void setSistema(final int sistema) {
        this.sistema = sistema;
    }
    
    @Column(name = "SUBTOTAL", nullable = false, precision = 22, scale = 0)
    public double getSubtotal() {
        return this.subtotal;
    }
    
    public void setSubtotal(final double subtotal) {
        this.subtotal = subtotal;
    }
    
    @Column(name = "SUBTOTAL_ML", nullable = false, precision = 22, scale = 0)
    public double getSubtotalMl() {
        return this.subtotalMl;
    }
    
    public void setSubtotalMl(final double subtotalMl) {
        this.subtotalMl = subtotalMl;
    }
    
    @Column(name = "SUCURSAL")
    public Integer getSucursal() {
        return this.sucursal;
    }
    
    public void setSucursal(final Integer sucursal) {
        this.sucursal = sucursal;
    }
    
    @Column(name = "TABLA_XML", length = 30)
    public String getTablaXml() {
        return this.tablaXml;
    }
    
    public void setTablaXml(final String tablaXml) {
        this.tablaXml = tablaXml;
    }
    
    @Column(name = "TIPO_CAMBIO", nullable = false, precision = 22, scale = 0)
    public double getTipoCambio() {
        return this.tipoCambio;
    }
    
    public void setTipoCambio(final double tipoCambio) {
        this.tipoCambio = tipoCambio;
    }
    
    @Column(name = "TIPO_COMPROBANTE", nullable = false)
    public int getTipoComprobante() {
        return this.tipoComprobante;
    }
    
    public void setTipoComprobante(final int tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }
    
    @Column(name = "TIPO_DOCUMENTO", nullable = false, length = 45)
    public String getTipoDocumento() {
        return this.tipoDocumento;
    }
    
    public void setTipoDocumento(final String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
    
    @Column(name = "TIPO_FISCAL", nullable = false)
    public int getTipoFiscal() {
        return this.tipoFiscal;
    }
    
    public void setTipoFiscal(final int tipoFiscal) {
        this.tipoFiscal = tipoFiscal;
    }
    
    @Column(name = "TOTAL", nullable = false, precision = 22, scale = 0)
    public double getTotal() {
        return this.total;
    }
    
    public void setTotal(final double total) {
        this.total = total;
    }
    
    @Column(name = "TOTAL_ML", nullable = false, precision = 22, scale = 0)
    public double getTotalMl() {
        return this.totalMl;
    }
    
    public void setTotalMl(final double totalMl) {
        this.totalMl = totalMl;
    }
    
    @Column(name = "uno")
    public String getUno() {
        return this.uno;
    }
    
    public void setUno(final String uno) {
        this.uno = uno;
    }
    
    @Column(name = "UUID", length = 100)
    public String getUuid() {
        return this.uuid;
    }
    
    public void setUuid(final String uuid) {
        this.uuid = uuid;
    }
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "mcfdRetencion")
    public Set<MOtroRetencion> getMOtroRetencions() {
        return this.MOtroRetencions;
    }
    
    public void setMOtroRetencions(final Set<MOtroRetencion> MOtroRetencions) {
        this.MOtroRetencions = MOtroRetencions;
    }
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "mcfdRetencion")
    public Set<MCfdXmlRetencion> getMCfdXmlRetencions() {
        return this.MCfdXmlRetencions;
    }
    
    public void setMCfdXmlRetencions(final Set<MCfdXmlRetencion> MCfdXmlRetencions) {
        this.MCfdXmlRetencions = MCfdXmlRetencions;
    }
    
    public String FECHA() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.fecha);
    }
    
    public String ESTATUS() {
        String res = "";
        if (this.estadoDocumento == 1) {
            res = "GENERADA";
        }
        else {
            res = "CANCELADA";
        }
        return res;
    }
    
    public String IVA() {
        String cantidad = "";
        final Locale locMEX = new Locale("sp_MX");
        final NumberFormat nf = NumberFormat.getInstance(locMEX);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        cantidad = nf.format(this.iva);
        return cantidad;
    }
    
    public String Total() {
        String cantidad = "";
        final Locale locMEX = new Locale("sp_MX");
        final NumberFormat nf = NumberFormat.getInstance(locMEX);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        cantidad = nf.format(this.total);
        return cantidad;
    }
    
    public String tranferirDireccionCifrada() {
        final SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        final CifraTexto c = new CifraTexto(String.valueOf(this.id), this.numeroFactura, sdf.format(new Date()));
        return c.httpEncode();
    }
    
//    public void escapeSqlAndHtmlCharacters(){
//        this.facturaOrigen = (this.facturaOrigen != null)?  StringEscapeUtils.escapeSql(this.facturaOrigen): "";
//        this.folioErp = (this.folioErp != null)?  StringEscapeUtils.escapeSql(this.folioErp):"";
//        this.moneda = (this.moneda != null)?  StringEscapeUtils.escapeSql(this.moneda):"";
//        this.nacionalidad = (this.nacionalidad != null) ? StringEscapeUtils.escapeSql(this.nacionalidad) : "";
//        this.numeroCliente = (this.numeroCliente != null) ? StringEscapeUtils.escapeSql(this.numeroCliente) : "";
//        this.numeroFactura = (this.numeroFactura != null) ? StringEscapeUtils.escapeSql(this.numeroFactura) : "";
//        this.razonSocial = (this.razonSocial != null)?  StringEscapeUtils.escapeSql(this.razonSocial):"";
//        this.rfc =  (this.rfc != null)?  StringEscapeUtils.escapeSql(this.rfc):"";
//        this.serie = (this.serie != null)?  StringEscapeUtils.escapeSql(this.serie):"";
//        this.serieErp = (this.serieErp != null)?  StringEscapeUtils.escapeSql(this.serieErp):"";
//        this.tablaXml = (this.tablaXml != null)?  StringEscapeUtils.escapeSql(this.tablaXml):"";
//        this.tipoDocumento = (this.tipoDocumento != null)? StringEscapeUtils.escapeSql(this.tipoDocumento) :"";
//        this.uno = (this.uno != null)?  StringEscapeUtils.escapeSql(this.uno):"";
//        this.uuid = (this.uuid != null)? StringEscapeUtils.escapeSql(this.uuid):"";
//        
//        this.facturaOrigen = (this.facturaOrigen != null)?  StringEscapeUtils.escapeHtml(this.facturaOrigen): "";
//        this.folioErp = (this.folioErp != null)?  StringEscapeUtils.escapeHtml(this.folioErp):"";
//        this.moneda = (this.moneda != null)?  StringEscapeUtils.escapeHtml(this.moneda):"";
//        this.nacionalidad = (this.nacionalidad != null) ? StringEscapeUtils.escapeHtml(this.nacionalidad) : "";
//        this.numeroCliente = (this.numeroCliente != null) ? StringEscapeUtils.escapeHtml(this.numeroCliente) : "";
//        this.numeroFactura = (this.numeroFactura != null) ? StringEscapeUtils.escapeHtml(this.numeroFactura) : "";
//        this.razonSocial = (this.razonSocial != null)?  StringEscapeUtils.escapeHtml(this.razonSocial):"";
//        this.rfc =  (this.rfc != null)?  StringEscapeUtils.escapeHtml(this.rfc):"";
//        this.serie = (this.serie != null)?  StringEscapeUtils.escapeHtml(this.serie):"";
//        this.serieErp = (this.serieErp != null)?  StringEscapeUtils.escapeHtml(this.serieErp):"";
//        this.tablaXml = StringEscapeUtils.escapeSql(this.tablaXml);
//        this.tipoDocumento = (this.tipoDocumento != null)? StringEscapeUtils.escapeHtml(this.tipoDocumento) :"";
//        this.uno = (this.uno != null)?  StringEscapeUtils.escapeHtml(this.uno):"";
//        this.uuid = (this.uuid != null)? StringEscapeUtils.escapeHtml(this.uuid):"";
//    }
}
