package com.beamsoftsolution.taxfm.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "service_rates")
public class ServiceRate {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long serviceId;
	
	@ManyToOne
	@JoinColumn(name = "department_id", nullable = false)
	private Department department;
	
	private String category;
	
	private Double turnoverMin;
	
	private Double turnoverMax;
	
	private String slabRange;
	
	private Double rate;
	
	private Double gstPercentage;
	
	private String frequency;
}
