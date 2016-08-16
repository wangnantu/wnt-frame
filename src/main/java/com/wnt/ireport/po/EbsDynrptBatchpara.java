package com.wnt.ireport.po;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class EbsDynrptBatchpara implements Serializable {

    /** identifier field */
    private String reportid;

    /** identifier field */
    private String reportsheetno;

    /** identifier field */
    private String rptexptype;

    /** identifier field */
    private String rptbatchpar1;

    /** identifier field */
    private String rptbatchpar2;

    /** identifier field */
    private String rptbatchpar3;

    /** identifier field */
    private String rptbatchpar4;

    /** identifier field */
    private String rptbatchpartype;

    /** identifier field */
    private String rptdefindname;

    /** identifier field */
    private String rptdefindtype;

    /** identifier field */
    private String rptremark;

    /** full constructor */
    public EbsDynrptBatchpara(String reportid, String reportsheetno, String rptexptype, String rptbatchpar1, String rptbatchpar2, String rptbatchpar3, String rptbatchpar4, String rptbatchpartype, String rptdefindname, String rptdefindtype, String rptremark) {
        this.reportid = reportid;
        this.reportsheetno = reportsheetno;
        this.rptexptype = rptexptype;
        this.rptbatchpar1 = rptbatchpar1;
        this.rptbatchpar2 = rptbatchpar2;
        this.rptbatchpar3 = rptbatchpar3;
        this.rptbatchpar4 = rptbatchpar4;
        this.rptbatchpartype = rptbatchpartype;
        this.rptdefindname = rptdefindname;
        this.rptdefindtype = rptdefindtype;
        this.rptremark = rptremark;
    }

    /** default constructor */
    public EbsDynrptBatchpara() {
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

    public String getRptexptype() {
        return this.rptexptype;
    }

    public void setRptexptype(String rptexptype) {
        this.rptexptype = rptexptype;
    }

    public String getRptbatchpar1() {
        return this.rptbatchpar1;
    }

    public void setRptbatchpar1(String rptbatchpar1) {
        this.rptbatchpar1 = rptbatchpar1;
    }

    public String getRptbatchpar2() {
        return this.rptbatchpar2;
    }

    public void setRptbatchpar2(String rptbatchpar2) {
        this.rptbatchpar2 = rptbatchpar2;
    }

    public String getRptbatchpar3() {
        return this.rptbatchpar3;
    }

    public void setRptbatchpar3(String rptbatchpar3) {
        this.rptbatchpar3 = rptbatchpar3;
    }

    public String getRptbatchpar4() {
        return this.rptbatchpar4;
    }

    public void setRptbatchpar4(String rptbatchpar4) {
        this.rptbatchpar4 = rptbatchpar4;
    }

    public String getRptbatchpartype() {
        return this.rptbatchpartype;
    }

    public void setRptbatchpartype(String rptbatchpartype) {
        this.rptbatchpartype = rptbatchpartype;
    }

    public String getRptdefindname() {
        return this.rptdefindname;
    }

    public void setRptdefindname(String rptdefindname) {
        this.rptdefindname = rptdefindname;
    }

    public String getRptdefindtype() {
        return this.rptdefindtype;
    }

    public void setRptdefindtype(String rptdefindtype) {
        this.rptdefindtype = rptdefindtype;
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
            .append("rptexptype", getRptexptype())
            .append("rptbatchpar1", getRptbatchpar1())
            .append("rptbatchpar2", getRptbatchpar2())
            .append("rptbatchpar3", getRptbatchpar3())
            .append("rptbatchpar4", getRptbatchpar4())
            .append("rptbatchpartype", getRptbatchpartype())
            .append("rptdefindname", getRptdefindname())
            .append("rptdefindtype", getRptdefindtype())
            .append("rptremark", getRptremark())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof EbsDynrptBatchpara) ) return false;
        EbsDynrptBatchpara castOther = (EbsDynrptBatchpara) other;
        return new EqualsBuilder()
            .append(this.getReportid(), castOther.getReportid())
            .append(this.getReportsheetno(), castOther.getReportsheetno())
            .append(this.getRptexptype(), castOther.getRptexptype())
            .append(this.getRptbatchpar1(), castOther.getRptbatchpar1())
            .append(this.getRptbatchpar2(), castOther.getRptbatchpar2())
            .append(this.getRptbatchpar3(), castOther.getRptbatchpar3())
            .append(this.getRptbatchpar4(), castOther.getRptbatchpar4())
            .append(this.getRptbatchpartype(), castOther.getRptbatchpartype())
            .append(this.getRptdefindname(), castOther.getRptdefindname())
            .append(this.getRptdefindtype(), castOther.getRptdefindtype())
            .append(this.getRptremark(), castOther.getRptremark())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getReportid())
            .append(getReportsheetno())
            .append(getRptexptype())
            .append(getRptbatchpar1())
            .append(getRptbatchpar2())
            .append(getRptbatchpar3())
            .append(getRptbatchpar4())
            .append(getRptbatchpartype())
            .append(getRptdefindname())
            .append(getRptdefindtype())
            .append(getRptremark())
            .toHashCode();
    }

}
