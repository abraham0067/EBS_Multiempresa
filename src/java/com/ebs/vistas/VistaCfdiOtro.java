package com.ebs.vistas;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by eflores on 22/01/2018.
 */
@Entity
@Table(name = "vista_cfdi_otro")
public class VistaCfdiOtro implements Serializable{
    @Id
    @Column(name = "ID", nullable = false)
    private int id = 0;

    @Column(name = "empresa_ID")
    private Integer idEmpresa = 0;

    @Column(name = "FECHA_ORIGEN", nullable = true)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaOrigen;

    @Column(name = "SUBTOTAL", nullable = false, precision = 13, scale = 2)
    private double subTotal = 0d;

    @Column(name = "iva", nullable = false, precision = 13, scale = 2)
    private double iva = 0d;

    @Column(name = "TOTAL", nullable = false, precision = 13, scale = 2)
    private double total = 0d;

    @Column(name = "RFC", nullable = false, length = 15)
    private String rfc = "XAXX010101000";

    @Column(name = "RAZON_SOCIAL", nullable = true, length = 100)
    private String razonSocial = "";

    @Column(name = "ESTADO_DOCUMENTO", nullable = false)
    private int edoDocumento = 1;

    @Column(name = "NUMERO_FACTURA", nullable = true, length = 70)
    private String numeroFactura = "";

    @Column(name = "UUID", nullable = true, length = 70)
    private String uuid = "";

    @Column(name = "SERIE", nullable = true, length = 25)
    private String serie = "";

    @Column(name = "FECHA", nullable = false)
    private Date fecha;

    @Column(name = "SERIE_ERP", nullable = true, length = 30)
    private String serieErp = "";

    @Column(name = "FOLIO_ERP", nullable = false, length = 30)
    private String folioErp = "";

    @Column(name = "MONEDA", nullable = false, length = 45)
    private String moneda = "MXN";

    @Column(name = "TIPO_CAMBIO", nullable = false, precision = 13, scale = 2)
    private double tipoCambio = 1d;

    @Column(name = "SUBTOTAL_ML", nullable = false, precision = 13, scale = 2)
    private double subTotalML = 0;

    @Column(name = "IVA_ML", nullable = false, precision = 13, scale = 2)
    private double ivaML = 0;

    @Column(name = "TOTAL_ML", nullable = false, precision = 13, scale = 2)
    private double totalML = 0;

    @Column(name = "NUMERO_CLIENTE", nullable = true, length = 45)
    private String noCliente = "";

    @Column(name = "FECHA_CANCELACION", nullable = true)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fechaCancelacion;

    @Column(name = "TIPO_DOCUMENTO", nullable = false, length = 45)
    private String tipoDocumento = "FACTURA";

    @Column(name = "TIPO_COMPROBANTE", nullable = false, length = 45)
    private int tipoComprobante = 0;

    @Column(name = "TIPO_FISCAL", nullable = false, precision = 1, scale = 0)
    private int tipoFiscal = 0; // 0 - clientFiles, 1 - CFDi

    @Column(name = "FORMA_PAGO", nullable = false)
    private int formaPago = 1; // 1 - Una exhibicion, n > 1 - Paricalidades

    @Column(name = "FACTURA_ORIGEN", nullable = true, length = 70)
    private String facturaOrigen = "";

    @Column(name = "plantilla_ID")
    private Integer idPlantilla = 0;

    @Column(name = "ESTATUS_DOC", nullable = false, precision = 1, scale = 0)
    private int estatusDoc = 0;

    @Column(name = "ESTATUS_MAIL", nullable = false, precision = 1, scale = 0)
    private int estatusMail = 0;

    @Column(name = "otroId")
    private Integer idOtro;

    @Column(name = "PARAM20", nullable = true, length = 3)
    private String param20 = "";

    @Column(name = "NO_POLIZA_SEGURO", nullable = false, length = 20)
    private String noPolizaSeguro = "";

    public String getEstadoDocumentoAsString() {
        String res = "";
        if (edoDocumento == 1) {
            res = "GENERADA";
        } else {
            res = "CANCELADA";
        }
        return res;
    }
    public String getEstatusEnvioCorreoAsString() {
        String resp = "ERROR";
        switch (estatusMail){
            case 0:
                resp = "NO";
                break;
            case 1:
                resp = "OK";
                break;
        }

        return resp;
    }


    public int getId() {
        return this.id;
    }

    public Integer getIdEmpresa() {
        return this.idEmpresa;
    }

    public Date getFechaOrigen() {
        return this.fechaOrigen;
    }

    public double getSubTotal() {
        return this.subTotal;
    }

    public double getIva() {
        return this.iva;
    }

    public double getTotal() {
        return this.total;
    }

    public String getRfc() {
        return this.rfc;
    }

    public String getRazonSocial() {
        return this.razonSocial;
    }

    public int getEdoDocumento() {
        return this.edoDocumento;
    }

    public String getNumeroFactura() {
        return this.numeroFactura;
    }

    public String getUuid() {
        return this.uuid;
    }

    public String getSerie() {
        return this.serie;
    }

    public Date getFecha() {
        return this.fecha;
    }

    public String getSerieErp() {
        return this.serieErp;
    }

    public String getFolioErp() {
        return this.folioErp;
    }

    public String getMoneda() {
        return this.moneda;
    }

    public double getTipoCambio() {
        return this.tipoCambio;
    }

    public double getSubTotalML() {
        return this.subTotalML;
    }

    public double getIvaML() {
        return this.ivaML;
    }

    public double getTotalML() {
        return this.totalML;
    }

    public String getNoCliente() {
        return this.noCliente;
    }

    public Date getFechaCancelacion() {
        return this.fechaCancelacion;
    }

    public String getTipoDocumento() {
        return this.tipoDocumento;
    }

    public int getTipoComprobante() {
        return this.tipoComprobante;
    }

    public int getTipoFiscal() {
        return this.tipoFiscal;
    }

    public int getFormaPago() {
        return this.formaPago;
    }

    public String getFacturaOrigen() {
        return this.facturaOrigen;
    }

    public Integer getIdPlantilla() {
        return this.idPlantilla;
    }

    public int getEstatusDoc() {
        return this.estatusDoc;
    }

    public int getEstatusMail() {
        return this.estatusMail;
    }

    public Integer getIdOtro() {
        return this.idOtro;
    }

    public String getParam20() {
        return this.param20;
    }

    public String getNoPolizaSeguro() {
        return this.noPolizaSeguro;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
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

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setSerieErp(String serieErp) {
        this.serieErp = serieErp;
    }

    public void setFolioErp(String folioErp) {
        this.folioErp = folioErp;
    }

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

    public void setFacturaOrigen(String facturaOrigen) {
        this.facturaOrigen = facturaOrigen;
    }

    public void setIdPlantilla(Integer idPlantilla) {
        this.idPlantilla = idPlantilla;
    }

    public void setEstatusDoc(int estatusDoc) {
        this.estatusDoc = estatusDoc;
    }

    public void setEstatusMail(int estatusMail) {
        this.estatusMail = estatusMail;
    }

    public void setIdOtro(Integer idOtro) {
        this.idOtro = idOtro;
    }

    public void setParam20(String param20) {
        this.param20 = param20;
    }

    public void setNoPolizaSeguro(String noPolizaSeguro) {
        this.noPolizaSeguro = noPolizaSeguro;
    }
}
