package com.designre.blog.interceptor;

import com.designre.blog.util.ErrorCode;
import com.designre.blog.util.FameUtils;
import com.designre.blog.util.JwtUtil;
import com.designre.blog.util.RestResponse;
import com.designre.blog.exception.TipException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = FameUtils.getJwtHeaderToken();
        if (StringUtils.hasText(jwtToken)) {
            String username;
            try {
                if (JwtUtil.isTokenExpired(jwtToken)) {
                    FameUtils.writeJsonResponse(RestResponse.fail(ErrorCode.LOGIN_EXPIRE.getCode(), ErrorCode.LOGIN_EXPIRE.getMsg()), resp);
                    return;
                }

                username = JwtUtil.getSubject(jwtToken);
            } catch (JwtException e) {
                log.error(e.getMessage(), e);
                FameUtils.writeJsonResponse(RestResponse.fail(ErrorCode.NOT_LOGIN.getCode(), ErrorCode.NOT_LOGIN.getMsg()), resp);
                return;
            }
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (null == userDetails) {
                throw new TipException("User does not exist");
            }
            List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(JwtUtil.getRoles(jwtToken));

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(token);
        }

        filterChain.doFilter(req, resp);
    }
}
