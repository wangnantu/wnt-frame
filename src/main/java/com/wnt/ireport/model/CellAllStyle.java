package com.wnt.ireport.model;

public class CellAllStyle {
	
	private String rowWidth;//行宽
	private String rowHeight;//行高
	private String rowLeft;//横坐标
	private String rowTop;  //纵坐标
	private String cellType;//类型
	private String dateformat;//小数位数标志
	private String alldata;//单元格全部的数据
	private String signdata;//带$P、$F、$V的数据
	private String nosigndata;//不带符号的数据
	private String fontstyle;//字体类型
	private String fontsize;//字体大小
	private String boldlight;//字体粗细
	private String horizonta;//水平对齐方式
	private String vertical;//垂直对齐方式
	
	public String getFontstyle() {
		return fontstyle;
	}
	public void setFontstyle(String fontstyle) {
		this.fontstyle = fontstyle;
	}
	public String getFontsize() {
		return new String().valueOf(Math.round((Integer.parseInt(fontsize)/20)));
	}
	public void setFontsize(String fontsize) {
		this.fontsize = fontsize;
	}
	public String getBoldlight() {
		return boldlight.equals("700")?"true":"false";//700粗体
	}
	public void setBoldlight(String boldlight) {
		this.boldlight = boldlight;
	}
	public String getAlldata() {
		return alldata;
	}
	public void setAlldata(String alldata) {
		this.alldata = alldata;
	}
	public String getSigndata() {
		return signdata;
	}
	public void setSigndata(String signdata) {
		this.signdata = signdata;
	}
	public String getNosigndata() {
		return nosigndata;
	}
	public void setNosigndata(String nosigndata) {
		this.nosigndata = nosigndata;
	}

	public String getDateformat() {
		return dateformat;
	}
	public void setDateformat(String dateformat) {
		this.dateformat = dateformat;
	}
	public String getRowWidth() {
		return new String().valueOf(Integer.parseInt(rowWidth)*8);
	}
	public void setRowWidth(String rowWidth) {
		this.rowWidth = rowWidth;
	}
	public String getRowHeight() {
		return new String().valueOf(Math.round(Integer.parseInt(rowHeight)*1));
	}
	public void setRowHeight(String rowHeight) {
		this.rowHeight = rowHeight;
	}
	public String getRowLeft() {
		return new String().valueOf(Integer.parseInt(rowLeft)*8);
	}
	public void setRowLeft(String rowLeft) {
		this.rowLeft = rowLeft;
	}
	public String getRowTop() {
		return  new String().valueOf(Math.round((int)Integer.parseInt(rowTop)*1));
	}
	public void setRowTop(String rowTop) {
		this.rowTop = rowTop;
	}
	public String getCellType() {
		return cellType;
	}
	public void setCellType(String cellType) {
		this.cellType = cellType;
	}
	public String getHorizonta() {
		if(this.horizonta.equals("1")){
			horizonta = "Left";
		}else if(this.horizonta.equals("3")){
			horizonta = "Right";
		}else{
			horizonta = "Center";
		}
		return horizonta;
	}
	public void setHorizonta(String horizonta) {
		this.horizonta = horizonta;
	}
	public String getVertical() {
		if(this.vertical.equals("0")){
			vertical = "Top";
		}else if(this.vertical.equals("1")){
			vertical = "Middle";
		}else{
			vertical ="Bottom";
		}
		return vertical;
	}
	public void setVertical(String vertical) {
		this.vertical = vertical;
	}
	

}
