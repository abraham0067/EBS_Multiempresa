package com.ebs.LeerExcel;

import com.ebs.complementoextdata.CustomComplementoComercioExteriorMetadata;
import com.ebs.util.TimeZoneCP;
import fe.sat.CommentFE;
import fe.sat.CommentFEData;
import fe.sat.Direccion12Data;
import fe.sat.complementos.comercioexterior.*;
import fe.sat.complementos.v33.ComplementoFe;
import fe.sat.v33.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class LeerDatosExcel {

    private ComprobanteData comprobanteData;
    private static final String PLANTILLAGRAL = "CFDIV33";//INGRESO Y EGRESO
    private String rfc;
    //--------------------------------EMISOR-------------------------------//
    private EmisorData emisorDataFactura;
    private Direccion12Data direccionFiscal12;
    //--------------------------------RECEPTOR----------------------------//
    private ReceptorData receptorData;
    //-------------------------------Conceptos----------------------------//
    private List<ConceptoData> listAuxCptData;
    //-------------------------------IMPUESTOS----------------------------//
    private List<RetencionConcepto> listRetencionesCpto;
    private List<TrasladoConcepto> listTrasladodsCpto;
    private int cont;
    private List<Retencion> listRetencionesComp;
    private List<Traslado> listTrasladosComp;
    private ImpuestosData impComp;
    private double totalTranfer = 0.00;
    private double totalReten = 0.00;
    //--------------------------COMERCIO EXTERIOR--------------------------//
    private DomicilioComercioData domicilioComercioData;
    private boolean disponibleComercioExterior;
    private EmisorComercioData emisorData;
    private ReceptorComercioData receptorComercio;
    private DestinatarioComercioData destinatarioComercioData;
    private PropietarioComercioData propietarioComercioData;
    private CustomComplementoComercioExteriorMetadata singleComplementoComercioExteriorData;
    private List<MercanciaComercio> mercanciasFe;

//    public static void main(String[] args) throws IOException {
//
//        LeerDatosExcel leerExcel = new LeerDatosExcel();
//
//    }

    public LeerDatosExcel(InputStream excelFile) throws IOException {

        rfc = "";
        comprobanteData = new ComprobanteData();
        comprobanteData.setDatosComprobante(new DatosComprobanteData());
        disponibleComercioExterior = false;
        singleComplementoComercioExteriorData = new CustomComplementoComercioExteriorMetadata();
        mercanciasFe = new ArrayList<>();
        listRetencionesCpto = new ArrayList<>();
        listTrasladodsCpto = new ArrayList<>();
        listRetencionesCpto = new ArrayList<>();
        listAuxCptData = new ArrayList<>();
        listTrasladosComp = new ArrayList<>();
        listRetencionesComp = new ArrayList<>();
        impComp = new ImpuestosData();
        this.readXLSFile(excelFile);
        this.generarComprobanteData();
    }

    public String readValue(XSSFCell cell) {

        String value = "";

        if (cell.getCellType() == 0) {
            value = "" + BigDecimal.valueOf(cell.getNumericCellValue());
        } else if (cell.getCellType() == 1) {
            value = cell.getStringCellValue();
        }

        return value;

    }


    public String readValueInt(XSSFCell cell) {

        String value = "";

        if (cell.getCellType() == 0) {
            value = "" + (int) cell.getNumericCellValue();
        } else if (cell.getCellType() == 1) {
            value = cell.getStringCellValue();
        }

        return value;

    }

    public void readXLSFile(InputStream excelFile) throws IOException {

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
                            datosDireccion(cell,cells,1);
                            break;
                        case 30: //Datos comercio exterior
                            System.out.println("Uso comercio exterior");
                            disponibleComercioExterior = true;
                            datosComercioExterior(cell, cells);
                            break;
                        case 40: //DIRECCION EMISOR COMERCIO EXTERIOR
                            datosDireccionCC(cell, cells, 1);
                            break;
                        case 50: //PROIETARIO COMERCIO EXTERIOR
                            propietarioComercioExterior(cell, cells);
                            break;
                        case 60: //RECEPTOR
                            datosReceptor(cell, cells);
                            break;
                        case 70: //DIRECCION RECEPTOR
                            datosDireccion(cell, cells, 2);
                            break;
                        case 80://DIRECCION RECEPTOR COMERCIO EXTERIOR
                            datosDireccionCC(cell,cells,2);
                            break;
                        case 90: //DIRECCION DESTINATARIO COMERCIO EXTERIOR
                            datosDireccionCC(cell, cells, 3);
                            break;
                        case 100: //DATOS FACTURA
                            datosFactura(cell, cells);
                            break;
                        case 110: //CONCEPTO
                            concepto(cell, cells);
                            break;
                        case 120: //TRASLADOS
                            impuestosTraslados(cell, cells);
                            break;
                        case 130: //RETENCIONES
                            impuestosRetenciones(cell, cells);
                            break;
                        case 140: //MERCANCIAS COMERCIO EXTERIOR
                            mercanciasComercioExterior(cell, cells);
                            break;
                        case 150: //TOTAL IMPUESTOS TRASLADOS
                            totalImpuestosTraslados(cell, cells);
                            break;
                        case 160: //TOTAL IMPUESTOS RETENCIONES
                            totalImpuestosRetenciones(cell, cells);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }


    public void datosEmisor(XSSFCell cell, Iterator cells) {

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

    public void datosComercioExterior(XSSFCell cell, Iterator cells) {

        while (cells.hasNext()) {
            cell = (XSSFCell) cells.next();
            switch (cell.getColumnIndex()) {
                case 1://MotivoTraslado
                    singleComplementoComercioExteriorData.setMotivoTraslado(cell.getStringCellValue());
                    break;
                case 2://Tipo Operación
                    singleComplementoComercioExteriorData.setTipoOperacion("" + (int) cell.getNumericCellValue());
                    break;
                case 3://clave pedimento
                    singleComplementoComercioExteriorData.setClaveDePedimento(readValue(cell));
                    break;
                case 4://Certificado origen
                    singleComplementoComercioExteriorData.setCertificadoOrigen("" + (int) cell.getNumericCellValue());
                    break;
                case 5://Num Certificado Origen
                    singleComplementoComercioExteriorData.setNumCertificadoOrigen(cell.getStringCellValue());
                    break;
                case 6://Num Exportador Confiable
                    singleComplementoComercioExteriorData.setNumExportadorConfiable(cell.getStringCellValue());
                    break;
                case 7://Incoterm
                    singleComplementoComercioExteriorData.setIncoterm(cell.getStringCellValue());
                    break;
                case 8://Subdivision
                    singleComplementoComercioExteriorData.setSubdivision("" + (int) cell.getNumericCellValue());
                    break;
                case 9://Observaciones
                    singleComplementoComercioExteriorData.setObservaciones(cell.getStringCellValue());
                    break;
                case 10://Tipo cambio USD
                    singleComplementoComercioExteriorData.setTipoCambioUSD(cell.getNumericCellValue());
                    break;
                case 11://Total USD
                    singleComplementoComercioExteriorData.setTotalUSD(cell.getNumericCellValue());
                    break;
            }
        }
    }

    public void datosDireccion(XSSFCell cell, Iterator cells, int direccion) {

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

    public void datosDireccionCC(XSSFCell cell, Iterator cells, int direccion) {

        domicilioComercioData = new DomicilioComercioData();
        direccionFiscal12 = new Direccion12Data();
        String regIdTrib = "";
        String nombre = "";

        while (cells.hasNext()) {
            cell = (XSSFCell) cells.next();
            switch (cell.getColumnIndex()) {
                case 1://Calle
                    domicilioComercioData.setCalle(cell.getStringCellValue());
                    direccionFiscal12.setCalle(cell.getStringCellValue());
                    break;
                case 2://NUMERO INTERIOR
                    domicilioComercioData.setNumeroInterior(readValueInt(cell));
                    direccionFiscal12.setNoInterior(readValueInt(cell));
                    break;
                case 3://NUMERO EXTERIOR
                    domicilioComercioData.setNumeroExterior(readValueInt(cell));
                    direccionFiscal12.setNoExterior(readValueInt(cell));
                    break;
                case 4://COLONIA
                    domicilioComercioData.setColonia(readValueInt(cell));
                    direccionFiscal12.setColonia(readValueInt(cell));
                    break;
                case 5://MUNICIPIO
                    domicilioComercioData.setMunicipio(readValueInt(cell));
                    direccionFiscal12.setMunicipio(readValueInt(cell));
                    break;
                case 6://ESTADO
                    domicilioComercioData.setEstado(readValueInt(cell));
                    direccionFiscal12.setEstado(readValueInt(cell));
                    break;
                case 7://PAIS
                    domicilioComercioData.setPais(readValueInt(cell));
                    direccionFiscal12.setPais(readValueInt(cell));
                    break;
                case 8://CP
                    domicilioComercioData.setCodigoPostal(readValueInt(cell));
                    direccionFiscal12.setCP(readValueInt(cell));
                    break;
                case 9://num reg trib solo DESTINATARIO CE
                    regIdTrib = readValueInt(cell);
                    break;
                case 10://nombre solo destinatario CE
                    nombre = cell.getStringCellValue();
                    break;
            }
        }

        direccionFiscal12.setReferencia("Ninguna");

        switch (direccion) {
            case 1:
                emisorData = new EmisorComercioData();
                emisorData.setDomicilioComercio(domicilioComercioData);
                emisorDataFactura.setDireccionFiscalEmisor(direccionFiscal12);
                break;
            case 2:
                receptorComercio = new ReceptorComercioData();
                receptorComercio.setDomicilioComercio(domicilioComercioData);
                receptorData.setDireccionReceptor(direccionFiscal12);
                break;
            case 3:
                List<DomicilioComercio> domicilios = new ArrayList<>();
                domicilios.add(domicilioComercioData);
                destinatarioComercioData = new DestinatarioComercioData();
                destinatarioComercioData.setDomicilio(domicilios);
                destinatarioComercioData.setNombre(nombre);
                destinatarioComercioData.setNumRegIdTrib(regIdTrib);
                break;
        }
    }

    public void propietarioComercioExterior(XSSFCell cell, Iterator cells) {

        propietarioComercioData = new PropietarioComercioData();
        while (cells.hasNext()) {
            cell = (XSSFCell) cells.next();
            switch (cell.getColumnIndex()) {
                case 1://NumRegId
                    propietarioComercioData.setNumRegIdTrib(readValue(cell));
                    break;
                case 2://ResidenciaFiscal
                    propietarioComercioData.setResidenciaFiscal(cell.getStringCellValue());
                    break;
            }
        }
    }

    public void datosReceptor(XSSFCell cell, Iterator cells) {

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

    public void datosFactura(XSSFCell cell, Iterator cells) {

        CatalogoData catTipo = new CatalogoData();
        CatalogoData catMoneda = new CatalogoData();
        CatalogoData catMetodo = new CatalogoData();
        CatalogoData catForma = new CatalogoData();
        CatalogoData catExpedicion = new CatalogoData();

        while (cells.hasNext()) {
            cell = (XSSFCell) cells.next();
            switch (cell.getColumnIndex()) {
                case 1://CLAVE TIPO DE COMPROBANTE
                    catTipo.setClave(cell.getStringCellValue());
                    break;
                case 2://DESCRIPCIÓN TIPO COMPROBANTE
                    catTipo.setDescripcion(cell.getStringCellValue());
                    break;
                case 3://SERIE
                    ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setSerie(cell.getStringCellValue());
                    break;
                case 4://CLAVE MONEDA
                    catMoneda.setClave(cell.getStringCellValue());
                    break;
                case 5://DESCRIPCIÓN MONEDA
                    catMoneda.setDescripcion(cell.getStringCellValue());
                    break;
                case 6://TIPO CAMBIO
                    ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setTipoDeCambio(cell.getNumericCellValue());
                    break;
                case 7://CLAVE METODO PAGO
                    catMetodo.setClave(cell.getStringCellValue());
                    break;
                case 8://DESCRIPCION METODO PAGO
                    catMetodo.setDescripcion(cell.getStringCellValue());
                    break;
                case 9://CLAVE FORMA PAGO
                    catForma.setClave(cell.getStringCellValue());
                    break;
                case 10:
                    catForma.setDescripcion(cell.getStringCellValue());
                    break;
                case 11://LUGAR EXPEDICION
                    int lugar = (int) cell.getNumericCellValue();
                    catExpedicion.setClave("" + lugar);
                    break;
                case 12:
                    catExpedicion.setDescripcion(cell.getStringCellValue());
                    break;
                case 13://SUBTOTAL
                    ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setSubTotal(cell.getNumericCellValue());
                    break;
                case 14://DESCUENTO
                    ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setDescuento(cell.getNumericCellValue());
                    break;
                case 15://TOTAL
                    ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setTotal(cell.getNumericCellValue());
                    break;
            }
        }

        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setTipoDeComprobante(catTipo);
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setMoneda(catMoneda);
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setMetodoDePago(catMetodo);
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setFormaDePago(catForma);
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setLugarExpedicion(catExpedicion);
    }

    public void concepto(XSSFCell cell, Iterator cells) {

        ConceptoData cptoData = new ConceptoData();

        while (cells.hasNext()) {
            cell = (XSSFCell) cells.next();
            switch (cell.getColumnIndex()) {
                case 1://NoIdentificacion
                    cptoData.setNoIdentificacion(readValue(cell));
                    break;
                case 2://Cantidad
                    cptoData.setCantidad(cell.getNumericCellValue());
                    break;
                case 3://ClaveUnidad
                    cptoData.setClaveUnidad(new CatalogoData(readValue(cell), ""));
                    break;
                case 4://Unidad
                    cptoData.setUnidad(cell.getStringCellValue());
                    break;
                case 5://ClaveProdServ
                    cptoData.setClaveProdServ(new CatalogoData(readValueInt(cell), ""));
                    break;
                case 6://Descripcion
                    cptoData.setDescripcion(cell.getStringCellValue());
                    break;
                case 7://ValorUnitario
                    cptoData.setValorUnitario(cell.getNumericCellValue());
                    break;
                case 8://Importe
                    cptoData.setImporte(cell.getNumericCellValue());
                    break;
                case 9://Descuento
                    cptoData.setDescuento(cell.getNumericCellValue());
                    break;
                case 10://NumeroPedimento
                    InformacionAduaneraData informacion = new InformacionAduaneraData();
                    informacion.setNumero(readValue(cell));
                    List<InformacionAduanera> listInfo = new ArrayList<>();
                    listInfo.add(informacion);

                    cptoData.setInformacionAduanera(listInfo);
                    break;
            }
        }

        cont++;
        listTrasladodsCpto.add(null);
        listRetencionesCpto.add(null);

        listAuxCptData.add(cptoData);
    }

    public void impuestosTraslados(XSSFCell cell, Iterator cells) {

        TrasladoConceptoData tcd = new TrasladoConceptoData();
        CatalogoData catImpuesto = new CatalogoData();
        while (cells.hasNext()) {
            cell = (XSSFCell) cells.next();
            switch (cell.getColumnIndex()) {
                case 1://Base
                    tcd.setBase(cell.getNumericCellValue());
                    break;
                case 2://Clave Impuesto
                    catImpuesto.setClave(cell.getStringCellValue());
                    break;
                case 3://Descripción impuesto
                    catImpuesto.setDescripcion(cell.getStringCellValue());
                    break;
                case 4://TasaOCuota
                    tcd.setTasaOCuota(cell.getNumericCellValue());
                    break;
                case 5://TipoFactor
                    tcd.setTipoFactor(cell.getStringCellValue());
                    break;
                case 6://Importe
                    tcd.setImporte(cell.getNumericCellValue());
                    break;
            }
        }

        tcd.setImpuesto(catImpuesto);

        listTrasladodsCpto.remove(cont);
        listTrasladodsCpto.add(cont, tcd);
    }

    public void impuestosRetenciones(XSSFCell cell, Iterator cells) {

        RetencionConceptoData rcd = new RetencionConceptoData();
        CatalogoData catImpuestoRetencion = new CatalogoData();

        while (cells.hasNext()) {
            cell = (XSSFCell) cells.next();
            switch (cell.getColumnIndex()) {
                case 1://Base
                    rcd.setBase(cell.getNumericCellValue());
                    break;
                case 2://Clave Impuesto
                    catImpuestoRetencion.setClave(cell.getStringCellValue());
                    break;
                case 3://Descripción Impuesto
                    catImpuestoRetencion.setDescripcion(cell.getStringCellValue());
                    break;
                case 4://TasaOCuota
                    rcd.setTasaOCuota(cell.getNumericCellValue());
                    break;
                case 5://TipoFactor
                    rcd.setTipoFactor(cell.getStringCellValue());
                    break;
                case 6://Importe
                    rcd.setImporte(cell.getNumericCellValue());
                    break;
            }
        }

        rcd.setImpuesto(catImpuestoRetencion);

        listRetencionesCpto.remove(cont);
        listRetencionesCpto.add(cont, rcd);

    }

    public void mercanciasComercioExterior(XSSFCell cell, Iterator cells) {

        MercanciaComercioData temp = new MercanciaComercioData();

        while (cells.hasNext()) {
            cell = (XSSFCell) cells.next();
            switch (cell.getColumnIndex()) {
                case 1://NoIdentificacion
                    temp.setIdentificacion(readValueInt(cell));
                    break;
                case 2://Cantidad Aduana
                    temp.setCantidadAduana(cell.getNumericCellValue());
                    break;
                case 3://FraccionArancelaria
                    temp.setFraccionArancelaria(readValueInt(cell));
                    break;
                case 4://Unidad Aduana
                    temp.setUnidadAduana(cell.getStringCellValue());
                    break;
                case 5://ValorDolares
                    temp.setValorDolares(cell.getNumericCellValue());
                    break;
                case 6://ValorUnitarioAduana
                    temp.setValorUnitarioAduana(cell.getNumericCellValue());
                    break;
                case 7://Maraca
                    List<DescripcionesEspecificas> descfe = new ArrayList<>();
                    DescripcionesEspecificasData descrips = new DescripcionesEspecificasData();
                    descrips.setMarca(cell.getStringCellValue());
                    descfe.add(descrips);
                    temp.setDescripcionesEspecificas(descfe);
                    break;
            }
        }
        mercanciasFe.add(temp);
    }

    public void totalImpuestosTraslados(XSSFCell cell, Iterator cells) {
        TrasladoData tcd = new TrasladoData();

        while (cells.hasNext()) {
            cell = (XSSFCell) cells.next();
            switch (cell.getColumnIndex()) {
                case 1://Impuesto
                    tcd.setImpuesto(new CatalogoData(readValue(cell), ""));
                    break;
                case 2://TasaOCuota
                    tcd.setTasaOCuota(cell.getNumericCellValue());
                    break;
                case 3://TipoFactor
                    tcd.setTipoFactor(cell.getStringCellValue());
                    break;
                case 4://Importe
                    tcd.setImporte(cell.getNumericCellValue());
                    totalTranfer = cell.getNumericCellValue();
                    break;
            }
        }

        listTrasladosComp.add(tcd);
    }

    public void totalImpuestosRetenciones(XSSFCell cell, Iterator cells) {

        RetencionData reten = new RetencionData();
        while (cells.hasNext()) {
            cell = (XSSFCell) cells.next();
            switch (cell.getColumnIndex()) {
                case 1://Impuesto
                    reten.setImpuesto(new CatalogoData(readValue(cell), ""));
                    break;
                case 2://Importe
                    reten.setImporte(cell.getNumericCellValue());
                    totalReten = cell.getNumericCellValue();//add importe
                    break;
            }
        }
        listRetencionesComp.add(reten);
    }

    public void addComercioExterior() {

        ComercioExteriorData comercioExteriorData = new ComercioExteriorData();

        comercioExteriorData.setEmisorComercio(emisorData);
        comercioExteriorData.setReceptorComercio(receptorComercio);
        List<DestinatarioComercio> destinatarios = new ArrayList<>();
        destinatarios.add(destinatarioComercioData);
        comercioExteriorData.setDestinatarioComercio(destinatarios);
        List<PropietarioComercio> propietarioComercioList = new ArrayList<>();
        propietarioComercioList.add(propietarioComercioData);
        comercioExteriorData.setPropietarioComercio(propietarioComercioList);

        comercioExteriorData.setMotivoTraslado(singleComplementoComercioExteriorData.getMotivoTraslado());
        comercioExteriorData.setTipoOperacion(singleComplementoComercioExteriorData.getTipoOperacion());
        comercioExteriorData.setClaveDePedimento(singleComplementoComercioExteriorData.getClaveDePedimento());
        comercioExteriorData.setCertificadoOrigen(Integer.parseInt(singleComplementoComercioExteriorData.getCertificadoOrigen()));
        comercioExteriorData.setNumeroExportadorConfiable(singleComplementoComercioExteriorData.getNumExportadorConfiable());
        comercioExteriorData.setIncoterm(singleComplementoComercioExteriorData.getIncoterm());
        comercioExteriorData.setSubdivision(Integer.parseInt(singleComplementoComercioExteriorData.getSubdivision()));
        comercioExteriorData.setObservaciones(singleComplementoComercioExteriorData.getObservaciones());
        comercioExteriorData.setTipoCambioUSD(singleComplementoComercioExteriorData.getTipoCambioUSD());
        comercioExteriorData.setTotalUSD(singleComplementoComercioExteriorData.getTotalUSD());

        comercioExteriorData.setMercanciaComercio(mercanciasFe);

       /* for (int i = 0; i < mercanciasFe.size(); i++) {
            System.out.println(mercanciasFe.get(i).getNoIdentificacion());
            System.out.println(mercanciasFe.get(i).getFraccionArancelaria());
            for (int j = 0; j < mercanciasFe.get(i).getDescripcionesEspecificas().size(); j++) {
                System.out.println(mercanciasFe.get(i).getDescripcionesEspecificas().get(j).getMarca());
            }
        }*/

        List<ComplementoFe> complementos = comprobanteData.getComplementosFe();
        if (complementos == null) {
            complementos = new ArrayList<>();
        }
        complementos.add(comercioExteriorData);
        comprobanteData.setComplemementosFe(complementos);

    }

    public void generarComprobanteData() {
        comprobanteData.setEmisor(emisorDataFactura);
        comprobanteData.setReceptor(receptorData);
        String comentarios = "";
        List<CommentFE> arrComentarios = new ArrayList<CommentFE>();
        CommentFEData comentario = new CommentFEData();
        comentario.setComment(comentarios);
        comentario.setPosition("9999");
        arrComentarios.add(comentario);
        comprobanteData.setComments9999(arrComentarios);
        // =========================================== Datos dummy para que las validaciones iniciales del proceso de captura manual, no genere errores de datos==========================
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setFolio("1");
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setFecha(
                (new TimeZoneCP().getUTC(
                        comprobanteData.getDatosComprobante().getLugarExpedicion().getClave())
                ));
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setSello("qQSvHZPzRB7eT6f9dJs3GepVl82eq4aOiI4b2hK+YSTcaJXgrDm8GfmZyUMnJ5XLs/TZDtAeafF5W1oyEygqva5fA3Ga5agq9HKHMEZx2qCOgOB+97C26StVKUdGD3jcPCh64AEgXLdCftIPwiRXP6eAr0IeeaujjVdEobvuxyo=");
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setNoCertificado("00001000000103168809");//Mas de 20 caracteres
        // ================================================================================================================================================================================

        List<Concepto> listaConceptoData = new ArrayList<>();

        for (int i = 0; i < listAuxCptData.size(); i++) {
            List<TrasladoConcepto> auxTraslado = new ArrayList<>();
            List<RetencionConcepto> auxRetencion = new ArrayList<>();

            ImpuestosConceptoData imp = new ImpuestosConceptoData();

            if (listTrasladodsCpto.get(i) != null) {
                auxTraslado.add(listTrasladodsCpto.get(i));
                imp.setTraslados(auxTraslado);
            }
            if (listRetencionesCpto.get(i) != null) {
                auxRetencion.add(listRetencionesCpto.get(i));
                imp.setRetenciones(auxRetencion);
            }
            if (auxRetencion.isEmpty() & auxTraslado.isEmpty()) {
                listAuxCptData.get(i).setImpuestosConcepto(null);
            } else {
                listAuxCptData.get(i).setImpuestosConcepto(imp);
            }
            listaConceptoData.add(listAuxCptData.get(i));
        }
        comprobanteData.setConceptos(listaConceptoData);

        if (disponibleComercioExterior) {
           // ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setTotal(0.00);
            //((DatosComprobanteData) comprobanteData.getDatosComprobante()).setSubTotal(0.00);
            addComercioExterior();
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

        ///------------------------ADDITIONAL DATA
        comprobanteData.setAdditional(new AdditionalData());

        ((AdditionalData) getComprobanteData().getAdditional()).setPlantilla(PLANTILLAGRAL);//PLANTILLA CFDIV33

    }

    public ComprobanteData getComprobanteData() {
        return comprobanteData;
    }

    public String getRFC() {
        return emisorDataFactura.getRfc();
    }

}
