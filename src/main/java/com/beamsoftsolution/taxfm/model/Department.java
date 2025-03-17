package com.beamsoftsolution.taxfm.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "departments")
public class Department {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long departmentId;
	
	@Column(nullable = false, unique = true)
	private String name;
	
	@Column(length = 500)
	private String description;
	
	private String imageName;
	
	private String url;
	
	@OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<ServiceRate> serviceRates;
}