package com.qadr.bankapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {


    @RequestMapping(value ="/")
    public String getHomePage(){
//        System.out.println("home page");
        return "/index.html";
    }
}
