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
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
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
@EnableMethodSecurity
@Slf4j
public class SecurityConfig {
	
	@Autowired
	CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
	
	@Autowired
	CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
	
	@Autowired
	SecurityUserDetailsService securityUserDetailsService;
	
	// Authorization
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
				.cors(Customizer.withDefaults())
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(authConfig -> {
					
					// Publicly accessible routes
					authConfig.requestMatchers("/css/**", "/js/**", "/images/**").permitAll();
					authConfig.requestMatchers(HttpMethod.GET, "/pricing", "/career/**", "/", "/login", "/login-error", "/error", "/logout", "/access-denied")
					          .permitAll();
					authConfig.requestMatchers(HttpMethod.POST, "/", "/login-error", "/error", "/logout").permitAll();
					
					// Routes for all authenticated users
					authConfig.requestMatchers(HttpMethod.GET, "/home", "/profile").hasAnyAuthority("ADMIN", "SENIOR", "JUNIOR", "CUSTOMER");
					
					// Reset Password Routes
					authConfig.requestMatchers(HttpMethod.POST, "/employees/{employeeId}/reset-password").hasAnyAuthority("ADMIN", "SENIOR", "JUNIOR");
					authConfig.requestMatchers(HttpMethod.POST, "/customers/{customerId}/reset-password").hasAuthority("CUSTOMER");
					
					// Employee routes
					authConfig.requestMatchers(HttpMethod.GET, "/employees/**").hasAnyAuthority("ADMIN", "SENIOR");
					authConfig.requestMatchers(HttpMethod.POST, "/employees/**").hasAnyAuthority("ADMIN", "SENIOR");
					
					// Customer routes
					authConfig.requestMatchers(HttpMethod.GET, "/customers/**").hasAnyAuthority("ADMIN", "SENIOR", "JUNIOR");
					authConfig.requestMatchers(HttpMethod.POST, "/customers/**").hasAnyAuthority("ADMIN", "SENIOR", "JUNIOR");
					
					// Services routes
					authConfig.requestMatchers(HttpMethod.GET, "/services/**").hasAuthority("CUSTOMER");
					
					authConfig.anyRequest().authenticated();
				})
				.formLogin(login -> login.
						loginPage("/login")
						.successHandler(customAuthenticationSuccessHandler)
						.failureUrl("/login-error")
						.failureHandler(customAuthenticationFailureHandler))
				.logout(logout -> logout.
						logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
						.logoutSuccessUrl("/")
						.deleteCookies("JSESSIONID")
						.invalidateHttpSession(true))
				.exceptionHandling(exception -> exception
						.accessDeniedPage("/access-denied"))
				.sessionManagement(sessionManagement -> sessionManagement
						.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
						.maximumSessions(1)
						.expiredUrl("/login")
						.sessionRegistry(sessionRegistry()))
				.httpBasic(Customizer.withDefaults())
				.build();
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
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
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