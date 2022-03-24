package com.designre.blog.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.designre.blog.exception.LoginExpireException;
import com.designre.blog.exception.NotFoundException;
import com.designre.blog.exception.TipException;
import com.designre.blog.model.dto.TokenDto;
import com.designre.blog.model.dto.UserDetailsDto;
import com.designre.blog.model.param.RefreshTokenParam;
import com.designre.blog.model.param.ResetUserParam;
import com.designre.blog.util.FameUtils;
import com.designre.blog.util.JwtUtil;
import com.designre.blog.mapper.UserMapper;
import com.designre.blog.model.entity.User;
import com.designre.blog.model.param.LoginParam;
import com.designre.blog.model.param.ResetPasswordParam;
import com.designre.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired, @Lazy})
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService, UserDetailsService {

    private static final String ADMIN_ROLE = "ADMIN";
    private static final int TOKEN_REFRESH_SECOND = 60 * 10;

    private TokenDto createTokenDto(UserDetails userDetails) {
        TokenDto tokenDto = new TokenDto();
        final String username = userDetails.getUsername();
        final Collection<? extends GrantedAuthority> authorityList = userDetails.getAuthorities();
        String roles = CollUtil.join(AuthorityUtils.authorityListToSet(authorityList), ",");
        String token = JwtUtil.generateToken(username, roles, null);
        tokenDto.setToken(token);
        String refreshToken = JwtUtil.generateRefreshToken(username, roles, null);
        tokenDto.setRefreshToken(refreshToken);

        return tokenDto;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public TokenDto login(LoginParam param) {
        String username = param.getUsername();
        String password = param.getPassword();

        User user = lambdaQuery()
                .eq(User::getUsername, username)
                .or()
                .eq(User::getEmail, username)
                .one();
        if (null == user) {
            throw new TipException("Username or password is incorrect");
        }
        String md5 = FameUtils.getMd5(password);
        if (!md5.equals(user.getPasswordMd5())) {
            throw new TipException("Username or password is incorrect");
        }
        user.setLogged(new Date());
        updateById(user);

        List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(ADMIN_ROLE);
        UserDetailsDto userDetailsDto = createUserDetails(user, authorityList);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetailsDto, null, userDetailsDto.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        return createTokenDto(userDetailsDto);
    }

    @Override
    public User getCurrentUser() {
        final User user = FameUtils.getLoginUser();
        User currentUser = new User();
        FameUtils.copyPropertiesIgnoreNull(user, currentUser);
        currentUser.setPasswordMd5(null);
        return currentUser;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void resetPassword(Integer id, ResetPasswordParam param) {
        User user = getById(id);
        if (null == user) {
            throw new NotFoundException(User.class);
        }

        if (!user.getPasswordMd5().equals(FameUtils.getMd5(param.getOldPassword()))) {
            throw new TipException("The original password is wrong");
        }

        user.setPasswordMd5(FameUtils.getMd5(param.getNewPassword()));
        updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void resetUser(Integer id, ResetUserParam param) {
        User user = getById(id);
        if (null == user) {
            throw new NotFoundException(User.class);
        }

        user.setUsername(param.getUsername());
        user.setEmail(param.getEmail());
        updateById(user);
    }

    @Override
    public TokenDto refreshToken(RefreshTokenParam param) {
        String refreshToken = param.getRefreshToken();

        if (JwtUtil.isTokenExpired(refreshToken)) {
            throw new LoginExpireException();
        }

        Date created = JwtUtil.getCreated(refreshToken);
        Date refreshDate = new Date();
        if (refreshDate.after(created) && refreshDate.before(DateUtil.offsetSecond(created, TOKEN_REFRESH_SECOND))) {
            String jwtToken = FameUtils.getJwtHeaderToken();
            TokenDto tokenDto = new TokenDto();
            tokenDto.setToken(jwtToken);
            tokenDto.setRefreshToken(refreshToken);
            return tokenDto;
        }

        String username = JwtUtil.getSubject(refreshToken);
        UserDetails userDetails = loadUserByUsername(username);
        if (null == userDetails) {
            throw new TipException("用户不存在");
        }

        return createTokenDto(userDetails);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = lambdaQuery()
                .eq(User::getUsername, username)
                .or()
                .eq(User::getEmail, username)
                .one();
        if (null == user) {
            throw new TipException("User does not exist");
        }

        List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(ADMIN_ROLE);
        return createUserDetails(user, authorityList);
    }

    private UserDetailsDto createUserDetails(User user, Collection<? extends GrantedAuthority> authorityList) {
        return new UserDetailsDto(user, authorityList);
    }
}
