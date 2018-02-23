package fe.model.fmanual;

import fe.db.MCfd;
import fe.db.MParcialidades;
import fe.sat.v33.Catalogo;
import fe.sat.v33.CatalogoData;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by eflores on 17/07/2017.
 */
@Data
public class DocRelacionadoContainer {

    private String idDocumento;
    private String serie;
    private String folio;
    private Catalogo monedaDR;
    private Float tipoCambioDR;
    private Catalogo metodoDePagoDR;
    private Integer numParcialidad;
    private Double impSaldoAnt;
    private Double impPagado;
    private Double impSaldoInsoluto;

    public DocRelacionadoContainer() {
        idDocumento = "";
        serie = "";
        folio = "";
        monedaDR = new CatalogoData("", "");
        tipoCambioDR = 0f;
        metodoDePagoDR = new CatalogoData("", "");
        numParcialidad = 1;
        impSaldoAnt = 0d;
        impPagado = 0d;
        impSaldoInsoluto = 0d;
    }
}
