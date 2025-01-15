package com.beamsoftsolution.taxfm.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ExceptionAdvice {
	
	private final Locale currentLocale = LocaleContextHolder.getLocale();
	@Autowired
	private MessageSource messageSource;
	
	@ExceptionHandler({ TaxFMException.class })
	public ResponseEntity<?> handleCustomException(HttpServletRequest request, TaxFMException TaxFMException) {
		if(StringUtils.containsIgnoreCase(ExceptionUtils.getRootCauseMessage(TaxFMException), "Broken pipe")) {
			return null;
		}
		else {
			HttpStatus statusCode = TaxFMException.getMessageKey() == null ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.BAD_REQUEST;
			if(TaxFMException.getErrorCode() > 0) {
				statusCode = HttpStatus.valueOf(TaxFMException.getErrorCode());
			}
			String message = messageSource.getMessage(TaxFMException.getMessageKey() == null ? "application.unknown.error" : TaxFMException.getMessageKey(),
			                                          TaxFMException.getArgs(), currentLocale);
			if(TaxFMException.getMessageKey() != null) {
				log.error(message);
			}
			else {
				log.error("-------------------< Exception Block : START >-------------------");
				log.error("Wrapped TaxFMException Exception: ", TaxFMException);
				log.error("-------------------< Exception Block :  End  >-------------------");
			}
			Map<String, String> responseBody = new HashMap<>();
			responseBody.put("path", request.getContextPath());
			responseBody.put("message", message);
			responseBody.put("url", request.getRequestURL().toString());
			return new ResponseEntity<Object>(responseBody, statusCode);
		}
	}
	
	@ExceptionHandler({ MethodArgumentNotValidException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ResponseEntity<?> processValidationError(MethodArgumentNotValidException e, HttpServletRequest request) {
		BindingResult result = e.getBindingResult();
		FieldError error = result.getFieldError();
		Map<String, String> responseBody = new HashMap<>();
		responseBody.put("path", request.getContextPath());
		responseBody.put("message", processFieldError(error));
		responseBody.put("url", request.getRequestURL().toString());
		return new ResponseEntity<Object>(responseBody, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler({ SQLIntegrityConstraintViolationException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ResponseEntity<?> processSQLValidationError(SQLIntegrityConstraintViolationException e, HttpServletRequest request) {
		Map<String, String> responseBody = new HashMap<>();
		responseBody.put("path", request.getContextPath());
		responseBody.put("message", messageSource.getMessage("sql.integrity.constraint.violation.exception",
		                                                     new Object[] { request.getRequestURL() }, currentLocale));
		responseBody.put("url", request.getRequestURL().toString());
		return new ResponseEntity<Object>(responseBody, HttpStatus.BAD_REQUEST);
	}
	
	private String processFieldError(FieldError error) {
		String message = null;
		if(error != null) {
			message = messageSource.getMessage(error.getDefaultMessage(), error.getArguments(), currentLocale);
		}
		return message;
	}
}
