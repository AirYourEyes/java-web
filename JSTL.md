### 一、简介
- 英文：JSP Standard Tag Library
- 中文：JSP标准标签库
- 封装了JSP应用的通用核心功能

### 二、分类
- 核心标签
	- 通过`<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>`
- 格式化标签
- SQL标签（了解）
- XML标签（了解）
- JSTL函数

### 三、安装JSTL
- 首先下载[jakarta-taglibs-standard-1.1.2](lib/jakarta-taglibs-standard-1.1.2.zip)
- 然后将`jakarta-taglibs-standard-1.1.2.zip\jakarta-taglibs-standard-1.1.2\lib`目录下的两个jar包拷贝到`/WEB-INF/lib/`目录下
- 最后使用taglib命令在JSP页面中导入要使用JSTL，如要使用核心库，使用以下命令`<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>`

### 四、详细介绍
#### 1. 核心标签
- `c:out`
	- 显示表达式结果
	- value属性支持EL表达式
	- 例子：`<c:out value="${requestScope.name} "></c:out>` 
- `c:set`
	- 给指定的变量或者java bean的指定属性设置值
	- target与value属性均支持EL表达式 
	- 典型用法：
		- 给变量赋值，并且放置在指定的作用域
			- 如：`<c:set var="age" value="123" scope="session"></c:set>`
		- 给java bean赋值
			- 如：`<c:set target="${requestScope.customer }" property="id" value="1999"></c:set>`
- `c:remote`
	- 移除指定范围的变量，若不指定范围，默认清除其第一次出现的作用域
	- 语法`<c:remove var="<string>" scope="<string>"/>`
	- 如：`<c:remove var="age" scope="session"/>`
- `c:if`
	- 相当于if语句
	- 语法：`<c:if test="<boolean>" var="<string>" scope="<string>"></c:if>`
		- test属性表示测试条件，支持EL表达式
		- var保留test的结果，为false或者true
		- scope将var表示的变量的值放置在指定的范围内，如request中
	- 如`<c:if test="${requestScope.score>60}" var="isSuccess" scope="request">恭喜你及格了</c:if>`  
- `c:choose, c:when, c:otherwise`
	- 类似于swicth语句，且每一个分支都有break结束
	- 语法：
		- ```
			<c:choose>
			    <c:when test="<boolean>"/>
			        ...
			    </c:when>
			    <c:when test="<boolean>"/>
			        ...
			    </c:when>
			    ...
			    ...
			    <c:otherwise>
			        ...
			    </c:otherwise>
			</c:choose>
		```
	- `c:choose`作为`c:when`和`c:otherwise`标签父标签
	- `c:otherwise`标签要在`c:when`之后
	- 如： 
		- ```
			<c:choose>
				<c:when test="${requestScope.score > 90 }">A</c:when>
				<c:when test="${requestScope.score > 80 }">B</c:when>
				<c:when test="${requestScope.score > 70 }">C</c:when>
				<c:when test="${requestScope.score > 60 }">D</c:when>
				<c:otherwise>E</c:otherwise>
			</c:choose>
		```
- `c:forEach`
	- 普通用法：
		- 如： 
		- ```
			<c:forEach var="i" begin="1" end="10" step="1">
				${i }<br />
			</c:forEach>
		```
	- 遍历数组
		- 如：
		- ```
			<%
				String[] strs = {"a", "b", "c", "d", "e"};
				request.setAttribute("strs", strs);
			%>
			<c:forEach var="str" items="${strs }">
				${str }<br />
			</c:forEach>
		```
	- 遍历集合
		- 如：
		- ```
			<% 
				List<Customer> customers = new ArrayList<Customer>();
				customers.add(new Customer(1, "AAA"));
				customers.add(new Customer(2, "BBB"));
				customers.add(new Customer(3, "CCC"));
				customers.add(new Customer(4, "DDD"));
				request.setAttribute("customers", customers);
			%>
			<c:forEach var="customer" items="${customers}">
				${customer.id}--${customer.name }<br />
			</c:forEach>
		``` 
	- 遍历Map
		- 如：
			<%
				Map<String, Customer> customerMap = new HashMap<String, Customer>();
				customerMap.put("1", new Customer(1, "AAA"));
				customerMap.put("2", new Customer(2, "BBB"));
				customerMap.put("3", new Customer(3, "CCC"));
				customerMap.put("4", new Customer(4, "DDD"));
				customerMap.put("5", new Customer(5, "EEE"));
				request.setAttribute("customerMap", customerMap);
			%>
			<c:forEach var="customer" 	items="${customerMap }">
				${customer.key }--${customer.value.id }--${customer.value.name }<br />
			</c:forEach>
		- 要注意的是EL是通过get方法来获取相对应的属性值的，我们使用c:forEach遍历Map的时候，每一个var指明的变量都是Map.Entry类的对对象，Map.Entry类中包含getValue和getKey方法，所以可以通过EL表达式获取value和key
	   