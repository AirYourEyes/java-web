<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
   <form action="${pageContext.servletContext.contextPath }/AuthorityServlet?method=getAuthorities" method="post">
       Name: <input name="username" type="text" />
       <input type="submit" value="Submit" />
   </form>
   <c:if test="${requestScope.user != null }">
       ${requestScope.user.username }的权限是：
       <form action="${pageContext.servletContext.contextPath }/AuthorityServlet?method=updateAuthorities" method="post">
           <input type="hidden" name="hide_username" value="${requestScope.user.username }">
           <!-- 遍历整个权限列表，包含在用户的相应权限的checked属性 -->
           <c:forEach items="${requestScope.authorities }" var="authority">
               <!-- flag标识是否需要打钩，默认不打钩 -->
               <c:set var="flag" value="false"></c:set>
               <!-- 遍历用户自己的权限列表 -->
               <c:forEach items="${user.authorities }" var="user_authority">
                   <!-- 若当前要显示的checkbox存在用户自己的权限列表中，将flag设置诶true，表示需要打钩 -->
                   <c:if test="${user_authority.url==authority.url }">
                       <c:set var="flag" value="true"></c:set>
                   </c:if>
               </c:forEach>  
               
               <!-- 若flag标识为true，显示的checkbox需要打钩 -->
               <c:if test="${flag==true }">
                   <input type="checkbox" name="authorities" value="${authority.url }" checked="checked">${authority.displayName }
                   <br /><br />
               </c:if>
               <!-- 否则不需要打钩 -->               
               <c:if test="${flag==false }">
                   <input type="checkbox" name="authorities" value="${authority.url }">${authority.displayName }
                   <br /><br />
               </c:if>  
           </c:forEach>
           <input type="submit" value="Update" />
       </form>
   </c:if>
</body>
</html>