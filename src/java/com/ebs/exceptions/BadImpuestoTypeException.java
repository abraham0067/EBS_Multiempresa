package com.ebs.exceptions;

/**
 * Created by eflores on 20/09/2017.
 * Excepcion cunando se intenta agregar un impuesto que no es del tipo requerido
 */
public class BadImpuestoTypeException extends Exception {
    public BadImpuestoTypeException() {
        this("El impuesto que intentas agregar no concuerda con el tipo(Traslado/Retencion) de la lista de impuestos");
    }
    public BadImpuestoTypeException(String message) {
        super(message);
    }
}
