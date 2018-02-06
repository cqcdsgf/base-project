package com.sgf.base.demo.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by sgf on 2017/11/2.
 */
@Controller
@RequestMapping("/demo")
public class DemoController {
    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

    @GetMapping(value = "/demoValidate")
    public ModelAndView demoValidate(){

        return new ModelAndView("demo/demoValidate","message","test");

    }

    @GetMapping(value = "/demoDate")
    public ModelAndView demoDate(){
        return new ModelAndView("demo/demoDatetime","message","test");
    }


    @PostMapping(value = "/demoPostDate")
    public ModelAndView demoPostDate(HttpServletRequest request, String choosedDate,String currentDate,  RedirectAttributes redirect){

        return new ModelAndView("demo/demoDatetime","choosedDate","2017-11-11");
    }

    @GetMapping(value = "/demoRichText")
    public ModelAndView demoRichText(){
        return new ModelAndView("demo/demoRichtext","message","test");
    }

    @GetMapping(value = "/demoCity")
    public ModelAndView demoCity(){
        return new ModelAndView("demo/demoCity","message","test");
    }

    @GetMapping(value = "/template")
    public ModelAndView template(){
        return new ModelAndView("demo/template","message","test");
    }

    @GetMapping(value = "/demoUpload")
    public ModelAndView demoUploadImg(){
        return new ModelAndView("demo/demoUpload","message","test");
    }


}
