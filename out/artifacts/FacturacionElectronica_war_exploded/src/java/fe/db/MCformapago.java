package fe.db;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by eflores on 06/06/2017.
 */
@Entity
@Table(name = "C_FORMAPAGO")
public class MCformapago implements Serializable{
    private Integer id;
    private String codigo;
    private String descripcion;
    private Integer bancarizado;
    private Integer numeroOperacion;
    private Integer rfcEmiCor;
    private Integer cuentaOrdenante;
    private String patronCo;
    private Integer rfcEmiBen;
    private Integer cuentaBeneficiario;
    private String patronCb;
    private Integer tipoCadenaPago;
    private String nombreBancoEmisor;

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
    @Column(name = "CLAVE", nullable = false, length = 2)
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Basic
    @Column(name = "DESCRIPCION", nullable = true, length = 50)
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Basic
    @Column(name = "BANCARIZADO", nullable = true)
    public Integer getBancarizado() {
        return bancarizado;
    }

    public void setBancarizado(Integer bancarizado) {
        this.bancarizado = bancarizado;
    }

    @Basic
    @Column(name = "NUMERO_OPERACION", nullable = true)
    public Integer getNumeroOperacion() {
        return numeroOperacion;
    }

    public void setNumeroOperacion(Integer numeroOperacion) {
        this.numeroOperacion = numeroOperacion;
    }

    @Basic
    @Column(name = "RFC_EMI_COR", nullable = true)
    public Integer getRfcEmiCor() {
        return rfcEmiCor;
    }

    public void setRfcEmiCor(Integer rfcEmiCor) {
        this.rfcEmiCor = rfcEmiCor;
    }

    @Basic
    @Column(name = "CUENTA_ORDENANTE", nullable = true)
    public Integer getCuentaOrdenante() {
        return cuentaOrdenante;
    }

    public void setCuentaOrdenante(Integer cuentaOrdenante) {
        this.cuentaOrdenante = cuentaOrdenante;
    }

    @Basic
    @Column(name = "PATRON_CO", nullable = true, length = 75)
    public String getPatronCo() {
        return patronCo;
    }

    public void setPatronCo(String patronCo) {
        this.patronCo = patronCo;
    }

    @Basic
    @Column(name = "RFC_EMI_BEN", nullable = true)
    public Integer getRfcEmiBen() {
        return rfcEmiBen;
    }

    public void setRfcEmiBen(Integer rfcEmiBen) {
        this.rfcEmiBen = rfcEmiBen;
    }

    @Basic
    @Column(name = "CUENTA_BENEFICIARIO", nullable = true)
    public Integer getCuentaBeneficiario() {
        return cuentaBeneficiario;
    }

    public void setCuentaBeneficiario(Integer cuentaBeneficiario) {
        this.cuentaBeneficiario = cuentaBeneficiario;
    }

    @Basic
    @Column(name = "PATRON_CB", nullable = true, length = 75)
    public String getPatronCb() {
        return patronCb;
    }

    public void setPatronCb(String patronCb) {
        this.patronCb = patronCb;
    }

    @Basic
    @Column(name = "TIPO_CADENA_PAGO", nullable = true)
    public Integer getTipoCadenaPago() {
        return tipoCadenaPago;
    }

    public void setTipoCadenaPago(Integer tipoCadenaPago) {
        this.tipoCadenaPago = tipoCadenaPago;
    }

    @Basic
    @Column(name = "NOMBRE_BANCO_EMISOR", nullable = true, length = 500)
    public String getNombreBancoEmisor() {
        return nombreBancoEmisor;
    }

    public void setNombreBancoEmisor(String nombreBancoEmisor) {
        this.nombreBancoEmisor = nombreBancoEmisor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MCformapago that = (MCformapago) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (codigo != null ? !codigo.equals(that.codigo) : that.codigo != null) return false;
        if (descripcion != null ? !descripcion.equals(that.descripcion) : that.descripcion != null) return false;
        if (bancarizado != null ? !bancarizado.equals(that.bancarizado) : that.bancarizado != null) return false;
        if (numeroOperacion != null ? !numeroOperacion.equals(that.numeroOperacion) : that.numeroOperacion != null)
            return false;
        if (rfcEmiCor != null ? !rfcEmiCor.equals(that.rfcEmiCor) : that.rfcEmiCor != null) return false;
        if (cuentaOrdenante != null ? !cuentaOrdenante.equals(that.cuentaOrdenante) : that.cuentaOrdenante != null)
            return false;
        if (patronCo != null ? !patronCo.equals(that.patronCo) : that.patronCo != null) return false;
        if (rfcEmiBen != null ? !rfcEmiBen.equals(that.rfcEmiBen) : that.rfcEmiBen != null) return false;
        if (cuentaBeneficiario != null ? !cuentaBeneficiario.equals(that.cuentaBeneficiario) : that.cuentaBeneficiario != null)
            return false;
        if (patronCb != null ? !patronCb.equals(that.patronCb) : that.patronCb != null) return false;
        if (tipoCadenaPago != null ? !tipoCadenaPago.equals(that.tipoCadenaPago) : that.tipoCadenaPago != null)
            return false;
        if (nombreBancoEmisor != null ? !nombreBancoEmisor.equals(that.nombreBancoEmisor) : that.nombreBancoEmisor != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (codigo != null ? codigo.hashCode() : 0);
        result = 31 * result + (descripcion != null ? descripcion.hashCode() : 0);
        result = 31 * result + (bancarizado != null ? bancarizado.hashCode() : 0);
        result = 31 * result + (numeroOperacion != null ? numeroOperacion.hashCode() : 0);
        result = 31 * result + (rfcEmiCor != null ? rfcEmiCor.hashCode() : 0);
        result = 31 * result + (cuentaOrdenante != null ? cuentaOrdenante.hashCode() : 0);
        result = 31 * result + (patronCo != null ? patronCo.hashCode() : 0);
        result = 31 * result + (rfcEmiBen != null ? rfcEmiBen.hashCode() : 0);
        result = 31 * result + (cuentaBeneficiario != null ? cuentaBeneficiario.hashCode() : 0);
        result = 31 * result + (patronCb != null ? patronCb.hashCode() : 0);
        result = 31 * result + (tipoCadenaPago != null ? tipoCadenaPago.hashCode() : 0);
        result = 31 * result + (nombreBancoEmisor != null ? nombreBancoEmisor.hashCode() : 0);
        return result;
    }
}
