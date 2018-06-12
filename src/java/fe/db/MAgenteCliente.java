package fe.db;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "M_AGENTE_CLIENTE", catalog = "FACCORP_APL")
public class MAgenteCliente implements Serializable {
    /** Identificador Unico AutoIncremental*/
    private Integer id;
    private String agente;
    private MAcceso mAccesoByAccesoAgenteId;
    private String NumeroCliente;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "agente", nullable = false, length = 255)
    public String getAgente() {
        return agente;
    }

    public void setAgente(String agente) {
        this.agente = agente;
    }

    //@Basic
    //@Column(name = "acceso_agente_ID", nullable = false)
    //public int getAccesoAgenteId() {
     //   return accesoAgenteId;
   // }

   // public void setAccesoAgenteId(int accesoAgenteId) {
     ///   this.accesoAgenteId = accesoAgenteId;
    //}

    //@Basic
    //@Column(name = "acceso_cliente_ID", nullable = false)
    //public int getAccesoClienteId() {
     //   return accesoClienteId;
    //}

    //public void setAccesoClienteId(int accesoClienteId) {
    //    this.accesoClienteId = accesoClienteId;
    //}


    @ManyToOne
    @JoinColumn(name = "acceso_agente_ID", referencedColumnName = "ID", nullable = false)
    public MAcceso getmAccesoByAccesoAgenteId() {
        return mAccesoByAccesoAgenteId;
    }

    public void setmAccesoByAccesoAgenteId(MAcceso mAccesoByAccesoAgenteId) {
        this.mAccesoByAccesoAgenteId = mAccesoByAccesoAgenteId;
    }

    @Column(name = "Numero_Cliente", nullable = false, length = 45)
    public String getNumeroCliente() {
        return NumeroCliente;
    }

    public void setNumeroCliente(String numeroCliente) {
        this.NumeroCliente = numeroCliente;
    }
}
