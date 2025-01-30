package com.beamsoftsolution.taxfm.service.impl;

import com.beamsoftsolution.taxfm.exception.TaxFMException;
import com.beamsoftsolution.taxfm.model.Authority;
import com.beamsoftsolution.taxfm.model.Customer;
import com.beamsoftsolution.taxfm.repository.CustomerRepository;
import com.beamsoftsolution.taxfm.service.AuthorityService;
import com.beamsoftsolution.taxfm.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	AuthorityService authorityService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Override
	public List<Customer> getAllCustomers() {
		return customerRepository.findAll();
	}
	
	@Override
	public Customer getLoggedInCustomer() {
		return customerRepository.findLoginCustomer().orElse(null);
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
	public Customer addCustomer(Customer customer) {
		customer.setPassword(passwordEncoder.encode(customer.getPassword()));
		return customerRepository.save(customer);
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
		existingCustomer.setAuthorities(customer.getAuthorities());
		
		// Add default "CUSTOMER" authority if not already present
		Authority customerAuthority = authorityService.getAuthorityByAuthority("CUSTOMER");
		if(customerAuthority != null && !existingCustomer.getAuthorities().contains(customerAuthority)) {
			existingCustomer.getAuthorities().add(customerAuthority);
		}
		
		existingCustomer.setServices(customer.getServices());
		return customerRepository.save(existingCustomer);
	}
	
	@Override
	public void updateCustomerPassword(Integer customerId, String newPassword) throws TaxFMException {
		Customer customerInDB = getCustomerById(customerId);
		customerInDB.setPassword(passwordEncoder.encode(newPassword));
		customerRepository.save(customerInDB);
	}
	
	//	@Override
	//	public void updateCustomer(Customer customer) throws TaxFMException {
	//		Customer existingCustomer = getCustomerById(customer.getCustomerId());
	//		existingCustomer.setCustomerName(customer.getCustomerName());
	//		existingCustomer.setFirmName(customer.getFirmName());
	//		existingCustomer.setAddress(customer.getAddress());
	//		existingCustomer.setEmail(customer.getEmail());
	//		existingCustomer.setPhoneNumber(customer.getPhoneNumber());
	//		existingCustomer.setPan(customer.getPan());
	//		existingCustomer.setAadharCardNumber(customer.getAadharCardNumber());
	//		existingCustomer.setGstNumber(customer.getGstNumber());
	//		// Ensure that the bank details are retained
	//		List<ServiceDetail> updatedServiceDetails = customer.getServiceDetails();
	//		if(updatedServiceDetails != null) {
	//			for(ServiceDetail updatedServiceDetail : updatedServiceDetails) {
	//				ServiceDetail existingServiceDetail = existingCustomer.getServiceDetails()
	//				                                             .stream()
	//				                                             .filter(b -> b.getBankDetailId().equals(updatedBankDetail.getBankDetailId()))
	//				                                             .findFirst()
	//				                                             .orElse(null);
	//
	//				if(existingServiceDetail != null) {
	//					// Update bank detail fields
	//					existingServiceDetail.setBankName(updatedServiceDetail.getBankName());
	//					existingServiceDetail.setBranch(updatedServiceDetail.getBranch());
	//					existingServiceDetail.setIfscCode(updatedServiceDetail.getIfscCode());
	//					existingServiceDetail.setAccountNumber(updatedServiceDetail.getAccountNumber());
	//					existingServiceDetail.setAccountHolderName(updatedServiceDetail.getAccountHolderName());
	//				}
	//			}
	//		}
	//		customerRepository.save(existingCustomer);
	//	}
	
	@Override
	public void deleteCustomerById(Integer customerId) {
		customerRepository.deleteById(customerId);
	}
	
	@Override
	public long getTotalCustomersCount() throws TaxFMException {
		return customerRepository.count();
	}
	
	//	@Override
	//	public void addServiceDetail(Integer customerId, ServiceDetail serviceDetail) throws TaxFMException {
	//		Customer customer = getCustomerById(customerId);
	//		customer.getServiceDetails().add(serviceDetail);
	//		customerRepository.save(customer);
	//	}
}
