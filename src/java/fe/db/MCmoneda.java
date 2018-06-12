/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.db;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by eflores on 25/04/2017.
 */
@Entity
@Table(name = "C_MONEDA")
public class MCmoneda implements Serializable{
    private Integer id;
    private String clave;
    private String descripcion;
    private Integer decimales;
    private double porcentajeVariacion;
    private double tipoCambio;

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
    @Column(name = "CLAVE", nullable = true, length = 5)
    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    @Basic
    @Column(name = "DESCRIPCION", nullable = true, length = 100)
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Basic
    @Column(name = "DECIMALES", nullable = true)
    public Integer getDecimales() {
        return decimales;
    }

    public void setDecimales(Integer decimales) {
        this.decimales = decimales;
    }

    @Basic
    @Column(name = "PORCENTAJE_VARIACION", nullable = true)
    public double getPorcentajeVariacion() {
        return porcentajeVariacion;
    }

    public void setPorcentajeVariacion(double porcentajeVariacion) {
        this.porcentajeVariacion = porcentajeVariacion;
    }

    @Basic
    @Column(name = "TIPO_CAMBIO", nullable = true)
    public double getTipoCambio() {
        return tipoCambio;
    }

    public void setTipoCambio(double tipoCambio) {
        this.tipoCambio = tipoCambio;
    }
}
