package com.airyoureyes.vo;

import java.util.List;

public class User {
	private String username;//�û���
	private List<Authority> authorities;//�û�ӵ�е�Ȩ��
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public List<Authority> getAuthorities() {
		return authorities;
	}
	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}
	public User(String username, List<Authority> authorities) {
		super();
		this.username = username;
		this.authorities = authorities;
	}
	
}
