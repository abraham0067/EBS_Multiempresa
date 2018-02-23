package fe.db;

import java.io.*;
import javax.persistence.*;
//import org.apache.commons.lang.StringEscapeUtils;

@Entity
@Table(name = "M_CFD_XML_RETENCION", catalog = "FACCORP_APL")
public class MCfdXmlRetencion implements Serializable
{
    private Integer id;
    private McfdRetencion mcfdRetencion;
    private String uuid;
    private byte[] xml;
    private byte[] xmlp;
    
    public MCfdXmlRetencion() {
    }
    
    public MCfdXmlRetencion(final byte[] xml, final byte[] xmlp) {
        this.xml = xml;
        this.xmlp = xmlp;
    }
    
    public MCfdXmlRetencion(final McfdRetencion mcfdRetencion, final String uuid, final byte[] xml, final byte[] xmlp) {
        this.mcfdRetencion = mcfdRetencion;
        this.uuid = uuid;
        this.xml = xml;
        this.xmlp = xmlp;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RETENCION_ID")
    public McfdRetencion getMcfdRetencion() {
        return this.mcfdRetencion;
    }
    
    public void setMcfdRetencion(final McfdRetencion mcfdRetencion) {
        this.mcfdRetencion = mcfdRetencion;
    }
    
    @Column(name = "UUID", length = 100)
    public String getUuid() {
        return this.uuid;
    }
    
    public void setUuid(final String uuid) {
        this.uuid = uuid;
    }
    
    @Column(name = "XML", nullable = false)
    public byte[] getXml() {
        return this.xml;
    }
    
    public void setXml(final byte[] xml) {
        this.xml = xml;
    }
    
    @Column(name = "XMLP", nullable = false)
    public byte[] getXmlp() {
        return this.xmlp;
    }
    
    public void setXmlp(final byte[] xmlp) {
        this.xmlp = xmlp;
    }
    
//    public void escapeSqlAndHtmlCharacters() {
//        this.uuid = (this.uuid != null) ? StringEscapeUtils.escapeSql(this.uuid) : "";
//        this.uuid = (this.uuid != null) ? StringEscapeUtils.escapeHtml(this.uuid) : "";
//    }
}
