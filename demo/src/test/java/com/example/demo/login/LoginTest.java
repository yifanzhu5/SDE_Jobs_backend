package com.example.demo.login;

import com.alibaba.fastjson.JSON;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest

@AutoConfigureMockMvc
public class LoginTest {
    @Autowired

    private MockMvc mvc;

    @Test
    public void testUnauthenticatedUser() throws Exception {

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("keywords","software");
        map.put("page_size",20);
        map.put("current_page",1);
        String json= JSON.toJSONString(map);
        mvc.perform(post("/api/v1/jobs/search").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    }

    @Test
    public void testUnauthenticatedUser2() throws Exception {
        mvc.perform(get("/api/v1/jobs/page/1"))
                .andExpect(status().isOk());

    }

    @Test
    void loginfunction() throws Exception {
        String result = mvc.perform(post("/api/v1/login").param("username","zhuliang").param("password","Gzl123")).andReturn().getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(result);
        Assert.assertNotNull(jsonObject.get("isMatch"));
        Assert.assertNotNull(jsonObject.get("username"));
        Assert.assertNotNull(jsonObject.get("email"));
    }


    @Test
    @WithMockUser
    void logout() throws Exception {
        String result = mvc.perform(post("/api/v1/logout")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(result);
        Assert.assertNotNull(jsonObject.get("code"));
        Assert.assertNotNull(jsonObject.get("message"));

    }

    @Test
    @WithMockUser
    void login() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        //authentication.getPrincipal();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("keywords","software");
        map.put("page_size",20);
        map.put("current_page",1);
        String json= JSON.toJSONString(map);
        String result = mvc.perform(post("/api/v1/jobs/search").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(result);
        Assert.assertNotNull(jsonObject.get("count"));
        Assert.assertNotNull(jsonObject.get("current_page"));
        Assert.assertNotNull(jsonObject.get("page_size"));
    }



}
