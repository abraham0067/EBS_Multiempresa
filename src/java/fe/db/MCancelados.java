/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.db;

/**
 *
 * @author pedro
 */

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
//import org.apache.commons.lang.StringEscapeUtils;

/**
 *
 * @author pedro
 */
@SuppressWarnings("serial")
// @SuppressWarnings("serial")
@Entity
@Table(name="M_CANCELADO", uniqueConstraints = {@UniqueConstraint(columnNames={"CFD_ID"})})
public class MCancelados  implements Serializable{
    
    @Id
    //@SequenceGenerator(name = "SCancelados", sequenceName = "SECUENCIA_CANCELADOS", initialValue = 1, allocationSize = 1)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SCancelados")
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="ID", nullable=false)
    private Integer id;
    
    @OneToOne
    private MCfd cfd;
    @Lob
    @Column(name="ACUSE")
    private byte[] xmlAcuse = new byte[0];
    
    @Column(name="FECHA_CANCELACION", nullable=false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fecha;
    
    @Column(name="RUTA", length = 250)
    private String ruta;
    
    
    public MCancelados() {}
	
 

    public MCancelados(MCfd cfd, byte[] xmlAcuse, Date fecha, String ruta) {
		super();
		this.cfd = cfd;
		this.xmlAcuse = xmlAcuse;
		this.fecha = fecha;
		this.ruta = ruta;
	}



	/**
     * @return the cfd
     */
    
    public MCfd getCfd() {
        return cfd;
    }

    /**
     * @param cfd the cfd to set
     */
    public void setCfd(MCfd cfd) {
        this.cfd = cfd;
    }

    /**
     * @return the xmlAcuse
     */
    public byte[] getXmlAcuse() {
        return xmlAcuse;
    }

    /**
     * @param xmlAcuse the xmlAcuse to set
     */
    public void setXmlAcuse(byte[] xmlAcuse) {
        this.xmlAcuse = xmlAcuse;
    }

    /**
     * @return the fecha
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

   

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }



	public String getRuta() {
		return ruta;
	}



	public void setRuta(String ruta) {
		this.ruta = ruta;
	}
        
//        public void escapeSqlAndHtmlCharacters(){
//            this.ruta = (this.ruta != null)?StringEscapeUtils.escapeSql(this.ruta):"";
//            this.ruta = (this.ruta != null)?StringEscapeUtils.escapeHtml(this.ruta):"";
//        }
}
