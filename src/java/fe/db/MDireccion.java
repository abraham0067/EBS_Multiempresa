/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.db;

import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.io.Serializable;

//import org.apache.commons.lang.StringEscapeUtils;

/**
 *
 * @author Carlo
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "M_DIRECCION") 
public class MDireccion implements Serializable {

    private int id = 0;
    private String calle = "";
    private String noExterior = "";
    private String noInterior = "";
    private String referencia = "";
    private String colonia = "";
    private String localidad = "";
    private String municipio = "";
    private String estado = "";
    private String cp = "";
    private String pais = "";
    
    public MDireccion() {
    }

    
 

    public MDireccion(String calle, String noExterior, String noInterior,
			String referencia, String colonia, String localidad,
			String municipio, String estado, String cp, String pais) {
		super();
		this.calle = calle;
		this.noExterior = noExterior;
		this.noInterior = noInterior;
		this.referencia = referencia;
		this.colonia = colonia;
		this.localidad = localidad;
		this.municipio = municipio;
		this.estado = estado;
		this.cp = cp;
		this.pais = pais;
	}
	

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
/* @SequenceGenerator(name = "SDireccion", sequenceName = "SECUENCIA_DIRECCION", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SDireccion")*/
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    /**
     * @return the calle
     */
    @Column(name = "CALLE", nullable = false, length = 150)
    public String getCalle() {
        return calle;
    }

    /**
     * @return the noExterior
     */
    @Column(name = "NO_EXTERIOR", nullable = true, length = 100)
    public String getNoExterior() {
        return noExterior;
    }

    /**
     * @return the noInterior
     */
    @Column(name = "NO_INTERIOR", nullable = true, length = 100)
    public String getNoInterior() {
        return noInterior;
    }

    /**
     * @return the referencia
     */
    @Column(name = "REFERENCIA", nullable = true, length = 150)
    public String getReferencia() {
        return referencia;
    }

    /**
     * @return the colonia
     */
    @Column(name = "COLONIA", nullable = true, length = 100)
    public String getColonia() {
        return colonia;
    }

    /**
     * @return the localidad
     */
    @Column(name = "LOCALIDAD", nullable = true, length = 100)
    public String getLocalidad() {
        return localidad;
    }

    /**
     * @return the municipio
     */
    @Column(name = "MUNICIPIO", nullable = true, length = 100)
    public String getMunicipio() {
        return municipio;
    }

    /**
     * @return the estado
     */
    @Index(name = "DIR_EDO_IDX")
    @Column(name = "ESTADO", nullable = true, length = 60)
    public String getEstado() {
        return estado;
    }

    /**
     * @return the cp
     */
    @Column(name = "CODIGO_POSTAL", nullable = true, length = 6)
    public String getCp() {
        return cp;
    }

    /**
     * @return the pais
     */
    @Column(name = "PAIS", nullable = false, length = 100)
    public String getPais() {
        return pais;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @param calle the calle to set
     */
    public void setCalle(String calle) {
        this.calle = (calle!=null && !"".equals(calle))?calle.trim().toUpperCase():"";
    }

    /**
     * @param noExterior the noExterior to set
     */
    public void setNoExterior(String noExterior) {
        this.noExterior =  (noExterior!=null && !"".equals(noExterior))?noExterior.trim().toUpperCase():"";
    }

    /**
     * @param noInterior the noInterior to set
     */
    public void setNoInterior(String noInterior) {
        this.noInterior = (noInterior!=null && !"".equals(noInterior))?noInterior.trim().toUpperCase():"";
    }

    /**
     * @param referencia the referencia to set
     */
    public void setReferencia(String referencia) {
        this.referencia = (referencia!=null && !"".equals(referencia))?referencia.trim().toUpperCase():"";
    }

    /**
     * @param colonia the colonia to set
     */
    public void setColonia(String colonia) {
        this.colonia = (colonia!=null && !"".equals(colonia))?colonia.trim().toUpperCase():"";
    }

    /**
     * @param localidad the localidad to set
     */
    public void setLocalidad(String localidad) {
        this.localidad = (localidad!=null && !"".equals(localidad))?localidad.trim().toUpperCase():"";
    }

    /**
     * @param municipio the municipio to set
     */
    public void setMunicipio(String municipio) {
        this.municipio =  (municipio!=null && !"".equals(municipio))?municipio.trim().toUpperCase():"";
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(String estado) {
        this.estado =  (estado!=null && !"".equals(estado))?estado.trim().toUpperCase():"";
    }

    /**
     * @param cp the cp to set
     */
    public void setCp(String cp) {
        this.cp =  (cp!=null && !"".equals(cp))?cp.trim().toUpperCase():"";
    }

    /**
     * @param pais the pais to set
     */
    public void setPais(String pais) {
        this.pais =  (pais!=null && !"".equals(pais))?pais.trim().toUpperCase():"";
    }

    public String Datos() {
        String Datos = "";
        Datos = Datos + ((pais != null && !"".equals(pais)) ? pais + ",	" : "");
        Datos = Datos + ((estado != null && !"".equals(estado)) ? estado + ",		" : "");
        Datos = Datos + ((municipio != null && !"".equals(municipio)) ? municipio + ", 	" : "");
        Datos = Datos + ((cp != null && !"".equals(cp)) ? cp + ",  " : "");
        Datos = Datos + ((localidad != null && !"".equals(localidad)) ? localidad + ",	" : "");
        Datos = Datos + ((colonia != null && !"".equals(colonia)) ? colonia + ",	" : "");
        Datos = Datos + ((calle != null && !"".equals(calle)) ? calle+",	" : "");
        Datos = Datos + ((noInterior != null && !"".equals(noInterior)) ? ",	 " + noInterior : "");
        Datos = Datos + ((noExterior != null && !"".equals(noExterior)) ? ",		" + noExterior : "");
        Datos = Datos + ((referencia != null && !"".equals(referencia)) ? ", 		" + referencia : "");
        return Datos;

    }
    
//    public void escapeSqlAndHtmlCharacters(){
//        this.setCalle(StringEscapeUtils.escapeSql(this.getCalle()));
//        this.setColonia(StringEscapeUtils.escapeSql(this.getColonia()));
//        this.setCp(StringEscapeUtils.escapeSql(this.getCp()));
//        this.setEstado(StringEscapeUtils.escapeSql(this.getEstado()));
//        this.setLocalidad(StringEscapeUtils.escapeSql(this.getLocalidad()));
//        this.setMunicipio(StringEscapeUtils.escapeSql(this.getMunicipio()));
//        this.setNoExterior(StringEscapeUtils.escapeSql(this.getNoExterior()));
//        this.setNoInterior(StringEscapeUtils.escapeSql(this.getNoInterior()));
//        this.setPais(StringEscapeUtils.escapeSql(this.getPais()));
//        this.setReferencia(StringEscapeUtils.escapeSql(this.getReferencia()));
//
//        this.setCalle(StringEscapeUtils.escapeHtml(this.getCalle()));
//        this.setColonia(StringEscapeUtils.escapeHtml(this.getColonia()));
//        this.setCp(StringEscapeUtils.escapeHtml(this.getCp()));
//        this.setEstado(StringEscapeUtils.escapeHtml(this.getEstado()));
//        this.setLocalidad(StringEscapeUtils.escapeHtml(this.getLocalidad()));
//        this.setMunicipio(StringEscapeUtils.escapeHtml(this.getMunicipio()));
//        this.setNoExterior(StringEscapeUtils.escapeHtml(this.getNoExterior()));
//        this.setNoInterior(StringEscapeUtils.escapeHtml(this.getNoInterior()));
//        this.setPais(StringEscapeUtils.escapeHtml(this.getPais()));
//        this.setReferencia(StringEscapeUtils.escapeHtml(this.getReferencia()));
//    }
}
