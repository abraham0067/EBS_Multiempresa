package com.ebs.complementoextdata;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by eflores on 17/01/2018.
 */
public class FlagsEntradasComercioExterior {
    @Getter @Setter private final int DESCONOCIDO  = -1;
    @Getter @Setter private final int NO_REQUERIDO = 0;
    @Getter @Setter private final int REQUERIDO    = 1;
    @Getter @Setter private final int OPCIONAL     = 2;
    ///-----------------------------------------------------------------------------------------------------------------
    /// DATOS COMPLEMENTO
    ///-----------------------------------------------------------------------------------------------------------------
    @Getter @Setter int motivoTraslado             = DESCONOCIDO;
    @Getter @Setter int tipoOperacion              = DESCONOCIDO;
    @Getter @Setter int clavePedimento             = DESCONOCIDO;
    @Getter @Setter int certificadoOrigen          = DESCONOCIDO;
    @Getter @Setter int numCertificadoOrigen       = DESCONOCIDO;
    @Getter @Setter int numExportadorConfiable     = DESCONOCIDO;
    @Getter @Setter int incoterm                   = DESCONOCIDO;
    @Getter @Setter int subdivision                = DESCONOCIDO;
    @Getter @Setter int observaciones              = DESCONOCIDO;
    @Getter @Setter int tipoCambioUSD              = DESCONOCIDO;
    @Getter @Setter int totalUSD                   = DESCONOCIDO;

    ///-----------------------------------------------------------------------------------------------------------------
    /// DATOS Emisor
    ///-----------------------------------------------------------------------------------------------------------------
    @Getter @Setter int emisorCurp = DESCONOCIDO;
    @Getter @Setter int emisorCalle = DESCONOCIDO;
    @Getter @Setter int emisorCodigoPostal = DESCONOCIDO;
    @Getter @Setter int emisorColonia = DESCONOCIDO;
    @Getter @Setter int emisorEstado = DESCONOCIDO;
    @Getter @Setter int emisorMunicipio = DESCONOCIDO;
    @Getter @Setter int emisorNumeroExterior = DESCONOCIDO;
    @Getter @Setter int emisorNumeroInterior = DESCONOCIDO;
    @Getter @Setter int emisorPais = DESCONOCIDO;
    @Getter @Setter int emisorReferencia = DESCONOCIDO;

    ///-----------------------------------------------------------------------------------------------------------------
    /// DATOS Receptor
    ///-----------------------------------------------------------------------------------------------------------------
    @Getter @Setter int receptorNumRegIdTributario = DESCONOCIDO;
    @Getter @Setter int receptorCalle = DESCONOCIDO;
    @Getter @Setter int receptorCodigoPostal = DESCONOCIDO;
    @Getter @Setter int receptorEstado = DESCONOCIDO;
    @Getter @Setter int receptorMunicipio = DESCONOCIDO;
    @Getter @Setter int receptorPais = DESCONOCIDO;
    @Getter @Setter int receptorNumeroExterior = DESCONOCIDO;
    @Getter @Setter int receptorNumeroInterior = DESCONOCIDO;


    ///-----------------------------------------------------------------------------------------------------------------
    /// DATOS Propietario
    ///-----------------------------------------------------------------------------------------------------------------
    @Getter @Setter int propietarioNumRegIdTrib = DESCONOCIDO;
    @Getter @Setter int propietarioResidenciaFiscal = DESCONOCIDO;

    ///-----------------------------------------------------------------------------------------------------------------
    /// DATOS Destinatario
    ///-----------------------------------------------------------------------------------------------------------------
    @Getter @Setter int destinatarioNumRegIdTrib = DESCONOCIDO;
    @Getter @Setter int destinatarioNombre = DESCONOCIDO;
    @Getter @Setter int destinatarioCalle = DESCONOCIDO;
    @Getter @Setter int destinatarioColonia = DESCONOCIDO;
    @Getter @Setter int destinatarioCodigoPostal = DESCONOCIDO;
    @Getter @Setter int destinatarioEstado = DESCONOCIDO;
    @Getter @Setter int destinatarioMunicipio = DESCONOCIDO;
    @Getter @Setter int destinatarioPais = DESCONOCIDO;





    public FlagsEntradasComercioExterior(String claveTipoDoc) {
        this.changeRequiredInputs(claveTipoDoc);

    }

    public void reset(){
        motivoTraslado             = DESCONOCIDO;
        tipoOperacion              = DESCONOCIDO;
        clavePedimento             = DESCONOCIDO;
        certificadoOrigen          = DESCONOCIDO;
        numCertificadoOrigen       = DESCONOCIDO;
        numExportadorConfiable     = DESCONOCIDO;
        incoterm                   = DESCONOCIDO;
        subdivision                = DESCONOCIDO;
        observaciones              = DESCONOCIDO;
        tipoCambioUSD              = DESCONOCIDO;
        totalUSD                   = DESCONOCIDO;

        emisorCurp                 = DESCONOCIDO;
        emisorCalle                = DESCONOCIDO;
        emisorCodigoPostal         = DESCONOCIDO;
        emisorColonia              = DESCONOCIDO;
        emisorEstado               = DESCONOCIDO;
        emisorMunicipio            = DESCONOCIDO;
        emisorNumeroExterior       = DESCONOCIDO;
        emisorNumeroInterior       = DESCONOCIDO;
        emisorPais                 = DESCONOCIDO;
        emisorReferencia           = DESCONOCIDO;

        receptorNumRegIdTributario = DESCONOCIDO;
        receptorCalle              = DESCONOCIDO;
        receptorCodigoPostal       = DESCONOCIDO;
        receptorEstado             = DESCONOCIDO;
        receptorMunicipio          = DESCONOCIDO;
        receptorPais               = DESCONOCIDO;
        receptorNumeroExterior     = DESCONOCIDO;
        receptorNumeroInterior     = DESCONOCIDO;

        propietarioResidenciaFiscal = DESCONOCIDO;
        propietarioNumRegIdTrib     = DESCONOCIDO;


        destinatarioNumRegIdTrib   = DESCONOCIDO;
        destinatarioNombre         = DESCONOCIDO;
        destinatarioCalle          = DESCONOCIDO;
        destinatarioColonia        = DESCONOCIDO;
        destinatarioCodigoPostal   = DESCONOCIDO;
        destinatarioEstado         = DESCONOCIDO;
        destinatarioMunicipio      = DESCONOCIDO;
        destinatarioPais           = DESCONOCIDO;

    }

    public void changeRequiredInputs(String claveTipoDocumento) {
        if (claveTipoDocumento.equals("I") || claveTipoDocumento.equals("E")) {
            motivoTraslado             = NO_REQUERIDO;
            tipoOperacion              = REQUERIDO;
            clavePedimento             = REQUERIDO;
            certificadoOrigen          = REQUERIDO;
            numCertificadoOrigen       = NO_REQUERIDO;
            numExportadorConfiable     = NO_REQUERIDO;
            incoterm                   = REQUERIDO;
            subdivision                = REQUERIDO;
            observaciones              = OPCIONAL;
            tipoCambioUSD              = REQUERIDO;
            totalUSD                   = REQUERIDO;

            emisorCurp                 = NO_REQUERIDO;
            emisorCalle                = REQUERIDO;
            emisorCodigoPostal         = REQUERIDO;
            emisorColonia              = OPCIONAL;
            emisorEstado               = REQUERIDO;
            emisorMunicipio            = OPCIONAL;
            emisorNumeroExterior       = OPCIONAL;
            emisorNumeroInterior       = OPCIONAL;
            emisorPais                 = REQUERIDO;
            emisorReferencia           = OPCIONAL;

            receptorNumRegIdTributario = NO_REQUERIDO;
            receptorCalle              = REQUERIDO;
            receptorCodigoPostal       = REQUERIDO;
            receptorEstado             = REQUERIDO;
            receptorMunicipio          = OPCIONAL;
            receptorPais               = REQUERIDO;
            receptorNumeroExterior     = OPCIONAL;
            receptorNumeroInterior     = OPCIONAL;


            propietarioResidenciaFiscal = NO_REQUERIDO;
            propietarioNumRegIdTrib     = NO_REQUERIDO;

            destinatarioNumRegIdTrib   = OPCIONAL;
            destinatarioNombre         = OPCIONAL;
            destinatarioCalle          = REQUERIDO;
            destinatarioColonia        = REQUERIDO;
            destinatarioCodigoPostal   = REQUERIDO;
            destinatarioEstado         = REQUERIDO;
            destinatarioMunicipio      = OPCIONAL;
            destinatarioPais           = REQUERIDO;


        } else if (claveTipoDocumento.equals("T")){
            motivoTraslado             = REQUERIDO;
            tipoOperacion              = REQUERIDO;
            clavePedimento             = REQUERIDO;
            certificadoOrigen          = REQUERIDO;
            numCertificadoOrigen       = OPCIONAL;
            numExportadorConfiable     = NO_REQUERIDO;
            incoterm                   = REQUERIDO;
            subdivision                = REQUERIDO;
            observaciones              = OPCIONAL;
            tipoCambioUSD              = REQUERIDO;
            totalUSD                   = REQUERIDO;

            propietarioResidenciaFiscal = REQUERIDO;
            propietarioNumRegIdTrib     = REQUERIDO;
        } else{
            System.out.println("TIPO DE DOCUMENTO NO IDENTIFICADO");
        }


    }



}
