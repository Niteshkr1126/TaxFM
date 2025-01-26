package com.beamsoftsolution.taxfm.service.impl;

import com.beamsoftsolution.taxfm.exception.TaxFMException;
import com.beamsoftsolution.taxfm.model.Customer;
import com.beamsoftsolution.taxfm.repository.CustomerRepository;
import com.beamsoftsolution.taxfm.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	private CustomerRepository customerRepository;
	
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
	public Customer addCustomer(Customer customer) {
		return customerRepository.save(customer);
	}
	
	@Override
	public Customer updateCustomer(Customer customer) {
		return null;
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
	
	//	@Override
	//	public void addServiceDetail(Integer customerId, ServiceDetail serviceDetail) throws TaxFMException {
	//		Customer customer = getCustomerById(customerId);
	//		customer.getServiceDetails().add(serviceDetail);
	//		customerRepository.save(customer);
	//	}
}
