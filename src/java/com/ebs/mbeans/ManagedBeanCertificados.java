/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.mbeans;

import fe.db.MAcceso;
import fe.db.MEmpresa;
import fe.db.MLlave;
import fe.model.dao.EmpresaDAO;
import fe.model.dao.LlaveDAO;
import fe.model.dao.LogAccesoDAO;
import fe.model.util.CifraTexto;
import fe.pki.PKI;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import fe.pki.PkiException;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.IOUtils;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 * @author Eduardo C. Flores Ambrosio <Eduardo at EB&S>
 */
public class ManagedBeanCertificados implements Serializable {

    private static final int ESTATUS_DESACTIVADO = 0;
    private static final int ESTATUS_ACTIVADO = 1;

    private List<MEmpresa> empresas;
    private List<MLlave> llaves;
    private MLlave llaveSelected;
    private MLlave llaveNueva;
    private EmpresaDAO daoEmp;
    private LlaveDAO daoLlave;
    private LogAccesoDAO daoLog;
    private MEmpresa empresa;

    @Getter
    @Setter
    private int idEmpresaSelect = -1;

    //Bandera
    private boolean logged;
    //Modelos
    private MAcceso usuarioAcceso;
    //Nuevo certificado
    @Getter
    @Setter
    private int idEmpresaNuevoCert;//Id de la empresa para la cual se agregara el nuevo certificado
    @Getter
    @Setter
    private String clave1;
    @Getter
    @Setter
    private String clave2;
    @Getter
    @Setter
    private String certName;
    @Getter
    @Setter
    private String keyName;
    @Getter
    @Setter
    private boolean activarCertificado = false;

    private UploadedFile key;
    private UploadedFile cert;
    //Contexto
    private HttpServletRequest httpServletRequest;
    private FacesContext facesContext;
    private FacesMessage facesMessage;
    private int currentOperation = -1;
    private String appContext;


    /**
     * Creates a new instance of ManagedBeanCertificados
     */
    public ManagedBeanCertificados() {
    }

    @PostConstruct
    public void init() {
        daoEmp = new EmpresaDAO();
        daoLlave = new LlaveDAO();
        daoLog = new LogAccesoDAO();
        facesContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        usuarioAcceso = (MAcceso) httpSession.getAttribute("macceso");
        appContext = httpServletRequest.getContextPath();
    }

    public void buscarCertificados() {
        llaveSelected = null;
        if (idEmpresaSelect > 0) {
            llaves = this.daoLlave.ListaDeLlavesIdEmpresa(idEmpresaSelect);
        } else {
            llaves = new ArrayList<>();
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Por favor seleccione una empresa.", "Info"));
        }

    }

    public void borrarCertificado() {
        if (llaveSelected != null) {
            if(llaveSelected.getEstatus() == ESTATUS_DESACTIVADO) {
                if (daoLlave.BorrarLlaveId(llaveSelected)) {
                    FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "El certificado se elimino correctamente.", "Info"));
                    daoLog.guardaRegistro(usuarioAcceso, "Borror el certificado: " + llaveSelected.getNoCertificado() + " de la empresa: " + llaveSelected.getEmpresa().getRfcOrigen());
                    buscarCertificados();
                } else {
                    FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "No pudo eliminarse el certficado.", "Info"));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "No puede eliminarse un certificado activo.", "Info"));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, "Debes seleccionar un certificado.", "Info"));
        }
    }

    public void activarCertificadoSeleccionado() {
        if (llaveSelected != null && idEmpresaSelect > 0) {

            List<MLlave> llavesEmp = this.daoLlave.ListaDeLlavesIdEmpresa(idEmpresaSelect);

            ///Desactivar todos los demas certificados
            for (int i = 0; i < llavesEmp.size(); i++) {
                MLlave tmp = llavesEmp.get(i);
                if (tmp.getId() != llaveSelected.getId()) {
                    tmp.setEstatus(ESTATUS_DESACTIVADO);
                    daoLlave.ActualizarLlave(tmp);
                } else {
                    ///
                    llaveSelected.setEstatus(ESTATUS_ACTIVADO);
                    daoLlave.ActualizarLlave(llaveSelected);
                }
            }
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "El certificado ha sido activado, el resto de los certificados fueron desactivados.", "Info"));
            buscarCertificados();

        } else {
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, "Debes seleccionar un certificado.", "Info"));
        }
    }

    public void redirectCrearCertificado() {
        currentOperation = 0;//Creacion
        idEmpresaNuevoCert = usuarioAcceso.getEmpresa().getId();//Ponemos como default la empresa del usuario logeado
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/admin/certs/nuevoCertificado.xhtml");
        } catch (IOException e1) {
            e1.printStackTrace(System.out);
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "A ocurrido un error al redireccionar.", "Error"));
        }
    }

    public void redirectCerts() {
        currentOperation = 0;//Creacion
        buscarCertificados();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(appContext + "/admin/administracionCertificados.xhtml");
        } catch (IOException e1) {
            e1.printStackTrace(System.out);
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "A ocurrido un error al redireccionar.", "Error"));
        }
    }

    public void handleCertUpload(FileUploadEvent event) {
        this.cert = event.getFile();
        certName = event.getFile().getFileName();
    }

    public void handleKeyUpload(FileUploadEvent event) {
        key = event.getFile();
        keyName = event.getFile().getFileName();
    }

    public void registrarNuevoCertificado() {
        llaveNueva = new MLlave();
        if (validarClaveAndEmpresa()) {
            llaveNueva.setEmpresa(daoEmp.BuscarEmpresaId(idEmpresaNuevoCert));
            try {
                byte[] privkey = key.getContents();
                byte[] certif = cert.getContents();
                if (validarCertificado()) {
                    llaveNueva.setCertificado(certif);
                    llaveNueva.setLlave2(privkey); //Save key directly
                    llaveNueva.setEmpresa(daoEmp.BuscarEmpresaId(idEmpresaNuevoCert));
                    llaveNueva.setFechaRegistro(new Date());
                    if (activarCertificado) {
                        llaveNueva.setEstatus(ESTATUS_ACTIVADO);//Mark as activated cert

                        List<MLlave> llavesEmp = this.daoLlave.ListaDeLlavesIdEmpresa(idEmpresaNuevoCert);
                        ///Desactivar todos los demas certificados
                        for (int i = 0; i < llavesEmp.size(); i++) {
                            MLlave tmp = llavesEmp.get(i);
                            tmp.setEstatus(ESTATUS_DESACTIVADO);
                            daoLlave.ActualizarLlave(tmp);
                        }

                    } else {
                        llaveNueva.setEstatus(ESTATUS_DESACTIVADO);
                    }

                    ///INSERT new certificado
                    if (daoLlave.GuardarOActualizarLlave(llaveNueva)) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                                FacesMessage.SEVERITY_INFO, "El certificado se registro correctamente.", "Info"));
                        daoLog.guardaRegistro(usuarioAcceso, "Registro el certificado: " +
                                llaveNueva.getNoCertificado() + " de la empresa: " + llaveNueva.getEmpresa().getRfcOrigen());
                        ///Reset data
                        llaveNueva = null;
                        keyName = "";
                        certName = "";
                        clave1 = "";
                        clave2 = "";
                        cert = null;
                        key = null;
                        activarCertificado = false;
                    } else {
                        FacesContext.getCurrentInstance().addMessage("nuevoForm", new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se logro almacenar el certificado.", "Info"));
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
                FacesContext.getCurrentInstance().addMessage("nuevoForm", new FacesMessage(FacesMessage.SEVERITY_ERROR, "A ocurrido un error.", "Info"));
                Logger.getLogger(ManagedBeanCertificados.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Valida el ingreso de todos los parametros necesarios
     *
     * @return
     */
    public boolean validarClaveAndEmpresa() {
        boolean resp = false;
        if (clave1 == null || clave2 == null) {
            FacesContext.getCurrentInstance().addMessage("nuevoForm", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Las claves no coincideden.", "Info"));
            return false;
        }
        if (key == null || key.getContents() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Seleccione el archivo .key de la empresa.", "Info"));
            return false;
        }
        if (cert == null || cert.getContents() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Seleccione el archivo .cer de la empresa.", "Info"));
            return false;
        }
        if (clave1.equals(clave2)) {
            resp = true;
            if (idEmpresaNuevoCert <= 0) {
                FacesContext.getCurrentInstance().addMessage("nuevoForm", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debes seleccionar una empresa.", "Info"));
                return false;
            }
        } else {
            FacesContext.getCurrentInstance().addMessage("nuevoForm", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Las claves no coincideden.", "Info"));
        }
        return resp;
    }

    /**
     * Validez del certificado
     *
     * @return
     */
    public boolean validarCertificado() {
        boolean val = false;
        try {
            empresa = daoEmp.BuscarEmpresaId(idEmpresaNuevoCert);
            PKI pki = new PKI();
            PrivateKey pk = null;
            try {
                pk = pki.getPrivateKeySAT(key.getInputstream(), clave1);
                if (pk == null) {
                    try {
                        pk = pki.readPKCS8Key(IOUtils.toByteArray(key.getInputstream()));
                    } catch (java.lang.Exception ex) {
                        ex.printStackTrace(System.out);
                        Logger.getLogger(ManagedBeanEmpresas.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (java.lang.IllegalArgumentException e) {
                e.printStackTrace(System.out);
                try {
                    pk = pki.readPKCS8Key(IOUtils.toByteArray(key.getInputstream()));
                } catch (java.lang.Exception ex) {
                    ex.printStackTrace(System.out);
                    Logger.getLogger(ManagedBeanEmpresas.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            X509Certificate[] cer = new X509Certificate[]{pki.getMyCertificate(cert.getInputstream())};
            if (!isFIEL(cer[0])) {
                if (validaCertificadoVsRfc(cer[0], empresa.getRfcOrigen().toUpperCase().trim())) {
                    pki.validaParDeLlaves(cer[0], pk);
                    pki.chkCertificate(new Date(), cer[0]);
                    llaveNueva.setNoCertificado(new String(cer[0].getSerialNumber().toByteArray()));
                    llaveNueva.setInicioVigencia(cer[0].getNotBefore());
                    llaveNueva.setFinVigencia(cer[0].getNotAfter());
                    //Se cifra la llave!!
                    SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
                    CifraTexto c = new CifraTexto(String.valueOf(clave1), sdf.format(new Date()));
                    llaveNueva.setClave(c.getEncode());
                    //llaveNueva.setLlave(pk.getEncoded());
                    val = true;
                }
            }
        } catch (PkiException ex) {
            ex.printStackTrace(System.out);
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Ha ocurrido un error al intentar cargar las llaves: " + ex.getMessage(), "Error"));
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Ha ocurrido un error al intentar cargar las llaves: " + ex.getMessage(), "Error"));
        }
        return val;
    }

    public static byte[] getBytesFromInputStreamDes(InputStream inStream) throws IOException {
        long streamLength = inStream.available();
        if (streamLength > Integer.MAX_VALUE) {
        }
        byte[] bytes = new byte[(int) streamLength];
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = inStream.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file ");
        }
        inStream.close();
        return bytes;
    }

    private boolean isFIEL(X509Certificate cer) {
        boolean resp = false;
        try {
            ASN1InputStream aIn = new ASN1InputStream(new ByteArrayInputStream(
                    cer.getEncoded()));
            DERSequence seq = (DERSequence) aIn.readObject();
            DERTaggedObject tag = (DERTaggedObject) ((DERSequence) ((DERSequence) seq
                    .toASN1Object()).getObjectAt(0)).getObjectAt(7);
            if (tag.getTagNo() == 3) {
                String id = ((DEREncodable) ((DERSequence) ((DERSequence) DERSequence
                        .getInstance(tag, false).getObjectAt(0)).getObjectAt(2))
                        .getObjectAt(0)).toString();
                resp = id.equals("2.16.840.1.113730.1.1");
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
        if (resp) {
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, "No es un Certificado de Sello Digital !\n\n- Es un Certificado de FIEL, verifique.", "error"));
        }
        return resp;
    }

    private boolean validaCertificadoVsRfc(X509Certificate cer, String rfc) {
        boolean val = true;
        String dn = cer.getSubjectDN().getName();
        int x = dn.indexOf("OID.2.5.4.45=");
        if (x >= 0) {
            String crfc = dn.substring(x + 13, x + 26).trim();
            if (dn.toUpperCase().indexOf(rfc.toUpperCase()) < 0 && !crfc.equals("AAA010101AAA")) {
                val = false;
                FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, "¡ El certificado no pertenece al rfc que intenta cargar !", "error"));
            }
        } else {
            val = false;
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, "¡ El certificado no pertenece al rfc que intenta guardar !", "error"));
        }
        return val;

    }

    /**
     * @return the llaveSelected
     */
    public MLlave getLlaveSelected() {
        return llaveSelected;
    }

    /**
     * @param llaveSelected the llaveSelected to set
     */
    public void setLlaveSelected(MLlave llaveSelected) {
        this.llaveSelected = llaveSelected;
    }

    /**
     * @return the empresas
     */
    public List<MEmpresa> getEmpresas() {
        return empresas;
    }

    /**
     * @return the llaves
     */
    public List<MLlave> getLlaves() {
        return llaves;
    }

    /**
     * @param empresas the empresas to set
     */
    public void setEmpresas(List<MEmpresa> empresas) {
        this.empresas = empresas;
    }

    /**
     * @param llaves the llaves to set
     */
    public void setLlaves(List<MLlave> llaves) {
        this.llaves = llaves;
    }

    /**
     * @return the logged
     */
    public boolean isLogged() {
        return logged;
    }

    /**
     * @return the usuarioAcceso
     */
    public MAcceso getUsuarioAcceso() {
        return usuarioAcceso;
    }

    /**
     * @param logged the logged to set
     */
    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    /**
     * @param usuarioAcceso the usuarioAcceso to set
     */
    public void setUsuarioAcceso(MAcceso usuarioAcceso) {
        this.usuarioAcceso = usuarioAcceso;
    }

    /**
     * @param facesMessage the facesMessage to set
     */
    public void setFacesMessage(FacesMessage facesMessage) {
        this.facesMessage = facesMessage;
    }

    /**
     * @return the facesMessage
     */
    public FacesMessage getFacesMessage() {
        return facesMessage;
    }


    /**
     * @return the key
     */
    public UploadedFile getKey() {
        return key;
    }

    /**
     * @return the cert
     */
    public UploadedFile getCert() {
        return cert;
    }

    /**
     * @param key the key to set
     */
    public void setKey(UploadedFile key) {
        this.key = key;
    }

    /**
     * @param cert the cert to set
     */
    public void setCert(UploadedFile cert) {
        this.cert = cert;
    }


    /**
     * @return the empresa
     */
    public MEmpresa getEmpresa() {
        return empresa;
    }

    /**
     * @return the currentOperation
     */
    public int getCurrentOperation() {
        return currentOperation;
    }

    /**
     * @param empresa the empresa to set
     */
    public void setEmpresa(MEmpresa empresa) {
        this.empresa = empresa;
    }

    /**
     * @param currentOperation the currentOperation to set
     */
    public void setCurrentOperation(int currentOperation) {
        this.currentOperation = currentOperation;
    }

    /**
     * @return the llaveNueva
     */
    public MLlave getLlaveNueva() {
        return llaveNueva;
    }

    /**
     * @param llaveNueva the llaveNueva to set
     */
    public void setLlaveNueva(MLlave llaveNueva) {
        this.llaveNueva = llaveNueva;
    }


}
