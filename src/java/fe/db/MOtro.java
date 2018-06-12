package fe.db;

/**
 * @Author Carlo Garcia Sanchez
 * @Date 02-07-2011
 * <p>
 * Tabla para almacenar campos adicionales a la tabla MCfd
 * que el cliente requiera en el portal.
 */

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
//import org.apache.commons.lang.StringEscapeUtils;

@SuppressWarnings("serial")
@Entity
@Table(name = "M_OTRO")
public class MOtro implements Serializable {

    // Identificador Unico AutoIncremental
    private Integer id;
    //	private MEmpresa empresa;
    private MCfd cfd;

    // Campos adicionales Varchar
    private String param0 = "";
    private String param1 = "";
    private String param2 = "";
    private String param3 = "";
    private String param4 = "";
    private String param5 = "";
    private String param6 = "";
    private String param7 = "";
    private String param8 = "";
    private String param9 = "";
    private String param10 = "";
    private String param11 = "";
    private String param12 = "";
    private String param13 = "";
    private String param14 = "";
    private String param15 = "";
    private String param16 = "";
    private String param17 = "";
    private String param18 = "";
    private String param19 = "";
    private String param20 = "";
    private String noPolizaSeguro = "";

    // Campos adicionales Double
    private double dparam0 = 0d;
    private double dparam1 = 0d;
    private double dparam2 = 0d;
    private double dparam3 = 0d;
    private double dparam4 = 0d;
    private double dparam5 = 0d;
    private double dparam6 = 0d;
    private double dparam7 = 0d;
    private double dparam8 = 0d;
    private double dparam9 = 0d;
    private double dparam10 = 0d;
    private double dparam11 = 0d;
    private double dparam12 = 0d;
    private double dparam13 = 0d;
    private double dparam14 = 0d;

    // Campos adicionales Double
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

    // Contructor privado
    public MOtro() {
    }

    // Constructor inicial
    public MOtro(MCfd cfd) {
//		this.empresa = empresa;
        this.cfd = cfd;
    }

    // Constructor inicial VARCHAR
    public MOtro(MCfd cfd, String... params) {
//		this.empresa = empresa;
        this.cfd = cfd;
        for (int i = 0; i < params.length; i++)
            switch (i) {
                case 0:
                    param0 = params[0];
                    break;
                case 1:
                    param1 = params[1];
                    break;
                case 2:
                    param2 = params[2];
                    break;
                case 3:
                    param3 = params[3];
                    break;
                case 4:
                    param4 = params[4];
                    break;
                case 5:
                    param5 = params[5];
                    break;
                case 6:
                    param6 = params[6];
                    break;
                case 7:
                    param7 = params[7];
                    break;
                case 8:
                    param8 = params[8];
                    break;
                case 9:
                    param9 = params[9];
                    break;
                case 10:
                    param10 = params[10];
                    break;
                case 11:
                    param11 = params[11];
                    break;
                case 12:
                    param12 = params[12];
                    break;
                case 13:
                    param13 = params[13];
                    break;
                case 14:
                    param14 = params[14];
                    break;
                case 15:
                    param15 = params[15];
                    break;
                case 16:
                    param16 = params[16];
                    break;
                case 17:
                    param17 = params[17];
                    break;
                case 18:
                    param18 = params[18];
                    break;
                case 19:
                    param19 = params[19];
                    break;
                case 20:
                    param20 = params[20];
                    break;
            }
    }

    // Constructor inicial Num�rico
    public MOtro(MCfd cfd, double... dparams) {
//		this.empresa = empresa;
//		this.cfd = cfd;
        for (int i = 0; i < dparams.length; i++)
            switch (i) {
                case 0:
                    dparam0 = dparams[0];
                case 1:
                    dparam1 = dparams[1];
                case 2:
                    dparam2 = dparams[2];
                case 3:
                    dparam3 = dparams[3];
                case 4:
                    dparam4 = dparams[4];
                case 5:
                    dparam5 = dparams[5];
                case 6:
                    dparam6 = dparams[6];
                case 7:
                    dparam7 = dparams[7];
                case 8:
                    dparam8 = dparams[8];
                case 9:
                    dparam9 = dparams[9];
                case 10:
                    dparam10 = dparams[10];
                case 11:
                    dparam11 = dparams[11];
                case 12:
                    dparam12 = dparams[12];
                case 13:
                    dparam13 = dparams[13];
                case 14:
                    dparam14 = dparams[14];
            }
    }

    // Constructor inicial Fecha
    public MOtro(MCfd cfd, Date... fparams) {
//		this.empresa = empresa;
//		this.cfd = cfd;
        for (int i = 0; i < fparams.length; i++)
            switch (i) {
                case 0:
                    fparam0 = fparams[0];
                case 1:
                    fparam1 = fparams[1];
                case 2:
                    fparam2 = fparams[2];
                case 3:
                    fparam3 = fparams[3];
                case 4:
                    fparam4 = fparams[4];
                case 5:
                    fparam5 = fparams[5];
                case 6:
                    fparam6 = fparams[6];
                case 7:
                    fparam7 = fparams[7];
                case 8:
                    fparam8 = fparams[8];
                case 9:
                    fparam9 = fparams[9];
            }
    }

    /**
     * Getters
     */

    @Id
    /*@SequenceGenerator(name = "SOtro", sequenceName = "SECUENCIA_OTRO", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SOtro")*/
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    public Integer getId() {
        return id;
    }


    @Column(name = "PARAM0", nullable = true, length = 50)
    public String getParam0() {
        return param0;
    }

    @Column(name = "PARAM1", nullable = true, length = 50)
    public String getParam1() {
        return param1;
    }

    @Column(name = "PARAM2", nullable = true, length = 50)
    public String getParam2() {
        return param2;
    }

    @Column(name = "PARAM3", nullable = true, length = 50)
    public String getParam3() {
        return param3;
    }

    @Column(name = "PARAM4", nullable = true, length = 50)
    public String getParam4() {
        return param4;
    }

    @Column(name = "PARAM5", nullable = true, length = 50)
    public String getParam5() {
        return param5;
    }

    @Column(name = "PARAM6", nullable = true, length = 50)
    public String getParam6() {
        return param6;
    }

    @Column(name = "PARAM7", nullable = true, length = 50)
    public String getParam7() {
        return param7;
    }

    @Column(name = "PARAM8", nullable = true, length = 50)
    public String getParam8() {
        return param8;
    }

    @Column(name = "PARAM9", nullable = true, length = 50)
    public String getParam9() {
        return param9;
    }

    @Column(name = "PARAM10", nullable = true, length = 100)
    public String getParam10() {
        return param10;
    }

    @Column(name = "PARAM11", nullable = true, length = 100)
    public String getParam11() {
        return param11;
    }

    @Column(name = "PARAM12", nullable = true, length = 100)
    public String getParam12() {
        return param12;
    }

    @Column(name = "PARAM13", nullable = true, length = 100)
    public String getParam13() {
        return param13;
    }

    @Column(name = "PARAM14", nullable = true, length = 100)
    public String getParam14() {
        return param14;
    }

    @Column(name = "PARAM15", nullable = true, length = 500)
    public String getParam15() {
        return param15;
    }

    @Column(name = "PARAM16", nullable = true, length = 500)
    public String getParam16() {
        return param16;
    }

    @Column(name = "PARAM17", nullable = true, length = 500)
    public String getParam17() {
        return param17;
    }

    @Column(name = "PARAM18", nullable = true, length = 500)
    public String getParam18() {
        return param18;
    }

    @Column(name = "PARAM19", nullable = true, length = 500)
    public String getParam19() {
        return param19;
    }

    @Column(name = "PARAM20", nullable = true, length = 3)
    public String getParam20() {
        return param20;
    }

    @Column(name = "NO_POLIZA_SEGURO", nullable = false, length = 20)
    public String getNoPolizaSeguro() {
        return noPolizaSeguro;
    }

    public void setNoPolizaSeguro(String noPolizaSeguro) {
        this.noPolizaSeguro = noPolizaSeguro;
    }

    @Column(name = "DPARAM0", nullable = true)
    public double getDParam0() {
        return dparam0;
    }

    @Column(name = "DPARAM1", nullable = true)
    public double getDParam1() {
        return dparam1;
    }

    @Column(name = "DPARAM2", nullable = true)
    public double getDParam2() {
        return dparam2;
    }

    @Column(name = "DPARAM3", nullable = true)
    public double getDParam3() {
        return dparam3;
    }

    @Column(name = "DPARAM4", nullable = true)
    public double getDParam4() {
        return dparam4;
    }

    @Column(name = "DPARAM5", nullable = true)
    public double getDParam5() {
        return dparam5;
    }

    @Column(name = "DPARAM6", nullable = true)
    public double getDParam6() {
        return dparam6;
    }

    @Column(name = "DPARAM7", nullable = true)
    public double getDParam7() {
        return dparam7;
    }

    @Column(name = "DPARAM8", nullable = true)
    public double getDParam8() {
        return dparam8;
    }

    @Column(name = "DPARAM9", nullable = true)
    public double getDParam9() {
        return dparam9;
    }

    @Column(name = "DPARAM10", nullable = true)
    public double getDParam10() {
        return dparam10;
    }

    @Column(name = "DPARAM11", nullable = true)
    public double getDParam11() {
        return dparam11;
    }

    @Column(name = "DPARAM12", nullable = true)
    public double getDParam12() {
        return dparam12;
    }

    @Column(name = "DPARAM13", nullable = true)
    public double getDParam13() {
        return dparam13;
    }

    @Column(name = "DPARAM14", nullable = true)
    public double getDParam14() {
        return dparam14;
    }

    @Column(name = "FPARAM0", nullable = true)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getFParam0() {
        return fparam0;
    }

    @Column(name = "FPARAM1", nullable = true)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getFParam1() {
        return fparam1;
    }

    @Column(name = "FPARAM2", nullable = true)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getFParam2() {
        return fparam2;
    }

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "FPARAM3", nullable = true)
    public Date getFParam3() {
        return fparam3;
    }

    @Column(name = "FPARAM4", nullable = true)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getFParam4() {
        return fparam4;
    }

    @Column(name = "FPARAM5", nullable = true)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getFParam5() {
        return fparam5;
    }

    @Column(name = "FPARAM6", nullable = true)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getFParam6() {
        return fparam6;
    }

    @Column(name = "FPARAM7", nullable = true)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getFParam7() {
        return fparam7;
    }

    @Column(name = "FPARAM8", nullable = true)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getFParam8() {
        return fparam8;
    }

    @Column(name = "FPARAM9", nullable = true)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getFParam9() {
        return fparam9;
    }

    /**
     * Setters
     */

    public void setId(Integer id) {
        this.id = id;
    }


    public void setParam0(String param0) {
        this.param0 = param0;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public void setParam3(String param3) {
        this.param3 = param3;
    }

    public void setParam4(String param4) {
        this.param4 = param4;
    }

    public void setParam5(String param5) {
        this.param5 = param5;
    }

    public void setParam6(String param6) {
        this.param6 = param6;
    }

    public void setParam7(String param7) {
        this.param7 = param7;
    }

    public void setParam8(String param8) {
        this.param8 = param8;
    }

    public void setParam9(String param9) {
        this.param9 = param9;
    }

    public void setParam10(String param10) {
        this.param10 = param10;
    }

    public void setParam11(String param11) {
        this.param11 = param11;
    }

    public void setParam12(String param12) {
        this.param12 = param12;
    }

    public void setParam13(String param13) {
        this.param13 = param13;
    }

    public void setParam14(String param14) {
        this.param14 = param14;
    }

    public void setParam15(String param15) {
        this.param15 = param15;
    }

    public void setParam16(String param16) {
        this.param16 = param16;
    }

    public void setParam17(String param17) {
        this.param17 = param17;
    }

    public void setParam18(String param18) {
        this.param18 = param18;
    }

    public void setParam19(String param19) {
        this.param19 = param19;
    }

    public void setParam20(String param2O) {
        this.param20 = param2O;
    }

    public void setDParam0(double dparam0) {
        this.dparam0 = dparam0;
    }

    public void setDParam1(double dparam1) {
        this.dparam1 = dparam1;
    }

    public void setDParam2(double dparam2) {
        this.dparam2 = dparam2;
    }

    public void setDParam3(double dparam3) {
        this.dparam3 = dparam3;
    }

    public void setDParam4(double dparam4) {
        this.dparam4 = dparam4;
    }

    public void setDParam5(double dparam5) {
        this.dparam5 = dparam5;
    }

    public void setDParam6(double dparam6) {
        this.dparam6 = dparam6;
    }

    public void setDParam7(double dparam7) {
        this.dparam7 = dparam7;
    }

    public void setDParam8(double dparam8) {
        this.dparam8 = dparam8;
    }

    public void setDParam9(double dparam9) {
        this.dparam9 = dparam9;
    }

    public void setDParam10(double dparam10) {
        this.dparam10 = dparam10;
    }

    public void setDParam11(double dparam11) {
        this.dparam11 = dparam11;
    }

    public void setDParam12(double dparam12) {
        this.dparam12 = dparam12;
    }

    public void setDParam13(double dparam13) {
        this.dparam13 = dparam13;
    }

    public void setDParam14(double dparam14) {
        this.dparam14 = dparam14;
    }

    public void setFParam0(Date fparam0) {
        this.fparam0 = fparam0;
    }

    public void setFParam1(Date fparam1) {
        this.fparam1 = fparam1;
    }

    public void setFParam2(Date fparam2) {
        this.fparam2 = fparam2;
    }

    public void setFParam3(Date fparam3) {
        this.fparam3 = fparam3;
    }

    public void setFParam4(Date fparam4) {
        this.fparam4 = fparam4;
    }

    public void setFParam5(Date fparam5) {
        this.fparam5 = fparam5;
    }

    public void setFParam6(Date fparam6) {
        this.fparam6 = fparam6;
    }

    public void setFParam7(Date fparam7) {
        this.fparam7 = fparam7;
    }

    public void setFParam8(Date fparam8) {
        this.fparam8 = fparam8;
    }

    public void setFParam9(Date fparam9) {
        this.fparam9 = fparam9;
    }

    /**
     * @return the cfd
     */
    @OneToOne
    public MCfd getCfd() {
        return cfd;
    }

    /**
     * @param cfd the cfd to set
     */
    public void setCfd(MCfd cfd) {
        this.cfd = cfd;
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