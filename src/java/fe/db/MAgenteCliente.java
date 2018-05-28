package fe.db;

import java.io.*;
import javax.persistence.*;


@Entity
@Table(name = "M_AGENTE_CLIENTE", catalog = "FACCORP_APL")
public class MAgenteCliente implements Serializable {
    private int id;
    private String agente;
    private int accesoAgenteId;
    private int accesoClienteId;
    private MAcceso mAccesoByAccesoAgenteId;
    private MAcceso mAccesoByAccesoClienteId;

    @Id
    @Column(name = "ID", nullable = true, insertable = "false", updatable = "false")
    public int getId() {
        return id;
    }

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

    @Basic
    @Column(name = "acceso_agente_ID", nullable = false)
    public int getAccesoAgenteId() {
        return accesoAgenteId;
    }

    public void setAccesoAgenteId(int accesoAgenteId) {
        this.accesoAgenteId = accesoAgenteId;
    }

    @Basic
    @Column(name = "acceso_cliente_ID", nullable = false)
    public int getAccesoClienteId() {
        return accesoClienteId;
    }

    public void setAccesoClienteId(int accesoClienteId) {
        this.accesoClienteId = accesoClienteId;
    }

    @ManyToOne
    @JoinColumn(name = "acceso_agente_ID", referencedColumnName = "ID", nullable = false)
    public MAcceso getmAccesoByAccesoAgenteId() {
        return mAccesoByAccesoAgenteId;
    }

    public void setmAccesoByAccesoAgenteId(MAcceso mAccesoByAccesoAgenteId) {
        this.mAccesoByAccesoAgenteId = mAccesoByAccesoAgenteId;
    }

    @ManyToOne
    @JoinColumn(name = "acceso_cliente_ID", referencedColumnName = "ID", nullable = false)
    public MAcceso getmAccesoByAccesoClienteId() {
        return mAccesoByAccesoClienteId;
    }

    public void setmAccesoByAccesoClienteId(MAcceso mAccesoByAccesoClienteId) {
        this.mAccesoByAccesoClienteId = mAccesoByAccesoClienteId;
    }
}
