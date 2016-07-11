package com.wnt.controller;


import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wnt.domain.User;
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
	    	
	    	String inputUserid = userV.getUserid();
	    	
	    	String inputpassword = userV.getPassword();
	    	
	    	User u = this.userService.getUserById(inputUserid);
	    	
	    	Map<String, String> map = new HashMap<String, String>();
	    	
	    	if(u == null){
	    		
	    		map.put("code", "101");
	    		
	    		map.put("msg", "用户不存在");
	    		
	    		logger.info("用户不存在");
	    		
	    	}else{
	    		
	    		String password = u.getPassword();
	    		
	    		String pswdsalt = u.getPswdsalt();
	    		
	    		String verify = MD5Util.MD5(inputpassword+"#"+pswdsalt).toUpperCase();
	    		
	    		if(verify.equals(password)){
	    			
	    			map.put("code", "0");
		    		
		    		map.put("msg", "登陆成功");
		    		
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
	}  

