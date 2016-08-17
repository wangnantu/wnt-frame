package com.wnt.ireport.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;

import org.apache.poi.ss.usermodel.CellStyle;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.wnt.ireport.util.CommUtil;

//动态报表类
public class TrendReport {

	private SAXBuilder builder = null;
	private Document document = null;
	private String xmlPath = null;
	private String compxmlPath = null;
	private String modelxmlname = "xmlmodel";
	private String nsstr = "http://jasperreports.sourceforge.net/jasperreports";
	private Namespace ns = null;
	private String jrxmldir = CommUtil.getProjectPath()
			+ "temp/";
	private int bandWidthValue = 8;
	private String pageHeight = "470";
	private String compjrxmldir = CommUtil.getProjectPath()
	        + "temp/";
	private String subreportdir = CommUtil.getProjectPath()+"jsp/sysadmin/ireport/reportjasper/";
	private String sysdate ="$P{SYSDATE}";
	private String CNCELLTYPEBIGDECIMAL="java.math.BigDecimal";
	private String COUNT="COUNT";

	public TrendReport(String jrxmlname) {
		// 确定父报表的路径
		jrxmldir = jrxmldir + jrxmlname + "/";
		compjrxmldir = compjrxmldir + jrxmlname +"/";
		subreportdir = subreportdir + jrxmlname +"/";
		
		// 获得命名空间
		ns = Namespace.getNamespace(nsstr);
	}

	// 生成jrxml文件
	public void CreateJrxml(String newxmlname) {
		String modelxmlpath = CommUtil.getProjectPath()
				+ "jsp/reports/models/" + modelxmlname
				+ ".jrxml";
		String newxmlpath = jrxmldir + newxmlname + ".jrxml";
		File molelfile = new File(modelxmlpath);
		File newfile = new File(newxmlpath);
		File newfiledir = new File(jrxmldir);
		if (newfiledir.exists()) {
			newfiledir.delete();
		}
		newfiledir.mkdirs();
		BufferedInputStream inbuff = null;
		BufferedOutputStream outbuff = null;
		try {
			// 新建文件输入流并对它缓冲
			inbuff = new BufferedInputStream(new FileInputStream(molelfile));
			// 新建文件输出流并对它进行缓冲
			outbuff = new BufferedOutputStream(new FileOutputStream(newfile));
			// 缓冲字符数组
			byte[] b = new byte[1024];
			int len = 0;
			while ((len = inbuff.read(b)) != -1) {
				outbuff.write(b, 0, len);
			}
			// 刷新次缓冲的输出流
			outbuff.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (inbuff != null)
					inbuff.close();
				if (outbuff != null)
					outbuff.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// 设置完成后写进对应的xml文件
	public void writerjrxml(String jrxmlname) {
		try {
			builder = new SAXBuilder(false);
			xmlPath = jrxmldir + jrxmlname + ".jrxml";
			XMLOutputter outputter = new XMLOutputter();
			Format fmt = Format.getPrettyFormat();
			// 缩进的长度
			fmt.setIndent("   ");
			outputter.setFormat(fmt);
			outputter.output(document.getRootElement().getDocument(),
					new FileOutputStream(xmlPath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	//循环编译所有的jrxml文件
	public String compileJrxml(){
		String rtn="";
		try {
			File compfile = new File(compjrxmldir);
			if(compfile.exists()){
				compfile.delete();
			}
			compfile.mkdirs();
			File jrxmlfile = new File(jrxmldir);
			String jrxmlfiles[] = jrxmlfile.list();
			for(int i=0;i<jrxmlfiles.length;i++){
				if(jrxmlfiles[i].equals(".jrxml")){
					continue;
				}
				if(jrxmlfiles[i].substring(jrxmlfiles[i].indexOf(".")+1, jrxmlfiles[i].length()).equals("jrxml")){
					xmlPath = jrxmldir + jrxmlfiles[i];
					compxmlPath = compjrxmldir + jrxmlfiles[i].substring(0, jrxmlfiles[i].indexOf(".")) + ".jasper";
					JasperCompileManager.compileReportToFile(xmlPath, compxmlPath);	
				}
			}
		} catch (JRException e) {
			rtn = e.getMessage();
		}
		return rtn;
	}

	// 获取document对象
	public Document getDocument(String jrxmlname) {
		builder = new SAXBuilder(false);
		xmlPath = jrxmldir + jrxmlname + ".jrxml";
		File xmlfile = new File(xmlPath);
		try {
			Reader xmlStreamReader = new InputStreamReader(new FileInputStream(
					xmlfile), "UTF-8");
			document = builder.build(xmlStreamReader);
			xmlStreamReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		}
		return document;
	}

	// 设置detail区域颜色隔行显示样式
	public void setStylewithColor(String jrxmlname, String detailColor,
			String tableHeadColor) {
		Element root = getDocument(jrxmlname).getRootElement();// 获得根元素
		List<Element> stylelist = root.getChildren("style", ns);
		for (int i = 0; i < stylelist.size(); i++) {

			Element styleelem = stylelist.get(i);
			if ("detail".equals(styleelem.getAttributeValue("name"))) {
				Element conditionalStyleelem = styleelem.getChild(
						"conditionalStyle", ns);
				Element substyleelem = conditionalStyleelem.getChild("style",
						ns);
				substyleelem.setAttribute("backcolor", detailColor);
			} else if ("tableHead".equals(styleelem.getAttributeValue("name"))) {
				styleelem.setAttribute("backcolor", tableHeadColor);
			}
		}
		writerjrxml(jrxmlname);
	}

	// 设置公共属性（名称、页面宽度、打印方向、列宽、是否忽略分页）
	public void setpublicProperty(String jrxmlname, String pageWidth,String orientation, String isIgnorePagination) {

		Element root = getDocument(jrxmlname).getRootElement();// 获得根元素
		root.setAttribute("name", jrxmlname);
		root.setAttribute("pageWidth", new String().valueOf(Integer
				.parseInt(pageWidth) * bandWidthValue));
		root.setAttribute("pageHeight", pageHeight);
		root.setAttribute("orientation", orientation);
		root.setAttribute("columnWidth", new String().valueOf(Integer
				.parseInt(pageWidth) * bandWidthValue));
		root.setAttribute("isIgnorePagination", isIgnorePagination);
		writerjrxml(jrxmlname);
	}

	// 设置参数parameter
	public void setParameter(String jrxmlname, CellAllStyle cellstyle) {
		Element root = getDocument(jrxmlname).getRootElement();// 获得根元素
		// parameter参数的位置
		int index = 0;

		// 设置子报表路径
		Element subparameter = root.getChild("parameter", ns);
		index = root.indexOf(subparameter);
		// 增加参数
		Element paramelem = new Element("parameter");
		paramelem.setNamespace(ns);
		paramelem.setAttribute("name", cellstyle.getNosigndata());
		paramelem.setAttribute("class", cellstyle.getCellType());
		//如果带感叹号时
		if(cellstyle.getSigndata()!= null && cellstyle.getSigndata().length() != 0 && cellstyle.getSigndata().contains("!")){
			//建立defaultValueExpression节点
			Element defaultValueExpressionelem = new Element("defaultValueExpression");
			defaultValueExpressionelem.setNamespace(ns);
			//默认值为系统时间
			if("$P!{RANKFIELD}".equals(cellstyle.getSigndata())){
				defaultValueExpressionelem.setText('"'+"rownum"+'"');
			}else if("$P!{RANKORDER}".equals(cellstyle.getSigndata())){
				defaultValueExpressionelem.setText('"'+"desc"+'"');
			}else {
				defaultValueExpressionelem.setText(CommUtil.getDate2());
			}
			paramelem.addContent(defaultValueExpressionelem);
		}
		root.addContent(index + 1, paramelem);
		writerjrxml(jrxmlname);
	}

	// 设置查询条件queryString
	public void setQueryString(String jrxmlname, String querysql) {
		Element root = getDocument(jrxmlname).getRootElement();// 获得根元素
		Element queryelem = root.getChild("queryString", ns);
		queryelem.setText(querysql);
		writerjrxml(jrxmlname);
	}

	// 设置字段值Fields
	public void setField(String jrxmlname, CellAllStyle cellstyle) {
		Element root = getDocument(jrxmlname).getRootElement();// 获得根元素
		int index = 0;

		// 得到queryString的位置
		Element queryelem = root.getChild("queryString", ns);
		index = root.indexOf(queryelem);

		Element fieldelem = new Element("field");
		fieldelem.setNamespace(ns);
		fieldelem.setAttribute("name", cellstyle.getNosigndata());
		fieldelem.setAttribute("class", cellstyle.getCellType());
		root.addContent(index + 1, fieldelem);
		writerjrxml(jrxmlname);
	}

	// 设置变量variable
	public void setVariable(String jrxmlname, String resetType,
			String calculation, CellAllStyle cellstyle) {
		Element root = getDocument(jrxmlname).getRootElement();// 获得根元素
		// 获取位置
		int index = 0;
		List<Element> variablelist = root.getChildren("variable", ns);
		for (int i = 0; i < variablelist.size(); i++) {

			Element variableelem = variablelist.get(i);
			if (variableelem.getAttributeValue("name").equals("nullvalue")) {
				index = root.indexOf(variableelem);
			}
		}
		// 设置变量
		Element variableelem = new Element("variable");
		variableelem.setNamespace(ns);
		variableelem.setAttribute("name", cellstyle.getNosigndata());
		if(COUNT.equals(calculation)){
			variableelem.setAttribute("class", CNCELLTYPEBIGDECIMAL);
		}else{
			variableelem.setAttribute("class", cellstyle.getCellType());
		}
		variableelem.setAttribute("resetType", resetType.substring(0, 1).toUpperCase()+resetType.substring(1, resetType.length()).toLowerCase());
		variableelem.setAttribute("calculation", calculation.substring(0, 1).toUpperCase()+calculation.substring(1, calculation.length()).toLowerCase());
		// 绑定变量
		Element variableExpressionelem = new Element("variableExpression");
		variableExpressionelem.setNamespace(ns);
		variableExpressionelem.setText("$F{"
				+ cellstyle.getNosigndata()+ "}");
		variableelem.addContent(variableExpressionelem);
		root.addContent(index + 1, variableelem);
		writerjrxml(jrxmlname);
	}

	// 设置band高度
	public void setBandHeight(String jrxmlname, String areaname, int bandHeight,int index) {
		Element root = getDocument(jrxmlname).getRootElement();// 获得根元素
		// 取得要放置高度的区域
		Element areaelem = root.getChild(areaname, ns);
		List<Element> bandlist = areaelem.getChildren("band", ns);
		Element bandelem = bandlist.get(index);
		bandelem.setAttribute("height", new String().valueOf(bandHeight));
		writerjrxml(jrxmlname);
	}

	// 生成通用的band节点及子节点的方法
	public void setBandinfo(Element element, CellAllStyle cellstyle) {
		List<Element> bandlist = element.getChildren("band", ns);
		Element bandelem = element.getChild("band", ns);
		bandelem.setAttribute("height", cellstyle.getRowHeight());
		bandelem.setAttribute("splitType", "Stretch");
		if (cellstyle.getAlldata().length() != 0) {
			if (cellstyle.getAlldata().contains("$")) {
				// 增加textField节点
				Element textFieldelem = new Element("textField");
				textFieldelem.setNamespace(ns);
				if(sysdate.equals(cellstyle.getSigndata())){
					textFieldelem.setAttribute("pattern","yyyy-MM-dd");
				}
				textFieldelem.setAttribute("isStretchWithOverflow", "true");
				textFieldelem.setAttribute("isBlankWhenNull", "true");
				// 设置小数点后保留位数(数值类型)
				if(cellstyle.getDateformat() != null){
					if(cellstyle.getDateformat() .indexOf("[Red]")>-1){
						String pattern = cellstyle.getDateformat().replace("[Red]", "-");
						textFieldelem.setAttribute("pattern", pattern);
					}else if(cellstyle.getDateformat() .indexOf("#")>-1){
						String pattern = cellstyle.getDateformat() +";-"+ cellstyle.getDateformat() ;
						textFieldelem.setAttribute("pattern", pattern);
					}else if(cellstyle.getDateformat() .indexOf("0")>-1){
						String pattern = "###"+cellstyle.getDateformat() +";-###"+cellstyle.getDateformat() ;
						textFieldelem.setAttribute("pattern", pattern);
					}
				}
				
				//货币类型
				if(cellstyle.getDateformat()!= null && cellstyle.getDateformat().indexOf("￥")!=-1){
					int strindex = cellstyle.getDateformat().indexOf("￥");
					String str = cellstyle.getDateformat().substring(strindex+2, cellstyle.getDateformat().length());
					if(str != null){
						if(str.indexOf("[Red]")>-1){
							String pattern = str.replace("[Red]", "-");
							textFieldelem.setAttribute("pattern", pattern);
						}else if(str.indexOf("#")>-1){
							String pattern = "¤"+str;
							textFieldelem.setAttribute("pattern", pattern);
						}else {
							String pattern = "¤###"+str;
							textFieldelem.setAttribute("pattern", pattern);
						}
					}
				}

				// 增加reportElement节点
				Element reportElementelem = new Element("reportElement");
				reportElementelem.setNamespace(ns);
				if(element.getName().equals("detail"))
				reportElementelem.setAttribute("style", "detail");
				reportElementelem.setAttribute("stretchType",
						"RelativeToBandHeight");
				reportElementelem.setAttribute("mode", "Opaque");
				reportElementelem.setAttribute("x", cellstyle.getRowLeft());
				reportElementelem.setAttribute("y", cellstyle.getRowTop());
				reportElementelem
						.setAttribute("width", cellstyle.getRowWidth());
				reportElementelem.setAttribute("height", cellstyle
						.getRowHeight());
				reportElementelem.setAttribute("isPrintWhenDetailOverflows",
						"true");
				textFieldelem.addContent(reportElementelem);

				// 增加box节点
				Element boxelem = new Element("box");
				boxelem.setNamespace(ns);
				// 分别增加topPen、leftPen、bottomPen、rightPen节点
				Element topPenelem = new Element("topPen");
				topPenelem.setNamespace(ns);
				topPenelem.setAttribute("lineWidth", "0.5");
				topPenelem.setAttribute("lineColor", "#CCCCCC");

				Element leftPenelem = new Element("leftPen");
				leftPenelem.setNamespace(ns);
				leftPenelem.setAttribute("lineWidth", "0.5");
				leftPenelem.setAttribute("lineColor", "#CCCCCC");

				Element bottomPenelem = new Element("bottomPen");
				bottomPenelem.setNamespace(ns);
				bottomPenelem.setAttribute("lineWidth", "0.5");
				bottomPenelem.setAttribute("lineColor", "#CCCCCC");

				Element rightPenelem = new Element("rightPen");
				rightPenelem.setNamespace(ns);
				rightPenelem.setAttribute("lineWidth", "0.5");
				rightPenelem.setAttribute("lineColor", "#CCCCCC");

				boxelem.addContent(topPenelem);
				boxelem.addContent(leftPenelem);
				boxelem.addContent(bottomPenelem);
				boxelem.addContent(rightPenelem);

				// 增加box节点到textField下面
				if(!"title".equals(element.getName()))
				textFieldelem.addContent(boxelem);

				// 增加textElement节点
				Element textElementelem = new Element("textElement");
				textElementelem.setNamespace(ns);
				textElementelem.setAttribute("textAlignment", cellstyle.getHorizonta());
				textElementelem.setAttribute("verticalAlignment", cellstyle.getVertical());

				// 增加textElement节点的子节点font节点
				Element fontelem = new Element("font");
				fontelem.setNamespace(ns);
				fontelem.setAttribute("fontName", cellstyle.getFontstyle());
				fontelem.setAttribute("size", cellstyle.getFontsize());
				fontelem.setAttribute("isBold", cellstyle.getBoldlight());
				fontelem.setAttribute("pdfFontName", "STSong-Light");
				fontelem.setAttribute("pdfEncoding", "UniGB-UCS2-H");
				fontelem.setAttribute("isPdfEmbedded", "true");
				textElementelem.addContent(fontelem);

				// 增加reportElement到textField下面
				textFieldelem.addContent(textElementelem);

				// 增加textFieldExpression节点
				Element textFieldExpressionelem = new Element(
						"textFieldExpression");
				textFieldExpressionelem.setNamespace(ns);
				// 拼接字符的情况
				if (cellstyle.getAlldata().length() != cellstyle.getSigndata().length()&&!cellstyle.getAlldata().contains("SUM")&&!cellstyle.getAlldata().contains("AVG")&&!cellstyle.getAlldata().contains("COUNT")) {
					
					int index = cellstyle.getAlldata().indexOf(cellstyle.getSigndata());
					String str1 = cellstyle.getAlldata().substring(0, index);
					String str2 = cellstyle.getAlldata().substring(index+cellstyle.getSigndata().length(),cellstyle.getAlldata().length());
					textFieldExpressionelem.setText('"'+str1+'"'+"+"+cellstyle.getSigndata().toUpperCase()+"+"+'"'+str2+'"');
					
				} else {
					//当前时间设置
					if(sysdate.equals(cellstyle.getSigndata())){
						textFieldExpressionelem.setText("new java.util.Date()");
					}else {
						textFieldExpressionelem.setText(cellstyle.getSigndata()
								.toUpperCase());	
					}
				}


				// 增加textFieldExpression到textField下面
				textFieldelem.addContent(textFieldExpressionelem);

				// 将textField节点增加到band节点下面
				bandelem.addContent(textFieldelem);
			} else {

				// 增加staticText节点
				Element staticTextelem = new Element("staticText");
				staticTextelem.setNamespace(ns);

				// 增加reportElement节点
				Element reportElementelem = new Element("reportElement");
				reportElementelem.setNamespace(ns);

				// 设置表头背景色
				if ("columnHeader".equals(element.getName())
						|| "pageHeader".equals(element.getName())) {
					reportElementelem.setAttribute("style", "tableHead");
					reportElementelem.setAttribute("forecolor", "#000000");
					reportElementelem.setAttribute("backcolor", "#AAD7FE");
				}
				reportElementelem.setAttribute("stretchType",
						"RelativeToBandHeight");
				reportElementelem.setAttribute("mode", "Opaque");
				reportElementelem.setAttribute("x", cellstyle.getRowLeft());
				reportElementelem.setAttribute("y", cellstyle.getRowTop());
				reportElementelem
						.setAttribute("width", cellstyle.getRowWidth());
				reportElementelem.setAttribute("height", cellstyle
						.getRowHeight());
				reportElementelem.setAttribute("isPrintWhenDetailOverflows",
						"true");
				staticTextelem.addContent(reportElementelem);

				// 增加box节点
				Element boxelem = new Element("box");
				boxelem.setNamespace(ns);
				// 分别增加topPen、leftPen、bottomPen、rightPen节点
				Element topPenelem = new Element("topPen");
				topPenelem.setNamespace(ns);
				topPenelem.setAttribute("lineWidth", "0.5");
				topPenelem.setAttribute("lineColor", "#CCCCCC");

				Element leftPenelem = new Element("leftPen");
				leftPenelem.setNamespace(ns);
				leftPenelem.setAttribute("lineWidth", "0.5");
				leftPenelem.setAttribute("lineColor", "#CCCCCC");

				Element bottomPenelem = new Element("bottomPen");
				bottomPenelem.setNamespace(ns);
				bottomPenelem.setAttribute("lineWidth", "0.5");
				bottomPenelem.setAttribute("lineColor", "#CCCCCC");

				Element rightPenelem = new Element("rightPen");
				rightPenelem.setNamespace(ns);
				rightPenelem.setAttribute("lineWidth", "0.5");
				rightPenelem.setAttribute("lineColor", "#CCCCCC");

				boxelem.addContent(topPenelem);
				boxelem.addContent(leftPenelem);
				boxelem.addContent(bottomPenelem);
				boxelem.addContent(rightPenelem);

				// 增加box节点到textField下面
				if(!"title".equals(element.getName()))
				staticTextelem.addContent(boxelem);

				// 增加textElement节点
				Element textElementelem = new Element("textElement");
				textElementelem.setNamespace(ns);
				textElementelem.setAttribute("textAlignment", cellstyle.getHorizonta());
				textElementelem.setAttribute("verticalAlignment", cellstyle.getVertical());

				// 增加textElement节点的子节点font节点
				Element fontelem = new Element("font");
				fontelem.setNamespace(ns);
				fontelem.setAttribute("fontName", cellstyle.getFontstyle());
				fontelem.setAttribute("size", cellstyle.getFontsize());
				fontelem.setAttribute("isBold", cellstyle.getBoldlight());
				fontelem.setAttribute("pdfFontName", "STSong-Light");
				fontelem.setAttribute("pdfEncoding", "UniGB-UCS2-H");
				fontelem.setAttribute("isPdfEmbedded", "true");
				textElementelem.addContent(fontelem);

				// 增加reportElement到textField下面
				staticTextelem.addContent(textElementelem);

				// 增加text节点
				Element textelem = new Element("text");
				textelem.setNamespace(ns);
				textelem.setText(cellstyle.getAlldata());

				// 增加text到textFields下面
				staticTextelem.addContent(textelem);

				// 将textField节点增加到band节点下面
				bandelem.addContent(staticTextelem);
			}
		} else {
			// 增加textField节点
			Element textFieldelem = new Element("textField");
			textFieldelem.setNamespace(ns);
			textFieldelem.setAttribute("isStretchWithOverflow", "true");
			textFieldelem.setAttribute("isBlankWhenNull", "true");
			// 设置小数点后保留位数(货币类型)
			if(cellstyle.getDateformat() != null){
				if(cellstyle.getDateformat().indexOf("[Red]")>-1){
					String pattern = cellstyle.getDateformat().replace("[Red]", "-");
					textFieldelem.setAttribute("pattern", pattern);
				}else if(cellstyle.getDateformat().indexOf("#")>-1){
					String pattern = cellstyle.getDateformat()+";-"+cellstyle.getDateformat();
					textFieldelem.setAttribute("pattern", pattern);
				}else {
					String pattern = "###"+cellstyle.getDateformat()+";-###"+cellstyle.getDateformat();
					textFieldelem.setAttribute("pattern", pattern);
				}
			}

			//货币类型
			if(cellstyle.getDateformat()!= null && cellstyle.getDateformat().indexOf("￥")!=-1){
				int strindex = cellstyle.getDateformat().indexOf("￥");
				String str = cellstyle.getDateformat().substring(strindex+2, cellstyle.getDateformat().length());
				if(str != null){
					if(str.indexOf("[Red]")>-1){
						String pattern = str.replace("[Red]", "-");
						textFieldelem.setAttribute("pattern", pattern);
					}else if(str.indexOf("#")>-1){
						String pattern = "¤"+str;
						textFieldelem.setAttribute("pattern", pattern);
					}else {
						String pattern = "¤###"+str;
						textFieldelem.setAttribute("pattern", pattern);
					}
				}
			}
			// 增加reportElement节点
			Element reportElementelem = new Element("reportElement");
			reportElementelem.setNamespace(ns);
			if(element.getName().equals("detail"))
			reportElementelem.setAttribute("style", "detail");
			reportElementelem.setAttribute("stretchType",
					"RelativeToBandHeight");
			reportElementelem.setAttribute("mode", "Opaque");
			reportElementelem.setAttribute("x", cellstyle.getRowLeft());
			reportElementelem.setAttribute("y", cellstyle.getRowTop());
			reportElementelem.setAttribute("width", cellstyle.getRowWidth());
			reportElementelem.setAttribute("height", cellstyle.getRowHeight());
			reportElementelem
					.setAttribute("isPrintWhenDetailOverflows", "true");
			textFieldelem.addContent(reportElementelem);

			// 增加box节点
			Element boxelem = new Element("box");
			boxelem.setNamespace(ns);
			// 分别增加topPen、leftPen、bottomPen、rightPen节点
			Element topPenelem = new Element("topPen");
			topPenelem.setNamespace(ns);
			topPenelem.setAttribute("lineWidth", "0.5");
			topPenelem.setAttribute("lineColor", "#CCCCCC");

			Element leftPenelem = new Element("leftPen");
			leftPenelem.setNamespace(ns);
			leftPenelem.setAttribute("lineWidth", "0.5");
			leftPenelem.setAttribute("lineColor", "#CCCCCC");

			Element bottomPenelem = new Element("bottomPen");
			bottomPenelem.setNamespace(ns);
			bottomPenelem.setAttribute("lineWidth", "0.5");
			bottomPenelem.setAttribute("lineColor", "#CCCCCC");

			Element rightPenelem = new Element("rightPen");
			rightPenelem.setNamespace(ns);
			rightPenelem.setAttribute("lineWidth", "0.5");
			rightPenelem.setAttribute("lineColor", "#CCCCCC");

			boxelem.addContent(topPenelem);
			boxelem.addContent(leftPenelem);
			boxelem.addContent(bottomPenelem);
			boxelem.addContent(rightPenelem);

			// 增加box节点到textField下面
			if(!"title".equals(element.getName()))
			textFieldelem.addContent(boxelem);

			// 增加textElement节点
			Element textElementelem = new Element("textElement");
			textElementelem.setNamespace(ns);
			textElementelem.setAttribute("textAlignment", cellstyle.getHorizonta());
			textElementelem.setAttribute("verticalAlignment", cellstyle.getVertical());

			// 增加textElement节点的子节点font节点
			Element fontelem = new Element("font");
			fontelem.setNamespace(ns);
			fontelem.setAttribute("fontName", cellstyle.getFontstyle());
			fontelem.setAttribute("size", cellstyle.getFontsize());
			fontelem.setAttribute("isBold", cellstyle.getBoldlight());
			fontelem.setAttribute("pdfFontName", "STSong-Light");
			fontelem.setAttribute("pdfEncoding", "UniGB-UCS2-H");
			fontelem.setAttribute("isPdfEmbedded", "true");
			textElementelem.addContent(fontelem);

			// 增加reportElement到textField下面
			textFieldelem.addContent(textElementelem);

			// 增加textFieldExpression节点
			Element textFieldExpressionelem = new Element("textFieldExpression");
			textFieldExpressionelem.setNamespace(ns);
			if(!"title".equals(element.getName()))
			textFieldExpressionelem.setText("$V{nullvalue}");

			// 增加textFieldExpression到textField下面
			textFieldelem.addContent(textFieldExpressionelem);

			// 将textField节点增加到band节点下面
			bandelem.addContent(textFieldelem);
		}

	}

	// 设置子报表
	public void setSubReport(String mainjxml, String jrxmlname,
			String areaname, String subreportname, CellAllStyle cellstyle,
			int index) {
		Element root = getDocument(jrxmlname).getRootElement();// 获得根元素
		String subdir[] = subreportdir.split("/");
        StringBuilder subreportdir = new StringBuilder();
        for(int i=0;i<subdir.length;i++){
        	subreportdir.append(subdir[i]);
        	subreportdir.append("\\\\");
        }
		List<Element> parameterlist = root.getChildren("parameter", ns);
		for (int i = 0; i < parameterlist.size(); i++) {
			Element parameter = parameterlist.get(i);
			if (parameter.getAttributeValue("name").equals("SUBREPORT_DIR")) {
				parameter.getChild("defaultValueExpression", ns).setText(
						'"' + subreportdir.toString()+ '"');
			}
		}
		// 取得要放置子报表的区域
		Element areaelem = root.getChild(areaname, ns);

		List<Element> bandlist = areaelem.getChildren("band", ns);
		Element bandelem = bandlist.get(index);
		// Element bandelem = areaelem.getChild("band", ns);
		// detail区域只有子报表的情况下，设置detail高度
		if (bandelem.getAttributeValue("height") == null) {
			bandelem.setAttribute("height", cellstyle.getRowHeight());
			bandelem.setAttribute("splitType", "Stretch");
		}

		// 新增subreport节点
		Element subreportelem = new Element("subreport");
		subreportelem.setNamespace(ns);
		Element reportElementelem = new Element("reportElement");
		reportElementelem.setNamespace(ns);
		reportElementelem.setAttribute("isPrintWhenDetailOverflows","true");
		reportElementelem.setAttribute("stretchType","RelativeToBandHeight");
		reportElementelem.setAttribute("x", cellstyle.getRowLeft());
		reportElementelem.setAttribute("y", cellstyle.getRowTop());
		reportElementelem.setAttribute("width", cellstyle.getRowWidth());
		reportElementelem.setAttribute("height", cellstyle.getRowHeight());
		// 增加reportElement节点到subreport下面
		subreportelem.addContent(reportElementelem);

		// 增加connectionExpression节点
		Element connectionExpressionelem = new Element("connectionExpression");
		connectionExpressionelem.setNamespace(ns);
		connectionExpressionelem.setText("$P{REPORT_CONNECTION}");
		subreportelem.addContent(connectionExpressionelem);

		// 增加subreportExpression节点
		Element subreportExpressionelem = new Element("subreportExpression");
		subreportExpressionelem.setNamespace(ns);
		subreportExpressionelem.setText("$P{SUBREPORT_DIR}+" + '"'
				+ subreportname + ".jasper" + '"');
		subreportelem.addContent(subreportExpressionelem);

		// 增加subreport节点到band下面
		bandelem.addContent(subreportelem);
		writerjrxml(jrxmlname);
	}

	// 设置子报表与父报表之间的变量绑定
	public void setBandValue(String jrxmlname, String subjrxml,
			String areaname, String param, String bindvalue, int index) {
		Element root = getDocument(jrxmlname).getRootElement();// 获得根元素

		// 取得要放置子报表的区域
		Element areaelem = root.getChild(areaname, ns);
		List<Element> bandlist = areaelem.getChildren("band", ns);
		Element bandelem = bandlist.get(index);
		// Element bandelem = areaelem.getChild("band", ns);

		// 得到subreport节点
		List<Element> subreportlist = bandelem.getChildren("subreport", ns);
		for (int i = 0; i < subreportlist.size(); i++) {
			Element subreportelem = subreportlist.get(i);
			// 取得subreportExpression节点
			Element subreportExpressionelem = subreportelem.getChild(
					"subreportExpression", ns);
			String subjrxmlname = subreportExpressionelem.getText();
			subjrxmlname = subjrxmlname.substring(
					subjrxmlname.indexOf("+") + 2, subjrxmlname
							.indexOf(".jasper"));
            
			if (subjrxml.equals(subjrxmlname)) {
				Element subreportParameterelem = new Element(
						"subreportParameter");
				subreportParameterelem.setNamespace(ns);
				
				if(param != null && param.contains("!")){
					subreportParameterelem.setAttribute("name", param.substring(4,
							param.length() - 1));
				}else {
					subreportParameterelem.setAttribute("name", param.substring(3,
							param.length() - 1));	
				}
				
				Element subreportParameterExpressionelem = new Element("subreportParameterExpression");
		       
				subreportParameterExpressionelem.setNamespace(ns);
		        subreportParameterExpressionelem.setText(bindvalue.replace("!", ""));

				List subreportParameterlist = subreportelem.getChildren("subreportParameter", ns);
				Boolean subreportflag = false;
				if(subreportParameterlist != null && subreportParameterlist.size()>0){
					for(int m=0;m<subreportParameterlist.size();m++){
						Element subreportParameter = (Element)subreportParameterlist.get(m);
						String subreportParameterName = subreportParameter.getAttributeValue("name");
						if(subreportParameterName != null && "SUBREPORT_DIR".equals(subreportParameterName)){
							subreportflag = true;
						}
					}
				}
				if(subreportflag == false){
					Element subreportParameterelemdir = new Element("subreportParameter");
					subreportParameterelemdir.setNamespace(ns);
					Element subreportParameterExpressionelemdir = new Element("subreportParameterExpression");
					subreportParameterExpressionelemdir.setNamespace(ns);
					subreportParameterelemdir.setAttribute("name", "SUBREPORT_DIR");
					subreportParameterExpressionelemdir.setText("$P{SUBREPORT_DIR}");
					
					subreportParameterelemdir.addContent(subreportParameterExpressionelemdir);
					subreportelem.addContent(2, subreportParameterelemdir);
				}
				
				// 增加subreportParameterExpression节点到subreportParameter
				subreportParameterelem.addContent(subreportParameterExpressionelem);
				// 增加subreportParameter节点到subreport
				subreportelem.addContent(2, subreportParameterelem);
			}
		}
		writerjrxml(jrxmlname);
	}

	// 新增detail区域
	public void addDetailBand(String jrxmlname, String subjrxml,
			CellAllStyle cellstyle) {
		Element root = getDocument(jrxmlname).getRootElement();// 获得根元素
		// 得到detail节点
		Element detailelem = root.getChild("detail", ns);

		// 新建band节点
		Element bandelem = new Element("band");
		bandelem.setNamespace(ns);
		bandelem.setAttribute("height", cellstyle.getRowHeight());
		bandelem.setAttribute("splitType", "Stretch");
		detailelem.addContent(bandelem);
		writerjrxml(jrxmlname);

	}

	// 设置交叉报表
	// rowField 行分组字段 columnField 列分组字段 measures 数据字段 calculationType 计算类型
	public void setCrossTab(String jrxmlname, String rowField,
			String columnField, String measures, String calculationType,
			CellStyle cellstyle) {
		Element root = getDocument(jrxmlname).getRootElement();// 获得根元素
		Element summaryelem = root.getChild("summary", ns);
		Element bandelem = summaryelem.getChild("band", ns);
		bandelem.setAttribute("height", "20");
		bandelem.setAttribute("splitType", "Stretch");
		// 创建crosstab节点
		Element crosstabelem = new Element("crosstab");
		crosstabelem.setNamespace(ns);

		// 创建reportElement节点
		Element reportElementelem = new Element("reportElement");
		reportElementelem.setNamespace(ns);
		reportElementelem.setAttribute("x", "0");
		reportElementelem.setAttribute("y", "0");
		reportElementelem.setAttribute("width", new String().valueOf(cellstyle.getBorderRight() - cellstyle.getBorderLeft()));
		reportElementelem.setAttribute("height", new String().valueOf(cellstyle.getBorderBottom() - cellstyle.getBorderTop()));

		// 增加reportElementelem到crosstab节点
		crosstabelem.addContent(reportElementelem);

		// 增加rowGroup节点
		Element rowgroupelem = new Element("rowGroup");
		rowgroupelem.setNamespace(ns);
		rowgroupelem.setAttribute("name", rowField);
		rowgroupelem.setAttribute("width", "92");
		rowgroupelem.setAttribute("totalPosition", "End");

		// 新增bucket节点
		Element bucketelem = new Element("bucket");
		bucketelem.setNamespace(ns);
		bucketelem.setAttribute("class", "java.lang.String");

		// 新增bucketExpression节点
		Element bucketExpressionelem = new Element("bucketExpression");
		bucketExpressionelem.setNamespace(ns);
		bucketExpressionelem.setText("$F{" + rowField + "}");
		bucketelem.addContent(bucketExpressionelem);
		// 增加bucket节点到rowgroup节点
		rowgroupelem.addContent(bucketelem);

		// 新增crosstabRowHeader节点
		Element crosstabRowHeaderelem = new Element("crosstabRowHeader");
		crosstabRowHeaderelem.setNamespace(ns);

		// 增加cellContents节点到crosstabRowHeader下面
		setCellContents(crosstabRowHeaderelem, ns, rowField, "1");

		// 增加crosstabRowHeader到rowGroup节点下面
		rowgroupelem.addContent(crosstabRowHeaderelem);

		// 增加crosstabTotalRowHeader节点
		Element crosstabTotalRowHeaderelem = new Element(
				"crosstabTotalRowHeader");
		crosstabTotalRowHeaderelem.setNamespace(ns);

		// 增加cellContents节点到crosstabTotalRowHeader下面
		setCellContents(crosstabTotalRowHeaderelem, ns, rowField, "0");

		// 增加crosstabTotalRowHeader到rowgroup下面
		rowgroupelem.addContent(crosstabTotalRowHeaderelem);

		// 增加rowgroup到crosstab节点下面
		crosstabelem.addContent(rowgroupelem);

		// 增加crosstab节点到band节点下面
		bandelem.addContent(crosstabelem);

	}

	// 生成交叉报表时构造共有的cellContents节点及其子节点
	// staticOrfieldFlag为1：textField节点，0：staticText节点的
	public void setCellContents(Element crosstabRowHeaderelem, Namespace ns,
			String rowField, String staticOrfieldFlag) {
		// 新增cellContents节点
		Element cellContentselem = new Element("cellContents");
		cellContentselem.setNamespace(ns);
		cellContentselem.setAttribute("backcolor", "#F0F8FF");
		cellContentselem.setAttribute("mode", "Opaque");
		crosstabRowHeaderelem.addContent(cellContentselem);
		if ("1".equals(staticOrfieldFlag)) {

			// 新增box节点
			Element boxelem = new Element("box");
			boxelem.setNamespace(ns);

			// 新增pen节点
			Element penelem = new Element("pen");
			penelem.setNamespace(ns);
			penelem.setAttribute("lineWidth", "0.5");
			penelem.setAttribute("lineStyle", "Solid");
			penelem.setAttribute("lineColor", "#000000");
			boxelem.addContent(penelem);
			cellContentselem.addContent(boxelem);

			// 新增textField节点
			Element textFieldelem = new Element("textField");
			textFieldelem.setNamespace(ns);

			// 新增reportElement节点
			Element textFieldreportElementelem = new Element("reportElement");
			textFieldreportElementelem.setNamespace(ns);
			textFieldreportElementelem.setAttribute("style",
					"Crosstab Data Text");
			textFieldreportElementelem.setAttribute("x", "0");
			textFieldreportElementelem.setAttribute("y", "0");
			textFieldreportElementelem.setAttribute("width", "92");
			textFieldreportElementelem.setAttribute("height", "25");
			textFieldelem.addContent(textFieldreportElementelem);

			// 新增box节点
			Element textFieldboxelem = new Element("box");
			textFieldboxelem.setNamespace(ns);

			// 新增topPen、leftPen、bottomPen、rightPen节点
			Element topPenelem = new Element("topPen");
			topPenelem.setNamespace(ns);
			topPenelem.setAttribute("lineWidth", "0.5");
			topPenelem.setAttribute("lineColor", "#CCCCCC");
			textFieldboxelem.addContent(topPenelem);

			Element leftPenelem = new Element("leftPen");
			leftPenelem.setNamespace(ns);
			leftPenelem.setAttribute("lineWidth", "0.5");
			leftPenelem.setAttribute("lineColor", "#CCCCCC");
			textFieldboxelem.addContent(leftPenelem);

			Element bottomPenelem = new Element("bottomPen");
			bottomPenelem.setNamespace(ns);
			bottomPenelem.setAttribute("lineWidth", "0.5");
			bottomPenelem.setAttribute("lineColor", "#CCCCCC");
			textFieldboxelem.addContent(bottomPenelem);

			Element rightPenelem = new Element("rightPen");
			rightPenelem.setNamespace(ns);
			rightPenelem.setAttribute("lineWidth", "0.5");
			rightPenelem.setAttribute("lineColor", "#CCCCCC");
			rightPenelem.addContent(bottomPenelem);

			textFieldelem.addContent(textFieldboxelem);

			// 新增textElement节点
			Element textElementelem = new Element("textElement");
			textElementelem.setNamespace(ns);
			textElementelem.setAttribute("verticalAlignment", "Middle");

			// 新增font节点
			Element fontelem = new Element("font");
			fontelem.setNamespace(ns);
			fontelem.setAttribute("fontName", "宋体");
			fontelem.setAttribute("size", "9");
			fontelem.setAttribute("pdfFontName", "STSong-Light");
			fontelem.setAttribute("pdfEncoding", "UniGB-UCS2-H");
			fontelem.setAttribute("isPdfEmbedded", "true");
			textElementelem.addContent(fontelem);
			textFieldelem.addContent(textElementelem);

			// 新增textFieldExpression节点
			Element textFieldExpressionelem = new Element("textFieldExpression");
			textFieldExpressionelem.setNamespace(ns);
			textFieldExpressionelem.setText("$V{" + rowField + "}");
			textFieldelem.addContent(textFieldExpressionelem);
			cellContentselem.addContent(textFieldelem);

		} else if ("0".equals(staticOrfieldFlag)) {

			// 新增staticText节点
			Element staticTextelem = new Element("staticText");
			staticTextelem.setNamespace(ns);

			// 新增reportElement节点
			Element textFieldreportElementelem = new Element("reportElement");
			textFieldreportElementelem.setNamespace(ns);
			textFieldreportElementelem.setAttribute("style",
					"Crosstab Data Text");
			textFieldreportElementelem.setAttribute("x", "0");
			textFieldreportElementelem.setAttribute("y", "0");
			textFieldreportElementelem.setAttribute("width", "92");
			textFieldreportElementelem.setAttribute("height", "25");
			staticTextelem.addContent(textFieldreportElementelem);

			// 新增box节点
			Element staticTextboxelem = new Element("box");
			staticTextboxelem.setNamespace(ns);

			// 新增topPen、leftPen、bottomPen、rightPen节点
			Element topPenelem = new Element("topPen");
			topPenelem.setNamespace(ns);
			topPenelem.setAttribute("lineWidth", "0.5");
			topPenelem.setAttribute("lineColor", "#CCCCCC");
			staticTextboxelem.addContent(topPenelem);

			Element leftPenelem = new Element("leftPen");
			leftPenelem.setNamespace(ns);
			leftPenelem.setAttribute("lineWidth", "0.5");
			leftPenelem.setAttribute("lineColor", "#CCCCCC");
			staticTextboxelem.addContent(leftPenelem);

			Element bottomPenelem = new Element("bottomPen");
			bottomPenelem.setNamespace(ns);
			bottomPenelem.setAttribute("lineWidth", "0.5");
			bottomPenelem.setAttribute("lineColor", "#CCCCCC");
			staticTextboxelem.addContent(bottomPenelem);

			Element rightPenelem = new Element("rightPen");
			rightPenelem.setNamespace(ns);
			rightPenelem.setAttribute("lineWidth", "0.5");
			rightPenelem.setAttribute("lineColor", "#CCCCCC");
			rightPenelem.addContent(bottomPenelem);

			staticTextelem.addContent(staticTextboxelem);

			// 新增textElement节点
			Element textElementelem = new Element("textElement");
			textElementelem.setNamespace(ns);
			textElementelem.setAttribute("verticalAlignment", "Middle");

			// 新增font节点
			Element fontelem = new Element("font");
			fontelem.setNamespace(ns);
			fontelem.setAttribute("fontName", "宋体");
			fontelem.setAttribute("size", "9");
			fontelem.setAttribute("pdfFontName", "STSong-Light");
			fontelem.setAttribute("pdfEncoding", "UniGB-UCS2-H");
			fontelem.setAttribute("isPdfEmbedded", "true");
			textElementelem.addContent(fontelem);
			staticTextboxelem.addContent(textElementelem);

			// 新增text节点
			Element textelem = new Element("text");
			textelem.setNamespace(ns);
			textelem.setText("总计值");
			staticTextboxelem.addContent(textelem);
			cellContentselem.addContent(staticTextboxelem);

		}

	}

	// 设置分组字段，及小计信息（Group）
	public void setGroupField(String jrxmlname, String groupfield,
			CellAllStyle cellstyle, String variables) {
		Element root = getDocument(jrxmlname).getRootElement();// 获得根元素
		Element groupelem = root.getChild("group", ns);
		Element groupExpressionelem = new Element("groupExpression");
		groupExpressionelem.setNamespace(ns);
		groupExpressionelem.setText(groupfield);
		groupelem.addContent(0, groupExpressionelem);

		// 设置小计字段在groupfoot区域
		Element groupfootelem = groupelem.getChild("groupFooter", ns);
		setBandinfo(groupfootelem, cellstyle);
		writerjrxml(jrxmlname);

	}

	// 设置Title
	public void setTitle(String jrxmlname, CellAllStyle cellstyle) {
		Element root = getDocument(jrxmlname).getRootElement();// 获得根元素
		Element titleelem = root.getChild("title", ns);
		setBandinfo(titleelem, cellstyle);
		writerjrxml(jrxmlname);
	}

	// 设置pageHeader
	public void setPageHeader(String jrxmlname, CellAllStyle cellstyle) {
		Element root = getDocument(jrxmlname).getRootElement();// 获得根元素
		Element pageHeader = root.getChild("pageHeader", ns);
		setBandinfo(pageHeader, cellstyle);
		writerjrxml(jrxmlname);
	}

	// 设置columnHeader
	public void setColumnHeader(String jrxmlname, CellAllStyle cellstyle) {
		Element root = getDocument(jrxmlname).getRootElement();// 获得根元素
		Element columnHeaderelem = root.getChild("columnHeader", ns);

		setBandinfo(columnHeaderelem, cellstyle);
		writerjrxml(jrxmlname);
	}

	// 设置detail(非子报表)
	public void setDetail(String jrxmlname, CellAllStyle cellstyle) {
		Element root = getDocument(jrxmlname).getRootElement();// 获得根元素
		Element detailelem = root.getChild("detail", ns);
		setBandinfo(detailelem, cellstyle);
		writerjrxml(jrxmlname);
	}

	// 设置columnFooter
	public void setColumnFooter(String jrxmlname, CellAllStyle cellstyle) {
		Element root = getDocument(jrxmlname).getRootElement();// 获得根元素
		Element columnfooterelem = root.getChild("columnFooter", ns);

		setBandinfo(columnfooterelem, cellstyle);
		writerjrxml(jrxmlname);
	}

	// 设置summary
	public void setSummary(String jrxmlname, CellAllStyle cellstyle) {
		Element root = getDocument(jrxmlname).getRootElement();// 获得根元素
		Element summaryelem = root.getChild("summary", ns);
		setBandinfo(summaryelem, cellstyle);
		writerjrxml(jrxmlname);
	}
}
