package fe.model.util;

import fe.db.MInvoice;
import fe.model.dao.CfdDAO;
import fe.model.dao.LoginDAO;
import fe.xml.Sat;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.com.ebs.emision.factura.utilierias.Limpiador;
import mx.com.ebs.emision.factura.utilierias.ManejadorFechas;
import mx.com.ebs.emision.factura.utilierias.PintarLog;
import mx.com.ebs.emision.factura.vistas.AuxDomainAction;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;

public class DescargarXML {

    private LoginDAO DAO = new LoginDAO();
    @SuppressWarnings("unused")
    private static final long serialVersionUID = 9149826260758390091L;


    /**
     * Recibe el nombre de archivo
     *
     * @param response
     * @param nombreArchivo
     */
    public void writeBytes(HttpServletResponse response, String nombreArchivo) {
        try {
            String rutaArchivos = "/lib/";
            java.net.URL path2 = AuxDomainAction.class
                    .getProtectionDomain().getCodeSource().getLocation();
            String rutaAbsoluta = "";// DAO.ObtenerRuta(path2.getPath());
            // PintarLog.println("Ruta Absoluta: "+rutaAbsoluta+rutaArchivos);
            File fr = new File(rutaAbsoluta + rutaArchivos + nombreArchivo);
            FileInputStream file = new FileInputStream(fr);
            long size = fr.length();
            int c = file.read();
            OutputStream salida = response.getOutputStream();
            // Salida al browser
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("UTF-8");
            response.setContentLength((int) size);
            response.setHeader("Content-Disposition", "attachment; filename=\""
                    + nombreArchivo + "\"");
            while (c != -1) {
                salida.write(c);
                c = file.read();
                // response.getOutputStream().write(c);
            }
            file.close();
            salida.flush();
            salida.close();
            fr = null;
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    /**
     * Metodo encargado de obtener y mostrar el XML de una factura X
     *
     * @param response
     * @param request
     * @throws Exception
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void writeXmlBytes(HttpServletRequest request, HttpServletResponse response) throws Exception {
        byte[] xmlDoc = "".getBytes();
        CfdDAO cfdDAO = new CfdDAO();

		//List<MCfdXml> archivosFactura = new ArrayList();
        //MOtro otro = null;
        try {
            String idCFD = request.getParameter("idCFD");
            if (idCFD == null) {
                idCFD = "0";
            }
            String nombreFactura = request.getParameter("numeroFactura");
            MInvoice datosFactura = cfdDAO.BuscarId(Limpiador.cleanStringToIntegers(idCFD));

            String Fol = "" + datosFactura.getFolio();
            Fol = Fol.replace(".0", "");

            if (datosFactura != null && datosFactura.getSerie() != null) {
                nombreFactura = datosFactura.getSerie().trim() + "." + Fol.trim();

            }

            if (nombreFactura == null) {
                nombreFactura = "";
            }

			//System.out.println("CFD, " + idCFD);
			// ===========================================================
            // VALIDACION DE INFORMACION
            // =================================================================
            if ("".equalsIgnoreCase(nombreFactura)) {
                xmlDoc = (new String(
                        "No se pudo obtener la factura debido a un error, idFactura nulo."))
                        .getBytes();
            } else {
                //System.out.println(nombreFactura);

                if (xmlDoc == null) {
                    xmlDoc = (new String("No se encontraron registros de la factura seleccionada.")).getBytes();

                }

            }
            // ========================================================================================================================================================

            String str = new String(xmlDoc).trim();
            str = new fe.xml.CharUnicode().getTextEncoded2(str);

			// ===========================================================
            // CONSTRUCCION DE ARCHIVO ADJUNTO
            // ===========================================================
            // System.out.println("<XML:>"+str+"<XML>");
            byte[] bytes = str.getBytes();
            if (bytes == null) {
                bytes = "".getBytes();
            }
            response.resetBuffer();
            response.reset();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\""
                    + nombreFactura + "_" + ManejadorFechas.obtenIdEvent()
                    + ".xml\"");
            response.getOutputStream().write(bytes);
            response.getOutputStream().flush();
            response.getOutputStream().close();

            // ========================================================================================================================================================
        } catch (Exception e) {
            PintarLog
                    .println(" ***======= Error al llamar al metodo writeXmlBytes: "
                            + e.getMessage());
            // e.printStackTrace();
            throw e;
        }

    }
    /**
     * Metodo encargado de obtener y mostrar el XML de una factura X
     *
     * @param response
     * @param request
     * @param idCFD
     * @param nombreFactura
     * @throws Exception
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void writeXmlBytes(HttpServletRequest request, HttpServletResponse response, String idCFD, String nombreFactura) throws Exception {
        byte[] xmlDoc = "".getBytes();
        CfdDAO cfdDAO = new CfdDAO();

		//List<MCfdXml> archivosFactura = new ArrayList();
        //MOtro otro = null;
        try {
            //String idCFD = request.getParameter("idCFD");
            if (idCFD == null) {
                idCFD = "0";
            }
            //String nombreFactura = request.getParameter("numeroFactura");
            MInvoice datosFactura = cfdDAO.BuscarId(Limpiador.cleanStringToIntegers(idCFD));

            String Fol = "" + datosFactura.getFolio();
            Fol = Fol.replace(".0", "");

            if (datosFactura != null && datosFactura.getSerie() != null) {
                nombreFactura = datosFactura.getSerie().trim() + "." + Fol.trim();

            }

            if (nombreFactura == null) {
                nombreFactura = "";
            }

			//System.out.println("CFD, " + idCFD);
			// ===========================================================
            // VALIDACION DE INFORMACION
            // =================================================================
            if ("".equalsIgnoreCase(nombreFactura)) {
                xmlDoc = (new String(
                        "No se pudo obtener la factura debido a un error, idFactura nulo."))
                        .getBytes();
            } else {
                //System.out.println(nombreFactura);

                if (xmlDoc == null) {
                    xmlDoc = (new String("No se encontraron registros de la factura seleccionada.")).getBytes();

                }

            }
            // ========================================================================================================================================================

            String str = new String(xmlDoc).trim();
            str = new fe.xml.CharUnicode().getTextEncoded2(str);

			// ===========================================================
            // CONSTRUCCION DE ARCHIVO ADJUNTO
            // ===========================================================
            // System.out.println("<XML:>"+str+"<XML>");
            byte[] bytes = str.getBytes();
            if (bytes == null) {
                bytes = "".getBytes();
            }
            response.resetBuffer();
            response.reset();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\""
                    + nombreFactura + "_" + ManejadorFechas.obtenIdEvent()
                    + ".xml\"");
            response.getOutputStream().write(bytes);
            response.getOutputStream().flush();
            response.getOutputStream().close();

            // ========================================================================================================================================================
        } catch (Exception e) {
            PintarLog
                    .println(" ***======= Error al llamar al metodo writeXmlBytes: "
                            + e.getMessage());
            // e.printStackTrace();
            throw e;
        }

    }

    public byte[] stringAByte(String cadena) {
        byte[] arreglo = new byte[cadena.length()];
        for (int i = 0; i < cadena.length(); i++) {
            arreglo[i] = (byte) cadena.substring(i, i + 1).hashCode();
        }
        return arreglo;
    }

    /**
     * Metodo encargado de obtener y mostrar el PDF de una factura X
     *
     * @param response
     * @param request
     * @throws Exception
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void writePdfBytes(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        byte[] xmlDoc = "".getBytes();
        byte[] plantilla = "".getBytes();
        byte[] bytes;
        CfdDAO cfdDAO = new CfdDAO();
		//FacturaManejador facturaManejador = new FacturaManejador();
        //List<MCfdXml> listaFacturas = new ArrayList();
        HashMap hm = new HashMap();
        //MOtro otro = null;
        MInvoice datosFactura = null;
        java.net.URL path2 = AuxDomainAction.class
                .getProtectionDomain().getCodeSource().getLocation();
        String rutaAbsoluta = DAO.ObtenerRuta(path2.getPath());
        rutaAbsoluta = DAO.obtenerRutaDeDirectorioAnterior(rutaAbsoluta,
                "WEB-INF");// Ruta

		//String rutaPDF = null;
        // del WebContent
        // PintarLog.println("ruta Absoluta: "+path2);
        try {
            String idCFD = request.getParameter("idCFD");
            if (idCFD == null) {
                idCFD = "0";
            }
            PintarLog.println("idCFD" + idCFD);
            String nombreFactura = request.getParameter("numeroFactura");
            datosFactura = cfdDAO.BuscarId(Limpiador.cleanStringToIntegers(idCFD));
            String Fol = "" + datosFactura.getFolio();
            Fol = Fol.replace(".0", "");
            if (datosFactura != null && datosFactura.getSerie() != null) {
                nombreFactura = datosFactura.getSerie().trim() + "." + Fol.trim();

            }
            if (nombreFactura == null) {
                nombreFactura = "";
            }
			// ===========================================================
            // VALIDACION DE INFORMACION
            // =================================================================
            //System.out.println("nombre_Factura: "+ nombreFactura);
            if ("".equalsIgnoreCase(nombreFactura)) {
                throw new Exception(
                        "No se pudo obtener la factura debido a un error, idFactura nulo.");
            } else {
//                System.out.println("empieza a crear pdf " + nombreFactura.trim() + "_P");
//                System.out.println("Xmldoc -- " + new String(xmlDoc));

                if (xmlDoc == null) {
//                    System.out.println("No se pudo descargar el documento");
                    xmlDoc = (new String("No se pudo obtener la factura debido a un error, idFactura nulo."))
                            .getBytes();

                }
                String str = new String(xmlDoc).trim();
                str = new fe.xml.CharUnicode().getTextEncoded2(str);
                xmlDoc = str.getBytes();

            }

			// =========================================================================================================================================================
            // Obtenemos los datos de la factura para enviar al QR
            datosFactura = cfdDAO.BuscarId(Limpiador
                    .cleanStringToIntegers(idCFD));
				// ===========================================================
            // CONSTRUCCION DE ARCHIVO ADJUNTO
            // ===========================================================

            String recordPath = "/Comprobante/Conceptos/Concepto";
				// String recordPath =
            // "/Invoice/E2EDP01[not(MENGE=0)][not(E2EDP19/KTEXT='Liquidaci�n de anticipo')][not(E2EDP19/KTEXT='Down payment settlement')]";

//            System.out.print("RutaAbsoluta: " + rutaAbsoluta);
            hm.put("IMAGES", rutaAbsoluta + "imagenesReportes/");
            hm.put("ESTATUS", "" + datosFactura.getEstatusDoc());
            hm.put("REPORTES", rutaAbsoluta + "/reportes/");
            hm.put("CANCELADO", rutaAbsoluta);
            hm.put("MONEDA", datosFactura.getMoneda());
            hm.put("CADENA", "");
            hm.put("REPORTS", rutaAbsoluta + "/reportes/");
            hm.put("STATUS", "" + datosFactura.getEstatusDoc());

            Document doc = null;
            try {
                InputStream fis = new ByteArrayInputStream(xmlDoc);
                SAXBuilder builder = new SAXBuilder();
                doc = builder.build(fis);
            } catch (Exception e) {
                e.printStackTrace(System.out);
                doc = null;
            }

            if(doc != null)
                hm.put("CADENA", new Sat().getCadenaOriginal(doc));

				// ======================= CORRE EL REPORTE PDF
            // ================================================
            InputStream in = null;
            if (datosFactura.getPlantilla() == null) {
                throw new Exception(
                        "No se encontraron plantillas  ligadas a la factura "
                        + nombreFactura);
            } else {
                if (datosFactura.getPlantilla().getRootPath() != null
                        && !"".equals(datosFactura.getPlantilla()
                                .getRootPath())) {
                    recordPath = datosFactura.getPlantilla().getRootPath();
                }
                plantilla = datosFactura.getPlantilla().getPlantilla();
                in = setParameters(plantilla, hm);
            }

            JRXmlDataSource jrxmlds = new JRXmlDataSource(new java.io.ByteArrayInputStream(xmlDoc), recordPath);
            jrxmlds.setLocale(new Locale("sp", "MX"));
            jrxmlds.setNumberPattern("#,##0.00");

            bytes = JasperRunManager.runReportToPdf(in, hm, jrxmlds);

            // =============================================================================================
            if (bytes == null) {
                bytes = "".getBytes();
            }
            response.setContentType("application/pdf");
            response.setBufferSize(1024 + bytes.length);
            response.setHeader("Content-Disposition", "attachment; filename=\""
                    + nombreFactura + "_" + ManejadorFechas.obtenIdEvent()
                    + ".pdf\"");

            response.getOutputStream().write(bytes);
            response.getOutputStream().flush();
			// response.getOutputStream().close();
            // ===========================================================================================================================================================
        } catch (Exception e) {
            PintarLog
                    .println(" ***======= Error al llamar al metodo writePdfBytes: "
                            + e.getMessage());
            // e.printStackTrace();
            throw e;
        }
    }
    /**
     * Metodo encargado de obtener y mostrar el PDF de una factura X
     *
     * @param response
     * @param request
     * @throws Exception
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void writePdfBytes(HttpServletRequest request,
            HttpServletResponse response, String idCFD, String nombreFactura) throws Exception {
        byte[] xmlDoc = "".getBytes();
        byte[] plantilla = "".getBytes();
        byte[] bytes;
        CfdDAO cfdDAO = new CfdDAO();
		//FacturaManejador facturaManejador = new FacturaManejador();
        //List<MCfdXml> listaFacturas = new ArrayList();
        HashMap hm = new HashMap();
        //MOtro otro = null;
        MInvoice datosFactura = null;
        java.net.URL path2 = AuxDomainAction.class
                .getProtectionDomain().getCodeSource().getLocation();
        String rutaAbsoluta = DAO.ObtenerRuta(path2.getPath());
        rutaAbsoluta = DAO.obtenerRutaDeDirectorioAnterior(rutaAbsoluta,
                "WEB-INF");// Ruta

		//String rutaPDF = null;
        // del WebContent
        // PintarLog.println("ruta Absoluta: "+path2);
        try {
            //String idCFD = request.getParameter("idCFD");
            if (idCFD == null) {
                idCFD = "0";
            }
            PintarLog.println("idCFD" + idCFD);
            //String nombreFactura = request.getParameter("numeroFactura");
            datosFactura = cfdDAO.BuscarId(Limpiador.cleanStringToIntegers(idCFD));
            String Fol = "" + datosFactura.getFolio();
            Fol = Fol.replace(".0", "");
            if (datosFactura != null && datosFactura.getSerie() != null) {
                nombreFactura = datosFactura.getSerie().trim() + "." + Fol.trim();

            }
            if (nombreFactura == null) {
                nombreFactura = "";
            }
			// ===========================================================
            // VALIDACION DE INFORMACION
            // =================================================================
            //System.out.println("nombre_Factura: "+ nombreFactura);
            if ("".equalsIgnoreCase(nombreFactura)) {
                throw new Exception(
                        "No se pudo obtener la factura debido a un error, idFactura nulo.");
            } else {
//                System.out.println("empieza a crear pdf " + nombreFactura.trim() + "_P");
//                System.out.println("Xmldoc -- " + new String(xmlDoc));

                if (xmlDoc == null) {
//                    System.out.println("No se pudo descargar el documento");
                    xmlDoc = (new String("No se pudo obtener la factura debido a un error, idFactura nulo."))
                            .getBytes();

                }
                String str = new String(xmlDoc).trim();
                str = new fe.xml.CharUnicode().getTextEncoded2(str);
                xmlDoc = str.getBytes();

            }

			// =========================================================================================================================================================
            // Obtenemos los datos de la factura para enviar al QR
            datosFactura = cfdDAO.BuscarId(Limpiador
                    .cleanStringToIntegers(idCFD));
				// ===========================================================
            // CONSTRUCCION DE ARCHIVO ADJUNTO
            // ===========================================================

            String recordPath = "/Comprobante/Conceptos/Concepto";
				// String recordPath =
            // "/Invoice/E2EDP01[not(MENGE=0)][not(E2EDP19/KTEXT='Liquidaci�n de anticipo')][not(E2EDP19/KTEXT='Down payment settlement')]";

//            System.out.print("RutaAbsoluta: " + rutaAbsoluta);
            hm.put("IMAGES", rutaAbsoluta + "imagenesReportes/");
            hm.put("ESTATUS", "" + datosFactura.getEstatusDoc());
            hm.put("REPORTES", rutaAbsoluta + "/reportes/");
            hm.put("CANCELADO", rutaAbsoluta);
            hm.put("MONEDA", datosFactura.getMoneda());
            hm.put("CADENA", "");
            hm.put("REPORTS", rutaAbsoluta + "/reportes/");
            hm.put("STATUS", "" + datosFactura.getEstatusDoc());

            Document doc = null;
            try {
                InputStream fis = new ByteArrayInputStream(xmlDoc);
                SAXBuilder builder = new SAXBuilder();
                doc = builder.build(fis);
            } catch (Exception e) {
                e.printStackTrace(System.out);
                doc = null;
            }

            if(doc != null)
                hm.put("CADENA", new Sat().getCadenaOriginal(doc));

				// ======================= CORRE EL REPORTE PDF
            // ================================================
            InputStream in = null;
            if (datosFactura.getPlantilla() == null) {
                throw new Exception(
                        "No se encontraron plantillas  ligadas a la factura "
                        + nombreFactura);
            } else {
                if (datosFactura.getPlantilla().getRootPath() != null
                        && !"".equals(datosFactura.getPlantilla()
                                .getRootPath())) {
                    recordPath = datosFactura.getPlantilla().getRootPath();
                }
                plantilla = datosFactura.getPlantilla().getPlantilla();
                in = setParameters(plantilla, hm);
            }

            JRXmlDataSource jrxmlds = new JRXmlDataSource(new java.io.ByteArrayInputStream(xmlDoc), recordPath);
            jrxmlds.setLocale(new Locale("sp", "MX"));
            jrxmlds.setNumberPattern("#,##0.00");

            bytes = JasperRunManager.runReportToPdf(in, hm, jrxmlds);

            // =============================================================================================
            if (bytes == null) {
                bytes = "".getBytes();
            }
            response.setContentType("application/pdf");
            response.setBufferSize(1024 + bytes.length);
            response.setHeader("Content-Disposition", "attachment; filename=\""
                    + nombreFactura + "_" + ManejadorFechas.obtenIdEvent()
                    + ".pdf\"");

            response.getOutputStream().write(bytes);
            response.getOutputStream().flush();
			// response.getOutputStream().close();
            // ===========================================================================================================================================================
        } catch (Exception e) {
            PintarLog
                    .println(" ***======= Error al llamar al metodo writePdfBytes: "
                            + e.getMessage());
            // e.printStackTrace();
            throw e;
        }
    }

    public void createZip(String filename, String carpeta) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] getZip(String[] reps, String[] imgs) throws Exception {
        return new JasperZip().getZipBytes(reps, imgs);
    }

    private InputStream setParameters(byte[] zip,
            HashMap<String, Object> parameters) throws Exception {
        return new JasperZip().setUnZipBytes(zip, parameters);
    }

    private class JasperZip {

        public byte[] getZipBytes(String[] fJaspers, String[] fImages)
                throws Exception {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ZipOutputStream zout = new ZipOutputStream(bout);
            // Reports
            for (String fJ : fJaspers) {
                ZipEntry zent = new ZipEntry("reports/" + fJ);
                zout.putNextEntry(zent);
                zout.write(getBytes(fJ));
                zout.closeEntry();
            }
            // Images
            for (String fI : fImages) {
                ZipEntry zent = new ZipEntry("images/" + fI);
                zout.putNextEntry(zent);
                zout.write(getBytes(fI));
                zout.closeEntry();
            }
            zout.flush();
            zout.close();

            return bout.toByteArray();
        }

        public InputStream setUnZipBytes(byte[] bytes,
                HashMap<String, Object> parameters) throws Exception {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ZipInputStream zin = new ZipInputStream(bais);
            ZipEntry zent = zin.getNextEntry();
            byte[] b = new byte[512];
            InputStream stream = null;

            int item = 0;
            while (zent != null) {
                ByteArrayOutputStream bout = new ByteArrayOutputStream();

                int buff = zin.read(b);
                while (buff > 0) {
                    bout.write(b, 0, buff);
                    buff = zin.read(b);
                }
                String name = zent.getName().toUpperCase().split("\\/")[1];
                name = name.toUpperCase().replaceAll(" ", "_")
                        .replaceAll("\\.", "_");

                if ("reports".equals(zent.getName().split("/")[0])) {
                    if (item == 0) {
                        stream = new ByteArrayInputStream(bout.toByteArray());
                    } else {
                        parameters.put(name,
                                new ByteArrayInputStream(bout.toByteArray()));
                    }

                    item++;
                }
                if ("images".equals(zent.getName().split("/")[0])) {
                    BufferedImage bimg = ImageIO.read(new ByteArrayInputStream(
                            bout.toByteArray()));
                    parameters.put(name, bimg);
                }
                zin.closeEntry();

                zent = zin.getNextEntry();
            }
            zin.close();

            return stream;
        }

        private byte[] getBytes(String file) throws Exception {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileInputStream fis = new FileInputStream(file);

            byte[] b = new byte[1024];

            int buff = fis.read(b);
            while (buff > 0) {
                baos.write(b, 0, buff);
                buff = fis.read(b);
            }
            fis.close();

            return baos.toByteArray();
        }
    }
}
