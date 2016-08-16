package com.wnt.ireport.po;

import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Ebsdynimpcparam implements Serializable {
    
    private String reportid;
    
    private String cname;
    
    private String param;

    public Ebsdynimpcparam(String reportid,String cname,String param) {
        this.reportid = reportid;
        this.cname = cname;
        this.param = param;
    }

    public Ebsdynimpcparam() {}
    public String getReportid() {return this.reportid;}
    public void setReportid(String reportid) {this.reportid = reportid;}
    public String getCname() {return this.cname;}
    public void setCname(String cname) {this.cname = cname;}
    public String getParam() {return this.param;}
    public void setParam(String param) {this.param = param;}

    public String toString() {
        return new ToStringBuilder(this)
            .append("reportid", getReportid())
            .append("cname", getCname())
            .append("param", getParam())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof Ebsdynimpcparam) ) return false;
        Ebsdynimpcparam castOther = (Ebsdynimpcparam) other;
        return new EqualsBuilder()
            .append(this.getReportid(), castOther.getReportid())
            .append(this.getCname(), castOther.getCname())
            .append(this.getParam(), castOther.getParam())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getReportid())
            .append(getCname())
            .append(getParam())
            .toHashCode();
    }
}
