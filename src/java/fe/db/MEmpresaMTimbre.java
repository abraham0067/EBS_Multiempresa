package fe.db;

import javax.persistence.*;
import java.io.Serializable;
//import org.apache.commons.lang.StringEscapeUtils;

@SuppressWarnings("serial")
@Entity
@Table(name = "M_EMPRESA_M_TIMBRE")
public class MEmpresaMTimbre implements Serializable {
	private long id;
	private long folios = 0;
	private long asignados = 0;
	private MEmpresa empresa;
	private int tipoCte;
	private int estatus = 1;
	private String rfc;
	private String claveWS;

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
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the empresa
	 */
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	public MEmpresa getEmpresa() {
		return empresa;
	}

	/**
	 * @param empresa
	 *            the empresa to set
	 */
	public void setEmpresa(MEmpresa empresa) {
		this.empresa = empresa;
	}

	/**
	 * @return the tipoCte
	 */
	@Column(name = "TIPO_CTE", nullable = false)
	public int getTipoCte() {
		return tipoCte;
	}

	/**
	 * @param tipoCte
	 *            the tipoCte to set
	 */
	public void setTipoCte(int tipoCte) {
		this.tipoCte = tipoCte;
	}

	/**
	 * @return the estatus
	 */
	@Column(name = "ESTATUS", nullable = false)
	public int getEstatus() {
		return estatus;
	}

	/**
	 * @param estatus
	 *            the estatus to set
	 */
	public void setEstatus(int estatus) {
		this.estatus = estatus;
	}

	/**
	 * @return the rfc
	 */
	@Column(name = "RFC", unique = true, nullable = false, length = 15)
	public String getRfc() {
		return rfc;
	}

	/**
	 * @param rfc
	 *            the rfc to set
	 */
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	/**
	 * @return the claveWS
	 */
	@Column(name = "CLAVE_WS", nullable = false, length = 20)
	public String getClaveWS() {
		return claveWS;
	}

	/**
	 * @param claveWS
	 *            the claveWS to set
	 */

	public void setClaveWS(String claveWS) {
		this.claveWS = claveWS;
	}
	
	@Column(name = "FOLIOS", nullable = false)
	public long getFolios() {
		return folios;
	}

	public void setFolios(long folios) {
		this.folios = folios;
	}
	@Column(name = "ASIGNADOS", nullable = false)
	public long getAsignados() {
		return asignados;
	}

	public void setAsignados(long asignados) {
		this.asignados = asignados;
	}

	public String Estatus() {
		return (estatus == 0 ? "Activo" : "No Activo");
	}
//        public void escapeSqlAndHtmlCharacters(){
//            this.rfc=(this.rfc != null) ? StringEscapeUtils.escapeSql(this.rfc) : "";
//            this.claveWS=(this.claveWS != null )? StringEscapeUtils.escapeSql(this.claveWS) : "";//Escapar claves con caracteres especiales?
//            
//            this.rfc=(this.rfc != null) ? StringEscapeUtils.escapeHtml(this.rfc) : "";
//            this.claveWS=(this.claveWS != null )? StringEscapeUtils.escapeHtml(this.claveWS) : "";
//        }

}
