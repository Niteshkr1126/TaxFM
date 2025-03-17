package com.beamsoftsolution.taxfm.service.impl;

import com.beamsoftsolution.taxfm.model.Department;
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
	ServiceRateRepository serviceRateRepository;
	
	@Override
	public List<ServiceRate> getAllServiceRates() {
		return serviceRateRepository.findAll();
	}
	
	@Override
	public ServiceRate getServiceRatesByServiceId(Integer serviceId) {
		return serviceRateRepository.findByServiceId(serviceId);
	}
	
	@Override
	public List<ServiceRate> getServiceRatesByDepartment(Department department) {
		return serviceRateRepository.findByDepartment(department);
	}
}