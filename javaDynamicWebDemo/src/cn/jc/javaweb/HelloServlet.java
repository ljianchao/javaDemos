package cn.jc.javaweb;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class HelloServlet implements Servlet{

	@Override
	public void destroy() {
		System.out.println("destroy...");
	}

	@Override
	public ServletConfig getServletConfig() {
		System.out.println("getServletInfo");
		return null;
	}

	@Override
	public String getServletInfo() {
		System.out.println("getServletInfo");
		return null;
	}

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		System.out.println("init");
		String user = servletConfig.getInitParameter("user");
		System.out.println("user:" + user);
		
		Enumeration<String> names = servletConfig.getInitParameterNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			String value = servletConfig.getInitParameter(name);
			System.out.println("^^name:" + name + ": " + value);
		}
		
		String servletName = servletConfig.getServletName();
		
		System.out.println(servletName);
		
		//获取ServletContext对象
		ServletContext servletContext = servletConfig.getServletContext();
		
		//获取当前Web应用的初始化参数
		String driver = servletContext.getInitParameter("driver");
		System.out.println("dirver: " + driver);
		
		Enumeration<String>	names2 = servletContext.getInitParameterNames();
		while (names2.hasMoreElements()) {
			String name = names2.nextElement();
			String value = servletContext.getInitParameter(name); 
			System.out.println("-->>" + name + ": " + value);			
		}
		
		//获取某个文件在服务器上的绝对路径
		String realPath = servletContext.getRealPath("/hellp.jsp");
		System.out.println(realPath);
		
		//获取当前web应用的名称
		String contextPath = servletContext.getContextPath();
		System.out.println(contextPath);
		
		//获取当前web应用的某一个文件对应的输入流
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			InputStream is = classLoader.getResourceAsStream("jdbc.properties");
			System.out.println("1. " + is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			InputStream is2 = servletContext.getResourceAsStream("/WEB-INF/classes/jdbc.properties");
			System.out.println("2. " + is2);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		

	}

	@Override
	public void service(ServletRequest arg0, ServletResponse arg1)
			throws ServletException, IOException { 
		System.out.println("service");
	}
	
	public HelloServlet(){
		System.out.println("HelloServlet's constructor");
	}

}
