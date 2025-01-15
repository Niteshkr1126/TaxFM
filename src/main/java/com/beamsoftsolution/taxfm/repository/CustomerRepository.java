package com.beamsoftsolution.taxfm.repository;

import com.beamsoftsolution.taxfm.model.Customer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	
	@EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = { "authorities" })
	Optional<Customer> findByUsername(String username);
	
	@EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = { "authorities" })
	List<Customer> findAll();
	@Query("SELECT e FROM Customer e WHERE e.username = ?#{ principal.username}")
	Optional<Customer> findLoginCustomer();
	
}