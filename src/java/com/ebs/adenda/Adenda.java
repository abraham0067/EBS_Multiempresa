package com.ebs.adenda;

import java.util.List;

public class Adenda {

    private List<Partes> partes;
    private Proveedor provedor;
    private Referencias referencias;

    public List<Partes> getPartes() {
        return this.partes;
    }

    public Proveedor getProvedor() {
        return this.provedor;
    }

    public Referencias getReferencias() {
        return this.referencias;
    }

    public void setPartes(List<Partes> partes) {
        this.partes = partes;
    }

    public void setProvedor(Proveedor provedor) {
        this.provedor = provedor;
    }

    public void setReferencias(Referencias referencias) {
        this.referencias = referencias;
    }
}
