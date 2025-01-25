package com.beamsoftsolution.taxfm.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {
	
	@Autowired
	CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
	
	@Autowired
	SecurityUserDetailsService securityUserDetailsService;
	
	// Authorization
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity.csrf(AbstractHttpConfigurer::disable)
		                   .authorizeHttpRequests(authConfig -> {
			
			                   authConfig.requestMatchers("/css/**", "/js/**", "/images/**").permitAll();
			
			                   authConfig.requestMatchers(HttpMethod.GET, "/services/**", "/career/**").permitAll();
			
			                   authConfig.requestMatchers(HttpMethod.GET, "/", "/login", "/login-error", "/error", "/logout").permitAll();
			                   authConfig.requestMatchers(HttpMethod.POST, "/", "/employee-login", "customer-login", "/login-error", "/error", "/logout").permitAll();
			
			                   authConfig.requestMatchers(HttpMethod.GET, "/home").hasAnyAuthority("ADMIN", "SENIOR", "JUNIOR", "CUSTOMER");
			
			                   authConfig.requestMatchers(HttpMethod.GET, "/employees/**").hasAnyAuthority("ADMIN");
			                   authConfig.requestMatchers(HttpMethod.POST, "/employees/**").hasAnyAuthority("ADMIN");
			
			                   authConfig.requestMatchers(HttpMethod.GET, "/customers/**").hasAnyAuthority("ADMIN");
			                   authConfig.requestMatchers(HttpMethod.POST, "/customers/**").hasAnyAuthority("ADMIN");
			
			                   authConfig.requestMatchers(HttpMethod.GET, "/authorities").hasAnyAuthority("ADMIN");
			                   authConfig.requestMatchers(HttpMethod.POST, "/authorities").hasAnyAuthority("ADMIN");
			                   authConfig.anyRequest().authenticated();
		                   })
		                   .formLogin(login -> {
			                   // Employee Login Configuration
			                   login.loginPage("/login");
			                   login.defaultSuccessUrl("/home");
			                   login.failureUrl("/login-error");
			                   login.failureHandler(customAuthenticationFailureHandler);
		                   })
		                   .logout(logout -> {
			                   logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
			                   logout.logoutSuccessUrl("/");
			                   logout.deleteCookies("JSESSIONID");
			                   logout.invalidateHttpSession(true);
		                   })
		                   .sessionManagement((sessionManagement) -> sessionManagement.sessionConcurrency((sessionConcurrency) ->
				                                                                                                  sessionConcurrency.maximumSessions(1).expiredUrl("/login")))
		                   .httpBasic(Customizer.withDefaults()).build();
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(securityUserDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		authenticationProvider.setHideUserNotFoundExceptions(false);
		return authenticationProvider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class)
		           .authenticationProvider(authenticationProvider())
		           .build();
	}
	
	// PasswordEncoder
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	SecurityEvaluationContextExtension securityEvaluationContextExtension() {
		return new SecurityEvaluationContextExtension();
	}
	
	@Bean
	ApplicationListener<AuthenticationSuccessEvent> successEvent() {
		return event -> {
			log.info("Login Success: " + event.getAuthentication().getClass().getSimpleName() + " - " + event.getAuthentication().getName());
			
		};
	}
	
	@Bean
	ApplicationListener<AuthenticationFailureBadCredentialsEvent> failureEvent() {
		return event -> {
			log.error("Login Failed: " + event.getAuthentication().getClass().getSimpleName() + " - " + event.getAuthentication().getName());
		};
	}
	
	@Bean
	public WebMvcConfigurer corsConfiguration() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry corsRegistry) {
				corsRegistry.addMapping("/**").allowedOrigins("http://localhost:8080");
			}
		};
	}
	
	@Bean
	public SessionRegistry sessionRegistry() {
		return new CustomSessionRegistry();
	}
	
	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
		return new HttpSessionEventPublisher();
	}
	
}