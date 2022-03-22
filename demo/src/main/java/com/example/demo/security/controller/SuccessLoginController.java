/*package com.example.demo.security.controller;

import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin(origins = "http://localhost:3001")
@Controller
@RequestMapping("/sucesslogin")
public class SuccessLoginController{

    @PostMapping("api/v1/login")
    public JSONObject index(@RequestParam(value="username") Boolean isMatch ,@RequestParam(value ="password") String token ){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("isMatch",isMatch);
        jsonObject.put("token",token);
        return jsonObject;
    }

}*/