package com.beamsoftsolution.taxfm.service.impl;

import com.beamsoftsolution.taxfm.model.Attendance;
import com.beamsoftsolution.taxfm.model.Employee;
import com.beamsoftsolution.taxfm.repository.AttendanceRepository;
import com.beamsoftsolution.taxfm.repository.EmployeeRepository;
import com.beamsoftsolution.taxfm.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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
	
	@Override
	public List<Attendance> getAttendanceByUsername(String username) {
		return attendanceRepository.findByUsername(username);
	}
	
	@Override
	public List<Attendance> getAttendanceByEmployeeAndDateRange(Employee employee, LocalDate startDate, LocalDate endDate) {
		if(startDate != null && endDate != null) {
			LocalDateTime start = startDate.atStartOfDay();
			LocalDateTime end = endDate.atTime(LocalTime.MAX);
			return attendanceRepository.findByEmployeeAndLoginTimeBetweenOrderByLoginTimeDesc(employee, start, end);
		}
		return attendanceRepository.findByEmployeeOrderByLoginTimeDesc(employee);
	}
	
	@ModelAttribute("formatDuration")
	public String formatDuration(LocalDateTime start, LocalDateTime end) {
		if(start == null || end == null) return "-";
		Duration duration = Duration.between(start, end);
		return String.format("%dh %02dm", duration.toHours(), duration.toMinutesPart());
	}
}