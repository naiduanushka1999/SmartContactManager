package com.smart.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.entities.Admin;



@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {
	@Query("select u from Admin u where u.username = :username")
	public Admin getAdminByUserName(@Param("username") String username);
	
	
	
}
