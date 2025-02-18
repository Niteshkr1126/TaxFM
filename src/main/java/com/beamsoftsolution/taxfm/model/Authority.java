package com.beamsoftsolution.taxfm.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@Table(name = "authorities")
public class Authority {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long authorityId;
	
	@Column(unique = true, nullable = false, length = 50)
	private String authority;
	
	private String description;
	
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		}
		catch(JsonProcessingException e) {
			return "Error converting Authority to JSON: " + e.getMessage();
		}
	}
}