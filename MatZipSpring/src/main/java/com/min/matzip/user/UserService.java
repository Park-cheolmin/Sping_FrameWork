package com.min.matzip.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.min.matzip.Const;
import com.min.matzip.SecurityUtils;
import com.min.matzip.rest.RestMapper;
import com.min.matzip.rest.model.RestPARAM;
import com.min.matzip.rest.model.RestRecMenuVO;
import com.min.matzip.user.model.UserDMI;
import com.min.matzip.user.model.UserPARAM;
import com.min.matzip.user.model.UserVO;

@Service
public class UserService {
	
	@Autowired //만들어진 DAO를 Autowired를 써서 주소값을 불러온다
	private UserMapper mapper;
	
	@Autowired
	private RestMapper restMapper;
	
	//1번 로그인 성공, 2번 아이디없음, 3번 비번 틀림
	public int login(UserPARAM param) {
		if(param.getUser_id().equals("")) { return Const.NO_ID;	}
		
		UserDMI dbUser = mapper.selUser(param);  
		if(dbUser == null) {	 return Const.NO_ID; }
		
		String cryptPw = SecurityUtils.getEncrypt(param.getUser_pw(), dbUser.getSalt());
		if(!cryptPw.equals(dbUser.getUser_pw())) { return Const.NO_PW; }
		
		param.setI_user(dbUser.getI_user());
		param.setUser_pw(null);
		param.setNm(dbUser.getNm());
		param.setProfile_img(dbUser.getProfile_img());
		return Const.SUCCESS;
		
	}
	
	public int join(UserVO param) {
		String pw = param.getUser_pw();
		String salt = SecurityUtils.generateSalt();
		String cryptPw = SecurityUtils.getEncrypt(pw, salt);
		
		param.setSalt(salt);
		param.setUser_pw(cryptPw);
		
		return mapper.insUser(param);
	}
	
	public int ajaxToggleFavorite(UserPARAM param) { //PARAM에 i_user, i_rest, proc_type이 들어있음
		switch(param.getProc_type()) {
		case "ins" : 
			return mapper.insFavorite(param);
		case "del" :
			return mapper.delFavorite(param);
		}
		return 0;
	}
	
	public List<UserDMI> selFavoriteList(UserPARAM param) {	
		List<UserDMI> list  = mapper.selFavoriteList(param); //유창반점, 태산만두, 뉴욕피자 큰걸 가져오는 list
		
		for(UserDMI vo : list) {	//하나 당 각각 추천 메뉴를 가져오기위해 for each문 사용
			RestPARAM param2 = new RestPARAM();
			param2.setI_rest(vo.getI_rest());
			
			List<RestRecMenuVO> eachRecMenuList = restMapper.selRestRecMenus(param2);
			vo.setMenuList(eachRecMenuList);
		}
		
		return list;
	}
}






















