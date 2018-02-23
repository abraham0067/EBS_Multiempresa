package fe.db;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Arrays;

/**
 * Created by eflores on 19/10/2017.
 */
@Entity
@Table(name = "M_CFD_XML_PROFORMA")
public class MCfdXmlProforma {
    private int id;
    private String uuid;
    private byte[] xml;
    private byte[] xmlp;
    private Integer cfdId;

    @Id
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "UUID", nullable = true, length = 100)
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Basic
    @Column(name = "XML", nullable = false)
    public byte[] getXml() {
        return xml;
    }

    public void setXml(byte[] xml) {
        this.xml = xml;
    }

    @Basic
    @Column(name = "XMLP", nullable = false)
    public byte[] getXmlp() {
        return xmlp;
    }

    public void setXmlp(byte[] xmlp) {
        this.xmlp = xmlp;
    }

    @Basic
    @Column(name = "CFD_ID", nullable = true)
    public Integer getCfdId() {
        return cfdId;
    }

    public void setCfdId(Integer cfdId) {
        this.cfdId = cfdId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MCfdXmlProforma that = (MCfdXmlProforma) o;

        if (id != that.id) return false;
        if (uuid != null ? !uuid.equals(that.uuid) : that.uuid != null) return false;
        if (!Arrays.equals(xml, that.xml)) return false;
        if (!Arrays.equals(xmlp, that.xmlp)) return false;
        if (cfdId != null ? !cfdId.equals(that.cfdId) : that.cfdId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(xml);
        result = 31 * result + Arrays.hashCode(xmlp);
        result = 31 * result + (cfdId != null ? cfdId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MCfdXmlProforma{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", cfdId=" + cfdId +
                '}';
    }
}
