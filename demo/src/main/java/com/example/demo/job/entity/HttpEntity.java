package com.example.demo.job.entity;

import java.util.List;


public class HttpEntity {
    private String keywords;
    private Integer page_size;
    private Integer current_page;
    //TODO: Optional<String>
    private List<String> locations;
    private List<String> companies;

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Integer getPage_size() {
        return page_size;
    }

    public void setPage_size(Integer page_size) {
        this.page_size = page_size;
    }

    public Integer getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(Integer current_page) {
        this.current_page = current_page;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public List<String> getCompanies() {
        return companies;
    }

    public void setCompanies(List<String> companies) {
        this.companies = companies;
    }

    public HttpEntity() {
    }

    public HttpEntity(String keywords, Integer page_size, Integer current_page, List<String> locations, List<String> companies) {
        this.keywords = keywords;
        this.page_size = page_size;
        this.current_page = current_page;
        this.locations = locations;
        this.companies = companies;
    }
}
