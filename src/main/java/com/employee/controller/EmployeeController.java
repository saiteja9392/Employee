package com.employee.controller;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.employee.entity.Employee;
import com.employee.entity.EmployeeLogin;
import com.employee.model.Response;
import com.employee.repository.EmployeeLoginRepo;
import com.employee.repository.EmployeeRepo;
import com.employee.util.Utils;

@RestController
@RequestMapping("/Employee")
public class EmployeeController {
	
	@Autowired
	EmployeeRepo eRepo;
	
	@Autowired
	EmployeeLoginRepo eLoginRepo;
	
	@GetMapping(value = "/GetEmployee/{id}", produces = MediaType.APPLICATION_XML_VALUE)
	@PreAuthorize(value = "hasRole('ROLE_ADMIN')")
	public Employee getEmployee(@PathVariable("id") String employeeid) throws Exception {
		
		ThreadMXBean bean = ManagementFactory.getThreadMXBean();
		System.out.println("Thread Count = " + bean.getThreadCount());
		
		Optional<Employee> employee = eRepo.findById(employeeid);
		
		if(!employee.isPresent())
			throw new Exception("Employee Details Not Found");
		
		return employee.get();
	}
	
	@PostMapping("/AddEmployee")
	@PreAuthorize(value = "hasAuthority('employee:write')")
	public ResponseEntity<Employee> addEmployee(@RequestBody Employee e) throws Exception{
		
		Optional<Employee> employee = eRepo.findById(e.getEmployeeid());
		
		if(employee.isPresent()) {
			
			throw new Exception("Employee already exsists with that username");
		}
		
		return new ResponseEntity<Employee>(eRepo.save(e), HttpStatus.CREATED);
	}
	
	@DeleteMapping("/DeleteEmployee/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	@PreAuthorize(value = "hasAuthority('employee:write')")
	public Response deleteEmployee(@PathVariable("id") String employeeid) throws Exception {
		
		Response resp = new Response();
		
		Optional<Employee> employee = eRepo.findById(employeeid);
		
		if(!employee.isPresent())
			throw new Exception("Employee Details Not Found");
		
		else{
			
			eRepo.delete(employee.get());
			
			EmployeeLogin employeeLogin = eLoginRepo.getById(employeeid);
			
			if(employeeLogin != null)
				eLoginRepo.delete(employeeLogin);
			
			resp.setMessage("Employee Records Has Been Deleted");
			resp.setDate(new Date());
		}
		
		return resp;
			
	}
	
	@PutMapping("/UpdateEmployee")
	@ResponseStatus(code = HttpStatus.OK)
	@PreAuthorize(value = "hasAuthority('employee:write')")
	public Response updateEmployee(@RequestBody Employee employee) throws Exception {
		
		Response resp = new Response();
		
		Optional<Employee> emp = eRepo.findById(employee.getEmployeeid());
		
		if(!emp.isPresent())
			throw new Exception("Employee Details Not Found");
		
		employee.setProfilelastmodified(Utils.getCurrentTimeStamp());
		eRepo.save(employee);
		
		resp.setMessage("Employee Record Has Been Updated Successfully");
		resp.setDate(new Date());
		
		return resp;
		
	}

}
