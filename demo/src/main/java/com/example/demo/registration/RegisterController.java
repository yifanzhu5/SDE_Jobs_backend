package com.example.demo.registration;
import com.example.demo.job.JobService;
import com.example.demo.job.constant.GlobalConst;
import com.example.demo.job.entity.Job;
import com.example.demo.job.entity.HttpEntity;
import net.sf.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Iterator;
import java.lang.String;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "api/v1")
public class RegisterController {

    private RegistrationService registrationService;

    @Autowired
    public RegisterController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping(value = "/register")
    @ResponseBody
    public JSONObject register(@RequestParam (value = "userid") String userid,@RequestParam(value = "password")String password,@RequestParam(value="email")String email){
        RegistrationRequest test  =  new RegistrationRequest(userid,email,password);
        ArrayList<String> out = registrationService.register(test);
        ArrayList<Boolean> flg = new ArrayList<Boolean>();
        flg.add(true);
        flg.add(true);
        for(int i = 0;i < out.size(); i ++){
            if (out.get(i).contains("user fail")){
                flg.set(0,false);
            }
            if (out.get(i).contains("email fail")){
                flg.set(1,false);
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("isRegistered",flg);
        jsonObject.put("userId",userid);
        jsonObject.put("email",email);
        return jsonObject;
    }


}
