package fe.db;

import javax.persistence.*;

/**
 * Created by eflores on 31/08/2017.
 */
@Entity
@Table(name = "M_ADDFIELDS_INVOICE")
public class MAddfieldsInvoice {
    private Integer id;
    private Integer idEmp;
    private String param;
    private String paramdesc;
    private int ordenMuestra;
    private int addEntryNumber;

    public void setId(int id) {
        this.id = id;
    }

    public void setIdEmp(int idEmp) {
        this.idEmp = idEmp;
    }

    @Id
    @Column(name = "ID", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "ID_EMP", nullable = false)
    public Integer getIdEmp() {
        return idEmp;
    }

    public void setIdEmp(Integer idEmp) {
        this.idEmp = idEmp;
    }

    @Basic
    @Column(name = "PARAM", nullable = true, length = 30)
    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MAddfieldsInvoice that = (MAddfieldsInvoice) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (idEmp != null ? !idEmp.equals(that.idEmp) : that.idEmp != null) return false;
        if (param != null ? !param.equals(that.param) : that.param != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (idEmp != null ? idEmp.hashCode() : 0);
        result = 31 * result + (param != null ? param.hashCode() : 0);
        return result;
    }

    @Basic
    @Column(name = "PARAMDESC", nullable = false, length = 50)
    public String getParamdesc() {
        return paramdesc;
    }

    public void setParamdesc(String paramdesc) {
        this.paramdesc = paramdesc;
    }

    @Basic
    @Column(name = "ORDEN_MUESTRA", nullable = false)
    public int getOrdenMuestra() {
        return ordenMuestra;
    }

    public void setOrdenMuestra(int ordenMuestra) {
        this.ordenMuestra = ordenMuestra;
    }

    @Basic
    @Column(name = "ADD_ENTRY_NUMBER", nullable = false)
    public int getAddEntryNumber() {
        return addEntryNumber;
    }

    public void setAddEntryNumber(int addEntryNumber) {
        this.addEntryNumber = addEntryNumber;
    }
}
