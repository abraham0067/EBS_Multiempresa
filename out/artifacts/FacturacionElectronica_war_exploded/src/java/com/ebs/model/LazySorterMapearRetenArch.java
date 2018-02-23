/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.model;

import fe.db.MapearRetencionArchi;
import java.util.Comparator;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Eduardo C. Flores Ambrosio <EB&S>
 */
public class LazySorterMapearRetenArch implements Comparator<MapearRetencionArchi> {

    private String sortField;

    private SortOrder sortOrder;

    public LazySorterMapearRetenArch(String sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    @Override
    public int compare(MapearRetencionArchi car1, MapearRetencionArchi car2) {
        try {
            Object value1 = MapearRetencionArchi.class.getField(this.sortField).get(car1);
            Object value2 = MapearRetencionArchi.class.getField(this.sortField).get(car2);

            int value = ((Comparable) value1).compareTo(value2);

            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
