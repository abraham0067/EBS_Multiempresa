package fe.db;

import java.io.*;
import java.util.*;
import javax.persistence.*;
//import org.apache.commons.lang.StringEscapeUtils;

@Entity
@Table(name = "M_OTRO_RETENCION", catalog = "FACCORP_APL")
public class MOtroRetencion implements Serializable
{
    private Integer id;
    private McfdRetencion mcfdRetencion;
    private Double dparam0;
    private Double dparam1;
    private Double dparam10;
    private Double dparam11;
    private Double dparam12;
    private Double dparam13;
    private Double dparam14;
    private Double dparam2;
    private Double dparam3;
    private Double dparam4;
    private Double dparam5;
    private Double dparam6;
    private Double dparam7;
    private Double dparam8;
    private Double dparam9;
    private Date fparam0;
    private Date fparam1;
    private Date fparam2;
    private Date fparam3;
    private Date fparam4;
    private Date fparam5;
    private Date fparam6;
    private Date fparam7;
    private Date fparam8;
    private Date fparam9;
    private String param0;
    private String param1;
    private String param10;
    private String param11;
    private String param12;
    private String param13;
    private String param14;
    private String param15;
    private String param16;
    private String param17;
    private String param18;
    private String param19;
    private String param2;
    private String param3;
    private String param4;
    private String param5;
    private String param6;
    private String param7;
    private String param8;
    private String param9;
    
    public MOtroRetencion() {
    }
    
    public MOtroRetencion(final McfdRetencion mcfdRetencion, final Double dparam0, final Double dparam1, final Double dparam10, final Double dparam11, final Double dparam12, final Double dparam13, final Double dparam14, final Double dparam2, final Double dparam3, final Double dparam4, final Double dparam5, final Double dparam6, final Double dparam7, final Double dparam8, final Double dparam9, final Date fparam0, final Date fparam1, final Date fparam2, final Date fparam3, final Date fparam4, final Date fparam5, final Date fparam6, final Date fparam7, final Date fparam8, final Date fparam9, final String param0, final String param1, final String param10, final String param11, final String param12, final String param13, final String param14, final String param15, final String param16, final String param17, final String param18, final String param19, final String param2, final String param3, final String param4, final String param5, final String param6, final String param7, final String param8, final String param9) {
        this.mcfdRetencion = mcfdRetencion;
        this.dparam0 = dparam0;
        this.dparam1 = dparam1;
        this.dparam10 = dparam10;
        this.dparam11 = dparam11;
        this.dparam12 = dparam12;
        this.dparam13 = dparam13;
        this.dparam14 = dparam14;
        this.dparam2 = dparam2;
        this.dparam3 = dparam3;
        this.dparam4 = dparam4;
        this.dparam5 = dparam5;
        this.dparam6 = dparam6;
        this.dparam7 = dparam7;
        this.dparam8 = dparam8;
        this.dparam9 = dparam9;
        this.fparam0 = fparam0;
        this.fparam1 = fparam1;
        this.fparam2 = fparam2;
        this.fparam3 = fparam3;
        this.fparam4 = fparam4;
        this.fparam5 = fparam5;
        this.fparam6 = fparam6;
        this.fparam7 = fparam7;
        this.fparam8 = fparam8;
        this.fparam9 = fparam9;
        this.param0 = param0;
        this.param1 = param1;
        this.param10 = param10;
        this.param11 = param11;
        this.param12 = param12;
        this.param13 = param13;
        this.param14 = param14;
        this.param15 = param15;
        this.param16 = param16;
        this.param17 = param17;
        this.param18 = param18;
        this.param19 = param19;
        this.param2 = param2;
        this.param3 = param3;
        this.param4 = param4;
        this.param5 = param5;
        this.param6 = param6;
        this.param7 = param7;
        this.param8 = param8;
        this.param9 = param9;
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
    @JoinColumn(name = "retencion_ID")
    public McfdRetencion getMcfdRetencion() {
        return this.mcfdRetencion;
    }
    
    public void setMcfdRetencion(final McfdRetencion mcfdRetencion) {
        this.mcfdRetencion = mcfdRetencion;
    }
    
    @Column(name = "DPARAM0", precision = 22, scale = 0)
    public Double getDparam0() {
        return this.dparam0;
    }
    
    public void setDparam0(final Double dparam0) {
        this.dparam0 = dparam0;
    }
    
    @Column(name = "DPARAM1", precision = 22, scale = 0)
    public Double getDparam1() {
        return this.dparam1;
    }
    
    public void setDparam1(final Double dparam1) {
        this.dparam1 = dparam1;
    }
    
    @Column(name = "DPARAM10", precision = 22, scale = 0)
    public Double getDparam10() {
        return this.dparam10;
    }
    
    public void setDparam10(final Double dparam10) {
        this.dparam10 = dparam10;
    }
    
    @Column(name = "DPARAM11", precision = 22, scale = 0)
    public Double getDparam11() {
        return this.dparam11;
    }
    
    public void setDparam11(final Double dparam11) {
        this.dparam11 = dparam11;
    }
    
    @Column(name = "DPARAM12", precision = 22, scale = 0)
    public Double getDparam12() {
        return this.dparam12;
    }
    
    public void setDparam12(final Double dparam12) {
        this.dparam12 = dparam12;
    }
    
    @Column(name = "DPARAM13", precision = 22, scale = 0)
    public Double getDparam13() {
        return this.dparam13;
    }
    
    public void setDparam13(final Double dparam13) {
        this.dparam13 = dparam13;
    }
    
    @Column(name = "DPARAM14", precision = 22, scale = 0)
    public Double getDparam14() {
        return this.dparam14;
    }
    
    public void setDparam14(final Double dparam14) {
        this.dparam14 = dparam14;
    }
    
    @Column(name = "DPARAM2", precision = 22, scale = 0)
    public Double getDparam2() {
        return this.dparam2;
    }
    
    public void setDparam2(final Double dparam2) {
        this.dparam2 = dparam2;
    }
    
    @Column(name = "DPARAM3", precision = 22, scale = 0)
    public Double getDparam3() {
        return this.dparam3;
    }
    
    public void setDparam3(final Double dparam3) {
        this.dparam3 = dparam3;
    }
    
    @Column(name = "DPARAM4", precision = 22, scale = 0)
    public Double getDparam4() {
        return this.dparam4;
    }
    
    public void setDparam4(final Double dparam4) {
        this.dparam4 = dparam4;
    }
    
    @Column(name = "DPARAM5", precision = 22, scale = 0)
    public Double getDparam5() {
        return this.dparam5;
    }
    
    public void setDparam5(final Double dparam5) {
        this.dparam5 = dparam5;
    }
    
    @Column(name = "DPARAM6", precision = 22, scale = 0)
    public Double getDparam6() {
        return this.dparam6;
    }
    
    public void setDparam6(final Double dparam6) {
        this.dparam6 = dparam6;
    }
    
    @Column(name = "DPARAM7", precision = 22, scale = 0)
    public Double getDparam7() {
        return this.dparam7;
    }
    
    public void setDparam7(final Double dparam7) {
        this.dparam7 = dparam7;
    }
    
    @Column(name = "DPARAM8", precision = 22, scale = 0)
    public Double getDparam8() {
        return this.dparam8;
    }
    
    public void setDparam8(final Double dparam8) {
        this.dparam8 = dparam8;
    }
    
    @Column(name = "DPARAM9", precision = 22, scale = 0)
    public Double getDparam9() {
        return this.dparam9;
    }
    
    public void setDparam9(final Double dparam9) {
        this.dparam9 = dparam9;
    }
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FPARAM0", length = 19)
    public Date getFparam0() {
        return this.fparam0;
    }
    
    public void setFparam0(final Date fparam0) {
        this.fparam0 = fparam0;
    }
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FPARAM1", length = 19)
    public Date getFparam1() {
        return this.fparam1;
    }
    
    public void setFparam1(final Date fparam1) {
        this.fparam1 = fparam1;
    }
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FPARAM2", length = 19)
    public Date getFparam2() {
        return this.fparam2;
    }
    
    public void setFparam2(final Date fparam2) {
        this.fparam2 = fparam2;
    }
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FPARAM3", length = 19)
    public Date getFparam3() {
        return this.fparam3;
    }
    
    public void setFparam3(final Date fparam3) {
        this.fparam3 = fparam3;
    }
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FPARAM4", length = 19)
    public Date getFparam4() {
        return this.fparam4;
    }
    
    public void setFparam4(final Date fparam4) {
        this.fparam4 = fparam4;
    }
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FPARAM5", length = 19)
    public Date getFparam5() {
        return this.fparam5;
    }
    
    public void setFparam5(final Date fparam5) {
        this.fparam5 = fparam5;
    }
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FPARAM6", length = 19)
    public Date getFparam6() {
        return this.fparam6;
    }
    
    public void setFparam6(final Date fparam6) {
        this.fparam6 = fparam6;
    }
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FPARAM7", length = 19)
    public Date getFparam7() {
        return this.fparam7;
    }
    
    public void setFparam7(final Date fparam7) {
        this.fparam7 = fparam7;
    }
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FPARAM8", length = 19)
    public Date getFparam8() {
        return this.fparam8;
    }
    
    public void setFparam8(final Date fparam8) {
        this.fparam8 = fparam8;
    }
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FPARAM9", length = 19)
    public Date getFparam9() {
        return this.fparam9;
    }
    
    public void setFparam9(final Date fparam9) {
        this.fparam9 = fparam9;
    }
    
    @Column(name = "PARAM0", length = 250)
    public String getParam0() {
        return this.param0;
    }
    
    public void setParam0(final String param0) {
        this.param0 = param0;
    }
    
    @Column(name = "PARAM1", length = 250)
    public String getParam1() {
        return this.param1;
    }
    
    public void setParam1(final String param1) {
        this.param1 = param1;
    }
    
    @Column(name = "PARAM10", length = 250)
    public String getParam10() {
        return this.param10;
    }
    
    public void setParam10(final String param10) {
        this.param10 = param10;
    }
    
    @Column(name = "PARAM11", length = 250)
    public String getParam11() {
        return this.param11;
    }
    
    public void setParam11(final String param11) {
        this.param11 = param11;
    }
    
    @Column(name = "PARAM12", length = 250)
    public String getParam12() {
        return this.param12;
    }
    
    public void setParam12(final String param12) {
        this.param12 = param12;
    }
    
    @Column(name = "PARAM13", length = 250)
    public String getParam13() {
        return this.param13;
    }
    
    public void setParam13(final String param13) {
        this.param13 = param13;
    }
    
    @Column(name = "PARAM14", length = 250)
    public String getParam14() {
        return this.param14;
    }
    
    public void setParam14(final String param14) {
        this.param14 = param14;
    }
    
    @Column(name = "PARAM15")
    public String getParam15() {
        return this.param15;
    }
    
    public void setParam15(final String param15) {
        this.param15 = param15;
    }
    
    @Column(name = "PARAM16")
    public String getParam16() {
        return this.param16;
    }
    
    public void setParam16(final String param16) {
        this.param16 = param16;
    }
    
    @Column(name = "PARAM17")
    public String getParam17() {
        return this.param17;
    }
    
    public void setParam17(final String param17) {
        this.param17 = param17;
    }
    
    @Column(name = "PARAM18")
    public String getParam18() {
        return this.param18;
    }
    
    public void setParam18(final String param18) {
        this.param18 = param18;
    }
    
    @Column(name = "PARAM19")
    public String getParam19() {
        return this.param19;
    }
    
    public void setParam19(final String param19) {
        this.param19 = param19;
    }
    
    @Column(name = "PARAM2", length = 250)
    public String getParam2() {
        return this.param2;
    }
    
    public void setParam2(final String param2) {
        this.param2 = param2;
    }
    
    @Column(name = "PARAM3", length = 250)
    public String getParam3() {
        return this.param3;
    }
    
    public void setParam3(final String param3) {
        this.param3 = param3;
    }
    
    @Column(name = "PARAM4", length = 250)
    public String getParam4() {
        return this.param4;
    }
    
    public void setParam4(final String param4) {
        this.param4 = param4;
    }
    
    @Column(name = "PARAM5", length = 250)
    public String getParam5() {
        return this.param5;
    }
    
    public void setParam5(final String param5) {
        this.param5 = param5;
    }
    
    @Column(name = "PARAM6", length = 250)
    public String getParam6() {
        return this.param6;
    }
    
    public void setParam6(final String param6) {
        this.param6 = param6;
    }
    
    @Column(name = "PARAM7", length = 250)
    public String getParam7() {
        return this.param7;
    }
    
    public void setParam7(final String param7) {
        this.param7 = param7;
    }
    
    @Column(name = "PARAM8", length = 250)
    public String getParam8() {
        return this.param8;
    }
    
    public void setParam8(final String param8) {
        this.param8 = param8;
    }
    
    @Column(name = "PARAM9", length = 250)
    public String getParam9() {
        return this.param9;
    }
    
    public void setParam9(final String param9) {
        this.param9 = param9;
    }
    
//    public void escapeSqlAndHtmlCharacters(){
//        this.param0 = (this.param0 != null)?StringEscapeUtils.escapeSql(this.param0) : "";
//	this.param1 = (this.param1 != null)?StringEscapeUtils.escapeSql(this.param1) : "";
//	this.param2 = (this.param2 != null)?StringEscapeUtils.escapeSql(this.param2) : "";
//	this.param3 = (this.param3 != null)?StringEscapeUtils.escapeSql(this.param3) : "";
//	this.param4 = (this.param4 != null)?StringEscapeUtils.escapeSql(this.param4) : "";
//	this.param5 = (this.param5 != null)?StringEscapeUtils.escapeSql(this.param5) : "";
//	this.param6 = (this.param6 != null)?StringEscapeUtils.escapeSql(this.param6) : "";
//	this.param7 = (this.param7 != null)?StringEscapeUtils.escapeSql(this.param7) : "";
//	this.param8 = (this.param8 != null)?StringEscapeUtils.escapeSql(this.param8) : "";
//	this.param9 = (this.param9 != null)?StringEscapeUtils.escapeSql(this.param9) : "";
//	this.param10 = (this.param10 != null)?StringEscapeUtils.escapeSql(this.param10) : "";
//	this.param11 = (this.param11 != null)?StringEscapeUtils.escapeSql(this.param11) : "";
//	this.param12 = (this.param12 != null)?StringEscapeUtils.escapeSql(this.param12) : "";
//	this.param13 = (this.param13 != null)?StringEscapeUtils.escapeSql(this.param13) : "";
//	this.param14 = (this.param14 != null)?StringEscapeUtils.escapeSql(this.param14) : "";
//	this.param15 = (this.param15 != null)?StringEscapeUtils.escapeSql(this.param15) : "";
//	this.param16 = (this.param16 != null)?StringEscapeUtils.escapeSql(this.param16) : "";
//	this.param17 = (this.param17 != null)?StringEscapeUtils.escapeSql(this.param17) : "";
//	this.param18 = (this.param18 != null)?StringEscapeUtils.escapeSql(this.param18) : "";
//	this.param19 = (this.param19 != null)?StringEscapeUtils.escapeSql(this.param19) : "";
//        
//        this.param0 = (this.param0 != null)?StringEscapeUtils.escapeHtml(this.param0) : "";
//	this.param1 = (this.param1 != null)?StringEscapeUtils.escapeHtml(this.param1) : "";
//	this.param2 = (this.param2 != null)?StringEscapeUtils.escapeHtml(this.param2) : "";
//	this.param3 = (this.param3 != null)?StringEscapeUtils.escapeHtml(this.param3) : "";
//	this.param4 = (this.param4 != null)?StringEscapeUtils.escapeHtml(this.param4) : "";
//	this.param5 = (this.param5 != null)?StringEscapeUtils.escapeHtml(this.param5) : "";
//	this.param6 = (this.param6 != null)?StringEscapeUtils.escapeHtml(this.param6) : "";
//	this.param7 = (this.param7 != null)?StringEscapeUtils.escapeHtml(this.param7) : "";
//	this.param8 = (this.param8 != null)?StringEscapeUtils.escapeHtml(this.param8) : "";
//	this.param9 = (this.param9 != null)?StringEscapeUtils.escapeHtml(this.param9) : "";
//	this.param10 = (this.param10 != null)?StringEscapeUtils.escapeHtml(this.param10) : "";
//	this.param11 = (this.param11 != null)?StringEscapeUtils.escapeHtml(this.param11) : "";
//	this.param12 = (this.param12 != null)?StringEscapeUtils.escapeHtml(this.param12) : "";
//	this.param13 = (this.param13 != null)?StringEscapeUtils.escapeHtml(this.param13) : "";
//	this.param14 = (this.param14 != null)?StringEscapeUtils.escapeHtml(this.param14) : "";
//	this.param15 = (this.param15 != null)?StringEscapeUtils.escapeHtml(this.param15) : "";
//	this.param16 = (this.param16 != null)?StringEscapeUtils.escapeHtml(this.param16) : "";
//	this.param17 = (this.param17 != null)?StringEscapeUtils.escapeHtml(this.param17) : "";
//	this.param18 = (this.param18 != null)?StringEscapeUtils.escapeHtml(this.param18) : "";
//	this.param19 = (this.param19 != null)?StringEscapeUtils.escapeHtml(this.param19) : "";
//    }
}
