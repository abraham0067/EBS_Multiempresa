/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.xml;

import com.barcodelib.barcode.QRCode;
import fe.db.DBServer;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRXmlDataSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 *
 * @author Carlo
 */
public class XMLCfd {

    public XMLCfd() {
    }   
    public void writeZipBytes(HttpServletRequest request, HttpServletResponse response, String path) {
//        System.out.println("path= " + path);
        try {
            String cfd = request.getParameter("id_cfd");
            String factura= request.getParameter("invNumber");
//            System.out.println("factura "+ factura);
            if (cfd == null) {
                cfd = "";
            }            
            response.setContentType("application/octet-stream");

            byte[] bytesZip = getZipBytes(cfd, path);

            response.setContentLength(bytesZip.length);
            response.setHeader("Content-Disposition", "attachment; filename=\"" + factura+ ".zip\"");
            response.getOutputStream().write(bytesZip, 0, bytesZip.length);


            response.getOutputStream().flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] getZipBytes(String cfd, String path) {
        String str = path + "/downloadxml/";
        byte[] bytesZip = new byte[0];
        DBServer db = new DBServer();

        try {
            ResourceBundle localResourceBundle = ResourceBundle.getBundle("ebs.config.dbConnect", new Locale("sp", "MX"));
            String str2 = localResourceBundle.getString("recordPath");
            if (str2 == null) {
                str2 = "";
            }
            String str5 = "";
            str5 = path + "/pagina/xsl/" + localResourceBundle.getString("reportFileName");
            String str6 = path + "/pagina/images/";
            db.init("newConnect");
            ResultSet rs = db.getRS("select  x. XMLP, x.XML, i.NUMERO_FACTURA,e.RFC_ORIGEN, i.RFC, i.TOTAL,  x.UUID from M_CFD i INNER  join M_CFD_XML x  on (i.id=x.CFD_ID)   inner join M_EMPRESA e on (i.empresa_id=e.id) where i.id='" + cfd + "'");
            String nombreARchi = "";
            if (rs.next()) {
                nombreARchi = rs.getString(3);
                HashMap<String, Object> hm = new HashMap<String, Object>();
                Blob blob = rs.getBlob(1);
                byte[] xmlb = blob.getBytes(1, (int) blob.length());
                String xmlDoc = new String(xmlb);
                xmlDoc = new fe.xml.CharUnicode().getTextEncoded2(xmlDoc);
                Blob blob2 = rs.getBlob(2);
                byte[] xmlb2 = blob2.getBytes(1, (int) blob2.length());
                String xmlDocX = new String(xmlb2);
                xmlDocX = new fe.xml.CharUnicode().getTextEncoded2(xmlDocX);
                String reportFileName = str5;
                {
                    Image img = get2DBarCode(
                            rs.getString(4),
                            rs.getString(5),
                            rs.getString(6),
                            rs.getString(7));
                    hm.put("2DCODEBAR", img);
                }
                rs.close();
                String recordPath = str2;
                JRXmlDataSource jrxmlds = new JRXmlDataSource(new java.io.ByteArrayInputStream(xmlDoc.getBytes()), recordPath);
                jrxmlds.setLocale(new Locale("sp", "MX"));
                jrxmlds.setNumberPattern("#,##0.00");
                hm.put("IMAGES", str6);
                byte[] bytesPdf = JasperRunManager.runReportToPdf(
                        reportFileName,
                        hm,
                        jrxmlds);
                byte[] bytesXml = xmlDocX.getBytes();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ZipOutputStream outZip = new ZipOutputStream(baos);
                ZipEntry entryPdf = new ZipEntry(nombreARchi + ".pdf");
                outZip.putNextEntry(entryPdf);
                outZip.write(bytesPdf, 0, bytesPdf.length);
                outZip.closeEntry();

                ZipEntry entryXml = new ZipEntry(nombreARchi + ".xml");
                outZip.putNextEntry(entryXml);
                outZip.write(new byte[]{0xffffffef, 0xffffffbb, 0xffffffbf}, 0, 3);
                outZip.write(bytesXml, 0, bytesXml.length);
                outZip.closeEntry();
                outZip.close();
                bytesZip = baos.toByteArray();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();

        return bytesZip;
    }

    public void writeBytes(HttpServletRequest request, HttpServletResponse response, String path) {
        DBServer db = new DBServer();
//        Collection col = null;
        try {
            {
                String cfd = request.getParameter("cfd");
                if (cfd == null) {
                    cfd = "";
                }

                db.init("newConnect");
                ResultSet rs = db.getRS("SELECT ESTADO_DOCUMENTO FROM SBFACTURA.invoice WHERE NUMERO_FACTURA='" + cfd + "'");
                String estado = "1";
                if (rs.next()) {
                    estado = rs.getString(1);
                }

                String xmlDoc = new String(rs.getBytes(2));
                rs.close();

                byte[] bytes = xmlDoc.getBytes();


                response.setContentLength(bytes.length + 3);
                response.setHeader("Content-Disposition", "attachment; filename=\"" + cfd + ".xml\"");
                response.getOutputStream().write(new byte[]{0xffffffef, 0xffffffbb, 0xffffffbf}, 0, 3);
                response.getOutputStream().write(bytes, 0, bytes.length);

            }

            response.getOutputStream().flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    private Image get2DBarCode(String rfcEmi, String rfcRec, String total, String uuid) throws Exception {
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
        BufferedImage bi = new BufferedImage(118, 118, BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g = (Graphics2D) bi.getGraphics();

        QRCode barcode = new QRCode();
        barcode.setData("?re=" + rfcEmi + "&rr=" + rfcRec + "&tt=" + nf.format(Double.parseDouble(total)) + "&id=" + uuid);
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

        return bi;
    }
}
