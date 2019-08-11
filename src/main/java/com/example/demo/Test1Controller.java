package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class Test1Controller {
    @ResponseBody
    @RequestMapping("/hello")
    public String getName(){
        return "hello";
    }

    @RequestMapping("/success")
    public String success(Map<String,String> obj){
        obj.put("hello","yech");
        return "success";
    }
}
