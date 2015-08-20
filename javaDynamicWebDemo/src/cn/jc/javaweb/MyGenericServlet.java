package cn.jc.javaweb;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 自定义的一个Servlet接口的实现类：让开发的任何Servlet都继承该类，以简化开发
 * @author Administrator
 *
 */
public abstract class MyGenericServlet implements Servlet, ServletConfig {

	private ServletConfig servletConfig;
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ServletConfig getServletConfig() {
		return servletConfig;
	}

	@Override
	public String getServletInfo() {
		return null;
	}

	@Override
	public void init(ServletConfig arg0) throws ServletException {
		this.servletConfig = arg0;
		
	}

	@Override
	public abstract void service(ServletRequest arg0, ServletResponse arg1)
			throws ServletException, IOException;

	/**
	 * 以下为ServletConfig接口的方法
	 */
	@Override
	public String getInitParameter(String arg0) {
		return servletConfig.getInitParameter(arg0);
	}

	@Override
	public Enumeration<String> getInitParameterNames() {
		return servletConfig.getInitParameterNames();
	}

	@Override
	public ServletContext getServletContext() {
		return servletConfig.getServletContext();
	}

	@Override
	public String getServletName() {
		return servletConfig.getServletName();
	}

}
