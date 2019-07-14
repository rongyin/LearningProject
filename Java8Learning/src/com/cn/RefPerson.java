package com.cn;

import com.cn.test.ag.Person;

public class RefPerson extends RefAbstract implements Person {
    public void refTest(String name){
        System.out.println("ref person"+name);
    }

    private void privateTest(){

    }
    @Override
    public void test(){
        System.out.println("test");
    }
}
