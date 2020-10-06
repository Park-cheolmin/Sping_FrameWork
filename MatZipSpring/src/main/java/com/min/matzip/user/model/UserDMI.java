package com.min.matzip.user.model;

import java.util.List;

import com.min.matzip.rest.model.RestRecMenuVO;

public class UserDMI extends UserVO {
	private int i_rest;
	private String rest_nm;
	private String rest_addr;
	private List<RestRecMenuVO> menuList; //이미지 한개 저장 ( 여러개하려고 LIST로 사용)
	
	
	public String getRest_nm() {
		return rest_nm;
	}
	public void setRest_nm(String rest_nm) {
		this.rest_nm = rest_nm;
	}
	public String getRest_addr() {
		return rest_addr;
	}
	public void setRest_addr(String rest_addr) {
		this.rest_addr = rest_addr;
	}
	public int getI_rest() {
		return i_rest;
	}
	public void setI_rest(int i_rest) {
		this.i_rest = i_rest;
	}
	public List<RestRecMenuVO> getMenuList() {
		return menuList;
	}
	public void setMenuList(List<RestRecMenuVO> menuList) {
		this.menuList = menuList;
	}
	
}
