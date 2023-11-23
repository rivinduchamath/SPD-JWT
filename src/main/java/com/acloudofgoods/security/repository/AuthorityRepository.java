package com.acloudofgoods.security.repository;

import com.acloudofgoods.security.entity.Role;
import com.acloudofgoods.security.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Role, Integer> {
    @Query("from Role a where a.id = :id")
    Optional<Role> findByAuthorityId(Long id);
    @Query("from Role a where a.roleName = :roleName")
    Optional<Role>  findByAuthorityName(String roleName);
}
