package fe.db;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

import org.apache.commons.codec.binary.Base64;
//import org.apache.commons.lang.StringEscapeUtils;
import org.hibernate.annotations.Index;

@SuppressWarnings("serial")
@Entity
@Table(name = "M_PLANTILLA")
public class MPlantilla implements Serializable {
	
	private int id = 0;
	private Date fecha = new Date();
	private byte[] plantilla = new byte[0];
	private byte[] imagen = new byte[0];
	private String nombre = "";
	private float version;
	private String rootPath = "/Comprobante/Conceptos/Concepto";
	/* 1- platilla activa, 2- plantilla desactivada */
	private int estatus = 1;
	
	private MEmpresa empresa;

	public MPlantilla(Date fecha, byte[] plantilla,
			byte[] imagen, String nombre, float version, int estatus) {
		super();
		this.fecha = fecha;
		this.plantilla = plantilla;
		this.imagen = imagen;
		this.nombre = nombre;
		this.version = version;
		this.estatus = estatus;
	}

	public MPlantilla() {
	}

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", nullable = false)
	public int getId() {
		return id;
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
	 * @return the plantilla
	 */
	@Lob
	@Column(name = "PLANTILLA", nullable = false)
	public byte[] getPlantilla() {
		return plantilla;
	}

	/**
	 * @return the imagen
	 */
	@Lob
	@Column(name = "IMAGEN", nullable = false)
	public byte[] getImagen() {
		return imagen;
	}

	/**
	 * @return the nombre
	 */
	@Index(name = "PLANTILLA_NOMBRE_IDX")
	@Column(name = "NOMBRE", nullable = false, unique=true, length = 100)
	public String getNombre() {
		return nombre;
	}

	/**
	 * @return the version
	 */
	@Column(name = "VERSION", nullable = false, precision = 13, scale = 1)
	public float getVersion() {
		return version;
	}

	/**
	 * @return the estatus
	 */
	@Index(name = "PLANTILLA_ST_IDX")
	@Column(name = "ESTATUS", nullable = false, precision = 1, scale = 0)
	public int getEstatus() {
		return estatus;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @param fecha
	 *            the fecha to set
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * @param plantilla
	 *            the plantilla to set
	 */
	
	public void setPlantilla(byte[] plantilla) {
		this.plantilla = plantilla;
	}

	/**
	 * @param imagen
	 *            the imagen to set
	 */
	public void setImagen(byte[] imagen) {
		this.imagen = imagen;
	}

	/**
	 * @param nombre
	 *            the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(float version) {
		this.version = version;
	}

	/**
	 * @param estatus
	 *            the estatus to set
	 */
	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}
	

	
	/**
	 * @return the empresa
	 */
	@ManyToOne(optional=false, fetch=FetchType.EAGER)	
	public MEmpresa getEmpresa() {
		return empresa;
	}

	/**
	 * @param empresa the empresa to set
	 */
	public void setEmpresa(MEmpresa empresa) {
		this.empresa = empresa;
	}

	/**
	 * @return the rootPath
	 */
	@Column(name = "ROOT_PATH", nullable = true, length = 255)
	public String getRootPath() {
		return rootPath;
	}

	/**
	 * @param rootPath the rootPath to set
	 */
	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public String URL(){
		String base64 = Base64.encodeBase64String(imagen);
		base64 = "data:image/png;base64," + base64;
		return base64;

	}
	

	public String FechaF() {
		return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(fecha);
	}
        
//        public void escapeSqlAndHtmlCharacters(){
//            this.nombre = (this.nombre != null)? StringEscapeUtils.escapeSql(this.nombre):"";
//            this.rootPath =(this.rootPath != null) ?  StringEscapeUtils.escapeSql(this.rootPath) : "";
//            this.nombre = (this.nombre != null)? StringEscapeUtils.escapeHtml(this.nombre):"";
//            this.rootPath =(this.rootPath != null) ?  StringEscapeUtils.escapeHtml(this.rootPath) : "";
//        }
}
