package com.sgf.app.example;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
}
