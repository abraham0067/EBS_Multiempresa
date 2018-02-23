package com.ebs.complementoextdata;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by eflores on 16/01/2018.
 */
public class CustomDescripcionEspecificaData {
    @Getter
    @Setter
    String Marca = "";
    @Getter
    @Setter
    String Modelo = "";
    @Getter
    @Setter
    String SubModelo = "";
    @Getter
    @Setter
    String NumeroSerie = "";

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
}
