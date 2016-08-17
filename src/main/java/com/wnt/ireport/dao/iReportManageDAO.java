package com.wnt.ireport.dao;

import java.sql.Connection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.DataAccessException;

import com.wnt.ireport.model.ResultInfo;
import com.wnt.ireport.po.EbsDynrptImg;
import com.wnt.ireport.po.EbsDynrptImgcols;
import com.wnt.ireport.po.EbsDynrptBatchpara;
import com.wnt.ireport.po.EbsDynrptBpara;
import com.wnt.ireport.po.EbsDynrptPagepara;
import com.wnt.ireport.po.EbsDynimpBpara;
import com.wnt.ireport.po.EbsDynimpCparam;
import com.wnt.ireport.po.EbsDynimpExesql;
import com.wnt.ireport.po.EbsDynimpFind;
import com.wnt.ireport.po.EbsDynimpHpara;
import com.wnt.ireport.po.EbsDynrptMsgpara;


/**
 * @author luzh
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface iReportManageDAO {

	public void procHtmlRpt(HttpServletRequest request,HttpServletResponse response, Map parameter, String reportFile,String title) throws Exception;	
	public String expRpt(HttpServletRequest request,HttpServletResponse response, Map parameter, String reportFile,String title,String type) throws Exception;	

	public String expRptManySheet(HttpServletRequest request,HttpServletResponse response, Map [] parammaps, String [] reportFile,String [] title,String type) throws Exception;
	public Connection getconn(HttpServletRequest request);
	
	//取得清算月份
	public String gettradingday()throws DataAccessException;
	public List gettradingdayList(String begindate,String enddate)throws DataAccessException;
	
	public String expRptToXls(HttpServletRequest request,HttpServletResponse response, Map parameter, String reportFile,String title,String path) throws Exception;
	
	//查询国泰君安期货下属营业部
	public List quoteGtjaqhdep(String depname) throws DataAccessException;

	//设置excel打印范围
	public void setPrintscope(String filepath,String tabtailinfo,String xmlpath,String reportno);
	
	//设置excel打印范围(Poi)
	public void setPrintscopePoi(String filepath,String tabtailinfo,String xmlpath,String reportno);
	
	//查询日均权益	
	public List quotedayequityall(String tradingday) throws DataAccessException;
    
	/************动态报表************/
	//保存动态报表参数
	public ResultInfo saverptpara(String curruserid,String reqip,EbsDynrptBpara dynrptb, List<String[][]> dynrpt_hpara,
			String subprt,String sheetname,String jxmls,String mjxmls,String ismjxmls,List<String[][]> dynrptbatchparam,List<String[][]> dynrptprintparam,TreeMap pagesqlmap,
			EbsDynrptMsgpara dyncSendMsg,EbsDynrptImg ebsdynimg,List<String[]> dyncimgcols,List<String[]> shttnamelist,String dynbodys) throws DataAccessException;
	//查询动态报表参数
	public EbsDynrptBpara getdynrptbpara(String table,String reportid)throws DataAccessException;
	//查询动态报表查询条件
	public List getdynrpthpara(String table,String reportid)throws DataAccessException;
	//查询动态报表下拉框的值
	public List getdynrptComboxList(String combsql,String curruserid,String departId)throws DataAccessException;
	
	//查询报表图表参数
	public EbsDynrptImg getdynrptimg(String table,String reportid)throws DataAccessException;
	//查询报表图表设置参数
	public EbsDynrptImgcols getdynrptimgcols(String table,String reportid)throws DataAccessException;
	//取得图表参数,显示格式转换
	public List getdynrptimgcolslist(String reportid) throws DataAccessException;
	//修改图表参数
	public ResultInfo saveimgcond(String curruserid,EbsDynrptImg dynrptimg,EbsDynrptImgcols dynrptimgcols) throws DataAccessException;

	
	//查询sql字段
	public List quoteRptSqlColIdList(String table,String rptId,String tname) throws DataAccessException;
	//查询动态报表sql语句
	public List getdynrptSqlList(String repId)throws DataAccessException;
	//查询动态报表文件
	public List getdynrptFileList(String table,String repId)throws DataAccessException;
	//解析Sql
	public ResultInfo SaveRptSqlColIdList(String rptId,String tname,String sql,
			String rankiptctrlname,String rankiptctrldef ,String iptctrltype) throws DataAccessException;
	//查询主报表文件
	public List getdynrptjxmlList(String repId)throws DataAccessException;
	public List getdynrptjxmlTmpList(String repId)throws DataAccessException;
	//将treeList转成tree格式的
	public String combtree(List comblist) throws DataAccessException;
	
	//动态报表设置打印格式
	public void setPrintPage(String filepath,String tableinfo,List dynrptPrintparalist,String xmlpath,String reportno)throws Exception;

	//动态报表设置打印格式(Poi)
	public void setPrintPagePoi(String filepath,String tableinfo,List dynrptPrintparalist,String xmlpath,String reportno)throws Exception;

	
	//修改报表参数
	public ResultInfo editdynrptBpara(String curruserid, EbsDynrptBpara dynrptBpara) throws DataAccessException;
	
	//动态报表重新编译excel文件
	public void compileExcel(String path,List<String> writecontents,String repid,String rptname,String repmarker,String rptversion,String rptremark,String writetyle,
			String isoutTxt,String pixs,String emptsperpix,String fildemps,String xlstype,EbsDynrptMsgpara dynrptSendMsgpara,EbsDynrptImg dynrptIMG);
	
	//保存查询条件到临时表
	public ResultInfo saverptparatmp(String curruserid,String reqid,String qhlabelname,String qhiptctrlname,String qhiptctrltype,String qhiptctrldef,String qhiptctrllist,String qhiptisnull,String qhiptpardef) throws DataAccessException;
	
	//保存查询sql到临时表
	public ResultInfo saverptsqltmp(String curruserid,String reqid,String tname,String sqlstr) throws DataAccessException;

	//按日期、营业部批量导出excel文件
	public String[] batchExpExcel(HttpServletRequest request,
			HttpServletResponse response, Map parameter, String reportFile,
			String title, String type,List<String []> datelist,List deplist,String begindate,String enddate,String batchparam)throws Exception;
	
	//根据开始日期和结束日期取对应的交易情况
	public List<String[]> gettradingdate(String begindate,String enddate,String datetype,String selfsql)throws DataAccessException;
	
	//动态报表html分页，查询记录数
	public String queryCountNum(String tablename,String reportno,String sqlname,Enumeration rnames,HttpServletRequest request)throws DataAccessException;
	
	//查询动态报表分页参数
	public EbsDynrptPagepara getdynrptpagepara(String table,String reportid)throws DataAccessException;

	//短信
	public EbsDynrptMsgpara getdynrptSendMsgpara(String table,String reportid)throws DataAccessException;
	//查询动态报表批量导出参数
	public List<EbsDynrptBatchpara> getdynrptbatchpara(String table,String reportid)throws DataAccessException;
	
	//查询所有部门集合
	public List quotedeplist() throws DataAccessException;
	
	//根据部门id取得部门信息
	public String quotedepbyid(String depcode) throws DataAccessException;
	
	//修改后保存打印参数
	public ResultInfo saverptprintpara(String curruserid,String reportid,String reportsheetno,String printdirect,String printalign,String autowidth,String pagefoot) throws DataAccessException;
	
	//修改后保存分页记录数
	public ResultInfo saverptpagepara(String curruserid,String reportid,String reportsheetno,String sqlname,String pagecount) throws DataAccessException;
	
	
	//取得打印参数，设置excel用
	public List getdynrptprintpara(String reportno) throws DataAccessException;
	
	//查询动态报表分页数
	public List getdynrptpagepara(String reportid)throws DataAccessException;
	
	//取得打印参数,显示格式转换
	public List getdynrptprintshowpara(String reportno) throws DataAccessException;
	
	//取得sql语句名称
	public List getdynrptsqlpara(String reportno) throws DataAccessException;
	
	//
	public List getcompdynrptjxmlList(String repId)throws DataAccessException;
	public void setdyncsessionsta(String dyncsession);
	public String getdyncsessionsta(String dyncsession) throws DataAccessException;
	/******************************/
	
	public Map<String, Object> queryBodyListByPro(String countsql,String srchsql,String rankfield,String rankorder,int pernum,int currpage);
	
	public List<Map<String, String>> querySendSmsListBySql(String sql);
	public List<String> getDyncTSqlList(String table,String reportid);
	public ResultInfo saverptmsgpara(String curruserid,String reportid,EbsDynrptMsgpara dynrptSendMsgpara) throws DataAccessException;
	
	public EbsDynimpBpara getdynimpbpara(String reportid)throws DataAccessException;
	public List getdynimphpara(String reportid)throws DataAccessException;
	
	//增加导入xls文件主表
	public boolean saveDynImpBpara(EbsDynimpBpara bpara);
	
	public boolean delDynImpBpara(String reportId);
	
	public EbsDynimpBpara queryDynImpBpara(String reportId);
	
	//增加导入xls文件的输入参数表
	public boolean saveDynImpHpara(EbsDynimpHpara hpara);
	
	public boolean delDynImpHpara(String reportId);
	
	//增加导入xls文件的列参数
	public boolean saveDynImpCpara(EbsDynimpCparam cparam);
	
	public boolean delDynImpCpara(String reportId);
	
	public List<EbsDynimpCparam> queryDynImpCpara(String reportId);
	
	//增加导入xls时查找关键字设置
	public boolean saveDynImpFind(EbsDynimpFind find);
	
	public boolean delDynImpFind(String reportId);
	
	public EbsDynimpFind queryDynImpFind(String reportId);
	
	//增加导入xls内容的sql
	public boolean saveDynImpExeSql(EbsDynimpExesql sql);
	
	public boolean delDynImpExeSql(String reportId);
	
	public List<EbsDynimpExesql> qeuryDynImpExeSql(String reportId);
	
	/**根据id修改url路径**/
	public boolean updmenuurl(String curruserid,String menuid, String reportno) throws DataAccessException;
	
	//取得图表数据列表
	public Map<String,Object> queryryDyncImgResult(String reportId,String reportJrxmlm,String tabflag,String inputName,String inputValue)throws DataAccessException;
	
	//获取图表相关参数
	public EbsDynrptImg getEbsdynrptImg(String table,String reportId)throws DataAccessException;
	
	//获取图表相关参数
	public List<EbsDynrptImgcols> getEbsdynrptImgCols(String table,String reportId,String sheetindex)throws DataAccessException;
	
	public String getEbsdynrptImgDefault(String reportId)throws DataAccessException;
	
	//查询首页右上角，每天的数据
	public Map<String, String> eveData()throws DataAccessException;
	//查询首页右下角，最近几天的数据
	public List<Map<String, String>> recData()throws DataAccessException;
}