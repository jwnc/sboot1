
package com.wnc.sboot1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorPageController
{

    @RequestMapping( "/404" )
    public String toPage()
    {
        return "404";
    }

}