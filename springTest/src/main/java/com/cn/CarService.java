package com.cn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarService {
    @Autowired
    private Car car;

    public Car buyCar(){
        return car;
    }
}
