package com.acloudofgoods.security.controller.management.role;

import com.acloudofgoods.security.dto.AuthorityDto;
import com.acloudofgoods.security.dto.response.common.ResponseModelDto;
import com.acloudofgoods.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleManageController {
    private final AuthenticationService authenticationService;


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "${server.servlet.role-register}")
    public ResponseEntity<ResponseModelDto> roleRegister(@RequestBody AuthorityDto request) {
        log.info("LOG:: Role Register Controller");
        return ResponseEntity.ok(authenticationService.authorityRegister(request));
    }
}
