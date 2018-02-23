package com.ebs.menu;

import java.util.List;

/**
 * Created by eflores on 06/04/2017.
 */
public class WebMenu {
    List<SubMenu> menus;
    List<MenuItem> items;
    List<Option> options;

    public int getNumberOfChild(){
        if(items == null || items.isEmpty()){
            return 0;
        } else{
            return items.size();
        }
    }

    public List<SubMenu> getMenus() {
        return menus;
    }

    public void setMenus(List<SubMenu> menus) {
        this.menus = menus;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public void setItems(List<MenuItem> items) {
        this.items = items;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }
}
