package com.wnc.sboot1;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

@WebFilter( filterName = "myFilter", urlPatterns = "/*" )
public class MyFilter implements Filter
{
    public void destroy()
    {
        // TODO Auto-generated method stub
    }

    public void doFilter( ServletRequest srequest, ServletResponse sresponse,
            FilterChain filterChain ) throws IOException, ServletException
    {
        // TODO Auto-generated method stub
        HttpServletRequest request = (HttpServletRequest)srequest;
        System.out.println( "this is MyFilter,url :" + request.getRequestURI() );
        filterChain.doFilter( srequest, sresponse );
    }

    public void init( FilterConfig arg0 ) throws ServletException
    {
        // TODO Auto-generated method stub
    }
}