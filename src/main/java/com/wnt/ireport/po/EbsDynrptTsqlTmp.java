package com.wnt.ireport.po;

import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class EbsDynrptTsqlTmp implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private String reportid;

    /** identifier field */
    private String tableName;

    /** identifier field */
    private String tname;

    /** identifier field */
    private String sqlstr;

    /** full constructor */
    public EbsDynrptTsqlTmp(String reportid, String tableName, String tname, String sqlstr) {
        this.reportid = reportid;
        this.tableName = tableName;
        this.tname = tname;
        this.sqlstr = sqlstr;
    }

    /** default constructor */
    public EbsDynrptTsqlTmp() {
    }

    public String getReportid() {
        return this.reportid;
    }

    public void setReportid(String reportid) {
        this.reportid = reportid;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTname() {
        return this.tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public Object getSqlstr() {
        return this.sqlstr;
    }

    public void setSqlstr(String sqlstr) {
        this.sqlstr = sqlstr;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("reportid", getReportid())
            .append("tableName", getTableName())
            .append("tname", getTname())
            .append("sqlstr", getSqlstr())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof EbsDynrptTsqlTmp) ) return false;
        EbsDynrptTsqlTmp castOther = (EbsDynrptTsqlTmp) other;
        return new EqualsBuilder()
            .append(this.getReportid(), castOther.getReportid())
            .append(this.getTableName(), castOther.getTableName())
            .append(this.getTname(), castOther.getTname())
            .append(this.getSqlstr(), castOther.getSqlstr())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getReportid())
            .append(getTableName())
            .append(getTname())
            .append(getSqlstr())
            .toHashCode();
    }

}
