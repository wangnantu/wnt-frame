package com.wnt.ireport.po;

import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Ebsdynrptmsgparatmp implements Serializable {
    
    private String reportid;
    
    private String remark;
    
    private String issendmsg;
    
    private String useid;
    
    private String upwd;
    
    private String appid;
    
    private String content;

    public Ebsdynrptmsgparatmp(String reportid,String remark,String issendmsg,String useid,String upwd,String appid,String content) {
        this.reportid = reportid;
        this.remark = remark;
        this.issendmsg = issendmsg;
        this.useid = useid;
        this.upwd = upwd;
        this.appid = appid;
        this.content = content;
    }

    public Ebsdynrptmsgparatmp() {}
    public String getReportid() {return this.reportid;}
    public void setReportid(String reportid) {this.reportid = reportid;}
    public String getRemark() {return this.remark;}
    public void setRemark(String remark) {this.remark = remark;}
    public String getIssendmsg() {return this.issendmsg;}
    public void setIssendmsg(String issendmsg) {this.issendmsg = issendmsg;}
    public String getUseid() {return this.useid;}
    public void setUseid(String useid) {this.useid = useid;}
    public String getUpwd() {return this.upwd;}
    public void setUpwd(String upwd) {this.upwd = upwd;}
    public String getAppid() {return this.appid;}
    public void setAppid(String appid) {this.appid = appid;}
    public String getContent() {return this.content;}
    public void setContent(String content) {this.content = content;}

    public String toString() {
        return new ToStringBuilder(this)
            .append("reportid", getReportid())
            .append("remark", getRemark())
            .append("issendmsg", getIssendmsg())
            .append("useid", getUseid())
            .append("upwd", getUpwd())
            .append("appid", getAppid())
            .append("content", getContent())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof Ebsdynrptmsgparatmp) ) return false;
        Ebsdynrptmsgparatmp castOther = (Ebsdynrptmsgparatmp) other;
        return new EqualsBuilder()
            .append(this.getReportid(), castOther.getReportid())
            .append(this.getRemark(), castOther.getRemark())
            .append(this.getIssendmsg(), castOther.getIssendmsg())
            .append(this.getUseid(), castOther.getUseid())
            .append(this.getUpwd(), castOther.getUpwd())
            .append(this.getAppid(), castOther.getAppid())
            .append(this.getContent(), castOther.getContent())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getReportid())
            .append(getRemark())
            .append(getIssendmsg())
            .append(getUseid())
            .append(getUpwd())
            .append(getAppid())
            .append(getContent())
            .toHashCode();
    }
}
