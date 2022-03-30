package com.example.demo.job.entity;

import java.util.List;
import java.util.Optional;


public class HttpEntity {
    private Optional<List<Long>> ids;
    private Optional<String> keywords;
    private Optional<Integer> page_size;
    private Optional<Integer> current_page;
    //TODO: Optional<String>
    private Optional<List<String>> cities;
    private Optional<List<String>> companys;
    //private Optional<Integer> update_time;
    private Optional<Boolean> has_remote;


    public Optional<List<Long>> getIds() {
        return ids;
    }

    public void setIds(Optional<List<Long>> ids) {
        this.ids = ids;
    }

    public Optional<List<String>> getCompanys() {
        return companys;
    }

    public void setCompanys(Optional<List<String>> companys) {
        this.companys = companys;
    }

    public Optional<List<String>> getCities() {
        return cities;
    }

    public void setCities(Optional<List<String>> cities) {
        this.cities = cities;
    }

    public Optional<Boolean> getHas_remote() {
        return has_remote;
    }

    public void setHas_remote(Optional<Boolean> has_remote) {
        this.has_remote = has_remote;
    }

    public Optional<String> getKeywords() {
        return keywords;
    }
    public void setKeywords(Optional<String> keywords) {
        this.keywords = keywords;
    }

    public Optional<Integer> getPage_size() {
        return page_size;
    }

    public void setPage_size(Optional<Integer> page_size) {
        this.page_size = page_size;
    }

    public Optional<Integer> getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(Optional<Integer> current_page) {
        this.current_page = current_page;
    }





    public HttpEntity() {
    }

    public HttpEntity(Optional<String> keywords, Optional<Integer> page_size, Optional<Integer> current_page, Optional<List<String>> cities, Optional<List<String>> companys,Optional<Boolean> has_remote) {
        this.keywords = keywords;
        this.page_size = page_size;
        this.current_page = current_page;
        this.cities = cities;
        this.companys = companys;
        this.has_remote=has_remote;
    }
}
