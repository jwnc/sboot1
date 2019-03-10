
package com.wnc.sboot1.util;

// 测试多线程与static代码块关系
public class Staticc
{
    static int i = 0;
    static
    {
        i = 100;
        try
        {
            Thread.sleep( 10000 );
        } catch ( InterruptedException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void a()
    {
        System.out.println( "XXXXX" );
    }
}
