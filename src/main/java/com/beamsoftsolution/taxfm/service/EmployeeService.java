package com.beamsoftsolution.taxfm.service;

import com.beamsoftsolution.taxfm.exception.TaxFMException;
import com.beamsoftsolution.taxfm.model.Customer;
import com.beamsoftsolution.taxfm.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {
	
	List<Employee> getAllEmployees();
	Employee getLoggedInEmployee();
	Employee getEmployeeById(Integer employeeId) throws TaxFMException;
	Employee getEmployeeByUsername(String username) throws TaxFMException;
	Employee addEmployee(Employee employee) throws TaxFMException;
	Employee updateEmployee(Employee employee) throws TaxFMException;
	void assignSubordinate(Integer employeeId, Integer subordinateId) throws TaxFMException;
	void removeAssignedSubordinate(Integer employeeId, Integer subordinateId) throws TaxFMException;
	void assignCustomer(Integer employeeId, Integer customerId) throws TaxFMException;
	void removeAssignedCustomer(Integer employeeId, Integer customerId) throws TaxFMException;
	void toggleLockUnlock(Integer employeeId) throws TaxFMException;
	void toggleEnableDisable(Integer employeeId) throws TaxFMException;
	void resetPassword(Integer employeeId, String newPassword) throws TaxFMException;
	void deleteEmployee(Employee employee) throws TaxFMException;
	void deleteEmployeeById(Integer employeeId) throws TaxFMException;
	long getTotalEmployeesCount() throws TaxFMException;
	Employee getSupervisor(Integer employeeId) throws TaxFMException;
	List<Employee> getSubordinates(Integer employeeId) throws TaxFMException;
	List<Employee> getAvailableEmployeesForSubordination(Employee loggedInEmployee, Employee employee) throws TaxFMException;
	List<Customer> getCustomersForEmployeeForAssignment(Employee loggedInEmployee, Employee employee) throws TaxFMException;
	void removeUnauthorisedCustomersFromEmployee(Employee loggedInEmployee, Employee employee) throws TaxFMException;
	Page<Employee> getPaginatedEmployees(Pageable pageable) throws TaxFMException;
}