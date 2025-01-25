package com.beamsoftsolution.taxfm.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "service_rates")
public class ServiceRate {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "service_type")
	private String serviceType;
	
	@Column(name = "category")
	private String category;
	
	@Column(name = "turnover_min")
	private Double turnoverMin;
	
	@Column(name = "turnover_max")
	private Double turnoverMax;
	
	@Column(name = "slab_range")
	private String slabRange;
	
	@Column(name = "rate")
	private Double rate;
	
	@Column(name = "gst_percentage")
	private Double gstPercentage;
	
	@Column(name = "frequency")
	private String frequency;
}
