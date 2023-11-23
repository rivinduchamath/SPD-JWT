package com.acloudofgoods.security.service.impl;

import com.acloudofgoods.security.dto.AuthorityDto;
import com.acloudofgoods.security.entity.Role;
import com.acloudofgoods.security.entity.Role;
import com.acloudofgoods.security.repository.AuthorityRepository;
import com.acloudofgoods.security.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final AuthorityRepository authorityRepository;

    @Override
    public Role saveRole(AuthorityDto authorityDto) {
        Role authority = new Role();
        if(authorityRepository.findByAuthorityName(authorityDto.getRoleName()).isEmpty()) {
            authority.setRoleName(authorityDto.getRoleName());
            authority.setPermission(authorityDto.getPermission());
            return authorityRepository.save(authority);
        }else {
            return null;
        }
    }
}
