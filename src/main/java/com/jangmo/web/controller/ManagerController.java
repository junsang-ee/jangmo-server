package com.jangmo.web.controller;

import com.jangmo.web.controller.base.BaseController;
import com.jangmo.web.model.dto.response.common.ApiSuccessResponse;
import com.jangmo.web.service.admin.UserManagementService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/manager")
@RestController
public class ManagerController extends BaseController {
    private final UserManagementService userManagementService;

    @PatchMapping("/mercenary/{mercenaryId}/approve")
    public ApiSuccessResponse<Object> approve(@PathVariable String mercenaryId) {
        userManagementService.approveMercenary(mercenaryId);
        return wrap(null);
    }




}
