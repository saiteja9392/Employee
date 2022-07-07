package com.employee.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.text.ParseException;
import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.employee.entity.Employee;
import com.employee.entity.EmployeeLogin;
import com.employee.model.Login;
import com.employee.model.ResetPassword;
import com.employee.model.Response;
import com.employee.repository.EmployeeLoginRepo;
import com.employee.repository.EmployeeRepo;
import com.employee.util.Utils;

@RestController
@RequestMapping("/EmployeeLogin")
public class EmployeeLoginController {
	
	@Autowired
	EmployeeRepo eRepo;
	
	@Autowired
	EmployeeLoginRepo eLoginRepo;

	@PostMapping("/AddEmployeeLogin")
	@ResponseStatus(code = HttpStatus.CREATED)
	public EmployeeLogin createEmployeeLogin(@RequestBody EmployeeLogin employee) throws Exception {
		
		Optional<Employee> emp = eRepo.findById(employee.getUsername());
		
		if(!emp.isPresent()) {
			
			throw new Exception("Employee Details Not Found");
		}
		
		Optional<EmployeeLogin> empLogin = eLoginRepo.findById(employee.getUsername());
		
		if(!empLogin.isPresent()) {
			employee.setPwdcreatedate(Utils.getCurrentTimeStamp());
			employee.setStatus("A");
			employee.setPwdexpires(10);
			employee.setAttempts(0);
			
			eLoginRepo.save(employee);
		}
		
		else {
			
			throw new Exception("EmployeeLogin already exsists with that username");
		}
		
		return employee;
	}
	
	@PostMapping("/ResetPassword")
	@Transactional
	@ResponseStatus(code = HttpStatus.OK)
	public Response resetPassword(@RequestBody ResetPassword reset) {
		
		EmployeeLogin eLogin = new EmployeeLogin();
		
		Optional<EmployeeLogin> employee = eLoginRepo.findById(reset.getUsername());
		
		Response resp = new Response();
		
		if(!employee.isPresent()) {
			
			resp.setMessage("EmployeeLogin Details Not Found");
			resp.setDate(new Date());
		}
		
		else {
			if(employee.get().getPassword().contentEquals(reset.getNewpassword())) {
				
				resp.setMessage("Old and New Passwords are Same, Please Enter Different Password");
				resp.setDate(new Date());
			}
			
			if(!reset.getNewpassword().contentEquals(reset.getConfirmpassword())){
				
				resp.setMessage("Password Mis-Match");
				resp.setDate(new Date());
			}
			else {
				
				eLogin = employee.get();
				
				eLogin.setPassword(reset.getNewpassword());
				eLogin.setStatus("A");
				eLogin.setPwdcreatedate(Utils.getCurrentTimeStamp());
				
				eLoginRepo.save(eLogin);
				
				resp.setMessage("Password Has Been Updated");
				resp.setDate(new Date());
			}
		}
		
		return resp;
	}
	
	@PostMapping("/UnlockAccount/{id}")
	@Transactional
	@ResponseStatus(code = HttpStatus.OK)
	public EntityModel<Response> unlockAccount(@PathVariable("id") String username) {
		
		EmployeeLogin eLogin = new EmployeeLogin();
		
		Optional<EmployeeLogin> employee = eLoginRepo.findById(username);
		
		Response resp = new Response();
		
		EntityModel<Response> model = EntityModel.of(resp);
		
		ResetPassword reset = new ResetPassword();
		
		
		if(!employee.isPresent()) {
			
			resp.setMessage("EmployeeLogin Details Not Found");
			resp.setDate(new Date());
		}
		
		else {
			
			if(employee.get().getStatus().contentEquals("L") || employee.get().getStatus().contentEquals("A")) {
				
				eLogin = employee.get();
				
				eLogin.setStatus("A");
				eLogin.setAttempts(0);
				
				eLoginRepo.save(eLogin);
				
				resp.setMessage("Account Has Been Unlocked");
				resp.setDate(new Date());
			}
			
			if(employee.get().getStatus().contentEquals("S")) {
				
				resp.setMessage("Account Has Been Suspended, Please Reset The Password");
				resp.setDate(new Date());
				
				WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).resetPassword(reset));
				model.add(linkTo.withRel("password-reset"));
			}
		}
		
		return model;
		
	}
	
	@PostMapping("/Login")
	@Transactional
	@ResponseStatus(code = HttpStatus.OK)
	public EntityModel<Response> employeeLogin(@RequestBody Login login) throws ParseException {
		
		EmployeeLogin elogin = new EmployeeLogin();
		
		Optional<EmployeeLogin> employee = eLoginRepo.findById(login.getUsername());
		
		String[] date = employee.get().getPwdcreatedate().split(" ");
		
		int days = (int) Utils.getDays(date[0]);
		System.out.println(days);
		
		Response resp = new Response();
		
		EntityModel<Response> model = EntityModel.of(resp);
		
		ResetPassword reset = new ResetPassword();
		
		if(!employee.isPresent()) {
			
			resp.setMessage("EmployeeLogin Details Not Found");
			resp.setDate(new Date());
		}
		
		else {
			
			elogin = employee.get();
			
			if (elogin.getPwdexpires() != days) {

				elogin.setPwdexpires(days);
				elogin = eLoginRepo.save(elogin);
			}
			
			if (!elogin.getPassword().contentEquals(login.getPassword())
					&& elogin.getStatus().contentEquals("A")) {

				if (elogin.getAttempts() < 3 && elogin.getAttempts() == 2) {
					
					elogin.setStatus("L");
					elogin.setAttempts(elogin.getAttempts()+1);
					
					elogin = eLoginRepo.save(elogin);
				}

				if (elogin.getAttempts() < 3) {
					
					elogin.setAttempts(elogin.getAttempts()+1);
					elogin = eLoginRepo.save(elogin);
				}

				resp.setMessage("Invalid Username/Password");
				resp.setDate(new Date());

			}

			if (!elogin.getPassword().contentEquals(login.getPassword()) && elogin.getAttempts() >= 3
					&& elogin.getStatus().contentEquals("L")) {

				resp.setMessage("User Account Has been Locked Due to Wrong Attempts, Please Reset Password");
				resp.setDate(new Date());
				
				WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).resetPassword(reset));
				model.add(linkTo.withRel("password-reset"));
			}

			if (elogin.getPassword().contentEquals(login.getPassword()) && elogin.getAttempts() >= 3
					&& elogin.getStatus().contentEquals("L")) {

				resp.setMessage("User Account Has Been Locked");
				resp.setDate(new Date());
				
				WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).unlockAccount(employee.get().getUsername()));
				model.add(linkTo.withRel("unlock-account"));
			}

			if (elogin.getPassword().contentEquals(login.getPassword()) && elogin.getAttempts() <= 3
					&& elogin.getPwdexpires() < 0 && elogin.getStatus().contentEquals("S")) {
				
				resp.setMessage("User Account Has Been Suspended, Please Reset Password");
				resp.setDate(new Date());
				
				WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).resetPassword(reset));
				model.add(linkTo.withRel("password-reset"));
			}

			if (elogin.getPassword().contentEquals(login.getPassword()) && elogin.getAttempts() <= 3
					&& elogin.getStatus().contentEquals("A")) {
				
				if (elogin.getPwdexpires() >= 10) {

					elogin.setStatus("S");
					elogin.setPwdexpires(0);
					elogin = eLoginRepo.save(elogin);
					
					resp.setMessage("Password Expired Please Reset");
					resp.setDate(new Date());
					
					WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).resetPassword(reset));
					model.add(linkTo.withRel("password-reset"));
				}
				
				else {

					if (elogin.getPwdexpires() < 10)
						resp.setWarning("Password Expires in " + (elogin.getPwdexpires() - days) + " Days");

					resp.setMessage("Login Successfull");
					resp.setDate(new Date());
					
					elogin.setLastlogin(Utils.getCurrentTimeStamp());
					elogin.setAttempts(0);
					
					elogin = eLoginRepo.save(elogin);
				}
			}

		}
		
		return model;
	}
}
