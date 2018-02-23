package com.ebs.reports;

import fe.db.MCfd;
import fe.db.MCfdXml;
import fe.db.MPlantilla;
import fe.model.dao.EmpresaDAO;
import fe.model.dao.LoginDAO;
import mx.com.ebs.emision.factura.utilierias.Limpiador;
import mx.com.ebs.emision.factura.utilierias.QRImagen;
import mx.com.ebs.emision.factura.vistas.AuxDomainAction;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by eflores on 21/08/2017.
 */
public class JasperGenCfdi32 {

    private static EmpresaDAO empresaDAO;

    static {
        empresaDAO = new EmpresaDAO();
    }

    public static byte[] genPdf(MCfd cfdi, MCfdXml xmlCfdi, MPlantilla plantilla) throws JRException {

        System.out.println("GENERANDO PDF CFDIV_32");
        byte[] bytesPdf = new byte[0];
        LoginDAO loginDao = new LoginDAO();
        HashMap hm = new HashMap();
        String recordPath = "/Comprobante/Conceptos/Concepto";
        try {
            Image img = new QRImagen().get2DBarCode(Limpiador
                            .cleanString(empresaDAO.getRfcEmpresaById(cfdi.getIdEmpresa())),
                    Limpiador.cleanString(cfdi.getRfc()), Limpiador
                            .cleanString("" + cfdi.getTotal()),
                    Limpiador.cleanString(cfdi.getUuid()));
            hm.put("2DCODEBAR", img);
        } catch (java.lang.NoClassDefFoundError e) {
            e.printStackTrace(System.out);
        } catch (java.lang.Exception e) {
            e.printStackTrace(System.out);
        }
        java.net.URL path2 = AuxDomainAction.class.getProtectionDomain().getCodeSource().getLocation();
        String rutaAbsoluta = loginDao.ObtenerRuta(path2.getPath());
        rutaAbsoluta = loginDao.obtenerRutaDeDirectorioAnterior(rutaAbsoluta, "WEB-INF");// Rutad
        System.out.print("RutaAbsoluta: " + rutaAbsoluta);
        hm.put("IMAGES", rutaAbsoluta + "imagenesReportes/");
        hm.put("ESTATUS", "" + cfdi.getEdoDocumento());
        hm.put("REPORTES", rutaAbsoluta + "/reportes/");
        hm.put("CANCELADO", rutaAbsoluta);

        InputStream in = null;
        if (plantilla.getRootPath() != null
                && !"".equals(plantilla
                .getRootPath())) {
            recordPath = plantilla.getRootPath();
        }
        try {
            in = setParameters(plantilla.getPlantilla(), hm);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        JRFileVirtualizer virt = new JRFileVirtualizer(300, System.getProperty("java.io.tmpdir"));
        virt.setReadOnly(false);
        hm.put(JRParameter.REPORT_VIRTUALIZER, virt);
        JRXmlDataSource jrxmlds = new JRXmlDataSource(new java.io.ByteArrayInputStream(xmlCfdi.getXmlP()), recordPath);
        jrxmlds.setLocale(new Locale("sp", "MX"));
        jrxmlds.setNumberPattern("#,##0.00");
        bytesPdf = JasperRunManager.runReportToPdf(in, hm, jrxmlds);
        return bytesPdf;
    }

    public static InputStream setParameters(byte[] zip, HashMap<String, Object> parameters) throws Exception {
        return JasperZip.setUnZipBytes(zip, parameters);
    }


}
