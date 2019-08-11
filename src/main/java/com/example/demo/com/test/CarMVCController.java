package com.example.demo.com.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;
@Controller
public class CarMVCController implements WebApplicationInitializer {

    @RequestMapping(value = "*/getCar",method = RequestMethod.GET)
    public void getCar( @PathVariable(name = "cName") String carName, @RequestParam(required = true,defaultValue = "12",name = "carId") String carId){

    }

    public void onStartup(javax.servlet.ServletContext ctx) throws ServletException {
        AnnotationConfigWebApplicationContext webCtx = new AnnotationConfigWebApplicationContext();
        //webCtx.register(QuotingServletConfig.class);
        webCtx.setServletContext(ctx);
        DispatcherServlet dispatcherServlet = new DispatcherServlet(webCtx);

        ServletRegistration.Dynamic dispatcher = ctx.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/controller/*");

    }
}
