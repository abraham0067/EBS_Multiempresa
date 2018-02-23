/**
 *
 */
package fe.model.util;

import com.ebs.util.TimeZoneCP;
import fe.db.MEmpresa;
import fe.db.MFolios;
import fe.db.MReceptor;
import fe.sat.*;
import fe.sat.v33.*;
import fe.sat.v33.AdditionalData;
import fe.sat.v33.ComprobanteData;
import fe.sat.v33.DatosComprobanteData;
import fe.sat.v33.EmisorData;
import fe.sat.v33.ReceptorData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Amiranda Modelo de la forma captura Manual
 */
public class CapturaManualModelo implements Serializable{
    private final String RFC_EXTRANJERO = "XEXX010101000";
    public ComprobanteData cargaDatosComprobante(
            ComprobanteData comprobanteData,
            MEmpresa emisor,
            MReceptor receptor,
            String comentarios,
            MFolios folio,
            CatalogoData moneda,
            CatalogoData regimenFiscal,
            CatalogoData usoCfdi,
            boolean esReceptorExtranjero,
            String residenciaFiscal,
            String numRegIdTrib
    ) {
        List<CommentFE> arrComentarios = new ArrayList<CommentFE>();
        CommentFEData comentario = new CommentFEData();
        comentario.setComment(comentarios);
        comentario.setPosition("9999");
        arrComentarios.add(comentario);
        comprobanteData.setComments9999(arrComentarios);
        //================================================================================================================
        //==================================== CARGA DE INFORMACION ADICIONAL ============================================
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setTipoDocumento(folio.getTipoDoc());
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setSerie(folio.getId());
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setMoneda(moneda);
        // ===============================================================================================================================================================================
        // =================== Agragando datos que no se capturan en la pantalla =========================================================================================================		
        // ========================
        // ==============  1---- Datos del Emisor  ===============================================================================================================
        EmisorData emisor12Data = new EmisorData();
        Direccion12Data direccionFiscal12 = new Direccion12Data();
        direccionFiscal12.setCalle(Limpiador.cleanString(emisor.getDireccion().getCalle()));
        direccionFiscal12.setColonia(Limpiador.cleanString(emisor.getDireccion().getColonia()));
        direccionFiscal12.setCP(Limpiador.cleanString(emisor.getDireccion().getCp()));
        direccionFiscal12.setEstado(Limpiador.cleanString(emisor.getDireccion().getEstado()));
        direccionFiscal12.setLocalidad(Limpiador.cleanString(emisor.getDireccion().getLocalidad()));
        direccionFiscal12.setMunicipio(Limpiador.cleanString(emisor.getDireccion().getMunicipio()));
        direccionFiscal12.setNoExterior(Limpiador.cleanString(emisor.getDireccion().getNoExterior()));
        direccionFiscal12.setNoInterior(Limpiador.cleanString(emisor.getDireccion().getNoInterior()));
        direccionFiscal12.setPais(Limpiador.cleanString(emisor.getDireccion().getPais()));
        direccionFiscal12.setReferencia("Ninguna");

        emisor12Data.setRfc(Limpiador.cleanString(emisor.getRfcOrigen()));
        emisor12Data.setRegimenFiscal(regimenFiscal);
        emisor12Data.setNombre(Limpiador.cleanString(emisor.getRazonSocial().replace(".", "")));// <---------------- Se carga el nombre del emisor
        emisor12Data.setDireccionFiscalEmisor(direccionFiscal12);
        comprobanteData.setEmisor(emisor12Data);
        // ===============================================================================================================================================================================

        // ======================================  1---- Datos del Receptor  ===============================================================================================================
        ReceptorData receptor12Data = new ReceptorData();
        Direccion12Data direccionReceptorFiscal12 = new Direccion12Data();
        direccionReceptorFiscal12.setCalle(Limpiador.cleanString(receptor.getDireccion().getCalle()));
        direccionReceptorFiscal12.setColonia(Limpiador.cleanString(receptor.getDireccion().getColonia()));
        direccionReceptorFiscal12.setCP(Limpiador.cleanString(receptor.getDireccion().getCp()));
        direccionReceptorFiscal12.setEstado(Limpiador.cleanString(receptor.getDireccion().getEstado()));
        direccionReceptorFiscal12.setLocalidad(Limpiador.cleanString(receptor.getDireccion().getLocalidad()));
        direccionReceptorFiscal12.setMunicipio(Limpiador.cleanString(receptor.getDireccion().getMunicipio()));
        direccionReceptorFiscal12.setNoExterior(Limpiador.cleanString(receptor.getDireccion().getNoExterior()));
        direccionReceptorFiscal12.setNoInterior(Limpiador.cleanString(receptor.getDireccion().getNoInterior()));
        direccionReceptorFiscal12.setPais(Limpiador.cleanString(receptor.getDireccion().getPais()));
        direccionReceptorFiscal12.setReferencia("Ninguna");
        receptor12Data.setRfc(Limpiador.cleanString(receptor.getRfcOrigen()));
        receptor12Data.setNombre(Limpiador.cleanString(receptor.getRazonSocial().replace(".", "")));// <---------------- Se carga el nombre del emisor
        receptor12Data.setUsoCFDI(usoCfdi);
        receptor12Data.setDireccionReceptor(direccionReceptorFiscal12);

        if (esReceptorExtranjero) {
            System.out.println("HACIENDO MODIFICACIONES AL RFC RECEPTOR EXTRANJERO");
            receptor12Data.setRfc(RFC_EXTRANJERO);
            String[] arrTmp = residenciaFiscal.split("-");
            receptor12Data.setResidenciaFiscal(new CatalogoData(arrTmp[0], arrTmp[1]));
            receptor12Data.setNumRegIdTrib(numRegIdTrib);
        }

        comprobanteData.setReceptor(receptor12Data);
        // ===============================================================================================================================================================================

        // =========================================== Datos dummy para que las validaciones iniciales del proceso de captura manual, no genere errores de datos==========================
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setFolio("1");
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setFecha(
                (new TimeZoneCP().getUTC(
                        comprobanteData.getDatosComprobante().getLugarExpedicion().getClave())
                ));
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setSello("qQSvHZPzRB7eT6f9dJs3GepVl82eq4aOiI4b2hK+YSTcaJXgrDm8GfmZyUMnJ5XLs/TZDtAeafF5W1oyEygqva5fA3Ga5agq9HKHMEZx2qCOgOB+97C26StVKUdGD3jcPCh64AEgXLdCftIPwiRXP6eAr0IeeaujjVdEobvuxyo=");
        ((DatosComprobanteData) comprobanteData.getDatosComprobante()).setNoCertificado("00001000000103168809");//Mas de 20 caracteres
        // ================================================================================================================================================================================

        // =================================================================================================================================================================================
        // ===========================================  Datos adicionales  Orden Interna, Centro Beneficio==================================================================================
        AdditionalData additionalData = new AdditionalData();
        additionalData.setParam(new String[]{"Orden Interna ", "Centro Beneficio"});
        comprobanteData.setAdditional(additionalData);

        return comprobanteData;
    }

}
