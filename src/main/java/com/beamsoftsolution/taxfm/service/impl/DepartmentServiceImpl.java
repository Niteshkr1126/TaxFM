package com.beamsoftsolution.taxfm.service.impl;

import com.beamsoftsolution.taxfm.exception.TaxFMException;
import com.beamsoftsolution.taxfm.model.Department;
import com.beamsoftsolution.taxfm.repository.DepartmentRepository;
import com.beamsoftsolution.taxfm.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {
	
	@Autowired
	DepartmentRepository departmentRepository;
	
	@Override
	public List<Department> getAllDepartments() {
		return departmentRepository.findAll();
	}
	
	@Override
	public Department getDepartmentById(Long departmentId) throws TaxFMException {
		Optional<Department> optionalDepartment = departmentRepository.findByDepartmentId(departmentId);
		if(optionalDepartment.isEmpty()) {
			throw new TaxFMException("department.not.found").withErrorCode(404);
		}
		return optionalDepartment.get();
	}
	
	@Override
	public Department addDepartment(Department department) {
		return departmentRepository.save(department);
	}
}