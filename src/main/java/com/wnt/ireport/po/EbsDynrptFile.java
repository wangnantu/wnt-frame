package com.wnt.ireport.po;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class EbsDynrptFile implements Serializable {

    /** identifier field */
    private String reportid;

    /** identifier field */
    private String subreportid;

    /** identifier field */
    private String sheetname;

    /** full constructor */
    public EbsDynrptFile(String reportid, String subreportid, String sheetname) {
        this.reportid = reportid;
        this.subreportid = subreportid;
        this.sheetname = sheetname;
    }

    /** default constructor */
    public EbsDynrptFile() {
    }

    public String getReportid() {
        return this.reportid;
    }

    public void setReportid(String reportid) {
        this.reportid = reportid;
    }

    public String getSubreportid() {
        return this.subreportid;
    }

    public void setSubreportid(String subreportid) {
        this.subreportid = subreportid;
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
            .append("subreportid", getSubreportid())
            .append("sheetname", getSheetname())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof EbsDynrptFile) ) return false;
        EbsDynrptFile castOther = (EbsDynrptFile) other;
        return new EqualsBuilder()
            .append(this.getReportid(), castOther.getReportid())
            .append(this.getSubreportid(), castOther.getSubreportid())
            .append(this.getSheetname(), castOther.getSheetname())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getReportid())
            .append(getSubreportid())
            .append(getSheetname())
            .toHashCode();
    }

}
