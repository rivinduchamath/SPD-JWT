package com.acloudofgoods.security.controller.user;

import com.acloudofgoods.security.dto.RegisterRequestDto;
import com.acloudofgoods.security.dto.response.common.ResponseModelDto;
import com.acloudofgoods.security.service.AuthenticationService;
import com.acloudofgoods.security.service.impl.auth.UserService;
import com.acloudofgoods.security.dto.ChangePasswordRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    private final AuthenticationService authenticationService;

    @PatchMapping public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequestDto request,
                                                          Principal connectedUser
    ) {
        service.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }
    // Register As a normal User
    @PostMapping(value = "${server.servlet.user-register}")
    public ResponseEntity<ResponseModelDto> registerAsUser(@RequestBody RegisterRequestDto request) {
        return ResponseEntity.ok(authenticationService.registerAsUser(request));
    }
}
