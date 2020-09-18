package com.min.matzip.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.min.matzip.Const;
import com.min.matzip.SecurityUtils;
import com.min.matzip.model.UserDMI;
import com.min.matzip.user.model.UserDTO;
import com.min.matzip.user.model.UserVO;

@Service
public class UserService {
	
	@Autowired //만들어진 DAO를 Autowired를 써서 주소값을 불러온다
	private UserMapper mapper;
	
	//1번 로그인 성공, 2번 아이디없음, 3번 비번 틀림
	public int login(UserDTO param) {
		if(param.getUser_id().equals("")) {
			return Const.NO_ID;
		}
		
		UserDMI dbUser = mapper.selUser(param);
		System.out.println("pw : " + dbUser.getUser_pw());
		return 2;
	}
	
	public int join(UserVO param) {
		String pw = param.getUser_pw();
		String salt = SecurityUtils.generateSalt();
		String cryptPw = SecurityUtils.getEncrypt(pw, salt);
		
		param.setSalt(salt);
		param.setUser_pw(cryptPw);
		
		return mapper.insUser(param);
	}
}
