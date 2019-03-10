
package com.wnc.sboot1.spy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RequestMapping( "/rest" )
public class ApiController
{

    @PostMapping("alert")
    public String alertTest(@RequestBody String body){
        System.out.println("this is getTest:"+body);
        return "成功获取body:"+body;
    }

    @RequestMapping(value = "alert2", method = RequestMethod.POST)
    @ResponseBody
    public String alert(@RequestBody String body){
        System.out.println("ddd:"+body);
//        return Result.getSuccessResult("调用alert成功.");
        return "S:"+body;
    }

    @RequestMapping(value = "alert3", method = RequestMethod.POST)
    @ResponseBody
    public String alert3(@RequestParam("body") String body){
        System.out.println("ddd:"+body);
//        return Result.getSuccessResult("调用alert成功.");
        return "S:"+body;
    }

}
