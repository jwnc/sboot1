
package com.wnc.sboot1.cluster.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wnc.sboot1.cluster.service.ProxyListService;
import com.wnc.sboot1.cluster.util.ProxyUtil;
import com.wnc.sboot1.common.beans.ResultBean;

@RestController
@RequestMapping( "/proxy" )
@SuppressWarnings( {"rawtypes", "unchecked"} )
public class ProxyController
{
    @Autowired
    ProxyListService proxyListService;

    @GetMapping( "fatest200" )
    public ResultBean<List> column( String dayStart, String dayEnd )
    {
        try
        {
            return new ResultBean<List>( proxyListService.getFatestProxies() );
        } catch ( IOException e )
        {
            e.printStackTrace();
            return new ResultBean( e );
        }
    }

    @GetMapping( "get66Proxy" )
    public ResultBean<List> proxy66Ip()
    {
        try
        {
            return new ResultBean<List>( ProxyUtil.get61Proxies() );
        } catch ( IOException e )
        {
            e.printStackTrace();
            return new ResultBean( e );
        }
    }
}