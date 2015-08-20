package cn.jc.javaweb;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class LoginServlet implements Servlet{

	@Override
	public void destroy() {
		
		
	}

	@Override
	public ServletConfig getServletConfig() {
		
		return null;
	}

	@Override
	public String getServletInfo() {
		
		return null;
	}

	@Override
	public void init(ServletConfig arg0) throws ServletException {
		
		
	}

	//service方法用于应答请求
	@Override
	public void service(ServletRequest request, ServletResponse response)
			throws ServletException, IOException {
		//根据请求参数的名字，获取参数值
		String user = request.getParameter("user");		
		String password = request.getParameter("password");
		System.out.println(user + "," + password);
		
		//获取一组参数值
		String[] intrestings = request.getParameterValues("intresting");
		for (String intresting : intrestings) {
			System.out.println(intresting);
		}
		
		Enumeration<String> params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String param = params.nextElement();
			String val = request.getParameter(param);
			System.out.println("^^" + param + ": " + val);			
		}
		
		Map<String, String[]> paramKeys = request.getParameterMap();
//		for (String paramKey : paramKeys.keySet()) {
//			System.out.println(paramKey);
//			String[] paramValues = paramKeys.get(paramKey);
//			for (String paramValue : paramValues) {
//				System.out.println("-->" + paramValue);
//			}
//		}
		
		for (Map.Entry<String, String[]> entry : paramKeys.entrySet()) {
			System.out.println("**" + entry.getKey() + ": " + Arrays.asList(entry.getValue()));
		}
		
		//获取请求的url
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String requestURI = httpServletRequest.getRequestURI();
		System.out.println("URI:" + requestURI);
		StringBuffer requestURL = httpServletRequest.getRequestURL();
		System.out.println("URL:" + requestURL);
		
		String method = httpServletRequest.getMethod();
		System.out.println("method:"+method);
		
		String queryString = httpServletRequest.getQueryString();
		System.out.println("queryString:"+queryString);
		
		String servletPath = httpServletRequest.getServletPath();
		System.out.println("servletPath:" + servletPath);
		
		//设置响应内容的类型
		response.setContentType("application/msword");
		
		PrintWriter out = response.getWriter();
		out.print("helloworld...");
	}

}
