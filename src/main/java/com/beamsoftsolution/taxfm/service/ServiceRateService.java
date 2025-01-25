package com.beamsoftsolution.taxfm.service;

import com.beamsoftsolution.taxfm.model.ServiceRate;

import java.util.List;

public interface ServiceRateService {
	
	List<ServiceRate> getAllServiceRates();
	List<ServiceRate> getServiceRatesByType(String serviceType);
	List<ServiceRate> getServiceRatesByCategory(String category);
	List<ServiceRate> getServiceRatesByFrequency(String frequency);
}
