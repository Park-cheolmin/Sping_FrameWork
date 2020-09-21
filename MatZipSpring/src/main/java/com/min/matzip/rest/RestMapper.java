package com.min.matzip.rest;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.min.matzip.rest.model.RestDMI;
import com.min.matzip.rest.model.RestPARAM;


@Mapper
public interface RestMapper {
	public List<RestDMI> selRestList(RestPARAM param);
	public List<RestDMI> insRest(RestPARAM param);
}
