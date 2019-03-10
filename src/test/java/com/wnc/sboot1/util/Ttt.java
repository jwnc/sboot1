
package com.wnc.sboot1.util;

import org.junit.Test;

public class Ttt
{
    @Test
    public void a() throws InterruptedException
    {
        new Thread( new Runnable()
        {

            @Override
            public void run()
            {
                Staticc.a();
            }

        } ).start();
        new Thread( new Runnable()
        {

            @Override
            public void run()
            {
                Staticc.a();
            }

        } ).start();

        Thread.sleep( 20000 );
    }
}
