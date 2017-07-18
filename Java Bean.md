# Java Bean(了解即可)
1. 简介
	- 用过Java bean的类必须具有公共的、无参数的构造方法
	- Java bean的属性和普通的Java类的属性的概念不同，Java bean的属性是以方法定义的形式出现的
	- 用于对属性赋值的方法成为属性的修改器或setter方法，用于读取属性值方法称为属性访问器或者getter方法
	- 属性修改器必须以小写的set前缀开始，后跟属性名，且属性名的第一个字母必须大写
	- 属性访问器必须以小写的get前缀开始，后跟属性名，且属性名的第一个字母必须大写
	- Java bean的属性名时根据setter和getter方法的名称来生成的
2. Java bean在jsp中的应用
    - `<jsp:useBean>`标签
    	- 用于在某一个指定的域范围（application、session、request、pageContext等）中查找一个指定名称的Javabean对象，如果存在则直接返回该Javabean的应用，如果不存在的话则实例化一个新的Javabean对象并将它按指定的名称存储在指定的范围内
    	```jsp

			<jsp:useBean id="customer" class="bean.Customer" scope="session" />
			============================等价于========================================
			<%
				//从session中获取id名为customer的对象
				Customer customer = (Customer)session.getAttribute("customer");
				//若该对象不存在于scope指定的范围内，则利用反射创建该对象并且将其放入到scope指定的范围中
				if (customer == null){
					customer = (Customer)Class.forName("bean.Customer").newInstance();
					session.setAttribute("customer", customer);
				} 
			%>
		```  
    	- 常见的语法：`<jsp:useBean id="beanInstanceName" class="package.class" scope="page|request|session|application">`
    	    - class属性用于指定Javabean的完整类名（即必须带包名）
    	    - id属性用于指定Javabean实例对象的引用名称和其存储在域范围中的名称
    	    - scope属性用于指定Javabean实例对象所存储的范围，其取值只能为page、request、session和application中的一种，默认是page
    	
    - `<jsp:setProperty>`标签
        - 语法：`<jsp:setProperty name="beanInstanceName" property="propertyName" value="setValue"/>`
        - 如以下的代码：  
        ``` jsp
			<jsp:setProperty name="customer" property="id" value="13" />
		```
    - `<jsp:getProperty>`标签
        - 语法：`<jsp:getProperty name="beanInstanceName" property="propertyName" />`
        - 如以下的代码：
        ```
			ID: <jsp:getProperty name="customer" property="id" />
		```

