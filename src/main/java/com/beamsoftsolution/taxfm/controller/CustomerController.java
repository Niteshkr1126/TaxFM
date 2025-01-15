package com.beamsoftsolution.taxfm.controller;

import com.beamsoftsolution.taxfm.constant.Constants;
import com.beamsoftsolution.taxfm.exception.TaxFMException;
import com.beamsoftsolution.taxfm.model.Customer;
import com.beamsoftsolution.taxfm.service.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(EndPoint.CUSTOMERS)
@Slf4j
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	@GetMapping
	public String getAllCustomers(Model model) {
		model.addAttribute("title", Constants.SHORT_COMPANY_NAME);
		model.addAttribute("topBannerCompanyName", Constants.COMPANY_NAME);
		model.addAttribute("footerCompanyName", Constants.APPLICATION_COMPANY_NAME);
		List<Customer> customers = customerService.getAllCustomers();
		model.addAttribute("customers", customers);
		return "customers/customers";
	}
	
	@GetMapping("/{customerId}")
	public String viewCustomer(@PathVariable Integer customerId, Model model) throws TaxFMException {
		model.addAttribute("title", Constants.SHORT_COMPANY_NAME);
		model.addAttribute("topBannerCompanyName", Constants.COMPANY_NAME);
		model.addAttribute("footerCompanyName", Constants.APPLICATION_COMPANY_NAME);
		Customer customer = customerService.getCustomerById(customerId);
		model.addAttribute("customer", customer);
		return "customers/customer";
	}
	
	@GetMapping("/add")
	public String showCustomerForm(Model model) {
		model.addAttribute("title", Constants.SHORT_COMPANY_NAME);
		model.addAttribute("topBannerCompanyName", Constants.COMPANY_NAME);
		model.addAttribute("footerCompanyName", Constants.APPLICATION_COMPANY_NAME);
		model.addAttribute("customer", new Customer());
		return "customers/addCustomer";
	}
	
	@PostMapping(value = "/add", params = { "addServiceDetail" })
	public String addServiceDetail(Customer customer, Model model, BindingResult bindingResult) {
		model.addAttribute("title", Constants.SHORT_COMPANY_NAME);
		model.addAttribute("topBannerCompanyName", Constants.COMPANY_NAME);
		model.addAttribute("footerCompanyName", Constants.APPLICATION_COMPANY_NAME);
		//		if(customer != null) {
		//			if(customer.getServiceDetails() == null) {
		//				List<ServiceDetail> serviceDetails = new ArrayList<>();
		//				serviceDetails.add(new ServiceDetail());
		//				customer.setServiceDetails(serviceDetails);
		//			}
		//			else {
		//				customer.getServiceDetails().add(new ServiceDetail());
		//			}
		//		}
		return "customers/addCustomer";
	}
	
	@PostMapping(value = "/add", params = { "removeServiceDetail" })
	public String removeServiceDetail(Customer customer, Model model, BindingResult bindingResult, HttpServletRequest request) {
		model.addAttribute("title", Constants.SHORT_COMPANY_NAME);
		model.addAttribute("topBannerCompanyName", Constants.COMPANY_NAME);
		model.addAttribute("footerCompanyName", Constants.APPLICATION_COMPANY_NAME);
		//		customer.getServiceDetails().remove(Integer.parseInt(request.getParameter("removeServiceDetail")));
		return "customers/addCustomer";
	}
	
	@PostMapping("/add")
	public String createCustomer(Customer customer, Errors errors) {
		if(errors != null && errors.getErrorCount() > 0) {
			errors.getAllErrors().forEach(a -> log.info(a.getDefaultMessage()));
			return "customers/addCustomer";
		}
		else {
			log.info(customer.toString());
			Customer addedCustomer = customerService.addCustomer(customer);
			return "redirect:/customers/" + addedCustomer.getCustomerId();
		}
	}
	
	@GetMapping("/{customerId}/edit")
	public String editCustomerForm(@PathVariable Integer customerId, Model model) throws TaxFMException {
		model.addAttribute("title", Constants.SHORT_COMPANY_NAME);
		model.addAttribute("topBannerCompanyName", Constants.COMPANY_NAME);
		model.addAttribute("footerCompanyName", Constants.APPLICATION_COMPANY_NAME);
		Customer customer = customerService.getCustomerById(customerId);
		model.addAttribute("customer", customer);
		return "customers/editCustomer";
	}
	
	@PostMapping(value = "/{customerId}/edit", params = { "addServiceDetail" })
	public String addServiceDetailInEdit(Customer customer, Model model) {
		model.addAttribute("title", Constants.SHORT_COMPANY_NAME);
		model.addAttribute("topBannerCompanyName", Constants.COMPANY_NAME);
		model.addAttribute("footerCompanyName", Constants.APPLICATION_COMPANY_NAME);
		//		if(customer != null) {
		//			if(customer.getServiceDetails() == null) {
		//				List<ServiceDetail> serviceDetails = new ArrayList<>();
		//				serviceDetails.add(new ServiceDetail());
		//				customer.setServiceDetails(serviceDetails);
		//			}
		//			else {
		//				customer.getServiceDetails().add(new ServiceDetail());
		//			}
		//		}
		return "customers/editCustomer";
	}
	
	@PostMapping(value = "/{customerId}/edit", params = { "removeServiceDetail" })
	public String removeServiceDetailInEdit(Customer customer, Model model, HttpServletRequest request) {
		model.addAttribute("title", Constants.SHORT_COMPANY_NAME);
		model.addAttribute("topBannerCompanyName", Constants.COMPANY_NAME);
		model.addAttribute("footerCompanyName", Constants.APPLICATION_COMPANY_NAME);
		//		ServiceDetail removeServiceDetail = customer.getServiceDetails().remove(Integer.parseInt(request.getParameter("removeServiceDetail")));
		//		if(removeServiceDetail.getServiceDetailId() != null) {
		//			serviceDetailService.deleteServiceDetailById(removeServiceDetail.getServiceDetailId());
		//		}
		return "customers/editCustomer";
	}
	
	@PostMapping("/{customerId}/edit")
	public String editCustomer(Customer customer, Errors errors) {
		if(errors != null && errors.getErrorCount() > 0) {
			errors.getAllErrors().forEach(a -> log.info(a.getDefaultMessage()));
			return "customers/editCustomer";
		}
		else {
			Customer updatedCustomer = customerService.updateCustomer(customer);
			return "redirect:/customers/" + updatedCustomer.getCustomerId();
		}
	}
	
	@GetMapping("/{customerId}/delete")
	public String deleteCustomer(@PathVariable Integer customerId) {
		customerService.deleteCustomerById(customerId);
		return "redirect:/customers";
	}
}
