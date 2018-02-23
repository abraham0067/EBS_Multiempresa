package com.ebs.complementoextdata;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by eflores on 16/01/2018.
 */
public class CustomCatalogoData {
    @Getter
    @Setter
    String Clave = "";
    @Getter
    @Setter
    String Descripcion = "";

    public CustomCatalogoData() {
        this("", "");
    }

    public CustomCatalogoData(String clave, String descripcion) {
        Clave = clave;
        Descripcion = descripcion;
    }

}
