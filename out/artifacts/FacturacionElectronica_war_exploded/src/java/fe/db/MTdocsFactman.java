package fe.db;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by eflores on 06/09/2017.
 */
@Entity
@Table(name = "M_TDOCS_FACTMAN")
public class MTdocsFactman implements Serializable{
    private Integer id;
    private String tipodoc;
    private MCtipoComprobante cTipoComprobanteByTdocId;

    @Id
    @Column(name = "ID", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "TIPODOC", nullable = false, length = 25)
    public String getTipodoc() {
        return tipodoc;
    }

    public void setTipodoc(String tipodoc) {
        this.tipodoc = tipodoc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MTdocsFactman that = (MTdocsFactman) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (tipodoc != null ? !tipodoc.equals(that.tipodoc) : that.tipodoc != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (tipodoc != null ? tipodoc.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "TDOC_ID", referencedColumnName = "ID", nullable = false)
    public MCtipoComprobante getcTipoComprobanteByTdocId() {
        return cTipoComprobanteByTdocId;
    }

    public void setcTipoComprobanteByTdocId(MCtipoComprobante cTipoComprobanteByTdocId) {
        this.cTipoComprobanteByTdocId = cTipoComprobanteByTdocId;
    }
}
