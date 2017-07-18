package com.airyoureyes.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.airyoureyes.dao.UserDao;
import com.airyoureyes.vo.Authority;
import com.airyoureyes.vo.User;

public class AuthorityFilter extends HttpFilter {

	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String path = request.getServletPath();//获取要访问的页面的url地址，形式类似于/article.jsp
		//不要拦截的页面的url地址
		List<String> uncheckUrls = Arrays.asList("/403.jsp", "/articles.jsp", "/authority-manager.jsp", "/login.jsp", "/logout.jsp");
		//直接放行不需要拦截的页面
		if (uncheckUrls.contains(path)){
			chain.doFilter(request, response);
			return;
		}
		//1、检查用户是否已经登录
		User user = (User)request.getSession().getAttribute("user");
		//若user为空，则重定向到403.jsp，并且提示没有登录的信息
		if (user == null){
			String message = "对不起，你当前还没有登录，请登录户再进行访问！";
			request.getSession().setAttribute("message", message);
			response.sendRedirect("403.jsp");
			return;
		}
		//2、检查用户是否有权限访问对应的页面
		boolean flag = false;
		//循环遍历用户所有可以访问的url地址列表，若path在url的列表当中，设置flag标识为true，标识该有权限访问该页面
		for (Authority authority: user.getAuthorities()){
			if (path.equals(authority.getUrl())){
				flag = true;
			}
		}
		//若有权限访问该页面，直接访问该页面
		if (flag){
			chain.doFilter(request, response);
			return;
		} else{ //否则重定向到403.jsp页面上，提示没有权限访问该页面
			String message = "对不起，你没有权限访问该页面！";
			request.getSession().setAttribute("message", message);
			response.sendRedirect("403.jsp");
			return;
		}
	}

}
