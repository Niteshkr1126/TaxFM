package com.beamsoftsolution.taxfm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "customers")
public class Customer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	
	private String gstNumber;
	
	@Singular("services")
	@OneToMany(targetEntity = ServiceRate.class, cascade = CascadeType.ALL)
	@Fetch(FetchMode.SUBSELECT)
	private List<ServiceRate> services;
	
	@NonNull
	@JsonIgnore
	private String password;
	
	@Singular("authority")
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinTable(name = "customers_authorities", joinColumns = {
			@JoinColumn(name = "customerId", referencedColumnName = "customerId") }, inverseJoinColumns = {
			@JoinColumn(name = "authorityId", referencedColumnName = "authorityId") })
	private List<Authority> authorities;
	
	@Builder.Default
	private Boolean accountNonExpired = true;
	
	@Builder.Default
	private Boolean accountNonLocked = true;
	
	@Builder.Default
	private Boolean credentialsNonExpired = true;
	
	@Builder.Default
	private Boolean enabled = true;
	
	public String toString() {
		return new Gson().toJson(this);
	}
}