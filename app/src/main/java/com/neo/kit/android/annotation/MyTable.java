package com.neo.kit.android.annotation;

import android.util.Log;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author neo.duan
 * @date 2019-09-02 21:10
 * @desc 自定义注解
 */

@Target(value = {ElementType.TYPE})
@Retention(value = RUNTIME)
public @interface MyTable {
    /**
     * ElementType[] value(); value为参数名，ElementType参数类型
     * <p>
     * public enum ElementType {
     * TYPE, //类
     * FIELD,  //字段
     * METHOD, //方法
     * PARAMETER,
     * CONSTRUCTOR,//构造
     * LOCAL_VARIABLE,
     * ANNOTATION_TYPE,
     * PACKAGE,
     * TYPE_PARAMETER,
     * TYPE_USE;
     * }
     * <p>
     * public enum RetentionPolicy {
     * SOURCE, //源码,编译时注解
     * CLASS, //编译时注解
     * RUNTIME; //运行时注解，通过反射获取
     * }
     */

    int id() default -1; //-1代表默认代表不存在

    String name() default ""; //参数student,默认值""

    int age() default 1;

    String[] schools() default {""};

    //一般只有一个参数，用value
    String value() default "";
}

@Target(ElementType.FIELD)
@Retention(RUNTIME)
@interface MyField {
    String columName() default "";

    String type() default "";

    int length() default 0;
}


@MyTable("tb_student")
class Student {

    @MyField(columName = "_id", type = "int", length = 10)
    private int stuId;

    @MyField(columName = "_name", type = "String", length = 20)
    private String stuName;

    @MyField(columName = "_age", type = "int", length = 10)
    private int stuAge;

    public int getStuId() {
        return stuId;
    }

    public void setStuId(int stuId) {
        this.stuId = stuId;
    }

    public String getStuName() {
        if (stuName == null) {
            stuName = "";
        }
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public int getStuAge() {
        return stuAge;
    }

    public void setStuAge(int stuAge) {
        this.stuAge = stuAge;
    }
}

@MyTable(name = "张三", age = 18, id = 1, schools = {"xx大学"})
class Demo {


    public static void main(String[] args) {
        /**
         * 使用反射读取注解信息
         */
        try {
            Class<?> clz = Class.forName("com.neo.kit.android.annotation.Student");
            //获取类身上的注解信息
            Annotation[] annotations = clz.getAnnotations();
            for (Annotation annotation : annotations) {
                Log.d("TAG", "annotation = " + annotation.toString());
            }

            //获取类身上的指定注解
            MyTable myTable = clz.getAnnotation(MyTable.class);
            Log.d("TAG", "myTable = " + myTable.value());

            MyField myField = clz.getAnnotation(MyField.class);
            Log.d("TAG", "MyField = " + myField.columName());

            Field stuNameField = clz.getDeclaredField("stuName");
            MyField fieldAnnotation = stuNameField.getAnnotation(MyField.class);
            Log.d("TAG", "stuNameField = "
                    + fieldAnnotation.columName() + fieldAnnotation.type() + fieldAnnotation.length());


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}