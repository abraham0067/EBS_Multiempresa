package fe.db;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
//import org.apache.commons.lang.StringEscapeUtils;

import org.hibernate.annotations.Index;

@SuppressWarnings("serial")
@Entity
@Table(name = "M_PAC_M_EMPRESA")
public class MPacMEmpresa  implements Serializable{
private long id;
private String usuario;
private String password;
private Date fechaRegistro= new Date();
/*
 * 1- activo
 * 0- desactivado
 */
private int estatus=0;
private MEmpresa empresa;
private MPac pac;




public MPacMEmpresa() {
	super();
	// TODO Auto-generated constructor stub
}

/**
 * @return the id
 */
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
@Column(name = "ID")
public long getId() {
	return id;
}
/**
 * @param id the id to set
 */
public void setId(long id) {
	this.id = id;
}
/**
 * @return the usuario
 */
@Index(name = "PAC_EMPRESA_USUARIO_IDX")
@Column(name = "USUARIO",nullable = false, length = 20)
public String getUsuario() {
	return usuario;
}
/**
 * @param usuario the usuario to set
 */

public void setUsuario(String usuario) {
	this.usuario = usuario;
}
/**
 * @return the password
 */
@Column(name = "CLAVE", nullable = false, length = 100)
public String getPassword() {
	return password;
}
/**
 * @param password the password to set
 */
public void setPassword(String password) {
	this.password = password;
}


/**
 * @return the estatus
 */
@Column(name = "ESTATUS", nullable = false)
public int getEstatus() {
	return estatus;
}
/**
 * @param estatus the estatus to set
 */
public void setEstatus(int estatus) {
	this.estatus = estatus;
}
/**
 * @return the empresa
 */
@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
public MEmpresa getEmpresa() {
	return empresa;
}
/**
 * @param empresa the empresa to set
 */
public void setEmpresa(MEmpresa empresa) {
	this.empresa = empresa;
}

@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
public MPac getPac() {
	return pac;
}

public void setPac(MPac pac) {
	this.pac = pac;
}
@Index(name = "PAC_EMPRESA_FECHA_IDX")
@Column(name = "FECHA_REGISTRO", nullable = false)
@Temporal(javax.persistence.TemporalType.TIMESTAMP)
public Date getFechaRegistro() {
	return fechaRegistro;
}

public void setFechaRegistro(Date fechaRegistro) {
	this.fechaRegistro = fechaRegistro;
}

public String FechaRegistro() {
	return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(fechaRegistro);
}
public String Estatus(){
	return (estatus==1?"ACTIVO":"DESACTIVADO");
}
//public void escapeSqlAndHtmlCharacters(){
//    this.password = (this.password != null) ? StringEscapeUtils.escapeSql(this.password) : "" ;
//    this.usuario = (this.usuario != null) ? StringEscapeUtils.escapeSql(this.usuario) : "" ;
//    
//    this.password = (this.password != null) ? StringEscapeUtils.escapeHtml(this.password) : "" ;
//    this.usuario = (this.usuario != null) ? StringEscapeUtils.escapeHtml(this.usuario) : "" ;
//            
//}
}
