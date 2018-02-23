/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.model;

import fe.db.MLogAcceso;
import fe.model.dao.LogAccesoDAO;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Eduardo C. Flores Ambrosio <EB&S>
 */
public class LazyLogsAccDataModel extends LazyDataModel<MLogAcceso> {

    private List<MLogAcceso> dataSource;
    private int idAcceso;
    private int idEmpresa;
    private String tipoBusq;
    private String paramBusq;
    private Date datFecha;
    private LogAccesoDAO daoLog;

    public LazyLogsAccDataModel() {
        this.setRowCount(0);
    }

    
    public LazyLogsAccDataModel(int idAcceso, int idEmpresa, String tipoBusq, String paramBusq, Date datFecha) {
        this.idAcceso = idAcceso;
        this.idEmpresa = idEmpresa;
        this.tipoBusq = tipoBusq;
        this.paramBusq = paramBusq;
        this.datFecha = datFecha;
        this.setRowCount(0);
    }

    @Override
    public Object getRowKey(MLogAcceso object) {
        return object.getId();
    }

    @Override
    public MLogAcceso getRowData(String rowKey) {
        for (MLogAcceso tmp : dataSource) {
            if (tmp.getId() == Integer.valueOf(rowKey)) {
                return tmp;
            }
        }
        return null;
    }

    @Override
    public List<MLogAcceso> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        if(daoLog == null){
            daoLog = new LogAccesoDAO();
        }
        if(dataSource != null){
            dataSource.clear();
        }
        //Sort
        try {
            //Get data
            dataSource = daoLog.ListaDeTodoLogdefecha(idAcceso, idEmpresa, tipoBusq, paramBusq, datFecha, first, pageSize);
        } catch (Exception ex) {
            Logger.getLogger(LazyLogsAccDataModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Number of rows found
        int rowCount = daoLog.rowCount;
        this.setRowCount(rowCount);
        //Return rows per page
        return dataSource;
    }
    
    
    
}
