package fe.db;

/**
 *
 * @Author Carlo Garcia Sanchez
 * @Date 04-10-2012
 *
 * Tabla de Log de Envo
 *
 */
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Temporal;

import org.hibernate.annotations.Index;
import javax.persistence.ManyToOne;
import java.util.Date;
//import org.apache.commons.lang.StringEscapeUtils;

@Entity
@Table(name = "M_LOG_ENVIO") 
public class MLogEnvio implements Serializable {

    private static final long serialVersionUID = 1L;
	
	private int id = 0;
    private MEmpresa empresa;
    private MCfd cfd;
    private int metodo = 0;
    private int estatus = 0;
    private Date fecha;
    private String mensaje = "";

    protected MLogEnvio() {
    }

    public MLogEnvio(MEmpresa empresa, MCfd cfd, int metodo, int estatus, Date fecha, String mensaje) {
        this.empresa = empresa;
        this.metodo = metodo;
        this.cfd = cfd;
        this.estatus = estatus;
        this.fecha = fecha;
        this.mensaje = mensaje;
    }
    public MLogEnvio ( MCfd cfd,Date fecha, String mensaje){
        this.cfd = cfd;        
        this.fecha = fecha;
        this.mensaje = mensaje;
    }

    @Id
   /*@SequenceGenerator(name = "SLogEnvio", sequenceName = "SECUENCIA_LOG_ENVIO", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SLogEnvio")*/
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
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

    @Index(name = "LOGENV_METODO_IDX")
    @Column(name = "METODO", nullable = false)
    public int getMetodo() {
        return metodo;
    }

    @Index(name = "LOGENV_ESTATUS_IDX")
    @Column(name = "ESTATUS", nullable = false)
    public int getEstatus() {
        return estatus;
    }

    @Index(name = "LOGENV_FECHA_IDX")
    @Column(name = "FECHA", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getFecha() {
        return fecha;
    }

    @Column(name = "MENSAJE", nullable = true, length = 500)
    public String getMensaje() {
        return mensaje;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCfd(MCfd cfd) {
        this.cfd = cfd;
    }

    public void setEmpresa(MEmpresa empresa) {
        this.empresa = empresa;
    }

    public void setMetodo(int metodo) {
        this.metodo = metodo;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    
//    public void escapeSqlAndHtmlCharacters(){
//        this.mensaje = (this.mensaje  != null)? StringEscapeUtils.escapeSql(this.mensaje) : "";
//        this.mensaje = (this.mensaje  != null)? StringEscapeUtils.escapeHtml(this.mensaje) : "";
//    }
}