package com.jangmo.web.controller;

import com.jangmo.web.controller.base.BaseController;
import com.jangmo.web.model.dto.request.MemberSignUpRequest;
import com.jangmo.web.model.dto.request.MercenaryRegistrationRequest;
import com.jangmo.web.model.dto.response.CityListResponse;
import com.jangmo.web.model.dto.response.DistrictListResponse;
import com.jangmo.web.model.dto.response.common.ApiSuccessResponse;
import com.jangmo.web.model.entity.MemberEntity;
import com.jangmo.web.model.entity.MercenaryEntity;
import com.jangmo.web.model.entity.UserEntity;
import com.jangmo.web.service.AuthService;

import com.jangmo.web.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/api/auth")
@RestController
public class AuthController extends BaseController {

    private final AuthService authService;

    private final UserService userService;

    @PostMapping("/signup/member")
    public ApiSuccessResponse<MemberEntity> signupMember(@RequestBody MemberSignUpRequest signup) {
        return wrap(authService.signupMember(signup));
    }

    @PostMapping("/signup/mercenary")
    public ApiSuccessResponse<MercenaryEntity> signupMercenary(@RequestBody MercenaryRegistrationRequest registration) {
        return wrap(authService.registerMercenary(registration));
    }

    @GetMapping("/signup/cities")
    public ApiSuccessResponse<List<CityListResponse>> cities() {
        return wrap(authService.getCities());
    }

    @GetMapping("/signup/cities/{cityName}/districts")
    public ApiSuccessResponse<List<DistrictListResponse>> districts(@PathVariable String cityName) {
        return wrap(authService.getDistrictsByCityName(cityName));
    }

    @GetMapping("/signup/user/test")
    public ApiSuccessResponse<UserEntity> getUserTest() {
        return wrap(userService.getUser());
    }


}
