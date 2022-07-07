package com.employee.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;

@Entity
public class Employee {

	@Id
	@NotNull
	private String employeeid;
	private String employeename;
	private Integer employeeage;
	private String employeeemailid;
	private String dob;
	private String doj;
	
	@JsonIgnore
	private String profilelastmodified;
	
	public String getEmployeeid() {
		return employeeid;
	}
	public void setEmployeeid(String employeeid) {
		this.employeeid = employeeid;
	}
	public String getEmployeename() {
		return employeename;
	}
	public void setEmployeename(String employeename) {
		this.employeename = employeename;
	}
	public Integer getEmployeeage() {
		return employeeage;
	}
	public void setEmployeeage(Integer employeeage) {
		this.employeeage = employeeage;
	}
	public String getEmployeeemailid() {
		return employeeemailid;
	}
	public void setEmployeeemailid(String employeeemailid) {
		this.employeeemailid = employeeemailid;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getDoj() {
		return doj;
	}
	public void setDoj(String doj) {
		this.doj = doj;
	}
	public String getProfilelastmodified() {
		return profilelastmodified;
	}
	public void setProfilelastmodified(String profilelastmodified) {
		this.profilelastmodified = profilelastmodified;
	}

}
