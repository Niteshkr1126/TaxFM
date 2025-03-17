package com.beamsoftsolution.taxfm.repository;

import com.beamsoftsolution.taxfm.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
	
	Optional<Department> findByDepartmentId(Long departmentId);
}