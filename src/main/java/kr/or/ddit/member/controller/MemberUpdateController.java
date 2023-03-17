package kr.or.ddit.member.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.or.ddit.enumpkg.ServiceResult;
import kr.or.ddit.member.service.MemberService;
import kr.or.ddit.member.service.MemberServiceImpl;
import kr.or.ddit.mvc.annotation.RequestMethod;
import kr.or.ddit.mvc.annotation.resolvers.ModelAttribute;
import kr.or.ddit.mvc.annotation.resolvers.RequestPart;
import kr.or.ddit.mvc.annotation.stereotype.Controller;
import kr.or.ddit.mvc.annotation.stereotype.RequestMapping;
import kr.or.ddit.mvc.mutipart.MultipartFile;
import kr.or.ddit.mvc.mutipart.MultipartHttpServletRequest;
import kr.or.ddit.validate.UpdateGroup;
import kr.or.ddit.validate.ValidationUtils;
import kr.or.ddit.vo.MemberVO;

@Controller
public class MemberUpdateController {
	private MemberService service = new MemberServiceImpl();
	
	private static final Logger log = LoggerFactory.getLogger(MemberUpdateController.class);
	
	@RequestMapping("/member/memberUpdate.do")
	public String UpdateForm(
//		,@SessionAttribute("authMember") MemberVO authMember
		HttpServletRequest req
		,HttpSession session) throws ServletException {
		MemberVO authMember = (MemberVO)session.getAttribute("authMember");
		
		MemberVO member = service.retrieveMember(authMember.getMemId());
		
		String viewName = "member/memberForm";
		
		req.setAttribute("member", member);
		return viewName;
	
	}
	
	@RequestMapping(value="/member/memberUpdate.do",method=RequestMethod.POST)
	public String UpdateProcess(
		@ModelAttribute("member") MemberVO member
		,@RequestPart(value="memImage",required=false) MultipartFile memImage
		,HttpServletRequest req
		,HttpSession session
		) throws ServletException, IOException {
	
		log.info("update post 왔다");
						
		/*req.getParameterMap();
		try {
			BeanUtils.populate(member, parameterMap); // map 데이터일떄
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new ServletException(e);
		}*/
		
		member.setMemImage(memImage);
		
		
		String viewName = "";
		Map<String, List<String>> errors = new LinkedHashMap<>();
		req.setAttribute("errors", errors);
		Boolean valid = ValidationUtils.validate(member, errors, UpdateGroup.class);
		
		if(valid) {
			ServiceResult result = service.modifyMember(member);
			
			switch (result) {
			case INVALIDPASSWORD:
				req.setAttribute("message", "비밀번호 오류");
				viewName = "member/memberForm";
				break;
				
			case FAIL:
				req.setAttribute("message", "서버에 문제 있음. 다시 ");
				viewName = "member/memberForm";
				break;
				
			default:
				MemberVO modifiedMember = service.retrieveMember(member.getMemId());
				session.setAttribute("authMember", modifiedMember);
				
				viewName = "redirect:/mypage.do";
				break;
			}
		}
		else {
			viewName = "member/memberForm";
			
		}
		return viewName;
	
	}
	
	
}
