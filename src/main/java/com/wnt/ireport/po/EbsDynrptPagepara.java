package com.wnt.ireport.po;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class EbsDynrptPagepara implements Serializable {

    /** identifier field */
    private String reportid;

    /** identifier field */
    private String reportsheetno;

    /** identifier field */
    private String sqlname;

    /** identifier field */
    private String pagecount;

    /** identifier field */
    private String rptremark;

    /** full constructor */
    public EbsDynrptPagepara(String reportid, String reportsheetno, String sqlname, String pagecount, String rptremark) {
        this.reportid = reportid;
        this.reportsheetno = reportsheetno;
        this.sqlname = sqlname;
        this.pagecount = pagecount;
        this.rptremark = rptremark;
    }

    /** default constructor */
    public EbsDynrptPagepara() {
    }

    public String getReportid() {
        return this.reportid;
    }

    public void setReportid(String reportid) {
        this.reportid = reportid;
    }

    public String getReportsheetno() {
        return this.reportsheetno;
    }

    public void setReportsheetno(String reportsheetno) {
        this.reportsheetno = reportsheetno;
    }

    public String getSqlname() {
        return this.sqlname;
    }

    public void setSqlname(String sqlname) {
        this.sqlname = sqlname;
    }

    public String getPagecount() {
        return this.pagecount;
    }

    public void setPagecount(String pagecount) {
        this.pagecount = pagecount;
    }

    public String getRptremark() {
        return this.rptremark;
    }

    public void setRptremark(String rptremark) {
        this.rptremark = rptremark;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("reportid", getReportid())
            .append("reportsheetno", getReportsheetno())
            .append("sqlname", getSqlname())
            .append("pagecount", getPagecount())
            .append("rptremark", getRptremark())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof EbsDynrptPagepara) ) return false;
        EbsDynrptPagepara castOther = (EbsDynrptPagepara) other;
        return new EqualsBuilder()
            .append(this.getReportid(), castOther.getReportid())
            .append(this.getReportsheetno(), castOther.getReportsheetno())
            .append(this.getSqlname(), castOther.getSqlname())
            .append(this.getPagecount(), castOther.getPagecount())
            .append(this.getRptremark(), castOther.getRptremark())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getReportid())
            .append(getReportsheetno())
            .append(getSqlname())
            .append(getPagecount())
            .append(getRptremark())
            .toHashCode();
    }

}
