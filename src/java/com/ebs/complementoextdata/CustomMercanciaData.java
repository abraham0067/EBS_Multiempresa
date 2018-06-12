package com.ebs.complementoextdata;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eflores on 16/01/2018.
 */
public class CustomMercanciaData {
    private String NoIdentificacion;
    private CustomCatalogoData FraccionArancelaria;
    private double CantidadAduana;
    private CustomCatalogoData UnidadAduana;
    private double ValorUnitarioAduana;
    private double ValorDolares;
    private List<CustomDescripcionEspecificaData> descripcionesEspecificas;

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

    public String getNoIdentificacion() {
        return this.NoIdentificacion;
    }

    public CustomCatalogoData getFraccionArancelaria() {
        return this.FraccionArancelaria;
    }

    public double getCantidadAduana() {
        return this.CantidadAduana;
    }

    public CustomCatalogoData getUnidadAduana() {
        return this.UnidadAduana;
    }

    public double getValorUnitarioAduana() {
        return this.ValorUnitarioAduana;
    }

    public double getValorDolares() {
        return this.ValorDolares;
    }

    public List<CustomDescripcionEspecificaData> getDescripcionesEspecificas() {
        return this.descripcionesEspecificas;
    }

    public void setNoIdentificacion(String NoIdentificacion) {
        this.NoIdentificacion = NoIdentificacion;
    }

    public void setFraccionArancelaria(CustomCatalogoData FraccionArancelaria) {
        this.FraccionArancelaria = FraccionArancelaria;
    }

    public void setCantidadAduana(double CantidadAduana) {
        this.CantidadAduana = CantidadAduana;
    }

    public void setUnidadAduana(CustomCatalogoData UnidadAduana) {
        this.UnidadAduana = UnidadAduana;
    }

    public void setValorUnitarioAduana(double ValorUnitarioAduana) {
        this.ValorUnitarioAduana = ValorUnitarioAduana;
    }

    public void setValorDolares(double ValorDolares) {
        this.ValorDolares = ValorDolares;
    }

    public void setDescripcionesEspecificas(List<CustomDescripcionEspecificaData> descripcionesEspecificas) {
        this.descripcionesEspecificas = descripcionesEspecificas;
    }
}
