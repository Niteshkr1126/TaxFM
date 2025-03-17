package com.beamsoftsolution.taxfm.service;

import com.beamsoftsolution.taxfm.model.Department;
import com.beamsoftsolution.taxfm.model.ServiceRate;

import java.time.LocalDate;
import java.util.List;

public interface ServiceRateService {
	
	List<ServiceRate> getAllServiceRates();
	ServiceRate getServiceRatesByServiceId(Integer serviceId);
	List<ServiceRate> getServiceRatesByDepartment(Department department);
}