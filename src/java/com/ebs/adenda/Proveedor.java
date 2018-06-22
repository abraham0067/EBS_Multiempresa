package com.ebs.adenda;

public class Proveedor {

    private String numero;
    private String nombre;
    private String referencia;

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }



    public String getNumero() {
        return this.numero;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
