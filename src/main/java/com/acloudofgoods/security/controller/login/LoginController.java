package com.acloudofgoods.security.controller.login;

import com.acloudofgoods.security.dto.AuthenticationRequestDto;
import com.acloudofgoods.security.dto.response.AuthenticationResponseDto;
import com.acloudofgoods.security.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationService authenticationService;

    @PostMapping(value = "${server.servlet.user-login}")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody AuthenticationRequestDto request) {
        long startTime = System.currentTimeMillis();
        AuthenticationResponseDto authenticate = authenticationService.authenticate(request);
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        log.info("LOG:: getAuthorities Controller Elapsed Time: " + elapsedTime + " milliseconds");
        return ResponseEntity.ok(authenticate);
    }

    @PostMapping(value = "${server.servlet.refresh-login}")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authenticationService.refreshToken(request, response);
    }


}
