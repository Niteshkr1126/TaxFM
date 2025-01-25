package com.beamsoftsolution.taxfm.service.impl;

import com.beamsoftsolution.taxfm.model.ServiceRate;
import com.beamsoftsolution.taxfm.repository.ServiceRateRepository;
import com.beamsoftsolution.taxfm.service.ServiceRateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ServiceRateServiceImpl implements ServiceRateService {
	
	@Autowired
	private ServiceRateRepository serviceRateRepository;
	
	@Override
	public List<ServiceRate> getAllServiceRates() {
		return serviceRateRepository.findAll();
	}
	
	@Override
	public List<ServiceRate> getServiceRatesByType(String serviceType) {
		return serviceRateRepository.findByServiceType(serviceType);
	}
	
	@Override
	public List<ServiceRate> getServiceRatesByCategory(String category) {
		return serviceRateRepository.findByCategory(category);
	}
	
	@Override
	public List<ServiceRate> getServiceRatesByFrequency(String frequency) {
		return serviceRateRepository.findByFrequency(frequency);
	}
}