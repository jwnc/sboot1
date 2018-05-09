
package com.wnc.sboot1.cluster.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapUtil
{
    public static class KeyValue
    {
        String k;
        Object v;

        public KeyValue( String k,Object v )
        {
            this.k = k;
            this.v = v;
        }

        public String getK()
        {
            return k;
        }

        public void setK( String k )
        {
            this.k = k;
        }

        public Object getV()
        {
            return v;
        }

        public void setV( Object v )
        {
            this.v = v;
        }

        @Override
        public String toString()
        {
            return "KeyValue [k=" + k + ", v=" + v + "]";
        }
    }

    public static <T> List<KeyValue> map2List( Map<String, T> map )
    {
        List<KeyValue> kvList = new ArrayList<>();
        for ( Map.Entry<String, T> entry : map.entrySet() )
        {
            kvList.add( new KeyValue( entry.getKey(), entry.getValue() ) );
        }
        return kvList;
    }
}
