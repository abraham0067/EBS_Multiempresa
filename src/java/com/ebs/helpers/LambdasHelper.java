package com.ebs.helpers;

import fe.db.MCformapago;
import fe.db.MCmoneda;
import fe.db.MCtipoRelacionCfdi;
import fe.db.MTdocsFactman;
import fe.sat.v33.CfdiRelacionadoData;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by eflores on 07/09/2017.
 */
public class LambdasHelper {
    public static MTdocsFactman findTipoDocById(List<MTdocsFactman> lista, int clave){
            return lista.stream().filter(p -> p.getId().equals(clave)).findAny().orElse(null);
    }

    public static MCtipoRelacionCfdi findTipoRelacionCfdiByClave(List<MCtipoRelacionCfdi> lista, String clave){
        return lista.stream().filter(p -> p.getClave().equalsIgnoreCase(clave)).findAny().orElse(null);
    }
    public static MCformapago findFormaPagoByClave(List<MCformapago> lista, String clave){
        return lista.stream().filter(p -> p.getCodigo().equalsIgnoreCase(clave)).findAny().orElse(null);
    }

    public static MCmoneda findMonedaByClave(List<MCmoneda> lista, String clave){
        return lista.stream().filter(p -> p.getClave().equalsIgnoreCase(clave)).findAny().orElse(null);
    }

    public static List<CfdiRelacionadoData> deleteInList(List<CfdiRelacionadoData> lista,List<CfdiRelacionadoData> deleteThis){
        return lista.stream()
                .filter(p -> deleteThis.contains(p.getUuid()))
                .collect(Collectors.toList());
    }

}
