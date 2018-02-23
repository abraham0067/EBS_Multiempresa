package com.ebs.menu;

import com.ebs.menu.MenuItem;

import java.util.List;

/**
 * Created by eflores on 06/04/2017.
 */
public class SubMenu {
    List<SubMenu> menus;
    List<MenuItem> items;
    private String title;
    private String name;
    private Long perfilValue;
    private String icon;
    private String url;
    private int nivel;
    private int position;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPerfilValue() {
        return perfilValue;
    }

    public void setPerfilValue(Long perfilValue) {
        this.perfilValue = perfilValue;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
