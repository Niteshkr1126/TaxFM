package com.beamsoftsolution.taxfm.controller;

import com.beamsoftsolution.taxfm.model.Department;
import com.beamsoftsolution.taxfm.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Slf4j
@RequestMapping(EndPoint.DEPARTMENTS)
public class DepartmentController {
	
	@Autowired
	DepartmentService departmentService;
	
	@GetMapping
	public List<Department> getAllDepartments() {
		return departmentService.getAllDepartments();
	}
	
	@PostMapping
	public ResponseEntity<Department> addDepartment(@RequestBody Department department) {
		return ResponseEntity.ok(departmentService.addDepartment(department));
	}
}
