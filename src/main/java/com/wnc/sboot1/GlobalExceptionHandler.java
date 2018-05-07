
package com.wnc.sboot1;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wnc.sboot1.common.beans.ResultBean;
import com.wnc.sboot1.common.exceptions.CheckException;

@ControllerAdvice
public class GlobalExceptionHandler
{

    // Controller 拦截异常处理
    @ExceptionHandler( value = Exception.class )
    public ModelAndView defaultErrorHandler( HttpServletRequest req, Exception e )
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.getModel().put( "errmsg", e.getMessage() );
        modelAndView.setViewName( "error" );
        return modelAndView;
    }

    // RestController 拦截异常处理
    @ExceptionHandler( value = CheckException.class )
    @ResponseBody
    public ResultBean<?> checkErrorHandler( HttpServletRequest req, Exception e )
    {
        return new ResultBean<>( e );
    }

    @ExceptionHandler( value = SQLException.class )
    @ResponseBody
    public ResultBean<?> sqlErrorHandler( HttpServletRequest req, Exception e )
    {
        return new ResultBean<>( e );
    }
}