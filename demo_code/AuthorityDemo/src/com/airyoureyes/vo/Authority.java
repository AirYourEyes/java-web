package com.airyoureyes.vo;

public class Authority {
	private String displayName;//Ȩ�޵�����
	private String url;//��ǰȨ�޶�Ӧ��url��һ��Ȩ�޶�Ӧһ��url��
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
