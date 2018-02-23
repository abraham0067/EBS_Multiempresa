package com.ebs.vistas;

import java.util.List;

/**
 * Created by eflores on 22/01/2018.
 */
public class PruebaConsultaVistas {
    public static void main(String[] args) {
        VistaCfdiOtroDao dao = new VistaCfdiOtroDao();
        Integer[] idsEmpresas = new Integer[1];
        idsEmpresas[0] = 4;

        List<VistaCfdiOtro> resultados = dao.ListaParametros(1, idsEmpresas, "DI.4", "", "", "", "", "",
                null, null, -1, "", 0, 50);




        resultados.forEach((r) -> {
                    System.out.println("CFDI ID:" + r.getId());
                    System.out.println("NUMERO_FACTURA:" + r.getNumeroFactura());
                }
        );

        System.out.println("Resultados:" + dao.rowCount);
    }
}
