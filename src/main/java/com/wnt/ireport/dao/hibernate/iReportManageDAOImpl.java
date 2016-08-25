package com.wnt.ireport.dao.hibernate;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.HeaderFooter;
import jxl.HeaderFooter.Contents;
import jxl.format.PageOrientation;
import jxl.format.PaperSize;
import jxl.read.biff.BiffException;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import oracle.jdbc.OracleTypes;

import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.HibernateException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate5.SessionFactoryUtils;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import com.wnt.ireport.dao.iReportManageDAO;
import com.wnt.ireport.model.JasperSubViewer;
import com.wnt.ireport.model.ResultInfo;
import com.wnt.ireport.model.Combmaplist;
import com.wnt.ireport.po.EbsDynrptImg;
import com.wnt.ireport.po.EbsDynrptImgcols;
import com.wnt.ireport.po.EbsDynrptBatchpara;
import com.wnt.ireport.po.EbsDynrptBpara;
import com.wnt.ireport.po.EbsDynrptFile;
import com.wnt.ireport.po.EbsDynrptHpara;
import com.wnt.ireport.po.EbsDynrptPagepara;
import com.wnt.ireport.po.EbsDynrptPrintpara;
import com.wnt.ireport.po.EbsDynrptSqlCol;
import com.wnt.ireport.po.EbsDynimpBpara;
import com.wnt.ireport.po.EbsDynimpCparam;
import com.wnt.ireport.po.EbsDynimpExesql;
import com.wnt.ireport.po.EbsDynimpFind;
import com.wnt.ireport.po.EbsDynimpHpara;
import com.wnt.ireport.po.EbsDynrptMsgpara;
import com.wnt.ireport.util.CommUtil;
import com.wnt.util.SingleGetCon;

/**
 * @author luzh
 * 
 * To change the template for this generated type comment go to
 */
public class iReportManageDAOImpl extends HibernateDaoSupport implements
		iReportManageDAO {

	private String treestr="";
	private String printscope = "0";//打印范围设置
	private String querycond= "1";//查询条件设置
	private String datasql  = "2";//sql语句设置
	private String pagecond  = "3";//分页记录设置
	private String imgcond = "4";//图表参数设置
	private String printone = "1";//纵向、水平居中
	private String printtwo = "2";//横向、垂直居中
	private String printthree = "3";//both
	public void procHtmlRpt(HttpServletRequest request,
			HttpServletResponse response, Map parameter, String reportFile,
			String title) throws Exception {
		Connection conn = null;

		// 获取Connection连接
		try {
			conn = getconn();

			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new File(reportFile));
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, conn);

			if (jasperPrint.getPages().size() > 0) {
				JasperSubViewer.viewReport(jasperPrint, false, null, title);
			}
			// 生成pdf文件在响应目录下，此时就可以在IE里查看了
			// File jasperFileName = new File(reportFile);
			// JasperRunManager.runReportToPdfFile(jasperFileName.getPath(),parameter,conn);
			// runReportToPdfFile(java.lang.String sourceFileName,
			// java.lang.String destFileName, java.util.Map parameters,
			// java.sql.Connection conn)

		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public Connection getconn() {
		Connection conn = null;
		try {
			conn = SessionFactoryUtils.getDataSource(getSessionFactory()).getConnection();;
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return conn;
	}

	// 取得清算月份
	public String gettradingday() throws DataAccessException {
		Connection connection = null;
		CallableStatement cstmt = null;
		ResultInfo reinfo = null;

		String tradingday = "";
		try {
			connection = getconn();
			cstmt = connection.prepareCall("{ ?= call pkg_tech_pub.uf_get_tradingday}");
			cstmt.registerOutParameter(1, Types.CHAR);
			cstmt.execute();
			tradingday = (String) cstmt.getObject(1);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (cstmt != null) {
					cstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return tradingday;
	}

	// 导出不同的报表在不同sheet中
	public String expRptManySheet(HttpServletRequest request,
			HttpServletResponse response, Map[] arrmap, String[] reportFile,
			String[] title, String type) throws Exception {
		Connection conn = null;
		String rptfile = "";
		String serverName = request.getServerName(); // 获得服务器的名字
		String realPath = request.getRealPath(serverName); // 取得互联网程序的绝对地址
		realPath = realPath.substring(0, realPath.lastIndexOf("\\")) + "\\temp\\";
		String sysid = CommUtil.getsysguid32();
		// 获取Connection连接
		try {
			conn = getconn();

			List<JasperReport> jasperReports = new ArrayList<JasperReport>();
			for (int i = 0; i < reportFile.length; i++) {
				jasperReports.add((JasperReport) JRLoader.loadObject(new File(reportFile[i])));
			}

			List<JasperPrint> jasperPrints = new ArrayList<JasperPrint>();

			for (int i = 0; i < jasperReports.size(); i++) {
				jasperPrints.add(JasperFillManager.fillReport(jasperReports.get(i), arrmap[i], conn));
				if (type.equalsIgnoreCase("pdf")) {
					// 如果是导出pdf，大数据量不分页会显示不出来，
					arrmap[i].put("IS_IGNORE_PAGINATION", Boolean.FALSE);
				}
			}
			if (type.equalsIgnoreCase("xls")||type.equalsIgnoreCase("xlsx")||type.equalsIgnoreCase("txt")||type.equalsIgnoreCase("dbf")) {
				JRExporter xlsExporter = null;
				if (type.equalsIgnoreCase("xlsx"))
				{
					xlsExporter = new JRXlsxExporter();//excel2007
					rptfile = realPath + sysid + ".xlsx";
				}
				else
				{
					xlsExporter = new JRXlsExporter();//excel2003
					rptfile = realPath + sysid + ".xls";
				}
				ByteArrayOutputStream xlsOut = new ByteArrayOutputStream();
				xlsExporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST,jasperPrints);
				xlsExporter.setParameter(JRExporterParameter.OUTPUT_STREAM,xlsOut);
				xlsExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE);// 删除记录最下面的空行
				xlsExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,Boolean.FALSE);// 删除多余的ColumnHeader
				xlsExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.FALSE);// 显示边框
				xlsExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE,Boolean.TRUE);// 单元格格式
				xlsExporter.setParameter(JRXlsExporterParameter.SHEET_NAMES,title);// sheet名称

				xlsExporter.exportReport();
				
				FileOutputStream out = new FileOutputStream(new File(rptfile));
				out.write(xlsOut.toByteArray());
				out.flush();
				out.close();
				xlsOut.flush();
				xlsOut.close();
			} else if (type.equalsIgnoreCase("pdf")) {
				// 使用JRExporter来生成PDF,很多参数可以查api或ireport的属性窗口
				JRExporter pdfExporter = new JRPdfExporter();
				ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
				pdfExporter.setParameter(JRPdfExporterParameter.JASPER_PRINT_LIST, jasperPrints);
				pdfExporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM,pdfOut);
				pdfExporter.setParameter(JRPdfExporterParameter.CHARACTER_ENCODING, "utf-8");
				pdfExporter.exportReport();
				rptfile = realPath + sysid + ".pdf";
				FileOutputStream out = new FileOutputStream(new File(rptfile));
				out.write(pdfOut.toByteArray());
				out.flush();
				out.close();
				pdfOut.flush();
				pdfOut.close();
			}
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				CommUtil.writeLogfile("expRptManySheet(01)"+e.getMessage());
				e.printStackTrace();
			}
		}
		return rptfile;
	}

	public String expRpt(HttpServletRequest request,
			HttpServletResponse response, Map parameter, String reportFile,
			String title, String type) throws Exception {
		Connection conn = null;
		String rptfile = "";
		String serverName = request.getServerName(); // 获得服务器的名字
		String realPath = request.getRealPath(serverName); // 取得互联网程序的绝对地址
		realPath = realPath.substring(0, realPath.lastIndexOf(File.separator))
				+ File.separator+"temp"+File.separator;
		String sysid = CommUtil.getsysguid32();
		// 获取Connection连接
		try {
			conn = getconn();
			JasperReport jasperReport = (JasperReport) JRLoader
					.loadObject(new File(reportFile));
//			if (type.equalsIgnoreCase("pdf")) {
//				// 如果是导出pdf，大数据量不分页会显示不出来，
//				parameter.put("IS_IGNORE_PAGINATION", Boolean.FALSE);
//			}
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, conn);
			String sheetname[] = new String[1];
			sheetname[0] = title;
			if (type.equalsIgnoreCase("xls")||type.equalsIgnoreCase("xlsx")||type.equalsIgnoreCase("txt")||type.equalsIgnoreCase("dbf")) {
				JRExporter xlsExporter = null;
				if (type.equalsIgnoreCase("xlsx"))
				{
					xlsExporter = new JRXlsxExporter();//excel2007
					rptfile = realPath + sysid + ".xlsx";
				}
				else
				{
					xlsExporter = new JRXlsExporter();//excel2003
					rptfile = realPath + sysid + ".xls";
				}
				ByteArrayOutputStream xlsOut = new ByteArrayOutputStream();
				xlsExporter.setParameter(JRExporterParameter.JASPER_PRINT,jasperPrint);
				xlsExporter.setParameter(JRExporterParameter.OUTPUT_STREAM,xlsOut);
				xlsExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE);// 删除记录最下面的空行
				xlsExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,Boolean.FALSE);// 删除多余的ColumnHeader
				xlsExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.FALSE);// 显示边框
				xlsExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE,Boolean.TRUE);// 单元格格式
				xlsExporter.setParameter(JRXlsExporterParameter.SHEET_NAMES,sheetname);// 单元格格式

				xlsExporter.exportReport();
				FileOutputStream out = new FileOutputStream(new File(rptfile));
				out.write(xlsOut.toByteArray());
				out.flush();
				out.close();
				xlsOut.flush();
				xlsOut.close();
			} else if (type.equalsIgnoreCase("pdf")) {
				// 使用JRExporter来生成PDF,很多参数可以查api或ireport的属性窗口
				JRExporter pdfExporter = new JRPdfExporter();
				ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
				pdfExporter.setParameter(JRPdfExporterParameter.JASPER_PRINT,jasperPrint);
				pdfExporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM,pdfOut);
				pdfExporter.setParameter(JRPdfExporterParameter.CHARACTER_ENCODING, "utf-8");
				pdfExporter.exportReport();
				rptfile = realPath + sysid + ".pdf";
				FileOutputStream out = new FileOutputStream(new File(rptfile));
				out.write(pdfOut.toByteArray());
				out.flush();
				out.close();
				pdfOut.flush();
				pdfOut.close();
			}else if(type.equalsIgnoreCase("csv")){
				JRCsvExporter csvExporter=new JRCsvExporter();
				ByteArrayOutputStream csvOut = new ByteArrayOutputStream();
                response.setContentType("application/x-msdownload"); 
                csvExporter.setParameter(JRExporterParameter.JASPER_PRINT,jasperPrint); 
                csvExporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "GBK");
                csvExporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM,csvOut);
                csvExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE);
                csvExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,Boolean.FALSE);
                csvExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.FALSE);
                csvExporter.exportReport();   
                rptfile = realPath + sysid + ".csv";
				FileOutputStream out = new FileOutputStream(new File(rptfile));
				out.write(csvOut.toByteArray());
				out.flush();
				out.close();
				csvOut.flush();
				csvOut.close();
			}
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				CommUtil.writeLogfile("expRpt(01)"+e.getMessage());
				e.printStackTrace();
			}
		}
		return rptfile;
	}

	public List gettradingdayList(String begindate, String enddate)
			throws DataAccessException {
		String str = "from BTrdcalendar where 1=1 ";

		if (begindate != null && begindate.trim().length() != 0
				&& enddate != null && enddate.trim().length() != 0)
			str = str + " and (tradingday >= '" + begindate
					+ "' and tradingday<='" + enddate + "') ";
		else if (begindate != null && begindate.trim().length() != 0)
			str = str + " and tradingday >= '" + begindate + "' ";
		else if (enddate != null && enddate.trim().length() != 0)
			str = str + " and tradingday <= '" + enddate.trim() + "' ";
		str = str + " order by tradingday asc";
		return getHibernateTemplate().find(str);
	}

	public String expRptToXls(HttpServletRequest request,
			HttpServletResponse response, Map parameter, String reportFile,
			String title, String path) throws Exception {
		Connection conn = null;
		String rptfile = "";
		String serverName = request.getServerName(); // 获得服务器的名字
		String realPath = request.getRealPath(serverName); // 取得互联网程序的绝对地址
		realPath = realPath.substring(0, realPath.lastIndexOf("\\"))
				+ "\\temp\\";
		String sysid = CommUtil.getsysguid32();
		// 获取Connection连接
		try {
			conn = getconn();
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new File(reportFile));
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, conn);
			JRXlsxExporter xlsExporter = new JRXlsxExporter();//excel2007
			//JRExporter xlsExporter = new JRXlsExporter();//excel2003
			ByteArrayOutputStream xlsOut = new ByteArrayOutputStream();
			xlsExporter.setParameter(JRExporterParameter.JASPER_PRINT,jasperPrint);
			xlsExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, xlsOut);
			xlsExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE);// 删除记录最下面的空行
			xlsExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,Boolean.FALSE);// 删除多余的ColumnHeader
			xlsExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.FALSE);// 显示边框
			xlsExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);// 单元格格式
			xlsExporter.exportReport();
			rptfile = path + "\\" + title + ".xlsx";
			File fl = new File(rptfile);
			if (fl.exists())
				fl.delete();
			FileOutputStream out = new FileOutputStream(new File(rptfile));
			out.write(xlsOut.toByteArray());
			out.flush();
			out.close();
			xlsOut.flush();
			xlsOut.close();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				CommUtil.writeLogfile("expRptToXls(01)"+e.getMessage());
				e.printStackTrace();
			}
		}
		return rptfile;
	}

	// 查询国泰君安期货下属营业部
	public List quoteGtjaqhdep(String depname) throws DataAccessException {

		String hql = "from VDepartment t where 1=1 ";
		if (depname != null && depname.trim().length() != 0)
			hql = hql + " and t.depname like '%" + depname.trim() + "%' ";
		hql = hql
				+ " and firmcode = '1000' and depcode not in ('1018','1006','1008')";
		return getHibernateTemplate().find(hql);
	}

	// 设置excel打印范围，及单元格类型为empty
	public void setPrintscope(String filepath, String tabtailinfo,
			String xmlPath, String reportno) {
		SAXBuilder builder = null;
		Document document = null;

		builder = new SAXBuilder(false);

		File xmlfile = new File(xmlPath);
		try {
			Reader xmlStreamReader = new InputStreamReader(new FileInputStream(xmlfile), "utf-8");
			document = builder.build(xmlStreamReader);
		} catch (JDOMException e) {
			CommUtil.writeLogfile("setPrintscope(01)"+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			CommUtil.writeLogfile("setPrintscope(02)"+e.getMessage());
			e.printStackTrace();
		}

		Element root = document.getRootElement();// 获得根元素

		Double pagewidth = Double.parseDouble((root.getAttributeValue("pageWidth")));
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMaximumFractionDigits(2);
		Double A4width = new Double(840);
		Double A3width = new Double(1215);
		String sa4 = format.format(A4width / pagewidth);
		String sa3 = format.format(A3width / pagewidth);
		int printscope4 = (int) (Double.parseDouble(sa4) * 100);
		int printscope3 = (int) (Double.parseDouble(sa3) * 100);
		jxl.Workbook rwb = null;
		WritableWorkbook wbook = null;
		try {
			InputStream is = new FileInputStream(filepath);
			rwb = jxl.Workbook.getWorkbook(is);
			wbook = jxl.Workbook.createWorkbook(new File(filepath), rwb);

			// 设置所有的sheet的打印范围
			for (int i = 0; i < wbook.getSheets().length; i++) {
				jxl.Sheet rs = wbook.getSheet(i);
				jxl.SheetSettings setting = rs.getSettings();
				if(reportno.equalsIgnoreCase("fustlded")||reportno.equalsIgnoreCase("cusmrgntrdsum")||reportno.equalsIgnoreCase("cuspledgetrdsum")||
				   reportno.equalsIgnoreCase("fustldednew")||reportno.equalsIgnoreCase("cusmrgntrdsumnew")||reportno.equalsIgnoreCase("cuspledgetrdsumnew"))
				{
					setting.setPaperSize(PaperSize.A3);// 设置A3纸
					if (pagewidth >= 1000) {
						setting.setScaleFactor(printscope3);
					} else {
						setting.setScaleFactor(100);
					}
				}else{
					setting.setPaperSize(PaperSize.A4);// 设置A4纸
					if (pagewidth >= 840) {
						setting.setScaleFactor(printscope4);
					} else {
						setting.setScaleFactor(100);
					}
				}if(reportno.equalsIgnoreCase("manysheets")||reportno.equalsIgnoreCase("manysheetsnew")){
					setting.setPaperSize(PaperSize.A3);// 设置A3纸
					if(i==0){
						Double sheet0wd = new Double(2829);
						sa3 = format.format(A3width / sheet0wd);
						printscope3 = (int) (Double.parseDouble(sa3) * 100);
						setting.setScaleFactor(printscope3);
					}else if(i == 1){
						Double sheet1wd = null;
						if(reportno.equalsIgnoreCase("manysheets")){
							sheet1wd = 	new Double(1946);
						}else{
							sheet1wd = 	new Double(2040);
						}
						sa3 = format.format(A3width / sheet1wd);
						printscope3 = (int) (Double.parseDouble(sa3) * 100);
						setting.setScaleFactor(printscope3);
					}else if(i == 2){
						Double sheet2wd = new Double(1272);
						sa3 = format.format(A3width / sheet2wd);
						printscope3 = (int) (Double.parseDouble(sa3) * 100);
						setting.setScaleFactor(printscope3);
					}
				}
				setting.setLeftMargin(new Double(0.5));// 左页边距
				setting.setBottomMargin(new Double(0.7));// 底页边距
				

				HeaderFooter hf = new HeaderFooter();
				setting.setFooter(hf); // 设置打印页眉
				Contents cl = hf.getRight();
				Contents cr = hf.getLeft();

				cr.append("制表人：");
				cr.append("");
				cr.append(tabtailinfo);
				cl.appendDate();
				cl.append(" ");
				cl.appendTime();
				cl.append("  ");
				cl.append("第 ");
				cl.appendPageNumber();
				cl.append(" 页, 共 ");
				cl.appendTotalPages();
				cl.append(" 页");
			}
			wbook.write();
			wbook.close();
			rwb.close();
		} catch (FileNotFoundException e) {
			CommUtil.writeLogfile("expRptToXls(03)"+e.getMessage());
			e.printStackTrace();
		} catch (BiffException e) {
			CommUtil.writeLogfile("setPrintscope(04)"+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			CommUtil.writeLogfile("setPrintscope(05)"+e.getMessage());
			e.printStackTrace();
		} catch (WriteException e) {
			CommUtil.writeLogfile("setPrintscope(06)"+e.getMessage());
			e.printStackTrace();
		}

		// 设置单元格格式为empty类型，导入金蝶数据程序用
		//以前：fustlded、manysheets、fustldednew、manysheetsnew、nobkcusfundioamtsumbkcusfundioamtsum
		if (reportno.equalsIgnoreCase("YYZX_DAILYSTL")||reportno.equalsIgnoreCase("YYZX_TDDAILYSTL")
				||reportno.equalsIgnoreCase("JSB_NOBFCUSFUNDIOSUM")||reportno.equalsIgnoreCase("JSB_BFCUSFUNDIOSUM")
				) {
			iReportManageDAOImpl.excelCellFormat(filepath);
		}
	}
	
	
	// 设置excel打印范围，及单元格类型为empty
	public void setPrintscopePoi(String filepath, String tabtailinfo,
			String xmlPath, String reportno) {
		SAXBuilder builder = null;
		Document document = null;

		builder = new SAXBuilder(false);

		File xmlfile = new File(xmlPath);
		try {
			Reader xmlStreamReader = new InputStreamReader(new FileInputStream(xmlfile), "utf-8");
			document = builder.build(xmlStreamReader);
		} catch (JDOMException e) {
			CommUtil.writeLogfile("setPrintscope(01)"+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			CommUtil.writeLogfile("setPrintscope(02)"+e.getMessage());
			e.printStackTrace();
		}

		Element root = document.getRootElement();// 获得根元素

		Double pagewidth = Double.parseDouble((root.getAttributeValue("pageWidth")));
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMaximumFractionDigits(2);
		Double A4width = new Double(840);
		Double A3width = new Double(1215);
		String sa4 = format.format(A4width / pagewidth);
		String sa3 = format.format(A3width / pagewidth);
		int printscope4 = (int) (Double.parseDouble(sa4) * 100);
		int printscope3 = (int) (Double.parseDouble(sa3) * 100);
		
		File excelFile = new File(filepath);
		Workbook wbs = null;
		FileOutputStream out = null;
		try {
			InputStream is = new FileInputStream(filepath);
			wbs = WorkbookFactory.create(is);

			// 设置所有的sheet的打印范围
			for (int i = 0; i < wbs.getNumberOfSheets(); i++) {
				Sheet rs = wbs.getSheetAt(i);
				PrintSetup setting = rs.getPrintSetup();
				if(reportno.equalsIgnoreCase("fustlded")||reportno.equalsIgnoreCase("cusmrgntrdsum")||reportno.equalsIgnoreCase("cuspledgetrdsum")||
				   reportno.equalsIgnoreCase("fustldednew")||reportno.equalsIgnoreCase("cusmrgntrdsumnew")||reportno.equalsIgnoreCase("cuspledgetrdsumnew"))
				{
					setting.setPaperSize(PrintSetup.A3_PAPERSIZE);// 设置A3纸
					if (pagewidth >= 1000) {
						setting.setScale((short)printscope3);
					} else {
						setting.setScale((short)100);
					}
				}else{
					setting.setPaperSize(PrintSetup.A4_PAPERSIZE);// 设置A4纸
					if (pagewidth >= 840) {
						setting.setScale((short)printscope4);
					} else {
						setting.setScale((short)100);
					}
				}if(reportno.equalsIgnoreCase("manysheets")||reportno.equalsIgnoreCase("manysheetsnew")){
					setting.setPaperSize(PrintSetup.A3_PAPERSIZE);// 设置A3纸
					if(i==0){
						Double sheet0wd = new Double(2829);
						sa3 = format.format(A3width / sheet0wd);
						printscope3 = (int) (Double.parseDouble(sa3) * 100);
						setting.setScale((short)printscope3);
					}else if(i == 1){
						Double sheet1wd = null;
						if(reportno.equalsIgnoreCase("manysheets")){
							sheet1wd = 	new Double(1946);
						}else{
							sheet1wd = 	new Double(2040);
						}
						sa3 = format.format(A3width / sheet1wd);
						printscope3 = (int) (Double.parseDouble(sa3) * 100);
						setting.setScale((short)printscope3);
					}else if(i == 2){
						Double sheet2wd = new Double(1272);
						sa3 = format.format(A3width / sheet2wd);
						printscope3 = (int) (Double.parseDouble(sa3) * 100);
						setting.setScale((short)printscope3);
					}
				}
				//rs.setMargin(Sheet.LeftMargin, new Double(0.2));// 左页边距
				rs.setMargin(Sheet.BottomMargin,new Double(0.3));// 底页边距
				
				// 设置打印页眉
				Footer footer = rs.getFooter();
				footer.setLeft("制表人:"+tabtailinfo);
				footer.setCenter(HSSFFooter.date()+" "+HSSFFooter.time());
				footer.setRight("第 "+HSSFFooter.page()+" 页, 共 "+HSSFFooter.numPages()+" 页");
			}
			is.close();
			out = new FileOutputStream(excelFile);
			wbs.write(out);
			out.close();
			out.flush();
		} catch (FileNotFoundException e) {
			CommUtil.writeLogfile("expRptToXls(03)"+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			CommUtil.writeLogfile("setPrintscope(05)"+e.getMessage());
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 设置单元格格式为empty类型，导入金蝶数据程序用
		//以前：fustlded、manysheets、fustldednew、manysheetsnew、nobkcusfundioamtsumbkcusfundioamtsum
		if (reportno.equalsIgnoreCase("YYZX_DAILYSTL")||reportno.equalsIgnoreCase("YYZX_TDDAILYSTL")
				||reportno.equalsIgnoreCase("JSB_NOBFCUSFUNDIOSUM")||reportno.equalsIgnoreCase("JSB_BFCUSFUNDIOSUM")
				) {
			iReportManageDAOImpl.excelCellFormat(filepath);
		}
	}

	// 查询日均权益
	public List quotedayequityall(String tradingday) throws DataAccessException {
		String str = "from VDayequityichart where 1=1 and tradingday = "
				+ tradingday + " and rownum <= 10 order by rank asc ";
		List list = getHibernateTemplate().find(str);
		if (list.size() == 0)
			return null;
		else
			return list;
	}
    
	public List getdynrptjxmlList(String reptId) throws DataAccessException {
		String hql = "from EbsVDynrptJxml t where reportid=? and ismain is null order by reportidjxml";
		List list = getHibernateTemplate().find(hql,reptId);
		if (list.size() == 0)
			return null;
		else
			return list;
	}
	
	public List getdynrptjxmlTmpList(String reptId) throws DataAccessException {
		String hql = "from EbsVDynrptJxmlTmp t where reportid=? and ismain is null order by reportidjxml";
		List list = getHibernateTemplate().find(hql,reptId);
		if (list.size() == 0)
			return null;
		else
			return list;
	}
	
	public ResultInfo saverptpara(String curruserid, String reqip,
			EbsDynrptBpara dynrptb, List<String[][]> dynrpt_hpara,String subprt,String sheetname,
			String jxmls,String mjxmls,String ismjxmls,List<String[][]> dynrptbatchparam,List<String[][]> dynrptprintparam,TreeMap pagesqlmap,
			EbsDynrptMsgpara dyncSendMsg,EbsDynrptImg ebsdynimg,List<String[]> dyncimgcols,List<String[]> shttnamelist,String dynbodys)
			throws DataAccessException {
		Connection conn = null;
		CallableStatement cstmt = null;
		ResultInfo reinfo = null;
		try {
			conn = getconn();
			cstmt = conn.prepareCall("{call pkg_dync_handle.up_upd_dynipt_para(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			cstmt.setString(1, curruserid);
			cstmt.setString(2, reqip);
			cstmt.setString(3, dynrptb.getReportid());
			cstmt.setString(4, dynrptb.getReportname());
			cstmt.setString(5, dynrptb.getFilename());
			cstmt.setString(6, dynrptb.getRptmaker());
			cstmt.setString(7, dynrptb.getRptmakedt());
			cstmt.setString(8, dynrptb.getRptversion());
			cstmt.setString(9, dynrptb.getRptremark());
			
			//批量导出参数
			String   rptexptype =""; 
			String   rptbatchpar1 =""; 
			String   rptbatchpar2 =""; 
			String   rptbatchpar3 =""; 
			String   rptbatchpar4 =""; 
			String   rptbatchpartype =""; 
			String   rptdefindname =""; 
			String   rptdefindtype =""; 
			
			for(int i=0;i<dynrptbatchparam.size();i++){
				rptexptype = rptexptype + dynrptbatchparam.get(i)[i][0]+"&";
				rptbatchpar1 = rptbatchpar1 + dynrptbatchparam.get(i)[i][1]+"&";
				rptbatchpar2 = rptbatchpar2 + dynrptbatchparam.get(i)[i][2]+"&";
				rptbatchpar3 = rptbatchpar3 + dynrptbatchparam.get(i)[i][3]+"&";
				rptbatchpar4 = rptbatchpar4 + dynrptbatchparam.get(i)[i][4]+"&";
				rptbatchpartype = rptbatchpartype + dynrptbatchparam.get(i)[i][5]+"&";
				rptdefindname = rptdefindname + dynrptbatchparam.get(i)[i][6]+"&";
				rptdefindtype = rptdefindtype + dynrptbatchparam.get(i)[i][7]+"&";
			}
			cstmt.setString(10, rptexptype);
			cstmt.setString(11, rptbatchpar1);
			cstmt.setString(12, rptbatchpar2);
			cstmt.setString(13, rptbatchpar3);
			cstmt.setString(14, rptbatchpar4);
			cstmt.setString(15, rptbatchpartype);
			cstmt.setString(16, rptdefindname);
			cstmt.setString(17, rptdefindtype);

		    //分页slq名称
		   String  sqlname  = "";    
		   String  pagecount = "";  
		   Iterator it = pagesqlmap.keySet().iterator();
		   while(it.hasNext()){
			   String key = (String)it.next();
			   sqlname = sqlname + key +"&";
			   pagecount = pagecount + (String)pagesqlmap.get(key)+"&";
		   }
			cstmt.setString(18, sqlname);
			cstmt.setString(19, pagecount);
			
		   //打印参数
		   String  printdirect ="";
		   String  printalign  =""; 
		   String  autowidth ="";  
		   String  pagefoot  ="";  
		   for(int i = 0;i< dynrptprintparam.size();i++){
			   String printdirecttmp = dynrptprintparam.get(i)[i][0];
			   String printaligntmp = dynrptprintparam.get(i)[i][1];
			   String pagefoottmp =  dynrptprintparam.get(i)[i][3];
			   if(printdirecttmp!= null)printdirecttmp = printdirecttmp.substring(0, 1);
			   printdirect = printdirect + printdirecttmp+"&";
			   if(printaligntmp!=null)printaligntmp = printaligntmp.substring(0, 1);
			   printalign = printalign + printaligntmp+"&";
			   autowidth = autowidth + dynrptprintparam.get(i)[i][2]+"&";
			   if(pagefoottmp != null)pagefoottmp = pagefoottmp.substring(0, 1);
			   pagefoot = pagefoot + pagefoottmp+"&";  
		   }

			cstmt.setString(20, printdirect);
			cstmt.setString(21, printalign);
			cstmt.setString(22, autowidth);
			cstmt.setString(23, pagefoot);
		   
			//查询条件
			String labelname = "";
			String iptctrlname = "";
			String iptctrltype = "";
			String iptctrldef = "";
			String iptctrllist = "";
			String iptisnull = "";
			String iptpardef = "";
			String iptreserve1 = "";
			String iptreserve2 = "";
			String iptreserve3 = "";
			for (int i = 0; i < dynrpt_hpara.size(); i++) {
				labelname = labelname + dynrpt_hpara.get(i)[i][0] + "&";
				iptctrlname = iptctrlname + (dynrpt_hpara.get(i)[i][1]).replace("$P{", "").replace("}", "") + "&";
				iptctrltype = iptctrltype + dynrpt_hpara.get(i)[i][2] + "&";
				iptctrldef = iptctrldef + dynrpt_hpara.get(i)[i][3] + "&";
				iptctrllist = iptctrllist + dynrpt_hpara.get(i)[i][4] + "&";
				iptisnull = iptisnull + dynrpt_hpara.get(i)[i][5] + "&";
				iptpardef = iptpardef +  dynrpt_hpara.get(i)[i][6] + "&";
			    iptreserve1 = iptreserve1 +  dynrpt_hpara.get(i)[i][7]+"&";
			    iptreserve2 = iptreserve2 + "&";
			    iptreserve3 = iptreserve3 + "&";
			}
			cstmt.setString(24, labelname);
			cstmt.setString(25, iptctrlname);
			cstmt.setString(26, iptctrltype);
			cstmt.setString(27, iptctrldef);
			cstmt.setString(28, iptctrllist);
			cstmt.setString(29, iptisnull);
			cstmt.setString(30, iptpardef);
			cstmt.setString(31, iptreserve1);
			cstmt.setString(32, iptreserve2);
			cstmt.setString(33, iptreserve3);
			
			cstmt.setString(34, subprt);
			cstmt.setString(35, sheetname);
			cstmt.setString(36, jxmls);
			cstmt.setString(37, mjxmls);
			cstmt.setString(38, ismjxmls);
			//导出txt、dbf
			cstmt.setString(39, dynrptb.getIsouttxt());
			cstmt.setString(40, dynrptb.getPixs());
			cstmt.setString(41, dynrptb.getEmptsperpix());
			cstmt.setString(42, dynrptb.getFildemps());
			//发送短信
			cstmt.setString(43, dyncSendMsg.getIssendmsg());
			cstmt.setString(44, dyncSendMsg.getAppid());
			cstmt.setString(45, dyncSendMsg.getUseid());
			cstmt.setString(46, dyncSendMsg.getUpwd());
			cstmt.setString(47, dyncSendMsg.getContent());
			
			//图 表
			cstmt.setString(48, dynrptb.getXlstype());
			cstmt.setString(49, ebsdynimg.getIsoutmg());
			cstmt.setString(50, ebsdynimg.getImgtype());
			cstmt.setString(51, ebsdynimg.getBeginrow());
			cstmt.setString(52, ebsdynimg.getEndrow());
			cstmt.setString(53, ebsdynimg.getXtitle());
			cstmt.setString(54, ebsdynimg.getYtitle());
			cstmt.setString(55, ebsdynimg.getFontsize());
			cstmt.setString(56, ebsdynimg.getBgcolor());
			cstmt.setString(57, ebsdynimg.getIstip());
			cstmt.setString(58, ebsdynimg.getIstext());
			cstmt.setString(59, ebsdynimg.getXcount());
			
			String fieldids = "";
			String fieldnames = "";
			String labelnames = "";
			String imgcolors = "";
			String borderwds = "";
			String imgcolshtidxs = "";
			for (int i = 0; i < dyncimgcols.size(); i++) {
				fieldnames = fieldnames + dyncimgcols.get(i)[0] + "&";
				labelnames = labelnames + dyncimgcols.get(i)[1] + "&";
				imgcolors = imgcolors + dyncimgcols.get(i)[2] + "&";
				borderwds = borderwds + dyncimgcols.get(i)[3] + "&";
				fieldids = fieldids + dyncimgcols.get(i)[4] + "&";
				imgcolshtidxs = imgcolshtidxs + dyncimgcols.get(i)[5] + "&";
				
			}
			cstmt.setString(60, fieldnames);
			cstmt.setString(61, labelnames);
			cstmt.setString(62, imgcolors);
			cstmt.setString(63, borderwds);
			cstmt.setString(64, fieldids);
			cstmt.setString(65, imgcolshtidxs);
			
			String sheetjxml = "";
			String jxmltname = "";
			String sheetindexs = "";
			for (int i = 0; i < shttnamelist.size(); i++) {
				sheetjxml = sheetjxml + shttnamelist.get(i)[0] + "&";
				jxmltname = jxmltname + shttnamelist.get(i)[1] + "&";
				sheetindexs = sheetindexs + shttnamelist.get(i)[2] + "&";
			}
			cstmt.setString(66, sheetjxml);
			cstmt.setString(67, jxmltname);
			cstmt.setString(68, sheetindexs);
			/*
			System.out.println(dynrptb.getXlstype());
			System.out.println(ebsdynimg.getIsoutmg());
			System.out.println(ebsdynimg.getImgtype());
			System.out.println(ebsdynimg.getBeginrow());
			System.out.println(ebsdynimg.getEndrow());
			System.out.println(ebsdynimg.getXtitle());
			System.out.println(ebsdynimg.getYtitle());
			System.out.println(ebsdynimg.getFontsize());
			System.out.println(ebsdynimg.getBgcolor());
			
			System.out.println(fieldnames);
			System.out.println(labelnames);
			System.out.println(imgcolors);
			System.out.println(borderwds);
			System.out.println(fieldids);
			System.out.println(sheetjxml);
			System.out.println(jxmltname);
			*/
			cstmt.setString(69, dynbodys);
			
			cstmt.setString(70, ebsdynimg.getIsshowimgtitle());
			cstmt.setString(71, ebsdynimg.getIsshowdatagrid());
			
			cstmt.registerOutParameter(72, Types.CHAR);
			cstmt.registerOutParameter(73, Types.CHAR);
			cstmt.executeUpdate();
			reinfo = ResultInfo.getInstance(cstmt.getString(72), cstmt.getString(73), "");
			//System.out.println(cstmt.getString(64)+":"+cstmt.getString(65));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (cstmt != null) {
					cstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return reinfo;
	}

	public EbsDynrptBpara getdynrptbpara(String table,String reportid)
			throws DataAccessException {
		Connection conn = null;
        ResultSet rs = null;
        List list = new ArrayList();
        
        EbsDynrptBpara dynbp = null;
        try {
            conn = getconn();
            String sqlStr = "select reportid, reportname,filename," +
            		"rptmaker,rptmakedt,rptversion,rptremark,nvl(isouttxt,'0'), pixs, emptsperpix, fildemps,nvl(xlstype,'xls') xlstype from "+table+" where reportid=?";
            PreparedStatement pstmt = conn.prepareStatement(sqlStr);
            //System.out.println(sqlStr+":"+reportid+":"+table);
            pstmt.setString(1, reportid);
            rs = pstmt.executeQuery();
            while(rs.next()){
            	dynbp = new EbsDynrptBpara();
            	dynbp.setReportid(rs.getString(1));
                dynbp.setReportname(rs.getString(2));
                dynbp.setFilename(rs.getString(3));
                dynbp.setRptmaker(rs.getString(4));
                dynbp.setRptmakedt(rs.getString(5));
                dynbp.setRptversion(rs.getString(6));
                dynbp.setRptremark(rs.getString(7));

                dynbp.setIsouttxt(rs.getString(8));
                dynbp.setPixs(rs.getString(9));
                dynbp.setEmptsperpix(rs.getString(10));
                dynbp.setFildemps(rs.getString(11));
                dynbp.setXlstype(rs.getString(12));
            	list.add(dynbp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        finally {
        	try {
                if (rs != null) {
                	rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (conn != null) {
                	conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
		if (list.size() == 0)
			return null;
		else
			return (EbsDynrptBpara) (list.get(0));
	}
	
	
	//实现getdynrptimg方法
	public EbsDynrptImg getdynrptimg(String table,String reportid)
	throws DataAccessException{
				Connection conn = null;
				ResultSet rs = null;
				List list = new ArrayList();

				EbsDynrptImg dynimg = null;
				try {
					conn = getconn();
					String sqlStr = "select reportid,isoutmg,imgtype,beginrow,endrow,xtitle,ytitle,bgcolor,fontsize,istip,istext from "+table+" where reportid=?";
					PreparedStatement pstmt = conn.prepareStatement(sqlStr);
					//System.out.println(sqlStr+":"+reportid+":"+table);
					pstmt.setString(1, reportid);
					rs = pstmt.executeQuery();
					while(rs.next()){
						dynimg = new EbsDynrptImg();//新建EbsDynrptImg对象dynimg
						dynimg.setReportid(rs.getString(1));
						dynimg.setIsoutmg(rs.getString(2));
						dynimg.setImgtype(rs.getString(3));
						dynimg.setBeginrow(rs.getString(4));
						dynimg.setEndrow(rs.getString(5));
						dynimg.setXtitle(rs.getString(6));
						dynimg.setYtitle(rs.getString(7));
						dynimg.setBgcolor(rs.getString(8));
						dynimg.setFontsize(rs.getString(9));
						dynimg.setIstip(rs.getString(10));
						dynimg.setIstext(rs.getString(11));
						list.add(dynimg);//将dynimg放入list
					}
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
				finally {
					try {
						if (rs != null) {
							rs.close();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						if (conn != null) {
							conn.close();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (list.size() == 0)
					return null;
				else
					return (EbsDynrptImg) (list.get(0));

	}
	
	//实现getdynrptimgcols方法，基本用不到
	public EbsDynrptImgcols getdynrptimgcols(String table,String reportid)
	throws DataAccessException{
			Connection conn = null;
			ResultSet rs = null;
			List list = new ArrayList();
			
			EbsDynrptImgcols dynimgcols = null;
			try {
				conn = getconn();
				String sqlStr = "select reportid, orderid," +
				"paramname,imgcolor,borderwd,labelname from "+table+" where reportid=?";
				PreparedStatement pstmt = conn.prepareStatement(sqlStr);
				//System.out.println(sqlStr+":"+reportid+":"+table);
				pstmt.setString(1, reportid);
				rs = pstmt.executeQuery();
				while(rs.next()){
					dynimgcols = new EbsDynrptImgcols();//新建EbsDynrptImgcols对象dynimgcols
					dynimgcols.setReportid(rs.getString(1));
					dynimgcols.setOrderid(rs.getString(2));
					dynimgcols.setParamname(rs.getString(3));
					dynimgcols.setImgcolor(rs.getString(4));
					dynimgcols.setBorderwd(rs.getString(5));
					dynimgcols.setLabelname(rs.getString(6));
					list.add(dynimgcols);//将dynimgcols放入list
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			finally {
				try {
					if (rs != null) {
						rs.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (list.size() == 0)
				return null;
			else
				return (EbsDynrptImgcols) (list.get(0));

	}
	
	//实现getdynrptprintimgcols方法，用于多行数据的临时存取
	public List getdynrptimgcolslist(String reportid)
	throws DataAccessException {
	Connection conn = null;
	ResultSet rs = null;
	List list = new ArrayList();
	EbsDynrptImgcols dynprintimgcols = null;
	try {
		conn = getconn();
		String sqlStr = "select reportid, orderid,paramname,labelname,imgcolor,borderwd from  ebs_dynrpt_img_cols where reportid=?";
	    PreparedStatement pstmt = conn.prepareStatement(sqlStr);
	    pstmt.setString(1, reportid);
	    rs = pstmt.executeQuery();
	    while(rs.next()){
	    	dynprintimgcols = new EbsDynrptImgcols();
	    	dynprintimgcols.setReportid(rs.getString(1));
	    	dynprintimgcols.setOrderid(rs.getString(2));
	    	dynprintimgcols.setParamname(rs.getString(3));
	    	dynprintimgcols.setLabelname(rs.getString(4));
	    	dynprintimgcols.setImgcolor(rs.getString(5));
	    	dynprintimgcols.setBorderwd(rs.getString(6));

	    	list.add(dynprintimgcols);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}
	finally {
		try {
	        if (rs != null) {
	        	rs.close();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    try {
	        if (conn != null) {
	        	conn.close();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	if (list.size() == 0)
		return null;
	else
		return list;
	}
	
	
	//保存图表参数到数据库
	public ResultInfo saveimgcond(String curruserid,EbsDynrptImg dynrptimg,EbsDynrptImgcols dynrptimgcols) throws DataAccessException {
		
		Connection connection = null;
        CallableStatement cstmt = null;
        ResultInfo reinfo = null;
        try {
        	connection = getconn();
			cstmt  = connection.prepareCall("{call pkg_dync_handle.up_upd_dynipt_img_tmp(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			cstmt.setString(1, curruserid);
			cstmt.setString(2, "edt");
			//ebs_dynrpt_img参数
			cstmt.setString(3, dynrptimg.getReportid());
			cstmt.setString(4, dynrptimg.getIsoutmg().substring(0, 1));
			cstmt.setString(5, dynrptimg.getImgtype().substring(0, 1));
			cstmt.setString(6, dynrptimg.getBeginrow());
			cstmt.setString(7, dynrptimg.getEndrow());
			cstmt.setString(8, dynrptimg.getXtitle());
			cstmt.setString(9, dynrptimg.getYtitle());
			cstmt.setString(10, dynrptimg.getBgcolor());
			cstmt.setString(11, dynrptimg.getFontsize());
			cstmt.setString(12, dynrptimg.getIstip().substring(0, 1));
			cstmt.setString(13, dynrptimg.getIstext().substring(0, 1));
			//ebs_dynrpt_img_cols参数
			cstmt.setString(14, dynrptimgcols.getOrderid());
			cstmt.setString(15, dynrptimgcols.getParamname());
			cstmt.setString(16, dynrptimgcols.getLabelname());
			cstmt.setString(17, dynrptimgcols.getImgcolor());
			cstmt.setString(18, dynrptimgcols.getBorderwd());
			
			cstmt.registerOutParameter(19, Types.CHAR);
			cstmt.registerOutParameter(20, Types.CHAR);
			cstmt.executeUpdate();
			reinfo =ResultInfo.getInstance(cstmt.getString(19),cstmt.getString(20),"");
		} catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        finally {
            try {
                if (cstmt != null) {
                    cstmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return reinfo;
	}
	
	public List getdynrptFileList(String table,String repId)throws DataAccessException{
		Connection conn = null;
        ResultSet rs = null;
        List list = new ArrayList();
        EbsDynrptFile dynfile = null;
        try {
            conn = getconn();
            String sqlStr = "select reportid, subreportid, sheetname from "+table+" where reportid=? order by subreportid asc";
            PreparedStatement pstmt = conn.prepareStatement(sqlStr);
            pstmt.setString(1, repId);
            rs = pstmt.executeQuery();
            while(rs.next()){
            	dynfile = new EbsDynrptFile();
            	dynfile.setReportid(rs.getString(1));
            	dynfile.setSubreportid(rs.getString(2));
                dynfile.setSheetname(rs.getString(3));
            	list.add(dynfile);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        finally {
        	try {
                if (rs != null) {
                	rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (conn != null) {
                	conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
		if (list.size() == 0)
			return null;
		else
			return list;
	};
	
	public List getdynrpthpara(String table,String reportid) throws DataAccessException {
		Connection conn = null;
        ResultSet rs = null;
        List list = new ArrayList();
        EbsDynrptHpara dynhp = null;
        try {
        	conn = getconn();
            String sqlStr = "select reportid, labelname, iptctrlname, iptctrltype, iptctrldef, iptctrllist, iptisnull, orderid ,iptpardef,iptreserve1,iptreserve2,iptreserve3 from "
            	            +table+" where reportid=? order by orderid asc";
            PreparedStatement pstmt = conn.prepareStatement(sqlStr);
            pstmt.setString(1, reportid);
            rs = pstmt.executeQuery();
            while(rs.next()){
            	dynhp = new EbsDynrptHpara();
            	dynhp.setReportid(rs.getString(1));
                dynhp.setLabelname(rs.getString(2));
                dynhp.setIptctrlname(rs.getString(3));
                dynhp.setIptctrltype(rs.getString(4));
                dynhp.setIptctrldef(rs.getString(5));
                dynhp.setIptctrllist(rs.getString(6));
                dynhp.setIptisnull(rs.getString(7));
                dynhp.setOrderid(rs.getInt(8));
                String Iptpardef=rs.getString(9);//参数默认值，可以支持sql
                if(Iptpardef == null)Iptpardef = "";
                if(Iptpardef.toLowerCase().indexOf("select")!=-1)
                	Iptpardef=CommUtil.getValueBySql(Iptpardef);
                //System.out.println(Iptpardef);
                dynhp.setIptpardef(Iptpardef);
                dynhp.setIptreserve1(rs.getString(10));
                dynhp.setIptreserve2(rs.getString(11));
                dynhp.setIptreserve3(rs.getString(12));
            	list.add(dynhp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        finally {
        	try {
                if (rs != null) {
                	rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (conn != null) {
                	conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
		if (list.size() == 0)
			return null;
		else
			return list;
	}
	
	//查询动态报表sql语句
	public List getdynrptSqlList(String repId) throws DataAccessException {
		String hql="from EbsDynrptTsql where reportid=?";
		List list = getHibernateTemplate().find(hql,repId);
		if (list.size() == 0)
			return null;
		else
			return list;
	}
	
	public List quoteRptSqlColIdList(String table, String rptId, String tname)
		throws DataAccessException {
		
		Connection conn = null;
        ResultSet rs = null;
        List list = new ArrayList();
        EbsDynrptSqlCol sqlcol = null;
        try {
        	conn = getconn();
            String sqlStr = "select reportid, table_name, column_name, data_type, data_length, data_precision, data_scale" +
            		        " from "+table+" where table_name=? ";
            PreparedStatement pstmt = conn.prepareStatement(sqlStr);
            pstmt.setString(1,(rptId+tname).toUpperCase());
            rs = pstmt.executeQuery();
            while(rs.next()){
            	sqlcol = new EbsDynrptSqlCol();
				sqlcol.setReportid(rs.getString(1));
				sqlcol.setTableName(rs.getString(2));
				sqlcol.setColumnName(rs.getString(3));
				String type=rs.getString(4);
				if(type.equalsIgnoreCase("NUMBER"))
					type = "java.math.BigDecimal";
				else
					type = "java.lang.String";
				sqlcol.setDataType(type);
            	list.add(sqlcol);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        finally {
        	try {
                if (rs != null) {
                	rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (conn != null) {
                	conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
		if (list.size() == 0)
			return null;
		else
			return list;
	}
	
	public List getdynrptComboxList(String combsql,String curruserid,String departId) throws DataAccessException {
		Connection conn = null;
		List list = new ArrayList();
		Combmaplist comb = null;
		String newcombsql=combsql;
		try{
		newcombsql=newcombsql.replaceAll("[$]P[{]CURRUSERID[}]", "'"+curruserid.toUpperCase()+"'");
		newcombsql=newcombsql.replaceAll("[$]P[{]CURRDEPCODE[}]", departId.toUpperCase());
		}catch(Exception e){
			
		}
		try {
			conn = getconn();
			PreparedStatement pstmt = conn.prepareStatement(newcombsql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				comb = new Combmaplist();
				String code = "";
				String name = "";
				String pcode = "";
				String lev = "";
				code = rs.getString(1);
				try {
					name = rs.getString(2);
				} catch (Exception e) {
					name = code;
				}
				try {
					pcode = rs.getString(3);
				} catch (Exception e) {
				}
				try {
					lev = rs.getString(4);
				} catch (Exception e) {
				}
				comb.setCode(code);
				comb.setName(name);
				comb.setPcode(pcode);
				comb.setLev(lev);
				comb.setFlag("");
				list.add(comb);
			}
		} catch (Exception e) {
			String[] combarrtmp =  newcombsql.split(";");
			for(int j=0;j<combarrtmp.length;j++){
				String[] combarr = combarrtmp[j].split(",");
					comb = new Combmaplist();
					comb.setCode(combarr[1]);
					comb.setName(combarr[0]);
					comb.setPcode("");
					comb.setLev("");
					comb.setFlag("");
					list.add(comb);
			
			}
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public ResultInfo SaveRptSqlColIdList(String rptId, String tname, String sql,
			String rankiptctrlname,String rankiptctrldef,String iptctrltype)
			throws DataAccessException {
		Connection connection = null;
		CallableStatement cstmt = null;
		ResultInfo reinfo = null;
		try {
			connection = getconn();
			Reader clobReader = new StringReader(sql); 
			
			cstmt = connection.prepareCall("{call Up_Save_Dynrpt_Sql_Cols(?,?,?,?,?,?,?,?)}");
			cstmt.setString(1, rptId);
			cstmt.setString(2, tname);
			cstmt.setCharacterStream(3, clobReader,sql.length());
			cstmt.setString(4, rankiptctrlname);
			cstmt.setString(5, rankiptctrldef);
			cstmt.setString(6, iptctrltype);
			cstmt.registerOutParameter(7, Types.CHAR);
			cstmt.registerOutParameter(8, Types.CHAR);
			cstmt.executeUpdate();
			reinfo = ResultInfo.getInstance(cstmt.getString(7), cstmt.getString(8), "");
		} catch (Exception e) {
			e.printStackTrace();
			return reinfo = ResultInfo.getInstance("1", "解析Sql语句出错！", "");
		} finally {
			try {
				if (cstmt != null) {
					cstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return reinfo;
	}
	
	public String combtree(List comblist) throws DataAccessException {
		/*List sortList = new ArrayList();
		for (int i = 0; i < comblist.size(); i++) {
			Combmaplist comb = (Combmaplist)comblist.get(i);
			String code = comb.getCode();
			String name = comb.getName();
			String pcode = comb.getPcode();
			String lev = comb.getLev();
			String flag="";
			int m=0;
			for (int j = 0; j < sortList.size(); j++) {
				Combmaplist comb1 = (Combmaplist)sortList.get(j);
				String code1 = comb1.getCode();
				String name1 = comb1.getName();
				String pcode1 = comb1.getPcode();
				String lev1 = comb1.getLev();
				if(code.equals(code1)&&pcode.equals(pcode1)&&lev.equals(lev1)){
					flag="1";
					break;
				}else if(pcode.equals(code1)&&!code.equals(code1)&&!lev.equals(lev1)){
					flag="2";
					m=j;
					break;
				}else if (code.equals(pcode1)&&!code.equals(code1)&&!lev.equals(lev1)){
					flag="3";
					break;
				}
			}
			if(flag.equals("")||flag.equals("3")){
				sortList.add(comb);
			}else if(flag.equals("2")){
				sortList.add(m+1, comb);
			}
		}*/
		String str="";
		for (int m = 0; m < comblist.size(); m++) {
			String code = ((Combmaplist)comblist.get(m)).getCode();
			String pcode = ((Combmaplist)comblist.get(m)).getPcode();
			String name = ((Combmaplist)comblist.get(m)).getName();
			String lev = ((Combmaplist)comblist.get(m)).getLev();
			String flag = ((Combmaplist)comblist.get(m)).getFlag();
			if(lev.equalsIgnoreCase("1")){
				str=str+"\n"+"{text: '"+name+"',id: '"+code+"',";
				String tmp="";
				String str1="";
				for (int n = m+1; n < comblist.size(); n++) {
					String code1 =  ((Combmaplist)comblist.get(n)).getCode();
					String pcode1 =  ((Combmaplist)comblist.get(n)).getPcode();
					String name1 =  ((Combmaplist)comblist.get(n)).getName();
					String lev1 =  ((Combmaplist)comblist.get(n)).getLev();
					String flag1 =  ((Combmaplist)comblist.get(n)).getFlag();
					if(lev1.equalsIgnoreCase("2")&&(!(code.equals(code1)&&pcode.equals(pcode1)&&lev.equals(lev1))&&code.equals(pcode1))){
						tmp="1";
						str1=str1+"\n"+"{text: '"+name1+"',id: '"+code1+"',";
						
						String tmp2="";
						String str2="";
						
						for (int k = n+1; k < comblist.size(); k++) {
							String code2 =  ((Combmaplist)comblist.get(k)).getCode();
							String pcode2 =  ((Combmaplist)comblist.get(k)).getPcode();
							String name2 =  ((Combmaplist)comblist.get(k)).getName();
							String lev2 =  ((Combmaplist)comblist.get(k)).getLev();
							String flag2 =  ((Combmaplist)comblist.get(k)).getFlag();
							if(lev2.equalsIgnoreCase("3")&&(!(code1.equals(code2)&&pcode1.equals(pcode2)&&lev1.equals(lev2))&&code1.equals(pcode2))){
								tmp2="1";
								str2=str2+"\n"+"{text: '"+name2+"',id: '"+code2+"'},";
							}
						}
						if(!tmp2.equals("")) {
							if(str2.length()>0)
								str2=str2.substring(0,str2.length()-1);
							str1 = str1+"children:["+str2+"]";
						}
						str1 = str1+"},";
					}
				}
				if(!tmp.equals(""))
				{
					if(str1.length()>0)
						str1=str1.substring(0,str1.length()-1);
					str=str+"children:["+str1+"]";
				}
				if(!str.equals(""))
					str=str+"},";
			}
		}
		if(str.substring(str.length()-1, str.length()).equalsIgnoreCase(","))
			str=str.substring(0,str.length()-1);
		//System.out.println(str);
		return str;
	}
	public String covtreestr(String code,String name,String pcode,String lev,int m,List comblist) throws DataAccessException {
		String tmp="";
		for (int i = m; i < comblist.size(); i++) {
			Combmaplist comb = (Combmaplist)comblist.get(i);
			String currpcode = comb.getPcode();
			String currname = comb.getName();
			String currcode = comb.getCode();
			String currlev = comb.getLev();
			String flag = comb.getFlag();
			if(!flag.equalsIgnoreCase("")) continue;
			if(!(code.equals(currcode)&&pcode.equals(currpcode)&&lev.equals(currlev))&&code.equals(currpcode))
			{
				tmp="{text: '"+currname+"',id: '"+currcode+"'";
				treestr=treestr+tmp+",";
				((Combmaplist)comblist.get(i)).setFlag("1");
				String tmp2=covtreestr(currcode,currname,currpcode,currlev,i+1,comblist);
				if(!tmp2.equals(""))
					treestr="children: ["+treestr;
			}
		}
		
		return tmp;
	}

	//动态报表设置打印格式
	public void setPrintPage(String filepath,String tableinfo,List dynrptPrintparalist,String xmlpath,String reportno) throws Exception
	{
		SAXBuilder builder = null;
		Document document = null;

		builder = new SAXBuilder(false);

		File xmlfile = new File(xmlpath);
		try {
			Reader xmlStreamReader = new InputStreamReader(new FileInputStream(xmlfile), "utf-8");
			document = builder.build(xmlStreamReader);
		} catch (JDOMException e) {
			CommUtil.writeLogfile("setPrintPage(01)"+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			CommUtil.writeLogfile("setPrintPage(01)"+e.getMessage());
			e.printStackTrace();
		}
		Element root = document.getRootElement();// 获得根元素
		Double pagewidth = Double.parseDouble((root.getAttributeValue("pageWidth")));
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMaximumFractionDigits(2);
		Double A4width = null;
		Double A3width = null;
		
		jxl.Workbook rwb = null;
		WritableWorkbook wbook = null;
		try {
			InputStream is = new FileInputStream(filepath);
			rwb = jxl.Workbook.getWorkbook(is);
			wbook = jxl.Workbook.createWorkbook(new File(filepath), rwb);
			// 设置所有的sheet的打印范围
			for (int i = 0; i < wbook.getSheets().length; i++) {
				jxl.Sheet rs =wbook.getSheet(i);
				jxl.SheetSettings setting = rs.getSettings();
				if(dynrptPrintparalist != null && dynrptPrintparalist.size() > 0){
					
					EbsDynrptPrintpara dynrptPrintpara= (EbsDynrptPrintpara)dynrptPrintparalist.get(0);
					
					int printscope4 = 0;
					int printscope3 = 0;
					//设置打印方向
					//纵向、A4/A3
					if(printone.equals(dynrptPrintpara.getPrintdirect())){
						setting.setOrientation(PageOrientation.PORTRAIT);// 纵向
						
						if(pagewidth<700){
				        	A4width = new Double(430);	
				        }else if(pagewidth>=700 && pagewidth<1000){
				        	A4width = new Double(480);	
				        }else{
				        	A4width = new Double(500);	
				        }
				        
				        if(pagewidth<2000){
				        	A3width = new Double(760);
				        }else{
				        	A3width = new Double(790);
				        }
						String sa4 = format.format(A4width / pagewidth);
						String sa3 = format.format(A3width / pagewidth);
						if(pagewidth<A4width){
							printscope4 = 100;
						}else{
							printscope4 = (int) (Double.parseDouble(sa4) * 100);
						}
						
						if(pagewidth<A3width){
							printscope3 = 100;
						}else{
							printscope3 = (int) (Double.parseDouble(sa3) * 100);
						}

					//横向、A4\A3	
					}else if(printtwo.equals(dynrptPrintpara.getPrintdirect())){
						setting.setOrientation(PageOrientation.LANDSCAPE);; // 横向 
						if(pagewidth<700){
				        	A4width = new Double(630);	
				        }else if(pagewidth>=700 && pagewidth<1000){
				        	A4width = new Double(680);	
				        }else{
				        	A4width = new Double(760);	
				        }
				        
				        if(pagewidth<2000){
				        	A3width = new Double(1150);
				        }else{
				        	A3width = new Double(1040);
				        }
						String sa4 = format.format(A4width / pagewidth);
						String sa3 = format.format(A3width / pagewidth);
						
						if(pagewidth<A4width){
							printscope4 = 100;
						}else{
							printscope4 = (int) (Double.parseDouble(sa4) * 100);
						}
						
						if(pagewidth<A3width){
							printscope3 = 100;
						}else{
							printscope3 = (int) (Double.parseDouble(sa3) * 100);
						}
					}
					
					// 设置纸张类型
					if("A3".equals(dynrptPrintpara.getAutowidth())){
						setting.setPaperSize(PaperSize.A3);
						setting.setScaleFactor(printscope3);//设置打印比例
					}else if("A4".equals(dynrptPrintpara.getAutowidth())){
						setting.setPaperSize(PaperSize.A4);
						setting.setScaleFactor(printscope4);//设置打印比例
					}

					//居中方式
					if(printone.equals(dynrptPrintpara.getPrintalign())){
						setting.setHorizontalCentre(true);//水平居中
					}else if(printtwo.equals(dynrptPrintpara.getPrintalign())){
						setting.setVerticalCentre(true);//垂直居中
					}else if(printthree.equals(dynrptPrintpara.getPrintalign())){
						setting.setHorizontalCentre(true);//水平居中
						setting.setVerticalCentre(true);//垂直居中
					}
					setting.setLeftMargin(new Double(0.5));// 左页边距
					setting.setBottomMargin(new Double(0.7));// 底页边距
					
					if(printone.equals(dynrptPrintpara.getPagefoot())){
						
						HeaderFooter hf = new HeaderFooter();
						setting.setFooter(hf); // 设置打印页眉
						Contents cl = hf.getRight();
						Contents cr = hf.getLeft();
						cr.append("制表人：");
						cr.append("");
						cr.append(tableinfo);
						cl.appendDate();
						cl.append(" ");
						cl.appendTime();
						cl.append("  ");
						cl.append("第 ");
						cl.appendPageNumber();
						cl.append(" 页, 共 ");
						cl.appendTotalPages();
						cl.append(" 页");
					}
				}
			}
			wbook.write();
			wbook.close();
			rwb.close();
		} catch (FileNotFoundException e) {
			CommUtil.writeLogfile("setPrintPage(03)"+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			CommUtil.writeLogfile("setPrintPage(05)"+e.getMessage());
			e.printStackTrace();
		} catch (WriteException e) {
			CommUtil.writeLogfile("setPrintPage(06)"+e.getMessage());
			e.printStackTrace();
		}
		
		// 设置单元格格式为empty类型，导入金蝶数据程序用
		if (reportno.equalsIgnoreCase("DCTRADECURRDAYREPORT")||reportno.equalsIgnoreCase("DCTRADEHISDAYREPORT")
			||reportno.equalsIgnoreCase("DCTRADECURRDAYREPORTNEW")||reportno.equalsIgnoreCase("DCTRADEHISDAYREPORTNEW")
			||reportno.equalsIgnoreCase("JSB_DAILYSTL")||reportno.equalsIgnoreCase("JSB_TDDAILYSTL")
			||reportno.equalsIgnoreCase("JSB_DAILYSTLCFFEX")||reportno.equalsIgnoreCase("JSB_DAILYSTLCFFEX")
			||reportno.equalsIgnoreCase("JSB_TDDAILYSTL")||reportno.equalsIgnoreCase("JSB_TDDAILYSTL")
			||reportno.equalsIgnoreCase("JSB_TDDAILYSTLCFFEX")||reportno.equalsIgnoreCase("JSB_TDDAILYSTLCFFEX")
			||reportno.equalsIgnoreCase("JSB_DAILYSTL2013CFFEX")
			||reportno.equalsIgnoreCase("JSB_NOBFCUSFUNDIOSUM")||reportno.equalsIgnoreCase("JSB_BFCUSFUNDIOSUM")) {
		    iReportManageDAOImpl.excelCellFormat(filepath);
		}
	}
	
	
	//动态报表设置打印格式
	public void setPrintPagePoi(String filepath,String tableinfo,List dynrptPrintparalist,String xmlpath,String reportno) throws Exception
	{
		SAXBuilder builder = null;
		Document document = null;

		builder = new SAXBuilder(false);

		File xmlfile = new File(xmlpath);
		try {
			Reader xmlStreamReader = new InputStreamReader(new FileInputStream(xmlfile), "utf-8");
			document = builder.build(xmlStreamReader);
		} catch (JDOMException e) {
			CommUtil.writeLogfile("setPrintPage(01)"+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			CommUtil.writeLogfile("setPrintPage(01)"+e.getMessage());
			e.printStackTrace();
		}
		Element root = document.getRootElement();// 获得根元素
		Double pagewidth = Double.parseDouble((root.getAttributeValue("pageWidth")));
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMaximumFractionDigits(2);
		Double A4width = null;
		Double A3width = null;
		
		File excelFile = new File(filepath);
		Workbook wbs = null;
		FileOutputStream out = null;
		try {
			FileInputStream is = new FileInputStream(excelFile);
			wbs = WorkbookFactory.create(is);
			// 设置所有的sheet的打印范围
			for (int i = 0; i < wbs.getNumberOfSheets(); i++) {
				Sheet rs =wbs.getSheetAt(i);
				PrintSetup setting = rs.getPrintSetup();
				if(dynrptPrintparalist != null && dynrptPrintparalist.size() > 0){
					
					EbsDynrptPrintpara dynrptPrintpara= (EbsDynrptPrintpara)dynrptPrintparalist.get(0);
					
					int printscope4 = 0;
					int printscope3 = 0;
					//设置打印方向 (true：横向，false：纵向  )
					//纵向、A4/A3
					if(printone.equals(dynrptPrintpara.getPrintdirect())){
						setting.setLandscape(false);//纵向 
						
						if(pagewidth<700){
				        	A4width = new Double(430);	
				        }else if(pagewidth>=700 && pagewidth<1000){
				        	A4width = new Double(480);	
				        }else{
				        	A4width = new Double(500);	
				        }
				        
				        if(pagewidth<2000){
				        	A3width = new Double(760);
				        }else{
				        	A3width = new Double(790);
				        }
						String sa4 = format.format(A4width / pagewidth);
						String sa3 = format.format(A3width / pagewidth);
						if(pagewidth<A4width){
							printscope4 = 100;
						}else{
							printscope4 = (int) (Double.parseDouble(sa4) * 100);
						}
						
						if(pagewidth<A3width){
							printscope3 = 100;
						}else{
							printscope3 = (int) (Double.parseDouble(sa3) * 100);
						}

					//横向、A4\A3	
					}else if(printtwo.equals(dynrptPrintpara.getPrintdirect())){
						setting.setLandscape(true); // 横向 
						if(pagewidth<700){
				        	A4width = new Double(630);	
				        }else if(pagewidth>=700 && pagewidth<1000){
				        	A4width = new Double(680);	
				        }else{
				        	A4width = new Double(760);	
				        }
				        
				        if(pagewidth<2000){
				        	A3width = new Double(1150);
				        }else{
				        	A3width = new Double(1040);
				        }
						String sa4 = format.format(A4width / pagewidth);
						String sa3 = format.format(A3width / pagewidth);
						
						if(pagewidth<A4width){
							printscope4 = 100;
						}else{
							printscope4 = (int) (Double.parseDouble(sa4) * 100);
						}
						
						if(pagewidth<A3width){
							printscope3 = 100;
						}else{
							printscope3 = (int) (Double.parseDouble(sa3) * 100);
						}
					}
					
					// 设置纸张类型
					if("A3".equals(dynrptPrintpara.getAutowidth())){
						setting.setPaperSize(PrintSetup.A3_PAPERSIZE);
						setting.setScale((short) printscope3);//设置打印比例
					}else if("A4".equals(dynrptPrintpara.getAutowidth())){
						setting.setPaperSize(PrintSetup.A4_PAPERSIZE);
						setting.setScale((short) printscope4);//设置打印比例
					}

					//居中方式
					if(printone.equals(dynrptPrintpara.getPrintalign())){
						rs.setHorizontallyCenter(true);//水平居中
					}else if(printtwo.equals(dynrptPrintpara.getPrintalign())){
						rs.setVerticallyCenter(true);//垂直居中
					}else if(printthree.equals(dynrptPrintpara.getPrintalign())){
						rs.setHorizontallyCenter(true);//水平居中
						rs.setVerticallyCenter(true);//垂直居中
					}
					//rs.setMargin(Sheet.LeftMargin, new Double(0.2));// 左页边距
					rs.setMargin(Sheet.BottomMargin,new Double(0.3));// 底页边距
					
					if(printone.equals(dynrptPrintpara.getPagefoot())){
						// 设置打印页眉
						Footer footer = rs.getFooter();
						footer.setLeft("制表人:"+tableinfo);
						footer.setCenter(HSSFFooter.date()+" "+HSSFFooter.time());
						footer.setRight("第 "+HSSFFooter.page()+" 页, 共 "+HSSFFooter.numPages()+" 页");
					}
				}
			}
			is.close();
			out = new FileOutputStream(excelFile);
			wbs.write(out);
			out.close();
			out.flush();
		} catch (FileNotFoundException e) {
			CommUtil.writeLogfile("setPrintPage(03)"+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			CommUtil.writeLogfile("setPrintPage(05)"+e.getMessage());
			e.printStackTrace();
		} 
		
		// 设置单元格格式为empty类型，导入金蝶数据程序用
		if (reportno.equalsIgnoreCase("DCTRADECURRDAYREPORT")||reportno.equalsIgnoreCase("DCTRADEHISDAYREPORT")
			||reportno.equalsIgnoreCase("DCTRADECURRDAYREPORTNEW")||reportno.equalsIgnoreCase("DCTRADEHISDAYREPORTNEW")
			||reportno.equalsIgnoreCase("JSB_DAILYSTL")||reportno.equalsIgnoreCase("JSB_TDDAILYSTL")
			||reportno.equalsIgnoreCase("JSB_DAILYSTLCFFEX")||reportno.equalsIgnoreCase("JSB_DAILYSTLCFFEX")
			||reportno.equalsIgnoreCase("JSB_TDDAILYSTL")||reportno.equalsIgnoreCase("JSB_TDDAILYSTL")
			||reportno.equalsIgnoreCase("JSB_TDDAILYSTLCFFEX")||reportno.equalsIgnoreCase("JSB_TDDAILYSTLCFFEX")
			||reportno.equalsIgnoreCase("JSB_DAILYSTL2013CFFEX")
			||reportno.equalsIgnoreCase("JSB_NOBFCUSFUNDIOSUM")||reportno.equalsIgnoreCase("JSB_BFCUSFUNDIOSUM")) {
		    iReportManageDAOImpl.excelCellFormat(filepath);
		}
	}
	
	//修改报表参数
	public ResultInfo editdynrptBpara(String curruserid,
			EbsDynrptBpara dynrptBpara) throws DataAccessException {
		
		Connection connection = null;
        CallableStatement cstmt = null;
        ResultInfo reinfo = null;
        try {
        	connection = getconn();
			cstmt  = connection.prepareCall("{call pkg_dync_handle.up_upd_dynrptBpara(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			cstmt.setString(1, curruserid);
			cstmt.setString(2, "edt");
			cstmt.setString(3, dynrptBpara.getReportid());
			cstmt.setString(4, dynrptBpara.getReportname());
			cstmt.setString(5, dynrptBpara.getRptmaker());
			cstmt.setString(6, dynrptBpara.getRptversion());
			cstmt.setString(7, dynrptBpara.getRptremark());
			
			cstmt.setString(8, dynrptBpara.getIsouttxt());
			cstmt.setString(9, dynrptBpara.getPixs());
			cstmt.setString(10, dynrptBpara.getEmptsperpix());
			cstmt.setString(11, dynrptBpara.getFildemps());
			cstmt.setString(12, dynrptBpara.getXlstype());
			
			cstmt.registerOutParameter(13, Types.CHAR);
			cstmt.registerOutParameter(14, Types.CHAR);
			cstmt.executeUpdate();
			reinfo =ResultInfo.getInstance(cstmt.getString(13),cstmt.getString(14),"");
		} catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        finally {
            try {
                if (cstmt != null) {
                    cstmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return reinfo;
	}

	//重新编译excel文件
	public void compileExcel(String path,List<String> writecontents,String repid,String rptname,String repmarker,String rptversion,String rptremark,String writetyle,
			String isoutTxt,String pixs,String emptsperpix,String fildemps,String xlstype,EbsDynrptMsgpara dynrptSendMsgpara,EbsDynrptImg dynrptimg) {
		File excelFile = new File(path);
		Workbook workbook = null;
		try {
			workbook = WorkbookFactory.create(new FileInputStream(excelFile));
			Sheet sheet = workbook.getSheet("${设置}");
			if("".equals(writetyle)){
				sheet.getRow(6).getCell(3).setCellValue(repid);//报表名称(英文)
				sheet.getRow(6).getCell(7).setCellValue(CommUtil.getDate2());//制作日期
				sheet.getRow(7).getCell(3).setCellValue(repmarker);//制作人
				sheet.getRow(7).getCell(7).setCellValue(rptremark);//说明
				sheet.getRow(8).getCell(3).setCellValue(rptversion);//版本号
				sheet.getRow(12).getCell(3).setCellValue(rptname);//报表标题
				
				try{
					sheet.getRow(6).getCell(11).setCellValue(xlstype);
				}catch(Exception e){
					Row row = sheet.getRow(6);
					row.createCell(16);
					sheet.getRow(6).getCell(11).setCellValue(xlstype);
				}
				
				//是否导出txt(1:txt 0:否 2:dbf 3:txt、dbf)
				try{
					sheet.getRow(53).getCell(16).setCellValue(isoutTxt);
				}catch(Exception e){
					Row row = sheet.getRow(53);
					row.createCell(16);
					sheet.getRow(53).getCell(16).setCellValue(isoutTxt);
				}
				//像素
				try{
					sheet.getRow(52).getCell(14).setCellValue(pixs);
				}catch(Exception e){
					Row row = sheet.getRow(52);
					row.createCell(14);
					sheet.getRow(52).getCell(14).setCellValue(pixs);
				}
				//每像素空格数
				try{
					sheet.getRow(52).getCell(15).setCellValue(emptsperpix);
				}catch(Exception e){
					Row row = sheet.getRow(52);
					row.createCell(15);
					sheet.getRow(52).getCell(15).setCellValue(emptsperpix);
				}
				//字段间空格数
				try{
					sheet.getRow(52).getCell(16).setCellValue(fildemps);
				}catch(Exception e){
					Row row = sheet.getRow(52);
					row.createCell(16);
					sheet.getRow(52).getCell(16).setCellValue(fildemps);
				}
				//短信
				try{
					sheet.getRow(62).getCell(16).setCellValue(dynrptSendMsgpara.getIssendmsg());
				}catch(Exception e){
					Row row = sheet.getRow(62);
					row.createCell(16);
					sheet.getRow(62).getCell(16).setCellValue(dynrptSendMsgpara.getIssendmsg());
				}
				try{
					sheet.getRow(58).getCell(15).setCellValue(dynrptSendMsgpara.getUseid());
				}catch(Exception e){
					Row row = sheet.getRow(58);
					row.createCell(15);
					sheet.getRow(58).getCell(15).setCellValue(dynrptSendMsgpara.getUseid());
				}
				try{
					sheet.getRow(59).getCell(15).setCellValue(dynrptSendMsgpara.getUpwd());
				}catch(Exception e){
					Row row = sheet.getRow(59);
					row.createCell(15);
					sheet.getRow(59).getCell(15).setCellValue(dynrptSendMsgpara.getUpwd());
				}
				try{
					sheet.getRow(60).getCell(15).setCellValue(dynrptSendMsgpara.getAppid());
				}catch(Exception e){
					Row row = sheet.getRow(60);
					row.createCell(15);
					sheet.getRow(60).getCell(15).setCellValue(dynrptSendMsgpara.getAppid());
				}
				try{
					sheet.getRow(61).getCell(15).setCellValue(dynrptSendMsgpara.getContent());
				}catch(Exception e){
					Row row = sheet.getRow(61);
					row.createCell(15);
					sheet.getRow(61).getCell(15).setCellValue(dynrptSendMsgpara.getContent());
				}
			}else if(pagecond.equals(writetyle)){
				 //清空分页参数
				for(int row=38;row<47;row++){
					for(int col=15;col<17;col++){
						sheet.getRow(row).getCell(col).setCellValue("");//清空报表查询条件
					}
				}
				int rownum = 38;
				//分页参数
				for(int i=0;i<writecontents.size();i++){
					String printcond = writecontents.get(i);
					String []printcondarr = printcond.split("@@");
					int t=14;
					for(int m=0;m<printcondarr.length;m++){
						sheet.getRow(rownum).getCell(t).setCellValue(printcondarr[m]);//报表分页参数
						t++;
					}
					rownum ++;
				}
				
			}else if(printscope.equals(writetyle)){
				 //清空打印参数
				for(int row=52;row<61;row++){
					for(int col=3;col<7;col++){
						sheet.getRow(row).getCell(col).setCellValue("");//清空报表查询条件
					}
				}
				int rownum = 52;
				//打印参数
				for(int i=0;i<writecontents.size();i++){
					String printcond = writecontents.get(i);
					String []printcondarr = printcond.split("@@");
					int k=2;
					for(int j=0;j<printcondarr.length;j++){
						sheet.getRow(rownum).getCell(k).setCellValue(printcondarr[j]);//报表打印参数
						k++;
					}
					rownum ++;
				}
			}else if(querycond.equals(writetyle) ||datasql.equals(writetyle)){
				if(querycond.equals(writetyle)){
					for(int row=14;row<33;row++){
						for(int col=3;col<8;col++){
							sheet.getRow(row).getCell(col).setCellValue("");//清空报表查询条件
						}
					}
				}else if(datasql.equals(writetyle)){
					for(int row=14;row<33;row++){
						for(int col=15;col<8;col++){
							sheet.getRow(row).getCell(col).setCellValue("");//清空报表sql语句
						}
					}
				}
				
				int rownum = 14;
				//查询条件
				for(int i=0;i<writecontents.size();i++){
					String queryconds = writecontents.get(i);
					String []querycondarr = queryconds.split("@@");
					int k = 0;
					if(querycond.equals(writetyle)){
						k = 3;
					}else if(datasql.equals(writetyle)){
					    k = 15;
					}
					for(int m=0;m<querycondarr.length;m++){
						sheet.getRow(rownum).getCell(k).setCellValue(querycondarr[m]);//报表查询条件
						k++;
					}
					rownum ++;
				}
			}
			
			//图表参数修改
			else if(imgcond.equals(writetyle)){
				sheet.getRow(65).getCell(4).setCellValue(dynrptimg.getIsoutmg());
				sheet.getRow(66).getCell(3).setCellValue(dynrptimg.getImgtype());
				sheet.getRow(67).getCell(3).setCellValue(dynrptimg.getBeginrow());
				sheet.getRow(68).getCell(3).setCellValue(dynrptimg.getEndrow());
				sheet.getRow(69).getCell(3).setCellValue(dynrptimg.getXtitle());
				sheet.getRow(70).getCell(3).setCellValue(dynrptimg.getYtitle());
				sheet.getRow(71).getCell(3).setCellValue(dynrptimg.getBgcolor());
				sheet.getRow(72).getCell(3).setCellValue(dynrptimg.getFontsize());
				sheet.getRow(73).getCell(3).setCellValue(dynrptimg.getIstip());
				sheet.getRow(74).getCell(3).setCellValue(dynrptimg.getIstext());
				for(int row=66;row<81;row++){
					for(int col=7;col<11;col++){
						sheet.getRow(row).getCell(col).setCellValue("");//清空图表cols参数
					}
				}
				//修改图表cols参数
				int rownum = 66;//从第67行开始
				for(int i=0;i<writecontents.size();i++){							
					String imgcolscond = writecontents.get(i);
					String []imgcolscondarr = imgcolscond.split("@@");//
					for(int j=1;j<imgcolscondarr.length;j++){				//只需修改4个参数,orderid不需要,去掉i=0
						sheet.getRow(rownum).getCell(j+6).setCellValue(imgcolscondarr[j]);//图表cols参数
					}
					rownum ++;
				}
			}
			//写入文件
			FileOutputStream  outexcel = new FileOutputStream(excelFile);
			workbook.write(outexcel);
			outexcel.flush();
			outexcel.close();
			workbook=null;
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
   //保存查询条件到临时表
	public ResultInfo saverptparatmp(String curruserid, String reqid,
			String qhlabelname, String qhiptctrlname, String qhiptctrltype,
			String qhiptctrldef, String qhiptctrllist, String qhiptisnull,String qhiptpardef)
			throws DataAccessException {
		Connection connection = null;
		CallableStatement cstmt = null;
		ResultInfo reinfo = null;
		try {
			connection = getconn();
			cstmt = connection
					.prepareCall("{call pkg_dync_handle.up_upd_dynipt_para_tmp(?,?,?,?,?,?,?,?,?,?,?,?)}");
			cstmt.setString(1, curruserid);
			cstmt.setString(2, "edit");
			cstmt.setString(3, reqid);
			cstmt.setString(4, qhlabelname);
			cstmt.setString(5, qhiptctrlname);
			cstmt.setString(6, qhiptctrltype);
			cstmt.setString(7, qhiptctrldef);
			cstmt.setString(8, qhiptctrllist);
			cstmt.setString(9, qhiptisnull);
			cstmt.setString(10, qhiptpardef);
			cstmt.registerOutParameter(11, Types.CHAR);
			cstmt.registerOutParameter(12, Types.CHAR);
			cstmt.executeUpdate();
			reinfo = ResultInfo.getInstance(cstmt.getString(11), cstmt
					.getString(12), "");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (cstmt != null) {
					cstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return reinfo;
	}
	//保存查询sql到临时表
	public ResultInfo saverptsqltmp(String curruserid, String reqid,
			String tname, String sqlstr) throws DataAccessException {
		Connection connection = null;
		CallableStatement cstmt = null;
		ResultInfo reinfo = null;
		try {
			connection = getconn();
			cstmt = connection
					.prepareCall("{call pkg_dync_handle.up_upd_dynipt_tsql_tmp(?,?,?,?,?,?,?)}");
			Reader clobReader = new StringReader(sqlstr); 
			cstmt.setString(1, curruserid);
			cstmt.setString(2, "edit");
			cstmt.setString(3, reqid);
			cstmt.setString(4, tname);
			cstmt.setCharacterStream(5, clobReader,sqlstr.length());
			cstmt.registerOutParameter(6, Types.CHAR);
			cstmt.registerOutParameter(7, Types.CHAR);
			cstmt.executeUpdate();
			reinfo = ResultInfo.getInstance(cstmt.getString(6), cstmt
					.getString(7), "");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (cstmt != null) {
					cstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return reinfo;
	}

	//按日期、营业部批量导出excel文件
	public String[] batchExpExcel(HttpServletRequest request,
			HttpServletResponse response, Map parameter, String reportFile,
			String title, String type,List<String []> datelist,List deplist,String begindate,String enddate,String batchparam) throws Exception {
		String fileNames [] = null;
		if(deplist.size() == 0){
			fileNames = new String[datelist.size()];
		}else{
			fileNames = new String[datelist.size()* deplist.size()];
		}
        int index = 0;
		for(int i=0;i<datelist.size();i++){
			if(deplist.size() == 0){
				parameter.put(begindate, datelist.get(i)[0]);
				parameter.put(enddate, datelist.get(i)[1]);
				if(index<=datelist.size())
					fileNames[index] = expBatchRpt(request, response, parameter, reportFile, title, type,datelist.get(i)[0],datelist.get(i)[1],"");
				    index++;
			}else{
				for(int j=0;j<deplist.size();j++){
					String depname = quotedepbyid((String)deplist.get(j));
					String depcode = (String)deplist.get(j);
					parameter.put(begindate, datelist.get(i)[0]);
					parameter.put(enddate, datelist.get(i)[1]);
					parameter.put(batchparam, depcode);
					if(!"1000".equals(depcode)&& !"8888".equals(depcode)&&!"1088".equals(depcode)){						
						if(index<=datelist.size()*deplist.size())
							fileNames[index] = expBatchRpt(request, response, parameter, reportFile, title, type,datelist.get(i)[0],datelist.get(i)[1],depname);
						index++;
					}
				}
			}
		}
		return fileNames;
	}
	
	//根据开始日期和结束日期取对应的交易情况
	public List<String[]> gettradingdate(String begindate,String enddate,String datetype,String selfsql) throws DataAccessException {

		Connection conn = null;
        ResultSet rs = null;
        List<String []> list = new ArrayList<String []>();
        String datearr[] = null;
        try {
        	conn = getconn();
            String sqlStr  = "";
            //日
    		if("day".equals(datetype)){
  			     sqlStr = "select  tradingday,tradingday from B_Calendar t where tradingday between "+ begindate+ " and "+enddate;
  			//周
    		}
    		else if("week".equals(datetype)){
    			 sqlStr = "select  min(tradingday),max(tradingday) from B_Calendar t where tradingday between "+ begindate+ " and "+enddate + "group by calweek";
   		    //半月
            }else if("halfmonth".equals(datetype)){
   			     sqlStr = "select mintradingday,maxtradingday from( select min(a.tradingday) mintradingday,max(a.tradingday) maxtradingday,a.halfmonthflag from"+
   			               "( select tradingday,case when substr(tradingday,7,2) <= 15 then 1 else 2 end halfmonthflag from b_calendar where tradingday between "
   			    	      +begindate+" and "+enddate+ " and tradeflag = 'T' order by tradingday) a group by substr(a.tradingday,1,6),a.halfmonthflag) order by mintradingday";
   		    //月
            }else if("month".equals(datetype)){
   			     sqlStr = "select  min(tradingday),max(tradingday) from B_Calendar t where tradingday between "+ begindate+ " and "+enddate + "group by calmonth";
   		    //季度
            }else if("quarter".equals(datetype)){
  			     sqlStr = "select min(a.tradingday) mintradingday,max(a.tradingday) maxtradingday from(select tradingday,substr(tradingday,5,2),case when substr(tradingday,5,2) <= 3 then 1 "+
   			     " when substr(tradingday,5,2) >= 4 and substr(tradingday,5,2)<=6 then 2  when substr(tradingday,5,2) >= 7 and substr(tradingday,5,2)<=9 then 3"+
                 " when substr(tradingday,5,2) >= 10 and substr(tradingday,5,2)<=12 then 4 end halfmonthflag from b_calendar where tradingday between "+begindate+" and "+enddate+" and tradeflag = 'T'"+
                 " order by tradingday)a group by a.halfmonthflag order by mintradingday ";
            //年	
            }else if("year".equals(datetype)){
   			     sqlStr = "select  min(tradingday),max(tradingday) from B_Calendar t where tradingday between "+ begindate+ " and "+enddate + "group by calyear";
            //自定义SQL
            }else if("selfdefind".equals(datetype)){
            	sqlStr = selfsql;
            }
    		
            PreparedStatement pstmt = conn.prepareStatement(sqlStr);
            rs = pstmt.executeQuery();
            while(rs.next()){
            	datearr = new String[2];
            	datearr[0] = rs.getString(1).trim();
            	datearr[1] = rs.getString(2).trim();
            	list.add(datearr);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        finally {
        	try {
                if (rs != null) {
                	rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (conn != null) {
                	conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
		if (list.size() == 0)
			return null;
		else
			return list;
	}
	
	public String expBatchRpt(HttpServletRequest request,
			HttpServletResponse response, Map parameter, String reportFile,
			String title, String type,String begindate,String enddate,String depname) throws Exception {
		Connection conn = null;
		String rptfile = "";
		String serverName = request.getServerName(); // 获得服务器的名字
		String realPath = request.getRealPath(serverName); // 取得互联网程序的绝对地址
		realPath = realPath.substring(0, realPath.lastIndexOf("\\")) + "\\temp\\";
		String sysid = CommUtil.getsysguid32();
		// 获取Connection连接
		try {
			conn= getconn();
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new File(reportFile));
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, conn);
			String sheetname[] = new String[1];
			sheetname[0] = title;
			JRXlsxExporter xlsExporter = new JRXlsxExporter();//excel2007
			//JExcelApiExporter xlsExporter = new JExcelApiExporter();//excel2003
			ByteArrayOutputStream xlsOut = new ByteArrayOutputStream();
			xlsExporter.setParameter(JRExporterParameter.JASPER_PRINT,jasperPrint);
			xlsExporter.setParameter(JRExporterParameter.OUTPUT_STREAM,xlsOut);
			xlsExporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE);// 删除记录最下面的空行
			xlsExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,Boolean.FALSE);// 删除多余的ColumnHeader
			xlsExporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.FALSE);// 显示边框
			xlsExporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE,Boolean.TRUE);// 单元格格式
			xlsExporter.setParameter(JRXlsExporterParameter.SHEET_NAMES,sheetname);// 单元格格式

			xlsExporter.exportReport();
			if(depname != "" && depname.length() != 0){
				rptfile = realPath + sysid+"_"+begindate+"_"+enddate+"("+depname+")" + ".xlsx";
			}else {
				rptfile = realPath + sysid+"_"+begindate+"_"+enddate+ ".xlsx";
			}
			FileOutputStream out = new FileOutputStream(new File(rptfile));
			out.write(xlsOut.toByteArray());
			out.flush();
			out.close();
			xlsOut.flush();
			xlsOut.close();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return rptfile;
	}
	
	// 动态报表html分页，查询记录数
	public String queryCountNum(String tablename,String reportno,String sqlname,Enumeration rnames,HttpServletRequest request) throws DataAccessException {
		
		Connection conn = null;
        ResultSet rs = null;
        String sqlquery = null;
        String sqlCount = null;
        String countnum = null;
        try {
        	conn= getconn();
             //取出sql语句，查询总记录用
            String sqlStr  = "select sqlstr from "+tablename+" where reportid = '" + reportno+"' and tname = '"+ sqlname +"'";
            PreparedStatement pstmt = conn.prepareStatement(sqlStr);
            rs = pstmt.executeQuery();
            while(rs.next()){
            	sqlquery = rs.getString(1);
            }
            sqlquery=sqlquery.toUpperCase();
            //进行参数替换
            for (Enumeration e = rnames; e.hasMoreElements();) {
				String thisName = e.nextElement().toString();
				String thisValue = request.getParameter(thisName);
				thisValue = thisValue.replace("-", "");
				if(sqlquery.indexOf("$P!{"+thisName+"}")!= -1 || sqlquery.indexOf("$P{"+thisName+"}")!= -1){
					if(thisValue == ""||thisValue.length()==0){
						sqlquery = sqlquery.replace("$P!{"+thisName+"}", "''");
						sqlquery = sqlquery.replace("$P{"+thisName+"}", "''");
					}else {
						sqlquery = sqlquery.replace("$P!{"+thisName+"}",thisValue);
						sqlquery = sqlquery.replace("$P{"+thisName+"}", "'"+thisValue+"'");
					}
					
				}
            }
            sqlCount = "select count(1) countnum from ("+ sqlquery +")";
            pstmt = conn.prepareStatement(sqlCount);
            rs = pstmt.executeQuery();
            while(rs.next()){
            	countnum = rs.getString(1);
            } 
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        finally {
        	try {
                if (rs != null) {
                	rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (conn != null) {
                	conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
		return countnum;
	}
	
	//分页参数
	public EbsDynrptPagepara getdynrptpagepara(String table,String reportid)
	throws DataAccessException {
	Connection conn = null;
	ResultSet rs = null;
	List list = new ArrayList();
	EbsDynrptPagepara dynpagep = null;
	try {
		conn= getconn();
	    String sqlStr = "select reportid,reportsheetno,sqlname,pagecount,rptremark from " +table+ " where reportid =?";
	    PreparedStatement pstmt = conn.prepareStatement(sqlStr);
	    pstmt.setString(1, reportid);
	    rs = pstmt.executeQuery();
	    while(rs.next()){
	    	dynpagep = new EbsDynrptPagepara();
	    	dynpagep.setReportid(rs.getString(1));
	    	dynpagep.setReportsheetno(rs.getString(2));
	    	dynpagep.setSqlname(rs.getString(3));
	    	dynpagep.setPagecount(rs.getString(4));
	    	dynpagep.setRptremark(rs.getString(5));
	    	list.add(dynpagep);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}
	finally {
		try {
	        if (rs != null) {
	        	rs.close();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    try {
	        if (conn != null) {
	        	conn.close();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	if (list.size() == 0)
		return null;
	else
		return (EbsDynrptPagepara) (list.get(0));
	}
	
	//批量导出参数
	public List<EbsDynrptBatchpara> getdynrptbatchpara(String table,String reportid)
	throws DataAccessException {
	Connection conn = null;
	ResultSet rs = null;
	List<EbsDynrptBatchpara> list = new ArrayList<EbsDynrptBatchpara>();
	EbsDynrptBatchpara dynbatchp = null;
	try {
		conn= getconn();
	    String sqlStr = "select reportid,reportsheetno,rptexptype,rptbatchpar1,rptbatchpar2,rptbatchpar3,rptbatchpar4,rptbatchpartype,rptdefindname,rptdefindtype,rptremark from " +table+ " where reportid =?";
	    PreparedStatement pstmt = conn.prepareStatement(sqlStr);
	    pstmt.setString(1, reportid);
	    rs = pstmt.executeQuery();
	    while(rs.next()){
	    	dynbatchp = new EbsDynrptBatchpara();
	    	dynbatchp.setReportid(rs.getString(1));
	    	dynbatchp.setReportsheetno(rs.getString(2));
	    	dynbatchp.setRptexptype(rs.getString(3));
	    	dynbatchp.setRptbatchpar1(rs.getString(4));
	    	dynbatchp.setRptbatchpar2(rs.getString(5));
	    	dynbatchp.setRptbatchpar3(rs.getString(6));
	    	dynbatchp.setRptbatchpar4(rs.getString(7));
	    	dynbatchp.setRptbatchpartype(rs.getString(8));
	    	dynbatchp.setRptdefindname(rs.getString(9));
	    	dynbatchp.setRptdefindtype(rs.getString(10));
	    	dynbatchp.setRptremark(rs.getString(11));
	    	list.add(dynbatchp);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}
	finally {
		try {
	        if (rs != null) {
	        	rs.close();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    try {
	        if (conn != null) {
	        	conn.close();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	if (list.size() == 0)
		return null;
	else
		return list;
	}
	
	
	//查询所有部门集合
	public List quotedeplist() throws DataAccessException {
		String hql="from VDynbatchexpDep  order by depcode";
		List list = getHibernateTemplate().find(hql);
		if (list.size()==0)
		    return null;
		else
			return list;
	}
	
	//根据部门id取得部门信息
	public String quotedepbyid(String depcode) throws DataAccessException {
		Connection conn = null;
		ResultSet rs = null;
		String depname = "";
		try {
			conn= getconn();
		    String sqlStr = "select depname from t_department where depcode =?";
		    PreparedStatement pstmt = conn.prepareStatement(sqlStr);
		    pstmt.setString(1, depcode);
		    rs = pstmt.executeQuery();
		    while(rs.next()){
		    	depname  = rs.getString(1);
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		    return null;
		}
		finally {
			try {
		        if (rs != null) {
		        	rs.close();
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		    try {
		        if (conn != null) {
		        	conn.close();
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}
		return depname;
	}
	
	
	public ResultInfo saverptprintpara(String curruserid,String reportid,String reportsheetno,String printdirect,String printalign,String autowidth,String pagefoot)
			throws DataAccessException {
		Connection connection = null;
		CallableStatement cstmt = null;
		ResultInfo reinfo = null;
		try {
			connection= getconn();
			cstmt = connection.prepareCall("{call pkg_dync_handle.up_upd_dynipt_printpara_tmp(?,?,?,?,?,?,?,?,?,?)}");
			cstmt.setString(1, curruserid);
			cstmt.setString(2, "add");
			cstmt.setString(3, reportid);
			cstmt.setString(4, reportsheetno);
			cstmt.setString(5, printdirect);
			cstmt.setString(6, printalign);
			cstmt.setString(7, autowidth);
			cstmt.setString(8, pagefoot);
			cstmt.registerOutParameter(9, Types.CHAR);
			cstmt.registerOutParameter(10, Types.CHAR);
			cstmt.executeUpdate();
			reinfo = ResultInfo.getInstance(cstmt.getString(9), cstmt.getString(10), "");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (cstmt != null) {
					cstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return reinfo;
	}
	
	public ResultInfo saverptpagepara(String curruserid,String reportid,String reportsheetno,String sqlname,String pagecount)
	throws DataAccessException {
		Connection connection = null;
		CallableStatement cstmt = null;
		ResultInfo reinfo = null;
		try {
			connection= getconn();
			cstmt = connection.prepareCall("{call pkg_dync_handle.up_upd_dynipt_pagepara_tmp(?,?,?,?,?,?,?,?)}");
			cstmt.setString(1, curruserid);
			cstmt.setString(2, "add");
			cstmt.setString(3, reportid);
			cstmt.setString(4, reportsheetno);
			cstmt.setString(5, sqlname);
			cstmt.setString(6, pagecount);
			cstmt.registerOutParameter(7, Types.CHAR);
			cstmt.registerOutParameter(8, Types.CHAR);
			cstmt.executeUpdate();
			reinfo = ResultInfo.getInstance(cstmt.getString(7), cstmt.getString(8), "");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (cstmt != null) {
					cstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return reinfo;
		}
	

	public List getdynrptprintpara(String reportno) throws DataAccessException {
		// TODO Auto-generated method stub
		String hql="from EbsDynrptPrintpara where reportid = '"+reportno+"' order by reportid";
		List list = getHibernateTemplate().find(hql);
		if (list.size()==0)
		    return null;
		else
			return list;
	}
	
	//分页数
	public List getdynrptpagepara(String reportid)
	throws DataAccessException {
		String hql="from EbsDynrptPagepara where reportid = '"+reportid+"' order by reportid";
		List list = getHibernateTemplate().find(hql);
		if (list.size()==0)
		    return null;
		else
			return list;
	}
	
	
	public List getdynrptprintshowpara(String reportid)
	throws DataAccessException {
	Connection conn = null;
	ResultSet rs = null;
	List list = new ArrayList();
	EbsDynrptPrintpara dynprint = null;
	try {
		conn = getconn();
	    String sqlStr = "select reportid,reportsheetno,printdirect,printalign,autowidth,pagefoot from ebs_dynrpt_printpara where reportid =?";
	    PreparedStatement pstmt = conn.prepareStatement(sqlStr);
	    pstmt.setString(1, reportid);
	    rs = pstmt.executeQuery();
	    while(rs.next()){
	    	dynprint = new EbsDynrptPrintpara();
	    	dynprint.setReportid(rs.getString(1));
	    	dynprint.setReportsheetno(rs.getString(2));
	    	dynprint.setPrintdirect(rs.getString(3));
	    	dynprint.setPrintalign(rs.getString(4));
	    	dynprint.setAutowidth(rs.getString(5));
	    	dynprint.setPagefoot(rs.getString(6));
	    	list.add(dynprint);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}
	finally {
		try {
	        if (rs != null) {
	        	rs.close();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    try {
	        if (conn != null) {
	        	conn.close();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	if (list.size() == 0)
		return null;
	else
		return list;
	}

	public List getdynrptsqlpara(String reportno) throws DataAccessException {
		String hql="from EbsDynrptTsql where reportid = '"+reportno+"' order by reportid";
		List list = getHibernateTemplate().find(hql);
		if (list.size()==0)
		    return null;
		else
			return list;
	}

	//编译后拷贝文件用
	public List getcompdynrptjxmlList(String reptId) throws DataAccessException {
		String hql = "from EbsDynrptJxml t where reportid=? ";
		List list = getHibernateTemplate().find(hql,reptId);
		if (list.size() == 0)
			return null;
		else
			return list;
	}
    
	public void setdyncsessionsta(String dyncsession)
	throws DataAccessException {
		Connection connection = null;
		CallableStatement cstmt = null;
		try {
			connection= getconn();
			cstmt = connection.prepareCall("{call pkg_tech_pub.UP_SetDyncSrchLog(?)}");
			cstmt.setString(1, dyncsession);
			cstmt.executeUpdate();
		} catch (Exception e) {
		} finally {
			try {
				if (cstmt != null) {
					cstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getdyncsessionsta(String dyncsession)
			throws DataAccessException {
		Connection connection = null;
		CallableStatement cstmt = null;
		ResultInfo reinfo = null;

		String rslt = "";
		try {
			connection= getconn();
			cstmt = connection.prepareCall("{ ?= call pkg_tech_pub.uf_getDyncSrchLog(?)}");
			cstmt.setString(2, dyncsession);
			cstmt.registerOutParameter(1, Types.CHAR);
			cstmt.execute();
			rslt = Integer.toString(cstmt.getInt(1));

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (cstmt != null) {
					cstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return rslt;
	}
	
	//导入金蝶软件格式化单元格
	public static void  excelCellFormat(String filepath){
		// 设置单元格格式为empty类型，导入金蝶数据程序用
		
			File file = new File(filepath);
			Workbook wbs = null;
			FileInputStream is;
			FileOutputStream out;
			try {
				is = new FileInputStream(file);
				wbs =WorkbookFactory.create(is);
				for (int i = 0; i < wbs.getNumberOfSheets(); i++) {
					Sheet childSheet = wbs.getSheetAt(i);
					for (int m = 0; m < childSheet.getLastRowNum(); m++) {
						Row row = childSheet.getRow(m);
						if (null != row) {
							for (int n = 0; n < row.getLastCellNum(); n++) {
								Cell cell = row.getCell(n);
								if (null != cell) {
									if (cell.getCellType() == cell.CELL_TYPE_STRING) {
										if(cell.getStringCellValue()!=null){
											if (cell.getStringCellValue().length() == 0) {
												cell.setCellType(3);// 3表示CELL_TYPE_BLANK（空值）
											}
										}
									}
								}

							}
						}
					}
				}
				is.close();
				//设置完成，写入对应的文件
				out = new FileOutputStream(file);
				wbs.write(out);
				out.flush();
				out.close();
				wbs=null;
			} catch (FileNotFoundException e) {
				CommUtil.writeLogfile("excelCellFormat(01)"+e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				CommUtil.writeLogfile("excelCellFormat(02)"+e.getMessage());
				e.printStackTrace();
			} catch (InvalidFormatException e) {
				CommUtil.writeLogfile("excelCellFormat(03)"+e.getMessage());
				e.printStackTrace();
			}
		}	
	
	public Map<String, Object> queryBodyListByPro(String countsql,String srchsql,String rankfield,String rankorder,int pernum,int currpage){
		Connection connection = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;
        Map<String, Object> map = new HashMap<String, Object>();
        
        try {
        	connection= getconn();
			cstmt  = connection.prepareCall("{call pkg_page.up_sp_page_cur(?,?,?,?,?,?,?,?,?)}");
			cstmt.setString(1, countsql);
			cstmt.setString(2, srchsql);
			cstmt.setString(3, rankfield);
			cstmt.setString(4, rankorder);
			cstmt.setInt(5, pernum);
			cstmt.setInt(6, currpage);
			cstmt.registerOutParameter(7, OracleTypes.INTEGER);
			cstmt.registerOutParameter(8, OracleTypes.INTEGER);
			cstmt.registerOutParameter(9, OracleTypes.CURSOR);
			cstmt.execute();
			int pagecount = cstmt.getInt(7);
			map.put("pagecount", pagecount);
			int numcount = cstmt.getInt(8);
			map.put("numcount", numcount);
			rs = (ResultSet)cstmt.getObject(9);
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			
			List<Map<String, String>> list = new ArrayList<Map<String,String>>();
			
		    while(rs!=null && rs.next()) {
		    	Map<String, String> row = new HashMap<String, String>();
				for (int i = 0; i < columnCount-1; i++) {
					//if(metaData.getColumnName(i).equalsIgnoreCase("")){}
					row.put(metaData.getColumnName(i+1), rs.getString(i+1));
				}
				list.add(row);
		   }
		    map.put("list", list);
		} catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        finally {
            try {
            	if (rs != null) {
            		rs.close();
            	}
                if (cstmt != null) {
                    cstmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return map;
	}
	
	public List<Map<String, String>> querySendSmsListBySql(String sql) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		try {
			conn = SingleGetCon.getInitGetcon().getConnection();
			pstmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE);
			rs = pstmt.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (rs.next()) {
				Map<String, String> columns = new HashMap<String, String>();
				for (int i = 0; i < columnCount; i++) {
					String sname=metaData.getColumnName(i+1);
					String sval="";
					if("NUMBER".equalsIgnoreCase(metaData.getColumnTypeName(i+1)))
					{
						try{
							sval=rs.getBigDecimal(i+1).toString();
						}catch(Exception e){
							sval="";
						}
					}else{
						sval=rs.getString(i+1);
					}
					columns.put(sname, sval);
				}
				list.add(columns);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
            		rs.close();
            	}
                if (pstmt != null) {
                	pstmt.close();
                }
                if (conn != null) {
                	conn.close();
                }
			} catch (Exception e) {
			}
		}
		return list;
	}

	public EbsDynrptMsgpara getdynrptSendMsgpara(String table, String reportid)
			throws DataAccessException {
		Connection conn = null;
		ResultSet rs = null;
		List list = new ArrayList();
		EbsDynrptMsgpara dynpagep = null;
		try {
			conn = getconn();
		    String sqlStr = "select reportid, issendmsg, useid, upwd, appid, content from " +table+ " where reportid =?";
		    PreparedStatement pstmt = conn.prepareStatement(sqlStr);
		    pstmt.setString(1, reportid);
		    rs = pstmt.executeQuery();
		    while(rs.next()){
		    	dynpagep = new EbsDynrptMsgpara();
		    	dynpagep.setReportid(rs.getString(1));
		    	dynpagep.setIssendmsg(rs.getString(2));
		    	dynpagep.setUseid(rs.getString(3));
		    	dynpagep.setUpwd(rs.getString(4));
		    	dynpagep.setAppid(rs.getString(5));
		    	dynpagep.setContent(rs.getString(6));
		    	list.add(dynpagep);
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		    return null;
		}
		finally {
			try {
		        if (rs != null) {
		        	rs.close();
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		    try {
		        if (conn != null) {
		        	conn.close();
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}
		if (list.size() == 0)
			return null;
		else
			return (EbsDynrptMsgpara) (list.get(0));
	}

	public List<String> getDyncTSqlList(String table, String reportid) {
		Connection conn = null;
		ResultSet rs = null;
		List list = new ArrayList();
		try {
			conn = getconn();
		    String sqlStr = "select sqlstr from " +table+ " where reportid =?";
		    PreparedStatement pstmt = conn.prepareStatement(sqlStr);
		    pstmt.setString(1, reportid);
		    rs = pstmt.executeQuery();
		    while(rs.next()){
		    	list.add(rs.getString(1));
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		    return null;
		}
		finally {
			try {
		        if (rs != null) {
		        	rs.close();
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		    try {
		        if (conn != null) {
		        	conn.close();
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}
		if (list.size() == 0)
			return null;
		else
			return list;
	}

	public ResultInfo saverptmsgpara(String curruserid, String reportid,
			EbsDynrptMsgpara dynrptSendMsgpara) throws DataAccessException {
		Connection connection = null;
		CallableStatement cstmt = null;
		ResultInfo reinfo = null;
		try {
			connection= getconn();
			cstmt = connection.prepareCall("{call pkg_dync_handle.up_upd_dynipt_msgpara(?,?,?,?,?,?,?,?,?,?)}");
			cstmt.setString(1, curruserid);
			cstmt.setString(2, "upd");
			cstmt.setString(3, reportid);
			cstmt.setString(4, dynrptSendMsgpara.getIssendmsg());
			cstmt.setString(5, dynrptSendMsgpara.getAppid());
			cstmt.setString(6, dynrptSendMsgpara.getUseid());
			cstmt.setString(7, dynrptSendMsgpara.getUpwd());
			cstmt.setString(8, dynrptSendMsgpara.getContent());
			cstmt.registerOutParameter(9, Types.CHAR);
			cstmt.registerOutParameter(10, Types.CHAR);
			cstmt.executeUpdate();
			reinfo = ResultInfo.getInstance(cstmt.getString(9), cstmt.getString(10), "");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (cstmt != null) {
					cstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return reinfo;
	}

	public EbsDynimpBpara getdynimpbpara(String reportid)
			throws DataAccessException {
		String hql="from EbsDynimpBpara where reportid=?";
		List list = getHibernateTemplate().find(hql,reportid);
		if (list.size() == 0)
			return null;
		else
			return (EbsDynimpBpara)list.get(0);
	}

	public List getdynimphpara(String reportid) throws DataAccessException {
		String hql="from EbsDynimpHpara where reportid=? order by orderid";
		List list = getHibernateTemplate().find(hql,reportid);
		if (list.size() == 0)
			return null;
		else
			return list;
	}

	public boolean saveDynImpBpara(EbsDynimpBpara bpara) {
		boolean flag = true;
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection= getconn();
			pstmt = connection.prepareStatement("insert into ebs_dynimp_bpara(reportid,reportname,filename,rptmaker,rptmakedt,rptversion,rptremark,resultsql) values(?,?,?,?,?,?,?,?)");
			pstmt.setString(1, bpara.getReportid());
			pstmt.setString(2, bpara.getReportname());
			pstmt.setString(3, bpara.getFilename());
			pstmt.setString(4, bpara.getRptmaker());
			pstmt.setString(5, bpara.getRptmakedt());
			pstmt.setString(6, bpara.getRptversion());
			pstmt.setString(7, bpara.getRptremark());
			pstmt.setString(8, bpara.getResultsql());
			
			int count = pstmt.executeUpdate();
			if(count == 0){
				flag = false;
			}
			
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return flag;
	}
	
	public boolean delDynImpBpara(String reportId) {
		boolean flag = true;
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection= getconn();
			pstmt = connection.prepareStatement("delete from ebs_dynimp_bpara where reportid = '"+reportId+"'");
			flag = pstmt.execute();
			
			
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	public boolean saveDynImpHpara(EbsDynimpHpara hpara) {
		boolean flag = true;
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection= getconn();
			pstmt = connection.prepareStatement("insert into ebs_dynimp_hpara(reportid,labelname,iptctrlname,iptctrltype,iptctrldef,iptctrllist,iptisnull,orderid,iptpardef,iptreserve1,iptreserve2,iptreserve3) values(?,?,?,?,?,?,?,?,?,?,?,?)");
			pstmt.setString(1, hpara.getReportid());
			pstmt.setString(2, hpara.getLabelname());
			pstmt.setString(3, hpara.getIptctrlname());
			pstmt.setString(4, hpara.getIptctrltype());
			pstmt.setString(5, hpara.getIptctrldef());
			pstmt.setString(6, hpara.getIptctrllist());
			pstmt.setString(7, hpara.getIptisnull());
			pstmt.setInt(8, hpara.getOrderid());
			pstmt.setString(9, hpara.getIptpardef());
			pstmt.setString(10, hpara.getIptreserve1());
			pstmt.setString(11, hpara.getIptreserve2());
			pstmt.setString(12, hpara.getIptreserve3());
			
			int count = pstmt.executeUpdate();
			if(count == 0){
				flag = false;
			}
			
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return flag;
	}
	
	public boolean delDynImpHpara(String reportId) {
		boolean flag = true;
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection= getconn();
			pstmt = connection.prepareStatement("delete from ebs_dynimp_hpara where reportid = '"+reportId+"'");
			flag = pstmt.execute();
			
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	public boolean saveDynImpCpara(EbsDynimpCparam cparam) {
		boolean flag = true;
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection= getconn();
			pstmt = connection.prepareStatement("insert into ebs_dynimp_cparam(reportid,cname,param) values(?,?,?)");
			pstmt.setString(1, cparam.getReportid());
			pstmt.setString(2, cparam.getCname());
			pstmt.setString(3, cparam.getParam());
			
			int count = pstmt.executeUpdate();
			if(count == 0){
				flag = false;
			}
			
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return flag;
	}
	
	public boolean delDynImpCpara(String reportId) {
		boolean flag = true;
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection= getconn();
			pstmt = connection.prepareStatement("delete from ebs_dynimp_cparam where reportid = '"+reportId+"'");
			flag = pstmt.execute();
			
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	public boolean saveDynImpFind(EbsDynimpFind find) {
		boolean flag = true;
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection= getconn();
			pstmt = connection.prepareStatement("insert into ebs_dynimp_find(reportid,sheetname,lbeginscope,lendscope,lscopecontent,loffset,lnth," +
					"tbeginscope,tendscope,tscopecontent,toffset,tnth,rbeginscope,rendscope,rscopecontent,roffset,rnth," +
					"bbeginscope,bendscope,bscopecontent,boffset,bnth,dtlbegin,dtlend) " +
					"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			pstmt.setString(1, find.getReportid());
			pstmt.setString(2, find.getSheetname());
			pstmt.setString(3, find.getLbeginscope());
			pstmt.setString(4, find.getLendscope());
			pstmt.setString(5, find.getLscopecontent());
			pstmt.setInt(6, find.getLoffset());
			pstmt.setInt(7, find.getLnth());
			
			pstmt.setString(8, find.getTbeginscope());
			pstmt.setString(9, find.getTendscope());
			pstmt.setString(10, find.getTscopecontent());
			pstmt.setInt(11, find.getToffset());
			pstmt.setInt(12, find.getTnth());
			
			pstmt.setString(13, find.getRbeginscope());
			pstmt.setString(14, find.getRendscope());
			pstmt.setString(15, find.getRscopecontent());
			pstmt.setInt(16, find.getRoffset());
			pstmt.setInt(17, find.getRnth());
			
			pstmt.setString(18, find.getBbeginscope());
			pstmt.setString(19, find.getBendscope());
			pstmt.setString(20, find.getBscopecontent());
			pstmt.setInt(21, find.getBoffset());
			pstmt.setInt(22, find.getBnth());
			
			pstmt.setInt(23, find.getDtlbegin());
			pstmt.setString(24, find.getDtlend());
			
			int count = pstmt.executeUpdate();
			if(count == 0){
				flag = false;
			}
			
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return flag;
	}
	
	public boolean delDynImpFind(String reportId) {
		boolean flag = true;
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection= getconn();
			pstmt = connection.prepareStatement("delete from ebs_dynimp_find where reportid = '"+reportId+"'");
			flag = pstmt.execute();
			
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	public boolean saveDynImpExeSql(EbsDynimpExesql sql) {
		boolean flag = true;
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection= getconn();
			pstmt = connection.prepareStatement("insert into ebs_dynimp_exesql(reportid,impsql,imporder,impsuborder) values(?,?,?,?)");
			pstmt.setString(1, sql.getReportid());
			pstmt.setString(2, sql.getImpsql());
			pstmt.setInt(3, sql.getImporder());
			pstmt.setInt(4, sql.getImpsuborder());
			
			int count = pstmt.executeUpdate();
			if(count == 0){
				flag = false;
			}
			
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return flag;
	}
	
	public boolean delDynImpExeSql(String reportId) {
		boolean flag = true;
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection= getconn();
			pstmt = connection.prepareStatement("delete from ebs_dynimp_exesql where reportid = '"+reportId+"'");
			flag = pstmt.execute();
			
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return flag;
	}
	
	public boolean updmenuurl(String curruserid, String menuid,
			String reportno) throws DataAccessException {
		boolean flag = true;
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection= getconn();
			String url = "/settle/jsp/sysadmin/ireport/reportjsp/dynimpjsp/dynimpjsp.jsp?reportid="+reportno;
			pstmt = connection.prepareStatement("update ebs_menu set url = ? where menuid = ?");
			pstmt.setString(1, url);
			pstmt.setString(2, menuid);
			
			int count = pstmt.executeUpdate();
			if(count == 0){
				flag = false;
			}
			
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return flag;
	}
	
	public EbsDynimpBpara queryDynImpBpara(String reportId) {
		EbsDynimpBpara bpara = null;
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet res = null;
		try {
			connection= getconn();
			pstmt = connection.prepareStatement("select reportid,reportname,filename,rptmaker,rptmakedt,rptversion,rptremark,resultsql from ebs_dynimp_bpara where reportid = ?");
			pstmt.setString(1, reportId);

			res = pstmt.executeQuery();
			if(res != null){
				bpara = new EbsDynimpBpara();
				while(res.next()){
					bpara.setReportid(reportId);
					bpara.setReportname(res.getString(2));
					bpara.setFilename(res.getString(3));
					bpara.setRptmaker(res.getString(4));
					bpara.setRptmakedt(res.getString(5));
					bpara.setRptversion(res.getString(6));
					bpara.setRptremark(res.getString(7));
					bpara.setResultsql(res.getString(8));
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(res != null){
					res.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return bpara;
	}

	public List<EbsDynimpCparam> queryDynImpCpara(String reportId) {
		List<EbsDynimpCparam> cparams = null;
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet res = null;
		try {
			connection= getconn();
			pstmt = connection.prepareStatement("select reportid,cname,param from ebs_dynimp_cparam where reportid = ? order by cname");
			pstmt.setString(1, reportId);

			res = pstmt.executeQuery();
			if(res != null){
				cparams = new ArrayList<EbsDynimpCparam>();
				while(res.next()){
					EbsDynimpCparam cparam = new EbsDynimpCparam();
					cparam.setReportid(reportId);
					cparam.setCname(res.getString(2));
					cparam.setParam(res.getString(3));
					
					cparams.add(cparam);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(res != null){
					res.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return cparams;
	}

	public EbsDynimpFind queryDynImpFind(String reportId) {
		EbsDynimpFind find = null;
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet res = null;
		try {
			connection= getconn();
			pstmt = connection.prepareStatement("select reportid,sheetname,lbeginscope,lendscope,lscopecontent,loffset,lnth," +
					"tbeginscope,tendscope,tscopecontent,toffset,tnth,rbeginscope,rendscope,rscopecontent,roffset,rnth," +
					"bbeginscope,bendscope,bscopecontent,boffset,bnth,dtlbegin,dtlend " +
					"from ebs_dynimp_find where reportid = ?");
			pstmt.setString(1, reportId);

			res = pstmt.executeQuery();
			if(res != null){
				find = new EbsDynimpFind();
				while(res.next()){
					find.setReportid(reportId);
					find.setSheetname(res.getString(2));
					find.setLbeginscope(res.getString(3));
					find.setLendscope(res.getString(4));
					find.setLscopecontent(res.getString(5));
					find.setLoffset(res.getInt(6));
					find.setLnth(res.getInt(7));
					
					find.setTbeginscope(res.getString(8));
					find.setTendscope(res.getString(9));
					find.setTscopecontent(res.getString(10));
					find.setToffset(res.getInt(11));
					find.setTnth(res.getInt(12));
					
					find.setRbeginscope(res.getString(13));
					find.setRendscope(res.getString(14));
					find.setRscopecontent(res.getString(15));
					find.setRoffset(res.getInt(16));
					find.setRnth(res.getInt(17));
					
					find.setBbeginscope(res.getString(18));
					find.setBendscope(res.getString(19));
					find.setBscopecontent(res.getString(20));
					find.setBoffset(res.getInt(21));
					find.setBnth(res.getInt(22));
					
					find.setDtlbegin(res.getInt(23));
					find.setDtlend(res.getString(24));
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(res != null){
					res.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return find;
	}

	public List<EbsDynimpExesql> qeuryDynImpExeSql(String reportId) {
		List<EbsDynimpExesql> exesqls = null;
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet res = null;
		try {
			connection= getconn();
			pstmt = connection.prepareStatement("select reportid,impsql,imporder,impsuborder from ebs_dynimp_exesql where reportid = ? order by imporder,impsuborder");
			pstmt.setString(1, reportId);

			res = pstmt.executeQuery();
			if(res != null){
				exesqls = new ArrayList<EbsDynimpExesql>();
				while(res.next()){
					EbsDynimpExesql sql = new EbsDynimpExesql();
					sql.setReportid(reportId);
					sql.setImpsql(res.getString(2));
					sql.setImporder(res.getInt(3));
					sql.setImpsuborder(res.getInt(4));
					
					exesqls.add(sql);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(res != null){
					res.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return exesqls;
	}
	//取得图表数据列表
	public Map<String, Object> queryryDyncImgResult(String reportId,
			String reportJrxmlm, String tabflag, String inputName,
			String inputValue) throws DataAccessException {
		Connection connection = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;
        Map<String, Object> map = new HashMap<String, Object>();
        
        try {
        	connection= getconn();
			cstmt  = connection.prepareCall("{call pkg_dync_handle.up_qry_DyncImgResult(?,?,?,?,?,?,?,?,?)}");
			cstmt.setString(1, reportId);
			cstmt.setString(2, reportJrxmlm);
			cstmt.setString(3, tabflag);
			cstmt.setString(4, inputName);
			cstmt.setString(5, inputValue);
			
			cstmt.registerOutParameter(6, OracleTypes.VARCHAR);
			cstmt.registerOutParameter(7, OracleTypes.VARCHAR);
			cstmt.registerOutParameter(8, OracleTypes.VARCHAR);
			cstmt.registerOutParameter(9, OracleTypes.CURSOR);
			cstmt.execute();
			String retcode = cstmt.getString(6);
			map.put("retcode", retcode);
			String retmsg = cstmt.getString(7);
			map.put("retmsg", retmsg);
			String labels = cstmt.getString(8);
			map.put("labels", labels);
			
			rs = (ResultSet)cstmt.getObject(9);
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			List<String[]> list = new ArrayList<String[]>();
			
		    while(rs!=null && rs.next()) {
				String [] dataArry = new String[columnCount];
				for(int j=0;j<dataArry.length;j++){
					dataArry[j] = rs.getString(j+1);
				}
				list.add(dataArry);
		   }
		    map.put("list", list);
		} catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        finally {
            try {
            	if (rs != null) {
            		rs.close();
            	}
                if (cstmt != null) {
                    cstmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return map;
	}
	
	public EbsDynrptImg getEbsdynrptImg(String table,String reportId) throws DataAccessException {
		Connection conn = null;
        ResultSet rs = null;
        List list = new ArrayList();
        
        EbsDynrptImg dynimg = null;
        try {
        	conn = getconn();
            String sqlStr = "select reportid, isoutmg, imgtype, beginrow, endrow, xtitle, ytitle, bgcolor, fontsize,nvl(istip,1) istip,nvl(istext,1) istext,xcount from "+table+" where reportid=?";
            PreparedStatement pstmt = conn.prepareStatement(sqlStr);
            //System.out.println(sqlStr+":"+reportid+":"+table);
            pstmt.setString(1, reportId);
            rs = pstmt.executeQuery();
            while(rs.next()){
            	dynimg = new EbsDynrptImg();
            	dynimg.setReportid(rs.getString(1));
            	dynimg.setIsoutmg(rs.getString(2));
            	dynimg.setImgtype(rs.getString(3));
            	dynimg.setBeginrow(rs.getString(4));
            	dynimg.setEndrow(rs.getString(5));
            	dynimg.setXtitle(rs.getString(6));
            	dynimg.setYtitle(rs.getString(7));
            	dynimg.setBgcolor(rs.getString(8));
            	dynimg.setFontsize(rs.getString(9));
            	dynimg.setIstip(rs.getString(10));
            	dynimg.setIstext(rs.getString(11));
            	dynimg.setXcount(rs.getString(12));
            	list.add(dynimg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        finally {
        	try {
                if (rs != null) {
                	rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (conn != null) {
                	conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
		if (list.size() == 0)
			return null;
		else
			return (EbsDynrptImg) (list.get(0));
	}
	
	public List<EbsDynrptImgcols> getEbsdynrptImgCols(String table,String reportId,String sheetindex) throws DataAccessException {
		// TODO Auto-generated method stub
		Connection conn = null;
		ResultSet rs = null;
		List<EbsDynrptImgcols> list = new ArrayList<EbsDynrptImgcols>();
		EbsDynrptImgcols dynimgcols = null;
		try {
			conn = getconn();
		    String sqlStr = "select reportid, orderid, fieldid, paramname, reportjxmlm, tname, fieldname, imgcolor, borderwd, labelname, fieldidc, sheetindex from " +table+ " where reportid =? and sheetindex=?";
		    PreparedStatement pstmt = conn.prepareStatement(sqlStr);
		    pstmt.setString(1, reportId);
		    pstmt.setString(2, sheetindex);
		    rs = pstmt.executeQuery();
		    while(rs.next()){
		    	dynimgcols = new EbsDynrptImgcols();
		    	dynimgcols.setReportid(rs.getString(1));
		    	dynimgcols.setOrderid(rs.getString(2));
		    	dynimgcols.setFieldid(rs.getString(3));
		    	dynimgcols.setParamname(rs.getString(4));
		    	dynimgcols.setReportjxmlm(rs.getString(5));
		    	dynimgcols.setTname(rs.getString(6));
		    	dynimgcols.setFieldname(rs.getString(7));
		    	dynimgcols.setImgcolor(rs.getString(8));
		    	dynimgcols.setBorderwd(rs.getString(9));
		    	dynimgcols.setLabelname(rs.getString(10));
		    	dynimgcols.setFieldidc(rs.getString(11));
		    	dynimgcols.setSheetindex(rs.getString(12));
		    	list.add(dynimgcols);
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		    return null;
		}
		finally {
			try {
		        if (rs != null) {
		        	rs.close();
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		    try {
		        if (conn != null) {
		        	conn.close();
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}
		if (list.size() == 0)
			return null;
		else
			return list;
	}

	public String getEbsdynrptImgDefault(String reportId) throws DataAccessException{
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		String params="";
		try {
			conn = getconn();
		    String sqlStr = "select iptctrlname,iptpardef from ebs_dynrpt_hpara where reportid =? order by orderid ";
		    pstmt = conn.prepareStatement(sqlStr);
		    pstmt.setString(1, reportId);
		    rs = pstmt.executeQuery();
		    while(rs.next()){
		    	String name=rs.getString(1);
		    	String val=rs.getString(2);
		    	if(val==null)val="";
		    	if(val.toLowerCase().indexOf("select")!=-1)
		    	{
		    		val=CommUtil.getValueBySql(val);
		    	}
		    	params = params+name+"@@"+val+";";
		    }
		} catch (Exception e){
		    e.printStackTrace();
		    return "";
		}
		finally {
			try {
		        if (rs != null) {
		        	rs.close();
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		    try {
		        if (pstmt != null) {
		        	pstmt.close();
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		    try {
		        if (conn != null) {
		        	conn.close();
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}
		return params;
	}
	
}
