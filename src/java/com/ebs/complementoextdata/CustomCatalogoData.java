package com.ebs.complementoextdata;

/**
 * Created by eflores on 16/01/2018.
 */
public class CustomCatalogoData {
    private String Clave = "";
    private String Descripcion = "";

    public CustomCatalogoData() {
        this("", "");
    }

    public CustomCatalogoData(String clave, String descripcion) {
        Clave = clave;
        Descripcion = descripcion;
    }

    public String getClave() {
        return this.Clave;
    }

    public String getDescripcion() {
        return this.Descripcion;
    }

    public void setClave(String Clave) {
        this.Clave = Clave;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }
}
