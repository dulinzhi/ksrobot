package com.dlz.ksrobot.ksrobot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PageController {

    @RequestMapping("/")
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView("index");
        return modelAndView;
    }

    @RequestMapping("/mylist")
    public ModelAndView list(){
        ModelAndView modelAndView = new ModelAndView("MyList");
        return modelAndView;
    }

}
