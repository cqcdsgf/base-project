package com.sgf.app.security.web;

import com.google.common.collect.Maps;
import com.sgf.app.security.domain.SysUser;
import com.sgf.app.security.service.UserService;
import com.sgf.base.constant.LoginConstant;
import com.sgf.base.constant.RedisConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by sgf on 2018/1/16.
 */
@Controller
@RequestMapping("/security")
public class SecurityController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    UserService userService;

    private boolean getLoginFailFlag(String username,HttpServletRequest request){
        boolean userCheck = false;
        boolean sessionCheck = false;

        String sessionId = request.getSession().getId();

        if(null != username) {
            String userLockFlag = stringRedisTemplate.opsForValue().get(username + RedisConstant.LOGIN_FAIL_LOCK_USER);
            if(null != userLockFlag){
                userCheck = true;
            }else{
                userCheck = false;
            }
        }
        if(null != sessionId) {
            String sessionLockFlag = stringRedisTemplate.opsForValue().get(sessionId + RedisConstant.LOGIN_FAIL_LOCK_SESSION);
            if(null != sessionLockFlag){
                sessionCheck = true;
            }else{
                sessionCheck = false;
            }
        }

        if(!userCheck && !sessionCheck){
            return  false;
        }else{
            return true;
        }
    }

    @RequestMapping(value = "/backLogin")
    public ModelAndView backLogin(HttpServletRequest request,String username, String password, String imageCode){
        boolean loginFailFlag = getLoginFailFlag(username,request);

        Map<String, Object> model = Maps.newHashMap();
        model.put(LoginConstant.LOGIN_USERNAME,username);
        model.put(LoginConstant.LOGIN_PASSWORD,password);
        model.put(LoginConstant.LOGIN_IMAGECODE,imageCode);
        model.put(LoginConstant.LOGIN_FAIL_FLAG,loginFailFlag);

        return new ModelAndView("/security/login",model);
    }

    @ResponseBody
    @GetMapping("/checkUsername")
    public  Map<String, Object> checkUsername(HttpServletRequest request,String username){
        boolean loginFailFlag = getLoginFailFlag(username,request);

        Map<String, Object> result = Maps.newHashMap();
        result.put("result", loginFailFlag);

        return result;

    }

    @RequestMapping(value = "/login")
    public ModelAndView toLogin(HttpServletRequest request,String username){
        boolean loginFailFlag = getLoginFailFlag(username,request);

        Map<String, Object> model = Maps.newHashMap();
        model.put(LoginConstant.LOGIN_FAIL_FLAG,loginFailFlag);
        return new ModelAndView("/security/login",model);
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
        redirectAttributes.addFlashAttribute( "username", username);

        if(username.length()==0){
            redirectAttributes.addFlashAttribute( "errorMessage", "用户名不能为空!");
            return "redirect:/security/register";
        }

        if(password.length()==0){
            redirectAttributes.addFlashAttribute( "errorMessage", "密码不能为空!");
            return "redirect:/security/register";
        }

        SysUser sysUser = userService.findByUsername(username);

        if (null == sysUser) {
            sysUser = new SysUser();
            sysUser.setUsername(username);

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            sysUser.setPassword(encoder.encode(password));

            userService.registerUser(sysUser);

            redirectAttributes.addFlashAttribute( "message", "注册成功！");

            return "redirect:/security/login";

        }else{

            redirectAttributes.addFlashAttribute( "errorMessage", "用户名重复！");

            return "redirect:/security/register";
        }
    }
}
