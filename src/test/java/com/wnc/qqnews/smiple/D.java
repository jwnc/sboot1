
package com.wnc.qqnews.smiple;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池使用示例, 主线程等待所有任务完成再结束.
 * 
 * @author chenlb 2008-12-2 上午10:31:03
 */
public class D
{

    public static class MyTask implements Runnable
    {
        private static int id = 0;

        private String name = "task-" + (++id);
        private int sleep;

        public MyTask( int sleep )
        {
            super();
            this.sleep = sleep;
        }

        public void run()
        {
            System.out.println( name + " -----start-----" );
            try
            {
                Thread.sleep( sleep ); // 模拟任务执行.
            } catch ( InterruptedException e )
            {
                e.printStackTrace();
            }
            System.out.println( name + " -----end " + sleep + "-----" );
        }

    }

    public static void main( String[] args )
    {
        System.out.println( "==================start==================" );
        ThreadPoolExecutor executor = new ThreadPoolExecutor( 5, 5, 60,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>() );
        int n = 10;
        int sleep = 10 * 1000; // 10s
        Random rm = new Random();
        for ( int i = 0; i < n; i++ )
        {
            executor.execute( new MyTask( rm.nextInt( sleep ) + 1 ) );
        }

        executor.shutdown();// 只是不能再提交新任务，等待执行的任务不受影响

        try
        {
            boolean loop = true;
            do
            { // 等待所有任务完成
                loop = !executor.awaitTermination( 2, TimeUnit.SECONDS ); // 阻塞，直到线程池里所有任务结束
            } while ( loop );
        } catch ( InterruptedException e )
        {
            e.printStackTrace();
        }

        System.out.println( "==================end====================" );
    }

}