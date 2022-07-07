package com.employee.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import com.employee.auth.ApplicationUserService;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class EmployeeSecurity extends WebSecurityConfigurerAdapter{

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private ApplicationUserService applicationUserService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
			.csrf().disable()
		//	.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		//	.and()
		//	.addFilter(new JWTCredentialsFilter(authenticationManager()))
			.authorizeRequests()
			.antMatchers("/").permitAll()
		//	.antMatchers("/Employee/GetEmployee/*").hasRole(Roles.EMPLOYEE.name())
		//	.antMatchers(HttpMethod.DELETE,"/Employee/*").hasAuthority(Permissions.EMPLOYEE_WRITE.getPermissions())
		//	.antMatchers(HttpMethod.POST,"/Employee/*").hasAuthority(Permissions.EMPLOYEE_WRITE.getPermissions())
			.anyRequest()
			.authenticated()
			.and()				//UnComment Below two lines for Basic Auth and Commment the sessionManagement & addFilter in the above
			.httpBasic();
	}
	
	//Below is In Memory User Implementation
	
	@Bean
	@Override
	protected UserDetailsService userDetailsService() {
	  
		  UserDetails adminrole = User.builder().username("admin")
				  								.password(passwordEncoder.encode("admin")) //.roles(Roles.ADMIN.name())
				  								.authorities(Roles.ADMIN.getGrantedAuthorities()) .build();
		  
		  UserDetails employeerole = User.builder().username("employee")
												  .password(passwordEncoder.encode("employee")) //.roles(Roles.EMPLOYEE.name())
												  .authorities(Roles.EMPLOYEE.getGrantedAuthorities()) .build();
												  
		  return new InMemoryUserDetailsManager(adminrole,employeerole); 
		  
	}
	 
	
	//Below is DB User Implementation
	/*
	 * @Bean 
	 * public DaoAuthenticationProvider doAuthenticationProvider() {
	 * 
	 * DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
	 * provider.setPasswordEncoder(passwordEncoder);
	 * provider.setUserDetailsService(applicationUserService);
	 * 
	 * return provider;
	 * 
	 * }
	 * 
	 * @Override protected void configure(AuthenticationManagerBuilder auth) throws
	 * Exception {
	 * 
	 * auth.authenticationProvider(doAuthenticationProvider()); }
	 */
}
