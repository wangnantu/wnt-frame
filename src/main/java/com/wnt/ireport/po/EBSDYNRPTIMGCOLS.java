package com.wnt.ireport.po;

import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class EbsDynrptImgcols implements Serializable {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private String reportid;

    /** identifier field */
    private String orderid;

    /** identifier field */
    private String fieldid;

    /** identifier field */
    private String paramname;

    /** identifier field */
    private String reportjxmlm;

    /** identifier field */
    private String tname;

    /** identifier field */
    private String fieldname;

    /** identifier field */
    private String imgcolor;

    /** identifier field */
    private String borderwd;

    /** identifier field */
    private String labelname;

    /** identifier field */
    private String fieldidc;

    /** identifier field */
    private String sheetindex;

    /** full constructor */
    public EbsDynrptImgcols(String reportid, String orderid, String fieldid, String paramname, String reportjxmlm, String tname, String fieldname, String imgcolor, String borderwd, String labelname, String fieldidc, String sheetindex) {
        this.reportid = reportid;
        this.orderid = orderid;
        this.fieldid = fieldid;
        this.paramname = paramname;
        this.reportjxmlm = reportjxmlm;
        this.tname = tname;
        this.fieldname = fieldname;
        this.imgcolor = imgcolor;
        this.borderwd = borderwd;
        this.labelname = labelname;
        this.fieldidc = fieldidc;
        this.sheetindex = sheetindex;
    }
    
    public EbsDynrptImgcols(String reportid, String orderid,String paramname, String imgcolor, String borderwd, String labelname ) {
        this.reportid = reportid;
        this.orderid = orderid;
        this.paramname = paramname;
        this.imgcolor = imgcolor;
        this.borderwd = borderwd;
        this.labelname = labelname;
    }

    /** default constructor */
    public EbsDynrptImgcols() {
    }

    public String getReportid() {
        return this.reportid;
    }

    public void setReportid(String reportid) {
        this.reportid = reportid;
    }

    public String getOrderid() {
        return this.orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getFieldid() {
        return this.fieldid;
    }

    public void setFieldid(String fieldid) {
        this.fieldid = fieldid;
    }

    public String getParamname() {
        return this.paramname;
    }

    public void setParamname(String paramname) {
        this.paramname = paramname;
    }

    public String getReportjxmlm() {
        return this.reportjxmlm;
    }

    public void setReportjxmlm(String reportjxmlm) {
        this.reportjxmlm = reportjxmlm;
    }

    public String getTname() {
        return this.tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getFieldname() {
        return this.fieldname;
    }

    public void setFieldname(String fieldname) {
        this.fieldname = fieldname;
    }

    public String getImgcolor() {
        return this.imgcolor;
    }

    public void setImgcolor(String imgcolor) {
        this.imgcolor = imgcolor;
    }

    public String getBorderwd() {
        return this.borderwd;
    }

    public void setBorderwd(String borderwd) {
        this.borderwd = borderwd;
    }

    public String getLabelname() {
        return this.labelname;
    }

    public void setLabelname(String labelname) {
        this.labelname = labelname;
    }

    public String getFieldidc() {
        return this.fieldidc;
    }

    public void setFieldidc(String fieldidc) {
        this.fieldidc = fieldidc;
    }

    public String getSheetindex() {
        return this.sheetindex;
    }

    public void setSheetindex(String sheetindex) {
        this.sheetindex = sheetindex;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("reportid", getReportid())
            .append("orderid", getOrderid())
            .append("fieldid", getFieldid())
            .append("paramname", getParamname())
            .append("reportjxmlm", getReportjxmlm())
            .append("tname", getTname())
            .append("fieldname", getFieldname())
            .append("imgcolor", getImgcolor())
            .append("borderwd", getBorderwd())
            .append("labelname", getLabelname())
            .append("fieldidc", getFieldidc())
            .append("sheetindex", getSheetindex())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof EbsDynrptImgcols) ) return false;
        EbsDynrptImgcols castOther = (EbsDynrptImgcols) other;
        return new EqualsBuilder()
            .append(this.getReportid(), castOther.getReportid())
            .append(this.getOrderid(), castOther.getOrderid())
            .append(this.getFieldid(), castOther.getFieldid())
            .append(this.getParamname(), castOther.getParamname())
            .append(this.getReportjxmlm(), castOther.getReportjxmlm())
            .append(this.getTname(), castOther.getTname())
            .append(this.getFieldname(), castOther.getFieldname())
            .append(this.getImgcolor(), castOther.getImgcolor())
            .append(this.getBorderwd(), castOther.getBorderwd())
            .append(this.getLabelname(), castOther.getLabelname())
            .append(this.getFieldidc(), castOther.getFieldidc())
            .append(this.getSheetindex(), castOther.getSheetindex())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getReportid())
            .append(getOrderid())
            .append(getFieldid())
            .append(getParamname())
            .append(getReportjxmlm())
            .append(getTname())
            .append(getFieldname())
            .append(getImgcolor())
            .append(getBorderwd())
            .append(getLabelname())
            .append(getFieldidc())
            .append(getSheetindex())
            .toHashCode();
    }

}
