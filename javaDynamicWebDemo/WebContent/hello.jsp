<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<p>This is a dynamic web.</p>
	<form action="loginServlet" method="post">
		user: <input type="text" name="user"/>
		password: <input type="password" name="password"/>
		<br/>
		<input type="checkbox" name="intresting" value="reading" />Reading
		<input type="checkbox" name="intresting" value="game" />Game
		<input type="checkbox" name="intresting" value="party" />party		
		<br />
		<input type="submit" value="Submit"/>
	</form>
</body>
</html>