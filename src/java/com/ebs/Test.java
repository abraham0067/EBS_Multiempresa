/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs;

import fe.db.MCaduana;
import fe.db.MCcodigopostal;
import fe.db.MCimpuesto;
import fe.db.MCmoneda;
import fe.db.MCpais;
import fe.db.MCpatentesAduanales;
import fe.db.MCpedimentoAduana;
import fe.db.MCregimenFiscal;
import fe.db.MCunidades;
import fe.db.*;
import fe.model.dao.ClaveUnidadDAO;
import fe.model.dao.*;
import fe.model.util.SendMail;
import fe.sat.v33.CFDIFactory33;
import fe.sat.v33.CatalogoData;
import fe.sat.v33.ComprobanteData;
import fe.sat.v33.TrasladoConceptoData;
import org.apache.axis.encoding.ser.ElementSerializer;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ebs.helpers.LambdasHelper;

/**
 * @author Eduardo C. Flores Ambrosio
 */
public class Test {

    public static void main(String args[]) throws Exception {

        //imprimirMOtro(20);

        /*
        String uuidNuevo1 = "01234567-9ABC-DEF0-1234-56789ABCDEF0";
        String uuidNuevo2 = "PRUEBAXX-ATEB-SERV-SACV-TIMBREPRUEBA";
        System.out.println(uuidNuevo1.matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"));
        System.out.println(uuidNuevo2.matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"));
        Pattern p = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f‌​]{4}-[0-9a-f]{12}$");
        System.out.println(p.matcher(uuidNuevo1).matches());
        System.out.println(p.matcher(uuidNuevo2).matches());

        TrasladoConceptoData tcd = new TrasladoConceptoData();
        tcd.setBase(Double.parseDouble("1.0"));
        tcd.setImporte(Double.parseDouble("100.0"));
        CatalogoData ci = new CatalogoData("IVA", "IVA");
        tcd.setImpuesto(ci);
        CatalogoData ctc = new CatalogoData("TASA", "TASA");
        tcd.setTasaOCuota(Double.parseDouble("0.16"));
        CatalogoData ctf = new CatalogoData("00", "00");
        tcd.setTipoFactor("TASA");
        System.out.println("CHECK DEBUG");

        castComprobanteData();
        testFormat();
        testRoundFloat();
        String respuestaServicio = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<cfdi:Comprobante xmlns:cfdi=\"http://www.sat.gob.mx/cfd/3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" print=\"print\" xsi:schemaLocation=\"http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv33.xsd\" Version=\"3.3\" Serie=\"AAA\" Folio=\"141\" Fecha=\"2017-07-20T15:57:51\" Sello=\"WOKOY16pzz5tkQERkufwUa/TsNczUIfdGe7/8oQZKNStxokTjZescuQO29fLZdaFU7cAGwZlbDg6wwWLTfUhagR6J+HPlKqyxnPzUO7gngZnatV6EfGIENKSIo8rFufTrBBIHQU//hi7dRkmjY5jBWcyP6Sa/dWrYRfeR9CcQwI=\" FormaPago=\"01\" NoCertificado=\"20001000000100005867\" Certificado=\"MIIEdDCCA1ygAwIBAgIUMjAwMDEwMDAwMDAxMDAwMDU4NjcwDQYJKoZIhvcNAQEFBQAwggFvMRgwFgYDVQQDDA9BLkMuIGRlIHBydWViYXMxLzAtBgNVBAoMJlNlcnZpY2lvIGRlIEFkbWluaXN0cmFjacOzbiBUcmlidXRhcmlhMTgwNgYDVQQLDC9BZG1pbmlzdHJhY2nDs24gZGUgU2VndXJpZGFkIGRlIGxhIEluZm9ybWFjacOzbjEpMCcGCSqGSIb3DQEJARYaYXNpc25ldEBwcnVlYmFzLnNhdC5nb2IubXgxJjAkBgNVBAkMHUF2LiBIaWRhbGdvIDc3LCBDb2wuIEd1ZXJyZXJvMQ4wDAYDVQQRDAUwNjMwMDELMAkGA1UEBhMCTVgxGTAXBgNVBAgMEERpc3RyaXRvIEZlZGVyYWwxEjAQBgNVBAcMCUNveW9hY8OhbjEVMBMGA1UELRMMU0FUOTcwNzAxTk4zMTIwMAYJKoZIhvcNAQkCDCNSZXNwb25zYWJsZTogSMOpY3RvciBPcm5lbGFzIEFyY2lnYTAeFw0xMjA3MjcxNzAyMDBaFw0xNjA3MjcxNzAyMDBaMIHbMSkwJwYDVQQDEyBBQ0NFTSBTRVJWSUNJT1MgRU1QUkVTQVJJQUxFUyBTQzEpMCcGA1UEKRMgQUNDRU0gU0VSVklDSU9TIEVNUFJFU0FSSUFMRVMgU0MxKTAnBgNVBAoTIEFDQ0VNIFNFUlZJQ0lPUyBFTVBSRVNBUklBTEVTIFNDMSUwIwYDVQQtExxBQUEwMTAxMDFBQUEgLyBIRUdUNzYxMDAzNFMyMR4wHAYDVQQFExUgLyBIRUdUNzYxMDAzTURGUk5OMDkxETAPBgNVBAsTCFVuaWRhZCAxMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC2TTQSPONBOVxpXv9wLYo8jezBrb34i/tLx8jGdtyy27BcesOav2c1NS/Gdv10u9SkWtwdy34uRAVe7H0a3VMRLHAkvp2qMCHaZc4T8k47Jtb9wrOEh/XFS8LgT4y5OQYo6civfXXdlvxWU/gdM/e6I2lg6FGorP8H4GPAJ/qCNwIDAQABox0wGzAMBgNVHRMBAf8EAjAAMAsGA1UdDwQEAwIGwDANBgkqhkiG9w0BAQUFAAOCAQEATxMecTpMbdhSHo6KVUg4QVF4Op2IBhiMaOrtrXBdJgzGotUFcJgdBCMjtTZXSlq1S4DG1jr8p4NzQlzxsdTxaB8nSKJ4KEMgIT7E62xRUj15jI49qFz7f2uMttZLNThipunsN/NF1XtvESMTDwQFvas/Ugig6qwEfSZc0MDxMpKLEkEePmQwtZD+zXFSMVa6hmOu4M+FzGiRXbj4YJXn9Myjd8xbL/c+9UIcrYoZskxDvMxc6/6M3rNNDY3OFhBK+V/sPMzWWGt8S1yjmtPfXgFs1t65AZ2hcTwTAuHrKwDatJ1ZPfa482ZBROAAX1waz7WwXp0gso7sDCm2/yUVww==\" SubTotal=\"1.00\" Descuento=\"0.00\" Moneda=\"MXN\" TipoCambio=\"1\" Total=\"1.16\" TipoDeComprobante=\"I\" MetodoPago=\"PUE\" LugarExpedicion=\"23234\" CatFormaDePago=\"Efectivo\" CatLugarExpedicion=\"BCS\" CatMetodoDePago=\"Pago en una sola exhibición\" CatMoneda=\"Peso Mexicano\" CatTipoDeComprobante=\"ingreso\">\n" +
                "  <cfdi:Emisor Rfc=\"AAA010101AAA\" Nombre=\"PRUEBA\" RegimenFiscal=\"601\" CatRegimenFiscal=\"General de Ley Personas Morales\"/>\n" +
                "  <cfdi:Receptor Rfc=\"EB&amp;060116TK9\" Nombre=\"Electronic Bills &amp; Services SA de CV\" UsoCFDI=\"G01\" CatUsoCFDI=\"Adquisici?n de mercancias\" />\n" +
                "  <cfdi:Conceptos>\n" +
                "    <cfdi:Concepto ClaveProdServ=\"81112201\" Cantidad=\"1\" ClaveUnidad=\"E48\" Unidad=\"PIZ\" Descripcion=\"Mantenimiento\" ValorUnitario=\"1.00\" Importe=\"1.00\" Descuento=\"0.00\" CatClaveProdServ=\"Tarifas de soporte o mantenimiento\" CatClaveUnidad=\"Unidad de servicio\" posicion=\"0\">\n" +
                "      <cfdi:Impuestos>\n" +
                "        <cfdi:Traslados>\n" +
                "          <cfdi:Traslado Base=\"1.00\" Impuesto=\"002\" TasaOCuota=\"0.160000\" TipoFactor=\"Tasa\" Importe=\"0.16\"/>\n" +
                "        </cfdi:Traslados>\n" +
                "      </cfdi:Impuestos>\n" +
                "    </cfdi:Concepto>\n" +
                "    <cfdi:Comment9999 posicion=\"9999\"/>\n" +
                "  </cfdi:Conceptos>\n" +
                "  <cfdi:Impuestos TotalImpuestosTrasladados=\"0.16\">\n" +
                "    <cfdi:Traslados>\n" +
                "      <cfdi:Traslado Impuesto=\"002\" TasaOCuota=\"0.160000\" TipoFactor=\"Tasa\" Importe=\"0.16\"/>\n" +
                "    </cfdi:Traslados>\n" +
                "  </cfdi:Impuestos>\n" +
                "  <cfdi:AdditionalData plantilla=\"GRUPO\">\n" +
                "    <cfdi:Entrega/>\n" +
                "    <cfdi:Params>\n" +
                "      <cfdi:Param0/>\n" +
                "      <cfdi:Param1/>\n" +
                "      <cfdi:Param2/>\n" +
                "    </cfdi:Params>\n" +
                "    <cfdi:Personals/>\n" +
                "    <cfdi:DirFiscal calle=\"\" noExterior=\"\" noInterior=\"\" colonia=\"\" localidad=\"\" municipio=\"\" estado=\"\" pais=\"\" cp=\"\"/>\n" +
                "  </cfdi:AdditionalData>\n" +
                "  <cfdi:Complemento>\n" +
                "    <tfd:TimbreFiscalDigital xmlns:tfd=\"http://www.sat.gob.mx/TimbreFiscalDigital\" version=\"1.0\" UUID=\"A7CD2E29-BBFF-4229-970E-26CEB6F03417\" FechaTimbrado=\"2017-06-23T17:43:15\" selloCFD=\"NYj1ruUsrHLyW7iCrbS5QpyToijSdXGeKiQiwxvy06YLE5Ft6VZS85FPjGKRo/HABkFKJaYfez8p+6iVKJRH0km2MVtJ7sL0J/bkC1p6M8359mbLdnQzXbKaw2/w3WPaGLin6H93x84hl6BAYOM/K1dkk5Thuy+OOryNpsa6WJg=\" noCertificadoSAT=\"20001000000300022323\" selloSAT=\"KRXjYgx/WngDNATS/HN1lIX1TM7XG2Jlv72wL9qZ/ngZnCf4jOUber8CcUSZi2OvE6wtiOGIg/GLhsJmlDVUX4+8BpB4cskHVdnu7Y0GgcAe203klYeUuxSyHhAfRrL5kydhE08ExJaH0E0rW/N5Nr0lrloAjVBFuA8x2Vx1iaGtTd+fDNkMy36bW4oak6H3mr1bBhlzcioBQVUOuy1kv0qMRxw1CXBcb7/94HXWN4fuYHLaOlQG//sRNoTQlf8Emtv3HtvgAURAfNw5KxsgTC1LitRxcNqOkqe9fkPAePpZivmogGN/vFJfOBtR7v/cP0UK0GB8iTLmvi/hTsFyaw==\" xsi:schemaLocation=\"http://www.sat.gob.mx/TimbreFiscalDigital http://www.sat.gob.mx/sitio_internet/TimbreFiscalDigital/TimbreFiscalDigital.xsd\"/>\n" +
                "  </cfdi:Complemento>\n" +
                "</cfdi:Comprobante>";
        System.out.println(checkRespuestaServicio(respuestaServicio));

        //LoginDAO daoLogin = new LoginDAO();

//        MEmpresa tmp;
//        JarsDAO jdao = new JarsDAO();
//        MJars jar = jdao.buscarJarEmpresa(1);
//        System.out.println("Jar"+jar.getDireccionJar());//Obtiene el nombre del jar
////        for(MEmpresa tmp : daoEmpresa.ListaEmpresasPadres(2)){
////            System.out.println("Empresa:"+tmp.getId(es=) + " " + tmp.getRazonSocial());
////        }
//        tmp=daoEmpresa.BuscarEmpresaId(1);
//        System.out.println(""+tmp.getRazonSocial());
        //MAcceso t;
        //t=daoLogin.buscarAccesoPorLoginPass("admingral", "admingral");
        //System.out.println("Acceso:"+t.getUsuario()+t.getEmail()+t.getRfc());
//        System.out.println("-" + config.getDato());
//        System.out.println("-" + config.getClasificacion());
//        System.out.println("-" + config.getValor());
        //        checkUnidad();//ok    
//          checkClaveProducto();//ok incluir por default el 01010101- no existe en el catalogo
//          checkAduana();//ok  
        //checkCodigoPostal();//ok
        //checkFormaPago();//ok
        //checkImpuesto();//ok
        //checkMetodoPago();//ok directly
        //checkMoneda();//ok
        //checkPedimentos();//ok
        //checkPais();//ok
        //checkPatenteAduanal();//ok
        //checkRegimenFiscal();//okc
        //checkTasaCuota();//ok
        //checkTipoComprobante();//ok
        //checkTipoFactor///ok directly
        //checoCfdi///ok directly
        */
        ///test();
        //testTipoDocFactura();
        ///sendMail();
        calculaPerfilUsandoAnd();
    }

    private static void test() {
        int test = 5;
        double dtest = 0.5;
        if (test % 1 == 0) {
            System.out.println("IS INTEGER");
            System.out.println(test * 0.01);
        } else {
            System.out.println("IS NOTA INTE");
        }


        if (dtest % 1 != 0) {
            System.out.println("IS DOUBLE");
        } else {
            System.out.println("IS NOTA INTE");
        }


    }

    private static void castComprobanteData() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<cfdi:Comprobante xmlns:cfdi=\"http://www.sat.gob.mx/cfd/3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" print=\"print\" xsi:schemaLocation=\"http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv33.xsd\" Version=\"3.3\" Serie=\"Z\" Folio=\"1\" Fecha=\"2017-06-12T10:24:50\" Sello=\"qQSvHZPzRB7eT6f9dJs3GepVl82eq4aOiI4b2hK+YSTcaJXgrDm8GfmZyUMnJ5XLs/TZDtAeafF5W1oyEygqva5fA3Ga5agq9HKHMEZx2qCOgOB+97C26StVKUdGD3jcPCh64AEgXLdCftIPwiRXP6eAr0IeeaujjVdEobvuxyo=\" FormaPago=\"01\" NoCertificado=\"00001000000103168809\" Certificado=\"TESTTESTTESTTESTTESTTESTTESTTESTTESTTEST\" SubTotal=\"1001.00\" Descuento=\"0.00\" Moneda=\"MXN\" TipoCambio=\"1\" Total=\"1161.16\" TipoDeComprobante=\"I\" MetodoPago=\"PUE\" LugarExpedicion=\"01000\" CatFormaDePago=\"Efectivo\" CatLugarExpedicion=\"DIF\" CatMetodoDePago=\"Pago en una sola exhibici&#243;n\" CatMoneda=\"Peso Mexicano\" CatTipoDeComprobante=\"ingreso\">\n" +
                "  <cfdi:Emisor Rfc=\"AAA010101AAA\" Nombre=\"PRUEBA\" RegimenFiscal=\"601\" CatRegimenFiscal=\"General de Ley Personas Morales\"/>\n" +
                "  <cfdi:Receptor Rfc=\"EB&amp;060116TK9\" Nombre=\"Electronic Bills &amp; Services SA de CV\" UsoCFDI=\"G01\" CatUsoCFDI=\"Adquisici&#243;n de mercancias\" />\n" +
                "  <cfdi:Conceptos>\n" +
                "    <cfdi:Concepto ClaveProdServ=\"56101500\" NoIdentificacion=\"KC999\" Cantidad=\"1\" ClaveUnidad=\"H87\" Unidad=\"PZA\" Descripcion=\"prueba01\" ValorUnitario=\"1.00\" Importe=\"1.00\" Descuento=\"0.00\" CatClaveProdServ=\"Muebles\" CatClaveUnidad=\"piece\" posicion=\"0\">\n" +
                "      <cfdi:Impuestos>\n" +
                "        <cfdi:Traslados>\n" +
                "          <cfdi:Traslado Base=\"1.00\" Impuesto=\"002\" TasaOCuota=\"0.16\" TipoFactor=\"Tasa\" Importe=\"0.16\" CatTasaOCuota=\"0.16\"/>\n" +
                "        </cfdi:Traslados>\n" +
                "      </cfdi:Impuestos>\n" +
                "    </cfdi:Concepto>\n" +
                "    <cfdi:Concepto ClaveProdServ=\"56101500\" NoIdentificacion=\"KC1000\" Cantidad=\"1\" ClaveUnidad=\"H87\" Unidad=\"PZA\" Descripcion=\"prueba02\" ValorUnitario=\"1000.00\" Importe=\"1000.00\" Descuento=\"0.00\" CatClaveProdServ=\"Muebles\" CatClaveUnidad=\"piece\" posicion=\"0\">\n" +
                "      <cfdi:Impuestos>\n" +
                "        <cfdi:Traslados>\n" +
                "          <cfdi:Traslado Base=\"1000.00\" Impuesto=\"002\" TasaOCuota=\"0.16\" TipoFactor=\"Tasa\" Importe=\"160.00\" CatTasaOCuota=\"0.16\"/>\n" +
                "        </cfdi:Traslados>\n" +
                "      </cfdi:Impuestos>\n" +
                "    </cfdi:Concepto>\n" +
                "    <cfdi:Comment9999 posicion=\"9999\">COMENTARIOS TEST</cfdi:Comment9999>\n" +
                "  </cfdi:Conceptos>\n" +
                "  <cfdi:Impuestos TotalImpuestosTrasladados=\"160.16\">\n" +
                "    <cfdi:Traslados>\n" +
                "      <cfdi:Traslado Impuesto=\"002\" TasaOCuota=\"0.16\" TipoFactor=\"Tasa\" Importe=\"160.16\" CatTasaOCuota=\"0.16\" CatTipoFactor=\"Tasa\"/>\n" +
                "    </cfdi:Traslados>\n" +
                "  </cfdi:Impuestos>\n" +
                "  <cfdi:AdditionalData plantilla=\"GRUPO\">\n" +
                "    <cfdi:Entrega rfc=\"\" nombre=\"\"/>\n" +
                "    <cfdi:Params>\n" +
                "      <cfdi:Param0>0</cfdi:Param0>\n" +
                "      <cfdi:Param1/>\n" +
                "      <cfdi:Param2/>\n" +
                "    </cfdi:Params>\n" +
                "    <cfdi:Personals/>\n" +
                "    <cfdi:DirFiscal calle=\"\" noExterior=\"\" noInterior=\"\" colonia=\"\" localidad=\"\" municipio=\"\" estado=\"\" pais=\"\" cp=\"\"/>\n" +
                "  </cfdi:AdditionalData>\n" +
                "</cfdi:Comprobante>\n";
        CFDIFactory33 factory33 = new CFDIFactory33();
        try {
            ComprobanteData cdata = (ComprobanteData) factory33.buildComprobante(xml.getBytes());
            System.out.println(cdata.getDatosComprobante().getMetodoDePago().getClave());
            System.out.println(cdata.getDatosComprobante().getMetodoDePago().getDescripcion());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void checkTipoComprobante() {
        TipoComprobanteDAO dao = new TipoComprobanteDAO();
        List<MCtipoComprobante> ls;
        ls = dao.getAll();
        for (MCtipoComprobante tmp : ls) {
            System.out.println("tmp = " + tmp.getDescripcion());
        }
    }

    private static boolean checkRespuestaServicio(String arg) {
        boolean res = true;
        if (arg != null && !arg.isEmpty() && !arg.contains("null") && !arg.contains("NULL") && !arg.contains("Null")) {
            // TODO: 02/06/2017 ACTUALIZAR LA FORMA EN QUE SE OBTIENE EL UUID
            String uuidNuevo = "";
            Pattern p1 = Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");
            Pattern p2 = Pattern.compile("PRUEBAXX-ATEB-SERV-SACV-TIMBREPRUEBA");
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

                if (uuidNuevo.matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}") ||
                        uuidNuevo.matches("PRUEBAXX-ATEB-SERV-SACV-TIMBREPRUEBA")) {
                    System.out.println("UUID->" + uuidNuevo);
                } else {
                    res = false;
                    System.out.println("El UUID obtenido no es valido" + uuidNuevo);
                }
            } else {
                res = false;
                // TODO: 19/07/2017  BUSCAR CODIGO DE ERROR EN EL XML DE RESPUESTA
                if (arg.length() > 400) {
                    System.out.println("{" + arg.substring(127, 391) + "}"); //Obtenemo un substring
                } else {
                    System.out.println(arg);
                }
            }
        } else {
            System.out.println("Ocurrio un error al procesar la factura, no se obtuvo respuesta del servidor");
            res = false;
        }
        return res;
    }

    private static void checkTasaCuota() {
        List<MCcuotasImpuestos> ls;//impuestos
        List<MCcuotasImpuestos> lf;//factores
        List<MCcuotasImpuestos> lt; //tasa o cuota
        TasasDAO dao = new TasasDAO();
        ls = dao.getByTipoAndImpuestoAndFactor("Fijo", "IVA", "Tasa");
        for (MCcuotasImpuestos tmp : ls) {
            System.out.println("tmp = " + tmp.getValorMinimo() + "-" + tmp.getValorMaximo() + "-" + tmp.getImpuesto());
        }
        System.out.println("-------------");
        ls = dao.getByTipoAndImpuestoAndFactor("Fijo", "IEPS", "Tasa");
        for (MCcuotasImpuestos tmp : ls) {
            System.out.println("tmp = " + tmp.getValorMinimo() + "-" + tmp.getValorMaximo() + "-" + tmp.getImpuesto());
        }
        System.out.println("-------------");
        ls = dao.getByTipoAndImpuestoAndFactor("Fijo", "IEPS", "Cuota");
        for (MCcuotasImpuestos tmp : ls) {
            System.out.println("tmp = " + tmp.getValorMinimo() + "-" + tmp.getValorMaximo() + "-" + tmp.getImpuesto());
        }
    }

    private static void checkRegimenFiscal() {
        List<MCregimenFiscal> ls;
        RegimenDAO dao = new RegimenDAO();
        ls = dao.getAll();
        for (MCregimenFiscal tmp : ls) {
            System.out.println("tmp = " + tmp.getDescripcion());
        }
        System.out.println("-----------------------");
        ls = dao.getRegimenFisicas();
        for (MCregimenFiscal tmp : ls) {
            System.out.println("tmp = " + tmp.getDescripcion());
        }
        System.out.println("-----------------------");
        ls = dao.getRegimenMoral();
        for (MCregimenFiscal tmp : ls) {
            System.out.println("tmp = " + tmp.getDescripcion());
        }
    }

    private static void checkPatenteAduanal() {
        List<MCpatentesAduanales> ls;
        PatenteAduanalDAO dao = new PatenteAduanalDAO();
        ls = dao.get50("400");
        for (MCpatentesAduanales tmp : ls) {
            System.out.println("tmp = " + tmp.getPatente());
        }

    }


    private static void checkPais() {
        List<MCpais> ls;
        PaisDAO dao = new PaisDAO();
        ls = dao.getAll();
        System.out.println("ls" + ls.size());
        ls = dao.get25("isr");
        for (MCpais tmp : ls) {
            System.out.println("tmp = " + tmp.getDescripcion());
        }
    }

    private static void checkPedimentos() {
        PedimentosDAO dao = new PedimentosDAO();
        List<MCpedimentoAduana> ls;
        ls = dao.get50("317");
        for (MCpedimentoAduana tmp : ls) {
            System.out.println("tmp = " + tmp.getAduana() + "-" + tmp.getPatente() + "-" + tmp.getEjercicio() + "-" + tmp.getCantidad());
        }
    }

    public static void checkMoneda() {
        List<MCmoneda> ls;
        MonedaDAO dao = new MonedaDAO();
        ls = dao.getAll();
        for (MCmoneda tmp : ls) {
            System.out.println("tmp = " + tmp.getClave());
        }
        System.out.println("------------------------------------------");
        ls = dao.get25("MX");
        for (MCmoneda tmp : ls) {
            System.out.println("tmp = " + tmp.getClave());
        }

    }

    public static void checkImpuesto() {
        List<MCimpuesto> ls;
        ImpuestoDAO dao = new ImpuestoDAO();
        ls = dao.getAll();
        for (MCimpuesto tmp : ls) {
            System.out.println("tmp = " + tmp.getClave());
        }
    }

    private static void checkFormaPago() {
        List<MCformapago> ls;
        FormaPagoDAO dao = new FormaPagoDAO();
        ls = dao.getAll();
        for (MCformapago tmp : ls) {
            System.out.println("tmp = " + tmp.getCodigo());
        }
    }

    private static void checkCodigoPostal() {
        CodigoPostalDAO dao = new CodigoPostalDAO();
        boolean res = dao.checkExist("00000");
        System.out.println("res" + res);
        List<MCcodigopostal> ls = dao.get50("200");
        for (MCcodigopostal tmp : ls) {
            System.out.println("tmp = " + tmp.getCodigoPostal());
        }
    }

    private static void checkClaveProducto() {
        ProductosDAO dao = new ProductosDAO();
        List<MCprodserv> res = dao.getLimitedRows("ratas");
        for (MCprodserv tmp : res) {
            System.out.println("tmp = " + tmp.getClave());
        }
    }

    public static void checkUnidad() {
        ClaveUnidadDAO fa = new ClaveUnidadDAO();
        List<MCunidades> res = fa.getLImitRows("centi");
        if (res != null) {
            System.out.println("res" + res.size());
            for (int i = 0; i < res.size(); i++) {
                System.out.println("i = " + res.get(i).getSimbolo());
            }
        }
    }

    private static void checkAduana() {
        AduanaDAO dao = new AduanaDAO();
        List<MCaduana> tmp = dao.getAll();
        for (MCaduana obj : tmp) {
            System.out.println("obj = " + obj.getcAduan());
        }
    }

    public static void testFormat() {
        ///CHECK THIS IS USING NUMBER FORMAT
        NumberFormat formatter1 = new DecimalFormat("#0.00");
        System.out.println(formatter1.format(1234.0));

        NumberFormat formatter2 = new DecimalFormat("#0.000");
        System.out.println(formatter2.format(1234.0));

        NumberFormat formatter3 = new DecimalFormat("#0.0000");
        System.out.println(formatter3.format(1234.0));

        NumberFormat formatter41 = new DecimalFormat("#0.000000");
        System.out.println(formatter41.format(1234.152225));

        NumberFormat formatter42 = new DecimalFormat("#0.000000");
        System.out.println(formatter42.format(1234.162226));

        NumberFormat formatter5 = new DecimalFormat("#0.");
        System.out.println(formatter5.format(1234.0));
    }

    public static void testRoundFloat() {
        System.out.println("ROUND FLOAT");
        ///THIS IS USING DECIMAL FORMATS
        DecimalFormat df = new DecimalFormat("#.####");
        ///USING MOST COMMON ROUNDING MODE
        df.setRoundingMode(RoundingMode.UP);
        for (Number n : Arrays.asList(12, 123.12344, 123.12345, 0.23, 0.1, 2341234.212431324)) {
            Double d = n.doubleValue();
            System.out.println(df.format(d));
        }
    }

    private static void imprimirMOtro(int id) {
        CfdiDAO dao = new CfdiDAO();
        MOtro otro = dao.Otro(id);
        if (otro != null)
            System.out.println(otro.getParam20());
    }

    private static void testTipoDocFactura() {
        System.out.println("Buscando tipo documento factura");
        TipoDocsFactManDao dao = new TipoDocsFactManDao();
        List<MTdocsFactman> res = dao.getAll();
        for (MTdocsFactman tmp : res) {
            System.out.println("TMP DATA:" + tmp.getTipodoc());
        }
        MTdocsFactman resObj = LambdasHelper.findTipoDocById(res, 1);
        System.out.println("resObj.getTipodoc() = " + resObj.getTipodoc());

    }


    private static void sendMail() {
        String mensaje = "Test";
        String asunto = "Test";
        String[] mails = new String[]{"eflores@ebs.com.mx"};
        SendMail mailManager = new SendMail();
        mailManager.sendEmail(asunto, mensaje, mails);
    }


    private static void calculaPerfilUsandoAnd(){
        ///(2^11)-1= 2047 -> 11111111111(binario)
        /// 2^11=2048
        ///               0,...,...,10(11 elementos)
        long[] valores = {1,2,4,8,16,32,64,128,256,512,1024};
        long valorFinalSuma = 0;
        long valorFinalOr = 0;
        for(long tmp:valores){
            valorFinalSuma+=tmp;
            valorFinalOr |= tmp;
        }
        System.out.println("valorFinalOr = " + valorFinalOr);
        System.out.println("valorFinalSuma = " + valorFinalSuma);
    }
}

