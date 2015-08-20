package cn.jc.javaweb;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class LoginServletSec extends GenericServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2965837251075863119L;

	@Override
	public void service(ServletRequest request, ServletResponse response)
			throws ServletException, IOException {		
				
		//1. 获取请求参数: username, password
		String username =request.getParameter("username");
		String password = request.getParameter("password");
		
		//2. 获取当前WEB应用的初始化参数: user, password
		String initUser = this.getServletContext().getInitParameter("user");
		String initPassword = this.getServletContext().getInitParameter("password");			
		
		PrintWriter out = response.getWriter();
		
		//3. 比对		
		//4. 打印响应字符串
		if (initUser.equals(username) && initPassword.equals(password)) {
			out.write("hello: " + username);
		} else{
			out.write("sorry: " + username);
		}
	}

}
