package fe.db;

/**
 *
 * @Author Carlo Garcia Sanchez
 * @Date 04-10-2012
 *
 * Tabla de M_CFD para almacenar documentos CFD / CFDi
 *
 *
 */
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

import org.hibernate.annotations.Index;


@SuppressWarnings({"serial", "unused"})
@Entity
@Table(name = "M_CFD")
public class MCfd implements Serializable {


    private int id = 0;
    private Integer idEmpresa = 0;
    
    private Date fechaOrigen;
    private double subTotal = 0d;
    private double iva = 0d;
    private double total = 0d;
    private String rfc = "XAXX010101000";
    private String razonSocial = "";
    private int edoDocumento = 1;
    private String numeroFactura = "";
    private String uuid = "";
    private String serie = "";
    private double folio = 0;
    private Date fecha;
    private int anoAprobacion;
    private int noAprobacion;
    private String serieErp = "";
    private String folioErp = "";
    private String folioErp2 = "";
    private String moneda = "MXN";
    private double tipoCambio = 1d;
    private double subTotalML = 0;
    private double ivaML = 0;
    private double totalML = 0;
    private String noCliente = "";
    private Date fechaCancelacion;
    private String tipoDocumento = "FACTURA";
    private int tipoComprobante = 0;
    private int tipoFiscal = 0; // 0 - CFD, 1 - CFDi
    private int formaPago = 1; // 1 - Una exhibicion, n > 1 - Paricalidades
    private int parcialidad = 1;
    private String facturaOrigen = "";
    private int sistema = 1;
    private Integer idPlantilla = 0;
    private String tablaXml = "";
    private int estatusDoc = 0;
    private int estatusEnt = 0;
    private int estatusMail = 0;
    private int estatusFtp = 0;
    private int estatusPrint = 0;
    private String uno;

    public MCfd() {
    }

    public MCfd(Integer idEmpresa, String serieErp, String folioErp, String numeroFactura, Date fecha, MPlantilla plantilla) {
        this.idEmpresa = idEmpresa;
        this.serieErp = serieErp;
        this.folioErp = folioErp;
        this.numeroFactura = numeroFactura;
        this.fecha = fecha;
    }

    public MCfd(Integer idEmpresa, String serieErp, String folioErp, String folioErp2, String numeroFactura, Date fecha, MPlantilla plantilla) {
        this.idEmpresa = idEmpresa;
        this.serieErp = serieErp;
        this.folioErp = folioErp;
        this.folioErp2 = folioErp2;
        this.numeroFactura = numeroFactura;
        this.fecha = fecha;
    }

    @Id
    /*@SequenceGenerator(name = "SCfd", sequenceName = "SECUENCIA_CFD", initialValue = 1, allocationSize = 1)
     @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SCfd")*/
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }


    @Column(name = "FECHA_ORIGEN", nullable = true)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getFechaOrigen() {
        return (this.fechaOrigen);
    }

    @Column(name = "SUBTOTAL", nullable = false, precision = 13, scale = 2)
    public double getSubTotal() {
        return (this.subTotal);
    }

    @Column(name = "iva", nullable = false, precision = 13, scale = 2)
    public double getIva() {
        return (this.iva);
    }

    @Column(name = "TOTAL", nullable = false, precision = 13, scale = 2)
    public double getTotal() {
        return (this.total);
    }

    @Index(name = "CFD_RFC_IDX")
    @Column(name = "RFC", nullable = false, length = 15)
    public String getRfc() {
        return (this.rfc);
    }

    @Index(name = "CFD_RS_IDX")
    @Column(name = "RAZON_SOCIAL", nullable = true, length = 100)
    public String getRazonSocial() {
        return (this.razonSocial);
    }

    @Index(name = "CFD_EST_DOC_IDX")
    @Column(name = "ESTADO_DOCUMENTO", nullable = false)
    public int getEdoDocumento() {
        return (this.edoDocumento);
    }

    @Index(name = "CFD_NO_FACTURA_IDX")
    @Column(name = "NUMERO_FACTURA", nullable = true, length = 70)
    public String getNumeroFactura() {
        return numeroFactura;
    }

    @Index(name = "CFD_UUID_IDX")
    @Column(name = "UUID", nullable = true, length = 70)
    public String getUuid() {
        return uuid;
    }

    @Index(name = "CFD_SERIE_IDX")
    @Column(name = "SERIE", nullable = true, length = 25)
    public String getSerie() {
        return (this.serie);
    }

    @Column(name = "FOLIO", nullable = true, precision = 20)
    public double getFolio() {
        return (this.folio);
    }

    @Index(name = "CFD_FECHA_IDX")
    @Column(name = "FECHA", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getFecha() {
        return (this.fecha);
    }

    @Column(name = "NO_APROBACION", nullable = true)
    public int getNoAprobacion() {
        return (this.noAprobacion);
    }

    @Column(name = "ANO_APROBACION", nullable = true)
    public int getAnoAprobacion() {
        return (this.anoAprobacion);
    }

    @Column(name = "SERIE_ERP", nullable = true, length = 30)
    public String getSerieErp() {
        return serieErp;
    }

    @Column(name = "FOLIO_ERP", nullable = false, length = 30)
    public String getFolioErp() {
        return (this.folioErp);
    }

    @Column(name = "FOLIO_ERP2", nullable = false, length = 30)
    public String getFolioErp2() {
        return (this.folioErp2);
    }

    @Column(name = "MONEDA", nullable = false, length = 45)
    public String getMoneda() {
        return (this.moneda);
    }

    @Column(name = "TIPO_CAMBIO", nullable = false, precision = 13, scale = 2)
    public double getTipoCambio() {
        return (this.tipoCambio);
    }

    @Column(name = "SUBTOTAL_ML", nullable = false, precision = 13, scale = 2)
    public double getSubTotalML() {
        return (this.subTotalML);
    }

    @Column(name = "IVA_ML", nullable = false, precision = 13, scale = 2)
    public double getIvaML() {
        return (this.ivaML);
    }

    @Column(name = "TOTAL_ML", nullable = false, precision = 13, scale = 2)
    public double getTotalML() {
        return (this.totalML);
    }
    
    @Column(name = "NUMERO_CLIENTE", nullable = true, length = 45)
    public String getNoCliente() {
        return (this.noCliente);
    }

    @Column(name = "FECHA_CANCELACION", nullable = true)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getFechaCancelacion() {
        return (this.fechaCancelacion);
    }

    @Index(name = "CFD_TIPO_DOC_IDX")
    @Column(name = "TIPO_DOCUMENTO", nullable = false, length = 45)
    public String getTipoDocumento() {
        return (this.tipoDocumento);
    }

    @Column(name = "TIPO_COMPROBANTE", nullable = false, length = 45)
    public int getTipoComprobante() {
        return (this.tipoComprobante);
    }

    @Index(name = "CFD_TIPO_FISCAL_IDX")
    @Column(name = "TIPO_FISCAL", nullable = false, precision = 1, scale = 0)
    public int getTipoFiscal() {
        return (this.tipoFiscal);
    }

    @Index(name = "CFD_FORMA_PAG_IDX")
    @Column(name = "FORMA_PAGO", nullable = false)
    public int getFormaPago() {
        return (this.formaPago);
    }

    @Column(name = "PARCIALIDAD", nullable = false)
    public int getParcialidad() {
        return (this.parcialidad);
    }

    @Column(name = "FACTURA_ORIGEN", nullable = true, length = 70)
    public String getFacturaOrigen() {
        return facturaOrigen;
    }

    @Column(name = "SISTEMA", nullable = false)
    public int getSistema() {
        return (this.sistema);
    }

    @Column(name = "TABLA_XML", nullable = true, length = 30)
    public String getTablaXml() {
        return (this.tablaXml);
    }

    @Index(name = "CFD_ST_DOC_IDX")
    @Column(name = "ESTATUS_DOC", nullable = false, precision = 1, scale = 0)
    public int getEstatusDoc() {
        return (this.estatusDoc);
    }

    @Index(name = "CFD_ST_ENT_IDX")
    @Column(name = "ESTATUS_ENT", nullable = false, precision = 1, scale = 0)
    public int getEstatusEnt() {
        return (this.estatusEnt);
    }

    @Index(name = "CFD_ST_MAIL_IDX")
    @Column(name = "ESTATUS_MAIL", nullable = false, precision = 1, scale = 0)
    public int getEstatusMail() {
        return (this.estatusMail);
    }

    @Index(name = "CFD_ST_FTP_IDX")
    @Column(name = "ESTATUS_FTP", nullable = false, precision = 1, scale = 0)
    public int getEstatusFtp() {
        return (this.estatusFtp);
    }

    @Index(name = "CFD_ST_PRINT_IDX")
    @Column(name = "ESTATUS_PRINT", nullable = false, precision = 1, scale = 0)
    public int getEstatusPrint() {
        return (this.estatusPrint);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFechaOrigen(Date fechaOrigen) {
        this.fechaOrigen = fechaOrigen;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public void setEdoDocumento(int edoDocumento) {
        this.edoDocumento = edoDocumento;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public void setFolio(double folio) {
        this.folio = folio;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setAnoAprobacion(int anoAprobacion) {
        this.anoAprobacion = anoAprobacion;
    }

    public void setNoAprobacion(int noAprobacion) {
        this.noAprobacion = noAprobacion;
    }

    public void setSerieErp(String serieErp) {
        this.serieErp = serieErp;
    }

    public void setFolioErp(String folioErp) {
        this.folioErp = folioErp;
    }

    public void setFolioErp2(String folioErp2) {this.folioErp2 = folioErp2;}

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public void setTipoCambio(double tipoCambio) {
        this.tipoCambio = tipoCambio;
    }

    public void setSubTotalML(double subTotalML) {
        this.subTotalML = subTotalML;
    }

    public void setIvaML(double ivaML) {
        this.ivaML = ivaML;
    }

    public void setTotalML(double totalML) {
        this.totalML = totalML;
    }

    public void setNoCliente(String noCliente) {
        this.noCliente = noCliente;
    }

    public void setFechaCancelacion(Date fechaCancelacion) {
        this.fechaCancelacion = fechaCancelacion;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public void setTipoComprobante(int tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    public void setTipoFiscal(int tipoFiscal) {
        this.tipoFiscal = tipoFiscal;
    }

    public void setFormaPago(int formaPago) {
        this.formaPago = formaPago;
    }

    public void setParcialidad(int parcialidad) {
        this.parcialidad = parcialidad;
    }

    public void setFacturaOrigen(String facturaOrigen) {
        this.facturaOrigen = facturaOrigen;
    }

    public void setSistema(int sistema) {
        this.sistema = sistema;
    }

    public void setTablaXml(String tablaXml) {
        this.tablaXml = tablaXml;
    }

    public void setEstatusDoc(int estatusDoc) {
        this.estatusDoc = estatusDoc;
    }

    public void setEstatusEnt(int estatusEnt) {
        this.estatusEnt = estatusEnt;
    }

    public void setEstatusMail(int estatusMail) {
        this.estatusMail = estatusMail;
    }

    public void setEstatusFtp(int estatusFtp) {
        this.estatusFtp = estatusFtp;
    }

    public void setEstatusPrint(int estatusPrint) {
        this.estatusPrint = estatusPrint;
    }

    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }
    
    public String FECHA() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(fecha);
    }

    public String ESTATUS() {
        String res = "";
        if (edoDocumento == 1) {
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

    public String Total() {
        String cantidad = "";
        Locale locMEX = new Locale("sp_MX");
        NumberFormat nf = NumberFormat.getInstance(locMEX);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        cantidad = nf.format(total);
        return cantidad;

    }
    
    @Column(name = "plantilla_ID")
    public Integer getIdPlantilla() {
        return idPlantilla;
    }

    public void setIdPlantilla(Integer idPlantilla) {
        this.idPlantilla = idPlantilla;
    }

    @Column(name = "empresa_ID")
    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }
}
