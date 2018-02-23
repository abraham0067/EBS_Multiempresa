package fe.db;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Created by eflores on 26/04/2017.
 */
@Entity
@Table(name = "C_IMPUESTO")
public class MCimpuesto implements Serializable{
    private Integer id;
    private String clave;
    private String descripcion;
    private Integer retencion;
    private Integer traslado;
    private String locFed;
    private String entidad;

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
    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    @Basic
    @Column(name = "DESCRIPCION", nullable = false, length = 10)
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Basic
    @Column(name = "RETENCION", nullable = true)
    public Integer getRetencion() {
        return retencion;
    }

    public void setRetencion(Integer retencion) {
        this.retencion = retencion;
    }

    @Basic
    @Column(name = "TRASLADO", nullable = true)
    public Integer getTraslado() {
        return traslado;
    }

    public void setTraslado(Integer traslado) {
        this.traslado = traslado;
    }

    @Basic
    @Column(name = "LOC_FED", nullable = true)
    public String getLocFed() {
        return locFed;
    }

    public void setLocFed(String locFed) {
        this.locFed = locFed;
    }

    @Basic
    @Column(name = "ENTIDAD", nullable = true, length = 100)
    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MCimpuesto that = (MCimpuesto) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (clave != null ? !clave.equals(that.clave) : that.clave != null) return false;
        if (descripcion != null ? !descripcion.equals(that.descripcion) : that.descripcion != null) return false;
        if (retencion != null ? !retencion.equals(that.retencion) : that.retencion != null) return false;
        if (traslado != null ? !traslado.equals(that.traslado) : that.traslado != null) return false;
        if (locFed != null ? !locFed.equals(that.locFed) : that.locFed != null) return false;
        if (entidad != null ? !entidad.equals(that.entidad) : that.entidad != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (clave != null ? clave.hashCode() : 0);
        result = 31 * result + (descripcion != null ? descripcion.hashCode() : 0);
        result = 31 * result + (retencion != null ? retencion.hashCode() : 0);
        result = 31 * result + (traslado != null ? traslado.hashCode() : 0);
        result = 31 * result + (locFed != null ? locFed.hashCode() : 0);
        result = 31 * result + (entidad != null ? entidad.hashCode() : 0);
        return result;
    }

}
