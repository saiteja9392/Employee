package com.employee.auth;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.employee.security.Roles;
import com.google.common.collect.Lists;

@Repository("fake")
public class ApplicationUserDAOImpl implements ApplicationUserDAO{

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public Optional<ApplicationUser> selectApplicationUserByUserName(String username) {
		
		return getApplicationUsers()
				.stream().filter(applicationUser -> username.equals(applicationUser.getUsername()))
				.findFirst();
	}

	private List<ApplicationUser> getApplicationUsers(){
		
		List<ApplicationUser> applicationUsers = Lists.newArrayList(
			
				new ApplicationUser("admin",passwordEncoder.encode("admin"), 
						Roles.ADMIN.getGrantedAuthorities(),
						true, true, true, true),
				
				new ApplicationUser("employee",passwordEncoder.encode("employee"), 
						Roles.EMPLOYEE.getGrantedAuthorities(),
						true, true, true, true)
				
		);
		
		return applicationUsers;
	}
}
