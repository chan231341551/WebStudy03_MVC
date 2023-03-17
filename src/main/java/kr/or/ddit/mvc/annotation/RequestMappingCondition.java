package kr.or.ddit.mvc.annotation;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * immutable 객체 형태로 값을 변경하지않음. @Setter 가 없음.
 */
@Getter
@EqualsAndHashCode // 맵의 키로 반환되기떄문에 식별성인 equals가 필요하다.
public class RequestMappingCondition {
	private String url;
	private RequestMethod method;
	
	public RequestMappingCondition(String url, RequestMethod method) {
		super();
		this.url = url;
		this.method = method;
	}
	
	@Override
	public String toString() {
		return String.format("%s[%s]", url,method);
	}
	
	
}
