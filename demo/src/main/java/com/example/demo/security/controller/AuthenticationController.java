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


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "api/v1")
@AllArgsConstructor
public class AuthenticationController {

    private WebUserService webUserService;

    @GetMapping(path = "/user")
    public ResponseEntity<?> login() {
            WebUser webUser = (WebUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return ResponseEntity.ok(new UserInfoResponse(webUser.getUsername(), webUser.getEmail(), webUser.getFavList()));
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
            jsonObject.put("errMsg", "Network error! Unable to update favorite list!");
            return ResponseEntity.badRequest().body(jsonObject);
        }
    }

}
