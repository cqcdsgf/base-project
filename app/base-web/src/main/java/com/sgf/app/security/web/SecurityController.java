package com.sgf.app.security.web;

import com.google.common.collect.Maps;
import com.sgf.app.security.domain.SysUser;
import com.sgf.app.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

/**
 * Created by sgf on 2018/1/16.
 */
@Controller
@RequestMapping("/security")
public class SecurityController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/login")
    public ModelAndView login(){
        return new ModelAndView("/security/login");
    }

    @RequestMapping("/")
    public String index(){
        return "/demo/template";
    }

    @GetMapping("/register")
    public String toRegister(){
        return "/security/register";
    }

    @PostMapping("/register")
    public String register(String username,String password,RedirectAttributes redirectAttributes){
        RedirectView view = new RedirectView();

        SysUser sysUser = userService.findByUsername(username);

        Map<String, Object> resultMap = Maps.newHashMap();

        if (null == sysUser) {
            sysUser = new SysUser();
            sysUser.setUsername(username);

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            sysUser.setPassword(encoder.encode(password));

            userService.registerUser(sysUser);

/*            view.setUrl("/security/login");
            resultMap.put("tip","注册成功！");
            return new ModelAndView(view,resultMap);*/

            redirectAttributes.addFlashAttribute( "tip", "注册成功！");

            return "redirect:/security/login";

        }else{
            resultMap.put("error","用户名重复！");
            resultMap.put("username",username);

/*            view.setUrl("/security/register");
            return new ModelAndView("/security/register",resultMap);*/

            redirectAttributes.addFlashAttribute( "error", "用户名重复！");
            redirectAttributes.addFlashAttribute( "username", username);
            return "redirect:/security/register";



        }


    }




}
