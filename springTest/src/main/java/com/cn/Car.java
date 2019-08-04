package com.cn;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class Car implements BeanNameAware, BeanFactoryAware, InitializingBean , ApplicationContextAware , DisposableBean {
    private String name;
    private Double price;


    public Car() {
        System.out.println("Car construct");
    }

    public String getName() {
        return name;
    }

    @Value("BMW")
    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setBeanName(String name) {
        System.out.println("BeanNameAware " + name);
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("BeanFactory aware"+this.getName());
    }

    public void afterPropertiesSet() throws Exception {
        System.out.println("Bean afterProperty" + this.getName());
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("application context aware");
    }

    public void destroy() throws Exception {
        System.out.println("destroy");
    }
}
