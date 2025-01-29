package com.beamsoftsolution.taxfm.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.util.Locale;

@Getter
@Setter
@Component
public class TaxFMException extends Exception {
	
	@Serial
	private static final long serialVersionUID = 3236478736863223453L;
	
	private static MessageSource messageSource = null;
	
	private static Locale currentLocale = LocaleContextHolder.getLocale();
	
	private String messageKey = null;
	
	private Object[] args = null;
	
	private int errorCode = 0;
	
	public TaxFMException() {
		super();
	}
	
	public TaxFMException(Exception e) {
		super(e.getMessage(), e);
	}
	
	public TaxFMException(String messageKey, Object... args) {
		super(messageSource.getMessage(messageKey, args, currentLocale));
		this.messageKey = messageKey;
		this.args = args;
	}
	
	public TaxFMException(String messageKey, Throwable throwable) {
		super(messageSource.getMessage(messageKey, null, currentLocale), throwable);
		this.messageKey = messageKey;
	}
	
	public TaxFMException(String messageKey, Throwable throwable, Object... args) {
		super(messageSource.getMessage(messageKey, args, currentLocale), throwable);
		this.messageKey = messageKey;
	}
	
	public static void setMessageSource(MessageSource msgSource) {
		messageSource = msgSource;
	}
	
	public TaxFMException withErrorCode(int errorCode) {
		this.errorCode = errorCode;
		return this;
	}
}
