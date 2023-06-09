package kr.or.ddit.mvc.annotation.resolvers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *	요청 파라미터(request parameter) 중 특정 파라미터(value) 하나의 값을 획득하기 위한 설정.
 *	ex) @RequestParam("who") : request.getParameter("who")
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface RequestParam {
	String value();
	boolean required() default true; // required 생략하면 필수 파라미터로 true
	String defaultValue() default "";
}
