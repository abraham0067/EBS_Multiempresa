package fe.net;

import com.ebs.fe.wsgi.prd.ClientePrd;
import com.ebs.fe.wsgi.test.ClienteTest;
import com.ebs.fe.wsgi.util.Zipper;
import com.ebs.util.TimeZoneCP;
import fe.sat.ComprobanteException;
import fe.sat.complementos.ComplementoException;
import fe.sat.v33.CFDIFactory33;
import fe.sat.v33.ComprobanteData;
import fe.sat.v33.DatosComprobanteData;
import fe.xml.CharUnicode;
import org.jdom.Document;
import org.jdom.output.XMLOutputter;

import java.io.*;
import java.util.Calendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

//import com.ebs.util.Zipper;

public class ClienteFacturaManual {

    public static void serializeObjectToFile(ComprobanteData data) {
        FileOutputStream fout = null;
        ObjectOutputStream oos = null;
        try {
            fout = new FileOutputStream("C:\\Temp\\Obj_" + (Calendar.getInstance().getTime().toString()) + ".ser");
            oos = new ObjectOutputStream(fout);
            oos.writeObject(data);
            System.out.println("Done");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String exeGenFactura(byte[] bxml, byte[] bxmlp, String clave, String ambiente, boolean debug) {
        String resp = "---";
        try {
            CharUnicode charUnicode = new CharUnicode();

            String xmlPrint = new String(bxmlp);
            xmlPrint = xmlPrint.replaceAll("ISO-8859-1", "UTF-8");
            xmlPrint = charUnicode.getTextEncoded2(xmlPrint);

            String xml = new String(bxml);
            xml = xml.replaceAll("ISO-8859-1", "UTF-8");
            xml = charUnicode.getTextEncoded2(xml);
            if(debug) {
                System.out.println(">>>>>>>>>> FACTURACION MANUAL XML_SAT :  <<< \n" + xml + "\n >>>");
                System.out.println(">>>>>>>>>> FACTURACION MANUAL XML_PRINT :  <<< \n" + xmlPrint + "\n >>>");
            }

            byte[] both = getZipBytes(xml, xmlPrint);//Empaquetamos xml y xmlp en un solo zip
            resp = generaTimbrado(both, clave, ambiente);

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


    public String exeGenFactura(ComprobanteData cmp, String clave, String ambiente, boolean debug) {
        String resp = "---";
        try {
            //serializeObjectToFile(cmp);
            CFDIFactory33 cfdi = new CFDIFactory33();
            CharUnicode charUnicode = new CharUnicode();


            // =========================================== Datos dummy para que las validaciones iniciales del proceso de captura manual, no genere errores de datos==========================

            ((DatosComprobanteData) cmp.getDatosComprobante()).setFecha(
                    (new TimeZoneCP().getUTC(
                            cmp.getDatosComprobante().getLugarExpedicion().getClave())
                    ));
            ((DatosComprobanteData) cmp.getDatosComprobante()).setSello("qQSvHZPzRB7eT6f9dJs3GepVl82eq4aOiI4b2hK+YSTcaJXgrDm8GfmZyUMnJ5XLs/TZDtAeafF5W1oyEygqva5fA3Ga5agq9HKHMEZx2qCOgOB+97C26StVKUdGD3jcPCh64AEgXLdCftIPwiRXP6eAr0IeeaujjVdEobvuxyo=");
            ((DatosComprobanteData) cmp.getDatosComprobante()).setNoCertificado("00001000000103168809");//Mas de 20 caracteres
            ((DatosComprobanteData) cmp.getDatosComprobante()).setCertificado("Certizficado de prueba");
            // ================================================================================================================================================================================





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

//            xout.setFormat(xout.getFormat().setEncoding("ISO-8859-1"));

       //     ByteArrayOutputStream bout = new ByteArrayOutputStream();
          //  ByteArrayOutputStream boutp = new ByteArrayOutputStream();

//            xout.output(doc, bout);
  //          xout.output(docp, boutp);

//            String xmlPrint = new String(bout.toByteArray());
//            xmlPrint = xmlPrint.replaceAll("ISO-8859-1", "UTF-8");
//            xmlPrint = charUnicode.getTextEncoded2(xmlPrint);

//            Document docXml = cfdi.getDocument();
//            XMLOutputter xoutXml = new XMLOutputter();
//            xoutXml.setFormat(xoutXml.getFormat().setEncoding("ISO-8859-1"));
//            ByteArrayOutputStream boutXml = new ByteArrayOutputStream();
//            xoutXml.output(docXml, boutXml);
//            String xml = new String(boutXml.toByteArray());
//            xml = xml.replaceAll("ISO-8859-1", "UTF-8");
//            xml = charUnicode.getTextEncoded2(xml);

//            String xml = new String(bout.toByteArray());
//            String xmlp = new String(boutp.toByteArray());
//            xml = charUnicode.getTextEncoded2(xml);
//            xmlp = charUnicode.getTextEncoded2(xmlp);

            XMLOutputter xoutXml = new XMLOutputter();
            xoutXml.setFormat(xoutXml.getFormat().setEncoding("ISO-8859-1"));
            ByteArrayOutputStream boutXml = new ByteArrayOutputStream();
            xoutXml.output(doc, boutXml);
            String xml = new String(boutXml.toByteArray());
            xml = xml.replaceAll("ISO-8859-1", "UTF-8");
            xml = charUnicode.getTextEncoded2(xml);

            if(debug) {
                System.out.println(">>>>>>>>>> FACTURACION MANUAL XML_SAT :  <<< \n" + xml + "\n >>>");
                System.out.println(">>>>>>>>>> FACTURACION MANUAL XML_PRINT :  <<< \n" + xmlPrint + "\n >>>");
            }
//            byte[] cdires = com.ebs.fe.wsgi.cliet.Cliente.genInvoice(Zipper.compress("cfdi", xml.getBytes()), clave);
            /**
             * com.ebs.fe.wsgi.client.TestCliente.genInvoice(Zipper.compress("cfdi",
             * xml.getBytes()),clave);
             */
            byte[] cdires = null;
            byte[] des = null;


            //Xml de timbrado/
            //byte[] xmls = Zipper.compress("cfdi", xml.getBytes());
            //Xml de impresion
            //byte[] xmlp = Zipper.compress("xml_p", xml.getBytes());
            byte[] both = getZipBytes(xml, xmlPrint);//Empaquetamos xml y xmlp en un solo zip
            resp = generaTimbrado(both, clave, ambiente);

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

    private String generaTimbrado(byte[] zip, String clave, String ambiente) throws Exception{
        String resp;
        byte[] cdires = null;
        byte[] des = null;

        if ((ambiente.equalsIgnoreCase("PRODUCTIVO") ||
                ambiente.equalsIgnoreCase("PRODUCCION") ||
                ambiente.equalsIgnoreCase("PROD"))) {

            System.out.println("GENERANDO FACTURA - PRODUCTIVO");

            //cdires = new wsGenInvoice().wsGenInvoice_Manual(both, clave);
            cdires = ClientePrd.genInvoiceManual(zip, clave);
            des = Zipper.uncompress(cdires);
            resp = new String(des);
        } else if( ambiente.equalsIgnoreCase("PRUEBAS")
                || ambiente.equalsIgnoreCase("QA")
                || ambiente.equalsIgnoreCase("DES")
                || ambiente.equalsIgnoreCase("TEST")
                || ambiente.equalsIgnoreCase("DESARROLLO")) {
            System.out.println("GENERANDO FACTURA - QA");

            //cdires = new wsGenInvoice().wsGenInvoice_Manual_Test(both, clave);
            cdires = ClienteTest.genInvoiceManual(zip, clave);
            des = Zipper.uncompress(cdires);
            resp = new String(des);
        } else {
            resp ="<DEVELOPMENT STAGE>";
        }

        return resp;
    }

    public byte[] getZipBytes(String xml, String xmlp)
            throws Exception {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ZipOutputStream zout = new ZipOutputStream(bout);
        ZipEntry zent1 = new ZipEntry("cfdi");
        zout.putNextEntry(zent1);
        zout.write(xml.getBytes());
        zout.closeEntry();
        ZipEntry zent2 = new ZipEntry("xml_p");
        zout.putNextEntry(zent2);
        zout.write(xmlp.getBytes());
        zout.closeEntry();
        zout.flush();
        zout.close();

        return bout.toByteArray();
    }
}
