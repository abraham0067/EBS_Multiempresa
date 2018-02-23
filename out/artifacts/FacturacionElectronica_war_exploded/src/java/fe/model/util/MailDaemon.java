package fe.model.util;

/**
 *
 * Clase para env�o de correo como demonio en FE
 *
 * @Author: Carlo Garcia
 * @Date: 16-03-2009
 *
 * Co: Electronic Bills & Services S de CV
 *
 */
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRXmlDataSource;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.barcodelib.barcode.QRCode;

import fe.db.DBServer;
import fe.db.MCfd;
import fe.db.MCfdXml;
import fe.db.MLogEnvio;
import fe.model.util.hibernateutil.HibernateUtilApl;
import fe.model.util.hibernateutil.HibernateUtilEmi;
import fe.xml.ReadXMLPropertiesX;


/**
 *
 * Demonio para el envio de Correos para FE
 *
 * Recupera las facturas de la tabla Invoice que tenga habilitado el envio por
 * correo en la tabla EBS_ACCESO, que este capturado el coreo(s) y no haya sido
 * enviado anteriormente
 *
 */
public class MailDaemon extends Thread {

    private HibernateUtilApl hibManagerRO ;//Read only interface
    private HibernateUtilEmi hibManagerSU ;
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private ZipOutputStream outZip;
    private String SMTP_HOST_NAME = "";
    private String SMTP_AUTH_USER = "";
    private String SMTP_AUTH_PWD = "";
    @SuppressWarnings("unused")
	private String SMTP_AUTH_PORT = "25";
    private int delay = 60000; // 60000 ms = 1 min

    public static void main(String[] args) {
        MailDaemon md = new MailDaemon(args[0]);
        md.start();
//        MailDaemon md = new MailDaemon("1000");
//        md.process();
    }
//    MailDaemon(){
//    }

    public MailDaemon(String myDelay) {
        super();
        hibManagerSU = new HibernateUtilEmi();
        hibManagerRO = new HibernateUtilApl();
        try {
            delay = Integer.parseInt(myDelay);
        } catch (Exception dex) {
        }
    }

    public void run() {
        System.out.println("Starting mail daemon at " + new Date().toString());
        while (true) {
            try {
                process();
                System.gc();

                sleep(delay);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @SuppressWarnings("unused")
	private void process() {

        DBServer db = new DBServer();
        try {
            ReadXMLPropertiesX sxml = new ReadXMLPropertiesX("config/MailDaemon.xml");

            System.out.println("  Reading invoice ... at " + new Date().toString());
            db.init();

            PreparedStatement ps = db.getConnection().prepareStatement(
                    sxml.getValue("/getInvoicesToMailing"));

            ResultSet rs = ps.executeQuery();
            int cont = 0;

            while (rs.next() && cont < 300) {

                String bill = rs.getString("BILL");
                String mail = rs.getString("MAIL");
                if (mail == null) {
                    mail = "";
                }
                String status = rs.getString("STATUS");

                System.out.print("    Sending " + bill + " ... ");
                try {
                    if (mail != null || !mail.equals("")) {
                        mail = mail.replace(';', ',');
                        String[] mails = mail.split(",");
                        if (getZip_PdfXml(bill, mails, status)) {
                            PreparedStatement psu = db.getConnection().prepareStatement(sxml.getValue("/updateInvoiceMailed"));
                            psu.setString(1, bill);

                            psu.execute();

                            System.out.println("Ok");

                            psu.close();
                        } else {
                            System.out.println("Error");
                        }
                    } else {
                        updateError(mail, "No se puede enviar esta factura ya que no cuenta con direccion de correo");
                    }
                } catch (Exception e) {
                    System.out.println("Error");
                    updateError(mail, e.toString());
                    e.printStackTrace();
                }

                cont++;
            }
            rs.close();
            ps.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            db.close();
        }
    }

    @SuppressWarnings("unused")
	private boolean getZip_PdfXml(String bill, String[] mails, String status) {
        boolean resp = false;
        try {
            String reportFileName = "AvisoBanco.jasper";
//            if(bill.toUpperCase().startsWith("NG"))
//                reportFileName = "ferreBnotaCargo.jasper";
//            if(bill.toUpperCase().startsWith("NC"))
//                reportFileName = "ferreBnotaCred.jasper";

            ReadXMLPropertiesX rxml = new ReadXMLPropertiesX("config/feConfig.xml");
            Map<String, String> map = rxml.getValuesMap("/GenComprobanteDefault");
            String us = map.get("dbXMLUser");
            String pw = map.get("dbXMLPass");
            String host = map.get("dbXMLHost");

            ByteArrayOutputStream mailOut = new ByteArrayOutputStream();
            ByteArrayOutputStream bufOutput = new ByteArrayOutputStream();
            ByteArrayOutputStream outpdf = new ByteArrayOutputStream();


            // Generacion PDF
            String reports = "./report/";
            String images = "./images/";

            reportFileName = reports + reportFileName;
            String recordPath = "/Comprobante/Conceptos/Concepto";
//			JRXmlDataSource jrxmlds = new JRXmlDataSource( new java.io.ByteArrayInputStream( xmlDoc_Prn.getBytes() ),recordPath );
//			jrxmlds.setLocale(new Locale("sp","MX"));
//			jrxmlds.setNumberPattern("#,##0.00");

            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("IMAGES", images);
            hm.put("REPORTS", reports);
            hm.put("STATUS", status);

//			byte[] bytes = 
//					JasperRunManager.runReportToPdf( 
//						reportFileName, 
//						hm, 
//						jrxmlds );

//      		outpdf.write( bytes );

            // Agrega al ZIP
//            initZip(mailOut);           
//			addToZip( bill , bufOutput, "xml" );
//			addToZip(bill, outpdf, "pdf");
//            closeZip();

//            System.out.println(outpdf);
            resp = sendMail(mails, outpdf, bill, status);
//			resp = sendMail( mails, mailOut, bill );
            bufOutput.close();
            outpdf.close();
            mailOut.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            updateError(bill, ex.toString());
        }

        return resp;
    }

    private boolean sendMail(String[] mails, ByteArrayOutputStream mailOut, String bill, String status) {
        boolean resp = false;

        try {
            ResourceBundle resourcebundle = ResourceBundle.getBundle("ebs.config.mail", new Locale("sp", "MX"));
            SMTP_HOST_NAME = resourcebundle.getString("host");
            SMTP_AUTH_USER = resourcebundle.getString("user");
            SMTP_AUTH_PWD = resourcebundle.getString("pwd");
            SMTP_AUTH_PORT = resourcebundle.getString("port");

            String subject = resourcebundle.getString("asunto");
            if (subject == null) {
                subject = " Su factura Banco Interacciones " + new Date().toString();
            }
            String content = "";
            if (status.equals("1")) {
                FileInputStream fis = new FileInputStream(resourcebundle.getString("archivohtml"));
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                for (int c = fis.read(); c != -1; c = fis.read()) {
                    bao.write(c);
                }

                content = new String(bao.toByteArray(), "ISO-8859-1");
                fis.close();
                bao.close();
            }
            if (status.equals("0")) {
                FileInputStream fisc = new FileInputStream(resourcebundle.getString("canceladohtml"));
                ByteArrayOutputStream baoc = new ByteArrayOutputStream();
                for (int cc = fisc.read(); cc != -1; cc = fisc.read()) {
                    baoc.write(cc);
                }

                content = new String(baoc.toByteArray(), "ISO-8859-1");
                fisc.close();
                baoc.close();
            }
            String from = resourcebundle.getString("cuentaMail");

            resp = postMail(mails, subject, content, from, mailOut, bill);
        } catch (Exception ex) {
            updateError(bill, ex.toString());
            ex.printStackTrace();
        }

        return resp;
    }

    private boolean postMail(
            String[] mails,
            String subject,
            String content,
            String from,
            ByteArrayOutputStream fileStream,
            String bill) throws UnsupportedEncodingException, MessagingException {

        Properties properties = new Properties();
        properties.put("mail.smtp.host", SMTP_HOST_NAME);
//        properties.put("mail.smtp.port", SMTP_AUTH_PORT);
        properties.put("mail.smtp.auth", "true");

        SMTPAuthenticator smtpauthenticator = new SMTPAuthenticator();
        Session session = Session.getDefaultInstance(properties, smtpauthenticator);
        session.setDebug(false);
        MimeMessage mimemessage = new MimeMessage(session);
        InternetAddress internetaddress = new InternetAddress(from, from);
        mimemessage.setFrom(internetaddress);
        InternetAddress ainternetaddress[] = new InternetAddress[mails.length];
        for (int i = 0; i < mails.length; i++) {
            ainternetaddress[i] = new InternetAddress(mails[i]);
        }

        mimemessage.setRecipients(javax.mail.Message.RecipientType.TO, ainternetaddress);
        mimemessage.setSubject(subject);

        MimeBodyPart mimebodypart = new MimeBodyPart();
        mimebodypart.setContent(content, "text/html");

        DataSource ds = new InputStreamDataSource(new ByteArrayInputStream(fileStream.toByteArray()), bill);
        MimeBodyPart mimebodypart1 = new MimeBodyPart();
        mimebodypart1.setDataHandler(new DataHandler(ds));
//        mimebodypart1.setFileName(bill + ".zip");
        mimebodypart1.setFileName(bill + ".pdf");


        MimeMultipart mimemultipart = new MimeMultipart();
        mimemultipart.addBodyPart(mimebodypart);
        mimemultipart.addBodyPart(mimebodypart1);

        mimemessage.setContent(mimemultipart);

        Transport.send(mimemessage);

        return true;
    }

    private class InputStreamDataSource implements DataSource {

        private InputStream inputStream;
        @SuppressWarnings("unused")
		private OutputStream outputStream;
        private String name;

        @SuppressWarnings("unused")
		public void setName(String name) {
            this.name = name;
        }

        @SuppressWarnings("unused")
		public InputStreamDataSource(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        public InputStreamDataSource(InputStream inputStream, String name) {
            this.inputStream = inputStream;
            this.name = name;
        }

        public InputStream getInputStream() {
            if (inputStream != null) {
                try {
                    inputStream.reset();
                } catch (Exception ex) {
                }
            }
            return inputStream;
        }

        public String getContentType() {
            return "application/octet-stream";
        }

        public OutputStream getOutputStream() {
            inputStream = new ByteArrayInputStream(new byte[]{});
            return new ByteArrayOutputStream();
        }

        public String getName() {
            return name;
        }
    }

    private class SMTPAuthenticator extends Authenticator {

        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(SMTP_AUTH_USER, SMTP_AUTH_PWD);
        }

        private SMTPAuthenticator() {
            super();
        }
    }

    @SuppressWarnings("unused")
	private void initZip(OutputStream myOut) {
        try {
            outZip = new ZipOutputStream(myOut);
        } catch (Exception ioexception) {
            System.out.println(ioexception.toString());
        }
    }

    @SuppressWarnings("unused")
	private void addToZip(String name, ByteArrayOutputStream out, String ext) {
        try {
            ZipEntry zipentry = new ZipEntry(
                    new StringBuilder()
                    .append(name)
                    .append(".")
                    .append(ext)
                    .toString());
            outZip.putNextEntry(zipentry);
            outZip.write(out.toByteArray(), 0, out.toByteArray().length);
            outZip.closeEntry();
        } catch (IOException ioexception) {
            System.out.println(ioexception.toString());
        }
    }

    @SuppressWarnings("unused")
	private void closeZip() {
        try {
            outZip.close();
        } catch (IOException ioexception) {
            System.out.println(ioexception.toString());
        }
    }

    private void updateError(String invoice, String error) {
        DBServer dberr = new DBServer();
        try {
            dberr.init();
        } catch (Exception ex) {
            Logger.getLogger(MailDaemon.class.getName()).log(Level.SEVERE, null, ex);
        }
        String errormail = error;
        if (errormail.startsWith("javax.mail.internet.AddressException: Illegal address in string ``")) {
            errormail = "No se puede enviar esta factura ya que no cuenta con direccion de correo";
        }
        String qry = "UPDATE INVOICE SET ERROR_FACTURA='" + errormail.replaceAll("'", "")
                + "' WHERE NUMERO_FACTURA='" + invoice + "'";
        try {
            PreparedStatement pserr = dberr.getConnection().prepareStatement(qry);
            pserr.executeUpdate();
            pserr.close();
            dberr.close();
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
    }

    @SuppressWarnings({ "unchecked", "unused" })
	private List<MCfd> ListaFacturasEnviar() throws Exception {
        List<MCfd> listMCFD = null;
        hibManagerRO.initTransaction();
        Criteria cr = hibManagerRO.getSession().createCriteria(MCfd.class);
        cr.add(Restrictions.eq("estatusMail", 1));
        listMCFD = cr.list();
        hibManagerRO.getTransaction().commit();
        hibManagerRO.closeSession();
        return listMCFD;

    }

    @SuppressWarnings("unused")
	private MCfdXml obtenerXMLFactura(MCfd cfd) {
        MCfdXml xmls = null;
        try {
            hibManagerRO.initTransaction();
            Criteria cr = hibManagerRO.getSession().createCriteria(MCfdXml.class);
            cr.add(Restrictions.eq("cfd.id", cfd.getId()));
            xmls = (MCfdXml) cr.uniqueResult();
            hibManagerRO.getTransaction().commit();
            hibManagerRO.closeSession();
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
        return xmls;
    }

    @SuppressWarnings("unused")
	private boolean ActualizarCFD(int idcfd) throws Exception {
        boolean val = false;

        hibManagerSU.initTransaction();
        try {
            MCfd cfd = (MCfd) hibManagerSU.getSession().get(MCfd.class, idcfd);
            cfd.setEstatusMail(0);
            hibManagerSU.getSession().update(cfd);
            hibManagerSU.getTransaction().commit();
            hibManagerSU.closeSession();
            val = true;
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        return val;
    }

    @SuppressWarnings("unused")
	private void ErrorLogEnvio(MCfd cfd, String error) {
        try {
            hibManagerSU.initTransaction();
            MLogEnvio log = new MLogEnvio(cfd, new Date(), error);
            hibManagerSU.getSession().saveOrUpdate(log);
            hibManagerSU.getTransaction().commit();
            hibManagerSU.closeSession();
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }


    }

    public void writeZipBytes(HttpServletRequest request, HttpServletResponse response, String path, MCfd mcfd) {
        try {
            String cfd = request.getParameter("id_cfd");
            String factura = request.getParameter("invNumber");
            System.out.println("factura " + factura);
            if (cfd == null) {
                cfd = "";
            }
            response.setContentType("application/octet-stream");

            byte[] bytesZip = getZipBytes(cfd, path);

            response.setContentLength(bytesZip.length);
            response.setHeader("Content-Disposition", "attachment; filename=\"" + factura + ".zip\"");
            response.getOutputStream().write(bytesZip, 0, bytesZip.length);
            response.getOutputStream().flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
	public void writeBytes(HttpServletRequest request, HttpServletResponse response, String path) {
        DBServer db = new DBServer();
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
            e.printStackTrace(System.out);
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