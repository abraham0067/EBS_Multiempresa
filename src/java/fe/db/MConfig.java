package fe.db;

/**
 *
 * @Author Carlo Garcia Sanchez
 * @Date 02-07-2011
 *
 * Tabla de Configuracion FE
 *
 */
import java.io.Serializable;
import java.text.SimpleDateFormat;

import javax.persistence.GenerationType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Column;
import org.hibernate.annotations.Index;
import java.util.Date;
import javax.persistence.Temporal;
//import org.apache.commons.lang.StringEscapeUtils;

@SuppressWarnings("serial")
@Entity
@Table(name = "M_CONFIG")
public class MConfig implements Serializable {

    /**
     * Identificador Unico AutoIncremental
     */
    private Integer id;
    private Date fecha = new Date();
    private String dato = "";
    private String valor = "";
    private String ruta = "";
    private String clasificacion;

    /**
     * Contructor privado
     */
    public MConfig() {
    }

    /**
     * Constructor inicial
     */
    public MConfig(String dato, String valor, String ruta, String clasificacion) {
        this.dato = dato;
        this.valor = valor;
        this.ruta = ruta;
        this.clasificacion = clasificacion;
    }

    @Id
   /* @SequenceGenerator(name = "SConfig", sequenceName = "SECUENCIA_CONFIG", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SConfig")*/
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "ID")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Index(name = "CFG_FECHA_IDX")
    @Column(name = "FECHA", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getFecha() {
        return fecha;
    }

    @Column(name = "DATO", nullable = false, length = 150)
    public String getDato() {
        return dato;
    }

    @Column(name = "VALOR", nullable = false, length = 500)
    public String getValor() {
        return valor;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String FechaF() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(fecha);
    }

    /**
     * @return the ruta
     */
    @Column(name = "RUTA", nullable = false, length = 200)
    public String getRuta() {
        return ruta;
    }

    /**
     * @param ruta the ruta to set
     */
    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    /**
     * @return the clasificacion
     */
    @Column(name = "CLASIFICACION", nullable = false, length = 20)
    public String getClasificacion() {
        return clasificacion;
    }

    /**
     * @param clasificacion the clasificacion to set
     */
    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }
    
//    public void escapeSqlAndHtmlCharacters(){
//        this.dato = (this.dato != null)?(StringEscapeUtils.escapeSql(this.dato)): "";
//        this.valor = (this.valor != null)? (StringEscapeUtils.escapeSql(this.valor)) : "";
//        this.ruta = (this.ruta != null)?(StringEscapeUtils.escapeSql(this.ruta)) : "";
//        this.clasificacion = (this.clasificacion != null) ? (StringEscapeUtils.escapeSql(this.clasificacion)): "";
//        
//        this.dato = (this.dato != null)?(StringEscapeUtils.escapeHtml(this.dato)): "";
//        this.valor = (this.valor != null)? (StringEscapeUtils.escapeHtml(this.valor)) : "";
//        this.ruta = (this.ruta != null)?(StringEscapeUtils.escapeHtml(this.ruta)) : "";
//        this.clasificacion = (this.clasificacion != null) ? (StringEscapeUtils.escapeHtml(this.clasificacion)): "";
//    }
}