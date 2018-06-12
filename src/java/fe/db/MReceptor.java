package fe.db;

/**
 * @author Amiranda
 *
 */

import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.io.Serializable;

//import org.apache.commons.lang.StringEscapeUtils;


@Entity
@Table(name = "M_RECEPTOR")
public class MReceptor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id = 0;
	private String rfcOrigen = "";
	private String razonSocial = "";
	private MDireccion direccion = new MDireccion();
	private String correo = "";
	private MEmpresa empresa = new MEmpresa();

	public MReceptor() {
	}

	
	public MReceptor(String rfcOrigen, String razonSocial, MDireccion direccion) {
		this.rfcOrigen = rfcOrigen;
		this.razonSocial = razonSocial;
		this.direccion = direccion;
	}


	public MReceptor(String rfcOrigen, String razonSocial,
			MDireccion direccion, String correo, MEmpresa empresa) {
		super();
		this.rfcOrigen = rfcOrigen;
		this.razonSocial = razonSocial;
		this.direccion = direccion;
		this.correo = correo;
		this.empresa = empresa;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", nullable = false)
	public int getId() {
		return id;
	}

	@Index(name = "REC_RFC_IDX")
	@Column(name = "RFC_ORIGEN", nullable = false, length = 15)
	public String getRfcOrigen() {
		return (this.rfcOrigen);
	}

	// @Embedded
	@Index(name = "REC_RS_IDX")
	@Column(name = "RAZON_SOCIAL", nullable = true, length = 100)
	public String getRazonSocial() {
		return (this.razonSocial);
	}

	/**
	 * @return the direccion
	 */
	@ManyToOne
	@Index(name = "RECEPTOR_DIRECCION_IDX")
	public MDireccion getDireccion() {
		return direccion;
	}

	/**
	 * Setters
	 */
	public void setId(int id) {
		this.id = id;
	}

	public void setRfcOrigen(String rfcOrigen) {
		this.rfcOrigen = (rfcOrigen != null && !rfcOrigen.trim().equals("")) ? rfcOrigen
				: "";
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = (razonSocial != null && !razonSocial.trim().equals(
				"")) ? razonSocial : "";
	}

	/**
	 * @param direccion
	 *            the direccion to set
	 */
	public void setDireccion(MDireccion direccion) {
		this.direccion = direccion;
	}

	@ManyToOne
	@Index(name = "RECEPTOR_EMPRESA_IDX")
	public MEmpresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(MEmpresa empresa) {
		this.empresa = empresa;
	}

	
	@Column(name = "CORREO", nullable = true)
	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String rfcRazonSoc() {
		return getRfcOrigen() + " - " + getRazonSocial();
	}


	//        public void escapeSqlAndHtmlCharacters(){
//            this.correo = (this.correo != null)?StringEscapeUtils.escapeSql(this.correo):"";
//            this.razonSocial = (this.razonSocial != null) ? StringEscapeUtils.escapeSql(this.razonSocial) : "";
//            this.rfcOrigen = (this.rfcOrigen != null) ? StringEscapeUtils.escapeSql(this.rfcOrigen) : "";
//            
//            this.correo = (this.correo != null)?StringEscapeUtils.escapeHtml(this.correo):"";
//            this.razonSocial = (this.razonSocial != null) ? StringEscapeUtils.escapeHtml(this.razonSocial) : "";
//            this.rfcOrigen = (this.rfcOrigen != null) ? StringEscapeUtils.escapeHtml(this.rfcOrigen) : "";
//            
//        }
}
