package com.airyoureyes.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.airyoureyes.vo.Authority;
import com.airyoureyes.vo.User;

public class UserDao {
	private static Map<String, User> users;
	private static List<Authority> authorities;
	
	//static块中相当于操作数据库
	static {
		//权限列表
		authorities = new ArrayList<>();
		authorities.add(new Authority("Article-1", "/article-1.jsp"));
		authorities.add(new Authority("Article-2", "/article-2.jsp"));
		authorities.add(new Authority("Article-3", "/article-3.jsp"));
		authorities.add(new Authority("Article-4", "/article-4.jsp"));
		
		users = new HashMap<String, User>();
		//添加用户AAA及权限
		User user1 = new User("AAA", authorities.subList(0, 2));
		users.put("AAA", user1);
		//添加用户BBB及权限
		User user2 = new User("BBB", authorities.subList(2, 4));
		users.put("BBB", user2);
	}
	//根据名字获取对应的User信息
	public static User get(String username){
		return users.get(username);
	}
	//更新用户的权限信息
	public static void update(String username, List<Authority> authorities){
		//先获取指定的用户，然后更改其权限
		users.get(username).setAuthorities(authorities);
	} 
	//获取所有的权限列表
	public static List<Authority> getAuthorities(){
		return authorities;
	}
}
