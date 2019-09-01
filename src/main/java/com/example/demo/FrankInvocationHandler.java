package com.example.demo;

import com.example.demo.aop.BeforeTest;
import com.example.demo.aop.FrankAdvise;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class FrankInvocationHandler implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        for (int i = 0; i < method.getAnnotations().length; i++) {
            Annotation annotation = method.getAnnotations()[i];
            if(annotation instanceof FrankAdvise){
                FrankAdvise advise = (FrankAdvise) annotation;
                if(advise.type().equalsIgnoreCase("before")){
                    BeforeTest beforeTest = new BeforeTest();
                    beforeTest.doBefore();
                }
            }
        }

        System.out.println("frank proxy:"+method.getName());

        return null;
    }
}
