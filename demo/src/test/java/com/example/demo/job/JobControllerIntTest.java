package com.example.demo.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


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
import java.lang.reflect.Array;
import java.util.*;


@JsonIgnoreProperties("hibernateLazyInitializer")
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
        String result = mockMvc.perform(get("/api/v1/jobs/page/{pgid}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(result);
        Assert.assertNotNull(jsonObject.get("count"));
        Assert.assertNotNull(jsonObject.get("current_page"));
        Assert.assertNotNull(jsonObject.get("page_size"));
        JSONArray jsonArrayData = (JSONArray) jsonObject.get("jobs");
        JSONObject jsonObject_data = null;
        if (jsonArrayData.length() > 0) {
            jsonObject_data = (JSONObject) jsonArrayData.get(0);
            Assert.assertNotNull(jsonObject_data.get("id"));
            Assert.assertNotNull(jsonObject_data.get("title"));
            Assert.assertNotNull(jsonObject_data.get("city"));
            Assert.assertNotNull(jsonObject_data.get("publish_time"));
            Assert.assertNotNull(jsonObject_data.get("description"));
            Assert.assertNotNull(jsonObject_data.get("company"));
            Assert.assertNotNull(jsonObject_data.get("apply_url"));
            Assert.assertNotNull(jsonObject_data.get("from_url"));
        }
        int numtotal = jsonArrayData.length();
        Assert.assertEquals(numtotal, 20);// page_size=20
    }
    @Test
    @Sql("/data.sql")
    void subpage() throws Exception {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("page_size",20);
        map.put("current_page",2);
        String json=JSON.toJSONString(map);
        //System.out.println(json);
        String result = mockMvc.perform(post("/api/v1/jobs/search").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(result);
        Assert.assertNotNull(jsonObject.get("count"));
        Assert.assertNotNull(jsonObject.get("current_page"));
        Assert.assertNotNull(jsonObject.get("page_size"));
        JSONArray jsonArrayData = (JSONArray) jsonObject.get("jobs");
        Assert.assertEquals(jsonArrayData.length(),3);

        map.clear();
        map.put("page_size",50);
        map.put("current_page",1);
        String json2=JSON.toJSONString(map);
        //System.out.println(json);
        String result2 = mockMvc.perform(post("/api/v1/jobs/search").content(json2).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        JSONObject jsonObject2 = new JSONObject(result2);
        Assert.assertNotNull(jsonObject2.get("count"));
        Assert.assertNotNull(jsonObject2.get("current_page"));
        Assert.assertNotNull(jsonObject2.get("page_size"));
        JSONArray jsonArrayData2 = (JSONArray) jsonObject2.get("jobs");
        Assert.assertEquals(jsonArrayData2.length(),23);

    }



    @Test
    @Sql("/data.sql")
    void search_keyword() throws Exception {

        String keywords_setup= "software";
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("keywords",keywords_setup);
        map.put("page_size",20);
        map.put("current_page",1);
        String json=JSON.toJSONString(map);
        //System.out.println(json);
        String result = mockMvc.perform(post("/api/v1/jobs/search").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(result);
        Assert.assertNotNull(jsonObject.get("count"));
        Assert.assertNotNull(jsonObject.get("current_page"));
        Assert.assertNotNull(jsonObject.get("page_size"));
        JSONArray jsonArrayData = (JSONArray) jsonObject.get("jobs");
        System.out.println(jsonArrayData.length());
        JSONObject jsonObject_data = null;
        if (jsonArrayData.length() > 0){
            for (int i = 0; i < jsonArrayData.length(); i++) {
                JSONObject test = jsonArrayData.getJSONObject(i);
                String title = test.getString("title").toLowerCase();
                String des = test.getString("description").toLowerCase();
                String bq = test.getString("basic_qualifications").toLowerCase();
                String loc = test.getString("locations").toLowerCase();
                String team = test.getString("team").toLowerCase();
                String pq = test.getString("preferred_qualifications").toLowerCase();
                String com = test.getString("company").toLowerCase();
                boolean status = title.contains(keywords_setup.toLowerCase()) || des.contains(keywords_setup.toLowerCase()) || com.contains(keywords_setup.toLowerCase());
                Assert.assertTrue(status);
            }

        }
        //System.out.println(result);
    }

    @Test
    @Sql("/data.sql")
    void search_filter_city1() throws Exception {
        List<String> cities_setup = new ArrayList<>();
        cities_setup.add("Waterloo");
        cities_setup.add("Vancouver");
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("cities",cities_setup);
        map.put("page_size",20);
        map.put("current_page",1);


        //
        String json=JSON.toJSONString(map);
        String result = mockMvc.perform(post("/api/v1/jobs/search").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(result);
        Assert.assertNotNull(jsonObject.get("count"));
        Assert.assertNotNull(jsonObject.get("current_page"));
        Assert.assertNotNull(jsonObject.get("page_size"));
        JSONArray jsonArrayData = (JSONArray) jsonObject.get("jobs");
        JSONObject jsonObject_data = null;
        if (jsonArrayData.length() > 0){
            for (int i = 0; i < jsonArrayData.length(); i++) {
                JSONObject test = jsonArrayData.getJSONObject(i);
                String cities = test.getString("city");
                boolean containsSearchStr = cities_setup.stream().anyMatch(cities::equalsIgnoreCase);
                Assert.assertTrue(containsSearchStr);
            }
        }
    }

    @Test
    @Sql("/data.sql")
    void search_filter_city2() throws Exception {
        List<String> cities_setup = new ArrayList<>();
        cities_setup.add("others");
        List<String> cities_setup2 = new ArrayList<>();
        cities_setup2.add("Vancouver");
        cities_setup2.add("Toronto");
        cities_setup2.add("Montreal");
        cities_setup2.add("Ottawa");
        cities_setup2.add("Waterloo");
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("cities",cities_setup);
        map.put("page_size",20);
        map.put("current_page",1);
        String json=JSON.toJSONString(map);
        String result = mockMvc.perform(post("/api/v1/jobs/search").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(result);
        Assert.assertNotNull(jsonObject.get("count"));
        Assert.assertNotNull(jsonObject.get("current_page"));
        Assert.assertNotNull(jsonObject.get("page_size"));
        JSONArray jsonArrayData = (JSONArray) jsonObject.get("jobs");
        JSONObject jsonObject_data = null;
        if (jsonArrayData.length() > 0){
            for (int i = 0; i < jsonArrayData.length(); i++) {
                JSONObject test = jsonArrayData.getJSONObject(i);
                String cities = test.getString("city");
                boolean containsSearchStr = cities_setup2.stream().anyMatch(cities::equalsIgnoreCase);
                Assert.assertTrue(!containsSearchStr);
            }
        }

    }
    @Test
    @Sql("/data.sql")
    void search_filter_city3() throws Exception {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("has_remote",true);
        map.put("page_size",20);
        map.put("current_page",1);

        String json=JSON.toJSONString(map);
        String result = mockMvc.perform(post("/api/v1/jobs/search").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(result);
        Assert.assertNotNull(jsonObject.get("count"));
        Assert.assertNotNull(jsonObject.get("current_page"));
        Assert.assertNotNull(jsonObject.get("page_size"));
        JSONArray jsonArrayData = (JSONArray) jsonObject.get("jobs");
        JSONObject jsonObject_data = null;
        if (jsonArrayData.length() > 0){
            for (int i = 0; i < jsonArrayData.length(); i++) {
                JSONObject test = jsonArrayData.getJSONObject(i);
                boolean has_remote = test.getBoolean("has_remote");
                Assert.assertTrue(has_remote);
            }
        }
    }

    @Test
    @Sql("/data.sql")
    void search_filter_company() throws Exception {
        List<String> company_setup = new ArrayList<>();
        company_setup.add("Google");
        company_setup.add("others");

        List<String> company_setup2 = new ArrayList<>();
        company_setup2.add("Amazon");
        company_setup2.add("Shopify");
        company_setup2.add("Microsoft");

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("companys",company_setup);
        map.put("page_size",20);
        map.put("current_page",1);
        String json=JSON.toJSONString(map);
        String result = mockMvc.perform(post("/api/v1/jobs/search").content(json).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(result);
        Assert.assertNotNull(jsonObject.get("count"));
        Assert.assertNotNull(jsonObject.get("current_page"));
        Assert.assertNotNull(jsonObject.get("page_size"));
        JSONArray jsonArrayData = (JSONArray) jsonObject.get("jobs");
        JSONObject jsonObject_data = null;
        if (jsonArrayData.length() > 0){
            for (int i = 0; i < jsonArrayData.length(); i++) {
                JSONObject test = jsonArrayData.getJSONObject(i);
                String cities = test.getString("city");
                boolean containsSearchStr = company_setup2.stream().anyMatch(cities::equalsIgnoreCase);
                Assert.assertTrue(!containsSearchStr);
            }
        }

    }


}


