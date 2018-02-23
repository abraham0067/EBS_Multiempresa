package fe.db;

public class Empresa_EmpresaTimbre {
	MEmpresa empresa;
	MEmpresaMTimbre empresaTimbre;	
	
	public Empresa_EmpresaTimbre() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	public Empresa_EmpresaTimbre(MEmpresa empresa, MEmpresaMTimbre empresaTimbre) {
		super();
		this.empresa = empresa;
		this.empresaTimbre = empresaTimbre;
	}


	public MEmpresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(MEmpresa empresa) {
		this.empresa = empresa;
	}

	public MEmpresaMTimbre getEmpresaTimbre() {
		return empresaTimbre;
	}

	public void setEmpresaTimbre(MEmpresaMTimbre empresaTimbre) {
		this.empresaTimbre = empresaTimbre;
	}

}
