package com.beamsoftsolution.taxfm.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.util.Collection;
import java.util.Collections;

public class SecurityUserAuthentication implements Authentication {
	
	@Serial
	private static final long serialVersionUID = 1L;
	private final boolean isAuthenticated;
	private final String name;
	private final String password;
	private final SecurityUser securityUser;
	private final Collection<GrantedAuthority> authorities;
	
	private SecurityUserAuthentication(Collection<GrantedAuthority> authorities, String name, SecurityUser securityUser, String password) {
		this.authorities = authorities;
		this.name = name;
		this.password = password;
		this.securityUser = securityUser;
		this.isAuthenticated = password == null;
	}
	
	public static SecurityUserAuthentication unauthenticated(String name, String password) {
		return new SecurityUserAuthentication(Collections.emptyList(), name, null, password);
	}
	
	public static SecurityUserAuthentication authenticated(SecurityUser securityUser) {
		return new SecurityUserAuthentication(securityUser.getAuthorities(), securityUser.getUsername(), securityUser, securityUser.getPassword());
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	
	@Override
	public Object getCredentials() {
		return null;
	}
	
	@Override
	public Object getDetails() {
		return null;
	}
	
	@Override
	public Object getPrincipal() {
		return securityUser;
	}
	
	@Override
	public boolean isAuthenticated() {
		return isAuthenticated;
	}
	
	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		throw new IllegalArgumentException("Don't do this");
	}
	
	public String getPassword() {
		return password;
	}
}