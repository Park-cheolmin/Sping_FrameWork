package com.min.matzip.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.min.matzip.Const;
import com.min.matzip.SecurityUtils;
import com.min.matzip.ViewRef;
import com.min.matzip.rest.model.RestPARAM;
import com.min.matzip.rest.model.RestVO;

@Controller
@RequestMapping("/rest")
public class RestController {

	@Autowired
	private RestService service;
	
	@RequestMapping(value="/map")
	public String restMap(Model model) {
		model.addAttribute(Const.TITLE, "지도보기");
		model.addAttribute(Const.VIEW, "rest/restMap");
		return ViewRef.TEMP_MENU_TEMP;
	}
	
	@RequestMapping("/ajaxGetList")
	@ResponseBody public String ajaxGetList(RestPARAM param) {
		System.out.println("sw_lat : " + param.getSw_lat());
		System.out.println("sw_lng : " + param.getSw_lng());
		System.out.println("ne_lat : " + param.getNe_lat());
		System.out.println("ne_lng : " + param.getNe_lng());
		
		return service.selRestList(param);
	}
	
	@RequestMapping(value="/restReg")
	public String restReg(Model model) {
		model.addAttribute(Const.TITLE, "가게등록");
		model.addAttribute(Const.VIEW, "rest/restReg");
		return ViewRef.TEMP_DEFAULT;
	}
	
	@RequestMapping(value="/restReg", method = RequestMethod.POST)
	public String restReg(RestVO param, RedirectAttributes ra) {
		int i_user = SecurityUtils.getLoginUserPk(request);
		param.setI_user(i_user);
		
		int result = service.insRest(param);
		
		
	}
}
