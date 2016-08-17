package com.wnt.ireport.po;

import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class EbsDynrptPrintpara implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private String reportid;

    /** identifier field */
    private String reportsheetno;

    /** identifier field */
    private String printdirect;

    /** identifier field */
    private String printalign;

    /** identifier field */
    private String autowidth;

    /** identifier field */
    private String pagefoot;

    /** identifier field */
    private String rptremark;

    /** full constructor */
    public EbsDynrptPrintpara(String reportid, String reportsheetno, String printdirect, String printalign, String autowidth, String pagefoot, String rptremark) {
        this.reportid = reportid;
        this.reportsheetno = reportsheetno;
        this.printdirect = printdirect;
        this.printalign = printalign;
        this.autowidth = autowidth;
        this.pagefoot = pagefoot;
        this.rptremark = rptremark;
    }

    /** default constructor */
    public EbsDynrptPrintpara() {
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

    public String getPrintdirect() {
        return this.printdirect;
    }

    public void setPrintdirect(String printdirect) {
        this.printdirect = printdirect;
    }

    public String getPrintalign() {
        return this.printalign;
    }

    public void setPrintalign(String printalign) {
        this.printalign = printalign;
    }

    public String getAutowidth() {
        return this.autowidth;
    }

    public void setAutowidth(String autowidth) {
        this.autowidth = autowidth;
    }

    public String getPagefoot() {
        return this.pagefoot;
    }

    public void setPagefoot(String pagefoot) {
        this.pagefoot = pagefoot;
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
            .append("printdirect", getPrintdirect())
            .append("printalign", getPrintalign())
            .append("autowidth", getAutowidth())
            .append("pagefoot", getPagefoot())
            .append("rptremark", getRptremark())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof EbsDynrptPrintpara) ) return false;
        EbsDynrptPrintpara castOther = (EbsDynrptPrintpara) other;
        return new EqualsBuilder()
            .append(this.getReportid(), castOther.getReportid())
            .append(this.getReportsheetno(), castOther.getReportsheetno())
            .append(this.getPrintdirect(), castOther.getPrintdirect())
            .append(this.getPrintalign(), castOther.getPrintalign())
            .append(this.getAutowidth(), castOther.getAutowidth())
            .append(this.getPagefoot(), castOther.getPagefoot())
            .append(this.getRptremark(), castOther.getRptremark())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getReportid())
            .append(getReportsheetno())
            .append(getPrintdirect())
            .append(getPrintalign())
            .append(getAutowidth())
            .append(getPagefoot())
            .append(getRptremark())
            .toHashCode();
    }

}
