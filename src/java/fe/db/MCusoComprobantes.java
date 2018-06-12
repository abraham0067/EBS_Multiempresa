package fe.db;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by eflores on 26/04/2017.
 */
@Entity
@Table(name = "C_USO_COMPROBANTES")
public class MCusoComprobantes implements Serializable {
    private Integer id;
    private String clave;
    private String descripcion;
    private Integer fisica;
    private Integer moral;
    private Date inicioVigencia;
    private Date finVigencia;

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
    @Column(name = "DESCRIPCION", nullable = true, length = 75)
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Basic
    @Column(name = "FISICA", nullable = true)
    public Integer getFisica() {
        return fisica;
    }

    public void setFisica(Integer fisica) {
        this.fisica = fisica;
    }

    @Basic
    @Column(name = "MORAL", nullable = true)
    public Integer getMoral() {
        return moral;
    }

    public void setMoral(Integer moral) {
        this.moral = moral;
    }

    @Basic
    @Column(name = "INICIO_VIGENCIA", nullable = true)
    public Date getInicioVigencia() {
        return inicioVigencia;
    }

    public void setInicioVigencia(Date inicioVigencia) {
        this.inicioVigencia = inicioVigencia;
    }

    @Basic
    @Column(name = "FIN_VIGENCIA", nullable = true)
    public Date getFinVigencia() {
        return finVigencia;
    }

    public void setFinVigencia(Date finVigencia) {
        this.finVigencia = finVigencia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MCusoComprobantes that = (MCusoComprobantes) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (clave != null ? !clave.equals(that.clave) : that.clave != null) return false;
        if (descripcion != null ? !descripcion.equals(that.descripcion) : that.descripcion != null) return false;
        if (fisica != null ? !fisica.equals(that.fisica) : that.fisica != null) return false;
        if (moral != null ? !moral.equals(that.moral) : that.moral != null) return false;
        if (inicioVigencia != null ? !inicioVigencia.equals(that.inicioVigencia) : that.inicioVigencia != null)
            return false;
        if (finVigencia != null ? !finVigencia.equals(that.finVigencia) : that.finVigencia != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (clave != null ? clave.hashCode() : 0);
        result = 31 * result + (descripcion != null ? descripcion.hashCode() : 0);
        result = 31 * result + (fisica != null ? fisica.hashCode() : 0);
        result = 31 * result + (moral != null ? moral.hashCode() : 0);
        result = 31 * result + (inicioVigencia != null ? inicioVigencia.hashCode() : 0);
        result = 31 * result + (finVigencia != null ? finVigencia.hashCode() : 0);
        return result;
    }
}
