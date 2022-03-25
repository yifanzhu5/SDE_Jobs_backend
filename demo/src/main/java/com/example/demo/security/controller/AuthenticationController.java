package com.example.demo.security.controller;

import com.example.demo.security.UserInfoResponse;
import com.example.demo.security.util.JwtUtil;
import com.example.demo.webuser.WebUser;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "api/v1")
public class AuthenticationController {


    @GetMapping(path = "/user")
    public ResponseEntity<?> login() {
        //TODO try catch
        try{
            WebUser webUser = (WebUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return ResponseEntity.ok(new UserInfoResponse(webUser.getUsername(), webUser.getEmail()));
        }catch (Exception e){
            //TODO
           return ResponseEntity.ok(new JSONObject());
        }
    }

    //TODO update favorite jobs ost mapping


}
