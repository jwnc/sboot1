package com.wnc.sboot1.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wnc.sboot1.itbook.helper.GistHelper;

@RestController
@RequestMapping( value = "/gist" )
public class GistController
{
    @PostMapping( "/pull" )
    public String downloadITBookGist()
    {
        try
        {
            new GistHelper().checkGist();
            return "成功下载";
        } catch ( Exception e )
        {
            e.printStackTrace();
        }
        return "下载失败!";
    }
}