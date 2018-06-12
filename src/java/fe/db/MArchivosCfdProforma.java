package fe.db;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by eflores on 24/10/2017.
 */
@Entity
@Table(name = "M_ARCHIVOS_CFD_PROFORMA")
public class MArchivosCfdProforma {
    private int id;
    private Timestamp fecha;
    private String nombre;
    private String ruta;
    private String usuario;
    private Integer cfdId;

    @Id
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "FECHA", nullable = false)
    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    @Basic
    @Column(name = "NOMBRE", nullable = false, length = 100)
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Basic
    @Column(name = "RUTA", nullable = false, length = 200)
    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    @Basic
    @Column(name = "USUARIO", nullable = false, length = 20)
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @Basic
    @Column(name = "CFD_ID", nullable = true)
    public Integer getCfdId() {
        return cfdId;
    }

    public void setCfdId(Integer cfdId) {
        this.cfdId = cfdId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MArchivosCfdProforma that = (MArchivosCfdProforma) o;

        if (id != that.id) return false;
        if (fecha != null ? !fecha.equals(that.fecha) : that.fecha != null) return false;
        if (nombre != null ? !nombre.equals(that.nombre) : that.nombre != null) return false;
        if (ruta != null ? !ruta.equals(that.ruta) : that.ruta != null) return false;
        if (usuario != null ? !usuario.equals(that.usuario) : that.usuario != null) return false;
        if (cfdId != null ? !cfdId.equals(that.cfdId) : that.cfdId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (fecha != null ? fecha.hashCode() : 0);
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        result = 31 * result + (ruta != null ? ruta.hashCode() : 0);
        result = 31 * result + (usuario != null ? usuario.hashCode() : 0);
        result = 31 * result + (cfdId != null ? cfdId.hashCode() : 0);
        return result;
    }
}
