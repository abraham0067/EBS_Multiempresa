package com.ebs.complementoextdata;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by eflores on 16/01/2018.
 */
public class CustomEmisorData {
    @Getter
    @Setter
    String CURP = "";
    @Getter
    @Setter
    CustomDomicilioData Domicilio;


    public CustomEmisorData() {
        this("", new CustomDomicilioData());
    }

    public CustomEmisorData(String CURP, CustomDomicilioData domicilio) {
        this.CURP = CURP;
        Domicilio = domicilio;
    }
}
