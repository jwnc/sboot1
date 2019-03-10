
package com.wnc.qqnews.user;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import com.wnc.qqnews.QqConsts;

public class UserStatFileUtil
{
    private static final int OBJ_BYTES = 24;
    private static RandomAccessFile raf;
    static
    {
        try
        {
            raf = new RandomAccessFile( QqConsts.USERS_DIR + "userstat.txt",
                    "rw" );
        } catch ( FileNotFoundException e )
        {
            e.printStackTrace();
        }
    }

    /**
     * 覆盖, 获取pos, 一个int4字节, 6个字段24字节
     * 
     * @param userStat
     * @throws IOException
     */
    public static synchronized void write( UserStat userStat )
            throws IOException
    {
        raf.seek( userStat.getPos() * OBJ_BYTES );
        raf.writeInt( userStat.getId() );
        raf.writeInt( userStat.getLastActiveTime() );
        raf.writeInt( userStat.getLastSpyTime() );
        raf.writeInt( userStat.getOrieffcommentnum() );
        raf.writeInt( userStat.getRepeffcommentnum() );
        raf.writeInt( userStat.getUpnum() );
    }

    public static synchronized UserStat read( int pos ) throws IOException
    {
        raf.seek( pos * OBJ_BYTES );// 读取时，将指针重置到文件的开始位置。
        UserStat userStat = new UserStat();
        userStat.setPos( pos );
        userStat.setId( raf.readInt() );
        userStat.setLastActiveTime( raf.readInt() );
        userStat.setLastSpyTime( raf.readInt() );
        userStat.setOrieffcommentnum( raf.readInt() );
        userStat.setRepeffcommentnum( raf.readInt() );
        userStat.setUpnum( raf.readInt() );
        return userStat;
    }

    public static List<UserStat> readAll() throws IOException
    {
        List<UserStat> list = new ArrayList<UserStat>();
        int i = 0;
        try
        {
            while ( true )
            {
                if ( i * OBJ_BYTES < raf.length() )
                {
                    list.add( read( i ) );
                } else
                {
                    break;
                }
                i++;
            }
        } catch ( Exception e )
        {
            e.printStackTrace();
        }
        return list;
    }

    public static void main( String[] args )
    {
        try
        {
            long s = System.currentTimeMillis();
            // testWrite();
            // readAll();
            // randomWrite();
            for ( int i = 0; i < 100; i++ )
            {
                UserStat read = read( i );
                System.out.println( read.getId() );
            }
            System.out.println( System.currentTimeMillis() - s );
        } catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    private static void randomWrite() throws IOException
    {
        write( new UserStat( 1 ).setPos( 0 ) );
    }

    private static void testWrite() throws IOException
    {
        for ( int i = 0; i < 2000000; i++ )
        {
            write( new UserStat( i + 1 ).setPos( i ) );
        }
    }

}
