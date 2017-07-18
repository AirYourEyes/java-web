package com.airyoureyes.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ʵ���Զ���HttpFilter�࣬�����doFilter��������ֱ��ʹ��HttpServletRequest��HttpServletResponse��ķ���
 * @author airyoureyes
 */

public abstract class HttpFilter implements Filter {
	//����FilterConfig����
	private FilterConfig filterConfig;
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		init();	//���������init���������÷��������า�ǣ�����������init����
	}
	
	//��������Ҫ���г�ʼ�����ã��踲�Ǹ÷���
	protected void init(){
		
	}
	
	//����FilterConfig����
	public FilterConfig getFilterConfig(){
		return filterConfig;
	}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;	//��reqǿת��HttpServeltRequest����
		HttpServletResponse response = (HttpServletResponse)res;	//��resǿת��HttpServletRepsonse����
		doFilter(request, response, chain);			//��������ʵ�ֵ�doFilter����
	}

	//���󷽷����������ʵ�ָ÷����������Ӧ�Ĺ����߼�
	public abstract void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException;
	
	@Override
	public void destroy() {
		destory();	//���������destroy���������÷��������า�ǣ�����������destroy����
	}
	
	//��������Ҫ�ͷ���Դ����Ҫ���Ǹ÷���
	protected void destory(){
		
	}
	
}
