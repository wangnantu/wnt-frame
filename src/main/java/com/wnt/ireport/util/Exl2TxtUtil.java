package com.wnt.ireport.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;

import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFWriter;

public class Exl2TxtUtil {
	
	private static double PIX_RATE;
	
	private static void setPixRate(){
		  PIX_RATE = 47;
	}
	
	/**     * 获取单元格数据内容为字符串类型的数据     *      
	 * * @param cell Excel单元格     
	 * * @return String 单元格数据内容     
	 * */    
	private static String getStringCellValue(Cell cell) {        
		String strCell = "";        
		switch (cell.getCellType()) {        
			case Cell.CELL_TYPE_STRING:            
				strCell = cell.getStringCellValue();            
				break;        
			case Cell.CELL_TYPE_NUMERIC:          
				strCell = String.valueOf(cell.getNumericCellValue());
				break;        
			case Cell.CELL_TYPE_BOOLEAN:            
				strCell = String.valueOf(cell.getBooleanCellValue());            
				break;        
			case Cell.CELL_TYPE_BLANK:            
				strCell = "";            
				break;        
			default:            
				strCell = "";            
				break;        
		}        
		if (strCell.equals("") || strCell == null) {            
			return "";        
		}        
		        
		return strCell;    
	}
	
	/**     * 获取单元格数据内容为日期类型的数据     *      
	 * * @param cell     *            Excel单元格     
	 * * @return String 单元格数据内容     
	 * */    
	@SuppressWarnings({ "unused", "deprecation" })
	private static String getDateCellValue(Cell cell) {        
		String result = "";        
		try {            
			int cellType = cell.getCellType();            
			if (cellType == Cell.CELL_TYPE_NUMERIC) {                
				Date date = cell.getDateCellValue();                
				result = (date.getYear() + 1900) + "-" + (date.getMonth() + 1) + "-" + date.getDate();            
			} else if (cellType == Cell.CELL_TYPE_STRING) {                
					String date = getStringCellValue(cell);                
					result = date.replaceAll("[年月]", "-").replace("日", "").trim();            
			} else if (cellType == Cell.CELL_TYPE_BLANK) {                
					result = "";            
			}        
		} catch (Exception e) {            
			System.out.println("日期格式不正确!");            
			e.printStackTrace();        
		}        
		return result;    
	}
	
	/**     * 根据HSSFCell类型设置数据     
	 * * @param cell     
	 * * @return     
	 * */    
	public static String getCellFormatValue(Cell cell) {        
		String cellvalue = "";        
		if (cell != null) {            
			// 判断当前Cell的Type            
			switch (cell.getCellType()) {            
				// 如果当前Cell的Type为NUMERIC 数值型     
				case Cell.CELL_TYPE_NUMERIC:{
					// 如果是纯数字
					String formatstring = cell.getCellStyle().getDataFormatString();
					DecimalFormat fmt = null;

					//#,##0.00_);[Red]\\(#,##0.00\\)   #,##0.00_);\\(#,##0.00\\)
					if(Pattern.compile("^#,#+0.0+").matcher(formatstring).find()){
						String temp = "";
						if(formatstring.indexOf("_")>-1){
							temp = formatstring.substring(formatstring.indexOf(".")+1,formatstring.indexOf("_"));
						}else {
							temp = formatstring.substring(formatstring.indexOf(".")+1);
						}
						fmt = new DecimalFormat("#,##0."+temp);
						cellvalue = String.valueOf(fmt.format(cell.getNumericCellValue()));
					}
					//0.00_);[Red]\\(0.00\\)    0.00_);\\(0.00\\)    #0.00
					else if (Pattern.compile("^(#)*0.0+").matcher(formatstring).find()) {
						String temp = "";
						if(formatstring.indexOf("_")>-1){
							temp = formatstring.substring(formatstring.indexOf(".")+1,formatstring.indexOf("_"));
						}else {
							temp = formatstring.substring(formatstring.indexOf(".")+1);
						}
						fmt = new DecimalFormat("###0."+temp);
						cellvalue = String.valueOf(fmt.format(cell.getNumericCellValue()));
					}
					//#,##0_);[Red]\\(#,##0\\)     #,##0_);\\(#,##0\\)
					else if (Pattern.compile("^#,#+0").matcher(formatstring).find()) {
						fmt = new DecimalFormat("#,##0");
						cellvalue = String.valueOf(fmt.format(cell.getNumericCellValue()));
					}
					//0_);[Red]\\(0\\)     0_);\\(0\\)
					else if (Pattern.compile("^0").matcher(formatstring).find()) {
						fmt = new DecimalFormat("###0");
						cellvalue = String.valueOf(fmt.format(cell.getNumericCellValue()));
					}else {
						fmt = new DecimalFormat("###0");
						cellvalue = String.valueOf(fmt.format(cell.getNumericCellValue()));
					}
					
					break;
				}            
				case Cell.CELL_TYPE_FORMULA: {         
					// 判断当前的cell是否为Date                
					if (DateUtil.isCellDateFormatted(cell)) {                    
						// 如果是Date类型则，转化为Data格式                                        
						//方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00                    
						//cellvalue = cell.getDateCellValue().toLocaleString();                                        
						//方法2：这样子的data格式是不带带时分秒的：2011-10-12                    
						Date date = cell.getDateCellValue();                    
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");                    
						cellvalue = sdf.format(date);                                    
					}           
					break;            
				}            
				// 如果当前Cell的Type为STRIN            
				case Cell.CELL_TYPE_STRING:                
					// 取得当前的Cell字符串                
					cellvalue = cell.getRichStringCellValue().getString();                
					break;            
					// 默认的Cell值           
				default:                
					cellvalue = " ";            
			}        
		} else {            
			cellvalue = "";        
		}        
		return cellvalue;    
	}
	
	//读取整个excel文件，返回excel内容集合
	public static Map<Integer,List<List<Map<String, Object>>>> readtargetexl(String targetexlpath){
		Map<Integer,List<List<Map<String, Object>>>> map = new HashMap<Integer,List<List<Map<String, Object>>>>();
		try {
			Workbook workbook = WorkbookFactory.create(new FileInputStream(targetexlpath));
			int sheetcount = workbook.getNumberOfSheets();
			for (int i = 0; i < sheetcount; i++) {
				List<List<Map<String, Object>>> sheetlist = new ArrayList<List<Map<String, Object>>>();
				Sheet sheet = workbook.getSheetAt(i);
				for (Iterator<Row> rows = sheet.iterator();rows.hasNext();) {
					List<Map<String, Object>> rowlist = new ArrayList<Map<String, Object>>();
					Row row = rows.next();
					int colcount = row.getPhysicalNumberOfCells();
					for (int j = 0; j < colcount; j++) {
						Map<String, Object> celllist = new HashMap<String, Object>();
						celllist.put("value", getCellFormatValue(row.getCell(j)));
						celllist.put("width", sheet.getColumnWidth(j));
						CellStyle cellStyle = row.getCell(j).getCellStyle();
						celllist.put("align", cellStyle.getAlignment());
						rowlist.add(celllist);
					}
					sheetlist.add(rowlist);
				}
				map.put(i, sheetlist);
			}
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	//字符两边添加空格
	private static String stringCenterPadding(String srcstr,double spacecount){
		StringBuilder str = new StringBuilder("");
		StringBuilder spacestr = new StringBuilder("");
		if((int)Math.rint(spacecount)==0){
			return srcstr;
		}else if ((int)Math.rint(spacecount)==1) {
			spacestr.append(" ");
			return srcstr + spacestr;
		}else {
			int leftsapcecount = 0;
			if((int)Math.rint(spacecount)%2!=0){
				leftsapcecount = (int)Math.floor(spacecount / 2);
			}else {
				leftsapcecount = (int)Math.rint(spacecount) / 2;
			}
			
			for (int i = 0; i < leftsapcecount; i++) {
				spacestr.append(" ");
			}
			str.append(spacestr).append(srcstr);
			int rightsapcecount = 0;
			if((int)Math.rint(spacecount)%2!=0){
				rightsapcecount = (int)Math.ceil(spacecount / 2);
			}else {
				rightsapcecount = (int)Math.rint(spacecount) / 2;
			}
			
			spacestr = new StringBuilder("");
			
			for (int i = 0; i < rightsapcecount; i++) {
				spacestr.append(" ");
			}
			str.append(spacestr);
			return str.toString();
		}
	}
	
	//字符左边添加空格
	private static String stringLeftPadding(String srcstr,double spacecount){
		String str = "";
		String spacestr = "";
		for (int i = 0; i < (int)Math.rint(spacecount); i++) {
			spacestr += " ";
		}
		str = spacestr + srcstr;
		return str;
	}
	
	//字符右边添加空格
	private static String stringRightPadding(String srcstr,double spacecount){
		String str = "";
		String spacestr = "";
		for (int i = 0; i < (int)Math.rint(spacecount); i++) {
			spacestr += " ";
		}
		str = srcstr + spacestr;
		return str;
	}
	
	//字符内容按对齐方式添加空格
	private static String stringPading(String srcstr,int align,double cellwidth,int betwFieldSpace){
		String str = "";
		double spacecount = cellwidth - srcstr.getBytes().length;
		switch (align) {
			case CellStyle.ALIGN_CENTER:{
				str = str + stringCenterPadding(srcstr, spacecount);
				str = stringLeftPadding(str, betwFieldSpace);
				break;
			}
			case CellStyle.ALIGN_LEFT:{
				str = str + stringRightPadding(srcstr, spacecount);
				str = stringLeftPadding(str, betwFieldSpace);
				break;
			}
			case CellStyle.ALIGN_RIGHT:{
				str = str + stringLeftPadding(srcstr, spacecount);
				str = stringLeftPadding(str, betwFieldSpace);
				break;
			}

			default:{
				str = str + stringRightPadding(srcstr, spacecount);
				str = stringLeftPadding(str, betwFieldSpace);
				break;
			}
		}
		return str;
	}
	
	//判断单元格是否被合并
	public static String iscombine(Sheet sheetcur,List<String[]> rptRegionList,int rptidex,int rowInd, int colInd)
	{
		String brtn="0";
		String rptid=Integer.toString(rptidex);
		//rptRegionList = new ArrayList<String[]>();//存放所有报表sheet里的合并项(rptid,CellRangeAddressIndex,是否取过)
		for(int i=0;i<rptRegionList.size();i++){
			String tmprptid=rptRegionList.get(i)[0];
			//String Cridx=rptRegionList.get(i)[1];
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
	
	//获取合并单元格高度、宽度
	public static double[] getcmbcellWH(Sheet sheetcur,List<String[]> rptRegionList,int rptidex,int rowInd, int colInd,double perPix,double oneSpace)
	{
		setPixRate();
		
		double[] wh = {0,0};
		String rptid=Integer.toString(rptidex);
		//rptRegionList = new ArrayList<String[]>();//存放所有报表sheet里的合并项(rptid,CellRangeAddressIndex,是否取过)
		for(int i=0;i<rptRegionList.size();i++){
			String tmprptid=rptRegionList.get(i)[0];
			//String Cridx=rptRegionList.get(i)[1];
			//String ised=rptRegionList.get(i)[2];
			CellRangeAddress rg = sheetcur.getMergedRegion(i);
			if(rg.isInRange(rowInd, colInd) && tmprptid.equalsIgnoreCase(rptid))
			{
				int wl=rg.getLastColumn(),wf=rg.getFirstColumn();
				//int rl=rg.getLastRow(),rf=rg.getFirstRow();
				double wd=0;
				//int ht=0;
				/*for(int m=rf;m<=rl;m++){
					ht=ht+(int)sheetcur.getRow(m).getHeightInPoints();
				}*/
				for(int n=wf;n<=wl;n++){
					//wd=wd+sheetcur.getColumnWidth(n)/256;
					wd=wd+sheetcur.getColumnWidth(n)/Exl2TxtUtil.PIX_RATE/perPix*oneSpace;
				}
				//wh[0]=ht;
				wh[1]=wd;
				break;
			}
		}
		return wh;
	}
	
	//读取整个excel文件，返回excel内容字符串
	public static String[] readexl(String targetexlpath,double perPix,double oneSpace,int betwFieldSpace){
		String[] exlstring = null;
		try {
			setPixRate();
			
			Workbook workbook = WorkbookFactory.create(new FileInputStream(targetexlpath));
			int sheetcount = workbook.getNumberOfSheets();
			
			exlstring = new String[sheetcount];
			
			for (int i = 0; i < sheetcount; i++) {
				StringBuilder builder = new StringBuilder();
				
				Sheet sheet = workbook.getSheetAt(i);
				List<String[]> rptRegionList = new ArrayList<String[]>();
				
				//取sheet里的合并单元格数
				int rptrgcnt = sheet.getNumMergedRegions();
				for(int rg = 0; rg < rptrgcnt; rg++){
					//String ss=sheet.getMergedRegion(rg).toString();
					String[] temp = new String[3];
					temp[0] = Integer.toString(i);
					temp[1] = Integer.toString(rg);
					temp[2] = "";
					rptRegionList.add(temp);
				}
				
				int rows = sheet.getLastRowNum() - sheet.getFirstRowNum();//sheet行数
				
				String currcell = "";
				
				for (int m = 0; m <= rows; m++) {
					Row row = sheet.getRow(m);
					if(row == null) break;
					
					int celllen = row.getLastCellNum();//sheet列数
					
					double cellwd = 0;
					for(int n = 0; n < celllen; n++){
						Cell cell = row.getCell((short)n);
						
						if(cell == null)continue;
						currcell = getCellFormatValue(cell);    //单元值
						String iscmflag = iscombine(sheet,rptRegionList,i,m, n);//是否合并的单元格
						
						if(iscmflag.equals("1")){
							double[] tmp = getcmbcellWH(sheet,rptRegionList,i,m, n,perPix,oneSpace);
							cellwd = tmp[1];
							continue;
						}else if(iscmflag.equals("2")){
							double[] tmp = getcmbcellWH(sheet,rptRegionList,i,m, n,perPix,oneSpace);
							cellwd = tmp[1];
						}else{
							cellwd = sheet.getColumnWidth(n)/Exl2TxtUtil.PIX_RATE/perPix*oneSpace;
						}
						
						CellStyle eStyle = cell.getCellStyle();
						int cellalign = eStyle.getAlignment();
						
						currcell = stringPading(currcell, cellalign, cellwd, betwFieldSpace);
						builder.append(currcell);
						   
					}
					if (m!=rows) {
						builder.append("\r\n");
					}
				}
				exlstring[i] = builder.toString();
			}
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return exlstring;
	}
	
	//先读取整个excel文件，然后再写入txt文件
	public static String[] exl2txt(double perPix,double oneSpace,int betwFieldSpace,String targetexlpath,String outtxtpath){
		String[] filenames = null;
		
		try {
			String[] contents = readexl(targetexlpath,perPix,oneSpace,betwFieldSpace);
			filenames = new String[contents.length];
			for (int i = 0; i < contents.length; i++) {
				String content = contents[i];
				
				String filename = targetexlpath.substring(0, targetexlpath.lastIndexOf("\\")+1);
				filename = targetexlpath.substring(filename.length(),targetexlpath.lastIndexOf("."));
				String filepath = outtxtpath+File.separator+filename+File.separator;
				
				File file = new File(filepath);
				if(!file.exists()){
					file.mkdir();
				}
				
				FileOutputStream fos = new FileOutputStream(filepath+filename+"_"+i+".txt");
				
				filenames[i] = filepath+filename+"_"+i+".txt";
				
				fos.write(content.getBytes());
				fos.flush();
				fos.close();
			}
			
			//pubcomm.zipFile(filenames);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filenames;
	}
	
	//按行读取excel文件，然后按行写入txt文件
	public static String[] exltotxt(double perPix,double oneSpace,int betwFieldSpace,String targetexlpath,String outtxtpath,String outtxtname){
		String[] filenames = null;
		try {
			setPixRate();
			
			Workbook workbook = WorkbookFactory.create(new FileInputStream(targetexlpath));
			int sheetcount = workbook.getNumberOfSheets();
			
			filenames = new String[sheetcount];
			
			for (int i = 0; i < sheetcount; i++) {
				String filename = targetexlpath.substring(0, targetexlpath.lastIndexOf(File.separator)+1);
				filename = targetexlpath.substring(filename.length(),targetexlpath.lastIndexOf("."));
				String filepath = outtxtpath+File.separator+filename+File.separator;
				
				File file = new File(filepath);
				if(!file.exists()){
					file.mkdir();
				}
				
				Sheet sheet = workbook.getSheetAt(i);
				String sheetname = sheet.getSheetName();
				
				FileOutputStream fos = null;
				if(sheetcount==1){
					if(outtxtname==null || "".equals(outtxtname)){
						fos = new FileOutputStream(filepath+sheetname+".txt");
						filenames[i] = filepath+sheetname+".txt";
					}else {
						fos = new FileOutputStream(filepath+outtxtname+".txt");
						filenames[i] = filepath+outtxtname+".txt";
					}
				}else {
					fos = new FileOutputStream(filepath+sheetname+".txt");
					filenames[i] = filepath+sheetname+".txt";
				}
				
				List<String[]> rptRegionList = new ArrayList<String[]>();
				
				//取sheet里的合并单元格数
				int rptrgcnt = sheet.getNumMergedRegions();
				for(int rg = 0; rg < rptrgcnt; rg++){
					//String ss=sheet.getMergedRegion(rg).toString();
					String[] temp = new String[3];
					temp[0] = Integer.toString(i);
					temp[1] = Integer.toString(rg);
					temp[2] = "";
					rptRegionList.add(temp);
				}
				
				int rows = sheet.getLastRowNum() - sheet.getFirstRowNum();//sheet行数
				
				for (int m = 0; m <= rows; m++) {
					Row row = sheet.getRow(m);
					if(row == null) break;
					
					StringBuilder builder = new StringBuilder();
					
					int celllen = row.getLastCellNum();//sheet列数
					
					double cellwd = 0;
					for(int n = 0; n < celllen; n++){
						Cell cell = row.getCell((short)n);
						
						if(cell == null)continue;
						String currcell = getCellFormatValue(cell);    //单元值
						String iscmflag = iscombine(sheet,rptRegionList,i,m, n);//是否合并的单元格
						
						if(iscmflag.equals("1")){
							double[] tmp = getcmbcellWH(sheet,rptRegionList,i,m, n,perPix,oneSpace);
							cellwd = tmp[1];
							continue;
						}else if(iscmflag.equals("2")){
							double[] tmp = getcmbcellWH(sheet,rptRegionList,i,m, n,perPix,oneSpace);
							cellwd = tmp[1];
						}else{
							cellwd = sheet.getColumnWidth(n)/Exl2TxtUtil.PIX_RATE/perPix*oneSpace;
						}
						
						CellStyle eStyle = cell.getCellStyle();
						int cellalign = eStyle.getAlignment();
						
						if(n==0){
							currcell = stringPading(currcell, cellalign, cellwd, 0);
						}else {
							currcell = stringPading(currcell, cellalign, cellwd, betwFieldSpace);
						}
						
						builder.append(currcell);
						
					}
					if (m!=rows) {
						builder.append("\r\n");
					}
					
					fos.write(builder.toString().getBytes());
					
				}
				fos.flush();
				fos.close();
			}
			
			
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filenames;
	}
	
	public static String[] exltoDBF(String targetexlpath,String outtxtpath,String outtxtname){
		String[] filenames = null;
		int first=1;
		try {
			setPixRate();
			
			Workbook workbook = WorkbookFactory.create(new FileInputStream(targetexlpath));
			int sheetcount = workbook.getNumberOfSheets();
			
			filenames = new String[sheetcount];
			
			for (int i = 0; i < sheetcount; i++) {
				String filename = targetexlpath.substring(0, targetexlpath.lastIndexOf(File.separator)+1);
				filename = targetexlpath.substring(filename.length(),targetexlpath.lastIndexOf("."));
				String filepath = outtxtpath+File.separator+filename+File.separator;
				
				File file = new File(filepath);
				if(!file.exists()){
					file.mkdir();
				}
				
				Sheet sheet = workbook.getSheetAt(i);
				String sheetname = sheet.getSheetName();
				
				FileOutputStream fos = null;
				if(sheetcount==1){
					if(outtxtname==null || "".equals(outtxtname)){
						fos = new FileOutputStream(filepath+sheetname+".DBF");
						filenames[i] = filepath+sheetname+".DBF";
					}else {
						fos = new FileOutputStream(filepath+outtxtname+".DBF");
						filenames[i] = filepath+outtxtname+".DBF";
					}
				}else {
					fos = new FileOutputStream(filepath+sheetname+".DBF");
					filenames[i] = filepath+sheetname+".DBF";
				}
				
				DBFWriter writer = new DBFWriter();
				writer.setCharactersetName("UTF-8");
				
				int rows = sheet.getLastRowNum() - sheet.getFirstRowNum();//sheet行数
				
				for (int m = first; m <= rows; m++) {
					Row row = sheet.getRow(m);
					if(row == null) break;
					
					int celllen = row.getLastCellNum();//sheet列数
					if(celllen==1){
						continue;
					}
					
					DBFField[] fields = new DBFField[celllen];
					Object[] rowData = new Object[celllen];
					if(m==first){
						for (int j = 0; j < fields.length; j++) {
							fields[j] = new DBFField();
						}
					}else {
						for (int j = 0; j < rowData.length; j++) {
							rowData[j] = new Object();
						}
					}
					
					for(int n = 0; n < celllen; n++){
						Cell cell = row.getCell((short)n);
						
						String currcell = " ";
						if(cell == null){
							currcell = " ";
						}else {
							currcell = getCellFormatValue(cell);    //单元值
						}
						
						Pattern pWord = Pattern.compile("[\u4e00-\u9fa5]");
						if(m==0){
							if(pWord.matcher(currcell).find() && currcell.getBytes().length>10){
								currcell = currcell.substring(0, 5);
							}else if(currcell.length()<=0){
								currcell = " ";
							}
						}
						
						if(pWord.matcher(currcell).find() && currcell.getBytes().length<5){
							int currcelllen = currcell.getBytes().length;
							String blank = "";
							for (int j = 0; j < currcelllen; j++) {
								blank += " ";
							}
							currcell += blank;
						}else if(pWord.matcher(currcell).find() && currcell.getBytes().length>50){
							currcell = currcell.substring(0, 25);
						}else if(currcell.getBytes().length>50){
							currcell = currcell.substring(0, 50);
						}
						
						if(m==first){
							fields[n].setName(currcell);
							fields[n].setDataType(DBFField.FIELD_TYPE_C);
							fields[n].setFieldLength(50);
							
						}else {
							
							rowData[n] = currcell;
						}
					}
					if(m==first){
						writer.setFields(fields);
					}else {
						writer.addRecord(rowData);
					}
					
				}
				
				writer.write(fos);
				
				fos.flush();
				fos.close();
				
			}
			
			//pubcomm.zipFile(filenames);
			
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filenames;
	}
	
	public static void main(String[] args) {
		//两种方式
		//一种按行读取excel，然后按行写入txt文件
		//System.out.println(pubcomm.getsysid());
		exltoDBF("e:\\123.xlsx", "e:\\", "test");
		//System.out.println(pubcomm.getsysid());
		//一种是先读取整个excel,然后全部写入txt文件
		//exl2txt("d:/报表/新模板.xlsx", "d:/报表/IB汇总2.xls","d:/报表/new.txt");
	}
}
