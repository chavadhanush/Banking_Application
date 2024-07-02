package com.ltp.banking_application.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.ltp.banking_application.entity.User;


public interface UserRepository extends CrudRepository<User, Long> {
	 Optional<User> findByUsername(String username);
	// boolean existsByUsername(String username);
}