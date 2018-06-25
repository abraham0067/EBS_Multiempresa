/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.db;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
//import org.apache.commons.lang.StringEscapeUtils;

/**
 * 
 * @Author Liliana pablo
 * @Date 14-01-2013
 * 
 */
@Entity
@Table(name = "M_LOG_APP")
public class MLogApp implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	/*
	 * @SequenceGenerator(name = "SLogApp", sequenceName = "SECUENCIA_LOG_APP",
	 * initialValue = 1, allocationSize = 1)
	 * 
	 * @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =
	 * "SLogApp")
	 */
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", nullable = false)
	private int id = 0;
	@ManyToOne
	private MEmpresa empresa;
	@Column(name = "FOLIO", length = 100)
	private String folio = "";
	@Column(name = "SERIE", length = 100)
	private String serie = "";
	@Column(name = "FECHA", nullable = false)
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	private Date fecha = new Date();
	@Column(name = "ORIGEN", nullable = false)
	private int origen = 0;
	@Column(name = "CLASE", nullable = false, length = 70)
	private String clase = "";
	@Column(name = "METODO", nullable = false, length = 70)
	private String metodo = "";
	@Column(name = "ERROR", nullable = false, length = 255)
	private String error = "";
	@Lob
	@Column(name = "TRACE", nullable = true)
	private String trace = "";

	@Column(name = "ESTATUS", nullable = false, length = 255)
	private int estatus = 0;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the empresa
	 */
	public MEmpresa getEmpresa() {
		return empresa;
	}

	/**
	 * @param empresa
	 *            the empresa to set
	 */
	public void setEmpresa(MEmpresa empresa) {
		this.empresa = empresa;
	}

	/**
	 * @return the fecha
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * @param fecha
	 *            the fecha to set
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return the clase
	 */
	public String getClase() {
		return clase;
	}

	/**
	 * @param clase
	 *            the clase to set
	 */
	public void setClase(String clase) {
		this.clase = clase;
	}

	/**
	 * @return the metodo
	 */
	public String getMetodo() {
		return metodo;
	}

	/**
	 * @param metodo
	 *            the metodo to set
	 */
	public void setMetodo(String metodo) {
		this.metodo = metodo;
	}

	/**
	 * @return the error
	 */
	public String getError() {
		return error;
	}

	/**
	 * @param error
	 *            the error to set
	 */
	public void setError(String error) {
		this.error = error;
	}

	/**
	 * @return the trace
	 */
	public String getTrace() {
		return trace;
	}

	/**
	 * @param trace
	 *            the trace to set
	 */
	public void setTrace(String trace) {
		this.trace = trace;
	}

	/**
	 * @return the origen
	 */
	public int getOrigen() {
		return origen;
	}

	/**
	 * @param origen
	 *            the origen to set
	 */
	public void setOrigen(int origen) {
		this.origen = origen;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String FechaF() {
		return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(fecha);
	}

	public int getEstatus() {
		return estatus;
	}

	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}

	//        public void escapeSqlAndHtmlCharacters(){
//            this.clase = (this.clase != null)? StringEscapeUtils.escapeSql(this.clase) : "";
//            this.clase = (this.clase != null)? StringEscapeUtils.escapeHtml(this.clase) : "";
//        }
}
