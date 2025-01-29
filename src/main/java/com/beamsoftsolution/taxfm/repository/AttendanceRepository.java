package com.beamsoftsolution.taxfm.repository;

import com.beamsoftsolution.taxfm.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
	
	Optional<Attendance> findByUsernameAndLogoutTimeNull(String username);
}
