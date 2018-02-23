/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.db;

import java.io.Serializable;
//import org.apache.commons.lang.StringEscapeUtils;

/**
 *
 * @author Lily
 */
@SuppressWarnings("serial")
public class MenuPerfiles implements Serializable {

    private String titulo;
    private String name;
    private long numPerfil;
    private String prefijo;
    private boolean mostrar;
    private int nivel= 1;
    private String icon ;
    private String url;
    
    public MenuPerfiles() {
        
    }

    public MenuPerfiles(String titulo, long numPerfil, String prefijo, int nivel) {
        this.titulo = titulo;       
        this.numPerfil = numPerfil;
        this.prefijo = prefijo;       
        this.nivel = nivel;
    }

    public MenuPerfiles(String titulo, long numPerfil, String prefijo, boolean mostrar, int nivel, String icon, String url) {
        this.titulo = titulo;        
        this.numPerfil = numPerfil;
        this.prefijo = prefijo;
        this.mostrar = mostrar;
        this.nivel = nivel;
        this.icon = icon;
        this.url = url;
    }
    
    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }
    
    /**
     * @return the titulo
     */
    

    public String getTitulo() {
        return titulo;
    }

    /**
     * @param titulo the titulo to set
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the numPerfil
     */
    public long getNumPerfil() {
        return numPerfil;
    }

    /**
     * @param numPerfil the numPerfil to set
     */
    public void setNumPerfil(long numPerfil) {
        this.numPerfil = numPerfil;
    }

    /**
     * @return the prefijo
     */
    public String getPrefijo() {
        return prefijo;
    }

    /**
     * @param prefijo the prefijo to set
     */
    public void setPrefijo(String prefijo) {
        this.prefijo = prefijo;
    }

    /**
     * @return the mostrar
     */
    public boolean isMostrar() {
        return mostrar;
    }

    /**
     * @param mostrar the mostrar to set
     */
    public void setMostrar(boolean mostrar) {
        this.mostrar = mostrar;
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
    
//    public void escapeSqlAndHtmlCharacters(){
//        this.icon = (this.icon != null)?  StringEscapeUtils.escapeSql(this.icon):"";
//        this.name = (this.name != null)?  StringEscapeUtils.escapeSql(this.name):"";
//        this.prefijo=(this.name != null)?  StringEscapeUtils.escapeSql(this.prefijo):"";
//        this.titulo=(this.prefijo != null)?  StringEscapeUtils.escapeSql(this.prefijo):"";
//        this.url = (this.url != null)?  StringEscapeUtils.escapeSql(this.url):"";
//        
//        this.icon = (this.icon != null)?  StringEscapeUtils.escapeHtml(this.icon):"";
//        this.name = (this.name != null)?  StringEscapeUtils.escapeHtml(this.name):"";
//        this.prefijo=(this.name != null)?  StringEscapeUtils.escapeHtml(this.prefijo):"";
//        this.titulo=(this.prefijo != null)?  StringEscapeUtils.escapeHtml(this.prefijo):"";
//        this.url = (this.url != null)?  StringEscapeUtils.escapeHtml(this.url):"";
//    }

   
}
