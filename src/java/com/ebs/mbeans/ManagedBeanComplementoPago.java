package com.ebs.mbeans;

import com.ebs.helpers.LambdasHelper;
import com.ebs.util.IdGenerator;
import com.ebs.util.XmlMocksProvider;
import com.ebs.util.FloatsNumbersUtil;
import fe.db.*;
import fe.model.dao.*;
//import fe.model.fmanual.DocRelacionadoContainer;
//import fe.model.fmanual.PagoContainer;
import fe.model.util.Limpiador;
import fe.net.ClienteFacturaManual;
import fe.sat.CommentFE;
import fe.sat.CommentFEData;
import fe.sat.ComprobanteException;
import fe.sat.Direccion12Data;
import fe.sat.complementos.v33.DoctoRelacionado;
import fe.sat.complementos.v33.DoctoRelacionadoData;
import fe.sat.complementos.v33.Pago;
import fe.sat.complementos.v33.PagoData;
import fe.sat.complementos.v33.Pagos;
import fe.sat.complementos.v33.PagosData;
import fe.sat.v33.AdditionalData;
import fe.sat.v33.CatalogoData;
import fe.sat.v33.CfdiRelacionado;
import fe.sat.v33.CfdiRelacionadoData;
import fe.sat.v33.CfdiRelacionadosData;
import fe.sat.v33.ComprobanteData;
import fe.sat.v33.Concepto;
import fe.sat.v33.ConceptoData;
import fe.sat.v33.DatosComprobanteData;
import fe.sat.v33.EmisorData;
import fe.sat.v33.ReceptorData;
import lombok.Getter;
import lombok.Setter;
import mx.com.ebs.emision.factura.utilierias.PintarLog;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by eflores on 19/07/2017.
 */
public class ManagedBeanComplementoPago implements Serializable {

    //Parametro obtenido por get
    @ManagedProperty(value = "#{param.uuid}")
    private String uuidGetParam;
    private final String PLANTILLAGRAL = "CFDIV33CP";//PLantilla complemento de pago
    private final String RFC_EXTRANJERO = "XEXX010101000";
    private final String RFC_GENERICO = "XAXX010101000";
    private static final String UUIDREGEXPATT = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";
    private static final String ATEBREGEXPATT = "PRUEBAXX-ATEB-SERV-SACV-TIMBREPRUEBA";
    private ArrayList<String> respuestas;

    @Getter
    private final int NO_REQUERIDO = 0;
    @Getter
    public final int REQUERIDO = 1;
    @Getter
    private final int OPCIONAL = 2;
    @Getter
    private final int DESCONOCIDO = -1;

    private int OPERATION_OVER_PAGO = 0;//0-creacion, 1-Modificacion
    private int CREACION = 0;//0-creacion, 1-Modificacion
    private int MODIFICACION = 1;//0-creacion, 1-Modificacion

    private final int RFC_MORAL_LENGTH = 12;
    private final int RFC_FISICO_LENGTH = 13;
    private final String MXN_STR = "MXN";
    private final String XXX_STR = "XXX";
    private final CatalogoData CAT_NINGUNA_MONEDA = new CatalogoData("XXX", "Ninguna Moneda");
    private final CatalogoData CAT_PESO_MEXICANO = new CatalogoData("MXN", "Peso Mexicano");
    private final CatalogoData CAT_DOLAR_AMERICANO = new CatalogoData("USD", "Dolar Americano");
    private final CatalogoData CAT_EURO = new CatalogoData("EUR", "Euro");

    private final int LENGTH_COD_CONF = 5;

    private boolean DEBUG = true;

    //Datos de sesion
    private MAcceso mAcceso;
    private MEmpresa empresa;//Global
    private HttpServletRequest httpServletRequest;
    private FacesContext faceContext;


    private double MAX_VALUE_MONTO_PAGO = 20000000;// TODO: 05/06/2017 CAMBIAR ESTE VALOR FIJO POR EL QUE ESTA GUARDADO EN LA BASE DE DATOS

    @Getter
    @Setter
    private boolean skip;

    //Elementos del form

    @Getter
    @Setter
    private List<MReceptor> receptores;
    @Getter
    @Setter
    private List<MFolios> folios;
    //Datos de generacion de factura
    private MEmpresa emisor;
    private MReceptor receptor;
    @Getter
    @Setter
    private int idEmpresa;//ID de la empresa emisora
    @Getter
    @Setter
    private int idCliente;//ID DEL RECEPTOR O CLIENTE

    private MFolios folio;

    /*
     * Codigo obtenido no automaticamente del PAC para facturar fuera de los limites
     * establecidos en el catalogo
     */
    @Setter
    @Getter
    private boolean codConfRequerido = false;

    @Setter
    @Getter
    private String codigoConfirmacion;
    @Getter
    @Setter
    private String comment;

    //CFDIs relacionados(reemplazao de un pago por otro pago por correccion de datos)
    @Getter
    @Setter
    private String uuidCfdiRelacionado;
    @Getter
    @Setter
    private String tipoRelacionCfdis;
    @Getter
    @Setter
    private List<CfdiRelacionadoData> cfdiRelacionados;
    @Getter
    @Setter
    private List<CfdiRelacionadoData> cfdiRelacionadosSelection;


    //-----------------------------------------------------------------------------------------------------------------
    //      PAGOS
    //-----------------------------------------------------------------------------------------------------------------

    @Getter
    @Setter
    private String uuidDocRel;
    //Busqueda de CFDI por  UUID
    @Getter
    @Setter
    private String uuidCfdiBusqueda;
    @Getter
    @Setter
    private int idCFDISelect;
    @Getter
    @Setter
    private List<MCfd> listaMCfdi;
    @Getter
    @Setter
    double saldoDisponibleEmisor = 0.0;///GUARDADO EN MXN Y CONVERTIDO A LA MONEDA DEL PAGO.
    @Getter
    @Setter
    double saldoDisponibleEmisorMonedaDR = 0.0;//En la moneda del docrel
    @Getter
    @Setter
    List<MPagos> pagosPendientes;
    @Getter
    @Setter
    List<MPagos> pagosPendientesAgregados;
    @Getter
    @Setter
    MPagos pagoPendienteSelection;

    @Getter
    @Setter
    MCfd mcfdi;

    @Getter
    @Setter
    public PagoData pagoTempContainer;
    @Getter
    @Setter
    public List<PagoData> pagos;
    @Getter
    @Setter
    public List<PagoData> pagosSelection;

    //-----------------------------------------------------------------------------------------------------------------
    //      DOCUMENTOS RELACIONADOS DEL PAGO
    //-----------------------------------------------------------------------------------------------------------------
    @Getter
    @Setter
    private DoctoRelacionadoData docRelTempPago;
    @Getter
    @Setter
    private List<DoctoRelacionadoData> docsRelPago;
    @Getter
    @Setter
    private List<DoctoRelacionadoData> docsRelPagoSelection;

    @Getter
    @Setter
    private boolean requiredTipoCambioDocRel = false;
    @Getter
    @Setter
    private boolean disabledBttnAgregarDoctoRel = true;


    @Getter
    @Setter
    private boolean esReceptorExtranjero = false;
    @Getter
    @Setter
    private boolean esEmisorExtranjero = false;

    ///Controlar la activacion de los campos
    @Getter
    @Setter
    public boolean inputActivoCertPago = false;
    @Getter
    @Setter
    public boolean inputActivoCadPago = false;
    @Getter
    @Setter
    public boolean inputActivoSelloPago = false;


    @Getter
    @Setter
    public int requiredBancarizado = 0;
    @Getter
    @Setter
    public int requiredNumOper = 0;
    @Getter
    @Setter
    public int requiredRfcEmiCor = 0;
    @Getter
    @Setter
    public int requiredCtaOrd = 0;
    @Getter
    @Setter
    public int requiredRfcEmiCtaBen = 0;
    @Getter
    @Setter
    public int requiredCtaBen = 0;
    @Getter
    @Setter
    public int requiredTipoCadPago = 0;
    @Getter
    @Setter
    public int requiredNomBancEmi = 0;
    @Getter
    @Setter
    public int requiredResidenciaFiscal = 0;
    @Getter
    @Setter
    public int requiredNumRegIdTrib = 0;

    @Getter
    @Setter
    private String residenciaFiscal;//Pais
    //Numero de registroIdTrib para cuado el receptor es extranjero
    @Getter
    @Setter
    private String numRegIdTrib;
    /*
     * Lugar de emision
     */
    @Getter
    @Setter
    private String codigoPostal;

    @Getter
    @Setter
    private String descCp;


    @Getter
    @Setter
    private boolean flgBttonEliminarUuid = true;
    @Getter
    @Setter
    private boolean flgBttonEliminarDocRelComplemento = true;

    @Getter
    @Setter
    private boolean flgDisabledBttonEliminarPago = true;

    /*
     * Datos para Documentos relacionados, pagos en parcialidades o diferido
     */
    @Getter
    @Setter
    private List<MCcodigopostal> cps;

    private MCformapago objFormaPago;


    /*
     * Regimen fiscal emisor
     */
    @Getter
    @Setter
    private String regimenFiscal;
    private String ambiente = "DESARROLLO";
    @Getter
    @Setter
    private Integer decimalesRequeridos;
    @Getter
    @Setter
    private Double porcentajeVariacion;
    @Setter
    @Getter
    private double tipoCambioPago = 1.0;//Tipo de cambio del pago

    //Conceptos
    private List<ConceptoFactura> conceptosAsignados;
    @Getter
    @Setter
    private int idFolio;
    private ComprobanteData comprobanteData;
    private boolean parcialidadValida = true;
    @Setter
    @Getter
    private String numeroProveedor;
    @Setter
    @Getter
    private String[] OrdenCompra;

    private DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
    private DecimalFormat df;
    private double limiteSuperior = 0.0;
    private double limiteInferior = 0.0;
    private IdGenerator idGen;

    //-------------------------------------------------------------------------
    // Daos
    //-------------------------------------------------------------------------
    @Getter
    @Setter
    private List<MCformapago> cfs;
    private List<MCimpuesto> imps;
    @Getter
    @Setter
    private List<MCmetodoPago> mps;
    @Getter
    @Setter
    private List<MCmoneda> monedas;
    @Getter
    @Setter
    private List<MCpais> paises;
    private List<MCpatentesAduanales> patentes;
    private List<MCpedimentoAduana> pedimentos;
    private List<MCprodserv> productos;
    @Getter
    @Setter
    private List<MCregimenFiscal> regims;
    private List<MCtipoComprobante> comprobantes;
    private List<MCtipoFactor> factores;

    //Tipos de relaciones CFDIs
    @Getter
    @Setter
    private List<MCtipoRelacionCfdi> relaciones;

    private List<MCunidades> unidades;
    @Getter
    @Setter
    private List<MCusoComprobantes> usos;

    //DAOS
    private EmpresaTimbreDAO daoEmpTimp;
    private CfdiDAO daoCFD;
    private XmlDao daoXml;
    private FoliosDAO daoFolio;
    private DetallePagosDao daoPagos;
    private ParcialidadesDAO daoParc;
    private ConfigDAO daoConfig;
    private FormaPagoDAO daoForma;
    private MetodoPagoDAO daoMetodo;
    private CodigoPostalDAO daoCp;
    private MonedaDAO daoMoneda;
    private PaisDAO daoPais;
    private RegimenDAO daoRegimen;
    private TipoComprobanteDAO daoTipoComproba;
    private TipoRelacionDAO daoTipoRelacion;
    private UsoCfdiDAO daoUsoCfdi;
    private EmpresaDAO daoEmp;
    private ReceptorDAO daoRec;

    //Bindings
    @Setter
    @Getter
    private UIInput inputTipoCambio;
    @Setter
    @Getter
    private UIInput inputCodigoConfirmacion;
    @Setter
    @Getter
    private UIInput inputFormaPagoComplemento;
    @Setter
    @Getter
    private UIInput inputRfcEmisorCtaOrd;
    @Setter
    @Getter
    private UIInput inputNomBancoOrdExt;
    @Setter
    @Getter
    private UIInput inputCtaOrdenante;
    @Setter
    @Getter
    private UIInput inputRfcEmisorCtaBen;
    @Setter
    @Getter
    private UIInput inputCtaBeneficiario;
    @Setter
    @Getter
    private UIInput inputTipoCadPago;


    @Setter
    @Getter
    private UIInput inputResidenciaFiscal;
    @Setter
    @Getter
    private UIInput inputNumRegIdTrib;

    //BINDINGS DOCUMENTOS RELACIONADOS

    @Getter
    @Setter
    private UIInput inputDocRelTipoCambio;
    @Getter
    @Setter
    private boolean cargaSpei;
    @Getter
    @Setter
    UploadedFile xmlSpei;


    final NumberFormat formatter2 = new DecimalFormat("#0.00");
    final NumberFormat formatter3 = new DecimalFormat("#0.000");
    final NumberFormat formatter4 = new DecimalFormat("#0.0000");

    /**
     * Creates a new instance of {@link ManagedBeanComplementoPago}
     */
    public ManagedBeanComplementoPago() {
        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        mAcceso = (MAcceso) httpSession.getAttribute("macceso");
        empresa = mAcceso.getEmpresa();
    }

    /**
     * Post Construct
     */
    @PostConstruct
    public void init() {

        idGen = new IdGenerator(0, 0l);
        daoEmpTimp = new EmpresaTimbreDAO();
        daoCFD = new CfdiDAO();
        daoCp = new CodigoPostalDAO();
        daoMoneda = new MonedaDAO();
        daoEmp = new EmpresaDAO();
        daoRec = new ReceptorDAO();
        daoFolio = new FoliosDAO();
        daoParc = new ParcialidadesDAO();
        daoConfig = new ConfigDAO();
        daoForma = new FormaPagoDAO();
        daoMetodo = new MetodoPagoDAO();
        daoPagos = new DetallePagosDao();
        daoXml = new XmlDao();
        daoTipoComproba = new TipoComprobanteDAO();
        daoTipoRelacion = new TipoRelacionDAO();
        daoRegimen = new RegimenDAO();
        daoUsoCfdi = new UsoCfdiDAO();
        daoPais = new PaisDAO();
        conceptosAsignados = new ArrayList<>();
        decimalesRequeridos = 2;

        cfs = daoForma.getAll();
        mps = daoMetodo.getAll();
        comprobantes = daoTipoComproba.getAll();
        relaciones = daoTipoRelacion.getAll();
        regims = daoRegimen.getAll();
        monedas = daoMoneda.getAll();
        usos = daoUsoCfdi.getAll();
        paises = daoPais.getAll();
        //Retenciones
        idEmpresa = -1;
        //Para dar formato a los numeros, de lo contrario se usario la comma como separador
        simbolos.setDecimalSeparator('.');
        df = new DecimalFormat("####.##", simbolos);

        tipoRelacionCfdis = "";
        cfdiRelacionados = new ArrayList<>();
        cfdiRelacionadosSelection = new ArrayList<>();


        codigoConfirmacion = "";
        codigoPostal = "";
        uuidCfdiRelacionado = "";

        regimenFiscal = "";
        numeroProveedor = "";
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

        if (uuidGetParam != null) {
            // TODO: 20/07/2017 CARGAR DATOS DE EL CFDI especificado por get, usar algun token para validar esto machear con la regex de un uuid
        }
        docRelTempPago = new DoctoRelacionadoData();
        ///-------------------------------------------------------------------------------------------------------------
        ///             PAGOS
        ///-------------------------------------------------------------------------------------------------------------
        pagosPendientes = new ArrayList<>();
        pagosPendientesAgregados = new ArrayList<>();
        pagos = new ArrayList<>();
        pagosSelection = new ArrayList<>();
        initPago();

        cargaSpei = true;
    }

    private void initPago() {
        OPERATION_OVER_PAGO = CREACION;
        pagoTempContainer = new PagoData();
        pagoTempContainer.setId(idGen.incrementAndGetAsLong());
        pagoTempContainer.setMonedaP(CAT_PESO_MEXICANO);
        pagoTempContainer.setFormaDePagoP(new CatalogoData("", ""));
        pagoTempContainer.setTipoCadPago(new CatalogoData("", ""));
        pagoTempContainer.setCadPago("");
        pagoTempContainer.setCertPago("");
        pagoTempContainer.setSelloPago("");
        pagoTempContainer.setTipoCambioP(1.0);
        pagoTempContainer.setFechaPago(Calendar.getInstance().getTime());
        pagoTempContainer.setMonto(0.0d);

        docsRelPago = new ArrayList<>();
        initDocRelTempPago();

    }

    private void initDocRelTempPago() {
        disabledBttnAgregarDoctoRel = true;
        docRelTempPago = new DoctoRelacionadoData();
        docRelTempPago.setImpSaldoInsoluto(0.0d);
        docRelTempPago.setImpPagado(0.0d);
        docRelTempPago.setImpSaldoAnt(0.0d);
        docRelTempPago.setNumParcialidad(1);
        docRelTempPago.setTipoCambioDR(1.0);
        docRelTempPago.setMonedaDR(new CatalogoData("", ""));
        docRelTempPago.setMetodoDePagoDR(new CatalogoData("", ""));

    }

    /**
     * DELETE ALL INFO ON VIEW
     */
    public void reset() {
        idEmpresa = -1;
        idCliente = -1;
        idFolio = -1;
        codigoPostal = "";
        conceptosAsignados.clear();
        this.receptores.clear();

        cfdiRelacionados.clear();
        cfdiRelacionadosSelection.clear();


        codConfRequerido = false;
        regimenFiscal = "";
        ///-------------------------------------------------------------------------------------------------------------
        ///             PAGOS
        ///-------------------------------------------------------------------------------------------------------------
        pagosPendientes = new ArrayList<>();
        pagosPendientesAgregados = new ArrayList<>();
        pagos = new ArrayList<>();
        pagosSelection = new ArrayList<>();
        initPago();
    }


    public void cargaCfdi() {

        listaMCfdi = new ArrayList<>();
        MCfd mcfd = null;

        System.out.println("uuid: " + uuidCfdiBusqueda);

        if (uuidCfdiBusqueda != null) {
            System.out.println("uuid: " + uuidCfdiBusqueda);

            mcfdi = daoCFD.findByUUID(uuidCfdiBusqueda, idEmpresa);
            if (mcfdi != null) {
                listaMCfdi.add(mcfdi);
            }
        }

        System.out.println("tamaño: " + listaMCfdi.size());


        buscarDocumentoRelacionado();

    }

    /**
     * Carga la informacion del documento relacionado seleccionado
     */
    public void buscarDocumentoRelacionado() {

        if (validaSeleccionEmisor()) {
            requiredTipoCambioDocRel = false;
            disabledBttnAgregarDoctoRel = true;
            boolean isDocValid = false;
            String formaP = "";
            String metodoP = "";
            docRelTempPago = new DoctoRelacionadoData();
            MCfdXml xml = null;
            MCfd cfdi = null;
            double auxSaldoAnterior = 0.00;
            docRelTempPago.setIdDocumento(uuidCfdiBusqueda);//No modificables


            //Validar la seleccion de la moneda
            if (pagoTempContainer.getMonedaP() == null || pagoTempContainer.getMonedaP().getClave().isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "Por favor seleccione la moneda del pago que esta realizando" +
                                " antes de buscar un documento relacionado.", "Info"));
            } else {
                //Buscamos el pago pendiente en especifico
                try {
                    cfdi = daoCFD.findByUUID(mcfdi.getUuid(), idEmpresa);
                } catch (NullPointerException ex) {

                }
                if (cfdi != null) {
                    pagoPendienteSelection = daoPagos.getPagoPendienteByUuid(cfdi.getUuid());
                    xml = daoXml.getXml_XmlP(cfdi.getId());
                    byte[] xmlDoc = xml.getXml();
                    String str = new String(xmlDoc);

                    try {
                        // TODO: 02/10/2017 CAMBIAR POR EL XML DEL CFDI en lugar del dato dummie
                        formaP = this.getAttributeComp("FormaPago", str);
                        metodoP = this.getAttributeComp("MetodoPago", str);
                        System.out.println(formaP);
                        System.out.println(metodoP);
                        if ((formaP != null && !formaP.isEmpty() && formaP.contains("99")) && (metodoP != null && !metodoP.isEmpty() && metodoP.contains("PPD"))) {
                            isDocValid = true;
                            docRelTempPago.setMetodoDePagoDR(new CatalogoData("PPD", "Pago en Parcialidades o diferido"));
                        }
                    } catch (ComprobanteException e) {
                        e.printStackTrace(System.out);
                        FacesContext.getCurrentInstance().addMessage(
                                null,
                                new FacesMessage(
                                        FacesMessage.SEVERITY_ERROR,
                                        "Encontramos el documento pero no se pudo corroborar la  validez de la información.",
                                        ""));
                    } catch (JDOMException e) {
                        e.printStackTrace(System.out);
                        FacesContext.getCurrentInstance().addMessage(
                                null,
                                new FacesMessage(
                                        FacesMessage.SEVERITY_ERROR,
                                        "Encontramos el documento pero no se pudo corroborar la validez de la información.",
                                        ""));
                    } catch (IOException e) {
                        e.printStackTrace(System.out);
                        FacesContext.getCurrentInstance().addMessage(
                                null,
                                new FacesMessage(
                                        FacesMessage.SEVERITY_ERROR,
                                        "Encontramos el documento pero no se pudo corroborar la validez de la información.",
                                        ""));
                    } catch (Exception e) {
                        e.printStackTrace(System.out);
                        FacesContext.getCurrentInstance().addMessage(
                                null,
                                new FacesMessage(
                                        FacesMessage.SEVERITY_ERROR,
                                        "Encontramos el documento pero no se pudo corroborar la validez de la información.",
                                        ""));
                    }

                    ///  Cargamos informacion solo si el padre es un documento valido
                    if (isDocValid) {

                        //Informacion del documento relacionado
                        docRelTempPago.setIdDocumento(cfdi.getUuid());//No modificables
                        docRelTempPago.setSerie((cfdi.getSerie() == null ? "" : cfdi.getSerie()));//No modificable
                        docRelTempPago.setFolio(String.valueOf(((int) cfdi.getFolio())));//No modificable

                        System.out.println("moneda cfdi: " + mcfdi.getMoneda());
                        //MONEDA DEL DOCUMENTO RELACIONADO ES MXN
                        if (mcfdi.getMoneda() == null
                                || mcfdi.getMoneda().isEmpty()
                                || mcfdi.getMoneda().contains("MXN")
                                || mcfdi.getMoneda().contains("mxn")) {
                            docRelTempPago.setMonedaDR(new CatalogoData(cfdi.getMoneda(), "Peso Mexicano"));
                            System.out.println("monedaDrw: " + docRelTempPago.getMonedaDR());
                        } else {//MONEDA DEL DOCUMENTO RELACIONADO ES XXX -> SE CONVIERTE EN MXN
                            if (cfdi.getMoneda().equalsIgnoreCase("XXX")) {
                                FacesContext.getCurrentInstance().addMessage(
                                        null,
                                        new FacesMessage(
                                                FacesMessage.SEVERITY_ERROR,
                                                "La moneda del docto rel es XXX se cambiara por MXN .",
                                                ""));
                                docRelTempPago.setMonedaDR(CAT_PESO_MEXICANO);
                            } else {//ES OTRA MONEDA, HAY QUE BUSCAR EL DETALLE DE LAS MONEDAS
                                MCmoneda temp = LambdasHelper.findMonedaByClave(monedas, mcfdi.getMoneda());
                                if (temp != null) {
                                    docRelTempPago.setMonedaDR(new CatalogoData(temp.getClave(), temp.getDescripcion()));
                                } else {
                                    docRelTempPago.setMonedaDR(new CatalogoData(MXN_STR, "Peso Mexicano"));///default
                                }
                            }
                        }

                        //CALCULAMOS EL TIPO DE CAMBIO DEL DOCUMENTO RELACIONADO
                        //double tipoCambioDR = pagoTempContainer.getMonto() / mcfdi.getImpSaldoAnterior();
                        if (pagoPendienteSelection != null) {
                            // double tipoCambioDR = saldoDisponibleEmisor / pagoPendienteSelection.getImpSaldoAnterior();
                            //docRelTempPago.setTipoCambioDR((float)FloatsNumbersUtil.round6Places(tipoCambioDR));
                            docRelTempPago.setImpSaldoAnt(pagoPendienteSelection.getImpSaldoAnterior());
                            docRelTempPago.setNumParcialidad(pagoPendienteSelection.getNumParcialidad() + 1);
                        } else {
                            System.out.println(cfdi.getTotal());
                            docRelTempPago.setImpSaldoAnt(cfdi.getTotal());
                            docRelTempPago.setNumParcialidad(1);
                        }

                        ///Son monedas diferentes
                        System.out.println("moneda dr: " + docRelTempPago.getMonedaDR().getClave());
                        System.out.println("mpneda p: " + pagoTempContainer.getMonedaP().getClave());
                        if (!docRelTempPago.getMonedaDR().getClave().equalsIgnoreCase(pagoTempContainer.getMonedaP().getClave())) {
                            if (docRelTempPago.getMonedaDR().getClave().equalsIgnoreCase("MXN")) {
                                docRelTempPago.setTipoCambioDR(1.0);
                            } else {
                                ///El CFDI ORIGEN(DOCRELACIONADO) ESTA EN OTRA MONEDA DIFERENTE DE MXN
                                FacesContext.getCurrentInstance().addMessage(
                                        null,
                                        new FacesMessage(
                                                FacesMessage.SEVERITY_INFO,
                                                "Indique el monto del importe que se esta cubriendo de la factura origen.",
                                                ""));

                                requiredTipoCambioDocRel = true;
                            }
                        } else {
                            ///Son monedas iguales entre el pago y el doc rel
                            System.out.println("son  monedas iguales");
                            docRelTempPago.setTipoCambioDR(null);
                            requiredTipoCambioDocRel = false;
                        }

                        if (metodoP.contains("PPD")) {

                            //si la suma del saldo disponible de la empresa mas el monto es <= pagopendienteselection evitar que se puedan agregar mas pagos ya que se
                            //trata del pago de un porcentaje de un pago pendiente
                            //Si el saldo sobrepasa el monto de un pago pendiente restar saldos y permitirle pagar mas de un pago pendiente<

                            // TODO: 02/10/2017 CHECAR SI HAY QUE VALIDAR LAS MONEDAS
                            if (pagoPendienteSelection != null) {
                                docRelTempPago.setNumParcialidad(pagoPendienteSelection.getNumParcialidad() + 1);//La parcialidad que se esta creando es la nueva
                                auxSaldoAnterior = docRelTempPago.getImpSaldoAnt();
                                docRelTempPago.setImpSaldoAnt(auxSaldoAnterior);

                            } else {
                                docRelTempPago.setNumParcialidad(1);//La parcialidad que se esta creando es la nueva
                                auxSaldoAnterior = cfdi.getTotal();
                                docRelTempPago.setImpSaldoAnt(auxSaldoAnterior);

                            }

                           /* double b = saldoDisponibleEmisor / docRelTempPago.getTipoCambioDR();//Para convertirlo al tipo del doc relacionado
                            System.out.println("tipo cambio: " + docRelTempPago.getTipoCambioDR());
                            if (b < (docRelTempPago.getImpSaldoAnt())) {
                                docRelTempPago.setImpPagado(FloatsNumbersUtil.round6Places(b));//PONEMOS EL EN IMPORTE PAGADO toda EL SALDO DISPONIBLE YA QUE NO ALCAZNA
                            } else {
                                //MAYOR O IGUAL ponemos el total
                                docRelTempPago.setImpPagado(docRelTempPago.getImpSaldoAnt());//PAGO TOTAL
                            }*/

                            //
                        }
                        docRelTempPago.setImpPagado(pagoTempContainer.getMonto());
                        docRelTempPago.setImpSaldoInsoluto(auxSaldoAnterior - pagoTempContainer.getMonto());
                        disabledBttnAgregarDoctoRel = false;
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "El documento no es un documento valido, el documento relacionado debe tener en el campo 'Metodo de Pago' el valor PPD" +
                                        "y en campo 'Forma de Pago' debe tener el codigo 99", "Info"));
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Ocurrio un error al intentar obtener la información", "Info"));
                }
            }
        }
    }

    /**
     * Cambio del input de impuesto pagado
     */
    public void handleChangeImpPagado() {
        if (!pagoTempContainer.getMonedaP().getClave().equalsIgnoreCase(docRelTempPago.getMonedaDR().getClave())) {
            ///Monedas diferentes
            // double tipoCambio = saldoDisponibleEmisor / docRelTempPago.getImpPagado();
            if (!pagoTempContainer.getMonedaP().getClave().equalsIgnoreCase("MXN") &&
                    docRelTempPago.getMonedaDR().getClave().equalsIgnoreCase("MXN")) {
                docRelTempPago.setTipoCambioDR(1.0);
            }

        } else {
            ///Monedas iguales
            docRelTempPago.setTipoCambioDR(null);
        }

        //saldoDisponibleEmisorMonedaDR = saldoDisponibleEmisor / docRelTempPago.getTipoCambioDR();
        docRelTempPago.setImpSaldoInsoluto(docRelTempPago.getImpSaldoAnt() - docRelTempPago.getImpPagado());
    }

    public void agregarDocRelTempPago() {
        if (saldoDisponibleEmisor > 0) {//En moneda del pago
            inputDocRelTipoCambio.setValid(true);
            if (docRelTempPago.getImpPagado() <= 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_INFO,
                        "El valor del Imp Pagado no puede ser 0 .", ""));
            } else if (docRelTempPago.getImpSaldoInsoluto() < 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_INFO,
                        "El saldo insoluto del Docto. Rel. no puede ser negativo,corrobore los montos.", ""));
            } else {
                if (requiredTipoCambioDocRel) {
                    if (docRelTempPago.getTipoCambioDR() == null ||
                            docRelTempPago.getTipoCambioDR() <= 0 ||
                            docRelTempPago.getTipoCambioDR() == 1.0f) {
                        inputDocRelTipoCambio.setValid(false);
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                                FacesMessage.SEVERITY_INFO,
                                "Indique en el campo tipo de cambio el número de unidades de la moneda señalada en el documento relacionado\n" +
                                        "que equivalen a una unidad de la moneda del pago.", ""));
                    } else {
                        System.out.println("no requiere tipo de cambio");
                        docsRelPago.add(docRelTempPago);
                        // saldoDisponibleEmisor -= (docRelTempPago.getImpPagado() * docRelTempPago.getTipoCambioDR());
                        // double b = saldoDisponibleEmisor / docRelTempPago.getTipoCambioDR();//Para convertirlo al tipo del doc relacionado
                        // saldoDisponibleEmisorMonedaDR = FloatsNumbersUtil.round6Places(b);

                        pagosPendientesAgregados.add(pagoPendienteSelection);
                        disabledBttnAgregarDoctoRel = true;
                        initDocRelTempPago();
                    }
                } else {
                    docsRelPago.add(docRelTempPago);
                    disabledBttnAgregarDoctoRel = true;

/*                    saldoDisponibleEmisor -= (docRelTempPago.getImpPagado() * docRelTempPago.getTipoCambioDR());
                    double b = saldoDisponibleEmisor / docRelTempPago.getTipoCambioDR();//Para convertirlo al tipo del doc relacionado
                    saldoDisponibleEmisorMonedaDR = FloatsNumbersUtil.round6Places(b);*/
                    pagosPendientesAgregados.add(pagoPendienteSelection);
                    initDocRelTempPago();
                }

                // TODO: 03/10/2017 REDONDEAR A UN MAXIMO DE 6 CIFRAS Y CONVERSION A MONEDA DEL DOCRELACIONADO
                //  double a = saldoDisponibleEmisor / docRelTempPago.getTipoCambioDR();
                //a = a - docRelTempPago.getImpPagado();//Restamos el saldo en la moneda del DR
                //saldoDisponibleEmisorMonedaDR = FloatsNumbersUtil.round6Places(a);
                ///CONVERTIMOS A MONEDA DEL PAGO
                //a = a * docRelTempPago.getTipoCambioDR();
                //saldoDisponibleEmisor = FloatsNumbersUtil.round6Places(a);

                requiredTipoCambioDocRel = false;

                pagosPendientes.remove(pagoPendienteSelection);
                pagoPendienteSelection = null;


            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_INFO,
                    "Saldo insuficiente para poder agregar mas documentos relacionados.", ""));
        }
    }

    /**
     * En el caso de complemento de pago un uuid relacionado a otro pago indica que al que se esta referenciando
     * se puede sustituir por el nuevo que se esta generando (especificado en el tipo de relacion) con las correaciones
     * respectivas a los datos
     */
    public void agregarUuidCfdiRelacionado() {
        // TODO: 26/09/2017 checar la existencia del cfdi relacionado y que sea de tipo complemento recepcion de pagos
        MCfd obj = daoCFD.findByUUID(uuidCfdiRelacionado.trim(), idEmpresa);
        if (obj != null) {
            flgBttonEliminarUuid = true;
            if (cfdiRelacionados != null &&
                    cfdiRelacionados.size() >= 1) {
                flgBttonEliminarUuid = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Por el momento solo se puede agregar un CFDI relacionado al comprobante, el documento que usted esta " +
                                "creando sustituira al UUID que se esté referenciando en los CFDIs relacionados.", ""));
            } else {
                CfdiRelacionadoData temp = new CfdiRelacionadoData();
                temp.setUuid(uuidCfdiRelacionado);
                cfdiRelacionados.add(temp);
                uuidCfdiRelacionado = "";
                flgBttonEliminarUuid = false;
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "El documento que intenta agregar no se encuentra en nuestra base  de datos o no es un documento de recepcion de pagos.", ""));
        }
    }

    public void eliminarUuidRelacionados() {
        cfdiRelacionados = LambdasHelper.deleteInList(cfdiRelacionados, cfdiRelacionadosSelection);
        cfdiRelacionadosSelection.clear();
        if (cfdiRelacionados.isEmpty()) {
            flgBttonEliminarUuid = true;
        } else {
            flgBttonEliminarUuid = false;
        }
    }

    public void eliminarDocsRelacionados() {
// TODO: 02/10/2017 Mover de la lista los elementos relacionados para poder seleccionarlos una vez mas dentro d elos pagos
        docsRelPago.removeAll(docsRelPagoSelection);
        docsRelPagoSelection.clear();
    }


    public void eliminarPago() {
        pagos.removeAll(pagosSelection);
        pagosSelection.clear();
    }

    public void buscarRegimenByRfc() {
        MEmpresa mywmp = daoEmp.BuscarEmpresaId(idEmpresa);
        String rfcEmisor = mywmp.getRfcOrigen();
        if (rfcEmisor != null && !rfcEmisor.isEmpty() && rfcEmisor.length() == RFC_FISICO_LENGTH) {
            regims = daoRegimen.getRegimenFisicas();
        } else if (rfcEmisor != null && !rfcEmisor.isEmpty() && rfcEmisor.length() == RFC_MORAL_LENGTH) {
            regims = daoRegimen.getRegimenMoral();
        } else {
            regims = daoRegimen.getAll();
        }
    }

    public void buscarUsoCfdiByRfc() {
        MReceptor mywmp = daoRec.BuscarReceptorIdr(idCliente);
        String rfcReceptor = mywmp.getRfcOrigen();
        if (rfcReceptor != null && !rfcReceptor.isEmpty() && rfcReceptor.length() == RFC_FISICO_LENGTH) {
            usos = daoUsoCfdi.getForFisicas();
        } else if (rfcReceptor != null && !rfcReceptor.isEmpty() && rfcReceptor.length() == RFC_MORAL_LENGTH) {
            usos = daoUsoCfdi.getForMorales();
        } else {
            usos = daoUsoCfdi.getAll();
        }
    }

    /**
     * Agrega los datos por defento para crear un documento con complemento de pago en parcialidaddes
     */
    public void initComprobanteAsPago() {
        //Si es una parcialidad
        // CUANDO SEA UNA PARCIALIDAD la que se esta creando
                    /*
                    Incorporar el complemento para recepcion de pagos, se detalla la cantidad que se paga e identificala factura cuyo saldo se liquida
                    */
        /*
         * El monto del pago se aplicara proporcionalmetne a los conceptos integradosen el comprobante emitido por el valor total de la operacion ¿¿A que se refiere proporcionalmente??*/
        /*
         * Se pude emitir un solo CFDI por cada parcialidad o una CFDI por todos los pagos recibidos en un periodo de un mes siempre que estos correspondan al mismo receptor del comprobante*/
        /* Cuando se emita un CFDI con el complemetno de recepcion de pagos, este debera emitirse a mas tardar el decimo dianatural al mes siguiente que se realizo el pago*/
        /*Cuando ya exista un CFDI de complemento de pagos para el CFDI de la operacion, este ultimo no podra ser cancelado, las correciones deberan ser realizada mediante la emision de CFDIs de egresos
         * por devoliciones, descuentos y bonificaciones */
        /*
         * Los CFDI´s con complemento de pago podran cancelarse siempre que se sustituya por otro con los datos correctos y cuando se realicen a mas tardar el ultimo dia del ejercicio actual(año)*/
        /*
         * No deben existir los campos MetodoPago y FormaPago, Condiciones de pago,Descuento, Tipo de Cambio
         * El subtotal debe ir en 0
         * la moneda debe de ser XXX
         * El total debe de ser 0
         * El tipo de comprobante debe de ser P(Pago)
         * Exigir el codigo de confirmacion cuando el campo Monto del Complemento exceda el limite
         *
         * */
        comprobanteData = new ComprobanteData();
        List<Concepto> listaConceptoData = new ArrayList();
        comprobanteData.setDatosComprobante(new DatosComprobanteData());
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setTotal(0.0);
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setMetodoDePago(null);
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setFormaDePago(null);
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setCondicionesDePago(null);
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setDescuento(null);
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setTipoDeCambio(null);
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setTipoDocumento(null);
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setSubTotal(0.0);
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setMoneda(new CatalogoData("XXX",
                "Ninguna moneda")); //a nivel comprobante debe de ser siempre XXX
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setTipoDeComprobante(new CatalogoData("P",
                "Pago"));
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setSello("1111111111111111111111111111111111111111=");
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setNoCertificado("00000000001234567890");//Mas de 20 caracteres
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setCertificado("TESTTESTTESTTESTTESTTESTTESTTESTTESTTEST");//Mas de 20 caracteres
        //--------------------------------------------------------------------
        // CONCEPTOS
        //--------------------------------------------------------------------
        /*
         * Para el nodo conceptos
         * Se debe registrar un solo concepto con los siguiente datos
         * En el campo ClaveProdServ registrar el valor 84111506 ok
         * El campo NoIdentificacion no debe existir ok
         * El campo cantidad debe de tener el valor 1 ok
         * El campo ClaveUnidad debe tener el valor ACT ok
         * El campo unidad no debe existir ok
         * La descripcion debe tener el valor Pago ok
         * El valor unitario debe de tener el valor 0 ok
         * El importe se debe de registrar el valor 0 ok
         * El descuento no debe de existir ok
         * */
        ConceptoData cptoData = new ConceptoData();
        cptoData.setCantidad(1);
        cptoData.setImporte(0);
        cptoData.setDescripcion("Pago");
        cptoData.setValorUnitario(0);
        cptoData.setNoIdentificacion(null);
        cptoData.setUnidad(null);
        cptoData.setClaveProdServ(new CatalogoData("84111506", "Servicios de facturación"));
        cptoData.setClaveUnidad(new CatalogoData("ACT", "Actividad"));
        cptoData.setDescuento(null);
        cptoData.setImpuestosConcepto(null);
        listaConceptoData.add(cptoData);//agregamos solo un concepto a la factura
        comprobanteData.setConceptos(listaConceptoData);
        comprobanteData.setComplemementosFe(new ArrayList<>());
    }

    /**
     * Agrega los datos por defecto para crear un documento con complemento de pagos diferido
     */
    public void initComprobanteAsDiferido() {
        comprobanteData = new ComprobanteData();
    }

    /**
     * Agregao los datos por defecto para crear un documento de anticipo usando el complemento de pagos
     */
    public void initComprobanteAsAnticipo() {
        // TODO: 21/09/2017 AGREGAR INFO DUMMIE PARA ANTICIPOS

    }


    /**
     * Generar la factura elaborada manualmente Llamar al ws de facturacion en
     * ebs
     */
    public void generarFactura() {
        if (preValidationFactura()) {//PREVALIDAR FACTURA CON COMPLEMENTO DE PAGO ANTES DE GENERAR Y ENVIAR EL DOCUMENTO A TIMBRAR
            //Creamos los contenedores

            // TODO: 22/09/2017 BUSCAR LA EMPRESA Y EL CLIENTE EN LA LISTA YA EXISTENTE EN VEZ DE IR A LA BD
            emisor = daoEmp.BuscarEmpresaId(this.idEmpresa);
            receptor = daoRec.BuscarReceptorIdr(this.idCliente);
            folio = daoFolio.BuscarFolioId(idFolio);

            //--------------------------------------------------------------------
            //DATOS DEL COMPROBANTE
            //--------------------------------------------------------------------
            initComprobanteAsPago();

            String[] arlugexp = codigoPostal.split("-");
            CatalogoData lugarExpedicion = new CatalogoData(arlugexp[0], arlugexp[1]);

            String[] myawarr;
            myawarr = regimenFiscal.split("-");
            CatalogoData cregimenfiscal = new CatalogoData(myawarr[0], myawarr[1]);

            ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setLugarExpedicion(lugarExpedicion);
            ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setSerie(folio.getId());
            ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setFolio("1");
            ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setFolioErp("");
            ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setFecha(Calendar.getInstance().getTime());

            if (codConfRequerido)
                ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setConfirmacion(codigoConfirmacion);

            //----------------------------------------------------------------------------------------
            // COMENTARIOS
            //----------------------------------------------------------------------------------------
            List<CommentFE> arrComentarios = new ArrayList();
            CommentFEData comentario = new CommentFEData();
            comentario.setComment(comment);
            comentario.setPosition("9999");
            arrComentarios.add(comentario);
            comprobanteData.setComments9999(arrComentarios);

            //CFDIS relacionados
            if (cfdiRelacionados.size() > 0)
                comprobanteData = agregarCfdisRelacionadosComprobante(cfdiRelacionados, relaciones, tipoRelacionCfdis, comprobanteData);

            //----------------------------------------------------------------------------------------
            // EMISOR
            //----------------------------------------------------------------------------------------
            EmisorData emisor12Data = new EmisorData();
            Direccion12Data direccionFiscal12 = new Direccion12Data();
            direccionFiscal12.setCalle(Limpiador.cleanString(emisor.getDireccion().getCalle()));
            direccionFiscal12.setColonia(Limpiador.cleanString(emisor.getDireccion().getColonia()));
            direccionFiscal12.setCP(Limpiador.cleanString(emisor.getDireccion().getCp()));
            direccionFiscal12.setEstado(Limpiador.cleanString(emisor.getDireccion().getEstado()));
            direccionFiscal12.setLocalidad(Limpiador.cleanString(emisor.getDireccion().getLocalidad()));
            direccionFiscal12.setMunicipio(Limpiador.cleanString(emisor.getDireccion().getMunicipio()));
            direccionFiscal12.setNoExterior(Limpiador.cleanString(emisor.getDireccion().getNoExterior()));
            direccionFiscal12.setNoInterior(Limpiador.cleanString(emisor.getDireccion().getNoInterior()));
            direccionFiscal12.setPais(Limpiador.cleanString(emisor.getDireccion().getPais()));
            direccionFiscal12.setReferencia("Ninguna");
            emisor12Data.setDireccionFiscalEmisor(direccionFiscal12);
            emisor12Data.setRegimenFiscal(cregimenfiscal);
            emisor12Data.setRfc(Limpiador.cleanString(emisor.getRfcOrigen()));
            emisor12Data.setNombre(Limpiador.cleanString(emisor.getRazonSocial().replace(".", "")));// <---------------- Se carga el nombre del emisor
            comprobanteData.setEmisor(emisor12Data);

            //----------------------------------------------------------------------------------------
            // RECEPTOR
            //----------------------------------------------------------------------------------------
            ReceptorData receptor12Data = new ReceptorData();
            Direccion12Data direccionReceptorFiscal12 = new Direccion12Data();
            direccionReceptorFiscal12.setCalle(Limpiador.cleanString(receptor.getDireccion().getCalle()));
            direccionReceptorFiscal12.setColonia(Limpiador.cleanString(receptor.getDireccion().getColonia()));
            direccionReceptorFiscal12.setCP(Limpiador.cleanString(receptor.getDireccion().getCp()));
            direccionReceptorFiscal12.setEstado(Limpiador.cleanString(receptor.getDireccion().getEstado()));
            direccionReceptorFiscal12.setLocalidad(Limpiador.cleanString(receptor.getDireccion().getLocalidad()));
            direccionReceptorFiscal12.setMunicipio(Limpiador.cleanString(receptor.getDireccion().getMunicipio()));
            direccionReceptorFiscal12.setNoExterior(Limpiador.cleanString(receptor.getDireccion().getNoExterior()));
            direccionReceptorFiscal12.setNoInterior(Limpiador.cleanString(receptor.getDireccion().getNoInterior()));
            direccionReceptorFiscal12.setPais(Limpiador.cleanString(receptor.getDireccion().getPais()));
            direccionReceptorFiscal12.setReferencia("Ninguna");
            receptor12Data.setDireccionReceptor(direccionReceptorFiscal12);
            receptor12Data.setRfc(Limpiador.cleanString(receptor.getRfcOrigen()));
            receptor12Data.setNombre(Limpiador.cleanString(receptor.getRazonSocial().replace(".", "")));
            receptor12Data.setUsoCFDI(new CatalogoData("P01", "Por definir"));//VALOR CONSTANTE

            if (esReceptorExtranjero) {
                receptor12Data.setRfc(RFC_EXTRANJERO);
                String[] arrTmp = residenciaFiscal.split("-");
                receptor12Data.setResidenciaFiscal(new CatalogoData(arrTmp[0], arrTmp[1]));
                receptor12Data.setNumRegIdTrib(numRegIdTrib);
            }
            comprobanteData.setReceptor(receptor12Data);

            //----------------------------------------------------------------------------------------
            // CARGA DE COMPLEMENTOS
            //----------------------------------------------------------------------------------------


            //----------------------------------------------------------------------------------------
            //    AGREGAR PAGOS
            //----------------------------------------------------------------------------------------
            this.comprobanteData = agregarPagosAlComprobante(pagos, this.comprobanteData);

            System.out.println("cuenta 1: " + pagoTempContainer.getCtaBeneficiario());
            System.out.println("cuenta 2: " + pagoTempContainer.getCtaOrdenante());
            //----------------------------------------------------------------------------------------
            // PAREMETROS ADICIONALES
            //----------------------------------------------------------------------------------------
            this.comprobanteData = agregarParamsComprobante(comprobanteData, new ArrayList<String>() {{
                add("0");
            }});


            PintarLog.println("Apunto de llamar al servicio de factura manual desde MBeanComplementoPago");
            String respuestaServicio = "";
            MEmpresaMTimbre m = daoEmpTimp.ObtenerClaveWSEmpresaTimbre(idEmpresa);
            if (m != null) {
                if (parcialidadValida) {


                    respuestaServicio = new ClienteFacturaManual().exeGenFactura(comprobanteData, m.getClaveWS(), ambiente, DEBUG);

                    //PintarLog.println("respuestaServicio:" + respuestaServicio);
                    if (checkRespuestaServicio(respuestaServicio)) {
                        // TODO: 09/10/2017  UPDATE data on BD
                        MEmpresa emisor = daoEmp.BuscarEmpresaId(idEmpresa);
                        emisor.setImpSaldoDisponible(saldoDisponibleEmisor);
                        daoEmp.GuardarOActualizaEmpresa(emisor);
                        // TODO: 09/10/2017 GUARDAR ACTUALIZACIONES DE CADA PAGO
                        for (Pago pg : pagos) {
                            for (DoctoRelacionado dr : pg.getDoctoRelacionado()) {
                                String uuid = dr.getIdDocumento();
                                MPagos pago = daoPagos.getPagoPendienteByUuid(uuid);

                                if (pago != null) {
                                    pago.setImpSaldoAnterior(dr.getImpSaldoInsoluto());
                                    pago.setNumParcialidad(dr.getNumParcialidad());
                                    daoPagos.savePago(pago);

                                } else {
                                    System.out.println("Guardando pagos");
                                    pago = new MPagos();
                                    if (mcfdi != null) {
                                        pago.setCfdiId(mcfdi.getId());
                                    } else {
                                        pago.setCfdiId(0);
                                    }
                                    pago.setUuid(dr.getIdDocumento());
                                    pago.setNumParcialidad(dr.getNumParcialidad());
                                    pago.setImpSaldoAnterior(dr.getImpSaldoInsoluto());
                                    if (dr.getImpSaldoInsoluto() == 0.0) {
                                        pago.setPagado(true);
                                    }
                                    pago.setEmpId(idEmpresa);
                                    pago.setMoneda(dr.getMonedaDR().getClave());

                                    pago.setFecha(pg.getFechaPago());
                                    System.out.println("save pago: " + pago.getImpSaldoAnterior());
                                    daoPagos.savePago(pago);

                                }

                            }
                        }
                        this.reset();
                        FacesContext.getCurrentInstance().addMessage("frmManual", new FacesMessage(FacesMessage.SEVERITY_INFO, "La factura se genero correctamente.", "Error"));
                    } else {
                        //Show all to user
                        for (String mssg : respuestas) {
                            FacesContext.getCurrentInstance().addMessage("frmManual", new FacesMessage(FacesMessage.SEVERITY_ERROR, mssg, "Error"));
                        }
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage("frmCompPago", new FacesMessage(FacesMessage.SEVERITY_WARN, "Verifique que el monto total y la numero de la parcialidad sea menor o igual a la factura original establecida.", "Error"));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage("frmCompPago", new FacesMessage(FacesMessage.SEVERITY_ERROR, "La empresa no cuenta con timbres.", "Error"));
            }
        }
    }

    private ComprobanteData agregarParamsComprobante(ComprobanteData comprobanteData, List<String> params) {
        AdditionalData additionalData = new AdditionalData();
        additionalData.setParam(new String[]{"Orden Interna ", "Centro Beneficio"});
        comprobanteData.setAdditional(additionalData);
        String[] datosCliente = new String[3];
        datosCliente[0] = "" + (Integer.parseInt(params.get(0)) > 0 ? params.get(0) : 0);
        datosCliente[1] = "";
        datosCliente[2] = "";
        ((AdditionalData) comprobanteData.getAdditional()).setParam(datosCliente);
        ((AdditionalData) comprobanteData.getAdditional()).setPlantilla(PLANTILLAGRAL);  //CFDIV33CP
        return comprobanteData;
    }

    private PagoData agregarDocumentosRelacionadosPago(List<DoctoRelacionadoData> docs, PagoData tempPago) {
        //Do modifications on docs
        //Then add DocsRels to tempPago
        List<DoctoRelacionado> tempList = new ArrayList<>();
        tempList.addAll(docs);
        tempPago.setDoctoRelacionado(tempList);


        /*for (DocRelacionadoContainer tmp : docsRels) {
            DoctoRelacionadoData dc1 = new DoctoRelacionadoData();
            dc1.setSerie(tmp.getSerie());
            dc1.setFolio(String.valueOf(tmp.getFolio()));
            dc1.setIdDocumento(tmp.getIdDocumento());//Puede ser UN uuid O un numero de comprobante
            if (tmp.getMoneda() == null || tmp.getMoneda().equalsIgnoreCase("MXN")) {
                dc1.setMonedaDR(new CatalogoData("MXN", "Peso Mexicano"));
            }

            if (!pago.getMonedaP().getClave().equals(tmp.getMoneda())) {
                if (tmp.getMoneda().equals("MXN")) {
                    dc1.setTipoCambioDR(1f);
                } else {//poner el tipod e cambio de acuerdo a las monedas que estan implicadas en la operacion
                    //cuantas unidades del cfdi origen equivalen a una unidad de la moneda del pago que se esta haciendo
                    dc1.setTipoCambioDR(Float.valueOf(tmp.getTipoCambio()));// Ponemos el tipo de cambio del doc relacionado
                }
            } else if (pago.getMonedaP().getClave().equals("MXN") && tmp.getMoneda().equals("MXN")) {
                dc1.setTipoCambioDR(null);
            }

            //Los importes deben corresponder a la moneda y ser redondeados hasta la cantidad de decimales que soporte la moneda
            //dc1.setMetodoDePagoDR(new CatalogoData("PIP", "Pago inicial y parcialidades"));
            dc1.setMetodoDePagoDR(new CatalogoData("PPD", "Pago en parcialidades o diferido"));
            //TODO siempre seran de este tipo de metodo de pago para los documentos relacionados
            dc1.setNumParcialidad(tmp.getNumUltParcialidad() + 1);//Numero de la parcialidad del documento relacionado que se esta pagando <i de N>s,1 si es diferido
            dc1.setImpSaldoAnt(tmp.getImporteSaldoAnterior());//importe pagado en este documento relacionado
            dc1.setImpPagado(tmp.getImportePagado());
            if (dc1.getMetodoDePagoDR().getClave().equalsIgnoreCase("PPD"))
                dc1.setImpSaldoInsoluto(dc1.getImpSaldoAnt() - dc1.getImpPagado());//diferencia entre el importe saldo anterior yel monto del pago

            docs.add(dc1);
        }
        pago.setDoctoRelacionado(docs);*/
        return tempPago;
    }

    private ComprobanteData agregarCfdisRelacionadosComprobante(List<CfdiRelacionadoData> cfdisRels,
                                                                List<MCtipoRelacionCfdi> relaciones,
                                                                String tipoRelacionCfdis,
                                                                ComprobanteData comp) {
        //--------------------------------------------------------------------------
        //CFDI's RELACIONADOS solo cuando el documento que se esta generando sustituye a uno anterior
        // En este caso en el campo Tipo de Relacion se debe de registrar la clave 04-Sustitucion de CFDIs previos
        //los uuids previos deben de ser CFDIs con complemento de recepcion de pagos que seran sustituidos
        //--------------------------------------------------------------------------
        if (cfdisRels != null && cfdisRels.size() == 1) {
            CfdiRelacionadosData cfdisRel = new CfdiRelacionadosData();
            CfdiRelacionadoData singleCfdi = new CfdiRelacionadoData();
            // TODO: 22/09/2017 AGREGAR TODOS LOS CFDIS relacionados al documento
            List<CfdiRelacionado> listCfdis = new ArrayList<>();
            singleCfdi.setUuid(cfdisRels.get(0).getUuid());
            listCfdis.add(singleCfdi);
            cfdisRel.setCfdiRelacionado(listCfdis);
            MCtipoRelacionCfdi obj = LambdasHelper.findTipoRelacionCfdiByClave(relaciones, tipoRelacionCfdis);
            //Cuando se realiza una correccion de un CFDI previo hay que poner el tipo de relacion como 04
            cfdisRel.setTipoRelacion(new CatalogoData(obj.getClave(), obj.getDescripcion()));
            comp.setCfdiRelacionados(cfdisRel);
        }
        return comp;

    }


    private ComprobanteData agregarPagosAlComprobante(List<PagoData> pagos, ComprobanteData tmpComp) {
        List<Pago> tempList = new ArrayList<>(pagos);
        tempList.forEach(p -> {
            if (p.getTipoCadPago() == null ||
                    p.getTipoCadPago().getClave() == null ||
                    p.getTipoCadPago().getClave().isEmpty()) {
                ((PagoData) p).setTipoCadPago(null);
                ((PagoData) p).setSelloPago(null);
                ((PagoData) p).setCertPago(null);
                ((PagoData) p).setCadPago(null);
                p.getDoctoRelacionado().forEach(dr -> {
                    ///Si monedas son iguales
                    if (dr.getMonedaDR().getClave().equalsIgnoreCase(p.getMonedaP().getClave())) {
                        if (dr.getMonedaDR().getClave().equalsIgnoreCase(MXN_STR)) {
                            ((DoctoRelacionadoData) dr).setTipoCambioDR(null);
                        }
                    } else {///son monedas diferentes
                        if (dr.getMonedaDR().getClave().equalsIgnoreCase("MXN")) {
                            ((DoctoRelacionadoData) dr).setTipoCambioDR(1.0);
                        } else {
                            ///DEJAR EL TIPO DE CAMBIO CALCULADO
                        }
                    }
                });
            }
            if (p.getTipoCadPago() == null || p.getNumOperacion().isEmpty()) {
                ((PagoData) p).setNumOperacion(null);
            }
            if (p.getMonedaP().getClave().equalsIgnoreCase(MXN_STR)) {
                ((PagoData) p).setTipoCambioP(null);
            }
        });
        Pagos mispagos = new PagosData();
        ((PagosData) mispagos).setPagos(tempList);
        tmpComp.getComplementosFe().add((PagosData) mispagos);
        return tmpComp;

    }

    public void handleChangeNumRegIdTrib() {
        inputNumRegIdTrib.setValid(true);
        //inputNumRegIdTrib.setRequired(true);
        if (requiredNumRegIdTrib == REQUERIDO && (numRegIdTrib.isEmpty())) {
            inputNumRegIdTrib.setValid(false);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "El campo NumRegIdTrib no puede estar vacio!!!", ""));
        }
    }

    public void handleChangeResidenciaFiscal() {
        inputResidenciaFiscal.setValid(true);
        if (requiredResidenciaFiscal == REQUERIDO && residenciaFiscal.isEmpty()) {
            inputResidenciaFiscal.setValid(false);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Por favor seleccione la residencia fiscal del cliente!!!", ""));
        }
    }

    public void handleMonedaChange() {
        inputTipoCambio.setValid(true);
        //docRelTempPago.setTipoCambioDR(pagoTempContainer.getTipoCambioP());
        // TODO: 09/10/2017 POR EL MOMENTO SOLO SE PERMITEN PAGOS EN MXN
        // pagoTempContainer.setMonedaP(CAT_PESO_MEXICANO);
        pagoTempContainer.setMonedaP(new CatalogoData(pagoTempContainer.getMonedaP().getClave(), ""));
       /* FacesContext.getCurrentInstance().addMessage("frmCompPago", new FacesMessage(FacesMessage.SEVERITY_WARN,
                "Por el momento solo se permite MXN como moneda de pago", "Info"));*/
        cambiarDecimalesRequeridos();
        if (!pagoTempContainer.getMonedaP().getClave().contains(XXX_STR)) {
            if (!pagoTempContainer.getMonedaP().getClave().contains(MXN_STR)) {
                if (pagoTempContainer.getTipoCambioP() == 1 || tipoCambioPago <= 0) {
                    FacesContext.getCurrentInstance().addMessage("frmCompPago", new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "El tipo de cambio del pago debe de ser diferente de 1.0 y mayor a 0 cuando la moneda no se 'MXN'", "Info"));
                    inputTipoCambio.setValid(false);
                }
            } else {
                ///ES MXN
                pagoTempContainer.setTipoCambioP(1.0);
            }
        } else {
            FacesContext.getCurrentInstance().addMessage("frmCompPago", new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "La moneda del pago no puede ser 'XXX'", "Info"));
            ((CatalogoData) pagoTempContainer.getMonedaP()).setClave("");
        }
    }

    public void handleChangeFormaPagoPago() {
        if (pagoTempContainer.getFormaDePagoP().getClave().contains("99")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "La forma de pago debe de ser diferente de '99'", ""));
            ((CatalogoData) pagoTempContainer.getFormaDePagoP()).setClave("");
        } else {
            buscarDetalleFormaDePago();
        }
    }

    public void buscarDetalleFormaDePago() {
        objFormaPago = LambdasHelper.findFormaPagoByClave(cfs, pagoTempContainer.getFormaDePagoP().getClave());
        cargarInfoParaCamposDefinidos();
    }

    private void cargarInfoParaCamposDefinidos() {

        if (objFormaPago != null) {
            requiredBancarizado = (objFormaPago.getBancarizado() == null) ? -1 : objFormaPago.getBancarizado();
            requiredNumOper = (objFormaPago.getNumeroOperacion() == null) ? -1 : objFormaPago.getNumeroOperacion();
            requiredRfcEmiCor = (objFormaPago.getRfcEmiCor() == null) ? -1 : objFormaPago.getRfcEmiCor();
            requiredCtaOrd = (objFormaPago.getCuentaOrdenante() == null) ? -1 : objFormaPago.getCuentaOrdenante();
            requiredRfcEmiCtaBen = (objFormaPago.getRfcEmiBen() == null) ? -1 : objFormaPago.getRfcEmiBen();
            requiredCtaBen = (objFormaPago.getCuentaBeneficiario() == null) ? -1 : objFormaPago.getCuentaBeneficiario();
            requiredTipoCadPago = (objFormaPago.getTipoCadenaPago() == null) ? -1 : objFormaPago.getTipoCadenaPago();
            if (requiredTipoCadPago == NO_REQUERIDO) {
                inputActivoSelloPago = false;
                inputActivoCadPago = false;
                inputActivoCertPago = false;
            }
            //TODO El siguiente campo es solo requerido caundo el rfc de la cuenta ordenante sea XEXX010101000
            requiredNomBancEmi = (objFormaPago.getNombreBancoEmisor() == null) ? -1 : Integer.valueOf(objFormaPago.getNombreBancoEmisor());
        } else {

        }
    }

    public void handleChangeNumOper() {
        System.out.println(pagoTempContainer.getNumOperacion());
    }

    public void handleChangeRfcEmisorCtaOrdenante() {
      /*  inputRfcEmisorCtaOrd.setValid(true);
        if (requiredRfcEmiCor == REQUERIDO && (pagoTempContainer.getRfcEmisorCtaOrd() == null || pagoTempContainer.getRfcEmisorCtaOrd().isEmpty())) {
            {
                inputRfcEmisorCtaOrd.setValid(false);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "El RFC del Emisor Cta. Ord. es requerido.", ""));
            }
        }*/
    }

    public void handleChangeCtaOrdenante() {
     /*   inputRfcEmisorCtaOrd.setValid(true);
        if (requiredCtaOrd == REQUERIDO && (pagoTempContainer.getCtaOrdenante() == null || pagoTempContainer.getCtaOrdenante().isEmpty())) {
            inputRfcEmisorCtaOrd.setValid(false);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "La Cta. Ordenante es requerida.", ""));
        }*/
    }

    public void handleChangeRfcEmisorBen() {
      /*  inputRfcEmisorCtaBen.setValid(true);
        if (requiredRfcEmiCtaBen == REQUERIDO & (pagoTempContainer.getRfcEmisorCtaBen() == null || pagoTempContainer.getRfcEmisorCtaBen().isEmpty())) {
            inputRfcEmisorCtaBen.setValid(false);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "El RFC del Emisor Cta. Ben. es requerido", ""));
        }*/
    }

    public void handleChangeCtaBen() {
 /*       inputCtaBeneficiario.setValid(true);
        if (requiredCtaBen == REQUERIDO && (pagoTempContainer.getCtaBeneficiario() == null || pagoTempContainer.getCtaBeneficiario().isEmpty())) {
            inputCtaBeneficiario.setValid(false);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "La Cta.Ben es requerida.", ""));
        }*/
    }

    public void handleChangeTipoCadPag() {

    }

    public void handleChangeNomBancEmi() {
      /*  inputNomBancoOrdExt.setValid(true);
        if (requiredNomBancEmi == REQUERIDO && (pagoTempContainer.getNomBancoOrdExt() == null
                || pagoTempContainer.getNomBancoOrdExt().isEmpty())) {
            inputNomBancoOrdExt.setValid(false);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "El nombre del banco emisor del pago es requerido.", ""));
        }*/
    }

    public void handleChangeEsReceptorExtranjero() {
        requiredResidenciaFiscal = NO_REQUERIDO;
        requiredNumRegIdTrib = NO_REQUERIDO;
        if (esReceptorExtranjero) {
            requiredResidenciaFiscal = REQUERIDO;
            requiredNumRegIdTrib = REQUERIDO;
        } else {
            residenciaFiscal = "";
            numRegIdTrib = "";
        }
        inputNumRegIdTrib.setValid(true);
        inputResidenciaFiscal.setValid(true);


        if (esReceptorExtranjero && (residenciaFiscal == null || residenciaFiscal.isEmpty()
                || numRegIdTrib == null || numRegIdTrib.isEmpty())) {
            inputNumRegIdTrib.setValid(false);
            inputResidenciaFiscal.setValid(false);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Cuando el receptor esta marcado como extranjero es necesario que seleccione e ingrese la " +
                            "'Residencia fiscal' y el 'No. Reg. Id. Trib.'.", ""));
        }
    }


    /**
     * Buscamos la moneda con la que se realizo el pago para obtener sus datos.
     */
    public void cambiarDecimalesRequeridos() {
       /* String moneda = this.pagoTempContainer.getMonedaP().getClave();
        MCmoneda tmp = LambdasHelper.findMonedaByClave(monedas, moneda);
        if (tmp != null) {
            decimalesRequeridos = tmp.getDecimales();
            porcentajeVariacion = tmp.getPorcentajeVariacion();
            pagoTempContainer.setTipoCambioP(tmp.getTipoCambio());
            inputTipoCambio.setValid(true);
        } else {
            decimalesRequeridos = 0;
            porcentajeVariacion = 0.01;
            pagoTempContainer.setTipoCambioP(1.0);
        }*/
        inputTipoCambio.setValid(true);
        decimalesRequeridos = 0;
        porcentajeVariacion = 0.01;
        pagoTempContainer.setTipoCambioP(1.0);
        //validarMontoComplemento();
    }

    public void validarMontoComplemento() {
        codConfRequerido = false;
        if (pagoTempContainer.getMonto() >= MAX_VALUE_MONTO_PAGO) {
            codConfRequerido = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "El monto del pago esta fuera del rango permitido, en este caso es necesario ingresar el código" +
                            "de confirmación.", ""));
        }
    }

    public void validaMontoTipoCambioPago() {
        inputTipoCambio.setValid(true);
        inputCodigoConfirmacion.setValid(true);
        if (porcentajeVariacion != null) {
            codConfRequerido = false;
            if (pagoTempContainer.getMonedaP().getClave() != null && !pagoTempContainer.getMonedaP().getClave().isEmpty()) {
                if (porcentajeVariacion != null && porcentajeVariacion > 0 && pagoTempContainer.getTipoCambioP() > 0) {
               /*     limiteSuperior = pagoTempContainer.getTipoCambioP() * (1 + porcentajeVariacion);
                    limiteInferior = pagoTempContainer.getTipoCambioP() * (1 - porcentajeVariacion);
                    if (pagoTempContainer.getTipoCambioP() < 0 || pagoTempContainer.getTipoCambioP() < limiteInferior
                            || pagoTempContainer.getTipoCambioP() > limiteSuperior) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "El tipo de cambio esta fuera del rango permitido, debe ingresar el codigo de confirmación o modificar el valor del tipo de cambio " +
                                        "para que este se encuentre dentro de un rango valido .", "Info"));
                        inputTipoCambio.setValid(false);
                        codConfRequerido = true;
                        if (((String) (inputCodigoConfirmacion.getValue())).length() != LENGTH_COD_CONF) {
                            inputCodigoConfirmacion.setValid(false);
                        }
                    }*/
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "El tipo de cambio debe ser mayor a 0.", "Info"));
                }
            }
        }
    }

    /**
     * Cambio del monto del pago que se esta realizando
     */
    public void handleMontoChange() {
        if (emisor != null) {
            validarMontoComplemento();
            if (pagoTempContainer.getTipoCambioP() != 0) {
                //Monto en la moneda del pago, por el momento solo sera en MXN
                saldoDisponibleEmisor = pagoTempContainer.getMonto();
                //Lo convertimos a la moneda del DOCRELACIONADOS
                // saldoDisponibleEmisorMonedaDR = saldoDisponibleEmisor / docRelTempPago.getTipoCambioDR();
            }


            //inputCodigoConfirmacion.setValid(true);
            // TODO: 22/09/2017 VALIDAR EL MONTO DEL PAGO VS EL MONTO DE LOS DOCS RELACIONADOS


            //if (pagoTempContainer.getMonto() > montoComplemento) {
            //FacesContext.getCurrentInstance().addMessage("frmCompPago", new FacesMessage(FacesMessage.SEVERITY_ERROR,
            //          "La suma de los valores registrados en el nodo DoctoRelacionado, atributo ImpPagao,sea menor o igual que eel valor de este atributo", "Info"));
            //inputCodigoConfirmacion.setValid(false);
            //}
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Por favor selecciona al emisor", "Info"));
            pagoTempContainer.setMonto(0.0d);
        }
    }

    public void handleTipoCadPagoChange() {
        if (pagoTempContainer.getTipoCadPago().getClave() != null && pagoTempContainer.getTipoCadPago().getClave().contains("01")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Por favor ingrese los valores para los campos Certificado Pago,Cadena Pago y Sello Pago", "Info"));
            inputActivoCertPago = true;
            inputActivoCadPago = true;
            inputActivoSelloPago = true;
            cargaSpei = false;
        } else {
            pagoTempContainer.setCertPago("");
            pagoTempContainer.setCadPago("");
            pagoTempContainer.setSelloPago("");
            inputActivoCertPago = false;
            inputActivoCadPago = false;
            inputActivoSelloPago = false;
            cargaSpei = true;
        }
    }
    /**
     * Cargamos el xml donde extraeremos los datos necesarios para SPEI
     */

    public void cargarXmlSpei(FileUploadEvent e) {

        xmlSpei = e.getFile();

        if (xmlSpei.getContents() != null && xmlSpei.getSize() > 0) {
            byte[] xmlSpeiContent = xmlSpei.getContents();
            String str = new String(xmlSpeiContent);

            try {
                if (contieneSpei("SPEI_Tercero", xmlSpeiContent)) {

                    pagoTempContainer.setCertPago(this.getDataSpei("numeroCertificado", xmlSpeiContent));
                    pagoTempContainer.setSelloPago(this.getDataSpei("sello", xmlSpeiContent));
                    String auxCadena = this.getDataSpei("cadenaCDA", xmlSpeiContent);
                    String cad = "";

                    for (int i = 0; i < auxCadena.length(); i++) {
                        cad += auxCadena.charAt(i);

                        if (i > 2 && auxCadena.charAt(i) == '|' && auxCadena.charAt(i - 1) == '|') {
                            i = auxCadena.length();
                        }
                    }

                    pagoTempContainer.setCadPago(cad);

                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Por favor eliga un xml valido", "Info"));
                    pagoTempContainer.setCadPago("");
                    pagoTempContainer.setCertPago("");
                    pagoTempContainer.setSelloPago("");
                }


            } catch (Exception ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        ex.getMessage(), "Info"));
            }

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Por favor eliga un xml valido", "Info"));
        }


    }

    /**
     * Validaciones previas antes de construir el documento
     *
     * @return
     */
    public boolean preValidationFactura() {
        boolean res = true;
        inputResidenciaFiscal.setValid(true);
        inputNumRegIdTrib.setValid(true);


        if (pagos.size() != 1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Por favor agrege un pago al documento.", ""));
            return false;
        }
        if (cfdiRelacionados.size() > 2) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Por el momento solo se permite agregar un uuid de en el campo CFDIs relacionados.", ""));
            res = false;
        }
        if (!tipoRelacionCfdis.contains("04") && cfdiRelacionados.size() > 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "El tipo de relación de los CFDI´s relacionados debe ser 04.", ""));
            res = false;
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

        return res;
    }

    private boolean checkValorTipoCambioComplemento() {
        boolean res = true;
        if (pagoTempContainer.getMonedaP().getClave().toUpperCase().contains("MXN")) {
            if (tipoCambioPago == 1) {
                FacesContext.getCurrentInstance().addMessage("frmCompPago", new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "El tipo de cambio debe ser 1.0 cuando la moneda sea 'MXN'.", "Info"));
                res = false;
                inputTipoCambio.setValid(false);
            }
        }
        return res;
    }

    /**
     * Valida si el importe del pago no esta por encima de los limites
     */
    private void validarMaxTotal() {
        double tmpAmm = 0.0;
        codConfRequerido = false;
        if (!pagoTempContainer.getMonedaP().getClave().toUpperCase().contains("MXN") && !pagoTempContainer.getMonedaP().getClave().toUpperCase().contains("XXX")) {
            tmpAmm = pagoTempContainer.getMonto() * tipoCambioPago;
        }
        if (tmpAmm > MAX_VALUE_MONTO_PAGO) {
            codConfRequerido = true;
        }
    }


    public boolean validaSeleccionEmisor() {
        if (this.idEmpresa != -1) {
            return true;
        } else {
            FacesContext.getCurrentInstance().addMessage("frmCompPago", new FacesMessage(FacesMessage.SEVERITY_INFO, "Debe seleccionar al emisor.", "Info"));
            return false;
        }
    }

    public void actualizaDatos() {
        System.out.println("actualiza: " + idEmpresa);
        if (idEmpresa != -1) {
            emisor = daoEmp.BuscarEmpresaId(this.idEmpresa);
            this.receptores = daoRec.ListaReceptoresporIdemp(this.idEmpresa);
            this.folios = daoFolio.ListaFoliosidEmpresa(this.idEmpresa);
            pagosPendientes = daoPagos.getPagosPendientesByIdEmp(this.idEmpresa);
            pagos.clear();
            pagosSelection.clear();
            idCliente = -1;
            idFolio = -1;
            docsRelPago = new ArrayList<>();
            initDocRelTempPago();
            buscarRegimenByRfc();

            System.out.println("Tam pagos; " + pagosPendientes.size());
        } else {
            idCliente = -1;
        }

        /*
        MEmpresa tobj = daoEmp.BuscarEmpresaId(idEmpresa);
        if (tobj.getDireccion() != null) {
            if (tobj.getDireccion().getCp() != null && !tobj.getDireccion().getCp().isEmpty()) {
                MCcodigopostal ecp = daoCp.getCP(tobj.getDireccion().getCp());
                if (ecp != null) {
                    codigoPostal = ecp.getCodigoPostal() + "-" + ecp.getEstado();
                }
            }
        } else {
            codigoPostal = "";
        }
        */

    }


    private boolean checkRespuestaServicio(String arg) {
        respuestas = new ArrayList<>();
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

    public void buscarCodigoPostal() {
        if (descCp != null && !descCp.isEmpty()) {
            descCp = descCp.split("-")[0];
        } else {
            descCp = "";
        }
        if (!descCp.isEmpty()) {
            cps = daoCp.get50(descCp);
        } else {
            cps = new ArrayList<>();
        }
    }


    public void handleChangeTipoRelacionCfdi() {
        // TODO: 22/09/2017 CHECAR LAS VALIDACIONES NECESARIOE PARA EL TIPO DE RELACION
    }


    public String onFlowProcess(FlowEvent event) {
        //actualizaDatos();
        System.out.println("Old Step:" + event.getOldStep());
        System.out.println("New Step: " + event.getNewStep());

        // TODO: 25/09/2017 HACER VALIDACIONES EN CADA STEP DEL WIZARD
        if (event.getNewStep().contains("confirmation")) {
            if (docsRelPago.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_INFO,
                        "Es necesario que agrege el DoctoRelacionado del pago", ""));
                return "docsRel";
            }
        }

        if (skip) {
            skip = false;   //reset in case user goes back
            return "confirm";
        } else {
            return event.getNewStep();
        }


    }

    public void salvarCambiosPago() {
        double sumaMax = 0.0d;
        for (DoctoRelacionadoData dr : docsRelPago) {
            if (dr.getTipoCambioDR() == null) {
                //Moneda MXN
                sumaMax += dr.getImpPagado();
            } else {
                //Otra moneda
                // TODO: 09/10/2017 CONSIDERAR LA VARIACION DE LOS DECIMALES
                //sumaMax += dr.getImpPagado() * dr.getTipoCambioDR();

                sumaMax += dr.getImpPagado();
            }
        }
        //  if (sumaMax <= pagoTempContainer.getMonto()) {
        pagoTempContainer = agregarDocumentosRelacionadosPago(docsRelPago, pagoTempContainer);
        MCmoneda moneda = LambdasHelper.findMonedaByClave(monedas, pagoTempContainer.getMonedaP().getClave());
        MCformapago fP = LambdasHelper.findFormaPagoByClave(cfs, pagoTempContainer.getFormaDePagoP().getClave());

        pagoTempContainer.setMonedaP(new CatalogoData(moneda.getClave(), moneda.getDescripcion()));
        pagoTempContainer.setFormaDePagoP(new CatalogoData(fP.getCodigo(), fP.getDescripcion()));

        // TODO: 27/09/2017 USAR LOS CATALOGOS EN BD
           if (pagoTempContainer.getTipoCadPago().getClave() != null &&
                    !pagoTempContainer.getTipoCadPago().getClave().isEmpty()) {
                pagoTempContainer.setTipoCadPago(new CatalogoData("01", "SPEI"));
            }
        FacesMessage msg = new FacesMessage("Pago agregado a la factura.", "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        pagos.add(pagoTempContainer);
        this.initPago();
        flgDisabledBttonEliminarPago = false;
        /*} else {
            FacesMessage msg = new FacesMessage("Error de validación: La suma de los valores registrados en el campo ImpPagado " +
                    "de los apartados DoctoRelacionado no es menor o igual que el valor del campo Monto.", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }*/

    }

    public void modificarPago() {
        OPERATION_OVER_PAGO = MODIFICACION;
        if (OPERATION_OVER_PAGO == CREACION) {
            System.out.println("CREANDO DOCUMENTO");
        } else if (OPERATION_OVER_PAGO == MODIFICACION) {
            System.out.println("EDITANDO DOCUMENTO");
        }
        System.out.println("modificando pago");
    }


    public void handleFechaPagoChange() {
        System.out.println(pagoTempContainer.getFechaPago().toString());
    }

    public void handleDoctoRelTipoCambioChange() {
        inputDocRelTipoCambio.setValid(true);
        if (isRequiredTipoCambioDocRel()) {
            if (docRelTempPago.getTipoCambioDR() == 1.00) {
                inputDocRelTipoCambio.setValid(false);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_INFO,
                        "El tipo de cambio debe ser diferente de 1.0 cuando la moneda del 'DoctoRelacionado' sea diferente a la del 'Pago'.", ""));
            }
        } else {
            docRelTempPago.setTipoCambioDR(1.00);
        }

        // double a  = saldoDisponibleEmisor / docRelTempPago.getTipoCambioDR();//Monto disponible en la moneda del docrelacionado
        // saldoDisponibleEmisorMonedaDR = FloatsNumbersUtil.round6Places(a);


        //El saldo es editable en la interfaz de usuario
        double a = saldoDisponibleEmisor / docRelTempPago.getTipoCambioDR();//Monto disponible en la moneda del docrelacionado

        //if (a < (pagoPendienteSelection.getImpSaldoAnterior())) {
        //  docRelTempPago.setImpPagado(FloatsNumbersUtil.round6Places(a));//PONEMOS EL EN IMPORTE PAGADO toda EL SALDO DISPONIBLE YA QUE NO ALCAZNA
        //} else {
        //MAYOR O IGUAL ponemos el total

        docRelTempPago.setImpPagado(FloatsNumbersUtil.round(a, 2));//PAGO TOTAL

        // }
        //docRelTempPago.setImpSaldoInsoluto(docRelTempPago.getImpSaldoAnt() - docRelTempPago.getImpPagado());
        double aux = FloatsNumbersUtil.round(docRelTempPago.getImpSaldoAnt() - docRelTempPago.getImpPagado(), 2);
        docRelTempPago.setImpSaldoInsoluto(aux);


    }

    private String getAttributeComp(String att, String xml) throws Exception {

        String resultado = "";
        try {
            SAXBuilder sax = new SAXBuilder();

            Document doc2 = sax.build(new ByteArrayInputStream(xml.getBytes()));
            Namespace ns = doc2.getRootElement().getNamespace();

            Namespace ns2 = Namespace.getNamespace("tfd", "http://www.sat.gob.mx/TimbreFiscalDigital");

            resultado = doc2.getRootElement().getAttributeValue(att);
                    /*.getChild("Complemento", ns)
                    .getChild("TimbreFiscalDigital", ns2)
                    .getAttribute("UUID").getValue().trim();
*/
        } catch (JDOMException ex) {
            resultado = "";
        } catch (IOException ex) {
            resultado = "";
        }
        return resultado;

    }

    public String getDataSpei(String atributo, byte[] xml) throws Exception {

        String resultado = "";

        try {
            SAXBuilder sax = new SAXBuilder();
            Document doc = sax.build(new ByteArrayInputStream(xml));
            resultado = doc.getRootElement().getAttributeValue(atributo);

        } catch (JDOMException ex) {
            resultado = "";
        }

        return resultado;
    }

    public boolean contieneSpei(String name, byte[] xml) throws Exception {
        boolean flag = false;

        try {
            SAXBuilder sax = new SAXBuilder();
            Document doc = sax.build(new ByteArrayInputStream(xml));

            if (doc.getRootElement().getName().equalsIgnoreCase(name)) {
                flag = true;
            }

        } catch (JDOMException ex) {
            flag = false;
        }

        return flag;

    }

    public void actualizaDatosReceptor() {

    }

//    public void testGuaradPago(){
//
//        System.out.println("Guardando pagos");
//        MPagos pago = new MPagos();
//        if (mcfdi != null) {
//            pago.setCfdiId(mcfdi.getId());
//        } else {
//            pago.setCfdiId(0);
//        }
//        pago.setUuid("522485222455");
//        pago.setNumParcialidad(1);
//        pago.setImpSaldoAnterior(200.00);
//        if (dr.getImpSaldoInsoluto() == 0.0) {
//            pago.setPagado(true);
//        }
//        pago.setEmpId(idEmpresa);
//        pago.setMoneda(dr.getMonedaDR().getClave());
//
//        pago.setFecha(pg.getFechaPago());
//        System.out.println("save pago: " + pago.getImpSaldoAnterior());
//        daoPagos.savePago(pago);
//    }

}
