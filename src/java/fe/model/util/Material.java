/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fe.model.util;

import java.io.Serializable;

/**
 *
 * @author HpAbraham
 */
public class Material implements Serializable{
    private int NumFactura;
    private String NumCajas;
    private String Material;
    private String Recibo;
    private String ReciboFecha;

    public Material() {
    }

    public Material(int NumFactura, String NumCajas, String Material, String Recibo, String ReciboFecha) {
        this.NumFactura = NumFactura;
        this.NumCajas = NumCajas;
        this.Material = Material;
        this.Recibo = Recibo;
        this.ReciboFecha = ReciboFecha;
    } 

    public String getNumCajas() {
        return NumCajas;
    }

    public void setNumCajas(String NumCajas) {
        this.NumCajas = NumCajas;
    }

    public String getMaterial() {
        return Material;
    }

    public void setMaterial(String Material) {
        this.Material = Material;
    }

    public String getRecibo() {
        return Recibo;
    }

    public void setRecibo(String Recibo) {
        this.Recibo = Recibo;
    }

    public String getReciboFecha() {
        return ReciboFecha;
    }

    public void setReciboFecha(String ReciboFecha) {
        this.ReciboFecha = ReciboFecha;
    }

    public int getNumFactura() {
        return NumFactura;
    }

    public void setNumFactura(int NumFactura) {
        this.NumFactura = NumFactura;
    }

     
     
    
    
}
