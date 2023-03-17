package kr.or.ddit.memo.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import kr.or.ddit.memo.dao.MemoDAO;
import kr.or.ddit.memo.dao.MemoDAOImpl;
import kr.or.ddit.mvc.annotation.RequestMethod;
import kr.or.ddit.mvc.annotation.resolvers.ModelAttribute;
import kr.or.ddit.mvc.annotation.stereotype.Controller;
import kr.or.ddit.mvc.annotation.stereotype.RequestMapping;
import kr.or.ddit.vo.MemoVO;

@Controller
public class MemoController{
	
	private static final Logger log = LoggerFactory.getLogger(MemoController.class);
	
//	private MemoDAO dao = DataBaseMemoDAOImpl.getInstance();
//	private MemoDAO dao = new DataBaseMemoDAOImpl();
	private MemoDAO dao = new MemoDAOImpl();
	
	@RequestMapping("/memo")
	public String memoListGet(
//		@RequestHeader("Accept")String accept
		@ModelAttribute("memoList")List<MemoVO> memoList
		,HttpServletRequest req
		,HttpServletResponse resp) throws ServletException, IOException {
		
//	1. 요청 분석
		String accept = req.getHeader("Accept");
		log.info("accept header : {}",accept);
		if(accept.contains("xml")) {
			resp.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
			return null;
		}
//	2. 모델 확보
		dao.selectMemoList();
		
//	4. 뷰 선택 
		String path = "";
		if(accept.contains("json")) {
			path = "forward:/jsonView.do";
		}
		
		return path;
	
	}
	
	@RequestMapping(value="/memo",method=RequestMethod.POST)
	public String memoListPost(HttpServletRequest req) throws ServletException, IOException {
//		Post-Redirect-Get : PRG pattern
	
//		2. 모델 확보
		MemoVO memo = getMemoFromRequest(req);
		dao.insertMemo(memo);
		
//		3. 뷰 선택 , 뷰 이동 
		return "redirect:/memo";
	
	}
	
	private MemoVO getMemoFromRequest(HttpServletRequest req) throws IOException {
//		1. 요청 분석
		MemoVO memo = null;
		String contentType = req.getContentType();
		if(contentType.contains("json")) {
			try(
				BufferedReader br = req.getReader();	// 역직렬화 , body content read용 입력 스트림	
			){
				memo = new ObjectMapper().readValue(br, MemoVO.class);
				System.out.println(memo);
				return memo;
			}	
		}
		else if(contentType.contains("xml")) {
			try(
				BufferedReader br = req.getReader();	// 역직렬화 , body content read용 입력 스트림	
			){
				memo = new XmlMapper().readValue(br, MemoVO.class);
				return memo;
			}	
		}
		else {
			String writer = req.getParameter("writer");
			String date = req.getParameter("date");
			String content = req.getParameter("content");
			memo = new MemoVO();
			memo.setContent(content);
			memo.setWriter(writer);
			memo.setDate(date);
		}
		return memo;
		
		
	}

	@RequestMapping(value="/memo",method=RequestMethod.PUT)
	public String doPut(HttpServletRequest req) throws ServletException{
		return null;
	
	
	}
	
	@RequestMapping(value="/memo",method=RequestMethod.DELETE)
	public String doDelete(HttpServletRequest req) throws ServletException{
		return null;
	
	
	}
}
