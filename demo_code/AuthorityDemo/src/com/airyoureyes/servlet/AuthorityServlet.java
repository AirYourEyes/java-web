package com.airyoureyes.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.airyoureyes.dao.UserDao;
import com.airyoureyes.vo.Authority;
import com.airyoureyes.vo.User;

public class AuthorityServlet extends HttpServlet {
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

	public void getAuthorities(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		User user = UserDao.get(username);
		request.setAttribute("user", user);// 键UserDao查询到的用户放置在request域中，当查找不到对应的user是，其值为null
		request.setAttribute("authorities", UserDao.getAuthorities());// 将整个权限列表放置在请求域中
		request.getRequestDispatcher("/authority-manager.jsp").forward(request, response);// 将请求转发到原页面中
	}

	public void updateAuthorities(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获取用户勾选的复选款，得到相应的value值（url）
		String[] authority_urls = request.getParameterValues("authorities");
		// 用于更新用户权限的列表
		List<Authority> user_authorities = new ArrayList<>();
		// 获取数据库中的所有权限列表
		List<Authority> authorities = UserDao.getAuthorities();
		// 将与value值对应的权限加入到用户的新权限列表中
		for (String url : authority_urls) {
			for (Authority authority : authorities) {
				if (url.equals(authority.getUrl())) {
					user_authorities.add(authority);
				}
			}
		}
		// 获取隐藏域中的用户名
		String username = request.getParameter("hide_username");
		// 更新用户权限
		UserDao.update(username, user_authorities);
		// 重定向到原页面
		response.sendRedirect("/AuthorityDemo/authority-manager.jsp");
	}
}
