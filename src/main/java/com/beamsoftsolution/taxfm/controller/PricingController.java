package com.beamsoftsolution.taxfm.controller;

import com.beamsoftsolution.taxfm.exception.TaxFMException;
import com.beamsoftsolution.taxfm.model.Department;
import com.beamsoftsolution.taxfm.model.ServiceRate;
import com.beamsoftsolution.taxfm.service.DepartmentService;
import com.beamsoftsolution.taxfm.service.ServiceRateService;
import com.beamsoftsolution.taxfm.utils.TaxFMUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(EndPoint.PRICING)
public class PricingController {
	
	@Autowired
	TaxFMUtils taxFMUtils;
	
	@Autowired
	ServiceRateService serviceRateService;
	
	@Autowired
	DepartmentService departmentService;
	
	@GetMapping("/{departmentId}")
	public String getServiceRatesByDepartment(Model model, @PathVariable Long departmentId) throws TaxFMException {
		taxFMUtils.setCompanyAttributes(model);
		Department department = departmentService.getDepartmentById(departmentId);
		model.addAttribute("department", department);
		List<ServiceRate> rates = serviceRateService.getServiceRatesByDepartment(department);
		model.addAttribute("rates", rates);
		return "landing/pricing";
	}
}