package com.beamsoftsolution.taxfm.repository;

import com.beamsoftsolution.taxfm.model.Employee;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
	
	@EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = { "authorities" })
	Optional<Employee> findByUsername(String username);
	
	@EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = { "authorities" })
	List<Employee> findAll();
	@Query("SELECT e FROM Employee e WHERE e.username = ?#{ principal.username}")
	Optional<Employee> findLoginEmployee();
}