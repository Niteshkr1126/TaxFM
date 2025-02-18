package com.beamsoftsolution.taxfm.repository;

import com.beamsoftsolution.taxfm.model.Customer;
import com.beamsoftsolution.taxfm.model.Employee;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
	
	@EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = { "roles" })
	Optional<Employee> findByUsername(String username);
	
	@EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = { "roles" })
	List<Employee> findAll();
	
	@Query("SELECT e FROM Employee e WHERE e.username = ?#{ principal.username}")
	Optional<Employee> findLoggedInEmployee();
	// Assuming you have a many-to-many or one-to-many relationship
	@Query("SELECT c FROM Employee e JOIN e.assignedCustomers c WHERE e.employeeId = :employeeId")
	List<Customer> findAssignedCustomersByEmployeeId(@Param("employeeId") Integer employeeId);
	List<Employee> findBySupervisor_EmployeeId(Integer supervisorId);
	List<Employee> findBySupervisorIsNull();
	@Query("SELECT e.supervisor FROM Employee e WHERE e.employeeId = :employeeId")
	Optional<Employee> findSupervisorByEmployeeId(@Param("employeeId") Integer employeeId);
}