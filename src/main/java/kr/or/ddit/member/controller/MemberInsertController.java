package kr.or.ddit.member.controller;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import kr.or.ddit.validate.InsertGroup;
import kr.or.ddit.validate.ValidationUtils;
import kr.or.ddit.vo.MemberVO;

/**
 * Backend controller(command hannler) --> Plain Old Java Object
 */
@Controller
public class MemberInsertController {
	
	private static final Logger log = LoggerFactory.getLogger(MemberInsertController.class);
	private MemberService service = new MemberServiceImpl(); // 의존관계 코드
	
	@RequestMapping("/member/memberInsert.do")
	public String memberFrom()  {
		
		return "member/memberForm";
	
	}
	
	@RequestMapping(value="/member/memberInsert.do",method=RequestMethod.POST)
	public String insert(
		HttpServletRequest req
		, @ModelAttribute("member") MemberVO member
		, @RequestPart(value="memImage",required=false) MultipartFile memImage
	) throws ServletException, IOException {
		
		log.info("post 온거 확인");
		member.setMemImage(memImage);
		
		Map<String, List<String>> errors = new LinkedHashMap<>();
		req.setAttribute("errors", errors);
		
		Boolean valid = ValidationUtils.validate(member, errors, InsertGroup.class);
		log.info("valid : {}",valid);
		
		String viewName = "";
		if(valid) {
			ServiceResult result = service.createMember(member);
			
			switch (result) {
			case PKDUPLICATED:
				req.setAttribute("message", "아이디 중복");
				viewName = "member/memberForm";
				break;
			case FAIL:
				req.setAttribute("message", "서버에 문제 있음. 다시 가입하셈");
				viewName = "member/memberForm";
				break;
			default:
				viewName = "redirect:/";
				break;
			}
		}
		else {
			viewName = "member/memberForm";
		}
		return viewName;
	}
}
