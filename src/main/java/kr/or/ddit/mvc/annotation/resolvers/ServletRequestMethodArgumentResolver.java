package kr.or.ddit.mvc.annotation.resolvers;

import java.io.IOException;
import java.lang.reflect.Parameter;
import java.security.Principal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * {@link HttpServletRequest} , {@link HttpSession} , {@link Principal} 타입의 헨들러 메소드 인자 해결.
 * 
 *
 */
public class ServletRequestMethodArgumentResolver implements HandlerMethodArgumentResolver {
	
	// 만들어줄 객체 타입을 결정
	@Override
	public boolean supportsParameter(Parameter parameter) {
		Class<?> parameterType = parameter.getType();
		boolean support = HttpServletRequest.class.equals(parameterType) // 이제부터 넣어줄 파라미터는 parameterType
							||
						  HttpSession.class.equals(parameterType)
						  	||
						  Principal.class.isAssignableFrom(parameterType); // parameterType 가 Principal 의 하위타입인지 판단
		return support;
	}
	
	//필요한 객체만들기
	@Override
	public Object resolveArgument(Parameter parameter, HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		Class<?> parameterType = parameter.getType();
		Object argumentObject = null;
		if(HttpServletRequest.class.equals(parameterType)) { 
			argumentObject = req;
		}
		else if(HttpSession.class.equals(parameterType)) {
			argumentObject = req.getSession();
		}
		else {
			argumentObject = req.getUserPrincipal();
		}
		return argumentObject;
	}

}
