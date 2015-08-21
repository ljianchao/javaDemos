package cn.jc.javaweb;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class TestAttr
 */
public class TestAttr extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestAttr() {
        super();
       
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		//1. 在Servlet中无法得到PageContext对象
		
		//2. request
		Object requestAttr = request.getAttribute("requestAttr");
		out.println("requestAttr: " + requestAttr);
		
		//3. session
		Object sessionAttr = request.getSession().getAttribute("sessionAttr");
		out.println("sessionAttr: " + sessionAttr);
		
		//4. application
		Object applicationAttr = getServletContext().getAttribute("applicationAttr");
		out.println("applicationAttr: " + applicationAttr);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
