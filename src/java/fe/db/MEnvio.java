package fe.db;

/**
 *
 * @Author Carlo Garcia Sanchez
 * @Date 04-10-2012
 *
 * Tabla de Log de FE
 *
 */
 
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Column;
import org.hibernate.annotations.Index;
import javax.persistence.ManyToOne;
import java.util.Date;
import javax.persistence.Temporal;
//import org.apache.commons.lang.StringEscapeUtils;

@SuppressWarnings("serial")
@Entity
@Table(name="M_ENVIO")
public class MEnvio implements Serializable {
	private int id = 0;
	private MEmpresa empresa;
	private MCfd cfd;
	private String rfc = "";
	@SuppressWarnings("unused")
	private int metodo = 0;
	private Date fecha = new Date();
	private String servidor = "";
	private String usuario = "";
	private String clave = "";
	private String dirFuente = "";
	private String dirDestino = "";
	
	protected MEnvio() {}
	
	public MEnvio(MEmpresa empresa, MCfd cfd, String rfc, int metodo, Date fecha, String servidor) {
		this.empresa = empresa;
		this.cfd=cfd;
		this.rfc = rfc;
		this.metodo = metodo;
		this.fecha = fecha;
		this.servidor = servidor;
	}

 	@Id
 	@GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="ID", nullable=false)
	public int getId() {
		return id;
	}

    @ManyToOne
	public MEmpresa getEmpresa() {
		return (this.empresa); 
	}

    @ManyToOne
	public MCfd getCfd() {
		return cfd;
	}


	
	@Index(name = "ENV_RFC_IDX") 
    @Column(name="RFC", nullable=true, length=15)
	public String getRfc() {
		return rfc;
	}

	@Index(name = "ENV_FECHA_IDX") 
    @Column(name="FECHA", nullable=false)
        @Temporal(javax.persistence.TemporalType.TIMESTAMP)
	public Date getFecha() {
		return fecha;
	}
	
    @Column(name="SERVIDOR", nullable=true, length=500)
	public String getServidor() {
		return servidor;
	}

    @Column(name="USUARIO", nullable=true, length=50)
	public String getUsuario() {
		return usuario;
	}

    @Column(name="CLAVE", nullable=true, length=50)
	public String getClave() {
		return clave;
	}

    @Column(name="DIR_FUENTE", nullable=true, length=500)
	public String getDirFuente() {
		return dirFuente;
	}

    @Column(name="DIR_DESTINO", nullable=true, length=500)
	public String getDirDestino() {
		return dirDestino;
	}

	public void setId(int id) {
		this.id = id; 
	}

	public void setEmpresa(MEmpresa empresa) {
		this.empresa = empresa; 
	}

	public void setCfd(MCfd cfd) {
		this.cfd = cfd;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public void setServidor(String servidor) {
		this.servidor = servidor;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public void setDirFuente(String dirFuente) {
		this.dirFuente = dirFuente;
	}

	public void setDirDestino(String dirDestino) {
		this.dirDestino = dirDestino;
	}
        
//        public void escapeSqlAndHtmlCharacters(){
//            this.rfc = (this.rfc != null) ? StringEscapeUtils.escapeSql(this.rfc) : "";
//            this.servidor = (this.servidor != null) ? StringEscapeUtils.escapeSql(this.servidor) : "";
//            this.usuario = (this.usuario != null) ? StringEscapeUtils.escapeSql(this.usuario) : "";
//            this.clave = (this.clave != null) ? StringEscapeUtils.escapeSql(this.clave) : "";
//            this.dirFuente = (this.dirFuente != null) ? StringEscapeUtils.escapeSql(this.dirFuente) : "";
//            this.dirDestino = (this.dirDestino != null) ? StringEscapeUtils.escapeSql(this.dirDestino) : "";
//            
//            this.rfc = (this.rfc != null) ? StringEscapeUtils.escapeHtml(this.rfc) : "";
//            this.servidor = (this.servidor != null) ? StringEscapeUtils.escapeHtml(this.servidor) : "";
//            this.usuario = (this.usuario != null) ? StringEscapeUtils.escapeHtml(this.usuario) : "";
//            this.clave = (this.clave != null) ? StringEscapeUtils.escapeHtml(this.clave) : "";
//            this.dirFuente = (this.dirFuente != null) ? StringEscapeUtils.escapeHtml(this.dirFuente) : "";
//            this.dirDestino = (this.dirDestino != null) ? StringEscapeUtils.escapeHtml(this.dirDestino) : "";
//        }
}