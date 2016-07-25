package com.wnt.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

public class PubComm {

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
}
