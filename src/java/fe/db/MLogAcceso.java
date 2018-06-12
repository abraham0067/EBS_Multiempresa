package fe.db;

/**
 *
 * @Author Carlo Garcia Sanchez
 * @Date 04-10-2012
 *
 * Tabla de Log de Accesos
 *
 */

import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

//import org.apache.commons.lang.StringEscapeUtils;

@Entity
@Table(name = "M_LOG_ACCESO") 
public class MLogAcceso implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id = 0;   
    private MCfd cfd;
    private MAcceso acceso;
    private Date fecha;
    private String mensaje = "";

    protected MLogAcceso() {
    }

    public MLogAcceso( MCfd cfd, MAcceso acceso, Date fecha, String mensaje) {        
        this.cfd = cfd;
        this.acceso = acceso;
        this.fecha = fecha;
        this.mensaje = mensaje;
    }
    
    public MLogAcceso( MAcceso acceso, Date fecha, String mensaje) {        
        this.acceso = acceso;
        this.fecha = fecha;
        this.mensaje = mensaje;
    }

    @Id
    //@SequenceGenerator(name = "SLogAcceso", sequenceName = "SECUENCIA_LOG_ACCESO", initialValue = 1, allocationSize = 1)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SLogAcceso")
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

   

    @ManyToOne
    public MCfd getCfd() {
        return cfd;
    }

    @ManyToOne
    @Index(name = "LOGACC_ID_IDX")
    public MAcceso getAcceso() {
        return acceso;
    }

    @Index(name = "LOGACC_FECHA_IDX")
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

    public void setAcceso(MAcceso acceso) {
        this.acceso = acceso;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String FechaFU() {
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(fecha);
    }
    
//    public void escapeSqlAndHtmlCharacters(){
//        this.mensaje  = (this.mensaje != null)?StringEscapeUtils.escapeSql(this.mensaje) : "";
//        this.mensaje  = (this.mensaje != null)?StringEscapeUtils.escapeHtml(this.mensaje) : "";
//    }
}