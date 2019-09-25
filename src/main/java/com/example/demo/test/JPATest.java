package com.example.demo.test;


import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

public class JPATest {
    public static void main(String[] args) {
        Tomcat server = new Tomcat();
        server.addWebapp("/","/Users/frankyin/IdeaProjects/LearningProject/springTest");
        server.setPort(9900);
        //找到所有实现WebApplicationInitializer的类，servlet标准
        try {
            server.start();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
        server.getServer().await();
    }
}
