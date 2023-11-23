package com.acloudofgoods.security.dto;

import com.acloudofgoods.security.entity.Role;

import com.acloudofgoods.security.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDto {

  private String firstname;
  private String lastname;
  private String email;
  private String password;
  private Set<Role> roles;
}
