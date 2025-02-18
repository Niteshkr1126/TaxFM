package com.beamsoftsolution.taxfm.service.impl;

import com.beamsoftsolution.taxfm.constant.Constants;
import com.beamsoftsolution.taxfm.exception.TaxFMException;
import com.beamsoftsolution.taxfm.model.Customer;
import com.beamsoftsolution.taxfm.model.Role;
import com.beamsoftsolution.taxfm.model.ServiceRate;
import com.beamsoftsolution.taxfm.repository.CustomerRepository;
import com.beamsoftsolution.taxfm.service.CustomerService;
import com.beamsoftsolution.taxfm.service.RoleService;
import com.beamsoftsolution.taxfm.service.ServiceRateService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	RoleService roleService;
	
	@Autowired
	ServiceRateService serviceRateService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Override
	public List<Customer> getAllCustomers() {
		return customerRepository.findAll();
	}
	
	@Override
	public Customer getLoggedInCustomer() {
		return customerRepository.findLoggedInCustomer().orElse(null);
	}
	
	@Override
	public Customer getCustomerById(Integer customerId) throws TaxFMException {
		Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
		if(optionalCustomer.isEmpty()) {
			throw new TaxFMException("customer.not.found").withErrorCode(404);
		}
		return optionalCustomer.get();
	}
	
	@Override
	public Customer getCustomerByUsername(String username) throws TaxFMException {
		Optional<Customer> optionalCustomer = customerRepository.findByUsername(username);
		if(optionalCustomer.isEmpty()) {
			throw new TaxFMException("customer.not.found").withErrorCode(404);
		}
		return optionalCustomer.get();
	}
	
	@Override
	public Customer addCustomer(Customer customer) throws TaxFMException {
		if(getTotalCustomersCount() < Constants.MAX_CUSTOMERS) {
			customer.setPassword(passwordEncoder.encode(customer.getPassword()));
			// Add default "ROLE_CUSTOMER" role if not already present
			Role customerRole = roleService.getRoleByRole("ROLE_CUSTOMER");
			List<Role> roles = new ArrayList<>();
			roles.add(customerRole);
			customer.setRoles(roles);
			return customerRepository.save(customer);
		}
		else {
			throw new TaxFMException("max.customer.reached").withErrorCode(400);
		}
	}
	
	@Override
	public Customer updateCustomer(Customer customer) throws TaxFMException {
		Customer existingCustomer = getCustomerById(customer.getCustomerId());
		existingCustomer.setUsername(customer.getUsername());
		existingCustomer.setFirstName(customer.getFirstName());
		existingCustomer.setLastName(customer.getLastName());
		existingCustomer.setFirmName(customer.getFirmName());
		existingCustomer.setAddress(customer.getAddress());
		existingCustomer.setEmail(customer.getEmail());
		existingCustomer.setPhoneNumber(customer.getPhoneNumber());
		existingCustomer.setPan(customer.getPan());
		existingCustomer.setAadharCardNumber(customer.getAadharCardNumber());
		existingCustomer.setGstNumber(customer.getGstNumber());
		existingCustomer.setAccountNonExpired(customer.getAccountNonExpired());
		existingCustomer.setAccountNonLocked(customer.getAccountNonLocked());
		existingCustomer.setCredentialsNonExpired(customer.getCredentialsNonExpired());
		existingCustomer.setEnabled(customer.getEnabled());
		existingCustomer.setServices(customer.getServices());
		return customerRepository.save(existingCustomer);
	}
	
	@Override
	@Transactional
	public void assignServiceRate(Integer customerId, Integer serviceId) throws TaxFMException {
		
		Customer customer = getCustomerById(customerId);
		ServiceRate serviceRate = serviceRateService.getServiceRatesByServiceId(serviceId);
		
		// Initialize assigned services list if null
		if(customer.getServices() == null) {
			customer.setServices(new ArrayList<>());
		}
		
		// Check if the service is already assigned
		boolean alreadyAssigned = customer.getServices().stream()
		                                  .anyMatch(service -> service.equals(serviceRate));
		
		if(!alreadyAssigned) {
			customer.getServices().add(serviceRate);
			customerRepository.save(customer);
		}
	}
	
	@Override
	public void removeAssignServiceRate(Integer customerId, Integer serviceId) throws TaxFMException {
		try {
			Customer customer = getCustomerById(customerId);
			ServiceRate serviceRate = serviceRateService.getServiceRatesByServiceId(serviceId);
			
			// Remove the service from the customer's assigned services
			if(customer.getServices() != null) {
				customer.getServices().removeIf(assignedService -> assignedService.equals(serviceRate));
				customerRepository.save(customer);
			}
			else {
				throw new TaxFMException("service.not.assigned").withErrorCode(404);
			}
		}
		catch(Exception e) {
			throw new TaxFMException("service.remove.failed").withErrorCode(400);
		}
	}
	
	@Override
	public void deleteCustomerById(Integer customerId) {
		customerRepository.deleteById(customerId);
	}
	
	@Override
	public void toggleLock(Integer customerId) throws TaxFMException {
		Customer customer = getCustomerById(customerId);
		customer.setAccountNonLocked(!customer.getAccountNonLocked());
		customerRepository.save(customer);
	}
	
	@Override
	public void toggleEnable(Integer customerId) throws TaxFMException {
		Customer customer = getCustomerById(customerId);
		customer.setEnabled(!customer.getEnabled());
		customerRepository.save(customer);
	}
	
	@Override
	public void resetPassword(Integer customerId, String newPassword) throws TaxFMException {
		Customer customer = getCustomerById(customerId);
		customer.setPassword(passwordEncoder.encode(newPassword));
		customerRepository.save(customer);
	}
	
	@Override
	public long getTotalCustomersCount() throws TaxFMException {
		return customerRepository.count();
	}
}