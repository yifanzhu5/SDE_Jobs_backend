package com.example.demo.security.config;

import com.example.demo.webuser.WebUser;
import com.example.demo.webuser.WebUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class SelfAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private final WebUserService webUserService;


    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public SelfAuthenticationProvider(WebUserService webUserService) {
        this.webUserService = webUserService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String account= authentication.getName();     //获取用户名
        String password= (String) authentication.getCredentials();  //获取密码
        WebUser userDetails= (WebUser) webUserService.loadUserByUsername(account);
        String email = userDetails.getEmail();
        String account2 = account + "&&&&&&" + email;
        //ArrayList<String> acc = new ArrayList<String>();
        //acc.add(account);
        //acc.add(email);
        boolean checkPassword= bCryptPasswordEncoder.matches(password,userDetails.getPassword());
        if(!checkPassword){
            throw new BadCredentialsException("密码不正确，请重新登录!");
        }
        return new UsernamePasswordAuthenticationToken(account2,password,userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}