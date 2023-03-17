package kr.or.ddit.prod.controller;

import javax.servlet.http.HttpServletRequest;

import kr.or.ddit.mvc.annotation.RequestMethod;
import kr.or.ddit.mvc.annotation.resolvers.RequestParam;
import kr.or.ddit.mvc.annotation.stereotype.Controller;
import kr.or.ddit.mvc.annotation.stereotype.RequestMapping;
import kr.or.ddit.vo.ProdVO;

@Controller
public class ProdDeleteController {

	@RequestMapping(value="/prod/prodDelete.do",method=RequestMethod.POST)
	public String prodDelete(
		@RequestParam("what")ProdVO prodId	
		,HttpServletRequest req) {
		
		
		
		return null;
		
		
	}
}
