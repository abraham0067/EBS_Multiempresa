/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.db;

import javax.persistence.*;
import java.io.Serializable;
/**
 *
 * @author Eduardo C. Flores Ambrosio <EB&S>
 */
@Entity
@Table(name = "C_PAIS")
public class MCpais implements Serializable{
    private Integer id;
    private String clave;
    private String descripcion;
    private String formatoCp;
    private String formatoRegIt;
    private String agrupaciones;

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
    @Column(name = "DESCRIPCION", nullable = false, length = 50)
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Basic
    @Column(name = "FORMATO_CP", nullable = true, length = 50)
    public String getFormatoCp() {
        return formatoCp;
    }

    public void setFormatoCp(String formatoCp) {
        this.formatoCp = formatoCp;
    }

    @Basic
    @Column(name = "FORMATO_REG_IT", nullable = true, length = 100)
    public String getFormatoRegIt() {
        return formatoRegIt;
    }

    public void setFormatoRegIt(String formatoRegIt) {
        this.formatoRegIt = formatoRegIt;
    }

    @Basic
    @Column(name = "AGRUPACIONES", nullable = true, length = 25)
    public String getAgrupaciones() {
        return agrupaciones;
    }

    public void setAgrupaciones(String agrupaciones) {
        this.agrupaciones = agrupaciones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MCpais mCpaises = (MCpais) o;

        if (id != null ? !id.equals(mCpaises.id) : mCpaises.id != null) return false;
        if (clave != null ? !clave.equals(mCpaises.clave) : mCpaises.clave != null) return false;
        if (descripcion != null ? !descripcion.equals(mCpaises.descripcion) : mCpaises.descripcion != null)
            return false;
        if (formatoCp != null ? !formatoCp.equals(mCpaises.formatoCp) : mCpaises.formatoCp != null) return false;
        if (formatoRegIt != null ? !formatoRegIt.equals(mCpaises.formatoRegIt) : mCpaises.formatoRegIt != null)
            return false;
        if (agrupaciones != null ? !agrupaciones.equals(mCpaises.agrupaciones) : mCpaises.agrupaciones != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (clave != null ? clave.hashCode() : 0);
        result = 31 * result + (descripcion != null ? descripcion.hashCode() : 0);
        result = 31 * result + (formatoCp != null ? formatoCp.hashCode() : 0);
        result = 31 * result + (formatoRegIt != null ? formatoRegIt.hashCode() : 0);
        result = 31 * result + (agrupaciones != null ? agrupaciones.hashCode() : 0);
        return result;
    }
}