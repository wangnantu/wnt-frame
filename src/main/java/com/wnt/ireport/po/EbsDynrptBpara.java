package com.wnt.ireport.po;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class EbsDynrptBpara implements Serializable {

    /** identifier field */
    private String reportid;

    /** identifier field */
    private String reportname;

    /** identifier field */
    private String filename;

    /** identifier field */
    private String rptmaker;

    /** identifier field */
    private String rptmakedt;

    /** identifier field */
    private String rptversion;

    /** identifier field */
    private String rptremark;

    private String isouttxt;
    
    private String pixs;
    
    private String emptsperpix;
    
    private String fildemps;
    
    private String xlstype;
    
    /** full constructor */
    public EbsDynrptBpara(String reportid, String reportname, String filename, String rptmaker, String rptmakedt, String rptversion, String rptremark,String isouttxt,String pixs,String emptsperpix,String fildemps,String xlstype) {
        this.reportid = reportid;
        this.reportname = reportname;
        this.filename = filename;
        this.rptmaker = rptmaker;
        this.rptmakedt = rptmakedt;
        this.rptversion = rptversion;
        this.rptremark = rptremark;
        this.isouttxt = isouttxt;
        this.pixs = pixs;
        this.emptsperpix = emptsperpix;
        this.fildemps = fildemps;
        this.xlstype = xlstype;
    }

    /** default constructor */
    public EbsDynrptBpara() {
    }

    public String getReportid() {
        return this.reportid;
    }

    public void setReportid(String reportid) {
        this.reportid = reportid;
    }

    public String getReportname() {
        return this.reportname;
    }

    public void setReportname(String reportname) {
        this.reportname = reportname;
    }

    public String getFilename() {
        return this.filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getRptmaker() {
        return this.rptmaker;
    }

    public void setRptmaker(String rptmaker) {
        this.rptmaker = rptmaker;
    }

    public String getRptmakedt() {
        return this.rptmakedt;
    }

    public void setRptmakedt(String rptmakedt) {
        this.rptmakedt = rptmakedt;
    }

    public String getRptversion() {
        return this.rptversion;
    }

    public void setRptversion(String rptversion) {
        this.rptversion = rptversion;
    }

    public String getRptremark() {
        return this.rptremark;
    }

    public void setRptremark(String rptremark) {
        this.rptremark = rptremark;
    }

    public String getIsouttxt() {return this.isouttxt;}
    public void setIsouttxt(String isouttxt) {this.isouttxt = isouttxt;}
    public String getPixs() {return this.pixs;}
    public void setPixs(String pixs) {this.pixs = pixs;}
    public String getEmptsperpix() {return this.emptsperpix;}
    public void setEmptsperpix(String emptsperpix) {this.emptsperpix = emptsperpix;}
    public String getFildemps() {return this.fildemps;}
    public void setFildemps(String fildemps) {this.fildemps = fildemps;}
    public String getXlstype() {return this.xlstype;}
    public void setXlstype(String xlstype) {this.xlstype = xlstype;}
    
    public String toString() {
        return new ToStringBuilder(this)
            .append("reportid", getReportid())
            .append("reportname", getReportname())
            .append("filename", getFilename())
            .append("rptmaker", getRptmaker())
            .append("rptmakedt", getRptmakedt())
            .append("rptversion", getRptversion())
            .append("rptremark", getRptremark())
            .append("isouttxt", getIsouttxt())
            .append("pixs", getPixs())
            .append("emptsperpix", getEmptsperpix())
            .append("fildemps", getFildemps())
            .append("xlstype", getXlstype())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof EbsDynrptBpara) ) return false;
        EbsDynrptBpara castOther = (EbsDynrptBpara) other;
        return new EqualsBuilder()
            .append(this.getReportid(), castOther.getReportid())
            .append(this.getReportname(), castOther.getReportname())
            .append(this.getFilename(), castOther.getFilename())
            .append(this.getRptmaker(), castOther.getRptmaker())
            .append(this.getRptmakedt(), castOther.getRptmakedt())
            .append(this.getRptversion(), castOther.getRptversion())
            .append(this.getRptremark(), castOther.getRptremark())
            .append(this.getIsouttxt(), castOther.getIsouttxt())
            .append(this.getPixs(), castOther.getPixs())
            .append(this.getEmptsperpix(), castOther.getEmptsperpix())
            .append(this.getFildemps(), castOther.getFildemps())
            .append(this.getXlstype(), castOther.getXlstype())
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
            .append(getIsouttxt())
            .append(getPixs())
            .append(getEmptsperpix())
            .append(getFildemps())
            .append(getXlstype())
            .toHashCode();
    }

}
