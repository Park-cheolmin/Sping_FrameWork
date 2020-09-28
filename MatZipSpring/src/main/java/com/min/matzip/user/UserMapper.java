package com.min.matzip.user;

import org.apache.ibatis.annotations.Mapper;

import com.min.matzip.user.model.UserDMI;
import com.min.matzip.user.model.UserPARAM;
import com.min.matzip.user.model.UserVO;


@Mapper
public interface UserMapper {
	public int insUser(UserVO p); //insUser와 (UserMapper.xml에)있는 id와 같으면됨
	public int insFavorite(UserPARAM param);
	
	public UserDMI selUser(UserPARAM p);
	
	public int delFavorite(UserPARAM param);
}
