package com.example.demo.security.config;

import com.alibaba.fastjson.JSON;
import com.example.demo.security.util.JwtUtil;
import com.example.demo.webuser.WebUser;
import com.example.demo.webuser.WebUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {


    private final WebUserService webUserService;
    private final JwtUtil jwtUtil;

    public JwtRequestFilter(WebUserService webUserService, JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        this.webUserService = webUserService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String token = request.getHeader("token");
        //continue
        if(!StringUtils.hasText(token)) {
            chain.doFilter(request, response);
            return;
        }
        String username = null;
        //read token
        try {
           username = jwtUtil.getUsernameFromToken(token);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (ExpiredJwtException e) {
            throw e;

        }

        //Once we get the token validate it.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.webUserService.loadUserByUsername(username);

            // if token is valid configure Spring Security to manually set authentication
            if (jwtUtil.validateToken(token, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // After setting the Authentication in the context, we specify
                // that the current user is authenticated. So it passes the Spring Security Configurations successfully.
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }
}
