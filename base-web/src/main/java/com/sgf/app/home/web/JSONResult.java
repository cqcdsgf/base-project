package com.sgf.app.home.web;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sgf on 2018/2/25.
 */
public class JSONResult {
    private static final Logger logger = LoggerFactory.getLogger(JSONResult.class);
    public static String fillResultString(Integer status, String message, Object result){
        JSONObject jsonObject = new JSONObject(){{
            put("status", status);
            put("message", message);
            put("result", result);
        }};
        return jsonObject.toString();
    }

}
