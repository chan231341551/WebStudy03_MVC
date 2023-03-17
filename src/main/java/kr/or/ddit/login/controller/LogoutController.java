package kr.or.ddit.login.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.or.ddit.mvc.annotation.RequestMethod;
import kr.or.ddit.mvc.annotation.stereotype.Controller;
import kr.or.ddit.mvc.annotation.stereotype.RequestMapping;

@Controller
public class LogoutController{
	
	@RequestMapping(value="/login/logout.do",method=RequestMethod.POST)
	public String logout(HttpSession session) throws ServletException {
	
		
//		session.removeAttribute("authMember");
		session.invalidate();
		
		String viewName = "redirect:/";
		return viewName;
		
		
	}
}
