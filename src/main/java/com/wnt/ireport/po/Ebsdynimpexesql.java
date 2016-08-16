package com.wnt.ireport.po;

import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Ebsdynimpexesql implements Serializable {
    
    private String reportid;
    
    private String impsql;
    
    private int imporder;
    
    private int impsuborder;

    public Ebsdynimpexesql(String reportid,String impsql,int imporder,int impsuborder) {
        this.reportid = reportid;
        this.impsql = impsql;
        this.imporder = imporder;
        this.impsuborder = impsuborder;
    }

    public Ebsdynimpexesql() {}
    public String getReportid() {return this.reportid;}
    public void setReportid(String reportid) {this.reportid = reportid;}
    public String getImpsql() {return this.impsql;}
    public void setImpsql(String impsql) {this.impsql = impsql;}
    public int getImporder() {return this.imporder;}
    public void setImporder(int imporder) {this.imporder = imporder;}
    public int getImpsuborder() {return this.impsuborder;}
    public void setImpsuborder(int impsuborder) {this.impsuborder = impsuborder;}

    public String toString() {
        return new ToStringBuilder(this)
            .append("reportid", getReportid())
            .append("impsql", getImpsql())
            .append("imporder", getImporder())
            .append("impsuborder", getImpsuborder())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof Ebsdynimpexesql) ) return false;
        Ebsdynimpexesql castOther = (Ebsdynimpexesql) other;
        return new EqualsBuilder()
            .append(this.getReportid(), castOther.getReportid())
            .append(this.getImpsql(), castOther.getImpsql())
            .append(this.getImporder(), castOther.getImporder())
            .append(this.getImpsuborder(), castOther.getImpsuborder())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getReportid())
            .append(getImpsql())
            .append(getImporder())
            .append(getImpsuborder())
            .toHashCode();
    }
}
