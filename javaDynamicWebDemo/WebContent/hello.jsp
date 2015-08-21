<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		Date date = new Date();
		//控制台输出
		//System.out.println(date);
		//页面输出
		//out.println(date);			
	%>
	<!-- 访问隐含对象 -->
	<%
		String name = request.getParameter("name");
		out.write(name);		
		out.write("<br>");
		//out.println(request.getMethod());
		Class clazz = response.getClass();
		out.println(clazz);
		out.write("<br>");
		out.println(session.getId());
		out.write("<br>");
		out.println(this);
		out.println("<br>");
		out.println(page);
	%>
	
</body>
</html>