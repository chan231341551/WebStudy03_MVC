package kr.or.ddit.mvc.annotation.resolvers;

import java.io.IOException;
import java.lang.reflect.Parameter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 *	@RequestParam 을 가지고 있으며, 기본형 타입인 인자를 해결. 
 *
 */
@Slf4j
public class RequestParamMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(Parameter parameter) {
		Class<?> parameterType = parameter.getType(); //memberList경우 parameterType -> int
		RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
		log.info("requestParam : {}",requestParam);
		boolean support = requestParam != null
					&&
				(
					parameterType.isPrimitive() // 기본형인지아닌지 판단 메소드
					||
					String.class.equals(parameterType) // 문자열 인지 아닌지 판단
					||
					(	
							parameterType.isArray() // 배열인지 아닌지 판단 메소드
							&& 
							(
									parameterType.getComponentType().isPrimitive() 
									|| 
									parameterType.getComponentType().equals(String.class)
							)
					)
				);
		return support;
	}

	@Override
	public Object resolveArgument(Parameter parameter, HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Class<?> parameterType = parameter.getType();
		RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
		
		String requestParameterName = requestParam.value();
		boolean required = requestParam.required();
		String defaultValue = requestParam.defaultValue();
		
		String[] requestParameterValues = req.getParameterValues(requestParameterName);
		if(required && (requestParameterValues==null 
						|| requestParameterValues.length == 0
						|| StringUtils.isBlank(requestParameterValues[0]) // null체크만으로 안되기떄문
					)) {
			throw new BadRequestException( requestParameterName+" 이름의 필수 파라미터 누락");
		}
		if(requestParameterValues == null || requestParameterValues.length == 0
				|| StringUtils.isBlank(requestParameterValues[0])) {
			requestParameterValues = new String[] {defaultValue};
		}
		
		Object argumentObject = null;
		if(parameterType.isArray()) {
			Object[] argumentObjects = new Object[requestParameterValues.length];
			for(int i=0; i<argumentObjects.length; i++) {
				argumentObjects[i] = singleValueGenerate(parameterType.getComponentType(), requestParameterValues[i]);
			}
			argumentObject = argumentObjects;
		}
		else {
			argumentObject = singleValueGenerate(parameterType, requestParameterValues[0]);
			log.info("argumentObject : {}",argumentObject);
		}
		return argumentObject;
	}
	
	private Object singleValueGenerate(Class<?> singleValueType, String requestParameter) {
		Object singleValue = null;
		if(int.class.equals(singleValueType)) {
			singleValue = Integer.parseInt(requestParameter);
		}
		else if(boolean.class.equals(singleValueType)) {
			singleValue = Boolean.parseBoolean(requestParameter);
		}
		else {
			singleValue = requestParameter;
		}
		return singleValue;
	}
	
	public static class BadRequestException extends RuntimeException{

		public BadRequestException(String message) {
			super(message);
		}
	}
}
