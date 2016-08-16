package com.wnt.ireport.po;

import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Ebsdynimpbpara implements Serializable {
    
    private String reportid;
    
    private String reportname;
    
    private String filename;
    
    private String rptmaker;
    
    private String rptmakedt;
    
    private String rptversion;
    
    private String rptremark;
    
    private String resultsql;

    public Ebsdynimpbpara(String reportid,String reportname,String filename,String rptmaker,String rptmakedt,String rptversion,String rptremark,String resultsql) {
        this.reportid = reportid;
        this.reportname = reportname;
        this.filename = filename;
        this.rptmaker = rptmaker;
        this.rptmakedt = rptmakedt;
        this.rptversion = rptversion;
        this.rptremark = rptremark;
        this.resultsql = resultsql;
    }

    public Ebsdynimpbpara() {}
    public String getReportid() {return this.reportid;}
    public void setReportid(String reportid) {this.reportid = reportid;}
    public String getReportname() {return this.reportname;}
    public void setReportname(String reportname) {this.reportname = reportname;}
    public String getFilename() {return this.filename;}
    public void setFilename(String filename) {this.filename = filename;}
    public String getRptmaker() {return this.rptmaker;}
    public void setRptmaker(String rptmaker) {this.rptmaker = rptmaker;}
    public String getRptmakedt() {return this.rptmakedt;}
    public void setRptmakedt(String rptmakedt) {this.rptmakedt = rptmakedt;}
    public String getRptversion() {return this.rptversion;}
    public void setRptversion(String rptversion) {this.rptversion = rptversion;}
    public String getRptremark() {return this.rptremark;}
    public void setRptremark(String rptremark) {this.rptremark = rptremark;}

	public String getResultsql() {
		return resultsql;
	}

	public void setResultsql(String resultsql) {
		this.resultsql = resultsql;
	}

	public String toString() {
        return new ToStringBuilder(this)
            .append("reportid", getReportid())
            .append("reportname", getReportname())
            .append("filename", getFilename())
            .append("rptmaker", getRptmaker())
            .append("rptmakedt", getRptmakedt())
            .append("rptversion", getRptversion())
            .append("rptremark", getRptremark())
            .append("resultsql", getResultsql())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof Ebsdynimpbpara) ) return false;
        Ebsdynimpbpara castOther = (Ebsdynimpbpara) other;
        return new EqualsBuilder()
            .append(this.getReportid(), castOther.getReportid())
            .append(this.getReportname(), castOther.getReportname())
            .append(this.getFilename(), castOther.getFilename())
            .append(this.getRptmaker(), castOther.getRptmaker())
            .append(this.getRptmakedt(), castOther.getRptmakedt())
            .append(this.getRptversion(), castOther.getRptversion())
            .append(this.getRptremark(), castOther.getRptremark())
            .append(this.getResultsql(), castOther.getResultsql())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getReportid())
            .append(getReportname())
            .append(getFilename())
            .append(getRptmaker())
            .append(getRptmakedt())
            .append(getRptversion())
            .append(getRptremark())
            .append(getResultsql())
            .toHashCode();
    }
}
