package com.example.demo.register;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.regex.*;
@JsonIgnoreProperties("hibernateLazyInitializer")
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class RegistrationControllerTest {
    @Autowired
    private MockMvc mockMvc;


    @Test
    void register() throws Exception {
        String result = mockMvc.perform(post("/api/v1/register").param("username","zhuliang").param("email","z22geng@uwaterloo.ca").param("password","Gzl123")).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(result);
        Assert.assertNotNull(jsonObject.get("isRegistered"));
        Assert.assertNotNull(jsonObject.get("username"));
        Assert.assertNotNull(jsonObject.get("email"));
        JSONArray jsonArrayData = (JSONArray) jsonObject.get("isRegistered");
        Assert.assertEquals(jsonArrayData.length(),2);
        Object a = jsonArrayData.get(0);
        Object b = jsonArrayData.get(1);
        if ((a != (Object) true) && (a != (Object) false)){
            Assert.assertTrue(false);
        }
        if ((b != (Object) true) && (b != (Object) false)){
            Assert.assertTrue(false);
        }
        String email = (String) jsonObject.get("email");
        String pattern = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        boolean isMatch = Pattern.matches(pattern, email);
        Assert.assertTrue(isMatch);
    }

}
