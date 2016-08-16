package com.wnt.ireport.model;


import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;

import com.wnt.ireport.model.TrendReport;
import com.wnt.ireport.util.Exl2TxtUtil;

public class ReadRptExcel {
	
	//const变量
	private static String CNTITLE="标题";
	private static String CNPGHEAD="表头";
	private static String CNPGDTL="明细";
	private static String CNPGSUM="小计";
	private static String CNSUM="总计";
	private static String CNFOOT="表尾";
	private static String CNNULLHIDE="空列,隐藏";
	
	private static String CNCELLTYPESTRING="java.lang.String";
	private static String CNCELLTYPEBIGDECIMAL="java.math.BigDecimal";
	private static String CNONESELECTROW="select * from ebs_dyn_empt";
	private int cncolumn;     //rpt的固定列数
	
	private int cfixr1column;     //rpt的固定行起始值
	private int cfixr2column;     //rpt的固定行最大值
	private String fixsql;        //隐藏列的sql
	
	private String dynrptver;//系统动态报表版本
	
	private int rptcnt;       //生成的报表数量
	private int mianrptno;    //生成的主报表数量
	private String reportid;  //报表文件Id
	private String reportname;//报表标题名称
	private String rptmaker;  //制作人
	private String rptmakedt; //制作时间
	private String rptversion;//版本
	private String rptremark; //备注
	private String resultSql; //结果查询sql
	
	private String ismpage;   //HTML是否分页(1:分页 2：不分页)

	private String viewtype;  //图表(1:表格2：图标3：全部)
	private String repeatsht; //重复sheet的sql语句
	private String iptbegin;  //查询条件开始行
	private String iptend;    //查询条件结束行
	private String sqlbegin;  //sql数据源开始行数
	private String sqlend;    //sql数据源结束行数
	private String batchbegin; //批量导出开始行数
	private String batchend;   //批量导出结束行数
	private String pagesqlbegin; //分页sql开始行数
	private String pagesqlend;  //分页sql结束行数
	private String printbegin; //打印参数开始行数
	private String printend;   //打印参数结束行数
	private String printdirect;//打印方向(1:普通 2:横打)
	private String printrate;  //打印比例(%)
	private String printalign; //打印页面对齐方式(1:水平居中 2:垂直居中 3:both)
	private String autowidth;  //自动数据调整列宽度(1:是 2:否)
	private String pagefoot;   //是否显示页脚(1:是 2:否)
	
	private int conditionBegin;//条件开始行
	private int conditionEnd;//条件结束行
	private TreeMap<Integer,List<String>> conditions;//查询条件
	
	private int valBegin;//明细开始行
	private int valEnd;//明细结束行
	private List<List<String>> valParams;//明细参数
	
	private String findSheetName;//查找工作簿名
	private int findBegin;//查找开始行
	private int findEnd;//查找结束行
	private Map<String,List<String>> findConfigs;//查询设置
	
	private int sqlBegin;//导入语句开始行
	private int sqlEnd;//导入语句结束行
	private TreeMap<Integer,TreeMap<Integer,String>> impSqls;//导入语句
    
    private List<String [][]> batchparam;//批量导出参数
	private List<String [][]> printparam;//打印参数
	private List<String []> imgcolparam;//图标列
	
	private String isoutTxt;   //是否导出txt(1:是 2:否)
	private String pixs;       //像素
	private String emptsperpix;//每像素空格数
	private String fildemps;   //字段间空格数
	private String xlstype;    //导出xls的类型
	private String isoutmg;
	private String imgtype;
	private String imgbeginrow;
	private String imgendrow;
	private String imgxtitle;
	private String imgytitle;
	private String imgbgcolor;
	private String imgfontsize;
	private String imgistip;
	private String imgistext;
	private String imgxcnt;
	private String isshowimgtitle;
	private String isshowdatagrid;
	
	public String getIsshowdatagrid() {
		return isshowdatagrid;
	}

	public void setIsshowdatagrid(String isshowdatagrid) {
		this.isshowdatagrid = isshowdatagrid;
	}

	public String getIsshowimgtitle() {
		return isshowimgtitle;
	}

	public void setIsshowimgtitle(String isshowimgtitle) {
		this.isshowimgtitle = isshowimgtitle;
	}

	private String issendmsg;  //是否发送短信
	private String useid;
	private String upwd;
	private String appid;
	private String content;//短信内容
	
	private List<String[]> dynrpt_rpt_file;//报表查询条件
	
	private List<String[][]> dynrpt_hpara;//报表查询条件
	private TreeMap sqlmap;               //存放sql语句
	private TreeMap pagesqlmap;           //存放分页sql语句名称
	
	private List<String[][]> dynrpt_sqlcol;//报表查询sql的字段	
	
	private List<String[]> rptlist;       //存放生成的报表文件列表，(父报表id，报表Id，列名前缀，第几个子报表｛确定前面列的区域类型｝)
	private List<String[]> rptAreaHeightlist;//存放生成的报表区域高度，(报表Id，区域，高度)
	private List<String[]> rptpagewidthlist;//存放生成的报表宽度，(报表Id，宽度)
	private List<String[]> rptAreaIdxlist;//存放生成的报表所在父报表区域索引号，(父报表Id，报表Id，区域，idx)
	
	private String p_rptpre[] = {"$P{","$p{"};//报表参数前缀
	private String p1_rptpre[] = {"$P!{","$p!{"};//报表参数前缀
	private String f_rptpre[] = {"$F{","$f{"};//报表字段前缀
	private String v_rptpre[] = {"$SUM{","$sum{","$COUNT{","$count{","$AVERAGE{","$average{","$LOWEST","$lowest","$HIGHEST","$highest"};//报表变量前缀
	private String rptpre[];
	
	private List<String[]> allparalist;   //存放所有参数（报表Id，类型(P，F，V),参数"name"）
	
	private List<String[]> rptRegionList;//存放所有报表sheet里的合并项(rptid,CellRangeAddressIndex,是否取过)
	
	private List<String[]> shttnamelist; //存放sheet中包含的语句名
	
	private String dynbodys;
	
	public String getReportid() {
		return reportid;
	}

	public void setReportid(String reportid) {
		this.reportid = reportid;
	}

	public String getReportname() {
		return reportname;
	}

	public void setReportname(String reportname) {
		this.reportname = reportname;
	}

	public String getIsmpage() {
		return ismpage;
	}

	public void setIsmpage(String ismpage) {
		this.ismpage = ismpage;
	}

	public String getViewtype() {
		return viewtype;
	}

	public void setViewtype(String viewtype) {
		this.viewtype = viewtype;
	}
	
	public String getRepeatsht() {
		return repeatsht;
	}

	public void setRepeatsht(String repeatsht) {
		this.repeatsht = repeatsht;
	}
	
	public String getIptbegin() {
		return iptbegin;
	}

	public void setIptbegin(String iptbegin) {
		this.iptbegin = iptbegin;
	}

	public String getIptend() {
		return iptend;
	}

	public void setIptend(String iptend) {
		this.iptend = iptend;
	}

	public String getSqlbegin() {
		return sqlbegin;
	}

	public void setSqlbegin(String sqlbegin) {
		this.sqlbegin = sqlbegin;
	}

	public String getSqlend() {
		return sqlend;
	}

	public void setSqlend(String sqlend) {
		this.sqlend = sqlend;
	}

	public String getPrintdirect() {
		return printdirect;
	}

	public void setPrintdirect(String printdirect) {
		this.printdirect = printdirect;
	}

	public String getPrintrate() {
		return printrate;
	}

	public void setPrintrate(String printrate) {
		this.printrate = printrate;
	}

	public String getPrintalign() {
		return printalign;
	}

	public void setPrintalign(String printalign) {
		this.printalign = printalign;
	}

	public String getAutowidth() {
		return autowidth;
	}

	public void setAutowidth(String autowidth) {
		this.autowidth = autowidth;
	}

	public String getPagefoot() {
		return pagefoot;
	}

	public void setPagefoot(String pagefoot) {
		this.pagefoot = pagefoot;
	}

	public List<String[][]> getDynrpt_hpara() {
		return dynrpt_hpara;
	}

	public void setDynrpt_hpara(List<String[][]> dynrpt_hpara) {
		this.dynrpt_hpara = dynrpt_hpara;
	}

	public TreeMap getSqlmap() {
		return sqlmap;
	}

	public void setSqlmap(TreeMap sqlmap) {
		this.sqlmap = sqlmap;
	}
	
	public String getRptmaker() {
		return rptmaker;
	}

	public void setRptmaker(String rptmaker) {
		this.rptmaker = rptmaker;
	}

	public String getRptmakedt() {
		return rptmakedt;
	}

	public void setRptmakedt(String rptmakedt) {
		this.rptmakedt = rptmakedt;
	}

	public String getRptversion() {
		return rptversion;
	}

	public void setRptversion(String rptversion) {
		this.rptversion = rptversion;
	}

	public String getRptremark() {
		return rptremark;
	}

	public void setRptremark(String rptremark) {
		this.rptremark = rptremark;
	}
	
	public List<String[]> getDynrpt_rpt_file() {
		return dynrpt_rpt_file;
	}

	public void setDynrpt_rpt_file(List<String[]> dynrpt_rpt_file) {
		this.dynrpt_rpt_file = dynrpt_rpt_file;
	}
	
	//从SQL语句定义中根据前缀name获取select语句
	private String getSqlByname(String sqlname){
		String sql = "";
		try {
			sql = sqlmap.get(sqlname).toString().toUpperCase();
		} catch (Exception e) {
		}
		
		return sql;
	}
	
	public List<String[][]> getDynrpt_sqlcol() {
		return dynrpt_sqlcol;
	}

	public void setDynrpt_sqlcol(List<String[][]> dynrpt_sqlcol) {
		this.dynrpt_sqlcol = dynrpt_sqlcol;
	}
	
	public List<String[]> getRptlist() {
		return rptlist;
	}

	public void setRptlist(List<String[]> rptlist) {
		this.rptlist = rptlist;
	}
	
	//取字段的数据类型
	public String getSqlColDataType(String tname,String field)
	{
		String dataType="";
		String mainrpt=reportid;
		for(int i=0;i<dynrpt_sqlcol.size();i++){
			if((mainrpt+tname).equalsIgnoreCase(dynrpt_sqlcol.get(i)[0][1]) && field.equalsIgnoreCase(dynrpt_sqlcol.get(i)[0][2]))
			{
				dataType=dynrpt_sqlcol.get(i)[0][3];
				break;
			}
		}
		if(dataType.equals(""))dataType=CNCELLTYPESTRING;
		return dataType;
	}
	
	//取报表的所有查询字段(字段、类型)
	public List<String[]> getRptFieldList(String tname){
		List<String[]> list = new ArrayList<String[]>();
		for(int i=0;i<dynrpt_sqlcol.size();i++){
			if(tname.equalsIgnoreCase(dynrpt_sqlcol.get(i)[0][1]))
			{
				String[] tmp = new String[2];
				tmp[0]=dynrpt_sqlcol.get(i)[0][2];
				tmp[1]=dynrpt_sqlcol.get(i)[0][3];
				list.add(tmp);
			}
		}
		return list;
	}
	
	//判断参数是否已经在查询字段里在（报表Id，参数"name"）
	public boolean ixexistsF(String rptid,String paraname){
		boolean brtn = false;
		for(int i=0;i<dynrpt_sqlcol.size();i++){
			String mainrrptid=dynrpt_sqlcol.get(i)[0][0];
			String currrptidtname=dynrpt_sqlcol.get(i)[0][1];
			String tname=currrptidtname.replace(mainrrptid, "");
			
			String currrptid=getRptIdByCellPreName(tname);
			String curpname=dynrpt_sqlcol.get(i)[0][2];
			
			if(currrptid.equalsIgnoreCase(rptid) && paraname.equalsIgnoreCase(curpname))
			{
				brtn = true;
				break;
			}
		}
		return brtn;
	}
	//获取单元格类型
	public String getCellTyle(CellStyle cellstyle){
		String celltype = "";
		switch(cellstyle.getDataFormat()){
		case 0://字符
			celltype = CNCELLTYPESTRING;
		    break;
		case 177://数字0位小数
			celltype = CNCELLTYPEBIGDECIMAL;
			break;
		case 178://数字1位小数
			celltype = CNCELLTYPEBIGDECIMAL;
			break;
		case 176://数字2位小数
			celltype = CNCELLTYPEBIGDECIMAL;
			break;
		case 180://数字3位小数
			celltype = CNCELLTYPEBIGDECIMAL;
			break;
		case 181://数字4位小数
			celltype = CNCELLTYPEBIGDECIMAL;
			break;
	    default:
	    	celltype = CNCELLTYPESTRING;
	    break;
		}
		return celltype;
	}
	
	//获取单元格内容不格式化
	private String getCellValueNOFamt(Cell cell)
	{
		String rtnval="";
		if(cell != null)
		{
			switch (cell.getCellType()) {
	        case Cell.CELL_TYPE_NUMERIC:   
	            try {
	            	double d = cell.getNumericCellValue();
	            	BigDecimal bd = new BigDecimal(d);   
	            	bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP); 
	            	rtnval = bd.toString();
	            }catch (Exception e) {
				}
	            break;
	        case Cell.CELL_TYPE_STRING:   
	        	rtnval = cell.getStringCellValue();
	            break;
	        default:
	            break;   
	        }
		}
		return rtnval;
	}
	
	public String getCellValue(Cell cell)
	{
		String rtnval="";
		if(cell != null)
		{
			switch (cell.getCellType()) {
	        case Cell.CELL_TYPE_NUMERIC:   
	            try {
	            	double d = cell.getNumericCellValue();
	            	BigDecimal bd = new BigDecimal(d);   
	            	bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP); 
	            	rtnval = bd.toString();
	            }catch (Exception e) {
				}
	            break;
	        case Cell.CELL_TYPE_STRING:   
	        	rtnval = cell.getStringCellValue();
	            break;
	        default:
	            break;   
	        }
		}
		return rtnval.toUpperCase();
	}
	
	public String getCellValueNormal(Cell cell)
	{
		String rtnval="";
		if(cell != null)
		{
			switch (cell.getCellType()) {
	        case Cell.CELL_TYPE_NUMERIC:   
	            try {
	            	double d = cell.getNumericCellValue();
	            	BigDecimal bd = new BigDecimal(d);   
	            	bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP); 
	            	rtnval = bd.toString();
	            }catch (Exception e) {
				}
	            break;
	        case Cell.CELL_TYPE_STRING:   
	        	rtnval = cell.getStringCellValue();
	            break;
	        default:
	            break;   
	        }
		}
		return rtnval;
	}
	//获取单元格内容,指定数值精度
	public String getCellValue(Cell cell,int scale)
	{
		String rtnval="";
		if(cell != null)
		{
			switch (cell.getCellType()) {
	        case Cell.CELL_TYPE_NUMERIC:   
	            try {
	            	double d = cell.getNumericCellValue();
	            	BigDecimal bd = new BigDecimal(d);
	            	bd = bd.setScale(scale, BigDecimal.ROUND_HALF_UP);
	            	rtnval = bd.toString();
	            }catch (Exception e) {
				}
	            break;
	        case Cell.CELL_TYPE_STRING:
	        	rtnval = cell.getStringCellValue();
	            break;
	        default:
	            break;
	        }
		}
		return rtnval.toUpperCase();
	}
	
	//取得报表Cell值里的前最$p{t1.name}-->t1
	public String getPreNameByCell(String cellvalue){
		String tname="";
		for(int i=0;i<rptpre.length;i++){
			if(cellvalue.indexOf(rptpre[i])!=-1)
				cellvalue = cellvalue.replace(rptpre[i], "");
		}
		if(cellvalue.indexOf(".")!=-1){
			tname = cellvalue.substring(0, cellvalue.indexOf("."));
		}
		return tname.toUpperCase();
	}
	//取上一列的前缀$p{t1.name}-->t1
	public String getPreCellValueByCellOfPrecell(Sheet sheetcur, int rowInd, int colInd){
		String tname="";
		int precolInd = colInd-1;
		Row row=sheetcur.getRow(rowInd);
		
		String precellvalue="";
		String flag="";
		for(int i=0;i<rptRegionList.size();i++){
			CellRangeAddress r = sheetcur.getMergedRegion(i);
			if(r.isInRange(rowInd, precolInd))
			{
				Row rowtmp=sheetcur.getRow(r.getFirstRow());
				precellvalue=getCellValue(rowtmp.getCell(r.getFirstColumn()));
				flag="1";
				break;
			}
		}
		if(flag.equals("")){
			precellvalue = getCellValue(row.getCell(precolInd));
		}
		return precellvalue.toUpperCase();
	}
	//excel多sheet列表
	public void SetMainSubRptList(String mainrptid,String sheetname){
		String[] temp = new String[2];
		temp[0]=mainrptid;
		temp[1]=sheetname;
		dynrpt_rpt_file.add(temp);
	}
	//保存生成的报表list(parentrtpId,rptId,t1,开始列，第几个列作为标志区域列,开始行，结束行)
	public String SetRptList(String currrptid,String currcell,int begincol,int fixcolid,String iscmflag, int rowInd, int colInd,Sheet sheetcur,String areaname){
		String[] temp = new String[9];
		String rtnval="";
		String tname=getPreNameByCell(currcell);//$p{t1.name}-->t1
		
		int lastrowidx=rowInd;
		if(iscmflag.equals(""))
			lastrowidx=getCellLastRowIdx(sheetcur, currrptid, rowInd, colInd);
		
		if(currrptid.equalsIgnoreCase(""))
		{
			temp[0] = "";
		    if(mianrptno!=0)
		    {
		    	rptcnt = rptcnt + 1;
		    	temp[1] = reportid+Integer.toString(mianrptno+rptcnt);
		    	rtnval=reportid+Integer.toString(mianrptno+rptcnt);
		    }
		    else
		    {
		    	temp[1] = reportid;
		    	rtnval=reportid;
		    }
			temp[2] = tname;
			temp[3] = Integer.toString(begincol);
			temp[4] = Integer.toString(fixcolid);
			temp[5] = Integer.toString(rowInd);//开始行
			temp[6] = Integer.toString(lastrowidx);//结束行
			temp[7] = areaname;
			temp[8] = "0";//结束列
			rptlist.add(temp);
			
		}else{
			rptcnt = rptcnt + 1;
			String newrptid = currrptid+String.valueOf(rptcnt);
			temp[0] = currrptid;
			temp[1] = newrptid;
			temp[2] = tname;
			temp[3] = Integer.toString(begincol);
			temp[4] = Integer.toString(fixcolid);
			temp[5] = Integer.toString(rowInd);//开始行
			temp[6] = Integer.toString(lastrowidx);//结束行
			temp[7] = areaname;
			temp[8] = "0";//结束列
			
			rptlist.add(temp);
			rtnval=newrptid;
		}
		return rtnval;
	}
	//保存生成的报表//结束行，结束列)
	public void SetRptEndCol(String currrptid,int rowInd, int colInd){
		for(int i=0;i<rptlist.size();i++){
			if(rptlist.get(i)[1].equalsIgnoreCase(currrptid))
			{
				rptlist.get(i)[6] = Integer.toString(rowInd);//结束行
				rptlist.get(i)[8] = Integer.toString(colInd);//结束列
				break;
			}
		}
	}
	//保存报表宽度
	public void SetRptpagewidth(String currrptid,int pagewidth)
	{
		String[] temp = new String[2];
		temp[0] = currrptid;
		temp[1] = Integer.toString(pagewidth);
		rptpagewidthlist.add(temp);
	}
	public int getRptpagewidth(String currrptid)
	{
		int pagewidth=0;
		for(int i=0;i<rptpagewidthlist.size();i++){
			if(rptpagewidthlist.get(i)[0].equalsIgnoreCase(currrptid))
			{
				pagewidth=Integer.parseInt(rptpagewidthlist.get(i)[1]);
				break;
			}
		}
		return pagewidth;
	}
	
	//判断是否是父报表
	public String chkishavesub(String currrptid)
	{
		String ishave="";
		String mainrpt=getMainRptId(mianrptno);
		if(mainrpt.equals(currrptid))ishave="1";
		else{
		for(int i=0;i<rptlist.size();i++){
			if(rptlist.get(i)[0].equalsIgnoreCase(currrptid))
			{
				ishave="1";
				break;
			}
		}}
		return ishave;
	}
	
	//取报表的sql语句前缀，没有说明没设置过sql  存放生成的报表文件列表，(父报表id，报表Id，列名前缀，第几个子报表｛确定前面列的区域类型｝)
	public String getrptsql(String currrptid){
		String rtn="";
		for(int i=0;i<rptlist.size();i++){
			if(rptlist.get(i)[1].equalsIgnoreCase(currrptid))
			{
				rtn = rptlist.get(i)[2];
				break;
			}
		}
		return rtn;
	}
	public void setRptListTname(String currrptid,String tname)
	{
		for(int i=0;i<rptlist.size();i++){
			if(rptlist.get(i)[1].equalsIgnoreCase(currrptid))
			{
				rptlist.get(i)[2] = tname;
				break;
			}
		}
	}
	//rptAreaHeightlist;//存放生成的报表区域高度，(报表Id，区域，高度,区域索引（同样的是第几个区域）)
	public void SetRptAreaMaxHeight(String currrptid,String areaname,int currareaheight ,int idx,String updflag,String rwhissum){
		String flag="";
		for(int i=0;i<rptAreaHeightlist.size();i++){
			if(rptAreaHeightlist.get(i)[0].equalsIgnoreCase(currrptid)&&rptAreaHeightlist.get(i)[1].equalsIgnoreCase(areaname)
					&&rptAreaHeightlist.get(i)[3].equalsIgnoreCase(Integer.toString(idx)))
			{
				int areahgt = Integer.parseInt(rptAreaHeightlist.get(i)[2]);
				
				if(updflag.equals("1"))
				{
					rptAreaHeightlist.get(i)[2]=Integer.toString(currareaheight);
				}else
				{
					if("1".equals(rwhissum))
					{
						rptAreaHeightlist.get(i)[2]=Integer.toString(currareaheight+areahgt);
					}else
					{
						if(currareaheight>areahgt)
							rptAreaHeightlist.get(i)[2]=Integer.toString(currareaheight);
					}
				}
				flag="1";
				break;
			}
		}
		if(flag.equals("")){
			String[] temp = new String[4];
			temp[0]=currrptid;
			temp[1]=areaname;
			temp[2]=Integer.toString(currareaheight);
			temp[3]=Integer.toString(idx);
			rptAreaHeightlist.add(temp);
		}
		//System.out.println(currrptid+",areaname:"+areaname+",areaght:"+currareaheight);
	}
	
	public String ishavetitle(String currrptid)
	{
		String ishave="1";
		for(int i=0;i<rptAreaHeightlist.size();i++){
			if("title".equalsIgnoreCase(rptAreaHeightlist.get(i)[1]))
			{
				ishave="0";
				break;
			}
		}
		return ishave;
	}
	public int getAreaHeight2(String currrptid,String areaname, String idx)
	{
		int areaght=0;
		for(int i=0;i<rptAreaHeightlist.size();i++){
			if(rptAreaHeightlist.get(i)[0].equalsIgnoreCase(currrptid)&&rptAreaHeightlist.get(i)[1].equalsIgnoreCase(areaname)
					&&rptAreaHeightlist.get(i)[3].equalsIgnoreCase(idx))
			{
				areaght = Integer.parseInt(rptAreaHeightlist.get(i)[2]);
				break;
			}
		}
		return areaght;
	}
	
	//rptlist;       //存放生成的报表文件列表，(父报表id，报表Id，列名前缀，第几个子报表｛确定前面列的区域类型｝)
	public int getAreaHeight(Sheet sheetcur,String currrptid,String areaname)
	{
		int areaght=0;
		int rptfixcloid=getRptFixcolIdx(currrptid);//取报表的区域的固定列
		int getrptbgnrow=getRptBeginRow(currrptid);
		String preareaname="";
		String flag="";
		int rows = sheetcur.getLastRowNum() - sheetcur.getFirstRowNum();
		for (int i = getrptbgnrow; i <= rows; i++) {
			Row row = sheetcur.getRow(i);
			if(row == null) break;
			Cell cell = row.getCell(rptfixcloid);
			String rptrowtype=getCellValue(cell);
			String currareaname="";
			if(rptrowtype.equalsIgnoreCase(CNTITLE)){ //"标题"
				currareaname = "title";
			}else if(rptrowtype.equalsIgnoreCase(CNPGHEAD)){//"表头"
				currareaname = "columnHeader";
			}else if(rptrowtype.equalsIgnoreCase(CNPGDTL)){ //"明细"
				currareaname = "detail";
			}else if(rptrowtype.equalsIgnoreCase(CNPGSUM)){ //"小计"
				currareaname = "summary";
			}else if(rptrowtype.equalsIgnoreCase(CNSUM)){   //"总计"
				currareaname = "summary";
			}else if(rptrowtype.equalsIgnoreCase(CNFOOT)){  //"表尾"
				currareaname = "summary";
			}
			if(!currareaname.equals(preareaname)&&flag.equals("1"))
				break;
			if(areaname.equalsIgnoreCase(currareaname)){
				areaght = areaght + (int)row.getHeightInPoints();
				flag="1";
			}
			preareaname=currareaname;
		}
		//System.out.println("currrptid:"+currrptid+",areaname:"+areaname+",areaght:"+areaght);
		return areaght;
	}
	
	//保存生成的子报表在父报表的区域里的idx(父报表Id，报表Id，区域，idx)
	public void SetsubRptList(String parentRptId,String currrptid,String areaname){
		String rtnval="";
		int idx=0;
		for(int i=0;i<rptAreaIdxlist.size();i++){
			if(rptAreaIdxlist.get(i)[0].equalsIgnoreCase(parentRptId)&&rptAreaIdxlist.get(i)[2].equalsIgnoreCase(areaname))
			{
				idx++;
				rtnval="1";
			}
		}
		if(rtnval.equalsIgnoreCase("")){
			idx=0;
		}
		String[] temp = new String[4];
		temp[0]=parentRptId;
		temp[1]=currrptid;
		temp[2]=areaname;
		temp[3]=Integer.toString(idx);
		rptAreaIdxlist.add(temp);
	}
	//取子报表在父报表的区
	public String getsubRptArea(String parentRptId,String currrptid,String areaname){
		String rtnval="";
		for(int i=0;i<rptlist.size();i++){
			if(rptlist.get(i)[0].equalsIgnoreCase(parentRptId)&&rptlist.get(i)[1].equalsIgnoreCase(currrptid))
			{
				rtnval=rptlist.get(i)[7];
				break;
			}
		}
		if(rtnval.equals(""))rtnval=areaname;
		return rtnval;
	}
	
	public int getsubRptList(String parentRptId,String currrptid){
		int idx=0;
		for(int i=0;i<rptAreaIdxlist.size();i++){
			if(rptAreaIdxlist.get(i)[0].equalsIgnoreCase(parentRptId)&&rptAreaIdxlist.get(i)[1].equalsIgnoreCase(currrptid))
			{
				idx = Integer.parseInt(rptAreaIdxlist.get(i)[3]);
				break;
			}
		}
		return idx;
	}
	//设置主报表的t前缀名
	public void SetMainRptList(String currrptid,String currcell){
		String[] temp = new String[5];
		String rtnval="";
		String tname=getPreNameByCell(currcell);//$p{t1.name}-->t1
		for(int i=0;i<rptlist.size();i++){
			if(rptlist.get(i)[1].equalsIgnoreCase(currrptid))
			{
				rptlist.get(i)[2]=tname;
				break;
			}
		}
	}
	
	//取报表的开始列
	public int getRptBegincol(String currrptid){
		int begincol=0;
		
		for(int i=0;i<rptlist.size();i++){
			if(rptlist.get(i)[1].equalsIgnoreCase(currrptid))
			{
				begincol = Integer.parseInt(rptlist.get(i)[3]);
				break;
			}
		}
		return begincol;
	}
	
	//取报表的开始列
	public int getRptendcol(String currrptid){
		int endcol=0;
		
		for(int i=0;i<rptlist.size();i++){
			if(rptlist.get(i)[1].equalsIgnoreCase(currrptid))
			{
				endcol = Integer.parseInt(rptlist.get(i)[8]);
				break;
			}
		}
		return endcol;
	}
	
	//取报表的开始行
	public int getRptBeginRow(String currrptid){
		int beginrow=0;
		
		for(int i=0;i<rptlist.size();i++){
			if(rptlist.get(i)[1].equalsIgnoreCase(currrptid))
			{
				beginrow = Integer.parseInt(rptlist.get(i)[5]);
				break;
			}
		}
		return beginrow;
	}
	
	//取报表对应的前面固定列的列号码
	public int getRptFixcolIdx(String currrptid){
		int fixcolIdx=0;
		if(cncolumn>1){
			for(int i=0;i<rptlist.size();i++){
				if(rptlist.get(i)[1].equalsIgnoreCase(currrptid))
				{
					fixcolIdx = Integer.parseInt(rptlist.get(i)[4]);
					break;
				}
			}
	    }
		return fixcolIdx;
	}
	
	//取报表对应的最后行号
	public int[] getRptLastRowIdx(String currrptid){
		int[] fisrtlastrowIdx={0,0};
		
		for(int i=0;i<rptlist.size();i++){
			if(rptlist.get(i)[1].equalsIgnoreCase(currrptid))
			{
				fisrtlastrowIdx[0] = Integer.parseInt(rptlist.get(i)[5]);
				fisrtlastrowIdx[1] = Integer.parseInt(rptlist.get(i)[6]);
				break;
			}
		}
		return fisrtlastrowIdx;
	}
	
	//获取主报表Id
	public String getMainRptId(int m){
		String mainrpt="";
		int cnt=0;
		for(int i=0;i<rptlist.size();i++){
			if(rptlist.get(i)[0].equalsIgnoreCase(""))
			{
				if(m==cnt)
					mainrpt = rptlist.get(i)[1];
				cnt=cnt+1;
			}
		}
		return mainrpt;
	}
	
	//根据报表的id或字段的前缀名从list里查找父报表
	public String getParentByRptId(String rptid){
		String parentrpt="";
		for(int i=0;i<rptlist.size();i++){
			if(rptid.equalsIgnoreCase(rptlist.get(i)[1]))
				parentrpt = rptlist.get(i)[0];
		}
		return parentrpt;
	}
	
	//根据报表Cell值里的前最从list里查找父报表
	public String getParentByCell(String cellvalue){
		String parentrpt="";
		String tname=getPreNameByCell(cellvalue);//$p{t1.name}-->t1
		
		for(int i=0;i<rptlist.size();i++){
			if(tname.equalsIgnoreCase(rptlist.get(i)[2]))
				parentrpt = rptlist.get(i)[0];
		}
		return parentrpt;
	}
	
	//根据报表Cell值里的前缀从list里查找报表Id
	public String getRptIdByCellPreName(String tname){
		String rptid="";
		for(int i=0;i<rptlist.size();i++){
			if(tname.equalsIgnoreCase(rptlist.get(i)[2]))
				rptid = rptlist.get(i)[1];
		}
		return rptid;
	}
	
	//去除cell里的前缀名,如$F{t.name}-->$F{name}最为报表的参数
	public String getNoPreCellValue(String cellvalue){
		String rtncellvalue="";
		String tname=getPreNameByCell(cellvalue);//$p{t1.name}-->t1
		rtncellvalue = cellvalue.replace(tname+".", "");
		return rtncellvalue;
	}
	//去除cell里的前缀名,如$F{t.name}-->name
	public String getNoPreNoCellValue(String cellvalue){
		String rtncellvalue="";
		int i1=cellvalue.indexOf(".");
		int i2=cellvalue.indexOf("}");
		if(i1!=-1&&i2!=-1)
			rtncellvalue = cellvalue.substring(i1+1, i2);
		else
			rtncellvalue = cellvalue;
		return rtncellvalue;
	}
	
	//初始化前缀列表
	public void initrptPreList(){
		int pcnt = p_rptpre.length;
		int p1cnt = p1_rptpre.length;
		int fcnt = f_rptpre.length;
		int vcnt = v_rptpre.length;
		rptpre = new String[pcnt+p1cnt+fcnt+vcnt];
		int m=0;
		for(int i=0;i<p_rptpre.length;i++){
			rptpre[m] = p_rptpre[i];
			m++;
		}
		for(int i=0;i<p1_rptpre.length;i++){
			rptpre[m] = p1_rptpre[i];
			m++;
		}
		for(int i=0;i<f_rptpre.length;i++){
			rptpre[m] = f_rptpre[i];
			m++;
		}
		for(int i=0;i<v_rptpre.length;i++){
			rptpre[m] = v_rptpre[i];
			m++;
		}
	}
	
	//解析CellValue中的参数$P{t.a}/$F{t.a}
	//数据$p{t.name}数据 --> 数据$p{name}数据,"P","name"，$p{name}
	public String[] getParamByCellValue(String cellvalue)
	{
		String[] temp = new String[4];
		String flag="";
		for(int i=0;i<p_rptpre.length;i++){
			int idx=cellvalue.indexOf(p_rptpre[i]);
			if(idx!=-1)
			{
				String pre = p_rptpre[i];
				String pstr=getNoPreCellValue(cellvalue);
				temp[0]=pstr;
				temp[1]=pre.replace("$", "").replace("{", "");
				idx=pstr.indexOf(p_rptpre[i])+p_rptpre[i].length();
				temp[2]=pstr.substring(idx,pstr.indexOf("}"));
				temp[3]=p_rptpre[i]+temp[2]+"}";
				flag=pre;
				break;
			}
		}
		if(flag.equalsIgnoreCase("")){
			for(int i=0;i<p1_rptpre.length;i++){
				int idx=cellvalue.indexOf(p1_rptpre[i]);
				if(idx!=-1)
				{
					String pre = p1_rptpre[i];
					String pstr=getNoPreCellValue(cellvalue);
					temp[0]=pstr;
					temp[1]=pre.replace("$", "").replace("{", "").replace("!", "");
					idx=pstr.indexOf(p1_rptpre[i])+p1_rptpre[i].length();
					temp[2]=pstr.substring(idx,pstr.indexOf("}"));
					temp[3]=p1_rptpre[i]+temp[2]+"}";
					flag=pre;
					break;
				}
			}
		}
		if(flag.equalsIgnoreCase("")){
			for(int i=0;i<f_rptpre.length;i++){
				int idx=cellvalue.indexOf(f_rptpre[i]);
				if(idx!=-1)
				{
					String pre = f_rptpre[i];
					String pstr=getNoPreCellValue(cellvalue);
					temp[0]=pstr;
					temp[1]=pre.replace("$", "").replace("{", "");
					idx=pstr.indexOf(f_rptpre[i])+f_rptpre[i].length();
					temp[2]=pstr.substring(idx,pstr.indexOf("}"));
					temp[3]=f_rptpre[i]+temp[2]+"}";
					
					flag=pre;
					
					break;
				}
			}
		}
		
		if(flag.equalsIgnoreCase("")){
		
			for(int i=0;i<v_rptpre.length;i++){
				int idx=cellvalue.indexOf(v_rptpre[i]);
				if(idx!=-1)
				{
					String pre = v_rptpre[i];
					String pstr=getNoPreCellValue(cellvalue);
					temp[0]=pstr;
					temp[1]=pre.replace("$", "").replace("{", "");
					idx=pstr.indexOf(v_rptpre[i])+v_rptpre[i].length();
					temp[2]=pstr.substring(idx,pstr.indexOf("}"));
					temp[3]="$V{"+temp[2]+"}";
					
					flag=pre;
					break;
				}
			}
		}
		if(flag.equalsIgnoreCase("")){
			temp[0]=cellvalue;
			temp[1]="";
			temp[2]=cellvalue;
			temp[3]=cellvalue;
		}
		return temp;
	}
	
	//解析Sql语句中的参数$P{t.a}/$F{t.a}
	//数据$p{name}数据 -->  $p{name},"P","name"，$p{name}
	public List<String[]> getParamListByCellValue(String cellvalue)
	{
		List list = new ArrayList<String[]>();
		
		String tname=getPreNameByCell(cellvalue);//得到字段前缀$p{t1.name}-->t1
		String sql=getSqlByname(tname);//获取sql语句及cellvalue
		String flag="";
		String tmp=sql;
		while(!tmp.equalsIgnoreCase("")){
			flag="";
			for(int i=0;i<p_rptpre.length;i++){
				int idx=tmp.indexOf(p_rptpre[i]);
				if(idx!=-1)
				{
					tmp = tmp.substring(idx, tmp.length());
					idx=tmp.indexOf("}");
					String[] temp = new String[4];
					
					String pre = p_rptpre[i];
					String pstr=getNoPreCellValue(tmp.substring(0, idx+1));
					
					temp[0]=pstr;
					temp[1]=pre.replace("$", "").replace("{", "");
					
					pstr=pstr.replace(pre, "");
					temp[2]=pstr.substring(0,pstr.indexOf("}"));
					temp[3]=temp[0];
					list.add(temp);
					tmp = tmp.substring(idx+1, tmp.length());
					flag="1";
				}
			}
			if(flag.equals(""))tmp="";
		}
		tmp=sql;
		while(!tmp.equalsIgnoreCase("")){
			flag="";
			for(int i=0;i<p1_rptpre.length;i++){
				int idx=tmp.indexOf(p1_rptpre[i]);
				if(idx!=-1)
				{
					tmp = tmp.substring(idx, tmp.length());
					idx=tmp.indexOf("}");
					String[] temp = new String[4];
					
					String pre = p1_rptpre[i];
					String pstr=getNoPreCellValue(tmp.substring(0, idx+1));
					
					temp[0]=pstr;
					temp[1]=pre.replace("$", "").replace("{", "").replace("!", "");
					
					pstr=pstr.replace(pre, "");
					temp[2]=pstr.substring(0,pstr.indexOf("}"));
					temp[3]=temp[0];
					list.add(temp);
					tmp = tmp.substring(idx+1, tmp.length());
					flag="1";
				}
			}
			if(flag.equals(""))tmp="";
		}
		tmp=sql;
		while(!tmp.equalsIgnoreCase("")){
			flag="";
			for(int i=0;i<f_rptpre.length;i++){
				int idx=tmp.indexOf(f_rptpre[i]);
				if(idx!=-1)
				{
					tmp = tmp.substring(idx, tmp.length());
					idx=tmp.indexOf("}");
					String[] temp = new String[4];
					
					String pre = f_rptpre[i];
					String pstr=getNoPreCellValue(tmp.substring(0, idx+1));
					
					temp[0]=pstr;
					temp[1]=pre.replace("$", "").replace("{", "");
					pstr=pstr.replace(pre, "");
					temp[2]=pstr.substring(0,pstr.indexOf("}"));
					temp[3]=temp[0];
					
					list.add(temp);
					tmp = tmp.substring(idx+1, tmp.length());
					flag="1";
				}
			}
			if(flag.equals(""))tmp="";
		}
		tmp=sql;
		while(!tmp.equalsIgnoreCase("")){
			flag="";
			for(int i=0;i<v_rptpre.length;i++){
				int idx=tmp.indexOf(v_rptpre[i]);
				if(idx!=-1)
				{
					tmp = tmp.substring(idx, tmp.length());
					idx=tmp.indexOf("}");
					String[] temp = new String[4];
					
					String pre = v_rptpre[i];
					String pstr=getNoPreCellValue(tmp.substring(0, idx+1));
					
					temp[0]=pstr;
					temp[1]=pre.replace("$", "").replace("{", "");
					pstr=pstr.replace(pre, "");
					temp[2]=pstr.substring(0,pstr.indexOf("}"));
					temp[3]=temp[0];
					
					list.add(temp);
					tmp = tmp.substring(idx+1, tmp.length());
					flag="1";
				}
			}
			if(flag.equals(""))tmp="";
		}
		return list;
	}
	
	//判断参数是否已经在报表里存在（报表Id，类型(P，F，V),参数"name"）
	public boolean isexist(String rptid,String paraname,String flag)
	{
		boolean brtn = false;
		for(int i=0;i<allparalist.size();i++){
			if(rptid.equalsIgnoreCase(allparalist.get(i)[0]) && paraname.equalsIgnoreCase(allparalist.get(i)[2]) && flag.equalsIgnoreCase(allparalist.get(i)[1]))
			{
				brtn = true;
				break;
			}
		}
		return brtn;
	}
	
	//将输入条件参数保存到报表参数对象list（报表Id，类型(P，F，V),参数"name"）
	public void setallparalist(String rptid,String parentrptid)
	{
		int hparacnt=dynrpt_hpara.size();
		for (int j = 0; j < hparacnt; j++) {
			String name = dynrpt_hpara.get(j)[j][1];
			String[] temp = {rptid,"P",name,""};
			allparalist.add(temp);//（报表Id，参数"name"，类型(P，F，V)）
		}
	}
	//判断参数是否已经绑定父报表（报表Id，类型(P，F，V),参数"name",父报表Id）
	public boolean isbind(String rptid,String paraname,String flag, String parentId)
	{
		boolean brtn = false;
		for(int i=0;i<allparalist.size();i++){
			if(rptid.equalsIgnoreCase(allparalist.get(i)[0]) && paraname.equalsIgnoreCase(allparalist.get(i)[2]) && flag.equalsIgnoreCase(allparalist.get(i)[1])
			  && parentId.equalsIgnoreCase(allparalist.get(i)[3]))
			{
				brtn = true;
				break;
			}
		}
		return brtn;
	}
	
	//设置参数已经绑定
	public boolean setParaBinded(String rptid,String paraname,String flag, String parentId)
	{
		boolean brtn = false;
		for(int i=0;i<allparalist.size();i++){
			if(rptid.equalsIgnoreCase(allparalist.get(i)[0]) && paraname.equalsIgnoreCase(allparalist.get(i)[2]) && flag.equalsIgnoreCase(allparalist.get(i)[1]))
			{
				allparalist.get(i)[3] = parentId;
				break;
			}
		}
		return brtn;
	}
	
	//判断单元格是否被合并
	public String iscombine(Sheet sheetcur,int rptidex,int rowInd, int colInd)
	{
		String brtn="0";
		String rptid=Integer.toString(rptidex);
		//rptRegionList = new ArrayList<String[]>();//存放所有报表sheet里的合并项(rptid,CellRangeAddressIndex,是否取过)
		for(int i=0;i<rptRegionList.size();i++){
			String tmprptid=rptRegionList.get(i)[0];
			String Cridx=rptRegionList.get(i)[1];
			String ised=rptRegionList.get(i)[2];
			CellRangeAddress r = sheetcur.getMergedRegion(i);
			if(r.isInRange(rowInd, colInd) && tmprptid.equalsIgnoreCase(rptid))
			{
				rptRegionList.get(i)[2]="1";
				if(ised.equalsIgnoreCase(""))
					brtn="2";
				else
					brtn="1";
				break;
			}
		}
		return brtn;
	}
	
	//判断单元格是否被合并，包含的行数最大值
	public int combinemaxRowIdx(Sheet sheetcur,int rowInd, int colInd)
	{
		int brtn=0;
		for(int i=0;i<rptRegionList.size();i++){
			CellRangeAddress r = sheetcur.getMergedRegion(i);
			if(r.isInRange(rowInd, colInd))
			{
				brtn=r.getLastRow();
				break;
			}
		}
		return brtn;
	}
	
	//判断单元格的结束行indx
	public int getCellLastRowIdx(Sheet sheetcur,String rptid,int rowInd, int colInd)
	{
		int brtn=0;
		for(int i=0;i<rptRegionList.size();i++){
			String tmprptid=rptRegionList.get(i)[0];
			String Cridx=rptRegionList.get(i)[1];
			String ised=rptRegionList.get(i)[2];
			CellRangeAddress r = sheetcur.getMergedRegion(i);
			if(r.isInRange(rowInd, colInd) && tmprptid.equalsIgnoreCase(rptid))
			{
				brtn=r.getLastRow();
				break;
			}
		}
		return brtn;
	}
	
	//获取合并单元格高度、宽度
	public int[] getcmbcellWH(Sheet sheetcur,int rptidex,int rowInd, int colInd)
	{
		int[] wh = {0,0};
		String rptid=Integer.toString(rptidex);
		//rptRegionList = new ArrayList<String[]>();//存放所有报表sheet里的合并项(rptid,CellRangeAddressIndex,是否取过)
		for(int i=0;i<rptRegionList.size();i++){
			String tmprptid=rptRegionList.get(i)[0];
			String Cridx=rptRegionList.get(i)[1];
			String ised=rptRegionList.get(i)[2];
			CellRangeAddress rg = sheetcur.getMergedRegion(i);
			if(rg.isInRange(rowInd, colInd) && tmprptid.equalsIgnoreCase(rptid))
			{
				int wl=rg.getLastColumn(),wf=rg.getFirstColumn();
				int rl=rg.getLastRow(),rf=rg.getFirstRow();
				int wd=0,ht=0;
				for(int m=rf;m<=rl;m++){
					ht=ht+(int)sheetcur.getRow(m).getHeightInPoints();
				}
				for(int n=wf;n<=wl;n++){
					wd=wd+sheetcur.getColumnWidth(n)/256;
				}
				wh[0]=ht;
				wh[1]=wd;
				break;
			}
		}
		return wh;
	}
	//获取单元格Left坐标(begincol 报表的开始列)
	public int getCellLeft(Sheet sheetcur,String currrptid,int rowInd,int colInd)
	{
		int rptbegincol = getRptBegincol(currrptid);
		if(rptbegincol == 0)rptbegincol=colInd;
		
		int cellLeft = 0;
		Row row=sheetcur.getRow(rowInd);
		for(int i=rptbegincol;i<colInd;i++){
			Cell cell = row.getCell(i);
			if(cell!=null) cellLeft = cellLeft + sheetcur.getColumnWidth(i)/256;
		}
		
		return cellLeft;
	}
	
	//获取单元格Top坐标()
	public int getCellTop(Sheet sheetcur,String currRptId,String rowtype,int rowInd,int colInd)
	{
		int rptbgrow = getRptBeginRow(currRptId);
		int cellTop = 0;
		int rptfixcolIdx=getRptFixcolIdx(currRptId);
		for(int m=rptbgrow;m<rowInd;m++){
			Row row=sheetcur.getRow(m);
			Cell cell = row.getCell(rptfixcolIdx);
			String tmptype=getCellValue(cell);
			if(tmptype.equalsIgnoreCase(rowtype))
				cellTop=cellTop+(int)sheetcur.getRow(m).getHeightInPoints();
			if(rowtype.equals(CNFOOT) && tmptype.equals(CNPGSUM))
			{
				cellTop=cellTop+(int)sheetcur.getRow(m).getHeightInPoints();
			}
		}		
		return cellTop;
	}
	
	//获取单元格在子报表里的Top坐标()
	public int getCellTopFromSub(Sheet sheetcur,String currRptId,String rowtype,int rowInd,int colInd)
	{
		int cellTop = 0;
		int rptfixcolIdx=getRptFixcolIdx(currRptId);
		int firstrowIdx=getRptLastRowIdx(currRptId)[0];//开始、结束行
		for(int m=firstrowIdx;m<rowInd;m++){
			Row row=sheetcur.getRow(m);
			Cell cell = row.getCell(rptfixcolIdx);
			if(getCellValue(cell).equalsIgnoreCase(rowtype))
				cellTop=cellTop+(int)sheetcur.getRow(m).getHeightInPoints();
		}		
		return cellTop;
	}
	
	//获取区域高度()
	public int getRowTypeMaxHeight(Sheet sheetcur,String currRptId,String rowtype)
	{
		int RowTypeMaxHeight = 0;
		int rptfixcolIdx=getRptFixcolIdx(currRptId);
		int rows = sheetcur.getLastRowNum() - sheetcur.getFirstRowNum();
		for(int m=1;m<rows;m++){
			Row row=sheetcur.getRow(m);
			if(row == null) continue;
			Cell cell = row.getCell(rptfixcolIdx);
			if(cell == null) continue;
			if(getCellValue(cell).equalsIgnoreCase(rowtype))
			{
				if (rptfixcolIdx<cncolumn)
				{
					Cell cell1 = row.getCell(rptfixcolIdx+1);
					if(!"".equalsIgnoreCase(getCellValue(cell1)))
						RowTypeMaxHeight=RowTypeMaxHeight+(int)sheetcur.getRow(m).getHeightInPoints();
				}else{
					RowTypeMaxHeight=RowTypeMaxHeight+(int)sheetcur.getRow(m).getHeightInPoints();
				}
			}
		}
		return RowTypeMaxHeight;
	}
	
	//保存sheet中包含的语句名
	public void SaveSheetsTName(String currrptid,String currcellpre,int shetidx)
	{
		boolean brtn = false;
		for(int i=0;i<shttnamelist.size();i++){
			String rptid=shttnamelist.get(i)[0];
			String tname=shttnamelist.get(i)[1];
			if(currrptid.equalsIgnoreCase(rptid) && currcellpre.equalsIgnoreCase(tname))
			{
				brtn = true;
				break;
			}
		}
		if (!brtn)
		{
			String[] temp = new String[3];
			temp[0]=currrptid;
			temp[1]=currcellpre;
			temp[2]=Integer.toString(shetidx);
			shttnamelist.add(temp);
		}
	}
	//图表列对应的xls列标号
	public void setimgcolparamIdx(String fieldname,int colidx,int shetidx){
		boolean brtn = false;
		for(int i=0;i<imgcolparam.size();i++){
			String temp=imgcolparam.get(i)[0];
			if(fieldname.equals(temp))
			{
				imgcolparam.get(i)[4]=Integer.toString(colidx);
				imgcolparam.get(i)[5]=Integer.toString(shetidx);
				break;
			}
		}
	}
	//读取配置文件内容
    public String readcfgfile(String filename)throws Exception  
    {
        String rtnMsg="";
	    Workbook workbook = null;
	    
	    initrptPreList();
	    
		try {
			workbook = WorkbookFactory.create(new FileInputStream(filename));
			
			//获取报表设置参数
			Sheet sheet = workbook.getSheet("${设置}");
			if(sheet==null) return "Excel文档格式不对！";
			//报表参数
			dynrptver=getCellValue(sheet.getRow(2).getCell((short)3));//版本
			reportname=getCellValue(sheet.getRow(12).getCell((short)3));//报表标题
			reportid=getCellValue(sheet.getRow(6).getCell((short)3));  //报表文件Id
			rptmaker=getCellValue(sheet.getRow(7).getCell((short)3));  //制作人
			rptmakedt=getCellValue(sheet.getRow(6).getCell((short)7)); //制作时间
			rptversion=getCellValue(sheet.getRow(8).getCell((short)3));//版本
			rptremark=getCellValue(sheet.getRow(7).getCell((short)7)); //备注
			
//			ismpage="";   //HTML是否分页(1:分页 2：不分页)
//			
//			
//			viewtype="";  //图表(1:表格2：图标3：全部)
//			
//			try{
//				repeatsht=""; //重复sheet的sql语句
//			}catch(Exception e){
//				repeatsht="";
//			}
			//数据位置
			iptbegin="15";  //查询条件开始行
			iptend="34";    //查询条件结束行
			sqlbegin="15";  //sql数据源开始行数
			sqlend="34";    //sql数据源结束行数
			batchbegin="38"; //批量开始行数
			batchend="48";   //批量导出结束行数
			pagesqlbegin="38";//分页开始行数
			pagesqlend="48";//分页sql结束行数
			printbegin="52";//打印参数开始行数
			printend="62";//打印参数结束行数
			
			//打印设置
//			printdirect=getCellValue(sheet.getRow(52).getCell((short)2),0);//打印方向(1:普通 2:横打)
//			if(!printdirect.equals(""))printdirect=printdirect.substring(0,1);
//			printrate="";    //打印比例(%)
//			printalign=getCellValue(sheet.getRow(38).getCell((short)3),0); //打印页面对齐方式(1:水平居中 2:垂直居中 3:both)
//			if(!printalign.equals(""))printalign=printalign.substring(0,1);
//			
//			autowidth=getCellValue(sheet.getRow(38).getCell((short)4));  //纸张大小
//			pagefoot=getCellValue(sheet.getRow(38).getCell((short)5));   //是否显示页脚(1:是 2:否)
//			
//			batchexpdate = getCellValue(sheet.getRow(38).getCell((short)12));   //批量导出日期
//			batchexpdep = getCellValue(sheet.getRow(38).getCell((short)13));   //批量导出营业部
//			
//			if(!pagefoot.equals(""))pagefoot=pagefoot.substring(0,1);
			
			dynrpt_rpt_file = new ArrayList<String[]>();
			dynrpt_hpara = new  ArrayList<String[][]>();
			dynrpt_sqlcol = new  ArrayList<String[][]>();
			
			batchparam = new ArrayList<String[][]>();
			printparam = new ArrayList<String[][]>();
			imgcolparam = new ArrayList<String[]>();
			shttnamelist = new ArrayList<String[]>();
			
			sqlmap = new TreeMap();
			pagesqlmap = new TreeMap();
			
			//取得查询条件
			int MAXARRLEN = 8;
			int rows = sheet.getLastRowNum() - sheet.getFirstRowNum();
			int maplen=0;
			for (int i = 14; i <= rows; i++) {
				Row row = sheet.getRow(i);
				if(row == null) break;
				int celllen = sheet.getRow(i).getLastCellNum();
				if(i+1>=Integer.parseInt(iptbegin) && i+1<=Integer.parseInt(iptend) ){       //查询条件
					String[][] temp = new String[rows][MAXARRLEN+1];
					String flag="";
					for (int j = 3; j < MAXARRLEN+3; j++) {
						String cellvalue = "";
						if(j<=celllen&&j!=7)
							cellvalue = getCellValue(sheet.getRow(i).getCell((short)j),0);
						else if(j<=celllen&&j==7)
							cellvalue = getCellValueNOFamt(sheet.getRow(i).getCell((short)j));//sql不大写
						if(j==3 && "".equalsIgnoreCase(cellvalue)) 
						{
							flag="1";
							break;
						}
						if(j==4) cellvalue = cellvalue.replace("$P{", "").replace("}", "") ;
						temp[maplen][j-3] = cellvalue;
						if(j==5){
							if(cellvalue.equals("数字框"))
								temp[maplen][MAXARRLEN]=CNCELLTYPEBIGDECIMAL;
							else
								temp[maplen][MAXARRLEN]=CNCELLTYPESTRING;
						}
					}
					if(flag.equals("")){
						maplen++;
						dynrpt_hpara.add(temp);
					}
			    }
				//取得查询sql
				if(i+1>=Integer.parseInt(sqlbegin) && i+1<=Integer.parseInt(sqlend) ){ //sql语句
			    	String key=getCellValue(sheet.getRow(i).getCell((short)15));
			    	String value=getCellValueNOFamt(sheet.getRow(i).getCell((short)16));
			    	if(!key.equals("") && !value.equals(""))
			    		sqlmap.put(key, value);
			    }
		    }
			
			//取得批量导出参数
			int BATCHMAXARRLEN = 8;
			int batchmaplen=0;
			for(int i=38;i<rows;i++){
				Row row = sheet.getRow(i);
				if(row == null) break;
				int celllen = sheet.getRow(i).getLastCellNum();
				if(i+1>=Integer.parseInt(batchbegin) && i+1<=Integer.parseInt(batchend) ){ //批量导出参数
					String[][] temp = new String[rows][BATCHMAXARRLEN+1];
					String flag="";
					for (int j = 3; j < BATCHMAXARRLEN+3; j++) {
						String cellvalue = "";
						if(j<=celllen)
							cellvalue = getCellValue(sheet.getRow(i).getCell((short)j),0);
						if(j==3 && "".equalsIgnoreCase(cellvalue)) 
						{
							flag="1";
							break;
						}
						temp[batchmaplen][j-3] = cellvalue;
					}
					if(flag.equals("")){
						batchmaplen++;
						batchparam.add(temp);
					}
				}
				//取得分页sql语句名称
				if(i+1>=Integer.parseInt(pagesqlbegin) && i+1<=Integer.parseInt(pagesqlend) ){ //分页sql语句名称
			    	String key=getCellValue(sheet.getRow(i).getCell((short)15));
			    	String value=getCellValue(sheet.getRow(i).getCell((short)16));
			    	if(!key.equals("") && !value.equals(""))
			    		pagesqlmap.put(key, value);
			    }
			}
			
			//取得打印参数
			int PRINTMAXARRLEN=4;
		    int printmaplen=0;
		    for(int i=52;i<rows;i++){
				Row row = sheet.getRow(i);
				if(row == null) break;
				int celllen = sheet.getRow(i).getLastCellNum();
				if(i+1>=Integer.parseInt(printbegin) && i+1<=Integer.parseInt(printend) ){       //打印参数
					String[][] temp = new String[rows][PRINTMAXARRLEN+1];
					String flag="";
					for (int j = 3; j < PRINTMAXARRLEN+3; j++) {
						String cellvalue = "";
						if(j<=celllen)
							cellvalue = getCellValue(sheet.getRow(i).getCell((short)j),0);
						if(j==3 && "".equalsIgnoreCase(cellvalue)) 
						{
							flag="1";
							break;
						}
						temp[printmaplen][j-3] = cellvalue;
					}
					if(flag.equals("")){
						printmaplen++;
						printparam.add(temp);
					}
				}
				
			}
			
		  //取得导出txt参数
		  try{
		      isoutTxt = getCellValue(sheet.getRow(53).getCell((short)16))+"-";   //是否导出txt(1:txt 0:否 2:dbf 3:txt、dbf)
		      isoutTxt = isoutTxt.split("-")[0];
			  pixs = getCellValue(sheet.getRow(52).getCell((short)14));       //像素
			  emptsperpix = getCellValue(sheet.getRow(52).getCell((short)15));//每像素空格数
			  fildemps = getCellValue(sheet.getRow(52).getCell((short)16));   //字段间空格数
		  }catch (Exception e) {
		  }
		  //短信参数
		  try{
			  issendmsg=getCellValue(sheet.getRow(62).getCell((short)16))+"-";  //是否发送短信
			  issendmsg = issendmsg.split("-")[0];
			  useid=getCellValue(sheet.getRow(58).getCell((short)15));
			  upwd=getCellValue(sheet.getRow(59).getCell((short)15));
			  appid=getCellValue(sheet.getRow(60).getCell((short)15));
			  content=getCellValue(sheet.getRow(61).getCell((short)15));//短信内容
		  }catch(Exception e){
			  
		  }
		  
		  //图标设置
		  try{
			  xlstype=getCellValue(sheet.getRow(6).getCell((short)11));//excel类型
			  int imgcolmaplen=0;
			  for(int i=66;i<rows;i++){
				Row row = sheet.getRow(i);
				if(row == null) break;
				int celllen = sheet.getRow(i).getLastCellNum();
				String[] temp = new String[6];
				String cellvalue = "";
				if (celllen>=7) cellvalue = getCellValue(sheet.getRow(i).getCell((short)7));
				else cellvalue = "";
				temp[0] = cellvalue;
				if (celllen>=8) cellvalue = getCellValue(sheet.getRow(i).getCell((short)8));
				else cellvalue = "";
				temp[1] = cellvalue;
				if (celllen>=9) cellvalue = getCellValue(sheet.getRow(i).getCell((short)9));
				else cellvalue = "";
				temp[2] = cellvalue;
				if (celllen>=10) cellvalue = getCellValue(sheet.getRow(i).getCell((short)10));
				else cellvalue = "";
				temp[3] = cellvalue;
				temp[4] = "";
				temp[5] = "";
				imgcolparam.add(temp);
				imgcolmaplen++;
			  }
			  
			  isoutmg=getCellValue(sheet.getRow(65).getCell((short)4),0); 
			  imgtype=getCellValue(sheet.getRow(66).getCell((short)3),0); 
			  imgbeginrow=getCellValue(sheet.getRow(67).getCell((short)3),0);
			  imgendrow=getCellValue(sheet.getRow(68).getCell((short)3),0); 
			  imgxtitle=getCellValue(sheet.getRow(69).getCell((short)3),0); 
			  imgytitle=getCellValue(sheet.getRow(70).getCell((short)3),0); 
			  imgbgcolor=getCellValue(sheet.getRow(71).getCell((short)3),0); 
			  imgfontsize=getCellValue(sheet.getRow(72).getCell((short)3),0);
			  
			  imgistip=getCellValue(sheet.getRow(73).getCell((short)3),0);
			  imgistext=getCellValue(sheet.getRow(74).getCell((short)3),0);
			  imgxcnt=getCellValue(sheet.getRow(75).getCell((short)3),0);
			  try{
				  isshowimgtitle=getCellValue(sheet.getRow(76).getCell((short)3),0);
			  }catch(Exception e){
				  isshowimgtitle="";
			  }
			  try{
				  isshowdatagrid=getCellValue(sheet.getRow(77).getCell((short)3),0);
			  }catch(Exception e){
				  isshowdatagrid="";
			  }
			  
		  }catch(Exception e){
			  
		  }
		  sheet = null;
		  workbook = null;
		} catch (InvalidFormatException e) {
			rtnMsg = e.getMessage();
		}
        return rtnMsg;
    }
    
  //读取导入数据配置文件内容
    public String readImpCfgFile(String filename)throws Exception  
    {
        String rtnMsg="";
	    Workbook workbook = null;
	    
	    //initrptPreList();
	    
		try {
			workbook = WorkbookFactory.create(new FileInputStream(filename));
			
			//获取报表设置参数
			Sheet sheet = workbook.getSheet("${设置}");
			if(sheet==null) return "Excel文档格式不对！";
			//报表参数
			reportname=getCellValueNormal(sheet.getRow(12).getCell((short)3));//报表标题
			findSheetName = getCellValueNormal(sheet.getRow(37).getCell((short)3));//查找工作簿名
			reportid=getCellValueNormal(sheet.getRow(6).getCell((short)3));  //报表文件Id
			rptmaker=getCellValueNormal(sheet.getRow(7).getCell((short)3));  //制作人
			rptmakedt=getCellValueNormal(sheet.getRow(6).getCell((short)7)); //制作时间
			rptversion=getCellValueNormal(sheet.getRow(8).getCell((short)3));//版本
			rptremark=getCellValueNormal(sheet.getRow(7).getCell((short)7)); //备注
			resultSql=getCellValueNormal(sheet.getRow(52).getCell((short)3)); //备注
			
			//数据位置
			conditionBegin = 15-1;  //查询条件开始行
			conditionEnd = 34-1;    //查询条件结束行
			valBegin = 15-1;//明细开始行
			valEnd = 51-1;//明细结束行
			findBegin = 40-1;//查找开始行
			findEnd = 44-1;//查找结束行
			sqlBegin = 49-1;//导入语句开始行
			sqlEnd = 52-1;//导入语句结束行
			
			conditions = new TreeMap<Integer, List<String>>();
			valParams = new ArrayList<List<String>>();
			findConfigs = new HashMap<String, List<String>>();
			impSqls = new TreeMap<Integer, TreeMap<Integer,String>>();
			
			//取得查询条件
			int COLLEN = 7;
			int index = 0;
			for (int i = conditionBegin; i <= conditionEnd; i++) {
				Row row = sheet.getRow(i);
				if(row == null) break;
				
				int countNull = 0;
				List<String> condis = new ArrayList<String>();
				for(int j=3;j<=9;j++){
					Cell cell = row.getCell(j);
					if(cell == null || "".equals(Exl2TxtUtil.getCellFormatValue(cell).trim())){
						countNull++;
						condis.add("");
					}else {
						condis.add(Exl2TxtUtil.getCellFormatValue(cell).trim());
					}
				}
				if(countNull != COLLEN){
					conditions.put(index++, condis);
				}
		    }
		  
			//取得明细
			index = 0;
			for(int i=valBegin;i<=valEnd;i++){
				Row row = sheet.getRow(i);
				if(row == null) break;
				
				List<String> params = new ArrayList<String>();
				for(int j=14;j<=15;j++){
					Cell cell = row.getCell(j);
					params.add(Exl2TxtUtil.getCellFormatValue(cell).trim());
				}
				valParams.add(params);
			}
			
			//取得查找
			for(int i=findBegin;i<=findEnd;i++){
				Row row = sheet.getRow(i);
				if(row == null) break;
				
				List<String> configs = new ArrayList<String>();
				for(int j=3;j<=7;j++){
					Cell cell = row.getCell(j);
					configs.add(Exl2TxtUtil.getCellFormatValue(cell).trim());
				}
				findConfigs.put(Exl2TxtUtil.getCellFormatValue(row.getCell(2)).trim(), configs);
			}
			
			//取得导入语句
			index = 0;
			for(int i=sqlBegin;i<=sqlEnd;i++){
				Row row = sheet.getRow(i);
				if(row == null) break;
				
				TreeMap<Integer,String> sqls = new TreeMap<Integer,String>();
				int jindex = 0;
				for(int j=3;j<=10;j++){
					Cell cell = row.getCell(j);
					if(cell != null && !"".equals(Exl2TxtUtil.getCellFormatValue(cell).trim())){
						sqls.put(jindex++, Exl2TxtUtil.getCellFormatValue(cell).trim());
					}
				}
				impSqls.put(index++, sqls);
			}
			
		  sheet = null;
		  workbook = null;
		} catch (InvalidFormatException e) {
			rtnMsg = e.getMessage();
		}
        return rtnMsg;
    }
    
    //读取rpt模版内容
    public String readmdlfile(String filename)throws Exception  
    {
    	String rtnMsg="";
	    Workbook workbook = null;
	    dynbodys="";
		  try {
			workbook = WorkbookFactory.create(new FileInputStream(filename));
			
			int currrptcnt=0;
			//读取report模版
			int sheetcnt = workbook.getNumberOfSheets();//获取sheet数量
			mianrptno=0;
			rptlist = new ArrayList<String[]>();
			rptpagewidthlist = new ArrayList<String[]>();
			TrendReport trendReport = new TrendReport(reportid);
			for(int i=0;i<sheetcnt;i++){
				Sheet sheetcur = workbook.getSheetAt(i);
				String sheetname = sheetcur.getSheetName();
				if(sheetname.indexOf("${") == -1){
					rptAreaHeightlist = new ArrayList<String[]>();
					allparalist = new ArrayList<String[]>();
					rptRegionList = new ArrayList<String[]>();
					rptAreaIdxlist = new ArrayList<String[]>();
					//子报表数
					int subrptcount=0;
					//取sheet里的合并单元格数
					int rptrgcnt = sheetcur.getNumMergedRegions();
					for(int rg=0;rg<rptrgcnt;rg++){
						String ss=sheetcur.getMergedRegion(rg).toString();
						String[] temp = new String[3];
						temp[0]=Integer.toString(i);
						temp[1]=Integer.toString(rg);
						temp[2]="";
						rptRegionList.add(temp);
					}
					
					cncolumn = Integer.parseInt(getCellValue(sheetcur.getRow(0).getCell((short)0),0));//固定列数
					
					int rows = sheetcur.getLastRowNum() - sheetcur.getFirstRowNum();//sheet行数
					String currrptid="";
					String currcell="";
					String currcellpre="",lastcellpre="";//字段前缀
					
					//判断空列、隐藏列数量
					Row row0 = sheetcur.getRow(0);
					
					int nullcolLen=0;
					String ishavenull="";
					if(row0 != null){
						int celllen = row0.getLastCellNum();//sheet列数
						for(int n = cncolumn; n<celllen; n++){
							Cell cell = row0.getCell((short)n);
							currcell = getCellValue(cell);    //单元值
							if(currcell.equalsIgnoreCase(CNNULLHIDE))//"空列,隐藏"
							{
								nullcolLen++;
								ishavenull="1";
							}
						}
					}
					String mainrpt="";
					int rptwidth=0;
					int rowhgttop=0;//行高
					int rowhgtbottom=0;
					int hearcnt=0;
					
					String mainsql="";
					String ishavemainrpt="";
					//多行高度汇总
					String mycurrrptId="";
					String myoldrptId="";
					
					for (int m = 1; m <= rows; m++) {
						Row row = sheetcur.getRow(m);
						if(row == null) break;
						int celllen = row.getLastCellNum();//sheet列数
						int rowwidth=0;//行宽
						int rowheight=0;
						String cellType = "";
						CellAllStyle cellstyle = new CellAllStyle();
						rowhgtbottom=0;
						rowheight = (int)row.getHeightInPoints();
						int cellwd=0,cellht=0;
						lastcellpre="";
						subrptcount=0;
						String currareaname="";
						String preareaname="";
						
						for(int n = cncolumn; n<celllen; n++){
							Cell cell = row.getCell((short)n);
							
							if(cell == null)continue;
							CellStyle eStyle = cell.getCellStyle();
							currcell = getCellValue(cell);    //单元值
							String iscmflag=iscombine(sheetcur,i,m, n);//是否合并的单元格
							currcellpre = getPreNameByCell(currcell);
							
							setimgcolparamIdx(currcell,n,i-1);
							//获取type
							String nopreCellvalue = getNoPreCellValue(currcell);//$F{name}
							
							String getNoPreNoCellValue=getNoPreNoCellValue(currcell);
							String type=getSqlColDataType(currcellpre,getNoPreNoCellValue);
							if(!type.equalsIgnoreCase(""))
								cellType = type;
							else
								cellType = getCellTyle(eStyle);
							
							//System.out.println(currcell);
							String preCellValue=getPreCellValueByCellOfPrecell(sheetcur, m, n);;
							if(lastcellpre.equals("")&&n==cncolumn+1)//取上一列CellValue
							{
							    lastcellpre=getPreNameByCell(preCellValue);//$p{t1.name}-->t1
						    }
							/*if (iscmflag.equals("0") && ((n == cncolumn+1)报表第一列) && (!currcellpre.equalsIgnoreCase(lastcellpre) || (!currcell.equals("") && !preCellValue.equals(currcell))))
							{
								currrptid=mainrpt;
							}*/
							
							int rptfixcloid=getRptFixcolIdx(currrptid);//取报表的区域的固定列
							String rptrowtype=getCellValue(sheetcur.getRow(m).getCell((short)rptfixcloid));//第m行第0列
							String rptcoltype=getCellValue(sheetcur.getRow(0).getCell((short)n),0);//第0行第n列
							
							String areaname="";
							if(rptrowtype.equalsIgnoreCase(CNTITLE)){ //"标题"
								areaname = "title";
							}else if(rptrowtype.equalsIgnoreCase(CNPGHEAD)){//"表头"
								areaname = "columnHeader";
								dynbodys=dynbodys+getCellValueNormal(cell)+","+Integer.toString(i-1)+"@@";
							}else if(rptrowtype.equalsIgnoreCase(CNPGDTL)){ //"明细"
								areaname = "detail";
							}else if(rptrowtype.equalsIgnoreCase(CNPGSUM)){ //"小计"
								areaname = "summary";
							}else if(rptrowtype.equalsIgnoreCase(CNSUM)){   //"总计"
								areaname = "summary";
							}else if(rptrowtype.equalsIgnoreCase(CNFOOT)){  //"表尾"
								areaname = "summary";
							}
							
							if(iscmflag.equals("1")){
								int[] tmp=getcmbcellWH(sheetcur,i,m, n);
								cellwd=tmp[1];
								continue;
							}else if(iscmflag.equals("2")){
								int[] tmp=getcmbcellWH(sheetcur,i,m, n);
								cellht=tmp[0];
								cellwd=tmp[1];
							}else{
								cellht=rowheight;
								cellwd=sheetcur.getColumnWidth(n)/256;
							}
							
							if((n == cncolumn && ishavenull.equals("")) || (n == cncolumn+1 && ishavenull.equals("1")))
							{
							    if(!areaname.equalsIgnoreCase("columnHeader"))
								SetRptAreaMaxHeight(currrptid,areaname,cellht,0,"","");
							}
							if(rptcoltype.equalsIgnoreCase(CNNULLHIDE)&&currcell.equalsIgnoreCase("")){//"空列,隐藏"
								continue;
							}
							
							if(((n == cncolumn && ishavenull.equals("")) || (n == cncolumn+1 && ishavenull.equals("1")))&&rptrowtype.equalsIgnoreCase(CNPGHEAD)){//多表头
								hearcnt=hearcnt+1;
								currareaname=areaname;
						    }
							
							int colleft = getCellLeft(sheetcur, currrptid, m,n);
							rowhgttop=getCellTop(sheetcur,currrptid,rptrowtype,m,n);
							//System.out.println("areaname:"+areaname+"currcell:"+currcell+",rptrowtype:"+rptrowtype+",m:"+m+",n:"+n+",colleft:"+colleft+",rowhgttop:"+rowhgttop+",cellwd:"+cellwd+",cellht:"+cellht);
							cellstyle.setRowLeft(new String().valueOf(colleft));
							cellstyle.setRowTop(new String().valueOf(rowhgttop));
							cellstyle.setRowWidth(new String().valueOf(cellwd));
							cellstyle.setRowHeight(new String().valueOf(cellht));
							
							cellstyle.setCellType(cellType);
							
							if(!eStyle.getDataFormatString().equalsIgnoreCase("General"))
							{
								int tmpi=eStyle.getDataFormatString().indexOf("_)");
								if(tmpi == -1)
									cellstyle.setDateformat(eStyle.getDataFormatString());
								else
									cellstyle.setDateformat(eStyle.getDataFormatString().substring(0,tmpi ));
							}
							rowhgtbottom=rowhgtbottom+cellht;
							rowwidth=rowwidth+cellwd;//列宽度
							
							Font eFont = workbook.getFontAt(eStyle.getFontIndex());
							cellstyle.setFontsize(Integer.toString(eFont.getFontHeight()));
							cellstyle.setFontstyle(eFont.getFontName());
							cellstyle.setBoldlight(Integer.toString(eFont.getBoldweight()));
							cellstyle.setHorizonta(Integer.toString(eStyle.getAlignment()));
							cellstyle.setVertical(Integer.toString(eStyle.getVerticalAlignment()));
							
							if(mainrpt.equals("")){//创建主报表
								currrptid = SetRptList(currrptid,currcell,n,subrptcount,iscmflag,m,n,sheetcur,areaname);
								trendReport.CreateJrxml(currrptid);
								cellstyle.setAlldata(reportname);
								mycurrrptId=currrptid;
								//设置查询条件参数,每一个报表都应定义
								setallparalist(currrptid,"");
								for (int a = 0; a < dynrpt_hpara.size(); a++) {
									CellAllStyle schstl = new CellAllStyle();
									schstl.setNosigndata(dynrpt_hpara.get(a)[a][1]);
									
									String schtmp=getSqlColDataType(currcellpre,dynrpt_hpara.get(a)[a][1]);
									schstl.setCellType(schtmp);
									
									trendReport.setParameter(currrptid, schstl);
								}
								mainrpt=currrptid;
								ishavemainrpt="1";
							}else{
								//应该判断字段的前缀，不一样的才是子报表
								if((!lastcellpre.equalsIgnoreCase("") && !currcellpre.equalsIgnoreCase(lastcellpre) && !currcell.equals("") && !preCellValue.equals(""))
								  ||(/*多表头*/currareaname.equalsIgnoreCase("columnHeader")&&!currareaname.equalsIgnoreCase(preareaname))&&(n == cncolumn && ishavenull.equals("")) || (n == cncolumn+1 && ishavenull.equals("1"))){
									//System.out.println(currcell+":"+preCellValue);
									SetRptEndCol(currrptid,m,n);//设置结束列
									if(!"".equalsIgnoreCase(getRptIdByCellPreName(currcellpre))&&!currcellpre.equals("")){
										//比如从T1.name列到t2.name在到t1.Id时
										currrptid=getRptIdByCellPreName(currcellpre);
										mycurrrptId=currrptid;
										int collefttmp = getCellLeft(sheetcur, currrptid, m,n);
										cellstyle.setRowLeft(new String().valueOf(collefttmp));
									}else{
									//判断当前和上一格是否相同，不同说明子报表，当是小计等就直接去父报表包括的下一列的第一个子报表，否则新建一个子报表
									//当前报表的行标志
									rptfixcloid=getRptFixcolIdx(currrptid);
									rptrowtype=getCellValue(sheetcur.getRow(m).getCell((short)rptfixcloid));//第m行第列
									if(!rptrowtype.equalsIgnoreCase(CNPGSUM)&&!rptrowtype.equalsIgnoreCase(CNSUM)){ //不等于"小计"\"总计"
										subrptcount++;
										String parrptid="";
										if(lastcellpre.equals(""))
											parrptid=mainrpt;
										else
											parrptid=getRptIdByCellPreName(lastcellpre);//取父报表id
										
										String rptsubid = "";
										String oldarea=areaname;//多表头
										if(hearcnt>1)//多表头
										{
											areaname="detail";
											
										}
										rptsubid=SetRptList(parrptid,currcell,n,subrptcount,iscmflag,m,n,sheetcur,areaname);
										trendReport.CreateJrxml(rptsubid);//创建子报表
										
										//保存生成的子报表在父报表的区域里的idx(父报表Id，报表Id，区域，idx)
										if(areaname.equalsIgnoreCase("detail"))
											SetsubRptList(parrptid,rptsubid,areaname);
										
										rowhgttop=getCellTop(sheetcur,rptsubid,rptrowtype,m,n);
										cellstyle.setRowLeft(new String().valueOf(0));
										
										cellstyle.setRowTop(new String().valueOf(rowhgttop));
										
										SetRptAreaMaxHeight(rptsubid,oldarea,cellht,0,"","");//设置子报表区域高读
										
										//System.out.println(rptrowtype+":"+"currcell:"+currcell+",m:"+m+",n:"+n+",colleft:"+colleft+",rowhgttop:"+rowhgttop+",cellwd:"+cellwd+",cellht:"+cellht);
										
										if(!currcellpre.equalsIgnoreCase("")){
											String sql = "";
											String excelsql=getSqlByname(currcellpre);
											Iterator it=pagesqlmap.keySet().iterator();
											Boolean sqlflag = false;
											while(it.hasNext()){
												String key=(String)it.next();
												if(key!=""&&key.equalsIgnoreCase(currcellpre)){
													sqlflag = true;
													String sqltmp = "SELECT a.*,ROWNUM as rn FROM ("+ excelsql +") a WHERE  ROWNUM <= $P{ENDRECORD}";
													sql = "SELECT * FROM ("+ sqltmp +") WHERE rn >= $P{STARTRECORD} ";
													String prptid=getParentByRptId(rptsubid);
													int subrptidx=getsubRptList(prptid,rptsubid);
													trendReport.setBandValue(prptid, rptsubid, getsubRptArea(prptid,rptsubid,areaname), "$P{STARTRECORD}", "$P{STARTRECORD}", subrptidx);
													trendReport.setBandValue(prptid, rptsubid, getsubRptArea(prptid,rptsubid,areaname), "$P{ENDRECORD}", "$P{ENDRECORD}", subrptidx);
												}
											}
											if(!sqlflag){
												sql = excelsql;
											}
											trendReport.setQueryString(rptsubid, sql);
//											trendReport.setQueryString(rptsubid, getSqlByname(currcellpre));
											//取报表的所有查询字段(字段、类型)
											List<String[]> RptFieldList = getRptFieldList(mainrpt+currcellpre);
											if(RptFieldList!=null){
												for(int rpfl=0;rpfl<RptFieldList.size();rpfl++){
													String[] rpfltmp= new String[3];
													rpfltmp[0]=rptsubid;
													rpfltmp[1]="F";
													rpfltmp[2]=RptFieldList.get(rpfl)[0];
													CellAllStyle rpflstl = new CellAllStyle();
													rpflstl.setAlldata("$F{"+rpfltmp[2]+"}");
													rpflstl.setSigndata("$F{"+rpfltmp[2]+"}");
													rpflstl.setNosigndata(rpfltmp[2]);
													
													String schtmp=getSqlColDataType(currcellpre,rpfltmp[2]);
													rpflstl.setCellType(schtmp);
													
													trendReport.setField(rptsubid, rpflstl);
													allparalist.add(rpfltmp);//（报表Id，类型(P，F，V),参数"name"）
												}
											}
										}
										int subrptidx=getsubRptList(parrptid,rptsubid);
										if(subrptidx!=0)trendReport.addDetailBand(parrptid, rptsubid, cellstyle);
										
										int tmpcurrhgt=cellht;
										//判断单元格是否被合并，包含的行数最大值
										int cbmaxRowIdx=combinemaxRowIdx(sheetcur, m, n-1);
										//System.out.println("currcell:"+currcell+":"+Integer.toString(cbmaxRowIdx)+":"+Integer.toString(tmpcurrhgt));
										for(int h=m+1;h<=cbmaxRowIdx;h++)
										{
											Row tmprow = sheetcur.getRow(h);
											Cell tmpcell=tmprow.getCell((short)n);
											String iscmflagtmp=iscombine(sheetcur,i,h, n);//是否合并的单元格
											int tmpficolid=getRptFixcolIdx(rptsubid);
											String tmprptrowtype=getCellValue(sheetcur.getRow(h).getCell((short)tmpficolid));//第m行第列
											//是合并列或不是小计列不算同一个报表
											if(!iscmflagtmp.equals("0")||!tmprptrowtype.equalsIgnoreCase(CNPGSUM))continue;
											String tmpnextrowpre = getPreNameByCell(getCellValue(tmpcell));
											if(tmpnextrowpre.equalsIgnoreCase(currcellpre)
													||(tmpnextrowpre.equals("")&&!currcellpre.equals(""))
													||(currcellpre.equals("")&&!tmpnextrowpre.equals(""))){
												tmpcurrhgt =tmpcurrhgt + (int)tmprow.getHeightInPoints();
												//判断是否被包含于同一个cell
											}else
											{
												break;
											}
										}
										//System.out.println("currcell:"+currcell+":"+Integer.toString(cbmaxRowIdx)+":"+Integer.toString(tmpcurrhgt));
										
										rowhgttop=0;
										cellstyle.setRowTop(Integer.toString(rowhgttop));
										CellAllStyle subcellstyle = new CellAllStyle();
										subcellstyle.setRowHeight(Integer.toString(tmpcurrhgt));
										subcellstyle.setRowLeft(Integer.toString(colleft));
										subcellstyle.setRowTop(Integer.toString(rowhgttop));
										subcellstyle.setRowWidth(Integer.toString(cellwd));
										
										subcellstyle.setAlldata(cellstyle.getAlldata());
										subcellstyle.setCellType(cellstyle.getCellType());
										subcellstyle.setDateformat(cellstyle.getDateformat());
										subcellstyle.setNosigndata(cellstyle.getNosigndata());
										subcellstyle.setSigndata(cellstyle.getSigndata());

										subcellstyle.setFontsize(Integer.toString(eFont.getFontHeight()));
										subcellstyle.setFontstyle(eFont.getFontName());
										subcellstyle.setBoldlight(Integer.toString(eFont.getBoldweight()));
										subcellstyle.setHorizonta(Integer.toString(eStyle.getAlignment()));
										subcellstyle.setVertical(Integer.toString(eStyle.getVerticalAlignment()));
										
										
										trendReport.setSubReport(mainrpt,parrptid, areaname, rptsubid, subcellstyle/*子报表的高度*/,subrptidx);
										if(parrptid.equals(mainrpt))
											SetRptAreaMaxHeight(parrptid,areaname,cellht,subrptidx,"1","");
										
										//设置查询条件参数
										setallparalist(rptsubid,parrptid);
										for (int a = 0; a < dynrpt_hpara.size(); a++) {
											String pn=dynrpt_hpara.get(a)[a][1];
											CellAllStyle schstl = new CellAllStyle();
											schstl.setNosigndata(pn);
											
											String schtmp=getSqlColDataType(currcellpre,pn);
											schstl.setCellType(schtmp);
											
											trendReport.setParameter(rptsubid, schstl);
										}
										//子报表的行标志
										rptfixcloid=getRptFixcolIdx(rptsubid);
										rptrowtype=getCellValue(sheetcur.getRow(m).getCell((short)rptfixcloid));//第m行第列
										if(rptrowtype.equalsIgnoreCase(CNTITLE)){ //"标题"
											areaname = "title";
										}else if(rptrowtype.equalsIgnoreCase(CNPGHEAD)){//"表头"
											areaname = "columnHeader";
										}else if(rptrowtype.equalsIgnoreCase(CNPGDTL)){ //"明细"
											areaname = "detail";
										}else if(rptrowtype.equalsIgnoreCase(CNPGSUM)){ //"小计"
											areaname = "summary";
										}else if(rptrowtype.equalsIgnoreCase(CNSUM)){   //"总计"
											areaname = "summary";
										}else if(rptrowtype.equalsIgnoreCase(CNFOOT)){  //"表尾"
											areaname = "summary";
										}
										currrptid = rptsubid;
										mycurrrptId=currrptid;
									}
								}
								}
							}
							//保存sheet中包含的语句名
							SaveSheetsTName(currrptid,currcellpre,i-1);
							
							if(!currcellpre.equalsIgnoreCase(""))
							lastcellpre = currcellpre;
							
							if(rptcoltype.equalsIgnoreCase(CNNULLHIDE)&&!currcell.equalsIgnoreCase("")){//"空列,隐藏"
								if(nullcolLen>1)//如果有多个空列隐藏列只全部把空列隐藏列作为子报表，否则作为主报表
								{
									subrptcount++;
									String rptsubid = SetRptList(currrptid,currcell,n,subrptcount,iscmflag,m,n,sheetcur,areaname);
									trendReport.CreateJrxml(rptsubid);//创建子报表
									
									//保存生成的子报表在父报表的区域里的idx(父报表Id，报表Id，区域，idx)
									if(areaname.equalsIgnoreCase("detail"))
										SetsubRptList(currrptid,rptsubid,areaname);
									
									rowhgttop=getCellTop(sheetcur,rptsubid,rptrowtype,m,n);
									cellstyle.setRowLeft(new String().valueOf(0));
									cellstyle.setRowTop(new String().valueOf(rowhgttop));
									
									//System.out.println(rptrowtype+":"+"currcell:"+currcell+",m:"+m+",n:"+n+",colleft:"+colleft+",rowhgttop:"+rowhgttop+",cellwd:"+cellwd+",cellht:"+cellht);
									
									int subrptidx=getsubRptList(currrptid,rptsubid);
									if(subrptidx!=0)trendReport.addDetailBand(currrptid, rptsubid, cellstyle);
									trendReport.setSubReport(mainrpt,currrptid, areaname, rptsubid, cellstyle,subrptidx);
									
									String prename=getPreNameByCell(currcell);
									String sql = "";
									String excelsql=getSqlByname(prename);
									Iterator it = pagesqlmap.keySet().iterator();
									Boolean sqlflag = false;
									while(it.hasNext()){
										String key = (String)it.next();
										if(key!=""&&key.equalsIgnoreCase(prename)){
											sqlflag = true;
											String sqltmp = "SELECT a.*,ROWNUM as rn FROM ("+ excelsql +") a WHERE  ROWNUM <= $P{ENDRECORD}";
											sql = "SELECT * FROM ("+ sqltmp +") WHERE rn >= $P{STARTRECORD} ";
											String prptid=getParentByRptId(rptsubid);
											int subrptidex=getsubRptList(prptid,rptsubid);
											trendReport.setBandValue(prptid, rptsubid, getsubRptArea(prptid,rptsubid,areaname), "$P{STARTRECORD}", "$P{STARTRECORD}", subrptidex);
											trendReport.setBandValue(prptid, rptsubid, getsubRptArea(prptid,rptsubid,areaname), "$P{ENDRECORD}", "$P{ENDRECORD}", subrptidex);
										}
									}
									if(!sqlflag){
										sql = excelsql;
									}
									trendReport.setQueryString(rptsubid, sql);
									
									//设置查询条件参数
									setallparalist(rptsubid,currrptid);
									for (int a = 0; a < dynrpt_hpara.size(); a++) {
										String pn=dynrpt_hpara.get(a)[a][1];
										CellAllStyle schstl = new CellAllStyle();
										schstl.setNosigndata(pn);
										
										String schtmp=getSqlColDataType(currcellpre,pn);
										schstl.setCellType(schtmp);
										
										trendReport.setParameter(rptsubid, schstl);
									}
									//取报表的所有查询字段(字段、类型)
									List<String[]> RptFieldList = getRptFieldList(mainrpt+currcellpre);
									if(RptFieldList!=null){
										for(int rpfl=0;rpfl<RptFieldList.size();rpfl++){
											String[] rpfltmp= new String[3];
											rpfltmp[0]=rptsubid;
											rpfltmp[1]="F";
											rpfltmp[2]=RptFieldList.get(rpfl)[0];
											CellAllStyle rpflstl = new CellAllStyle();
											rpflstl.setAlldata("$F{"+rpfltmp[2]+"}");
											rpflstl.setSigndata("$F{"+rpfltmp[2]+"}");
											rpflstl.setNosigndata(rpfltmp[2]);
											rpflstl.setCellType(RptFieldList.get(rpfl)[1]);
											trendReport.setField(rptsubid, rpflstl);
											//System.out.println(rpfltmp[2]+":"+RptFieldList.get(rpfl)[1]);
											allparalist.add(rpfltmp);//（报表Id，类型(P，F，V),参数"name"）
										}
									}
									SetRptAreaMaxHeight(rptsubid,areaname,cellht,subrptidx,"","");
									
									//子报表的行标志
									rptfixcloid=getRptFixcolIdx(rptsubid);
									rptrowtype=getCellValue(sheetcur.getRow(m).getCell((short)rptfixcloid));//第m行第列
									if(rptrowtype.equalsIgnoreCase(CNTITLE)){ //"标题"
										areaname = "title";
									}else if(rptrowtype.equalsIgnoreCase(CNPGHEAD)){//"表头"
										areaname = "columnHeader";
									}else if(rptrowtype.equalsIgnoreCase(CNPGDTL)){ //"明细"
										areaname = "detail";
									}else if(rptrowtype.equalsIgnoreCase(CNPGSUM)){ //"小计"
										areaname = "summary";
									}else if(rptrowtype.equalsIgnoreCase(CNSUM)){   //"总计"
										areaname = "summary";
									}else if(rptrowtype.equalsIgnoreCase(CNFOOT)){  //"表尾"
										areaname = "summary";
									}
									currrptid = rptsubid;
									mycurrrptId=currrptid;
								}else{
									// 设置sql
									String prename=getPreNameByCell(currcell);
									String sql = "";
									String excelsql=getSqlByname(prename);
									Iterator it = pagesqlmap.keySet().iterator();
									Boolean sqlflag = false;
									while(it.hasNext()){
										String key = (String)it.next();
										if(key!=""&&key.equalsIgnoreCase(prename)){
											sqlflag = true;
											String sqltmp = "SELECT a.*,ROWNUM as rn FROM ("+ excelsql +") a WHERE  ROWNUM <= $P{ENDRECORD}";
											sql = "SELECT * FROM ("+ sqltmp +") WHERE rn >= $P{STARTRECORD} ";
											String prptid=getParentByRptId(mainrpt);
											int subrptidx=getsubRptList(prptid,mainrpt);
											trendReport.setBandValue(prptid, mainrpt, getsubRptArea(prptid,mainrpt,areaname), "$P{STARTRECORD}", "$P{STARTRECORD}", subrptidx);
											trendReport.setBandValue(prptid, mainrpt, getsubRptArea(prptid,mainrpt,areaname), "$P{ENDRECORD}", "$P{ENDRECORD}", subrptidx);
										}
									}
									if(!sqlflag){
										sql = excelsql;
									}
									mainsql=sql;
									trendReport.setQueryString(mainrpt, sql);
									SetMainRptList(mainrpt,currcell);
									//取报表的所有查询字段(字段、类型)
									List<String[]> RptFieldList = getRptFieldList(mainrpt+currcellpre);
									if(RptFieldList!=null){
										for(int rpfl=0;rpfl<RptFieldList.size();rpfl++){
											String[] rpfltmp= new String[3];
											rpfltmp[0]=mainrpt;
											rpfltmp[1]="F";
											rpfltmp[2]=RptFieldList.get(rpfl)[0];
											CellAllStyle rpflstl = new CellAllStyle();
											rpflstl.setAlldata("$F{"+rpfltmp[2]+"}");
											rpflstl.setSigndata("$F{"+rpfltmp[2]+"}");
											rpflstl.setNosigndata(rpfltmp[2]);
											rpflstl.setCellType(RptFieldList.get(rpfl)[1]);
											trendReport.setField(mainrpt, rpflstl);
											//System.out.println(rpfltmp[2]+":"+RptFieldList.get(rpfl)[1]);
											allparalist.add(rpfltmp);//（报表Id，类型(P，F，V),参数"name"）
										}
									}
								}
							}else{
								//System.out.println(rptrowtype+":"+"currcell:"+currcell+",m:"+m+",n:"+n+",colleft:"+colleft+",rowhgttop:"+rowhgttop+",cellwd:"+cellwd+",cellht:"+cellht);
								
								//新建单元格里的参数 数据$p{name}数据,"P","name"，$p{name}
								String[] tmp = new String[4];
								tmp=getParamByCellValue(currcell);
								cellstyle.setAlldata(tmp[0]);
								cellstyle.setSigndata(tmp[3]);
								cellstyle.setNosigndata(tmp[2]);

								//把新建的参数放入参数列表
								String tmp2[] = {currrptid,tmp[1],tmp[2],""};//currrptid,"P","name"
								if(!isexist(currrptid,tmp[2],tmp[1])){
									if("P".equalsIgnoreCase(tmp[1])){
										trendReport.setParameter(currrptid,cellstyle);
										//System.out.println("currrptid:"+currrptid+":flag:"+tmp[1]+":name:"+tmp[0]);
									}else if("F".equalsIgnoreCase(tmp[1])){
										//System.out.println("currrptid:"+currrptid+":"+cellstyle.getNosigndata());
										//判断当前报表是否已经设置过sql
										if("".equals(getrptsql(currrptid)))
										{
											
											String sql = "";
											String excelsql=getSqlByname(currcellpre);
											Iterator it = pagesqlmap.keySet().iterator();
											Boolean sqlflag = false;
											while(it.hasNext()){
												String key = (String)it.next();
												if(key!=""&&key.equalsIgnoreCase(currcellpre)){
													sqlflag = true;
													String sqltmp = "SELECT a.*,ROWNUM as rn FROM ("+ excelsql +") a WHERE  ROWNUM <= $P{ENDRECORD}";
													sql = "SELECT * FROM ("+ sqltmp +") WHERE rn >= $P{STARTRECORD} ";
													String prptid=getParentByRptId(currrptid);
													int subrptidx=getsubRptList(prptid,currrptid);
													trendReport.setBandValue(prptid, currrptid, getsubRptArea(prptid,currrptid,areaname), "$P{STARTRECORD}", "$P{STARTRECORD}", subrptidx);
													trendReport.setBandValue(prptid, currrptid, getsubRptArea(prptid,currrptid,areaname), "$P{ENDRECORD}", "$P{ENDRECORD}", subrptidx);
												}
											}
											if(!sqlflag){
												sql = excelsql;
											}
											trendReport.setQueryString(currrptid, sql);
//											trendReport.setQueryString(currrptid, getSqlByname(currcellpre));
											setRptListTname(currrptid,currcellpre);
										}
										trendReport.setField(currrptid, cellstyle);
									}else if(tmp[1]!=null && !"".equalsIgnoreCase(tmp[1])){// $p{name},"P","name"
										trendReport.setVariable(currrptid, "Report", tmp[1], cellstyle);
									}
									if(tmp[1]!=null && !tmp[1].equals(""))
										allparalist.add(tmp2);//（报表Id，类型(P，F，V),参数"name"）
								}
								String prptid=getParentByRptId(currrptid);
								//将sql语句的参数也加入参数列表 $p{t.name} --> $p{name},"P","name"
								if(tmp[1]!=null && !"".equalsIgnoreCase(tmp[1]) && tmp[2]!=null && !"".equalsIgnoreCase(tmp[2])){
								    List<String[]> list = new ArrayList<String[]>();
								    list = getParamListByCellValue(currcell);
								    CellAllStyle Sqlcellstyle = new CellAllStyle();
								    for(int a=0;a<list.size();a++){
								    	
								    	//$p{name}数据,"P","name"，$p{name}
								    	String t1=list.get(a)[0];//$p{name}数据
								    	String t2=list.get(a)[1];//"P"
								    	String t3=list.get(a)[2];//"name"
								    	String t4=list.get(a)[3];//$p{name}
								    	
								    	String tmp21[] = {currrptid,t2,t3,""};//currrptid,"P","name"
								    	Sqlcellstyle.setAlldata(t1);
								    	Sqlcellstyle.setSigndata(t4);
								    	Sqlcellstyle.setNosigndata(t3);
								    	
								    	String schtmp=getSqlColDataType(currcellpre,t3);
								    	Sqlcellstyle.setCellType(schtmp);
										
								    	if(!isexist(currrptid,tmp21[2],tmp21[1])){
								    		if("P".equalsIgnoreCase(t2)){
												trendReport.setParameter(currrptid, Sqlcellstyle);
												//System.out.println("currrptid:"+currrptid+":flag:"+t2+":name:"+t1);
											}else if("F".equalsIgnoreCase(t2)){
												//System.out.println("currrptid:"+currrptid+":"+cellstyle.getNosigndata());
												trendReport.setField(currrptid, cellstyle);
											}else if(t2!=null){
												trendReport.setVariable(currrptid, "Report", t2, Sqlcellstyle);
											}
								    		if(tmp2[2]!=null && !"".equalsIgnoreCase(tmp21[2]))
								    			allparalist.add(tmp21);//（报表Id，参数"name"，类型(P，F，V)）
								    	}
										
								    	//父报表没有这个参数也增加变量
								    	if(prptid.equalsIgnoreCase(""))prptid=mainrpt;
								    	String tmp22[] = {prptid,t2,t3,""};//currrptid,"P","name"
								    	
								    	if(!isexist(prptid,tmp22[2],tmp22[1])){
								    		if("P".equalsIgnoreCase(t2)){
												trendReport.setParameter(prptid, Sqlcellstyle);
												//System.out.println("prptid:"+prptid+":currrptid:"+currrptid+":flag:"+t2+":name:"+t1);
											}else if("F".equalsIgnoreCase(t2)){
												//System.out.println("currrptid:"+currrptid+":"+cellstyle.getNosigndata());
												trendReport.setField(prptid, cellstyle);
											}else if(t2!=null){
												trendReport.setVariable(prptid, "Report", t2, Sqlcellstyle);
											}
								    		if(tmp22[2]!=null && !"".equalsIgnoreCase(tmp22[2]))
								    			allparalist.add(tmp22);//（报表Id，参数"name"，类型(P，F，V)）
								    	}
								    	// 设置子报表与父报表之间的变量绑定
							    		if(!isbind(currrptid,tmp22[2],tmp22[1],prptid)){
							    			int subrptidx=getsubRptList(prptid,currrptid);
							    			//t3 "name"  t4 $p{name}
							    			String bids=t4;
							    			if(isexist(prptid,t3,"F")){
							    				bids = "$F{"+t3+"}";
							    			}
									    	trendReport.setBandValue(prptid, currrptid, getsubRptArea(prptid,currrptid,areaname)/*父报表区域*/, t4, bids/*父报表的F或P，应该先找F，没有的再找P*/,subrptidx);
									    	setParaBinded(currrptid,tmp22[2],tmp22[1],prptid);//已经绑定
							    		}
								    }
								    //将查询条件也绑定到子报表
									for (int a = 0; a < dynrpt_hpara.size(); a++) {
										String pn=dynrpt_hpara.get(a)[a][1];
										if(!isbind(currrptid,pn,"P",prptid)){
											int subrptidx=getsubRptList(prptid,currrptid);
											trendReport.setBandValue(prptid, currrptid, getsubRptArea(prptid,currrptid,areaname)/*父报表区域*/, "$P{"+pn+"}", "$P{"+pn+"}"/*父报表的F或P，应该先找F，没有的再找P*/,subrptidx);
//											trendReport.setBandValue(prptid, currrptid, getsubRptArea(prptid,currrptid,areaname), "$P{STARTRECORD}", "$P{STARTRECORD}", subrptidx);
//											trendReport.setBandValue(prptid, currrptid, getsubRptArea(prptid,currrptid,areaname), "$P{ENDRECORD}", "$P{ENDRECORD}", subrptidx);
											setParaBinded(currrptid,pn,"P",prptid);//已经绑定
										}
									}
								}
								
								if(rptrowtype.equalsIgnoreCase(CNTITLE)){ //"标题"
									trendReport.setTitle(currrptid, cellstyle);
								}else if(rptrowtype.equalsIgnoreCase(CNPGHEAD)){//"表头"
									trendReport.setColumnHeader(currrptid, cellstyle);
								}else if(rptrowtype.equalsIgnoreCase(CNPGDTL)){ //"明细"
									trendReport.setDetail(currrptid, cellstyle);
								}else if(rptrowtype.equalsIgnoreCase(CNPGSUM) 
										|| rptrowtype.equalsIgnoreCase(CNSUM) 
										|| rptrowtype.equalsIgnoreCase(CNFOOT)){ //"小计"\"总计"\"表尾"
									trendReport.setSummary(currrptid, cellstyle);
								}
							}
							/*int rowAreaheight= getRowTypeMaxHeight(sheetcur,currrptid,rptrowtype);
							trendReport.setBandHeight(currrptid,areaname,rowAreaheight);
							System.out.println("setBandHeight:"+rptrowtype+":"+"currcell:"+currcell+":rowAreaheight:"+Integer.toString(rowAreaheight));*/
						}
						//多行明细高度汇总
						if(mycurrrptId.equalsIgnoreCase(myoldrptId)&&!myoldrptId.equals(""))
						{
							int rptfixcolid=getRptFixcolIdx(currrptid);
							String myrptrowtype=getCellValue(sheetcur.getRow(m).getCell((short)rptfixcolid));//第m行第列
							if(myrptrowtype.equals(CNPGDTL)){
								SetRptAreaMaxHeight(currrptid,"detail",cellht,0,"","1");
							}
						}
						myoldrptId=mycurrrptId;
						preareaname=currareaname;
						if(rptwidth<rowwidth)rptwidth=rowwidth;//报表的最大宽度
					}
					
					//设置区域高度(报表Id，区域，高度)
					String mainrptId=getMainRptId(mianrptno);
					//保存报表宽度
					SetRptpagewidth(mainrptId,rptwidth);
					//如果没有标题的话
					if(mainsql.equals("")&&"0".equalsIgnoreCase(ishavetitle(mainrptId))){
						trendReport.setQueryString(mainrptId, CNONESELECTROW);
					}
					for(int ai=0;ai<rptAreaHeightlist.size();ai++){
						//System.out.println("rptAreaHeightlist:"+rptAreaHeightlist.get(ai)[0]+":"+rptAreaHeightlist.get(ai)[1]+":"+rptAreaHeightlist.get(ai)[2]);
						if(!"".equalsIgnoreCase(rptAreaHeightlist.get(ai)[0])&&!"".equalsIgnoreCase(rptAreaHeightlist.get(ai)[1]))
						{
							String tmparea=rptAreaHeightlist.get(ai)[1];//aream
							String tmprpt=rptAreaHeightlist.get(ai)[0];
							String tmpidx=rptAreaHeightlist.get(ai)[3];
							if(tmparea.equalsIgnoreCase("detail")){
								int rptareahgt=Integer.parseInt(rptAreaHeightlist.get(ai)[2]);
								if(!tmprpt.equals(mainrptId))
									rptareahgt=getAreaHeight2(tmprpt,tmparea,tmpidx);
								trendReport.setBandHeight(tmprpt,rptAreaHeightlist.get(ai)[1],rptareahgt,Integer.parseInt(tmpidx));
							}
							else{
								int rptght=getAreaHeight(sheetcur,tmprpt,tmparea);//String currrptid,String areaname)
								trendReport.setBandHeight(tmprpt,rptAreaHeightlist.get(ai)[1],rptght,0);
							}
						}
					}
					String printdirecttmp="";
					if("1".equals(printdirect))printdirecttmp="Landscape";
					else printdirecttmp="Portrait";
					//设置报表宽度
					//存放生成的报表文件列表，(父报表id，报表Id，列名前缀，第几个子报表｛确定前面列的区域类型｝)
					for(int ai=0;ai<rptlist.size();ai++){
						String rpt=rptlist.get(ai)[1];
						int rptbgncolidx=getRptBegincol(rpt);
						int wd=0;
						if(chkishavesub(rpt).equals("1")){//父报表
							for(int mi=cncolumn;mi<=rptbgncolidx;mi++){
								wd=wd+sheetcur.getColumnWidth(mi)/256;
							}
							rptwidth=getRptpagewidth(rpt);
							int tmpwd=rptwidth-wd;
							if(tmpwd<0)tmpwd=0;
							trendReport.setpublicProperty(rpt,String.valueOf(tmpwd),printdirecttmp,"true");//名称、页面宽度、打印方向、列宽、是否忽略分页）
						}else{
							wd=0;
							int bgcol=Integer.parseInt(rptlist.get(ai)[3]);
							int endcol=Integer.parseInt(rptlist.get(ai)[8]);
							int bgrowInd = Integer.parseInt(rptlist.get(ai)[5]);
							int celllen = sheetcur.getRow(bgrowInd).getLastCellNum();//sheet列数
							if(endcol==0)endcol=celllen;
							for(int mi=bgcol;mi<endcol;mi++){
								wd=wd+sheetcur.getColumnWidth(mi)/256;
							}
							trendReport.setpublicProperty(rpt,String.valueOf(wd),printdirecttmp,"true");//名称、页面宽度、打印方向、列宽、是否忽略分页）
						}
					}
					
					String cmprtn = trendReport.compileJrxml();
					if(!cmprtn.equalsIgnoreCase("")){
						rtnMsg = "报表文件编译出错";
						break;
					}
					
					//excel多sheet列表
					SetMainSubRptList(getMainRptId(mianrptno),sheetname);
					if(ishavemainrpt.equals("1"))mianrptno=mianrptno+1;
					/*for(int a=0;a<allparalist.size();a++)
					{System.out.println(allparalist.get(a)[0]+":"+allparalist.get(a)[1]+":"+allparalist.get(a)[2]);};*/
				}
			}
			
		} catch (InvalidFormatException e) {
			rtnMsg = e.getMessage();
		}
        return rtnMsg;
    }
    
  //读取rpt模版内容1.1,支持：模版一(1.1版本)
    public String readmdlfile2(String filename)throws Exception  
    {
    	String rtnMsg="";
	    Workbook workbook = null;
	    dynbodys="";
		  try {
			workbook = WorkbookFactory.create(new FileInputStream(filename));
			
			int currrptcnt=0;
			//读取report模版
			int sheetcnt = workbook.getNumberOfSheets();//获取sheet数量
			mianrptno=0;
			rptlist = new ArrayList<String[]>();
			rptpagewidthlist = new ArrayList<String[]>();
			TrendReport trendReport = new TrendReport(reportid);
			for(int i=0;i<sheetcnt;i++){
				Sheet sheetcur = workbook.getSheetAt(i);
				String sheetname = sheetcur.getSheetName();
				if(sheetname.indexOf("${") == -1){
					rptAreaHeightlist = new ArrayList<String[]>();
					allparalist = new ArrayList<String[]>();
					rptRegionList = new ArrayList<String[]>();
					rptAreaIdxlist = new ArrayList<String[]>();
					//子报表数
					int subrptcount=0;
					//取sheet里的合并单元格数
					int rptrgcnt = sheetcur.getNumMergedRegions();
					for(int rg=0;rg<rptrgcnt;rg++){
						String ss=sheetcur.getMergedRegion(rg).toString();
						String[] temp = new String[3];
						temp[0]=Integer.toString(i);
						temp[1]=Integer.toString(rg);
						temp[2]="";
						rptRegionList.add(temp);
					}
					
					cncolumn = Integer.parseInt(getCellValue(sheetcur.getRow(0).getCell((short)0),0));//固定列数
					cfixr1column=0;
					cfixr2column=0;
					int rows = sheetcur.getLastRowNum() - sheetcur.getFirstRowNum();//sheet行数
					String currrptid="";
					String currcell="";
					String currcellpre="",lastcellpre="";//字段前缀
					
					//判断空列、隐藏列数量
					Row row0 = sheetcur.getRow(0);
					
					int nullcolLen=0;
					String ishavenull="";
					if(row0 != null){
						int celllen = row0.getLastCellNum();//sheet列数
						for(int n = cncolumn; n<celllen; n++){
							Cell cell = row0.getCell((short)n);
							currcell = getCellValue(cell);    //单元值
							if(currcell.equalsIgnoreCase(CNNULLHIDE))//"空列,隐藏"
							{
								nullcolLen++;
								ishavenull="1";
							}
						}
					}
					String fixPrename="";
					//获取空列的范围
					if(!"".equalsIgnoreCase(ishavenull)){
						for (int m = 1; m <= rows; m++) {
							Row row = sheetcur.getRow(m);
							if(row == null) break;
							Cell cell = row.getCell((short)cncolumn);
							String fixcell = getCellValue(cell);    //单元值
							if(!"".equalsIgnoreCase(fixcell)){
								cfixr1column=m;
								cfixr2column=combinemaxRowIdx(sheetcur,m,cncolumn);
								fixPrename=getPreNameByCell(fixcell);
								fixsql=getSqlByname(fixPrename);
								break;
							}
						}
					}
					String mainrpt="";
					String fixrpt="";
					int rptwidth=0;
					int rowhgttop=0;//行高
					int rowhgtbottom=0;
					int hearcnt=0;
					
					String mainsql="";
					String ishavemainrpt="";
					//多行高度汇总
					String mycurrrptId="";
					String myoldrptId="";
					
					for (int m = 1; m <= rows; m++) {
						Row row = sheetcur.getRow(m);
						if(row == null) break;
						int celllen = row.getLastCellNum();//sheet列数
						int rowwidth=0;//行宽
						int rowheight=0;
						String cellType = "";
						CellAllStyle cellstyle = new CellAllStyle();
						rowhgtbottom=0;
						rowheight = (int)row.getHeightInPoints();
						int cellwd=0,cellht=0;
						lastcellpre="";
						subrptcount=0;
						String currareaname="";
						String preareaname="";
						
						if(!"".equalsIgnoreCase(ishavenull)&&!"VER 1.0".equalsIgnoreCase(dynrptver)){
							for(int n = cncolumn+1; n<celllen; n++){
								Cell cell = row.getCell((short)n);
								
								if(cell == null)continue;
								CellStyle eStyle = cell.getCellStyle();
								currcell = getCellValue(cell);    //单元值
								String iscmflag=iscombine(sheetcur,i,m, n);//是否合并的单元格
								currcellpre = getPreNameByCell(currcell);
								setimgcolparamIdx(currcell,n,i-1);
								//获取type
								String nopreCellvalue = getNoPreCellValue(currcell);//$F{name}
								
								String getNoPreNoCellValue=getNoPreNoCellValue(currcell);
								String type=getSqlColDataType(currcellpre,getNoPreNoCellValue);
								if(!type.equalsIgnoreCase(""))
									cellType = type;
								else
									cellType = getCellTyle(eStyle);
								
								String preCellValue=getPreCellValueByCellOfPrecell(sheetcur, m, n);;
								if(lastcellpre.equals(""))//取上一列CellValue
								{
								    lastcellpre=getPreNameByCell(preCellValue);//$p{t1.name}-->t1
							    }
								
								if(cfixr2column<m)
									currrptid=mainrpt;
								
								int rptfixcloid=getRptFixcolIdx(currrptid);//取报表的区域的固定列
								String rptrowtype=getCellValue(sheetcur.getRow(m).getCell((short)rptfixcloid));//第m行第0列
								String rptcoltype=getCellValue(sheetcur.getRow(0).getCell((short)n),0);//第0行第n列
								
								String areaname="";
								if(rptrowtype.equalsIgnoreCase(CNTITLE)){ //"标题"
									areaname = "title";
								}else if(rptrowtype.equalsIgnoreCase(CNPGHEAD)){//"表头"
									areaname = "columnHeader";
									dynbodys=dynbodys+getCellValueNormal(cell)+","+Integer.toString(i-1)+"@@";
								}else if(rptrowtype.equalsIgnoreCase(CNPGDTL)){ //"明细"
									areaname = "detail";
								}else if(rptrowtype.equalsIgnoreCase(CNPGSUM)){ //"小计"
									areaname = "summary";
								}else if(rptrowtype.equalsIgnoreCase(CNSUM)){   //"总计"
									areaname = "summary";
								}else if(rptrowtype.equalsIgnoreCase(CNFOOT)){  //"表尾"
									areaname = "summary";
								}
								
								if(iscmflag.equals("1")){
									int[] tmp=getcmbcellWH(sheetcur,i,m, n);
									cellwd=tmp[1];
									continue;
								}else if(iscmflag.equals("2")){
									int[] tmp=getcmbcellWH(sheetcur,i,m, n);
									cellht=tmp[0];
									cellwd=tmp[1];
								}else{
									cellht=rowheight;
									cellwd=sheetcur.getColumnWidth(n)/256;
								}
								
								int colleft = getCellLeft(sheetcur, currrptid, m,n);
								rowhgttop=getCellTop(sheetcur,currrptid,rptrowtype,m,n);
								cellstyle.setRowLeft(new String().valueOf(colleft));
								cellstyle.setRowTop(new String().valueOf(rowhgttop));
								cellstyle.setRowWidth(new String().valueOf(cellwd));
								cellstyle.setRowHeight(new String().valueOf(cellht));
								cellstyle.setCellType(cellType);
								
								if(!eStyle.getDataFormatString().equalsIgnoreCase("General"))
								{
									int tmpi=eStyle.getDataFormatString().indexOf("_)");
									if(tmpi == -1)
										cellstyle.setDateformat(eStyle.getDataFormatString());
									else
										cellstyle.setDateformat(eStyle.getDataFormatString().substring(0,tmpi ));
								}
								rowhgtbottom=rowhgtbottom+cellht;
								rowwidth=rowwidth+cellwd;//列宽度
								
								Font eFont = workbook.getFontAt(eStyle.getFontIndex());
								cellstyle.setFontsize(Integer.toString(eFont.getFontHeight()));
								cellstyle.setFontstyle(eFont.getFontName());
								cellstyle.setBoldlight(Integer.toString(eFont.getBoldweight()));
								cellstyle.setHorizonta(Integer.toString(eStyle.getAlignment()));
								cellstyle.setVertical(Integer.toString(eStyle.getVerticalAlignment()));
								
								if(!"".equalsIgnoreCase(ishavenull)){//有隐藏列
									if(mainrpt.equals("")){//创建主报表
										currrptid = SetRptList(currrptid,currcell,n,subrptcount,iscmflag,m,n,sheetcur,areaname);
										trendReport.CreateJrxml(currrptid);
										cellstyle.setAlldata(reportname);
										mycurrrptId=currrptid;
										//设置查询条件参数,每一个报表都应定义
										setallparalist(currrptid,"");
										for (int a = 0; a < dynrpt_hpara.size(); a++) {
											CellAllStyle schstl = new CellAllStyle();
											schstl.setNosigndata(dynrpt_hpara.get(a)[a][1]);
											
											String schtmp=getSqlColDataType(currcellpre,dynrpt_hpara.get(a)[a][1]);
											schstl.setCellType(schtmp);
											
											trendReport.setParameter(currrptid, schstl);
										}
										mainrpt=currrptid;
										ishavemainrpt="1";
										
										//确定隐藏列的是作为最外层主报表还是内层子报表
										if(cfixr2column>=rows)//作为主报表
										{
											mainsql=fixsql;
											trendReport.setQueryString(currrptid, fixsql);
										}
										
									}else{
										if(cfixr1column==m && n==cncolumn+1)
										{
											int subrptidx=0;
											String sdrptrowtype=getCellValue(sheetcur.getRow(m).getCell((short)1));//第m行第列
											//如果不是xls里多报表的循环
											if(!sdrptrowtype.equalsIgnoreCase(CNTITLE)){ //"标题"
												String subfixrpt = SetRptList(currrptid,currcell,n,subrptcount,iscmflag,m,n,sheetcur,areaname);
												trendReport.CreateJrxml(subfixrpt);
												cellstyle.setAlldata(reportname);
												SetsubRptList(currrptid,subfixrpt,areaname);
												subrptidx=getsubRptList(currrptid,subfixrpt);
												if(subrptidx!=0)trendReport.addDetailBand(currrptid, subfixrpt, cellstyle);
												trendReport.setSubReport(mainrpt,currrptid, areaname, subfixrpt, cellstyle,subrptidx);
												trendReport.setQueryString(subfixrpt, fixsql);
												//增加父报表传入子报表的参数
												for (int a = 0; a < dynrpt_hpara.size(); a++) {
													String pn=dynrpt_hpara.get(a)[a][1];
													if(!isbind(subfixrpt,pn,"P",currrptid)){
														trendReport.setBandValue(currrptid, subfixrpt, getsubRptArea(currrptid,subfixrpt,areaname)/*父报表区域*/, "$P{"+pn+"}", "$P{"+pn+"}"/*父报表的F或P，应该先找F，没有的再找P*/,subrptidx);
														setParaBinded(currrptid,pn,"P",currrptid);//已经绑定
													}
												}
												
												//取报表的所有查询字段(字段、类型)
												List<String[]> RptFieldList = getRptFieldList(mainrpt+fixPrename);
												if(RptFieldList!=null){
													for(int rpfl=0;rpfl<RptFieldList.size();rpfl++){
														String[] rpfltmp= new String[3];
														rpfltmp[0]=subfixrpt;
														rpfltmp[1]="F";
														rpfltmp[2]=RptFieldList.get(rpfl)[0];
														CellAllStyle rpflstl = new CellAllStyle();
														rpflstl.setAlldata("$F{"+rpfltmp[2]+"}");
														rpflstl.setSigndata("$F{"+rpfltmp[2]+"}");
														rpflstl.setNosigndata(rpfltmp[2]);
														rpflstl.setCellType(RptFieldList.get(rpfl)[1]);
														trendReport.setField(subfixrpt, rpflstl);
														//System.out.println(rpfltmp[2]+":"+RptFieldList.get(rpfl)[1]);
														allparalist.add(rpfltmp);//（报表Id，类型(P，F，V),参数"name"）
													}
												}
												trendReport.setBandValue(currrptid, subfixrpt, getsubRptArea(currrptid,subfixrpt,areaname)/*父报表区域*/, "$P{STARTRECORD}", "$P{STARTRECORD}"/*父报表的F或P，应该先找F，没有的再找P*/,subrptidx);
												trendReport.setBandValue(currrptid, subfixrpt, getsubRptArea(currrptid,subfixrpt,areaname)/*父报表区域*/, "$P{ENDRECORD}", "$P{ENDRECORD}"/*父报表的F或P，应该先找F，没有的再找P*/,subrptidx);
												currrptid=subfixrpt;
											}else{//如果是xls里多报表的循环，则主报表里应该增加以上设置
												//取报表的所有查询字段(字段、类型)
												List<String[]> RptFieldList = getRptFieldList(mainrpt+fixPrename);
												if(RptFieldList!=null){
													for(int rpfl=0;rpfl<RptFieldList.size();rpfl++){
														String[] rpfltmp= new String[3];
														rpfltmp[0]=mainrpt;
														rpfltmp[1]="F";
														rpfltmp[2]=RptFieldList.get(rpfl)[0];
														CellAllStyle rpflstl = new CellAllStyle();
														rpflstl.setAlldata("$F{"+rpfltmp[2]+"}");
														rpflstl.setSigndata("$F{"+rpfltmp[2]+"}");
														rpflstl.setNosigndata(rpfltmp[2]);
														rpflstl.setCellType(RptFieldList.get(rpfl)[1]);
														trendReport.setField(mainrpt, rpflstl);
														//System.out.println(rpfltmp[2]+":"+RptFieldList.get(rpfl)[1]);
														allparalist.add(rpfltmp);//（报表Id，类型(P，F，V),参数"name"）
													}
												}
												trendReport.setBandValue(mainrpt, currrptid, getsubRptArea(currrptid,currrptid,areaname)/*父报表区域*/, "$P{STARTRECORD}", "$P{STARTRECORD}"/*父报表的F或P，应该先找F，没有的再找P*/,subrptidx);
												trendReport.setBandValue(mainrpt, currrptid, getsubRptArea(currrptid,currrptid,areaname)/*父报表区域*/, "$P{ENDRECORD}", "$P{ENDRECORD}"/*父报表的F或P，应该先找F，没有的再找P*/,subrptidx);
											}
											subrptcount++;
											fixrpt = SetRptList(currrptid,currcell,n,subrptcount,iscmflag,m,n,sheetcur,areaname);
											trendReport.CreateJrxml(fixrpt);
											SetsubRptList(currrptid,fixrpt,areaname);
											trendReport.addDetailBand(currrptid, fixrpt, cellstyle);
											trendReport.setSubReport(mainrpt,currrptid, areaname, fixrpt, cellstyle,subrptidx);
											String prename=getPreNameByCell(currcell);
											String excelsql=getSqlByname(prename);
											trendReport.setQueryString(fixrpt, excelsql);
											trendReport.setBandValue(currrptid, fixrpt, getsubRptArea(currrptid,fixrpt,areaname)/*父报表区域*/, "$P{STARTRECORD}", "$P{STARTRECORD}"/*父报表的F或P，应该先找F，没有的再找P*/,subrptidx);
											trendReport.setBandValue(currrptid, fixrpt, getsubRptArea(currrptid,fixrpt,areaname)/*父报表区域*/, "$P{ENDRECORD}", "$P{ENDRECORD}"/*父报表的F或P，应该先找F，没有的再找P*/,subrptidx);
											
											currrptid=fixrpt;
											subrptcount++;
										}
									}
									
									//绑定字段
									if(cfixr1column<=m&&cfixr2column>=m)
									{
										currrptid=fixrpt;
										if(cfixr1column==m && n==cncolumn+1)
										{
											rptfixcloid=getRptFixcolIdx(currrptid);
											rptrowtype=getCellValue(sheetcur.getRow(m).getCell((short)rptfixcloid));//第m行第列
											if(rptrowtype.equalsIgnoreCase(CNTITLE)){ //"标题"
												areaname = "title";
											}else if(rptrowtype.equalsIgnoreCase(CNPGHEAD)){//"表头"
												areaname = "columnHeader";
											}else if(rptrowtype.equalsIgnoreCase(CNPGDTL)){ //"明细"
												areaname = "detail";
											}else if(rptrowtype.equalsIgnoreCase(CNPGSUM)){ //"小计"
												areaname = "summary";
											}else if(rptrowtype.equalsIgnoreCase(CNSUM)){   //"总计"
												areaname = "summary";
											}else if(rptrowtype.equalsIgnoreCase(CNFOOT)){  //"表尾"
												areaname = "summary";
											}
										}
									}
									else{
										currrptid=mainrpt;
									}
									
									//新建单元格里的参数 数据$p{name}数据,"P","name"，$p{name}
									String[] tmp = new String[4];
									tmp=getParamByCellValue(currcell);
									cellstyle.setAlldata(tmp[0]);
									cellstyle.setSigndata(tmp[3]);
									cellstyle.setNosigndata(tmp[2]);
									//把新建的参数放入参数列表
									String tmp2[] = {currrptid,tmp[1],tmp[2],""};//currrptid,"P","name"
									if(!isexist(currrptid,tmp[2],tmp[1])){
										if("P".equalsIgnoreCase(tmp[1])){
											trendReport.setParameter(currrptid,cellstyle);
											//System.out.println("currrptid:"+currrptid+":flag:"+tmp[1]+":name:"+tmp[0]);
										}else if("F".equalsIgnoreCase(tmp[1])){
											//System.out.println("currrptid:"+currrptid+":"+cellstyle.getNosigndata());
											//判断当前报表是否已经设置过sql
											String rptsql=getrptsql(currrptid);
											if("".equals(rptsql)||rptsql.equalsIgnoreCase(CNONESELECTROW))
											{
												String sql = "";
												String excelsql=getSqlByname(currcellpre);
												Iterator it = pagesqlmap.keySet().iterator();
												Boolean sqlflag = false;
												while(it.hasNext()){
													String key = (String)it.next();
													if(key!=""&&key.equalsIgnoreCase(currcellpre)){
														sqlflag = true;
														String sqltmp = "SELECT a.*,ROWNUM as rn FROM ("+ excelsql +") a WHERE  ROWNUM <= $P{ENDRECORD}";
														sql = "SELECT * FROM ("+ sqltmp +") WHERE rn >= $P{STARTRECORD} ";
														String prptid=getParentByRptId(currrptid);
														int subrptidx=getsubRptList(prptid,currrptid);
														trendReport.setBandValue(prptid, currrptid, getsubRptArea(prptid,currrptid,areaname), "$P{STARTRECORD}", "$P{STARTRECORD}", subrptidx);
														trendReport.setBandValue(prptid, currrptid, getsubRptArea(prptid,currrptid,areaname), "$P{ENDRECORD}", "$P{ENDRECORD}", subrptidx);
													}
												}
												if(!sqlflag){
													sql = excelsql;
												}
												if(currrptid.equalsIgnoreCase(mainrpt))
													mainsql=sql;
												trendReport.setQueryString(currrptid, sql);
												setRptListTname(currrptid,currcellpre);
											}
											trendReport.setField(currrptid, cellstyle);
										}else if(tmp[1]!=null && !"".equalsIgnoreCase(tmp[1])){// $p{name},"P","name"
											trendReport.setVariable(currrptid, "Report", tmp[1], cellstyle);
										}
										if(tmp[1]!=null && !tmp[1].equals(""))
											allparalist.add(tmp2);//（报表Id，类型(P，F，V),参数"name"）
									}
									String prptid=getParentByRptId(currrptid);
									//将sql语句的参数也加入参数列表 $p{t.name} --> $p{name},"P","name"
									if(tmp[1]!=null && !"".equalsIgnoreCase(tmp[1]) && tmp[2]!=null && !"".equalsIgnoreCase(tmp[2])){
									    List<String[]> list = new ArrayList<String[]>();
									    list = getParamListByCellValue(currcell);
									    CellAllStyle Sqlcellstyle = new CellAllStyle();
									    for(int a=0;a<list.size();a++){
									    	
									    	//$p{name}数据,"P","name"，$p{name}
									    	String t1=list.get(a)[0];//$p{name}数据
									    	String t2=list.get(a)[1];//"P"
									    	String t3=list.get(a)[2];//"name"
									    	String t4=list.get(a)[3];//$p{name}
									    	
									    	String tmp21[] = {currrptid,t2,t3,""};//currrptid,"P","name"
									    	Sqlcellstyle.setAlldata(t1);
									    	Sqlcellstyle.setSigndata(t4);
									    	Sqlcellstyle.setNosigndata(t3);
									    	
									    	String schtmp=getSqlColDataType(currcellpre,t3);
									    	Sqlcellstyle.setCellType(schtmp);
											
									    	if(!isexist(currrptid,tmp21[2],tmp21[1])){
									    		if("P".equalsIgnoreCase(t2)){
													trendReport.setParameter(currrptid, Sqlcellstyle);
													//System.out.println("currrptid:"+currrptid+":flag:"+t2+":name:"+t1);
												}else if("F".equalsIgnoreCase(t2)){
													String prename=getPreNameByCell(currcell);
													String fsql=getSqlByname(prename);
													if(!"".equalsIgnoreCase(fsql))
													  trendReport.setQueryString(currrptid, fsql);
													trendReport.setField(currrptid, cellstyle);
												}else if(t2!=null){
													trendReport.setVariable(currrptid, "Report", t2, Sqlcellstyle);
												}
									    		if(tmp2[2]!=null && !"".equalsIgnoreCase(tmp21[2]))
									    			allparalist.add(tmp21);//（报表Id，参数"name"，类型(P，F，V)）
									    	}
											
									    	//父报表没有这个参数也增加变量
									    	if(prptid.equalsIgnoreCase(""))prptid=mainrpt;
									    	String tmp22[] = {prptid,t2,t3,""};//currrptid,"P","name"
									    	
									    	if(!isexist(prptid,tmp22[2],tmp22[1])){
									    		if("P".equalsIgnoreCase(t2)){
													trendReport.setParameter(prptid, Sqlcellstyle);
												}else if("F".equalsIgnoreCase(t2)){
													trendReport.setField(prptid, cellstyle);
												}else if(t2!=null){
													trendReport.setVariable(prptid, "Report", t2, Sqlcellstyle);
												}
									    		if(tmp22[2]!=null && !"".equalsIgnoreCase(tmp22[2]))
									    			allparalist.add(tmp22);//（报表Id，参数"name"，类型(P，F，V)）
									    	}
									    	// 设置子报表与父报表之间的变量绑定
								    		if(!isbind(currrptid,tmp22[2],tmp22[1],prptid)){
								    			int subrptidx=getsubRptList(prptid,currrptid);
								    			//t3 "name"  t4 $p{name}
								    			String bids=t4;
								    			if(isexist(prptid,t3,"F")){
								    				bids = "$F{"+t3+"}";
								    			}
										    	trendReport.setBandValue(prptid, currrptid, getsubRptArea(prptid,currrptid,areaname)/*父报表区域*/, t4, bids/*父报表的F或P，应该先找F，没有的再找P*/,subrptidx);
										    	setParaBinded(currrptid,tmp22[2],tmp22[1],prptid);//已经绑定
								    		}
									    }
									    //将查询条件也绑定到子报表
										for (int a = 0; a < dynrpt_hpara.size(); a++) {
											String pn=dynrpt_hpara.get(a)[a][1];
											if(!isbind(currrptid,pn,"P",prptid)){
												int subrptidx=getsubRptList(prptid,currrptid);
												trendReport.setBandValue(prptid, currrptid, getsubRptArea(prptid,currrptid,areaname)/*父报表区域*/, "$P{"+pn+"}", "$P{"+pn+"}"/*父报表的F或P，应该先找F，没有的再找P*/,subrptidx);
												setParaBinded(currrptid,pn,"P",prptid);//已经绑定
											}
										}
									}
									
									int subrptidx=getsubRptList(prptid,currrptid);
									SetRptAreaMaxHeight(currrptid,areaname,cellht,subrptidx,"","");
									
									if(rptrowtype.equalsIgnoreCase(CNTITLE)){ //"标题"
										trendReport.setTitle(currrptid, cellstyle);
									}else if(rptrowtype.equalsIgnoreCase(CNPGHEAD)){//"表头"
										trendReport.setColumnHeader(currrptid, cellstyle);
									}else if(rptrowtype.equalsIgnoreCase(CNPGDTL)){ //"明细"
										trendReport.setDetail(currrptid, cellstyle);
									}else if(rptrowtype.equalsIgnoreCase(CNPGSUM) 
											|| rptrowtype.equalsIgnoreCase(CNSUM) 
											|| rptrowtype.equalsIgnoreCase(CNFOOT)){ //"小计"\"总计"\"表尾"
										trendReport.setSummary(currrptid, cellstyle);
									}
								}
								//保存sheet中包含的语句名
								SaveSheetsTName(currrptid,currcellpre,i-1);
								if(!currcellpre.equalsIgnoreCase(""))
									lastcellpre = currcellpre;
								}
						    }else if(!"VER 1.0".equalsIgnoreCase(dynrptver)){//1.1版本的支持多明细，多小计的
						    	
							}else if ("VER 1.0".equalsIgnoreCase(dynrptver)){//没有隐藏列,和老版本一样的代码
								for(int n = cncolumn; n<celllen; n++){
									Cell cell = row.getCell((short)n);
									
									if(cell == null)continue;
									CellStyle eStyle = cell.getCellStyle();
									currcell = getCellValue(cell);    //单元值
									String iscmflag=iscombine(sheetcur,i,m, n);//是否合并的单元格
									currcellpre = getPreNameByCell(currcell);
									setimgcolparamIdx(currcell,n,i-1);
									//获取type
									String nopreCellvalue = getNoPreCellValue(currcell);//$F{name}
									
									String getNoPreNoCellValue=getNoPreNoCellValue(currcell);
									String type=getSqlColDataType(currcellpre,getNoPreNoCellValue);
									if(!type.equalsIgnoreCase(""))
										cellType = type;
									else
										cellType = getCellTyle(eStyle);
									
									//System.out.println(currcell);
									String preCellValue=getPreCellValueByCellOfPrecell(sheetcur, m, n);;
									if(lastcellpre.equals("")&&n==cncolumn+1)//取上一列CellValue
									{
									    lastcellpre=getPreNameByCell(preCellValue);//$p{t1.name}-->t1
								    }
									
									int rptfixcloid=getRptFixcolIdx(currrptid);//取报表的区域的固定列
									String rptrowtype=getCellValue(sheetcur.getRow(m).getCell((short)rptfixcloid));//第m行第0列
									String rptcoltype=getCellValue(sheetcur.getRow(0).getCell((short)n),0);//第0行第n列
									
									String areaname="";
									if(rptrowtype.equalsIgnoreCase(CNTITLE)){ //"标题"
										areaname = "title";
									}else if(rptrowtype.equalsIgnoreCase(CNPGHEAD)){//"表头"
										areaname = "columnHeader";
									}else if(rptrowtype.equalsIgnoreCase(CNPGDTL)){ //"明细"
										areaname = "detail";
									}else if(rptrowtype.equalsIgnoreCase(CNPGSUM)){ //"小计"
										areaname = "summary";
									}else if(rptrowtype.equalsIgnoreCase(CNSUM)){   //"总计"
										areaname = "summary";
									}else if(rptrowtype.equalsIgnoreCase(CNFOOT)){  //"表尾"
										areaname = "summary";
									}
									
									if(iscmflag.equals("1")){
										int[] tmp=getcmbcellWH(sheetcur,i,m, n);
										cellwd=tmp[1];
										continue;
									}else if(iscmflag.equals("2")){
										int[] tmp=getcmbcellWH(sheetcur,i,m, n);
										cellht=tmp[0];
										cellwd=tmp[1];
									}else{
										cellht=rowheight;
										cellwd=sheetcur.getColumnWidth(n)/256;
									}
									
									if((n == cncolumn && ishavenull.equals("")) || (n == cncolumn+1 && ishavenull.equals("1")))
									{
									    if(!areaname.equalsIgnoreCase("columnHeader"))
										SetRptAreaMaxHeight(currrptid,areaname,cellht,0,"","");
									}
									if(rptcoltype.equalsIgnoreCase(CNNULLHIDE)&&currcell.equalsIgnoreCase("")){//"空列,隐藏"
										continue;
									}
									
									if(((n == cncolumn && ishavenull.equals("")) || (n == cncolumn+1 && ishavenull.equals("1")))&&rptrowtype.equalsIgnoreCase(CNPGHEAD)){//多表头
										hearcnt=hearcnt+1;
										currareaname=areaname;
								    }
									
									int colleft = getCellLeft(sheetcur, currrptid, m,n);
									rowhgttop=getCellTop(sheetcur,currrptid,rptrowtype,m,n);
									//System.out.println("areaname:"+areaname+"currcell:"+currcell+",rptrowtype:"+rptrowtype+",m:"+m+",n:"+n+",colleft:"+colleft+",rowhgttop:"+rowhgttop+",cellwd:"+cellwd+",cellht:"+cellht);
									cellstyle.setRowLeft(new String().valueOf(colleft));
									cellstyle.setRowTop(new String().valueOf(rowhgttop));
									cellstyle.setRowWidth(new String().valueOf(cellwd));
									cellstyle.setRowHeight(new String().valueOf(cellht));
									
									cellstyle.setCellType(cellType);
									
									if(!eStyle.getDataFormatString().equalsIgnoreCase("General"))
									{
										int tmpi=eStyle.getDataFormatString().indexOf("_)");
										if(tmpi == -1)
											cellstyle.setDateformat(eStyle.getDataFormatString());
										else
											cellstyle.setDateformat(eStyle.getDataFormatString().substring(0,tmpi ));
									}
									rowhgtbottom=rowhgtbottom+cellht;
									rowwidth=rowwidth+cellwd;//列宽度
									
									Font eFont = workbook.getFontAt(eStyle.getFontIndex());
									cellstyle.setFontsize(Integer.toString(eFont.getFontHeight()));
									cellstyle.setFontstyle(eFont.getFontName());
									cellstyle.setBoldlight(Integer.toString(eFont.getBoldweight()));
									cellstyle.setHorizonta(Integer.toString(eStyle.getAlignment()));
									cellstyle.setVertical(Integer.toString(eStyle.getVerticalAlignment()));
									
									if(mainrpt.equals("")){//创建主报表
										currrptid = SetRptList(currrptid,currcell,n,subrptcount,iscmflag,m,n,sheetcur,areaname);
										trendReport.CreateJrxml(currrptid);
										cellstyle.setAlldata(reportname);
										mycurrrptId=currrptid;
										//设置查询条件参数,每一个报表都应定义
										setallparalist(currrptid,"");
										for (int a = 0; a < dynrpt_hpara.size(); a++) {
											CellAllStyle schstl = new CellAllStyle();
											schstl.setNosigndata(dynrpt_hpara.get(a)[a][1]);
											
											String schtmp=getSqlColDataType(currcellpre,dynrpt_hpara.get(a)[a][1]);
											schstl.setCellType(schtmp);
											
											trendReport.setParameter(currrptid, schstl);
										}
										mainrpt=currrptid;
										ishavemainrpt="1";
									}else{
										//应该判断字段的前缀，不一样的才是子报表
										if((!lastcellpre.equalsIgnoreCase("") && !currcellpre.equalsIgnoreCase(lastcellpre) && !currcell.equals("") && !preCellValue.equals(""))
										  ||(/*多表头*/currareaname.equalsIgnoreCase("columnHeader")&&!currareaname.equalsIgnoreCase(preareaname))&&(n == cncolumn && ishavenull.equals("")) || (n == cncolumn+1 && ishavenull.equals("1"))){
											//System.out.println(currcell+":"+preCellValue);
											SetRptEndCol(currrptid,m,n);//设置结束列
											if(!"".equalsIgnoreCase(getRptIdByCellPreName(currcellpre))&&!currcellpre.equals("")){
												//比如从T1.name列到t2.name在到t1.Id时
												currrptid=getRptIdByCellPreName(currcellpre);
												mycurrrptId=currrptid;
												int collefttmp = getCellLeft(sheetcur, currrptid, m,n);
												cellstyle.setRowLeft(new String().valueOf(collefttmp));
											}else{
											//判断当前和上一格是否相同，不同说明子报表，当是小计等就直接去父报表包括的下一列的第一个子报表，否则新建一个子报表
											//当前报表的行标志
											rptfixcloid=getRptFixcolIdx(currrptid);
											rptrowtype=getCellValue(sheetcur.getRow(m).getCell((short)rptfixcloid));//第m行第列
											if(!rptrowtype.equalsIgnoreCase(CNPGSUM)&&!rptrowtype.equalsIgnoreCase(CNSUM)){ //不等于"小计"\"总计"
												subrptcount++;
												String parrptid="";
												if(lastcellpre.equals(""))
													parrptid=mainrpt;
												else
													parrptid=getRptIdByCellPreName(lastcellpre);//取父报表id
												
												String rptsubid = "";
												String oldarea=areaname;//多表头
												if(hearcnt>1)//多表头
												{
													areaname="detail";
													
												}
												rptsubid=SetRptList(parrptid,currcell,n,subrptcount,iscmflag,m,n,sheetcur,areaname);
												trendReport.CreateJrxml(rptsubid);//创建子报表
												
												//保存生成的子报表在父报表的区域里的idx(父报表Id，报表Id，区域，idx)
												if(areaname.equalsIgnoreCase("detail"))
													SetsubRptList(parrptid,rptsubid,areaname);
												
												rowhgttop=getCellTop(sheetcur,rptsubid,rptrowtype,m,n);
												cellstyle.setRowLeft(new String().valueOf(0));
												
												cellstyle.setRowTop(new String().valueOf(rowhgttop));
												
												SetRptAreaMaxHeight(rptsubid,oldarea,cellht,0,"","");//设置子报表区域高读
												
												//System.out.println(rptrowtype+":"+"currcell:"+currcell+",m:"+m+",n:"+n+",colleft:"+colleft+",rowhgttop:"+rowhgttop+",cellwd:"+cellwd+",cellht:"+cellht);
												
												if(!currcellpre.equalsIgnoreCase("")){
													String sql = "";
													String excelsql=getSqlByname(currcellpre);
													Iterator it=pagesqlmap.keySet().iterator();
													Boolean sqlflag = false;
													while(it.hasNext()){
														String key=(String)it.next();
														if(key!=""&&key.equalsIgnoreCase(currcellpre)){
															sqlflag = true;
															String sqltmp = "SELECT a.*,ROWNUM as rn FROM ("+ excelsql +") a WHERE  ROWNUM <= $P{ENDRECORD}";
															sql = "SELECT * FROM ("+ sqltmp +") WHERE rn >= $P{STARTRECORD} ";
															String prptid=getParentByRptId(rptsubid);
															int subrptidx=getsubRptList(prptid,rptsubid);
															trendReport.setBandValue(prptid, rptsubid, getsubRptArea(prptid,rptsubid,areaname), "$P{STARTRECORD}", "$P{STARTRECORD}", subrptidx);
															trendReport.setBandValue(prptid, rptsubid, getsubRptArea(prptid,rptsubid,areaname), "$P{ENDRECORD}", "$P{ENDRECORD}", subrptidx);
														}
													}
													if(!sqlflag){
														sql = excelsql;
													}
													trendReport.setQueryString(rptsubid, sql);
//													trendReport.setQueryString(rptsubid, getSqlByname(currcellpre));
													//取报表的所有查询字段(字段、类型)
													List<String[]> RptFieldList = getRptFieldList(mainrpt+currcellpre);
													if(RptFieldList!=null){
														for(int rpfl=0;rpfl<RptFieldList.size();rpfl++){
															String[] rpfltmp= new String[3];
															rpfltmp[0]=rptsubid;
															rpfltmp[1]="F";
															rpfltmp[2]=RptFieldList.get(rpfl)[0];
															CellAllStyle rpflstl = new CellAllStyle();
															rpflstl.setAlldata("$F{"+rpfltmp[2]+"}");
															rpflstl.setSigndata("$F{"+rpfltmp[2]+"}");
															rpflstl.setNosigndata(rpfltmp[2]);
															
															String schtmp=getSqlColDataType(currcellpre,rpfltmp[2]);
															rpflstl.setCellType(schtmp);
															
															trendReport.setField(rptsubid, rpflstl);
															allparalist.add(rpfltmp);//（报表Id，类型(P，F，V),参数"name"）
														}
													}
												}
												int subrptidx=getsubRptList(parrptid,rptsubid);
												if(subrptidx!=0)trendReport.addDetailBand(parrptid, rptsubid, cellstyle);
												
												int tmpcurrhgt=cellht;
												//判断单元格是否被合并，包含的行数最大值
												int cbmaxRowIdx=combinemaxRowIdx(sheetcur, m, n-1);
												//System.out.println("currcell:"+currcell+":"+Integer.toString(cbmaxRowIdx)+":"+Integer.toString(tmpcurrhgt));
												for(int h=m+1;h<=cbmaxRowIdx;h++)
												{
													Row tmprow = sheetcur.getRow(h);
													Cell tmpcell=tmprow.getCell((short)n);
													String iscmflagtmp=iscombine(sheetcur,i,h, n);//是否合并的单元格
													int tmpficolid=getRptFixcolIdx(rptsubid);
													String tmprptrowtype=getCellValue(sheetcur.getRow(h).getCell((short)tmpficolid));//第m行第列
													//是合并列或不是小计列不算同一个报表
													if(!iscmflagtmp.equals("0")||!tmprptrowtype.equalsIgnoreCase(CNPGSUM))continue;
													String tmpnextrowpre = getPreNameByCell(getCellValue(tmpcell));
													if(tmpnextrowpre.equalsIgnoreCase(currcellpre)
															||(tmpnextrowpre.equals("")&&!currcellpre.equals(""))
															||(currcellpre.equals("")&&!tmpnextrowpre.equals(""))){
														tmpcurrhgt =tmpcurrhgt + (int)tmprow.getHeightInPoints();
														//判断是否被包含于同一个cell
													}else
													{
														break;
													}
												}
												//System.out.println("currcell:"+currcell+":"+Integer.toString(cbmaxRowIdx)+":"+Integer.toString(tmpcurrhgt));
												
												rowhgttop=0;
												cellstyle.setRowTop(Integer.toString(rowhgttop));
												CellAllStyle subcellstyle = new CellAllStyle();
												subcellstyle.setRowHeight(Integer.toString(tmpcurrhgt));
												subcellstyle.setRowLeft(Integer.toString(colleft));
												subcellstyle.setRowTop(Integer.toString(rowhgttop));
												subcellstyle.setRowWidth(Integer.toString(cellwd));
												
												subcellstyle.setAlldata(cellstyle.getAlldata());
												subcellstyle.setCellType(cellstyle.getCellType());
												subcellstyle.setDateformat(cellstyle.getDateformat());
												subcellstyle.setNosigndata(cellstyle.getNosigndata());
												subcellstyle.setSigndata(cellstyle.getSigndata());

												subcellstyle.setFontsize(Integer.toString(eFont.getFontHeight()));
												subcellstyle.setFontstyle(eFont.getFontName());
												subcellstyle.setBoldlight(Integer.toString(eFont.getBoldweight()));
												subcellstyle.setHorizonta(Integer.toString(eStyle.getAlignment()));
												subcellstyle.setVertical(Integer.toString(eStyle.getVerticalAlignment()));
												
												
												trendReport.setSubReport(mainrpt,parrptid, areaname, rptsubid, subcellstyle/*子报表的高度*/,subrptidx);
												if(parrptid.equals(mainrpt))
													SetRptAreaMaxHeight(parrptid,areaname,cellht,subrptidx,"1","");
												
												//设置查询条件参数
												setallparalist(rptsubid,parrptid);
												for (int a = 0; a < dynrpt_hpara.size(); a++) {
													String pn=dynrpt_hpara.get(a)[a][1];
													CellAllStyle schstl = new CellAllStyle();
													schstl.setNosigndata(pn);
													
													String schtmp=getSqlColDataType(currcellpre,pn);
													schstl.setCellType(schtmp);
													
													trendReport.setParameter(rptsubid, schstl);
												}
												//子报表的行标志
												rptfixcloid=getRptFixcolIdx(rptsubid);
												rptrowtype=getCellValue(sheetcur.getRow(m).getCell((short)rptfixcloid));//第m行第列
												if(rptrowtype.equalsIgnoreCase(CNTITLE)){ //"标题"
													areaname = "title";
												}else if(rptrowtype.equalsIgnoreCase(CNPGHEAD)){//"表头"
													areaname = "columnHeader";
												}else if(rptrowtype.equalsIgnoreCase(CNPGDTL)){ //"明细"
													areaname = "detail";
												}else if(rptrowtype.equalsIgnoreCase(CNPGSUM)){ //"小计"
													areaname = "summary";
												}else if(rptrowtype.equalsIgnoreCase(CNSUM)){   //"总计"
													areaname = "summary";
												}else if(rptrowtype.equalsIgnoreCase(CNFOOT)){  //"表尾"
													areaname = "summary";
												}
												currrptid = rptsubid;
												mycurrrptId=currrptid;
											}
										}
										}
									}
									//保存sheet中包含的语句名
									SaveSheetsTName(currrptid,currcellpre,i-1);
									if(!currcellpre.equalsIgnoreCase(""))
									lastcellpre = currcellpre;
									
									if(rptcoltype.equalsIgnoreCase(CNNULLHIDE)&&!currcell.equalsIgnoreCase("")){//"空列,隐藏"
										if(nullcolLen>1)//如果有多个空列隐藏列只全部把空列隐藏列作为子报表，否则作为主报表
										{
											subrptcount++;
											String rptsubid = SetRptList(currrptid,currcell,n,subrptcount,iscmflag,m,n,sheetcur,areaname);
											trendReport.CreateJrxml(rptsubid);//创建子报表
											
											//保存生成的子报表在父报表的区域里的idx(父报表Id，报表Id，区域，idx)
											if(areaname.equalsIgnoreCase("detail"))
												SetsubRptList(currrptid,rptsubid,areaname);
											
											rowhgttop=getCellTop(sheetcur,rptsubid,rptrowtype,m,n);
											cellstyle.setRowLeft(new String().valueOf(0));
											cellstyle.setRowTop(new String().valueOf(rowhgttop));
											
											//System.out.println(rptrowtype+":"+"currcell:"+currcell+",m:"+m+",n:"+n+",colleft:"+colleft+",rowhgttop:"+rowhgttop+",cellwd:"+cellwd+",cellht:"+cellht);
											
											int subrptidx=getsubRptList(currrptid,rptsubid);
											if(subrptidx!=0)trendReport.addDetailBand(currrptid, rptsubid, cellstyle);
											trendReport.setSubReport(mainrpt,currrptid, areaname, rptsubid, cellstyle,subrptidx);
											
											String prename=getPreNameByCell(currcell);
											String sql = "";
											String excelsql=getSqlByname(prename);
											Iterator it = pagesqlmap.keySet().iterator();
											Boolean sqlflag = false;
											while(it.hasNext()){
												String key = (String)it.next();
												if(key!=""&&key.equalsIgnoreCase(prename)){
													sqlflag = true;
													String sqltmp = "SELECT a.*,ROWNUM as rn FROM ("+ excelsql +") a WHERE  ROWNUM <= $P{ENDRECORD}";
													sql = "SELECT * FROM ("+ sqltmp +") WHERE rn >= $P{STARTRECORD} ";
													String prptid=getParentByRptId(rptsubid);
													int subrptidex=getsubRptList(prptid,rptsubid);
													trendReport.setBandValue(prptid, rptsubid, getsubRptArea(prptid,rptsubid,areaname), "$P{STARTRECORD}", "$P{STARTRECORD}", subrptidex);
													trendReport.setBandValue(prptid, rptsubid, getsubRptArea(prptid,rptsubid,areaname), "$P{ENDRECORD}", "$P{ENDRECORD}", subrptidex);
												}
											}
											if(!sqlflag){
												sql = excelsql;
											}
											trendReport.setQueryString(rptsubid, sql);
											
											//设置查询条件参数
											setallparalist(rptsubid,currrptid);
											for (int a = 0; a < dynrpt_hpara.size(); a++) {
												String pn=dynrpt_hpara.get(a)[a][1];
												CellAllStyle schstl = new CellAllStyle();
												schstl.setNosigndata(pn);
												
												String schtmp=getSqlColDataType(currcellpre,pn);
												schstl.setCellType(schtmp);
												
												trendReport.setParameter(rptsubid, schstl);
											}
											//取报表的所有查询字段(字段、类型)
											List<String[]> RptFieldList = getRptFieldList(mainrpt+currcellpre);
											if(RptFieldList!=null){
												for(int rpfl=0;rpfl<RptFieldList.size();rpfl++){
													String[] rpfltmp= new String[3];
													rpfltmp[0]=rptsubid;
													rpfltmp[1]="F";
													rpfltmp[2]=RptFieldList.get(rpfl)[0];
													CellAllStyle rpflstl = new CellAllStyle();
													rpflstl.setAlldata("$F{"+rpfltmp[2]+"}");
													rpflstl.setSigndata("$F{"+rpfltmp[2]+"}");
													rpflstl.setNosigndata(rpfltmp[2]);
													rpflstl.setCellType(RptFieldList.get(rpfl)[1]);
													trendReport.setField(rptsubid, rpflstl);
													//System.out.println(rpfltmp[2]+":"+RptFieldList.get(rpfl)[1]);
													allparalist.add(rpfltmp);//（报表Id，类型(P，F，V),参数"name"）
												}
											}
											SetRptAreaMaxHeight(rptsubid,areaname,cellht,subrptidx,"","");
											
											//子报表的行标志
											rptfixcloid=getRptFixcolIdx(rptsubid);
											rptrowtype=getCellValue(sheetcur.getRow(m).getCell((short)rptfixcloid));//第m行第列
											if(rptrowtype.equalsIgnoreCase(CNTITLE)){ //"标题"
												areaname = "title";
											}else if(rptrowtype.equalsIgnoreCase(CNPGHEAD)){//"表头"
												areaname = "columnHeader";
											}else if(rptrowtype.equalsIgnoreCase(CNPGDTL)){ //"明细"
												areaname = "detail";
											}else if(rptrowtype.equalsIgnoreCase(CNPGSUM)){ //"小计"
												areaname = "summary";
											}else if(rptrowtype.equalsIgnoreCase(CNSUM)){   //"总计"
												areaname = "summary";
											}else if(rptrowtype.equalsIgnoreCase(CNFOOT)){  //"表尾"
												areaname = "summary";
											}
											currrptid = rptsubid;
											mycurrrptId=currrptid;
										}else{
											// 设置sql
											String prename=getPreNameByCell(currcell);
											String sql = "";
											String excelsql=getSqlByname(prename);
											Iterator it = pagesqlmap.keySet().iterator();
											Boolean sqlflag = false;
											while(it.hasNext()){
												String key = (String)it.next();
												if(key!=""&&key.equalsIgnoreCase(prename)){
													sqlflag = true;
													String sqltmp = "SELECT a.*,ROWNUM as rn FROM ("+ excelsql +") a WHERE  ROWNUM <= $P{ENDRECORD}";
													sql = "SELECT * FROM ("+ sqltmp +") WHERE rn >= $P{STARTRECORD} ";
													String prptid=getParentByRptId(mainrpt);
													int subrptidx=getsubRptList(prptid,mainrpt);
													trendReport.setBandValue(prptid, mainrpt, getsubRptArea(prptid,mainrpt,areaname), "$P{STARTRECORD}", "$P{STARTRECORD}", subrptidx);
													trendReport.setBandValue(prptid, mainrpt, getsubRptArea(prptid,mainrpt,areaname), "$P{ENDRECORD}", "$P{ENDRECORD}", subrptidx);
												}
											}
											if(!sqlflag){
												sql = excelsql;
											}
											mainsql=sql;
											trendReport.setQueryString(mainrpt, sql);
											SetMainRptList(mainrpt,currcell);
											//取报表的所有查询字段(字段、类型)
											List<String[]> RptFieldList = getRptFieldList(mainrpt+currcellpre);
											if(RptFieldList!=null){
												for(int rpfl=0;rpfl<RptFieldList.size();rpfl++){
													String[] rpfltmp= new String[3];
													rpfltmp[0]=mainrpt;
													rpfltmp[1]="F";
													rpfltmp[2]=RptFieldList.get(rpfl)[0];
													CellAllStyle rpflstl = new CellAllStyle();
													rpflstl.setAlldata("$F{"+rpfltmp[2]+"}");
													rpflstl.setSigndata("$F{"+rpfltmp[2]+"}");
													rpflstl.setNosigndata(rpfltmp[2]);
													rpflstl.setCellType(RptFieldList.get(rpfl)[1]);
													trendReport.setField(mainrpt, rpflstl);
													//System.out.println(rpfltmp[2]+":"+RptFieldList.get(rpfl)[1]);
													allparalist.add(rpfltmp);//（报表Id，类型(P，F，V),参数"name"）
												}
											}
										}
									}else{
										//新建单元格里的参数 数据$p{name}数据,"P","name"，$p{name}
										String[] tmp = new String[4];
										tmp=getParamByCellValue(currcell);
										cellstyle.setAlldata(tmp[0]);
										cellstyle.setSigndata(tmp[3]);
										cellstyle.setNosigndata(tmp[2]);

										//把新建的参数放入参数列表
										String tmp2[] = {currrptid,tmp[1],tmp[2],""};//currrptid,"P","name"
										if(!isexist(currrptid,tmp[2],tmp[1])){
											if("P".equalsIgnoreCase(tmp[1])){
												trendReport.setParameter(currrptid,cellstyle);
												//System.out.println("currrptid:"+currrptid+":flag:"+tmp[1]+":name:"+tmp[0]);
											}else if("F".equalsIgnoreCase(tmp[1])){
												//System.out.println("currrptid:"+currrptid+":"+cellstyle.getNosigndata());
												//判断当前报表是否已经设置过sql
												if("".equals(getrptsql(currrptid)))
												{
													
													String sql = "";
													String excelsql=getSqlByname(currcellpre);
													Iterator it = pagesqlmap.keySet().iterator();
													Boolean sqlflag = false;
													while(it.hasNext()){
														String key = (String)it.next();
														if(key!=""&&key.equalsIgnoreCase(currcellpre)){
															sqlflag = true;
															String sqltmp = "SELECT a.*,ROWNUM as rn FROM ("+ excelsql +") a WHERE  ROWNUM <= $P{ENDRECORD}";
															sql = "SELECT * FROM ("+ sqltmp +") WHERE rn >= $P{STARTRECORD} ";
															String prptid=getParentByRptId(currrptid);
															int subrptidx=getsubRptList(prptid,currrptid);
															trendReport.setBandValue(prptid, currrptid, getsubRptArea(prptid,currrptid,areaname), "$P{STARTRECORD}", "$P{STARTRECORD}", subrptidx);
															trendReport.setBandValue(prptid, currrptid, getsubRptArea(prptid,currrptid,areaname), "$P{ENDRECORD}", "$P{ENDRECORD}", subrptidx);
														}
													}
													if(!sqlflag){
														sql = excelsql;
													}
													trendReport.setQueryString(currrptid, sql);
//													trendReport.setQueryString(currrptid, getSqlByname(currcellpre));
													setRptListTname(currrptid,currcellpre);
												}
												trendReport.setField(currrptid, cellstyle);
											}else if(tmp[1]!=null && !"".equalsIgnoreCase(tmp[1])){// $p{name},"P","name"
												trendReport.setVariable(currrptid, "Report", tmp[1], cellstyle);
											}
											if(tmp[1]!=null && !tmp[1].equals(""))
												allparalist.add(tmp2);//（报表Id，类型(P，F，V),参数"name"）
										}
										String prptid=getParentByRptId(currrptid);
										//将sql语句的参数也加入参数列表 $p{t.name} --> $p{name},"P","name"
										if(tmp[1]!=null && !"".equalsIgnoreCase(tmp[1]) && tmp[2]!=null && !"".equalsIgnoreCase(tmp[2])){
										    List<String[]> list = new ArrayList<String[]>();
										    list = getParamListByCellValue(currcell);
										    CellAllStyle Sqlcellstyle = new CellAllStyle();
										    for(int a=0;a<list.size();a++){
										    	
										    	//$p{name}数据,"P","name"，$p{name}
										    	String t1=list.get(a)[0];//$p{name}数据
										    	String t2=list.get(a)[1];//"P"
										    	String t3=list.get(a)[2];//"name"
										    	String t4=list.get(a)[3];//$p{name}
										    	
										    	String tmp21[] = {currrptid,t2,t3,""};//currrptid,"P","name"
										    	Sqlcellstyle.setAlldata(t1);
										    	Sqlcellstyle.setSigndata(t4);
										    	Sqlcellstyle.setNosigndata(t3);
										    	
										    	String schtmp=getSqlColDataType(currcellpre,t3);
										    	Sqlcellstyle.setCellType(schtmp);
												
										    	if(!isexist(currrptid,tmp21[2],tmp21[1])){
										    		if("P".equalsIgnoreCase(t2)){
														trendReport.setParameter(currrptid, Sqlcellstyle);
														//System.out.println("currrptid:"+currrptid+":flag:"+t2+":name:"+t1);
													}else if("F".equalsIgnoreCase(t2)){
														//System.out.println("currrptid:"+currrptid+":"+cellstyle.getNosigndata());
														trendReport.setField(currrptid, cellstyle);
													}else if(t2!=null){
														trendReport.setVariable(currrptid, "Report", t2, Sqlcellstyle);
													}
										    		if(tmp2[2]!=null && !"".equalsIgnoreCase(tmp21[2]))
										    			allparalist.add(tmp21);//（报表Id，参数"name"，类型(P，F，V)）
										    	}
												
										    	//父报表没有这个参数也增加变量
										    	if(prptid.equalsIgnoreCase(""))prptid=mainrpt;
										    	String tmp22[] = {prptid,t2,t3,""};//currrptid,"P","name"
										    	
										    	if(!isexist(prptid,tmp22[2],tmp22[1])){
										    		if("P".equalsIgnoreCase(t2)){
														trendReport.setParameter(prptid, Sqlcellstyle);
														//System.out.println("prptid:"+prptid+":currrptid:"+currrptid+":flag:"+t2+":name:"+t1);
													}else if("F".equalsIgnoreCase(t2)){
														//System.out.println("currrptid:"+currrptid+":"+cellstyle.getNosigndata());
														trendReport.setField(prptid, cellstyle);
													}else if(t2!=null){
														trendReport.setVariable(prptid, "Report", t2, Sqlcellstyle);
													}
										    		if(tmp22[2]!=null && !"".equalsIgnoreCase(tmp22[2]))
										    			allparalist.add(tmp22);//（报表Id，参数"name"，类型(P，F，V)）
										    	}
										    	// 设置子报表与父报表之间的变量绑定
									    		if(!isbind(currrptid,tmp22[2],tmp22[1],prptid)){
									    			int subrptidx=getsubRptList(prptid,currrptid);
									    			//t3 "name"  t4 $p{name}
									    			String bids=t4;
									    			if(isexist(prptid,t3,"F")){
									    				bids = "$F{"+t3+"}";
									    			}
											    	trendReport.setBandValue(prptid, currrptid, getsubRptArea(prptid,currrptid,areaname)/*父报表区域*/, t4, bids/*父报表的F或P，应该先找F，没有的再找P*/,subrptidx);
											    	setParaBinded(currrptid,tmp22[2],tmp22[1],prptid);//已经绑定
									    		}
										    }
										    //将查询条件也绑定到子报表
											for (int a = 0; a < dynrpt_hpara.size(); a++) {
												String pn=dynrpt_hpara.get(a)[a][1];
												if(!isbind(currrptid,pn,"P",prptid)){
													int subrptidx=getsubRptList(prptid,currrptid);
													trendReport.setBandValue(prptid, currrptid, getsubRptArea(prptid,currrptid,areaname)/*父报表区域*/, "$P{"+pn+"}", "$P{"+pn+"}"/*父报表的F或P，应该先找F，没有的再找P*/,subrptidx);
													setParaBinded(currrptid,pn,"P",prptid);//已经绑定
												}
											}
										}
										
										if(rptrowtype.equalsIgnoreCase(CNTITLE)){ //"标题"
											trendReport.setTitle(currrptid, cellstyle);
										}else if(rptrowtype.equalsIgnoreCase(CNPGHEAD)){//"表头"
											trendReport.setColumnHeader(currrptid, cellstyle);
										}else if(rptrowtype.equalsIgnoreCase(CNPGDTL)){ //"明细"
											trendReport.setDetail(currrptid, cellstyle);
										}else if(rptrowtype.equalsIgnoreCase(CNPGSUM) 
												|| rptrowtype.equalsIgnoreCase(CNSUM) 
												|| rptrowtype.equalsIgnoreCase(CNFOOT)){ //"小计"\"总计"\"表尾"
											trendReport.setSummary(currrptid, cellstyle);
										}
									}
								}
						    }
						    
							//多行明细高度汇总
							if(mycurrrptId.equalsIgnoreCase(myoldrptId)&&!myoldrptId.equals(""))
							{
								int rptfixcolid=getRptFixcolIdx(currrptid);
								String myrptrowtype=getCellValue(sheetcur.getRow(m).getCell((short)rptfixcolid));//第m行第列
								if(myrptrowtype.equals(CNPGDTL)){
									SetRptAreaMaxHeight(currrptid,"detail",cellht,0,"","1");
								}
							}
							myoldrptId=mycurrrptId;
							preareaname=currareaname;
							if(rptwidth<rowwidth)rptwidth=rowwidth;//报表的最大宽度
						}
						
						//设置区域高度(报表Id，区域，高度)
						String mainrptId=getMainRptId(mianrptno);
						//保存报表宽度
						SetRptpagewidth(mainrptId,rptwidth);
						if(mainsql.equals("")&&"0".equalsIgnoreCase(ishavetitle(mainrptId))){
							trendReport.setQueryString(mainrptId, CNONESELECTROW);
						}
						for(int ai=0;ai<rptAreaHeightlist.size();ai++){
							if(!"".equalsIgnoreCase(rptAreaHeightlist.get(ai)[0])&&!"".equalsIgnoreCase(rptAreaHeightlist.get(ai)[1]))
							{
								String tmparea=rptAreaHeightlist.get(ai)[1];//aream
								String tmprpt=rptAreaHeightlist.get(ai)[0];
								String tmpidx=rptAreaHeightlist.get(ai)[3];
								if(tmparea.equalsIgnoreCase("detail")){
									int rptareahgt=Integer.parseInt(rptAreaHeightlist.get(ai)[2]);
									if(!tmprpt.equals(mainrptId))
										rptareahgt=getAreaHeight2(tmprpt,tmparea,tmpidx);
									trendReport.setBandHeight(tmprpt,rptAreaHeightlist.get(ai)[1],rptareahgt,Integer.parseInt(tmpidx));
								}
								else{
									int rptght=getAreaHeight(sheetcur,tmprpt,tmparea);//String currrptid,String areaname)
									trendReport.setBandHeight(tmprpt,rptAreaHeightlist.get(ai)[1],rptght,0);
								}
							}
						}
						String printdirecttmp="";
						if("1".equals(printdirect))printdirecttmp="Landscape";
						else printdirecttmp="Portrait";
						//设置报表宽度
						//存放生成的报表文件列表，(父报表id，报表Id，列名前缀，第几个子报表｛确定前面列的区域类型｝)
						for(int ai=0;ai<rptlist.size();ai++){
							String rpt=rptlist.get(ai)[1];
							int rptbgncolidx=getRptBegincol(rpt);
							int wd=0;
							if(chkishavesub(rpt).equals("1")){//父报表
								for(int mi=cncolumn;mi<=rptbgncolidx;mi++){
									wd=wd+sheetcur.getColumnWidth(mi)/256;
								}
								rptwidth=getRptpagewidth(rpt);
								int tmpwd=rptwidth-wd;
								if(tmpwd<0)tmpwd=0;
								trendReport.setpublicProperty(rpt,String.valueOf(tmpwd),printdirecttmp,"true");//名称、页面宽度、打印方向、列宽、是否忽略分页）
							}else{
								wd=0;
								int bgcol=Integer.parseInt(rptlist.get(ai)[3]);
								int endcol=Integer.parseInt(rptlist.get(ai)[8]);
								int bgrowInd = Integer.parseInt(rptlist.get(ai)[5]);
								if(bgrowInd<rows)
								{
									int celllen = sheetcur.getRow(bgrowInd).getLastCellNum();//sheet列数
									if(endcol==0)endcol=celllen;
									for(int mi=bgcol;mi<endcol;mi++){
										wd=wd+sheetcur.getColumnWidth(mi)/256;
									}
									trendReport.setpublicProperty(rpt,String.valueOf(wd),printdirecttmp,"true");//名称、页面宽度、打印方向、列宽、是否忽略分页）
								}
							}
						}
						
						String cmprtn = trendReport.compileJrxml();
						if(!cmprtn.equalsIgnoreCase("")){
							rtnMsg = "报表文件编译出错";
							break;
						}
						
						//excel多sheet列表
						SetMainSubRptList(getMainRptId(mianrptno),sheetname);
						if(ishavemainrpt.equals("1"))mianrptno=mianrptno+1;
					}
				}
		} catch (InvalidFormatException e) {
			rtnMsg = e.getMessage();
		}
        return rtnMsg;
    }
    
	public List<String[][]> getPrintparam() {
		return printparam;
	}

	public void setPrintparam(List<String[][]> printparam) {
		this.printparam = printparam;
	}

	public String getBatchbegin() {
		return batchbegin;
	}

	public void setBatchbegin(String batchbegin) {
		this.batchbegin = batchbegin;
	}

	public String getBatchend() {
		return batchend;
	}

	public void setBatchend(String batchend) {
		this.batchend = batchend;
	}

	public String getPagesqlbegin() {
		return pagesqlbegin;
	}

	public void setPagesqlbegin(String pagesqlbegin) {
		this.pagesqlbegin = pagesqlbegin;
	}

	public String getPagesqlend() {
		return pagesqlend;
	}

	public void setPagesqlend(String pagesqlend) {
		this.pagesqlend = pagesqlend;
	}

	public String getPrintbegin() {
		return printbegin;
	}

	public void setPrintbegin(String printbegin) {
		this.printbegin = printbegin;
	}

	public String getPrintend() {
		return printend;
	}

	public void setPrintend(String printend) {
		this.printend = printend;
	}

	public List<String[][]> getBatchparam() {
		return batchparam;
	}

	public void setBatchparam(List<String[][]> batchparam) {
		this.batchparam = batchparam;
	}

	public TreeMap getPagesqlmap() {
		return pagesqlmap;
	}

	public void setPagesqlmap(TreeMap pagesqlmap) {
		this.pagesqlmap = pagesqlmap;
	}

	public String getIsoutTxt() {
		return isoutTxt;
	}

	public void setIsoutTxt(String isoutTxt) {
		this.isoutTxt = isoutTxt;
	}

	public String getPixs() {
		return pixs;
	}

	public void setPixs(String pixs) {
		this.pixs = pixs;
	}

	public String getEmptsperpix() {
		return emptsperpix;
	}

	public void setEmptsperpix(String emptsperpix) {
		this.emptsperpix = emptsperpix;
	}

	public String getFildemps() {
		return fildemps;
	}

	public void setFildemps(String fildemps) {
		this.fildemps = fildemps;
	}

	public String getIssendmsg() {
		return issendmsg;
	}

	public void setIssendmsg(String issendmsg) {
		this.issendmsg = issendmsg;
	}

	public String getUseid() {
		return useid;
	}

	public void setUseid(String useid) {
		this.useid = useid;
	}

	public String getUpwd() {
		return upwd;
	}

	public void setUpwd(String upwd) {
		this.upwd = upwd;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFindSheetName() {
		return findSheetName;
	}

	public void setFindSheetName(String findSheetName) {
		this.findSheetName = findSheetName;
	}

	public TreeMap<Integer, List<String>> getConditions() {
		return conditions;
	}

	public void setConditions(TreeMap<Integer, List<String>> conditions) {
		this.conditions = conditions;
	}

	public List<List<String>> getValParams() {
		return valParams;
	}

	public void setValParams(List<List<String>> valParams) {
		this.valParams = valParams;
	}

	public Map<String, List<String>> getFindConfigs() {
		return findConfigs;
	}

	public void setFindConfigs(Map<String, List<String>> findConfigs) {
		this.findConfigs = findConfigs;
	}

	public TreeMap<Integer, TreeMap<Integer, String>> getImpSqls() {
		return impSqls;
	}

	public void setImpSqls(TreeMap<Integer, TreeMap<Integer, String>> impSqls) {
		this.impSqls = impSqls;
	}

	public String getDynrptver() {
		return dynrptver;
	}

	public void setDynrptver(String dynrptver) {
		this.dynrptver = dynrptver;
	}
	
	public String getResultSql() {
		return resultSql;
	}

	public void setResultSql(String resultSql) {
		this.resultSql = resultSql;
	}

	public List<String[]> getImgcolparam() {
		return imgcolparam;
	}

	public void setImgcolparam(List<String[]> imgcolparam) {
		this.imgcolparam = imgcolparam;
	}

	public String getXlstype() {
		return xlstype;
	}

	public void setXlstype(String xlstype) {
		this.xlstype = xlstype;
	}

	public String getIsoutmg() {
		return isoutmg;
	}

	public void setIsoutmg(String isoutmg) {
		this.isoutmg = isoutmg;
	}

	public String getImgtype() {
		return imgtype;
	}

	public void setImgtype(String imgtype) {
		this.imgtype = imgtype;
	}

	public String getImgbeginrow() {
		return imgbeginrow;
	}

	public void setImgbeginrow(String imgbeginrow) {
		this.imgbeginrow = imgbeginrow;
	}

	public String getImgendrow() {
		return imgendrow;
	}

	public void setImgendrow(String imgendrow) {
		this.imgendrow = imgendrow;
	}

	public String getImgxtitle() {
		return imgxtitle;
	}

	public void setImgxtitle(String imgxtitle) {
		this.imgxtitle = imgxtitle;
	}

	public String getImgytitle() {
		return imgytitle;
	}

	public void setImgytitle(String imgytitle) {
		this.imgytitle = imgytitle;
	}

	public String getImgbgcolor() {
		return imgbgcolor;
	}

	public void setImgbgcolor(String imgbgcolor) {
		this.imgbgcolor = imgbgcolor;
	}

	public String getImgfontsize() {
		return imgfontsize;
	}

	public void setImgfontsize(String imgfontsize) {
		this.imgfontsize = imgfontsize;
	}

	public List<String[]> getShttnamelist() {
		return shttnamelist;
	}

	public String getImgistip() {
		return imgistip;
	}

	public void setImgistip(String imgistip) {
		this.imgistip = imgistip;
	}

	public String getImgistext() {
		return imgistext;
	}

	public void setImgistext(String imgistext) {
		this.imgistext = imgistext;
	}
  
  public String getDynbodys() {
		return dynbodys;
	}

	public void setDynbodys(String dynbodys) {
		this.dynbodys = dynbodys;
	}

	public String getImgxcnt() {
		return imgxcnt;
	}

	public void setImgxcnt(String imgxcnt) {
		this.imgxcnt = imgxcnt;
	}
}
