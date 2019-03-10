
package tech.dyn;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 获取子类 传进来的泛型
 * 
 * @author nengcai.wang
 */
public class Dyn2
{
    public abstract class S<T>
    {
        public S()
        {
            System.out.println( this );
            Class<? extends S> class1 = getClass();
            System.out.println( class1 );
            System.out.println( "create S" );
            // 1获取子类的class(在创建子类对象的时候,会返回父类的构造方法)
            Class<? extends S> clazz = this.getClass(); // A
            // 2获取当前类的带有泛型的父类类型
            ParameterizedType type = (ParameterizedType)clazz
                    .getGenericSuperclass();
            // 3返回实际参数类型(泛型可以写多个)
            Type[] types = type.getActualTypeArguments();
            // 4 获取第一个参数(泛型的具体类) String.class
            clazz = (Class)types[0];

        }
    }

    public class A extends S<String>
    {
        public A()
        {
            super();
            System.out.println( this );
            System.out.println( "create A" );
        }
    }

    public static void main( String[] args )
    {
        A a = new Dyn2().new A();
        System.out.println( a );
    }
}
