package fe.db;

import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * Created by eflores on 18/09/2017.
 */
@Entity
@Table(name = "M_IMPUESTOS")
public class MImpuestos {
    private Integer id;
    private String claveImpuesto;
    private String descripcionImpuesto;
    private String tipoFactor;
    private boolean aplicableRets;
    private boolean aplicableTras;
    private String porcentajeMinimo;
    private String porcentajeMaximo;
    private Integer tipoEntrada;

    @Id
    @Column(name = "ID", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "CLAVE_IMPUESTO", nullable = true, length = 5)
    public String getClaveImpuesto() {
        return claveImpuesto;
    }

    public void setClaveImpuesto(String claveImpuesto) {
        this.claveImpuesto = claveImpuesto;
    }

    @Basic
    @Column(name = "DESCRIPCION_IMPUESTO", nullable = true, length = 10)
    public String getDescripcionImpuesto() {
        return descripcionImpuesto;
    }

    public void setDescripcionImpuesto(String descripcionImpuesto) {
        this.descripcionImpuesto = descripcionImpuesto;
    }

    @Basic
    @Column(name = "TIPO_FACTOR", nullable = true, length = 10)
    public String getTipoFactor() {
        return tipoFactor;
    }

    public void setTipoFactor(String tipoFactor) {
        this.tipoFactor = tipoFactor;
    }

    @Column(name = "APLICABLE_RETS", nullable = true)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    public boolean getAplicableRets() {
        return aplicableRets;
    }

    public void setAplicableRets(boolean aplicableRets) {
        this.aplicableRets = aplicableRets;
    }


    @Column(name = "APLICABLE_TRAS", nullable = true)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    public boolean getAplicableTras() {
        return aplicableTras;
    }

    public void setAplicableTras(boolean aplicableTras) {
        this.aplicableTras = aplicableTras;
    }

    @Basic
    @Column(name = "PORCENTAJE_MINIMO", nullable = true, length = 15)
    public String getPorcentajeMinimo() {
        return porcentajeMinimo;
    }

    public void setPorcentajeMinimo(String porcentajeMinimo) {
        this.porcentajeMinimo = porcentajeMinimo;
    }

    @Basic
    @Column(name = "PORCENTAJE_MAXIMO", nullable = true, length = 15)
    public String getPorcentajeMaximo() {
        return porcentajeMaximo;
    }

    public void setPorcentajeMaximo(String porcentajeMaximo) {
        this.porcentajeMaximo = porcentajeMaximo;
    }

    @Basic
    @Column(name = "TIPO_ENTRADA", nullable = true)
    public Integer getTipoEntrada() {
        return tipoEntrada;
    }

    public void setTipoEntrada(Integer tipoEntrada) {
        this.tipoEntrada = tipoEntrada;
    }

}
