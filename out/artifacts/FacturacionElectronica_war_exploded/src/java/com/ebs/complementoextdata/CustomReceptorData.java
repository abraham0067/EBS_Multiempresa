package com.ebs.complementoextdata;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by eflores on 16/01/2018.
 */
public class CustomReceptorData {
    @Getter
    @Setter
    String NumRegIdTrib;
    @Getter
    @Setter
    CustomDomicilioData Domicilio;

    public CustomReceptorData() {
        this("",new CustomDomicilioData());
    }

    public CustomReceptorData(String numRegIdTrib, CustomDomicilioData domicilio) {
        NumRegIdTrib = numRegIdTrib;
        Domicilio = domicilio;
    }
}
