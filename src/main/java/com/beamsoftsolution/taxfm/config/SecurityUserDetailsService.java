package com.beamsoftsolution.taxfm.config;

import com.beamsoftsolution.taxfm.model.Customer;
import com.beamsoftsolution.taxfm.model.Employee;
import com.beamsoftsolution.taxfm.repository.CustomerRepository;
import com.beamsoftsolution.taxfm.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Component
public class SecurityUserDetailsService implements UserDetailsService {
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			Optional<Employee> optionalEmployee = employeeRepository.findByUsername(username);
			if(optionalEmployee.isPresent()) {
				Employee employee = optionalEmployee.get();
				if(!employee.getUsername().equals(username)) {
					log.error("Could not find employee with username: {}", username);
					throw new UsernameNotFoundException("Invalid credentials!");
				}
				
				if(!employee.getAccountNonExpired()) {
					log.error("Account Expired for employee: {}", username);
					throw new UsernameNotFoundException("Account Expired!");
				}
				
				if(!employee.getAccountNonLocked()) {
					log.error("Account Locked for employee: {}", username);
					throw new UsernameNotFoundException("Account Locked!");
				}
				
				if(!employee.getCredentialsNonExpired()) {
					log.error("Credentials Expired for employee: {}", username);
					throw new UsernameNotFoundException("Credentials Expired!");
				}
				
				if(!employee.getEnabled()) {
					log.error("Account disabled: {}", username);
					throw new UsernameNotFoundException("Account disabled!");
				}
				
				Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
				employee.getAuthorities().forEach(authority -> grantedAuthorities.add(new SimpleGrantedAuthority(authority.getAuthority())));
				
				return new SecurityUser(employee.getUsername(), employee.getPassword(), true, true, true, true, grantedAuthorities,
				                        employee.getFirstName(), employee.getLastName(), employee.getEmail());
			}
			
			Optional<Customer> optionalCustomer = customerRepository.findByUsername(username);
			if(optionalCustomer.isPresent()) {
				Customer customer = optionalCustomer.get();
				if(!customer.getUsername().equals(username)) {
					log.error("Could not find customer with username: {}", username);
					throw new UsernameNotFoundException("Invalid credentials!");
				}
				
				if(!customer.getAccountNonExpired()) {
					log.error("Account Expired for customer: {}", username);
					throw new UsernameNotFoundException("Account Expired!");
				}
				
				if(!customer.getAccountNonLocked()) {
					log.error("Account Locked for customer: {}", username);
					throw new UsernameNotFoundException("Account Locked!");
				}
				
				if(!customer.getCredentialsNonExpired()) {
					log.error("Credentials Expired for customer: {}", username);
					throw new UsernameNotFoundException("Credentials Expired!");
				}
				
				if(!customer.getEnabled()) {
					log.error("Account disabled: {}", username);
					throw new UsernameNotFoundException("Account disabled!");
				}
				
				Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
				customer.getAuthorities().forEach(authority -> grantedAuthorities.add(new SimpleGrantedAuthority(authority.getAuthority())));
				
				return new SecurityUser(customer.getUsername(), customer.getPassword(), true, true, true, true, grantedAuthorities,
				                            customer.getFirstName(), customer.getLastName(), customer.getEmail());
			}
			
			log.error("Could not find employee or customer with username: {}", username);
			throw new UsernameNotFoundException("Employee or Customer not found!");
			
		}
		catch(UsernameNotFoundException usernameNotFoundException) {
			log.error("UsernameNotFoundException: {}", usernameNotFoundException.toString());
			throw usernameNotFoundException;
		}
		catch(Exception e) {
			throw new UsernameNotFoundException("An error occurred while loading the employee details.", e);
		}
	}
}