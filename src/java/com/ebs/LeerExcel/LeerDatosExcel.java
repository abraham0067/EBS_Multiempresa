package com.ebs.LeerExcel;

import com.ebs.complementoextdata.CustomComplementoComercioExteriorMetadata;
import com.ebs.util.TimeZoneCP;
import fe.db.ConceptoFactura;
import fe.db.MEmpresa;
import fe.db.MFolios;
import fe.db.MReceptor;
import fe.model.dao.EmpresaDAO;
import fe.model.dao.FoliosDAO;
import fe.model.dao.ReceptorDAO;
import fe.model.util.CapturaManualModelo;
import fe.sat.CommentFE;
import fe.sat.CommentFEData;
import fe.sat.complementos.comercioexterior.*;
import fe.sat.complementos.v33.ComplementoFe;
import fe.sat.v33.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.wsdl.Input;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class LeerDatosExcel {

    private ComprobanteData comprobanteData;
    private static final String PLANTILLAGRAL = "CFDIV33";//INGRESO Y EGRESO
    //--------------------------------EMISOR-------------------------------//
    private EmisorData emisorDataFactura;
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
                        case 20: //Datos comercio exterior
                            System.out.println("Uso comercio exterior");
                            disponibleComercioExterior = true;
                            break;
                        case 30: //DIRECCION EMISOR COMERCIO EXTERIOR
                            direccionComercioExterior(cell, cells, 1);
                            break;
                        case 40: //PROIETARIO COMERCIO EXTERIOR
                            propietarioComercioExterior(cell, cells);
                            break;
                        case 50: //RECEPTOR
                            datosReceptor(cell, cells);
                            break;
                        case 60: //DIRECCION RECEPTOR COMERCIO EXTERIOR
                            direccionComercioExterior(cell, cells, 2);
                            break;
                        case 70: //DIRECCION DESTINATARIO COMERCIO EXTERIOR
                            direccionComercioExterior(cell, cells, 3);
                            break;
                        case 80: //DATOS FACTURA
                            datosFactura(cell, cells);
                            break;
                        case 90: //CONCEPTO
                            concepto(cell, cells);
                            break;
                        case 100: //TRASLADOS
                            impuestosTraslados(cell, cells);
                            break;
                        case 110: //RETENCIONES
                            impuestosRetenciones(cell, cells);
                            break;
                        case 120: //MERCANCIAS COMERCIO EXTERIOR
                            mercanciasComercioExterior(cell, cells);
                            break;
                        case 130: //TOTAL IMPUESTOS TRASLADOS
                            totalImpuestosTraslados(cell, cells);
                            break;
                        case 140: //TOTAL IMPUESTOS RETENCIONES
                            totalImpuestosRetenciones(cell,cells);
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
                    emisorDataFactura.setRegimenFiscal(new CatalogoData("" + regimenFiscal, ""));
                    break;
            }
        }
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

    public void direccionComercioExterior(XSSFCell cell, Iterator cells, int direccion) {

        domicilioComercioData = new DomicilioComercioData();
        String regIdTrib = "";
        String nombre = "";

        while (cells.hasNext()) {
            cell = (XSSFCell) cells.next();
            switch (cell.getColumnIndex()) {
                case 1://Calle
                    domicilioComercioData.setCalle(cell.getStringCellValue());
                    System.out.println("calle: " + cell.getStringCellValue());
                    break;
                case 2://NUMERO INTERIOR
                    domicilioComercioData.setNumeroInterior(readValue(cell));
                    break;
                case 3://NUMERO EXTERIOR
                    domicilioComercioData.setNumeroExterior(readValue(cell));
                    break;
                case 4://COLONIA
                    domicilioComercioData.setColonia(readValue(cell));
                    break;
                case 5://MUNICIPIO
                    domicilioComercioData.setMunicipio(readValue(cell));
                    break;
                case 6://ESTADO
                    domicilioComercioData.setEstado(readValue(cell));
                    break;
                case 7://PAIS
                    domicilioComercioData.setPais(readValue(cell));
                    break;
                case 8://CP
                    domicilioComercioData.setCodigoPostal(readValue(cell));
                    break;
                case 9://num reg trib solo DESTINATARIO CE
                    regIdTrib = readValue(cell);
                    break;
                case 10://nombre solo destinatario CE
                    nombre = cell.getStringCellValue();
                    break;
            }
        }

        switch (direccion) {
            case 1:
                emisorData = new EmisorComercioData();
                emisorData.setDomicilioComercio(domicilioComercioData);
                break;
            case 2:
                receptorComercio = new ReceptorComercioData();
                receptorComercio.setDomicilioComercio(domicilioComercioData);
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
        while (cells.hasNext()) {
            cell = (XSSFCell) cells.next();
            switch (cell.getColumnIndex()) {
                case 1://RFC
                    receptorData.setRfc(cell.getStringCellValue());
                    break;
                case 2://Nombre
                    receptorData.setNombre(cell.getStringCellValue());
                    break;
                case 3://USO CFDI
                    receptorData.setUsoCFDI(new CatalogoData(cell.getStringCellValue(), ""));
                    break;
                case 4://RESIDENCIA FISCAL
                    receptorData.setResidenciaFiscal(new CatalogoData(cell.getStringCellValue(), ""));
                    break;
                case 5://NUM REG ID TRIB
                    receptorData.setNumRegIdTrib(readValue(cell));
                    break;
            }
        }
    }

    public void datosFactura(XSSFCell cell, Iterator cells) {

        while (cells.hasNext()) {
            cell = (XSSFCell) cells.next();
            switch (cell.getColumnIndex()) {
                case 1://TIPO DE COMPROBANTE
                    ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setTipoDeComprobante(
                            new CatalogoData(cell.getStringCellValue(), ""));
                    break;
                case 2://SERIE
                    ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setSerie(cell.getStringCellValue());
                    break;
                case 3://MONEDA
                    ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setMoneda(new CatalogoData(cell.getStringCellValue(), ""));
                    break;
                case 4://TIPO CAMBIO
                    ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setTipoDeCambio(cell.getNumericCellValue());
                    break;
                case 5://METODO PAGO
                    ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setMetodoDePago(new CatalogoData(cell.getStringCellValue(), ""));
                    break;
                case 6://FORMA PAGO
                    ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setFormaDePago(new CatalogoData(readValue(cell), ""));
                    break;
                case 7://LUGAR EXPEDICION
                    int lugar = (int)cell.getNumericCellValue();
                    System.out.println("expedicion: " + lugar);
                    ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setLugarExpedicion(new CatalogoData(""+lugar, ""));
                    break;
                case 8://SUBTOTAL
                    ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setSubTotal(cell.getNumericCellValue());
                    break;
                case 9://DESCUENTO
                    ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setDescuento(cell.getNumericCellValue());
                    break;
                case 10://TOTAL
                    ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setTotal(cell.getNumericCellValue());
                    break;
            }
        }
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

        while (cells.hasNext()) {
            cell = (XSSFCell) cells.next();
            switch (cell.getColumnIndex()) {
                case 1://Base
                    tcd.setBase(cell.getNumericCellValue());
                    break;
                case 2://Impuesto
                    tcd.setImpuesto(new CatalogoData(cell.getStringCellValue(), ""));
                    break;
                case 3://TasaOCuota
                    tcd.setTasaOCuota(cell.getNumericCellValue());
                    break;
                case 4://TipoFactor
                    tcd.setTipoFactor(cell.getStringCellValue());
                    break;
                case 5://Importe
                    tcd.setImporte(cell.getNumericCellValue());
                    break;
            }
        }

        listTrasladodsCpto.remove(cont);
        listTrasladodsCpto.add(cont, tcd);
    }

    public void impuestosRetenciones(XSSFCell cell, Iterator cells) {

        RetencionConceptoData rcd = new RetencionConceptoData();

        while (cells.hasNext()) {
            cell = (XSSFCell) cells.next();
            switch (cell.getColumnIndex()) {
                case 1://Base
                    rcd.setBase(cell.getNumericCellValue());
                    break;
                case 2://Impuesto
                    rcd.setImpuesto(new CatalogoData(cell.getStringCellValue(), ""));
                    break;
                case 3://TasaOCuota
                    rcd.setTasaOCuota(cell.getNumericCellValue());
                    break;
                case 4://TipoFactor
                    rcd.setTipoFactor(cell.getStringCellValue());
                    break;
                case 5://Importe
                    rcd.setImporte(cell.getNumericCellValue());
                    break;
            }
        }

        listRetencionesCpto.remove(cont);
        listRetencionesCpto.add(cont, rcd);

    }

    public void mercanciasComercioExterior(XSSFCell cell, Iterator cells) {

        MercanciaComercioData temp = new MercanciaComercioData();

        while (cells.hasNext()) {
            cell = (XSSFCell) cells.next();
            switch (cell.getColumnIndex()) {
                case 1://NoIdentificacion
                    temp.setIdentificacion(cell.getStringCellValue());
                    break;
                case 2://Cantidad Aduana
                    temp.setCantidadAduana(cell.getNumericCellValue());
                    break;
                case 3://FraccionArancelaria
                    temp.setFraccionArancelaria(readValue(cell));
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
        System.out.println("---------------------------------Mercancias----------------------");
        for (int i = 0; i < mercanciasFe.size(); i++) {
            System.out.println(mercanciasFe.get(i).getNoIdentificacion());
            System.out.println(mercanciasFe.get(i).getFraccionArancelaria());
            for (int j = 0; j < mercanciasFe.get(i).getDescripcionesEspecificas().size(); j++) {
                System.out.println(mercanciasFe.get(i).getDescripcionesEspecificas().get(j).getMarca());
            }
        }

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


        System.out.println("-----------------Conceptos----------------");

        List<Concepto> listaConceptoData = new ArrayList<>();

        for (int i = 0; i < listAuxCptData.size(); i++) {
            List<TrasladoConcepto> auxTraslado = new ArrayList<>();
            List<RetencionConcepto> auxRetencion = new ArrayList<>();
            System.out.println(listAuxCptData.get(i).getImporte());

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


        for (int n = 0; n < listaConceptoData.size(); n++) {
            if (listaConceptoData.get(n).getImpuestosConcepto().getTraslados() != null) {
                System.out.println("------------------------TRASLADOS-----------------------");
                System.out.println("Tipo factor: " + listaConceptoData.get(n).getImpuestosConcepto().getTraslados().get(0).getTipoFactor());
            }
            if (listaConceptoData.get(n).getImpuestosConcepto().getRetenciones() != null) {
                System.out.println("------------------------RETENCIONES-----------------------");
                System.out.println("Tipo factor: " + listaConceptoData.get(n).getImpuestosConcepto().getRetenciones().get(0).getTipoFactor());
            }
        }


        if (disponibleComercioExterior) {
            ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setTotal(0.00);
            ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setSubTotal(0.00);
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

        System.out.println("--------------------TOTAL TRASLADOS------------------");
        System.out.println(totalTranfer);
        System.out.println("--------------------TOTAL RETENCIONES------------------");
        System.out.println(totalReten);
        ///------------------------ADDITIONAL DATA
        comprobanteData.setAdditional(new AdditionalData());

        ((AdditionalData) getComprobanteData().getAdditional()).setPlantilla(PLANTILLAGRAL);//PLANTILLA CFDIV33

    }

    public ComprobanteData getComprobanteData(){
        return comprobanteData;
    }

}
