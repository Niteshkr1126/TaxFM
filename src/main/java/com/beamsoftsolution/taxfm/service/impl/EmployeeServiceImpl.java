package com.beamsoftsolution.taxfm.service.impl;

import com.beamsoftsolution.taxfm.config.CustomSessionManagementService;
import com.beamsoftsolution.taxfm.constant.Constants;
import com.beamsoftsolution.taxfm.exception.TaxFMException;
import com.beamsoftsolution.taxfm.model.Customer;
import com.beamsoftsolution.taxfm.model.Employee;
import com.beamsoftsolution.taxfm.repository.EmployeeRepository;
import com.beamsoftsolution.taxfm.service.CustomerService;
import com.beamsoftsolution.taxfm.service.EmployeeService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	CustomSessionManagementService customSessionManagementService;
	
	@Autowired
	CustomerService customerService;
	
	@Override
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}
	
	@Override
	public Employee getLoggedInEmployee() {
		return employeeRepository.findLoginEmployee().orElse(null);
	}
	
	@Override
	public Employee getEmployeeById(Integer employeeId) throws TaxFMException {
		Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
		if(optionalEmployee.isEmpty()) {
			throw new TaxFMException("employee.not.found").withErrorCode(404);
		}
		return optionalEmployee.get();
	}
	
	@Override
	public Employee getEmployeeByUsername(String username) throws TaxFMException {
		Optional<Employee> optionalEmployee = employeeRepository.findByUsername(username);
		if(optionalEmployee.isEmpty()) {
			throw new TaxFMException("employee.not.found").withErrorCode(404);
		}
		return optionalEmployee.get();
	}
	
	@Override
	public void addEmployee(Employee employee) throws TaxFMException {
		if(getTotalEmployeesCount() < Constants.MAX_EMPLOYEES) {
			employee.setPassword(passwordEncoder.encode(employee.getPassword()));
			employeeRepository.save(employee);
		}
		else {
			throw new TaxFMException("max.employee.reached").withErrorCode(400);
		}
	}
	
	@Override
	public Employee updateEmployee(Employee employee) throws TaxFMException {
		Employee employeeInDB = getEmployeeById(employee.getEmployeeId());
		employeeInDB.setUsername(employee.getUsername());
		employeeInDB.setFirstName(employee.getFirstName());
		employeeInDB.setLastName(employee.getLastName());
		employeeInDB.setDesignation(employee.getDesignation());
		employeeInDB.setEmail(employee.getEmail());
		employeeInDB.setPhoneNumber(employee.getPhoneNumber());
		employeeInDB.setDob(employee.getDob());
		employeeInDB.setGender(employee.getGender());
		employeeInDB.setMaritalStatus(employee.getMaritalStatus());
		employeeInDB.setAddress(employee.getAddress());
		employeeInDB.setPan(employee.getPan());
		employeeInDB.setAadharCardNumber(employee.getAadharCardNumber());
		employeeInDB.setAgreement(employee.getAgreement());
		employeeInDB.setAccountNonExpired(employee.getAccountNonExpired());
		employeeInDB.setAccountNonLocked(employee.getAccountNonLocked());
		employeeInDB.setCredentialsNonExpired(employee.getCredentialsNonExpired());
		employeeInDB.setEnabled(employee.getEnabled());
		employeeInDB.setAuthorities(employee.getAuthorities());
		employeeInDB.setAssignedCustomers(employee.getAssignedCustomers());
		customSessionManagementService.invalidateUserSessions(employee.getUsername());
		return employeeRepository.save(employeeInDB);
	}
	
	@Override
	public void updateEmployeePassword(Integer employeeId, String newPassword) throws TaxFMException {
		Employee employeeInDB = getEmployeeById(employeeId);
		employeeInDB.setPassword(passwordEncoder.encode(newPassword));
		employeeRepository.save(employeeInDB);
	}
	
	@Override
	public void assignCustomer(Integer employeeId, Integer customerId) throws TaxFMException {
		try {
			// Fetch the employee and customer
			Employee employee = getEmployeeById(employeeId);
			Customer customer = customerService.getCustomerById(customerId);
			
			// Initialize assigned customers list if null
			if(employee.getAssignedCustomers() == null) {
				employee.setAssignedCustomers(new ArrayList<>());
			}
			
			// Check if the customer is already assigned
			boolean alreadyAssigned = employee.getAssignedCustomers().stream()
			                                  .anyMatch(assignedCustomer -> assignedCustomer.getCustomerId().equals(customerId));
			
			// If not assigned, add the customer
			if(!alreadyAssigned) {
				employee.getAssignedCustomers().add(customer);
				employeeRepository.save(employee);
			}
		}
		catch(Exception e) {
			throw new TaxFMException("customer.assignment.failed").withErrorCode(404);
		}
	}
	
	@Override
	public void removeCustomerFromEmployee(Integer employeeId, Integer customerId) throws TaxFMException {
		try {
			Employee employee = getEmployeeById(employeeId);
			
			// Remove the customer from the employee's assigned customers
			if(employee.getAssignedCustomers() != null) {
				employee.getAssignedCustomers().removeIf(assignedCustomer -> assignedCustomer.getCustomerId().equals(customerId));
				employeeRepository.save(employee);
			}
			else {
				throw new TaxFMException("customer.not.assigned").withErrorCode(404);
			}
		}
		catch(Exception e) {
			throw new TaxFMException("customer.remove.failed").withErrorCode(400);
		}
	}
	
	@Override
	public void deleteEmployee(Employee employee) {
		// Remove assigned customers
		employee.getAssignedCustomers().clear();
		employeeRepository.save(employee);
		employeeRepository.delete(employee);
		customSessionManagementService.invalidateUserSessions(employee.getUsername());
	}
	
	@Override
	public void deleteEmployeeById(Integer employeeId) throws TaxFMException {
		employeeRepository.deleteById(employeeId);
		customSessionManagementService.invalidateUserSessions(getEmployeeById(employeeId).getUsername());
	}
	
	@Override
	public long getTotalEmployeesCount() throws TaxFMException {
		return employeeRepository.count();
	}
	
	@Override
	public Page<Employee> getPaginatedEmployees(Pageable pageable) {
		return employeeRepository.findAll(pageable);
	}
	
	@Override
	public List<Customer> getAllCustomersForEmployee(Integer employeeId) throws TaxFMException {
		Employee employee = getEmployeeById(employeeId);
		// Check if the employee has the ADMIN authority
		boolean isAdmin = employee.getAuthorities().stream()
		                          .anyMatch(authority -> authority.getAuthority().equalsIgnoreCase("ADMIN"));
		
		if(isAdmin) {
			// If the user has ADMIN authority, return all customers
			return customerService.getAllCustomers();
		}
		else {
			// Otherwise, return only assigned customers
			return employeeRepository.findAssignedCustomersByEmployeeId(employeeId);
		}
	}
	
	// Add subordinate to an employee
	@Override
	@Transactional
	public Employee addSubordinate(Integer employeeId, Integer subordinateId) throws TaxFMException {
		Employee supervisor = getEmployeeById(employeeId);
		Employee subordinate = getEmployeeById(subordinateId);
		
		if(supervisor.getSubordinates().contains(subordinate)) {
			throw new TaxFMException("employee.already.subordinate").withErrorCode(400);
		}
		
		supervisor.getSubordinates().add(subordinate);
		subordinate.setSupervisor(supervisor);
		
		employeeRepository.save(supervisor);
		return employeeRepository.save(subordinate);
	}
	
	// Remove subordinate from an employee
	@Override
	@Transactional
	public void removeSubordinate(Integer employeeId, Integer subordinateId) throws TaxFMException {
		Employee supervisor = getEmployeeById(employeeId);
		
		Employee subordinate = supervisor.getSubordinates().stream()
		                                 .filter(e -> e.getEmployeeId().equals(subordinateId))
		                                 .findFirst()
		                                 .orElseThrow(() -> new TaxFMException("subordinate.not.found"));
		
		supervisor.getSubordinates().remove(subordinate);
		subordinate.setSupervisor(null);
		
		employeeRepository.save(supervisor);
		employeeRepository.save(subordinate);
	}
	
	public List<Employee> getSubordinates(Integer employeeId) {
		return employeeRepository.findBySupervisor_EmployeeId(employeeId);
	}
	
	public List<Employee> getAvailableEmployeesForSubordination(Integer employeeId) throws TaxFMException {
		Employee current = getEmployeeById(employeeId);
		
		return employeeRepository.findAll().stream()
		                         .filter(e -> !e.equals(current))
		                         .filter(e -> !current.getSubordinates().contains(e))
		                         .filter(e -> e.getAuthorities().stream().noneMatch(authority -> "ADMIN".equalsIgnoreCase(authority.getAuthority())))
		                         .collect(Collectors.toList());
	}
}