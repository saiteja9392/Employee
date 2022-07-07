package com.employee.security;

import static com.employee.security.Permissions.EMPLOYEE_READ;
import static com.employee.security.Permissions.EMPLOYEE_WRITE;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.google.common.collect.Sets;

public enum Roles {

	ADMIN(Sets.newHashSet(EMPLOYEE_READ,EMPLOYEE_WRITE)),
	EMPLOYEE(Sets.newHashSet());
	
	private final Set<Permissions> permissions;

	private Roles(Set<Permissions> permissions) {
		this.permissions = permissions;
	}

	public Set<Permissions> getPermissions() {
		return permissions;
	}
	
	public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
		
		Set<SimpleGrantedAuthority> permission = getPermissions().stream()
						.map(permissions -> new SimpleGrantedAuthority(permissions.getPermissions()))
						.collect(Collectors.toSet());
		
		permission.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
		
		return permission;
	}
}
