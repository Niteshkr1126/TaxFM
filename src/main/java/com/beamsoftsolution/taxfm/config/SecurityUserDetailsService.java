package com.beamsoftsolution.taxfm.config;

import com.beamsoftsolution.taxfm.model.Authority;
import com.beamsoftsolution.taxfm.model.Customer;
import com.beamsoftsolution.taxfm.model.Employee;
import com.beamsoftsolution.taxfm.model.Role;
import com.beamsoftsolution.taxfm.repository.CustomerRepository;
import com.beamsoftsolution.taxfm.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@Component
public class SecurityUserDetailsService implements UserDetailsService {
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			// Try loading employee first
			Optional<Employee> optionalEmployee = employeeRepository.findByUsername(username);
			if(optionalEmployee.isPresent()) {
				Employee employee = optionalEmployee.get();
				validateEmployeeAccountStatus(employee);
				
				return new SecurityUser(employee.getUsername(), employee.getPassword(), true, true, true, true,
				                        getGrantedAuthorities(employee.getRoles(), employee.getAuthorities()), employee.getFirstName(), employee.getLastName(),
				                        employee.getEmail());
			}
			
			// Try loading customer if employee not found
			Optional<Customer> optionalCustomer = customerRepository.findByUsername(username);
			if(optionalCustomer.isPresent()) {
				Customer customer = optionalCustomer.get();
				validateCustomerAccountStatus(customer);
				
				return new SecurityUser(customer.getUsername(), customer.getPassword(), true, true, true, true,
				                        getGrantedAuthorities(customer.getRoles(), customer.getAuthorities()), customer.getFirstName(), customer.getLastName(), customer.getEmail());
			}
			
			throw new UsernameNotFoundException("No user found with username: " + username);
		}
		catch(UsernameNotFoundException e) {
			log.error("Authentication failure: {}", e.getMessage());
			throw e;
		}
		catch(Exception e) {
			log.error("Unexpected error loading user: {}", e.getMessage());
			throw new UsernameNotFoundException("Error loading user", e);
		}
	}
	
	private void validateEmployeeAccountStatus(Employee employee) {
		if(!employee.getEnabled()) {
			log.warn("Employee account disabled: {}", employee.getUsername());
			throw new UsernameNotFoundException("Account disabled");
		}
		if(!employee.getAccountNonExpired()) {
			log.warn("Employee account expired: {}", employee.getUsername());
			throw new UsernameNotFoundException("Account expired");
		}
		if(!employee.getAccountNonLocked()) {
			log.warn("Employee account locked: {}", employee.getUsername());
			throw new UsernameNotFoundException("Account locked");
		}
		if(!employee.getCredentialsNonExpired()) {
			log.warn("Employee credentials expired: {}", employee.getUsername());
			throw new UsernameNotFoundException("Credentials expired");
		}
	}
	
	private void validateCustomerAccountStatus(Customer customer) {
		if(!customer.getEnabled()) {
			log.warn("Customer account disabled: {}", customer.getUsername());
			throw new UsernameNotFoundException("Account disabled");
		}
		if(!customer.getAccountNonExpired()) {
			log.warn("Customer account expired: {}", customer.getUsername());
			throw new UsernameNotFoundException("Account expired");
		}
		if(!customer.getAccountNonLocked()) {
			log.warn("Customer account locked: {}", customer.getUsername());
			throw new UsernameNotFoundException("Account locked");
		}
		if(!customer.getCredentialsNonExpired()) {
			log.warn("Customer credentials expired: {}", customer.getUsername());
			throw new UsernameNotFoundException("Credentials expired");
		}
	}
	
	private Collection<? extends GrantedAuthority> getGrantedAuthorities(List<Role> roles, List<Authority> authorities) {
		return Stream.concat(
				// Stream roles as SimpleGrantedAuthority("ROLE_<roleName>")
				roles.stream()
				     .flatMap(role -> Stream.concat(
						     Stream.of(new SimpleGrantedAuthority(role.getRole())),
						     role.getAuthorities().stream().map(auth -> new SimpleGrantedAuthority(auth.getAuthority()))
				     )),
				
				// Stream standalone authorities as SimpleGrantedAuthority("<authority>")
				authorities.stream().map(auth -> new SimpleGrantedAuthority(auth.getAuthority()))
		).collect(Collectors.toSet());
	}
}