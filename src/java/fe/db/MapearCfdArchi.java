package fe.db;

public class MapearCfdArchi {
	private MCfd cfd;
	/**
	 * existe=1 tiene archivos 0 no tiene
	 */
	private int existe=0;
	private String noPolizaSeguro = "";
	
	public MapearCfdArchi() {
		super();
	}

    public MCfd getCfd() {
        return this.cfd;
    }

    public int getExiste() {
        return this.existe;
    }

    public String getNoPolizaSeguro() {
        return this.noPolizaSeguro;
    }

    public void setCfd(MCfd cfd) {
        this.cfd = cfd;
    }

    public void setExiste(int existe) {
        this.existe = existe;
    }

    public void setNoPolizaSeguro(String noPolizaSeguro) {
        this.noPolizaSeguro = noPolizaSeguro;
    }
}
