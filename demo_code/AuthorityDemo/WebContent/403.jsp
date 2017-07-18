<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
    <!-- 显示提示信息 -->
    <h1>${sessionScope.message }</h1>
    <!-- 若用户未登录，添加去往登录的链接 -->
	<c:if test="${sessionScope.user == null}">
	    <a href="login.jsp">login...</a>
	</c:if>
	<!-- 若用户已经登录，但跳转到了该页面说明，该用户没有权限访问该页面，此时添加去往articles.jsp页面的链接 -->
	<c:if test="${sessionScope.user != null }">
	    <a href="articles.jsp">Go to article list</a>
	</c:if>
</body>
</html>