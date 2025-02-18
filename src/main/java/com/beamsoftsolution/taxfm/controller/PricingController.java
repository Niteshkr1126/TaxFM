package com.beamsoftsolution.taxfm.controller;

import com.beamsoftsolution.taxfm.model.ServiceRate;
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
	private ServiceRateService serviceRateService;
	
	@GetMapping
	public String viewServiceRates(Model model) {
		taxFMUtils.setCompanyAttributes(model);
		List<ServiceRate> rates = serviceRateService.getAllServiceRates();
		model.addAttribute("rates", rates);
		return "landing/pricing";
	}
	
	@GetMapping("/type/{serviceType}")
	public ResponseEntity<List<ServiceRate>> getServiceRatesByType(@PathVariable String serviceType) {
		List<ServiceRate> rates = serviceRateService.getServiceRatesByType(serviceType);
		return ResponseEntity.ok(rates);
	}
	
	@GetMapping("/category/{category}")
	public ResponseEntity<List<ServiceRate>> getServiceRatesByCategory(@PathVariable String category) {
		List<ServiceRate> rates = serviceRateService.getServiceRatesByCategory(category);
		return ResponseEntity.ok(rates);
	}
	
	@GetMapping("/frequency/{frequency}")
	public ResponseEntity<List<ServiceRate>> getServiceRatesByFrequency(@PathVariable String frequency) {
		List<ServiceRate> rates = serviceRateService.getServiceRatesByFrequency(frequency);
		return ResponseEntity.ok(rates);
	}
}