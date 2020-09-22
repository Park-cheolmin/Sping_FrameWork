package com.min.matzip.user;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.min.matzip.Const;
import com.min.matzip.ViewRef;
import com.min.matzip.user.model.UserPARAM;
import com.min.matzip.user.model.UserVO;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired  //빈등록(스프링이 객체화시킨 것) 중에 자동으로 가져온다
	private UserService service;
	
	@RequestMapping(value="/logout", method = RequestMethod.GET) 
	public String logout(HttpSession hs) {
		hs.invalidate();
		return "redirect:/";
	}
	
	@RequestMapping(value="/login", method = RequestMethod.GET) //GET 화면열기
	public String login(Model model) {
		model.addAttribute(Const.TITLE, "로그인");
		model.addAttribute(Const.VIEW, "user/login");
		return ViewRef.TEMP_DEFAULT;
	}
	
	@RequestMapping(value="/login", method = RequestMethod.POST) //처리
	public String login(UserPARAM param, HttpSession hs, RedirectAttributes ra) {
		int result = service.login(param);
		
		if(result == Const.SUCCESS) {
			hs.setAttribute(Const.LOGIN_USER, param); //로그인 됬을때 세션에다가 박음
			return "redirect:/rest/map";
		}
		
		String msg = null;
		if(result == Const.NO_ID) {
			msg = "아이디를 확인해 주세요.";
		} else if(result == Const.NO_PW) {
			msg = "비밀번호를 확인해 주세요.";
		}
		
		param.setMsg(msg);
		ra.addFlashAttribute("data", param); //객체로 넘어감, 주소값에 쿼리스트링으로 값이 박히지않음. 	
															//addFlashAttribute : session에 박히고 쓰고나면 세션에서 지움, 마치 post처럼 사용가능
		return "redirect:/user/login";
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
	public String join(UserVO param, RedirectAttributes ra) {
		int result = service.join(param);  //그걸 서비스로 넘김
		
		if(result == 1) {
			return "redirect:/user/login";
		}
		
		ra.addAttribute("err", result);  //주소값에 쿼리스트링으로 박음
		return "redirect:/user/join";
	}
	
	@RequestMapping(value="/ajaxIdChk", method = RequestMethod.POST)
	@ResponseBody //이거 주면 jsp파일 찾지 않는다. 이자체가 응답결과물임
	public String ajaxIdChk(@RequestBody UserPARAM param) {
		System.out.println("user_id : " + param.getUser_id());
		int result = service.login(param);
		
		return String.valueOf(result);
	}
}
