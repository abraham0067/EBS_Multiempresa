package mx.com.ebs.emision.factura.vistas;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import mx.com.ebs.emision.factura.catalogos.CatCapturaManual;
import mx.com.ebs.emision.factura.catalogos.SelectCapturaManual;
import mx.com.ebs.emision.factura.controladores.CapturaManualManejador;
import mx.com.ebs.emision.factura.modelos.CapturaManualModelo_ANTERIOS;
import mx.com.ebs.emision.factura.utilierias.Limpiador;
import mx.com.ebs.emision.factura.utilierias.PintarLog;


import com.opensymphony.xwork2.ActionSupport;

import fe.db.MCfd;
import fe.db.MConceptosFacturacion;
import fe.db.MEmpresa;
import fe.db.MFolios;
import fe.db.MReceptor;
//import fe.model.dao.ConceptosFacturacionDAO;
//import fe.model.dao.UnidadesDAO;
import fe.sat.ComprobanteData;
import fe.sat.ConceptoData;
/**
 * @author Amiranda
 *
 */
public class CapturaManualAction extends ActionSupport{
	
	private static final long serialVersionUID = 1L;
	
	
	public  CatCapturaManual       catalogos              = new CatCapturaManual();
	public  CapturaManualModelo_ANTERIOS    capturaManualModelo    = new CapturaManualModelo_ANTERIOS();//En esta clase se llena el objeto general para enviarlo al servicio de facturacion
	public  SelectCapturaManual    selecionCapturaManual  = new SelectCapturaManual();//Contiene toda la información de los objetos SELECT  de la forma
	public  String                 totalContable          = "0.00";// Suma de los conceptos
	public  String                 mensajeError           = "";
	public  String                 mensajeCorrecto        = "";
	public  String                 comentarios            = "";
	public  String                 comentariosAddenda     = "";
	public  String                 conceptoSeleccionado   = "";
	public  String                 existeDirReceptor      = "";//Variable utilizada para determinar si los datos del receptor se consultan o solo se almacenan
	public  List<Object>           concepto               = new ArrayList<Object>();
	private ComprobanteData        comprobanteData        = new ComprobanteData();    //Contiene toda la informacion de los objetos Input   de la forma
	private List<ConceptoData>     listaConceptoData      = new ArrayList<ConceptoData>();	
	private ConceptoData           conceptoData           ;
	private HttpSession session;
	private MEmpresa   emisor   = new MEmpresa();
	private MReceptor   receptor  = new MReceptor();
	private MConceptosFacturacion  conceptoFac;
	//private MUnidad 			   unidad				  ;
	//private List<MUnidad>		   ListUnidades			  ;	
	private List<MConceptosFacturacion> ListaconceptosFac ;
	/*private ConceptosFacturacionDAO DAOCop                = new ConceptosFacturacionDAO();*/
//	private UnidadDAO				DAOUnid				  = new UnidadDAO();
	private String					validoOrden="0";
	private String 					ordeninterna	  ;
	private String 					impDescuento;
	private String					tipopersona;
	private String 					ValidoCentro="0";
	private String 					centroBeneficio;
	private String 					promotor;
	private String 					id_cliente;
	private MCfd					FacturaGenerada;
	private String 					uuid;
	

	
	/**
	 * @return the validoCentro
	 */
	public String getValidoCentro() {
		return ValidoCentro;
	}

	/**
	 * @param validoCentro the validoCentro to set
	 */
	public void setValidoCentro(String validoCentro) {
		ValidoCentro = validoCentro;
	}

	/**
	 * @return the centroBeneficio
	 */
	public String getCentroBeneficio() {
		return centroBeneficio;
	}

	/**
	 * @param centroBeneficio the centroBeneficio to set
	 */
	public void setCentroBeneficio(String centroBeneficio) {
		this.centroBeneficio = centroBeneficio;
	}

		
	/**
	 * Metodo encargado de llenar los datos de la factura manual y llamar al proceso de generación
	 * */
	@SuppressWarnings("unchecked")
	
	public String generaFacturaManual()throws Exception{
		String respuestaServicio = "";
		
		if((List<ConceptoData>)getSession().getAttribute("listaConceptoData")!=null)
			listaConceptoData = (List<ConceptoData>)getSession().getAttribute("listaConceptoData");	
		getSession().setAttribute("comprobanteData",   comprobanteData);			
		ConceptoData[] arrConceptos = listaConceptoData.toArray((new ConceptoData[listaConceptoData.size()]));
		comprobanteData.setConceptos(arrConceptos);
		comprobanteData.getAdditional().setParam(new String[]{ordeninterna,impDescuento});
		//Aqui llamamos a la clase de carlo encagada de generar la factura
		try{
			List<MFolios> listaFolios = new CapturaManualManejador().obtenNumeroDeFolio();
			
			if(listaFolios.size()==0)throw new Exception("No se encontraron folios registrados en la base de datos");
			
			
			// ================= Llenado manual de la información seleccionada en los catalogos ==========================
			comprobanteData = capturaManualModelo.cargaDatosComprobante(comprobanteData, selecionCapturaManual,emisor,receptor,comentarios,comentariosAddenda);
			// ===========================================================================================================
			
			// =================== Llamando al cliente de factura manual =======================================
			PintarLog.println("Apunto de llamar al servicio de factura manual");
			//respuestaServicio = new ClienteFacturaManual().exeGenFactura(comprobanteData);	
			System.out.println("respuesta Servidor "+ respuestaServicio);
			PintarLog.println("respuestaServicio:"+respuestaServicio);
			addActionMessage(respuestaServicio);// correcto 
			if((respuestaServicio.toLowerCase()).indexOf("error")!=-1){
				mensajeError = respuestaServicio;				
			}					
			// =================================================================================================
			
			getSession().setAttribute("listaConceptoData",  new ArrayList<ConceptoData>());
		}catch(Exception e){		
			
			PintarLog.println(e);
			mensajeError = "Error general en la llamada al proceso de generacion:"+e;
			//System.out.print(" error "+ mensajeError );
			addFieldError("mensajeError", mensajeError);//error
			return ERROR;
		}
			if((id_cliente == null || "".equals(id_cliente.trim())) && receptor!=null){
				id_cliente= new CapturaManualManejador().obtenerId_cliente(receptor.getRfcOrigen());
				
			}
			
			 
			 
			String []claves= new CapturaManualManejador().ObtId_emp_id_mon(emisor.getRfcOrigen(),emisor.getRazonSocial(),selecionCapturaManual.getMoneda());
			
			String cve_concepto= new CapturaManualManejador().ObtenCveConcepto(listaConceptoData.get(0).getDescripcion());
			if(claves!=null && !respuestaServicio.contains("error")){
			uuid=respuestaServicio;
			FacturaGenerada=new CapturaManualManejador().ObtenFacturaGeneradaUUID(uuid);
			if(FacturaGenerada!=null ){
				int id_empresa= 0, cve_moneda=0;
				if(claves[0]!=null && !"".equals(claves[0].trim())){
					id_empresa=Integer.parseInt(claves[0].trim());
				}
				if(claves[1]!=null && !"".equals(claves[1].trim())){
					cve_moneda=Integer.parseInt(claves[1].trim());
				}
				double id_clien=0;
				if(id_cliente!=null && !"".equals(id_cliente.trim())){
					id_clien=Double.parseDouble(id_cliente);
				}
				String Tx_orden=null, soc_centro=null;
				if(ordeninterna!=null && !"".equals(ordeninterna.trim())){
					Tx_orden=promotor;
				}
				if(centroBeneficio!=null && !"".equals(centroBeneficio.trim())){
					soc_centro=promotor;
				}
				int ID_AREA_FACT=1;
				if("FIDE".equals(FacturaGenerada.getSerieErp().trim())){
					ID_AREA_FACT=2;
				}
				System.out.println("Intenta Insertar datos");
				if(id_empresa>0 ){
				new CapturaManualManejador().InsertaSAT	( id_empresa, ID_AREA_FACT,FacturaGenerada.getNumeroFactura().replace(".", ""), FacturaGenerada.getTotal(), FacturaGenerada.getIva(), cve_concepto, "de la Disposición del Crédito : "+FacturaGenerada.getFolioErp(), FacturaGenerada.getTotal(), FacturaGenerada.getIva(),0, 0, 
						cve_moneda, "CESÓN: "+FacturaGenerada.getFolioErp(),0, FacturaGenerada.getFecha(), FacturaGenerada.getFecha(),  FacturaGenerada.getFecha(), null,null, null, id_clien,null, "F", comentariosAddenda, FacturaGenerada.getFolioErp(), 0, null, comentarios, ordeninterna, Tx_orden, centroBeneficio, soc_centro);
				}
					
			}
			
			}
		
		mensajeCorrecto = "Se generó correctamente la factura :"+respuestaServicio;
	
		inicializaObjetos();// limpiando los datos de la memoria.
		
		return SUCCESS;
	}
	
	
	/**
	 * Metodo encargado de llenar los arreglos de conceptos
	 * @return String , la cadena resultado a la accion
	 * */
	@SuppressWarnings("unchecked")
	public String agregaNuevoConcepto()throws Exception{	
		mensajeError = "";
		if(emisor!=null){			
			//ListaconceptosFac=DAOCop.listaDeConceptos_mediante_empresa(emisor);
		}
	//	ListUnidades=DAOUnid.ListaUnidad();
		if(conceptoData!=null){//se valida que el concepto no sea nulo para agregarlo al arreglo de conceptos		
			if(null==(List<ConceptoData>)getSession().getAttribute("listaConceptoData"))
				listaConceptoData = new ArrayList<ConceptoData>();
			else
				listaConceptoData=(List<ConceptoData>)getSession().getAttribute("listaConceptoData");
			
			listaConceptoData.add(conceptoData);
			getSession().setAttribute("listaConceptoData", listaConceptoData);
			
			//=================== Suma del valor total de los conceptos ========================================
			double sumaTotalConceptos = 0.00;
			
			for(ConceptoData concepto : listaConceptoData){
				sumaTotalConceptos += concepto.getPrecioTotal();
				totalContable       = new BigDecimal(sumaTotalConceptos).toString();
			}
			//==================================================================================================
			cargaSeriesEmisorSeleccionado();// Se busca las series asignadas al emisor seleccionado
			return "conceptoAgregado";
		}
		return SUCCESS;
	}
	
	/***
	 * Metodo encargado de eliminar un concepto de la lista de conceptos
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	public String eliminaConcepto()throws Exception{
		mensajeError = "";
		listaConceptoData=(List<ConceptoData>)getSession().getAttribute("listaConceptoData");
		if(listaConceptoData!=null && listaConceptoData.size()>0){
			PintarLog.println("concepto a eliminar: "+conceptoSeleccionado);
			for(int i=0;i<listaConceptoData.size();i++){				
				if(conceptoSeleccionado.equalsIgnoreCase(listaConceptoData.get(i).getDescripcion()))
					listaConceptoData.remove(i);
			}
			//=================== Suma del valor total de los conceptos ========================================
			double sumaTotalConceptos = 0.00;
			       totalContable      = new BigDecimal("0.00").toString();  
			for(ConceptoData concepto : listaConceptoData){
				sumaTotalConceptos += concepto.getPrecioTotal();
				totalContable       = new BigDecimal(sumaTotalConceptos).toString();
			}
			//==================================================================================================
			cargaSeriesEmisorSeleccionado();// Se busca las series asignadas al emisor seleccionado
			conceptoSeleccionado = "";
			getSession().setAttribute("listaConceptoData", listaConceptoData); // envia el arreglo de conceptos sin el elemento eliminado 
		}else{
			getSession().setAttribute("listaConceptoData",  new ArrayList<ConceptoData>()); // limpia el arreglo de conceptos
		}
		return SUCCESS;
	}

	/***
	 * Metodo encargado de buscar la direccion del emisor seleccionado en pantalla 
	 * @return String
	 */
	public String buscaDireccionEmisor()throws Exception{
		mensajeError = "";
		getSession().setAttribute("listaConceptoData",  new ArrayList<ConceptoData>());
		totalContable = "0.00";
		
		String idEmisor = selecionCapturaManual.getEmisor();
		if(selecionCapturaManual !=null && selecionCapturaManual.getEmisor() != null ){
			String[] llaveCompuestaEmisor = selecionCapturaManual.getEmisor().split("\\|");		
			idEmisor=llaveCompuestaEmisor[0];
		}
		List<MEmpresa> empresas = new CapturaManualManejador().consultaEmpresas(idEmisor);// Se la direccion del emisor seleccionado
		cargaSeriesEmisorSeleccionado();// Se busca las series asignadas al emisor seleccionado
		for(int i=0;i<empresas.size();i++)
			setEmisor(empresas.get(i));
		
		
		return SUCCESS;
	}
	
	/***
	 * Metodo encargado de buscar la direccion del receptor, mediante su RFC
	 * @return String
	 */
	public String buscaDireccionReceptor()throws Exception{
		mensajeError = "";		
		totalContable = "0.00";
		System.out.println("tipoPersona "+tipopersona);		
		try{
			cargaSeriesEmisorSeleccionado();
			Object[] objects= new CapturaManualManejador().consultaDireccionReceptorUnionBDS(Limpiador.cleanString(receptor.getRfcOrigen()),Limpiador.cleanString(tipopersona));
			 if(objects != null){
		        	MReceptor rep= (MReceptor) objects[0];
		        	setReceptor(rep);// Se la direccion del receptor
		        	setId_cliente((String) objects[1]);		        	
		        }
			
			//Validamos que exista registro , si no existe se manda la instruccion de dejar insertar el registro
			if(getReceptor()==null || getReceptor().getDireccion()==null || getReceptor().getDireccion().getCalle()==null || "".equalsIgnoreCase(getReceptor().getDireccion().getCalle()) )
				existeDirReceptor = "NO";
		}catch(Exception e){
			mensajeError = "Error:"+e;
			addFieldError("mensajeError", mensajeError);//error
		}
		PintarLog.println("existeDirReceptor?: "+existeDirReceptor);
		
		return SUCCESS;
	}
	
	/***
	 * Metodo encargado de almacenar los datos del receptor
	 * @return String
	 */
	public String almacenarReceptor()throws Exception{
		mensajeError = "";		
		totalContable = "0.00";
		existeDirReceptor ="";
		try{
			cargaSeriesEmisorSeleccionado();
			PintarLog.println("RFC a consultar: "+receptor.getRfcOrigen());		
			new CapturaManualManejador().almacenaDatosReceptor(receptor);// alamcenando datos del receptor
		}catch(Exception e){
			mensajeError = "Error:"+e;
			addFieldError("mensajeError", mensajeError);//error
		}
		return SUCCESS;
	}
	
	/***
	 * Metodo encargado de validar OrdenInterna
	 * @return String
	 */
	public String validarOrdenInterna()throws Exception{
		mensajeError = "";		
		totalContable = "0.00";
				
		try{
			cargaSeriesEmisorSeleccionado();
			if(ordeninterna!=null && !"".equals(ordeninterna.trim())){
				String []datos=new CapturaManualManejador().ValiadaOrdenCentro(ordeninterna,centroBeneficio);
				if(datos!= null && datos.length>=2){
					ordeninterna=datos[0];
					promotor=datos[1];
					
					if( !"No encontrado".equals(promotor.trim())){
						validoOrden="1";
					}
				}
			    ValidoCentro="0";
			}else{
				String []datos=new CapturaManualManejador().ValiadaOrdenCentro(ordeninterna,centroBeneficio);
				if(datos!= null && datos.length>=2){
					centroBeneficio=datos[0];
					promotor=datos[1];
					System.out.print(promotor);
					if( !"No encontrado".equals(promotor.trim())){
						ValidoCentro="1";
					}
				}			
				validoOrden="0";
			}
		}catch(Exception e){
			mensajeError = "Error:"+e;
			addFieldError("mensajeError", mensajeError);//error
		}
		PintarLog.println("existeDirReceptor?: "+existeDirReceptor);
		return SUCCESS;
	}
	
	
	
	
	/**
	 * Metodo encargado de inicializar objetos
	 * */
	public void inicializaObjetos(){		
		this.catalogos              = new CatCapturaManual();
		this.capturaManualModelo    = null;//En esta clase se llena el objeto general para enviarlo al servicio de facturacion
		this.selecionCapturaManual  = new SelectCapturaManual();//Contiene toda la información de los objetos SELECT  de la forma
		this.totalContable          = "0.00";// Suma de los conceptos		
		this.comprobanteData        = null;    //Contiene toda la informacion de los objetos Input   de la forma
		this.listaConceptoData      = null;
		this.conceptoData           = null;
		this.concepto               = null;
		this.emisor                 = new MEmpresa();
		this.receptor				= new MReceptor();
		this.id_cliente = "";
		this.ordeninterna ="";
		this.validoOrden="0";
		this.centroBeneficio="";
		this.ValidoCentro="0";
		this.comentarios            = "";
		this.comentariosAddenda     = "";
		this.existeDirReceptor      = "";
	}
	
	
	// Metodo control para cargar las serias de la empresa seleccionada por cada iteraccion en acciones de la pagina
	public void cargaSeriesEmisorSeleccionado(){
		System.out.println("Emisor --"+ emisor.getId());
		System.out.println("Emisor --"+ selecionCapturaManual.getEmisor());
		String[] llaveCompuestaEmisor = selecionCapturaManual.getEmisor().split("\\|");		
		catalogos.setListSerie(new CapturaManualManejador().consultaSeries(Integer.parseInt(llaveCompuestaEmisor[0])));
		//catalogos.setListSerie(new CapturaManualManejador().consultaSeries(Limpiador.cleanString(llaveCompuestaEmisor[2])));// Se busca las series asignadas al emisor seleccionado		
	}
	/**
	 * @return the comprobanteData
	 */
	public ComprobanteData getComprobanteData() {
		return comprobanteData;
	}

	/**
	 * @param comprobanteData the comprobanteData to set
	 */
	public void setComprobanteData(ComprobanteData comprobanteData) {
		this.comprobanteData = comprobanteData;
	}

	/**
	 * @return the conceptoData
	 */
	public ConceptoData getConceptoData() {
		return conceptoData;
	}

	/**
	 * @param conceptoData the conceptoData to set
	 */
	public void setConceptoData(ConceptoData conceptoData) {
		this.conceptoData = conceptoData;
	}

	/**
	 * @return the listaConceptoData
	 */
	public List<ConceptoData> getListaConceptoData() {
		return listaConceptoData;
	}

	/**
	 * @param listaConceptoData the listaConceptoData to set
	 */
	public void setListaConceptoData(List<ConceptoData> listaConceptoData) {
		this.listaConceptoData = listaConceptoData;
	}

	
	/**
	 * @return the session
	 */
	public HttpSession getSession() {
		return session;
	}

	/**
	 * @param session the session to set
	 */
	public void setSession(HttpSession session) {
		this.session = session;
	}

	/**
	 * @return the emisor
	 */
	public MEmpresa getEmisor() {
		return emisor;
	}

	/**
	 * @param emisor the emisor to set
	 */
	public void setEmisor(MEmpresa emisor) {
		this.emisor = emisor;
	}

	/**
	 * @return the receptor
	 */
	public MReceptor getReceptor() {
		return receptor;
	}

	/**
	 * @param receptor the receptor to set
	 */
	public void setReceptor(MReceptor receptor) {
		this.receptor = receptor;
	}

	/**
	 * @return the conceptoFac
	 */
	public MConceptosFacturacion getConceptoFac() {
		return conceptoFac;
	}

	/**
	 * @param conceptoFac the conceptoFac to set
	 */
	public void setConceptoFac(MConceptosFacturacion conceptoFac) {
		this.conceptoFac = conceptoFac;
	}

	/**
	 * @return the unidad
	
	//public MUnidad getUnidad() {
		return unidad;
	} */

	/**
	 * @param unidad the unidad to set
	
	public void setUnidad(MUnidad unidad) {
		this.unidad = unidad;
	} */

	/**
	 * @return the listUnidades
	
	public List<MUnidad> getListUnidades() {
		return ListUnidades;
	} */

	/**
	 * @param listUnidades the listUnidades to set
	 
	public void setListUnidades(List<MUnidad> listUnidades) {
		ListUnidades = listUnidades;
	}*/

	/**
	 * @return the listaconceptosFac
	 */
	public List<MConceptosFacturacion> getListaconceptosFac() {
		return ListaconceptosFac;
	}

	/**
	 * @param listaconceptosFac the listaconceptosFac to set
	 */
	public void setListaconceptosFac(List<MConceptosFacturacion> listaconceptosFac) {
		ListaconceptosFac = listaconceptosFac;
	}

	

	/**
	 * @return the ordeninterna
	 */
	public String getOrdeninterna() {
		return ordeninterna;
	}

	/**
	 * @param ordeninterna the ordeninterna to set
	 */
	public void setOrdeninterna(String ordeninterna) {
		this.ordeninterna = ordeninterna;
	}

	/**
	 * @return the validoOrden
	 */
	public String getValidoOrden() {
		return validoOrden;
	}

	/**
	 * @param validoOrden the validoOrden to set
	 */
	public void setValidoOrden(String validoOrden) {
		this.validoOrden = validoOrden;
	}

	/**
	 * @return the impDescuento
	 */
	public String getImpDescuento() {
		return impDescuento;
	}

	/**
	 * @param impDescuento the impDescuento to set
	 */
	public void setImpDescuento(String impDescuento) {
		this.impDescuento = impDescuento;
	}

	/**
	 * @return the tipopersona
	 */
	public String getTipopersona() {
		return tipopersona;
	}

	/**
	 * @param tipopersona the tipopersona to set
	 */
	public void setTipopersona(String tipopersona) {
		this.tipopersona = tipopersona;
	}

	public String getPromotor() {
		return promotor;
	}

	public void setPromotor(String promotor) {
		this.promotor = promotor;
	}

	public String getId_cliente() {
		return id_cliente;
	}

	public void setId_cliente(String id_cliente) {
		this.id_cliente = id_cliente;
	}

	public MCfd getFacturaGenerada() {
		return FacturaGenerada;
	}

	public void setFacturaGenerada(MCfd facturaGenerada) {
		FacturaGenerada = facturaGenerada;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}


	
}
