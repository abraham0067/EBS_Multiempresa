/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.model.fmanual;

import com.ebs.catalogos.TipoImpuesto;
import com.ebs.util.FloatsNumbersUtil;
import com.mchange.lang.FloatUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Eduardo C. Flores Ambrosio <EB&S>
 */
public class ImpuestoContainer implements Serializable {
    /*
     * Tipo de impuesto del objeto
     */
    @Getter
    @Setter
    private TipoImpuesto tipo = TipoImpuesto.TRASLADO;
    /*
     * Clave de impuesto
     */
    @Getter
    @Setter
    private String impuesto;
    @Getter
    @Setter
    private String descripcionImpuesto;
    /*
     * Cantidad base
     */
    @Getter
    @Setter
    private String base;
    /*
     * Clave del tipo de factos
     */
    @Getter
    @Setter
    private String tipoFactor;
    /*
     * Tasa
     */
    @Getter
    @Setter
    private String tasaOcuota;
    /*
     * Requerido en caso de que el tipo de factos se diferente de EXENTO()
     */
    @Getter
    @Setter
    private String importe;
    @Getter
    @Setter
    private boolean aplicar;

    public ImpuestoContainer(TipoImpuesto tipo) {
        this.tipo = tipo;
        impuesto = "";
        descripcionImpuesto = "";
        base = "";
        tipoFactor = "";
        tasaOcuota = "";
        importe = "";
        aplicar = false;
    }

    public ImpuestoContainer(TipoImpuesto tipo, @NonNull String tipoFactor) {
        this.tipo = tipo;
        this.tipoFactor = tipoFactor;
        impuesto = "";
        descripcionImpuesto = "";
        base = "";
        tasaOcuota = "";
        importe = "";
        aplicar = false;
    }

    public ImpuestoContainer(@NonNull String impuesto, @NonNull String base, @NonNull String tipoFactor, @NonNull String tasaOcuota, @NonNull TipoImpuesto tipo) {
        this.impuesto = impuesto;
        this.base = base;
        this.tipoFactor = tipoFactor;
        this.tasaOcuota = tasaOcuota;
        this.tipo = tipo;
        descripcionImpuesto = "";
        importe = "";
        aplicar = false;

    }

    public void loadData(@NonNull String impuesto, @NonNull String base, @NonNull String tipoFactor,
                         @NonNull String tasaOcuota, @NonNull TipoImpuesto tipo) {
        this.impuesto = impuesto;
        this.base = base;
        this.tipoFactor = tipoFactor;
        this.tasaOcuota = tasaOcuota;
        this.tipo = tipo;
        if (descripcionImpuesto == null || descripcionImpuesto.isEmpty())
            descripcionImpuesto = "";
        importe = "";
    }


    public void calcularImpuesto() {
        if (!this.tipoFactor.toLowerCase().contains("EXENTO") && !this.tipoFactor.isEmpty()) {
            try {
                Double tmp1 = Double.parseDouble(base);
                Double tmp2 = Double.parseDouble(tasaOcuota);
                //Formatear a los decimales requeridos por la moneda
                importe = String.valueOf(FloatsNumbersUtil.round(tmp1 * tmp2, 2));//Hacer el redondeo a 2
            } catch (NumberFormatException ex) {
                importe = "0.00";
            }
        } else if (this.tipoFactor.toLowerCase().contains("EXENTO")) {
            // TODO: 08/06/2017 PONER VALORES CORRECTOS CUANDO EL CONCEPTO ES EXENTO DE UN  IMPUESTO
            importe = "0.0";
        }
    }

}
