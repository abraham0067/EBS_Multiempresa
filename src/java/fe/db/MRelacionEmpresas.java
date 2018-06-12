package fe.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author lilo
 * 
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "M_RELACION_EMPRESAS")
public class MRelacionEmpresas implements Serializable {
	/**
	 * 
	 */
	@Id
	@Column(name = "ID_EMPRESA", nullable = false)
	private int idempresa;
	@Id
	@Column(name = "ID_SUBEMPRESA", nullable = false)
	private int idsubempresa;	
	
	


	public MRelacionEmpresas() {
		super();
	}



	public MRelacionEmpresas(int idempresa, int idsubempresa) {
		super();
		this.idempresa = idempresa;
		this.idsubempresa = idsubempresa;
	}



	/**
	 * @return the idempresa
	 */
	public int getIdempresa() {
		return idempresa;
	}



	/**
	 * @return the idsubempresa
	 */
	public int getIdsubempresa() {
		return idsubempresa;
	}



	/**
	 * @param idempresa the idempresa to set
	 */
	public void setIdempresa(int idempresa) {
		this.idempresa = idempresa;
	}



	/**
	 * @param idsubempresa the idsubempresa to set
	 */
	public void setIdsubempresa(int idsubempresa) {
		this.idsubempresa = idsubempresa;
	}

		
}
