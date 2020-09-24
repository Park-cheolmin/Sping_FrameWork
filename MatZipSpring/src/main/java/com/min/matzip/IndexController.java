package com.min.matzip;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	
	@RequestMapping("/")
	public String index(HttpServletRequest req) {
		if(Const.realPath == null) {   //서버 구동하자마자 실행한다.
			Const.realPath = req.getServletContext().getRealPath("");
		}
		System.out.println("root!!");
		return "redirect:/rest/map";
	}
}
