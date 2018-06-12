/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ebs.mbeans;

import fe.db.*;
import fe.model.dao.*;
import mx.com.ebs.emision.factura.vo.catalogos.CatalogosBean;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Eduardo C. Flores Ambrosio <EB&S>s
 */
public class ManagedBeanCatalogos implements Serializable {

    private List<MCaduana> aduanas;
    private List<MCcodigopostal> cps;

    //tassa de TrasladadosRetenciones
    private List<MImpuestos> ivaTasasTraslados;
    private List<MImpuestos> iepsTasasTraslados;
    private List<MImpuestos> ivaTasasRetenciones;
    private List<MImpuestos> isrTasasRetenciones;

    /*Impuestos retenidos
    private List<CatalogosBean> ivaTasasRets;
    private List<CatalogosBean> isrTasasRets;
    */
    private List<CatalogosBean> factoresRets;
    private List<CatalogosBean> factoresIsrRet;
    private List<CatalogosBean> factoresAll;

    private List<MCformapago> cfs;
    private List<MCimpuesto> imps;
    private List<MCmetodoPago> mps;
    private List<MCmoneda> monedas;
    private List<MCpais> paises;
    private List<MCpatentesAduanales> patentes;
    private List<MCpedimentoAduana> pedimentos;
    private List<MCprodserv> productos;
    private List<MCregimenFiscal> regims;
    private List<MCtipoComprobante> comprobantes;

    private List<MCtipoRelacionCfdi> relaciones;
    private List<MCunidades> unidades;
    private List<MCusoComprobantes> usos;

    private FormaPagoDAO daoForma;
    private MetodoPagoDAO daoMetodo;
    private AduanaDAO daoAduana;
    private CodigoPostalDAO daoCp;
    private ImpuestoDAO daoImpuestos;
    private MonedaDAO daoMoneda;
    private PaisDAO daoPais;
    private PatenteAduanalDAO daoPatentes;
    private PedimentosDAO daoPedimentos;
    private ProductosDAO daoProductos;
    private RegimenDAO daoRegimen;
    private TasasDAO daoTasas;
    private TipoComprobanteDAO daoTipoComproba;
    private TipoFactorDAO daoTipoFactor;
    private TipoRelacionDAO daoTipoRelacion;
    private ClaveUnidadDAO daoUnidades;
    private UsoCfdiDAO daoUsoCfdi;
    private EmpresaDAO daoEmp;
    private ReceptorDAO daoRec;
    private ImpuestosFactManDao daoImpFactMan;

    private String descProdServ;
    private String descUnidad;
    private String descCp;

    /**
     * Creates a new instance of ManagedBeanCatalogos
     */
    public ManagedBeanCatalogos() {

    }

    @PostConstruct
    public void init() {
        daoForma = new FormaPagoDAO();
        daoMetodo = new MetodoPagoDAO();
        daoMoneda = new MonedaDAO();
        daoUnidades = new ClaveUnidadDAO();
        daoProductos = new ProductosDAO();
        daoTipoComproba = new TipoComprobanteDAO();
        daoTipoRelacion = new TipoRelacionDAO();
        daoCp = new CodigoPostalDAO();
        daoTasas = new TasasDAO();
        daoEmp = new EmpresaDAO();
        daoRec = new ReceptorDAO();
        /**

         daoAduana = new AduanaDAO();
         daoCp = new CodigoPostalDAO();
         daoImpuestos = new ImpuestoDAO();

         daoPais = new PaisDAO();
         daoPatentes = new PatenteAduanalDAO();
         daoPedimentos = new PedimentosDAO();
         daoProductos = new ProductosDAO();
         daoUnidades = new ClaveUnidadDAO();*/
        daoRegimen = new RegimenDAO();
        daoTipoFactor = new TipoFactorDAO();
        daoUsoCfdi = new UsoCfdiDAO();

        cfs = daoForma.getAll();
        mps = daoMetodo.getAll();
        comprobantes = daoTipoComproba.getAll();
        relaciones = daoTipoRelacion.getAll();
        regims = daoRegimen.getAll();
        monedas = daoMoneda.getAll();
        usos = daoUsoCfdi.getAll();
        daoImpFactMan = new ImpuestosFactManDao();
        /*
        
        aduanas = daoAduana.getAll();
        cps = daoCp.getAll();
        imps = daoImpuestos.getAll();
        
        paises = daoPais.getAll();
        patentes =daoPatentes.getAll();
        pedimentos = daoPedimentos.getAll();
        productos = daoProductos.getAll();
        comprobantes = daoTipoComproba.getAll();
        factoresAll = daoTipoFactor.getAll();
        unidades = daoUnidades.getAll();
*/


        ivaTasasTraslados = new ArrayList<>();
        iepsTasasTraslados = new ArrayList<>();
        ivaTasasRetenciones = new ArrayList<>();
        isrTasasRetenciones = new ArrayList<>();



        // TODO: 18/09/2017 Reemplazar los siguientes datos por un parametro o alguna tabla para poder configurarlos mas tarde
        factoresAll = new ArrayList<>();
        factoresAll.add(new CatalogosBean("Tasa","Tasa"));
        factoresAll.add(new CatalogosBean("NA","NA"));
        factoresAll.add(new CatalogosBean("Exento","Exento"));
        factoresAll.add(new CatalogosBean("Cuota","Cuota"));

        factoresRets = new ArrayList<>();
        // TODO: 18/09/2017 REEMPLAZAR LOS SIGUIENTES CATALOGOS CON LOS DATOS GUARDADOS EN LA BASE DE DATOS
        factoresRets.add(new CatalogosBean("Tasa", "Tasa"));
        factoresRets.add(new CatalogosBean("NA", "NA"));
        factoresRets.add(new CatalogosBean("Cuota", "Cuota"));

        factoresIsrRet = new ArrayList<>();
        factoresIsrRet.add(new CatalogosBean("Exento", "Exento"));
        factoresIsrRet.add(new CatalogosBean("Tasa", "Tasa"));
        factoresIsrRet.add(new CatalogosBean("NA", "NA"));
    }

    /**
     * Carga informacion usando los parametros de busqueda guardado en cada concepto
     *
     * @param busqPS
     * @param busqUnidad
     */
    public void buscarInfoProdUnidadForCptoChanges(String busqPS, String busqUnidad) {
        resetDatosBusqueda();
        this.descProdServ = busqPS;//In this case the order is important
        this.descUnidad = busqUnidad;
        buscarProductos();
        buscarUnidades();
    }

    public void buscarRegimenByRfc(int idEmisor) {
        //
        MEmpresa mywmp = daoEmp.BuscarEmpresaId(idEmisor);
        String rfcEmisor = mywmp.getRfcOrigen();
        if (rfcEmisor != null && !rfcEmisor.isEmpty() && rfcEmisor.length() == 13) {
            regims = daoRegimen.getRegimenFisicas();
        } else if (rfcEmisor != null && !rfcEmisor.isEmpty() && rfcEmisor.length() == 12) {
            regims = daoRegimen.getRegimenMoral();
        } else {
            regims = daoRegimen.getAll();
        }
    }

    public void buscarUsoCfdiByRfc(int idReceptor) {
        MReceptor mywmp = daoRec.BuscarReceptorIdr(idReceptor);
        String rfcReceptor = mywmp.getRfcOrigen();
        if (rfcReceptor != null && !rfcReceptor.isEmpty() && rfcReceptor.length() == 13) {
            usos = daoUsoCfdi.getForFisicas();
        } else if (rfcReceptor != null && !rfcReceptor.isEmpty() && rfcReceptor.length() == 12) {
            usos = daoUsoCfdi.getForMorales();
        } else {
            usos = daoUsoCfdi.getAll();
        }
    }

    public void resetDatosBusqueda() {

        descCp = "";
        descProdServ = "";
        descUnidad = "";

        if (productos == null) {
            productos = new ArrayList<MCprodserv>();
        } else {
            productos.clear();
        }

        if (unidades == null) {
            unidades = new ArrayList<MCunidades>();
        } else {
            unidades.clear();
        }
        if (ivaTasasTraslados != null) {
            ivaTasasTraslados.clear();
        }
        if (iepsTasasTraslados != null) {
            iepsTasasTraslados.clear();
        }
        if (ivaTasasRetenciones != null) {
            ivaTasasRetenciones.clear();
        }
        if (isrTasasRetenciones != null) {
            isrTasasRetenciones.clear();
        }
    }


    // TODO: 18/09/2017 AJUSTAR LOS DOS METODOS SIGUIENTES
    public void buscarIvaTasasPorFactorTras(String factor) {
        if (factor.toUpperCase().contains("EXENTO") || factor.contains("NA")) {
            ivaTasasTraslados = new ArrayList<>();
        } else {
            ivaTasasTraslados = daoImpFactMan.getImpuestosTrasladosByImpuestoAndTipoFactor("002", factor);
        }
    }

    public void buscarIepsTasasPorFactorTras(String factor) {
        if (factor.toUpperCase().contains("EXENTO") || factor.contains("NA")) {
            iepsTasasTraslados = new ArrayList<>();
        } else {
            iepsTasasTraslados = daoImpFactMan.getImpuestosTrasladosByImpuestoAndTipoFactor("003", factor);
        }
    }


    public void buscarTasasIvaRetenciones(String factor){
        if (factor.toUpperCase().contains("EXENTO") || factor.contains("NA")) {
            ivaTasasRetenciones = new ArrayList<>();
        } else {
            ivaTasasRetenciones = daoImpFactMan.getImpuestosRetenidosByImpuestoAndTipoFactor("002",  factor);
        }
    }


    public void buscarTasasIsrRetenciones(String factor){
        if (factor.toUpperCase().contains("EXENTO") || factor.contains("NA")) {
            isrTasasRetenciones = new ArrayList<>();
        } else {
            isrTasasRetenciones = daoImpFactMan.getImpuestosRetenidosByImpuestoAndTipoFactor("001", factor);
        }
    }

    public void buscarCodigoPostal(String arg) {
        if (arg != null && !arg.isEmpty()) {
            descCp = arg.split("-")[0];
        } else {
            descCp = "";
        }
        if (!descCp.isEmpty()) {
            cps = daoCp.get50(descCp);
        } else {
            cps = new ArrayList<>();
        }
    }

    public void buscarProductos() {
        productos = daoProductos.getLimitedRows(descProdServ);
    }

    public void buscarUnidades() {
        unidades = daoUnidades.getLImitRows(descUnidad);
    }

    public List<MCaduana> getAduanas() {
        return aduanas;
    }

    public List<MCcodigopostal> getCps() {
        return cps;
    }

    public List<MCformapago> getCfs() {
        return cfs;
    }

    public List<MCimpuesto> getImps() {
        return imps;
    }

    public List<MCmetodoPago> getMps() {
        return mps;
    }

    public List<MCmoneda> getMonedas() {
        return monedas;
    }

    public List<MCpais> getPaises() {
        return paises;
    }

    public List<MCpatentesAduanales> getPatentes() {
        return patentes;
    }

    public List<MCpedimentoAduana> getPedimentos() {
        return pedimentos;
    }

    public List<MCprodserv> getProductos() {
        return productos;
    }

    public List<MCregimenFiscal> getRegims() {
        return regims;
    }

    public List<MCtipoComprobante> getComprobantes() {
        return comprobantes;
    }

    public List<MCtipoRelacionCfdi> getRelaciones() {
        return relaciones;
    }

    public List<MCunidades> getUnidades() {
        return unidades;
    }

    public List<MCusoComprobantes> getUsos() {
        return usos;
    }

    public String getDescProdServ() {
        return descProdServ;
    }

    public void setDescProdServ(String descProdServ) {
        this.descProdServ = descProdServ;
    }

    public String getDescUnidad() {
        return descUnidad;
    }

    public void setDescUnidad(String descUnidad) {
        this.descUnidad = descUnidad;
    }

    public String getDescCp() {
        return descCp;
    }

    public void setDescCp(String descCp) {
        this.descCp = descCp;
    }

    public List<MImpuestos> getIvaTasasTraslados() {
        return this.ivaTasasTraslados;
    }

    public List<MImpuestos> getIepsTasasTraslados() {
        return this.iepsTasasTraslados;
    }

    public List<MImpuestos> getIvaTasasRetenciones() {
        return this.ivaTasasRetenciones;
    }

    public List<MImpuestos> getIsrTasasRetenciones() {
        return this.isrTasasRetenciones;
    }

    public List<CatalogosBean> getFactoresRets() {
        return this.factoresRets;
    }

    public List<CatalogosBean> getFactoresIsrRet() {
        return this.factoresIsrRet;
    }

    public List<CatalogosBean> getFactoresAll() {
        return this.factoresAll;
    }
}
