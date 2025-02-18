package com.beamsoftsolution.taxfm.service;

import com.beamsoftsolution.taxfm.exception.TaxFMException;
import com.beamsoftsolution.taxfm.model.Customer;

import java.util.List;

public interface CustomerService {
	
	List<Customer> getAllCustomers();
	Customer getLoggedInCustomer();
	Customer getCustomerById(Integer customerId) throws TaxFMException;
	Customer getCustomerByUsername(String username) throws TaxFMException;
	Customer addCustomer(Customer customer) throws TaxFMException;
	Customer updateCustomer(Customer customer) throws TaxFMException;
	void assignServiceRate(Integer customerId, Integer serviceId) throws TaxFMException;
	void removeAssignServiceRate(Integer customerId, Integer serviceId) throws TaxFMException;
	void deleteCustomerById(Integer customerId) throws TaxFMException;
	void toggleLock(Integer customerId) throws TaxFMException;
	void toggleEnable(Integer customerId) throws TaxFMException;
	void resetPassword(Integer customerId, String newPassword) throws TaxFMException;
	long getTotalCustomersCount() throws TaxFMException;
}