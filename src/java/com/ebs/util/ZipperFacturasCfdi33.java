package com.ebs.util;

import com.ebs.reports.JasperGenCfdi33;
import fe.db.MCfdPagos;
import fe.db.MCfdXmlPagos;
import fe.db.MPlantilla;
import fe.model.dao.AutoPagosDao;
import fe.model.dao.LoginDAO;
import fe.model.dao.PlantillaDAO;
import fe.xml.CharUnicode;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by eflores on 16/11/2017.
 */
public class ZipperFacturasCfdi33 {
    private LoginDAO DAOLogin = new LoginDAO();
    private PlantillaDAO daoPlantilla;
    private AutoPagosDao autoPagosDao;
    private CharUnicode charUnicode = new CharUnicode();

    public ZipperFacturasCfdi33() {
        this.DAOLogin = new LoginDAO();
        autoPagosDao = new AutoPagosDao();
        daoPlantilla = new PlantillaDAO();
    }

    public byte[] getXmlBytesPago(MCfdPagos cfd) throws Exception {
        byte[] xmlDoc = "".getBytes();
        MCfdXmlPagos xmlsObj = autoPagosDao.findXmlByCfdiId(cfd.getId());
        if (xmlsObj == null)
            return null;
        xmlDoc = xmlsObj.getXml();
        String str = new String(xmlDoc).trim();
        str = charUnicode.getTextEncoded2(str);
        xmlDoc = str.getBytes();
        return xmlDoc;
    }

    public byte[] getPdfBytesPago(MCfdPagos cfd) {
        byte[] xmlDoc = "".getBytes();
        byte[] xml = "".getBytes();
        byte[] resultBytes = null;
        MCfdXmlPagos xmlsObj = autoPagosDao.findXmlByCfdiId(cfd.getId());
        MPlantilla objPlantilla = null;
        if (cfd.getPlantillaId() != null)
            objPlantilla = daoPlantilla.BuscarPlantilla(cfd.getPlantillaId());
        if (xmlsObj != null) {
            xmlDoc = xmlsObj.getXmlp();
            xml = xmlsObj.getXml();
        }
        if (objPlantilla != null && xmlDoc != null && xml != null) {

            try {
                resultBytes = JasperGenCfdi33.genPdf(cfd.getEstadoDocumento(), xml, xmlDoc, objPlantilla);
            } catch (Exception e) {
                resultBytes = null;
                e.printStackTrace();
            }
        }
        return resultBytes;
    }

    public byte[] ZipCfdisPagos(List<MCfdPagos> facturas, OutputStream out) {
        byte[] zipCFD = "".getBytes();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(baos);
            for (int i = 0; i < facturas.size(); i++) {
                if (facturas.get(i) != null && facturas.get(i).getId() > 0) {
                    // TODO: 16/11/2017 OPTIMINZAR LA OBTENCION DEL REGISTRO MXMLPAGO EN LOS METODOS QUE GENERAN LOS BYTES
                    byte[] xml = getXmlBytesPago(facturas.get(i));
                    if (xml != null) {
                        ZipEntry entry = new ZipEntry(facturas.get(i).getNumeroFactura().trim() + ".xml");
                        entry.setSize(xml.length);
                        zos.putNextEntry(entry);
                        zos.write(xml);
                        zos.closeEntry();
                    } else {
                        String notfound = "<XmlNotFound/>";
                        byte[] error = notfound.getBytes();
                        ZipEntry entry = new ZipEntry("NotFound_" + facturas.get(i).getNumeroFactura().trim() + ".xml");
                        entry.setSize(error.length);
                        zos.putNextEntry(entry);
                        zos.write(error);
                        zos.closeEntry();
                    }
                    ///Solo se adjunta el pdf cuando se logra generar el pdf
                    byte[] pdf = getPdfBytesPago(facturas.get(i));
                    if (pdf != null) {
                        ZipEntry entrypdf = new ZipEntry(facturas.get(i)
                                .getNumeroFactura().trim()
                                + ".pdf");
                        entrypdf.setSize(pdf.length);
                        zos.putNextEntry(entrypdf);
                        zos.write(pdf);
                        zos.closeEntry();
                    }
                }
            }
            zos.close();
            zipCFD = null;
            out.write(baos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return zipCFD;

    }
}
