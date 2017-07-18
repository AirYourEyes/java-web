<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
    <a href="article-1.jsp">Go to Article-1</a>
    <br /><br />
    
    <a href="article-2.jsp">Go to Article-2</a>
    <br /><br />
    
    <a href="article-3.jsp">Go to Article-3</a>
    <br /><br />
    
    <a href="article-4.jsp">Go to Article-4</a>
    <br /><br />
    
    <a href="${pageContext.servletContext.contextPath }/LogoutServlet?method=logout">Logout....</a>
</body>
</html>