package com.beamsoftsolution.taxfm.repository;

import com.beamsoftsolution.taxfm.model.Attendance;
import com.beamsoftsolution.taxfm.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
	
	Optional<Attendance> findByUsernameAndLogoutTimeNull(String username);
	List<Attendance> findByUsername(String username);
	List<Attendance> findByEmployeeOrderByLoginTimeDesc(Employee employee);
	List<Attendance> findByEmployeeAndLoginTimeBetweenOrderByLoginTimeDesc(Employee employee, LocalDateTime start, LocalDateTime end);
}