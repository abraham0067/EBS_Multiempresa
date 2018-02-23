package fe.db;

import lombok.Getter;
import lombok.Setter;

public class MapearCfdArchi {
	@Getter @Setter
	private MCfd cfd;
	/**
	 * existe=1 tiene archivos 0 no tiene
	 */
	@Getter @Setter
	private int existe=0;
	@Getter @Setter
	private String noPolizaSeguro = "";
	
	public MapearCfdArchi() {
		super();
	}

}
