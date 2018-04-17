package com.ebs.complementoextdata;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by eflores on 16/01/2018.
 */
public class CustomDestinatarioData {
    @Getter
    @Setter
    String NumRegIdTrib = "";
    @Getter
    @Setter
    String Nombre = "";
    @Getter
    @Setter
    CustomDomicilioData Domicilio;

    public CustomDestinatarioData() {
        new CustomDomicilioData();
    }

    public CustomDestinatarioData(String numRegIdTrib, String nombre, CustomDomicilioData domicilio) {
        NumRegIdTrib = numRegIdTrib;
        Nombre = nombre;
        Domicilio = domicilio;
    }
}
