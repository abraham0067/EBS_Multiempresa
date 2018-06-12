package fe.db;

import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

//import org.apache.commons.lang.StringEscapeUtils;

@SuppressWarnings("serial")
@Entity
@Table(name = "M_TIMBRE")
public class MTimbre  implements  Serializable{
	private long id;
	private Date fechaAlta= new Date();
	private long folioscomprados;
	private MEmpresaMTimbre empresatimbre;
	private String usuario;
	
	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the fechaAlta
	 */
	@Index(name = "TIMBRE_FECHA_IDX")
	@Column(name = "FECHA_ALTA", nullable = false)
	 @Temporal(javax.persistence.TemporalType.TIMESTAMP)
	public Date getFechaAlta() {
		return fechaAlta;
	}
	
	/**
	 * @return the empresatimbre
	 */
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	public MEmpresaMTimbre getEmpresatimbre() {
		return empresatimbre;
	}
	@Column(name = "FOLIOS_COMPRADOS", nullable = false)
	public long getFolioscomprados() {
		return folioscomprados;
	}

	public void setFolioscomprados(long folioscomprados) {
		this.folioscomprados = folioscomprados;
	}

	/**
	 * @param empresatimbre the empresatimbre to set
	 */
	public void setEmpresatimbre(MEmpresaMTimbre empresatimbre) {
		this.empresatimbre = empresatimbre;
	}

	/**
	 * @param fechaAlta the fechaAlta to set
	 */
	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}
	/**
	 * @return the usuario
	 */
	@Column(name = "USUARIO", length = 50)
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * @param folioscomprados the folioscomprados to set
	 */
	
	
	
	public String Fechas(){
		return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(fechaAlta);
	}
        
//        public void escapeSqlAndHtmlCharacters(){
//            this.usuario = (this.usuario != null)? StringEscapeUtils.escapeSql(this.usuario) : "" ;
//            
//            this.usuario = (this.usuario != null)? StringEscapeUtils.escapeHtml(this.usuario) : "" ;
//        }
	
}
