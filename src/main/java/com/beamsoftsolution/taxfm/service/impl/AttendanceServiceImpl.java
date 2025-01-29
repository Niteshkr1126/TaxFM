package com.beamsoftsolution.taxfm.service.impl;

import com.beamsoftsolution.taxfm.model.Attendance;
import com.beamsoftsolution.taxfm.model.Employee;
import com.beamsoftsolution.taxfm.repository.AttendanceRepository;
import com.beamsoftsolution.taxfm.repository.EmployeeRepository;
import com.beamsoftsolution.taxfm.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AttendanceServiceImpl implements AttendanceService {
	
	@Autowired
	AttendanceRepository attendanceRepository;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Override
	public void saveLoginTime(String username) {
		Employee employee = employeeRepository.findByUsername(username).orElse(null);
		if(employee != null) {
			Attendance attendance = new Attendance();
			attendance.setUsername(username);
			attendance.setLoginTime(LocalDateTime.now());
			attendance.setEmployee(employee);
			
			attendanceRepository.save(attendance);
		}
	}
	
	@Override
	public void saveLogoutTime(String username) {
		Employee employee = employeeRepository.findByUsername(username).orElse(null);
		if(employee != null) {
			Attendance attendance = attendanceRepository.findByUsernameAndLogoutTimeNull(username)
			                                            .orElseThrow(() -> new RuntimeException("Attendance record not found for the user"));
			attendance.setLogoutTime(LocalDateTime.now());
			attendanceRepository.save(attendance);
		}
	}
}