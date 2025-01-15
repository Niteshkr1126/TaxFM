package com.beamsoftsolution.taxfm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "employees")
public class Employee {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer employeeId;
	@NonNull
	@Column(unique = true)
	private String username;
	private String firstName;
	private String lastName;
	@NonNull
	@Column(unique = true)
	private String email;
	@NonNull
	@Column(unique = true)
	private String phoneNumber;
	private Date dob;
	private String maritalStatus;
	private String address;
	private String pan;
	private Long aadharCardNumber;
	private String agreement;
	@NonNull
	@JsonIgnore
	private String password;
	@Singular("authority")
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinTable(name = "employees_authorities", joinColumns = {
			@JoinColumn(name = "employeeId", referencedColumnName = "employeeId") }, inverseJoinColumns = {
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