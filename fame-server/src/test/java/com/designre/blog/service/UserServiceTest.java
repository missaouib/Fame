package com.designre.blog.service;

import com.designre.blog.model.dto.TokenDto;
import com.designre.blog.model.param.ResetUserParam;
import com.designre.blog.util.JwtUtil;
import com.designre.blog.BaseTest;
import com.designre.blog.model.param.LoginParam;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zzzzbw
 * @since 2019/6/24 11:32
 */
@Slf4j
public class UserServiceTest extends BaseTest {

    @Autowired
    private UserService userService;

    @Test
    public void login() {
        LoginParam loginParam = new LoginParam();
        loginParam.setUsername("fame");
        loginParam.setPassword("123456");
        TokenDto tokenDto = userService.login(loginParam);
        log.info("{}", tokenDto);
    }

    @Test
    public void resetUser() {
        userService.resetUser(1, new ResetUserParam());
    }

    @Test
    public void generateToken() {
        final String token = JwtUtil.generateToken("fame", "ADMIN", null);
        log.info("{}", token);
        final Claims claims = JwtUtil.getClaims(token);
        log.info("{}", claims);


    }

}
