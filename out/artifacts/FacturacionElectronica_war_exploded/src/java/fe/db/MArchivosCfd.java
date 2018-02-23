package fe.db;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
//import org.apache.commons.lang.StringEscapeUtils;

import org.hibernate.annotations.Index;

@SuppressWarnings("serial")
@Entity
@Table(name = "M_ARCHIVOS_CFD")
public class MArchivosCfd implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private int id;

	/**
	 * Nombre del archivo
	 */

	@Index(name = "ARCHIVO_CFD_USUARIO_IDX")
	@Column(name = "NOMBRE", nullable = false, length = 100)
	private String nombre;

	@Column(name = "RUTA", nullable = false, length = 200)
	private String ruta;
	/**
	 * usuario del sistema que lo registro
	 */
	@Column(name = "USUARIO", nullable = false, length = 20)
	private String usuario;
	/**
	 * Fecha en que se registras el archivo.
	 */
	@Index(name = "ARCHIVO_FECHA_IDX")
	@Column(name = "FECHA", nullable = false)
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	private Date fecha = new Date();

	
	@ManyToOne
	private MCfd cfd;

	public MArchivosCfd() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public MArchivosCfd(String nombre, String ruta, String usuario, Date fecha,
			MCfd cfd) {
		super();
		this.nombre = nombre;
		this.ruta = ruta;
		this.usuario = usuario;
		this.fecha = fecha;
		this.cfd = cfd;
	}


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
	 * @return the ruta
	 */
	public String getRuta() {
		return ruta;
	}

	/**
	 * @param ruta
	 *            the ruta to set
	 */
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario
	 *            the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
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
	 * @return the cfd
	 */
	public MCfd getCfd() {
		return cfd;
	}

	/**
	 * @param cfd
	 *            the cfd to set
	 */
	public void setCfd(MCfd cfd) {
		this.cfd = cfd;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
        
//        public void escapeSqlAndHtmlCharacters(){
//            this.nombre = (this.nombre != null) ? StringEscapeUtils.escapeSql(this.nombre) : "";
//            this.ruta = (this.ruta != null) ? StringEscapeUtils.escapeSql(this.ruta) : "";
//            this.usuario = (this.usuario != null) ? StringEscapeUtils.escapeSql(this.usuario) : "";
//            
//            this.nombre = (this.nombre != null) ? StringEscapeUtils.escapeHtml(this.nombre) : "";
//            this.ruta = (this.ruta != null) ? StringEscapeUtils.escapeHtml(this.ruta) : "";
//            this.usuario = (this.usuario != null) ? StringEscapeUtils.escapeHtml(this.usuario) : "";
//        }
}
