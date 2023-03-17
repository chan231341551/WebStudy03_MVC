package kr.or.ddit.member.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.or.ddit.member.service.MemberService;
import kr.or.ddit.member.service.MemberServiceImpl;
import kr.or.ddit.memo.controller.MemoController;
import kr.or.ddit.mvc.annotation.resolvers.ModelAttribute;
import kr.or.ddit.mvc.annotation.resolvers.RequestParam;
import kr.or.ddit.mvc.annotation.stereotype.Controller;
import kr.or.ddit.mvc.annotation.stereotype.RequestMapping;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.PagingVO;
import kr.or.ddit.vo.SearchVO;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
public class MemberListController{
	
	private static final Logger log = LoggerFactory.getLogger(MemoController.class);
	private MemberService service = new MemberServiceImpl();
	
	@RequestMapping("/member/memberList.do")
	public String memberList(
		@RequestParam(value="page",required=false, defaultValue="1") int currentPage
		,@ModelAttribute SearchVO simpleCondition // SearchVO -> searchVO 바뀌고 들어감
		,HttpServletRequest req
	){
				
		PagingVO<MemberVO> pagingVO = new PagingVO<>(4,2);
		pagingVO.setCurrentPage(currentPage);
		pagingVO.setSimpleCondition(simpleCondition);
				
//		2.
		service.retrieveMemberList(pagingVO);
		
//		3.
		req.setAttribute("pagingVO", pagingVO);
		
		
		log.info("paging data : {}",pagingVO);
//		4.
		String viewName = "member/memberList";
		
//		5.
		return viewName;
	}
}
