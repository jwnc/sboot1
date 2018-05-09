
package com.wnc.sboot1.common.aop;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;

import com.wnc.sboot1.common.beans.ResultBean;
import com.wnc.sboot1.common.exceptions.CheckException;
import com.wnc.sboot1.common.exceptions.UnloginException;

/**
 * 处理和包装异常
 * <p>
 * 停止使用, 添加全局异常处理 GlobleExceptionHandler
 * </p>
 * 
 * @author 肖文杰
 */
// @Aspect
// @Component
public class ControllerAOP
{
    private static final Logger logger = Logger
            .getLogger( ControllerAOP.class );

    @Pointcut( "execution(public com.wnc.sboot1.common.beans.ResultBean *(..))" )
    public void controllerMethod()
    {
    }

    @Around( "controllerMethod()" )
    public Object handlerControllerMethod( ProceedingJoinPoint pjp )
    {
        long startTime = System.currentTimeMillis();

        ResultBean<?> result;

        try
        {
            result = (ResultBean<?>)pjp.proceed();
            logger.info( pjp.getSignature() + "use time:"
                    + (System.currentTimeMillis() - startTime) );
        } catch ( Throwable e )
        {
            result = handlerException( pjp, e );
        }

        return result;
    }

    private ResultBean<?> handlerException( ProceedingJoinPoint pjp,
            Throwable e )
    {
        ResultBean<?> result = new ResultBean();

        // 已知异常
        if ( e instanceof CheckException )
        {
            result.setMsg( e.getLocalizedMessage() );
            result.setCode( ResultBean.FAIL );
        } else if ( e instanceof UnloginException )
        {
            result.setMsg( "Unlogin" );
            result.setCode( ResultBean.NO_LOGIN );
        } else
        {
            logger.error( pjp.getSignature() + " error ", e );

            // TODO 未知的异常，应该格外注意，可以发送邮件通知等
            result.setMsg( e.toString() );
            result.setCode( ResultBean.FAIL );
        }

        return result;
    }
}