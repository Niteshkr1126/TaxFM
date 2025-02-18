package com.beamsoftsolution.taxfm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "customers")
public class Customer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer customerId;
	
	@NonNull
	@Column(unique = true)
	private String username;
	
	private String firstName;
	
	private String lastName;
	
	@NonNull
	private String firmName;
	
	private String address;
	
	private String email;
	
	@NonNull
	@Column(unique = true)
	private String phoneNumber;
	
	@Column(unique = true)
	private String pan;
	
	@Column(unique = true)
	private Long aadharCardNumber;
	
	@Column(unique = true)
	private String gstNumber;
	
	@NonNull
	@JsonIgnore
	private String password;
	
	@Singular("role")
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinTable(
			name = "customers_roles",
			joinColumns = { @JoinColumn(name = "customerId", referencedColumnName = "customerId") },
			inverseJoinColumns = { @JoinColumn(name = "roleId", referencedColumnName = "roleId") }
	)
	private List<Role> roles;
	
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinTable(
			name = "customers_services",
			joinColumns = { @JoinColumn(name = "customerId", referencedColumnName = "customerId") },
			inverseJoinColumns = { @JoinColumn(name = "serviceId", referencedColumnName = "serviceId") },
			uniqueConstraints = { @UniqueConstraint(columnNames = { "customerId", "serviceId" }) }
	)
	private List<ServiceRate> services;
	
	@Builder.Default
	private Boolean accountNonExpired = true;
	
	@Builder.Default
	private Boolean accountNonLocked = true;
	
	@Builder.Default
	private Boolean credentialsNonExpired = true;
	
	@Builder.Default
	private Boolean enabled = true;
	
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		}
		catch(JsonProcessingException e) {
			return "Error converting Customer to JSON: " + e.getMessage();
		}
	}
}