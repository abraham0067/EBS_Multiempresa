package fe.db;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
//import org.apache.commons.lang.StringEscapeUtils;

import org.hibernate.annotations.Index;

@SuppressWarnings("serial")
@Entity
@Table(name = "M_EMPRESA")
public class MEmpresa implements Serializable {

    private int id = 0;
    /* Datos de la empresa */
    private String rfcOrigen = "";
    private String razonSocial = "";
    private MDireccion direccion;
    private byte[] licencia = new byte[0];
    private Date fechaRegistro = new Date();
    /* 1-confirmado,  2- bloqueado-falta-pago, 3-cancelado */
    private int estatusEmpresa;
    /* 1-- autoregistro, 0- resgistro portal */
    private int tipoRegistro = 1;
    /* Datos del contacto */
    private String nombreContacto = "";
    private String telefonoContacto = "";
    private String celularContacto = "";
    private String correoContacto = "";
    /* Datos para configurar la de la web en la base de datos */
    private String colorWeb = "";
    /*
     * 1- La plantilla toda la empresa
     * 2- La plantilla por serie
     * 3- Tipo de documento
     * 4- Controlado
     */
    private int tipo_Plantilla;
    private String logo;
    private Double impSaldoDisponible;

    //private MPlantilla plantilla;

    public MEmpresa() {
    }


    public MEmpresa(String rfcOrigen, String razonSocial, MDireccion direccion,
                    Date fechaRegistro, int estatusEmpresa,
                    int tipoRegistro, String nombreContacto, String telefonoContacto,
                    String celularContacto, String correoContacto, String colorWeb,
                    String logo) {
        super();
        this.rfcOrigen = rfcOrigen;
        this.razonSocial = razonSocial;
        this.direccion = direccion;
        this.fechaRegistro = fechaRegistro;
        this.estatusEmpresa = estatusEmpresa;
        this.tipoRegistro = tipoRegistro;
        this.nombreContacto = nombreContacto;
        this.telefonoContacto = telefonoContacto;
        this.celularContacto = celularContacto;
        this.correoContacto = correoContacto;
        this.colorWeb = colorWeb;
        this.logo = logo;
    }


    public MEmpresa(String rfcOrigen, String razonSocial,
                    MDireccion direccion, Date fechaRegistro,
                    int estatusEmpresa, int tipoRegistro,
                    String nombreContacto, String telefonoContacto,
                    String celularContacto, String correoContacto, String colorWeb) {
        super();
        this.rfcOrigen = rfcOrigen;
        this.razonSocial = razonSocial;
        this.direccion = direccion;
        this.fechaRegistro = fechaRegistro;
        this.estatusEmpresa = estatusEmpresa;
        this.tipoRegistro = tipoRegistro;
        this.nombreContacto = nombreContacto;
        this.telefonoContacto = telefonoContacto;
        this.celularContacto = celularContacto;
        this.correoContacto = correoContacto;
        this.colorWeb = colorWeb;

    }

    public MEmpresa(String rfcOrigen, String razonSocial, MDireccion direccion, Date fechaRegistro, int estatusEmpresa, int tipoRegistro, String nombreContacto, String telefonoContacto, String celularContacto, String correoContacto) {
        super();
        this.rfcOrigen = rfcOrigen.trim().toUpperCase();
        this.razonSocial = razonSocial.trim().toUpperCase();
        this.direccion = direccion;
        this.fechaRegistro = fechaRegistro;
        this.estatusEmpresa = estatusEmpresa;
        this.tipoRegistro = tipoRegistro;
        this.nombreContacto = nombreContacto.trim().toUpperCase();
        this.telefonoContacto = telefonoContacto;
        this.celularContacto = celularContacto;
        this.correoContacto = correoContacto;

    }


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    @Index(name = "EMP_RFC_IDX")
    @Column(name = "RFC_ORIGEN", unique = true, nullable = false, length = 15)
    public String getRfcOrigen() {
        return rfcOrigen;
    }

    @Index(name = "EMP_RS_IDX")
    @Column(name = "RAZON_SOCIAL", nullable = true, length = 100)
    public String getRazonSocial() {
        return razonSocial;
    }

    @ManyToOne
    @Index(name = "EMPRESA_DIRECCION_IDX")
    public MDireccion getDireccion() {
        return direccion;
    }

    @Lob
    @Column(name = "LICENCIA", nullable = true)
    public byte[] getLicencia() {
        return licencia;
    }

    @Index(name = "ACCESO_FECHA_IDX")
    @Column(name = "FECHA_REGISTRO", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getFechaRegistro() {
        return fechaRegistro;
    }


    @Column(name = "ESTATUS_EMPRESA", nullable = false)
    public int getEstatusEmpresa() {
        return estatusEmpresa;
    }

    @Column(name = "TIPO_REGISTRO", nullable = false)
    public int getTipoRegistro() {
        return tipoRegistro;
    }

    @Column(name = "NOMBRE_CONTACTO", nullable = true, length = 100)
    public String getNombreContacto() {
        return nombreContacto;
    }

    @Column(name = "TELEFONO_CONTACTO", nullable = true, length = 20)
    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    @Column(name = "CELULAR_CONTACTO", nullable = true, length = 100)
    public String getCelularContacto() {
        return celularContacto;
    }

    @Column(name = "CORREO_CONTACTO", nullable = true, length = 100)
    public String getCorreoContacto() {
        return correoContacto;
    }

    @Column(name = "COLOR_WEB", nullable = true, length = 100)
    public String getColorWeb() {
        return colorWeb;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRfcOrigen(String rfcOrigen) {
        this.rfcOrigen = (rfcOrigen != null && !"".equals(rfcOrigen)) ? rfcOrigen.trim().toUpperCase() : "";
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public void setDireccion(MDireccion direccion) {
        this.direccion = direccion;
    }

    public void setLicencia(byte[] licencia) {
        this.licencia = licencia;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public void setEstatusEmpresa(int estatusEmpresa) {
        this.estatusEmpresa = estatusEmpresa;
    }

    public void setTipoRegistro(int tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    public void setNombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public void setCelularContacto(String celularContacto) {
        this.celularContacto = celularContacto;
    }

    public void setCorreoContacto(String correoContacto) {
        this.correoContacto = correoContacto;
    }

    public void setColorWeb(String colorWeb) {
        this.colorWeb = colorWeb;
    }


    /**
     * @return the logo
     */
    @Column(name = "LOGO", nullable = true, length = 100)
    public String getLogo() {
        return logo;
    }


    /**
     * @param logo the logo to set
     */

    public void setLogo(String logo) {
        this.logo = logo;
    }


    public int getTipo_Plantilla() {
        return tipo_Plantilla;
    }


    @Column(name = "TIPO_PLANTILLA", nullable = false)
    public void setTipo_Plantilla(int tipo_Plantilla) {
        this.tipo_Plantilla = tipo_Plantilla;
    }

    @Basic
    @Column(name = "IMP_SALDO_DISPONIBLE", nullable = true, precision = 6)
    public Double getImpSaldoDisponible() {
        return impSaldoDisponible;
    }

    public void setImpSaldoDisponible(Double impSaldoDisponible) {
        this.impSaldoDisponible = impSaldoDisponible;
    }

    public String RFC_Empresa() {
        return rfcOrigen + " - " + razonSocial;
    }

    public String FECHA() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(fechaRegistro);
    }
}