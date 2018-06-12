package fe.db;

import javax.persistence.*;
import java.io.Serializable;
//import org.apache.commons.lang.StringEscapeUtils;

@Entity
@Table(name = "M_SERVICIO", catalog = "FACCORP_APL")
public class MServicio implements Serializable
{
    private Integer id;
    private MPlantilla MPlantilla;
    private String modulo;
    
    public MServicio() {
    }
    
    public MServicio(final String modulo) {
        this.modulo = modulo;
    }
    
    public MServicio(final MPlantilla MPlantilla, final String modulo) {
        this.MPlantilla = MPlantilla;
        this.modulo = modulo;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plantilla_ID")
    public MPlantilla getMPlantilla() {
        return this.MPlantilla;
    }
    
    public void setMPlantilla(final MPlantilla MPlantilla) {
        this.MPlantilla = MPlantilla;
    }
    
    @Column(name = "Modulo", nullable = false, length = 50)
    public String getModulo() {
        return this.modulo;
    }
    
    public void setModulo(final String modulo) {
        this.modulo = modulo;
    }
    
//    public void escapeSqlAndHtmlCharacters(){
//        this.modulo = (this.modulo != null)?StringEscapeUtils.escapeSql(this.modulo) : "";
//        this.modulo = (this.modulo != null)?StringEscapeUtils.escapeHtml(this.modulo) : "";
//    }
}
