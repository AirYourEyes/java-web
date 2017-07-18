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
	
	//static�����൱�ڲ������ݿ�
	static {
		//Ȩ���б�
		authorities = new ArrayList<>();
		authorities.add(new Authority("Article-1", "/article-1.jsp"));
		authorities.add(new Authority("Article-2", "/article-2.jsp"));
		authorities.add(new Authority("Article-3", "/article-3.jsp"));
		authorities.add(new Authority("Article-4", "/article-4.jsp"));
		
		users = new HashMap<String, User>();
		//����û�AAA��Ȩ��
		User user1 = new User("AAA", authorities.subList(0, 2));
		users.put("AAA", user1);
		//����û�BBB��Ȩ��
		User user2 = new User("BBB", authorities.subList(2, 4));
		users.put("BBB", user2);
	}
	//�������ֻ�ȡ��Ӧ��User��Ϣ
	public static User get(String username){
		return users.get(username);
	}
	//�����û���Ȩ����Ϣ
	public static void update(String username, List<Authority> authorities){
		//�Ȼ�ȡָ�����û���Ȼ�������Ȩ��
		users.get(username).setAuthorities(authorities);
	} 
	//��ȡ���е�Ȩ���б�
	public static List<Authority> getAuthorities(){
		return authorities;
	}
}
