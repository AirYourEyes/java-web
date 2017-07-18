### 一、简介
- 专门用于对其他对象身上发生的事件或者状态改变进行监听和相应处理的对象，当被监视的对象发生情况时，立即采用相应的行动
- Servlet监听器：servlet规范中定义了一类特殊类，它用于监听web应用程序中的ServletContext，HttpSession和ServletRequest等域对象的创建和销毁事件，以及监听这些域对象中属性发生改变的事件

### 二、Servlet监听器的分类
- 按监听的事件类型Servlet监听器可以分为如下三种类型：
    - 监听域对象自身的创建和销毁的事件监听器
    - 监听域对象中的属性的增加和删除的事件监听器
    - 监听绑定到HttpSession域中的某个对象的状态的事件监听器 

### 三、编写Servlet监听器
- Servlet规范为每种事件监听器都定义了相应的接口，开发人员编写的事件监听器程序只需要实现这些接口，web服务器根据用户编写的事件监听器所实现的接口把它注册到相应的被监听的对象上
- 一些Servlet事件监听器需要在web程序的web.xml文件中进行注册，一个web.xml文件中可以注册多个Servlet事件监听器，web服务器按照它们在web.xml文件中的注册顺序来加载和注册这些Servlet事件监听器
- Servlet事件监听器的注册和调用过程都是由web容器自动完成的，当发生被监听的对象被创建，修改或者销毁事件时，web容器将调用与之相关的Servlet事件监听器对象的相关方法，开发人员在这些方法中编写事件处理代码即被执行
- 由于一个web应用程序只会为每个事件监听器创建一个对象，有可能出现多个线程同时调用同一个事件监听器对象的情况，所以，在编写事件监听器类时，应考虑多线程安全的问题
- web.xml配置监听器` <listener> <listener-class>com.airyoureyes.listener.TestListener/listener-class>    </listener>`

### 四、监听域对象的创建和销毁
- 域对象的创建和销毁的事件监听器就是用来监听ServletContext，HttpSession，HttpServletRequest这三个对象的创建和销毁事件的监听器
- 域对象的创建和销毁的时机
    - ServletContext
        - 创建时机：web服务器启动时为每一个web应用程序创建响应的ServletContext对象
        - 销毁时机：web服务器关闭时为每一个web应用程序销毁相应的ServletContext对象
    - HttpSession
        - 创建时机：浏览器开始与服务器会话时创建
        - 销毁时机：调用HttpSession.invadiatte()，超过了session的最大有效时间，服务器被停止
    - ServletRequest
        - 创建时机：每次请求开始时创建
        - 销毁时机：每次返回结束后销毁
- 相关的接口
	- ServletContextListener接口
	    - ServletContextListener接口用于监听ServletContext对象的创建和销毁事件
		    - 当ServletContext对象被创建时，激发contextInitialized(ServletContextEvnet sce)方法
		    - 当ServletContext对象被销毁时，激发contextDestroyed(ServletContextEvent sce)方法
		- ServletContextListener是最常用的Listener，可以在当前web应用被加载时对当前WEB应用的相关资源进行初始化操作：如创建数据库连接池，创建Spring的IOC容器，读取当前WEB应用的初始化参数等... 
	- HttpSessionListener接口
	    - HttpSessionListener用于监听HttpSession对象的创建和销毁
	        - 当HttpSession对象被创建时，激发sessionCreated(HttpSessio se)方法
	        - 当HttpSession对象被销毁时，激发sessionDestroyed(HttpSessionEvent se)方法 
	        - **浏览器关闭并不意味着session声明周期的结束**，该session还是会在服务器上存活一定的时间的，在此期间我们重新启动浏览器，在访问的URL后附加session id还是可以找到该session的，此时服务器也不会重新创建新的session，如使用`http://localhost:9999/ListenerDemo/test.jsp?jsession=F1CCE48CD9599BC7C3512FD8923A59F2` 访问被关闭的页面，若服务器上人存在该session，则返回该session，不再重新创建新的session对象      
	- ServletRequestListener接口
	    - ServletRequestListener接口用于监听ServletRequest对象的创建和销毁
	        - 创建一个ServletRequest对象时，激发requestInitialized(ServletRequest sre)方法
	        - 销毁一个ServletRequest对象时，激发requestDestroy(ServletRequestEvent sre)方法  
	   
### 五、域对象中属性的变更的事件监听器
- 域对象中属性的变更的事件监听器就是用来监听ServletContext，HttpSession，ServletRequest这三个对象中的属性变更信息事件的监听器
- 相关的接口
    - ServletContextAttributeListener
    - HttpSessionAttributeListener
    - ServletRequestAttributeListener
    - 实现三个接口的类需要在web.xml中进行注册
    - ` <listener> <listener-class>com.airyoureyes.listener.TestAttributeListener/listener-class>    </listener>`
- 上面的三个接口中都定义了三个方法来处理被监听对象中的属性的增加，删除与替换的事件，同一事件在这三个接口中对应的方法名称完全相同，只是接受的参数类型不同，三个方法分别是：
    - attributeAdded方法：
        - 当向被监听对象中增加一个属性时，web容器就调用事件监听器的attributeAdded方法进行相应的处理，这个方法接口一个事件类型的参数，监听器可以通过这个参数来获取正在增加属性的域对象和保存到域中的属性对象
        - 各个域属性监听器中的完整语法定义为：
            - public void attributeAdded(ServletContextAttributeEvent scae)
            - public void attributeAdded(HttpSessionBindingEvent hsbe)
            - public void attributeAdded(ServletRequestAttributeEvent srae)
    - attributeReplaced方法
        - 各个域属性监听器中的完整语法定义为： 
	        - public void attributeReplaced(ServletContextAttributeEvent scae)
	        - public void attributeReplaced(HttpSessionBindingEvent hsbe)
	        - public void attributeReplaced(ServletRequestAttributeEvent srae)
    - attributeRemoved方法     
        - 各个域属性监听器中的完整语法定义为：   
	        - public void attributeRemoved(ServletContextAttributeEvent scae)
	        - public void attributeRemoved(HttpSessionBindingEvent hsbe)
	        - public void attributeRemoved(ServletRequestAttributeEvent srae)
- 可以通过事件对象中的getName，和getValue方法获取被监听的域的对象中增加的属性和值，不过要注意的是在attributeRemoved方法中获取的是被替换之前的值，而不是已经替换的新值

### 六、 感知Session绑定的事件监听器（了解）
- 保存在Session域中的对象可以有多种状态：绑定到Session中；从Session域中解除绑定；随Session对象持久化到一个存储设备当中；随Session对象从一个存储设备中恢复
- Servlet规范中定义了两个特殊的监听接口来帮助Javabean对象了解自己在Session域中的这些状态
    - HttpSessionBindingListener接口
        - 实现了HttpSessionBindingListener接口中的Javabean对象可以感知自己被绑定到Session中和从Session中删除的事件
        - 当对象被绑定到HttpSession对象中时，web服务器调用该对象的void valueBound(HttpSessionBindingEvent event)方法
        - 当对象从HttpSession对象中解除 绑定时，web服务器调用该对象中的void valueUnbound(HttpSessionBindingEvent event)方法
    - HttpSessionActivationListener接口
        - 实现了HttpSessionActivationListener接口的JavaBean对象可以感知自己被活化和钝化的事件（活化指的是从硬盘中读到内存中，钝化指的是从内存写到硬盘中）
        - 当绑定到HttpSession对象中的对象将要随HttpSession对象被钝化之前，web服务器调用该对象的void sessionWillPassivate(HttpSessionBindingEvent event)方法
        - 当绑定到HttpSession对象中的对象将要随HttpSession对象被活化之后，web服务器调用该对象的void sessionDidActive(HttpSessionBingdingEvent event)方法 
    - 实现上述两个接口不需要在web.xml文件中进行注册 