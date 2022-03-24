package com.designre.blog.config;

import com.designre.blog.interceptor.JwtAuthenticationFilter;
import com.designre.blog.util.ErrorCode;
import com.designre.blog.util.FameUtils;
import com.designre.blog.util.RestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    /**
     * URLs excluded from the security authentication
     */
    private static final String[] EXCLUDED_AUTH_PAGES = {
            "/css/**", "/js/**", "/images/**", "/webjars/**", "/**/favicon*",
            "/*.html", "/**/*.html", "/**/*.css", "/**/*.js"
    };

    private static final String LOGIN_IRL = "admin/login";

    private static final String REFRESH_TOKEN = "admin/refresh";

    private static final String LOGOUT_URL = "admin/logout";

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {

        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS)
                .antMatchers(HttpMethod.GET, EXCLUDED_AUTH_PAGES)
                .antMatchers(apiUrl(LOGIN_IRL), apiUrl(REFRESH_TOKEN))
                .requestMatchers(request -> !new AntPathRequestMatcher(apiUrl("admin/**")).matches(request));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http

                .cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .headers().frameOptions().disable()
                .and()
                .logout()
                .logoutUrl(apiUrl(LOGOUT_URL))
                .logoutSuccessHandler(logoutSuccessHandler())
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers(apiUrl("admin/**"))
                .authenticated();

        http
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler());
    }

    private String apiUrl(String url) {
        return "/api/" + url;
    }

    /**
     * JWT filter.
     * <p>
     * This must be injected through new instead of Spring, otherwise Spring will inject this Filter into the global, without being controlled by Spring Security
     *
     * @return jwtAuthenticationFilter
     */

    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(userDetailsService);
    }

    /**
     * Logout succeeded
     *
     * @return logoutSuccessHandler
     */
    public LogoutSuccessHandler logoutSuccessHandler() {
        return (request, response, authentication) -> FameUtils.writeJsonResponse(RestResponse.ok(), response);
    }

    /**
     * AccessDeniedException Strategy
     *
     * @return accessDeniedHandler
     */

    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) ->
                FameUtils.writeJsonResponse(RestResponse.fail(ErrorCode.NOT_LOGIN.getCode(), ErrorCode.NOT_LOGIN.getMsg()), response);
    }

    /**
     * AuthenticationException Strategy
     *
     * @return authenticationEntryPoint
     */
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authenticationException) ->
                FameUtils.writeJsonResponse(RestResponse.fail(ErrorCode.NOT_LOGIN.getCode(), ErrorCode.NOT_LOGIN.getMsg()), response);
    }

}
