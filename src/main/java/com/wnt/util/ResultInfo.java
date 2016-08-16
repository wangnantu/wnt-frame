package com.wnt.util;

public class ResultInfo {
	private static ResultInfo ri;
	private String retId;
	private String retCode;
	private String retMsg;
	private ResultInfo(){}
	private ResultInfo(String retCode,String retMsg,String retId){
		this.retCode=retCode;
		this.retMsg=retMsg;
		this.retId=retId;
	}
	public static ResultInfo getInstance(String retCode,String retMsg,String retId){
		if(ri==null)
			ri=new ResultInfo(retCode,retMsg,retId);
		ri.setRetCode(retCode);
		ri.setRetMsg(retMsg);
		ri.setRetId(retId);
		return ri;
	}
	public String getRetCode(){
		return retCode;
	}
	public String getRetMsg(){
		return retMsg;
	}
	public String getRetId(){
		return retId;
	}
	private void setRetCode(String retCode){
		this.retCode=retCode;
	}
	private void setRetMsg(String retMsg){
		this.retMsg=retMsg;
	}	
	private void setRetId(String retId){
		this.retId=retId;
	}
}
