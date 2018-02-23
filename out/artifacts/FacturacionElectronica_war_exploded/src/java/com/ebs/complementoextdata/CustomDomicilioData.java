package com.ebs.complementoextdata;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by eflores on 16/01/2018.
 */
public class CustomDomicilioData {
    @Getter
    @Setter
    String Calle = "";
    @Getter
    @Setter
    String NumeroExterior = "";
    @Getter
    @Setter
    String NumeroInterior = "";
    @Getter
    @Setter
    String Colonia = "";
    @Getter
    @Setter
    String Localidad = "";
    @Getter
    @Setter
    String Referencia = "";
    @Getter
    @Setter
    String Municipio = "";
    @Getter
    @Setter
    String Estado = "";
    @Getter
    @Setter
    CustomCatalogoData Pais;
    @Getter
    @Setter
    String CodigoPostal = "";

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
}
