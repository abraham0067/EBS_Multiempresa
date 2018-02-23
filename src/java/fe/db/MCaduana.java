/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.db;
import java.io.Serializable;
import javax.persistence.*;
/**
 *
 * @author Eduardo C. Flores Ambrosio <EB&S>
 */
@Entity
@Table(name = "C_ADUANA")
public class MCaduana implements Serializable{
    private Integer id;
    private String cAduan;
    private String descripcion;

    public MCaduana() {
    }

    public MCaduana(String cAduan, String descripcion) {
        this.cAduan = cAduan;
        this.descripcion = descripcion;
    }

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
    @Column(name = "CLAVE", nullable = false, length = 5)
    public String getcAduan() {
        return cAduan;
    }

    public void setcAduan(String cAduan) {
        this.cAduan = cAduan;
    }

    @Basic
    @Column(name = "DESCRIPCION", nullable = false, length = 70)
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MCaduana that = (MCaduana) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (cAduan != null ? !cAduan.equals(that.cAduan) : that.cAduan != null) return false;
        if (descripcion != null ? !descripcion.equals(that.descripcion) : that.descripcion != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (cAduan != null ? cAduan.hashCode() : 0);
        result = 31 * result + (descripcion != null ? descripcion.hashCode() : 0);
        return result;
    }
}
