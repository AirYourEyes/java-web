# 会话与状态管理

## 问题：
- http是无状态协议：WEB服务器本身不能识别出那些请求是同一个浏览器发出的，浏览器的每一次请求都是完全孤立的
- 即使http1.1支持持续连接，但当用户有一段时间没有提交请求，连接也会关闭
- 作为web服务器，必须能够采用一种机制来唯一地标识一个用户，同时记录该用户的状态

## 会话和会话状态
- web应用中的会话是指一个客户端浏览器与web服务器之间连续发生的一系列请求和响应的过程。如登录到某一个购物网站，到关闭浏览器的过程就是一个会话
- web应用的会话状态是指web服务器与浏览器在会话过程中产生的状态信息，借助会话状态，web服务器能够把属于同一会话中的一系列请求和响应过程关联起来
 
## 实现有状态的会话
- web服务器端程序要能从大量的请求信息中区分出哪些请求消息属于同一 个会话，即能识别出来自同一个浏览器的访问请求，这需要浏览器对其发出的每个请求信息都进行标识：**属于同一个会话的停球消息都附带同样的标识号，而属于不同的会话的请求附带不同的标识号，这个标识号就称为会话ID（SessionID）**
- 在servlet中，常用以下两种机制来完成会话跟踪
    - Cookie
    - Session

## Cookie机制
- Cookie机制采用的是在客户端保持http状态信息的方案
- Cookie是在浏览器访问web服务器的某个资源的时候，由web服务器在http**响应消息头**中附带传送给浏览器的一个小文本文件
- 一旦web浏览器保存了某个Cookie，那么它以后每次访问该web服务器时，都会在http请求头中将这个Cookie回传个web服务器
- 底层实现的原理：web服务器通过在http响应消息中增加set-cookie响应头字段将Cookie信息发送给浏览器，浏览器则通过在http在请求消息中增加cookie请求头字段将Cookie回传给web服务器
- 一个cookie只能标识一种信息，它至少含有一个标识该信息的名称（Name）和设置值（Value）
- 一个web站点可以给一个web浏览器发送多个cookie，一个web浏览器也可以存储多个web站点提供的cookie
- 浏览器一般只允许存放300个cookie，每个站点最多存放20个cookie，每个cookie的大小限制为4kb

## servlet api中提供了一个javax.servlet.http.Cookie类来封装cookie信息
- Cookie类的方法： 
  - 构造方法，public Cookie(String name, String value)
  - getName方法
  - setValue和getValue方法
  - setMaxAge和getMaxAge方法
  - setPath与getPath方法
- HttpServletResponse接口中定义了一个addCookie方法，它用来发送给浏览器的http响应消息中增加一个Set-Cookie响应头字段
- HttpServletRequest接口中定义了一个getCookie方法，它用于从http请求消息中的Cookie请求头字段中读取所有的Cookie项

## Cookie的发送
1. 创建Cookie对象
2. 设置最大时效
3. 将Cookie放入到Http响应头中
    - 如果创建了一个cookie，并它发送给浏览器，默认情况下它是一个会话级别的cookie；存储在浏览器的内存当中，用户退出浏览器之后被删除。若希望浏览器将cookie保存在硬盘当中，则需要使用maxage，并给出一个以秒为单位的时间。将最大时效设为0则是命令浏览器删除该cookie
    - 发送cookie需要使用HttpServletResponse的addCookie方法，将cookie插入到一个Set-Cookie的Http响应头中，由于这个方法不修改任何之前指定的Set-Cookie报头，而是创建新的报头，因此将这个方法称为addCookie，而非setCookie

## 会话cookie与持久cookie的区别
- 如果不设置过期时间，则表示这个cookie生命周期为浏览器会话期间，只要关闭浏览器窗口，cookie消失。这种生命期为浏览器会话期的cookie被称为会话cookie。会话cookie一般不保存在硬盘上而是保存在内存当中
- 如果设置过期时间，浏览器就会把cookie保存到硬盘当中，关闭后再打开浏览器，这些cookie依然有效直到超过设定的过期时间
- 存储在硬盘上的cookie 可以在不同的浏览器进程间共享，比如两个IE窗口。而对于保存在内存的cookie，不同的浏览器有不同的处理方式

## Cookie的读取
1. 调用request.getCookies方法
    - 要获取浏览器发送来的cookie，需要调用HttpServletRequest的getCookies方法，这个调用返回Cookie对象的数组，对应由HTTP请求中Cookie报头输入的值
2. 对数组进行遍历操作，调用每个cookie的getName方法直到找到感兴趣的cookie为止

## Session
- 一类用来解决在客户端和服务器之间保持状态的解决方案，有时候Session也用来指这种解决方案的存储结构
- session机制采用的是在***服务端保持Http状态信息的解决方案*
- 当程序需要为某个客户端的请求创建一个session的时候，服务器首先检查这个客户端的请求里是否包含了一个session的标识（即sessionid），如果已经包含一个sessionid的话，则说明以前已经为客户创建了session，服务器就按照session id把这个session检索出来使用（如果检索不到，可能会新建一个，这种情况可能出现在服务端已经删除了该用户对应的session对象，但用户人为在请求的URL后面附加了一个JSESSION的参数）。如果客户请求不包含sessionid，则为用户创建一个session并且生成一个与此session相关联的sessionid，这个session id将在本次响应中给客户端保存
- 保存session id几种方式
    - 保存session id的方式可以采用cookie，这种在交互过程中浏览器可以自动的按照规则把这个标识发送给服务器
    - 由于cookie可以被人为的禁用，必须有其他的机制以便于在cookie被禁用时仍然可以把session id发送给服务器，经常使用的一种技术叫做URL重写，就是把session id附加到URL路径的后面，附加的方式也有两种，一种是作为URL路径的附加信息，另外一种是作为查询字符串附加到URL后面。网络在整个交互过程中时钟保持状态，就必须在每个客户端可能请求的路径后面都包含这个session id
- Session cookie
    - session 通过session id来区分不同的客户session是以cookie或URL重写为基础的，**默认使用cookie来实现，，系统会创造一个名为JSESSION的输出cookie，这称之为session cookie，以区别persistent cookie（也就是我们通常所说的cookie），session cookie是存储在浏览器内存当中的，并不是写到硬盘上的，通常看不到JSESSIONID，但是当把浏览器的cookie禁止后，web服务器会采用URL重写的方式传递session id，这时在地址栏中就可以看到了**
    - session cookie针对某一次会话而言，会话结束session cookie也就随之消失了，而persistent cookie只是存在于客户端的硬盘上的一段文本
    - 关闭浏览器，只会是浏览器端内存里的session cookie消失，但不会使保存在服务端的session对象消失，同样也不会使已经保存在硬盘上的持久化cookie消失
- Session对象的生命周期
    - 什么时候创建HttpSession对象？
        - 是否浏览器访问服务器的任何一个jsp或者servlet的时候，服务器立即创建一个HttpSession对象？
            - 不一定的
            - 对于jsp
                - 若当前jsp是客户端访问的当前web应用的第一个资源，且jsp的page指令中指定session的值为false，则服务器就不会为jsp创建一个HttpSession对象
                - 若当前jsp不是客户端访问的当前web应用的第一个资源（该jsp的page指令中指定session的值为false），且其他的页面已经创建了一个HttpSession对象，则当前jsp页面会返回一个会话的HttpSesion对象，而不会创建一个新的HttpSession对象
                - session="false"表示的是当前jsp页面禁止使用session隐藏对象，但是可以使用其他显示的HttpSession对象
            - 对于servlet而言
                - 若servlet是客户端访问的第一个web应用程序的资源，则只有调用request.getSession（）;或者request.getSession(true);才会创建一个HttpSession对象
                - request.getSession(boolean create);
                     - 返回与请求相关的当前HttpSession对象，若该HttpSession对象不存在还有create的值为true，那么创建并返回一个新的HttpSession对象，如果当前HttpSession对象不存在且create的值为false的话，返回null
    - 什么时候销毁HttpSession对象
        - 直接调用HttpSession的invalidate方法，该方法使得HttpSession对象失效
        - 卸载当前web应用
        - 在web-xml文件中设置HttpSession过期时间，单位为分钟  
        ```
			<session-config>
					<session-timeout>30</session-timeout>
			</session-config>
		``` 
    - 并不是关闭了浏览器就销毁了HttpSession对象

- 利用URL重写实现Session跟踪
    - servlet规范中引入了一种补充会话管理机制，它允许不支持Cookie的浏览器也可以与WEB服务器保持连续的会话，这种补充机制要求在响应信息的实体内容中必须包含下一次请求的超链接，并将会话标志号作为超链接的URL的一个特殊的参数
    - 将会话标志号以参数形式附加到超链接的URL地址后面的技术叫做URL重写。如果在浏览器不支持Cookie或者关闭了Cookie功能的情况下，WEB服务器还要能够与浏览器实现有状态的会话，就必须对所有可能被客户端访问的请求路径（包括超链接，form表单的action属性设置和重定向的URL）进行URL重写
    - HttpServletResponse接口中定义了两个用于URL重写方法
        - encodeURL方法
        - encodeRedirectURL方法
- HttpSession中的常见方法
    - setAttribute方法
    - getAttribute方法
    - getId方法
    - isNew方法
    - getMaxIncttiveInterval方法
    - getLastAccessedTime方法




