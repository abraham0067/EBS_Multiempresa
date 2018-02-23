package fe.db;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by eflores on 06/06/2017.
 */
@Entity
@Table(name = "C_CUOTAS_IMPUESTOS")
public class MCcuotasImpuestos implements Serializable {
    private Integer id;
    private String tipo;
    private String valorMinimo;
    private String valorMaximo;
    private String impuesto;
    private String factor;
    private Integer traslado;
    private Integer retencion;

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
    @Column(name = "TIPO", nullable = true, length = 11)
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Basic
    @Column(name = "VALOR_MINIMO", nullable = true, length = 15)
    public String getValorMinimo() {
        return valorMinimo;
    }

    public void setValorMinimo(String valorMinimo) {
        this.valorMinimo = valorMinimo;
    }

    @Basic
    @Column(name = "VALOR_MAXIMO", nullable = true, length = 15)
    public String getValorMaximo() {
        return valorMaximo;
    }

    public void setValorMaximo(String valorMaximo) {
        this.valorMaximo = valorMaximo;
    }

    @Basic
    @Column(name = "IMPUESTO", nullable = true, length = 10)
    public String getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(String impuesto) {
        this.impuesto = impuesto;
    }

    @Basic
    @Column(name = "FACTOR", nullable = true, length = 10)
    public String getFactor() {
        return factor;
    }

    public void setFactor(String factor) {
        this.factor = factor;
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
    @Column(name = "RETENCION", nullable = true)
    public Integer getRetencion() {
        return retencion;
    }

    public void setRetencion(Integer retencion) {
        this.retencion = retencion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MCcuotasImpuestos that = (MCcuotasImpuestos) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (tipo != null ? !tipo.equals(that.tipo) : that.tipo != null) return false;
        if (valorMinimo != null ? !valorMinimo.equals(that.valorMinimo) : that.valorMinimo != null) return false;
        if (valorMaximo != null ? !valorMaximo.equals(that.valorMaximo) : that.valorMaximo != null) return false;
        if (impuesto != null ? !impuesto.equals(that.impuesto) : that.impuesto != null) return false;
        if (factor != null ? !factor.equals(that.factor) : that.factor != null) return false;
        if (traslado != null ? !traslado.equals(that.traslado) : that.traslado != null) return false;
        if (retencion != null ? !retencion.equals(that.retencion) : that.retencion != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (tipo != null ? tipo.hashCode() : 0);
        result = 31 * result + (valorMinimo != null ? valorMinimo.hashCode() : 0);
        result = 31 * result + (valorMaximo != null ? valorMaximo.hashCode() : 0);
        result = 31 * result + (impuesto != null ? impuesto.hashCode() : 0);
        result = 31 * result + (factor != null ? factor.hashCode() : 0);
        result = 31 * result + (traslado != null ? traslado.hashCode() : 0);
        result = 31 * result + (retencion != null ? retencion.hashCode() : 0);
        return result;
    }
}
