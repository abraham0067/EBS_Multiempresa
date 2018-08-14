/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.model;

import fe.db.MLogApp;
import fe.db.MapearCfdArchi;
import fe.model.dao.LogAPPDAO;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Eduardo C. Flores Ambrosio <EB&S>
 */
public class LazyLogsAppDataModel extends LazyDataModel<MLogApp> {

    private List<MLogApp> dataSource;
    private int idAcceso;
    private Integer[] idEmpresa;
    private String serie;
    private String folioErp;
    private String tipoServicio;
    private Date datFecha;
    private LogAPPDAO daoApp;

    public LazyLogsAppDataModel() {
        this.setRowCount(0);
    }
    
    public LazyLogsAppDataModel(int idAcceso, Integer[] idEmpresa, String serie, String folioErp, Date datFecha) {
        this.idAcceso = idAcceso;
        this.idEmpresa = idEmpresa;
        this.serie = serie;
        this.folioErp = folioErp;
        this.datFecha = datFecha;
        this.setRowCount(0);
    }

    public LazyLogsAppDataModel(int idAcceso, Integer[] idEmpresa, String serie, String folioErp, String tipoServicio, Date datFecha) {
        this.idAcceso = idAcceso;
        this.idEmpresa = idEmpresa;
        this.serie = serie;
        this.folioErp = folioErp;
        this.tipoServicio = tipoServicio;
        this.datFecha = datFecha;
        this.setRowCount(0);
    }

    @Override
    public Object getRowKey(MLogApp object) {
        return object.getId();
    }

    @Override
    public List<MLogApp> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        if (daoApp == null) {
            daoApp = new LogAPPDAO();
        }
        //Sort
        //Get data
        dataSource = daoApp.BusquedaPorParametros(idAcceso, idEmpresa, serie, folioErp, tipoServicio, datFecha, first, pageSize);

        //Total rows found
        int dataSourceSize = daoApp.rowCount;
        this.setRowCount(dataSourceSize);
        return dataSource;
    }

    @Override
    public MLogApp getRowData(String rowKey) {
        for (MLogApp tmp : dataSource) {
            if (tmp.getId() == Integer.valueOf(rowKey)) {
                return tmp;
            }
        }
        return null;
    }

}
