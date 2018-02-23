package fe.db;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
//import org.apache.commons.lang.StringEscapeUtils;
@SuppressWarnings({ "serial" })
@Entity
@Table(name = "M_CPTOFE")

public class MConceptosFacturacion implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private int id;
	@Column(name = "CONCEPTO_DE_FACTURACION", nullable = true, length = 500)
	private String conceptofacturacion;
	@Column(name = "CLAVE_CONCEPTO", nullable = false, length = 20)
	private String claveconcepto;
	@Column(name = "PRECIO_UNITARIO", nullable = false)
	private double precioUnitario;
	@Column(name = "UNIDAD_MEDIDA", nullable = false)
	private String UnidadMedida;
	//TODO	ELIMINAR LA RELACION CONCEPTO EMPRESA EN EL MAPEO
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private MEmpresa empresa;	

	public MConceptosFacturacion() {
                             conceptofacturacion = "";
	}

	public MConceptosFacturacion(String conceptofacturacion,String claveconcepto, double precioUnitario, String unidadMedida,MEmpresa empresa) {
		this.conceptofacturacion = conceptofacturacion;
		this.claveconcepto = claveconcepto;
		this.precioUnitario = precioUnitario;
		this.UnidadMedida = unidadMedida;
		this.empresa = empresa;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the conceptofacturacion
	 */
	public String getConceptofacturacion() {
		return conceptofacturacion;
	}

	/**
	 * @return the claveconcepto
	 */
	public String getClaveconcepto() {
		return claveconcepto;
	}

	/**
	 * @return the precioUnitario
	 */
	public double getPrecioUnitario() {
		return precioUnitario;
	}

	/**
	 * @return the unidadMedida
	 */
	public String getUnidadMedida() {
		return UnidadMedida;
	}

	/**
	 * @return the empresa
	 */
	public MEmpresa getEmpresa() {
		return empresa;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @param conceptofacturacion the conceptofacturacion to set
	 */
	public void setConceptofacturacion(String conceptofacturacion) {
		this.conceptofacturacion = conceptofacturacion;
	}

	/**
	 * @param claveconcepto the claveconcepto to set
	 */
	public void setClaveconcepto(String claveconcepto) {
		this.claveconcepto = claveconcepto;
	}

	/**
	 * @param precioUnitario the precioUnitario to set
	 */
	public void setPrecioUnitario(double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	/**
	 * @param unidadMedida the unidadMedida to set
	 */
	public void setUnidadMedida(String unidadMedida) {
		UnidadMedida = unidadMedida;
	}

	/**
	 * @param empresa the empresa to set
	 */
	public void setEmpresa(MEmpresa empresa) {
		this.empresa = empresa;
	}
        
//        public void escapeSqlAndHtmlCharacters(){
//            this.conceptofacturacion=(this.conceptofacturacion != null)?(StringEscapeUtils.escapeSql(this.conceptofacturacion)) : "" ;
//            this.claveconcepto = (this.claveconcepto != null)? (StringEscapeUtils.escapeSql(this.claveconcepto)) : "";
//            this.UnidadMedida = (this.UnidadMedida != null) ? (StringEscapeUtils.escapeSql(this.UnidadMedida)) : "";
//            
//            this.conceptofacturacion=(this.conceptofacturacion != null)?(StringEscapeUtils.escapeHtml(this.conceptofacturacion)) : "" ;
//            this.claveconcepto = (this.claveconcepto != null)? (StringEscapeUtils.escapeHtml(this.claveconcepto)) : "";
//            this.UnidadMedida = (this.UnidadMedida != null) ? (StringEscapeUtils.escapeHtml(this.UnidadMedida)) : "";
//        }

}
