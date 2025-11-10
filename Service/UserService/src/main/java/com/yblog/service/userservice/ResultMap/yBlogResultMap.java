package com.yblog.service.userservice.ResultMap;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class yBlogResultMap {

    private Map<String,Object> resultMap;
    public yBlogResultMap() {
        resultMap = new HashMap<String,Object>();
        resultMap.put("data", null);
        resultMap.put("status", false);
    }
    public yBlogResultMap(Map<String,Object> resultMap) {
        this.resultMap = resultMap;
    }
    public Map<String,Object> addMap(String key,Object value) {
        resultMap.put(key,value);
        return resultMap;
    }
    public Map<String,Object> rmMap(String key) {
        resultMap.remove(key);
        return resultMap;
    }
}
