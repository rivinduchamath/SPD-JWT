package com.acloudofgoods.security.repository;

import java.util.Optional;

import com.acloudofgoods.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

}
