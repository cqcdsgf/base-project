package com.sgf.app.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by sgf on 2017/10/18.
 */
@Controller
public class thymeleafController {


    @GetMapping(value = "/list",produces = "application/json")
    public String testList() throws Exception{

        throw  new Exception("test");
        //return "list";
    }

    @GetMapping("/testList")
    public ModelAndView List(){

        return new ModelAndView("list","message","ok");
    }


    @GetMapping("/testPath")
    public String testPath(){


        return "/path/list";
    }


    @GetMapping("/deep")
    public ModelAndView testDeep(){

        return  new ModelAndView("path/deeppath/deep");
    }

}
