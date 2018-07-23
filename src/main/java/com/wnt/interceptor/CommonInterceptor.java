package com.wnt.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class CommonInterceptor extends HandlerInterceptorAdapter{

	private final Logger logger = Logger.getLogger(CommonInterceptor.class);  
	 
	@Override    
    public boolean preHandle(HttpServletRequest request,    
            HttpServletResponse response, Object handler) throws Exception {    
        
        logger.info("==============执行顺序: 1、preHandle================");    
       // String requestUri = request.getRequestURI();  
       // String contextPath = request.getContextPath();  
      //  String url = requestUri.substring(contextPath.length());  
        
        //logger.info("requestUri:"+requestUri);    
        //logger.info("contextPath:"+contextPath);    
       // logger.info("url:"+url);    
          
       /* String username =  (String)request.getSession().getAttribute("username");   
        if(username == null){  
            logger.info("Interceptor：跳转到login页面！");  
            request.getRequestDispatcher("/login.html").forward(request, response);  
            return false;  
        }else  */
            return true;   
	}
	
	@Override    
    public void postHandle(HttpServletRequest request,    
            HttpServletResponse response, Object handler,    
            ModelAndView modelAndView) throws Exception {     
        logger.info("==============执行顺序: 2、postHandle================");    
        if(modelAndView != null){  //加入当前时间    
            modelAndView.addObject("var", "测试postHandle");    
        }    
    }    
	
	  @Override    
	    public void afterCompletion(HttpServletRequest request,    
	            HttpServletResponse response, Object handler, Exception ex)    
	            throws Exception {    
	        logger.info("==============执行顺序: 3、afterCompletion================");    
	    }    
}
