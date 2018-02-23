package fe.db;
// Generated 26-nov-2015 16:41:11 by Hibernate Tools 4.3.1


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
//import org.apache.commons.lang.StringEscapeUtils;

/**
 * MJars generated by hbm2java
 */
@Entity
@Table(name="m_jars"
    ,catalog="faccorp_apl"
)
public class MJars  implements java.io.Serializable {


     private Integer id;
     private int idEmpresa;
     private String direccionJar;

    public MJars() {
    }

    public MJars(int idEmpresa, String direccionJar) {
       this.idEmpresa = idEmpresa;
       this.direccionJar = direccionJar;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="id", unique=true, nullable=false)
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    
    @Column(name="idEmpresa", nullable=false)
    public int getIdEmpresa() {
        return this.idEmpresa;
    }
    
    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    
    @Column(name="direccionJar", nullable=false, length=50)
    public String getDireccionJar() {
        return this.direccionJar;
    }
    
    public void setDireccionJar(String direccionJar) {
        this.direccionJar = direccionJar;
    }
    
//    public void escapeSqlAndHtmlCharacters(){
//        this.direccionJar = (direccionJar != null) ? StringEscapeUtils.escapeSql(this.direccionJar) : "";
//        this.direccionJar = (direccionJar != null) ? StringEscapeUtils.escapeHtml(this.direccionJar) : "";
//    }



}


