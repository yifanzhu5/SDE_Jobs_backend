package com.example.demo.security.config;

import com.example.demo.webuser.WebUser;
import com.example.demo.webuser.WebUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final WebUserService webUserService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthenticationProvider SelfAuthenticationProvider;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //disable cross-site request
                .csrf().disable()
                //set up access restrictions
                .authorizeRequests()
                .antMatchers("/api/v1/register/**")
                .permitAll()
                .antMatchers("/api/v1/jobs/page/**")
                .permitAll()
                //require authentication for any other url requests
                .anyRequest()
                .authenticated().and()
                .formLogin()
                //customized handler for successfully login
                .failureHandler((request, response, ex) -> {
                    response.setContentType("application/json;charset=utf-8");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    PrintWriter out = response.getWriter();
                    Map<String, Object> map = new HashMap<String, Object>();

                    if (ex instanceof UsernameNotFoundException || ex instanceof BadCredentialsException) {
                        map.put("isMatch",false);
                        map.put("userid", "");
                        map.put("email", "");
                        //map.put("message", "用户名或密码错误");
                    }
                    out.write(objectMapper.writeValueAsString(map));
                    out.flush();
                    out.close();
                })
                .successHandler((request, response, authentication) -> {
                    Map<String, Object> map = new HashMap<String, Object>();
                    //map.put("code", 200);
                    //map.put("message", "登录成功");
                    String result = authentication.getName();
                    String[] temp1;
                    temp1 = result.split("&&&&&&");
                    map.put("isMatch",true);
                    map.put("userid", temp1[0]);
                    map.put("email", temp1[1]);
                    //authentication.getName();
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter out = response.getWriter();
                    out.write(objectMapper.writeValueAsString(map));
                    out.flush();
                    out.close();
                });
    }

    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(webUserService).passwordEncoder(bCryptPasswordEncoder);
        //auth.authenticationProvider(daoAuthenticationProvider());

    }*/

    /*@Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(webUserService);
        return provider;
    }*/

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(SelfAuthenticationProvider);
    }



}
