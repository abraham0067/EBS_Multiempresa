package fe.db;

/**
 *
 * @Author Carlo Garcia Sanchez
 * @Date 04-10-2012
 *
 * Tabla de Informaci�n Aduanera
 *
 */

import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
//import org.apache.commons.lang.StringEscapeUtils;

@SuppressWarnings("serial")
@Entity
@Table(name="M_INFORMACION_ADUANERA")
public class MInfoAduana implements Serializable {
	private int id = 0;
	private MEmpresa empresa;
	private MCfd cfd;

	private String pedimento = "";
	private Date fecha;
	private String aduana= "";
	
	protected MInfoAduana() {}
	
	public MInfoAduana(MEmpresa empresa, MCfd cfd, String pedimento, Date fecha, String aduana) {
		this.empresa = empresa;
		this.cfd = cfd;
		this.pedimento = pedimento;
		this.fecha = fecha;
		this.aduana = aduana;
	}

 	@Id
 	/*@SequenceGenerator(name = "SInfoAduana", sequenceName = "SECUENCIA_INFO_ADUANA", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SInfoAduana")*/
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

	@Index(name = "INFOAD_PEDIMENTO_IDX") 
    @Column(name="PEDIMENTO", nullable=false, length=300)
	public String getPedimento() {
		return pedimento;
	}

    @Column(name="FECHA", nullable=false)
        @Temporal(javax.persistence.TemporalType.TIMESTAMP)
	public Date getFecha() {
		return fecha;
	}
	
    @Column(name="ADUANA", nullable=true, length=500)
	public String getAduana() {
		return aduana;
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


	public void setPedimento(String pedimento) {
		this.pedimento = pedimento;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public void setAduana(String aduana) {
		this.aduana = aduana;
	}
        
//        public void escapeSqlAndHtmlCharacters(){
//            this.pedimento=(this.pedimento != null) ? StringEscapeUtils.escapeSql(this.pedimento) : "";
//            this.aduana=(this.aduana != null) ? StringEscapeUtils.escapeSql(this.aduana) : "";
//            
//            this.pedimento=(this.pedimento != null) ? StringEscapeUtils.escapeHtml(this.pedimento) : "";
//            this.aduana=(this.aduana != null) ? StringEscapeUtils.escapeHtml(this.aduana) : "";
//        }
}