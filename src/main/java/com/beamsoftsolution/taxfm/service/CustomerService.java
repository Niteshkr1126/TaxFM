package com.beamsoftsolution.taxfm.service;

import com.beamsoftsolution.taxfm.exception.TaxFMException;
import com.beamsoftsolution.taxfm.model.Customer;

import java.util.List;

public interface CustomerService {
	
	List<Customer> getAllCustomers();
	Customer getLoggedInCustomer();
	Customer getCustomerById(Integer customerId) throws TaxFMException;
	Customer addCustomer(Customer customer);
	Customer updateCustomer(Customer customer);
	void deleteCustomerById(Integer customerId);
}
