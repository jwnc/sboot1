
package com.wnc.sboot1.small.charge;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

public class JsonFieldTest
{

    public static class Person
    {

        // @JSONField( name = "First_Name" )
        private String firstName;

        @JSONField( name = "Age" )
        private String age;

        @JSONField( name = "Desc" )
        private String desc;

        public String getFirstName()
        {
            return firstName;
        }

        public void setFirstName( String firstName )
        {
            this.firstName = firstName;
        }

        public String getAge()
        {
            return age;
        }

        public void setAge( String age )
        {
            this.age = age;
        }

        public String getDesc()
        {
            return desc;
        }

        public void setDesc( String desc )
        {
            this.desc = desc;
        }

        public void setAGE( String AGE )
        {
            this.age = AGE;
        }

        public void setDESC( String DESC )
        {
            this.desc = DESC;
        }

        public String toString()
        {
            return JSONObject.toJSONString( this );
        }
    }

    public static void main( String[] args )
    {
        String json = "{\"Age\":\"20\",\"DESC\":\"只是一个测试\",\"First_Name\":\"XIANGLJ\"} ";
        Person parseObject = JSONObject.parseObject( json, Person.class );
        System.out.println( parseObject );
    }
}
