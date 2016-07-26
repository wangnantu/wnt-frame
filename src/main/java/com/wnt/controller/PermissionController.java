package com.wnt.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wnt.domain.Permission;
import com.wnt.domain.Menu;
import com.wnt.service.IMenuService;
import com.wnt.service.IPermissionService;
import com.wnt.util.PubComm;

@Controller  
@RequestMapping("/permission")  
public class PermissionController {
	private static Logger logger = Logger.getLogger(UserController.class);
	@Resource  
    private IPermissionService permissionService;
	
	@Resource
	private IMenuService menuService;
	
	@RequestMapping(value="/actions",method = RequestMethod.GET, consumes="application/json")
	@ResponseBody 
	public String getUserActionsByUseridAndMenuid(HttpServletRequest request, HttpServletResponse response){
		
    	Map<String, Object> paramMap = PubComm.getResultMap(request);
    	String userid = (String)paramMap.get("userid");
    	int menuid = Integer.parseInt((String)paramMap.get("menuid"));
    	List<Permission> actionList = permissionService.getUserActionsByUseridAndMenuid(userid, menuid);
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
	
	@RequestMapping(value="/menus",method = RequestMethod.GET, produces="application/json;charset=UTF-8")
	@ResponseBody 
	public String getMenusByUserid(HttpServletRequest request, HttpServletResponse response){
		
		Map<String, Object> paramMap = PubComm.getResultMap(request);
    	String userid = (String)paramMap.get("userid");
    	List<Integer> menuList = menuService.getMenusByUserid(userid);
    	//List<Menu> menus = new ArrayList<Menu>();
    	String json = "[";
    	for(int i=0;i<menuList.size();i++){
    		Menu menu = menuService.getMenuById(menuList.get(i));
    		int menuid = menu.getMenuid();
    		int parentid = menu.getParentid();
    		String menuname = menu.getMenuname();
    		String url = menu.getUrl();
    		String icon = menu.getIcon();
    		int soncnt = menuService.getSoncnt(menuid);
    		if(i != menuList.size()-1){
    			json+="{\"menuid\":\""+menuid+"\",\"parentid\":\""+parentid+"\",\"menuname\":\""+menuname+"\",\"url\":\""+url+"\",\"icon\":\""+icon+"\",\"soncnt\":\""+soncnt+"\"},";
    		}else{
    			json+="{\"menuid\":\""+menuid+"\",\"parentid\":\""+parentid+"\",\"menuname\":\""+menuname+"\",\"url\":\""+url+"\",\"icon\":\""+icon+"\",\"soncnt\":\""+soncnt+"\"}";
    		}
    	}
    	json+="]";
    	logger.info(json);
		return json;
	}
}
