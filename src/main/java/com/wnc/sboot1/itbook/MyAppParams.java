
package com.wnc.sboot1.itbook;

import org.springframework.context.annotation.DependsOn;

import com.wnc.basic.BasicFileUtil;
import com.wnc.sboot1.SpringContextUtils;
import com.wnc.sboot1.util.MyProperties;

public class MyAppParams
{
    private String root;
    // = "D:\\itbook\\";
    private static MyAppParams instance = new MyAppParams();

    private MyAppParams()
    {
        MyProperties bean = (MyProperties)SpringContextUtils.getContext()
                .getBean( "myProperties" );
        this.root = bean.getItbookRoot();
        BasicFileUtil.makeDirectory( getGistFolder() );
        BasicFileUtil.makeDirectory( getitwordSaveFolder() );
    }

    public static MyAppParams getInstance()
    {
        return instance;
    }

    public String getSqliteName()
    {
        return "jdbc:sqlite:" + root + "itdict.db";
    }

    public String getitwordSaveFolder()
    {
        return root + "itword-save-dir/";
    }

    public String getGistFolder()
    {
        return root + "itword-save-dir/GIST/";
    }

    public String getRoot()
    {
        return root;
    }

    public void setRoot( String root )
    {
        this.root = root;
    }

}
