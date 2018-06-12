package fe.db;

import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

//import org.apache.commons.lang.StringEscapeUtils;


@SuppressWarnings("serial")
@Entity
@Table(name = "M_LLAVE")
public class MLlave implements Serializable {
	private int id;
	private String noCertificado;
	private Date fechaRegistro= new Date();
	private byte[] certificado = new byte[0];
	private byte[] llave = new byte[0];
	private byte[] llave2 = new byte[0];
	private String clave;
	private Date inicioVigencia;
	private Date finVigencia;
	/*
	 * 1:- Activo
	 * 2.- Inactivo
	 */
	private int estatus=1;
	private MEmpresa empresa;
	
	public MLlave() {
		super();
	}

	public MLlave(String noCertificado, Date fechaRegistro, byte[] certificado,
			byte[] llave, byte[] llave2, String clave, Date inicioVigencia,
			Date finVigencia, int estatus, MEmpresa empresa) {
		super();
		this.noCertificado = noCertificado;
		this.fechaRegistro = fechaRegistro;
		this.certificado = certificado;
		this.llave = llave;
		this.llave2 = llave2;
		this.clave = clave;
		this.inicioVigencia = inicioVigencia;
		this.finVigencia = finVigencia;
		this.estatus = estatus;
		this.empresa = empresa;
	}

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	public int getId() {
		return id;
	}
	/**
	 * @return the noCertificado
	 */
	@Index(name = "LLAVE_NO_CERTIFICADO_IDX")
	@Column(name = "NO_CERTIFICADO",nullable = false, length = 100)
	public String getNoCertificado() {
		return noCertificado;
	}
	/**
	 * @return the fechaRegistro
	 */
	@Index(name = "LLAVE_FECHA_IDX")
	@Column(name = "FECHA_REGISTRO", nullable = false)
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	/**
	 * @return the certificado
	 */
	@Lob
    @Column(name="CERTIFICADO", nullable=false)
	public byte[] getCertificado() {
		return certificado;
	}
	/**
	 * @return the llave
	 */
	@Lob
    @Column(name="LLAVE", nullable=false)
	public byte[] getLlave() {
		return llave;
	}
	/**
	 * @return the clave
	 */
	@Column(name = "CLAVE", nullable = false, length=50)
	public String getClave() {
		return clave;
	}
	/**
	 * @return the inicioVigencia
	 */
	@Index(name = "INICIO_VIGENCIA_IDX")
	@Column(name = "INICIO_VIGENCIA", nullable = false)
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	public Date getInicioVigencia() {
		return inicioVigencia;
	}
	/**
	 * @return the finVigencia
	 */
	@Index(name = "FIN_VIGENCIA_IDX")
	@Column(name = "FIN_VIGENCIA", nullable = false)
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	public Date getFinVigencia() {
		return finVigencia;
	}
	/**
	 * @return the estatus
	 */
	@Column(name = "ESTATUS", nullable = false)
	public int getEstatus() {
		return estatus;
	}
	/**
	 * @return the empresa
	 */
	@ManyToOne
	public MEmpresa getEmpresa() {
		return empresa;
	}
	/**
	 * @return the llave2
	 */
	@Lob
    @Column(name="LLAVE2", nullable=false)
	public byte[] getLlave2() {
		return llave2;
	}

	/**
	 * @param llave2 the llave2 to set
	 */
	public void setLlave2(byte[] llave2) {
		this.llave2 = llave2;
	}

	/**
	 * @param empresa the empresa to set
	 */
	public void setEmpresa(MEmpresa empresa) {
		this.empresa = empresa;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @param noCertificado the noCertificado to set
	 */
	public void setNoCertificado(String noCertificado) {
		this.noCertificado = noCertificado;
	}
	/**
	 * @param fechaRegistro the fechaRegistro to set
	 */
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	/**
	 * @param certificado the certificado to set
	 */
	public void setCertificado(byte[] certificado) {
		this.certificado = certificado;
	}
	/**
	 * @param llave the llave to set
	 */
	public void setLlave(byte[] llave) {
		this.llave = llave;
	}
	/**
	 * @param clave the clave to set
	 */
	public void setClave(String clave) {
		this.clave = clave;
	}
	/**
	 * @param inicioVigencia the inicioVigencia to set
	 */
	public void setInicioVigencia(Date inicioVigencia) {
		this.inicioVigencia = inicioVigencia;
	}
	/**
	 * @param finVigencia the finVigencia to set
	 */
	public void setFinVigencia(Date finVigencia) {
		this.finVigencia = finVigencia;
	}
	/**
	 * @param estatus the estatus to set
	 */
	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}
	public String FechaRegistro() {
		return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(fechaRegistro);
	}
	
	public String FechaInicio() {
		return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(inicioVigencia);
	}

	public String FechaFin() {
		return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(finVigencia);
	}
        
//        public void escapeSqlAndHtmlCharacters(){
//            this.clave = (this.clave != null)? StringEscapeUtils.escapeSql(this.clave) : "";
//            this.noCertificado = (this.noCertificado != null)? StringEscapeUtils.escapeSql(this.noCertificado) : "";
//            this.clave = (this.clave != null)? StringEscapeUtils.escapeHtml(this.clave) : "";
//            this.noCertificado = (this.noCertificado != null)? StringEscapeUtils.escapeHtml(this.noCertificado) : "";
//        }
}
