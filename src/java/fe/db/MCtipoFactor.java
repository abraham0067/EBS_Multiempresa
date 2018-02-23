package fe.db;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Created by eflores on 26/04/2017.
 */
@Entity
@Table(name = "C_TIPO_FACTOR")
public class MCtipoFactor implements Serializable{
    private Integer id;
    private String clave;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "CLAVE", nullable = true, length = 20)
    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MCtipoFactor that = (MCtipoFactor) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (clave != null ? !clave.equals(that.clave) : that.clave != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (clave != null ? clave.hashCode() : 0);
        return result;
    }
}
