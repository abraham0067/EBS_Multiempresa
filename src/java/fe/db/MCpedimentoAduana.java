/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.db;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Created by eflores on 24/04/2017.
 */
@Entity
@Table(name = "C_PEDIMENTO_ADUANA")
public class MCpedimentoAduana implements Serializable{
    private Integer id;
    private String aduana;
    private String patente;
    private String ejercicio;
    private String cantidad;

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
    @Column(name = "ADUANA", nullable = false, length = 3)
    public String getAduana() {
        return aduana;
    }

    public void setAduana(String aduana) {
        this.aduana = aduana;
    }

    @Basic
    @Column(name = "PATENTE", nullable = false, length = 5)
    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    @Basic
    @Column(name = "EJERCICIO", nullable = false, length = 4)
    public String getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(String ejercicio) {
        this.ejercicio = ejercicio;
    }

    @Basic
    @Column(name = "CANTIDAD", nullable = false, length = 10)
    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MCpedimentoAduana that = (MCpedimentoAduana) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (aduana != null ? !aduana.equals(that.aduana) : that.aduana != null) return false;
        if (patente != null ? !patente.equals(that.patente) : that.patente != null) return false;
        if (ejercicio != null ? !ejercicio.equals(that.ejercicio) : that.ejercicio != null) return false;
        if (cantidad != null ? !cantidad.equals(that.cantidad) : that.cantidad != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (aduana != null ? aduana.hashCode() : 0);
        result = 31 * result + (patente != null ? patente.hashCode() : 0);
        result = 31 * result + (ejercicio != null ? ejercicio.hashCode() : 0);
        result = 31 * result + (cantidad != null ? cantidad.hashCode() : 0);
        return result;
    }
}
