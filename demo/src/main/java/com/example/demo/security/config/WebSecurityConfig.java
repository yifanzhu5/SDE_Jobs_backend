package com.example.demo.security.config;

import com.alibaba.fastjson.JSON;
import com.example.demo.security.util.JwtUtil;
import com.example.demo.webuser.WebUser;
import com.example.demo.webuser.WebUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;



@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final WebUserService webUserService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    private CorsConfigurationSource CorsConfigurationSource() {
        CorsConfigurationSource source =   new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");    //同源配置，表示任何请求都视为同源，若需指定ip和端口可以改为如“localhost：8080”，多个以“，”分隔；
        corsConfiguration.addAllowedHeader("*");//header，允许哪些header，本案中使用的是token，此处可将替换为token；
        corsConfiguration.addAllowedMethod("*");    //允许的请求方法，PSOT、GET等
        ((UrlBasedCorsConfigurationSource) source).registerCorsConfiguration("/**",corsConfiguration); //配置允许跨域访问的url
        return source;
    }

    /*@Bean
    public SavedRequestAwareAuthenticationSuccessHandler successHandler() {
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("/succeslogin");
        return successHandler;
    }*/


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.cors().configurationSource(CorsConfigurationSource());
        http
                .authenticationProvider(daoAuthenticationProvider())
                .httpBasic()
                //When authentication is required, prompt in json format
                // set url without authentication
                .authenticationEntryPoint((request,response,authException) -> {
                    response.setContentType("application/json;charset=utf-8");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.addHeader("Access-Allow-Control-Origin","*");
                    PrintWriter out = response.getWriter();
                    Map<String,Object> map = new HashMap<>();
                    map.put("username","");
                    map.put("password","");
                    out.write(objectMapper.writeValueAsString(map));
                    out.flush();
                    out.close();
                })

                .and()
                .authorizeRequests()
                    .antMatchers("/api/v1/register/**")
                    .permitAll()
                    .antMatchers("/api/v1/jobs/page/**")
                    .permitAll()
                    .antMatchers("/api/v1/jobs/search/**")
                    .permitAll()
                    .antMatchers("/api/v1/login")
                    .permitAll()
                    .antMatchers("/api/v1/user")
                    .permitAll()
                .anyRequest().authenticated().and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .formLogin() //使用自带的登录
                .loginProcessingUrl("/api/v1/login")
                .permitAll()
                //登录失败，返回json
                .failureHandler((request,response,ex) -> {
                    logger.info("login failed");
                    response.setContentType("application/json;charset=utf-8");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    //response.addHeader("Access-Allow-Control-Origin","*");
                    PrintWriter out = response.getWriter();
                    Map<String,Object> map = new HashMap<>();
                    if (ex instanceof UsernameNotFoundException || ex instanceof BadCredentialsException) {
                        map.put("isMatch",false);
                    }
                    out.write(objectMapper.writeValueAsString(map));
                    out.flush();
                    out.close();

                })
                //登录成功，返回json
                .successHandler((request,response,authentication) -> {
                    logger.info("login succeed");
                    Map<String,Object> map = new HashMap<>();
                    map.put("isMatch",true);
                    WebUser res = (WebUser) authentication.getPrincipal();
                    String jwt = jwtUtil.generateToken(res);

                    map.put("token",jwt);
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType("application/json;charset=utf-8");
                    //response.addHeader("Access-Allow-Control-Origin","*");
                    PrintWriter out = response.getWriter();
                    out.write(objectMapper.writeValueAsString(map));
                    out.flush();
                    out.close();
                })
                .and()
                .exceptionHandling()
                //没有权限，返回json
                .accessDeniedHandler((request,response,ex) -> {
                    response.setContentType("application/json;charset=utf-8");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    PrintWriter out = response.getWriter();
                    Map<String,Object> map = new HashMap<>();
                    map.put("code",403);
                    map.put("message", "permission denied");
                    out.write(objectMapper.writeValueAsString(map));
                    out.flush();
                    out.close();
                })
                .and()
                .logout()
                //退出成功，返回json
                .logoutUrl("/api/v1/logout")
                .logoutSuccessHandler((request,response,authentication) -> {
                    Map<String,Object> map = new HashMap<>();
                    map.put("code",200);
                    map.put("message","logout succeed");
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter out = response.getWriter();
                    out.write(objectMapper.writeValueAsString(map));
                    out.flush();
                    out.close();
                })
                //.deleteCookies("JSESSIONID")
                .permitAll();
        //开启跨域访问
        //http.cors().disable();
        //开启模拟请求，比如API POST测试工具的测试，不开启时，API POST为报403错误
        http.csrf().disable();

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/css/**")
                .antMatchers("/404.html")
                .antMatchers("/500.html")
                .antMatchers("/html/**")
                .antMatchers("/js/**");
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(webUserService);
        return provider;
    }



    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() { //登出处理
        return new LogoutSuccessHandler() {
            @Override
            public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                httpServletResponse.sendRedirect("/login");
            }
        };
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
