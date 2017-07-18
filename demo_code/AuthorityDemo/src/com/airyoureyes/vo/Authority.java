package com.airyoureyes.vo;

public class Authority {
	private String displayName;//权限的名称
	private String url;//当前权限对应的url（一个权限对应一个url）
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Authority(String displayName, String url) {
		super();
		this.displayName = displayName;
		this.url = url;
	}
	
}
