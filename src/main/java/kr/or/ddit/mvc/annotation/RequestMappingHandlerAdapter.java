package kr.or.ddit.mvc.annotation;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.or.ddit.mvc.annotation.resolvers.HandlerMethodArgumentResolver;
import kr.or.ddit.mvc.annotation.resolvers.ModelAtrributeMethodProcessor;
import kr.or.ddit.mvc.annotation.resolvers.RequestParamMethodArgumentResolver;
import kr.or.ddit.mvc.annotation.resolvers.RequestParamMethodArgumentResolver.BadRequestException;
import kr.or.ddit.mvc.annotation.resolvers.RequestPartMethodArgumentResolver;
import kr.or.ddit.mvc.annotation.resolvers.ServletRequestMethodArgumentResolver;
import kr.or.ddit.mvc.annotation.resolvers.ServletResponseMethodArgumentResolver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestMappingHandlerAdapter implements HandlerAdapter {
	
	private List<HandlerMethodArgumentResolver> argumentResolvers;
	{
		argumentResolvers = new ArrayList<>();
		argumentResolvers.add(new ServletRequestMethodArgumentResolver());
		argumentResolvers.add(new ServletResponseMethodArgumentResolver());
		argumentResolvers.add(new RequestParamMethodArgumentResolver());
		argumentResolvers.add(new ModelAtrributeMethodProcessor());
		argumentResolvers.add(new RequestPartMethodArgumentResolver());
	}
	
	private HandlerMethodArgumentResolver findArgumentResolver(Parameter param) { // 여러개의 argumentResolvers 중 골라내는 작업 메소드
		
		HandlerMethodArgumentResolver finded = null;
		for(HandlerMethodArgumentResolver resolver : argumentResolvers) {
			if(resolver.supportsParameter(param)) {
				finded = resolver;
				log.info("finded : {}",finded);
				break;
			}
		}
		return finded;
	}
	
	@Override
	public String invokeHandler(RequestMappingInfo mappingInfo, HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Object handlerObject = mappingInfo.getCommandHandler();
		Method handlerMethod = mappingInfo.getHandlerMethod();
		int parameterCount = handlerMethod.getParameterCount();
		try {
			
			String viewName = null;
			if(parameterCount > 0) {
				Parameter[] parameters = handlerMethod.getParameters();
				
				Object[] arguments = new Object[parameterCount];
				for(int i=0; i<parameterCount; i++) {
					Parameter param = parameters[i];
					HandlerMethodArgumentResolver findedResolver = findArgumentResolver(param);
					if(findedResolver == null) {
						throw new RuntimeException(String.format("%s 타입의 메소드 인자는 현재 처리가능한  resolver 가 없음.",param.getType()));
					}
					else {
						arguments[i] = findedResolver.resolveArgument(param, req, resp);
						log.info("arguments : {}",arguments[0]);
					}
				}
				
				viewName = (String) handlerMethod.invoke(handlerObject, arguments);
				log.info("viewName : {}",viewName);
			}
			else {
				viewName = (String) handlerMethod.invoke(handlerObject);				
			}
			return viewName;
		}catch(BadRequestException e) {
			log.error("handler adapter 가 handler method argument resolver 사용 중 문제 발생",e);
			resp.sendError(400,e.getMessage());
			return null;
		}catch (Exception e) {
			throw new ServletException(e);
		
		}
	}

	

}
