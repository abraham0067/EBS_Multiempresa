package fe.db;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by eflores on 19/10/2017.
 */
@Entity
@Table(name = "M_OTRO_PROFORMA")
public class MOtroProforma {
    private int id;
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
    private Timestamp fparam0;
    private Timestamp fparam1;
    private Timestamp fparam2;
    private Timestamp fparam3;
    private Timestamp fparam4;
    private Timestamp fparam5;
    private Timestamp fparam6;
    private Timestamp fparam7;
    private Timestamp fparam8;
    private Timestamp fparam9;
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
    private Integer cfdId;
    private String param20;

    @Id
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "DPARAM0", nullable = true, precision = 0)
    public Double getDparam0() {
        return dparam0;
    }

    public void setDparam0(Double dparam0) {
        this.dparam0 = dparam0;
    }

    @Basic
    @Column(name = "DPARAM1", nullable = true, precision = 0)
    public Double getDparam1() {
        return dparam1;
    }

    public void setDparam1(Double dparam1) {
        this.dparam1 = dparam1;
    }

    @Basic
    @Column(name = "DPARAM10", nullable = true, precision = 0)
    public Double getDparam10() {
        return dparam10;
    }

    public void setDparam10(Double dparam10) {
        this.dparam10 = dparam10;
    }

    @Basic
    @Column(name = "DPARAM11", nullable = true, precision = 0)
    public Double getDparam11() {
        return dparam11;
    }

    public void setDparam11(Double dparam11) {
        this.dparam11 = dparam11;
    }

    @Basic
    @Column(name = "DPARAM12", nullable = true, precision = 0)
    public Double getDparam12() {
        return dparam12;
    }

    public void setDparam12(Double dparam12) {
        this.dparam12 = dparam12;
    }

    @Basic
    @Column(name = "DPARAM13", nullable = true, precision = 0)
    public Double getDparam13() {
        return dparam13;
    }

    public void setDparam13(Double dparam13) {
        this.dparam13 = dparam13;
    }

    @Basic
    @Column(name = "DPARAM14", nullable = true, precision = 0)
    public Double getDparam14() {
        return dparam14;
    }

    public void setDparam14(Double dparam14) {
        this.dparam14 = dparam14;
    }

    @Basic
    @Column(name = "DPARAM2", nullable = true, precision = 0)
    public Double getDparam2() {
        return dparam2;
    }

    public void setDparam2(Double dparam2) {
        this.dparam2 = dparam2;
    }

    @Basic
    @Column(name = "DPARAM3", nullable = true, precision = 0)
    public Double getDparam3() {
        return dparam3;
    }

    public void setDparam3(Double dparam3) {
        this.dparam3 = dparam3;
    }

    @Basic
    @Column(name = "DPARAM4", nullable = true, precision = 0)
    public Double getDparam4() {
        return dparam4;
    }

    public void setDparam4(Double dparam4) {
        this.dparam4 = dparam4;
    }

    @Basic
    @Column(name = "DPARAM5", nullable = true, precision = 0)
    public Double getDparam5() {
        return dparam5;
    }

    public void setDparam5(Double dparam5) {
        this.dparam5 = dparam5;
    }

    @Basic
    @Column(name = "DPARAM6", nullable = true, precision = 0)
    public Double getDparam6() {
        return dparam6;
    }

    public void setDparam6(Double dparam6) {
        this.dparam6 = dparam6;
    }

    @Basic
    @Column(name = "DPARAM7", nullable = true, precision = 0)
    public Double getDparam7() {
        return dparam7;
    }

    public void setDparam7(Double dparam7) {
        this.dparam7 = dparam7;
    }

    @Basic
    @Column(name = "DPARAM8", nullable = true, precision = 0)
    public Double getDparam8() {
        return dparam8;
    }

    public void setDparam8(Double dparam8) {
        this.dparam8 = dparam8;
    }

    @Basic
    @Column(name = "DPARAM9", nullable = true, precision = 0)
    public Double getDparam9() {
        return dparam9;
    }

    public void setDparam9(Double dparam9) {
        this.dparam9 = dparam9;
    }

    @Basic
    @Column(name = "FPARAM0", nullable = true)
    public Timestamp getFparam0() {
        return fparam0;
    }

    public void setFparam0(Timestamp fparam0) {
        this.fparam0 = fparam0;
    }

    @Basic
    @Column(name = "FPARAM1", nullable = true)
    public Timestamp getFparam1() {
        return fparam1;
    }

    public void setFparam1(Timestamp fparam1) {
        this.fparam1 = fparam1;
    }

    @Basic
    @Column(name = "FPARAM2", nullable = true)
    public Timestamp getFparam2() {
        return fparam2;
    }

    public void setFparam2(Timestamp fparam2) {
        this.fparam2 = fparam2;
    }

    @Basic
    @Column(name = "FPARAM3", nullable = true)
    public Timestamp getFparam3() {
        return fparam3;
    }

    public void setFparam3(Timestamp fparam3) {
        this.fparam3 = fparam3;
    }

    @Basic
    @Column(name = "FPARAM4", nullable = true)
    public Timestamp getFparam4() {
        return fparam4;
    }

    public void setFparam4(Timestamp fparam4) {
        this.fparam4 = fparam4;
    }

    @Basic
    @Column(name = "FPARAM5", nullable = true)
    public Timestamp getFparam5() {
        return fparam5;
    }

    public void setFparam5(Timestamp fparam5) {
        this.fparam5 = fparam5;
    }

    @Basic
    @Column(name = "FPARAM6", nullable = true)
    public Timestamp getFparam6() {
        return fparam6;
    }

    public void setFparam6(Timestamp fparam6) {
        this.fparam6 = fparam6;
    }

    @Basic
    @Column(name = "FPARAM7", nullable = true)
    public Timestamp getFparam7() {
        return fparam7;
    }

    public void setFparam7(Timestamp fparam7) {
        this.fparam7 = fparam7;
    }

    @Basic
    @Column(name = "FPARAM8", nullable = true)
    public Timestamp getFparam8() {
        return fparam8;
    }

    public void setFparam8(Timestamp fparam8) {
        this.fparam8 = fparam8;
    }

    @Basic
    @Column(name = "FPARAM9", nullable = true)
    public Timestamp getFparam9() {
        return fparam9;
    }

    public void setFparam9(Timestamp fparam9) {
        this.fparam9 = fparam9;
    }

    @Basic
    @Column(name = "PARAM0", nullable = true, length = 250)
    public String getParam0() {
        return param0;
    }

    public void setParam0(String param0) {
        this.param0 = param0;
    }

    @Basic
    @Column(name = "PARAM1", nullable = true, length = 250)
    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    @Basic
    @Column(name = "PARAM10", nullable = true, length = 250)
    public String getParam10() {
        return param10;
    }

    public void setParam10(String param10) {
        this.param10 = param10;
    }

    @Basic
    @Column(name = "PARAM11", nullable = true, length = 250)
    public String getParam11() {
        return param11;
    }

    public void setParam11(String param11) {
        this.param11 = param11;
    }

    @Basic
    @Column(name = "PARAM12", nullable = true, length = 250)
    public String getParam12() {
        return param12;
    }

    public void setParam12(String param12) {
        this.param12 = param12;
    }

    @Basic
    @Column(name = "PARAM13", nullable = true, length = 250)
    public String getParam13() {
        return param13;
    }

    public void setParam13(String param13) {
        this.param13 = param13;
    }

    @Basic
    @Column(name = "PARAM14", nullable = true, length = 250)
    public String getParam14() {
        return param14;
    }

    public void setParam14(String param14) {
        this.param14 = param14;
    }

    @Basic
    @Column(name = "PARAM15", nullable = true, length = -1)
    public String getParam15() {
        return param15;
    }

    public void setParam15(String param15) {
        this.param15 = param15;
    }

    @Basic
    @Column(name = "PARAM16", nullable = true, length = -1)
    public String getParam16() {
        return param16;
    }

    public void setParam16(String param16) {
        this.param16 = param16;
    }

    @Basic
    @Column(name = "PARAM17", nullable = true, length = -1)
    public String getParam17() {
        return param17;
    }

    public void setParam17(String param17) {
        this.param17 = param17;
    }

    @Basic
    @Column(name = "PARAM18", nullable = true, length = -1)
    public String getParam18() {
        return param18;
    }

    public void setParam18(String param18) {
        this.param18 = param18;
    }

    @Basic
    @Column(name = "PARAM19", nullable = true, length = -1)
    public String getParam19() {
        return param19;
    }

    public void setParam19(String param19) {
        this.param19 = param19;
    }

    @Basic
    @Column(name = "PARAM2", nullable = true, length = 250)
    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    @Basic
    @Column(name = "PARAM3", nullable = true, length = 250)
    public String getParam3() {
        return param3;
    }

    public void setParam3(String param3) {
        this.param3 = param3;
    }

    @Basic
    @Column(name = "PARAM4", nullable = true, length = 250)
    public String getParam4() {
        return param4;
    }

    public void setParam4(String param4) {
        this.param4 = param4;
    }

    @Basic
    @Column(name = "PARAM5", nullable = true, length = 250)
    public String getParam5() {
        return param5;
    }

    public void setParam5(String param5) {
        this.param5 = param5;
    }

    @Basic
    @Column(name = "PARAM6", nullable = true, length = 250)
    public String getParam6() {
        return param6;
    }

    public void setParam6(String param6) {
        this.param6 = param6;
    }

    @Basic
    @Column(name = "PARAM7", nullable = true, length = 250)
    public String getParam7() {
        return param7;
    }

    public void setParam7(String param7) {
        this.param7 = param7;
    }

    @Basic
    @Column(name = "PARAM8", nullable = true, length = 250)
    public String getParam8() {
        return param8;
    }

    public void setParam8(String param8) {
        this.param8 = param8;
    }

    @Basic
    @Column(name = "PARAM9", nullable = true, length = 250)
    public String getParam9() {
        return param9;
    }

    public void setParam9(String param9) {
        this.param9 = param9;
    }

    @Basic
    @Column(name = "CFD_ID", nullable = true)
    public Integer getCfdId() {
        return cfdId;
    }

    public void setCfdId(Integer cfdId) {
        this.cfdId = cfdId;
    }

    @Basic
    @Column(name = "PARAM20", nullable = true, length = 3)
    public String getParam20() {
        return param20;
    }

    public void setParam20(String param20) {
        this.param20 = param20;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MOtroProforma that = (MOtroProforma) o;

        if (id != that.id) return false;
        if (dparam0 != null ? !dparam0.equals(that.dparam0) : that.dparam0 != null) return false;
        if (dparam1 != null ? !dparam1.equals(that.dparam1) : that.dparam1 != null) return false;
        if (dparam10 != null ? !dparam10.equals(that.dparam10) : that.dparam10 != null) return false;
        if (dparam11 != null ? !dparam11.equals(that.dparam11) : that.dparam11 != null) return false;
        if (dparam12 != null ? !dparam12.equals(that.dparam12) : that.dparam12 != null) return false;
        if (dparam13 != null ? !dparam13.equals(that.dparam13) : that.dparam13 != null) return false;
        if (dparam14 != null ? !dparam14.equals(that.dparam14) : that.dparam14 != null) return false;
        if (dparam2 != null ? !dparam2.equals(that.dparam2) : that.dparam2 != null) return false;
        if (dparam3 != null ? !dparam3.equals(that.dparam3) : that.dparam3 != null) return false;
        if (dparam4 != null ? !dparam4.equals(that.dparam4) : that.dparam4 != null) return false;
        if (dparam5 != null ? !dparam5.equals(that.dparam5) : that.dparam5 != null) return false;
        if (dparam6 != null ? !dparam6.equals(that.dparam6) : that.dparam6 != null) return false;
        if (dparam7 != null ? !dparam7.equals(that.dparam7) : that.dparam7 != null) return false;
        if (dparam8 != null ? !dparam8.equals(that.dparam8) : that.dparam8 != null) return false;
        if (dparam9 != null ? !dparam9.equals(that.dparam9) : that.dparam9 != null) return false;
        if (fparam0 != null ? !fparam0.equals(that.fparam0) : that.fparam0 != null) return false;
        if (fparam1 != null ? !fparam1.equals(that.fparam1) : that.fparam1 != null) return false;
        if (fparam2 != null ? !fparam2.equals(that.fparam2) : that.fparam2 != null) return false;
        if (fparam3 != null ? !fparam3.equals(that.fparam3) : that.fparam3 != null) return false;
        if (fparam4 != null ? !fparam4.equals(that.fparam4) : that.fparam4 != null) return false;
        if (fparam5 != null ? !fparam5.equals(that.fparam5) : that.fparam5 != null) return false;
        if (fparam6 != null ? !fparam6.equals(that.fparam6) : that.fparam6 != null) return false;
        if (fparam7 != null ? !fparam7.equals(that.fparam7) : that.fparam7 != null) return false;
        if (fparam8 != null ? !fparam8.equals(that.fparam8) : that.fparam8 != null) return false;
        if (fparam9 != null ? !fparam9.equals(that.fparam9) : that.fparam9 != null) return false;
        if (param0 != null ? !param0.equals(that.param0) : that.param0 != null) return false;
        if (param1 != null ? !param1.equals(that.param1) : that.param1 != null) return false;
        if (param10 != null ? !param10.equals(that.param10) : that.param10 != null) return false;
        if (param11 != null ? !param11.equals(that.param11) : that.param11 != null) return false;
        if (param12 != null ? !param12.equals(that.param12) : that.param12 != null) return false;
        if (param13 != null ? !param13.equals(that.param13) : that.param13 != null) return false;
        if (param14 != null ? !param14.equals(that.param14) : that.param14 != null) return false;
        if (param15 != null ? !param15.equals(that.param15) : that.param15 != null) return false;
        if (param16 != null ? !param16.equals(that.param16) : that.param16 != null) return false;
        if (param17 != null ? !param17.equals(that.param17) : that.param17 != null) return false;
        if (param18 != null ? !param18.equals(that.param18) : that.param18 != null) return false;
        if (param19 != null ? !param19.equals(that.param19) : that.param19 != null) return false;
        if (param2 != null ? !param2.equals(that.param2) : that.param2 != null) return false;
        if (param3 != null ? !param3.equals(that.param3) : that.param3 != null) return false;
        if (param4 != null ? !param4.equals(that.param4) : that.param4 != null) return false;
        if (param5 != null ? !param5.equals(that.param5) : that.param5 != null) return false;
        if (param6 != null ? !param6.equals(that.param6) : that.param6 != null) return false;
        if (param7 != null ? !param7.equals(that.param7) : that.param7 != null) return false;
        if (param8 != null ? !param8.equals(that.param8) : that.param8 != null) return false;
        if (param9 != null ? !param9.equals(that.param9) : that.param9 != null) return false;
        if (cfdId != null ? !cfdId.equals(that.cfdId) : that.cfdId != null) return false;
        if (param20 != null ? !param20.equals(that.param20) : that.param20 != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (dparam0 != null ? dparam0.hashCode() : 0);
        result = 31 * result + (dparam1 != null ? dparam1.hashCode() : 0);
        result = 31 * result + (dparam10 != null ? dparam10.hashCode() : 0);
        result = 31 * result + (dparam11 != null ? dparam11.hashCode() : 0);
        result = 31 * result + (dparam12 != null ? dparam12.hashCode() : 0);
        result = 31 * result + (dparam13 != null ? dparam13.hashCode() : 0);
        result = 31 * result + (dparam14 != null ? dparam14.hashCode() : 0);
        result = 31 * result + (dparam2 != null ? dparam2.hashCode() : 0);
        result = 31 * result + (dparam3 != null ? dparam3.hashCode() : 0);
        result = 31 * result + (dparam4 != null ? dparam4.hashCode() : 0);
        result = 31 * result + (dparam5 != null ? dparam5.hashCode() : 0);
        result = 31 * result + (dparam6 != null ? dparam6.hashCode() : 0);
        result = 31 * result + (dparam7 != null ? dparam7.hashCode() : 0);
        result = 31 * result + (dparam8 != null ? dparam8.hashCode() : 0);
        result = 31 * result + (dparam9 != null ? dparam9.hashCode() : 0);
        result = 31 * result + (fparam0 != null ? fparam0.hashCode() : 0);
        result = 31 * result + (fparam1 != null ? fparam1.hashCode() : 0);
        result = 31 * result + (fparam2 != null ? fparam2.hashCode() : 0);
        result = 31 * result + (fparam3 != null ? fparam3.hashCode() : 0);
        result = 31 * result + (fparam4 != null ? fparam4.hashCode() : 0);
        result = 31 * result + (fparam5 != null ? fparam5.hashCode() : 0);
        result = 31 * result + (fparam6 != null ? fparam6.hashCode() : 0);
        result = 31 * result + (fparam7 != null ? fparam7.hashCode() : 0);
        result = 31 * result + (fparam8 != null ? fparam8.hashCode() : 0);
        result = 31 * result + (fparam9 != null ? fparam9.hashCode() : 0);
        result = 31 * result + (param0 != null ? param0.hashCode() : 0);
        result = 31 * result + (param1 != null ? param1.hashCode() : 0);
        result = 31 * result + (param10 != null ? param10.hashCode() : 0);
        result = 31 * result + (param11 != null ? param11.hashCode() : 0);
        result = 31 * result + (param12 != null ? param12.hashCode() : 0);
        result = 31 * result + (param13 != null ? param13.hashCode() : 0);
        result = 31 * result + (param14 != null ? param14.hashCode() : 0);
        result = 31 * result + (param15 != null ? param15.hashCode() : 0);
        result = 31 * result + (param16 != null ? param16.hashCode() : 0);
        result = 31 * result + (param17 != null ? param17.hashCode() : 0);
        result = 31 * result + (param18 != null ? param18.hashCode() : 0);
        result = 31 * result + (param19 != null ? param19.hashCode() : 0);
        result = 31 * result + (param2 != null ? param2.hashCode() : 0);
        result = 31 * result + (param3 != null ? param3.hashCode() : 0);
        result = 31 * result + (param4 != null ? param4.hashCode() : 0);
        result = 31 * result + (param5 != null ? param5.hashCode() : 0);
        result = 31 * result + (param6 != null ? param6.hashCode() : 0);
        result = 31 * result + (param7 != null ? param7.hashCode() : 0);
        result = 31 * result + (param8 != null ? param8.hashCode() : 0);
        result = 31 * result + (param9 != null ? param9.hashCode() : 0);
        result = 31 * result + (cfdId != null ? cfdId.hashCode() : 0);
        result = 31 * result + (param20 != null ? param20.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MOtroProforma{" +
                "id=" + id +
                ", cfdId=" + cfdId +
                '}';
    }
}
