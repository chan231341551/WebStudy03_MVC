package kr.or.ddit.member.controller;


import javax.servlet.http.HttpServletRequest;

import kr.or.ddit.member.service.MemberService;
import kr.or.ddit.member.service.MemberServiceImpl;
import kr.or.ddit.mvc.annotation.stereotype.Controller;
import kr.or.ddit.mvc.annotation.stereotype.RequestMapping;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.MemberVOWrapper;

@Controller
public class MypageController{
	private MemberService service = new MemberServiceImpl();
	
	@RequestMapping("/mypage.do")
	public String myPage(
		HttpServletRequest req
		,MemberVOWrapper principal) {
	
//		principal = (MemberVOWrapper) req.getUserPrincipal();
		MemberVO authMember = principal.getRealMember();
		MemberVO member = service.retrieveMember(authMember.getMemId());
		
		req.setAttribute("member", member);
				
		String viewName = "member/memberView"; // logical view name
		
		return viewName;
	}
}
