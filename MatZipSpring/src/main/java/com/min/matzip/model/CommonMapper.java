package com.min.matzip.model;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommonMapper {
	List<CodeVO> selCodeList(CodeVO p); //Common mapper을 이용해 DAO를 이용하고 Bean등록까지한다
}
