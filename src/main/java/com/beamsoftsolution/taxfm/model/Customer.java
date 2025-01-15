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
	private String pan;
	private Long aadharCardNumber;
	private String gstNumber;
	@Singular("serviceDetail")
	@OneToMany(targetEntity = ServiceDetail.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "customerId", nullable = false)
	@Fetch(FetchMode.SUBSELECT)
	private List<ServiceDetail> serviceDetails;
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