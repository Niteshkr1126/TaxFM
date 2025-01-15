package com.beamsoftsolution.taxfm.service.impl;

import com.beamsoftsolution.taxfm.config.CustomSessionManagementService;
import com.beamsoftsolution.taxfm.constant.Constants;
import com.beamsoftsolution.taxfm.exception.TaxFMException;
import com.beamsoftsolution.taxfm.model.Employee;
import com.beamsoftsolution.taxfm.repository.EmployeeRepository;
import com.beamsoftsolution.taxfm.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	private CustomSessionManagementService customSessionManagementService;
	
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
	public void addEmployee(Employee employee) throws TaxFMException {
		if(this.getTotalEmployeesCount() < Constants.MAX_EMPLOYEES) {
			employee.setPassword(passwordEncoder.encode(employee.getPassword()));
			employeeRepository.save(employee);
		} else {
			throw new TaxFMException("max.employee.reached").withErrorCode(400);
		}
	}
	
	@Override
	public Employee updateEmployee(Employee employee) throws TaxFMException {
		Employee employeeInDB = getEmployeeById(employee.getEmployeeId());
		employeeInDB.setUsername(employee.getUsername());
		employeeInDB.setFirstName(employee.getFirstName());
		employeeInDB.setLastName(employee.getLastName());
		employeeInDB.setEmail(employee.getEmail());
		employeeInDB.setPhoneNumber(employee.getPhoneNumber());
		employeeInDB.setDob(employee.getDob());
		employeeInDB.setMaritalStatus(employee.getMaritalStatus());
		employeeInDB.setAddress(employee.getAddress());
		employeeInDB.setAccountNonExpired(employee.getAccountNonExpired());
		employeeInDB.setAccountNonLocked(employee.getAccountNonLocked());
		employeeInDB.setCredentialsNonExpired(employee.getCredentialsNonExpired());
		employeeInDB.setEnabled(employee.getEnabled());
		employeeInDB.setAuthorities(employee.getAuthorities());
		customSessionManagementService.invalidateUserSessions(employee.getUsername());
		return employeeRepository.save(employeeInDB);
	}
	
	@Override
	public void deleteEmployee(Employee employee) {
		employeeRepository.delete(employee);
		customSessionManagementService.invalidateUserSessions(employee.getUsername());
	}
	
	@Override
	public void deleteEmployeeById(Integer employeeId) throws TaxFMException {
		employeeRepository.deleteById(employeeId);
		customSessionManagementService.invalidateUserSessions(this.getEmployeeById(employeeId).getUsername());
	}
	
	@Override
	public long getTotalEmployeesCount() {
		return employeeRepository.count();
	}
	
	@Override
	public Page<Employee> getPaginatedEmployees(Pageable pageable) {
		return employeeRepository.findAll(pageable);
	}
}