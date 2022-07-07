package com.employee.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "employeelogin")
public class EmployeeLogin {

	@Id
	private String username;
	private String password;
	private String pwdcreatedate;
	private Integer attempts;
	private String lastlogin;
	private String status;
	private Integer pwdexpires;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPwdcreatedate() {
		return pwdcreatedate;
	}
	public void setPwdcreatedate(String pwdcreatedate) {
		this.pwdcreatedate = pwdcreatedate;
	}
	public Integer getAttempts() {
		return attempts;
	}
	public void setAttempts(Integer attempts) {
		this.attempts = attempts;
	}
	public String getLastlogin() {
		return lastlogin;
	}
	public void setLastlogin(String lastlogin) {
		this.lastlogin = lastlogin;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getPwdexpires() {
		return pwdexpires;
	}
	public void setPwdexpires(Integer pwdexpires) {
		this.pwdexpires = pwdexpires;
	}
}
