package com.sgf.app.security.web;

import com.sgf.app.security.domain.Msg;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by sgf on 2017\12\26 0026.
 */
@Controller
public class HomeController {

    @RequestMapping(value = "/login")
    public ModelAndView login(){

        return new ModelAndView("login");

    }

    @RequestMapping("/")
    public String index(Model model){
        return "demo/template";
    }

}
