package com.beamsoftsolution.taxfm.service;

import com.beamsoftsolution.taxfm.exception.TaxFMException;
import com.beamsoftsolution.taxfm.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {
	
	List<Employee> getAllEmployees();
	Employee getLoggedInEmployee();
	Employee getEmployeeById(Integer employeeId) throws TaxFMException;
	void addEmployee(Employee employee) throws TaxFMException;
	Employee updateEmployee(Employee employee) throws TaxFMException;
	void deleteEmployee(Employee employee);
	void deleteEmployeeById(Integer employeeId) throws TaxFMException;
	long getTotalEmployeesCount();
	Page<Employee> getPaginatedEmployees(Pageable pageable);
}