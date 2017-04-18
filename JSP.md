***

### 简介

- 在很多动态网页中，绝大部分内容都是固定的，只有局部需要动态产生和改变
- 如果动态网页中固定的部分也交给编写Servlet的程序来负责，整个Servlet程序将会变得很臃肿，不便于维护
- JSP简化了Servlet的编写，在jsp文件中，Java代码和html代码混合在一个编写
- 静态的内容由html负责，而动态的内容使用Java代码编写
- Java服务端网页，即在HTML页面中编写Java代码的页面
- JSP可以放置在WEB应用程序中的除了WEB-INF及其子目录外的其他任何目录当中

***

### JSP的运行原理

- WEB容器（Servlet引擎）接收到以.jsp为拓展名的的URL的访问请求时，它将把该访问的请求交给jsp引擎进行处理
- 每个jsp页面在第一次被访问的时候，jsp引擎将它翻译为一个Servlet源程序，接着再把这个Servlet源程序编译成一个class文件，然后再由WEB容器
像调用普通的servlet程序那样装载和解释执行这个由jsp程序产生的Servlet程序
- jsp规范中也没有规定jsp中的脚本程序必须使用Java程序来编写，jsp中的脚本程序代码可以采用Java语言之外的其他脚本语言进行编写，但是jsp文件最后一定要生成Servlet程序进行执行
- 可以在WEB程序正式发布之前，将其中所有的jsp页面余弦编译成servlet程序

***

### JSP中的九个隐藏对象
1. request对象：HttpServletRequest类的对象
2. response对象：HttpServletResponse类的对象
3. pageContext对象：PageContext类的对象，可以获取其他八个隐藏对象
4. session对象：代表的是浏览器和服务器之间的一次会话
5. application对象：ServletContext类的对象，代表是整个WEB应用
6. config对象：当前jsp对应的servlet的ServletConfig类的对象
7. out对象：JspWriter类对象，可以直接将内容输出到浏览器上
8. page对象：代表的是当前jsp生成的对应的servlet对象本身，为Object类型，只可以调用Object类中的方法
9. exception对象：只有特殊的情况下才可以使用（当前jsp文件中声明了<%@ page isErrorPage="true" %>）

***

### 编写JSP

- html代码的编写与编写普通的html文件没有区别
- java代码写在<% %>之间

***

### 请求与重定向

- 本质区别：请求的转发只发出了一次请求，而重定向则发出了两次请求
- 具体：  
  - 对于请求的转发，地址栏是初次发出请求的地址；对于请求的重定向：地址栏不在是初次发出的请求地址，地址栏为最后响应的地址
  - 对于请求的转发，在最终的Servlet中，request对象和中转的那个request对象是同一个对象；而对于重定向，在最终的servlet中，request对象和中转的那个request不是同一对象
  - 对于请求转发，只能转发给当前web应用的资源；而对于请求重定向，可以重定向到任何资源
  - 对于请求转发，/代表的是当前web应用的根目录；而对于重定向，/代表的是当前web站点的根目录
  
- 实现请求转发的代码实现  
``` java
//1.调用HttpServletRequest的getRequestDispatcher()方法获得RequestDispacher对象
//调用getRequestDispatcher()传入需要转发的地址
String path = "/testServlet";
RequestDispather requestDispatcher = request.getDispacher(path);
//2.调用HttpServletRequest的forward(request, response)进行请求的转发
requestDispatcher.forward(request, response);
```
- 实现重定向的代码
``` java
//直接调用response.sendDirect(path);
String path = "http://www.baidu.com";
response.sendDirect(path);
```
***