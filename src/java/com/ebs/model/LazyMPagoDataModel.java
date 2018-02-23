/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.model;

import com.ebs.catalogos.TiposDocumento;
import fe.db.MAcceso;
import fe.db.MCfdPagos;
import fe.model.dao.AutoPagosDao;
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
public class LazyMPagoDataModel extends LazyDataModel<MCfdPagos> {

    private List<MCfdPagos> dataSource;
    private boolean isCliente;
    private MAcceso idAcceso;
    private Integer[] idsEmpresas;
    private String tipoBusq;
    private String paramBusq;
    private Date datDesde;//Fecha Inicial
    private Date datHasta;//Fecha Final
    private String cliente;
    private String estatus;

    public LazyMPagoDataModel() {
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
    public LazyMPagoDataModel(boolean isCliente,
                                        MAcceso idAcceso,
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

    public LazyMPagoDataModel(boolean isCliente,
                                        MAcceso idAcceso,
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


    public LazyMPagoDataModel(List<MCfdPagos> dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public MCfdPagos getRowData(String rowKey) {
        for (MCfdPagos inv : dataSource) {
            if (inv.getId() == Integer.valueOf(rowKey)) {
                return inv;
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(MCfdPagos cfd) {
        return cfd.getId();
    }

    @Override
    public List<MCfdPagos> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        AutoPagosDao daoCFDI = new AutoPagosDao();

        //sort
        if (sortField != null) {
            Collections.sort(dataSource, new LazySorterPagos(sortField, sortOrder));
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
