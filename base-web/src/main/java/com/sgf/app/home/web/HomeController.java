package com.sgf.app.home.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sgf on 2017\12\26 0026.
 */
@Controller
//@RestController
public class HomeController {

/*    @RequestMapping("/")
    public String index(){
        return "/demo/template";
    }*/

    // 根目录映射 Get访问方式 直接返回一个字符串
    @RequestMapping("/")
    @ResponseBody
    Map<String, String> hello2() {
        // 返回map会变成JSON key value方式
        Map<String,String> map=new HashMap<String,String>();
        map.put("content", "hello freewolf~");
        return map;
    }


    // 路由映射到/users
    @RequestMapping(value = "/users", produces="application/json;charset=UTF-8")
    @ResponseBody
    public String usersList() {

        ArrayList<String> users =  new ArrayList<String>(){{
            add("freewolf");
            add("tom");
            add("jerry");
        }};

        return JSONResult.fillResultString(0, "", users);
    }

    @RequestMapping(value = "/hello", produces="application/json;charset=UTF-8")
    @ResponseBody
    public String hello() {
        ArrayList<String> users =  new ArrayList<String>(){{ add("hello"); }};
        return JSONResult.fillResultString(0, "", users);
    }

    @RequestMapping(value = "/world", produces="application/json;charset=UTF-8")
    @ResponseBody
    public String world() {
        ArrayList<String> users =  new ArrayList<String>(){{ add("world"); }};
        return JSONResult.fillResultString(0, "", users);
    }
}
