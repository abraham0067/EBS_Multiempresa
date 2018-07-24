package com.ebs.LeerExcel;

import com.ebs.util.TimeZoneCP;
import fe.sat.ComprobanteException;
import fe.sat.Direccion12Data;
import fe.sat.complementos.ComplementoException;
import fe.sat.complementos.v33.*;
import fe.sat.v33.*;
import fe.xml.CharUnicode;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jdom.Document;
import org.jdom.output.XMLOutputter;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;


public class ComplementoPagoExcel {

    int cont = 0;
    int contDocrel = 0;
    private List<Integer> numDR = new ArrayList<>();
    boolean flag = false;

    List<DoctoRelacionado> tempList = new ArrayList<>();

    List<List<DoctoRelacionado>> listaDoctosRel = new ArrayList<List<DoctoRelacionado>>();

    //--------------------------------EMISOR-------------------------------//
    private EmisorData emisorDataFactura;
    private Direccion12Data direccionFiscal12;
    //--------------------------------RECEPTOR----------------------------//
    private ReceptorData receptorData;
    //-------------------------------Conceptos----------------------------//


    private List<CfdiRelacionado> cfdiRelacionadoList;
    private CfdiRelacionadosData cfdisrel = new CfdiRelacionadosData();

    private PagoData pagoTempContainer;
    private List<PagoData> pagos;


    private DoctoRelacionado docRelTempPago;
    private List<DoctoRelacionado> docsRelPago;

    private ComprobanteData comprobanteData;

    private final String MXN_STR = "MXN";
    private final String PLANTILLAGRAL = "CFDIV33CP";//PLantilla complemento de pago

//    public static void main(String[] args) throws Exception {
//        ComplementoPagoExcel cpe = new ComplementoPagoExcel();
//        InputStream excelFile = new FileInputStream("C:\\test\\pago.xlsx");
//        cpe.readXLSFile(excelFile);
//        cpe.generarFactura();
//    }

    public ComplementoPagoExcel(InputStream excelFile) throws IOException {
        pagoTempContainer = new PagoData();
        pagos = new ArrayList<>();
        //docRelTempPago = new DoctoRelacionadoData();
        docsRelPago = new ArrayList<>();
        initComprobanteAsPago();
        readXLSFile(excelFile);
        generarFactura();
    }

//    public ComplementoPagoExcel() throws IOException {
//        pagoTempContainer = new PagoData();
//        pagos = new ArrayList<>();
//        //docRelTempPago = new DoctoRelacionadoData();
//        docsRelPago = new ArrayList<>();
//        initComprobanteAsPago();
//        // readXLSFile(excelFile);
//        // generarFactura();
//    }


    private String readValueInt(XSSFCell cell) {

        String value = "";

        if (cell.getCellType() == 0) {
            value = "" + (int) cell.getNumericCellValue();
        } else if (cell.getCellType() == 1) {
            value = cell.getStringCellValue();
        }

        return value;
    }

    private String readValue(XSSFCell cell) {

        String value = "";

        if (cell.getCellType() == 0) {
            value = "" + BigDecimal.valueOf(cell.getNumericCellValue());
        } else if (cell.getCellType() == 1) {
            value = cell.getStringCellValue();
        }

        return value;

    }

    private void readXLSFile(InputStream excelFile) throws IOException {

        //InputStream excelFile = new FileInputStream(name);
        XSSFWorkbook wb = new XSSFWorkbook(excelFile);

        XSSFSheet sheet = wb.getSheetAt(0);
        XSSFCell cell;
        XSSFRow row;

        Iterator rows = sheet.rowIterator();

        while (rows.hasNext()) {
            row = (XSSFRow) rows.next();
            Iterator cells = row.cellIterator();
            while (cells.hasNext()) {
                cell = (XSSFCell) cells.next();
                if (cell.getColumnIndex() == 0) {
                    switch ((int) cell.getNumericCellValue()) {
                        case 10: //Emisor
                            datosEmisor(cell, cells);
                            break;
                        case 20://DIRECCION EMISOR
                            datosDireccion(cell, cells, 1);
                            break;
                        case 30: //RECEPTOR
                            datosReceptor(cell, cells);
                            break;
                        case 40: //DIRECCION RECEPTOR
                            datosDireccion(cell, cells, 2);
                            break;
                        case 50://CFDI´s relacionados
                            comprobantesRelacionados(cell, cells);
                            break;
                        case 60://Información pago
                            informacionPago(cell, cells);
                            cont = 1;
                            //contDocrel = 0;
                            flag = true;
                            break;
                        case 70://Forma de pago
                            formaPago(cell, cells);
                            break;
                        case 80://Datos SPEI
                            datosSpei(cell, cells);
                            break;
                        case 90://Documento relacionado
                            documentoRelacionado(cell, cells);
                            contDocrel++;
                            break;
                        default:
                            break;
                    }
                }
            }

        }

        if((cont == 0) && !flag && (contDocrel > 0)){
            listaDoctosRel.add(docsRelPago);
        }
    }

    private void datosEmisor(XSSFCell cell, Iterator cells) {

        emisorDataFactura = new EmisorData();
        CatalogoData catalogoData = new CatalogoData();
        while (cells.hasNext()) {
            cell = (XSSFCell) cells.next();
            switch (cell.getColumnIndex()) {
                case 1:
                    emisorDataFactura.setRfc(cell.getStringCellValue());
                    break;
                case 2:
                    emisorDataFactura.setNombre(cell.getStringCellValue());
                    break;
                case 3:
                    int regimenFiscal = (int) cell.getNumericCellValue();
                    catalogoData.setClave("" + regimenFiscal);
                    //emisorDataFactura.setRegimenFiscal(new CatalogoData("" + regimenFiscal, ""));
                    break;
                case 4:
                    catalogoData.setDescripcion(cell.getStringCellValue());
                    break;
            }
        }

        emisorDataFactura.setRegimenFiscal(catalogoData);
    }

    private void datosDireccion(XSSFCell cell, Iterator cells, int direccion) {

        direccionFiscal12 = new Direccion12Data();

        while (cells.hasNext()) {
            cell = (XSSFCell) cells.next();
            switch (cell.getColumnIndex()) {
                case 1://Calle
                    direccionFiscal12.setCalle(cell.getStringCellValue());
                    break;
                case 2://NUMERO INTERIOR
                    direccionFiscal12.setNoInterior(readValueInt(cell));
                    break;
                case 3://NUMERO EXTERIOR
                    direccionFiscal12.setNoExterior(readValueInt(cell));
                    break;
                case 4://COLONIA
                    direccionFiscal12.setColonia(readValueInt(cell));
                    break;
                case 5://MUNICIPIO
                    direccionFiscal12.setMunicipio(readValueInt(cell));
                    break;
                case 6://ESTADO
                    direccionFiscal12.setEstado(readValueInt(cell));
                    break;
                case 7://PAIS
                    direccionFiscal12.setPais(readValueInt(cell));
                    break;
                case 8://CP
                    direccionFiscal12.setCP(readValueInt(cell));
                    break;
            }
        }

        direccionFiscal12.setReferencia("Ninguna");

        switch (direccion) {
            case 1:
                emisorDataFactura.setDireccionFiscalEmisor(direccionFiscal12);
                break;
            case 2:
                receptorData.setDireccionReceptor(direccionFiscal12);
                break;
        }
    }

    private void datosReceptor(XSSFCell cell, Iterator cells) {

        receptorData = new ReceptorData();
        CatalogoData catUso = new CatalogoData();
        CatalogoData catResidencia = new CatalogoData();
        while (cells.hasNext()) {
            cell = (XSSFCell) cells.next();
            switch (cell.getColumnIndex()) {
                case 1://RFC
                    receptorData.setRfc(cell.getStringCellValue());
                    break;
                case 2://Nombre
                    receptorData.setNombre(cell.getStringCellValue());
                    break;
                case 3://CLAVE RESIDENCIA FISCAL
                    catResidencia.setClave(cell.getStringCellValue());
                    break;
                case 4://DESCRIPCION RESIDENCIA FISCAL
                    catResidencia.setDescripcion(cell.getStringCellValue());
                    break;
                case 5://NUM REG ID TRIB
                    receptorData.setNumRegIdTrib(readValue(cell));
                    break;
            }
        }
        catUso.setClave("P01");
        catUso.setDescripcion("Por definir");
        receptorData.setUsoCFDI(catUso);
        receptorData.setResidenciaFiscal(catResidencia);
    }

    private void comprobantesRelacionados(XSSFCell cell, Iterator cells) {

        String uuid = "";
        String clave = "";
        String descripcion = "";

        while (cells.hasNext()) {
            cell = (XSSFCell) cells.next();
            switch (cell.getColumnIndex()) {
                case 1://Tipo de Relación
                    clave = readValue(cell);

                    break;
                case 2://Descripción
                    descripcion = readValue(cell);

                    break;
                case 3://UUID
                    uuid = readValue(cell);

                    break;
            }
        }

        CfdiRelacionado singleCfdi = new CfdiRelacionadoData();

        ((CfdiRelacionadoData) singleCfdi).setUuid(uuid);

        cfdiRelacionadoList.add(singleCfdi);

        CatalogoData catData = new CatalogoData(clave, descripcion);

        cfdisrel.setTipoRelacion(catData);
        cfdisrel.setCfdiRelacionado(cfdiRelacionadoList);

    }

    public void informacionPago(XSSFCell cell, Iterator cells) {

        CatalogoData lugarExpedicion = new CatalogoData();
        CatalogoData catMoneda = new CatalogoData();

        while (cells.hasNext()) {
            cell = (XSSFCell) cells.next();
            switch (cell.getColumnIndex()) {
                case 1://Serie
                    ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setSerie(readValue(cell));
                    break;
                case 2://Lugar de expedicion
                    lugarExpedicion.setClave(readValueInt(cell));
                    break;
                case 3://Descripcion
                    lugarExpedicion.setDescripcion(cell.getStringCellValue());
                    break;
                case 4://Fecha pago
                    pagoTempContainer.setFechaPago(cell.getDateCellValue());
                    break;
                case 5://Moneda pago
                    catMoneda.setClave(readValue(cell));
                    break;
                case 6://Descripción
                    catMoneda.setDescripcion(cell.getStringCellValue());
                    break;
                case 7://Tipo cambio
                    pagoTempContainer.setTipoCambioP(cell.getNumericCellValue());
                    break;
                case 8://Monto
                    pagoTempContainer.setMonto(cell.getNumericCellValue());
                    break;

            }
        }

        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setLugarExpedicion(lugarExpedicion);

        pagoTempContainer.setMonedaP(catMoneda);
    }

    public void formaPago(XSSFCell cell, Iterator cells) {

        CatalogoData catPago = new CatalogoData();

        while (cells.hasNext()) {
            cell = (XSSFCell) cells.next();
            switch (cell.getColumnIndex()) {
                case 1://Forma de pago
                    catPago.setClave(readValue(cell));
                    break;
                case 2://Descripcion
                    catPago.setDescripcion(readValue(cell));
                    break;
                case 3://Numero de operacion
                    pagoTempContainer.setNumOperacion(readValue(cell));
                    break;
                case 4://RfcEmisorCtaOrd
                    pagoTempContainer.setRfcEmisorCtaOrd(readValue(cell));
                    break;
                case 5://NomBancoOrdExt
                    pagoTempContainer.setNomBancoOrdExt(readValue(cell));
                    break;
                case 6://CtaOrdenante
                    pagoTempContainer.setCtaOrdenante(readValue(cell));
                    break;
                case 7://RfcEmisorCtaBen
                    pagoTempContainer.setRfcEmisorCtaBen(readValue(cell));
                    break;
                case 8://CtaBeneficiario
                    pagoTempContainer.setCtaBeneficiario(readValue(cell));
                    break;

            }
        }

        pagoTempContainer.setFormaDePagoP(catPago);
    }

    public void datosSpei(XSSFCell cell, Iterator cells) {

        CatalogoData catCadPago = new CatalogoData();

        while (cells.hasNext()) {
            cell = (XSSFCell) cells.next();
            switch (cell.getColumnIndex()) {
                case 1://TipoCadPago
                    catCadPago.setClave(readValue(cell));
                    break;
                case 2://Descripcion
                    catCadPago.setDescripcion(readValue(cell));
                    break;
                case 3://CertPago
                    pagoTempContainer.setCertPago(readValue(cell));
                    break;
                case 4://CadPago
                    pagoTempContainer.setCadPago(readValue(cell));
                    break;
                case 5://SelloPago
                    pagoTempContainer.setSelloPago(readValue(cell));
                    break;

            }
        }

        pagoTempContainer.setTipoCadPago(catCadPago);
    }

    public void documentoRelacionado(XSSFCell cell, Iterator cells) {


        if(cont == 1 && flag){
            listaDoctosRel.add(docsRelPago);

            pagos.add(pagoTempContainer);
            docsRelPago = new ArrayList<>();
            pagoTempContainer = new PagoData();
            numDR.add(contDocrel);
            contDocrel = 0;
            cont = 0;
            flag = false;
        }


        docRelTempPago = new DoctoRelacionadoData();
        CatalogoData catMoneda = new CatalogoData();
        CatalogoData catTipoCambio = new CatalogoData();
        CatalogoData metodoPago = new CatalogoData();

        while (cells.hasNext()) {
            cell = (XSSFCell) cells.next();

            switch (cell.getColumnIndex()) {
                case 1://UUID
                    ((DoctoRelacionadoData)docRelTempPago).setIdDocumento(readValue(cell));
                    break;
                case 2://Serie
                    ((DoctoRelacionadoData)docRelTempPago).setSerie(readValue(cell));
                    break;
                case 3://Folio
                    ((DoctoRelacionadoData)docRelTempPago).setFolio(readValue(cell));
                    break;
                case 4://Moneda dr
                    catMoneda.setClave(readValue(cell));
                    break;
                case 5://Descripcion
                    catMoneda.setDescripcion(readValue(cell));
                    break;
                case 6://Tipo Cambio dr

                    String aux = readValue(cell);

                    if (aux.equalsIgnoreCase("")) {
                        ((DoctoRelacionadoData)docRelTempPago).setTipoCambioDR(null);
                    } else {
                        ((DoctoRelacionadoData)docRelTempPago).setTipoCambioDR(cell.getNumericCellValue());
                    }

                    break;
                case 7://Metodo de pago dr
                    metodoPago.setClave(cell.getStringCellValue());
                    break;
                case 8://Descripción
                    metodoPago.setDescripcion(cell.getStringCellValue());
                    break;
                case 9://Numero de parcialidad
                    ((DoctoRelacionadoData)docRelTempPago).setNumParcialidad(Integer.parseInt(readValueInt(cell)));
                    break;
                case 10://Importe saldo anterior
                    ((DoctoRelacionadoData)docRelTempPago).setImpSaldoAnt(cell.getNumericCellValue());
                    break;
                case 11://importe saldo pagado
                    ((DoctoRelacionadoData)docRelTempPago).setImpPagado(cell.getNumericCellValue());
                    break;
                case 12://importe saldo insoluto
                    ((DoctoRelacionadoData)docRelTempPago).setImpSaldoInsoluto(cell.getNumericCellValue());
                    break;

            }
        }

        ((DoctoRelacionadoData)docRelTempPago).setMetodoDePagoDR(metodoPago);
        ((DoctoRelacionadoData)docRelTempPago).setMonedaDR(catMoneda);
        docsRelPago.add(docRelTempPago);

        //tempList.addAll(docsRelPago);

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

    private ComprobanteData agregarPagosAlComprobante(List<PagoData> pagos, ComprobanteData tmpComp) {
        List<Pago> tempList = new ArrayList<>(pagos);
        tempList.forEach(p -> {
//            if (p.getTipoCadPago() == null ||
//                    p.getTipoCadPago().getClave() == null ||
//                    p.getTipoCadPago().getClave().isEmpty()) {
//                ((PagoData) p).setTipoCadPago(null);
//                ((PagoData) p).setSelloPago(null);
//                ((PagoData) p).setCertPago(null);
//                ((PagoData) p).setCadPago(null);
//
//            }

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


    public void generarFactura() {

        comprobanteData.setEmisor(emisorDataFactura);
        comprobanteData.setReceptor(receptorData);

        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setFolio("1");
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setFolioErp("");
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setFecha(Calendar.getInstance().getTime());

        for (int k = 0; k< pagos.size(); k++){

            pagos.get(k).setDoctoRelacionado(listaDoctosRel.get(k+1));
        }

        this.comprobanteData = agregarPagosAlComprobante(pagos, this.comprobanteData);

        //----------------------------------------------------------------------------------------
        // PAREMETROS ADICIONALES
        //----------------------------------------------------------------------------------------
        this.comprobanteData = agregarParamsComprobante(comprobanteData, new ArrayList<String>() {{
            add("0");
        }});


        exeGenFactura(this.comprobanteData);

    }

    public ComprobanteData getComprobanteData() {
        return comprobanteData;
    }

    public PagoData getPagoTempContainer(){return pagoTempContainer;}

    public DoctoRelacionadoData getDocRelTempPago(){return ((DoctoRelacionadoData)docRelTempPago);}

    public List<PagoData> getListPago(){return pagos;}

    public String getRFC() {
        return emisorDataFactura.getRfc();
    }


    public String exeGenFactura(ComprobanteData cmp) {
        String resp = "---";
        try {
            //serializeObjectToFile(cmp);
            CFDIFactory33 cfdi = new CFDIFactory33();
            CharUnicode charUnicode = new CharUnicode();

            Document doc = cfdi.buildComprobanteDocConAddenda(cmp);// TODO: 12/06/2017 CHECK feConfig.xml NOT FOUND EXCENTION
            Document docp = cfdi.buildComprobanteDocPrintConAddenda(cmp);

            ///Convierte a string con encoding ISO-8859-1 para soportar los acentos
            /// se reemplaza el string de ISO-8859-1 por UTF-8
            ///Ser escapan los caracteres no soportados en UTF-8
            // XMLOutputter xout = new XMLOutputter();

            XMLOutputter xout = new XMLOutputter();
            xout.setFormat(xout.getFormat().setEncoding("ISO-8859-1"));
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            xout.output(docp, bout);

            String xmlPrint = new String(bout.toByteArray());
            xmlPrint = xmlPrint.replaceAll("ISO-8859-1", "UTF-8");
            xmlPrint = charUnicode.getTextEncoded2(xmlPrint);

            XMLOutputter xoutXml = new XMLOutputter();
            xoutXml.setFormat(xoutXml.getFormat().setEncoding("ISO-8859-1"));
            ByteArrayOutputStream boutXml = new ByteArrayOutputStream();
            xoutXml.output(doc, boutXml);
            String xml = new String(boutXml.toByteArray());
            xml = xml.replaceAll("ISO-8859-1", "UTF-8");
            xml = charUnicode.getTextEncoded2(xml);


            System.out.println(">>>>>>>>>> FACTURACION MANUAL XML_SAT :  <<< \n" + xml + "\n >>>");
            System.out.println(">>>>>>>>>> FACTURACION MANUAL XML_PRINT :  <<< \n" + xmlPrint + "\n >>>");

//            byte[] cdires = com.ebs.fe.wsgi.cliet.Cliente.genInvoice(Zipper.compress("cfdi", xml.getBytes()), clave);
            /**
             * com.ebs.fe.wsgi.client.TestCliente.genInvoice(Zipper.compress("cfdi",
             * xml.getBytes()),clave);
             */


        } catch (ComprobanteException ce) {
            ce.printStackTrace();
            resp = "Error---" + ce.getMessage();
        } catch (ComplementoException e) {
            e.printStackTrace();
            resp = "Error--" + e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            resp = "Error-" + e.getMessage();

        } catch (Exception e) {
            e.printStackTrace();
            resp = "Error-:...." + e.getMessage();
        }
        return resp;
    }

}
