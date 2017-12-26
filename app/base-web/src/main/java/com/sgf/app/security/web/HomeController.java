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
        Msg msg =  new Msg("测试标题","测试内容","额外信息，只对管理员显示");
        model.addAttribute("msg", msg);
        return "home";
    }

}
