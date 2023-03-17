package kr.or.ddit.filter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BlindFilter implements Filter{
	
	private Map<String, String> blindMap;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.info("{} 초기화",this.getClass().getName());
		blindMap = new LinkedHashMap<>();
		blindMap.put("127.0.0.1","나니깐 블라인드");
		blindMap.put("0:0:0:0:0:0:0:1","나니깐 블라인드");
		blindMap.put("192.168.35.28","나니깐 블라인드");
		blindMap.put("192.168.35.55","알리미니깐 블라인드");
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		log.info("blind filter 동작시작");
//		1.클라이언트의 ip 주소 필요
		String idAddress = request.getRemoteAddr();
//		2.ip 주소 를 기준으로 블라인드 대상 여부 판단
		boolean containsKey = blindMap.containsKey(idAddress);
		String viewName = null;
		if(containsKey) {
//		블라인드 대상 이면 블라인드 타입 메시지 알려주기
			String reason = blindMap.get(idAddress);
			String message = String.format("당신은 %s 사유로 블라인드 처리 되었음", reason);
			request.setAttribute("message", message);
			viewName = "/WEB-INF/views/commons/messageView.jsp";
			request.getRequestDispatcher(viewName).forward(request, response);
		}
		else {
//		블라인드 대상 아니면 정상적으로 서비스 이용
			chain.doFilter(request, response);
			
		}
		
		log.info("blind filter 동작종료");
	}

	@Override
	public void destroy() {
		log.info("{} 소멸",this.getClass().getName());
		
	}

}
