package com.sgf.app.user.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.sgf.base.constant.BaseMessageConstant;
import com.sgf.base.constant.LoginConstant;
import com.sgf.base.security.domain.AuthRole;
import com.sgf.base.security.domain.AuthUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by sgf on 2018\2\8 0008.
 */
@Controller
//@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/user/toUpdate")
    public ModelAndView toUpdate(){
        AuthUser authUser = new AuthUser();
        authUser.setId(1L);
        authUser.setUsername("测试用户名");
        authUser.setEmail("test@163.com");
        authUser.setPhone("12345678901");


        AuthRole authRole1 = new AuthRole();
        authRole1.setId(1L);
        authRole1.setSelected(true);
        authRole1.setName("name1");
        authRole1.setSummary("测试1");

        AuthRole authRole2 = new AuthRole();
        authRole2.setId(2L);
        authRole2.setSelected(true);
        authRole2.setName("name2");
        authRole2.setSummary("测试2");

        AuthRole authRole3 = new AuthRole();
        authRole3.setId(3L);
        authRole3.setName("name2");
        authRole3.setSummary("测试2");

        authUser.getRoles().add(authRole1);
        authUser.getRoles().add(authRole2);
        authUser.getRoles().add(authRole3);

        Map<String, Object> model = Maps.newHashMap();
        model.put("authUser",authUser);

        return new ModelAndView("user/userUpdate",model);
    }



    @PostMapping("/user/update")
    public String toUpdate(AuthUser authUser, RedirectAttributes redirectAttributes){


        redirectAttributes.addFlashAttribute(BaseMessageConstant.MESSAGE,"用户修改成功！");
        return "redirect:/user/toQueryList";
    }


    @GetMapping("/user/toQueryList")
    public ModelAndView toQueryList(){



        return new ModelAndView("/user/userList");

    }



}
