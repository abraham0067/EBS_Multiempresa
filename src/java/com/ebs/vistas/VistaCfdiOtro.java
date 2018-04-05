package com.ebs.vistas;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by eflores on 22/01/2018.
 */
@Entity
@Table(name = "vista_cfdi_otro")
public class VistaCfdiOtro implements Serializable{
    @Id
    @Column(name = "ID", nullable = false)
    @Getter
    @Setter
    private int id = 0;

    @Column(name = "empresa_ID")
    @Getter
    @Setter
    private Integer idEmpresa = 0;

    @Column(name = "FECHA_ORIGEN", nullable = true)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Getter
    @Setter
    private Date fechaOrigen;

    @Column(name = "SUBTOTAL", nullable = false, precision = 13, scale = 2)
    @Getter
    @Setter
    private double subTotal = 0d;

    @Column(name = "iva", nullable = false, precision = 13, scale = 2)
    @Getter
    @Setter
    private double iva = 0d;

    @Column(name = "TOTAL", nullable = false, precision = 13, scale = 2)
    @Getter
    @Setter
    private double total = 0d;

    @Column(name = "RFC", nullable = false, length = 15)
    @Getter
    @Setter
    private String rfc = "XAXX010101000";

    @Column(name = "RAZON_SOCIAL", nullable = true, length = 100)
    @Getter
    @Setter
    private String razonSocial = "";

    @Column(name = "ESTADO_DOCUMENTO", nullable = false)
    @Getter
    @Setter
    private int edoDocumento = 1;

    @Column(name = "NUMERO_FACTURA", nullable = true, length = 70)
    @Getter
    @Setter
    private String numeroFactura = "";

    @Column(name = "UUID", nullable = true, length = 70)
    @Getter
    @Setter
    private String uuid = "";

    @Column(name = "SERIE", nullable = true, length = 25)
    @Getter
    @Setter
    private String serie = "";

    @Column(name = "FECHA", nullable = false)
    @Getter
    @Setter
    private Date fecha;

    @Column(name = "SERIE_ERP", nullable = true, length = 30)
    @Getter
    @Setter
    private String serieErp = "";

    @Column(name = "FOLIO_ERP", nullable = false, length = 30)
    @Getter
    @Setter
    private String folioErp = "";

    @Column(name = "MONEDA", nullable = false, length = 45)
    @Getter
    @Setter
    private String moneda = "MXN";

    @Column(name = "TIPO_CAMBIO", nullable = false, precision = 13, scale = 2)
    @Getter
    @Setter
    private double tipoCambio = 1d;

    @Column(name = "SUBTOTAL_ML", nullable = false, precision = 13, scale = 2)
    @Getter
    @Setter
    private double subTotalML = 0;

    @Column(name = "IVA_ML", nullable = false, precision = 13, scale = 2)
    @Getter
    @Setter
    private double ivaML = 0;

    @Column(name = "TOTAL_ML", nullable = false, precision = 13, scale = 2)
    @Getter
    @Setter
    private double totalML = 0;

    @Column(name = "NUMERO_CLIENTE", nullable = true, length = 45)
    @Getter
    @Setter
    private String noCliente = "";

    @Column(name = "FECHA_CANCELACION", nullable = true)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Getter
    @Setter
    private Date fechaCancelacion;

    @Column(name = "TIPO_DOCUMENTO", nullable = false, length = 45)
    @Getter
    @Setter
    private String tipoDocumento = "FACTURA";

    @Column(name = "TIPO_COMPROBANTE", nullable = false, length = 45)
    @Getter
    @Setter
    private int tipoComprobante = 0;

    @Column(name = "TIPO_FISCAL", nullable = false, precision = 1, scale = 0)
    @Getter
    @Setter
    private int tipoFiscal = 0; // 0 - clientFiles, 1 - CFDi

    @Column(name = "FORMA_PAGO", nullable = false)
    @Getter
    @Setter
    private int formaPago = 1; // 1 - Una exhibicion, n > 1 - Paricalidades

    @Column(name = "FACTURA_ORIGEN", nullable = true, length = 70)
    @Getter
    @Setter
    private String facturaOrigen = "";

    @Column(name = "plantilla_ID")
    @Getter
    @Setter
    private Integer idPlantilla = 0;

    @Column(name = "ESTATUS_DOC", nullable = false, precision = 1, scale = 0)
    @Getter
    @Setter
    private int estatusDoc = 0;

    @Column(name = "ESTATUS_MAIL", nullable = false, precision = 1, scale = 0)
    @Getter
    @Setter
    private int estatusMail = 0;

    @Column(name = "otroId")
    @Getter
    @Setter
    private Integer idOtro;

    @Column(name = "PARAM20", nullable = true, length = 3)
    @Getter
    @Setter
    private String param20 = "";

    @Column(name = "NO_POLIZA_SEGURO", nullable = false, length = 20)
    @Getter
    @Setter
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


}
