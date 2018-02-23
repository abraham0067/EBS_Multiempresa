/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fe.model.dao;

import fe.db.MCfdXml;
import fe.model.util.Material;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 *
 * @author HpAbraham
 */
public class MaterialDAO {

    private Material mat;

    public MaterialDAO() {
    }

    public String insertDetalleAdenda(Material mat) {
        String msg = "";
        try {

            XmlDao xmlDAO = new XmlDao();

            MCfdXml cfd = xmlDAO.getXml_XmlP(mat.getNumFactura());
            byte[] xml = cfd.getXml();
            byte[] xmlp = cfd.getXmlP();

            ByteArrayInputStream binD = new ByteArrayInputStream(xml);
            ByteArrayInputStream binP = new ByteArrayInputStream(xmlp);

            SAXBuilder sax = new SAXBuilder();
            Document doc = sax.build(binD);
            Document docP = sax.build(binP);
            binP.close();
            binD.close();

            Namespace ns = doc.getRootElement().getNamespace();
            Namespace nsP = docP.getRootElement().getNamespace();
            Namespace nsd = doc.getRootElement().getNamespace("detallista");
            Namespace nsdP = docP.getRootElement().getNamespace("detallista");

            Element e = docP.getRootElement().getChild("AdditionalData", nsP);

            if (!mat.getNumCajas().equals("")) {
                e.setAttribute("cajas", mat.getNumCajas());
            }
            if (!mat.getMaterial().equals("")) {
                e.setAttribute("material", mat.getMaterial());
            }

            if (!mat.getRecibo().equals("") && !mat.getReciboFecha().equals("")) {
                try {
                    if (doc.getRootElement().getChild("Complemento", ns) != null
                            && doc.getRootElement().getChild("Complemento", ns).getChild("detallista", nsd) != null) {
                        if (doc.getRootElement().getChild("Complemento", ns).getChild("detallista", nsd).getChild("DeliveryNote", nsd) == null) {
                            doc.getRootElement().getChild("Complemento", ns).getChild("detallista", nsd).addContent(new Element("DeliveryNote", nsd));
                        }
                        if (doc.getRootElement().getChild("Complemento", ns).getChild("detallista", nsd).getChild("DeliveryNote", nsd).getChild("referenceIdentification", nsd) == null) {
                            doc.getRootElement().getChild("Complemento", ns).getChild("detallista", nsd).getChild("DeliveryNote", nsd).addContent(new Element("referenceIdentification", nsd));
                        }
                        if (doc.getRootElement().getChild("Complemento", ns).getChild("detallista", nsd).getChild("DeliveryNote", nsd).getChild("ReferenceDate", nsd) == null) {
                            doc.getRootElement().getChild("Complemento", ns).getChild("detallista", nsd).getChild("DeliveryNote", nsd).addContent(new Element("ReferenceDate", nsd));
                        }

                        if (!mat.getRecibo().equals("")) {
                            doc.getRootElement()
                                    .getChild("Complemento", ns)
                                    .getChild("detallista", nsd)
                                    .getChild("DeliveryNote", nsd)
                                    .getChild("referenceIdentification", nsd).setText(mat.getRecibo());
                        }

                        if (!mat.getReciboFecha().equals("")) {
                            doc.getRootElement()
                                    .getChild("Complemento", ns)
                                    .getChild("detallista", nsd)
                                    .getChild("DeliveryNote", nsd)
                                    .getChild("ReferenceDate", nsd).setText(mat.getReciboFecha());
                        }
                    } else {
                        msg = "'�No se pudo actualizar este valor, no se encontro la Addenda!'";
                    }
                } catch (Exception rex) {
                    rex.printStackTrace();
//             out.print("<script>alert('�No se pudo actualizar este valor!');</script>");
                }
            }

            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ByteArrayOutputStream boutP = new ByteArrayOutputStream();
            XMLOutputter xout = new XMLOutputter();
            xout.output(doc, bout);
            xout.output(docP, boutP);

            xml = bout.toByteArray();
            xmlp = boutP.toByteArray();
            bout.close();
            boutP.close();

            //Actualizar Xml y Xml_p en base de datos
            cfd.setXml(xml);
            cfd.setXmlP(xmlp);

            if (xmlDAO.guardarActualizar(cfd)) {
                msg = "'�Cambio exitoso!'";
            }

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return msg;
    }

}
