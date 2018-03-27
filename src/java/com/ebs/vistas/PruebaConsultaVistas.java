package com.ebs.vistas;

import java.util.List;

/**
 * Created by eflores on 22/01/2018.
 */
public class PruebaConsultaVistas {
    public static void main(String[] args) {
        VistaCfdiOtroDao dao = new VistaCfdiOtroDao();
        Integer[] idsEmpresas = new Integer[1];
        idsEmpresas[0] = 5;

        List<VistaCfdiOtro> resultados = dao.ListaParametros(0, idsEmpresas, null, "", "", "", "", "",
                null, null, -1, "","7A63521B-D59B-4D41-9260-3D7589D50829", 0, 50);




        resultados.forEach((r) -> {
                    System.out.println("CFDI ID:" + r.getId());
                    System.out.println("NUMERO_FACTURA:" + r.getNumeroFactura());
                }
        );

        System.out.println("Resultados:" + dao.rowCount);
    }
}
