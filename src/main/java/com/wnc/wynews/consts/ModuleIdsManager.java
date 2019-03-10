
package com.wnc.wynews.consts;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.JSONObject;
import com.wnc.basic.BasicFileUtil;
import com.wnc.string.PatternUtil;

public class ModuleIdsManager
{
    private static Map<String, List<String>> map = new ConcurrentHashMap<String, List<String>>();
    private static String moduleIdsFile = WyConsts.NEWS_MODULE_IDS;

    static
    {
        initModuleIds2();
    }

    /**
     * @Description 从文件中加载模块中的新闻数据, 已废弃, 改为根据文件夹实际来获取
     * @Date 2018/7/27 23:14
     * @Param
     * @Return void
     */
    // public static void initModuleIds() {
    // if (map.size() > 0) {
    // return;
    // }
    // if (BasicFileUtil.isExistFile(moduleIdsFile)) {
    // List<String> lines = FileOp.readFrom(moduleIdsFile, "UTF-8");
    // String join = StringUtils.join(lines, "");
    // map = JSONObject.parseObject(join, ConcurrentHashMap.class);
    // }
    // }

    public static void initModuleIds2()
    {
        if ( map.size() > 0 )
        {
            return;
        }
        for ( File f : new File( WyConsts.COMMENTS_DIR ).listFiles() )
        {
            if ( f.isDirectory() )
            {
                String key = f.getName();
                System.out.println( f.getName() );
                List<String> values = new ArrayList<String>();
                for ( File f2 : f.listFiles() )
                {
                    values.add( PatternUtil.getFirstPatternGroup( f2.getName(),
                            "cmt-(.*?)\\." ) );
                }
                map.put( key, values );
            }
        }
    }

    /**
     * @Description 判断模块中是否已存在该新闻, 不存在则添加并返回true
     * @Date 2018/7/27 23:12
     * @Param moduleName
     * @Param code
     * @Return boolean
     */
    public static synchronized boolean addModuleNews( String moduleName,
            String code )
    {
        if ( !map.containsKey( moduleName ) )
        {
            map.put( moduleName, new ArrayList<String>() );
        }

        List<String> strings = map.get( moduleName );
        if ( !strings.contains( code ) )
        {
            strings.add( code );
            return true;
        }
        return false;
    }

    public static synchronized void output()
    {
        BasicFileUtil.writeFileString( WyConsts.NEWS_MODULE_IDS,
                JSONObject.toJSONString( map ), null, false );
    }

    public static void main( String[] args )
    {
        // initUsers();
        // output();
        initModuleIds2();
    }
}
