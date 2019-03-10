
package tech.dyn;

/**
 * 获取子类 Class
 * 
 * @author nengcai.wang
 */
public class Dyn1
{
    public abstract class S
    {
        public S()
        {
            Class<? extends S> class1 = getClass();
            System.out.println( class1 );
            System.out.println( "create S" );
        }
    }

    public class A extends S
    {
        public A()
        {
            super();
            System.out.println( "create A" );
        }
    }

    public static void main( String[] args )
    {
        A a = new Dyn1().new A();
        System.out.println( a );
    }
}
