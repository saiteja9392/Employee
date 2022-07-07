package com.employee.security;

public enum Permissions {

	EMPLOYEE_READ("employee:read"),
	EMPLOYEE_WRITE("employee:write");
	
	private String permissions;
	
	private Permissions(String permission) {
		
		this.permissions = permission;
	}

	public String getPermissions() {
		return permissions;
	}
	
}
