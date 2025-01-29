package com.beamsoftsolution.taxfm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class TaxFMApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(TaxFMApplication.class, args);
	}
	
//	public static void main(String[] args) {
//		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//		String encodedPassword = encoder.encode("admin");
//		System.out.println("Encoded Password: " + encodedPassword);
//	}
}