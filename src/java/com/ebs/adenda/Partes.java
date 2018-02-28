package com.ebs.adenda;

import lombok.Setter;
import lombok.Getter;

public class Partes {

    @Getter @Setter
    int posicion;
    @Getter @Setter
    String numeroMaterial;
    @Getter @Setter
    String descripcionMterial;
    @Getter @Setter
    double cantidadMaterial;
    @Getter @Setter
    String unidadMedida;
    @Getter @Setter
    double precioUnitario;
    @Getter @Setter
    double montoLinea;
    @Getter @Setter
    String odenCompra;
    @Getter @Setter
    String codigoImpuesto;

}
