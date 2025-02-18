package com.beamsoftsolution.taxfm.model;

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
@Table(name = "roles")
public class Role {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long roleId;
	
	@Column(unique = true, nullable = false, length = 50)
	private String role;
	
	private String description;
	
	@Singular("authority")
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinTable(
			name = "roles_authorities",
			joinColumns = { @JoinColumn(name = "roleId", referencedColumnName = "roleId") },
			inverseJoinColumns = { @JoinColumn(name = "authorityId", referencedColumnName = "authorityId") }
	)
	private List<Authority> authorities;
	
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		}
		catch(JsonProcessingException e) {
			return "Error converting Role to JSON: " + e.getMessage();
		}
	}
}