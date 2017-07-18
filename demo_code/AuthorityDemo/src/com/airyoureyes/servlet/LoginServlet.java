package com.airyoureyes.servlet;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.airyoureyes.dao.UserDao;
import com.airyoureyes.vo.User;

/**
 * Servlet implementation class LoginServlet
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String methodName = request.getParameter("method");
		try {
			Method method = getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
			method.invoke(this, request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void login(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//1、获取username
		String username = request.getParameter("username");
		//2、调用UserDao获取用户的信息，把用户信息放入到request中
		User user = UserDao.get(username);
		request.getSession().setAttribute("user", user);
		//3、重定向到articles.jsp
		response.sendRedirect(request.getContextPath() + "/articles.jsp");
	}
	
	

}
