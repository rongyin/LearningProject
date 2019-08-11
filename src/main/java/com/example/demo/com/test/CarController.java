package com.example.demo.com.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CarController {
    @Autowired
    private CarService carService;

}
