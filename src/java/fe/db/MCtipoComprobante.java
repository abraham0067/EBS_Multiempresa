package fe.db;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by eflores on 26/04/2017.
 */
@Entity
@Table(name = "C_TIPO_COMPROBANTE")
public class MCtipoComprobante implements Serializable{
    private Integer id;
    private String clave;
    private String descripcion;
    private String valorMinimo;
    private String valoMaximo;

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
    @Column(name = "DESCRIPCION", nullable = true, length = 15)
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Basic
    @Column(name = "VALOR_MINIMO", nullable = true, length = 20)
    public String getValorMinimo() {
        return valorMinimo;
    }

    public void setValorMinimo(String valorMinimo) {
        this.valorMinimo = valorMinimo;
    }

    @Basic
    @Column(name = "VALO_MAXIMO", nullable = true, length = 20)
    public String getValoMaximo() {
        return valoMaximo;
    }

    public void setValoMaximo(String valoMaximo) {
        this.valoMaximo = valoMaximo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MCtipoComprobante that = (MCtipoComprobante) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (clave != null ? !clave.equals(that.clave) : that.clave != null) return false;
        if (descripcion != null ? !descripcion.equals(that.descripcion) : that.descripcion != null) return false;
        if (valorMinimo != null ? !valorMinimo.equals(that.valorMinimo) : that.valorMinimo != null) return false;
        if (valoMaximo != null ? !valoMaximo.equals(that.valoMaximo) : that.valoMaximo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (clave != null ? clave.hashCode() : 0);
        result = 31 * result + (descripcion != null ? descripcion.hashCode() : 0);
        result = 31 * result + (valorMinimo != null ? valorMinimo.hashCode() : 0);
        result = 31 * result + (valoMaximo != null ? valoMaximo.hashCode() : 0);
        return result;
    }
}
