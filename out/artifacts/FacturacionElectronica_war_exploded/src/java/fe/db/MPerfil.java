package fe.db;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
//import org.apache.commons.lang.StringEscapeUtils;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Index;

@Entity
@Table(name = "M_PERFIL")
@BatchSize(size = 10)
public class MPerfil implements Serializable {

    private static final long serialVersionUID = 1L;
    // Identificador nico AutoIncremental
    private Integer id;

    // Tiempo de sesion antes de marcar timeout
    private Integer timeOut = -1;

    /// Bits que identifican el nivel de acceso
    private Long perfil = 1l;

    // Nombre del perfil definido
    private String tipoUser = "ADMINISTRADOR";


    private MEmpresa empresa;


    public MPerfil() {
        super();
    }


    public MPerfil(Integer timeOut, Long perfil, String tipoUser, MEmpresa empresa) {
        super();
        this.timeOut = timeOut;
        this.perfil = perfil;
        this.tipoUser = tipoUser;
        this.empresa = empresa;
    }

    @Index(name = "PERFIL_PERFIL_IDX")
    @Column(name = "PERFIL")
    public Long getPerfil() {
        return perfil;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    public Integer getId() {
        return id;
    }

    @Column(name = "TIPO_USER", nullable = false, length = 50)
    public String getTipoUser() {
        return tipoUser;
    }

    @Column(name = "TIME_OUT")
    public Integer getTimeOut() {
        return timeOut;
    }


    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    public MEmpresa getEmpresa() {
        return empresa;
    }

    public void setPerfil(Long perfil) {
        this.perfil = perfil;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTipoUser(String tipoUser) {
        this.tipoUser = tipoUser;
    }

    public void setTimeOut(Integer timeOut) {
        this.timeOut = timeOut;
    }

    public void setEmpresa(MEmpresa empresa) {
        this.empresa = empresa;
    }

}