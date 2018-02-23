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
import fe.db.MCfd;
import java.util.Comparator;
import org.primefaces.model.SortOrder;
 
public class LazySorter implements Comparator<MCfd> {
 
    private String sortField;
     
    private SortOrder sortOrder;
     
    public LazySorter(String sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }
 
    @Override
    public int compare(MCfd inv1, MCfd inv2) {
        try {
            Object value1 = MCfd.class.getField(this.sortField).get(inv1);
            Object value2 = MCfd.class.getField(this.sortField).get(inv2);
 
            int value = ((Comparable)value1).compareTo(value2);
             
            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
        }
        catch(Exception e) {
            throw new RuntimeException();
        }
    }
}