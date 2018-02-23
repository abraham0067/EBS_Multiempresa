package fe.db;

import java.util.List;

public class EmpresaTimbre_Timbre {
	private MEmpresaMTimbre empresaTimbre;
	private List<MTimbre> timbres;
	
	public EmpresaTimbre_Timbre() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public EmpresaTimbre_Timbre(MEmpresaMTimbre empresaTimbre,
			List<MTimbre> timbres) {
		super();
		this.empresaTimbre = empresaTimbre;
		this.timbres = timbres;
	}

	public MEmpresaMTimbre getEmpresaTimbre() {
		return empresaTimbre;
	}
	public void setEmpresaTimbre(MEmpresaMTimbre empresaTimbre) {
		this.empresaTimbre = empresaTimbre;
	}
	public List<MTimbre> getTimbres() {
		return timbres;
	}
	public void setTimbres(List<MTimbre> timbres) {
		this.timbres = timbres;
	}

}
