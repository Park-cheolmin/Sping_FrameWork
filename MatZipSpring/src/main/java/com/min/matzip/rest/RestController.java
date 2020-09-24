package com.min.matzip.rest;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.min.matzip.Const;
import com.min.matzip.SecurityUtils;
import com.min.matzip.ViewRef;
import com.min.matzip.rest.model.RestDMI;
import com.min.matzip.rest.model.RestFile;
import com.min.matzip.rest.model.RestPARAM;
import com.min.matzip.rest.model.RestRecMenuVO;

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
	
	@RequestMapping(value="/ajaxGetList", produces = {"application/json; charset=UTF-8"}) //UTF-8 설정 하는 방법
	@ResponseBody
	public List<RestDMI> ajaxGetList(RestPARAM param) { //spring에 json라이브러리를 자동으로 받아져있음, 리턴으로 String으로 하지않고 List<RestDMI>할수있는 이유
		System.out.println("sw_lat : " + param.getSw_lat());
		System.out.println("sw_lng : " + param.getSw_lng());
		System.out.println("ne_lat : " + param.getNe_lat());
		System.out.println("ne_lng : " + param.getNe_lng());
		
		return service.selRestList(param);
	}
	
	@RequestMapping(value="/reg")
	public String restReg(Model model) {
		model.addAttribute("categoryList",service.selCategoryList());
		
		model.addAttribute(Const.TITLE, "가게등록");
		model.addAttribute(Const.VIEW, "rest/restReg");
		return ViewRef.TEMP_MENU_TEMP;
	}
	
	@RequestMapping(value="/reg", method = RequestMethod.POST)
	public String restReg(RestPARAM param, HttpSession hs) {
		param.setI_user(SecurityUtils.getLoginUserPk(hs));
		
		int result = service.insRest(param);
		
		return "redirect:/";
	}
	
	@RequestMapping(value="/detail")
	public String detail(RestPARAM param, Model model) {
		RestDMI data = service.selRest(param);
		
		
		model.addAttribute("menuList", service.selRestMenus(param));
		model.addAttribute("recMenuList", service.selRestRecMenus(param));
		model.addAttribute("data", data);
		
		model.addAttribute("css", new String[] {"restDetail"});
		
		model.addAttribute(Const.TITLE, data.getNm()); //가게명
		model.addAttribute(Const.VIEW, "rest/restDetail"); //파일명 쓰는곳
		
		return ViewRef.TEMP_MENU_TEMP;
	}
	
	@RequestMapping("/del")
	public String del(RestPARAM param, HttpSession hs) {
		int loginI_user = SecurityUtils.getLoginUserPk(hs);
		param.setI_user(loginI_user);
		int result = 1;
		try {
			service.delRestTran(param);
		} catch(Exception e) {
			result = 0;
		}		
		System.out.println("result : " + result);
		return "redirect:/";
	}
	
	@RequestMapping(value="/recMenus", method=RequestMethod.POST)
	public String recMenus(MultipartHttpServletRequest mReq, RedirectAttributes ra) { 
		
		int i_rest = service.insRecMenus(mReq);
		
		
		ra.addAttribute("i_rest", i_rest); //addAttribute : 쿼리스트링 만듬, addFlashAttribute: 응답하면 session지움 (RedirectAttributes의 2가지 방법)
		return "redirect:/rest/detail";
	}
	
	@RequestMapping("/ajaxDelRecMenu")
	@ResponseBody
	public int ajaxDelRecMenu(RestPARAM param, HttpSession hs) {
		String path = "/resources/img/rest/" + param.getI_rest() + "/rec_menu/";
		String realPath = hs.getServletContext().getRealPath(path);
		param.setI_user(SecurityUtils.getLoginUserPk(hs)); //loginuser pk담기
		return service.delRecMenu(param, realPath);
	}
	
	@RequestMapping("/menus")
	public String menus(@ModelAttribute RestFile param, HttpSession hs, RedirectAttributes ra) {//@ModelAttribute(객체로 받을떄 이걸 사용해야하지만 자동으로 해준다)
		int i_user = SecurityUtils.getLoginUserPk(hs);
		int result = service.insMenus(param, i_user);
		ra.addAttribute("i_rest", param.getI_rest());
		return "redirect:/rest/detail";
	}
}
