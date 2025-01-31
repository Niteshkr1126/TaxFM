package com.beamsoftsolution.taxfm.service;

import com.beamsoftsolution.taxfm.exception.TaxFMException;
import com.beamsoftsolution.taxfm.model.Customer;
import com.beamsoftsolution.taxfm.model.Employee;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {
	
	List<Employee> getAllEmployees();
	Employee getLoggedInEmployee();
	Employee getEmployeeById(Integer employeeId) throws TaxFMException;
	Employee getEmployeeByUsername(String username) throws TaxFMException;
	void addEmployee(Employee employee) throws TaxFMException;
	Employee updateEmployee(Employee employee) throws TaxFMException;
	void updateEmployeePassword(Integer employeeId, String newPassword) throws TaxFMException;
	void assignCustomer(Integer employeeId, Integer customerId) throws TaxFMException;
	void deleteEmployee(Employee employee) throws TaxFMException;
	void deleteEmployeeById(Integer employeeId) throws TaxFMException;
	long getTotalEmployeesCount() throws TaxFMException;
	Page<Employee> getPaginatedEmployees(Pageable pageable) throws TaxFMException;
	List<Customer> getAllCustomersForEmployee(Integer employeeId) throws TaxFMException;
	void removeCustomerFromEmployee(Integer employeeId, Integer customerId) throws TaxFMException;
	// Add subordinate to an employee
	@Transactional
	Employee addSubordinate(Integer employeeId, Integer subordinateId) throws TaxFMException;
	// Remove subordinate from an employee
	@Transactional
	void removeSubordinate(Integer employeeId, Integer subordinateId) throws TaxFMException;
	List<Employee> getAvailableEmployeesForSubordination(Integer employeeId) throws TaxFMException;
	void toggleLock(Integer employeeId) throws TaxFMException;
	void toggleEnable(Integer employeeId) throws TaxFMException;
	void resetPassword(Integer employeeId, String newPassword) throws TaxFMException;
}