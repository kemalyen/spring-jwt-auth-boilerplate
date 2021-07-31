package com.gazatem.corvus;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello {



    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index()
    {
        return "Hello index";
    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
     public String hi()
    {
        return "Hello";
    }


    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public void testFunction() {

        System.out.println("Test function is executed");

    }

    @RequestMapping(value = "/currentuser", method = RequestMethod.GET)
    public void test() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        System.out.println(currentPrincipalName);

    }

}
