package com.beamsoftsolution.taxfm.service;

import com.beamsoftsolution.taxfm.exception.TaxFMException;
import com.beamsoftsolution.taxfm.model.Attendance;
import com.beamsoftsolution.taxfm.model.Employee;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {
	
	void saveLoginTime(String username) throws TaxFMException;
	void saveLogoutTime(String username);
	List<Attendance> getAttendanceByUsername(String username);
	List<Attendance> getAttendanceByEmployeeAndDateRange(Employee employee, LocalDate startDate, LocalDate endDate);
}