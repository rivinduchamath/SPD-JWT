package com.acloudofgoods.security.dto.serverstart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityDto {
    private Long id;
    private String roleName;
    private Set<String> permission;
}
