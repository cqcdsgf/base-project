package com.sgf.app.home.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by sgf on 2017\12\26 0026.
 */
@Controller
public class HomeController {

    @RequestMapping("/")
    public String index(){
        return "/home/home";
    }

}
