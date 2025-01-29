package com.beamsoftsolution.taxfm.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.Serial;
import java.util.Collection;

@Getter
@Setter
public class SecurityUser extends User {
	
	@Serial
	private static final long serialVersionUID = 1L;
	
	private String firstName;
	
	private String lastName;
	
	private String fullName;
	
	private String email;
	
	public SecurityUser(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities,
			String firstName, String lastName, String email) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.firstName = firstName;
		this.lastName = lastName;
		this.fullName = firstName + " " + lastName;
		this.email = email;
	}
	
	@Override
	public String toString() {
		return "SecurityUser [firstName=" + firstName + ", lastName=" + lastName + ", name=" + fullName + ", email=" + email + "] " + super.toString();
	}
}