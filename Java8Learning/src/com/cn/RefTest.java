package com.cn;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

public class RefTest {

    @Test
    public void testPerson(){
        try{
            Class ref= Class.forName("com.cn.RefPerson");
             RefPerson refPerson =  (RefPerson) ref.newInstance();
            refPerson.refTest("test");
             Method refTest = ref.getDeclaredMethod("refTest",String.class);
            refTest.invoke(refPerson,"invoke");
            //Method printName = ref.getDeclaredMethod("printName");//能获取所有方法，没发获得继承方法，实现接口方法
            //System.out.println(printName);
            Arrays.asList(ref.getMethods()).forEach(System.out::println);//只能获取public方法

        }catch (Exception e){
e.printStackTrace();
        }


    }
}
