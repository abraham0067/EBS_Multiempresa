package fe.db;

import java.io.*;
import javax.persistence.*;

@Entity
@Table(name = "M_EMPRESA_SERVICIO", catalog = "FACCORP_APL")
public class MEmpresaServicio implements Serializable
{
    private Integer id;
    private MEmpresa MEmpresa;
    private MServicio MServicio;
    
    public MEmpresaServicio() {
    }
    
    public MEmpresaServicio(final MEmpresa MEmpresa, final MServicio MServicio) {
        this.MEmpresa = MEmpresa;
        this.MServicio = MServicio;
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
    @JoinColumn(name = "empresa_ID", nullable = false)
    public MEmpresa getMEmpresa() {
        return this.MEmpresa;
    }
    
    public void setMEmpresa(final MEmpresa MEmpresa) {
        this.MEmpresa = MEmpresa;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servicio_ID", nullable = false)
    public MServicio getMServicio() {
        return this.MServicio;
    }
    
    public void setMServicio(final MServicio MServicio) {
        this.MServicio = MServicio;
    }
}
