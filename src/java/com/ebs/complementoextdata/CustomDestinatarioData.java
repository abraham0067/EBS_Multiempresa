package com.ebs.complementoextdata;

/**
 * Created by eflores on 16/01/2018.
 */
public class CustomDestinatarioData {
    private String NumRegIdTrib = "";
    private String Nombre = "";
    private CustomDomicilioData Domicilio;

    public CustomDestinatarioData() {
        new CustomDomicilioData();
    }

    public CustomDestinatarioData(String numRegIdTrib, String nombre, CustomDomicilioData domicilio) {
        NumRegIdTrib = numRegIdTrib;
        Nombre = nombre;
        Domicilio = domicilio;
    }

    public String getNumRegIdTrib() {
        return this.NumRegIdTrib;
    }

    public String getNombre() {
        return this.Nombre;
    }

    public CustomDomicilioData getDomicilio() {
        return this.Domicilio;
    }

    public void setNumRegIdTrib(String NumRegIdTrib) {
        this.NumRegIdTrib = NumRegIdTrib;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public void setDomicilio(CustomDomicilioData Domicilio) {
        this.Domicilio = Domicilio;
    }
}
