package com.wnt.ireport.po;

import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class EbsVDynrptJxml implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private String reportid;

    /** identifier field */
    private String reportidjxmlm;

    /** identifier field */
    private String reportidjxml;

    /** identifier field */
    private String ismain;

    /** identifier field */
    private String sheetname;

    /** full constructor */
    public EbsVDynrptJxml(String reportid, String reportidjxmlm, String reportidjxml, String ismain, String sheetname) {
        this.reportid = reportid;
        this.reportidjxmlm = reportidjxmlm;
        this.reportidjxml = reportidjxml;
        this.ismain = ismain;
        this.sheetname = sheetname;
    }

    /** default constructor */
    public EbsVDynrptJxml() {
    }

    public String getReportid() {
        return this.reportid;
    }

    public void setReportid(String reportid) {
        this.reportid = reportid;
    }

    public String getReportidjxmlm() {
        return this.reportidjxmlm;
    }

    public void setReportidjxmlm(String reportidjxmlm) {
        this.reportidjxmlm = reportidjxmlm;
    }

    public String getReportidjxml() {
        return this.reportidjxml;
    }

    public void setReportidjxml(String reportidjxml) {
        this.reportidjxml = reportidjxml;
    }

    public String getIsmain() {
        return this.ismain;
    }

    public void setIsmain(String ismain) {
        this.ismain = ismain;
    }

    public String getSheetname() {
        return this.sheetname;
    }

    public void setSheetname(String sheetname) {
        this.sheetname = sheetname;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("reportid", getReportid())
            .append("reportidjxmlm", getReportidjxmlm())
            .append("reportidjxml", getReportidjxml())
            .append("ismain", getIsmain())
            .append("sheetname", getSheetname())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof EbsVDynrptJxml) ) return false;
        EbsVDynrptJxml castOther = (EbsVDynrptJxml) other;
        return new EqualsBuilder()
            .append(this.getReportid(), castOther.getReportid())
            .append(this.getReportidjxmlm(), castOther.getReportidjxmlm())
            .append(this.getReportidjxml(), castOther.getReportidjxml())
            .append(this.getIsmain(), castOther.getIsmain())
            .append(this.getSheetname(), castOther.getSheetname())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getReportid())
            .append(getReportidjxmlm())
            .append(getReportidjxml())
            .append(getIsmain())
            .append(getSheetname())
            .toHashCode();
    }

}
