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
		String path = request.getServletPath();//��ȡҪ���ʵ�ҳ���url��ַ����ʽ������/article.jsp
		//��Ҫ���ص�ҳ���url��ַ
		List<String> uncheckUrls = Arrays.asList("/403.jsp", "/articles.jsp", "/authority-manager.jsp", "/login.jsp", "/logout.jsp");
		//ֱ�ӷ��в���Ҫ���ص�ҳ��
		if (uncheckUrls.contains(path)){
			chain.doFilter(request, response);
			return;
		}
		//1������û��Ƿ��Ѿ���¼
		User user = (User)request.getSession().getAttribute("user");
		//��userΪ�գ����ض���403.jsp��������ʾû�е�¼����Ϣ
		if (user == null){
			String message = "�Բ����㵱ǰ��û�е�¼�����¼���ٽ��з��ʣ�";
			request.getSession().setAttribute("message", message);
			response.sendRedirect("403.jsp");
			return;
		}
		//2������û��Ƿ���Ȩ�޷��ʶ�Ӧ��ҳ��
		boolean flag = false;
		//ѭ�������û����п��Է��ʵ�url��ַ�б���path��url���б��У�����flag��ʶΪtrue����ʶ����Ȩ�޷��ʸ�ҳ��
		for (Authority authority: user.getAuthorities()){
			if (path.equals(authority.getUrl())){
				flag = true;
			}
		}
		//����Ȩ�޷��ʸ�ҳ�棬ֱ�ӷ��ʸ�ҳ��
		if (flag){
			chain.doFilter(request, response);
			return;
		} else{ //�����ض���403.jspҳ���ϣ���ʾû��Ȩ�޷��ʸ�ҳ��
			String message = "�Բ�����û��Ȩ�޷��ʸ�ҳ�棡";
			request.getSession().setAttribute("message", message);
			response.sendRedirect("403.jsp");
			return;
		}
	}

}
