package com.wnt.ireport.controller;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wnt.controller.UserController;
import com.wnt.ireport.dao.iReportManageDAO;
import com.wnt.ireport.dao.hibernate.iReportManageDAOImpl;
import com.wnt.ireport.model.ResultInfo;
import com.wnt.ireport.model.ReadRptExcel;
import com.wnt.ireport.po.EbsDynrptImg;
import com.wnt.ireport.po.EbsDynrptBatchpara;
import com.wnt.ireport.po.EbsDynrptBpara;
import com.wnt.ireport.po.EbsDynrptHpara;
import com.wnt.ireport.po.EbsDynrptJxml;
import com.wnt.ireport.po.EbsDynrptSqlCol;
import com.wnt.ireport.po.EbsDynrptMsgpara;
import com.wnt.ireport.util.CommUtil;
import com.wnt.ireport.util.CopyFileUtil;




@Controller  
@RequestMapping("/ireport")  
public class iReportManageController {
	private static Logger logger = Logger.getLogger(UserController.class);
	
	
	String rptbatchpartype1 = "日期区间";
	//String rptbatchpartype2 = "营业部集合";
	
	@RequestMapping(value="/dynireport",method = RequestMethod.GET)
	@ResponseBody  
	private String dynireport(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String curruserid = request.getParameter("curruserid");
		
		String departId = "";
		try{
			departId = request.getParameter("currdepcode").toString();
		}catch(Exception e){
			
		}
		String pageheight="";
		try{
			pageheight = request.getParameter("pageheight").toString();
		}catch(Exception e){
			
		}
		request.setAttribute("pageheight", pageheight);
		//导入报表 1:代表从页面直接修改报表后导入
		String imptype = request.getParameter("imptype");
		
		String filename = request.getParameter("filename");
		String fname = request.getParameter("fname");
		String serverName = request.getServerName(); // 获得服务器的名字
		String realPath = request.getRealPath(serverName); // 取得互联网程序的绝对地址
		realPath = realPath.substring(0, realPath.lastIndexOf(File.separator));
		filename = realPath + File.separator+"temp"+File.separator + filename;
		String compflag = "";
		try {
			compflag = request.getParameter("compflag").toString();
		} catch (Exception e) {
			CommUtil.writeLogfile("dynireport(01)"+e.getMessage());
			compflag = "";
		}

		ReadRptExcel rptxls = new ReadRptExcel();
		
		String rtnval = "";
		try {
			rtnval = rptxls.readcfgfile(filename);
		} catch (Exception e) {
			CommUtil.writeLogfile("dynireport(02)"+e.getMessage());
			e.printStackTrace();
			rtnval = e.getMessage();
		}
		
		if(!rtnval.equals("")){
			String returnMsg = "解析报表设置错误：" + rtnval;
			returnMsg = returnMsg.replace("\"", "");
			request.setAttribute("rtnmsg", returnMsg);
			CommUtil.writeLogfile("dynireport(03)"+returnMsg);
			return "dynireportrtnfail";	
		}
		iReportManageDAO rptDaO = new iReportManageDAOImpl();
		
		//查询条件List
		List<String[][]> dynrpthpara = rptxls.getDynrpt_hpara();
		
		//批量导出参数List
		List<String[][]> dynrptbatchpara = rptxls.getBatchparam();
		
		//打印参数List
		List<String [][]> dynrptprintpara = rptxls.getPrintparam();
		//图标列
		List<String[]> dyncimgcols = rptxls.getImgcolparam();
		//存放sheet中包含的语句名
		List<String[]> shttnamelist = rptxls.getShttnamelist();
		
		//分页sql语句名称
		TreeMap pagesqlmap = new TreeMap();
		pagesqlmap = rptxls.getPagesqlmap();
		
		//处理排序情况
		String rankiptctrlname = "";
		String rankiptctrldef = "";
		String iptctlts = "";
		for (int i = 0; i < dynrpthpara.size(); i++) {
			rankiptctrlname = rankiptctrlname+ (dynrpthpara.get(i)[i][1]).replace("$P{", "").replace("}", "") + "&";
			iptctlts = iptctlts + dynrpthpara.get(i)[i][2] + "&";
			rankiptctrldef = rankiptctrldef + dynrpthpara.get(i)[i][3] + "&";
		}
		String reportid = rptxls.getReportid();
		
		//加入报表导入日志
		if("1".equalsIgnoreCase(imptype)){
			CommUtil.SetLog("动态报表导入(页面修改)", curruserid, "addRpt", "页面导入动态报表:"+reportid+"_"+fname, CommUtil.getIpAddr(request));
		}else{
			CommUtil.SetLog("动态报表导入(正常导入)", curruserid, "addRpt", "正常导入动态报表:"+reportid+"_"+fname, CommUtil.getIpAddr(request));
		}
		
		List<String[][]> dynrpt_sqlcol = new ArrayList<String[][]>();
		if (rtnval.equals("")) {
			//生成查询字段
			TreeMap sqlmap = new TreeMap();
			sqlmap = rptxls.getSqlmap();
			Iterator it=sqlmap.keySet().iterator();
			while(it.hasNext()){
				String key=(String)it.next();
				String val=(String)sqlmap.get(key);
				ResultInfo reinfo = rptDaO.SaveRptSqlColIdList(reportid,key,val,rankiptctrlname,rankiptctrldef,iptctlts);
				if ((reinfo != null)&&((reinfo.getRetCode()).equalsIgnoreCase("0"))) {
					List list = rptDaO.quoteRptSqlColIdList("ebs_dynrpt_sql_cols_tmp",reportid,key);
					if(list!=null){
						for(int l=0;l<list.size();l++){
							String[][] temp = new String[1][4];
							EbsDynrptSqlCol sqlcol = (EbsDynrptSqlCol)list.get(l);
							temp[0][0]=sqlcol.getReportid();
							temp[0][1]=sqlcol.getTableName();
							temp[0][2]=sqlcol.getColumnName();
							temp[0][3]=sqlcol.getDataType();
							dynrpt_sqlcol.add(temp);
						}
					}
				}else{
					rtnval = reinfo.getRetMsg();
					break;
				}
			}
			
			rptxls.setDynrpt_sqlcol(dynrpt_sqlcol);
		}
		String dynrptver=rptxls.getDynrptver();
		String rtnval2="";
		if (rtnval.equals("")) {
			try {
				if("VER 1.0".equalsIgnoreCase(dynrptver))
				{
					rtnval=rptxls.readmdlfile(filename);
				}
				else{
					rtnval2=rptxls.readmdlfile2(filename);
				}
			} catch (Exception e) {
				CommUtil.writeLogfile("dynireport(04)"+":"+filename+":"+rtnval+","+e.getMessage());
				rtnval = e.getMessage();
				try{
					if("VER 1.0".equalsIgnoreCase(dynrptver))
						rtnval2=rptxls.readmdlfile2(filename);
				} catch (Exception ee) {
					rtnval2 = ee.getMessage();
				}
			}
		}else {
			rtnval = rtnval.replace("\"", "");
			request.setAttribute("rtnmsg", rtnval);
			CommUtil.writeLogfile("dynireport(05)"+rtnval);
			return "dynireportrtnfail";
		}
		if (rtnval2.equals("")&&!"VER 1.0".equalsIgnoreCase(dynrptver)) rtnval=rtnval2;
		
		if (rtnval.equals("")) {
			EbsDynrptBpara dynrptbpara = new EbsDynrptBpara();
			EbsDynrptMsgpara dyncSendMsg = new EbsDynrptMsgpara();
			EbsDynrptImg ebsdynimg = new EbsDynrptImg();
			try {
				dynrptbpara.setReportid(rptxls.getReportid());
				dynrptbpara.setReportname(rptxls.getReportname());
				dynrptbpara.setRptmakedt(rptxls.getRptmakedt());
				
				dynrptbpara.setRptmaker(rptxls.getRptmaker());
				dynrptbpara.setRptversion(rptxls.getRptversion());
				dynrptbpara.setRptremark(rptxls.getRptremark());

				dynrptbpara.setFilename(fname);
				dynrptbpara.setIsouttxt(rptxls.getIsoutTxt());
				dynrptbpara.setPixs(rptxls.getPixs());
				dynrptbpara.setEmptsperpix(rptxls.getEmptsperpix());
				dynrptbpara.setFildemps(rptxls.getFildemps());
				dynrptbpara.setXlstype(rptxls.getXlstype());
				
				ebsdynimg.setIsoutmg(rptxls.getIsoutmg());
				ebsdynimg.setImgtype(rptxls.getImgtype());
				ebsdynimg.setBeginrow(rptxls.getImgbeginrow());
				ebsdynimg.setEndrow(rptxls.getImgendrow());
				ebsdynimg.setXtitle(rptxls.getImgxtitle());
				ebsdynimg.setYtitle(rptxls.getImgytitle());
				ebsdynimg.setFontsize(rptxls.getImgfontsize());
				ebsdynimg.setBgcolor(rptxls.getImgbgcolor());
				
				ebsdynimg.setIstip(rptxls.getImgistip());
				ebsdynimg.setIstext(rptxls.getImgistext());
				ebsdynimg.setXcount(rptxls.getImgxcnt());
				ebsdynimg.setIsshowimgtitle(rptxls.getIsshowimgtitle());
				ebsdynimg.setIsshowdatagrid(rptxls.getIsshowdatagrid());
				
				List<String[]> dynrptfile = rptxls.getDynrpt_rpt_file();
				String subprt="";
				String sheetname="";
				for(int i=0;i<dynrptfile.size();i++){
					subprt=subprt+dynrptfile.get(i)[0]+"@@";
					sheetname=sheetname+dynrptfile.get(i)[1]+"@@";
				}
				
				String ismjxmls="";
				String mjxmls="";
				String jxmls="";
				List<String[]> rptlist = rptxls.getRptlist();
				for(int i=0;i<rptlist.size();i++){
					String tmp_mian = rptlist.get(i)[0];
					String tmp_sub = rptlist.get(i)[1];
					if(!"".equals(tmp_sub))
					{
						mjxmls = mjxmls+tmp_mian+"@@";
						jxmls = jxmls+tmp_sub+"@@";
						if(!"".equals(tmp_mian))
							ismjxmls = ismjxmls+"1"+"@@";
						else
							ismjxmls = ismjxmls+"@@";
					}
				}
				
				//发短信信息
				dyncSendMsg.setIssendmsg(rptxls.getIssendmsg());
				dyncSendMsg.setAppid(rptxls.getAppid());
				dyncSendMsg.setUseid(rptxls.getUseid());
				dyncSendMsg.setUpwd(rptxls.getUpwd());
				dyncSendMsg.setContent(rptxls.getContent());
				
				ResultInfo reinfo = rptDaO.saverptpara(curruserid, CommUtil.getIpAddr(request), dynrptbpara, dynrpthpara,
						subprt,sheetname,jxmls,mjxmls,ismjxmls,dynrptbatchpara,dynrptprintpara,pagesqlmap,dyncSendMsg,ebsdynimg,dyncimgcols,shttnamelist,rptxls.getDynbodys());
				String rtnmsg = "";
				if ((reinfo == null)
						|| (!(reinfo.getRetCode()).equalsIgnoreCase("0"))) {
					rtnmsg = reinfo.getRetMsg().replace("\"", "");
					request.setAttribute("rtnmsg", rtnmsg);
					return "dynireportrtnfail";
				} else {
					List dynrptHpara = rptDaO.getdynrpthpara("ebs_dynrpt_hpara_tmp",reportid);
					List dynrptBatchparalist = rptDaO.getdynrptbatchpara("ebs_dynrpt_batchpara_tmp",reportid);
					EbsDynrptBpara dynrptBpara = rptDaO.getdynrptbpara("ebs_dynrpt_bpara_tmp",reportid);
					String tradingday = rptDaO.gettradingday();
					request.setAttribute("tradingday", tradingday);
					request.setAttribute("dynrptHpara", dynrptHpara);
					request.setAttribute("dynrptBpara", dynrptBpara);
					if(dynrptBatchparalist != null && dynrptBatchparalist.size()>0){
					  request.setAttribute("batchexpflag", "batchexp");
					}
					// 查询下拉框的值
					for (int i = 0; i < dynrpthpara.size(); i++) {
						String iptctrlname = (dynrpthpara.get(i)[i][1])
								.replace("$P{", "").replace("}", "");
						String iptctrltype = dynrpthpara.get(i)[i][2];
						String iptctrllist = dynrpthpara.get(i)[i][4];
						if (!"".equalsIgnoreCase(iptctrllist)
								&& iptctrllist != null) {
							List comblist = rptDaO.getdynrptComboxList(iptctrllist,curruserid,departId);
							if(iptctrltype.indexOf("树") != -1)
							{
								String treelist = rptDaO.combtree(comblist);
								request.setAttribute(iptctrlname + "list",treelist);
							}else
							{
								request.setAttribute(iptctrlname + "list",comblist);
							}
						}
					}
					List rptmianjxmltmp = rptDaO.getdynrptjxmlTmpList(reportid);
					request.setAttribute("rptmianjxmltmp", rptmianjxmltmp);
					//若是从菜单查询，就不显示保存按钮
					request.setAttribute("saveflag", "saveflag");
					
					request.setAttribute("isouttxt", dynrptBpara.getIsouttxt());
					request.setAttribute("dynrptBpara", dynrptBpara);
					
					EbsDynrptMsgpara dynrptSendMsgpara = rptDaO.getdynrptSendMsgpara("ebs_dynrpt_msgpara_tmp",reportid);
					request.setAttribute("issendmsg", dynrptSendMsgpara.getIssendmsg());
					
					//若是重新编译后则生成新的jrxml、jasper文件
					if(compflag !=""&& compflag.length() != 0){
						//copy报表xls文件
						CopyFileUtil.copyFile(realPath + File.separator+"temp"+File.separator + fname,realPath + File.separator+"dynreport" +File.separator+ fname);
						
						String jxmlpath=realPath + File.separator+"jsp"+File.separator+"sysadmin"+File.separator+"ireport"+File.separator+"reportjrxml"+File.separator+reportid.toUpperCase()+File.separator;
						String jasperpath=realPath + File.separator+"jsp"+File.separator+"sysadmin"+File.separator+"ireport"+File.separator+"reportjasper"+File.separator+reportid.toUpperCase()+File.separator;
						File jxfile = new File(jxmlpath);
						if(!jxfile.exists()){
							jxfile.mkdirs();
						}
						File jaspfile = new File(jasperpath);
						if(!jaspfile.exists()){
							jaspfile.mkdirs();
						}
						
						List jxmllist = rptDaO.getcompdynrptjxmlList(reportid.toUpperCase());
						for(int i=0;i<jxmllist.size();i++){
							EbsDynrptJxml jxml = (EbsDynrptJxml)jxmllist.get(i);
							if(jxml==null)continue;
							String jxmlfn = jxml.getReportidjxml();
							CopyFileUtil.copyFile(realPath + File.separator+"temp"+File.separator+ reportid.toUpperCase() +File.separator+ jxmlfn+".jrxml",jxmlpath + jxmlfn+".jrxml");
							CopyFileUtil.copyFile(realPath + File.separator+"temp"+File.separator+ reportid.toUpperCase() +File.separator+ jxmlfn+".jasper",jasperpath + jxmlfn+".jasper");
				     	}
						
						
						dynrptHpara = rptDaO.getdynrpthpara("ebs_dynrpt_hpara",reportid);
						dynrptBatchparalist = rptDaO.getdynrptbatchpara("ebs_dynrpt_batchpara",reportid);
						dynrptBpara = rptDaO.getdynrptbpara("ebs_dynrpt_bpara",reportid);
						tradingday = rptDaO.gettradingday();
						request.setAttribute("tradingday", tradingday);
						request.setAttribute("dynrptHpara", dynrptHpara);
						request.setAttribute("dynrptBpara", dynrptBpara);
						Boolean batchdateflag = true;
						String rptbatchpar1 = "";
						String rptbatchpar2 = "";
						if(dynrptBatchparalist != null){
							for(int i=0;i<dynrptBatchparalist.size();i++){
								EbsDynrptBatchpara dynrptBatchpara = (EbsDynrptBatchpara)dynrptBatchparalist.get(i);
								if(rptbatchpartype1.equals(dynrptBatchpara.getRptbatchpartype())&& batchdateflag){
									rptbatchpar1 = dynrptBatchpara.getRptbatchpar1().toUpperCase();
									rptbatchpar2 = dynrptBatchpara.getRptbatchpar2().toUpperCase();
									batchdateflag = false;
								}
							}
							
							request.setAttribute("batchexpflag", "batchexp");
							request.setAttribute("rptbatchpar1", rptbatchpar1);
							request.setAttribute("rptbatchpar2", rptbatchpar2);
						}
						
						// 查询下拉框的值
						for (int i = 0; i < dynrptHpara.size(); i++) {
							EbsDynrptHpara dynhpa = (EbsDynrptHpara)dynrptHpara.get(i);
							String iptctrlname = dynhpa.getIptctrlname();
							String iptctrllist = dynhpa.getIptctrllist();
							String iptctrltype = dynhpa.getIptctrltype();
							if (!"".equalsIgnoreCase(iptctrllist) && iptctrllist != null) {
								List comblist = rptDaO.getdynrptComboxList(iptctrllist,curruserid,departId);
								if(iptctrltype.indexOf("树") != -1)
								{
									String treelist = rptDaO.combtree(comblist);
									request.setAttribute(iptctrlname + "list",treelist);
								}else
								{
									request.setAttribute(iptctrlname + "list",comblist);
								}
							}
						}
						List rptmianjxml = rptDaO.getdynrptjxmlList(reportid);
						request.setAttribute("rptmianjxml", rptmianjxml);
						request.setAttribute("saveflag", "");
						return "dynireporthead";
					}else{
						return "dynrptjsphead";
					}
				}
			} catch (Exception e) {
				String returnMsg = "未知错误!" + e.getMessage();
				returnMsg = returnMsg.replace("\"", "");
				request.setAttribute("rtnmsg", returnMsg);
				CommUtil.writeLogfile("dynireport(06)"+returnMsg);
				return "dynireportrtnfail";
			}
		} else {
			rtnval = rtnval.replace("\"", "");
			request.setAttribute("rtnmsg", rtnval);
			CommUtil.writeLogfile("dynireport(07)"+rtnval);
			return "dynireportrtnfail";
		}
	}
	
}