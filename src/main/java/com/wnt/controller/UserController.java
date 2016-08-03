package com.wnt.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wnt.util.JsonUtil;
import com.wnt.domain.User;
import com.wnt.domain.UserExtend;
import com.wnt.model.UserVO;
import com.wnt.service.IUserService;
import com.wnt.util.MD5Util;


	@Controller  
	@RequestMapping("/user")  
	public class UserController {  
		private static Logger logger = Logger.getLogger(UserController.class);
	    @Resource  
	    private IUserService userService; 
	    
	    @RequestMapping(value="/login",method = RequestMethod.POST, consumes="application/json")
		@ResponseBody  
	    public Map<String, String> login(@RequestBody UserVO userV,HttpSession session){
	    	String inputuserid = userV.getUserid();
	    	String inputpassword = userV.getPassword();
	    	User u = this.userService.getUserById(inputuserid);
	    	Map<String, String> map = new HashMap<String, String>();
	    	if(u == null){
	    		map.put("code", "101");
	    		map.put("msg", "用户不存在");
	    		logger.info("用户不存在");
	    	}else{
	    		if(verifyPassword(inputuserid,inputpassword)){
	    			map.put("code", "0");
		    		map.put("msg", "登陆成功");
		    		map.put("userid", u.getUserid());
		    		map.put("username", u.getUsername());
		    		session.setAttribute("userid", u.getUserid());
		    		session.setAttribute("username", u.getUsername());
	    			logger.info("登陆成功");
	    		}else{
	    			map.put("code", "102");
		    		map.put("msg", "密码错误");
		    		logger.info("密码错误");
	    		}
	    	}
	    	return map;
	    }
	    
	    @RequestMapping(value="/signout",method = RequestMethod.GET)
		@ResponseBody  
	    public Map<String, String> signout(HttpServletRequest request, HttpServletResponse response,HttpSession session){
	    	Map<String, String> map = new HashMap<String, String>();
	    	if(!session.getAttribute("userid").equals(null)){
	    		session.invalidate();
	    	}
	    	map.put("code", "0");
	    	map.put("msg","登出成功");
	    	logger.info("登出成功");
	    	return map;
	    	
	    }
	    
	    @RequestMapping(value="/changeu",method = RequestMethod.GET, consumes="application/json")
		@ResponseBody  
		public Map<String, String> changeUser(HttpServletRequest request, HttpServletResponse response,HttpSession session){
	    	Map<String, String> map = new HashMap<String, String>();
	    	Map<String, Object> paramMap = getResultMap(request);
	    	String userid = (String)paramMap.get("userid");
	    	User u = this.userService.getUserById(userid);
	    	session.setAttribute("userid", u.getUserid());
    		session.setAttribute("username", u.getUsername());
	    	map.put("code", "0");
	    	map.put("msg","切换成功");
	    	map.put("userid", u.getUserid());
    		map.put("username", u.getUsername());
	    	logger.info("切换成功");
			return map;
	    	
	    }
	    
	    @RequestMapping(value="/chgpswd",method = RequestMethod.GET)
		@ResponseBody  
		public Map<String, String> changePassword(HttpServletRequest request, HttpServletResponse response){
	    	Map<String, String> map = new HashMap<String, String>();
	    	Map<String, Object> paramMap = getResultMap(request);
	    	String userid = (String)paramMap.get("userid");
	    	String olduserpswd = (String)paramMap.get("olduserpswd");
	    	String newuserpswd = (String)paramMap.get("newuserpswd");
	    	User u = this.userService.getUserById(userid);
	    	String pswdsalt = u.getPswdsalt();
	    	String newpassword = MD5Util.MD5(newuserpswd+"#"+pswdsalt).toUpperCase();
    		if(verifyPassword(userid,olduserpswd)){
    			User user = new User();
    	    	user.setUserid(userid);
    	    	user.setPassword(newpassword);
    	    	int r = this.userService.updateUser(user);
    	    	logger.debug(r);
    	    	map.put("code", "0");
	    		map.put("msg", "修改成功");
    		}else{
    			map.put("code", "102");
	    		map.put("msg", "密码错误");
	    		logger.info("密码错误");
    		}
	    	return map;
	    }
	    
	    @RequestMapping(value="/list" ,produces= "application/json;charset=UTF-8")
		@ResponseBody  
		public String getUserList(){
	    	List<User> userList = this.userService.getAllUsers();
	    	String json = "[";
	    	for(int i=0;i<userList.size();i++){
	    		String userid = userList.get(i).getUserid();
		    	String username = userList.get(i).getUsername();
		    	if(i != userList.size()-1){
	    		json+="{\"userid\":\""+userid+"\",\"username\":\""+username+"\"},";
		    	}else{
		    		json+="{\"userid\":\""+userid+"\",\"username\":\""+username+"\"}";
		    	}
	    	}
	    	json+="]";
			return json;
	    }
	    
	    @RequestMapping(value="/extendlist" ,produces= "application/json;charset=UTF-8")
		@ResponseBody  
		public String getUserExtendList(){
	    	List<UserExtend> userList = this.userService.getAllUserExtends();
//	    	String json = "[";
//	    	for(int i=0;i<userList.size();i++){
//	    		String userid = userList.get(i).getUserid();
//		    	String username = userList.get(i).getUsername();
//		    	if(i != userList.size()-1){
//	    		json+="{\"userid\":\""+userid+"\",\"username\":\""+username+"\"},";
//		    	}else{
//		    		json+="{\"userid\":\""+userid+"\",\"username\":\""+username+"\"}";
//		    	}
//	    	}
//	    	json+="]";
	    	String json = JsonUtil.obj2json(userList);
	    	String jsondata = "{\"page\":\"1\"," +
			        "      \"total\":1," +
			        "      \"records\":\"2\"," +
			        "      \"rows\":"  + json +
			        "    }";

			return jsondata;
	    }
		
	  private Boolean verifyPassword(String inputuserid,String inputpassword){
		  Boolean pswdOk = false;
		  User u = this.userService.getUserById(inputuserid);
		  String password = u.getPassword();
  		  String pswdsalt = u.getPswdsalt();
  		  String verify = MD5Util.MD5(inputpassword+"#"+pswdsalt).toUpperCase();
  		  if(verify.equals(password)){
  			pswdOk=true;
  		  }else{
  			pswdOk=false;
  		  }
		  return pswdOk;
	  }
	  
	  private Map<String, Object> getResultMap(HttpServletRequest request) {
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

