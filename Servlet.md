### Servlet简介

- Servlet是一个接口，由服务器实现，运行在Servlet容器当中
- Servlet

### Servlet的声明周期

- 构造器

- init(ServletConfig config)

- service(ServletRequest request, ServletResponse response)

- getServletConfig()

- getServletInfo()

- destroy

### GenericServlet

实现了Servlet中的方法

### HttpServlet

- 一个抽象类，继承GenericServlet
