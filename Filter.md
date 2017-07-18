### 1. 简介
- 过滤器
- 基本功能：
	- 对Servlet容器调用Servlet的过程进行拦截，从而在Servlet进行响应处理的前后实现一些特殊的处理的功能
- 在Servlet API中定义了三个接口类来供开发人员编写Filter程序
	- Filter
	- FilterChain
	- FilterConfig
- Filter程序是一个实现了Filter接口的java类，与Servlet程序相似，它由Servlet容器进行调用执行
	- Filter程序需要在web.xml文件中进行注册和设置它所能拦截的资源	- Filter程序可以拦截JSP，Servlet，静态图片文件静态html文件 

### 2. Filter的基本工作原理
- 当在web.xml中注册了一个Filter来对某个Servlet程序进行拦截处理时，这个Filter就成了Servlet程序与该Servlet程序通信线路上的一道关卡，该Filter可以对Servlet容器发送给Servlet程序的请求和Servlet程序回送给Servlet容器的响应进行拦截，可以决定是否将请求继续传递给Servlet程序，以及对请求和响应信息是否进行修改
- 在一个web应用程序中可以注册多个Filter程序，每个Filter程序都可以对一个或者一组Servlet程序进行拦截
- 若有多个Filter程序对某个Servlet程序的访问过程进行拦截，当针对该Servlet的访问请求到达时，web容器将把这多个Filter程序组合成一个Filter链（过滤器链）。Filter链中各个Filter的拦截顺序与它们在应用程序的web.xml中映射的顺序一致

### 3.使用Filter的步骤
- 实现Filter接口
- 在web.xml文件中配置和映射Filter
- ```
	<!-- 注册Filter -->
	  <filter>
	  	<filter-name>testFilter</filter-name>
	  	<filter-class>com.airyoureyes.filters.PageFilter</filter-class>
	  </filter>
  
	  <!-- 映射Filter -->
	  <filter-mapping>
	  	<filter-name>testFilter</filter-name>
	  	<url-pattern>/test.jsp</url-pattern>
	  </filter-mapping>
``` 
- 上面的url-pattern标签配置的是要拦截的资源

### 4. Filter接口相关的API
- `public void init(FilterConfig config) throws ServletException`
	- 跟Servlet中的init方法作用相同，用来对Filter对象进行初始化配置
	- Web容器创建Filter对象实例后，将立即执行该Filter对象的init方法。init方法在Filter生命周期中仅执行一次，web容器在调用init方法时，会传递一个包含Filter的配置和运行环境的FilterConfig对象
	- FilterConfig类似于Servlet中的ServletConfig对象
		- getInitParameter方法
			- 获取配置的内容  
			- 如： 
			- ```
			 web.xml中的配置如下：
			 <init-param>
	  			 <param-name>init_param</param-name>
	  			 <param-value>666</param-value>
           	 </init-param>
					
			``` 
			- 通过以下的方式获取init参数的值`String init_value = config.getInitParameter("init_param");`
- `public void destroy()`方法
	- 该方法在Filter对象生命周期结束之前调用，释放该Filter对象占有的资源，且只被执行一次
- `public void doFilter(ServletRequest requset, ServletResponse response, FilterChain chain)` 方法
	- 类似于Servlet接口中的service方法 
	- 该方法是真正执行过滤逻辑的部分
	- 当客户端请求目标资源时，容器就会调用与这个目标资源相关联的过滤器的doFilter方法
	- 其中request，response为web容器或Filter链上的一个Filter传递过来的请求和响应对象；参数chain代表当前Filter链对象，在特定的操作完成后，可以在当前Filter对象的doFilter方法中调用chain的doFilter方法，将request，response对象传递给chain链上的下一个Filter对象处理，若该Filter对象是链中最后一个Filter对象，则可将请求或者响应直接发给目标Servlet处理，这时也可以直接向客户端返回响应信息，或者利用RequestDipatcher的forward()和include()方法，以及HttpServletResponse的sendRedirect()方法将请求转向其他的资源。这个方法的请求和响应参数的类型是ServletRequest和ServletResponse，也就是说，过滤器的使用并不依赖于具体的协议
	- FilterChain接口
		- 代表的是当前Filter链对象
		- 由容器实现，容器将其实例作为参数传入过滤器对象的doFilter方法中。过滤对象使用FilterChain对象调用过滤器链中的下一个过滤器，如果该过滤器是链中最后一个过滤器，那么将调用目标资源 

### 5. 定义HttpFilter类
``` java
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

```

### 6. Filter Chain中代码的执行路径
现在我们假设有两个Filter类，他们都是用来过滤test.jsp的，而且在web.xml中对第一个Filter的映射要比第二个Filter早
- 这是第一个Filter类中doFilter方法中的代码  
``` java
	System.out.println("Before First Filter ......"); ①
	chain.doFilter(request, response);
	System.out.println("After First Filter ......");  ②
```
- 第二个Filter类中doFilter方法中的代码如下所示
```java
	System.out.println("Before Second Filter ......");	③
	chain.doFilter(request, response);
	System.out.println("After Second Filter ......");	④
``` 
- 下面是test.jsp中的主要内容
```jsp
	<%
		System.out.println("The test jsp");	⑤
	%>
```  
- 则他们的执行顺序时①③⑤④②

### 7. 映射Filter详解
- `<filter-mapping>`元素用来设置一个Filter所负责拦截的资源
- 一个Filter拦截的资源可通过两种方式来执行：
	- Servelt名称
	- 资源访问的请求路径（url路径）
- `<filter-mapping>`的子标签
	- `<filter-name>`:用于设置filter的注册名称。该值必须是在filter元素中声明过的过滤器的名字
	- `<url-patten>`:用于设置filter所拦截的请求路径（过滤器关联的URL样式）
	- `<dispatcher>`:指定过滤器所拦截的资源被Servlet容器调用的方式，可以是REQUEST,INCLUDE,FORWARD和ERROR之一，默认是REQUEST。可以设置多个`<dispatcher>`子元素用来指定filter对资源的多种调用方式拦截
		- REQUEST
			- 当用户直接访问页面时，web容器将会调用过滤器，除此之外，该过滤不会被调用
		- INCLUDE
			- 通过RequestsDispatcher的include方法访问目标资源时，调用过滤器
			- 通过`<jsp:include page="...">`访问目标资源，调用过滤器
			- 除上面两种情况，该过滤器不会被调用
		- FORWARD:
			- 通过RequestDispatcher的forward方法访问目标资源，会调用过滤器
			- 通过`<jsp:forward page="...">`访问目标资源，会调用过滤器
			- 通过`<%@ page errorPage="..."%>`，当页面发生错误的时候，会调用过滤器
			- 除上面两种情况，不调用过滤器
		- ERROR
			- 当页面出现异常或者错误时，可以在web.xml的`<error-page>`标签中声明要处理的异常类型与处理异常或错误的页面
			- 如
			- ```
				<error-page>
  					<exception-type>java.lang.ArithmeticException</exception-type>
  					<location>/error.jsp</location>
  				</error-page>
			```
			- 要拦截这些异常或者错误需要在配置filter时将`<dispatcher>`的值设置为ERROR

### 8. 处理页面上出现异常的方法
- 方法一：使用page指令
	- 在可能会出现异常的页面上使用以下指令`<%@ page errorPage="..." %>`，其中errorPage指的是处理错误的页面
- 方法二：使用filter拦截
	- 在web.xml中指定处理错误的页面，如：
	- ```
		<error-page>
			<exception-type>java.lang.ArithmeticException</exception-type>
			<location>/error.jsp</location>
		</error-page>
	```
	- 在filter的映射配置中指定`<dispatcher>`的值为ERROR，如：
	- ```
		<filter-mapping>
	    	<filter-name>JspFilter</filter-name>
	    	<url-pattern>/test.jsp</url-pattern>
	    	<dispatcher>ERROR</dispatcher>
		</filter-mapping>
	```

### 9. Filter应用一
- 在Filter中设置浏览器禁止缓存，是每次访问到的资源都是最新的
	- 有三个http响应头字段都可以禁止浏览器缓存当前页面，分别是
		- response.setDateHeader("Expires",-1);
		- response.setHeader("Cache-Control", "no-cache");
		- response.setHeader("Pragma", "no-cache"); 
	- 由于并不是所有的浏览器都能完全支持上面的三个响应头，因此最好是同时使用上面的三个响应头

### 10. Filter应用二
- 设置页面的request编码为指定的编码
	- 首先在页面中 

### 11. Filter应用三
- 检查用户是否登录
	- 情景：系统中的某些页面只有在用户正常登录之后才可以使用，用户请求这些页面时要检查session中有无该用户信息，但在所有必要的页面加上session的判断有无该用户是相当麻烦的事情
	- 解决方案：编写一个用于检测用户是否登录的过滤器，如果用户未登录，则重定向到登录页面上
	- 要求：需检查在session中保存的关键字，如果用户未登录，需重定向到指定的页面（URL不包括ContextPath）；不做检查的URL列表（以分号分隔开，并且URL中不包括ContextPath）都要采取可配置的

### 12. Filter应用四
- 权限管理
    - 可以在authority-manager.jsp页面上设置用户能访问的文章（共有四个，分别是article-1.jsp, article-2.jsp, article-3.jsp, article-4.jsp）
    - 启动程序时先到articles.jsp页面，该页面中包含所有的文章列表，点击可以去往相应的文章
    - 但是去往目的文章的过程中，请求会被AuthorityFilter拦截
    - 首先检查用户是否已经登录
    - 若登录的话，再看其有没有相应的访问权限
    - 若有权限，直接跳转到目的页面，否则跳转到403.jsp页面，提示相关的信息
    - 详细的代码[具体实现](demo_code/AuthorityDemo)

### 13. Filter应用五
- 使用HttpServletRequestWrapper类和HttpServletResponseWrapper类
    - Servlet API中提供了一个HttpServletRequestWrapper类来包装原始的request对象，该类实现了HttpServletRequest接口中的所有方法，这些方法的内部实现都是仅仅调用了一下所包装的request对象的对应的方法。相类似的是，Servlet API也提供了一个HttpServletResponseWrapper类来包装原始的response对象。
- 
  