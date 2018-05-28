/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebs.model;

import com.ebs.catalogos.TiposComprobante;
import com.ebs.catalogos.TiposDocumento;
import com.ebs.vistas.VistaCfdiOtro;
import com.ebs.vistas.VistaCfdiOtroDao;
import fe.db.MapearCfdArchi;
import fe.model.dao.CfdiDAO;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 * @author Eduardo C. Flores Ambrosio <EB&S>
 */
public class LazyCfdiDataModel extends LazyDataModel<VistaCfdiOtro> {

    private List<VistaCfdiOtro> dataSource;
    private boolean isCliente;
    private boolean isAgente;
    private int idAcceso;
    private Integer[] idsEmpresas;
    private String numeroFactura;
    private String folioErp;
    private String rfc;
    private String serie;
    private String noCliente;
    private List<Integer> clientesAgente;
    private String razonSocial;
    private Date datDesde;//Fecha Inicial
    private Date datHasta;//Fecha Final
    private String cliente;
    private String estatus;
    private String numPolizaSeguro;
    private String UUID;



    public LazyCfdiDataModel() {
        this.setRowCount(0);
    }

    ///Constructor clientes de los clientes
    public LazyCfdiDataModel(boolean isCliente,
                             boolean isAgente,
                             int idAcceso,
                             Integer[] idsEmpresas,
                             String numeroFactura,
                             String folioErp,
                             String rfc,
                             String serie,
                             String noCliente,
                             List<Integer> clientesAgente,
                             String razonSocial,
                             Date datDesde,
                             Date datHasta,
                             String cliente,
                             String numPolizaSeguro,
                             String UUID
    ) {
        this.isCliente = isCliente;
        this.isAgente = isAgente;
        this.idAcceso = idAcceso;
        this.idsEmpresas = idsEmpresas;
        this.numeroFactura = numeroFactura;
        this.folioErp = folioErp;
        this.rfc = rfc;
        this.serie = serie;
        this.noCliente = noCliente;
        this.clientesAgente = clientesAgente;
        this.razonSocial = razonSocial;
        this.datDesde = datDesde;
        this.datHasta = datHasta;
        this.cliente = cliente;
        this.numPolizaSeguro = numPolizaSeguro;
        this.UUID = UUID;
        this.setRowCount(0);
    }

    ///Constructor clientes de EBS
    public LazyCfdiDataModel(boolean isCliente,
                             boolean isAgente,
                             int idAcceso,
                             Integer[] idsEmpresas,
                             String numeroFactura,
                             String folioErp,
                             String rfc,
                             String serie,
                             String noCliente,
                             List<Integer> clientesAgente,
                             String razonSocial,
                             Date datDesde,
                             Date datHasta,
                             String cliente,
                             String estatus,
                             String numPolizaSeguro,
                             String UUID
    ) {
        this.isCliente = isCliente;
        this.isAgente = isAgente;
        this.idAcceso = idAcceso;
        this.idsEmpresas = idsEmpresas;
        this.numeroFactura = numeroFactura;
        this.folioErp = folioErp;
        this.rfc = rfc;
        this.serie = serie;
        this.noCliente = noCliente;
        this.clientesAgente = clientesAgente;
        this.razonSocial = razonSocial;
        this.datDesde = datDesde;
        this.datHasta = datHasta;
        this.cliente = cliente;
        this.estatus = estatus;
        this.numPolizaSeguro = numPolizaSeguro;
        this.UUID = UUID;
        this.setRowCount(0);
    }



    public LazyCfdiDataModel(List<VistaCfdiOtro> dataSource) {
        this.dataSource = dataSource;
    }




    @Override
    public VistaCfdiOtro getRowData(String rowKey) {
        for (VistaCfdiOtro inv : dataSource) {
            if (inv.getId() == Integer.valueOf(rowKey)) {
                return inv;
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(VistaCfdiOtro cfd) {
        return cfd.getId();
    }

    @Override
    public List<VistaCfdiOtro> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        VistaCfdiOtroDao daoCfdi = new VistaCfdiOtroDao();
        //sort
        if (sortField != null) {
            Collections.sort(dataSource, new LazySorterCfdi(sortField, sortOrder));
        }


        if (isCliente) {
            dataSource = daoCfdi.ListaParametrosClientes(
                    idAcceso,
                    isAgente,
                    idsEmpresas,
                    numeroFactura,
                    folioErp,
                    rfc,
                    serie,
                    noCliente,
                    clientesAgente,
                    razonSocial,
                    datDesde,
                    datHasta,
                    numPolizaSeguro,
                    UUID,
                    first,
                    pageSize);
        } else {
            ///Usuarios ebs o de clientes que puedan ver todas las facturas
            dataSource = daoCfdi.ListaParametros(
                    idAcceso,
                    isAgente,
                    idsEmpresas,
                    numeroFactura,
                    folioErp,
                    rfc,
                    serie,
                    noCliente,
                    clientesAgente,
                    razonSocial,
                    datDesde,
                    datHasta,
                    Integer.parseInt(estatus),
                    numPolizaSeguro,
                    UUID,
                    first, pageSize);
        }


        ///rowCount
        int dataSourceSize = daoCfdi.rowCount;
        this.setRowCount(dataSourceSize);

        return dataSource;
    }
}
