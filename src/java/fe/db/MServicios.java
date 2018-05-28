package fe.db;

import java.io.*;
import javax.persistence.*;


@Entity
@Table(name = "M_SERVICIOS", catalog = "FACCORP_APL")
public class MServicios implements Serializable {
    private int id;
    private Integer cfdiId;
    private Integer envioCorreo;
    private Integer servicio_empresa;
    private MEmpresa mEmpresaByEmpresaId;

    @Id
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "CFDI_ID", nullable = true)
    public Integer getCfdiId() {
        return cfdiId;
    }

    public void setCfdiId(Integer cfdiId) {
        this.cfdiId = cfdiId;
    }

    @Basic
    @Column(name = "ENVIO_CORREO", nullable = true)
    public Integer getEnvioCorreo() {
        return envioCorreo;
    }

    public void setEnvioCorreo(Integer envioCorreo) {
        this.envioCorreo = envioCorreo;
    }

    @Basic
    @Column(name = "servicio_empresa", nullable = true)
    public Integer getServicio_empresa() {
        return servicio_empresa;
    }

    public void setServicio_empresa(Integer servicio_empresa) {
        this.servicio_empresa = servicio_empresa;
    }


    @ManyToOne
    @JoinColumn(name = "empresa_ID", referencedColumnName = "ID", nullable = false)
    public MEmpresa getmEmpresaByEmpresaId() {
        return mEmpresaByEmpresaId;
    }

    public void setmEmpresaByEmpresaId(MEmpresa mEmpresaByEmpresaId) {
        this.mEmpresaByEmpresaId = mEmpresaByEmpresaId;
    }
}