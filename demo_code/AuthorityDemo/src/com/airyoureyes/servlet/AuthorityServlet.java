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
		request.setAttribute("user", user);// ��UserDao��ѯ�����û�������request���У������Ҳ�����Ӧ��user�ǣ���ֵΪnull
		request.setAttribute("authorities", UserDao.getAuthorities());// ������Ȩ���б��������������
		request.getRequestDispatcher("/authority-manager.jsp").forward(request, response);// ������ת����ԭҳ����
	}

	public void updateAuthorities(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// ��ȡ�û���ѡ�ĸ�ѡ��õ���Ӧ��valueֵ��url��
		String[] authority_urls = request.getParameterValues("authorities");
		// ���ڸ����û�Ȩ�޵��б�
		List<Authority> user_authorities = new ArrayList<>();
		// ��ȡ���ݿ��е�����Ȩ���б�
		List<Authority> authorities = UserDao.getAuthorities();
		// ����valueֵ��Ӧ��Ȩ�޼��뵽�û�����Ȩ���б���
		for (String url : authority_urls) {
			for (Authority authority : authorities) {
				if (url.equals(authority.getUrl())) {
					user_authorities.add(authority);
				}
			}
		}
		// ��ȡ�������е��û���
		String username = request.getParameter("hide_username");
		// �����û�Ȩ��
		UserDao.update(username, user_authorities);
		// �ض���ԭҳ��
		response.sendRedirect("/AuthorityDemo/authority-manager.jsp");
	}
}
