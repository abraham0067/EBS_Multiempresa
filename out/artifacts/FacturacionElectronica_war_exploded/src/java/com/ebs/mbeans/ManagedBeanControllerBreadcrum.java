/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.mbeans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;

/**
 * Creacion dinamica del breadcrum, aun no terminada de implementarse
 * @author Eduardo C. Flores Ambrosio
 */
@ManagedBean (name = "controllerBreadcrum")
@RequestScoped
public class ManagedBeanControllerBreadcrum {
    private DefaultMenuModel breadcrumbmodel;
    /**
     * Creates a new instance of ManagedBeanControllerBreadcrum
     */
    public ManagedBeanControllerBreadcrum() {
        breadcrumbmodel = new DefaultMenuModel();
        DefaultMenuItem item = new DefaultMenuItem();
        item.setValue("Inicio");
        //item.setUrl("/main/principal.xhtml");
        item.setId("home");
        this.breadcrumbmodel.addElement(item);
    }
    public DefaultMenuModel getBreadcrumbmodel() {
        return breadcrumbmodel;
    }
    public void setBreadcrumbmodel(DefaultMenuModel breadcrumbmodel) {
        this.breadcrumbmodel = breadcrumbmodel;
    }
    public void addItem(String value, String url, String id){
        DefaultMenuItem item = new DefaultMenuItem();
        item.setValue(value);
        item.setUrl(url);
        item.setId(id);
        this.breadcrumbmodel.addElement(item);
    }
    
}
