/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.model;

import fe.db.MCfd;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Eduardo C. Flores Ambrosio <EB&S>
 */
public class LazyMcfdDataModel extends LazyDataModel<MCfd> {
     
    private List<MCfd> datasource;
     
    public LazyMcfdDataModel(List<MCfd> datasource) {
        this.datasource = datasource;
    }
     
    @Override
    public MCfd getRowData(String rowKey) {
        for(MCfd inv : datasource) {
            if(inv.getId() == Integer.valueOf(rowKey))
                return inv;
        }
        return null;
    }
 
    @Override
    public Object getRowKey(MCfd car) {
        return car.getId();
    }
 
    @Override
    public List<MCfd> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
        List<MCfd> data = new ArrayList<MCfd>();
        //filter
        for(MCfd car : datasource) {
            boolean match = true;
            if (filters != null) {
                for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                    try {
                        String filterProperty = it.next();
                        Object filterValue = filters.get(filterProperty);
                        String fieldValue = String.valueOf(car.getClass().getField(filterProperty).get(car));
 
                        if(filterValue == null || fieldValue.startsWith(filterValue.toString())) {
                            match = true;
                    }
                    else {
                            match = false;
                            break;
                        }
                    } catch(Exception e) {
                        match = false;
                    }
                }
            }
            if(match) {
                data.add(car);
            }
        }
        //sort
        if(sortField != null) {
            Collections.sort(data, new LazySorter(sortField, sortOrder));
        }
        //rowCount
        int dataSize = data.size();
        this.setRowCount(dataSize);
        //paginate
        if(dataSize > pageSize) {
            try {
                return data.subList(first, first + pageSize);
            }
            catch(IndexOutOfBoundsException e) {
                return data.subList(first, first + (dataSize % pageSize));
            }
        }
        else {
            return data;
        }
    }
}