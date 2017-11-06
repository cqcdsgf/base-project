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

    @GetMapping(value = "testValidate")
    public ModelAndView testValidate(){

        return new ModelAndView("example/jquery-validate","message","test");

    }

    @GetMapping(value = "testDate")
    public ModelAndView testDate(){
        return new ModelAndView("example/datetime","message","test");
    }


    @PostMapping(value = "/testPostDate")
    public ModelAndView testPostDate(HttpServletRequest request, String choosedDate,String currentDate,  RedirectAttributes redirect){

        return new ModelAndView("example/datetime","choosedDate","2017-11-11");
    }

    @GetMapping(value = "testRichText")
    public ModelAndView testRichText(){
        return new ModelAndView("example/rich-text","message","test");
    }


}
