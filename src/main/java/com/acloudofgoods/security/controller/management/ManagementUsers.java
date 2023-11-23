package com.acloudofgoods.security.controller.management;

import com.acloudofgoods.security.dto.RegisterRequestDto;
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
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ManagementUsers {

    private final AuthenticationService authenticationService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "${server.servlet.admin-register}")
    public ResponseEntity<ResponseModelDto> registerAsAdmin(@RequestBody RegisterRequestDto request) {
        return org.springframework.http.ResponseEntity.ok(authenticationService.registerAsAdmin(request));
    }
}

