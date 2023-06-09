package kr.or.ddit.mvc.annotation;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.or.ddit.mvc.annotation.stereotype.Controller;
import kr.or.ddit.mvc.annotation.stereotype.RequestMapping;
import kr.or.ddit.mvc.utils.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * RequestMappingHandlerMapping 역할
 * 1. 핸들러맵을 만드는 역할
 * 2. findCommandHandler메소드를 만들면서 수집된 정보들을 검색해주는 역할
 *
 */
@Slf4j
public class RequestMappingHandlerMapping implements HandlerMapping {
 	private Map<RequestMappingCondition, RequestMappingInfo> handlerMap;
 	

	public RequestMappingHandlerMapping(String...basePackages) {
		handlerMap = new LinkedHashMap<>();
		scanBasePackages(basePackages);
	}

	private void scanBasePackages(String[] basePackages) {
		ReflectionUtils.getClassesWithAnnotationAtBasePackages(Controller.class, basePackages)
			.forEach((handlerClass,controller)->{
				try {
					Object commandHandler = handlerClass.newInstance();
					ReflectionUtils.getMethodsWithAnnotationAtClass(
						handlerClass, RequestMapping.class, String.class
					).forEach((handlerMethod,reqsetMapping)->{
						// key
						RequestMappingCondition mappingCondition = 
								new RequestMappingCondition(reqsetMapping.value(), reqsetMapping.method());
						// value
						RequestMappingInfo mappingInfo = 
								new RequestMappingInfo(mappingCondition, commandHandler, handlerMethod);
						handlerMap.put(mappingCondition,mappingInfo);
						log.info("수집된 핸들러 정보 : {}",mappingInfo);
					}); // 메소드 호출
					
				}catch (Exception e) {
					log.error("핸들러 클래스 스캔 중 문제 발생", e);
				}
			});
	}

	@Override
	public RequestMappingInfo findCommandHandler(HttpServletRequest request) {
		String url = request.getServletPath();
		RequestMethod method = RequestMethod.valueOf(request.getMethod().toUpperCase());
		
		RequestMappingCondition mappingCondition = 
				new RequestMappingCondition(url, method);
		return handlerMap.get(mappingCondition);
	}

}
