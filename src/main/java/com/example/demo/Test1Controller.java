package com.example.demo;

import com.example.demo.com.test.BusDao;
import com.example.demo.com.test.Person;
import com.example.demo.dao.TutorialsDao;
import com.example.demo.vo.Tutorials;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
@ResponseBody
@Controller
public class Test1Controller {
    @Autowired
    private TutorialsDao tutorialsDao;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private BusDao busDao;


    @RequestMapping("/bus")
    public String busDao(){
        busDao.onWork();
        return "work";
    }

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

    @RequestMapping("/getT")
    public List<Tutorials> getTutorials(){

        return tutorialsDao.findByTitle("manager");
        //return tutorialsDao.findAll();
    }
    @RequestMapping("/getL")
    public Tutorials getLeader(){
        return tutorialsDao.testFindByTitle("leader");
    }

    @RequestMapping("/getN")
    public Tutorials getAuthor(@RequestParam String name){
        return tutorialsDao.testFindByAuthor(name);
    }

    @RequestMapping("/redis")
    public String getRedis(){
        return stringRedisTemplate.opsForValue().get("myString");
    }

    @RequestMapping("/success")
    public String success(Map<String,String> obj){
        obj.put("hello","yech");
        return "success";
    }
}
