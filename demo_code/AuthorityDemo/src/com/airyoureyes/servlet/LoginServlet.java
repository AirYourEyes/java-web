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
		//1����ȡusername
		String username = request.getParameter("username");
		//2������UserDao��ȡ�û�����Ϣ�����û���Ϣ���뵽request��
		User user = UserDao.get(username);
		request.getSession().setAttribute("user", user);
		//3���ض���articles.jsp
		response.sendRedirect(request.getContextPath() + "/articles.jsp");
	}
	
	

}
