package kr.or.ddit.mvc;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.or.ddit.mvc.annotation.HandlerAdapter;
import kr.or.ddit.mvc.annotation.HandlerMapping;
import kr.or.ddit.mvc.annotation.RequestMappingHandlerAdapter;
import kr.or.ddit.mvc.annotation.RequestMappingHandlerMapping;
import kr.or.ddit.mvc.annotation.RequestMappingInfo;
import kr.or.ddit.mvc.view.InternalResourceViewResolver;
import kr.or.ddit.mvc.view.ViewResolver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DispatcherServlet extends HttpServlet {
	private ViewResolver viewResolver;
	private HandlerMapping handlerMapping;
	private HandlerAdapter handlerAdapter;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		viewResolver = new InternalResourceViewResolver("/WEB-INF/views/", ".jsp");
		handlerMapping = new RequestMappingHandlerMapping("kr.or.ddit");
		handlerAdapter = new RequestMappingHandlerAdapter();
	}
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		
		String requestURI = req.getServletPath();
		log.info("requestURI : {}",requestURI);
		
		RequestMappingInfo mappingInfo = handlerMapping.findCommandHandler(req);
		log.info("mappingInfo : {}",mappingInfo);
		
		
		if(mappingInfo == null) {
			resp.sendError(404,requestURI+"는 처리할 수 없는 자원임");
			return;
		}
		
		String viewName = handlerAdapter.invokeHandler(mappingInfo, req, resp);
		if(viewName == null) {
			if(!resp.isCommitted()) {
				resp.sendError(500,"논리적인 뷰 네임은 NULL일수 없음.");
			}
			
		}
		else {
			viewResolver.resolveView(viewName, req, resp);			
		}
		
		
	}
}
