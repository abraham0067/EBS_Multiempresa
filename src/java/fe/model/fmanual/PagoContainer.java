package fe.model.fmanual;

import fe.sat.complementos.v33.PagoData;
import fe.sat.v33.Impuestos;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by eflores on 22/09/2017.
 */
@Data
public class PagoContainer {

    private LocalDate fechaPago;
    private String formaDePagoP;
    private String monedaP;
    private Float tipoCambioP;
    private Double monto;
    private String numOperacion;
    private String rfcEmisorCtaOrd;
    private String nomBancoOrdExt;
    private String ctaOrdenante;
    private String rfcEmisorCtaBen;
    private String ctaBeneficiario;
    private String tipoCadPago;
    private String certPago;
    private String cadPago;
    private String selloPago;
    private List<DocRelacionadoContainer> doctoRelacionado;
    private List<Impuestos> impuestos;

    public PagoContainer() {
        fechaPago = LocalDate.now();
        formaDePagoP  = "";
        monedaP = "";
        tipoCambioP = 1.0f;
        monto = 0.0d;
        numOperacion = "";
        rfcEmisorCtaOrd = "";
        nomBancoOrdExt = "";
        ctaOrdenante = "";
        rfcEmisorCtaBen = "";
        ctaBeneficiario = "";
        tipoCadPago = "";
        certPago = "";
        cadPago = "";
        selloPago = "";
        doctoRelacionado = new ArrayList<>();
        impuestos = new ArrayList<>();
    }
}
