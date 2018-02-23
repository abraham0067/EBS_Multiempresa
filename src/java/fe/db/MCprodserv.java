package fe.db;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by eflores on 06/06/2017.
 */
@Entity
@Table(name = "C_PRODSERV")
public class MCprodserv implements Serializable{
    private Integer id;
    private String descripcion;
    private Date inicioVigencia;
    private Date finVigencia;
    private Integer incluirIvaTras;
    private Integer incluirIepsTras;
    private String complementoInc;
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
    @Column(name = "DESCRIPCION", nullable = false, length = 150)
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Basic
    @Column(name = "INICIO_VIGENCIA")
    public Date getInicioVigencia() {
        return inicioVigencia;
    }

    public void setInicioVigencia(Date inicioVigencia) {
        this.inicioVigencia = inicioVigencia;
    }

    @Basic
    @Column(name = "FIN_VIGENCIA")
    public Date getFinVigencia() {
        return finVigencia;
    }

    public void setFinVigencia(Date finVigencia) {
        this.finVigencia = finVigencia;
    }

    @Basic
    @Column(name = "INCLUIR_IVA_TRAS", nullable = false)
    public Integer getIncluirIvaTras() {
        return incluirIvaTras;
    }

    public void setIncluirIvaTras(Integer incluirIvaTras) {
        this.incluirIvaTras = incluirIvaTras;
    }

    @Basic
    @Column(name = "INCLUIR_IEPS_TRAS", nullable = false)
    public Integer getIncluirIepsTras() {
        return incluirIepsTras;
    }

    public void setIncluirIepsTras(Integer incluirIepsTras) {
        this.incluirIepsTras = incluirIepsTras;
    }

    @Basic
    @Column(name = "COMPLEMENTO_INC", nullable = true, length = 50)
    public String getComplementoInc() {
        return complementoInc;
    }

    public void setComplementoInc(String complementoInc) {
        this.complementoInc = complementoInc;
    }

    @Basic
    @Column(name = "CLAVE", nullable = false, length = 10)
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

        MCprodserv that = (MCprodserv) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (descripcion != null ? !descripcion.equals(that.descripcion) : that.descripcion != null) return false;
        if (inicioVigencia != null ? !inicioVigencia.equals(that.inicioVigencia) : that.inicioVigencia != null)
            return false;
        if (finVigencia != null ? !finVigencia.equals(that.finVigencia) : that.finVigencia != null) return false;
        if (incluirIvaTras != null ? !incluirIvaTras.equals(that.incluirIvaTras) : that.incluirIvaTras != null)
            return false;
        if (incluirIepsTras != null ? !incluirIepsTras.equals(that.incluirIepsTras) : that.incluirIepsTras != null)
            return false;
        if (complementoInc != null ? !complementoInc.equals(that.complementoInc) : that.complementoInc != null)
            return false;
        if (clave != null ? !clave.equals(that.clave) : that.clave != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (descripcion != null ? descripcion.hashCode() : 0);
        result = 31 * result + (inicioVigencia != null ? inicioVigencia.hashCode() : 0);
        result = 31 * result + (finVigencia != null ? finVigencia.hashCode() : 0);
        result = 31 * result + (incluirIvaTras != null ? incluirIvaTras.hashCode() : 0);
        result = 31 * result + (incluirIepsTras != null ? incluirIepsTras.hashCode() : 0);
        result = 31 * result + (complementoInc != null ? complementoInc.hashCode() : 0);
        result = 31 * result + (clave != null ? clave.hashCode() : 0);
        return result;
    }
}
