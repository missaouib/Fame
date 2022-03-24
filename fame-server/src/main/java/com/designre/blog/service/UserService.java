package com.designre.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.designre.blog.model.dto.TokenDto;
import com.designre.blog.model.param.RefreshTokenParam;
import com.designre.blog.model.param.ResetUserParam;
import com.designre.blog.model.entity.User;
import com.designre.blog.model.param.LoginParam;
import com.designre.blog.model.param.ResetPasswordParam;

public interface UserService extends IService<User> {

    TokenDto login(LoginParam param);
    User getCurrentUser();
    void resetPassword(Integer id, ResetPasswordParam param);
    void resetUser(Integer id, ResetUserParam param);
    TokenDto refreshToken(RefreshTokenParam param);
}
