package com.example.demo.security.controller;

import com.example.demo.security.UserInfoResponse;
import com.example.demo.webuser.WebUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1")
public class AuthenticationController {

    @GetMapping(path = "/user")
    public ResponseEntity<?> login() {
        WebUser webUser = (WebUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(new UserInfoResponse(webUser.getUsername(), webUser.getEmail()));
    }

}
