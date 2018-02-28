package com.ebs.complementoextdata;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eflores on 16/01/2018.
 */
public class CustomComplementoComercioExteriorMetadata {
    @Getter @Setter String motivoTraslado = "";
    @Getter @Setter String tipoOperacion = "";
    @Getter @Setter String claveDePedimento = "A1";
    @Getter @Setter String certificadoOrigen = "0";
    @Getter @Setter String numCertificadoOrigen = "";
    @Getter @Setter String numExportadorConfiable = "";
    @Getter @Setter String incoterm = "";
    @Getter @Setter String subdivision = "0";
    @Getter @Setter String observaciones = "";
    @Getter @Setter double tipoCambioUSD = 1.0;
    @Getter @Setter double totalUSD = 0.0;
    //@Getter @Setter CustomEmisorData emisor ;
    //@Getter @Setter CustomPropietarioData propietario;
    //@Getter @Setter CustomReceptorData receptor;
    //@Getter @Setter CustomDestinatarioData destinatario;
    @Getter @Setter List<CustomMercanciaData> mercancias ;
    @Setter @Getter private boolean disponibleComercioExterior = false;
    @Setter @Getter private boolean usarComercioExterior = false;

    @Getter @Setter CustomDomicilioData emisor ;
    @Getter @Setter CustomDomicilioData propietario;
    @Getter @Setter CustomDomicilioData receptor;
    @Getter @Setter CustomDomicilioData destinatario;

    @Getter @Setter String nombreDestinatario = "";
    @Getter @Setter String idTribDestinatario = "";


}
