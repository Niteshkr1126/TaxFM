package com.beamsoftsolution.taxfm.controller;

import com.beamsoftsolution.taxfm.exception.TaxFMException;
import com.beamsoftsolution.taxfm.model.Attendance;
import com.beamsoftsolution.taxfm.model.Employee;
import com.beamsoftsolution.taxfm.service.AttendanceService;
import com.beamsoftsolution.taxfm.service.EmployeeService;
import com.beamsoftsolution.taxfm.utils.TaxFMUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(EndPoint.PROFILE_ATTENDANCE)
public class AttendanceController {
	
	@Autowired
	AttendanceService attendanceService;
	
	@Autowired
	EmployeeService employeeService;
	
	@Autowired
	TaxFMUtils taxFMUtils;
	
	@GetMapping
	@PreAuthorize("hasAuthority('VIEW_ATTENDANCE')")
	public String viewAttendance(Model model, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) throws TaxFMException {
		taxFMUtils.setCompanyAttributes(model);
		Employee loggedInEmployee = employeeService.getLoggedInEmployee();
		LocalDate today = LocalDate.now();
		
		// If dates are not provided, default to the current date range
		if(startDate == null) {
			startDate = today;
		}
		if(endDate == null) {
			endDate = today;
		}
		
		// Validate date range
		if(startDate.isAfter(endDate)) {
			model.addAttribute("error", "End date cannot be before start date");
		}
		
		List<Attendance> attendances = attendanceService.getAttendanceByEmployeeAndDateRange(loggedInEmployee, startDate, endDate);
		
		model.addAttribute("employee", loggedInEmployee);
		model.addAttribute("attendances", attendances);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		
		return "attendance/attendance";
	}
}