
package tech.dyn.construction;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class S
{
    private String a;
    private String b;

    public String getA()
    {
        return a;
    }

    public void setA( String a )
    {
        this.a = a;
    }

    public String getB()
    {
        return b;
    }

    public void setB( String b )
    {
        this.b = b;
    }

    public S( String a,String b )
    {
        this.a = a;
        this.b = b;
    }

    public S getClone() throws NoSuchMethodException, SecurityException,
            InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException
    {
        Class clazz = this.getClass();
        Constructor c = clazz.getDeclaredConstructor( String.class,
                String.class );// 获取有参构造
        return (S)c.newInstance( a, b ); // 通过有参构造创建对象
    }
}