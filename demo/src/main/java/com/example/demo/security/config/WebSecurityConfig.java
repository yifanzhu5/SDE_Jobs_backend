package com.example.demo.security.config;

import com.example.demo.webuser.WebUser;
import com.example.demo.webuser.WebUserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final WebUserService webUserService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //disable cross-site request
                .csrf().disable()
                //set up access restrictions
                .authorizeRequests()
                    .antMatchers("/api/v*/registration/**")
                    .permitAll()
                    .antMatchers("/api/v1/jobs/page/**")
                    .permitAll()
                    //require authentication for any other url requests
                    .anyRequest()
                    .authenticated().and()
                .formLogin()
                    //customized handler for successfully login
                    .successHandler(myLoginSuccessHandler())
                    .failureHandler(myLoginFailureHandler());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(webUserService);
        return provider;
    }

    // Successfully login
    @Bean
    public SavedRequestAwareAuthenticationSuccessHandler myLoginSuccessHandler() {
        return new SavedRequestAwareAuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request,
                                                HttpServletResponse response,
                                                FilterChain chain,
                                                Authentication authentication)
                    throws IOException, ServletException {
                WebUser webUser = (WebUser) authentication.getPrincipal();
                logger.info("USER : " + webUser.getUsername() + " LOGIN SUCCESS !  ");
                super.onAuthenticationSuccess(request, response, chain, authentication);
            }
        };
    }

    // Unsuccessfully login
    @Bean
    public SimpleUrlAuthenticationFailureHandler myLoginFailureHandler() {
        return new SimpleUrlAuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                logger.info(" LOGIN FAILED !  ");
                super.onAuthenticationFailure(request, response, exception);
            }
        };
    }
}
