package com.example.demo.job;



import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
class JobControllerIntTest {
    @Autowired
    private MockMvc mockMvc;


    @Test
    @Sql("/data.sql")
    void getAllJobs() throws Exception {

        String result = mockMvc.perform(get("/api/v1/jobs/page/{pgid}",1)//使用get方式来调用接口。
                        .contentType(MediaType.APPLICATION_JSON)//请求参数的类型
                ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        JSONObject jsonObject =new JSONObject(result);
        JSONArray jsonArrayData = (JSONArray)jsonObject.get("jobs");
        JSONObject jsonObject_data = null;
        if(jsonArrayData.length()>0){
            jsonObject_data = (JSONObject) jsonArrayData.get(0);

        }
        int numtotal= jsonArrayData.length();
        Assert.assertNotNull(jsonObject.get("count"));
        Assert.assertNotNull(jsonObject.get("current_page"));

        Assert.assertEquals(numtotal,20);
        System.out.println(jsonObject_data);
    }


    @Test
    void search() {
    }
}