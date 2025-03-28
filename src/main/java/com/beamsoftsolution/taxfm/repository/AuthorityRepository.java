package com.beamsoftsolution.taxfm.repository;

import com.beamsoftsolution.taxfm.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
	
	Optional<Authority> findByAuthority(String authority);
	Optional<Authority> findByAuthorityId(Long authorityId);
}