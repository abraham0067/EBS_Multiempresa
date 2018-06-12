/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.db;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
/**
 * Created by eflores on 24/04/2017.
 */
@Entity
@Table(name = "C_PATENTES_ADUANALES")
public class MCpatentesAduanales implements Serializable{
    private Integer id;
    private String patente;
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
    @Column(name = "PATENTE", nullable = false, length = 5)
    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
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

        MCpatentesAduanales that = (MCpatentesAduanales) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (patente != null ? !patente.equals(that.patente) : that.patente != null) return false;
        if (inicioVigencia != null ? !inicioVigencia.equals(that.inicioVigencia) : that.inicioVigencia != null)
            return false;
        if (finVigencia != null ? !finVigencia.equals(that.finVigencia) : that.finVigencia != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (patente != null ? patente.hashCode() : 0);
        result = 31 * result + (inicioVigencia != null ? inicioVigencia.hashCode() : 0);
        result = 31 * result + (finVigencia != null ? finVigencia.hashCode() : 0);
        return result;
    }
}

