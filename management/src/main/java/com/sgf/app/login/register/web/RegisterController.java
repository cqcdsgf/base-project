package com.sgf.app.login.register.web;

import com.sgf.app.domain.AuthUser;
import com.sgf.app.security.service.UserService;
import com.sgf.app.user.AuthUserService;
import com.sgf.base.security.domain.SysUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Created by sgf on 2018\1\30 0030.
 */
@Controller
//@RequestMapping("/register")
public class RegisterController {
    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    UserService userService;

    @Autowired
    AuthUserService authUserService;

    @GetMapping("/register/toRegister")
    public String toRegister(){

        return "/register/register";
    }

    @PostMapping("/register/register")
    public String register(String username,String password,RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute( "username", username);

        if(username.length()==0){
            redirectAttributes.addFlashAttribute( "errorMessage", "用户名不能为空!");
            return "redirect:/register/toRegister";
        }

        if(password.length()==0){
            redirectAttributes.addFlashAttribute( "errorMessage", "密码不能为空!");
            return "redirect:/register/toRegister";
        }

        AuthUser authUser = authUserService.findByUsername(username);

        if (null == authUser) {
            authUser = new AuthUser();
            authUser.setUsername(username);

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            authUser.setPassword(encoder.encode(password));

            authUserService.save(authUser);

            redirectAttributes.addFlashAttribute( "message", "注册成功！");

            return "redirect:/login/login";

        }else{

            redirectAttributes.addFlashAttribute( "errorMessage", "用户名重复！");

            return "redirect:/register/toRegister";
        }
    }
}
