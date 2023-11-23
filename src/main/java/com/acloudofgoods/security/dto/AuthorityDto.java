package com.acloudofgoods.security.dto;

import com.acloudofgoods.security.annotations.NotEmptyOrNull;
import lombok.*;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorityDto {

  // Used Model Mapper. So, Attributes Should be Equals in Entity and Dto both Sides.
  private Long  id;
  @NotEmptyOrNull
  private String roleName;
  private Set<String> permission;
}
