package fe.db;

import com.ebs.catalogos.TipoImpuesto;
import com.ebs.exceptions.BadImpuestoTypeException;
import fe.model.fmanual.ImpuestoContainer;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ConceptoFactura implements Serializable {

    private int id;
    private String conceptofacturacion;//descripcion
    private String claveconcepto;//noidentificacion
    private String noCuentaPredial;
    private String pedimento = "";
    private double valorUnitario;
    private double cantidad = 1.0;
    private double cantidadAduana = 1.0;
    private double descuento;//
    private String claveProdServ;//Clave
    private String claveUnidad;//Clave
    private String unidad;//unidad de medida
    private double importe = 0.00;//
    private String busquedaProdServ = "";
    private String busquedaUnidad = "";
    private List<ImpuestoContainer> traslados;//Datos de impuestos de traslados
    private List<ImpuestoContainer> retenciones;//Datos de impuestos de retenciones
    private List<String> params;
    private List<Integer> indicesParams;
    private double precioUnitarioAdenda = 0.0;
    private double montoLineaAdenda= 0.0;

    private double tipoCambioUsd = 1.0;
    private boolean usandoComplementoComercioExterior = false;
    String fraccionArancelaria;
    String unidadAduana;
    double valorUnitarioAduana;
    double valorDolares;
    ///Comercio exterior descripcion especifica del concepto
    String marca        ;
    String modelo       ;
    String subModelo    ;
    String numeroSerie  ;


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

        this.importe = (valorUnitario*cantidad);
        calcularTraslados();
        calcularRetenciones();
    }

    public void calcularMontoComercioExterior(Double tipoCambio){

        this.valorDolares = (importe * tipoCambio) / tipoCambioUsd;

    }

    public void calcularValorUnitarioAduana(){

        if(cantidadAduana > 0){
            DecimalFormat df = new DecimalFormat("#.000000");
            this.valorUnitarioAduana = (valorUnitario*cantidad) / cantidadAduana;
            String number = df.format(valorUnitarioAduana);
            this.valorUnitarioAduana = Double.parseDouble(number);
        }



        //this.valorUnitarioAduana = (valorUnitario * tipoCambio) / tipoCambioUsd;
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

    public int getId() {
        return this.id;
    }

    public String getConceptofacturacion() {
        return this.conceptofacturacion;
    }

    public String getClaveconcepto() {
        return this.claveconcepto;
    }

    public String getNoCuentaPredial() {
        return this.noCuentaPredial;
    }

    public String getPedimento() {
        return this.pedimento;
    }

    public double getValorUnitario() {
        return this.valorUnitario;
    }

    public double getCantidad() {
        return this.cantidad;
    }

    public double getCantidadAduana() {
        return this.cantidadAduana;
    }

    public double getDescuento() {
        return this.descuento;
    }

    public String getClaveProdServ() {
        return this.claveProdServ;
    }

    public String getClaveUnidad() {
        return this.claveUnidad;
    }

    public String getUnidad() {
        return this.unidad;
    }

    public double getImporte() {
        return this.importe;
    }

    public String getBusquedaProdServ() {
        return this.busquedaProdServ;
    }

    public String getBusquedaUnidad() {
        return this.busquedaUnidad;
    }

    public List<ImpuestoContainer> getTraslados() {
        return this.traslados;
    }

    public List<ImpuestoContainer> getRetenciones() {
        return this.retenciones;
    }

    public List<String> getParams() {
        return this.params;
    }

    public List<Integer> getIndicesParams() {
        return this.indicesParams;
    }

    public double getPrecioUnitarioAdenda() {
        return this.precioUnitarioAdenda;
    }

    public double getMontoLineaAdenda() {
        return this.montoLineaAdenda;
    }

    public double getTipoCambioUsd() {
        return this.tipoCambioUsd;
    }

    public boolean isUsandoComplementoComercioExterior() {
        return this.usandoComplementoComercioExterior;
    }

    public String getFraccionArancelaria() {
        return this.fraccionArancelaria;
    }

    public String getUnidadAduana() {
        return this.unidadAduana;
    }

    public double getValorUnitarioAduana() {
        return this.valorUnitarioAduana;
    }

    public double getValorDolares() {
        return this.valorDolares;
    }

    public String getMarca() {
        return this.marca;
    }

    public String getModelo() {
        return this.modelo;
    }

    public String getSubModelo() {
        return this.subModelo;
    }

    public String getNumeroSerie() {
        return this.numeroSerie;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setConceptofacturacion(String conceptofacturacion) {
        this.conceptofacturacion = conceptofacturacion;
    }

    public void setClaveconcepto(String claveconcepto) {
        this.claveconcepto = claveconcepto;
    }

    public void setNoCuentaPredial(String noCuentaPredial) {
        this.noCuentaPredial = noCuentaPredial;
    }

    public void setPedimento(String pedimento) {
        this.pedimento = pedimento;
    }

    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public void setCantidadAduana(double cantidadAduana) {
        this.cantidadAduana = cantidadAduana;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public void setClaveProdServ(String claveProdServ) {
        this.claveProdServ = claveProdServ;
    }

    public void setClaveUnidad(String claveUnidad) {
        this.claveUnidad = claveUnidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public void setBusquedaProdServ(String busquedaProdServ) {
        this.busquedaProdServ = busquedaProdServ;
    }

    public void setBusquedaUnidad(String busquedaUnidad) {
        this.busquedaUnidad = busquedaUnidad;
    }

    public void setTraslados(List<ImpuestoContainer> traslados) {
        this.traslados = traslados;
    }

    public void setRetenciones(List<ImpuestoContainer> retenciones) {
        this.retenciones = retenciones;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public void setIndicesParams(List<Integer> indicesParams) {
        this.indicesParams = indicesParams;
    }

    public void setPrecioUnitarioAdenda(double precioUnitarioAdenda) {
        this.precioUnitarioAdenda = precioUnitarioAdenda;
    }

    public void setMontoLineaAdenda(double montoLineaAdenda) {
        this.montoLineaAdenda = montoLineaAdenda;
    }

    public void setTipoCambioUsd(double tipoCambioUsd) {
        this.tipoCambioUsd = tipoCambioUsd;
    }

    public void setUsandoComplementoComercioExterior(boolean usandoComplementoComercioExterior) {
        this.usandoComplementoComercioExterior = usandoComplementoComercioExterior;
    }

    public void setFraccionArancelaria(String fraccionArancelaria) {
        this.fraccionArancelaria = fraccionArancelaria;
    }

    public void setUnidadAduana(String unidadAduana) {
        this.unidadAduana = unidadAduana;
    }

    public void setValorUnitarioAduana(double valorUnitarioAduana) {
        this.valorUnitarioAduana = valorUnitarioAduana;
    }

    public void setValorDolares(double valorDolares) {
        this.valorDolares = valorDolares;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setSubModelo(String subModelo) {
        this.subModelo = subModelo;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }
}
