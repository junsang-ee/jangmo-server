package com.jangmo.web.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncryptUtil {
	private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

	public static String encode(String rawData) {
		return bCryptPasswordEncoder.encode(rawData);
	}

	public static boolean matches(String rawData, String encodedData) {
		return bCryptPasswordEncoder.matches(rawData, encodedData);
	}
}
