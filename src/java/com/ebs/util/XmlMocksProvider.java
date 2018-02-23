package com.ebs.util;

import lombok.Getter;

/**
 * Created by eflores on 26/09/2017.
 */
public class XmlMocksProvider {
    //CFDI dummie
    @Getter
    private static String xml1 = "";

    //CFDI para complemento de pago
    @Getter
    private static String xml2 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<cfdi:Comprobante xmlns:cfdi=\"http://www.sat.gob.mx/cfd/3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv33.xsd\" Version=\"3.3\" Serie=\"EBSF\" Folio=\"1593\" Fecha=\"2017-08-29T10:59:18\" Sello=\"A4IDFVEw7YxZR4hms6xHxYnJN9a5E+O9bgW67uTHAW8c5gIpE3ZPbwef5BFO21q/m5WxjRhkq4X3OhDROwe04w2FbfVM1tYSLAYF2q84Z6oB8kpZ1i83zyQcPBZDm3cRJpZgOHGPb2AeVD+CxTfQgQT8+tYdFkO2wpP72N/iyBvvbyJIas+nQqjXoyzDH5NPYMyh84n5d80N54J/3GL+XCZYtD1i2IBG0AIye3lCE2RdouUZSOkoe1Nxf/BLgWv7tvkgO0Ci9tjWC8nv90AMlekCqQM7LhyZVM71V/EQuefTLREDMBTseAWgkJ7wvWoRNWZgtW9FvKyGjph9OUIGOg==\" FormaPago=\"99\" NoCertificado=\"00001000000407263084\" Certificado=\"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\" SubTotal=\"1.00\" Descuento=\"0.00\" Moneda=\"MXN\" TipoCambio=\"1\" Total=\"1.16\" TipoDeComprobante=\"I\" MetodoPago=\"PPD\" LugarExpedicion=\"11590\">\n" +
            "    <cfdi:Emisor Rfc=\"EB&amp;060116TK9\" Nombre=\"ELECTRONIC BILLS &amp; SERVICES S A DE C V\" RegimenFiscal=\"601\" />\n" +
            "    <cfdi:Receptor Rfc=\"SIE931112PA1\" Nombre=\"SIEMENS S A DE C V\" UsoCFDI=\"G03\"/>\n" +
            "    <cfdi:Conceptos>\n" +
            "        <cfdi:Concepto ClaveProdServ=\"78141800\" Cantidad=\"1\" ClaveUnidad=\"ACT\" Unidad=\"servicio\" Descripcion=\"soporte tecnico\" ValorUnitario=\"1.00\" Importe=\"1.00\" Descuento=\"0.00\">\n" +
            "            <cfdi:Impuestos>\n" +
            "                <cfdi:Traslados>\n" +
            "                    <cfdi:Traslado Base=\"1.00\" Impuesto=\"002\" TasaOCuota=\"0.160000\" TipoFactor=\"Tasa\" Importe=\"0.16\"/>\n" +
            "                </cfdi:Traslados>\n" +
            "            </cfdi:Impuestos>\n" +
            "        </cfdi:Concepto>\n" +
            "    </cfdi:Conceptos>\n" +
            "    <cfdi:Impuestos TotalImpuestosTrasladados=\"0.16\">\n" +
            "        <cfdi:Traslados>\n" +
            "            <cfdi:Traslado Impuesto=\"002\" TasaOCuota=\"0.160000\" TipoFactor=\"Tasa\" Importe=\"0.16\"/>\n" +
            "        </cfdi:Traslados>\n" +
            "    </cfdi:Impuestos>\n" +
            "    <cfdi:Complemento>\n" +
            "        <tfd:TimbreFiscalDigital xmlns:tfd=\"http://www.sat.gob.mx/TimbreFiscalDigital\" Version=\"1.1\" UUID=\"A555C08A-54E4-4236-AC79-14DC057CE218\" FechaTimbrado=\"2017-08-29T10:59:01\" RfcProvCertif=\"ASE0209252Q1\" Leyenda=\"Timbre de prueba\" SelloCFD=\"A4IDFVEw7YxZR4hms6xHxYnJN9a5E+O9bgW67uTHAW8c5gIpE3ZPbwef5BFO21q/m5WxjRhkq4X3OhDROwe04w2FbfVM1tYSLAYF2q84Z6oB8kpZ1i83zyQcPBZDm3cRJpZgOHGPb2AeVD+CxTfQgQT8+tYdFkO2wpP72N/iyBvvbyJIas+nQqjXoyzDH5NPYMyh84n5d80N54J/3GL+XCZYtD1i2IBG0AIye3lCE2RdouUZSOkoe1Nxf/BLgWv7tvkgO0Ci9tjWC8nv90AMlekCqQM7LhyZVM71V/EQuefTLREDMBTseAWgkJ7wvWoRNWZgtW9FvKyGjph9OUIGOg==\" NoCertificadoSAT=\"20001000000300022323\" SelloSAT=\"eFW4fm16UrhYR07u5T6eO9QjSd6WI0XFDrLHmYQAKiOqDXr7r+IcCxBsdyVisBEDDsr++U4R6CkX4O49C9NhkHd8eauD3C3X05h2aWkvNdXwpF0YR4D29nVOIZSNnlgCSm/dLJkau1taE/D2t1FyhKB8wAeaiia/AkiUEoMX7m/vIOWZNrH6SiBD1AlRyJsp5ZKg2R5j5izxiOHBEhl/5S2GA2jLWkNesBTCx/MSzIDwOa/0Rfd8632GaZOSuSDxMZqOe7cpU5D/w5qV92fSu67q8sbRFZ1+MJZAaT2t9ND1Ba2b+07kvOlEr/mC1l5cOtSY2b6rDmy5mX/XFwgY3Q==\" xsi:schemaLocation=\"http://www.sat.gob.mx/TimbreFiscalDigital http://www.sat.gob.mx/sitio_internet/cfd/TimbreFiscalDigital/TimbreFiscalDigitalv11.xsd\"/>\n" +
            "    </cfdi:Complemento>\n" +
            "</cfdi:Comprobante>";
    @Getter
    private static String xml3 = "";
    @Getter
    private static String xml4 = "";
    @Getter
    private static String xml5 = "";
    @Getter
    private static String xml6 = "";
    @Getter
    private static String xml7 = "";
    @Getter
    private static String xml8 = "";
    @Getter
    private static String xml9 = "";
    @Getter
    private static String xml10 = "";


}
