- 什么是自定义标签
	- 用户定义的一种自定义的jsp标记，当一个含有自定义标签的jsp页面被jsp引擎编译成servlet时，tag标签被转化成了对一个被称为**标签处理类**的对象操作。于是当jsp页面被jsp引擎转化为servlet时后，实际上tag标签被转化为了对tag类的操作
	- 标签库API  
	![tag_class_hierarchy](images/tag_class_hierarchy.png)  
	- 传统标签与简单标签
		- 开发自定义标签，**其核心就是要编写处理器类**，一个标签对应一个标签处理器类，而一个标签库则是很多标签处理器类的集合。所有的标签处理器类都要实现JspTag接口，该接口中没有定义任何的方法，重要作为Tag和SimpleTag接口的父接口
		- 在JSP2.0以前，所有标签处理器类必须实现Tag接口，这样的标签叫做传统标签
		- JSP2.0规范中又定义了一种新的类型的标签，称为简单标签，其对应的处理器类要实现SimpleTag接口
	- 实现SimpleTag接口的标签处理类的声明周期
		1. JSP引擎将代表JSP页面的pageContext对象传递给标签处理类对象（即调用setJspContext方法）
		2. JSP引擎将父标签处理器对象传递给当前标签处理器对象。只有存在父标签时，JSP引擎才会调用该方法（即调用setParent方法）
		3. 设置标签属性。只有定义属性才调用该方法（即调用setXXX方法）
		4. 若存在标签体，JSP引擎将标签体封装成一个JspFragment对象，调用setJspBody方法将JspFrament对象传递给标签处理器对象。若标签体为空，这setJspBody将不会被JSP引擎调用
		5. 容器调用处理器对象的doTag方法执行标签逻辑
	- 自定义标签的开发与应用步骤
		- 编写完成标签功能的Java类（标签处理器类）：实现SimpleTag接口
		- 表写标签库中的描述（tld）文件，在tld文件中对自定义标签进行描述，如下是对自定义标签类的描述：    
		![tld_format](images/tld_format.png)  
		- 在JSP页面中导入和使用自定义标签
			- 使用taglib指令导入标签库    
			![import_taglib](images/import_taglib.png)  
			- 使用自定义标签  
			![use_my_taglib](images/use_my_taglib.png) 
   
- 定义带属性的自定义标签
	- 先在标签处理器类中定义setter方法，建议把所有的属性类型都设置为String类型，如：  
	```java
		private String value;
		private String count;
		
		public void setValue(String value){
			this.value = value;
		}
		
		public void setCount(String count){
			this.count = count;
		}
	```    
	- 此时在标签处理类中可以直接使用这些属性  
	```java
		public void doTag() throws JspException, IOException {
		int c = Integer.parseInt(count);
			for (int i = 0; i < c; i++){
				pageContext.getOut().print((i + 1) + ": " + value + "<br />");
			}
		}
	```  
	-  在tablib文件中描述属性，子标签name的值参照自定义标签类中定义的属性    
	```xml
		<attribute>
	  		<!-- 属性名，需要与标签处理器类的setter方法定义的属性相同 -->
	  		<name>value</name>
	  		<!-- 该属性是否是必须的 -->
	  		<required>true</required>
	  		<!-- rtexpvalue: runtime expression value-->
	  		<!-- 表示的是当前属性是否可接受运行时表达式的动态值 -->
	  		<rtexprvalue>true</rtexprvalue>
	  	</attribute>
	  	
		<attribute>
	  		<!-- 属性名 -->
	  		<name>count</name>
	  		<!-- 该属性是否是必须的 -->
	  		<!-- 如下代码表示的是若在jsp标签中没有count属性是被允许的 -->
	  		<required>false</required>
	  		<!-- rtexpvalue: runtime expression value-->
	  		<!-- 表示的是当前属性是否可接受运行时表达式的动态值 -->
	  		<!-- 如下代码表示的是若在jsp页面中使用el表达式给count赋值，会提示："count" does not support runtime expressions -->
	  		<rtexprvalue>false</rtexprvalue>
	  	</attribute>
	```   
	- 在页面中使用属性，属性名与tld文件中定义的名字相同
	```jsp
		<mytag:helloworld value="${param.name}" count="10"/>
	```

- 在开发简单标签中，我们一般直接继承SimpleTagSupport
	- SimpleTagSupport类
		- 该类实现了SimpleTag接口
		- SimpleTagSupport不仅实现了SimpleTag中的所有方法，还定义了getJspContext，getJspBody，findAncestorWithClass方法
		- 在类中还定义了三个成员变量，分别是JspTag对象parentTag，JspContext对象jspContext，JspFragment对象jspBody，这三个对象那个分别可以通过getParent，getJspContext，getJspBody方法获得，这三个方法只能被其子类使用

-  带标签体的自定义标签
	- 所谓的标签体类似我们之前学过的HTML标签中的文本内容，如`<mytag:uppercase>abcdefghijk</mytag:uppercase>`，`mytag:uppercase`标签的标签体就是`abcdefghijk`
	- SimpleTagSupport类中有两个与标签体相关的方法，一个方法为setJspBody，该方法是被jsp引擎调用的，当一个标签带有标签体时，jsp引擎会调用该方法设置SimpleTagSupport中代表标签体的JFragment对象，还有另外一个getJspBody方法，继承SimpleTagSupport的子类可以通过该方法获取标签体对象
	- JspFragment类中有一个比较重要的方法，就是invoke(Writer)方法
		- 该方法会直接将标签体的内容输出到字符流中（也就是输出到Writer对象）
		- 如若Writer对象为null，则默认将标签体的内容输出到jsp的out对象，即输出到jsp页面上
		- StringWriter是Writer的子类。我们可以使用它来接收代表标签体内容的字符流，这样做有一个好处就是我们可以在标签处理器中获取标签体中的内容并直接进行处理，如将标签体中的有小写字母全转化成大写字符等。如在下面的标签处理类中我们利用StringWriter获取标签体的内容，并将其转化成大写字母的形式
		- 处理标签体时，tld文件的相关配置：在配置tld文件时，tag标签有一个名为body-content的子标签，它代表的是标签体的类型。标签体有三种类型，一种为empty，代表的是标签体为空；一种是scriptless，表示的是标签体中允许使用EL表达式，jsp动作，但是不能是其他的代码，如java代码等;最后一种能够是tagdependent，它表示的是标签体中内容不会进行任何的处理就输出，即把标签体内容当做普通的文本处理，标签体内容是什么就输出什么，其中若有EL表达式也不会被执行

- 实例：实现类似于JSTL标签库中的foreach功能的标签
1 定义标签处理器类ForEach：

```java
	package com.wxx.tag;
	
	import java.io.IOException;
	import java.util.Collection;
	
	import javax.servlet.jsp.JspException;
	import javax.servlet.jsp.tagext.SimpleTagSupport;
	
	public class ForEach extends SimpleTagSupport{
		//表示要遍历的集合
		private Collection<?> items;
		//代表集合元素的对象名
		private String var;
		
		//jsp引擎自动将要遍历的对象传过来
		public void setItems(Collection<?> items) {
			this.items = items;
		}
		
		//设置集合元素对象名
		public void setVar(String var){
			this.var = var;
		}
		//执行标签体
		@Override
		public void doTag() throws JspException, IOException {
			//遍历集合
			for (int i = 0; i < items.size(); i++){
				//将var关联当前被遍历的元素
				this.getJspContext().setAttribute(var, items.toArray()[i]);
				//直接输出到页面
				this.getJspBody().invoke(null);
			}
		}
	}

``` 
2 在tld文件中进行相关的配置：

```xml
	<?xml version="1.0" encoding="UTF-8"?>
	
	<taglib xmlns="http://java.sun.com/xml/ns/j2ee"
	    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	    xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-jsptaglibrary_2_0.xsd"
	    version="2.0">
	    
	  <description>MyJSTL 3.0 core library</description>
	  <display-name>MyJSTL core</display-name>
	  <tlib-version>3.0</tlib-version>
	  <!-- 建议在jsp页面中使用的前缀，当然我们也可以在jsp页面的taglib指定中指定前缀 -->
	  <short-name>mytag</short-name>
	  <!-- 作为tld文件的id，用于唯一标识当前的tld文件，多个tld文件的URI不能重复，通过jsp页面的taglib标签的uri属性来引用 -->
	  <uri>http://wxx.com/jsp/jstl/core3</uri>
	 
	  <tag>
		 <!-- 标签体的名字 -->  
	  	 <name>foreach</name>
	  	 <!-- 标签处理器类 -->
	  	 <tag-class>com.wxx.tag.ForEach</tag-class>
	  	 <!-- 标签体的类型 -->
	  	 <body-content>scriptless</body-content>
	  	 
	  	 <attribute>
	  	 	<name>items</name>
	  	 	<required>true</required>
	  	 	<rtexprvalue>true</rtexprvalue>
	  	 </attribute>
	  	 
	  	 <attribute>
	  	 	<name>var</name>
	  	 	<required>true</required>
	  	 	<rtexprvalue>true</rtexprvalue>
	  	 </attribute>
	  </tag>
	  
	</taglib>

```

3 在jsp页面上引用自定义的foreach标签

```jsp
	<%@ page language="java" contentType="text/html; charset=UTF-8"
	    pageEncoding="UTF-8" import="com.wxx.bean.Customer, java.util.*"%>
	<%@ taglib prefix="mytag" uri="http://wxx.com/jsp/jstl/core3" %>
	<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Insert title here</title>
	</head>
	<body>
	
		<%
			List<Customer> customers = new ArrayList<Customer>();
			customers.add(new Customer(1, "aaa", "gz"));
			customers.add(new Customer(2, "bbb", "sh"));
			customers.add(new Customer(3, "ccc", "bj"));
			customers.add(new Customer(4, "ddd", "sz"));
			request.setAttribute("customers", customers);
		%>
		
		<mytag:foreach items="${requestScope.customers }" var="customer">
			--${customer.id }, --${customer.name }, --${customer.address} <br /> 
		</mytag:foreach>
	</body>
	</html>
```
4 程序执行的结果如下所示：

```
 --1, --aaa, --gz 
 --2, --bbb, --sh 
 --3, --ccc, --bj 
 --4, --ddd, --sz 
```

- 引用父标签
	- 子标签可以通过getParent方法获取父标签的引用，将获得的引用进行强转可获取父标签的细节
	- 父标签无法得到子标签的引用，父标签仅将子标签当标签体引用
	- 注意：在tld配置文件中，无需为父标签设置额外的配置，但子标签是以标签体的形式存在的，所以父标签的`<body-content></body-content>`需设置为scriptless

- EL自定义函数
	- 为了简化在jsp页面上操作字符串，JSTL中提供了一套EL自定义函数，这些自定义函数包含了jsp页面经常使用要用到的字符操作
	- 在EL表达式中调用了某个Java类中的静态方法，这个静态方法需要在web应用程序中进行配置才可以被EL表达式调用
	- 在JSTL的表达式中要使用一个函数，其格式如下`${ns:methodName(args...)}`，在使用这些函数之前必须在jsp中引入标准函数的声明`<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>`
	- EL自定义函数可以拓展EL表达式的功能，让EL表达式完成普通Java程序代码所能完成的功能
	- 编写自定义函数的开发步骤：
		- 编写EL自定义函数映射的Java类中的静态方法：这个Java类必须带有public修饰符，方法必须是这个类的带public修饰符的静态方法，如：
		```java
			package com.wxx.tag;
			public class Concat {
				
				public static String concat(String str1, String str2){
					return str1 + str2;
				}
			}
		```
		- 编写标签库中的描述文件（tld）文件，在tld文件中描述自定义函数，如
		```
			 <function>
			  	<name>concat</name>
			  	<function-class>com.wxx.tag.Concat</function-class>
			  	<function-signature>java.lang.String concat(java.lang.String, java.lang.String)</function-signature>
			  </function>
		```
		- 在jsp页面中导入和使用自定义函数
			- 导入tld标签：使用taglib指令，如`<%@ taglib prefix="mytag" uri="http://wxx.com/jsp/jstl/core" %>`
			- 调用EL自定义函数：${prefixName:functionName(paramList)}，如`${mytag:concat(param.str1, param.str2) }`
				- prefixName指的是taglib中prefix属性的值
				- functionName指的是Java中静态方法的名字
				- paramList指的是参数列表  

	
		   