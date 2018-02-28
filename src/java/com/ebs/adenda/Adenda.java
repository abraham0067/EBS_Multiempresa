package com.ebs.adenda;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class Adenda {

    @Getter @Setter
    List<Partes> partes;
    @Getter @Setter
    Proveedor provedor;
    @Getter @Setter
    Referencias referencias;
}
