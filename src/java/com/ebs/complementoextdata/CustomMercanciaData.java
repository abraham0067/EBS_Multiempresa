package com.ebs.complementoextdata;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eflores on 16/01/2018.
 */
public class CustomMercanciaData {
    @Getter
    @Setter
    String NoIdentificacion;
    @Getter
    @Setter
    CustomCatalogoData FraccionArancelaria;
    @Getter
    @Setter
    double CantidadAduana;
    @Getter
    @Setter
    CustomCatalogoData UnidadAduana;
    @Getter
    @Setter
    double ValorUnitarioAduana;
    @Getter
    @Setter
    double ValorDolares;
    @Getter
    @Setter
    List<CustomDescripcionEspecificaData> descripcionesEspecificas;

    public CustomMercanciaData() {
        this("", new CustomCatalogoData("", ""), 1.0,
                new CustomCatalogoData("", ""), 1.0, 1.0,
                new ArrayList<>()
        );
    }

    public CustomMercanciaData(String noIdentificacion,
                               CustomCatalogoData fraccionArancelaria,
                               double cantidadAduana,
                               CustomCatalogoData unidadAduana,
                               double valorUnitarioAduana,
                               double valorDolares,
                               List<CustomDescripcionEspecificaData> descripcionesEspecificas) {
        NoIdentificacion = noIdentificacion;
        FraccionArancelaria = fraccionArancelaria;
        CantidadAduana = cantidadAduana;
        UnidadAduana = unidadAduana;
        ValorUnitarioAduana = valorUnitarioAduana;
        ValorDolares = valorDolares;
        this.descripcionesEspecificas = descripcionesEspecificas;
    }
}
