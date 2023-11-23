package com.acloudofgoods.security.service.impl;

import com.acloudofgoods.security.dto.AuthenticationRequestDto;
import com.acloudofgoods.security.dto.AuthorityDto;
import com.acloudofgoods.security.dto.RegisterRequestDto;
import com.acloudofgoods.security.dto.response.AuthenticationResponseDto;
import com.acloudofgoods.security.dto.response.common.ResponseModelDto;
import com.acloudofgoods.security.entity.Role;
import com.acloudofgoods.security.entity.Token;
import com.acloudofgoods.security.entity.User;
import com.acloudofgoods.security.enums.TokenType;
import com.acloudofgoods.security.repository.AuthorityRepository;
import com.acloudofgoods.security.repository.TokenRepository;
import com.acloudofgoods.security.repository.UserRepository;
import com.acloudofgoods.security.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static com.acloudofgoods.security.common.Utils.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceImpl jwtService;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;
    @Value("${initial.role.name.user}")
    private String roleUser;

    public void registerAdminWhenServerStart(RegisterRequestDto request) {
        var user = User.builder().firstname(request.getFirstname()).lastname(request.getLastname()).email(request.getEmail()).password(passwordEncoder.encode(request.getPassword())).roles(request.getRoles()).build();
        if (userRepository.findByEmail(request.getEmail()).isEmpty()) {
            var savedUser = userRepository.save(user);
            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            saveUserToken(savedUser, jwtToken);
            AuthenticationResponseDto.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
        }
    }

    @Override
    public ResponseModelDto registerAsAdmin(RegisterRequestDto request) {
        ResponseModelDto responseModelDto = new ResponseModelDto();
        var user = User.builder().firstname(request.getFirstname()).lastname(request.getLastname()).email(request.getEmail()).password(passwordEncoder.encode(request.getPassword())).roles(request.getRoles()).build();
        if (userRepository.findByEmail(request.getEmail()).isEmpty()) {
            var savedUser = userRepository.save(user);
            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            saveUserToken(savedUser, jwtToken);
            responseModelDto.setData(AuthenticationResponseDto.builder().accessToken(jwtToken).refreshToken(refreshToken).build());
            responseModelDto.setMessage(MESSAGE_SUCCESS);
            responseModelDto.setCode(STATUS_CODE_OK);
            responseModelDto.setDescription("Registration Task Success");
            return responseModelDto;
        } else {
            responseModelDto.setMessage(MESSAGE_FAIL);
            responseModelDto.setCode(STATUS_CODE_NOT_ACCEPTABLE);
            responseModelDto.setDescription("Email You Entered Already Registered in the Application");
            return responseModelDto;
        }
    }

    public ResponseModelDto registerAsUser(RegisterRequestDto request) {
        ResponseModelDto responseModelDto = new ResponseModelDto();
        if (userRepository.findByEmail(request.getEmail().trim()).isEmpty()) {
            Set<Role> roles = new HashSet<>();
            roles.add(new Role(2L, roleUser));
            request.setRoles(roles);
            var user = User.builder().firstname(request.getFirstname()).lastname(request.getLastname()).email(request.getEmail().trim()).password(passwordEncoder.encode(request.getPassword())).roles(request.getRoles()).build();
            var savedUser = userRepository.save(user);
            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            saveUserToken(savedUser, jwtToken);
            responseModelDto.setData(AuthenticationResponseDto.builder().accessToken(jwtToken).refreshToken(refreshToken).build());
            responseModelDto.setMessage(MESSAGE_SUCCESS);
            responseModelDto.setCode(STATUS_CODE_OK);
            responseModelDto.setDescription("Task Success");
            return responseModelDto;
        } else {
            responseModelDto.setMessage(MESSAGE_FAIL);
            responseModelDto.setCode(STATUS_CODE_NOT_ACCEPTABLE);
            responseModelDto.setDescription("Email You Entered Already Registered in the Application");
            return responseModelDto;
        }
    }

    public AuthenticationResponseDto authenticate(AuthenticationRequestDto request) {
        long startTime = System.currentTimeMillis();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        log.info("LOG:: getAuthorities Elapsed Time: " + elapsedTime + " milliseconds");
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponseDto.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder().user(user).token(jwtToken).tokenType(TokenType.BEARER).expired(false).revoked(false).build();
        tokenRepository.save(token);
    }
    public void revokeAllUserTokens(User user) {
        tokenRepository.deleteByUser(user);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.userRepository.findByEmail(userEmail).orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponseDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }


    public ResponseModelDto authorityRegister(AuthorityDto authorityDto) {
        ResponseModelDto responseModelDto = new ResponseModelDto();
        Role authority = authorityRepository.findByAuthorityId(authorityDto.getId()).orElseGet(Role::new);
        if (authorityDto.getId() == null && authorityRepository.findByAuthorityName(authorityDto.getRoleName()).isPresent()) {
            responseModelDto.setMessage("Cannot Add Role Name " + authorityDto.getRoleName());
            responseModelDto.setDescription("Role Name Should Be a Unique Value. This Roll Name Already Exists in the database");
            responseModelDto.setCode(STATUS_CODE_NOT_ACCEPTABLE);
            responseModelDto.setHttpStatus(STATUS_OK);
            return responseModelDto;
        }

        authority.setPermission(authorityDto.getPermission() != null ? authorityDto.getPermission() : authority.getPermission());
        authority.setRoleName(authorityDto.getRoleName() != null ? authorityDto.getRoleName().trim() : authority.getRoleName());
        try {
            responseModelDto.setData(convertAuthorityToDto(authorityRepository.save(authority)));
            responseModelDto.setMessage(MESSAGE_SUCCESS);
            responseModelDto.setCode(STATUS_CODE_OK);
            responseModelDto.setDescription("Task Success");
        } catch (Exception e) {
            responseModelDto.setError(e.getMessage());
            responseModelDto.setMessage(MESSAGE_FAIL);
            responseModelDto.setCode(STATUS_CODE_INTERNAL_SERVER_ERROR);
            responseModelDto.setDescription("Task Fail");
        }
        return responseModelDto;
    }

    private AuthorityDto convertAuthorityToDto(Role authority) {
        return modelMapper.map(authority, AuthorityDto.class);
    }
}
