package com.ebs.complementoextdata;

/**
 * Created by eflores on 16/01/2018.
 */
public class CustomDescripcionEspecificaData {
    private String Marca = "";
    private String Modelo = "";
    private String SubModelo = "";
    private String NumeroSerie = "";

    public CustomDescripcionEspecificaData() {
        this("", "", "", "");
    }

    public CustomDescripcionEspecificaData(String marca) {

        this(marca, "", "", "");
    }

    public CustomDescripcionEspecificaData(String marca, String modelo, String subModelo, String numeroSerie) {
        Marca = marca;
        Modelo = modelo;
        SubModelo = subModelo;
        NumeroSerie = numeroSerie;
    }

    public String getMarca() {
        return this.Marca;
    }

    public String getModelo() {
        return this.Modelo;
    }

    public String getSubModelo() {
        return this.SubModelo;
    }

    public String getNumeroSerie() {
        return this.NumeroSerie;
    }

    public void setMarca(String Marca) {
        this.Marca = Marca;
    }

    public void setModelo(String Modelo) {
        this.Modelo = Modelo;
    }

    public void setSubModelo(String SubModelo) {
        this.SubModelo = SubModelo;
    }

    public void setNumeroSerie(String NumeroSerie) {
        this.NumeroSerie = NumeroSerie;
    }
}
