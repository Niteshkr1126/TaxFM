package com.beamsoftsolution.taxfm.repository;

import com.beamsoftsolution.taxfm.model.ServiceRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRateRepository extends JpaRepository<ServiceRate, Long> {
	
	List<ServiceRate> findByServiceType(String serviceType);
	
	List<ServiceRate> findByCategory(String category);
	
	List<ServiceRate> findByFrequency(String frequency);
}