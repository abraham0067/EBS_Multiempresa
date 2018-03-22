package com.ebs.reports;

import com.barcodelib.barcode.QRCode;
import fe.db.MPlantilla;
import fe.model.dao.LoginDAO;
import mx.com.ebs.emision.factura.vistas.AuxDomainAction;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by eflores on 21/08/2017.
 */
public class JasperGenCfdi33 {
    public static byte[] genPdf(int edoDocumento, byte[] xml, byte[] xmlp, MPlantilla plantilla) throws Exception {
        System.out.println("GENERANDO PDF CFDIV_33");
        byte[] bytesPdf = new byte[0];
        LoginDAO loginDao = new LoginDAO();
        HashMap<String, Object> hm = new HashMap();
        String recordPath = "/Comprobante/Conceptos/Concepto";

        java.net.URL path2 = AuxDomainAction.class.getProtectionDomain().getCodeSource().getLocation();
        String rutaAbsoluta = loginDao.ObtenerRuta(path2.getPath());
        rutaAbsoluta = loginDao.obtenerRutaDeDirectorioAnterior(rutaAbsoluta, "WEB-INF");// Rutad

        hm.put("VISTA", "1");
        hm.put("IMAGES", rutaAbsoluta + "imagenesReportes/");
        hm.put("ESTATUS", "" + edoDocumento);
        hm.put("REPORTES", rutaAbsoluta + "/reportes/");
        hm.put("CANCELADO", rutaAbsoluta);
        hm.put("Proporcion", 0.56d);

        SAXBuilder sax = new SAXBuilder();
        Document doc = null;
        try {
            //System.out.println(new String(xml));
            doc = sax.build(new ByteArrayInputStream(xml));
        } catch (JDOMException e) {
            e.printStackTrace(System.out);
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
        Namespace ns = doc.getRootElement().getNamespace();
        Namespace tfd = Namespace.getNamespace("http://www.sat.gob.mx/TimbreFiscalDigital");

        String uuid = "AAAAAAAA-AAAA-AAAA-AAAA-AAAAAAAAAAAA";
        String rfcEmisor = "AAAAAAAAAAAAA";
        String rfcReceptor = "AAAAAAAAAAAAA";
        String total = "00.00";
        String sello = "1111111111111111111111111111111111111111=";

        try {
            uuid = doc.getRootElement().getChild("Complemento", ns).getChild("TimbreFiscalDigital", tfd).getAttributeValue("UUID");
        } catch (NullPointerException e) {
            System.out.println("UUID NOT FOUND");
            uuid = "AAAAAAAA-AAAA-AAAA-AAAA-AAAAAAAAAAAA";
            e.printStackTrace(System.out);
        }

        try {
            rfcEmisor = doc.getRootElement().getChild("Emisor", ns).getAttributeValue("Rfc");
        } catch (NullPointerException e) {
            System.out.println("EMISOR RFC NOT FOUND");
            e.printStackTrace();
            rfcEmisor = "AAAAAAAAAAAAA";
        }
        try {
            rfcReceptor = doc.getRootElement().getChild("Receptor", ns).getAttributeValue("Rfc");
        } catch (NullPointerException e) {
            System.out.println("RECEPTOR RFC NOT FOUND");
            rfcReceptor = "AAAAAAAAAAAAA";
            e.printStackTrace();
        }

        try {
            total = doc.getRootElement().getAttributeValue("Total");
        } catch (NullPointerException e) {
            System.out.println("TOTAL NOT FOUND");
            total = "00.00";
            e.printStackTrace();
        }
        //ultimos 8 numeros del sello del emisor
        try {
            sello = doc.getRootElement().getAttributeValue("Sello");
        } catch (NullPointerException e) {
            sello = "1111111111111111111111111111111111111111=";
            System.out.println("SELLO NOT FOUND");
            e.printStackTrace();
        }

        if (total == null) {
            total = "00.00";
        }
        if (sello == null) {
            sello = "1111111111111111111111111111111111111111=";
        }

        Image img = JasperGenCfdi33.get2DBarCode(
                "https://verificacfdi.facturaelectronica.sat.gob.mx/default.aspx",//Url de consulta
                uuid,//UUID
                rfcEmisor,
                rfcReceptor,
                total,
                sello
        );
        ///Ponemos la imagen del qr en ambos parametros debido a que aun no se estandariza el nombre del param del QR
        hm.put("2DCODEBAR", img);
        hm.put("QRCODE", img);

        InputStream in = null;
        if (plantilla.getRootPath() != null
                && !plantilla.getRootPath().isEmpty()) {
            recordPath = plantilla.getRootPath();
        }

        in = setParameters(plantilla.getPlantilla(), hm);

        JRFileVirtualizer virt = new JRFileVirtualizer(300, System.getProperty("java.io.tmpdir"));
        virt.setReadOnly(false);
        hm.put(JRParameter.REPORT_VIRTUALIZER, virt);
        ///Para la informacion del pdf enviamos el xmlp
        JRXmlDataSource jrxmlds = new JRXmlDataSource(new java.io.ByteArrayInputStream(xmlp), recordPath);
        jrxmlds.setLocale(new Locale("sp", "MX"));
        jrxmlds.setNumberPattern("#,##0.00");
        bytesPdf = JasperRunManager.runReportToPdf(in, hm, jrxmlds);
        return bytesPdf;
    }

    /**
     * Can throw zip exception
     *
     * @param zip
     * @param parameters
     * @return
     * @throws Exception
     */
    public static InputStream setParameters(byte[] zip, HashMap<String, Object> parameters) throws Exception {
        return JasperZip.setUnZipBytes(zip, parameters);
    }

    private static Image get2DBarCode(String url, String uuid, String rfcEmi, String rfcRec, String total, String selloDigEmisor) throws Exception {
        NumberFormat nf = NumberFormat.getInstance(new Locale("sp", "MX"));
        nf.setMaximumFractionDigits(6);
        nf.setMinimumFractionDigits(6);
        nf.setMaximumIntegerDigits(10);
        nf.setMinimumIntegerDigits(10);
        nf.setGroupingUsed(false);

        // Gen 2D CodeBar barcode
        int uom = 1;        //  0 - Pixel, 1 - CM, 2 - Inch
        int resolution = 75;
        int rotate = 0;     //  0 - 0, 1 - 90, 2 - 180, 3 - 270
        float moduleSize = 0.100f;

        // save barcode in "generated" folder
        //    BufferedImage bi = new BufferedImage(118,118,BufferedImage.TYPE_BYTE_BINARY);
        BufferedImage bi = new BufferedImage(118, 118, BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g = (Graphics2D) bi.getGraphics();
        String tt = "0.000000";
        try {
            tt = nf.format(Double.parseDouble(total));
        } catch (NumberFormatException e) {
            tt = "0.000000";
            e.printStackTrace();
        }
        String selloAppend = "";
        if (selloDigEmisor.length() <= 8) {
            selloAppend = selloDigEmisor;
        } else {
            selloAppend = selloDigEmisor.substring(selloDigEmisor.length() - 8, selloDigEmisor.length());
        }
        QRCode barcode = new QRCode();
        barcode.setData(url + "?id=" + uuid + "&re=" + rfcEmi + "&rr=" + rfcRec + "&tt=" + tt + "&fe=" + selloAppend);
        //https://sat.mx/detallecfdi.aspx?&id=ad662d33-6934-459c-a128-bdf0393f0f44&fe=MVC0rdw%3Dsa&re=XAXX010101000&rr=XAXX010101000&tt=123456789012345678.123456
        // barcode.setDataMode(QRCode.MODE_BYTE);
        barcode.setDataMode(QRCode.MODE_AUTO);
        barcode.setVersion(10);
        barcode.setEcl(QRCode.ECL_M);

        barcode.setUOM(uom);
        barcode.setModuleSize(moduleSize);
        barcode.setLeftMargin(0.1f);
        barcode.setRightMargin(0.1f);
        barcode.setTopMargin(0.1f);
        barcode.setBottomMargin(0.1f);
        barcode.setResolution(resolution);
        barcode.setRotate(rotate);

        barcode.renderBarcode(g, new Rectangle());

        // Insert IT image
        //    BufferedImage img = ImageIO.read(getClass().getResourceAsStream("/sb/emitir/resources/if_logo_f.jpg"));
        //    g.drawImage(img, (int)((bi.getWidth() - img.getWidth()) / 2), (int)((bi.getHeight() - img.getHeight()) / 2), null);

        return bi;
    }
}
