package fe.db;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by eflores on 19/10/2017.
 */
@Entity
@Table(name = "M_CFD_PAGOS")
public class MCfdPagos {
    private int id;
    private Integer anoAprobacion;
    private int estadoDocumento;
    private int estatusDoc;
    private int estatusEnt;
    private int estatusFtp;
    private int estatusMail;
    private int estatusPrint;
    private String facturaOrigen;
    private Timestamp fecha;
    private Timestamp fechaCancelacion;
    private Timestamp fechaOrigen;
    private Integer folio;
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
    private Integer empresaId;
    private Integer plantillaId;

    @Id
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "ANO_APROBACION", nullable = true)
    public Integer getAnoAprobacion() {
        return anoAprobacion;
    }

    public void setAnoAprobacion(Integer anoAprobacion) {
        this.anoAprobacion = anoAprobacion;
    }

    @Basic
    @Column(name = "ESTADO_DOCUMENTO", nullable = false)
    public int getEstadoDocumento() {
        return estadoDocumento;
    }

    public void setEstadoDocumento(int estadoDocumento) {
        this.estadoDocumento = estadoDocumento;
    }

    @Basic
    @Column(name = "ESTATUS_DOC", nullable = false)
    public int getEstatusDoc() {
        return estatusDoc;
    }

    public void setEstatusDoc(int estatusDoc) {
        this.estatusDoc = estatusDoc;
    }

    @Basic
    @Column(name = "ESTATUS_ENT", nullable = false)
    public int getEstatusEnt() {
        return estatusEnt;
    }

    public void setEstatusEnt(int estatusEnt) {
        this.estatusEnt = estatusEnt;
    }

    @Basic
    @Column(name = "ESTATUS_FTP", nullable = false)
    public int getEstatusFtp() {
        return estatusFtp;
    }

    public void setEstatusFtp(int estatusFtp) {
        this.estatusFtp = estatusFtp;
    }

    @Basic
    @Column(name = "ESTATUS_MAIL", nullable = false)
    public int getEstatusMail() {
        return estatusMail;
    }

    public void setEstatusMail(int estatusMail) {
        this.estatusMail = estatusMail;
    }

    @Basic
    @Column(name = "ESTATUS_PRINT", nullable = false)
    public int getEstatusPrint() {
        return estatusPrint;
    }

    public void setEstatusPrint(int estatusPrint) {
        this.estatusPrint = estatusPrint;
    }

    @Basic
    @Column(name = "FACTURA_ORIGEN", nullable = true, length = 70)
    public String getFacturaOrigen() {
        return facturaOrigen;
    }

    public void setFacturaOrigen(String facturaOrigen) {
        this.facturaOrigen = facturaOrigen;
    }

    @Basic
    @Column(name = "FECHA", nullable = false)
    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    @Basic
    @Column(name = "FECHA_CANCELACION", nullable = true)
    public Timestamp getFechaCancelacion() {
        return fechaCancelacion;
    }

    public void setFechaCancelacion(Timestamp fechaCancelacion) {
        this.fechaCancelacion = fechaCancelacion;
    }

    @Basic
    @Column(name = "FECHA_ORIGEN", nullable = true)
    public Timestamp getFechaOrigen() {
        return fechaOrigen;
    }

    public void setFechaOrigen(Timestamp fechaOrigen) {
        this.fechaOrigen = fechaOrigen;
    }

    @Basic
    @Column(name = "FOLIO", nullable = true)
    public Integer getFolio() {
        return folio;
    }

    public void setFolio(Integer folio) {
        this.folio = folio;
    }

    @Basic
    @Column(name = "FOLIO_ERP", nullable = false, length = 30)
    public String getFolioErp() {
        return folioErp;
    }

    public void setFolioErp(String folioErp) {
        this.folioErp = folioErp;
    }

    @Basic
    @Column(name = "FORMA_PAGO", nullable = false)
    public int getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(int formaPago) {
        this.formaPago = formaPago;
    }

    @Basic
    @Column(name = "IVA", nullable = false, precision = 0)
    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    @Basic
    @Column(name = "IVA_ML", nullable = false, precision = 0)
    public double getIvaMl() {
        return ivaMl;
    }

    public void setIvaMl(double ivaMl) {
        this.ivaMl = ivaMl;
    }

    @Basic
    @Column(name = "MONEDA", nullable = true, length = 45)
    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    @Basic
    @Column(name = "NO_APROBACION", nullable = true)
    public Integer getNoAprobacion() {
        return noAprobacion;
    }

    public void setNoAprobacion(Integer noAprobacion) {
        this.noAprobacion = noAprobacion;
    }

    @Basic
    @Column(name = "NUMERO_CLIENTE", nullable = true, length = 45)
    public String getNumeroCliente() {
        return numeroCliente;
    }

    public void setNumeroCliente(String numeroCliente) {
        this.numeroCliente = numeroCliente;
    }

    @Basic
    @Column(name = "NUMERO_FACTURA", nullable = true, length = 70)
    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    @Basic
    @Column(name = "PARCIALIDAD", nullable = false)
    public int getParcialidad() {
        return parcialidad;
    }

    public void setParcialidad(int parcialidad) {
        this.parcialidad = parcialidad;
    }

    @Basic
    @Column(name = "RAZON_SOCIAL", nullable = true, length = 256)
    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    @Basic
    @Column(name = "RFC", nullable = false, length = 15)
    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    @Basic
    @Column(name = "SERIE", nullable = true, length = 25)
    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    @Basic
    @Column(name = "SERIE_ERP", nullable = true, length = 30)
    public String getSerieErp() {
        return serieErp;
    }

    public void setSerieErp(String serieErp) {
        this.serieErp = serieErp;
    }

    @Basic
    @Column(name = "SISTEMA", nullable = false)
    public int getSistema() {
        return sistema;
    }

    public void setSistema(int sistema) {
        this.sistema = sistema;
    }

    @Basic
    @Column(name = "SUBTOTAL", nullable = false, precision = 0)
    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    @Basic
    @Column(name = "SUBTOTAL_ML", nullable = false, precision = 0)
    public double getSubtotalMl() {
        return subtotalMl;
    }

    public void setSubtotalMl(double subtotalMl) {
        this.subtotalMl = subtotalMl;
    }

    @Basic
    @Column(name = "SUCURSAL", nullable = true)
    public Integer getSucursal() {
        return sucursal;
    }

    public void setSucursal(Integer sucursal) {
        this.sucursal = sucursal;
    }

    @Basic
    @Column(name = "TABLA_XML", nullable = true, length = 30)
    public String getTablaXml() {
        return tablaXml;
    }

    public void setTablaXml(String tablaXml) {
        this.tablaXml = tablaXml;
    }

    @Basic
    @Column(name = "TIPO_CAMBIO", nullable = false, precision = 0)
    public double getTipoCambio() {
        return tipoCambio;
    }

    public void setTipoCambio(double tipoCambio) {
        this.tipoCambio = tipoCambio;
    }

    @Basic
    @Column(name = "TIPO_COMPROBANTE", nullable = false)
    public int getTipoComprobante() {
        return tipoComprobante;
    }

    public void setTipoComprobante(int tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    @Basic
    @Column(name = "TIPO_DOCUMENTO", nullable = false, length = 45)
    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    @Basic
    @Column(name = "TIPO_FISCAL", nullable = false)
    public int getTipoFiscal() {
        return tipoFiscal;
    }

    public void setTipoFiscal(int tipoFiscal) {
        this.tipoFiscal = tipoFiscal;
    }

    @Basic
    @Column(name = "TOTAL", nullable = false, precision = 0)
    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Basic
    @Column(name = "TOTAL_ML", nullable = false, precision = 0)
    public double getTotalMl() {
        return totalMl;
    }

    public void setTotalMl(double totalMl) {
        this.totalMl = totalMl;
    }

    @Basic
    @Column(name = "UNO", nullable = true, length = 255)
    public String getUno() {
        return uno;
    }

    public void setUno(String uno) {
        this.uno = uno;
    }

    @Basic
    @Column(name = "UUID", nullable = true, length = 100)
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Basic
    @Column(name = "EMPRESA_ID", nullable = true)
    public Integer getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Integer empresaId) {
        this.empresaId = empresaId;
    }

    @Basic
    @Column(name = "PLANTILLA_ID", nullable = true)
    public Integer getPlantillaId() {
        return plantillaId;
    }

    public void setPlantillaId(Integer plantillaId) {
        this.plantillaId = plantillaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MCfdPagos mCfdPagos = (MCfdPagos) o;

        if (id != mCfdPagos.id) return false;
        if (estadoDocumento != mCfdPagos.estadoDocumento) return false;
        if (estatusDoc != mCfdPagos.estatusDoc) return false;
        if (estatusEnt != mCfdPagos.estatusEnt) return false;
        if (estatusFtp != mCfdPagos.estatusFtp) return false;
        if (estatusMail != mCfdPagos.estatusMail) return false;
        if (estatusPrint != mCfdPagos.estatusPrint) return false;
        if (formaPago != mCfdPagos.formaPago) return false;
        if (Double.compare(mCfdPagos.iva, iva) != 0) return false;
        if (Double.compare(mCfdPagos.ivaMl, ivaMl) != 0) return false;
        if (parcialidad != mCfdPagos.parcialidad) return false;
        if (sistema != mCfdPagos.sistema) return false;
        if (Double.compare(mCfdPagos.subtotal, subtotal) != 0) return false;
        if (Double.compare(mCfdPagos.subtotalMl, subtotalMl) != 0) return false;
        if (Double.compare(mCfdPagos.tipoCambio, tipoCambio) != 0) return false;
        if (tipoComprobante != mCfdPagos.tipoComprobante) return false;
        if (tipoFiscal != mCfdPagos.tipoFiscal) return false;
        if (Double.compare(mCfdPagos.total, total) != 0) return false;
        if (Double.compare(mCfdPagos.totalMl, totalMl) != 0) return false;
        if (anoAprobacion != null ? !anoAprobacion.equals(mCfdPagos.anoAprobacion) : mCfdPagos.anoAprobacion != null)
            return false;
        if (facturaOrigen != null ? !facturaOrigen.equals(mCfdPagos.facturaOrigen) : mCfdPagos.facturaOrigen != null)
            return false;
        if (fecha != null ? !fecha.equals(mCfdPagos.fecha) : mCfdPagos.fecha != null) return false;
        if (fechaCancelacion != null ? !fechaCancelacion.equals(mCfdPagos.fechaCancelacion) : mCfdPagos.fechaCancelacion != null)
            return false;
        if (fechaOrigen != null ? !fechaOrigen.equals(mCfdPagos.fechaOrigen) : mCfdPagos.fechaOrigen != null)
            return false;
        if (folio != null ? !folio.equals(mCfdPagos.folio) : mCfdPagos.folio != null) return false;
        if (folioErp != null ? !folioErp.equals(mCfdPagos.folioErp) : mCfdPagos.folioErp != null) return false;
        if (moneda != null ? !moneda.equals(mCfdPagos.moneda) : mCfdPagos.moneda != null) return false;
        if (noAprobacion != null ? !noAprobacion.equals(mCfdPagos.noAprobacion) : mCfdPagos.noAprobacion != null)
            return false;
        if (numeroCliente != null ? !numeroCliente.equals(mCfdPagos.numeroCliente) : mCfdPagos.numeroCliente != null)
            return false;
        if (numeroFactura != null ? !numeroFactura.equals(mCfdPagos.numeroFactura) : mCfdPagos.numeroFactura != null)
            return false;
        if (razonSocial != null ? !razonSocial.equals(mCfdPagos.razonSocial) : mCfdPagos.razonSocial != null)
            return false;
        if (rfc != null ? !rfc.equals(mCfdPagos.rfc) : mCfdPagos.rfc != null) return false;
        if (serie != null ? !serie.equals(mCfdPagos.serie) : mCfdPagos.serie != null) return false;
        if (serieErp != null ? !serieErp.equals(mCfdPagos.serieErp) : mCfdPagos.serieErp != null) return false;
        if (sucursal != null ? !sucursal.equals(mCfdPagos.sucursal) : mCfdPagos.sucursal != null) return false;
        if (tablaXml != null ? !tablaXml.equals(mCfdPagos.tablaXml) : mCfdPagos.tablaXml != null) return false;
        if (tipoDocumento != null ? !tipoDocumento.equals(mCfdPagos.tipoDocumento) : mCfdPagos.tipoDocumento != null)
            return false;
        if (uno != null ? !uno.equals(mCfdPagos.uno) : mCfdPagos.uno != null) return false;
        if (uuid != null ? !uuid.equals(mCfdPagos.uuid) : mCfdPagos.uuid != null) return false;
        if (empresaId != null ? !empresaId.equals(mCfdPagos.empresaId) : mCfdPagos.empresaId != null) return false;
        if (plantillaId != null ? !plantillaId.equals(mCfdPagos.plantillaId) : mCfdPagos.plantillaId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + (anoAprobacion != null ? anoAprobacion.hashCode() : 0);
        result = 31 * result + estadoDocumento;
        result = 31 * result + estatusDoc;
        result = 31 * result + estatusEnt;
        result = 31 * result + estatusFtp;
        result = 31 * result + estatusMail;
        result = 31 * result + estatusPrint;
        result = 31 * result + (facturaOrigen != null ? facturaOrigen.hashCode() : 0);
        result = 31 * result + (fecha != null ? fecha.hashCode() : 0);
        result = 31 * result + (fechaCancelacion != null ? fechaCancelacion.hashCode() : 0);
        result = 31 * result + (fechaOrigen != null ? fechaOrigen.hashCode() : 0);
        result = 31 * result + (folio != null ? folio.hashCode() : 0);
        result = 31 * result + (folioErp != null ? folioErp.hashCode() : 0);
        result = 31 * result + formaPago;
        temp = Double.doubleToLongBits(iva);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(ivaMl);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (moneda != null ? moneda.hashCode() : 0);
        result = 31 * result + (noAprobacion != null ? noAprobacion.hashCode() : 0);
        result = 31 * result + (numeroCliente != null ? numeroCliente.hashCode() : 0);
        result = 31 * result + (numeroFactura != null ? numeroFactura.hashCode() : 0);
        result = 31 * result + parcialidad;
        result = 31 * result + (razonSocial != null ? razonSocial.hashCode() : 0);
        result = 31 * result + (rfc != null ? rfc.hashCode() : 0);
        result = 31 * result + (serie != null ? serie.hashCode() : 0);
        result = 31 * result + (serieErp != null ? serieErp.hashCode() : 0);
        result = 31 * result + sistema;
        temp = Double.doubleToLongBits(subtotal);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(subtotalMl);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (sucursal != null ? sucursal.hashCode() : 0);
        result = 31 * result + (tablaXml != null ? tablaXml.hashCode() : 0);
        temp = Double.doubleToLongBits(tipoCambio);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + tipoComprobante;
        result = 31 * result + (tipoDocumento != null ? tipoDocumento.hashCode() : 0);
        result = 31 * result + tipoFiscal;
        temp = Double.doubleToLongBits(total);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(totalMl);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (uno != null ? uno.hashCode() : 0);
        result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
        result = 31 * result + (empresaId != null ? empresaId.hashCode() : 0);
        result = 31 * result + (plantillaId != null ? plantillaId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MCfdPagos{" +
                "id=" + id +
                ", folio=" + folio +
                ", serie='" + serie + '\'' +
                ", total=" + total +
                ", uuid='" + uuid + '\'' +
                ", empresaId=" + empresaId +
                ", plantillaId=" + plantillaId +
                '}';
    }
}
