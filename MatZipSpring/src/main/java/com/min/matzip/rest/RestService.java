package com.min.matzip.rest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.min.matzip.CommonUtils;
import com.min.matzip.Const;
import com.min.matzip.FileUtils;
import com.min.matzip.SecurityUtils;
import com.min.matzip.model.CodeVO;
import com.min.matzip.model.CommonMapper;
import com.min.matzip.rest.model.RestDMI;
import com.min.matzip.rest.model.RestFile;
import com.min.matzip.rest.model.RestPARAM;
import com.min.matzip.rest.model.RestRecMenuVO;

@Service
public class RestService {
	
	@Autowired
	private RestMapper mapper;
	
	@Autowired
	private CommonMapper cMapper;
	
	public List<RestDMI> selRestList(RestPARAM param) {
		return mapper.selRestList(param);
	}
	
	public List<CodeVO> selCategoryList() {
		CodeVO p = new CodeVO();
		p.setI_m(1); //음식점 카테고리 코드 = 1
		
		return cMapper.selCodeList(p);
	}

	public int insRest(RestPARAM param) {
		return mapper.insRest(param);
	}
	
	public void addHits(RestPARAM param, HttpServletRequest req) {
		String myIp = req.getRemoteAddr();
		ServletContext ctx = req.getServletContext();

		int i_user = SecurityUtils.getLoginUserPk(req);

		String currentRestReadIp = (String)ctx.getAttribute(Const.CURRENT_REST_READ_IP + param.getI_rest());
		if(currentRestReadIp == null || !currentRestReadIp.equals(myIp)) {

			param.setI_user(i_user); //내가 쓴 글이면 조회수 안 올라가게 쿼리문으로 막는다
			//조회수 올림 처리 할꺼임
			mapper.updAddHits(param);
			ctx.setAttribute(Const.CURRENT_REST_READ_IP + param.getI_rest(), myIp);
		}
	}
	
	public RestDMI selRest(RestPARAM param) {
		
		return mapper.selRest(param);
	}
	
	@Transactional
	public void delRestTran(RestPARAM param) {
		mapper.delRestRecMenu(param);
		mapper.delRestMenu(param);
		mapper.delRest(param);
	}	

	public int delRestRecMenu(RestPARAM param, String realPath) {
		//파일 삭제
		List<RestRecMenuVO> list = mapper.selRestRecMenus(param);
		if(list.size() == 1) {
			RestRecMenuVO item = list.get(0);

			if(item.getMenu_pic() != null && !"".equals(item.getMenu_pic())) { //이미지 있음 > 삭제!!
				File file = new File(realPath + item.getMenu_pic());
				if(file.exists()) {
					if(file.delete()) {
						return mapper.delRestRecMenu(param);
					} else {
						return 0;
					}
				}
			}
		}

		return mapper.delRestRecMenu(param);
	}
	
	public int delRestMenu(RestPARAM param) {
		if(param.getMenu_pic() != null && !"".equals(param.getMenu_pic())) {
			String path = Const.realPath + "/resources/img/rest/" + param.getI_rest() + "/menu/";

			if(FileUtils.delFile(path + param.getMenu_pic())) {
				return mapper.delRestMenu(param);		
			} else {
				return Const.FAIL;
			}
		}
		return mapper.delRestMenu(param);
	}
	
	
	public int insRecMenus(MultipartHttpServletRequest mReq) {
		
		int i_rest = Integer.parseInt(mReq.getParameter("i_rest")); 
		int i_user = SecurityUtils.getLoginUserPk(mReq.getSession());
		if(_authFail(i_rest, i_user)) {
			return Const.FAIL;
		}  //내가 쓴글이 아닌글은 못쓰게 막는것
		
		List<MultipartFile> fileList = mReq.getFiles("menu_pic");
		String[] menuNmArr = mReq.getParameterValues("menu_nm");
		String[] menuPriceArr = mReq.getParameterValues("menu_price");
		
		String path = mReq.getServletContext().getRealPath("/resources/img/rest/" + i_rest + "/rec_menu/");
		
		List<RestRecMenuVO> list = new ArrayList();
		
		for(int i=0; i<menuNmArr.length; i++) {
			RestRecMenuVO vo = new RestRecMenuVO();
			list.add(vo);
			
			String menu_nm = menuNmArr[i];
			int menu_price = CommonUtils.parseStringToInt(menuPriceArr[i]);
			vo.setI_rest(i_rest);
			vo.setMenu_nm(menu_nm);
			vo.setMenu_price(menu_price);
			
			//파일 각 저장
			MultipartFile mf = fileList.get(i);
			String saveFileNm = FileUtils.saveFile(path, mf);
			vo.setMenu_pic(saveFileNm);  //null이박히면 file이 없는거
		
		}
		
		for(RestRecMenuVO vo : list) {
			mapper.insRestRecMenu(vo);
		}
		
		return i_rest;
	}
	
	public  List<RestRecMenuVO> selRestMenus(RestPARAM param) {
		return mapper.selRestMenus(param);
	}
	
	public List<RestRecMenuVO> selRestRecMenus(RestPARAM param) {
	
		return mapper.selRestRecMenus(param);
	}
	
	
	public int insMenus(RestFile param, int i_user) {		
		if(_authFail(param.getI_rest(), i_user)) {
			return Const.FAIL;
		}
		System.out.println(Const.realPath);

		String path = Const.realPath + "/resources/img/rest/" + param.getI_rest() + "/menu/";

		List<RestRecMenuVO> list = new ArrayList();

		for(MultipartFile mf : param.getMenu_pic()) {
			RestRecMenuVO vo = new RestRecMenuVO();
			list.add(vo);			

			String saveFileNm = FileUtils.saveFile(path, mf);
			vo.setMenu_pic(saveFileNm);
			vo.setI_rest(param.getI_rest());
		}

		for(RestRecMenuVO vo : list) {
			mapper.insRestMenu(vo);
		}

		return Const.SUCCESS;
	}
	
	private boolean _authFail(int i_rest, int i_user) {	//모든글 모든 가게에는 누가쓴 글인지 db에 저장되어 있다, i_user는 로그인한 i_user 
		RestPARAM param = new RestPARAM();
		param.setI_rest(i_rest);
		int dbI_user = mapper.selRestChkUser(i_rest); //0줄 아니면 1줄이 넘어온다 
		if(i_user != dbI_user) {
			return true; // 실패한거
		}
		return false; // _authFaildl false는 인증 완료됨
	}
	
}
