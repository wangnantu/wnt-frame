package com.wnt.ireport.po;

import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Ebsdynimphpara implements Serializable {
    
    private String reportid;
    
    private String labelname;
    
    private String iptctrlname;
    
    private String iptctrltype;
    
    private String iptctrldef;
    
    private String iptctrllist;
    
    private String iptisnull;
    
    private int orderid;
    
    private String iptpardef;
    
    private String iptreserve1;
    
    private String iptreserve2;
    
    private String iptreserve3;

    public Ebsdynimphpara(String reportid,String labelname,String iptctrlname,String iptctrltype,String iptctrldef,String iptctrllist,String iptisnull,int orderid,String iptpardef,String iptreserve1,String iptreserve2,String iptreserve3) {
        this.reportid = reportid;
        this.labelname = labelname;
        this.iptctrlname = iptctrlname;
        this.iptctrltype = iptctrltype;
        this.iptctrldef = iptctrldef;
        this.iptctrllist = iptctrllist;
        this.iptisnull = iptisnull;
        this.orderid = orderid;
        this.iptpardef = iptpardef;
        this.iptreserve1 = iptreserve1;
        this.iptreserve2 = iptreserve2;
        this.iptreserve3 = iptreserve3;
    }

    public Ebsdynimphpara() {}
    public String getReportid() {return this.reportid;}
    public void setReportid(String reportid) {this.reportid = reportid;}
    public String getLabelname() {return this.labelname;}
    public void setLabelname(String labelname) {this.labelname = labelname;}
    public String getIptctrlname() {return this.iptctrlname;}
    public void setIptctrlname(String iptctrlname) {this.iptctrlname = iptctrlname;}
    public String getIptctrltype() {return this.iptctrltype;}
    public void setIptctrltype(String iptctrltype) {this.iptctrltype = iptctrltype;}
    public String getIptctrldef() {return this.iptctrldef;}
    public void setIptctrldef(String iptctrldef) {this.iptctrldef = iptctrldef;}
    public String getIptctrllist() {return this.iptctrllist;}
    public void setIptctrllist(String iptctrllist) {this.iptctrllist = iptctrllist;}
    public String getIptisnull() {return this.iptisnull;}
    public void setIptisnull(String iptisnull) {this.iptisnull = iptisnull;}
    public int getOrderid() {return this.orderid;}
    public void setOrderid(int orderid) {this.orderid = orderid;}
    public String getIptpardef() {return this.iptpardef;}
    public void setIptpardef(String iptpardef) {this.iptpardef = iptpardef;}
    public String getIptreserve1() {return this.iptreserve1;}
    public void setIptreserve1(String iptreserve1) {this.iptreserve1 = iptreserve1;}
    public String getIptreserve2() {return this.iptreserve2;}
    public void setIptreserve2(String iptreserve2) {this.iptreserve2 = iptreserve2;}
    public String getIptreserve3() {return this.iptreserve3;}
    public void setIptreserve3(String iptreserve3) {this.iptreserve3 = iptreserve3;}

    public String toString() {
        return new ToStringBuilder(this)
            .append("reportid", getReportid())
            .append("labelname", getLabelname())
            .append("iptctrlname", getIptctrlname())
            .append("iptctrltype", getIptctrltype())
            .append("iptctrldef", getIptctrldef())
            .append("iptctrllist", getIptctrllist())
            .append("iptisnull", getIptisnull())
            .append("orderid", getOrderid())
            .append("iptpardef", getIptpardef())
            .append("iptreserve1", getIptreserve1())
            .append("iptreserve2", getIptreserve2())
            .append("iptreserve3", getIptreserve3())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof Ebsdynimphpara) ) return false;
        Ebsdynimphpara castOther = (Ebsdynimphpara) other;
        return new EqualsBuilder()
            .append(this.getReportid(), castOther.getReportid())
            .append(this.getLabelname(), castOther.getLabelname())
            .append(this.getIptctrlname(), castOther.getIptctrlname())
            .append(this.getIptctrltype(), castOther.getIptctrltype())
            .append(this.getIptctrldef(), castOther.getIptctrldef())
            .append(this.getIptctrllist(), castOther.getIptctrllist())
            .append(this.getIptisnull(), castOther.getIptisnull())
            .append(this.getOrderid(), castOther.getOrderid())
            .append(this.getIptpardef(), castOther.getIptpardef())
            .append(this.getIptreserve1(), castOther.getIptreserve1())
            .append(this.getIptreserve2(), castOther.getIptreserve2())
            .append(this.getIptreserve3(), castOther.getIptreserve3())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getReportid())
            .append(getLabelname())
            .append(getIptctrlname())
            .append(getIptctrltype())
            .append(getIptctrldef())
            .append(getIptctrllist())
            .append(getIptisnull())
            .append(getOrderid())
            .append(getIptpardef())
            .append(getIptreserve1())
            .append(getIptreserve2())
            .append(getIptreserve3())
            .toHashCode();
    }
}
