package fe.db;

/**
 *
 * @Author Carlo Garcia Sanchez
 * @Date 04-10-2012
 *
 * Tabla de INVOICE_XML para almacenar
 * los XML y PDF
 *
 */

import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.io.Serializable;

//import org.apache.commons.lang.StringEscapeUtils;

@SuppressWarnings("serial")
@Entity
@Table(name="M_CFD_XML") 
public class MCfdXml implements Serializable {

	private int id = 0;
	private MCfd cfd;
	private String uuid = "";
	private byte[] xml = new byte[0];
	private byte[] xmlP = new byte[0];
	
	protected MCfdXml() {}
	
	public MCfdXml(MCfd cfd, byte[] xml, byte[] xmlP) {
		this.cfd = cfd;
		this.xml = xml;
		this.xmlP = xmlP;
	}

 	

	@Id
 /*	@SequenceGenerator(name = "SCfdXml", sequenceName = "SECUENCIA_CFD_XML", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SCfdXml")*/
 	@GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="ID", nullable=false)
	public int getId() {
		return id;
	}

    @OneToOne
    public MCfd getCfd() {
            return cfd;
    }

	@Index(name = "CFDXML_UUID_IDX") 
    @Column(name="UUID", nullable=true, length=70)
	public String getUuid() {
		return uuid;
	}

	@Lob
    @Column(name="XML", nullable=false)
	public byte[] getXml() {
		return xml;
	}
	
	@Lob
    @Column(name="XMLP", nullable=false)
	public byte[] getXmlP() {
		return xmlP;
	}

	public void setId(int id) {
		this.id = id; 
	}

	public void setCfd(MCfd cfd) {
		this.cfd = cfd;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid; 
	}

//	public void setEmpresa(MEmpresa empresa) {
//		this.empresa = empresa; 
//	}


	public void setXml(byte[] xml) {
		this.xml = xml;
	}

	public void setXmlP(byte[] xmlP) {
		this.xmlP = xmlP;
	}
        
//        public void escapeSqlAndHtmlCharacters(){
//            this.uuid = (this.uuid != null) ? StringEscapeUtils.escapeSql(this.uuid) : "";
//            this.uuid = (this.uuid != null) ? StringEscapeUtils.escapeHtml(this.uuid) : "";
//        }
}