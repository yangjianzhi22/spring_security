package com.yang.security;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class SecurityApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void testBCryptPasswordEncoder() {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		/**
		 * encode()：加密方法，传入一个明文，加密后返回一个密文
		 * 同一明文，每次调用encode()方法生成出来的密文都是不一样的，
		 * 因为内部进行加密的时候，会生成一个【随机的加密盐】，
		 * 底层是通过【加密盐】和原文进行一系列处理之后再进行加密
		 * 这样的话，虽然明文一样，但是每一次的密文都是不一样的
		 */
		String encode_pwd_1 = passwordEncoder.encode("rabbit");
		String encode_pwd_2 = passwordEncoder.encode("rabbit");
		System.out.println("encode_pwd_1:{}" + encode_pwd_1);
		System.out.println("encode_pwd_2:{}" + encode_pwd_2);
	}

}
