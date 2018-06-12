/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.db;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by eflores on 24/04/2017.
 */
@Entity
@Table(name = "C_CODIGOPOSTAL")
public class MCcodigopostal implements Serializable{
    private Integer id;
    private String codigoPostal;
    private String estado;
    private String municipio;
    private String localidad;

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
    @Column(name = "CODIGO_POSTAL", nullable = false, length = 8)
    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    @Basic
    @Column(name = "ESTADO", nullable = false, length = 5)
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Basic
    @Column(name = "MUNICIPIO", nullable = true, length = 5)
    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    @Basic
    @Column(name = "LOCALIDAD", nullable = true, length = 5)
    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MCcodigopostal that = (MCcodigopostal) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (codigoPostal != null ? !codigoPostal.equals(that.codigoPostal) : that.codigoPostal != null) return false;
        if (estado != null ? !estado.equals(that.estado) : that.estado != null) return false;
        if (municipio != null ? !municipio.equals(that.municipio) : that.municipio != null) return false;
        if (localidad != null ? !localidad.equals(that.localidad) : that.localidad != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (codigoPostal != null ? codigoPostal.hashCode() : 0);
        result = 31 * result + (estado != null ? estado.hashCode() : 0);
        result = 31 * result + (municipio != null ? municipio.hashCode() : 0);
        result = 31 * result + (localidad != null ? localidad.hashCode() : 0);
        return result;
    }
}
