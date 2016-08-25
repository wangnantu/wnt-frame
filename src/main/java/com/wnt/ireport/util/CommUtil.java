package com.wnt.ireport.util;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import java.util.regex.PatternSyntaxException;

import javax.servlet.http.HttpServletRequest;
import org.springframework.dao.DataAccessException;

import com.wnt.util.SingleGetCon;

public class CommUtil {

	 public static Map<String, Object> getResultMap(HttpServletRequest request) {
		 Map<String, String[]> map = request.getParameterMap();
		Map<String,Object> mapParams = new HashMap<String,Object>();
		for (Entry<String, String[]> entry : map.entrySet()) {
				String[] arr = entry.getValue();
				if ( arr.length == 1)
					mapParams.put( entry.getKey(), arr[0]);
				else {
					mapParams.put( entry.getKey(), arr);
				}
		}
		return mapParams;
	}
	 
		/**
		 * 得到服务器下当前项目的名称目录
		 * 
		 * @return
		 */
	 public static String getProjectPath() {
			String projectPath = Thread.currentThread().getContextClassLoader()
					.getResource("").getPath();

			projectPath = projectPath.substring(1);
			projectPath = projectPath.substring(0, projectPath.indexOf("WEB-INF"));

			try {
				projectPath = URLDecoder.decode(projectPath, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return projectPath;
		}
	 
	// 取系统时间
		public static String getDate2() throws PatternSyntaxException {
			java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
					"yyyy-MM-dd");
			java.util.Date currentTime = new java.util.Date();
			String str_date = formatter.format(currentTime);
			StringTokenizer token = new StringTokenizer(str_date, "-");
			String year = token.nextToken();
			String month = token.nextToken();
			String day = token.nextToken();
			String date = year + month + day;
			return date;
		}
		

		// 取系统时间
		public static String getsysid() throws PatternSyntaxException {
			java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(
					"yyyy-MM-dd-HH-mm-ss-SSS");
			java.util.Date currentTime = new java.util.Date();
			String str_date = formatter.format(currentTime);
			StringTokenizer token = new StringTokenizer(str_date, "-");
			String year = token.nextToken();
			String month = token.nextToken();
			String day = token.nextToken();
			String hh = token.nextToken();
			String mm = token.nextToken();
			String ss = token.nextToken();
			String sss = token.nextToken();
			String date = year + month + day;
			String time = hh + mm + ss + sss;
			return date + time;
		}
		
		public static String getsysguid32() {
			String guid=java.util.UUID.randomUUID().toString();
			guid=guid.replaceAll("-", "");
			return guid;
		}
		public static String getsysguid50() {
			String guid=java.util.UUID.randomUUID().toString();
			guid=getsysid()+guid.replaceAll("-", "");
			return guid;
		}
		
		public static String getValueBySql(String sqlStr) throws DataAccessException {
			Connection connection = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String rtnval="";
			try {
				SingleGetCon.getInitGetcon();
				connection =  SingleGetCon.getConnection();
				pstmt = connection.prepareStatement(sqlStr);
	            rs = pstmt.executeQuery();
	            while(rs.next()){
	            	rtnval=rs.getString(1);
	            	break;
	            }
	            
			} catch (Exception e) {
				System.out.println(e.getStackTrace());
			} finally {
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
	                if (connection != null) {
	                	connection.close();
	                }
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
			}
			return rtnval;
		}
		
		public static String getIpAddr(HttpServletRequest request) {
			String ip = "";
			String localAddress="";
			try{
				localAddress=getsysGlobalVariableBy("localAddress");
			}catch(Exception e){
				
			}
			if("".equalsIgnoreCase(localAddress)){
				ip = request.getHeader("x-forwarded-for");
				if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
					ip = request.getHeader("Proxy-Client-IP"); 
				}
				if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
					ip = request.getHeader("WL-Proxy-Client-IP"); 
				} 
				if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
					ip = request.getRemoteAddr(); 
				}
			}else{
				ip = localAddress;
			}
			return ip;
		} 
		
		//使用相对定位的方法从文件取得系统全局变量
		public static String getsysGlobalVariableBy(String key){
			// 生成输入流  
	        InputStream ins=CommUtil.class.getResourceAsStream("/systemGlobalVariable.properties");  
	        // 生成properties对象  
	        Properties p = new Properties();  
	        try {  
	            p.load(ins);  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        } 
	        String returnValue=p.getProperty(key);
			return returnValue;
		}
		
		public static void SetLog(String tab_name, String curruserid, String OpType, String detail,String Ip)
				throws DataAccessException {
					Connection connection = null;
					CallableStatement cstmt = null;
					String currdate = getDate2();
					try {
						SingleGetCon.getInitGetcon();
						connection =  SingleGetCon.getConnection();
						cstmt = connection.prepareCall("{call pkg_tech_pub.UP_SetLog(?,?,?,?,?,?)}");
						cstmt.setString(1, currdate);
						cstmt.setString(2, tab_name);
						cstmt.setString(3, curruserid);
						cstmt.setString(4, OpType);
						cstmt.setString(5, detail);
						cstmt.setString(6, Ip);
						cstmt.executeUpdate();
					} catch (Exception e) {
						System.out.println(e.getStackTrace());
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
		
		public static void writeLogfile(String str)
		{
		
		}
}
