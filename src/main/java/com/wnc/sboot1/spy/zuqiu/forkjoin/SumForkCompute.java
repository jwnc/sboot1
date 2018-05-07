
package com.wnc.sboot1.spy.zuqiu.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class SumForkCompute
{

    public int compute() throws Exception
    {
        ForkJoinPool pool = new ForkJoinPool( 5 );

        int i = 1;
        int s = 0;
        while ( i <= 100 )
        {
            SumForkTask task = new SumForkTask( i );
            s += pool.invoke( task );
            i++;
        }
        return s;
    }

    private class SumForkTask extends RecursiveTask<Integer>
    {

        private int num;

        public SumForkTask( int i )
        {
            this.num = i;
        }

        @Override
        protected Integer compute()
        {
            try
            {
                Thread.sleep( 100 );
            } catch ( InterruptedException e )
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println( Thread.currentThread().getName() );
            return 2 * num;
        }
    }

    public static void main( String[] args ) throws Exception
    {
        System.out.println(
                "SumForkCompute计算耗时" + new SumForkCompute().compute() );
    }
}
