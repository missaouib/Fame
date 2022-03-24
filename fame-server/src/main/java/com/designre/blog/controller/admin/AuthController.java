package com.designre.blog.controller.admin;


import com.designre.blog.model.dto.TokenDto;
import com.designre.blog.model.param.RefreshTokenParam;
import com.designre.blog.model.param.ResetUserParam;
import com.designre.blog.util.FameUtils;
import com.designre.blog.util.RestResponse;
import com.designre.blog.model.entity.User;
import com.designre.blog.model.param.LoginParam;
import com.designre.blog.model.param.ResetPasswordParam;
import com.designre.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AuthController {

    private final UserService userService;

    @PostMapping("login")
    public RestResponse<TokenDto> login(@RequestBody @Valid LoginParam param) {
        TokenDto tokenDto = userService.login(param);
        return RestResponse.ok(tokenDto);
    }

    @PutMapping("reset/password")
    public RestResponse<RestResponse.Empty> resetPassword(@RequestBody @Valid ResetPasswordParam param) {
        userService.resetPassword(FameUtils.getLoginUserId(), param);
        return RestResponse.ok();
    }

    @PutMapping("reset/user")
    public RestResponse<RestResponse.Empty> resetUser(@RequestBody @Valid ResetUserParam param) {
        userService.resetUser(FameUtils.getLoginUserId(), param);
        return RestResponse.ok();
    }

    @GetMapping("user")
    public RestResponse<User> getUser() {
        User user = userService.getCurrentUser();
        return RestResponse.ok(user);
    }

    @PostMapping("logout")
    public RestResponse<RestResponse.Empty> logout() {
        return RestResponse.ok();
    }

    @PostMapping("refresh")
    public RestResponse<TokenDto> refresh(@RequestBody @Valid RefreshTokenParam param) {
        TokenDto tokenDto = userService.refreshToken(param);
        return RestResponse.ok(tokenDto);
    }
}
