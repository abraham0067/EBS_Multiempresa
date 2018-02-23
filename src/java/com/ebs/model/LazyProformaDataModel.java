/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.model;

import fe.db.MCfdProforma;
import fe.model.dao.ProformaDao;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Eduardo C. Flores Ambrosio <EB&S>
 */
public class LazyProformaDataModel extends LazyDataModel<MCfdProforma> {

    private List<MCfdProforma> dataSource;
    private boolean isCliente;
    private int idAcceso;
    private Integer[] idsEmpresas;
    private String tipoBusq;
    private String paramBusq;
    private Date datDesde;//Fecha Inicial
    private Date datHasta;//Fecha Final
    private String cliente;
    private String estatus;

    public LazyProformaDataModel() {
        this.setRowCount(0);
    }

    /**
     * Clintes
     *
     * @param isCliente
     * @param idAcceso
     * @param idsEmpresas
     * @param tipoBusq
     * @param paramBusq
     * @param datDesde
     * @param datHasta
     * @param cliente
     */
    public LazyProformaDataModel(boolean isCliente,
                                 int idAcceso,
                                 Integer[] idsEmpresas,
                                 String tipoBusq,
                                 String paramBusq,
                                 Date datDesde,
                                 Date datHasta,
                                 String cliente) {
        this.isCliente = isCliente;
        this.idAcceso = idAcceso;
        this.idsEmpresas = idsEmpresas;
        this.tipoBusq = tipoBusq;
        this.paramBusq = paramBusq;
        this.datDesde = datDesde;
        this.datHasta = datHasta;
        this.cliente = cliente;
        this.setRowCount(0);
    }

    public LazyProformaDataModel(boolean isCliente,
                                 int idAcceso,
                                 Integer[] idsEmpresas,
                                 String tipoBusq,
                                 String paramBusq,
                                 Date datDesde,
                                 Date datHasta,
                                 String cliente,
                                 String estatus
    ) {
        this.isCliente = isCliente;
        this.idAcceso = idAcceso;
        this.idsEmpresas = idsEmpresas;
        this.tipoBusq = tipoBusq;
        this.paramBusq = paramBusq;
        this.datDesde = datDesde;
        this.datHasta = datHasta;
        this.cliente = cliente;
        this.estatus = estatus;
        this.setRowCount(0);
    }


    public LazyProformaDataModel(List<MCfdProforma> dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public MCfdProforma getRowData(String rowKey) {
        for (MCfdProforma inv : dataSource) {
            if (inv.getId() == Integer.valueOf(rowKey)) {
                return inv;
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(MCfdProforma cfd) {
        return cfd.getId();
    }

    @Override
    public List<MCfdProforma> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        ProformaDao daoCFDI = new ProformaDao();
        //sort
        if (sortField != null) {
            Collections.sort(dataSource, new LazySorterProforma(sortField, sortOrder));
        }
        if (isCliente) {
            dataSource = daoCFDI.ListaParametrosClientes(
                    idAcceso,
                    idsEmpresas,
                    tipoBusq,
                    paramBusq,
                    datDesde,
                    datHasta,
                    cliente,
                    first,
                    pageSize);
        } else {
            dataSource = daoCFDI.ListaParametros(
                    idAcceso,
                    idsEmpresas,
                    tipoBusq,
                    paramBusq,
                    datDesde,
                    datHasta,
                    Integer.parseInt(estatus),
                    first, pageSize);
        }
        ///rowCount
        int dataSourceSize = daoCFDI.rowCount;
        this.setRowCount(dataSourceSize);

        return dataSource;
    }
}
