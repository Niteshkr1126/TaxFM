package com.beamsoftsolution.taxfm;

import com.beamsoftsolution.taxfm.exception.TaxFMException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class StaticContextInitializer {
	
	@Autowired
	private MessageSource messageSource;
	
	@PostConstruct
	public void init() {
		TaxFMException.setMessageSource(messageSource);
	}
}
