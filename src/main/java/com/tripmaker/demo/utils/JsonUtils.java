package com.tripmaker.demo.utils;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class JsonUtils {

    public static String convertToJson(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;
        try {
            json = objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

}
