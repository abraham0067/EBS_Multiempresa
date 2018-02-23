package fe.db;

//import org.apache.commons.lang.StringEscapeUtils;

public class InformacionCorreos {
	private String estatus_mail;
	private String estatus_factura;
	private Integer Total;
	
public InformacionCorreos(String estatus_mail, String estatus_factura,Integer Total){
	this.estatus_mail=estatus_mail;
	this.estatus_factura=estatus_factura;
	this.Total=Total;
}
	public String getEstatus_mail() {
		return estatus_mail;
	}
	public String getEstatus_factura() {
		return estatus_factura;
	}
	public Integer getTotal() {
		return Total;
	}
	public void setEstatus_mail(String estatus_mail) {
		this.estatus_mail = estatus_mail;
	}
	public void setEstatus_factura(String estatus_factura) {
		this.estatus_factura = estatus_factura;
	}
	public void setTotal(Integer total) {
		Total = total;
	}
        
        /**
         * Escape sql and html special strings
         */
//        public void escapeSqlAndHtmlCharacters(){
//            this.estatus_factura =(this.estatus_factura != null) ? StringEscapeUtils.escapeSql(this.estatus_factura) : "";
//            this.estatus_mail =(this.estatus_mail != null) ? StringEscapeUtils.escapeSql(this.estatus_mail) : "";
//            
//            this.estatus_factura =(this.estatus_factura != null) ? StringEscapeUtils.escapeHtml(this.estatus_factura) : "";
//            this.estatus_mail =(this.estatus_mail != null) ? StringEscapeUtils.escapeHtml(this.estatus_mail) : "";
//        }
        
	

}
