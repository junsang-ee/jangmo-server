package com.jangmo.web.controller;

import com.jangmo.web.model.dto.request.MemberSignupRequest;
import com.jangmo.web.model.entity.MemberEntity;
import com.jangmo.web.service.AuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/api/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/member/sign-up")
    public MemberEntity signUp(@RequestBody MemberSignupRequest signup) {
        return authService.signUp(signup);
    }

}
