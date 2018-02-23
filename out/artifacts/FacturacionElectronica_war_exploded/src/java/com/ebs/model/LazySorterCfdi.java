/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.model;

/**
 *
 * @author Eduardo C. Flores Ambrosio <EB&S>
 */
import com.ebs.vistas.VistaCfdiOtro;
import java.util.Comparator;
import org.primefaces.model.SortOrder;
 
public class LazySorterCfdi implements Comparator<VistaCfdiOtro> {
 
    private String sortField;
     
    private SortOrder sortOrder;
     
    public LazySorterCfdi(String sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }
 
    @Override
    public int compare(VistaCfdiOtro obj1, VistaCfdiOtro obj2) {
        try {
            Object value1 = VistaCfdiOtro.class.getField(this.sortField).get(obj1);
            Object value2 = VistaCfdiOtro.class.getField(this.sortField).get(obj2);
 
            int value = ((Comparable)value1).compareTo(value2);
             
            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
        }
        catch(Exception e) {
            throw new RuntimeException();
        }
    }
}