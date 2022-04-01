package com.example.demo.security.controller;

import com.example.demo.job.entity.HttpEntity;
import com.example.demo.security.entity.FavListRequest;
import com.example.demo.security.entity.UserInfoResponse;
import com.example.demo.webuser.WebUser;
import com.example.demo.webuser.WebUserService;
import lombok.AllArgsConstructor;
import net.sf.json.JSONObject;
import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "api/v1")
@AllArgsConstructor
public class AuthenticationController {

    private WebUserService webUserService;

    @GetMapping(path = "/user")
    public ResponseEntity<?> login() {
        try{
            WebUser webUser = (WebUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<Long> test = webUser.getFavList();
            for (int i =0 ; i < test.size();i++){
                if (test.get(i).equals(0L)) {
                    test.remove(i);
                }
            }
            return ResponseEntity.ok(new UserInfoResponse(webUser.getUsername(), webUser.getEmail(),test));
        }catch (Exception e){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("errMsg", "Login expired. Please login!");
            return ResponseEntity.badRequest().body(jsonObject);
        }
    }

    @PostMapping(path = "/updateFavJobs")
    public ResponseEntity<?> updateFav(@RequestBody FavListRequest favListRequest) {
        JSONObject jsonObject = new JSONObject();
        try {
            WebUser webUser = (WebUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            boolean isAdd = favListRequest.isAdd();
            Long jobId = favListRequest.getId();
            webUserService.updateFav(webUser, isAdd, jobId);
            jsonObject.put("successMsg", isAdd);
            return ResponseEntity.ok(jsonObject);

        }catch (Exception e) {
            if(e instanceof ClassCastException)
                jsonObject.put("errMsg", "Login expired. Please login!");
            else
                jsonObject.put("errMsg", "Unable to update favorite list");
            return ResponseEntity.badRequest().body(jsonObject);
        }
    }

}
