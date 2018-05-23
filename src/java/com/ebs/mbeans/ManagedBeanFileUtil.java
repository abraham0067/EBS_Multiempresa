/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.mbeans;

import com.ebs.clienteFEWS.ClienteFEWS;
import com.ebs.reports.JasperGenCfdi32;
import com.ebs.reports.JasperGenCfdi33;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import eu.medsea.mimeutil.MimeUtil;
import fe.db.*;
import fe.model.dao.*;
import fe.xml.CharUnicode;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.faces.bean.ViewScoped;
import javax.imageio.ImageIO;

import mx.com.ebs.emision.factura.controladores.FacturaManejador;
import mx.com.ebs.emision.factura.utilierias.Limpiador;
import mx.com.ebs.emision.factura.utilierias.ManejadorFechas;
import mx.com.ebs.emision.factura.utilierias.PintarLog;
import mx.com.ebs.emision.factura.utilierias.QRImagen;
import mx.com.ebs.emision.factura.vistas.AuxDomainAction;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;
import org.primefaces.model.DefaultStreamedContent;

/**
 * @author Eduardo C. Flores Ambrosio
 */
@ManagedBean(name = "mbFileUtil")
@ViewScoped
public class ManagedBeanFileUtil implements Serializable {

    private LoginDAO DAO = new LoginDAO();
    private DefaultStreamedContent scFile;
    private SimpleDateFormat sdf;
    private CfdiDAO daoCFDI;
    private ProformaDao daoProforma;
    private CfdiXmlDAO daoCfdiXml;
    private AutoPagosDao daoPagos;
    private PlantillaDAO daoPlantilla;
    private MArchivosCfd archivoSelectCfd;
    private CharUnicode charUnicode;
    //private ClienteFEWS clienteFEWS;


    /**
     * Creates a new instance of ManagedBeanFileUtil
     */
    public ManagedBeanFileUtil() {

    }

    /**
     * Post constructor
     */
    @PostConstruct
    public void init() {
        /*
        The PostConstruct annotation is used on a method that needs to be executed after dependency injection
        is done to perform any initialization. This method MUST be invoked before the class is put into service.
        This annotation MUST be supported on all classes that support dependency injection. The method annotated 
        with PostConstruct MUST be invoked even if the class does not request any resources to be injected. 
        Only one method can be annotated with this annotation.
         */
        daoCFDI = new CfdiDAO();
        daoPlantilla = new PlantillaDAO();
        daoCfdiXml = new CfdiXmlDAO();
        daoProforma = new ProformaDao();
        daoPagos = new AutoPagosDao();
        charUnicode = new CharUnicode();
        //clienteFEWS = new ClienteFEWS();

    }

    /**
     * Descarga el archivo xml asociado a la factura(CFDI)
     *
     * @param idCfd
     * @param nombreArchivo
     * @return
     */
    public void downloadXmlCfdiFile(Integer idCfd, String nombreArchivo) {
        scFile = null;
        byte[] xmlDoc = new byte[0];
        ClienteFEWS clienteFEWS = new ClienteFEWS();
        try {
            xmlDoc = clienteFEWS.clienteFEWS("XML", idCfd);
            String name = nombreArchivo + "_" + ManejadorFechas.obtenIdEvent() + ".xml";
            scFile = new DefaultStreamedContent(new ByteArrayInputStream(xmlDoc), "application/xml", name);
        } catch (Exception e) {
            PintarLog
                    .println(" ***======= Error al llamar al metodo writeXmlBytes: " + e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Xml Exception." + e.getMessage(), "Detail"));
            e.printStackTrace();
        }finally{
            clienteFEWS = null;
        }

       /* FacturaManejador facturaManejador = new FacturaManejador();
        //String name = "xml" + ".xml";
        setScFile(null);//Clear data
        if (xmlDoc != null) {
            //String nombreFactura = request.getParameter("numeroFactura");
            if (nombreArchivo == null) {
                nombreArchivo = "";
            }
            // ===========================================================
            // VALIDACION DE INFORMACION
            // =================================================================
            if (idCfd <= 0) {
                xmlDoc = (new String("No se pudo obtener la factura debido a un error, idFactura nulo.")).getBytes();
                nombreArchivo = "error";
            } else {
                MOtro otro = facturaManejador.ObtenerMOtroPorCFDIXML(idCfd);
                if (otro != null) {
                    FileInputStream fi = null;
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
                    try {

                        List<MCfdXml> archivosFactura = facturaManejador
                                .consultaStreamFactura(idCfd);
                        if (archivosFactura != null && archivosFactura.size() > 0) {
                            xmlDoc = ((MCfdXml) archivosFactura.get(0)).getXml();
                        } // AQUI AGREGAMOS EL VALOR DEL CAMPO MCfdXml.XML
                        else if (idCfd > 0 && archivosFactura.size() == 0) {
                            xmlDoc = (new String(
                                    "No se encontraron registros de la factura seleccionada.")).getBytes();
                        }
                    }catch (Exception e){

                    }
                }
            }

            String str = new String(xmlDoc).trim();
            byte[] bytes = str.getBytes();
            if (bytes == null) {
                bytes = "".getBytes();
            }

            String name = nombreArchivo + "_" + ManejadorFechas.obtenIdEvent() + ".xml";
            scFile = new DefaultStreamedContent(new ByteArrayInputStream(xmlDoc), "application/xml", name);
        } else {
            PintarLog
                    .println(" ***======= Error al llamar al metodo writeXmlBytes: ");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Xml Exception.", "Detail"));
        }*/
    }



    /**
     * Descarga de xml proforma
     *
     * @param idCfd
     * @param nombreArchivo
     */
    public void downloadXmlProformaFile(Integer idCfd, String nombreArchivo) {
        MCfdProforma doc = daoProforma.BuscarId(idCfd);
        scFile = null;
        byte[] xmlDoc = "".getBytes();
        MCfdXmlProforma xmlCfdi = null;
        String name = "xml" + ".xml";
        try {
            //String nombreFactura = request.getParameter("numeroFactura");
            if (nombreArchivo == null) {
                nombreArchivo = "";
            }
            // ===========================================================
            // VALIDACION DE INFORMACION
            // =================================================================
            if (idCfd <= 0) {
                xmlDoc = (new String("No se pudo obtener la factura debido a un error, idFactura nulo.")).getBytes();
                nombreArchivo = "error";
            } else {
                xmlCfdi = daoProforma.findXmlByCfdiId(idCfd);
                if (xmlCfdi != null && xmlCfdi.getXml() != null) {
                    xmlDoc = xmlCfdi.getXml();
                }
            }
            String str = new String(xmlDoc).trim();
            str = charUnicode.getTextEncoded2(str);
            byte[] bytes = str.getBytes();
            if (bytes == null) {
                bytes = "".getBytes();
            }
            name = nombreArchivo + "_" + ManejadorFechas.obtenIdEvent() + ".xml";
            scFile = new DefaultStreamedContent(new ByteArrayInputStream(bytes), "application/xml", name);
        } catch (Exception e) {
            PintarLog.println(" ***======= Error al llamar al metodo writeXmlProformaBytes: " + e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_INFO, "Xml Exception." + e.getMessage(), "Detail"));
        }
    }

    /**
     * Descarga de xml PAGOS
     *
     * @param idCfd
     * @param nombreArchivo
     */
    public void downloadXmlPagoFile(Integer idCfd, String nombreArchivo) {
        //MCfdPagos doc = daoPagos.BuscarId(idCfd);
        scFile = null;
        byte[] xmlDoc = "".getBytes();
        //MCfdXmlPagos xmlCfdi = null;
        String name = "xml" + ".xml";
        ClienteFEWS clienteFEWS = new ClienteFEWS();
        try {
         //   String nombreFactura = request.getParameter("numeroFactura");
           if (nombreArchivo == null) {
                nombreArchivo = "";
            }
            // ===========================================================
            // VALIDACION DE INFORMACION
            // =================================================================
            if (idCfd <= 0) {
                xmlDoc = (new String("No se pudo obtener la factura debido a un error, idFactura nulo.")).getBytes();
                nombreArchivo = "error";
            } else {
                xmlDoc = clienteFEWS.clienteFEWS("XML_PAGO", idCfd);
                /*xmlCfdi = daoPagos.findXmlByCfdiId(idCfd);
                if (xmlCfdi != null && xmlCfdi.getXml() != null) {
                    xmlDoc = xmlCfdi.getXml();
                }*/
            }
            String str = new String(xmlDoc).trim();
            byte[] bytes = str.getBytes();
            if (bytes == null) {
                bytes = "".getBytes();
            }

            if(xmlDoc != null){
                name = nombreArchivo + "_" + ManejadorFechas.obtenIdEvent() + ".xml";
                scFile = new DefaultStreamedContent(new ByteArrayInputStream(bytes), "application/xml", name);
            }else{
                PintarLog
                        .println(" ***======= Error al llamar al metodo downloadXmlPagoFile: ");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Xml Exception.", "downloadXmlPagoFile"));
            }

        } catch (Exception e) {
            PintarLog.println(" ***======= Error al llamar al metodo writeXMlPagos: " + e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_INFO, "Xml Exception." + e.getMessage(), "Detail"));
        }finally{
           clienteFEWS = null;
        }
    }


    /**
     * Descarga la factura en formato pdf (CFDI)
     *
     * @param idCfd
     * @param nombreArchivo
     */
    public void downloadPdfCfdiFile(int idCfd, String nombreArchivo) {

        byte[] pdfBytes = null;
        scFile = null;
        //String rutaPDF = null;
        ClienteFEWS clienteFEWS = new ClienteFEWS();
        try {
            pdfBytes = clienteFEWS.clienteFEWS("PDF", idCfd);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }finally{
            clienteFEWS = null;
        }

        if (pdfBytes != null) {
            String name = nombreArchivo + "_" + ManejadorFechas.obtenIdEvent() + ".pdf";
            scFile = new DefaultStreamedContent(new ByteArrayInputStream(pdfBytes), "application/pdf", name);
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Lo sentimos, al parecer el registro no tiene asociado una plantilla.", "Detail"));
        }


      /*  MOtro otro = daoCFDI.Otro(idCfd);
        MCfd cfdi = daoCFDI.BuscarId(idCfd);
        MPlantilla plantilla = null;
        if (cfdi != null && cfdi.getIdPlantilla() != null) {
            plantilla = daoPlantilla.BuscarPlantilla(cfdi.getIdPlantilla());
        }
        MCfdXml cfdiXml = daoCfdiXml.getCfdiXmlByCfdiId(idCfd);
        scFile = null;
        String rutaPDF = null;
        String name = "pdf.pdf";
        byte[] xmlDoc = null;
        byte[] xmlXml = null;
        //byte[] pdfBytes = null;
        if (plantilla != null && plantilla.getPlantilla() != null) {
            if (cfdiXml != null && cfdiXml.getXmlP() != null) {
                ///RUN REPORTER
                name = nombreArchivo + "_" + ManejadorFechas.obtenIdEvent() + ".pdf";
                if (otro != null) {
                    ///CARGA EL PDF DESDE DISCO
                    if (otro.getParam2() != null && !otro.getParam2().trim().equals("")) {
                        FileInputStream fi = null;
                        rutaPDF = otro.getParam2().trim();
                        File archivo = new File(otro.getParam2().trim());
                        if (archivo.exists()) {
                            xmlDoc = new byte[(int) archivo.length()];
                            try {
                                fi = new FileInputStream(archivo);
                                fi.read(xmlDoc);
                                fi.close();
                            } catch (Exception e) {
                                e.printStackTrace(System.out);
                            }
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Lo sentimos, no pudimos encontrar el PDF.", "Detail"));
                        }
                    } else {
                        ///CARGA DESDE DB
                        if (null != cfdiXml && cfdiXml.getXmlP() != null) {
                            xmlDoc = cfdiXml.getXmlP();
                            xmlXml = cfdiXml.getXml();
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Lo sentimos, no pudimos encontrar el Xml.", "Detail"));
                        }
                    }
                } else {
                    ///CARGA DESDE DB
                    if (null != cfdiXml && cfdiXml.getXmlP() != null) {
                        xmlDoc = cfdiXml.getXmlP();
                        xmlXml = cfdiXml.getXml();
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Lo sentimos, no pudimos encontrar el Xml.", "Detail"));
                    }
                }

                if (otro != null) {
                    if (otro.getParam20() != null &&
                            (otro.getParam20().equalsIgnoreCase("33") ||
                                    otro.getParam20().equalsIgnoreCase("3.3"))) {
                        try {
                            pdfBytes = JasperGenCfdi33.genPdf(cfdi.getEdoDocumento(), xmlXml, xmlDoc, plantilla);
                            scFile = new DefaultStreamedContent(new ByteArrayInputStream(pdfBytes), "application/pdf", name);
                        } catch (JRException e) {
                            e.printStackTrace();
                            genPdfError("JRExeption:" + e.getMessage());
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                                    FacesMessage.SEVERITY_ERROR,
                                    "No se pudo generar el pdf.", "Detail"));
                        } catch (Exception e) {
                            e.printStackTrace();
                            genPdfError("Exception:" + e.getMessage());
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                                    FacesMessage.SEVERITY_ERROR,
                                    "No se pudo generar el pdf.", "Detail"));
                        }
                    } else {
                        try {
                            pdfBytes = JasperGenCfdi32.genPdf(cfdi, cfdiXml, plantilla);
                            scFile = new DefaultStreamedContent(new ByteArrayInputStream(pdfBytes), "application/pdf", name);
                        } catch (JRException e) {
                            e.printStackTrace();
                            genPdfError("JRExeption:" + e.getMessage());
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                                    FacesMessage.SEVERITY_ERROR,
                                    "No se pudo generar el pdf.", "Detail"));
                        } catch (Exception e) {
                            e.printStackTrace();
                            genPdfError("Exception:" + e.getMessage());
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                                    FacesMessage.SEVERITY_ERROR,
                                    "No se pudo generar el pdf.", "Detail"));
                        }
                    }

                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Lo sentimos, no pudimos encontrar el Xml.", "Detail"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Lo sentimos, al parecer el registro no tiene asociado una plantilla.", "Detail"));
        }*/
    }

    public void downloadPdfProformaFile(int idCfd, String nombreArchivo) {
        /*MCfdProforma cfdi = daoProforma.BuscarId(idCfd);
        MOtroProforma otro = daoProforma.Otro(idCfd);
        MPlantilla plantilla = null;
        if (cfdi != null && cfdi.getPlantillaId() != null) {
            plantilla = daoPlantilla.BuscarPlantilla(cfdi.getPlantillaId());
        }
        MCfdXmlProforma xmlProf = daoProforma.findXmlByCfdiId(idCfd);*/
        scFile = null;
        String rutaPDF = null;
        String name = "pdf.pdf";
        byte[] xmlDoc = null;
        byte[] xmlXml = null;
        byte[] pdfBytes = null;

        ClienteFEWS clienteFEWS = new ClienteFEWS();
        try {
            pdfBytes = clienteFEWS.clienteFEWS("PDF_PROFORMA", idCfd);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            clienteFEWS = null;
        }
        if (pdfBytes != null) {
            name = nombreArchivo + "_" + ManejadorFechas.obtenIdEvent() + ".pdf";
            scFile = new DefaultStreamedContent(new ByteArrayInputStream(pdfBytes), "application/pdf", name);
           /* if (xmlProf != null && xmlProf.getXmlp() != null) {
                ///RUN REPORTER
                name = nombreArchivo + "_" + ManejadorFechas.obtenIdEvent() + ".pdf";
                if (otro != null) {
                    ///CARGA EL PDF DESDE DISCO
                    if (otro.getParam2() != null && !otro.getParam2().trim().equals("")) {
                        FileInputStream fi = null;
                        rutaPDF = otro.getParam2().trim();
                        File archivo = new File(otro.getParam2().trim());
                        if (archivo.exists()) {
                            xmlDoc = new byte[(int) archivo.length()];
                            try {
                                fi = new FileInputStream(archivo);
                                fi.read(xmlDoc);
                                fi.close();
                            } catch (Exception e) {
                                e.printStackTrace(System.out);
                            }
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Lo sentimos, no pudimos encontrar el PDF.", "Detail"));
                        }
                    } else {
                        xmlDoc = xmlProf.getXmlp();
                        xmlXml = xmlProf.getXml();
                    }
                } else {
                    xmlDoc = xmlProf.getXmlp();
                    xmlXml = xmlProf.getXml();
                }
                if (otro != null) {
                    if (otro.getParam20() != null &&
                            (otro.getParam20().equalsIgnoreCase("33") ||
                                    otro.getParam20().equalsIgnoreCase("3.3"))) {
                        try {
                            pdfBytes = JasperGenCfdi33.genPdf(cfdi.getEstadoDocumento(), xmlXml, xmlDoc, plantilla);
                            scFile = new DefaultStreamedContent(new ByteArrayInputStream(pdfBytes), "application/pdf", name);
                        } catch (JRException e) {
                            e.printStackTrace();
                            genPdfError("JRExeption:" + e.getMessage());
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                                    FacesMessage.SEVERITY_ERROR,
                                    "No se pudo generar el pdf.", "Detail"));
                        } catch (Exception e) {
                            e.printStackTrace();
                            genPdfError("Exception:" + e.getMessage());
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                                    FacesMessage.SEVERITY_ERROR,
                                    "No se pudo generar el pdf.", "Detail"));
                        }
                    }
                }

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Lo sentimos, no pudimos encontrar el Xml.", "Detail"));
            }*/
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Lo sentimos, al parecer el registor no tiene asociado una plantilla.", "Detail"));
        }
    }

    /*
    public void downloadPdfPagoFile(int idCfd, String nombreArchivo) {
       /* MCfdPagos cfdi = daoPagos.BuscarId(idCfd);
        MOtroPagos otro = daoPagos.Otro(idCfd);
        MPlantilla plantilla = null;
        if (cfdi != null && cfdi.getPlantillaId() != null) {
            plantilla = daoPlantilla.BuscarPlantilla(cfdi.getPlantillaId());
        }
        MCfdXmlPagos xmlPago = daoPagos.findXmlByCfdiId(idCfd);
        scFile = null;
        String rutaPDF = null;
        String name = "pdf.pdf";
        byte[] xmlDoc = null;
        byte[] xmlXml = null;
        byte[] pdfBytes = null;

        ClienteFEWS clienteFEWS = new ClienteFEWS();
        try {
            pdfBytes = clienteFEWS.clienteFEWS("PDF_PAGO_FILE", idCfd);
            if (pdfBytes != null) {
                name = nombreArchivo + "_" + ManejadorFechas.obtenIdEvent() + ".pdf";
                name = nombreArchivo + "_" + ManejadorFechas.obtenIdEvent() + ".pdf";
                scFile = new DefaultStreamedContent(new ByteArrayInputStream(pdfBytes), "application/pdf", name);
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Lo sentimos, no pudimos encontrar el PDF.", "Detail"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            clienteFEWS = null;
        }
          /*  if (xmlPago != null && xmlPago.getXmlp() != null) {
                ///RUN REPORTER
                name = nombreArchivo + "_" + ManejadorFechas.obtenIdEvent() + ".pdf";
                if (otro != null) {
                    ///CARGA EL PDF DESDE DISCO
                    if (otro.getParam2() != null && !otro.getParam2().trim().equals("")) {
                        FileInputStream fi = null;
                        rutaPDF = otro.getParam2().trim();
                        File archivo = new File(otro.getParam2().trim());
                        if (archivo.exists()) {
                            xmlDoc = new byte[(int) archivo.length()];
                            try {
                                fi = new FileInputStream(archivo);
                                fi.read(xmlDoc);
                                fi.close();
                            } catch (Exception e) {
                                e.printStackTrace(System.out);
                            }
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Lo sentimos, no pudimos encontrar el PDF.", "Detail"));
                        }
                    } else {
                        xmlDoc = xmlPago.getXmlp();
                        xmlXml = xmlPago.getXml();
                    }
                } else {
                    xmlDoc = xmlPago.getXmlp();
                    xmlXml = xmlPago.getXml();
                }
                if (otro != null) {
                    if (otro.getParam20() != null &&
                            (otro.getParam20().equalsIgnoreCase("33") ||
                                    otro.getParam20().equalsIgnoreCase("3.3"))) {
                        try {
                            pdfBytes = JasperGenCfdi33.genPdf(cfdi.getEstadoDocumento(),xmlXml, xmlDoc, plantilla);
                            scFile = new DefaultStreamedContent(new ByteArrayInputStream(pdfBytes), "application/pdf", name);
                        } catch (JRException e) {
                            e.printStackTrace();
                            genPdfError("JRExeption:" + e.getMessage());
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                                    FacesMessage.SEVERITY_ERROR,
                                    "No se pudo generar el pdf.", "Detail"));
                        } catch (Exception e) {
                            e.printStackTrace();
                            genPdfError("Exception:" + e.getMessage());
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                                    FacesMessage.SEVERITY_ERROR,
                                    "No se pudo generar el pdf.", "Detail"));
                        }
                    }
                }

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Lo sentimos, no pudimos encontrar el Xml.", "Detail"));
            }

    }*/

    /**
     * Genera pdf de pagos
     *
     * @param idCfd
     * @param nombreArchivo
     */
    public void downloadPdfPagoFile(int idCfd, String nombreArchivo) {
        byte[] pdfBytes = null;
        String name = "pdf.pdf";
        name = nombreArchivo + "_" + ManejadorFechas.obtenIdEvent() + ".pdf";
        ClienteFEWS clienteFEWS = new ClienteFEWS();
        try {
            pdfBytes = clienteFEWS.clienteFEWS("PDF_PAGO_FILE", idCfd);
            scFile = new DefaultStreamedContent(new ByteArrayInputStream(pdfBytes), "application/pdf", name);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Lo sentimos, no pudimos encontrar el PDF.", "Detail"));
        }

        /*MCfdPagos cfdi = daoPagos.BuscarId(idCfd);
        MOtroPagos otro = daoPagos.Otro(idCfd);
        MPlantilla plantilla = null;
        if (cfdi != null && cfdi.getPlantillaId() != null) {
            plantilla = daoPlantilla.BuscarPlantilla(cfdi.getPlantillaId());
        }
        MCfdXmlPagos xmlPago = daoPagos.findXmlByCfdiId(idCfd);
        scFile = null;
        String rutaPDF = null;
        String name = "pdf.pdf";
        byte[] xmlDoc = null;
        byte[] xmlXml = null;
        byte[] pdfBytes = null;
        if (plantilla != null && plantilla.getPlantilla() != null) {
            if (xmlPago != null && xmlPago.getXmlp() != null) {
                ///RUN REPORTER
                name = nombreArchivo + "_" + ManejadorFechas.obtenIdEvent() + ".pdf";
                if (otro != null) {
                    ///CARGA EL PDF DESDE DISCO
                    if (otro.getParam2() != null && !otro.getParam2().trim().equals("")) {
                        FileInputStream fi = null;
                        rutaPDF = otro.getParam2().trim();
                        File archivo = new File(otro.getParam2().trim());
                        if (archivo.exists()) {
                            xmlDoc = new byte[(int) archivo.length()];
                            try {
                                fi = new FileInputStream(archivo);
                                fi.read(xmlDoc);
                                fi.close();
                            } catch (Exception e) {
                                e.printStackTrace(System.out);
                            }
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Lo sentimos, no pudimos encontrar el PDF.", "Detail"));
                        }
                    } else {
                        xmlDoc = xmlPago.getXmlp();
                        xmlXml = xmlPago.getXml();
                    }
                } else {
                    xmlDoc = xmlPago.getXmlp();
                    xmlXml = xmlPago.getXml();
                }
                if (otro != null) {
                    if (otro.getParam20() != null &&
                            (otro.getParam20().equalsIgnoreCase("33") ||
                                    otro.getParam20().equalsIgnoreCase("3.3"))) {
                        try {
                            pdfBytes = JasperGenCfdi33.genPdf(cfdi.getEstadoDocumento(),xmlXml, xmlDoc, plantilla);
                            scFile = new DefaultStreamedContent(new ByteArrayInputStream(pdfBytes), "application/pdf", name);
                        } catch (JRException e) {
                            e.printStackTrace();
                            genPdfError("JRExeption:" + e.getMessage());
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                                    FacesMessage.SEVERITY_ERROR,
                                    "No se pudo generar el pdf.", "Detail"));
                        } catch (Exception e) {
                            e.printStackTrace();
                            genPdfError("Exception:" + e.getMessage());
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                                    FacesMessage.SEVERITY_ERROR,
                                    "No se pudo generar el pdf.", "Detail"));
                        }
                    }
                }

            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Lo sentimos, no pudimos encontrar el Xml.", "Detail"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Lo sentimos, al parecer el registor no tiene asociado una plantilla.", "Detail"));
        }*/
    }



    public void genPdfError(String message) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            com.lowagie.text.Document document = new com.lowagie.text.Document();
            PdfWriter.getInstance(document, out);
            document.open();
            document.add(new Paragraph("Pdf can´t be generated!!!!!"));
            document.add(new Paragraph("Error:" + message));
            document.close();
            scFile = new DefaultStreamedContent(new ByteArrayInputStream(out.toByteArray()), "application/pdf",
                    "error_" + ManejadorFechas.obtenIdEvent() + ".pdf");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Descarga de diferentes tipos de archivos
     *
     * @param idarchivo
     * @return
     * @throws java.lang.Exception
     */
    public void descargarArchivosVarios(String idarchivo) throws Exception {
        scFile = null;
        byte[] doc = "".getBytes();
        String nombreA = "";
        String contentType = "application/zip";
        //For recognize the mime type
        MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
        Collection<?> mimeTypes;
        try {
            int id = Integer.parseInt(idarchivo);
            if (id > 0) {
                ArchivosCfdDAO DAOArchi = new ArchivosCfdDAO();
                MArchivosCfd archivos = DAOArchi.BuscarArchivoCfdId(id);
                if (archivos != null) {
                    FileInputStream fi = null;
                    File archivo = new File(archivos.getRuta().trim());
                    nombreA = archivo.getName();
                    doc = new byte[(int) archivo.length()];
                    try {
                        fi = new FileInputStream(archivo);
                        fi.read(doc);
                        fi.close();
                        mimeTypes = MimeUtil.getMimeTypes(archivo);
                        contentType = mimeTypes.toString();
                        MimeUtil.unregisterMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        contentType = "application/zip";
                    }
                }
            }
            scFile = new DefaultStreamedContent(new ByteArrayInputStream(doc), contentType, nombreA);
        } catch (Exception e) {
            throw e;
        }
        //return scFile;
    }

    public void eliminarArchivosVarios() {
        ArchivosCfdDAO daoArchi = new ArchivosCfdDAO();
        if (daoArchi.BorrarArchivoCfd(archivoSelectCfd)) {
            File fileToDelete = new File(archivoSelectCfd.getRuta());
            if (fileToDelete.exists() && !fileToDelete.isDirectory()) {
                if (fileToDelete.delete()) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "El archivo ha sido eliminado.", "--"));
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "El archivo no pudo ser eliminado.", "--"));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "El archivo no existe en disco.", "--"));
            }

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No pudo ser eliminado el registro de la base de datos.", "--"));
        }
        archivoSelectCfd = null;
    }

    /**
     * Download the xml File for clientFiles Invoice
     *
     * @param numeroFactura
     * @param nombreFactura
     * @return
     * @throws java.lang.Exception
     */
    public void descargarArchivoXMLCFD(String numeroFactura, String nombreFactura) throws Exception {
        scFile = null;
        byte[] xmlDoc = "".getBytes();
        CfdDAO cfdDAO = new CfdDAO();
        String name = "xml.xml";
        ClienteFEWS clienteFEWS = new ClienteFEWS();
        try {
           /* if (numeroFactura == null) {
                numeroFactura = "0";
            }
            MInvoice datosFactura = cfdDAO.BuscarId(Limpiador.cleanStringToIntegers(numeroFactura));
            String Fol = "" + datosFactura.getFolio();
            Fol = Fol.replace(".0", "");
            if (datosFactura != null && datosFactura.getSerie() != null) {
                nombreFactura = datosFactura.getSerie().trim() + "." + Fol.trim();
            }
            if (nombreFactura == null) {
                nombreFactura = "";
            }
            if ("".equalsIgnoreCase(nombreFactura)) {
                xmlDoc = (new String(
                        "No se pudo obtener la factura debido a un error, idFactura nulo."))
                        .getBytes();
            } else if (xmlDoc == null) {
                xmlDoc = (new String("No se encontraron registros de la factura seleccionada.")).getBytes();

            }
            String str = new String(xmlDoc).trim();
            str = new fe.xml.CharUnicode().getTextEncoded2(str);
            byte[] bytes = str.getBytes();
            if (bytes == null) {
                bytes = "".getBytes();
            }*/
            xmlDoc = clienteFEWS.clientFiles("clientFiles", numeroFactura);
            name = nombreFactura + "_" + ManejadorFechas.obtenIdEvent() + ".xml";
            scFile = new DefaultStreamedContent(new ByteArrayInputStream(xmlDoc), "application/xml", name);
            // ========================================================================================================================================================
        } catch (Exception e) {
            PintarLog
                    .println(" ***======= Error al llamar al metodo writeXmlBytes: "
                            + e.getMessage());
            // e.printStackTrace();
            throw e;
        }finally{
            clienteFEWS = null;
        }
    }

    /**
     * @param numeroFactura
     * @param nombreFactura
     * @return
     * @throws java.lang.Exception
     */
    public void descargarArchivoPDFCFD(String numeroFactura, String nombreFactura) throws Exception {
        scFile = null;
        byte[] xmlDoc = "".getBytes();
        byte[] plantilla = "".getBytes();
        byte[] bytes;
        String name = "pdf.pdf";
       /*
        CfdDAO cfdDAO = new CfdDAO();
        HashMap hm = new HashMap();
        MInvoice datosFactura = null;
        java.net.URL path2 = AuxDomainAction.class.getProtectionDomain().getCodeSource().getLocation();
        String rutaAbsoluta = DAO.ObtenerRuta(path2.getPath());
        rutaAbsoluta = DAO.obtenerRutaDeDirectorioAnterior(rutaAbsoluta, "WEB-INF");// Ruta*/
        ClienteFEWS clienteFEWS = new ClienteFEWS();
        try {
           /* if (numeroFactura == null) {
                numeroFactura = "0";
            }
            datosFactura = cfdDAO.BuscarId(Limpiador.cleanStringToIntegers(numeroFactura));
            String Fol = "" + datosFactura.getFolio();
            Fol = Fol.replace(".0", "");
            if (datosFactura != null && datosFactura.getSerie() != null) {
                nombreFactura = datosFactura.getSerie().trim() + "." + Fol.trim();
            }
            if (nombreFactura == null) {
                nombreFactura = "";
            }
            if ("".equalsIgnoreCase(nombreFactura)) {
                throw new Exception("No se pudo obtener la factura debido a un error, idFactura nulo.");
            } else {
                System.out.println("empieza a crear pdf " + nombreFactura.trim() + "_P");
                if (xmlDoc == null) {
                    System.out.println("No se pudo descargar el documento");
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
                    .cleanStringToIntegers(numeroFactura));
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
            if (doc != null) {
                hm.put("CADENA", new Sat().getCadenaOriginal(doc));
            }

            // ======================= CORRE EL REPORTE PDF
            // ================================================
            InputStream in = null;
            if (datosFactura.getPlantilla() == null) {
                throw new Exception("No se encontraron plantillas  ligadas a la factura " + nombreFactura);
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
            }*/
            bytes = clienteFEWS.clientFiles("PDFCFD", numeroFactura);
            name = nombreFactura + "_" + ManejadorFechas.obtenIdEvent() + ".pdf";
            scFile = new DefaultStreamedContent(new ByteArrayInputStream(bytes), "application/pdf", name);
        } catch (Exception e) {
            PintarLog.println(" ***======= Error al llamar al metodo descargarArchivoPDFCFD(): " + e.getMessage());
            throw e;
        }finally {
            clienteFEWS = null;
        }
    }

    /**
     * Acuse de una factura cancelada
     *
     * @param numeroFactura
     */
    public void descargarAcuse(String numeroFactura) throws Exception {
        scFile = null;
        byte[] xmlDoc = "".getBytes();
        byte[] bytes = null;
        MCancelados cancel = null;
        String nombre = "";
        String name = "";
        try {
            String idcfd = numeroFactura;
            int id = Integer.parseInt(idcfd);
            if (id > 0) {
                CanceladoDAO DAOCan = new CanceladoDAO();
                cancel = DAOCan.buscarCancelados_IdCfd(id);
                if (cancel != null) {
                    nombre = cancel.getCfd().getUuid();
                    if (cancel.getRuta() != null && !cancel.getRuta().trim().equals("")) {
                        FileInputStream fi = null;
                        File archivo = new File(cancel.getRuta().trim());
                        bytes = new byte[(int) archivo.length()];
                        try {
                            fi = new FileInputStream(archivo);
                            fi.read(bytes);
                            fi.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        xmlDoc = cancel.getXmlAcuse();
                        String str = new String(xmlDoc).trim();
                        str = new fe.xml.CharUnicode().getTextEncoded2(str);
                        bytes = str.getBytes();
                    }
                }
            }
            if (bytes == null) {
                bytes = "No se encontro el acuse de la factura".getBytes();
            }
            name = "Acuse_" + nombre + ".xml";
            scFile = new DefaultStreamedContent(new ByteArrayInputStream(bytes), "application/xml", name);

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Descarga el zip de la plantilla
     *
     * @param idPlantilla
     * @return
     */
    public void descargarPlantillaZip(int idPlantilla) {
        scFile = null;
        PlantillaDAO daoPlantillas = new PlantillaDAO();
        MPlantilla plant = daoPlantillas.BuscarPlantilla(idPlantilla);
        String nombreplan = "";
        setScFile(null);
        if (plant.getNombre() != null && !"".equals(plant.getNombre().trim())) {
            nombreplan = nombreplan + plant.getNombre().trim();
        }
        nombreplan = nombreplan + "_" + plant.getVersion();
        nombreplan = nombreplan + ".zip";
        if (plant != null) {
            scFile = new DefaultStreamedContent(new ByteArrayInputStream(plant.getPlantilla()), "application/zip", nombreplan);
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

    public void downloadXmlRetenFile(String idCFD, String nombreFactura) throws Exception {
        scFile = null;
        byte[] bytes = "".getBytes();
        final FacturaManejador facturaManejador = new FacturaManejador();
        //List<MCfdXmlRetencion> archivosFactura = new ArrayList<MCfdXmlRetencion>();
        //MOtroRetencion otro = null;
        ClienteFEWS clienteFEWS = new ClienteFEWS();
        try {

           /* if ("".equalsIgnoreCase(idCFD)) {
                xmlDoc = new String("No se pudo obtener la factura debido a un error, idFactura nulo.").getBytes();
            } else {
                otro = facturaManejador.ObtenerMOtroPorCFDIXMLRetencion(Integer.parseInt(idCFD));
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
                    archivosFactura = facturaManejador.consultaStreamFacturaRetencion(Limpiador.cleanStringToIntegers(idCFD));
                    if (archivosFactura != null && archivosFactura.size() > 0) {
                        xmlDoc = archivosFactura.get(0).getXml();
                    } else if (!"".equalsIgnoreCase(idCFD) && archivosFactura.size() == 0) {
                        xmlDoc = new String("No se encontraron registros de la factura seleccionada.").getBytes();
                    }
                }
            }
            String str = new String(xmlDoc).trim();
            //str = charUnicode.getTextEncoded2(str);
            byte[] bytes = str.getBytes();
            if (bytes == null) {
                bytes = "".getBytes();
            }*/
            bytes = clienteFEWS.clientFiles("XML_RETENCIONES", idCFD);
            nombreFactura += "_" + ManejadorFechas.obtenIdEvent() + ".xml";
            scFile = new DefaultStreamedContent(new ByteArrayInputStream(bytes), "application/octet-stream", nombreFactura);
        } catch (Exception e2) {
            PintarLog.println(" ***======= Error al llamar al metodo writeXmlBytes: " + e2.getMessage());
            throw e2;
        }finally {
            clienteFEWS = null;
        }
    }

    public void downloadPdfRetenFile(String idCFD, String nombreFactura) {
        scFile = null;
        String recordPath = "/Retenciones";
        // FacturaManejador facturaManejador = new FacturaManejador();
        // List<MCfdXmlRetencion> listaFacturas = new ArrayList<MCfdXmlRetencion>();
        //MOtroRetencion otro = null;
        byte[] xmlDoc = "".getBytes();
        ClienteFEWS clienteFEWS = new ClienteFEWS();
        try {
            xmlDoc = clienteFEWS.clientFiles("PDF_RETENCIONES", idCFD);
            if (xmlDoc != null) {
                nombreFactura += "_" + ManejadorFechas.obtenIdEvent() + ".pdf";
                scFile = new DefaultStreamedContent(new ByteArrayInputStream(xmlDoc), "application/pdf", nombreFactura);
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se encontraron registros de la factura seleccionada", "--"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            clienteFEWS = null;
        }

        /*URL path2 = AuxDomainAction.class.getProtectionDomain().getCodeSource().getLocation();
        String rutaAbsoluta = DAO.ObtenerRuta(path2.getPath());
        rutaAbsoluta = DAO.obtenerRutaDeDirectorioAnterior(rutaAbsoluta, "WEB-INF");
        String rutaPDF = null;
        PintarLog.println("idCFD" + idCFD);
        if ("".equalsIgnoreCase(idCFD)) {
            //throw new Exception("No se pudo obtener la factura debido a un error, idFactura nulo.");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo descargar la factura, id de factura vacio.", "--"));
        }
        otro = facturaManejador.ObtenerMOtroPorCFDIPDFRetencion(Integer.parseInt(idCFD));
        if (otro != null) {
            System.out.print("otro Es distindo de null");
            if (otro.getParam2() != null && !otro.getParam2().trim().equals("")) {
                FileInputStream fi = null;
                rutaPDF = otro.getParam2().trim();
                final File archivo = new File(otro.getParam2().trim());
                xmlDoc = new byte[(int) archivo.length()];
                try {
                    fi = new FileInputStream(archivo);
                    fi.read(xmlDoc);
                    fi.close();
                } catch (Exception e) {
                    e.printStackTrace(System.out);
                }
            } else {
                try {
                    listaFacturas = (List<MCfdXmlRetencion>) facturaManejador.consultaStreamFacturaRetencion(Limpiador.cleanStringToIntegers(idCFD));
                } catch (Exception ex) {
                    Logger.getLogger(ManagedBeanFileUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (null != listaFacturas && listaFacturas.size() > 0) {
                    xmlDoc = listaFacturas.get(0).getXmlp();
                } else if (!"".equalsIgnoreCase(idCFD) && listaFacturas.size() == 0) {
                    //throw new Exception("No se encontraron registros de la factura seleccionada.");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se encontraron registros de la factura seleccionada", "--"));
                }
            }
        } else {
            try {
                listaFacturas = (List<MCfdXmlRetencion>) facturaManejador.consultaStreamFacturaRetencion(Limpiador.cleanStringToIntegers(idCFD));
            } catch (Exception ex) {
                Logger.getLogger(ManagedBeanFileUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (null != listaFacturas && listaFacturas.size() > 0) {
                xmlDoc = listaFacturas.get(0).getXmlp();
            } else if (!"".equalsIgnoreCase(idCFD) && listaFacturas.size() == 0) {
                //throw new Exception("No se encontraron registros de la factura seleccionada.");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se encontraron registros de la factura seleccionada.", "--"));
            }
        }
        McfdRetencion datosFactura = null;
        try {
            datosFactura = facturaManejador.consultaDatosFacturaRetencion(Limpiador.cleanStringToIntegers(idCFD));
        } catch (Exception ex) {
            Logger.getLogger(ManagedBeanFileUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] plantilla = "".getBytes();
        if (rutaPDF != null && (rutaPDF.trim().contains(".pdf") || rutaPDF.trim().contains(".PDF"))) {
            byte[] bytes = xmlDoc;
            if (bytes == null) {
                bytes = "".getBytes();
            }
            nombreFactura += "_" + ManejadorFechas.obtenIdEvent() + ".pdf";
            scFile = new DefaultStreamedContent(new ByteArrayInputStream(bytes), "application/pdf", nombreFactura);

        } else {
            try {
                if (datosFactura.getMPlantilla() == null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No existe una plantilla asociada.", "--"));
                    //this.addFieldError("error", "Es necesario Insertar el nombre");
                } else {
                    if (datosFactura.getMPlantilla().getRootPath() != null && !"".equals(datosFactura.getMPlantilla().getRootPath())) {
                        recordPath = datosFactura.getMPlantilla().getRootPath();
                    }
                    plantilla = datosFactura.getMPlantilla().getPlantilla();
                }
                String rfcE = "Nacional-";
                if ("Nacional".equals(datosFactura.getNacionalidad())) {
                    rfcE += datosFactura.getRfc();
                }
                if ("Extranjero".equals(datosFactura.getNacionalidad())) {
                    rfcE = "Extranjero-" + datosFactura.getRfc();
                }
                HashMap hm = new HashMap();
                Image img;
                img = new QRImagen().get2DBarCode(Limpiador.cleanString(
                        datosFactura.getMEmpresa().getRfcOrigen()),
                        Limpiador.cleanString(rfcE),
                        Limpiador.cleanString("" + datosFactura.getTotal()),
                        Limpiador.cleanString(datosFactura.getUuid()));
                hm.put("2DCODEBAR", img);
                hm.put("IMAGES", rutaAbsoluta + "imagenesReportes/");
                hm.put("ESTATUS", "" + datosFactura.getEstadoDocumento());
                hm.put("REPORTES", rutaAbsoluta + "/reportes/");
                hm.put("CANCELADO", rutaAbsoluta);
                JRFileVirtualizer virt = new JRFileVirtualizer(300, System.getProperty("java.io.tmpdir"));
                virt.setReadOnly(false);
                hm.put("REPORT_VIRTUALIZER", virt);
                JRXmlDataSource jrxmlds;
                try {
                    jrxmlds = new JRXmlDataSource((InputStream) new ByteArrayInputStream(xmlDoc), recordPath);
                    jrxmlds.setLocale(new Locale("sp", "MX"));
                    jrxmlds.setNumberPattern("#,##0.00");
                    nombreFactura += "_" + ManejadorFechas.obtenIdEvent() + ".pdf";
                    InputStream in = this.setParameters(plantilla, hm);
                    byte[] res = JasperRunManager.runReportToPdf(in, (Map) hm, (JRDataSource) jrxmlds);
                    scFile = new DefaultStreamedContent(new ByteArrayInputStream(res), "application/pdf", nombreFactura);
                } catch (JRException ex) {
                    Logger.getLogger(ManagedBeanFileUtil.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(ManagedBeanFileUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (Exception ex) {
                Logger.getLogger(ManagedBeanFileUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (Runtime.getRuntime().totalMemory() > 700.0 * Math.pow(2.0, 20.0)) {
            System.gc();
        }*/
    }

    /**
     * @return the scFile
     */
    public DefaultStreamedContent getScFile() {

        return scFile;
    }

    /**
     * @param scFile the scFile to set
     */
    public void setScFile(DefaultStreamedContent scFile) {
        this.scFile = scFile;
    }

    public MArchivosCfd getArchivoSelectCfd() {
        return archivoSelectCfd;
    }

    public void setArchivoSelectCfd(MArchivosCfd archivoSelectCfd) {
        this.archivoSelectCfd = archivoSelectCfd;
    }


}
