package com.ebs.complementoextdata;

/**
 * Created by eflores on 16/01/2018.
 */
public class CustomEmisorData {
    private String CURP = "";
    private CustomDomicilioData Domicilio;


    public CustomEmisorData() {
        this("", new CustomDomicilioData());
    }

    public CustomEmisorData(String CURP, CustomDomicilioData domicilio) {
        this.CURP = CURP;
        Domicilio = domicilio;
    }

    public String getCURP() {
        return this.CURP;
    }

    public CustomDomicilioData getDomicilio() {
        return this.Domicilio;
    }

    public void setCURP(String CURP) {
        this.CURP = CURP;
    }

    public void setDomicilio(CustomDomicilioData Domicilio) {
        this.Domicilio = Domicilio;
    }
}
