package com.ebs.menu;

/**
 * Created by eflores on 06/04/2017.
 */
public class MenuItem {
    private String title;
    private String name;
    private Long perfilValue;
    private String icon;
    private String url;
    private int position;
    private int nivel;

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public Long getPerfilValue() {
        return perfilValue;
    }

    public String getIcon() {
        return icon;
    }

    public String getUrl() {
        return url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPerfilValue(Long perfilValue) {
        this.perfilValue = perfilValue;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }
}
