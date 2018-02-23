package fe.db;

/**
 *
 * @Author Carlo Garcia Sanchez
 * @Date 04-10-2012
 *
 * Tabla para almacenar a los Clientes
 *
 */
 
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Column;

import org.hibernate.annotations.Index;
import javax.persistence.ManyToOne;
//import org.apache.commons.lang.StringEscapeUtils;

@SuppressWarnings("serial")
@Entity
@Table(name="M_CLIENTE") 
public class MCliente implements Serializable {
	private int id = 0;
	private MEmpresa empresa;
	private String noCliente = "";
	private String rfc = "";
	private String razonSocial = "";
	private int ctaPadre = 0;
	
	protected MCliente() {}
	
	public MCliente(MEmpresa empresa, String noCliente, String rfc, String razonSocial, int ctaPadre) {
		this.empresa = empresa;
		this.noCliente = noCliente;
		this.rfc = rfc;
		this.razonSocial = razonSocial;
		this.ctaPadre = ctaPadre;
	}

 	
 	@Id
  	@GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="ID", nullable=false)
	public int getId() {
		return id;
	}

    @ManyToOne
	public MEmpresa getEmpresa() {
		return (this.empresa); 
	}

	@Index(name = "CTE_NO_CTE_IDX") 
	public String getNoCliente() {
		return noCliente;
	}

	@Index(name = "CTE_RFC_IDX") 
    @Column(name="RFC", nullable=false, length=15)
	public String getRfc() {
		return rfc;
	}

	@Index(name = "CTE_RAZON_SOC_IDX") 
    @Column(name="RAZON_SOCIAL", nullable=true)
	public String getRazonSocial() {
		return razonSocial;
	}
	
    @Column(name="CUENTA_PADRE", nullable=true)
	public int getCtaPadre() {
		return ctaPadre;
	}

	public void setId(int id) {
		this.id = id; 
	}

	public void setEmpresa(MEmpresa empresa) {
		this.empresa = empresa; 
	}

	public void setNoCliente(String noCliente) {
		this.noCliente = noCliente;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc; 
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public void setCtaPadre(int ctaPadre) {
		this.ctaPadre = ctaPadre; 
	}
        
//        public void escapeSqlAndHtmlCharacters(){
//            this.noCliente=(this.noCliente != null)?StringEscapeUtils.escapeSql(this.noCliente) : "";
//            this.razonSocial=(this.razonSocial != null)?StringEscapeUtils.escapeSql(this.razonSocial) : "";
//            this.rfc=(this.rfc != null)?StringEscapeUtils.escapeSql(this.rfc) : "";
//            
//            this.noCliente=(this.noCliente != null)?StringEscapeUtils.escapeHtml(this.noCliente) : "";
//            this.razonSocial=(this.razonSocial != null)?StringEscapeUtils.escapeHtml(this.razonSocial) : "";
//            this.rfc=(this.rfc != null)?StringEscapeUtils.escapeHtml(this.rfc) : "";
//        }
}