package com.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.employee.entity.EmployeeLogin;

@Repository
public interface EmployeeLoginRepo extends JpaRepository<EmployeeLogin, String>{

	@Modifying
	@Query(value = "update employeelogin set lastlogin = ?2 where username = ?1", nativeQuery = true)
	void updateLastLogin(String username, String lastLogin);

	@Modifying
	@Query(value = "update employeelogin set attempts = ?2 where username = ?1", nativeQuery = true)
	void updateAttempts(String username, int count);

	@Modifying
	@Query(value = "update employeelogin set status = ?2 where username = ?1", nativeQuery = true)
	void updateStatus(String username, String string);

	@Modifying
	@Query(value = "update employeelogin set pwdexpires = ?2 where username = ?1", nativeQuery = true)
	void updatePasswordExpireDays(String username, int days);

	@Modifying
	@Query(value = "update employeelogin set password = ?2 where username = ?1", nativeQuery = true)
	void updatePassword(String username, String newpassword);

}
