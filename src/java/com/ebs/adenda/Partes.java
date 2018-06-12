package com.ebs.adenda;

public class Partes {

    private int posicion;
    private String numeroMaterial;
    private String descripcionMterial;
    private double cantidadMaterial;
    private String unidadMedida;
    private double precioUnitario;
    private double montoLinea;
    private String odenCompra;
    private String codigoImpuesto;

    public int getPosicion() {
        return this.posicion;
    }

    public String getNumeroMaterial() {
        return this.numeroMaterial;
    }

    public String getDescripcionMterial() {
        return this.descripcionMterial;
    }

    public double getCantidadMaterial() {
        return this.cantidadMaterial;
    }

    public String getUnidadMedida() {
        return this.unidadMedida;
    }

    public double getPrecioUnitario() {
        return this.precioUnitario;
    }

    public double getMontoLinea() {
        return this.montoLinea;
    }

    public String getOdenCompra() {
        return this.odenCompra;
    }

    public String getCodigoImpuesto() {
        return this.codigoImpuesto;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public void setNumeroMaterial(String numeroMaterial) {
        this.numeroMaterial = numeroMaterial;
    }

    public void setDescripcionMterial(String descripcionMterial) {
        this.descripcionMterial = descripcionMterial;
    }

    public void setCantidadMaterial(double cantidadMaterial) {
        this.cantidadMaterial = cantidadMaterial;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public void setMontoLinea(double montoLinea) {
        this.montoLinea = montoLinea;
    }

    public void setOdenCompra(String odenCompra) {
        this.odenCompra = odenCompra;
    }

    public void setCodigoImpuesto(String codigoImpuesto) {
        this.codigoImpuesto = codigoImpuesto;
    }
}
