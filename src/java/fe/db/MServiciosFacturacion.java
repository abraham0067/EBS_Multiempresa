package fe.db;
// Generated 6/04/2018 03:52:38 PM by Hibernate Tools 4.3.1


import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * MServiciosFacturacion generated by hbm2java
 */
@Entity
@Table(name="M_SERVICIOS_FACTURACION"
    ,catalog="FACCORP_APL"
)
public class MServiciosFacturacion  implements java.io.Serializable {


     private Integer id;
     private CServiciosFacturacion CServiciosFacturacion;
     private int idEmpresa;

    public MServiciosFacturacion() {
    }

    public MServiciosFacturacion(CServiciosFacturacion CServiciosFacturacion, int idEmpresa) {
       this.CServiciosFacturacion = CServiciosFacturacion;
       this.idEmpresa = idEmpresa;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="id", unique=true, nullable=false)
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="servicio", nullable=false)
    public CServiciosFacturacion getCServiciosFacturacion() {
        return this.CServiciosFacturacion;
    }
    
    public void setCServiciosFacturacion(CServiciosFacturacion CServiciosFacturacion) {
        this.CServiciosFacturacion = CServiciosFacturacion;
    }

    
    @Column(name="id_empresa", nullable=false)
    public int getIdEmpresa() {
        return this.idEmpresa;
    }
    
    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }


}


