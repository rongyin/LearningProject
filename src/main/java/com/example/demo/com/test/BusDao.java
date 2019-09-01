package com.example.demo.com.test;

import com.example.demo.aop.FrankAdvise;

public interface BusDao {

    @FrankAdvise(type = "before")
    public void onWork();
}
