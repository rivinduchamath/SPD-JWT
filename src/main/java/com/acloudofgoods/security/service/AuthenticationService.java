package com.acloudofgoods.security.service;

import com.acloudofgoods.security.dto.AuthenticationRequestDto;
import com.acloudofgoods.security.dto.AuthorityDto;
import com.acloudofgoods.security.dto.RegisterRequestDto;
import com.acloudofgoods.security.dto.response.AuthenticationResponseDto;
import com.acloudofgoods.security.dto.response.common.ResponseModelDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthenticationService {
    ResponseModelDto registerAsUser(RegisterRequestDto request);
    void registerAdminWhenServerStart(RegisterRequestDto request);

    ResponseModelDto authorityRegister(AuthorityDto request);

    AuthenticationResponseDto authenticate(AuthenticationRequestDto request);

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

    ResponseModelDto registerAsAdmin(RegisterRequestDto request);
}
