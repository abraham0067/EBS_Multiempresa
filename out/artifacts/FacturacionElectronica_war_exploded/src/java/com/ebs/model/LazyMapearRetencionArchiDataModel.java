/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.model;

import fe.db.MapearRetencionArchi;
import fe.model.dao.RetencionDAO;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Eduardo C. Flores Ambrosio <EB&S>
 */
public class LazyMapearRetencionArchiDataModel extends LazyDataModel<MapearRetencionArchi> {

    private List<MapearRetencionArchi> dataSource;
    private boolean isCliente;
    private int idAcceso;
    private int idEmpresa;
    private String tipoBusq;
    private String paramBusq;
    private Date datDesde;//Fecha Inicial
    private Date datHasta;//Fecha Final
    private String cliente;
    private Integer estatus;
    public int pageNumber = 0;

    public LazyMapearRetencionArchiDataModel() {
        this.setRowCount(0);
    }

    /**
     * Constructor para la consulta de clientes
     *
     * @param idAcceso
     * @param idEmpresa
     * @param tipoBusq
     * @param paramBusq
     * @param datDesde
     * @param datHasta
     * @param cliente
     */
    public LazyMapearRetencionArchiDataModel(boolean isCliente, int idAcceso, int idEmpresa, String tipoBusq, String paramBusq, Date datDesde, Date datHasta, String cliente) {
        this.idAcceso = idAcceso;
        this.isCliente = isCliente;
        this.idEmpresa = idEmpresa;
        this.tipoBusq = tipoBusq;
        this.paramBusq = paramBusq;
        this.datDesde = datDesde;
        this.datHasta = datHasta;
        this.cliente = cliente;
        this.setRowCount(0);
    }

    /**
     * Constructor para usuarios internos
     *
     * @param idAcceso
     * @param idEmpresa
     * @param tipoBusq
     * @param paramBusq
     * @param datDesde
     * @param datHasta
     * @param estatus
     */
    public LazyMapearRetencionArchiDataModel(int idAcceso, int idEmpresa, String tipoBusq, String paramBusq, Date datDesde, Date datHasta, Integer estatus) {
        this.isCliente = false;
        this.idAcceso = idAcceso;
        this.idEmpresa = idEmpresa;
        this.tipoBusq = tipoBusq;
        this.paramBusq = paramBusq;
        this.datDesde = datDesde;
        this.datHasta = datHasta;
        this.estatus = estatus;
        this.setRowCount(0);
    }

    @Override
    public Object getRowKey(MapearRetencionArchi object) {
        return object.getRetencion().getId();
    }

    @Override
    public MapearRetencionArchi getRowData(String rowKey) {
        for (MapearRetencionArchi inv : dataSource) {
            if (inv.getRetencion().getId().intValue() == Integer.parseInt(rowKey)) {
                return inv;
            }
        }
        return null;
    }

    @Override
    public List<MapearRetencionArchi> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        RetencionDAO daoReten = new RetencionDAO();
        pageNumber  = first / pageSize;
        //sort
        if (sortField != null) {
            Collections.sort(dataSource, new LazySorterMapearRetenArch(sortField, sortOrder));
        }
        
        if(dataSource != null){
            dataSource.clear();
        }
        
        if (isCliente) {
            dataSource = daoReten.ListaArchivos(daoReten.ListaParametrosClientes(idAcceso, idEmpresa, tipoBusq, paramBusq, datDesde, datHasta, cliente, first, pageSize));
        } else {
            dataSource = daoReten.ListaArchivos(daoReten.ListaParametros(idAcceso, idEmpresa, tipoBusq, paramBusq, datDesde, datHasta, estatus, first, pageSize));
        }
        //rowCount
        int dataSourceSize = daoReten.rowCount;
        this.setRowCount(dataSourceSize);

        return dataSource;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
    
    

}
