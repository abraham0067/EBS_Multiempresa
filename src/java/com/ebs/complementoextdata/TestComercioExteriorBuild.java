package com.ebs.complementoextdata;


import com.ebs.complementoextdata.CustomComplementoComercioExteriorMetadata;
import com.ebs.complementoextdata.CustomCatalogoData;
import com.ebs.complementoextdata.CustomDescripcionEspecificaData;
import com.ebs.complementoextdata.CustomDestinatarioData;
import com.ebs.complementoextdata.CustomDomicilioData;
import com.ebs.complementoextdata.CustomEmisorData;
import com.ebs.complementoextdata.CustomMercanciaData;
import com.ebs.complementoextdata.CustomPropietarioData;
import com.ebs.complementoextdata.CustomReceptorData;
import fe.sat.Direccion12Data;
import fe.sat.complementos.v33.ComplementoFe;
import fe.sat.complementos.comercioexterior.*;
import fe.sat.v33.AdditionalData;
import fe.sat.v33.CFDIFactory33;
import fe.sat.v33.CatalogoData;
import fe.sat.v33.Comprobante;
import fe.sat.v33.ComprobanteData;
import fe.sat.v33.Concepto;
import fe.sat.v33.ConceptoData;
import fe.sat.v33.DatosComprobanteData;
import fe.xml.CharUnicode;
import lombok.Getter;
import lombok.Setter;
import org.jdom.Document;
import org.jdom.output.XMLOutputter;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by eflores on 16/01/2018.
 */
public class TestComercioExteriorBuild {
    private static String traslado = "T";
    private static String ingreso = "I";
    private static String egreso = "E";


    public static void main(String[] args) {


        TestComercioExteriorBuild test = new TestComercioExteriorBuild();
        CharUnicode charUnicode = new CharUnicode();
        ///Complemento Comercio Exterior data model
        ///Son similares a los conceptos solo que hay que convertir cantidades cuando la moneda no es USD
        ///Algunos campo se copian tal cual y existen algunos nuevos que se agregan

        CustomCatalogoData fraccionArancelaria = new CustomCatalogoData("85015204", "85015204");
        CustomCatalogoData unidadAduana = new CustomCatalogoData("06", "06");
        CustomCatalogoData paisMexico = new CustomCatalogoData("MEX", "MEX");
        CustomCatalogoData paisAlemania = new CustomCatalogoData("DEU", "DEU");

        List<CustomMercanciaData> mercanciasData;
        CustomMercanciaData singleMercanciaData;
        CustomMercanciaData singleMercanciaData2;
        CustomEmisorData singleEmisorData;
        CustomDomicilioData singleDomicilioEmisorData;
        CustomPropietarioData singlePropietarioData;
        CustomReceptorData singleReceptorData;
        CustomDomicilioData singleDomicilioReceptorData;
        CustomDestinatarioData singleDestinatarioData;
        CustomDomicilioData singleDomicilioDestinatarioData;
        CustomDescripcionEspecificaData singleDescripcionEspecificaData;
        CustomComplementoComercioExteriorMetadata singleComplementoComercioExteriorData;

        singleDescripcionEspecificaData = new CustomDescripcionEspecificaData("siemens", "", "", "");
        List<CustomDescripcionEspecificaData> descripciones = new ArrayList<>();
        descripciones.add(singleDescripcionEspecificaData);

        mercanciasData = new ArrayList<>();
        singleMercanciaData = new CustomMercanciaData(
                "1LE24212AB213AA3",
                fraccionArancelaria,
                2.00d,
                unidadAduana,
                441.92d,
                883.84d,
                descripciones);

        singleMercanciaData2 = new CustomMercanciaData(
                "1LE24222AB212AA3",
                fraccionArancelaria,
                4.00d,
                unidadAduana,
                473.36d,
                1893.44d,
                descripciones);

        mercanciasData.add(singleMercanciaData);
        mercanciasData.add(singleMercanciaData2);


        singleDomicilioEmisorData = new CustomDomicilioData("Av. Ejército Nacional", "350", "Piso 3", "2785", "",
                "", "016", "DIF", paisMexico, "11560");
        singleEmisorData = new CustomEmisorData();
        singleEmisorData.setCURP("");
        singleEmisorData.setDomicilio(singleDomicilioEmisorData);

        singleDomicilioReceptorData = new CustomDomicilioData("101 Airport Industrial Drive", "", "", "Southaven", "",
                "", "", "MS", paisAlemania, "38671-4536");
        singleReceptorData = new CustomReceptorData("", singleDomicilioReceptorData);


        singleDomicilioDestinatarioData = new CustomDomicilioData("101 Airport Industrial Drive", "", "", "Southaven", "",
                "", "", "MS", paisAlemania, "38671-4536");
        singleDestinatarioData = new CustomDestinatarioData("", "", singleDomicilioDestinatarioData);


        singleComplementoComercioExteriorData = new CustomComplementoComercioExteriorMetadata();
        singleComplementoComercioExteriorData.setEmisor(singleEmisorData);
        singleComplementoComercioExteriorData.setReceptor(singleReceptorData);
        singleComplementoComercioExteriorData.setDestinatario(singleDestinatarioData);
        singleComplementoComercioExteriorData.setMercancias(mercanciasData);
        singleComplementoComercioExteriorData.setIncoterm("FCA");
        singleComplementoComercioExteriorData.setCertificadoOrigen("0");
        singleComplementoComercioExteriorData.setClaveDePedimento("A1");
        singleComplementoComercioExteriorData.setSubdivision("1");
        singleComplementoComercioExteriorData.setTipoCambioUSD(19.44897);
        singleComplementoComercioExteriorData.setTipoOperacion("2");
        singleComplementoComercioExteriorData.setTotalUSD(2777.28);


        ComprobanteData finalComprobante = test.addComplementoComercioExterior(test.buildBaseComprobante(), singleComplementoComercioExteriorData);
        try {
            System.out.println("Building xml");
            CFDIFactory33 factory33 = new CFDIFactory33();
            factory33.buildComprobanteDocConAddenda(finalComprobante);// TODO: 12/06/2017 CHECK feConfig.xml NOT FOUND EXCENTION

            Document doc = factory33.getDocumentPrint();
            XMLOutputter xout = new XMLOutputter();
            xout.setFormat(xout.getFormat().setEncoding("ISO-8859-1"));
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            xout.output(doc, bout);

            String xmlPrint = new String(bout.toByteArray());
            xmlPrint = xmlPrint.replaceAll("ISO-8859-1", "UTF-8");
            xmlPrint = charUnicode.getTextEncoded2(xmlPrint);

            Document docXml = factory33.getDocument();
            XMLOutputter xoutXml = new XMLOutputter();
            xoutXml.setFormat(xoutXml.getFormat().setEncoding("ISO-8859-1"));
            ByteArrayOutputStream boutXml = new ByteArrayOutputStream();
            xoutXml.output(docXml, boutXml);
            String xml = new String(boutXml.toByteArray());
            xml = xml.replaceAll("ISO-8859-1", "UTF-8");
            xml = charUnicode.getTextEncoded2(xml);

            System.out.println(">>>>>>>>>> FACTURACION MANUAL XML_SAT :  <<< \n" + xml + "\n >>>");
            System.out.println(">>>>>>>>>> FACTURACION MANUAL XML_PRINT :  <<< \n" + xmlPrint + "\n >>>");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public ComprobanteData buildBaseComprobante() {
        ComprobanteData comprobanteData = TestComercioExteriorBuild.intiComprobante();
        return comprobanteData;

    }


    public ComprobanteData addComplementoComercioExterior(ComprobanteData cdata, CustomComplementoComercioExteriorMetadata singleComplementoComercioExteriorData) {


        ComercioExteriorData comercioExteriorData = new ComercioExteriorData();
        CustomDomicilioData domEmisor = singleComplementoComercioExteriorData.getEmisor().getDomicilio();
        CustomDomicilioData domReceptor = singleComplementoComercioExteriorData.getReceptor().getDomicilio();
        CustomDomicilioData domDestinatario = singleComplementoComercioExteriorData.getDestinatario().getDomicilio();
        CustomDomicilioData domPropietario = (singleComplementoComercioExteriorData.getPropietario() != null) ? singleComplementoComercioExteriorData.getPropietario().getDomicilioData() : null;


        ///-------------------------------------------------------------------------------------------------------------
        /// Emisor
        ///-------------------------------------------------------------------------------------------------------------
        DomicilioComercioData domicilioComercioData = new DomicilioComercioData();
        domicilioComercioData.setCalle(domEmisor.getCalle());
        domicilioComercioData.setCodigoPostal(domEmisor.getCodigoPostal());
        domicilioComercioData.setColonia(domEmisor.getColonia());
        domicilioComercioData.setEstado(domEmisor.getEstado());
        domicilioComercioData.setMunicipio(domEmisor.getMunicipio());
        domicilioComercioData.setNumeroExterior(domEmisor.getNumeroExterior());
        domicilioComercioData.setNumeroInterior(domEmisor.getNumeroInterior());
        domicilioComercioData.setPais(domEmisor.getPais().getClave());

        EmisorComercioData emisorData = new EmisorComercioData();
        emisorData.setDomicilioComercio(domicilioComercioData);


        ///-------------------------------------------------------------------------------------------------------------
        /// Receptor
        ///-------------------------------------------------------------------------------------------------------------
        domicilioComercioData = new DomicilioComercioData();
        domicilioComercioData.setCalle(domReceptor.getCalle());
        domicilioComercioData.setCodigoPostal(domReceptor.getCodigoPostal());
        domicilioComercioData.setColonia(domReceptor.getColonia());
        domicilioComercioData.setEstado(domReceptor.getEstado());
        domicilioComercioData.setMunicipio(domReceptor.getMunicipio());
        domicilioComercioData.setNumeroExterior(domReceptor.getNumeroExterior());
        domicilioComercioData.setNumeroInterior(domReceptor.getNumeroInterior());
        domicilioComercioData.setPais(domReceptor.getPais().getClave());

        ReceptorComercioData receptorComercio = new ReceptorComercioData();
        receptorComercio.setDomicilioComercio(domicilioComercioData);


        ///-------------------------------------------------------------------------------------------------------------
        /// Destinatario
        ///-------------------------------------------------------------------------------------------------------------

        domicilioComercioData = new DomicilioComercioData();
        domicilioComercioData.setCalle(domDestinatario.getCalle());
        domicilioComercioData.setCodigoPostal(domDestinatario.getCodigoPostal());
        domicilioComercioData.setColonia(domDestinatario.getColonia());
        domicilioComercioData.setEstado(domDestinatario.getEstado());
        domicilioComercioData.setMunicipio(domDestinatario.getMunicipio());
        domicilioComercioData.setNumeroExterior(domDestinatario.getNumeroExterior());
        domicilioComercioData.setNumeroInterior(domDestinatario.getNumeroInterior());
        domicilioComercioData.setPais(domDestinatario.getPais().getClave());

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

        List<CustomMercanciaData> mercancias = singleComplementoComercioExteriorData.getMercancias();

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
                descrips.setModelo(desctmp.getModelo());
                descrips.setNumeroSerie(desctmp.getNumeroSerie());
                descrips.setSubModelo(desctmp.getSubModelo());
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


        List<ComplementoFe> complementos = cdata.getComplementosFe();
        if (complementos == null) {
            complementos = new ArrayList<>();
        }
        complementos.add(comercioExteriorData);
        cdata.setComplemementosFe(complementos);

        return cdata;
    }


    public static ComprobanteData intiComprobante() {
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
        ComprobanteData comprobanteData = new ComprobanteData();
        List<Concepto> listaConceptoData = new ArrayList();
        comprobanteData.setDatosComprobante(new DatosComprobanteData());
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setTotal(2777.28);
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setCertificado("12345677890123232323");
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setFecha(new Date());
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setLugarExpedicion(new CatalogoData("45647", "45647"));
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setFolio("1488871");
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setFolioErp("1488871");
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setSerie("AAA");
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setSerieErp("AAA");
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setMetodoDePago(new CatalogoData("PPD", "PPD"));
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setFormaDePago(new CatalogoData("99", "99"));
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setCondicionesDePago("De acuerdo a orden de compra o contrato");
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setDescuento(null);
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setTipoDeCambio(19.44897d);
        //  ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setTipoDocumento(null); ///  Por el momento se creara
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setSubTotal(2777.28);
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setMoneda(new CatalogoData("USD",
                "USD")); //a nivel comprobante debe de ser siempre XXX
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setTipoDeComprobante(new CatalogoData("I",
                "Ingreso"));
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setSello("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa=");
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setNoCertificado("20001000000300022323");//Mas de 20 caracteres


        // TODO: 12/01/2018 ADD EMISOR AND RECEPTOR
        fe.sat.v33.EmisorData emisorData = new fe.sat.v33.EmisorData();
        emisorData.setRfc("SIE931112PA1");
        emisorData.setNombre("Siemens S.A. de C.V.");
        emisorData.setRegimenFiscal(new CatalogoData("601", "601"));

        fe.sat.v33.ReceptorData receptorData = new fe.sat.v33.ReceptorData();
        receptorData.setRfc("XEXX010101000");
        receptorData.setNombre("Siemens AG I DT LD HQ NBG VO");
        receptorData.setUsoCFDI(new CatalogoData("P01", "P01"));
        receptorData.setNumRegIdTrib("DE129274202");
        receptorData.setResidenciaFiscal(new CatalogoData("DEU", "DEU"));

        comprobanteData.setEmisor(emisorData);
        comprobanteData.setReceptor(receptorData);

        /*
        *
      <cfdi:Concepto ClaveProdServ="26101100" NoIdentificacion="1LE24212AB213AA3" Cantidad="2" ClaveUnidad="EA"
      Unidad="PCE" Descripcion="1LE24212AB213AA3 MOT.3F.SD100 IEEE MNP.575V.215T 10-4 THREE PHASE ASYNCHRONOUS
      ELECTRIC MOTOR 7.457 KW MOTOR ELECTRICO TRIFASICO ASINCRONO 7.457 KW LV MOTOR SD100 IEEE ALUMINIUM ROTOR MOTOR,10,1800,SD100 IEEE841,575,215T,NP" ValorUnitario="441.92" Importe="883.84">
      <cfdi:Impuestos>
        <cfdi:Traslados>
          <cfdi:Traslado Base="883.84" Impuesto="002" TasaOCuota="0.000000" TipoFactor="Tasa" Importe="0.00"/>
        </cfdi:Traslados>
      </cfdi:Impuestos>
    </cfdi:Concepto>
        * */


        ConceptoData cptoData = new ConceptoData();
        cptoData.setCantidad(2);
        cptoData.setImporte(883.84);
        cptoData.setDescripcion("1LE24212AB213AA3 ");
        cptoData.setValorUnitario(441.92);
        cptoData.setNoIdentificacion("1LE24212AB213AA3");
        cptoData.setUnidad("PCE");
        cptoData.setClaveProdServ(new CatalogoData("26101100", "26101100"));
        cptoData.setClaveUnidad(new CatalogoData("EA", "EA"));
        cptoData.setDescuento(null);
        cptoData.setImpuestosConcepto(null);
        listaConceptoData.add(cptoData);//agregamos solo un concepto a la factura


/*    <cfdi:Concepto ClaveProdServ="26101100" NoIdentificacion="1LE24222AB212AA3" Cantidad="4" ClaveUnidad="EA"
      Unidad="PCE" Descripcion="1LE24222AB212AA3 MOT.3F.SD661 215T 460V. 10-4 THREE PHASE ASYNCHRONOUS ELECTRIC MOTOR
      7.457 KW MOTOR ELECTRICO TRIFASICO ASINCRONO 7.457 KW LV MOTOR SD661 ALUMINIUM ROTOR MOTOR,10,1800,SD661,460,215T,NP" ValorUnitario="473.36" Importe="1893.44">
      <cfdi:Impuestos>
        <cfdi:Traslados>
          <cfdi:Traslado Base="1893.44" Impuesto="002" TasaOCuota="0.000000" TipoFactor="Tasa" Importe="0.00"/>
        </cfdi:Traslados>
      </cfdi:Impuestos>
    </cfdi:Concepto>*/
        ConceptoData cptoData2 = new ConceptoData();
        cptoData2.setCantidad(4);
        cptoData2.setImporte(1893.44);
        cptoData2.setDescripcion("1LE24212AB213AA3 ");
        cptoData2.setValorUnitario(473.36);
        cptoData2.setNoIdentificacion("1LE24222AB212AA3");
        cptoData2.setUnidad("PCE");
        cptoData2.setClaveProdServ(new CatalogoData("26101100", "26101100"));
        cptoData2.setClaveUnidad(new CatalogoData("EA", "EA"));
        cptoData2.setDescuento(null);
        cptoData2.setImpuestosConcepto(null);
        listaConceptoData.add(cptoData2);
        comprobanteData.setConceptos(listaConceptoData);
        comprobanteData.setAdditional(new AdditionalData());
        //--------------------------------------------------------------------
        // COMPLEMENTOS
        //--------------------------------------------------------------------
        comprobanteData.setComplemementosFe(new ArrayList<ComplementoFe>());
        return comprobanteData;
    }

}
