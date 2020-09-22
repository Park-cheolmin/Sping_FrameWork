package com.min.matzip.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.min.matzip.model.CodeVO;
import com.min.matzip.model.CommonMapper;
import com.min.matzip.rest.model.RestDMI;
import com.min.matzip.rest.model.RestPARAM;

@Service
public class RestService {
	
	@Autowired
	private RestMapper mapper;
	
	@Autowired
	private CommonMapper cMapper;
	
	public List<RestDMI> selRestList(RestPARAM param) {
		return mapper.selRestList(param);
	}
	
	public List<CodeVO> selCategoryList() {
		CodeVO p = new CodeVO();
		p.setI_m(1); //음식점 카테고리 코드 = 1
		
		return cMapper.selCodeList(p);
	}

	public int insRest(RestPARAM param) {
		return mapper.insRest(param);
	}
	
	public RestDMI selRest(RestPARAM param) {
		
		return mapper.selRest(param);
	}
}
