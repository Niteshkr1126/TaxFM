package com.beamsoftsolution.taxfm.service;

import com.beamsoftsolution.taxfm.exception.TaxFMException;

public interface AttendanceService {
	
	void saveLoginTime(String username) throws TaxFMException;
	void saveLogoutTime(String username);
}
