package com.beamsoftsolution.taxfm.service;

import com.beamsoftsolution.taxfm.exception.TaxFMException;
import com.beamsoftsolution.taxfm.model.Department;

import java.util.List;

public interface DepartmentService {
	
	List<Department> getAllDepartments();
	Department getDepartmentById(Long departmentId) throws TaxFMException;
	Department addDepartment(Department department);
}