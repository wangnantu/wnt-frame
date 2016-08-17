package com.wnt.ireport.po;

import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class EbsDynrptJxml implements Serializable {

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

    /** full constructor */
    public EbsDynrptJxml(String reportid, String reportidjxmlm, String reportidjxml, String ismain) {
        this.reportid = reportid;
        this.reportidjxmlm = reportidjxmlm;
        this.reportidjxml = reportidjxml;
        this.ismain = ismain;
    }

    /** default constructor */
    public EbsDynrptJxml() {
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

    public String toString() {
        return new ToStringBuilder(this)
            .append("reportid", getReportid())
            .append("reportidjxmlm", getReportidjxmlm())
            .append("reportidjxml", getReportidjxml())
            .append("ismain", getIsmain())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof EbsDynrptJxml) ) return false;
        EbsDynrptJxml castOther = (EbsDynrptJxml) other;
        return new EqualsBuilder()
            .append(this.getReportid(), castOther.getReportid())
            .append(this.getReportidjxmlm(), castOther.getReportidjxmlm())
            .append(this.getReportidjxml(), castOther.getReportidjxml())
            .append(this.getIsmain(), castOther.getIsmain())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getReportid())
            .append(getReportidjxmlm())
            .append(getReportidjxml())
            .append(getIsmain())
            .toHashCode();
    }

}
