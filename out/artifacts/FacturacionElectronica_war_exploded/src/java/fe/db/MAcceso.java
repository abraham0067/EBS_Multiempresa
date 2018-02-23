package fe.db;

/**
 * @Author Liliana pablo
 * @Date 14-01-2013
 * Tabla de Acceso para almacenar e identificar a usuarios
 */
import java.io.Serializable;
import java.text.SimpleDateFormat;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.CascadeType;
import org.hibernate.annotations.Index;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Temporal;
//import org.apache.commons.lang.StringEscapeUtils;

@SuppressWarnings("serial")
@Entity
@Table(name = "M_ACCESO")
public class MAcceso implements Serializable {
	/** Identificador Unico AutoIncremental*/
	private Integer id;
	/**  Alias del usuario*/
	private String usuario;
	/** Password*/
	private String clave;
	/** Perfil del usuario*/
	private MPerfil perfil;
	/**Email */
	private String email;
	/** Nombre del usuario*/
	private String nombre;
	/** Estatus del usuario  
	 * 0: Bloqueado permanete, 
	 * 1: Activo,
	 *  2: Bloqueo temporal, 
	 *   3: Auto-registro no activo */
	private Integer estatus = 1;
	/** Fecha de creaci�n */
	private Date fechaRegistro = new Date();
	/** Intentos del usuario	 */
	private Integer intentos = 0;
	/** Fecha de Ultimo acceso	 */
	private Date ultimoAcceso = new Date();
	/** Fecha de Ultimo intento  */
	private Date ultimoIntento = new Date();
	/** Indicador de cambio de clave   1:cambiar clave 0: No cambiar clave */
	private Integer cambiaClave = 1;
	/** R.F.C. Fiscal */
	private String rfc;
	/** N�mero de cliente */
	private String cliente;	
	/** Empresa padre */
	private MEmpresa empresa = new MEmpresa();
	/** Empresas que tendran acceso */
	private List<MEmpresa> empresas = new ArrayList<MEmpresa>();
	/** Nivel de usuario (Externo->1/ Interno -> 0)*/
	private Nivel nivel = Nivel.INTERNO;
	
	private String sector="";
        
	private String verificationKey="";

	public static enum Nivel {

		INTERNO, EXTERNO
	}
	
	
	public MAcceso() {
	}

	public MAcceso(String usuario, String clave, MPerfil perfil,
			String email, String nombre, Integer estatus, Date fechaRegistro,
			Integer intentos, Date ultimoAcceso, Date ultimoIntento,
			Integer cambiaClave, String rfc, String cliente,
			MEmpresa empresa, List<MEmpresa> empresas,
			Nivel nivel) {
		super();	
		this.usuario = usuario;
		this.clave = clave;
		this.perfil = perfil;
		this.email = email;
		this.nombre = nombre;
		this.estatus = estatus;
		this.fechaRegistro = fechaRegistro;
		this.intentos = intentos;
		this.ultimoAcceso = ultimoAcceso;
		this.ultimoIntento = ultimoIntento;
		this.cambiaClave = cambiaClave;
		this.rfc = rfc;
		this.cliente = cliente;
		this.empresa = empresa;
		this.empresas = empresas;
		this.nivel = nivel;
	}
	
	public MAcceso(String usuario, String clave, MPerfil perfil,
			String email, String nombre, Integer estatus, Integer intentos,
			Integer cambiaClave, String rfc, 
			MEmpresa empresa, Nivel nivel) {
		super();	
		this.usuario = usuario;
		this.clave = clave;
		this.perfil = perfil;
		this.email = email;
		this.nombre = nombre.trim().toUpperCase();
		this.estatus = estatus;
		this.intentos = intentos;		
		this.cambiaClave = cambiaClave;
		this.rfc = rfc.trim().toUpperCase();	
		this.empresa = empresa;
		this.nivel = nivel;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}

	@Index(name = "ACCESO_USUARIO_IDX")
	@Column(name = "USUARIO", unique = true, nullable = false, length = 20)
	public String getUsuario() {
		return usuario;
	}

	@Column(name = "CLAVE", nullable = false, length = 100)
	public String getClave() {
		return clave;
	}



    @ManyToOne(optional = false, fetch = FetchType.EAGER)
	public MPerfil getPerfil() {
		return perfil;
	}

	public String getEmail() {
		return email;
	}

	@Index(name = "ACCESO_NOMBRE_IDX")
	@Column(name = "NOMBRE")
	public String getNombre() {
		return nombre;
	}

	@Column(name = "ESTATUS", nullable = false)
	public Integer getEstatus() {
		return estatus;
	}
	
	@Index(name = "ACCESO_FECHA_IDX")
	@Column(name = "FECHA_REGISTRO", nullable = false)
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	@Column(name = "INTENTOS", nullable = false)
	public Integer getIntentos() {
		return intentos;
	}
	
	@Index(name = "ACCESO_ULTIMO_IDX")
	@Column(name = "ULTIMO_ACCESO", nullable = false)
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	public Date getUltimoAcceso() {
		return ultimoAcceso;
	}

	@Index(name = "ACCESO_ULTIMO_INTENTO_IDX")
	@Column(name = "ultimoIntento", nullable = false)
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	public Date getUltimoIntento() {
		return ultimoIntento;
	}

	@Column(name = "CAMBIO_CLAVE", nullable = false)
	public Integer getCambiaClave() {
		return cambiaClave;
	}

	@Column(name = "RFC", length = 13)
	public String getRfc() {
		return rfc;
	}
	
	@Column(name = "CLIENTE", length = 50)
	public String getCliente() {
		return cliente;
	}
	
	@Column(name = "NIVEL")
	public Nivel getNivel() {
		return nivel;
	}

	
	

	public void setId(Integer id) {
		this.id = id;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public void setPerfil(MPerfil perfil) {
		this.perfil = perfil;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setNombre(String nombre) {
		this.nombre =(nombre!=null && !"".equals(nombre.trim()))?nombre.trim().toUpperCase():null;
	}

	public void setEstatus(Integer estatus) {
		this.estatus = estatus;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public void setIntentos(Integer intentos) {
		this.intentos = intentos;
	}

	public void setUltimoAcceso(Date ultimoAcceso) {
		this.ultimoAcceso = ultimoAcceso;
	}

	public void setUltimoIntento(Date ultimoIntento) {
		this.ultimoIntento = ultimoIntento;
	}

	public void setCambiaClave(Integer cambiaClave) {
		this.cambiaClave = cambiaClave;
	}

	public void setRfc(String rfc) {
		this.rfc = (rfc!=null && !"".equals(rfc.trim()))?rfc.trim().toUpperCase():null;;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	

	

	/**
	 * @return the empresa
	 */
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	public MEmpresa getEmpresa() {
		return empresa;
	}

	/**
	 * @return the empresas
	 */
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public List<MEmpresa> getEmpresas() {
		return empresas;
	}

	/**
	 * @param empresa the empresa to set
	 */
	
	public void setEmpresa(MEmpresa empresa) {
		this.empresa = empresa;
	}

	/**
	 * @param empresas the empresas to set
	 */

	public void setEmpresas(List<MEmpresa> empresas) {
		this.empresas = empresas;
	}

	public void setNivel(Nivel nivel) {
		this.nivel = nivel;
	};
	
	

	/**
	 * @return the sector
	 */
	@Column(name = "SECTOR", nullable = true, length = 500)
	public String getSector() {
		return sector;
	}

	/**
	 * @param sector the sector to set
	 */
	public void setSector(String sector) {
		this.sector = sector;
	}

	public String FechaF() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ultimoAcceso);
	}
        
    @Column(name = "VERIFICATION_KEY", nullable = true, length = 250)
    public String getVerificationKey() {
        return verificationKey;
    }

    public void setVerificationKey(String verificationKey) {
        this.verificationKey = verificationKey;
    }
    
//    public void escapeSqlAndHtmlCharacters(){
//        this.usuario = (this.usuario != null) ? StringEscapeUtils.escapeSql(this.usuario):"";
//        //this.clave = (this.clave != null) ? StringEscapeUtils.escapeSql(this.clave):"";
//        //this.email = (this.email != null) ? StringEscapeUtils.escapeSql(this.email):"";
//        this.nombre = (this.nombre != null) ? StringEscapeUtils.escapeSql(this.nombre):"";
//        //this.rfc = (this.rfc != null) ? StringEscapeUtils.escapeSql(this.rfc): "";
//        this.cliente = (this.cliente != null) ? StringEscapeUtils.escapeSql(this.cliente) : "";        
//        this.sector = (this.sector != null) ? StringEscapeUtils.escapeSql(this.sector) : "";
//        
//        this.usuario = (this.usuario != null) ? StringEscapeUtils.escapeHtml(this.usuario):"";
//        //this.clave = (this.clave != null) ? StringEscapeUtils.escapeHtml(this.clave):"";
//        //this.email = (this.email != null) ? StringEscapeUtils.escapeHtml(this.email):"";
//        this.nombre = (this.nombre != null) ? StringEscapeUtils.escapeHtml(this.nombre):"";
//        //this.rfc = (this.rfc != null) ? StringEscapeUtils.escapeHtml(this.rfc): "";
//        this.cliente = (this.cliente != null) ? StringEscapeUtils.escapeHtml(this.cliente) : "";        
//        this.sector = (this.sector != null) ? StringEscapeUtils.escapeHtml(this.sector) : "";        
//    }
	
}
