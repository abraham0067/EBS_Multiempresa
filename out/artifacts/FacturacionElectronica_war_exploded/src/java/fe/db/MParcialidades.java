package fe.db;

import java.io.*;
import javax.persistence.*;

@Entity
@Table(name = "M_PARCIALIDADES", catalog = "FACCORP_APL")
public class MParcialidades implements Serializable
{
    private Integer id;
    private MCfd MCfd;
    private int estatus;
    private int parcialidadId;
    private int numParcialidad;
    private int totalParcialidades;
    private double total;
    private String idsFactura;
    private int numConteo;
    private int numParcialidades;
    private double subtotal;
    
    public MParcialidades() {
    }
    
    public MParcialidades(final int estatus, final int parcialidadId, final int numParcialidad, final int totalParcialidades, final double total, final int numConteo, final int numParcialidades, final double subtotal) {
        this.estatus = estatus;
        this.parcialidadId = parcialidadId;
        this.numParcialidad = numParcialidad;
        this.totalParcialidades = totalParcialidades;
        this.total = total;
        this.numConteo = numConteo;
        this.numParcialidades = numParcialidades;
        this.subtotal = subtotal;
    }
    
    public MParcialidades(final MCfd MCfd, final int estatus, final int parcialidadId, final int numParcialidad, final int totalParcialidades, final double total, final String idsFactura, final int numConteo, final int numParcialidades, final double subtotal) {
        this.MCfd = MCfd;
        this.estatus = estatus;
        this.parcialidadId = parcialidadId;
        this.numParcialidad = numParcialidad;
        this.totalParcialidades = totalParcialidades;
        this.total = total;
        this.idsFactura = idsFactura;
        this.numConteo = numConteo;
        this.numParcialidades = numParcialidades;
        this.subtotal = subtotal;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cfd_ID")
    public MCfd getMCfd() {
        return this.MCfd;
    }
    
    public void setMCfd(final MCfd MCfd) {
        this.MCfd = MCfd;
    }
    
    @Column(name = "ESTATUS", nullable = false)
    public int getEstatus() {
        return this.estatus;
    }
    
    public void setEstatus(final int estatus) {
        this.estatus = estatus;
    }
    
    @Column(name = "PARCIALIDAD_ID", nullable = false)
    public int getParcialidadId() {
        return this.parcialidadId;
    }
    
    public void setParcialidadId(final int parcialidadId) {
        this.parcialidadId = parcialidadId;
    }
    
    @Column(name = "NUM_PARCIALIDAD", nullable = false)
    public int getNumParcialidad() {
        return this.numParcialidad;
    }
    
    public void setNumParcialidad(final int numParcialidad) {
        this.numParcialidad = numParcialidad;
    }
    
    @Column(name = "TOTAL_PARCIALIDADES", nullable = false)
    public int getTotalParcialidades() {
        return this.totalParcialidades;
    }
    
    public void setTotalParcialidades(final int totalParcialidades) {
        this.totalParcialidades = totalParcialidades;
    }
    
    @Column(name = "TOTAL", nullable = false, precision = 13, scale = 2)
    public double getTotal() {
        return this.total;
    }
    
    public void setTotal(final double total) {
        this.total = total;
    }
    
    @Column(name = "IDS_FACTURA")
    public String getIdsFactura() {
        return this.idsFactura;
    }
    
    public void setIdsFactura(final String idsFactura) {
        this.idsFactura = idsFactura;
    }
    
    @Column(name = "NUM_CONTEO", nullable = false)
    public int getNumConteo() {
        return this.numConteo;
    }
    
    public void setNumConteo(final int numConteo) {
        this.numConteo = numConteo;
    }
    
    @Column(name = "NUM_PARCIALIDADES", nullable = false)
    public int getNumParcialidades() {
        return this.numParcialidades;
    }
    
    public void setNumParcialidades(final int numParcialidades) {
        this.numParcialidades = numParcialidades;
    }
    
    @Column(name = "SUBTOTAL", nullable = false, precision = 22, scale = 0)
    public double getSubtotal() {
        return this.subtotal;
    }
    
    public void setSubtotal(final double subtotal) {
        this.subtotal = subtotal;
    }
}
