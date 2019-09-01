package com.example.demo;

import com.example.demo.com.test.BusDao;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

public class FrankFactoryBean implements FactoryBean {
    @Override
    public Object getObject() throws Exception {
        Class[] clazzs = new Class[]{BusDao.class};
        BusDao busDao = (BusDao)Proxy.newProxyInstance(this.getClass().getClassLoader(),clazzs,new FrankInvocationHandler());
        return busDao;
    }

    @Override
    public Class<?> getObjectType() {
        return BusDao.class;
    }
}
