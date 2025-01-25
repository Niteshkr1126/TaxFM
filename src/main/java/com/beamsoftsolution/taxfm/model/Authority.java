package com.beamsoftsolution.taxfm.model;

import com.google.gson.Gson;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "authorities")
public class Authority {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer authorityId;
	
	@NonNull
	@Column(unique = true)
	private String authority;
	
	public String toString() {
		return new Gson().toJson(this);
	}
}