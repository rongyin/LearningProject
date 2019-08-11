package com.example.demo;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Component
public class MyLocalAdpator implements LocaleResolver {
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String l = request.getParameter("l");
        if("zh".equalsIgnoreCase(l)){
            return new Locale("zh","CN");
        }else if ("us".equalsIgnoreCase(l)){
            return new Locale("en","US");
        }else {
            return new Locale("zh","CN");
        }
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {

    }
}
