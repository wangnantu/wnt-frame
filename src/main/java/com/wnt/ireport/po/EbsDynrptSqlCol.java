package com.wnt.ireport.po;

import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class EbsDynrptSqlCol implements Serializable {

    /** identifier field */
    private String reportid;

    /** identifier field */
    private String tableName;

    /** identifier field */
    private String columnName;

    /** identifier field */
    private String dataType;

    /** identifier field */
    private BigDecimal dataLength;

    /** identifier field */
    private BigDecimal dataPrecision;

    /** identifier field */
    private BigDecimal dataScale;

    /** full constructor */
    public EbsDynrptSqlCol(String reportid, String tableName, String columnName, String dataType, BigDecimal dataLength, BigDecimal dataPrecision, BigDecimal dataScale) {
        this.reportid = reportid;
        this.tableName = tableName;
        this.columnName = columnName;
        this.dataType = dataType;
        this.dataLength = dataLength;
        this.dataPrecision = dataPrecision;
        this.dataScale = dataScale;
    }

    /** default constructor */
    public EbsDynrptSqlCol() {
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

    public String getColumnName() {
        return this.columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDataType() {
        return this.dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public BigDecimal getDataLength() {
        return this.dataLength;
    }

    public void setDataLength(BigDecimal dataLength) {
        this.dataLength = dataLength;
    }

    public BigDecimal getDataPrecision() {
        return this.dataPrecision;
    }

    public void setDataPrecision(BigDecimal dataPrecision) {
        this.dataPrecision = dataPrecision;
    }

    public BigDecimal getDataScale() {
        return this.dataScale;
    }

    public void setDataScale(BigDecimal dataScale) {
        this.dataScale = dataScale;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("reportid", getReportid())
            .append("tableName", getTableName())
            .append("columnName", getColumnName())
            .append("dataType", getDataType())
            .append("dataLength", getDataLength())
            .append("dataPrecision", getDataPrecision())
            .append("dataScale", getDataScale())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof EbsDynrptSqlCol) ) return false;
        EbsDynrptSqlCol castOther = (EbsDynrptSqlCol) other;
        return new EqualsBuilder()
            .append(this.getReportid(), castOther.getReportid())
            .append(this.getTableName(), castOther.getTableName())
            .append(this.getColumnName(), castOther.getColumnName())
            .append(this.getDataType(), castOther.getDataType())
            .append(this.getDataLength(), castOther.getDataLength())
            .append(this.getDataPrecision(), castOther.getDataPrecision())
            .append(this.getDataScale(), castOther.getDataScale())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getReportid())
            .append(getTableName())
            .append(getColumnName())
            .append(getDataType())
            .append(getDataLength())
            .append(getDataPrecision())
            .append(getDataScale())
            .toHashCode();
    }

}
