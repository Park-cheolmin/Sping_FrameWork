package com.min.matzip.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.min.matzip.Const;
import com.min.matzip.ViewRef;
import com.min.matzip.user.model.UserDTO;
import com.min.matzip.user.model.UserVO;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired  //빈등록(스프링이 객체화시킨 것) 중에 자동으로 가져온다
	private UserService service;
	
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public String login(Model model) {
		model.addAttribute(Const.TITLE, "로그인");
		model.addAttribute(Const.VIEW, "user/login");
		return ViewRef.TEMP_DEFAULT;
	}
	
	@RequestMapping(value="/login", method = RequestMethod.POST)
	public String login(UserDTO param) {
		int result = service.login(param);
		
		if(result == 1) {
			return "redirect:/rest/map";
		}
		
		
		return "redirect:/user/login?err=" + result;
	}
	
	@RequestMapping(value="/join", method = RequestMethod.GET)
	public String join(Model model, @RequestParam(required = false, defaultValue="0") int err) { //딱 한값만 받고싶을때 사용하는 방법  //String값대신에 int로 받을수 있다 
		System.out.println("err : " + err);
		
		if(err > 0) {
			model.addAttribute("msg", "에러가 발생하였습니다.");
		}
		model.addAttribute(Const.TITLE, "회원가입"); 				//required=true key값에대한 value값이 필수로 잇어야함 ex) localhost8089:/user/login에 err이잇어야함
		model.addAttribute(Const.VIEW, "user/join");
		return ViewRef.TEMP_DEFAULT;
	}
	
	@RequestMapping(value="/join", method = RequestMethod.POST) //join.jsp에서 날라와서 param에 저장됨 
	public String join(UserVO param) {
		int result = service.join(param);  //그걸 서비스로 넘김
		
		if(result == 1) {
			return "redirect:/user/login";
		}
		return "redirect:/user/join?err=" + result;
	}
}
