
package mx.com.ebs.emision.factura.catalogos;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import mx.com.ebs.emision.factura.utilierias.PintarLog;
import mx.com.ebs.emision.factura.utilierias.SesionUsuario;
import mx.com.ebs.emision.factura.vo.catalogos.CatalogosBean;
import fe.db.MEmpresa;
import fe.db.MFolios;
import fe.model.dao.EmpresaDAO;
import java.io.Serializable;


public class CatCapturaManual extends Catalogo implements Serializable {

    private static List<MFolios> listSerie;        // lista de  Series
    // Lista catalogos para traslados
    private static List<CatalogosBean> ivaMeta;
    private static List<CatalogosBean> iepsMeta;
    private static List<CatalogosBean> isrMeta;
    //Lista catalogos para retenciones
    private static List<CatalogosBean> ivaMetaRet;
    private static List<CatalogosBean> isrMetaRet;

    @Getter
    private static CatalogosBean ivaCatSingle;
    @Getter
    private static CatalogosBean iepsCatSingle;
    @Getter
    private static CatalogosBean isrCatSingle;

    private List<MEmpresa> listEmisor;       // lista de Emisores tomados de la base de datos

    public CatCapturaManual() {

        //TODO generar lista con la informacion que se encuentra en la base de datos
        ivaMeta = new ArrayList<CatalogosBean>();
        ivaMeta.add(generaElementoLista("002", "IVA"));
        iepsMeta = new ArrayList<CatalogosBean>();
        iepsMeta.add(generaElementoLista("003", "IEPS"));
        isrMeta = new ArrayList<CatalogosBean>();
        isrMeta.add(generaElementoLista("001", "ISR"));

        ivaMetaRet = new ArrayList<>();
        ivaMetaRet.add(new CatalogosBean("002","IVA"));
        isrMetaRet = new ArrayList<>();
        isrMetaRet.add(new CatalogosBean("001","ISR"));

        ivaCatSingle = new CatalogosBean("002", "IVA");
        iepsCatSingle = new CatalogosBean("003", "IEPS");
        isrCatSingle = new CatalogosBean("001", "ISR");

        //Cargando Catalogo de empresas 
        listEmisor = new ArrayList<MEmpresa>();
        if (listSerie == null) {
            listSerie = new ArrayList<MFolios>();
        }

        setListEmisor(consultaCatalogosEmpresasAsociadas());
        setIvaMeta(ivaMeta);

    }

    /**
     * Metodo encargado de obtener la lista de empresas que puede acceder el
     * usuario entrante
     */
    public List<MEmpresa> consultaCatalogosEmpresasAsociadas() {

        SesionUsuario sesionUsuario = new SesionUsuario();
        EmpresaDAO DAOEmp = new EmpresaDAO();
        try {
            return DAOEmp.ListaEmpresasPadres(sesionUsuario.getMAcceso().getId());
        } catch (Exception e) {
            PintarLog.println("Error al consultar las empresas del perfil----: " + e);
            return null;
        }

    }

    public List<MFolios> getListSerie() {
        return listSerie;
    }

    public void setListSerie(List<MFolios> listSerie) {
        this.listSerie = listSerie;
    }

    public List<CatalogosBean> getIvaMeta() {
        return ivaMeta;
    }

    public void setIvaMeta(List<CatalogosBean> ivaMeta) {
        this.ivaMeta = ivaMeta;
    }

    public List<MEmpresa> getListEmisor() {
        return listEmisor;
    }

    public void setListEmisor(List<MEmpresa> listEmisor) {
        this.listEmisor = listEmisor;
    }

    public List<CatalogosBean> getIepsMeta() {
        return iepsMeta;
    }

    public void setIepsMeta(List<CatalogosBean> iepsMeta) {
        CatCapturaManual.iepsMeta = iepsMeta;
    }

    public static List<CatalogosBean> getIsrMeta() {
        return isrMeta;
    }

    public void setIsrMeta(List<CatalogosBean> isrMeta) {
        CatCapturaManual.isrMeta = isrMeta;
    }

    public List<CatalogosBean> getIvaMetaRet() {
        return ivaMetaRet;
    }

    public void setIvaMetaRet(List<CatalogosBean> ivaMetaRet) {
        CatCapturaManual.ivaMetaRet = ivaMetaRet;
    }

    public List<CatalogosBean> getIsrMetaRet() {
        return isrMetaRet;
    }

    public void setIsrMetaRet(List<CatalogosBean> isrMetaRet) {
        CatCapturaManual.isrMetaRet = isrMetaRet;
    }
}
