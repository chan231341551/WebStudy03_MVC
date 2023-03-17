package kr.or.ddit.prod.controller;

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

import org.apache.commons.beanutils.BeanUtils;

import kr.or.ddit.enumpkg.ServiceResult;
import kr.or.ddit.mvc.annotation.RequestMethod;
import kr.or.ddit.mvc.annotation.resolvers.ModelAttribute;
import kr.or.ddit.mvc.annotation.resolvers.RequestParam;
import kr.or.ddit.mvc.annotation.resolvers.RequestPart;
import kr.or.ddit.mvc.annotation.stereotype.Controller;
import kr.or.ddit.mvc.annotation.stereotype.RequestMapping;
import kr.or.ddit.mvc.mutipart.MultipartFile;
import kr.or.ddit.prod.dao.OthersDAO;
import kr.or.ddit.prod.dao.OthersDAOImpl;
import kr.or.ddit.prod.service.ProdService;
import kr.or.ddit.prod.service.ProdServiceImpl;
import kr.or.ddit.validate.UpdateGroup;
import kr.or.ddit.validate.ValidationUtils;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.ProdVO;

@Controller
public class ProdUpdateController {

	private ProdService service = new ProdServiceImpl();
	private OthersDAO othersDAO = new OthersDAOImpl();
	
	private void addAttribute(HttpServletRequest req) {
		req.setAttribute("lprodList", othersDAO.selectLprodList());
		req.setAttribute("buyerList", othersDAO.selectBuyerList(null));
	}
	
	@RequestMapping("/prod/prodUpdate.do")
	public String UpdateForm(
		@RequestParam("what") String prodId
		,HttpServletRequest req	
		) {
		addAttribute(req);
		
		ProdVO prod =  service.retriveProd(prodId);
			
		req.setAttribute("prod", prod);
		String viewName = "prod/prodForm";
			
		return viewName;
	}
	
	@RequestMapping(value="/prod/prodUpdate.do",method=RequestMethod.POST)
	public String UpdateProcess(
//		@RequestParam("what") String prodId	
		@ModelAttribute("prod") ProdVO prod
		,@RequestPart(value="prodImage",required=false) MultipartFile prodImage
		,HttpServletRequest req
		) throws ServletException, IOException {
		
		prod.setProdImage(prodImage);
		
//		1. 저장
		String saveFolderURL = "/resources/prodImages";
		ServletContext application = req.getServletContext();
		String saveFolderPath = application.getRealPath(saveFolderURL);
		File saveFolder = new File(saveFolderPath);
		if(!saveFolder.exists()) { 
			saveFolder.mkdirs();
		}
		prod.saveTo(saveFolder);
		
		
		addAttribute(req);
		String viewName = "";
		Map<String, List<String>> errors = new LinkedHashMap<>();
		Boolean valid = ValidationUtils.validate(prod, errors, UpdateGroup.class);
		req.setAttribute("errors", errors);
		
		
		
		if(valid) {
			ServiceResult result = service.modifyProd(prod);
			if(ServiceResult.OK == result) {
				viewName = "redirect:/prod/prodView.do?what="+prod.getProdId();
			}
			else {
				req.setAttribute("message", "서버 오류");
				viewName = "prod/prodForm";
			}
		}
		else {
			viewName = "prod/prodForm";
		}

		return viewName;

	}
	
}
