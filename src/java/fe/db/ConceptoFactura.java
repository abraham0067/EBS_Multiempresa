package fe.db;

import com.ebs.catalogos.TipoImpuesto;
import com.ebs.exceptions.BadImpuestoTypeException;
import fe.model.fmanual.ImpuestoContainer;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ConceptoFactura implements Serializable {

    @Getter
    @Setter
    private int id;
    @Getter
    @Setter
    private String conceptofacturacion;//descripcion
    @Getter
    @Setter
    private String claveconcepto;//noidentificacion
    @Getter
    @Setter
    private String noCuentaPredial;
    @Getter
    @Setter
    private String pedimento = "";
    @Getter
    @Setter
    private double valorUnitario = 0.0;
    @Getter
    @Setter
    private double cantidad = 1.0;
    @Getter
    @Setter
    private double descuento;//
    @Getter
    @Setter
    private String claveProdServ;//Clave
    @Getter
    @Setter
    private String claveUnidad;//Clave
    @Getter
    @Setter
    private String unidad;//unidad de medida
    @Getter
    @Setter
    private double importe = 0.0;//
    @Getter
    @Setter
    private String busquedaProdServ = "";
    @Getter
    @Setter
    private String busquedaUnidad = "";
    @Getter
    @Setter
    private List<ImpuestoContainer> traslados;//Datos de impuestos de traslados
    @Getter
    @Setter
    private List<ImpuestoContainer> retenciones;//Datos de impuestos de retenciones
    @Getter
    @Setter
    private List<String> params;
    @Getter
    @Setter
    private List<Integer> indicesParams;
    @Getter
    @Setter
    private double precioUnitarioAdenda = 0.0;
    @Getter
    @Setter
    private double montoLineaAdenda= 0.0;

    @Getter
    @Setter
    private double tipoCambioUsd = 1.0;
    @Getter @Setter private boolean usandoComplementoComercioExterior = false;
    @Getter @Setter  String fraccionArancelaria;
    @Getter @Setter  String unidadAduana;
    @Getter @Setter  double valorUnitarioAduana;
    @Getter @Setter  double valorDolares;
    ///Comercio exterior descripcion especifica del concepto
    @Getter @Setter  String marca        ;
    @Getter @Setter  String modelo       ;
    @Getter @Setter  String subModelo    ;
    @Getter @Setter  String numeroSerie  ;


    DecimalFormat decf = new DecimalFormat("00.00");
    Locale locMEX = new Locale("sp_MX");
    NumberFormat nf = NumberFormat.getInstance(locMEX);

    public ConceptoFactura(int id) {
        this.id = id;
        traslados = new ArrayList<>();
        retenciones = new ArrayList<>();
        decf.setDecimalFormatSymbols(new DecimalFormatSymbols(locMEX));
        params = new ArrayList<>();
        indicesParams = new ArrayList<>();
        nf.setRoundingMode(RoundingMode.HALF_UP);//.5-.9 to up and -.0-.4 to down
    }

    /**
     * @param id
     * @param conceptofacturacion
     * @param claveconcepto
     * @param valorUnitario
     * @param cantidad
     * @param descuento
     * @param claveProdServ       La cadena debe de estar conformada por  <clave>-<descripcion> del producto o servicio
     * @param claveUnidad         La cadena debe de estar conformada por  <clave>-<descripcion> de la unidad de medida
     * @param unidad
     * @param importe
     */
    public ConceptoFactura(int id, String conceptofacturacion, String claveconcepto, double valorUnitario, double cantidad, double descuento, String claveProdServ, String claveUnidad, String unidad, double importe) {
        this.conceptofacturacion = conceptofacturacion;
        this.claveconcepto = claveconcepto;
        this.valorUnitario = valorUnitario;
        this.cantidad = cantidad;
        this.descuento = descuento;
        this.claveProdServ = claveProdServ;
        this.claveUnidad = claveUnidad;
        this.unidad = unidad;
        this.importe = importe;
        traslados = new ArrayList<>();
        retenciones = new ArrayList<>();
        decf.setDecimalFormatSymbols(new DecimalFormatSymbols(locMEX));
    }

    /**
     * Limpia impuestos del concepto
     */
    public void clearImpuestos() {
        if (traslados != null) {
            traslados.clear();
        } else {
            traslados = new ArrayList<>();
        }

        if (retenciones != null) {
            retenciones.clear();
        } else {
            retenciones = new ArrayList<>();
        }
    }

    public String getFactorIvaTras() {
        String res = "";
        for (ImpuestoContainer ttrs : traslados) {
            if (ttrs.getImpuesto().contains("002")) {
                res = ttrs.getTipoFactor();
                break;
            }
        }
        return res;
    }

    public String getFactorIvaRets() {
        String res = "";
        for (ImpuestoContainer ttrs : retenciones) {
            if (ttrs.getImpuesto().contains("002")) {
                res = ttrs.getTipoFactor();
                break;
            }
        }
        return res;
    }


    public String getFactorIsrRets() {
        String res = "";
        for (ImpuestoContainer ttrs : retenciones) {
            if (ttrs.getImpuesto().contains("001")) {
                res = ttrs.getTipoFactor();
                break;
            }
        }
        return res;

    }

    public String getFactorIepsTras() {
        String res = "";
        for (ImpuestoContainer ttrs : traslados) {
            if (ttrs.getImpuesto().contains("003")) {
                res = ttrs.getTipoFactor();
                break;
            }
        }
        return res;
    }

    public void agregarImpTraslado(ImpuestoContainer arg) throws BadImpuestoTypeException {
        if(arg.getTipo() == TipoImpuesto.TRASLADO){
            traslados.add(arg);
        } else{
            throw new BadImpuestoTypeException();
        }

    }

    public void agregarImpRetencion(ImpuestoContainer arg) throws BadImpuestoTypeException {
        if(arg.getTipo() == TipoImpuesto.RETENCION){
            retenciones.add(arg);
        } else {
            throw new BadImpuestoTypeException();
        }

    }

    public void calcularMontos() {
        importe = valorUnitario * cantidad;
        calcularTraslados();
        calcularRetenciones();
    }

    public void calcularMontoComercioExterior(Double tipoCambio){

        this.valorDolares = (importe * tipoCambio) / tipoCambioUsd;
        System.out.println("Montos: " + this.valorDolares);
    }

    public void calcularValorUnitarioAduana(Double tipoCambio){
        this.valorUnitarioAduana = (valorUnitario * tipoCambio) / tipoCambioUsd;
        System.out.println("Montos aduana: " + this.valorUnitarioAduana);
    }


    private void calcularTraslados() {
        for (ImpuestoContainer imp : traslados) {
            //La base es igual al importe menos el descuento, el descuento no puede ser negativo y debe ser menor que el importe
            imp.setBase(String.valueOf(importe- descuento));
            imp.calcularImpuesto();
        }
    }

    private void calcularRetenciones() {
        for (ImpuestoContainer imp : retenciones) {
            imp.setBase(String.valueOf(importe - descuento));
            imp.calcularImpuesto();
        }
    }

    public void Reset() {
        this.valorUnitario = 0.0;
        this.cantidad = 1.0;
        this.importe = 0.0;
        this.tipoCambioUsd = 1.0;
        this.valorDolares = 0.0;

        decf = new DecimalFormat("00.00");
        locMEX = new Locale("sp_MX");
        nf = NumberFormat.getInstance(locMEX);
        retenciones.clear();
        traslados.clear();
    }

    public double getCantidadFormatted() {
        cantidad = Double.parseDouble(decf.format(cantidad));
        return cantidad;
    }

    public double getTotalFormatted() {
        importe = Double.parseDouble(decf.format(importe));
        return importe;
    }

    public double getValorUnitarioFormatted() {

        valorUnitario = Double.parseDouble(decf.format(valorUnitario));
        return valorUnitario;
    }

    public String TOTAL() {
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        return nf.format(importe);
    }

    public String VALOR() {
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        return nf.format(valorUnitario);
    }

    public String CANT() {
        nf.setMaximumFractionDigits(0);
        nf.setMinimumFractionDigits(0);
        return nf.format(cantidad);
    }

}
