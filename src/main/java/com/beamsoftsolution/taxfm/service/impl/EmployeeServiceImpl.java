package com.beamsoftsolution.taxfm.service.impl;

import com.beamsoftsolution.taxfm.config.CustomSessionManagementService;
import com.beamsoftsolution.taxfm.constant.Constants;
import com.beamsoftsolution.taxfm.exception.TaxFMException;
import com.beamsoftsolution.taxfm.model.Authority;
import com.beamsoftsolution.taxfm.model.Customer;
import com.beamsoftsolution.taxfm.model.Employee;
import com.beamsoftsolution.taxfm.repository.EmployeeRepository;
import com.beamsoftsolution.taxfm.service.CustomerService;
import com.beamsoftsolution.taxfm.service.EmployeeService;
import com.beamsoftsolution.taxfm.utils.TaxFMUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
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
	
	@Autowired
	TaxFMUtils taxFMUtils;
	
	@Override
	@Transactional
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}
	
	@Override
	@Transactional
	public Employee getLoggedInEmployee() {
		return employeeRepository.findLoggedInEmployee().orElse(null);
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
	public Employee addEmployee(Employee employee) throws TaxFMException {
		if(getTotalEmployeesCount() < Constants.MAX_EMPLOYEES) {
			employee.setPassword(passwordEncoder.encode(employee.getPassword()));
			return employeeRepository.save(employee);
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
		employeeInDB.setRoles(employee.getRoles());
		employeeInDB.setAssignedCustomers(employee.getAssignedCustomers());
		customSessionManagementService.invalidateUserSessions(employee.getUsername());
		return employeeRepository.save(employeeInDB);
	}
	
	@Override
	@Transactional
	public void assignSubordinate(Integer employeeId, Integer subordinateId) throws TaxFMException {
		Employee supervisor = getEmployeeById(employeeId);
		Employee subordinate = getEmployeeById(subordinateId);
		
		if(supervisor.getSubordinates().contains(subordinate)) {
			throw new TaxFMException("employee.already.subordinate").withErrorCode(400);
		}
		
		supervisor.getSubordinates().add(subordinate);
		subordinate.setSupervisor(supervisor);
		
		employeeRepository.save(supervisor);
		employeeRepository.save(subordinate);
	}
	
	@Override
	@Transactional
	public void removeAssignedSubordinate(Integer employeeId, Integer subordinateId) throws TaxFMException {
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
	
	@Override
	@Transactional
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
	public void removeAssignedCustomer(Integer employeeId, Integer customerId) throws TaxFMException {
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
	public void toggleLockUnlock(Integer employeeId) throws TaxFMException {
		Employee employee = getEmployeeById(employeeId);
		employee.setAccountNonLocked(!employee.getAccountNonLocked());
		employeeRepository.save(employee);
	}
	
	@Override
	public void toggleEnableDisable(Integer employeeId) throws TaxFMException {
		Employee employee = getEmployeeById(employeeId);
		employee.setEnabled(!employee.getEnabled());
		employeeRepository.save(employee);
	}
	
	@Override
	public void resetPassword(Integer employeeId, String newPassword) throws TaxFMException {
		Employee employee = getEmployeeById(employeeId);
		employee.setPassword(passwordEncoder.encode(newPassword));
		employeeRepository.save(employee);
	}
	
	@Override
	public void deleteEmployee(Employee employee) {
		employee.getAssignedCustomers().clear();
		employeeRepository.save(employee);
		employeeRepository.delete(employee);
		customSessionManagementService.invalidateUserSessions(employee.getUsername());
	}
	
	@Override
	public void deleteEmployeeById(Integer employeeId) throws TaxFMException {
		customSessionManagementService.invalidateUserSessions(getEmployeeById(employeeId).getUsername());
		employeeRepository.deleteById(employeeId);
	}
	
	@Override
	public long getTotalEmployeesCount() throws TaxFMException {
		return employeeRepository.count();
	}
	
	@Override
	public Employee getSupervisor(Integer employeeId) {
		return employeeRepository.findSupervisorByEmployeeId(employeeId).orElse(null);
	}
	
	public List<Employee> getSubordinates(Integer employeeId) {
		return employeeRepository.findBySupervisor_EmployeeId(employeeId);
	}
	
	@Transactional
	public List<Employee> getAvailableEmployeesForSubordination(Employee loggedInEmployee, Employee employee) throws TaxFMException {
		return employeeRepository.findAll().stream()
		                         .filter(e -> !e.equals(employee))  // Exclude the given employee
		                         .filter(e -> !employee.getSubordinates().contains(e))  // Exclude existing subordinates
		                         .filter(e -> {
			                         Set<String> employeeAuthorities = e.getRoles().stream()
			                                                            .flatMap(role -> role.getAuthorities().stream()) // Get all authorities from roles
			                                                            .map(Authority::getAuthority) // Extract authority names
			                                                            .collect(Collectors.toSet()); // Get employee authorities
			                         return !employeeAuthorities.contains("VIEW_ALL_EMPLOYEES"); // Ensure VIEW_ALL_EMPLOYEES is not present
		                         })
		                         .toList();
	}
	
	@Override
	public List<Customer> getCustomersForEmployeeForAssignment(Employee loggedInEmployee, Employee employee) throws TaxFMException {
		Set<String> loggedInEmployeeAuthorities = taxFMUtils.getEmployeeAuthorities(loggedInEmployee);
		Set<Customer> assignedCustomers = Set.copyOf(employee.getAssignedCustomers());
		
		if(loggedInEmployeeAuthorities.contains("VIEW_ALL_CUSTOMERS")) {
			return customerService.getAllCustomers().stream()
			                      .filter(customer -> !assignedCustomers.contains(customer))
			                      .toList();
		}
		
		if(loggedInEmployeeAuthorities.contains("VIEW_ASSIGNED_CUSTOMERS")) {
			return employeeRepository.findAssignedCustomersByEmployeeId(loggedInEmployee.getEmployeeId()).stream()
			                         .filter(customer -> !assignedCustomers.contains(customer))
			                         .toList();
		}
		
		return Collections.emptyList();
	}
	
	@Override
	public void removeUnauthorisedCustomersFromEmployee(Employee loggedInEmployee, Employee employee) throws TaxFMException {
		Set<String> loggedInEmployeeAuthorities = taxFMUtils.getEmployeeAuthorities(loggedInEmployee);
		List<Customer> assignedCustomers = employee.getAssignedCustomers();
		Set<Customer> loggedInEmployeeAssignedCustomers = Set.copyOf(loggedInEmployee.getAssignedCustomers());
		if(loggedInEmployeeAuthorities.contains("VIEW_ALL_CUSTOMERS")) {
			return;
		}
		if(loggedInEmployeeAuthorities.contains("VIEW_ASSIGNED_CUSTOMERS")) {
			// Get the intersection (common customers in both sets)
			List<Customer> finalList = assignedCustomers.stream()
			                                            .filter(loggedInEmployeeAssignedCustomers::contains)
			                                            .toList();
			
			employee.setAssignedCustomers(finalList);
		}
	}
	
	@Override
	public Page<Employee> getPaginatedEmployees(Pageable pageable) {
		return employeeRepository.findAll(pageable);
	}
}