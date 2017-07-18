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
 * 实现自定义HttpFilter类，该类的doFilter方法可以直接使用HttpServletRequest和HttpServletResponse类的方法
 * @author airyoureyes
 */

public abstract class HttpFilter implements Filter {
	//保存FilterConfig对象
	private FilterConfig filterConfig;
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		init();	//调用下面的init方法，若该方法被子类覆盖，则调用子类的init方法
	}
	
	//若子类需要进行初始化配置，需覆盖该方法
	protected void init(){
		
	}
	
	//返回FilterConfig对象
	public FilterConfig getFilterConfig(){
		return filterConfig;
	}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;	//将req强转成HttpServeltRequest类型
		HttpServletResponse response = (HttpServletResponse)res;	//将res强转成HttpServletRepsonse类型
		doFilter(request, response, chain);			//调用子类实现的doFilter方法
	}

	//抽象方法，子类必须实现该方法，完成相应的过滤逻辑
	public abstract void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException;
	
	@Override
	public void destroy() {
		destory();	//调用下面的destroy方法，若该方法被子类覆盖，则调用子类的destroy方法
	}
	
	//若子类需要释放资源，需要覆盖该方法
	protected void destory(){
		
	}
	
}
