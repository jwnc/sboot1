
package com.wnc.sboot1.spy;

public interface Spy
{
    void spy() throws Exception;

    void callBackComplete( int type, String msg, Runnable task );

    boolean isTaskOver();

    long getSpyDuration();
}
