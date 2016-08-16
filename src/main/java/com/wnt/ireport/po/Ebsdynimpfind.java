package com.wnt.ireport.po;

import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Ebsdynimpfind implements Serializable {
    
    private String reportid;
    
    private String sheetname;
    
    private String lbeginscope;
    
    private String lendscope;
    
    private String lscopecontent;
    
    private int loffset;
    
    private int lnth;
    
    private String tbeginscope;
    
    private String tendscope;
    
    private String tscopecontent;
    
    private int toffset;
    
    private int tnth;
    
    private String rbeginscope;
    
    private String rendscope;
    
    private String rscopecontent;
    
    private int roffset;
    
    private int rnth;
    
    private String bbeginscope;
    
    private String bendscope;
    
    private String bscopecontent;
    
    private int boffset;
    
    private int bnth;
    
    private int dtlbegin;
    
    private String dtlend;

    public Ebsdynimpfind(String reportid,String sheetname,String lbeginscope,String lendscope,String lscopecontent,int loffset,int lnth,String tbeginscope,String tendscope,String tscopecontent,int toffset,int tnth,String rbeginscope,String rendscope,String rscopecontent,int roffset,int rnth,String bbeginscope,String bendscope,String bscopecontent,int boffset,int bnth,int dtlbegin,String dtlend) {
        this.reportid = reportid;
        this.sheetname = sheetname;
        this.lbeginscope = lbeginscope;
        this.lendscope = lendscope;
        this.lscopecontent = lscopecontent;
        this.loffset = loffset;
        this.lnth = lnth;
        this.tbeginscope = tbeginscope;
        this.tendscope = tendscope;
        this.tscopecontent = tscopecontent;
        this.toffset = toffset;
        this.tnth = tnth;
        this.rbeginscope = rbeginscope;
        this.rendscope = rendscope;
        this.rscopecontent = rscopecontent;
        this.roffset = roffset;
        this.rnth = rnth;
        this.bbeginscope = bbeginscope;
        this.bendscope = bendscope;
        this.bscopecontent = bscopecontent;
        this.boffset = boffset;
        this.bnth = bnth;
        this.dtlbegin = dtlbegin;
        this.dtlend = dtlend;
    }

    public Ebsdynimpfind() {}
    public String getReportid() {return this.reportid;}
    public void setReportid(String reportid) {this.reportid = reportid;}
    public String getSheetname() {return this.sheetname;}
    public void setSheetname(String sheetname) {this.sheetname = sheetname;}
    public String getLbeginscope() {return this.lbeginscope;}
    public void setLbeginscope(String lbeginscope) {this.lbeginscope = lbeginscope;}
    public String getLendscope() {return this.lendscope;}
    public void setLendscope(String lendscope) {this.lendscope = lendscope;}
    public String getLscopecontent() {return this.lscopecontent;}
    public void setLscopecontent(String lscopecontent) {this.lscopecontent = lscopecontent;}
    public int getLoffset() {return this.loffset;}
    public void setLoffset(int loffset) {this.loffset = loffset;}
    public int getLnth() {return this.lnth;}
    public void setLnth(int lnth) {this.lnth = lnth;}
    public String getTbeginscope() {return this.tbeginscope;}
    public void setTbeginscope(String tbeginscope) {this.tbeginscope = tbeginscope;}
    public String getTendscope() {return this.tendscope;}
    public void setTendscope(String tendscope) {this.tendscope = tendscope;}
    public String getTscopecontent() {return this.tscopecontent;}
    public void setTscopecontent(String tscopecontent) {this.tscopecontent = tscopecontent;}
    public int getToffset() {return this.toffset;}
    public void setToffset(int toffset) {this.toffset = toffset;}
    public int getTnth() {return this.tnth;}
    public void setTnth(int tnth) {this.tnth = tnth;}
    public String getRbeginscope() {return this.rbeginscope;}
    public void setRbeginscope(String rbeginscope) {this.rbeginscope = rbeginscope;}
    public String getRendscope() {return this.rendscope;}
    public void setRendscope(String rendscope) {this.rendscope = rendscope;}
    public String getRscopecontent() {return this.rscopecontent;}
    public void setRscopecontent(String rscopecontent) {this.rscopecontent = rscopecontent;}
    public int getRoffset() {return this.roffset;}
    public void setRoffset(int roffset) {this.roffset = roffset;}
    public int getRnth() {return this.rnth;}
    public void setRnth(int rnth) {this.rnth = rnth;}
    public String getBbeginscope() {return this.bbeginscope;}
    public void setBbeginscope(String bbeginscope) {this.bbeginscope = bbeginscope;}
    public String getBendscope() {return this.bendscope;}
    public void setBendscope(String bendscope) {this.bendscope = bendscope;}
    public String getBscopecontent() {return this.bscopecontent;}
    public void setBscopecontent(String bscopecontent) {this.bscopecontent = bscopecontent;}
    public int getBoffset() {return this.boffset;}
    public void setBoffset(int boffset) {this.boffset = boffset;}
    public int getBnth() {return this.bnth;}
    public void setBnth(int bnth) {this.bnth = bnth;}
    public int getDtlbegin() {return this.dtlbegin;}
    public void setDtlbegin(int dtlbegin) {this.dtlbegin = dtlbegin;}
    public String getDtlend() {return this.dtlend;}
    public void setDtlend(String dtlend) {this.dtlend = dtlend;}

    public String toString() {
        return new ToStringBuilder(this)
            .append("reportid", getReportid())
            .append("sheetname", getSheetname())
            .append("lbeginscope", getLbeginscope())
            .append("lendscope", getLendscope())
            .append("lscopecontent", getLscopecontent())
            .append("loffset", getLoffset())
            .append("lnth", getLnth())
            .append("tbeginscope", getTbeginscope())
            .append("tendscope", getTendscope())
            .append("tscopecontent", getTscopecontent())
            .append("toffset", getToffset())
            .append("tnth", getTnth())
            .append("rbeginscope", getRbeginscope())
            .append("rendscope", getRendscope())
            .append("rscopecontent", getRscopecontent())
            .append("roffset", getRoffset())
            .append("rnth", getRnth())
            .append("bbeginscope", getBbeginscope())
            .append("bendscope", getBendscope())
            .append("bscopecontent", getBscopecontent())
            .append("boffset", getBoffset())
            .append("bnth", getBnth())
            .append("dtlbegin", getDtlbegin())
            .append("dtlend", getDtlend())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof Ebsdynimpfind) ) return false;
        Ebsdynimpfind castOther = (Ebsdynimpfind) other;
        return new EqualsBuilder()
            .append(this.getReportid(), castOther.getReportid())
            .append(this.getSheetname(), castOther.getSheetname())
            .append(this.getLbeginscope(), castOther.getLbeginscope())
            .append(this.getLendscope(), castOther.getLendscope())
            .append(this.getLscopecontent(), castOther.getLscopecontent())
            .append(this.getLoffset(), castOther.getLoffset())
            .append(this.getLnth(), castOther.getLnth())
            .append(this.getTbeginscope(), castOther.getTbeginscope())
            .append(this.getTendscope(), castOther.getTendscope())
            .append(this.getTscopecontent(), castOther.getTscopecontent())
            .append(this.getToffset(), castOther.getToffset())
            .append(this.getTnth(), castOther.getTnth())
            .append(this.getRbeginscope(), castOther.getRbeginscope())
            .append(this.getRendscope(), castOther.getRendscope())
            .append(this.getRscopecontent(), castOther.getRscopecontent())
            .append(this.getRoffset(), castOther.getRoffset())
            .append(this.getRnth(), castOther.getRnth())
            .append(this.getBbeginscope(), castOther.getBbeginscope())
            .append(this.getBendscope(), castOther.getBendscope())
            .append(this.getBscopecontent(), castOther.getBscopecontent())
            .append(this.getBoffset(), castOther.getBoffset())
            .append(this.getBnth(), castOther.getBnth())
            .append(this.getDtlbegin(), castOther.getDtlbegin())
            .append(this.getDtlend(), castOther.getDtlend())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getReportid())
            .append(getSheetname())
            .append(getLbeginscope())
            .append(getLendscope())
            .append(getLscopecontent())
            .append(getLoffset())
            .append(getLnth())
            .append(getTbeginscope())
            .append(getTendscope())
            .append(getTscopecontent())
            .append(getToffset())
            .append(getTnth())
            .append(getRbeginscope())
            .append(getRendscope())
            .append(getRscopecontent())
            .append(getRoffset())
            .append(getRnth())
            .append(getBbeginscope())
            .append(getBendscope())
            .append(getBscopecontent())
            .append(getBoffset())
            .append(getBnth())
            .append(getDtlbegin())
            .append(getDtlend())
            .toHashCode();
    }
}
