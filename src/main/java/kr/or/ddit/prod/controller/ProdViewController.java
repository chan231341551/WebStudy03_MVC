package kr.or.ddit.prod.controller;

import javax.servlet.http.HttpServletRequest;

import kr.or.ddit.mvc.annotation.resolvers.RequestParam;
import kr.or.ddit.mvc.annotation.stereotype.Controller;
import kr.or.ddit.mvc.annotation.stereotype.RequestMapping;
import kr.or.ddit.prod.service.ProdService;
import kr.or.ddit.prod.service.ProdServiceImpl;
import kr.or.ddit.vo.ProdVO;

/**
 * 상품 상세 조회 시, 해당 거래처의 모든 정보 함께 조회함.
 * 상품 상세 조회 시, 구매자 리스트(회원아이디, 회원명, 휴대폰, 이메일, 마일리지) 함께 조회.
 * 분류명도 함께 조회함.
 *
 */
@Controller
public class ProdViewController {
   private ProdService service = new ProdServiceImpl();
   
   @RequestMapping("/prod/prodView.do")
   public String prodView(
         @RequestParam("what") String prodId
         , HttpServletRequest req
   ){
//      String prodId = req.getParameter("what");
//      if(StringUtils.isBlank(prodId)) {
//         resp.sendError(400);
//         return;
//      }

      ProdVO prod = service.retriveProd(prodId);
      
      req.setAttribute("prod", prod);
      return "prod/prodView"; // logical view name
      
   }
}