package kr.or.ddit.security;

import org.junit.Test;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PassowrdEncoderTest {
//	7J206rGwIOyVlO2YuO2ZlCDtlZzqsbDsnoQg7ZWoIOuztOyFiA==
	PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
	String password = "java";
	String mem_pass = "{bcrypt}$2a$10$oNmxnP3k.Sys9A04aO97o.xdZc.3ImKgxrVANODHbolbCJEdeEHH2";  
			 
	
	@Test	
	public void encodeTest() {
		String encoded = encoder.encode(password);
		
		log.info("mem_pass : {}",encoded);
	}
	
	public void matchTest() {
		log.info("match result : {}",encoder.matches(password, mem_pass));
		
	}
}
