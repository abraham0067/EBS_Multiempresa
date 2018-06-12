package com.ebs.complementoextdata;

/**
 * Created by eflores on 16/01/2018.
 */
public class CustomDomicilioData {
    private String Calle = "";
    private String NumeroExterior = "";
    private String NumeroInterior = "";
    private String Colonia = "";
    private String Localidad = "";
    private String Referencia = "";
    private  String Municipio = "";
    private  String Estado = "";
    private CustomCatalogoData Pais;
    private String CodigoPostal = "";

    public CustomDomicilioData() {
        this("", "", "", "", "", "", "", "", new CustomCatalogoData("MEX","MEXICO"), "");
    }

    public CustomDomicilioData(String calle, String numeroExterior, String numeroInterior, String colonia, String localidad,
                               String referencia, String municipio, String estado, CustomCatalogoData pais, String codigoPostal) {
        Calle = calle;
        NumeroExterior = numeroExterior;
        NumeroInterior = numeroInterior;
        Colonia = colonia;
        Localidad = localidad;
        Referencia = referencia;
        Municipio = municipio;
        Estado = estado;
        Pais = pais;
        CodigoPostal = codigoPostal;
    }

    public String getCalle() {
        return this.Calle;
    }

    public String getNumeroExterior() {
        return this.NumeroExterior;
    }

    public String getNumeroInterior() {
        return this.NumeroInterior;
    }

    public String getColonia() {
        return this.Colonia;
    }

    public String getLocalidad() {
        return this.Localidad;
    }

    public String getReferencia() {
        return this.Referencia;
    }

    public String getMunicipio() {
        return this.Municipio;
    }

    public String getEstado() {
        return this.Estado;
    }

    public CustomCatalogoData getPais() {
        return this.Pais;
    }

    public String getCodigoPostal() {
        return this.CodigoPostal;
    }

    public void setCalle(String Calle) {
        this.Calle = Calle;
    }

    public void setNumeroExterior(String NumeroExterior) {
        this.NumeroExterior = NumeroExterior;
    }

    public void setNumeroInterior(String NumeroInterior) {
        this.NumeroInterior = NumeroInterior;
    }

    public void setColonia(String Colonia) {
        this.Colonia = Colonia;
    }

    public void setLocalidad(String Localidad) {
        this.Localidad = Localidad;
    }

    public void setReferencia(String Referencia) {
        this.Referencia = Referencia;
    }

    public void setMunicipio(String Municipio) {
        this.Municipio = Municipio;
    }

    public void setEstado(String Estado) {
        this.Estado = Estado;
    }

    public void setPais(CustomCatalogoData Pais) {
        this.Pais = Pais;
    }

    public void setCodigoPostal(String CodigoPostal) {
        this.CodigoPostal = CodigoPostal;
    }
}
