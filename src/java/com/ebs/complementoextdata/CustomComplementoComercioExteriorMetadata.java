package com.ebs.complementoextdata;

import java.util.List;

/**
 * Created by eflores on 16/01/2018.
 */
public class CustomComplementoComercioExteriorMetadata {
    private String motivoTraslado = "";
    private String tipoOperacion = "";
    private String claveDePedimento = "A1";
    private String certificadoOrigen = "0";
    private String numCertificadoOrigen = "";
    private String numExportadorConfiable = "";
    private String incoterm = "";
    private String subdivision = "0";
    private String observaciones = "";
    private double tipoCambioUSD = 1.0;
    private double totalUSD = 0.0;
    //@Getter @Setter CustomEmisorData emisor ;
    //@Getter @Setter CustomPropietarioData propietario;
    //@Getter @Setter CustomReceptorData receptor;
    //@Getter @Setter CustomDestinatarioData destinatario;
    private List<CustomMercanciaData> mercancias ;
    private boolean disponibleComercioExterior = false;
    private boolean usarComercioExterior = false;

    CustomDomicilioData emisor ;
    CustomDomicilioData propietario;
    CustomDomicilioData receptor;
    CustomDomicilioData destinatario;

    String nombreDestinatario = "";
    String idTribDestinatario = "";


    public String getMotivoTraslado() {
        return this.motivoTraslado;
    }

    public String getTipoOperacion() {
        return this.tipoOperacion;
    }

    public String getClaveDePedimento() {
        return this.claveDePedimento;
    }

    public String getCertificadoOrigen() {
        return this.certificadoOrigen;
    }

    public String getNumCertificadoOrigen() {
        return this.numCertificadoOrigen;
    }

    public String getNumExportadorConfiable() {
        return this.numExportadorConfiable;
    }

    public String getIncoterm() {
        return this.incoterm;
    }

    public String getSubdivision() {
        return this.subdivision;
    }

    public String getObservaciones() {
        return this.observaciones;
    }

    public double getTipoCambioUSD() {
        return this.tipoCambioUSD;
    }

    public double getTotalUSD() {
        return this.totalUSD;
    }

    public List<CustomMercanciaData> getMercancias() {
        return this.mercancias;
    }

    public boolean isDisponibleComercioExterior() {
        return this.disponibleComercioExterior;
    }

    public boolean isUsarComercioExterior() {
        return this.usarComercioExterior;
    }

    public CustomDomicilioData getEmisor() {
        return this.emisor;
    }

    public CustomDomicilioData getPropietario() {
        return this.propietario;
    }

    public CustomDomicilioData getReceptor() {
        return this.receptor;
    }

    public CustomDomicilioData getDestinatario() {
        return this.destinatario;
    }

    public String getNombreDestinatario() {
        return this.nombreDestinatario;
    }

    public String getIdTribDestinatario() {
        return this.idTribDestinatario;
    }

    public void setMotivoTraslado(String motivoTraslado) {
        this.motivoTraslado = motivoTraslado;
    }

    public void setTipoOperacion(String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    public void setClaveDePedimento(String claveDePedimento) {
        this.claveDePedimento = claveDePedimento;
    }

    public void setCertificadoOrigen(String certificadoOrigen) {
        this.certificadoOrigen = certificadoOrigen;
    }

    public void setNumCertificadoOrigen(String numCertificadoOrigen) {
        this.numCertificadoOrigen = numCertificadoOrigen;
    }

    public void setNumExportadorConfiable(String numExportadorConfiable) {
        this.numExportadorConfiable = numExportadorConfiable;
    }

    public void setIncoterm(String incoterm) {
        this.incoterm = incoterm;
    }

    public void setSubdivision(String subdivision) {
        this.subdivision = subdivision;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setTipoCambioUSD(double tipoCambioUSD) {
        this.tipoCambioUSD = tipoCambioUSD;
    }

    public void setTotalUSD(double totalUSD) {
        this.totalUSD = totalUSD;
    }

    public void setMercancias(List<CustomMercanciaData> mercancias) {
        this.mercancias = mercancias;
    }

    public void setDisponibleComercioExterior(boolean disponibleComercioExterior) {
        this.disponibleComercioExterior = disponibleComercioExterior;
    }

    public void setUsarComercioExterior(boolean usarComercioExterior) {
        this.usarComercioExterior = usarComercioExterior;
    }

    public void setEmisor(CustomDomicilioData emisor) {
        this.emisor = emisor;
    }

    public void setPropietario(CustomDomicilioData propietario) {
        this.propietario = propietario;
    }

    public void setReceptor(CustomDomicilioData receptor) {
        this.receptor = receptor;
    }

    public void setDestinatario(CustomDomicilioData destinatario) {
        this.destinatario = destinatario;
    }

    public void setNombreDestinatario(String nombreDestinatario) {
        this.nombreDestinatario = nombreDestinatario;
    }

    public void setIdTribDestinatario(String idTribDestinatario) {
        this.idTribDestinatario = idTribDestinatario;
    }
}
