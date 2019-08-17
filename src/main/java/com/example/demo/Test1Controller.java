package com.example.demo;

import com.example.demo.com.test.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class Test1Controller {

    @Autowired
    MongoTemplate mongoTemplate;


    @ResponseBody
    @RequestMapping("/hello")
    public List<Person> getName(){

        List<Person> list = mongoTemplate.findAll(Person.class);
/*
        list.forEach(p->{
            System.out.println(p.getName()+":"+p.getAge());
        });
*/
        return list;
    }

    @RequestMapping("/success")
    public String success(Map<String,String> obj){

        obj.put("hello","yech");
        return "success";
    }
}
