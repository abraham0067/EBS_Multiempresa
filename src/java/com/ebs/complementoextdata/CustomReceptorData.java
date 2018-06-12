package com.ebs.complementoextdata;

/**
 * Created by eflores on 16/01/2018.
 */
public class CustomReceptorData {
    private String NumRegIdTrib;
    private CustomDomicilioData Domicilio;

    public CustomReceptorData() {
        this("",new CustomDomicilioData());
    }

    public CustomReceptorData(String numRegIdTrib, CustomDomicilioData domicilio) {
        NumRegIdTrib = numRegIdTrib;
        Domicilio = domicilio;
    }

    public String getNumRegIdTrib() {
        return this.NumRegIdTrib;
    }

    public CustomDomicilioData getDomicilio() {
        return this.Domicilio;
    }

    public void setNumRegIdTrib(String NumRegIdTrib) {
        this.NumRegIdTrib = NumRegIdTrib;
    }

    public void setDomicilio(CustomDomicilioData Domicilio) {
        this.Domicilio = Domicilio;
    }
}
