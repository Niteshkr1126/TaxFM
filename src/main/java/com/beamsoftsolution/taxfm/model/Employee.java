package com.beamsoftsolution.taxfm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "employees")
public class Employee {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer employeeId;
	
	@NonNull
	@Column(unique = true)
	private String username;
	
	private String firstName;
	
	private String lastName;
	
	private String designation;
	
	@NonNull
	@Column(unique = true)
	private String email;
	
	@NonNull
	@Column(unique = true)
	private String phoneNumber;
	
	private Date dob;
	
	private String gender;
	
	private String maritalStatus;
	
	private String address;
	
	@Column(unique = true)
	private String pan;
	
	@Column(unique = true)
	private Long aadharCardNumber;
	
	private String agreement;
	
	@NonNull
	@JsonIgnore
	private String password;
	
	@Singular("role")
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinTable(
			name = "employees_roles",
			joinColumns = { @JoinColumn(name = "employeeId", referencedColumnName = "employeeId") },
			inverseJoinColumns = { @JoinColumn(name = "roleId", referencedColumnName = "roleId") }
	)
	private List<Role> roles;
	
	@Singular("authority")
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinTable(
			name = "employees_authorities",
			joinColumns = { @JoinColumn(name = "employeeId", referencedColumnName = "employeeId") },
			inverseJoinColumns = { @JoinColumn(name = "authorityId", referencedColumnName = "authorityId") }
	)
	private List<Authority> authorities;
	
	// Owning side (foreign key in DB)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "supervisor_id")
	@JsonIgnore
	private Employee supervisor;
	
	// Inverse side (no DB column)
	@OneToMany(mappedBy = "supervisor", fetch = FetchType.LAZY)
	@JsonIgnore
	@Builder.Default
	private List<Employee> subordinates = new ArrayList<>();
	
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinTable(
			name = "employees_customers",
			joinColumns = { @JoinColumn(name = "employeeId", referencedColumnName = "employeeId") },
			inverseJoinColumns = { @JoinColumn(name = "customerId", referencedColumnName = "customerId") },
			uniqueConstraints = { @UniqueConstraint(columnNames = { "employeeId", "customerId" }) }
	)
	private List<Customer> assignedCustomers;
	
	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private transient List<Attendance> attendanceRecords;
	
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
			return "Error converting Employee to JSON: " + e.getMessage();
		}
	}
}