package com.jangmo.web.controller;

import com.jangmo.web.controller.base.BaseController;
import com.jangmo.web.model.dto.request.MemberSignupRequest;
import com.jangmo.web.model.dto.response.CityListResponse;
import com.jangmo.web.model.dto.response.DistrictListResponse;
import com.jangmo.web.model.dto.response.common.ApiSuccessResponse;
import com.jangmo.web.model.entity.MemberEntity;
import com.jangmo.web.service.AuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/api/auth")
@RestController
public class AuthController extends BaseController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ApiSuccessResponse<MemberEntity> signup(@RequestBody MemberSignupRequest signup) {
        return wrap(authService.signup(signup));
    }

    @GetMapping("/signup/cities")
    public ApiSuccessResponse<List<CityListResponse>> cities() {
        return wrap(authService.getCities());
    }

    @GetMapping("/signup/cities/{cityName}/districts")
    public ApiSuccessResponse<List<DistrictListResponse>> districts(@PathVariable String cityName) {
        return wrap(authService.getDistrictsByCityName(cityName));
    }


}
