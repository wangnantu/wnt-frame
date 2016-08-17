package com.wnt.ireport.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import java.util.regex.PatternSyntaxException;

import javax.servlet.http.HttpServletRequest;

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
}
