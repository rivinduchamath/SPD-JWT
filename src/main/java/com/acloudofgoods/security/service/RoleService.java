package com.acloudofgoods.security.service;

import com.acloudofgoods.security.dto.AuthorityDto;
import com.acloudofgoods.security.entity.Role;
import com.acloudofgoods.security.entity.Role;

public interface RoleService {
    Role saveRole(AuthorityDto authorityDto);
}
