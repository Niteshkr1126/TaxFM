package com.beamsoftsolution.taxfm.controller;

public interface EndPoint {
	
	String HOME = "/home";
	String EMPLOYEES = "/employees";
	String EMPLOYEE_ATTENDANCE = "/{employeeId}/attendance";
	String AUTHORITIES = "/authorities";
	String CUSTOMERS = "/customers";
	String PROFILE = "/profile";
	String PROFILE_ATTENDANCE = "/profile/attendance";
	String SERVICES = "/services";
	String PRICING = "/pricing";
}
