package fe.model.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.ebs.reports.JasperGenCfdi32;
import com.ebs.reports.JasperGenCfdi33;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import fe.model.dao.CfdiXmlDAO;
import fe.model.dao.EmpresaDAO;
import net.sf.jasperreports.engine.JRException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.imageio.ImageIO;

import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import fe.db.MCfd;
import fe.db.MCfdXml;
import fe.db.MCfdXmlRetencion;
import fe.db.MEmpresa;
import fe.db.MOtro;
import fe.db.MPlantilla;
import fe.db.McfdRetencion;
import fe.model.dao.CfdiDAO;
import fe.model.dao.LoginDAO;
import fe.model.dao.PlantillaDAO;
import fe.model.dao.RetencionDAO;
import fe.xml.CharUnicode;

import java.io.OutputStream;
import java.net.URL;
import java.util.Map;

import mx.com.ebs.emision.factura.controladores.FacturaManejador;
import mx.com.ebs.emision.factura.utilierias.Limpiador;
import mx.com.ebs.emision.factura.utilierias.PintarLog;
import mx.com.ebs.emision.factura.utilierias.QRImagen;
import mx.com.ebs.emision.factura.vistas.AuxDomainAction;
import net.sf.jasperreports.engine.JRDataSource;
import org.primefaces.model.DefaultStreamedContent;

public class CrearZIPFacturas {

    private LoginDAO DAOLogin = new LoginDAO();
    private PlantillaDAO daoPlantilla;
    private EmpresaDAO empresaDAO;
    private CfdiDAO daoCFDI;
    private CfdiXmlDAO daoCfdiXml;
    private CharUnicode charUnicode = new CharUnicode();

    public CrearZIPFacturas() {
        daoPlantilla = new PlantillaDAO();
        empresaDAO = new EmpresaDAO();
        daoCFDI = new CfdiDAO();
        daoCfdiXml = new CfdiXmlDAO();
        charUnicode = new CharUnicode();

    }

    public byte[] writeXmlBytes(MCfd cfd) throws Exception {
        byte[] xmlDoc = "".getBytes();
        FacturaManejador facturaManejador = new FacturaManejador();
        List<MCfdXml> archivosFactura = new ArrayList<MCfdXml>();
        MOtro otro = null;

        try {
            otro = facturaManejador.ObtenerMOtroPorCFDIXML(cfd.getId());
            if (otro != null) {
                // System.out.println("Entra en motro");
                FileInputStream fi = null;
                // System.out.println("URL" + otro.getParam1().trim());
                File archivo = new File(otro.getParam1().trim());
                xmlDoc = new byte[(int) archivo.length()];
                try {
                    fi = new FileInputStream(archivo);
                    fi.read(xmlDoc);
                    fi.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                archivosFactura = facturaManejador.consultaStreamFactura(cfd
                        .getId());
                if (archivosFactura != null && archivosFactura.size() > 0) {
                    xmlDoc = ((MCfdXml) archivosFactura.get(0)).getXml();
                    // AQUI AGREGAMOS EL VALOR DEL CAMPO MCfdXml.XML
                }
            }

            // ========================================================================================================================================================
            String str = new String(xmlDoc).trim();
            str = new fe.xml.CharUnicode().getTextEncoded2(str);
            // xmlDoc = null;
            xmlDoc = str.getBytes();
            // ========================================================================================================================================================
        } catch (Exception e) {
            PintarLog
                    .println(" ***======= Error al llamar al metodo writeXmlBytes: "
                            + e.getMessage());
            // e.printStackTrace();
            throw e;
        }
        return xmlDoc;

    }

    public byte[] writePdfBytes(MCfd cfd) {
        byte[] xmlDoc = null;
        byte[] xmlSat = null;

        byte[] bytes = "".getBytes();///Pdf
        MPlantilla objPlantilla = null;
        MOtro otro = daoCFDI.Otro(cfd.getId());
        MCfdXml cfdiXml = daoCfdiXml.getCfdiXmlByCfdiId(cfd.getId());

        java.net.URL path2 = AuxDomainAction.class.getProtectionDomain().getCodeSource().getLocation();
        String rutaAbsoluta = DAOLogin.ObtenerRuta(path2.getPath());
        rutaAbsoluta = DAOLogin.obtenerRutaDeDirectorioAnterior(rutaAbsoluta, "WEB-INF");
        if (cfd != null && cfd.getIdPlantilla() != null) {
            objPlantilla = daoPlantilla.BuscarPlantilla(cfd.getIdPlantilla());
        }

        try {
            if (otro != null) {
                if (otro.getParam2() != null && !otro.getParam2().trim().equals("")) {//Carga desde disco
                    FileInputStream fi = null;
                    File archivo = new File(otro.getParam2().trim());
                    xmlDoc = new byte[(int) archivo.length()];
                    try {
                        fi = new FileInputStream(archivo);
                        fi.read(xmlDoc);
                        fi.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ///CARGA DESDE DB
                    if (null != cfdiXml && cfdiXml.getXmlP() != null) {
                        xmlDoc = cfdiXml.getXmlP();
                        xmlSat = cfdiXml.getXml();
                    } else {
                    }
                }


            } else {
                ///CARGA DESDE DB
                if (null != cfdiXml && cfdiXml.getXmlP() != null) {
                    xmlDoc = cfdiXml.getXmlP();
                    xmlSat = cfdiXml.getXml();
                } else {

                }
            }

            if (otro != null) {
                if (otro.getParam20() != null &&
                        (otro.getParam20().equalsIgnoreCase("33") ||
                                otro.getParam20().equalsIgnoreCase("3.3"))) {
                    try {
                        bytes = JasperGenCfdi33.genPdf(cfd.getEdoDocumento(), xmlSat, xmlDoc, objPlantilla);
                    } catch (JRException e) {
                        e.printStackTrace();
                        bytes = genPdfError("JRExeption:" + e.getMessage());

                    } catch (Exception e) {
                        e.printStackTrace();
                        bytes = genPdfError("GenericException:" + e.getMessage());

                    }
                } else {
                    try {
                        bytes = JasperGenCfdi32.genPdf(cfd, cfdiXml, objPlantilla);
                    } catch (JRException e) {
                        e.printStackTrace();
                        bytes = genPdfError("JRExeption:" + e.getMessage());

                    } catch (Exception e) {
                        e.printStackTrace();
                        bytes = genPdfError("GenericException:" + e.getMessage());

                    }
                }

            }


        } catch (Exception e) {
            PintarLog.println(" ***======= Error al llamar al metodo writePdfBytes: " + e.getMessage());
            bytes = genPdfError("GenericException:" + e.getMessage());
        }
        return bytes;
    }

    public byte[] genPdfError(String message) {
        byte[] dummiePdfBytes = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            com.lowagie.text.Document document = new com.lowagie.text.Document();
            PdfWriter.getInstance(document, out);
            document.open();
            document.add(new Paragraph("Pdf can´t be generated!!!!!"));
            document.add(new Paragraph("Error:" + message));
            document.close();


        } catch (Exception e) {
            e.printStackTrace();
            dummiePdfBytes = "".getBytes();
        }
        return dummiePdfBytes;
    }


    private InputStream setParameters(byte[] zip,
                                      HashMap<String, Object> parameters) throws Exception {
        return new JasperZip().setUnZipBytes(zip, parameters);
    }

    private class JasperZip {

        @SuppressWarnings("unused")
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

    public static byte[] zipBytes(String filename, byte[] input) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ZipOutputStream zos = new ZipOutputStream(baos);
            ZipEntry entry = new ZipEntry(filename);
            entry.setSize(input.length);
            zos.putNextEntry(entry);
            zos.write(input);
            zos.closeEntry();
            zos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    public byte[] ZipCfdis(List<Integer> cfdiIds, OutputStream out) {
        byte[] zipCFD = "".getBytes();
        try {
            if (cfdiIds != null && !cfdiIds.isEmpty()) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ZipOutputStream zos = new ZipOutputStream(baos);
                for (int i = 0; i < cfdiIds.size(); i++) {
                    MCfd tmp = daoCFDI.BuscarId(cfdiIds.get(i));
                    if (tmp != null) {

                        byte[] xml = writeXmlBytes(tmp);
                        byte[] pdf = writePdfBytes(tmp);
                        if (xml != null && pdf != null) {
                            ZipEntry entry = new ZipEntry(tmp
                                    .getNumeroFactura().trim()
                                    + ".xml");
                            entry.setSize(xml.length);
                            zos.putNextEntry(entry);
                            zos.write(xml);
                            zos.closeEntry();

                            ZipEntry entrypdf = new ZipEntry(tmp
                                    .getNumeroFactura().trim()
                                    + ".pdf");
                            entrypdf.setSize(pdf.length);
                            zos.putNextEntry(entrypdf);
                            zos.write(pdf);
                            zos.closeEntry();
                        } else {
                            System.out
                                    .println("El xml o pdf de la factura "
                                            + tmp
                                            .getNumeroFactura().trim());
                        }
                    }
                    tmp = null;
                }
                zos.close();
                zipCFD = null;
//				zipCFD = baos.toByteArray();
                out.write(baos.toByteArray());
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return zipCFD;

    }

    public void ReporteCVS(List<MCfd> facturas, OutputStream out) {
//		byte[] reporte = "".getBytes();
        try {
            HSSFWorkbook libro = new HSSFWorkbook();
//			String tDir = System.getProperty("java.io.tmpdir");
//			Date fecha = new Date();
//			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyHHmmss");
//			String rutaArchivo = tDir + "/Reporte" + sdf.format(fecha) + ".xls";
//			FileOutputStream archivo = new FileOutputStream(rutaArchivo);
            CfdiDAO cfdidao = new CfdiDAO();
            Sheet hoja = libro.createSheet("Facturas");
            Row cabecera = hoja.createRow(0);
            cabecera.createCell(0).setCellValue("NUMERO FACTURA");
            cabecera.createCell(1).setCellValue("FOLIO ERP");
            cabecera.createCell(2).setCellValue("NO CLIENTE");
            cabecera.createCell(3).setCellValue("RAZON SOCIAL");
            cabecera.createCell(4).setCellValue("R.F.C.");
            cabecera.createCell(5).setCellValue("FECHA");
            cabecera.createCell(6).setCellValue("SUBTOTAL");
            cabecera.createCell(7).setCellValue("IVA");
            cabecera.createCell(8).setCellValue("TOTAL");
            cabecera.createCell(9).setCellValue("MONEDA");
            cabecera.createCell(10).setCellValue("TIPO CAMBIO");
            cabecera.createCell(11).setCellValue("ESTATUS");
            cabecera.createCell(12).setCellValue("UUID");

            for (int x = 0; x < facturas.size(); x++) {
                Row fila = hoja.createRow((short) x + 1);
                Cell celda = fila.createCell((short) 0);
                celda.setCellValue(facturas.get(x).getNumeroFactura());
                Cell celda1 = fila.createCell((short) 1);
                celda1.setCellValue(facturas.get(x).getFolioErp());
                Cell celda2 = fila.createCell((short) 2);
                MOtro otro = cfdidao.Otro(facturas.get(x).getId());
                if (otro != null) {
                    celda2.setCellValue(otro.getParam5() != null ? otro
                            .getParam5() : "");
                } else {
                    celda2.setCellValue("");
                }
                otro = null;
                Cell celda3 = fila.createCell((short) 3);
                celda3.setCellValue(facturas.get(x).getRazonSocial());
                Cell celda4 = fila.createCell((short) 4);
                celda4.setCellValue(facturas.get(x).getRfc());
                Cell celda5 = fila.createCell((short) 5);
                celda5.setCellValue(facturas.get(x).FECHA());
                Cell celda6 = fila.createCell((short) 6);
                celda6.setCellValue(facturas.get(x).getSubTotalML());
                Cell celda7 = fila.createCell((short) 7);
                celda7.setCellValue(facturas.get(x).getIva());
                Cell celda8 = fila.createCell((short) 8);
                celda8.setCellValue(facturas.get(x).getTotal());
                Cell celda9 = fila.createCell((short) 9);
                celda9.setCellValue(facturas.get(x).getMoneda());
                Cell celda10 = fila.createCell((short) 10);
                celda10.setCellValue(facturas.get(x).getTipoCambio());
                Cell celda11 = fila.createCell((short) 11);
                celda11.setCellValue(facturas.get(x).ESTATUS());
                Cell celda12 = fila.createCell((short) 12);
                celda12.setCellValue(facturas.get(x).getUuid());
            }
            libro.write(out);
//			archivo.close();

//			FileInputStream fis = new FileInputStream(new File(rutaArchivo));
//			ByteArrayOutputStream bos = new ByteArrayOutputStream();
//			byte[] buf = new byte[1024];
//			try {
//				for (int readNum; (readNum = fis.read(buf)) != -1;) {
//					bos.write(buf, 0, readNum); // no doubt here is 0
//				}
//			} catch (Exception ex) {
//				ex.printStackTrace();
//			}
//			reporte = bos.toByteArray();
//			bos.close();
//			fis.close();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

//		return reporte;
    }

    public void ReporteCVSRetenciones(final List<McfdRetencion> facturas, OutputStream out) {
        HSSFWorkbook libro = null;
        try {
            libro = new HSSFWorkbook();
            final Sheet hoja = (Sheet) libro.createSheet("Facturas");
            final Row cabecera = hoja.createRow(0);
            cabecera.createCell(0).setCellValue("FOLIO ERP");
            cabecera.createCell(1).setCellValue("RAZON SOCIAL");
            cabecera.createCell(2).setCellValue("R.F.C.");
            cabecera.createCell(3).setCellValue("FECHA");
            cabecera.createCell(4).setCellValue("NUMERO FACTURA");
            cabecera.createCell(5).setCellValue("IVA");
            cabecera.createCell(6).setCellValue("TOTAL");
            cabecera.createCell(7).setCellValue("MONEDA");
            cabecera.createCell(8).setCellValue("TIPO DE DOCUMENTO");
            cabecera.createCell(9).setCellValue("ESTATUS");
            for (int x = 0; x < facturas.size(); ++x) {
                final Row fila = hoja.createRow((short) x + 1);
                final Cell celda = fila.createCell(0);
                celda.setCellValue(facturas.get(x).getFolioErp());
                final Cell celda2 = fila.createCell(1);
                celda2.setCellValue(facturas.get(x).getRazonSocial());
                final Cell celda3 = fila.createCell(2);
                celda3.setCellValue(facturas.get(x).getRfc());
                final Cell celda4 = fila.createCell(3);
                celda4.setCellValue(facturas.get(x).FECHA());
                final Cell celda5 = fila.createCell(4);
                celda5.setCellValue(facturas.get(x).getNumeroFactura());
                final Cell celda6 = fila.createCell(5);
                celda6.setCellValue(facturas.get(x).getIva());
                final Cell celda7 = fila.createCell(6);
                celda7.setCellValue(facturas.get(x).getTotal());
                final Cell celda8 = fila.createCell(7);
                celda8.setCellValue(facturas.get(x).getMoneda());
                final Cell celda9 = fila.createCell(8);
                celda9.setCellValue(facturas.get(x).getTipoDocumento());
                final Cell celda10 = fila.createCell(9);
                celda10.setCellValue(facturas.get(x).ESTATUS());
            }

            libro.write(out);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

    }

    public byte[] ZipRetenciones(final List<McfdRetencion> facturas) {
        byte[] zipCFD = "".getBytes();
        try {
            if (facturas != null && !facturas.isEmpty()) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ZipOutputStream zos = new ZipOutputStream(baos);
                for (int i = 0; i < facturas.size(); ++i) {
                    if (facturas.get(i) != null && facturas.get(i).getId() > 0) {
                        byte[] xml = null;
                        byte[] pdf = null;
                        try {
                            xml = this.writeXmlBytesRetenciones(facturas.get(i));
                        } catch (Exception e) {
                            e.printStackTrace(System.out);
                        }
                        try {
                            pdf = this.writePdfBytesRetenciones(facturas.get(i));
                        } catch (Exception e) {
                            e.printStackTrace(System.out);
                        }
                        if (xml != null && pdf != null) {
                            String name = facturas.get(i).getNumeroFactura() + "_" + facturas.get(i).getFecha();
                            ZipEntry entry = new ZipEntry(name.trim() + ".xml");
                            entry.setSize(xml.length);
                            zos.putNextEntry(entry);
                            zos.write(xml);
                            zos.closeEntry();
                            ZipEntry entrypdf = new ZipEntry(name.trim() + ".pdf");
                            entrypdf.setSize(pdf.length);
                            zos.putNextEntry(entrypdf);
                            zos.write(pdf);
                            zos.closeEntry();
                        } else {
                            System.out.println("El xml o pdf de la factura " + facturas.get(i).getNumeroFactura().trim());
                        }
                    }
                }
                zos.close();
                zipCFD = baos.toByteArray();
                //out.write(baos.toByteArray());
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return zipCFD;
    }

    public byte[] writeXmlBytesRetenciones(McfdRetencion cfd) throws Exception {
        byte[] xmlDoc = "".getBytes();
        final FacturaManejador facturaManejador = new FacturaManejador();
        List<MCfdXmlRetencion> archivosFactura = new ArrayList<MCfdXmlRetencion>();
        MOtro otro = null;
        try {
            otro = facturaManejador.ObtenerMOtroPorCFDIXML((int) cfd.getId());
            if (otro != null) {
                FileInputStream fi = null;
                final File archivo = new File(otro.getParam1().trim());
                xmlDoc = new byte[(int) archivo.length()];
                try {
                    fi = new FileInputStream(archivo);
                    fi.read(xmlDoc);
                    fi.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                archivosFactura = (List<MCfdXmlRetencion>) facturaManejador.consultaStreamFacturaRetencion((int) cfd.getId());
                if (archivosFactura != null && archivosFactura.size() > 0) {
                    xmlDoc = archivosFactura.get(0).getXml();
                }
            }
            String str = new String(xmlDoc).trim();
            str = charUnicode.getTextEncoded2(str);
            xmlDoc = str.getBytes();
        } catch (Exception e2) {
            PintarLog.println(" ***======= Error al llamar al metodo writeXmlBytes: " + e2.getMessage());
            throw e2;
        }
        return xmlDoc;
    }

    public byte[] writePdfBytesRetenciones(McfdRetencion cfd) throws Exception {
        byte[] xmlDoc = "".getBytes();
        byte[] plantilla = "".getBytes();
        byte[] bytes = "".getBytes();
        final FacturaManejador facturaManejador = new FacturaManejador();
        List<MCfdXmlRetencion> listaFacturas = new ArrayList<MCfdXmlRetencion>();
        HashMap hm = new HashMap();
        MOtro otro = null;
        URL path2 = AuxDomainAction.class.getProtectionDomain().getCodeSource().getLocation();
        String rutaAbsoluta = this.DAOLogin.ObtenerRuta(path2.getPath());
        rutaAbsoluta = this.DAOLogin.obtenerRutaDeDirectorioAnterior(rutaAbsoluta, "WEB-INF");
        String rutaPDF = null;
        try {
            otro = facturaManejador.ObtenerMOtroPorCFDIPDF((int) cfd.getId());
            if (otro != null) {
                FileInputStream fi = null;
                rutaPDF = otro.getParam2().trim();
                File archivo = new File(otro.getParam2().trim());
                xmlDoc = new byte[(int) archivo.length()];
                try {
                    fi = new FileInputStream(archivo);
                    fi.read(xmlDoc);
                    fi.close();
                } catch (Exception ex) {
                }
            } else {
                listaFacturas = (List<MCfdXmlRetencion>) facturaManejador.consultaStreamFacturaRetencion((int) cfd.getId());
                if (null != listaFacturas && listaFacturas.size() > 0) {
                    xmlDoc = listaFacturas.get(0).getXmlp();
                }
            }
            if (rutaPDF != null && (rutaPDF.toLowerCase().trim().contains(".pdf"))) {
                bytes = xmlDoc;
            } else {
                Map<Integer, Object> obj = (Map<Integer, Object>) new RetencionDAO().getObjectsMap((int) cfd.getId());
                MEmpresa m_empresa = (MEmpresa) obj.get(1);
                String registro = "";
                if ("Nacional".equals(cfd.getNacionalidad())) {
                    registro = "Nacional-" + cfd.getRfc();
                } else if ("Extrangero".equals(cfd.getNacionalidad())) {
                    registro = "Extrangero-" + cfd.getRfc();
                } else {
                    registro = cfd.getRfc();
                }
                MPlantilla m_plantilla = (MPlantilla) obj.get(2);
                final Image img = new QRImagen().get2DBarCode(Limpiador.cleanString(m_empresa.getRfcOrigen()), Limpiador.cleanString(registro), Limpiador.cleanString("" + cfd.getTotal()), Limpiador.cleanString(cfd.getUuid()));
                hm.put("2DCODEBAR", img);
                String recordPath = "/Comprobante/Conceptos/Concepto";
                hm.put("IMAGES", rutaAbsoluta + "imagenesReportes/");
                hm.put("STATUS", "" + cfd.getEstadoDocumento());
                hm.put("REPORTES", rutaAbsoluta + "/reportes/");
                hm.put("CANCELADO", rutaAbsoluta);
                InputStream in = null;
                if (m_plantilla == null) {
                    throw new Exception("No se encontraron plantillas ligadas a la factura " + cfd.getNumeroFactura());
                }
                if (m_plantilla.getRootPath() != null && !"".equals(m_plantilla.getRootPath())) {
                    recordPath = m_plantilla.getRootPath();
                }
                plantilla = m_plantilla.getPlantilla();
                in = this.setParameters(plantilla, hm);
                if (xmlDoc != null && hm != null && in != null) {
                    JRXmlDataSource jrxmlds = new JRXmlDataSource((InputStream) new ByteArrayInputStream(xmlDoc), recordPath);
                    jrxmlds.setLocale(new Locale("sp", "MX"));
                    jrxmlds.setNumberPattern("#,##0.00");
                    bytes = JasperRunManager.runReportToPdf(in, (Map) hm, (JRDataSource) jrxmlds);
                }
                obj.clear();
                m_empresa = null;
                m_plantilla = null;
            }
            if (bytes == null) {
                bytes = "".getBytes();
            }
        } catch (Exception e) {
            PintarLog.println(" ***======= Error al llamar al metodo writePdfBytes: " + e.getMessage());
            throw e;
        }
        return bytes;
    }
}
