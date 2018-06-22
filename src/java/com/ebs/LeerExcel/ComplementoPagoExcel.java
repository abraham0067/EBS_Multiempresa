package com.ebs.LeerExcel;

import fe.sat.Direccion12Data;
import fe.sat.complementos.v33.DoctoRelacionadoData;
import fe.sat.complementos.v33.PagoData;
import fe.sat.v33.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;


public class ComplementoPagoExcel {

    int cont = 0;
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


    private DoctoRelacionadoData docRelTempPago;
    private List<DoctoRelacionadoData> docsRelPago;

    private ComprobanteData comprobanteData;

    public ComplementoPagoExcel() {
        pagoTempContainer = new PagoData();
        pagos = new ArrayList<>();
        docRelTempPago = new DoctoRelacionadoData();
        docsRelPago = new ArrayList<>();
        initComprobanteAsPago();

    }


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
        boolean flag = false;
        cont = -1;

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
                            break;
                        case 70://Forma de pago
                            formaPago(cell, cells);
                            break;
                        case 80://Datos SPEI
                            datosSpei(cell, cells);
                            break;
                        case 90://Documento relacionado
                            docuementoRelacioando(cell, cells);
                            break;
                        default:
                            break;
                    }
                }
            }
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
                case 3://CLAVE USO CFDI
                    catUso.setClave(cell.getStringCellValue());
                    break;
                case 4://DESCRIPCIÓN USO CFDI
                    catUso.setDescripcion(cell.getStringCellValue());
                    break;
                case 5://CLAVE RESIDENCIA FISCAL
                    catResidencia.setClave(cell.getStringCellValue());
                    break;
                case 6://DESCRIPCION RESIDENCIA FISCAL
                    catResidencia.setDescripcion(cell.getStringCellValue());
                    break;
                case 7://NUM REG ID TRIB
                    receptorData.setNumRegIdTrib(readValue(cell));
                    break;
            }
        }
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
                    System.out.println("clave: " + clave);
                    break;
                case 2://Descripción
                    descripcion = readValue(cell);
                    System.out.println("descripcion: " + descripcion);
                    break;
                case 3://UUID
                    uuid = readValue(cell);
                    System.out.println("uuid: " + uuid);
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
        CatalogoData  catMoneda = new CatalogoData();

        while (cells.hasNext()) {
            cell = (XSSFCell) cells.next();
            switch (cell.getColumnIndex()) {
                case 1://Serie
                    ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setSerie(readValue(cell));
                    break;
                case 2://Lugar de expedicion
                    lugarExpedicion.setClave(readValue(cell));
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

        while (cells.hasNext()) {
            cell = (XSSFCell) cells.next();
            switch (cell.getColumnIndex()) {
                case 1://Forma de pago

                    break;
                case 2://Descripcion

                    break;
                case 3://Numero de operacion

                    break;
                case 4://RfcEmisorCtaOrd

                    break;
                case 5://NomBancoOrdExt
                    break;
                case 6://CtaOrdenante
                    break;
                case 7://RfcEmisorCtaBen
                    break;
                case 8://CtaBeneficiario
                    break;

            }
        }
    }

    public void datosSpei(XSSFCell cell, Iterator cells) {

        while (cells.hasNext()) {
            cell = (XSSFCell) cells.next();
            switch (cell.getColumnIndex()) {
                case 1://TipoCadPago

                    break;
                case 2://Descripcion

                    break;
                case 3://CertPago

                    break;
                case 4://CadPago

                    break;
                case 5://SelloPago
                    break;

            }
        }
    }

    public void docuementoRelacioando(XSSFCell cell, Iterator cells) {

        while (cells.hasNext()) {
            cell = (XSSFCell) cells.next();
            switch (cell.getColumnIndex()) {
                case 1://UUID

                    break;
                case 2://Serie

                    break;
                case 3://Folio

                    break;
                case 4://Moneda dr

                    break;
                case 5://Descripcion
                    break;
                case 6://Tipo Cambio dr
                    break;
                case 7://Descripción
                    break;
                case 8://Metodo de pago dr
                    break;
                case 9://Numero de parcialidad
                    break;
                case 10://Importe saldo anterior
                    break;
                case 11://importe saldo pagado
                    break;
                case 12://importe saldo insoluto
                    break;

            }
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

    public void generarFactura() {

        // ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setLugarExpedicion(lugarExpedicion);
        // ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setSerie(folio.getId());
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setFolio("1");
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setFolioErp("");
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setFecha(Calendar.getInstance().getTime());
    }

}
