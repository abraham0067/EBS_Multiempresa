package fe.db;

/**
 *
 * @Author Carlo Garcia Sanchez
 * @Date 02-07-2011
 *
 * Tabla de Acceso para almacenar e identificar a usuarios
 *
 */

import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
//import org.apache.commons.lang.StringEscapeUtils;

@SuppressWarnings("serial")
@Entity
@Table(name = "M_FOLIOS")
public class MFolios implements Serializable {

    /**
     * Identificador de Serie
     */
    private String id;    
    private Date fecha = new Date();
    private String serie; 
    private Long rango = 1l;
    private String tipoDoc;
    private String descripcion;
    private Long asignados = 0l;
    private Integer estatus = 1;   
   public MFolios(String id, String serie, Long rango, String tipoDoc,
			String descripcion, Long asignados, Integer estatus,
			MEmpresa empresa) {
		super();
		this.id = id;
		this.serie = serie.trim().toUpperCase();
		this.rango = rango;
		this.tipoDoc = tipoDoc;
		this.descripcion = descripcion.trim().toUpperCase();
		this.asignados = asignados;
		this.estatus = estatus;
		this.empresa = empresa;
	}

private MEmpresa empresa;
    private Integer idFolio;
   

    // Constructor privado
    public MFolios() {
    }  

    /**
     * @return the id
     */
    @Index(name = "FOLIOS_ID_IDX")
    @Column(name = "ID", length = 20)
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the fecha
     */
    @Column(name = "FECHA", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the serie
     */
    @Index(name = "FOLIOS_SERIE_IDX")
    @Column(name = "SERIE", nullable = true, length = 20)
    public String getSerie() {
        return serie;
    }

    /**
     * @param serie the serie to set
     */
    public void setSerie(String serie) {
        this.serie = serie;
    }

    /**
     * @return the rango
     */
    @Column(name = "RANGO", nullable = false)
    public Long getRango() {
        return rango;
    }

    /**
     * @param rango the rango to set
     */
    public void setRango(Long rango) {
        this.rango = rango;
    }

   
    /**
     * @return the tipoDoc
     */
    @Column(name = "TIPODOC", length = 30)
    public String getTipoDoc() {
        return tipoDoc;
    }

    /**
     * @param tipoDoc the tipoDoc to set
     */
    public void setTipoDoc(String tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    /**
     * @return the descripcion
     */
    @Column(name = "DESCRIPCION")
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return the asignados
     */
    @Column(name = "ASIGNADOS", nullable = false)
    public Long getAsignados() {
        return asignados;
    }

    /**
     * @param asignados the asignados to set
     */
    public void setAsignados(Long asignados) {
        this.asignados = asignados;
    }

    /**
     * @return the estatus
     */
    @Index(name = "FOLIOS_ESTATUS_IDX")
    @Column(name = "ESTATUS", nullable = false)
    public Integer getEstatus() {
        return estatus;
    }

    /**
     * @param estatus the estatus to set
     */
    public void setEstatus(Integer estatus) {
        this.estatus = estatus;
    }

    
    /**
     * @return the idFolio
     */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "ID_FOLIO")
    public Integer getIdFolio() {
        return idFolio;
    }

    /**
     * @param idFolio the idFolio to set
     */
    public void setIdFolio(Integer idFolio) {
        this.idFolio = idFolio;
    }
	/**
	 * @return the empresa
	 */
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	public MEmpresa getEmpresa() {
		return empresa;
	}

	/**
	 * @param empresa the empresa to set
	 */
	public void setEmpresa(MEmpresa empresa) {
		this.empresa = empresa;
	}
  
	public String FECHA(){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(fecha);
	}
        
        
//        public void escapeSqlAndHtmlCharacters(){
//            this.id=(this.id != null)? StringEscapeUtils.escapeSql(id) : "";
//            this.serie=(this.serie != null)? StringEscapeUtils.escapeSql(serie) : "";
//            this.tipoDoc=(this.tipoDoc != null) ? StringEscapeUtils.escapeSql(tipoDoc) : "";
//            this.descripcion=(this.descripcion != null) ? StringEscapeUtils.escapeSql(descripcion) : "";
//            
//            this.id=(this.id != null)? StringEscapeUtils.escapeHtml(id) : "";
//            this.serie=(this.serie != null)? StringEscapeUtils.escapeHtml(serie) : "";
//            this.tipoDoc=(this.tipoDoc != null) ? StringEscapeUtils.escapeHtml(tipoDoc) : "";
//            this.descripcion=(this.descripcion != null) ? StringEscapeUtils.escapeHtml(descripcion) : "";
//        }
}
