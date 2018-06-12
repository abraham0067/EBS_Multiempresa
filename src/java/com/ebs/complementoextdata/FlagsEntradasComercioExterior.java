package com.ebs.complementoextdata;

/**
 * Created by eflores on 17/01/2018.
 */
public class FlagsEntradasComercioExterior {
    private final int DESCONOCIDO  = -1;
    private final int NO_REQUERIDO = 0;
    private final int REQUERIDO    = 1;
    private final int OPCIONAL     = 2;
    ///-----------------------------------------------------------------------------------------------------------------
    /// DATOS COMPLEMENTO
    ///-----------------------------------------------------------------------------------------------------------------
    private int motivoTraslado             = DESCONOCIDO;
    private int tipoOperacion              = DESCONOCIDO;
    private int clavePedimento             = DESCONOCIDO;
    private int certificadoOrigen          = DESCONOCIDO;
    private int numCertificadoOrigen       = DESCONOCIDO;
    private int numExportadorConfiable     = DESCONOCIDO;
    private int incoterm                   = DESCONOCIDO;
    private int subdivision                = DESCONOCIDO;
    private int observaciones              = DESCONOCIDO;
    private int tipoCambioUSD              = DESCONOCIDO;
    private int totalUSD                   = DESCONOCIDO;

    ///-----------------------------------------------------------------------------------------------------------------
    /// DATOS Emisor
    ///-----------------------------------------------------------------------------------------------------------------
    private int emisorCurp = DESCONOCIDO;
    private int emisorCalle = DESCONOCIDO;
    private int emisorCodigoPostal = DESCONOCIDO;
    private int emisorColonia = DESCONOCIDO;
    private int emisorEstado = DESCONOCIDO;
    private int emisorMunicipio = DESCONOCIDO;
    private int emisorNumeroExterior = DESCONOCIDO;
    private int emisorNumeroInterior = DESCONOCIDO;
    private int emisorPais = DESCONOCIDO;
    private int emisorReferencia = DESCONOCIDO;

    ///-----------------------------------------------------------------------------------------------------------------
    /// DATOS Receptor
    ///-----------------------------------------------------------------------------------------------------------------
    private int receptorNumRegIdTributario = DESCONOCIDO;
    private int receptorCalle = DESCONOCIDO;
    private int receptorCodigoPostal = DESCONOCIDO;
    private int receptorEstado = DESCONOCIDO;
    private int receptorMunicipio = DESCONOCIDO;
    private int receptorPais = DESCONOCIDO;
    private int receptorNumeroExterior = DESCONOCIDO;
    private int receptorNumeroInterior = DESCONOCIDO;


    ///-----------------------------------------------------------------------------------------------------------------
    /// DATOS Propietario
    ///-----------------------------------------------------------------------------------------------------------------
    private int propietarioNumRegIdTrib = DESCONOCIDO;
    private int propietarioResidenciaFiscal = DESCONOCIDO;

    ///-----------------------------------------------------------------------------------------------------------------
    /// DATOS Destinatario
    ///-----------------------------------------------------------------------------------------------------------------
    private int destinatarioNumRegIdTrib = DESCONOCIDO;
    private int destinatarioNombre = DESCONOCIDO;
    private int destinatarioCalle = DESCONOCIDO;
    private int destinatarioColonia = DESCONOCIDO;
    private int destinatarioCodigoPostal = DESCONOCIDO;
    private int destinatarioEstado = DESCONOCIDO;
    private int destinatarioMunicipio = DESCONOCIDO;
    private int destinatarioPais = DESCONOCIDO;





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


    public int getDESCONOCIDO() {
        return this.DESCONOCIDO;
    }

    public int getNO_REQUERIDO() {
        return this.NO_REQUERIDO;
    }

    public int getREQUERIDO() {
        return this.REQUERIDO;
    }

    public int getOPCIONAL() {
        return this.OPCIONAL;
    }

    public int getMotivoTraslado() {
        return this.motivoTraslado;
    }

    public int getTipoOperacion() {
        return this.tipoOperacion;
    }

    public int getClavePedimento() {
        return this.clavePedimento;
    }

    public int getCertificadoOrigen() {
        return this.certificadoOrigen;
    }

    public int getNumCertificadoOrigen() {
        return this.numCertificadoOrigen;
    }

    public int getNumExportadorConfiable() {
        return this.numExportadorConfiable;
    }

    public int getIncoterm() {
        return this.incoterm;
    }

    public int getSubdivision() {
        return this.subdivision;
    }

    public int getObservaciones() {
        return this.observaciones;
    }

    public int getTipoCambioUSD() {
        return this.tipoCambioUSD;
    }

    public int getTotalUSD() {
        return this.totalUSD;
    }

    public int getEmisorCurp() {
        return this.emisorCurp;
    }

    public int getEmisorCalle() {
        return this.emisorCalle;
    }

    public int getEmisorCodigoPostal() {
        return this.emisorCodigoPostal;
    }

    public int getEmisorColonia() {
        return this.emisorColonia;
    }

    public int getEmisorEstado() {
        return this.emisorEstado;
    }

    public int getEmisorMunicipio() {
        return this.emisorMunicipio;
    }

    public int getEmisorNumeroExterior() {
        return this.emisorNumeroExterior;
    }

    public int getEmisorNumeroInterior() {
        return this.emisorNumeroInterior;
    }

    public int getEmisorPais() {
        return this.emisorPais;
    }

    public int getEmisorReferencia() {
        return this.emisorReferencia;
    }

    public int getReceptorNumRegIdTributario() {
        return this.receptorNumRegIdTributario;
    }

    public int getReceptorCalle() {
        return this.receptorCalle;
    }

    public int getReceptorCodigoPostal() {
        return this.receptorCodigoPostal;
    }

    public int getReceptorEstado() {
        return this.receptorEstado;
    }

    public int getReceptorMunicipio() {
        return this.receptorMunicipio;
    }

    public int getReceptorPais() {
        return this.receptorPais;
    }

    public int getReceptorNumeroExterior() {
        return this.receptorNumeroExterior;
    }

    public int getReceptorNumeroInterior() {
        return this.receptorNumeroInterior;
    }

    public int getPropietarioNumRegIdTrib() {
        return this.propietarioNumRegIdTrib;
    }

    public int getPropietarioResidenciaFiscal() {
        return this.propietarioResidenciaFiscal;
    }

    public int getDestinatarioNumRegIdTrib() {
        return this.destinatarioNumRegIdTrib;
    }

    public int getDestinatarioNombre() {
        return this.destinatarioNombre;
    }

    public int getDestinatarioCalle() {
        return this.destinatarioCalle;
    }

    public int getDestinatarioColonia() {
        return this.destinatarioColonia;
    }

    public int getDestinatarioCodigoPostal() {
        return this.destinatarioCodigoPostal;
    }

    public int getDestinatarioEstado() {
        return this.destinatarioEstado;
    }

    public int getDestinatarioMunicipio() {
        return this.destinatarioMunicipio;
    }

    public int getDestinatarioPais() {
        return this.destinatarioPais;
    }

    public void setMotivoTraslado(int motivoTraslado) {
        this.motivoTraslado = motivoTraslado;
    }

    public void setTipoOperacion(int tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    public void setClavePedimento(int clavePedimento) {
        this.clavePedimento = clavePedimento;
    }

    public void setCertificadoOrigen(int certificadoOrigen) {
        this.certificadoOrigen = certificadoOrigen;
    }

    public void setNumCertificadoOrigen(int numCertificadoOrigen) {
        this.numCertificadoOrigen = numCertificadoOrigen;
    }

    public void setNumExportadorConfiable(int numExportadorConfiable) {
        this.numExportadorConfiable = numExportadorConfiable;
    }

    public void setIncoterm(int incoterm) {
        this.incoterm = incoterm;
    }

    public void setSubdivision(int subdivision) {
        this.subdivision = subdivision;
    }

    public void setObservaciones(int observaciones) {
        this.observaciones = observaciones;
    }

    public void setTipoCambioUSD(int tipoCambioUSD) {
        this.tipoCambioUSD = tipoCambioUSD;
    }

    public void setTotalUSD(int totalUSD) {
        this.totalUSD = totalUSD;
    }

    public void setEmisorCurp(int emisorCurp) {
        this.emisorCurp = emisorCurp;
    }

    public void setEmisorCalle(int emisorCalle) {
        this.emisorCalle = emisorCalle;
    }

    public void setEmisorCodigoPostal(int emisorCodigoPostal) {
        this.emisorCodigoPostal = emisorCodigoPostal;
    }

    public void setEmisorColonia(int emisorColonia) {
        this.emisorColonia = emisorColonia;
    }

    public void setEmisorEstado(int emisorEstado) {
        this.emisorEstado = emisorEstado;
    }

    public void setEmisorMunicipio(int emisorMunicipio) {
        this.emisorMunicipio = emisorMunicipio;
    }

    public void setEmisorNumeroExterior(int emisorNumeroExterior) {
        this.emisorNumeroExterior = emisorNumeroExterior;
    }

    public void setEmisorNumeroInterior(int emisorNumeroInterior) {
        this.emisorNumeroInterior = emisorNumeroInterior;
    }

    public void setEmisorPais(int emisorPais) {
        this.emisorPais = emisorPais;
    }

    public void setEmisorReferencia(int emisorReferencia) {
        this.emisorReferencia = emisorReferencia;
    }

    public void setReceptorNumRegIdTributario(int receptorNumRegIdTributario) {
        this.receptorNumRegIdTributario = receptorNumRegIdTributario;
    }

    public void setReceptorCalle(int receptorCalle) {
        this.receptorCalle = receptorCalle;
    }

    public void setReceptorCodigoPostal(int receptorCodigoPostal) {
        this.receptorCodigoPostal = receptorCodigoPostal;
    }

    public void setReceptorEstado(int receptorEstado) {
        this.receptorEstado = receptorEstado;
    }

    public void setReceptorMunicipio(int receptorMunicipio) {
        this.receptorMunicipio = receptorMunicipio;
    }

    public void setReceptorPais(int receptorPais) {
        this.receptorPais = receptorPais;
    }

    public void setReceptorNumeroExterior(int receptorNumeroExterior) {
        this.receptorNumeroExterior = receptorNumeroExterior;
    }

    public void setReceptorNumeroInterior(int receptorNumeroInterior) {
        this.receptorNumeroInterior = receptorNumeroInterior;
    }

    public void setPropietarioNumRegIdTrib(int propietarioNumRegIdTrib) {
        this.propietarioNumRegIdTrib = propietarioNumRegIdTrib;
    }

    public void setPropietarioResidenciaFiscal(int propietarioResidenciaFiscal) {
        this.propietarioResidenciaFiscal = propietarioResidenciaFiscal;
    }

    public void setDestinatarioNumRegIdTrib(int destinatarioNumRegIdTrib) {
        this.destinatarioNumRegIdTrib = destinatarioNumRegIdTrib;
    }

    public void setDestinatarioNombre(int destinatarioNombre) {
        this.destinatarioNombre = destinatarioNombre;
    }

    public void setDestinatarioCalle(int destinatarioCalle) {
        this.destinatarioCalle = destinatarioCalle;
    }

    public void setDestinatarioColonia(int destinatarioColonia) {
        this.destinatarioColonia = destinatarioColonia;
    }

    public void setDestinatarioCodigoPostal(int destinatarioCodigoPostal) {
        this.destinatarioCodigoPostal = destinatarioCodigoPostal;
    }

    public void setDestinatarioEstado(int destinatarioEstado) {
        this.destinatarioEstado = destinatarioEstado;
    }

    public void setDestinatarioMunicipio(int destinatarioMunicipio) {
        this.destinatarioMunicipio = destinatarioMunicipio;
    }

    public void setDestinatarioPais(int destinatarioPais) {
        this.destinatarioPais = destinatarioPais;
    }
}
