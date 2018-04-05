package com.ebs.mbeans;


import com.ebs.adenda.Adenda;
import com.ebs.adenda.Partes;
import com.ebs.adenda.Proveedor;
import com.ebs.adenda.Referencias;
import com.ebs.complementoextdata.*;
import com.ebs.exceptions.BadImpuestoTypeException;
import com.ebs.util.FloatsNumbersUtil;
import com.ebs.util.IdGenerator;
import com.ebs.catalogos.TipoImpuesto;
import com.ebs.helpers.LambdasHelper;
import fe.db.*;
import fe.model.dao.*;
import fe.model.fmanual.ImpuestoContainer;
import fe.model.util.CapturaManualModelo;
import fe.net.ClienteFacturaManual;
import fe.sat.addenda.coats.AddendaCoatsData;
import fe.sat.complementos.comercioexterior.ComercioExteriorData;
import fe.sat.complementos.comercioexterior.DescripcionesEspecificas;
import fe.sat.complementos.comercioexterior.DescripcionesEspecificasData;
import fe.sat.complementos.comercioexterior.DestinatarioComercio;
import fe.sat.complementos.comercioexterior.DestinatarioComercioData;
import fe.sat.complementos.comercioexterior.DomicilioComercio;
import fe.sat.complementos.comercioexterior.DomicilioComercioData;
import fe.sat.complementos.comercioexterior.EmisorComercioData;
import fe.sat.complementos.comercioexterior.MercanciaComercio;
import fe.sat.complementos.comercioexterior.MercanciaComercioData;
import fe.sat.complementos.comercioexterior.PropietarioComercio;
import fe.sat.complementos.comercioexterior.PropietarioComercioData;
import fe.sat.complementos.comercioexterior.ReceptorComercioData;
import fe.sat.complementos.v33.ComplementoFe;
import fe.sat.v33.RetencionData;
import fe.sat.v33.TrasladoData;
import fe.sat.v33.*;
import lombok.Getter;
import lombok.Setter;
import mx.com.ebs.emision.factura.catalogos.CatCapturaManual;
import mx.com.ebs.emision.factura.utilierias.PintarLog;
import mx.com.ebs.emision.factura.vo.catalogos.CatalogosBean;
import org.primefaces.behavior.ajax.AjaxBehavior;
import org.primefaces.component.fieldset.Fieldset;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.panelgrid.PanelGrid;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.component.UIInput;

import fe.vw.*;
import fe.audi.*;

import static com.sun.faces.el.ELUtils.createValueExpression;

/**
 * @author Eduardo C. Flores Ambrosio <Eduardo at EB&S>
 */
public class ManagedBeanFacturacionManual implements Serializable {

    //Datos de sesion
    private MAcceso mAcceso;
    private MEmpresa empresa;
    private HttpServletRequest httpServletRequest;
    private FacesContext faceContext;
    private String appContext;
    private String ISR_CODE = "001";
    private String ISR_DESC = "ISR";
    private String IVA_CODE = "002";
    private String IVA_DESC = "IVA";
    private String IEPS_CODE = "003";
    private String IEPS_DESC = "IEPS";
    private String IMP_EXENTO = "EXENTO";

    private final String RFC_EXTRANJERO = "XEXX010101000";
    private final String RFC_GENERICO = "XAXX010101000";
    private final String FACTURA_CLAVE = "I";
    private final String NOTA_CREDITO_CLAVE = "E";
    private final String TRASLADO_CLAVE = "T";

    private String IMP_NA = "NA";
    private static final String ZERO_STRING = "0.0";
    private static final String PPDSTR = "PPD";
    private static final String PUESTR = "PUE";
    private static final String MXNSTR = "MXN";
    private static final String XXXSTR = "XXX";
    private static final String UUIDREGEXPATT = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";
    private static final String ATEBREGEXPATT = "PRUEBAXX-ATEB-SERV-SACV-TIMBREPRUEBA";
    private static final String PANELGRIDSTRCOLUMNS6 = "ui-grid-col-1,ui-grid-col-3,ui-grid-col-1,ui-grid-col-3,ui-grid-col-1,ui-grid-col-3";
    private static final String PANELGRIDSTRCOLUMNS4 = "ui-grid-col-2,ui-grid-col-4,ui-grid-col-2,ui-grid-col-4";
    private static final int PANELGRIDINTCOLUMNS6 = 6;
    private static final int PANELGRIDINTCOLUMNS4 = 4;
    private static final String PLANTILLAGRAL = "CFDIV33";//INGRESO Y EGRESO
    private static final String PLANTILLAGRALTRASLADO = "TRASLADO_CFDIV33";//TRASLADOS
    private static final int MAXDEFVALUE = 20000000;
    private static final String PANELCPTOGRID = "ui-grid-col-2,ui-grid-col-4,ui-grid-col-2,ui-grid-col-4";
    private static final int COLUMNSPANELCTO = 4;

    //Para controlar algunos mensajes de debug
    private static boolean DEBUG = false;

    // Big decimal comparisons results
    private static final int EQUALS = 0;
    private static final int FIRSTGREATER = 1;
    private static final int SECONDGREATER = -1;

    private BigDecimal maxValueImporte;
    private final int LENGTHCODCONF = 5;
    //DAOS
    private EmpresaTimbreDAO daoEmpTimp;
    private CfdiDAO daoCFD;
    private EmpresaDAO daoEmp;
    private ReceptorDAO daoRec;
    private MonedaDAO daoMoneda;
    private FoliosDAO daoFolio;
    private ParcialidadesDAO daoParc;
    private ConceptoFacturacionDAO daoConFact;
    private ConfigDAO daoConfig;
    private ParamsAdditionalDao daoParamsFactMan;
    private TipoDocsFactManDao daoTipoDocs;
    private PaisDAO daoPais;

    //Elementos del form
    private MEmpresa empresaEmisora;
    private List<MReceptor> receptores;
    private List<MFolios> Folios;
    private CatCapturaManual catalogos;

    private List<MAddfieldsInvoice> paramsInvoice;
    private List<MAddfieldsCpto> paramsCpto;
    private List<AdendaFe> lstAdd = new ArrayList<AdendaFe>();


    private int idConcepto = -1;
    private MFolios folio;

    private double subTotal;
    private String busCop;//Busqueda conceptoAgregar
    private String quitarconcepto;
    /*
     * Codigo obtenido no automaticamente del PAC para facturar fuera de los limites
     * establecidos en el catalogo
     */
    private boolean codConfRequerido = false;
    private String codigoConfirmacion;

    @Getter
    @Setter
    private boolean cptoTmpShldAdd = false;
    /*
     * Lugar de emision
     */
    @Getter
    @Setter
    private String codigoPostal;
    @Getter
    @Setter
    private String residenciaFiscal;
    @Getter
    @Setter
    private String numRegIdTrib;
    @Getter
    @Setter
    private List<MCpais> paises;
    @Getter
    @Setter
    private boolean esReceptorExtranjero = false;

    @Getter
    @Setter
    public int requiredResidenciaFiscal = 0;
    @Getter
    @Setter
    public int requiredNumRegIdTrib = 0;

    /*
     * CFDIs relacionados
     */
    private String uuidTmp;
    private String tipoRelacion;
    private List<String> uuidsRels;
    private List<String> uuidsRelsSelection;
    private boolean flgBttonEliminarUuid = true;

    /*
     * Regimen fiscal emisor
     */
    private String regimenFiscal;
    /*
     * Uso CFDI receptor
     */
    private String usoCfdi;

    //DATOS DEL EMISOR Y EL RECEPTOR
    private String noCliente;
    private String refCliente;

    private int idEmpresa;//ID de la empresa emisora
    private int idCliente;//ID DEL RECEPTOR O CLIENTE

    //Datos generales de documentos
    private String formaPago = "01-Efectivo";
    private String metodoPago = PUESTR;
    // TODO: 01/09/2017 ELIMINAR LOS SIGUIENTES DOS ATRIBUTOS
    //TODO investigar en donde se guardara el numero de cuenta ahora
    private String noCuenta = "";
    //TODO investigar en donde se guardara el motivo de descuento
    private String motivoDescuento;

    private String condiciones;
    private String ambiente = "DESARROLLO";
    /*
     * Moneda
     */
    private String moneda;
    @Getter
    @Setter
    private Integer decimalesRequeridos;
    @Getter
    @Setter
    private Double porcentajeVariacion;
    @Getter
    @Setter
    boolean resOperationApplyChanges = false;
    /**
     * Catalogo Impuestos de traslados por objeto temporal
     * IVA e IEPS
     */
    private ImpuestoContainer ivaTrasladado; //USAR DATOS DEL CATALOGO DEL SAT
    private ImpuestoContainer iepsTrasladado;//USAR DATOS DEL CATALOGO DEL SAT
    /**
     * Catalogo Impuestos de retenciones por objeto temporal
     * IVA e IEPS
     */
    private ImpuestoContainer ivaRetenido;//10.6666(mas usado)%, 16%,4%
    private ImpuestoContainer isrRetenido;//EXENTO o FIJO AL 10%
    /**
     * Sumatorias del tipo de impuesto para el nodo impuesto
     */
    private double totalTranfer = 0.0;//total transferencias
    private double totalReten = 0.0;//total retenciones
    private List<ImpuestoContainer> trasladosFact;//Pueden haber varias combinaciones de IMPUESTO,FACTOR Y TASA
    private List<ImpuestoContainer> retencionesFact;//Se deben agrupar por tipo de impuesto

    @Getter
    @Setter
    private double tipoCambioDatosFacturaInput = 1.0;//Proporcionado por el usuario e
    private double tipoCambioActualBackend = 1.0;//Proporcionado por el SAT
    private double montoDescuento = 0.0;//Nivel Comprobante
    //Comentarios
    private String comentarios;
    //Conceptos
    private List<ConceptoFactura> conceptosAsignados;
    private List<ConceptoFactura> conceptosAsignadosSelect;
    //Creacion Nuevo Concepto
    private ConceptoFactura tempConcepto;//Cuando se crea y se agrea un concepto


    @Getter
    private final int NO_REQUERIDO = 0;
    @Getter
    public final int REQUERIDO = 1;
    @Getter
    private final int OPCIONAL = 2;
    @Getter
    private final int DESCONOCIDO = -1;


    @Getter
    @Setter
    private int idFolio;
    @Getter
    @Setter
    private int idTipoDoc;
    private MTdocsFactman tipoDocObjFact;
    //Conceptos Asignados
    private List<MConceptosFacturacion> conceptosEmpresa;// Conceptos Disponibles para cada empresa
    private double total = 0.0;

    //Datos de generacion de factura
    private MEmpresa emisor;
    private MReceptor receptor;

    private ComprobanteData comprobanteData;
    private CapturaManualModelo capturaManualModelo;
    //Datos agragados para la addenda
    private int keyReceptorAddenda = 0;//0-No addenda 1-Addenda coats 2-...
    private static Map<String, Integer> empresasConAddenda;
    private String numeroProveedor;
    private String[] OrdenCompra;
    private String ordenCompraInput = "";
    //UTIL
    private DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
    private DecimalFormat df;
    private boolean flgButtonEliminarDisabled = true;//Inicialmente el boton esta desactivado
    private double limiteSuperior = 0.0;
    private double limiteInferior = 0.0;
    @Getter
    @Setter
    private List<MTdocsFactman> tiposDocs;
    private IdGenerator idGen;

    enum OPERACION {
        CREACION,
        MODIFICACION
    }

    ;
    private OPERACION currentOperationOverCpto = OPERACION.CREACION;

    private ArrayList<String> respuestas;

    @Setter
    @Getter
    private String[] valuesAdditionalDataComp;
    private Integer[] valuesAdditionalDataCompIndexes;///para determinar el indice del additional params en el que seagregara cada param
    @Setter
    @Getter
    private String[] valuesAdditionalDataCpto;
    private Integer[] valuesAdditionalDataCptoIndexes;///para determinar el indice del additional params en el que seagregara cada param

    ///
    static final NumberFormat formatter2 = new DecimalFormat("#0.00");
    static final NumberFormat formatter3 = new DecimalFormat("#0.000");
    static final NumberFormat formatter4 = new DecimalFormat("#0.0000");


    //Bindings
    @Setter
    @Getter
    private UIInput inputTipoCambio;
    @Setter
    @Getter
    private UIInput inputCodigoConfirmacion;
    @Setter
    @Getter
    private UIInput inputResidenciaFiscal;
    @Setter
    @Getter
    private UIInput inputNumRegIdTrib;

    @Setter
    @Getter
    private boolean disabledInputCondicionesDePago;
    @Setter
    @Getter
    private boolean disabledInputFormaDePago;
    @Setter
    @Getter
    private boolean disabledInputMetodoDePago;
    @Setter
    @Getter
    private boolean disabledInputTipoCambio;


    @Setter
    @Getter
    private boolean disabledInputDescuentoDetalleConcepto;
    @Setter
    @Getter
    private boolean disabledInputValorUnitarioDetalleConcepto;
    @Setter
    @Getter
    private boolean disabledFieldSetImpuestosDetalleConcepto;

    @Setter
    @Getter
    private Fieldset fieldSetRefComp;

    @Setter
    @Getter
    private Fieldset fieldSetRefCpto;
    private static final String RFC_CME = "CME891127EA1";
    private static final String RFC_AAA = "AAA010101AAA";
    private static final String RFC_VW = "VME640813HF6";
    private static final String RFC_AUDI = "AAU120905KG9";


    ///Envio de correo automatico factura manual
    @Setter
    @Getter
    private String emailCliente;
    @Setter
    @Getter
    private boolean enviarEmailCliente;


    ///Complemento Comercio Exterior data model
    ///Son similares a los conceptos solo que hay que convertir cantidades cuando la moneda no es USD
    ///Algunos campo se copian tal cual y existen algunos nuevos que se agregan
    @Setter
    @Getter
    private boolean disponibleComercioExterior;
    @Setter
    @Getter
    private boolean usarComplementoComercioExterior;
    @Setter
    @Getter
    private FlagsEntradasComercioExterior flagsEntradasComercioExterior;
    @Getter
    @Setter
    List<CustomMercanciaData> mercanciasData;
    @Getter
    @Setter
    CustomEmisorData singleEmisorData;
    @Getter
    @Setter
    CustomDomicilioData singleDomicilioEmisorData;
    @Getter
    @Setter
    CustomPropietarioData singlePropietarioData;
    @Getter
    @Setter
    CustomReceptorData singleReceptorData;
    @Getter
    @Setter
    CustomDomicilioData singleDomicilioReceptorData;
    @Getter
    @Setter
    CustomDestinatarioData singleDestinatarioData;
    @Getter
    @Setter
    CustomDomicilioData singleDomicilioDestinatarioData;
    @Getter
    @Setter
    CustomComplementoComercioExteriorMetadata singleComplementoComercioExteriorData;
    @Getter
    @Setter
    Adenda singleAdenda;
    @Getter
    @Setter
    Proveedor dataProveedorAdenda;
    @Getter
    @Setter
    Referencias dataReferenciaAdenda;
    @Getter
    @Setter
    List<Partes> dataPartesAdenda;
    @Getter
    @Setter
    private boolean usarComplementoAdendaVW;
    @Getter
    @Setter
    private boolean usarComplementoAdendaAudi;
    @Getter
    @Setter
    private boolean adendaProductos;
    @Getter
    @Setter
    private boolean adendaServicios;
    @Getter
    @Setter
    private String tipoAdenda;
    @Getter
    @Setter
    private String clienteAddenda; //1 volkswagwn 2 audi
    @Getter
    @Setter
    private FacturaVWData factura;
    @Getter
    @Setter
    private String codigoImpuestoAdenda;
    @Getter
    @Setter
    private FacturaAudiData facturaAudi;


    static {
        empresasConAddenda = new TreeMap<String, Integer>();
        empresasConAddenda.put(RFC_CME, 1);
        empresasConAddenda.put(RFC_AAA, 15);//Empresa Prueba
        empresasConAddenda.put(RFC_VW, 2);
        empresasConAddenda.put(RFC_AUDI, 3);
    }


    /**
     * Creates a new instance of ManagedBeanFacturacionManual
     */
    public ManagedBeanFacturacionManual() {
        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        mAcceso = (MAcceso) httpSession.getAttribute("macceso");
        empresa = mAcceso.getEmpresa();
        appContext = httpServletRequest.getContextPath();
    }

    /**
     * Post Construct
     */
    @PostConstruct
    public void init() {
        idGen = new IdGenerator(0, 0l);
        daoEmpTimp = new EmpresaTimbreDAO();
        daoCFD = new CfdiDAO();
        daoMoneda = new MonedaDAO();
        daoEmp = new EmpresaDAO();
        daoRec = new ReceptorDAO();
        daoFolio = new FoliosDAO();
        daoParc = new ParcialidadesDAO();
        daoConFact = new ConceptoFacturacionDAO();
        daoConfig = new ConfigDAO();
        catalogos = new CatCapturaManual();
        daoParamsFactMan = new ParamsAdditionalDao();
        daoTipoDocs = new TipoDocsFactManDao();
        daoPais = new PaisDAO();

        idTipoDoc = -1;
        idConcepto = -1;
        conceptosAsignados = new ArrayList<>();
        conceptosAsignadosSelect = new ArrayList<>();
        tempConcepto = new ConceptoFactura(idGen.incrementAndGet());//Creacion y agregacion
        decimalesRequeridos = 2;
        //Retenciones
        montoDescuento = 0.0;
        idEmpresa = -1;
        //Impuestos por concepto
        initImpuestosConcepto();
        //Impuestos totales de la factura
        trasladosFact = new ArrayList<>();
        retencionesFact = new ArrayList<>();
        //Para dar formato a los numeros, de lo contrario se usario la comma como separador
        getSimbolos().setDecimalSeparator('.');
        setDf(new DecimalFormat("####.##", getSimbolos()));
        uuidsRels = new ArrayList<>();
        cptoTmpShldAdd = false;
        busCop = "";
        quitarconcepto = "";
        codigoConfirmacion = "";
        codigoPostal = "";
        uuidTmp = null;
        tipoRelacion = "";
        regimenFiscal = "";
        usoCfdi = "";
        noCliente = "";
        refCliente = "";
        formaPago = "";
        metodoPago = "";
        noCuenta = "";
        condiciones = "";
        moneda = "";
        motivoDescuento = "";
        comentarios = "";
        numeroProveedor = "";
        ordenCompraInput = "";
        maxValueImporte = new BigDecimal(20000000);
        emailCliente = "";
        enviarEmailCliente = false;
        paises = daoPais.getAll();

        //Inicializamos el tipo de documento
        tiposDocs = daoTipoDocs.getAll();
        if (tiposDocs != null && !tiposDocs.isEmpty()) {
            idTipoDoc = tiposDocs.get(0).getId();
            tipoDocObjFact = LambdasHelper.findTipoDocById(tiposDocs, idTipoDoc);
        }
        MConfig ambObj = daoConfig.BuscarConfigDatoClasificacion("AMBIENTE", "SERVIDOR");
        if (ambObj != null) {
            if (ambObj.getValor() != null && !ambObj.getValor().isEmpty()) {
                ambiente = ambObj.getValor().toUpperCase();
            }
        }
        MConfig debugObj = daoConfig.BuscarConfigDatoClasificacion("DEBUG", "SERVIDOR");
        if (debugObj != null) {
            if (debugObj.getValor() != null && !debugObj.getValor().isEmpty()) {
                // TODO: 14/09/2017 AGREGAR UN PARAMETRO PARA CONTROLAR EL DEBUG
                if (debugObj.getValor().equalsIgnoreCase("SI") || debugObj.getValor().equalsIgnoreCase("1")
                        || debugObj.getValor().equalsIgnoreCase("YES")) {
                    DEBUG = true;
                } else {
                    DEBUG = false;
                }
            }
        }

        respuestas = new ArrayList<>();
        initComercioExterior();
        initAdenda();

        //? TRASLADOS COMERCIO


        if (tiposDocs != null && tiposDocs.size() > 0) {
            if (idTipoDoc < 0) {
                idTipoDoc = tiposDocs.get(0).getId();
                tipoDocObjFact = LambdasHelper.findTipoDocById(tiposDocs, idTipoDoc);
                agruparImpuestosComprobante();
            } else {
                tipoDocObjFact = LambdasHelper.findTipoDocById(tiposDocs, idTipoDoc);
                agruparImpuestosComprobante();

            }
        }


        ///Manejar entradas deacuerdo al tipo de documento que se esta manejando
        if (idTipoDoc > 0 && tipoDocObjFact != null) {
            if (tipoDocObjFact.getcTipoComprobanteByTdocId().getClave().equals("I")) {

            } else if (tipoDocObjFact.getcTipoComprobanteByTdocId().getClave().equals("E")) {

            } else if (tipoDocObjFact.getcTipoComprobanteByTdocId().getClave().equals("T")) {
                condiciones = "";
                formaPago = "-1";
                metodoPago = "-1";
                disabledInputCondicionesDePago = true;
                disabledInputFormaDePago = true;
                disabledInputMetodoDePago = true;
                disabledFieldSetImpuestosDetalleConcepto = true;
                disabledInputDescuentoDetalleConcepto = true;
                disabledInputValorUnitarioDetalleConcepto = true;
            }
        } else {

        }


    }

    private void initComercioExterior() {
        System.out.println("Init comercio exterior models   ");
        flagsEntradasComercioExterior = new FlagsEntradasComercioExterior(FACTURA_CLAVE);
        disponibleComercioExterior = false;
        usarComplementoComercioExterior = false;
        mercanciasData = new ArrayList<>();
        singleEmisorData = new CustomEmisorData();
        singleDomicilioEmisorData = new CustomDomicilioData();
        singlePropietarioData = new CustomPropietarioData();
        singleReceptorData = new CustomReceptorData();
        singleDomicilioReceptorData = new CustomDomicilioData();
        singleDestinatarioData = new CustomDestinatarioData();
        singleDomicilioDestinatarioData = new CustomDomicilioData();
        singleComplementoComercioExteriorData = new CustomComplementoComercioExteriorMetadata();
    }


    private void initAdenda() {
        usarComplementoAdendaVW = false;
        usarComplementoAdendaAudi = false;
        dataPartesAdenda = new ArrayList<>();
        dataReferenciaAdenda = new Referencias();
        dataProveedorAdenda = new Proveedor();
    }


    /**
     * DELETE ALL INFO ON VIEW
     */
    public void reset() {

        disabledInputCondicionesDePago = false;
        disabledInputFormaDePago = false;
        disabledInputMetodoDePago = false;
        disabledFieldSetImpuestosDetalleConcepto = false;
        disabledInputDescuentoDetalleConcepto = false;
        disabledInputValorUnitarioDetalleConcepto = false;
        idEmpresa = -1;
        idCliente = -1;
        noCliente = "";
        refCliente = "";

        if (tiposDocs != null && tiposDocs.size() > 0) {
            idTipoDoc = tiposDocs.get(0).getId();
            tipoDocObjFact = LambdasHelper.findTipoDocById(tiposDocs, idTipoDoc);
        }

        formaPago = "Pago en una sola exhibición";
        metodoPago = "NO IDENTIFICADO";
        idFolio = -1;
        motivoDescuento = "";
        noCuenta = "";
        codigoPostal = "";
        condiciones = "";
        moneda = "";
        tipoCambioDatosFacturaInput = 1.0;
        montoDescuento = 0.0d;
        comentarios = "";
        busCop = "";
        idConcepto = -1;
        flgButtonEliminarDisabled = true;
        conceptosAsignados.clear();
        conceptosAsignadosSelect.clear();
        this.receptores.clear();
        subTotal = 0.0;
        totalTranfer = 0.0;
        totalReten = 0.0;
        total = 0.0;
        emailCliente = "";
        enviarEmailCliente = false;
        //CUADRO DE DIALOGO CREACION Y AGREGACION
        tempConcepto = new ConceptoFactura(idGen.incrementAndGet());
        //Impuestos por concepto
        initImpuestosConcepto();
        //Impuestos totales de la factura
        trasladosFact = new ArrayList<>();
        retencionesFact = new ArrayList<>();
        uuidsRels = new ArrayList<>();
        //Campos addenda coats
        numeroProveedor = "";
        OrdenCompra = null;
        ordenCompraInput = "";
        cptoTmpShldAdd = false;
        codConfRequerido = false;
        usoCfdi = "";
        regimenFiscal = "";
        if (conceptosEmpresa != null)
            conceptosEmpresa.clear();

        resetComercioExterior();
    }

    public void resetComercioExterior() {
        initComercioExterior();
        flagsEntradasComercioExterior.reset();
    }

    public void procesarConcepto() {

        if (currentOperationOverCpto == OPERACION.CREACION) {
            registrarAgregarNuevoConcepto();
        } else {
            aplicarCambiosConcepto();
        }

    }

    /**
     * Registrar un tempConcepto en la base datos y lo agrega a los
     * conceptosEmpresa de la factura considerando la cantidad ingresada en el
     * cuadro de dialogo
     */
    public void registrarAgregarNuevoConcepto() {
        handleChangeTipoCambioUSD();
        boolean resOperation = false;//calback param
        boolean continuar = true;
        MConceptosFacturacion tmp;
        if (usarComplementoComercioExterior) {
            continuar = validarCamposRequeridosConceptoComercioExterior(tempConcepto);
            if (!continuar) {
                FacesContext.getCurrentInstance().addMessage("frmManual", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Usted a marcado que esta usando el Complemento de Comercio Exterior, " +
                                "los campos 'FraccionArancelaria', 'NoIdentificacion', 'UnidadAduana' y 'Marca' son valores requeridos.", "Error"));
            } else {
                System.out.println("OK parametros comercio exterior");
            }
        }

        if (continuar && validaSeleccionEmisor()) {
            //Guarda un nuevo conceptoAgregar en la base de datos y lo agrega junto a los demas conceptosEmpresa ya existentes
            MConceptosFacturacion ctofe = daoConFact.BuscarConceptoEmpresa(tempConcepto.getConceptofacturacion().trim(), this.idEmpresa);
            if (ctofe != null) {
                FacesContext.getCurrentInstance().addMessage("frmManual", new FacesMessage(FacesMessage.SEVERITY_ERROR, "El concepto ya existe, cambie el nombre del concepto de facturación.", "Error"));
            } else if (tempConcepto.getValorUnitario() < 0) {
                FacesContext.getCurrentInstance().addMessage("frmManual", new FacesMessage(FacesMessage.SEVERITY_WARN, "El precio unitario no debe ser negativo.", "Error"));
            } else if (tempConcepto.getCantidad() <= 0) {
                FacesContext.getCurrentInstance().addMessage("frmManual", new FacesMessage(FacesMessage.SEVERITY_WARN, "La cantidad debe ser mayor a 0.", "Error"));
            } else {
                empresaEmisora = daoEmp.BuscarEmpresaId(this.idEmpresa);
                if (empresa != null) {//Si el usuario tiene empresas asignadas
                    tmp = new MConceptosFacturacion();//Objeto para la BD
                    tmp.setClaveconcepto(tempConcepto.getClaveconcepto());
                    tmp.setConceptofacturacion(tempConcepto.getConceptofacturacion());
                    tmp.setPrecioUnitario(tempConcepto.getValorUnitario());
                    tmp.setUnidadMedida(tempConcepto.getUnidad());
                    tmp.setEmpresa(empresaEmisora);
                    agregarImpuestos(tempConcepto);//agrega los impuestos al concepto
                    agregarParamsCpto(tempConcepto);
                    //modificarConceptBYTipoDoc(tempConcepto);

                    if (!resOperationApplyChanges) {
                        FacesContext.getCurrentInstance().addMessage("frmManual", new FacesMessage(FacesMessage.SEVERITY_WARN, "Por favor revise los datos de impuestos del concepto, " +
                                "quiza no ha seleccionado algun valor", ""));
                        resOperation = false;
                    } else {
                        if (daoConFact.GuardarOActualizar(tmp)) {
                            this.conceptosEmpresa = daoConFact.ListaConceptosIdEmpresa(this.idEmpresa);//Actualiza la lista de conceptos de la empresa
                            tempConcepto.calcularMontos();//Calcula las cifras del concepto
                            conceptosAsignados.add(tempConcepto);
                            this.ejecutaOperaciones();//Actualizamos el sutotal y el total con las nuevas cantidades
                            flgButtonEliminarDisabled = false;
                            reiniciarDatosConcepto();
                            resOperation = true;
                            FacesContext.getCurrentInstance().addMessage("frmManual", new FacesMessage(FacesMessage.SEVERITY_INFO, "El concepto ha sido registrado y agregado a la factura.", "Info"));
                        } else {
                            FacesContext.getCurrentInstance().addMessage("frmManual", new FacesMessage(FacesMessage.SEVERITY_INFO, "No pudimos guardar el concepto." +
                                    " El numero de identificación es requerido para esta accion.", "Info"));
                        }
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage("frmManual", new FacesMessage(FacesMessage.SEVERITY_INFO, "Debe especificar la empresa en el campo desplegable.", "Info"));
                }
            }
            //Enviamos retrollamada
            RequestContext.getCurrentInstance().addCallbackParam("succes", resOperation);
        } else {
            //Enviamos retrollamada
            RequestContext.getCurrentInstance().addCallbackParam("succes", resOperation);
        }
    }

    public void addMessageAdenda() {
        String summary = adendaProductos ? "Ha seleccionado una adenda de tipo producto" : adendaServicios ? "Ha seleccionado una adenda de tipo servicio" : "";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
    }

    private boolean validarCamposRequeridosConceptoComercioExterior(ConceptoFactura conceptoValida) {

        String fraccionArancelaria = conceptoValida.getFraccionArancelaria() == null ? "" : conceptoValida.getFraccionArancelaria();
        String NoIdentificacion = conceptoValida.getClaveconcepto() == null ? "" : conceptoValida.getClaveconcepto();
        String UnidadAduana = conceptoValida.getUnidadAduana() == null ? "" : conceptoValida.getUnidadAduana();
        String marca = conceptoValida.getMarca() == null ? "" : conceptoValida.getMarca();

        if (singleComplementoComercioExteriorData.getTipoCambioUSD() == 1.0) {
            FacesContext.getCurrentInstance().addMessage("frmManual", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Antes de poder agregar conceptos ingrese el tipo de cambio USD. El tipo de cambioUSD no puede ser 1.0", ""));
            return false;
        }


        if (fraccionArancelaria == null || fraccionArancelaria.isEmpty()) {

            if (!UnidadAduana.equalsIgnoreCase("99")) {
                return false;
            } else {
                System.out.println("validación ok");
            }

        }


        if (NoIdentificacion == null || NoIdentificacion.isEmpty()) {
            return false;
        }


        if (UnidadAduana == null || UnidadAduana.isEmpty()) {
            return false;
        }


        if (marca == null || marca.isEmpty()) {
            return false;
        }


        return true;
    }

    private boolean validarCamposAdenda() {

        if (getKeyReceptorAddenda() == 2) {
            if ((numeroProveedor != null && ordenCompraInput != null) || (!numeroProveedor.isEmpty() && !ordenCompraInput.isEmpty())) {
                return true;
            }
        }


        return false;
    }

    /*
    private void modificarConceptBYTipoDoc(ConceptoFactura tmp) {
        //MOdificamos el concepto deacuerdo al tipo de documento
        if(idTipoDoc > 0) {
            if (tipoDocObjFact.getcTipoComprobanteByTdocId().getClave().equalsIgnoreCase("T")) {
                tmp.setDescuento(0.0);
                tmp.setTraslados(new ArrayList<ImpuestoContainer>(){{
                }});
                tmp.setRetenciones(new ArrayList<ImpuestoContainer>(){{
                }});
            }
        }
    }
    */

    /**
     * METODO MODIFICACION DE CONCEPTOS
     */
    public void aplicarCambiosConcepto() {
        boolean continuar = true;

        if (usarComplementoComercioExterior) {
            continuar = validarCamposRequeridosConceptoComercioExterior(tempConcepto);
            if (!continuar) {
                FacesContext.getCurrentInstance().addMessage("frmManual", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Un campo del comercio exterior es requerido y usted no lo a agregado.", "Error"));
            } else {
                System.out.println("OK parametros comercio exterior");
            }
        }


        if (continuar) {

            agregarImpuestos(tempConcepto);
            agregarParamsCpto(tempConcepto);
            //modificarConceptBYTipoDoc(tempConcepto);
            if (!resOperationApplyChanges) {
                FacesContext.getCurrentInstance().addMessage("frmManual", new FacesMessage(FacesMessage.SEVERITY_WARN, "Por favor revise los datos de impuestos del concepto, quiza no ha seleccionado algun valor.", "->"));
                RequestContext.getCurrentInstance().addCallbackParam("succes", false);//Enviamos un parametro al view
            } else {

                tempConcepto.calcularMontos();
                if (cptoTmpShldAdd) {
                    flgButtonEliminarDisabled = false;
                    conceptosAsignados.add(tempConcepto);
                    handleChangeTipoCambioUSD();
                    if (usarComplementoComercioExterior) {
                        transformarConceptosAMercancias();
                    }
                    cptoTmpShldAdd = false;
                }

                flgButtonEliminarDisabled = false;
                this.ejecutaOperaciones();
                // this.ejecutaOperacionComercioExterior();
                reiniciarDatosConcepto();
                FacesContext.getCurrentInstance().addMessage("frmManual", new FacesMessage(FacesMessage.SEVERITY_INFO, "Cambios aplicados.", "Info"));
                RequestContext.getCurrentInstance().addCallbackParam("succes", true);//Enviamos un parametro al view
            }

        } else {
            //Enviamos retrollamada
            RequestContext.getCurrentInstance().addCallbackParam("succes", false);
        }
    }


    private void transformarConceptosAMercancias() {
        mercanciasData.clear();
        for (ConceptoFactura tmp : this.conceptosAsignados) {
            CustomMercanciaData merc = new CustomMercanciaData();
            merc.setCantidadAduana(tmp.getCantidad());
            merc.setFraccionArancelaria(new CustomCatalogoData(tmp.getFraccionArancelaria(), tmp.getFraccionArancelaria()));
            merc.setNoIdentificacion(tmp.getClaveconcepto());
            merc.setUnidadAduana(new CustomCatalogoData(tmp.getUnidadAduana(), tmp.getUnidadAduana()));
            merc.setValorDolares(tmp.getValorDolares());
            merc.setValorUnitarioAduana(tmp.getValorUnitarioAduana());
            merc.getDescripcionesEspecificas().add(new CustomDescripcionEspecificaData(tmp.getMarca()));
            mercanciasData.add(merc);
        }
    }

    private void transformarConceptosAProdServicios() {

        System.out.println("transforma conceptos");

        int posicion = 0;
        for (ConceptoFactura tmp : this.conceptosAsignados) {
            posicion++;
            Partes parte = new Partes();
            parte.setCantidadMaterial(tmp.getCantidad());
            parte.setPosicion(posicion);
            parte.setNumeroMaterial(tmp.getNumeroSerie());
            parte.setDescripcionMterial(tmp.getConceptofacturacion());
            parte.setUnidadMedida(tmp.getUnidad());
            parte.setPrecioUnitario(tmp.getValorUnitario());
            parte.setMontoLinea(tmp.getImporte());
            parte.setOdenCompra(ordenCompraInput);
            parte.setCodigoImpuesto(codigoImpuestoAdenda);
            dataPartesAdenda.add(parte);
        }
    }


    public void saveParamProd(String param) {
        tempConcepto.setBusquedaProdServ(param);
    }

    public void saveParamUnidad(String param) {
        tempConcepto.setBusquedaUnidad(param);
    }


    /**
     * Agregar los impuestos seleccionados al nuevo concepto o al concepto AGREGAR
     */
    public void agregarImpuestos(ConceptoFactura tmp) {
        resOperationApplyChanges = true;
        tmp.clearImpuestos();//Limpiamos impuestos del concepto antes de agregar los nuevos impuestos
        try {
            ///Traslados
            if (ivaTrasladado.getTipoFactor() != null
                    && !ivaTrasladado.getTipoFactor().trim().isEmpty()
                    && !ivaTrasladado.getTipoFactor().equals(IMP_NA)) {
                if (ivaTrasladado.getTipoFactor().equalsIgnoreCase(IMP_EXENTO)) {//Exento ->eliminamos la tasay la cuota
                    ivaTrasladado.setTasaOcuota(ZERO_STRING);
                    ivaTrasladado.setImporte(ZERO_STRING);
                    tmp.agregarImpTraslado(ivaTrasladado);
                } else if (!ivaTrasladado.getTipoFactor().equalsIgnoreCase(IMP_EXENTO)
                        && (ivaTrasladado.getTasaOcuota() != null
                        && !ivaTrasladado.getTasaOcuota().isEmpty())) {
                    tmp.agregarImpTraslado(ivaTrasladado);
                } else {
                    resOperationApplyChanges = false;
                }
            }
            if (iepsTrasladado.getTipoFactor() != null && !iepsTrasladado.getTipoFactor().trim().isEmpty() && !iepsTrasladado.getTipoFactor().equals(IMP_NA)) {
                if (iepsTrasladado.getTipoFactor().equalsIgnoreCase(IMP_EXENTO)) {
                    iepsTrasladado.setTasaOcuota(ZERO_STRING);
                    iepsTrasladado.setImporte(ZERO_STRING);
                    tmp.agregarImpTraslado(iepsTrasladado);
                } else if (!iepsTrasladado.getTipoFactor().equalsIgnoreCase(IMP_EXENTO)
                        && (iepsTrasladado.getTasaOcuota() != null
                        && !iepsTrasladado.getTasaOcuota().isEmpty())) {
                    tmp.agregarImpTraslado(iepsTrasladado);
                } else {
                    resOperationApplyChanges = false;
                }
            }

            ///Retenciones
            if (ivaRetenido.getTipoFactor() != null && !ivaRetenido.getTipoFactor().trim().isEmpty() && !ivaRetenido.getTipoFactor().equals(IMP_NA)) {
                if (ivaRetenido.getTipoFactor().equalsIgnoreCase(IMP_EXENTO)) {
                    //Exento excluye los dos siguientes campso
                    ivaRetenido.setTasaOcuota(ZERO_STRING);
                    ivaRetenido.setImporte(ZERO_STRING);
                    tmp.agregarImpRetencion(ivaRetenido);
                } else if (!ivaRetenido.getTipoFactor().equalsIgnoreCase(IMP_EXENTO)
                        && (ivaRetenido.getTasaOcuota() != null
                        && !ivaRetenido.getTasaOcuota().isEmpty())) {
                    tmp.agregarImpRetencion(ivaRetenido);
                } else {
                    resOperationApplyChanges = false;
                }
            }
            if (isrRetenido.getTipoFactor() != null
                    && !isrRetenido.getTipoFactor().trim().isEmpty()
                    && !isrRetenido.getTipoFactor().equals(IMP_NA)) {
                if (isrRetenido.getTipoFactor().equalsIgnoreCase(IMP_EXENTO)) {
                    isrRetenido.setTasaOcuota(ZERO_STRING);
                    isrRetenido.setImporte(ZERO_STRING);
                    tmp.agregarImpRetencion(isrRetenido);
                } else if (!isrRetenido.getTipoFactor().equalsIgnoreCase(IMP_EXENTO)
                        && (isrRetenido.getTasaOcuota() != null
                        && !isrRetenido.getTasaOcuota().isEmpty())) {
                    tmp.agregarImpRetencion(isrRetenido);
                } else {
                    resOperationApplyChanges = false;
                }
            }
        } catch (BadImpuestoTypeException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Ha ocurrido un error al intentar agregar un impuesto", ""));
        }
    }


    private void agregarParamsCpto(ConceptoFactura tmp) {
        tmp.setParams(Arrays.asList(valuesAdditionalDataCpto));
        tmp.setIndicesParams(Arrays.asList(valuesAdditionalDataCptoIndexes));
    }

    /**
     * Reinicia los datos que se cargan cuando se registra o carga un concepto
     */
    public void reiniciarDatosConcepto() {
        currentOperationOverCpto = OPERACION.CREACION;
        cptoTmpShldAdd = false;
        tempConcepto = null;
        tempConcepto = new ConceptoFactura(idGen.incrementAndGet());
        idConcepto = -1;
        initImpuestosConcepto();
        creaCamposAdicionalesCpto();
    }


    public ComprobanteData addComplementoComercioExterior(ComprobanteData cdata) {

        ComercioExteriorData comercioExteriorData = new ComercioExteriorData();
        ///-------------------------------------------------------------------------------------------------------------
        /// Emisor
        ///-------------------------------------------------------------------------------------------------------------
        DomicilioComercioData domicilioComercioData = new DomicilioComercioData();
        domicilioComercioData.setCalle(singleDomicilioEmisorData.getCalle());
        domicilioComercioData.setCodigoPostal(singleDomicilioEmisorData.getCodigoPostal());
        domicilioComercioData.setColonia(singleDomicilioEmisorData.getColonia());
        domicilioComercioData.setEstado(singleDomicilioEmisorData.getEstado());
        domicilioComercioData.setMunicipio(singleDomicilioEmisorData.getMunicipio());
        domicilioComercioData.setNumeroExterior(singleDomicilioEmisorData.getNumeroExterior());
        domicilioComercioData.setNumeroInterior(singleDomicilioEmisorData.getNumeroInterior());
        domicilioComercioData.setPais(singleDomicilioEmisorData.getPais().getClave());

        EmisorComercioData emisorData = new EmisorComercioData();
        emisorData.setDomicilioComercio(domicilioComercioData);


        ///-------------------------------------------------------------------------------------------------------------
        /// Receptor
        ///-------------------------------------------------------------------------------------------------------------
        domicilioComercioData = new DomicilioComercioData();
        domicilioComercioData.setCalle(singleDomicilioReceptorData.getCalle());
        domicilioComercioData.setCodigoPostal(singleDomicilioReceptorData.getCodigoPostal());
        domicilioComercioData.setColonia(singleDomicilioReceptorData.getColonia());
        domicilioComercioData.setEstado(singleDomicilioReceptorData.getEstado());
        domicilioComercioData.setMunicipio(singleDomicilioReceptorData.getMunicipio());
        domicilioComercioData.setNumeroExterior(singleDomicilioReceptorData.getNumeroExterior());
        domicilioComercioData.setNumeroInterior(singleDomicilioReceptorData.getNumeroInterior());
        domicilioComercioData.setPais(singleDomicilioReceptorData.getPais().getClave());

        ReceptorComercioData receptorComercio = new ReceptorComercioData();
        receptorComercio.setDomicilioComercio(domicilioComercioData);


        ///-------------------------------------------------------------------------------------------------------------
        /// Destinatario
        ///-------------------------------------------------------------------------------------------------------------

        domicilioComercioData = new DomicilioComercioData();
        domicilioComercioData.setCalle(singleDomicilioDestinatarioData.getCalle());
        domicilioComercioData.setCodigoPostal(singleDomicilioDestinatarioData.getCodigoPostal());
        domicilioComercioData.setColonia(singleDomicilioDestinatarioData.getColonia());
        domicilioComercioData.setEstado(singleDomicilioDestinatarioData.getEstado());
        domicilioComercioData.setMunicipio(singleDomicilioDestinatarioData.getMunicipio());
        domicilioComercioData.setNumeroExterior(singleDomicilioDestinatarioData.getNumeroExterior());
        domicilioComercioData.setNumeroInterior(singleDomicilioDestinatarioData.getNumeroInterior());
        domicilioComercioData.setPais(singleDomicilioDestinatarioData.getPais().getClave());

        List<DomicilioComercio> domicilios = new ArrayList<>();
        domicilios.add(domicilioComercioData);

        DestinatarioComercioData destinatarioComercioData = new DestinatarioComercioData();
        destinatarioComercioData.setDomicilio(domicilios);


        ///-------------------------------------------------------------------------------------------------------------
        /// Propietario
        ///-------------------------------------------------------------------------------------------------------------
        PropietarioComercioData propietarioComercioData = null;

        ///-------------------------------------------------------------------------------------------------------------
        /// Mercancias
        ///-------------------------------------------------------------------------------------------------------------

        List<CustomMercanciaData> mercancias = mercanciasData;

        // TODO: 12/01/2018 AGRUPAR MERCANCIAS POR NOIDENTIFICACION
        List<MercanciaComercio> mercanciasFe = new ArrayList<>();


        for (CustomMercanciaData mercancia : mercancias) {
            MercanciaComercioData temp = new MercanciaComercioData();
            temp.setCantidadAduana(mercancia.getCantidadAduana());
            temp.setFraccionArancelaria(mercancia.getFraccionArancelaria().getClave());
            temp.setIdentificacion(mercancia.getNoIdentificacion());
            temp.setUnidadAduana(mercancia.getUnidadAduana().getClave());
            temp.setValorDolares(mercancia.getValorDolares());
            temp.setValorUnitarioAduana(mercancia.getValorUnitarioAduana());

            List<CustomDescripcionEspecificaData> deskt = mercancia.getDescripcionesEspecificas();
            List<DescripcionesEspecificas> descfe = new ArrayList<>();

            for (CustomDescripcionEspecificaData desctmp : deskt) {
                DescripcionesEspecificasData descrips = new DescripcionesEspecificasData();
                descrips.setMarca(desctmp.getMarca());
                //descrips.setModelo(desctmp.getModelo());
                //descrips.setNumeroSerie(desctmp.getNumeroSerie());
                //descrips.setSubModelo(desctmp.getSubModelo());
                descfe.add(descrips);
            }

            temp.setDescripcionesEspecificas(descfe);

            mercanciasFe.add(temp);


            ///-------------------------------------------------------------------------------------------------------------
            /// Complemento comercio exterior
            ///-------------------------------------------------------------------------------------------------------------

            comercioExteriorData.setEmisorComercio(emisorData);
            comercioExteriorData.setReceptorComercio(receptorComercio);

            List<PropietarioComercio> propietarios = new ArrayList<>();
            propietarios.add(propietarioComercioData);
            comercioExteriorData.setPropietarioComercio(propietarios);

            List<DestinatarioComercio> destinatarios = new ArrayList<>();
            destinatarios.add(destinatarioComercioData);
            comercioExteriorData.setDestinatarioComercio(destinatarios);

            comercioExteriorData.setMercanciaComercio(mercanciasFe);

            comercioExteriorData.setCertificadoOrigen(Integer.parseInt(singleComplementoComercioExteriorData.getCertificadoOrigen()));
            comercioExteriorData.setClaveDePedimento(singleComplementoComercioExteriorData.getClaveDePedimento());
            comercioExteriorData.setIncoterm(singleComplementoComercioExteriorData.getIncoterm());
            comercioExteriorData.setSubdivision(Integer.parseInt(singleComplementoComercioExteriorData.getSubdivision()));
            comercioExteriorData.setTipoCambioUSD(singleComplementoComercioExteriorData.getTipoCambioUSD());
            comercioExteriorData.setTotalUSD(singleComplementoComercioExteriorData.getTotalUSD());

        }

        if (tipoDocObjFact.getcTipoComprobanteByTdocId().getClave().equalsIgnoreCase("T")) {
            System.out.println("Entro trasalado exterior");
            comercioExteriorData.setMotivoTraslado(singleComplementoComercioExteriorData.getMotivoTraslado());
        }


        List<ComplementoFe> complementos = cdata.getComplementosFe();
        if (complementos == null) {
            complementos = new ArrayList<>();
        }
        complementos.add(comercioExteriorData);
        cdata.setComplemementosFe(complementos);

        return cdata;
    }

    public void facturaVw(String nombreProveedor, String sociedad) {

        if (tipoDocObjFact.getcTipoComprobanteByTdocId().getClave().equalsIgnoreCase("E")) {
            factura.setTipoDocumentoFiscal("CR");
        } else if (tipoDocObjFact.getcTipoComprobanteByTdocId().getClave().equalsIgnoreCase("I")) {
            factura.setTipoDocumentoFiscal("FA");
        }

        if (!moneda.toUpperCase().equals("MXN")) {
            factura.setTipoMoneda("MXP");
        } else {
            factura.setTipoMoneda(moneda);
            factura.setTipoCambio(tipoCambioDatosFacturaInput);
        }

        factura.setCodigoProveedor(dataReferenciaAdenda.getCodigoProveedor());

        factura.setNombreProveedor(nombreProveedor);
        factura.setCodigoDestino(dataReferenciaAdenda.getCodigoDestino());
        factura.setReferenciaProveedor(dataReferenciaAdenda.getReferenciaProvedor());
        factura.setRemision(dataReferenciaAdenda.getRemision());

        factura.setNombreSolicitante(dataReferenciaAdenda.getNombreSolicitante());
        factura.setCorreoSolicitante(dataReferenciaAdenda.getCorreoSolicitante());
        factura.setCorreoContacto(dataReferenciaAdenda.getCorreoContacto());
        factura.setSociedad(sociedad);

    }

    public void facturaAudi() {

        facturaAudi.setTipo(adendaProductos ? FacturaAudi.TIPO.MATERIALES : FacturaAudi.TIPO.SERVICIO);

        if (tipoDocObjFact.getcTipoComprobanteByTdocId().getClave().equalsIgnoreCase("E")) {
            facturaAudi.tipoDocumentoFiscal = "CR";
        } else if (tipoDocObjFact.getcTipoComprobanteByTdocId().getClave().equalsIgnoreCase("I")) {
            facturaAudi.tipoDocumentoFiscal = "FA";
        }

        if (!moneda.toUpperCase().equals("MXN")) {
            facturaAudi.tipoMoneda = "MXP";
        } else {
            facturaAudi.tipoMoneda = moneda;
            facturaAudi.tipoCambio = tipoCambioDatosFacturaInput;
        }

        facturaAudi.noProveedor = dataReferenciaAdenda.getCodigoProveedor();
        facturaAudi.codigoDestino = dataReferenciaAdenda.getCodigoDestino();
        facturaAudi.correoSolicitante = dataReferenciaAdenda.getCorreoSolicitante();
        facturaAudi.eMail = dataReferenciaAdenda.getCorreoContacto();

    }

    public void usarAddenda() {
        usarComplementoAdendaAudi = false;
        usarComplementoAdendaVW = false;
        System.out.println("valor addenda: " + clienteAddenda);
        if (clienteAddenda.equalsIgnoreCase("1")) {
            usarComplementoAdendaVW = true;
            System.out.println("usa addenda vw");
        } else if (clienteAddenda.equalsIgnoreCase("2")) {
            System.out.println("usa addenda audi");
            usarComplementoAdendaAudi = true;
        }
    }

    public ComprobanteData addAddenda(ComprobanteData cdata) {

        AddendaVolksWagenData adendaVolksWagen = null;
        AddendaAudi addendaAudi = null;
        List<Partes> listPartes = dataPartesAdenda;
        String rfcCliente = daoRec.BuscarReceptorIdr(idCliente).getRfcOrigen();
        String nombreProveedor = daoRec.BuscarReceptorIdr(idCliente).getRazonSocial();

        List<ParteVW> partesFe = new ArrayList<>();
        List<ParteAudi> partesAudi = new ArrayList<>();
        try {

            if (usarComplementoAdendaVW) {
                factura = new FacturaVWData();
                usarComplementoAdendaVW = true;
                facturaVw(nombreProveedor, "");
            } else if (usarComplementoAdendaAudi) {
                usarComplementoAdendaAudi = true;
                facturaAudi = new FacturaAudiData();
                facturaAudi();
            }

            if (usarComplementoAdendaAudi) {

                for (Partes parte : listPartes) {

                    ParteAudiData parteAudi = new ParteAudiData();
                    parteAudi.cantidad = parte.getCantidadMaterial();
                    parteAudi.codigoImpuesto = parte.getCodigoImpuesto();
                    parteAudi.desMaterial = parte.getDescripcionMterial();
                    parteAudi.montoLinea = parte.getMontoLinea();
                    //parteAudi.setNoMaterial(parte.getNumeroMaterial());
                    parteAudi.unidad = parte.getUnidadMedida();
                    parteAudi.pu = parte.getPrecioUnitario();
                    parteAudi.posicion = parte.getPosicion();
                    parteAudi.ordenCompra = ordenCompraInput;


                    partesAudi.add(parteAudi);

                    facturaAudi.partes = partesAudi.toArray(facturaAudi.getPartes());
                }

            } else if (usarComplementoAdendaVW) {
                for (Partes parte : listPartes) {

                    ParteVWData tmpParte = new ParteVWData();
                    tmpParte.setCantidad(parte.getCantidadMaterial());
                    tmpParte.setCodigoImpuesto(parte.getCodigoImpuesto());
                    tmpParte.setDesMaterial(parte.getDescripcionMterial());
                    tmpParte.setMontoLinea(parte.getMontoLinea());
                    tmpParte.setNoMaterial(parte.getNumeroMaterial());
                    tmpParte.setUnidadMedida(parte.getUnidadMedida());
                    tmpParte.setPrecioUnitario(parte.getPrecioUnitario());
                    tmpParte.setPosicion(parte.getPosicion());
                    tmpParte.setOrdenCompra(ordenCompraInput);

                    partesFe.add(tmpParte);

                    factura.setPartes(partesFe.toArray(factura.getPartes()));
                }
            }


        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }


        if (usarComplementoAdendaVW) {
            adendaVolksWagen = new AddendaVolksWagenData(adendaProductos ? AddendaVolksWagenData.TIPO_ADENDA_VW.PMT : AddendaVolksWagenData.TIPO_ADENDA_VW.PSV);
            adendaVolksWagen.setFacturaVW(factura);
            lstAdd.add(adendaVolksWagen);
            System.out.println("Entro agregar adenda vw");

        } else if (usarComplementoAdendaAudi) {
            lstAdd.add(facturaAudi);
            System.out.println("Entro agregar adenda audi");
        }

        cdata.setAdendas(lstAdd);

        return cdata;

    }

    public void genAdenda(FacturaVWData factura) {

        tipoDocObjFact.getcTipoComprobanteByTdocId().getClave().equalsIgnoreCase("T");


    }


    private void initImpuestosConcepto() {
        //Reset impuestos del concepto temporal
        ivaTrasladado = new ImpuestoContainer(TipoImpuesto.TRASLADO);
        iepsTrasladado = new ImpuestoContainer(TipoImpuesto.TRASLADO);
        ivaRetenido = new ImpuestoContainer(TipoImpuesto.RETENCION);
        isrRetenido = new ImpuestoContainer(TipoImpuesto.RETENCION);
        ivaTrasladado.setImpuesto(IVA_CODE);
        ivaTrasladado.setDescripcionImpuesto(IVA_DESC);
        iepsTrasladado.setImpuesto(IEPS_CODE);
        iepsTrasladado.setDescripcionImpuesto(IEPS_DESC);
        ivaRetenido.setImpuesto(IVA_CODE);
        ivaRetenido.setDescripcionImpuesto(IVA_DESC);
        isrRetenido.setImpuesto(ISR_CODE);
        isrRetenido.setDescripcionImpuesto(ISR_DESC);

    }

    public void cargaInfoConceptoExistente(ConceptoFactura tmp) {
        currentOperationOverCpto = OPERACION.MODIFICACION;
        /*
        buscarInfoProdUnidadForCptoChanges(tmp.getBusquedaProdServ(), tmp.getBusquedaUnidad());
        buscarIvaTasasPorFactorTras(tmp.getFactorIvaTras());
        buscarIepsTasasPorFactorTras(tmp.getFactorIepsTras());
        buscarTasasIsrRetenciones(tmp.getFactorIsrRets());
        buscarTasasIvaRetenciones(tmp.getFactorIvaRets());
        * */
        cargarInfoImpuestos(tmp.getTraslados(), tmp.getRetenciones());
        agregarValorAdditionalParamsCpto(tmp);
    }

    /**
     * CARGA INFORMACION DE UN CONCEPTO CON LOS DATOS QUE YA EXISTEN
     */
    public void cargaInfoConceptoFromDB() {

        if (this.idConcepto > 0) {
            MConceptosFacturacion res = daoConFact.BuscarConceptoId(this.idConcepto);
            reiniciarDatosConcepto();
            tempConcepto.setClaveconcepto(res.getClaveconcepto() == null ? "" : res.getClaveconcepto());
            tempConcepto.setConceptofacturacion(res.getConceptofacturacion());
            tempConcepto.setUnidad(res.getUnidadMedida());
            cptoTmpShldAdd = true;//Indicamos que el concepto se debe agregar a la lista de conceptos uan vez que se realizen las
            currentOperationOverCpto = OPERACION.MODIFICACION;


            tipoDocObjFact = LambdasHelper.findTipoDocById(tiposDocs, idTipoDoc);
            if (tipoDocObjFact != null) {
                if (tipoDocObjFact.getcTipoComprobanteByTdocId().getClave().equalsIgnoreCase("I")) {
                    tempConcepto.setValorUnitario(res.getPrecioUnitario());
                } else if (tipoDocObjFact.getcTipoComprobanteByTdocId().getClave().equalsIgnoreCase("E")) {
                    tempConcepto.setValorUnitario(res.getPrecioUnitario());
                } else if (tipoDocObjFact.getcTipoComprobanteByTdocId().getClave().equalsIgnoreCase("T")) {
                    tempConcepto.setValorUnitario(0.0);
                }
            }
        }

    }

    public void agregarValorAdditionalParamsCpto(ConceptoFactura tmp) {

        if (tmp.getParams().size() > 0) {
            /*valuesAdditionalDataCpto = tmp.getParams().toArray(valuesAdditionalDataComp);
            valuesAdditionalDataCptoIndexes = tmp.getParams().toArray(valuesAdditionalDataCptoIndexes);*/
            valuesAdditionalDataCpto = tmp.getParams().toArray(new String[0]);
            valuesAdditionalDataCptoIndexes = tmp.getIndicesParams().toArray(new Integer[0]);
        }

        //System.out.println("values: " + Arrays.toString(valuesAdditionalDataCpto));
        //System.out.println("values: " + Arrays.toString(valuesAdditionalDataCptoIndexes));

    }

    public void eliminarConceptosLista() {
        for (ConceptoFactura tmp : this.conceptosAsignadosSelect) {
            this.conceptosAsignados.remove(tmp);
        }
        //NESESARIO?
        conceptosAsignadosSelect.clear();
        this.flgButtonEliminarDisabled = conceptosAsignados.isEmpty();
        ejecutaOperaciones();
    }


    /**
     * Hace unaa busqueda de entre los conceptosEmpresa disponibles, si no se
     * especifico el conceptoAgregar se muestran todos
     */
    public void buscarConcepto() {
        if (validaSeleccionEmisor()) {
            if (getBusCop() != null && !busCop.trim().equals("")) {
                setConceptosEmpresa(daoConFact.BusConceptoIdEmpresa(getBusCop().trim(), this.idEmpresa));
                //buscaParcialidades(this.idOrigen);// Por el momento lo desactivaremos
            } else {
                this.conceptosEmpresa = daoConFact.ListaConceptosIdEmpresa(this.idEmpresa);
                FacesContext.getCurrentInstance().addMessage("frmManual", new FacesMessage(FacesMessage.SEVERITY_INFO, "Es necesario especificar el nombre del concepto.", "Info"));
            }
        }
    }

    public void cargarInfoImpuestos(List<ImpuestoContainer> traslados,
                                    List<ImpuestoContainer> retenciones) {
        cptoTmpShldAdd = false;
        initImpuestosConcepto();
        //001-ISR rets
        //002-IVA tras rets
        //003-IEPS tras

        for (ImpuestoContainer ttrs : traslados) {
            if (ttrs.getImpuesto().contains(IVA_CODE)) {
                ivaTrasladado.loadData(ttrs.getImpuesto(), ttrs.getBase(), ttrs.getTipoFactor(), ttrs.getTasaOcuota(), TipoImpuesto.TRASLADO);

            }
            if (ttrs.getImpuesto().contains(IEPS_CODE)) {
                iepsTrasladado.loadData(ttrs.getImpuesto(), ttrs.getBase(), ttrs.getTipoFactor(), ttrs.getTasaOcuota(), TipoImpuesto.TRASLADO);
            }
        }
        for (ImpuestoContainer trts : retenciones) {
            if (trts.getImpuesto().contains(ISR_CODE)) {
                isrRetenido.loadData(trts.getImpuesto(), trts.getBase(), trts.getTipoFactor(), trts.getTasaOcuota(), TipoImpuesto.RETENCION);
            }
            if (trts.getImpuesto().contains(IVA_CODE)) {
                ivaRetenido.loadData(trts.getImpuesto(), trts.getBase(), trts.getTipoFactor(), trts.getTasaOcuota(), TipoImpuesto.RETENCION);
            }
        }
    }

    private void cargarInfoComercioExterior(ConceptoFactura tmp) {

    }

    public void handleMetodoDePagoChange() {
        if (metodoPago.contains(PPDSTR)) {
            this.sendFacesMessage(null, FacesMessage.SEVERITY_INFO, "Cuando el campo 'Metodo de Pago' es 'Pago en Parcialidades o Diferido', " +
                    "la 'Forma de Pago' del comprobante debe ser la clave 99", "");
            formaPago = "99-Por definir";
            if (moneda.contains("XXX")) {
                this.sendFacesMessage(null, FacesMessage.SEVERITY_INFO,
                        "La moneda de ", "");
            }
        } else {
            formaPago = "";
        }
    }

    public void handleChangeTipoRelacion() {
        tipoDocObjFact = LambdasHelper.findTipoDocById(tiposDocs, idTipoDoc);
        if (tipoDocObjFact != null) {
            if (tipoDocObjFact.getcTipoComprobanteByTdocId().getClave().equalsIgnoreCase("I")) {

            } else if (tipoDocObjFact.getcTipoComprobanteByTdocId().getClave().equalsIgnoreCase("E")) {

            } else if (tipoDocObjFact.getcTipoComprobanteByTdocId().getClave().equalsIgnoreCase("T")) {
                if (tipoRelacion != null && !tipoRelacion.isEmpty()) {
                    if (!tipoRelacion.startsWith("05")) {
                        tipoRelacion = "";
                        this.sendFacesMessage(null, FacesMessage.SEVERITY_WARN,
                                "Cuando el tipo de documento es de tipo traslado el tipo de relacion debe estar" +
                                        " vacio o debe ser la clave '05-Traslados de mercancias facturados previamente'",
                                "");
                    }
                }
            }
        }
    }

    public void handleOnTipoDocChange() {
        // TODO: 17/01/2018 MANEJAR COMERCIO EXTERIOR SOLO PARA INGRESO
        //disponibleComercioExterior = false;
        condiciones = "";
        //formaPago = "Pago en una sola exhibición";
        //metodoPago = "NO IDENTIFICADO";

        formaPago = "-1";
        metodoPago = "-1";
        disabledInputCondicionesDePago = false;
        disabledInputFormaDePago = false;
        disabledInputMetodoDePago = false;
        disabledFieldSetImpuestosDetalleConcepto = false;
        disabledInputDescuentoDetalleConcepto = false;
        disabledInputValorUnitarioDetalleConcepto = false;

        tipoDocObjFact = LambdasHelper.findTipoDocById(tiposDocs, idTipoDoc);
        if (tipoDocObjFact != null) {
            if (tipoDocObjFact.getcTipoComprobanteByTdocId().getValoMaximo() != null
                    && !tipoDocObjFact.getcTipoComprobanteByTdocId().getValoMaximo().isEmpty()) {
                try {
                    maxValueImporte = new BigDecimal(
                            tipoDocObjFact.
                                    getcTipoComprobanteByTdocId().
                                    getValoMaximo().
                                    replace(",", ""));
                } catch (java.lang.NumberFormatException e) {
                    maxValueImporte = new BigDecimal(MAXDEFVALUE);
                }
                if (maxValueImporte.compareTo(BigDecimal.ZERO) != EQUALS &&
                        maxValueImporte.compareTo(BigDecimal.ZERO) == FIRSTGREATER) {
                    maxValueImporte = new BigDecimal(MAXDEFVALUE);
                }
            } else {
                ///Set default
                maxValueImporte = new BigDecimal(MAXDEFVALUE);
            }

            //TODO recalcular los montos de la factura deacuerdo al tipo de documento

            if (tipoDocObjFact.getcTipoComprobanteByTdocId().getClave().equalsIgnoreCase(TRASLADO_CLAVE)) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "Cuando es un comprobante de Tipo 'Traslado' los campos 'Condiciones de Pago'," +
                                        "'Forma de Pago', 'Metodo de pago','Descuento' en los conceptos y ademas del nodo" +
                                        " 'Impuestos' seran omitidos." +
                                        "No es necesario que usted los llene",
                                ""));
                condiciones = "";
                // formaPago = "Pago en una sola exhibición";
                // metodoPago = "NO IDENTIFICADO";
                formaPago = "-1";
                metodoPago = "-1";
                disabledInputCondicionesDePago = true;
                disabledInputFormaDePago = true;
                disabledInputMetodoDePago = true;
                disabledFieldSetImpuestosDetalleConcepto = true;
                disabledInputDescuentoDetalleConcepto = true;
                disabledInputValorUnitarioDetalleConcepto = true;
                //disponibleComercioExterior = false;
                //flagsEntradasComercioExterior.changeRequiredInputs(TRASLADO_CLAVE);
            } /*else if (tipoDocObjFact.getcTipoComprobanteByTdocId().getClave().equalsIgnoreCase(FACTURA_CLAVE)) {
                disponibleComercioExterior = true;
                flagsEntradasComercioExterior.changeRequiredInputs(FACTURA_CLAVE);
            } else if (tipoDocObjFact.getcTipoComprobanteByTdocId().getClave().equalsIgnoreCase(NOTA_CREDITO_CLAVE)) {
                disponibleComercioExterior = false;
                flagsEntradasComercioExterior.changeRequiredInputs(NOTA_CREDITO_CLAVE);
            }*/
            this.ejecutaOperaciones();
        }
    }

    private void sendFacesMessage(String target, FacesMessage.Severity severity, String tittle, String detail) {
        this.sendFacesMessage(target, severity, tittle, detail, false);
    }

    private void sendFacesMessage(String target, FacesMessage.Severity severity, String tittle, String detail, boolean keepFlash) {
        FacesContext.getCurrentInstance().addMessage(target, new FacesMessage(severity, tittle, detail));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(keepFlash);
    }

    public void handleCodigoConfirmacionChange() {
        if (codConfRequerido) {
            if (codigoConfirmacion != null && codigoConfirmacion.length() != 5) {
                inputCodigoConfirmacion.setValid(true);
            } else {
                inputCodigoConfirmacion.setValid(false);
            }
        }
    }

    /**
     * Agrega documentos relaciondados para cuando se genera una nota de credito por ejemplo
     */
    public void agregarUuidRelacionado() {
        if (uuidTmp != null && !uuidTmp.isEmpty() && uuidTmp.trim().length() == 36) {
            uuidsRels.add(uuidTmp);
            uuidTmp = "";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Especifique un UUID valido.", "Info"));
        }

        if (uuidsRels.isEmpty()) {
            flgBttonEliminarUuid = true;
        } else {
            flgBttonEliminarUuid = false;
        }
    }

    public void eliminarUuidRelacionados() {
        for (String tmp : uuidsRelsSelection) {
            uuidsRels.remove(tmp);
        }
        uuidsRelsSelection.clear();
        if (uuidsRels.isEmpty()) {
            flgBttonEliminarUuid = true;
        } else {
            flgBttonEliminarUuid = false;
        }
    }

    /**
     * Generar la factura elaborada manualmente Llamar al ws de facturacion en
     * ebs
     */
    public void generarFactura() {
        boolean resOperation = false;
        if (preValidationFactura()) {//PERFORM PRE VALIDATIONS
            List<Concepto> listaConceptoData = new ArrayList();
            comprobanteData = new ComprobanteData();
            comprobanteData.setDatosComprobante(new DatosComprobanteData());
            capturaManualModelo = new CapturaManualModelo();
            //Cargamos emisor,receptor(cliente) y folio
            setEmisor(daoEmp.BuscarEmpresaId(this.getIdEmpresa()));
            receptor = daoRec.BuscarReceptorIdr(this.getIdCliente());
            folio = daoFolio.BuscarFolioId(idFolio);

            if (conceptosAsignados != null && !conceptosAsignados.isEmpty()) {
                for (ConceptoFactura tmp : conceptosAsignados) {
                    ConceptoData cptoData = new ConceptoData();
                    cptoData.setCantidad(tmp.getCantidad());
                    cptoData.setImporte(tmp.getTotalFormatted());
                    cptoData.setDescripcion(tmp.getConceptofacturacion());
                    cptoData.setValorUnitario(tmp.getValorUnitario());
                    cptoData.setNoIdentificacion(tmp.getClaveconcepto());
                    cptoData.setUnidad(tmp.getUnidad());
                    String[] arrProdServ = tmp.getClaveProdServ().split("-");
                    String[] arrUnidad = tmp.getClaveUnidad().split("-");
                    cptoData.setClaveProdServ(new CatalogoData(arrProdServ[0], arrProdServ[1]));
                    cptoData.setClaveUnidad(new CatalogoData(arrUnidad[0], arrUnidad[1]));
                    int max = tmp.getParams().size();
                    if (max > 0) {
                        max = tmp.getIndicesParams().get(0);
                    }
                    if (tmp.getNoCuentaPredial() != null && !tmp.getNoCuentaPredial().trim().isEmpty()) {
                        CuentaPredialData cuentaPredialData = new CuentaPredialData();
                        cuentaPredialData.setNumero(tmp.getNoCuentaPredial());
                        cptoData.setCuentaPredial(cuentaPredialData);
                    }

                    //Pedimento v33
                    if (tmp.getPedimento() != null && !tmp.getPedimento().trim().isEmpty()) {
                        InformacionAduaneraData informacion = new InformacionAduaneraData();
                        informacion.setNumero(tmp.getPedimento());
                        List<InformacionAduanera> listInfo = new ArrayList<>();
                        listInfo.add(informacion);
                        cptoData.setInformacionAduanera(listInfo);
                    }

                    ///Buscamos el indice maximo
                    for (Integer tempIdx : tmp.getIndicesParams()) {
                        if (max < tempIdx) {
                            max = tempIdx;
                        }
                    }
                    String[] addFiltereds = new String[max + 1];
                    for (int i = 0; i < tmp.getParams().size(); i++) {
                        addFiltereds[tmp.getIndicesParams().get(i)] = tmp.getParams().get(i);
                    }
                    cptoData.setAdditionalColumns(Arrays.asList(addFiltereds));
                    ///Deacuerdo al tipo de comprobante
                    if (tipoDocObjFact.getcTipoComprobanteByTdocId().getClave().equalsIgnoreCase("T")) {
                        cptoData.setDescuento(null);
                    } else if (tipoDocObjFact.getcTipoComprobanteByTdocId().getClave().equalsIgnoreCase("I")) {
                        ///Atributos concepto
                        cptoData = agregarAtributosCptoFact(tmp, cptoData);
                        ///Impuestos del concepto
                        agregarImpuestosCptoFact(tmp, cptoData);
                    } else if (tipoDocObjFact.getcTipoComprobanteByTdocId().getClave().equalsIgnoreCase("E")) {
                        ///Atributos concepto
                        cptoData = agregarAtributosCptoFact(tmp, cptoData);
                        ///Impuestos del concepto
                        agregarImpuestosCptoFact(tmp, cptoData);
                    }
                    listaConceptoData.add(cptoData);
                }
                comprobanteData.setConceptos(listaConceptoData);
            }

            ///------------------------ADDITIONAL DATA
            comprobanteData.setAdditional(new AdditionalData());


            ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setTipoDeComprobante(
                    new CatalogoData(tipoDocObjFact.getcTipoComprobanteByTdocId().getClave(),
                            tipoDocObjFact.getcTipoComprobanteByTdocId().getDescripcion())
            );
            ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setTotal(total);
            ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setSubTotal(subTotal);

            if (tipoRelacion != null && !tipoRelacion.isEmpty() && tipoRelacion.contains("-")) {
                CfdiRelacionadosData cfdisrel = new CfdiRelacionadosData();
                List<CfdiRelacionado> myawlist = new ArrayList<>();
                for (String udis : uuidsRels) {
                    CfdiRelacionadoData cfdireltmp = new CfdiRelacionadoData();
                    cfdireltmp.setUuid(udis);
                    myawlist.add(cfdireltmp);
                }
                String[] arrtiporelacion = tipoRelacion.split("-");
                CatalogoData tipoRelacionCat = new CatalogoData(arrtiporelacion[0], arrtiporelacion[1]);
                cfdisrel.setTipoRelacion(tipoRelacionCat);
                cfdisrel.setCfdiRelacionado(myawlist);
                comprobanteData.setCfdiRelacionados(cfdisrel);
            }

            //Lugar de Expecidion
            String[] arlugexp = codigoPostal.split("-");
            CatalogoData lugarExpedicion = new CatalogoData(arlugexp[0], arlugexp[1]);
            ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setLugarExpedicion(lugarExpedicion);

            // TODO: 05/09/2017 REALIZAR LAS VALIDACIONES AL CFDI DEACUERDO AL TIPO DE DOCUMENTO EN EL MODULO FACTURACION MANUAL SOLO SE PODRAN ELEGIR ENTRE INGRESO, EGRESO, TRASLADO
            // TODO: 05/09/2017 EL RECIBO DE NOMINA ESTA MANEJADO EN  OTRO SISTEMA Y EL TIPO DE COMPROBANTE DE PAGO ESTARA EN  LA PARTE DE COMPLEMENTO DE PAGOS
            //CREAR UNA NUEVA TABLA CON EL MAPEO DE TIPO DE DOCUMENTO Y EL CODIGO Y EL TIPO DE COMPROBANTE
            //-> FACTURA INGRESO, I,
            //-> NOTA DE CREDITO EGREO, E,
            //-> TRASLADO, TRASLADO,T
            //-> NOTA DE CARGO INGRESO, I,
            //-> HONORARIOS, INGRESO,I
            //Cfdis relacionados

            String[] myawarr = null;

            myawarr = moneda.split("-");
            CatalogoData cmoneda = new CatalogoData(myawarr[0], myawarr[1]);
            myawarr = regimenFiscal.split("-");
            CatalogoData cregimenfiscal = new CatalogoData(myawarr[0], myawarr[1]);
            myawarr = usoCfdi.split("-");
            CatalogoData cusocfdi = new CatalogoData(myawarr[0], myawarr[1]);

            comprobanteData = capturaManualModelo.cargaDatosComprobante(
                    comprobanteData,
                    emisor,
                    receptor,
                    comentarios,
                    folio,
                    cmoneda,
                    cregimenfiscal,
                    cusocfdi,
                    esReceptorExtranjero,
                    residenciaFiscal,
                    numRegIdTrib);
            ///String[] datosCliente = new String[3];
            ///datosCliente[1] = refCliente != null ? refCliente : "";
            ///datosCliente[2] = "|||" + noCliente != null ? noCliente : "";
            ///Cuando sea el CFDI que resume el total de la factura ,
            ///Continuar con las validaciones normales

            ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setTipoDocumento(
                    tipoDocObjFact.getcTipoComprobanteByTdocId().getDescripcion());
            if (!moneda.toUpperCase().contains(XXXSTR) && !moneda.toUpperCase().contains(MXNSTR))
                ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setTipoDeCambio(tipoCambioDatosFacturaInput);

            if (codConfRequerido)
                ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setConfirmacion(codigoConfirmacion);

            int max = valuesAdditionalDataComp.length;
            if (max > 0) {
                max = valuesAdditionalDataCompIndexes[0];
            }
            ///Buscamos el indice maximo
            for (int ij = 0; ij < valuesAdditionalDataCompIndexes.length; ij++) {
                if (max < valuesAdditionalDataCompIndexes[ij]) {
                    max = valuesAdditionalDataCompIndexes[ij];
                }
            }
            String[] addFilteredsCfdi = new String[max + 1];///Info
            for (int i = 0; i < valuesAdditionalDataComp.length; i++) {
                addFilteredsCfdi[valuesAdditionalDataCompIndexes[i]] = valuesAdditionalDataComp[i];
            }
            ((AdditionalData) getComprobanteData().getAdditional()).setParam(addFilteredsCfdi);

            ///Agregamos los campos deacuardo al tipo de documento
            if (tipoDocObjFact.getcTipoComprobanteByTdocId().getClave().equalsIgnoreCase("T")) {
                ///No agregar nodos y atributos
                ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setDescuento(null);
                ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setCondicionesDePago(null);
                ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setFormaDePago(null);
                ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setMetodoDePago(null);
                comprobanteData.setImpuestos(null);

                // en caso de ser comercio exterior

                if (usarComplementoComercioExterior) {
                    codConfRequerido = false;
                    ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setTotal(0.00);
                    ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setSubTotal(0.00);
                    System.out.println("valida traslado comercio exterior");
                }

            } else if (tipoDocObjFact.getcTipoComprobanteByTdocId().getClave().equalsIgnoreCase("I")) {
                agregarImpuestosComprobante();
                agregarAtributosComprobante();
            } else if (tipoDocObjFact.getcTipoComprobanteByTdocId().getClave().equalsIgnoreCase("E")) {
                agregarImpuestosComprobante();
                agregarAtributosComprobante();
            }

            ///=========================================================================================================
            /// COMPLEMENTO DE COMERCIO EXTERIOR
            ///=========================================================================================================
            if (usarComplementoComercioExterior) {
                System.out.println("Timbra complemento exterior");
                handleChangeTipoCambioUSD();
                transformarConceptosAMercancias();
                addComplementoComercioExterior(getComprobanteData());
            }


            ((AdditionalData) getComprobanteData().getAdditional()).setPlantilla(PLANTILLAGRAL);//PLANTILLA CFDIV33

            agregarEmailCliente();

            //agregaAddendas();
            if (usarComplementoAdendaVW || usarComplementoAdendaAudi) {
                System.out.println("Addendas");
                transformarConceptosAProdServicios();
                addAddenda(getComprobanteData());
            }
            PintarLog.println("Apunto de llamar al servicio de factura manual");
            String respuestaServicio = "";
            MEmpresaMTimbre m = daoEmpTimp.ObtenerClaveWSEmpresaTimbre(idEmpresa);
            if (m != null) {
                respuestaServicio = new ClienteFacturaManual().exeGenFactura(getComprobanteData(), m.getClaveWS(), ambiente, DEBUG);
                //PintarLog.println("respuestaServicio:" + respuestaServicio);
                if (checkRespuestaServicio(respuestaServicio)) {
                    this.reset();
                    FacesContext.getCurrentInstance().addMessage("frmManual", new FacesMessage(FacesMessage.SEVERITY_INFO, "La factura se genero correctamente.", "Error"));
                    resOperation = true;//Bandera para ejecutar el otro metodo del otro ben
                } else {
                    //Show all to user
                    for (String mssg : respuestas) {
                        FacesContext.getCurrentInstance().addMessage("frmManual", new FacesMessage(FacesMessage.SEVERITY_ERROR, mssg, "Error"));
                    }
                }
            } else {
                FacesContext.getCurrentInstance().addMessage("frmManual", new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo obtener las claves de acceso para el timbrado del emisor.", "Error"));
            }
        }
        //Enviamos respuesta de procesamiento de la factura
        RequestContext.getCurrentInstance().addCallbackParam("succes", resOperation);
    }

    private void agregarEmailCliente() {
        if (enviarEmailCliente) {
            ((AdditionalData) getComprobanteData().getAdditional()).setCorreo(emailCliente);
        }
    }

    private ConceptoData agregarAtributosCptoFact(ConceptoFactura tmp, ConceptoData cptoData) {
        if (montoDescuento > 0) {
            cptoData.setDescuento(tmp.getDescuento());
        } else {
            cptoData.setDescuento(null);
        }
        return cptoData;
    }

    /**
     * Agrega los impuestos de tmp a cptoData
     *
     * @param tmp
     * @param cptoData
     */
    private void agregarImpuestosCptoFact(ConceptoFactura tmp, ConceptoData cptoData) {

        ImpuestosConceptoData imp = new ImpuestosConceptoData();
        List<RetencionConcepto> listRetencionesCpto = new ArrayList<>();
        List<TrasladoConcepto> listTrasladodsCpto = new ArrayList<>();

        //Retenciones
        for (ImpuestoContainer i : tmp.getRetenciones()) {
            RetencionConceptoData rcd = new RetencionConceptoData();
            rcd.setBase(Double.parseDouble(i.getBase()));
            rcd.setImporte(Double.parseDouble(i.getImporte()));
            CatalogoData ci = new CatalogoData(i.getImpuesto(), i.getDescripcionImpuesto());
            rcd.setImpuesto(ci);
            rcd.setTipoFactor(i.getTipoFactor());
            rcd.setTasaOCuota(Float.parseFloat(i.getTasaOcuota()));
            listRetencionesCpto.add(rcd);
        }

        //Traslados
        for (ImpuestoContainer i : tmp.getTraslados()) {
            TrasladoConceptoData tcd = new TrasladoConceptoData();
            tcd.setBase(Double.parseDouble(i.getBase()));
            CatalogoData ci = new CatalogoData(i.getImpuesto(), i.getDescripcionImpuesto());
            tcd.setImpuesto(ci);
            tcd.setTipoFactor(i.getTipoFactor());
            if (i.getTipoFactor().equalsIgnoreCase(IMP_EXENTO)) {
                tcd.setTasaOCuota(null);
                tcd.setImporte(null);
            } else {
                CatalogoData ctc = new CatalogoData(i.getTasaOcuota(), i.getTasaOcuota());
                tcd.setTasaOCuota(Double.valueOf(i.getTasaOcuota()));
                tcd.setImporte(Double.valueOf(i.getImporte()));
            }
            listTrasladodsCpto.add(tcd);
        }

        if (listRetencionesCpto.size() > 0)
            imp.setRetenciones(listRetencionesCpto);
        if (listTrasladodsCpto.size() > 0)
            imp.setTraslados(listTrasladodsCpto);

        if (listRetencionesCpto.isEmpty() && listTrasladodsCpto.isEmpty()) {
            cptoData.setImpuestosConcepto(null);
        } else {
            cptoData.setImpuestosConcepto(imp);
        }
    }


    /**
     * Agrega los impuesots al comprobante, agrupados por impuesto/tipoFactor/TasaOCuota
     */
    private void agregarImpuestosComprobante() {
        ImpuestosData impComp = new ImpuestosData();
        List<Retencion> listRetencionesComp = new ArrayList<>();
        List<Traslado> listTrasladosComp = new ArrayList<>();

        for (ImpuestoContainer i : retencionesFact) {
            RetencionData rcd = new RetencionData();
            rcd.setImporte(Double.parseDouble(i.getImporte()));
            CatalogoData ci = new CatalogoData(i.getImpuesto(), i.getDescripcionImpuesto());
            rcd.setImpuesto(ci);
            listRetencionesComp.add(rcd);
        }

        for (ImpuestoContainer i : trasladosFact) {
            TrasladoData tcd = new TrasladoData();
            tcd.setImporte(Double.parseDouble(i.getImporte()));
            CatalogoData ci = new CatalogoData(i.getImpuesto(), i.getDescripcionImpuesto());
            tcd.setImpuesto(ci);
            if (i.getTipoFactor().equalsIgnoreCase(IMP_EXENTO)) {
                tcd.setImporte(null);
                tcd.setTasaOCuota(null);
            } else {
                tcd.setTasaOCuota(Double.parseDouble(i.getTasaOcuota()));
            }
            tcd.setTipoFactor(i.getTipoFactor());
            listTrasladosComp.add(tcd);
        }

        if (listTrasladosComp.size() > 0) {
            impComp.setTraslados(listTrasladosComp);
            impComp.setTotalTraslados(this.totalTranfer);
        }
        if (listRetencionesComp.size() > 0) {
            impComp.setRetenciones(listRetencionesComp);
            impComp.setTotalRetenciones(this.totalReten);
        }

        if (this.totalReten >= 0.0) {
            impComp.setTotalRetenciones(this.totalReten);
        }

        if (this.totalTranfer >= 0.0) {
            impComp.setTotalTraslados(this.totalTranfer);
        }
        comprobanteData.setImpuestos(impComp);
    }

    /**
     * Cuando el comprobante no es de tipo T
     */
    private void agregarAtributosComprobante() {
        String[] myawarr = metodoPago.split("-");
        CatalogoData cmetodoPago = new CatalogoData(myawarr[0], myawarr[1]);
        myawarr = formaPago.split("-");
        CatalogoData cformaPago = new CatalogoData(myawarr[0], myawarr[1]);
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setFormaDePago(cformaPago);
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setMetodoDePago(cmetodoPago);
        if (montoDescuento > 0)
            ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setDescuento(montoDescuento);
        else
            ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setDescuento(null);
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setCondicionesDePago(condiciones);
    }


    private String getUiid(String str) {
        String uuid = null;
        Matcher mat = Pattern.compile("UUID=\".{36}\"").matcher(str);
        if (mat.find()) {
            uuid = mat.group().substring(6, 42);
        }

        return uuid;
    }

    private boolean checkRespuestaServicio(String arg) {
        respuestas.clear();
        boolean res = true;
        if (arg != null && !arg.isEmpty() && !arg.contains("null") && !arg.contains("NULL") && !arg.contains("Null")) {
            String uuidNuevo = "";
            Pattern p1 = Pattern.compile(UUIDREGEXPATT);
            Pattern p2 = Pattern.compile(ATEBREGEXPATT);
            Matcher matcher1 = p1.matcher(arg);
            Matcher matcher2 = p2.matcher(arg);
            if (matcher1.find()) {
                uuidNuevo = matcher1.group(0);
            } else if (matcher2.find()) {
                uuidNuevo = matcher2.group(0);
            } else {
                uuidNuevo = "";
            }
            if (uuidNuevo != null && !uuidNuevo.isEmpty() && uuidNuevo.length() == 36) {

                if (uuidNuevo.matches(UUIDREGEXPATT) ||
                        uuidNuevo.matches(ATEBREGEXPATT)) {
                    System.out.println("UUID->" + uuidNuevo);
                    //ok
                } else {
                    res = false;
                    respuestas.add("El UUID obtenido no es valido" + uuidNuevo);
                }
            } else {
                res = false;
                // TODO: 19/07/2017  BUSCAR CODIGO DE ERROR EN EL XML DE RESPUESTA
                if (arg.length() > 400) {
                    respuestas.add("{" + arg.substring(127, 391) + "}"); //Obtenemo un substring
                } else {
                    respuestas.add(arg);
                }
            }
        } else {
            respuestas.add("Ocurrio un error al procesar la factura, no se obtuvo respuesta del servidor");
            res = false;
        }
        return res;
    }


    public void cambiarDecimalesRequeridos() {
        disabledInputTipoCambio = false;
        String[] tmpMon = moneda.split("-");
        if (tmpMon[0].equalsIgnoreCase("MXN") || tmpMon[0].equalsIgnoreCase("XXX")) {
            tipoCambioActualBackend = 1.0;
            disabledInputTipoCambio = true;
        }
        MCmoneda tmp = daoMoneda.getUniqueByCode(tmpMon[0]);
        if (tmp != null) {
            decimalesRequeridos = tmp.getDecimales();
            porcentajeVariacion = tmp.getPorcentajeVariacion();
            tipoCambioActualBackend = FloatsNumbersUtil.round(tmp.getTipoCambio(), 6);
        } else {
            decimalesRequeridos = 2;
            porcentajeVariacion = .0;
            tipoCambioActualBackend = 1.0;
        }
        validaMontoTipoCambio();
    }

    /**
     * Validaciones previas antes de construir la factura
     *
     * @return
     */
    public boolean preValidationFactura() {
        boolean res = true;//All OK
        if (idTipoDoc <= 0) {
            FacesContext.getCurrentInstance().addMessage("frmManual", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Es necesario que seleccione el tipo de documento", "info"));
            res = false;
        } else {
            res = validarDocumentoPorTipoDoc();
        }
        if (conceptosAsignados == null || conceptosAsignados.size() == 0) {
            FacesContext.getCurrentInstance().addMessage("frmManual", new FacesMessage(FacesMessage.SEVERITY_WARN, "Por favor agregue al menos un concepto a la factura.", "Info"));
            res = false;
        }
        // TODO: 31/05/2017 validar los UUIDS relacionados sol cuando sea una nota de credito o un comprobante en el que se requiera un uuid relacionado
        if (!validarUUIDs()) {
            //FacesContext.getCurrentInstance().addMessage("frmManual", new FacesMessage(FacesMessage.SEVERITY_WARN, "UUIDs relacionados invalidos.", "Info"));
            res = false;
        }
        // TODO: 31/05/2017 validar el tipo de cambio cuando la moneda sea diferente de MXN y XXX
        if (!checkValorTipoCambio()) {
            FacesContext.getCurrentInstance().addMessage("frmManual", new FacesMessage(FacesMessage.SEVERITY_WARN, "El tipo de cambio debe ser diferente de 1.00 cuando la moneda sea diferente" +
                    "de MXN o XXX.", "Info"));
            inputTipoCambio.setValid(false);// TODO: 19/07/2017 CREAR VALIDADORES PARA CADA VALIDACION EN ESPECIFICA Y QUE PUEDAN SER VALIDADAS APARTE
            res = false;
        }

        if (enviarEmailCliente) {
            if (emailCliente == null || emailCliente.isEmpty() || !emailCliente.contains("@")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "Por favor agregue uno o varios emails de envio separados por (;).", ""));
                res = false;
            }
        }

        if (esReceptorExtranjero) {
            if (residenciaFiscal == null || residenciaFiscal.isEmpty()) {
                inputResidenciaFiscal.setValid(false);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Por favor seleccione la residencia fiscal.", ""));
                res = false;
            }
            if (numRegIdTrib == null || numRegIdTrib.isEmpty()) {
                inputNumRegIdTrib.setValid(false);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Por favor ingrese el Num.Reg.Id.Trib.", ""));
                res = false;
            }
        }

        if (codConfRequerido) {
            if (codigoConfirmacion == null || codigoConfirmacion.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage("frmManual", new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "Ingrese un codigo de confirmacion valido", "Info"));
                res = false;
            }
        }
        return res;
    }

    private boolean validarDocumentoPorTipoDoc() {
        boolean res = true;
        if (tipoDocObjFact.getcTipoComprobanteByTdocId().getClave().equalsIgnoreCase("T")) {
            ///Hacer validaciones para traslados

        } else if (tipoDocObjFact.getcTipoComprobanteByTdocId().getClave().equalsIgnoreCase("I")) {
            ///Hacer validaciones para ingresos
            res = validaMetodoFormaPago(res);

        } else if (tipoDocObjFact.getcTipoComprobanteByTdocId().getClave().equalsIgnoreCase("E")) {
            ///Hacer validaciones para egresos
            res = validaMetodoFormaPago(res);
        }
        return res;
    }

    private boolean validaMetodoFormaPago(boolean res) {
        if (formaPago == null || formaPago.isEmpty() || formaPago.equalsIgnoreCase("-1")) {
            res = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Seleccione una forma de pago", ""));
        }
        if (metodoPago == null || metodoPago.isEmpty() || metodoPago.equalsIgnoreCase("-1")) {
            res = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Seleccione un metodo de pago"
                    , ""));
        }
        return res;
    }

    private boolean validarUUIDs() {
        boolean res = true;
        if ((tipoRelacion == null || tipoRelacion.isEmpty() || !tipoRelacion.contains("-")) &&
                (uuidsRels != null && !uuidsRels.isEmpty())) {
            FacesContext.getCurrentInstance().addMessage("frmManual", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Debe seleccionar el tipo de relacion del comprobante para los CFDIs relacionados", "Info"));
            res = false;
        }

        if ((tipoRelacion != null && !tipoRelacion.isEmpty() && tipoRelacion.contains("-"))
                && (uuidsRels != null && uuidsRels.isEmpty())) {
            FacesContext.getCurrentInstance().addMessage("frmManual", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Debe agregar al menos un UUID cuando tenga seleccionado un tipo de relacion en CFDIs relacionados", "Info"));
            res = false;
        }
        return res;
    }

    private boolean checkValorTipoCambio() {
        boolean res = true;
        if (!moneda.toUpperCase().contains(MXNSTR)) {//Si es otra moneda
            if (tipoCambioDatosFacturaInput == 1) {//El valor del tipo de cambio debe ser diferente de 1.0
                res = false;
            }
        }
        return res;
    }

    /**
     * Valida si el importe de la factura no esta por encima de los limites permitidos para el tipo de COMPROBANTE
     */
    private void validarMaxTotal() {
        if (idTipoDoc > 0) {
            double tmpAmm = 0.0;
            codConfRequerido = false;
            inputCodigoConfirmacion.setValid(true);
            if (!moneda.toUpperCase().contains(MXNSTR) && !moneda.toUpperCase().contains(XXXSTR)) {
                tmpAmm = total * tipoCambioDatosFacturaInput;
            }
            int res = maxValueImporte.compareTo(new BigDecimal(tmpAmm));
            if (res == -1) {
                codConfRequerido = true;
               /* if(usarComplementoComercioExterior && tipoDocObjFact.getcTipoComprobanteByTdocId().getClave().equalsIgnoreCase("T")){
                    codConfRequerido=false;
                    System.out.println("omitir codigo");
                }else{
                    codConfRequerido = true;
                    System.out.println("No omitir codigo");
                }*/

                if (((String) (inputCodigoConfirmacion.getValue())).length() != LENGTHCODCONF) {

                    inputCodigoConfirmacion.setValid(false);

                  /*  if(usarComplementoComercioExterior && tipoDocObjFact.getcTipoComprobanteByTdocId().getClave().equalsIgnoreCase("T")){
                        inputCodigoConfirmacion.setValid(true);
                        System.out.println("omitir codigo");

                    }else{
                        inputCodigoConfirmacion.setValid(false);
                        System.out.println("No omitir codigo");
                    }*/

                }
                FacesContext.getCurrentInstance().addMessage("frmManual", new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "Debe proporcionar el codigo de confirmación ya que el importe de la factura sobrepasa los limites permitidos.", "Info"));

            }
        }
    }

    public void validaMontoTipoCambio() {
        if (porcentajeVariacion != null) {
            codConfRequerido = false;
            inputTipoCambio.setValid(true);
            inputCodigoConfirmacion.setValid(true);
            if (moneda.contains("-")) {
                if (porcentajeVariacion != null && porcentajeVariacion > 0 && tipoCambioActualBackend > 0) {

                    limiteSuperior = tipoCambioActualBackend * (1 + porcentajeVariacion);
                    limiteInferior = tipoCambioActualBackend * (1 - porcentajeVariacion);
                    if (tipoCambioDatosFacturaInput < 0 || tipoCambioDatosFacturaInput < limiteInferior || tipoCambioDatosFacturaInput > limiteSuperior) {
                        FacesContext.getCurrentInstance().addMessage("frmManual", new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "El tipo de cambio esta fuera del rango permitido, debe ingresar el codigo de confirmación o modificar el valor del tipo de cambio. ", "Info"));

                        /*if(usarComplementoComercioExterior && tipoDocObjFact.getcTipoComprobanteByTdocId().getClave().equalsIgnoreCase("T")){
                            codConfRequerido=false;
                            inputCodigoConfirmacion.setValid(true);
                            inputTipoCambio.setValid(true);
                            System.out.println("omitir codigo");
                        }else{
                            codConfRequerido = true;
                            inputTipoCambio.setValid(false);
                            System.out.println("no omitir codigo");
                        }*/

                        inputTipoCambio.setValid(false);
                        codConfRequerido = true;
                        if (((String) (inputCodigoConfirmacion.getValue())).length() != LENGTHCODCONF) {
                            /*if(usarComplementoComercioExterior && tipoDocObjFact.getcTipoComprobanteByTdocId().getClave().equalsIgnoreCase("T")){
                                inputCodigoConfirmacion.setValid(true);
                                System.out.println("omitir codigo");
                            }else{
                                inputCodigoConfirmacion.setValid(false);
                                System.out.println("No omitir codigo");
                            }*/

                            inputCodigoConfirmacion.setValid(false);

                        }
                    }
                }
            }
        }
    }

    public boolean validaSeleccionEmisor() {
        if (this.idEmpresa != -1) {
            return true;
        } else {
            FacesContext.getCurrentInstance().addMessage("frmManual", new FacesMessage(FacesMessage.SEVERITY_INFO, "Debe seleccionar la empresa emisora.", "Info"));
            return false;
        }
    }

    public void actualizaDatoPorEmpresaEmisora() {
        if (idEmpresa != -1) {
            this.receptores = daoRec.ListaReceptoresporIdemp(this.idEmpresa);
            this.conceptosEmpresa = daoConFact.ListaConceptosIdEmpresa(this.idEmpresa);
            this.Folios = daoFolio.ListaFoliosidEmpresa(this.idEmpresa);
            CodigoPostalDAO daoCp = new CodigoPostalDAO();
            MEmpresa emisor = daoEmp.BuscarEmpresaId(idEmpresa);
            if (emisor.getDireccion() != null) {
                if (emisor.getDireccion().getCp() != null && !emisor.getDireccion().getCp().isEmpty()) {
                    MCcodigopostal ecp = daoCp.getCP(emisor.getDireccion().getCp());
                    if (ecp != null) {
                        codigoPostal = ecp.getCodigoPostal() + "-" + ecp.getEstado();
                    }
                }
            } else {
                codigoPostal = "";
            }
            idCliente = -1;
            actualizaDatosReceptor(false);
            creaCamposAdicionalesComprobante();
        } else {
            idCliente = -1;
        }
    }

    public void actualizaDatosReceptor(boolean handle) {
        if (handle)
            handleChangeEsReceptorExtranjero();
        filtraEmpresaConAddenda();//Si filtran las empresas que tienen addenda
    }

    public void handleChangeReceptor() {
        esReceptorExtranjero = false;
        MReceptor mywmp = daoRec.BuscarReceptorIdr(idCliente);
        String rfcReceptor = mywmp.getRfcOrigen();
        if (rfcReceptor.contains(RFC_EXTRANJERO)) {
            esReceptorExtranjero = true;
        }
        emailCliente = mywmp.getCorreo();
        handleChangeEsReceptorExtranjero();
        filtraEmpresaConAddenda();
    }


    /**
     * Hace requerido los campos de residencia fiscal y numregidtributario
     */
    public void handleChangeEsReceptorExtranjero() {
        disponibleComercioExterior = false;
        requiredResidenciaFiscal = NO_REQUERIDO;
        requiredNumRegIdTrib = NO_REQUERIDO;
        if (esReceptorExtranjero) {
            disponibleComercioExterior = true;
            requiredResidenciaFiscal = REQUERIDO;
            requiredNumRegIdTrib = REQUERIDO;
            usoCfdi = "P01-Por definir";///Cuando es un receptor EXTRANJERO EL USO DEBE SER P01
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Cuando el Receptor es extranjero el 'UsoCFDI' debe ser la clave 'P01-Por definir'", ""));
        } else {
            residenciaFiscal = "";
            numRegIdTrib = "";
            usoCfdi = "";
        }
        if (inputNumRegIdTrib != null)
            inputNumRegIdTrib.setValid(true);
        if (inputResidenciaFiscal != null)
            inputResidenciaFiscal.setValid(true);


        if (esReceptorExtranjero && (residenciaFiscal == null || residenciaFiscal.isEmpty()
                || numRegIdTrib == null || numRegIdTrib.isEmpty())) {
            if (inputNumRegIdTrib != null)
                inputNumRegIdTrib.setValid(true);
            if (inputResidenciaFiscal != null)
                inputResidenciaFiscal.setValid(true);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Cuando el receptor es extranjero es necesario que seleccione e ingrese la " +
                            "'Residencia fiscal' y el 'No. Reg. Id. Trib.'.", ""));
        }
    }


    public void handleChangeUsoCFDI() {
        if (esReceptorExtranjero) {
            if (usoCfdi != null && !usoCfdi.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "Cuando el Receptor es extranjero el 'UsoCFDI' debe corresponder a la clave 'P01-Por definir'", ""));
                usoCfdi = "P01-Por definir";///Cuando es un receptor EXTRANJERO
            }
        }
    }

    public void handleChangeResidenciaFiscal() {
        inputResidenciaFiscal.setValid(true);
        if (requiredResidenciaFiscal == REQUERIDO && (residenciaFiscal.isEmpty() || residenciaFiscal.contains("MEX"))) {
            inputResidenciaFiscal.setValid(false);
            residenciaFiscal = "";
            FacesContext.getCurrentInstance().addMessage(
                    null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Por favor seleccione la residencia fiscal del cliente extranjero diferente de MEX!!!", ""));
        }
    }

    public void handleChangeTipoAdenda() {

        String msge = "";
        System.out.println("Tipo adenda: " + tipoAdenda);
        adendaServicios = false;
        adendaServicios = false;

        if (tipoAdenda.equalsIgnoreCase("PROD")) {
            adendaProductos = true;
            adendaServicios = false;
            msge = "Producto";
        }
        if (tipoAdenda.equalsIgnoreCase("SERV")) {
            adendaServicios = true;
            adendaProductos = false;
            msge = "Servicio";
        }

        FacesContext.getCurrentInstance().addMessage(
                null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Ha seleccionado el tipo de adenda " + msge, ""));

    }

    public void handleChangeNumRegIdTrib() {
        inputNumRegIdTrib.setValid(true);
        //inputNumRegIdTrib.setRequired(true);
        if (requiredNumRegIdTrib == REQUERIDO && (numRegIdTrib.isEmpty())) {
            inputNumRegIdTrib.setValid(false);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "El campo NumRegIdTrib no puede estar vacio!!!", ""));
        }
    }

    public void handleChangeUsarComercioExterior() {
        System.out.println("Handle change comercio exterior flag if need");
    }

    public void handleChangeTipoCambioUSD() {
        ejecutaOperacionComercioExterior();
    }

    public void ejecutaOperacionComercioExterior() {
        singleComplementoComercioExteriorData.setTotalUSD(0.0);
        double tempTotalUsd = 0.0;
        for (ConceptoFactura tmp : this.conceptosAsignados) {
            tmp.setTipoCambioUsd(singleComplementoComercioExteriorData.getTipoCambioUSD());
            tmp.calcularMontoComercioExterior(tipoCambioDatosFacturaInput);
            tmp.calcularValorUnitarioAduana(tipoCambioDatosFacturaInput);
            tempTotalUsd += tmp.getValorDolares();
        }

        DecimalFormat df = new DecimalFormat("#.00");
        String number = df.format(tempTotalUsd);
        double tmpnum = Double.parseDouble(number);
        singleComplementoComercioExteriorData.setTotalUSD(tmpnum);
    }

    /**
     * Ejecuta todas las operaciones de la factura electronica
     * Las operaciones se realizan considerando el tipo de documento
     */
    public void ejecutaOperaciones() {
        ///ORDER IS IMPORTANT!!!
        calculaSubtotalAndDescuento();
        calculaTrasladosYRetencionesTotales();
        calculaTotal();
        //validarMaxTotal();

    }

    /**
     * Calcula la suma de los importes de los conceptos ademas de tambien la de los
     * descuentos
     */
    public void calculaSubtotalAndDescuento() {
        this.subTotal = 0.0;
        montoDescuento = 0.0;
        for (ConceptoFactura tmp : this.conceptosAsignados) {
            // this.subTotal += tmp.getTotalFormatted();
            if (idTipoDoc > 0) {
                if (tipoDocObjFact.getcTipoComprobanteByTdocId().getClave().equalsIgnoreCase("T")) {
                    //Operaciones espaciales para  los docs de traslados
                } else if (tipoDocObjFact.getcTipoComprobanteByTdocId().getClave().equalsIgnoreCase("I")) {
                    montoDescuento += tmp.getDescuento();
                    this.subTotal += tmp.getTotalFormatted();
                } else if (tipoDocObjFact.getcTipoComprobanteByTdocId().getClave().equalsIgnoreCase("E")) {
                    montoDescuento += tmp.getDescuento();
                    this.subTotal += tmp.getTotalFormatted();
                }
            }
        }
    }

    public void calculaTotal() {
        total = ((subTotal - montoDescuento) + totalTranfer) - totalReten;
    }

    /**
     * Calcula el resumen de los impuestos trasladados de la factura, agrupar por impuesto, tasa y cuota  en caso de que existan mas de uno
     */
    public void calculaTrasladosYRetencionesTotales() {
        //002-IVA
        //003-IEPS
        totalTranfer = 0.0;
        trasladosFact.clear();
        totalReten = 0.0;
        retencionesFact.clear();
        if (idTipoDoc > 0) {
            if (tipoDocObjFact.getcTipoComprobanteByTdocId().getClave().equalsIgnoreCase("T")) {
                //Hacer los cambios que se deseen
            } else if (tipoDocObjFact.getcTipoComprobanteByTdocId().getClave().equalsIgnoreCase("I")) {
                agruparImpuestosComprobante();
            } else if (tipoDocObjFact.getcTipoComprobanteByTdocId().getClave().equalsIgnoreCase("E")) {
                agruparImpuestosComprobante();
            }
        }
    }

    private void agruparImpuestosComprobante() {
        try {
            for (ConceptoFactura tmp : this.conceptosAsignados) {
                agrupaTraslados(tmp.getTraslados());
                agrupaRetenciones(tmp.getRetenciones());
            }
        } catch (NumberFormatException e1) {
            e1.printStackTrace(System.out);
        }
    }

    private void agrupaTraslados(List<ImpuestoContainer> args) {
        boolean match;
        double tasa1 = 0.0;
        double tasa2 = 0.0;

        for (ImpuestoContainer tmp : args) {
            try {
                tasa1 = Double.parseDouble(tmp.getTasaOcuota());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                tasa1 = 0.0;
            }
            match = false;
            totalTranfer += Double.parseDouble(tmp.getImporte());

            for (ImpuestoContainer impComp : trasladosFact) {
                try {
                    tasa2 = Double.parseDouble(impComp.getTasaOcuota());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    tasa2 = 0.0;
                }

                if (!impComp.getTipoFactor().equalsIgnoreCase(IMP_NA)) {
                    if (tmp.getImpuesto().equalsIgnoreCase(impComp.getImpuesto()) &&
                            tasa1 == tasa2 &&
                            tmp.getTipoFactor().equalsIgnoreCase(impComp.getTipoFactor())) {
                        if (impComp.getTipoFactor().equalsIgnoreCase(IMP_EXENTO)) {
                            impComp.setImporte(ZERO_STRING);
                            impComp.setTasaOcuota(ZERO_STRING);
                        } else {
                            impComp.setImporte(String.valueOf(
                                    Double.parseDouble(impComp.getImporte()) +
                                            Double.parseDouble(tmp.getImporte())));
                        }
                        match = true;
                        break;
                    }
                } else {
                    break;
                }
            }


            if (!match && !tmp.getTipoFactor().equalsIgnoreCase(IMP_NA)) {

                ImpuestoContainer nuevoImpuestoComp = new ImpuestoContainer(
                        tmp.getImpuesto(),
                        tmp.getBase(),
                        tmp.getTipoFactor(),
                        tmp.getTasaOcuota(),
                        TipoImpuesto.TRASLADO);
                nuevoImpuestoComp.setDescripcionImpuesto(tmp.getDescripcionImpuesto());
                nuevoImpuestoComp.calcularImpuesto();
                trasladosFact.add(nuevoImpuestoComp);
            }
        }
    }

    private void agrupaRetenciones(List<ImpuestoContainer> args) {
        boolean match;

        for (ImpuestoContainer tmp : args) {
            match = false;
            totalReten += Double.parseDouble(tmp.getImporte());//add importe
            for (ImpuestoContainer impComp : retencionesFact) {
                if (!impComp.getTipoFactor().equalsIgnoreCase(IMP_NA)) {
                    if (tmp.getImpuesto().equalsIgnoreCase(impComp.getImpuesto())) {
                        impComp.setImporte(String.valueOf(
                                Double.parseDouble(impComp.getImporte()) +
                                        Double.parseDouble(tmp.getImporte())));
                        match = true;
                        break;
                    }
                } else {
                    break;
                }
            }

            if (!match && !tmp.getTipoFactor().equalsIgnoreCase(IMP_NA)) {
                // TODO: 20/09/2017 INVESTIGAR SI CUANDO 
                ImpuestoContainer nuevoImpuestoComp = new ImpuestoContainer(tmp.getImpuesto(),
                        tmp.getBase(),
                        tmp.getTipoFactor(),
                        tmp.getTasaOcuota(),
                        TipoImpuesto.RETENCION);
                nuevoImpuestoComp.setDescripcionImpuesto(tmp.getDescripcionImpuesto());
                nuevoImpuestoComp.calcularImpuesto();
                retencionesFact.add(nuevoImpuestoComp);
            }
        }
    }

    private void agregaAddendas() {
        //Addenda coats
        if (RFC_CME.equals(comprobanteData.getReceptor().getRfc())) {
            if (ValidaProveedor(numeroProveedor) && ValidaOrdenCompra(ordenCompraInput)) {
                AddendaCoatsData addenda = new AddendaCoatsData();//Create addenda
                addenda.setNumeroProveedor(numeroProveedor);
                //Split  into string arrays
                if (ordenCompraInput.contains(",")) {
                    OrdenCompra = ordenCompraInput.split(",");
                } else {
                    OrdenCompra = new String[1];
                    OrdenCompra[0] = ordenCompraInput;
                }
                addenda.setOrdenCompra(OrdenCompra);
                //TODO TRABAJO POSPUESTO ADDENDAS, POSTERIORMENTE SE DECIDIRA SI SE TRABAJA CONTINUA O NO
                //List<AdendaFe> listaAdendas = new ArrayList<>();
                //listaAdendas.add(addenda);
                //comprobanteData.setAdendas(listaAdendas);
            }
        }
    }

    public void creaCamposAdicionalesCpto() {
        //test crea campos adicionales
        OutputLabel ol;
        InputText it;
        AjaxBehavior ajax;
        PanelGrid pg;
        valuesAdditionalDataCpto = new String[0];
        valuesAdditionalDataCptoIndexes = new Integer[0];
        if (fieldSetRefCpto != null)
            fieldSetRefCpto.getChildren().clear();
        paramsCpto = daoParamsFactMan.getAllAdditionalForCptoByEmp(idEmpresa);
        if (paramsCpto != null && paramsCpto.size() > 0) {
            pg = new PanelGrid();
            pg.setColumnClasses(PANELCPTOGRID);
            pg.setId("pgParamsCpto");
            pg.setColumns(COLUMNSPANELCTO);
            pg.setLayout("grid");
            pg.setStyleClass("ui-panelgrid-blank");
            pg.setStyle("width: 100%;");
            fieldSetRefCpto.getChildren().add(pg);

            int i = 0;
            valuesAdditionalDataCpto = new String[paramsCpto.size()];
            valuesAdditionalDataCptoIndexes = new Integer[paramsCpto.size()];
            for (MAddfieldsCpto tmp : paramsCpto) {
                //Add elements
                ol = new OutputLabel();
                it = new InputText();
                ajax = new AjaxBehavior();
                it.setId("idCpto_" + tmp.getParam());
                ajax.setProcess("@this,idCpto_" + tmp.getParam());
                ///it.addClientBehavior("keyup", ajax); El evento keyup no se lanza cuando se selecciona una cadena que recuerda el navegador
                it.addClientBehavior("change", ajax);
                //Add data
                ol.setValue(tmp.getParamdesc());
                it.setValueExpression("value", createValueExpression("#{managedBeanFacturacionManual.valuesAdditionalDataCpto['" + i + "']}", String.class));
                valuesAdditionalDataCpto[i] = "";
                valuesAdditionalDataCptoIndexes[i] = tmp.getAddEntryNumber();
                pg.getChildren().add(ol);
                pg.getChildren().add(it);
                i++;
            }
        } else {
            //TODO aqui se podria hacer que el field set se oculte de la vista totalmente
            OutputLabel ol5 = new OutputLabel();
            ol5.setValue("La empresa emisora no tiene definidos parametros adicionales para los conceptos.");
            fieldSetRefCpto.getChildren().add(ol5);
        }
    }


    public void imprimirValores() {
        for (String tmp : valuesAdditionalDataComp) {
            System.out.println("tmp = " + tmp);
        }
        for (String tmp : valuesAdditionalDataCpto) {
            System.out.println("tmp = " + tmp);
        }
    }

    public void creaCamposAdicionalesComprobante() {
        //test crea campos adicionales
        valuesAdditionalDataComp = new String[0];
        valuesAdditionalDataCompIndexes = new Integer[0];
        if (fieldSetRefComp != null) {
            if (fieldSetRefComp.getChildren() != null) {
                fieldSetRefComp.getChildren().clear();
            }
        }

        paramsInvoice = daoParamsFactMan.getAllAdditionalForCfdiByEmp(idEmpresa);///Ordenados por numero en que deben aparecer y despues por ID
        if (paramsInvoice != null && paramsInvoice.size() > 0) {
            //add the fieldset and panelgrid for every submenu
            PanelGrid pg = null;
            OutputLabel ol1;
            InputText it2;
            InputTextarea ita;//Input text area
            OutputLabel ol3;
            InputText it4;
            OutputLabel ol5;

            //Panel grid
            pg = new PanelGrid();
            pg.setColumnClasses(PANELGRIDSTRCOLUMNS4);
            pg.setId("pg" + "Params");
            pg.setColumns(PANELGRIDINTCOLUMNS4);
            pg.setLayout("grid");
            pg.setStyleClass("ui-panelgrid-blank");
            pg.setStyle("width: 100%;");
            fieldSetRefComp.getChildren().add(pg);
            AjaxBehavior ajax;
            int i = 0;
            valuesAdditionalDataComp = new String[paramsInvoice.size()];
            valuesAdditionalDataCompIndexes = new Integer[paramsInvoice.size()];
            for (MAddfieldsInvoice tmp : paramsInvoice) {
                ///Add elements
                ol1 = new OutputLabel();
                /*USING AN INPUT TEXT AREA
                 * */
                ita = new InputTextarea();
                ita.setAutoResize(false);
                ///ita.setMaxlength();

                ///it2 = new InputText();
                ///it2.setMaxlength();
                ///ajax = (AjaxBehavior) faceContext.getApplication().createBehavior(
                ///AjaxBehavior.BEHAVIOR_ID);
                ajax = new AjaxBehavior();
                ///it2.setId("id_" + tmp.getParam());///Param ID
                ita.setId("id_" + tmp.getParam());
                ajax.setProcess("@this,id_" + tmp.getParam());///ParamID
                ///it2.addClientBehavior("change", ajax);
                ita.addClientBehavior("change", ajax);
                //Add data
                ol1.setValue(tmp.getParamdesc());///Descripcion del param para la lectura clara de los usuarios
                ///it2.setValueExpression("value", createValueExpression("#{managedBeanFacturacionManual.valuesAdditionalDataComp['" + i + "']}", String.class));
                ita.setValueExpression("value", createValueExpression("#{managedBeanFacturacionManual.valuesAdditionalDataComp['" + i + "']}", String.class));
                valuesAdditionalDataComp[i] = "";
                valuesAdditionalDataCompIndexes[i] = tmp.getAddEntryNumber();///Guarda los indices de los params
                pg.getChildren().add(ol1);
                ///pg.getChildren().add(it2);
                pg.getChildren().add(ita);
                i++;
            }
        } else {
            OutputLabel ol5 = new OutputLabel();
            ol5.setValue("No hay parametros adicionales");
            fieldSetRefComp.getChildren().add(ol5);
        }
    }

    /**
     * Buscar empresa con addenda y mapear un identificador para mostrar los formularios
     * de entrada
     */
    private void filtraEmpresaConAddenda() {
        if (idCliente > 0) {
            String rfcCliente = daoRec.BuscarReceptorIdr(idCliente).getRfcOrigen();
            if (AddendaInList(rfcCliente)) {
                switch (rfcCliente) {
                    case RFC_CME:
                        setKeyReceptorAddenda(empresasConAddenda.get(RFC_CME));
                        break;
                    case RFC_AAA:
                        setKeyReceptorAddenda(empresasConAddenda.get(RFC_AAA));//

                        break;
                    case RFC_VW:
                        setKeyReceptorAddenda(empresasConAddenda.get(RFC_VW));//
                        System.out.println("Adenda vw");
                        break;
                    default:
                        setKeyReceptorAddenda(-1);//set not found

                        break;
                }
            } else {
                setKeyReceptorAddenda(0);
            }
        }
    }

    private boolean AddendaInList(String RFC_Emisor) {
        try {
            Iterator iter = empresasConAddenda.keySet().iterator();
            while (iter.hasNext()) {
                String key = (String) iter.next();
                if (!key.equals("") && key.toUpperCase().contains(RFC_Emisor.toUpperCase())) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return false;
    }

    private boolean ValidaProveedor(String dato) {
        return dato != null && !dato.isEmpty();
    }

    private boolean ValidaOrdenCompra(String dato) {
        return dato != null && !dato.isEmpty();
    }

    public String getFormatedNumber(double number) {
        DecimalFormat df2 = new DecimalFormat("#,###,###,##0.00");
        return df2.format(number);
    }

    public List<MReceptor> getReceptores() {
        return receptores;
    }

    public void setReceptores(List<MReceptor> Receptores) {
        this.receptores = Receptores;
    }

    public MAcceso getmAcceso() {
        return mAcceso;
    }

    public void setmAcceso(MAcceso mAcceso) {
        this.mAcceso = mAcceso;
    }

    public MEmpresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(MEmpresa empresa) {
        this.empresa = empresa;
    }

    public MEmpresa getEmpresaEmisora() {
        return empresaEmisora;
    }

    public void setEmpresaEmisora(MEmpresa empresaEmisora) {
        this.empresaEmisora = empresaEmisora;
    }

    public MReceptor getReceptor() {
        return receptor;
    }

    public void setReceptor(MReceptor receptor) {
        this.receptor = receptor;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public List<MConceptosFacturacion> getConceptosEmpresa() {
        return conceptosEmpresa;
    }

    public List<MFolios> getFolios() {
        return Folios;
    }

    public CatCapturaManual getCatalogos() {
        return catalogos;
    }

    public void setConceptosEmpresa(List<MConceptosFacturacion> conceptosEmpresa) {
        this.conceptosEmpresa = conceptosEmpresa;
    }

    public void setFolios(List<MFolios> Folios) {
        this.Folios = Folios;
    }

    public void setCatalogos(CatCapturaManual catalogos) {
        this.catalogos = catalogos;
    }


    public int getIdConcepto() {
        return idConcepto;
    }

    public void setIdConcepto(int idConcepto) {
        this.idConcepto = idConcepto;
    }

    public MFolios getFolio() {
        return folio;
    }

    public void setFolio(MFolios folio) {
        this.folio = folio;
    }


    public List<ConceptoFactura> getConceptosAsignados() {
        return conceptosAsignados;
    }

    public void setConceptosAsignados(List<ConceptoFactura> conceptosAsignados) {
        this.conceptosAsignados = conceptosAsignados;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public String getBusCop() {
        return busCop;
    }

    public void setBusCop(String busCop) {
        this.busCop = busCop;
    }

    public String getQuitarconcepto() {
        return quitarconcepto;
    }

    public void setQuitarconcepto(String quitarconcepto) {
        this.quitarconcepto = quitarconcepto;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getMotivoDescuento() {
        return motivoDescuento;
    }

    public String getNoCuenta() {
        return noCuenta;
    }

    public String getCondiciones() {
        return condiciones;
    }

    public double getMontoDescuento() {
        return montoDescuento;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setMotivoDescuento(String motivoDescuento) {
        this.motivoDescuento = motivoDescuento;
    }

    public void setNoCuenta(String noCuenta) {
        this.noCuenta = noCuenta;
    }

    public void setCondiciones(String condiciones) {
        this.condiciones = condiciones;
    }


    public void setMontoDescuento(double montoDescuento) {
        this.montoDescuento = montoDescuento;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public String getNoCliente() {

        return noCliente;
    }

    public String getRefCliente() {
        return refCliente;
    }

    public void setNoCliente(String noCliente) {
        this.noCliente = noCliente;
    }

    public void setRefCliente(String refCliente) {
        this.refCliente = refCliente;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public List<ConceptoFactura> getConceptosAsignadosSelect() {
        return conceptosAsignadosSelect;
    }

    public void setConceptosAsignadosSelect(List<ConceptoFactura> conceptosAsignadosSelect) {
        this.conceptosAsignadosSelect = conceptosAsignadosSelect;
    }

    public ConceptoFactura getTempConcepto() {
        return tempConcepto;
    }

    public void setTempConcepto(ConceptoFactura tempConcepto) {
        this.tempConcepto = tempConcepto;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getTotalTranfer() {
        return totalTranfer;
    }

    public void setTotalTranfer(double totalTranfer) {
        this.totalTranfer = totalTranfer;
    }

    public double getTotalReten() {
        return totalReten;
    }

    public void setTotalReten(double totalReten) {
        this.totalReten = totalReten;
    }

    public MEmpresa getEmisor() {
        return emisor;
    }

    public ComprobanteData getComprobanteData() {
        return comprobanteData;
    }

    public CapturaManualModelo getCapturaManualModelo() {
        return capturaManualModelo;
    }

    public DecimalFormatSymbols getSimbolos() {
        return simbolos;
    }

    public DecimalFormat getDf() {
        return df;
    }

    public void setEmisor(MEmpresa emisor) {
        this.emisor = emisor;
    }


    public void setComprobanteData(ComprobanteData comprobanteData) {
        this.comprobanteData = comprobanteData;
    }

    public void setCapturaManualModelo(CapturaManualModelo capturaManualModelo) {
        this.capturaManualModelo = capturaManualModelo;
    }

    public void setSimbolos(DecimalFormatSymbols simbolos) {
        this.simbolos = simbolos;
    }

    public void setDf(DecimalFormat df) {
        this.df = df;
    }

    public boolean isFlgButtonEliminarDisabled() {
        return flgButtonEliminarDisabled;
    }

    public void setFlgButtonEliminarDisabled(boolean flgButtonEliminarDisabled) {
        this.flgButtonEliminarDisabled = flgButtonEliminarDisabled;
    }

    public int getKeyReceptorAddenda() {
        return keyReceptorAddenda;
    }

    public String getNumeroProveedor() {
        return numeroProveedor;
    }

    public String[] getOrdenCompra() {
        return OrdenCompra;
    }

    public void setKeyReceptorAddenda(int keyReceptorAddenda) {
        this.keyReceptorAddenda = keyReceptorAddenda;
    }

    public void setNumeroProveedor(String NumeroProveedor) {
        this.numeroProveedor = NumeroProveedor;
    }

    public void setOrdenCompra(String[] OrdenCompra) {
        this.OrdenCompra = OrdenCompra;
    }

    public String getOrdenCompraInput() {
        return ordenCompraInput;
    }

    public void setOrdenCompraInput(String ordenCompraInput) {
        this.ordenCompraInput = ordenCompraInput;
    }

    public Map<String, Integer> getEmpresasConAddenda() {
        return empresasConAddenda;
    }

    public static void setEmpresasConAddenda(Map<String, Integer> empresasConAddenda) {
        ManagedBeanFacturacionManual.empresasConAddenda = empresasConAddenda;
    }

    public String getCodigoConfirmacion() {
        return codigoConfirmacion;
    }

    public void setCodigoConfirmacion(String codigoConfirmacion) {
        this.codigoConfirmacion = codigoConfirmacion;
    }

    public String getTipoRelacion() {
        return tipoRelacion;
    }

    public void setTipoRelacion(String tipoRelacion) {
        this.tipoRelacion = tipoRelacion;
    }

    public String getUuidTmp() {
        return uuidTmp;
    }

    public void setUuidTmp(String uuidTmp) {
        this.uuidTmp = uuidTmp;
    }

    public List<String> getUuidsRels() {
        return uuidsRels;
    }

    public void setUuidsRels(List<String> uuidsRels) {
        this.uuidsRels = uuidsRels;
    }

    public List<String> getUuidsRelsSelection() {
        return uuidsRelsSelection;
    }

    public void setUuidsRelsSelection(List<String> uuidsRelsSelection) {
        this.uuidsRelsSelection = uuidsRelsSelection;
    }

    public boolean isFlgBttonEliminarUuid() {
        return flgBttonEliminarUuid;
    }

    public void setFlgBttonEliminarUuid(boolean flgBttonEliminarUuid) {
        this.flgBttonEliminarUuid = flgBttonEliminarUuid;
    }

    public String getRegimenFiscal() {
        return regimenFiscal;
    }

    public void setRegimenFiscal(String regimenFiscal) {
        this.regimenFiscal = regimenFiscal;
    }

    public String getUsoCfdi() {
        return usoCfdi;
    }

    public void setUsoCfdi(String usoCfdi) {
        this.usoCfdi = usoCfdi;
    }

    public boolean isCodConfRequerido() {
        return codConfRequerido;
    }

    public void setCodConfRequerido(boolean codConfRequerido) {
        this.codConfRequerido = codConfRequerido;
    }

    public ImpuestoContainer getIvaTrasladado() {
        return ivaTrasladado;
    }

    public void setIvaTrasladado(ImpuestoContainer ivaTrasladado) {
        this.ivaTrasladado = ivaTrasladado;
    }

    public ImpuestoContainer getIepsTrasladado() {
        return iepsTrasladado;
    }

    public void setIepsTrasladado(ImpuestoContainer iepsTrasladado) {
        this.iepsTrasladado = iepsTrasladado;
    }


    public ImpuestoContainer getIvaRetenido() {
        return ivaRetenido;
    }

    public void setIvaRetenido(ImpuestoContainer ivaRetenido) {
        this.ivaRetenido = ivaRetenido;
    }

    public ImpuestoContainer getIsrRetenido() {
        return isrRetenido;
    }

    public void setIsrRetenido(ImpuestoContainer isrRetenido) {
        this.isrRetenido = isrRetenido;
    }

}
