package com.min.matzip.user.model;

public class UserPARAM extends UserVO{ //data transfer object , db이름과 view이름이 다를때 변환시킬때 쓴다.
	private String msg;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
