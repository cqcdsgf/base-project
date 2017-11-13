package com.sgf.app.example;

import com.sgf.app.example.message.Message;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created by sgf on 2017/11/2.
 */
@Controller
public class ExampleController {

    @GetMapping(value = "demoValidate")
    public ModelAndView demoValidate(){

        return new ModelAndView("example/demo-validate","message","test");

    }

    @GetMapping(value = "demoDate")
    public ModelAndView demoDate(){
        return new ModelAndView("example/demo-datetime","message","test");
    }


    @PostMapping(value = "/demoPostDate")
    public ModelAndView demoPostDate(HttpServletRequest request, String choosedDate,String currentDate,  RedirectAttributes redirect){

        return new ModelAndView("example/demo-datetime","choosedDate","2017-11-11");
    }

    @GetMapping(value = "demoRichText")
    public ModelAndView demoRichText(){
        return new ModelAndView("example/demo-rich-text","message","test");
    }

    @GetMapping(value = "demoCity")
    public ModelAndView demoCity(){
        return new ModelAndView("example/demo-city","message","test");
    }

    @GetMapping(value = "demoUploadImg")
    public ModelAndView demoUploadImg(){
        return new ModelAndView("example/demo-upload-img","message","test");
    }
}
