/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.model.fmanual;

import com.ebs.catalogos.TipoImpuesto;
import com.ebs.util.FloatsNumbersUtil;
import com.mchange.lang.FloatUtils;


import java.io.Serializable;

/**
 * @author Eduardo C. Flores Ambrosio <EB&S>
 */
public class ImpuestoContainer implements Serializable {
    /*
     * Tipo de impuesto del objeto
     */
    
    private TipoImpuesto tipo = TipoImpuesto.TRASLADO;
    /*
     * Clave de impuesto
     */
    
    private String impuesto;
    
    private String descripcionImpuesto;
    /*
     * Cantidad base
     */
    
    private String base;
    /*
     * Clave del tipo de factos
     */
    
    private String tipoFactor;
    /*
     * Tasa
     */
    
    private String tasaOcuota;
    /*
     * Requerido en caso de que el tipo de factos se diferente de EXENTO()
     */
    
    private String importe;
    
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

    public ImpuestoContainer(TipoImpuesto tipo,  String tipoFactor) {
        this.tipo = tipo;
        this.tipoFactor = tipoFactor;
        impuesto = "";
        descripcionImpuesto = "";
        base = "";
        tasaOcuota = "";
        importe = "";
        aplicar = false;
    }

    public ImpuestoContainer( String impuesto,  String base,  String tipoFactor,  String tasaOcuota,  TipoImpuesto tipo) {
        this.impuesto = impuesto;
        this.base = base;
        this.tipoFactor = tipoFactor;
        this.tasaOcuota = tasaOcuota;
        this.tipo = tipo;
        descripcionImpuesto = "";
        importe = "";
        aplicar = false;

    }

    public void loadData( String impuesto,  String base,  String tipoFactor,
                          String tasaOcuota,  TipoImpuesto tipo) {
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

    public TipoImpuesto getTipo() {
        return tipo;
    }

    public void setTipo(TipoImpuesto tipo) {
        this.tipo = tipo;
    }

    public String getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(String impuesto) {
        this.impuesto = impuesto;
    }

    public String getDescripcionImpuesto() {
        return descripcionImpuesto;
    }

    public void setDescripcionImpuesto(String descripcionImpuesto) {
        this.descripcionImpuesto = descripcionImpuesto;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getTipoFactor() {
        return tipoFactor;
    }

    public void setTipoFactor(String tipoFactor) {
        this.tipoFactor = tipoFactor;
    }

    public String getTasaOcuota() {
        return tasaOcuota;
    }

    public void setTasaOcuota(String tasaOcuota) {
        this.tasaOcuota = tasaOcuota;
    }

    public String getImporte() {
        return importe;
    }

    public void setImporte(String importe) {
        this.importe = importe;
    }

    public boolean isAplicar() {
        return aplicar;
    }

    public void setAplicar(boolean aplicar) {
        this.aplicar = aplicar;
    }
}
