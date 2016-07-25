package com.wnt.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wnt.domain.UserAction;
import com.wnt.service.IUserActionService;
import com.wnt.util.PubComm;

@Controller  
@RequestMapping("/action")  
public class UserActionController {
	private static Logger logger = Logger.getLogger(UserController.class);
	@Resource  
    private IUserActionService userActionService;
	
	@RequestMapping(value="/get",method = RequestMethod.GET, consumes="application/json")
	@ResponseBody 
	public String getUserActionsByUseridAndMenuid(HttpServletRequest request, HttpServletResponse response,HttpSession session){
		
    	Map<String, Object> paramMap = PubComm.getResultMap(request);
    	String userid = (String)paramMap.get("userid");
    	int menuid = Integer.parseInt((String)paramMap.get("menuid"));
    	List<UserAction> actionList = userActionService.getUserActionsByUseridAndMenuid(userid, menuid);
    	String json = "[";
    	for(int i=0;i<actionList.size();i++){
    		int actionid = actionList.get(i).getActionid();
	    	String actionname = actionList.get(i).getActionname();
	    	if(i != actionList.size()-1){
    		json+="{\"actionid\":\""+actionid+"\",\"actionname\":\""+actionname+"\"},";
	    	}else{
	    		json+="{\"actionid\":\""+actionid+"\",\"actionname\":\""+actionname+"\"}";
	    	}
    	}
    	json+="]";
		return json;
		
	}
}
