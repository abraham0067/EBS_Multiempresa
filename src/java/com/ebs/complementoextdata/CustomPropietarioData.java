package com.ebs.complementoextdata;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by eflores on 16/01/2018.
 */
public class CustomPropietarioData {
    @Getter
    @Setter
    String NumRegIdTrib;
    @Getter
    @Setter
    CustomCatalogoData ResidenciaFiscal;
    @Getter
    @Setter
    CustomDomicilioData domicilioData;
}
