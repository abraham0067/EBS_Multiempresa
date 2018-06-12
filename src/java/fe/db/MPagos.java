package fe.db;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by eflores on 02/10/2017.
 */
@Entity
@Table(name = "M_PAGOS_DETALLE")
public class MPagos {
    private Integer id;
    private Integer cfdiId;
    private Integer empId;
    private String uuid;
    private Integer numParcialidad;
    private String moneda;
    private Double impSaldoAnterior;
    @Column(name = "PAGADO", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean pagado;
    private Date fecha;

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
    @Column(name = "EMP_ID", nullable = false)
    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    @Basic
    @Column(name = "CFDI_ID", nullable = false)
    public Integer getCfdiId() {
        return cfdiId;
    }

    public void setCfdiId(Integer cfdiId) {
        this.cfdiId = cfdiId;
    }

    @Basic
    @Column(name = "UUID", nullable = false, length = 68)
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Basic
    @Column(name = "MONEDA", nullable = false, length = 3)
    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    @Basic
    @Column(name = "NUM_PARCIALIDAD", nullable = true)
    public Integer getNumParcialidad() {
        return numParcialidad;
    }

    public void setNumParcialidad(Integer numParcialidad) {
        this.numParcialidad = numParcialidad;
    }

    @Basic
    @Column(name = "IMP_SALDO_ANTERIOR", nullable = true, precision = 6)
    public Double getImpSaldoAnterior() {
        return impSaldoAnterior;
    }

    public void setImpSaldoAnterior(Double impSaldoAnterior) {
        this.impSaldoAnterior = impSaldoAnterior;
    }



    public boolean isPagado() {
        return pagado;
    }

    public void setPagado(boolean pagado) {
        this.pagado = pagado;
    }

    @Column(name = "FECHA", nullable = true)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getFecha() {
        return (this.fecha);
    }

    public void setFecha(Date fecha){
        this.fecha = fecha;
    }

}
