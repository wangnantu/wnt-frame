package com.wnt.ireport.po;

import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class EbsDynrptImg implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String reportid;
    
    private String isoutmg;
    
    private String imgtype;
    
    private String beginrow;
    
    private String endrow;
    
    private String xtitle;
    
    private String ytitle;
    
    private String bgcolor;
    
    private String fontsize;
    
    private String istip;
    
    private String istext;
    
    private String xcount;
    
    private String isshowimgtitle;
    
    private String isshowdatagrid;
    
    private String imgtitle;
    
    public EbsDynrptImg(String reportid,String isoutmg,String imgtype,String beginrow,String endrow,String xtitle,String ytitle,String bgcolor,String fontsize,String istip,String istext,String xcount,String isshowimgtitle,String isshowdatagrid,String imgtitle) {
        this.reportid = reportid;
        this.isoutmg = isoutmg;
        this.imgtype = imgtype;
        this.beginrow = beginrow;
        this.endrow = endrow;
        this.xtitle = xtitle;
        this.ytitle = ytitle;
        this.bgcolor = bgcolor;
        this.fontsize = fontsize;
        this.istip = istip;
        this.istext = istext;
        this.xcount = xcount;
        this.isshowimgtitle=isshowimgtitle;
        this.isshowdatagrid=isshowdatagrid;
        this.imgtitle=imgtitle;
    }

    public String getImgtitle() {
		return imgtitle;
	}

	public void setImgtitle(String imgtitle) {
		this.imgtitle = imgtitle;
	}

	public String getIsshowimgtitle() {
		return isshowimgtitle;
	}

	public void setIsshowimgtitle(String isshowimgtitle) {
		this.isshowimgtitle = isshowimgtitle;
	}

	public String getIsshowdatagrid() {
		return isshowdatagrid;
	}

	public void setIsshowdatagrid(String isshowdatagrid) {
		this.isshowdatagrid = isshowdatagrid;
	}

	public EbsDynrptImg() {}
    public String getReportid() {return this.reportid;}
    public void setReportid(String reportid) {this.reportid = reportid;}
    public String getIsoutmg() {return this.isoutmg;}
    public void setIsoutmg(String isoutmg) {this.isoutmg = isoutmg;}
    public String getImgtype() {return this.imgtype;}
    public void setImgtype(String imgtype) {this.imgtype = imgtype;}
    public String getBeginrow() {return this.beginrow;}
    public void setBeginrow(String beginrow) {this.beginrow = beginrow;}
    public String getEndrow() {return this.endrow;}
    public void setEndrow(String endrow) {this.endrow = endrow;}
    public String getXtitle() {return this.xtitle;}
    public void setXtitle(String xtitle) {this.xtitle = xtitle;}
    public String getYtitle() {return this.ytitle;}
    public void setYtitle(String ytitle) {this.ytitle = ytitle;}
    public String getBgcolor() {return this.bgcolor;}
    public void setBgcolor(String bgcolor) {this.bgcolor = bgcolor;}
    public String getFontsize() {return this.fontsize;}
    public void setFontsize(String fontsize) {this.fontsize = fontsize;}
    public String getIstip() {return this.istip;}
    public void setIstip(String istip) {this.istip = istip;}
    public String getIstext() {return this.istext;}
    public void setIstext(String istext) {this.istext = istext;}
	
    public String toString() {
        return new ToStringBuilder(this)
            .append("reportid", getReportid())
            .append("isoutmg", getIsoutmg())
            .append("imgtype", getImgtype())
            .append("beginrow", getBeginrow())
            .append("endrow", getEndrow())
            .append("xtitle", getXtitle())
            .append("ytitle", getYtitle())
            .append("bgcolor", getBgcolor())
            .append("fontsize", getFontsize())
            .append("istip", getIstip())
            .append("istext", getIstext())
            .append("xcount", getXcount())
            .append("isshowimgtitle", getIsshowimgtitle())
            .append("isshowdatagrid", getIsshowdatagrid())
            .append("imgtitle", getImgtitle())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof EbsDynrptImg) ) return false;
        EbsDynrptImg castOther = (EbsDynrptImg) other;
        return new EqualsBuilder()
            .append(this.getReportid(), castOther.getReportid())
            .append(this.getIsoutmg(), castOther.getIsoutmg())
            .append(this.getImgtype(), castOther.getImgtype())
            .append(this.getBeginrow(), castOther.getBeginrow())
            .append(this.getEndrow(), castOther.getEndrow())
            .append(this.getXtitle(), castOther.getXtitle())
            .append(this.getYtitle(), castOther.getYtitle())
            .append(this.getBgcolor(), castOther.getBgcolor())
            .append(this.getFontsize(), castOther.getFontsize())
            .append(this.getIstip(), castOther.getIstip())
            .append(this.getIstext(), castOther.getIstext())
            .append(this.getXcount(), castOther.getXcount())
            .append(this.getIsshowimgtitle(), castOther.getIsshowimgtitle())
            .append(this.getIsshowdatagrid(), castOther.getIsshowdatagrid())
            .append(this.getImgtitle(), castOther.getImgtitle())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getReportid())
            .append(getIsoutmg())
            .append(getImgtype())
            .append(getBeginrow())
            .append(getEndrow())
            .append(getXtitle())
            .append(getYtitle())
            .append(getBgcolor())
            .append(getFontsize())
            .append(getIstip())
            .append(getIstext())
            .append(getXcount())
            .append(getIsshowimgtitle())
            .append(getIsshowdatagrid())
            .append(getImgtitle())
            .toHashCode();
    }

	public String getXcount() {
		return xcount;
	}

	public void setXcount(String xcount) {
		this.xcount = xcount;
	}
}
