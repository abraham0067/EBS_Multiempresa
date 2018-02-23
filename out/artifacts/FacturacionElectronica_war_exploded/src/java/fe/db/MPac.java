package fe.db;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
//import org.apache.commons.lang.StringEscapeUtils;

import org.hibernate.annotations.Index;

@SuppressWarnings("serial")
@Entity
@Table(name = "M_PAC")
public class MPac implements Serializable{
	private long id;
	private String nombre;
	private String className;
	private Date fecha= new Date();
	
	public MPac() {
		super();
	}

	public MPac(String nombre, String className) {
		super();
		this.nombre = nombre;
		this.className = className;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	@Index(name = "PAC_NOMBRE_IDX")
	@Column(name = "NOMBRE" ,nullable = false,unique=true ,length = 50)
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	@Index(name = "PAC_CLASSNAME_IDX")
	@Column(name = "CLASSNAME",nullable = false, length = 50)
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	
	@Index(name = "PAC_FECHA_IDX")
	@Column(name = "FECHA", nullable = false)
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public String FechaRegistro() {
		return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(fecha);
	}
//        public void escapeSqlAndHtmlCharacters(){
//            this.className = (this.className != null) ? StringEscapeUtils.escapeSql(this.className) : "";
//            this.nombre = (this.nombre != null) ? StringEscapeUtils.escapeSql(this.nombre) : "";
//            
//            this.className = (this.className != null) ? StringEscapeUtils.escapeHtml(this.className) : "";
//            this.nombre = (this.nombre != null) ? StringEscapeUtils.escapeHtml(this.nombre) : "";
//        }

}
