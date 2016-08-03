package com.wnt.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class LoginFilter implements Filter {
	
	private static Logger logger = Logger.getLogger(LoginFilter.class);
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		HttpSession session = request.getSession();
		// 获得用户请求的URI
		String path = request.getRequestURI();
		String contextPath = request.getContextPath();
		String url = path.substring(contextPath.length());
		Object user = session.getAttribute("userid");
		if ( "/html/404.html".equals(url)  || "/html/500.html".equals(url) ||"/user/login".equals(url)) {
			filterChain.doFilter(request, response);
			return;
		}
        if( user==null){
        	response.sendRedirect(contextPath+"/login.html");
        	logger.info("LoginFilter重定向请求["+url+"]");
        	return;
        }
        filterChain.doFilter(request, response);
        logger.info("LoginFilter通过");
	}
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}
}
