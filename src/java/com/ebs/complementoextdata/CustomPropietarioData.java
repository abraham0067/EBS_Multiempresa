package com.ebs.complementoextdata;

/**
 * Created by eflores on 16/01/2018.
 */
public class CustomPropietarioData {
    private String NumRegIdTrib;
    private CustomCatalogoData ResidenciaFiscal;
    private CustomDomicilioData domicilioData;

    public String getNumRegIdTrib() {
        return this.NumRegIdTrib;
    }

    public CustomCatalogoData getResidenciaFiscal() {
        return this.ResidenciaFiscal;
    }

    public CustomDomicilioData getDomicilioData() {
        return this.domicilioData;
    }

    public void setNumRegIdTrib(String NumRegIdTrib) {
        this.NumRegIdTrib = NumRegIdTrib;
    }

    public void setResidenciaFiscal(CustomCatalogoData ResidenciaFiscal) {
        this.ResidenciaFiscal = ResidenciaFiscal;
    }

    public void setDomicilioData(CustomDomicilioData domicilioData) {
        this.domicilioData = domicilioData;
    }
}
