
package tech.dyn.construction;

import java.lang.reflect.InvocationTargetException;

/**
 * 获取子类 传进来的泛型
 * 
 * @author nengcai.wang
 */
public class Dyn3
{

    public static void main( String[] args ) throws NoSuchMethodException,
            SecurityException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException
    {
        S s = new S( "s", "G" );
        S a = new A( "a", "G" );
        System.out.println( a.getClone().getA() );
    }
}
