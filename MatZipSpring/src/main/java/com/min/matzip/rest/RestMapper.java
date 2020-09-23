package com.min.matzip.rest;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.min.matzip.rest.model.RestDMI;
import com.min.matzip.rest.model.RestPARAM;
import com.min.matzip.rest.model.RestRecMenuVO;


@Mapper
public interface RestMapper { //interface에는 public abstract가 생략되어있음
	int insRest(RestPARAM param);
	int insRestRecMenu(RestRecMenuVO param);
	List<RestDMI> selRestList(RestPARAM param);
	RestDMI selRest(RestPARAM param);
	List<RestRecMenuVO> selRestRecMenus(RestPARAM param);
	int delRestRecMenu(RestPARAM param);
	int delRestMenu(RestPARAM param);
	int delRest(RestPARAM param);
}
