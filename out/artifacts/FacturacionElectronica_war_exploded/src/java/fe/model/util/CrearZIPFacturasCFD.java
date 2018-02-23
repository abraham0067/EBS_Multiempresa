package fe.model.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


import javax.imageio.ImageIO;

import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import fe.db.MCfd;
import fe.db.MInvoice;
import fe.db.MOtro;
import fe.model.dao.CfdiDAO;
import fe.model.dao.LoginDAO;
import mx.com.ebs.emision.factura.utilierias.PintarLog;
import mx.com.ebs.emision.factura.vistas.AuxDomainAction;

public class CrearZIPFacturasCFD {
	private LoginDAO DAOLogin = new LoginDAO();


	public byte[] writeXmlBytes(MInvoice cfd) throws Exception {
		byte[] xmlDoc = "".getBytes();
		try {
			if (cfd.getSerie() != null) {

				String Fol = "" + cfd.getFolio();
				Fol = Fol.replace(".0", "");

				String nomFactura = cfd.getSerie().trim() + "." + Fol;
//				System.out.println("Nombre Factura:  " + nomFactura);


				// ========================================================================================================================================================

				String str = new String(xmlDoc).trim();
				str = new fe.xml.CharUnicode().getTextEncoded2(str);
				// xmlDoc = null;
				xmlDoc = str.getBytes();
			}
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public byte[] writePdfBytes(MInvoice cfd) throws Exception {
		byte[] xmlDoc = "".getBytes();
		byte[] plantilla = "".getBytes();
		byte[] bytes = "".getBytes();
		// FacturaManejador facturaManejador = new FacturaManejador();
		// List<MCfdXml> listaFacturas = new ArrayList();
		HashMap hm = new HashMap();
		// MOtro otro = null;
		java.net.URL path2 = AuxDomainAction.class
				.getProtectionDomain().getCodeSource().getLocation();
		String rutaAbsoluta = DAOLogin.ObtenerRuta(path2.getPath());
		rutaAbsoluta = DAOLogin.obtenerRutaDeDirectorioAnterior(rutaAbsoluta,
				"WEB-INF");// Ruta
		// String rutaPDF = null;
		// del WebContent
		// PintarLog.println("ruta Absoluta: "+path2);

		try {
			if (cfd.getSerie() != null) {
				String Fol = "" + cfd.getFolio();
				Fol = Fol.replace(".0", "");
				String nomFactura = cfd.getSerie().trim() + "." + Fol;
//				System.out.println("Nombre Factura:  " + nomFactura);
				
				if(xmlDoc==null){
//					System.out.println("No se pudo descargar el documento");
					xmlDoc=(new String("No se pudo obtener la factura debido a un error, idFactura nulo."))
							.getBytes();
					
				}
				String str = new String(xmlDoc).trim();
				str = new fe.xml.CharUnicode().getTextEncoded2(str);
				xmlDoc = str.getBytes();
				
				// ===========================================================
				// CONSTRUCCION DE ARCHIVO ADJUNTO
				// ===========================================================

				// String reportFileName = rutaAbsoluta +
				// "/reportes/Interacciones.jasper";
				// PintarLog.println("Ruta del reporte "+reportFileName);
				// Namespace tfd =
				// Namespace.getNamespace("tfd","http://www.sat.gob.mx/TimbreFiscalDigital");
				

				String recordPath = "/Comprobante/Conceptos/Concepto";
				// String recordPath =
				// "/Invoice/E2EDP01[not(MENGE=0)][not(E2EDP19/KTEXT='Liquidación de anticipo')][not(E2EDP19/KTEXT='Down payment settlement')]";

				// System.out.print("RutaAbsoluta: " + rutaAbsoluta);
				hm.put("IMAGES", rutaAbsoluta + "imagenesReportes/");
				hm.put("STATUS", "" + cfd.getEstatusDoc());
				hm.put("REPORTES", rutaAbsoluta + "/reportes/");
				hm.put("CANCELADO", rutaAbsoluta);
				hm.put("MONEDA", cfd.getMoneda());

				// ======================= CORRE EL REPORTE PDF
				// ================================================
				InputStream in = null;
				if (cfd.getPlantilla() == null) {
					throw new Exception(
							"No se encontraron plantillas ligadas a la factura "
									+ cfd.getNumeroFactura());
				} else {
					if (cfd.getPlantilla().getRootPath() != null
							&& !"".equals(cfd.getPlantilla().getRootPath()))
						recordPath = cfd.getPlantilla().getRootPath();
					plantilla = cfd.getPlantilla().getPlantilla();
					in = setParameters(plantilla, hm);
					// System.out.println("in"+ (in!=null)+ " hm: "+
					// (hm!=null));
				}

				if (xmlDoc != null && hm != null && in != null) {
					JRXmlDataSource jrxmlds = new JRXmlDataSource(
							new java.io.ByteArrayInputStream(xmlDoc),
							recordPath);
					jrxmlds.setLocale(new Locale("sp", "MX"));
					jrxmlds.setNumberPattern("#,##0.00");
					// bytes = null;
					bytes = JasperRunManager.runReportToPdf(in, hm, jrxmlds);
				}

			}
			// =============================================================================================
			if (bytes == null) {
				bytes = "".getBytes();
			}

			// response.getOutputStream().close();
			// ===========================================================================================================================================================
		} catch (Exception e) {
			PintarLog
					.println(" ***======= Error al llamar al metodo writePdfBytes: "
							+ e.getMessage());
			// e.printStackTrace();
			throw e;
		}
		return bytes;
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
					if (item == 0)
						stream = new ByteArrayInputStream(bout.toByteArray());
					else
						parameters.put(name,
								new ByteArrayInputStream(bout.toByteArray()));

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

	public byte[] ZipCfdis(List<MInvoice> facturas) {
		byte[] zipCFD = "".getBytes();
		try {
			if (facturas != null && !facturas.isEmpty()) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ZipOutputStream zos = new ZipOutputStream(baos);
				for (int i = 0; i < facturas.size(); i++) {
					if (facturas.get(i) != null && facturas.get(i).getId() > 0) {
						byte[] xml = writeXmlBytes(facturas.get(i));
						byte[] pdf = writePdfBytes(facturas.get(i));
						if (xml != null && pdf != null) {
							ZipEntry entry = new ZipEntry(facturas.get(i)
									.getNumeroFactura().trim()
									+ ".xml");
							entry.setSize(xml.length);
							zos.putNextEntry(entry);
							zos.write(xml);
							zos.closeEntry();

							ZipEntry entrypdf = new ZipEntry(facturas.get(i)
									.getNumeroFactura().trim()
									+ ".pdf");
							entrypdf.setSize(pdf.length);
							zos.putNextEntry(entrypdf);
							zos.write(pdf);
							zos.closeEntry();
						} else {
							System.out
									.println("El xml o pdf de la factura "
											+ facturas.get(i)
													.getNumeroFactura().trim());
						}
					}
				}
				zos.close();
				zipCFD = null;
				zipCFD = baos.toByteArray();
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return zipCFD;

	}

	public byte[] ReporteCVS(List<MCfd> facturas) {
		byte[] reporte = "".getBytes();
		try {
			HSSFWorkbook libro = new HSSFWorkbook();
			String tDir = System.getProperty("java.io.tmpdir");
			Date fecha = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyHHmmss");
			String rutaArchivo = tDir + "Reporte" + sdf.format(fecha) + ".xls";
			FileOutputStream archivo = new FileOutputStream(rutaArchivo);
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
			}
			libro.write(archivo);
			archivo.close();

			FileInputStream fis = new FileInputStream(new File(rutaArchivo));
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			try {
				for (int readNum; (readNum = fis.read(buf)) != -1;) {
					bos.write(buf, 0, readNum); // no doubt here is 0
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			reporte = bos.toByteArray();
			bos.close();
			fis.close();

		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

		return reporte;

	}
}
