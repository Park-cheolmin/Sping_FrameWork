package com.min.matzip;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.min.matzip.user.model.UserPARAM;

public class LoginCheckInterceptor extends HandlerInterceptorAdapter{
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception { // preHandle은 controller 실행되기 직전에 실행된다 
																																											   //postHandle은 컨트롤러 진입후 view가 실행되기전에 수행
		String uri = request.getRequestURI();
		System.out.println("uri : " + uri);
		String[] uriArr = uri.split("/");
		
		System.out.println("uriArr.length : " + uriArr.length);
		if(uriArr[1].equals("res")) { //리소스 (js, css, img)는 통과
			return true;
		} else if(uriArr.length < 3)  {//주소가 3차가 아닌이상 무조건 실패
			return false;
		}
		
		System.out.println("인터셉터!");
		boolean isLogout = SecurityUtils.isLogout(request);
		
		switch(uriArr[1]) {
		case ViewRef.URI_USER : //user
				switch(uriArr[2]) {
				case "login" : case "join" :
					if(!isLogout) { //로그인 되어있는 상태
						response.sendRedirect("/rest/map");
						return false;
					}
				}
			
		case ViewRef.URI_REST : //rest
			switch(uriArr[2]) {
			case "reg" :
				if(isLogout) {
					response.sendRedirect("/user/login");
					return false;
				}
			}
		}
		
		return true;
	}
}
