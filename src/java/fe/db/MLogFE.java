package fe.db;

/**
 *
 * @Author Carlo Garcia Sanchez
 * @Date 04-10-2012
 *
 * Tabla de Log de FE
 *
 */

import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
//import org.apache.commons.lang.StringEscapeUtils;

@Entity
@Table(name = "M_LOG_FE")
public class MLogFE implements Serializable {
   
	private static final long serialVersionUID = 1L;
	
	private int id = 0;
    private MEmpresa empresa;
    private MCfd cfd;
    private int estatus = 0;
    private Date fecha;
    private String mensaje = "";

    protected MLogFE() {
    }

    public MLogFE(MEmpresa empresa, MCfd cfd, int estatus, Date fecha, String mensaje) {
        this.empresa = empresa;
        this.cfd = cfd;
        this.estatus = estatus;
        this.fecha = fecha;
        this.mensaje = mensaje;
    }

    @Id
    /*@SequenceGenerator(name = "SLogFE", sequenceName = "SECUENCIA_LOG_FE", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SLogFE")*/
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

    @Index(name = "LOGFE_ESTATUS_IDX")
    @Column(name = "ESTATUS", nullable = false)
    public int getEstatus() {
        return estatus;
    }

    @Index(name = "LOGFE_FECHA_IDX")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "FECHA", nullable = false)
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

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    public String FechaF() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(fecha);
    }
    
//    public void escapeSqlAndHtmlCharacters(){
//        this.mensaje = (this.mensaje != null)? StringEscapeUtils.escapeSql(this.mensaje) :"";
//        this.mensaje = (this.mensaje != null)? StringEscapeUtils.escapeHtml(this.mensaje) :"";
//    }
}