package com.shtydic.neo4j.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dengyichao on 2017/2/21.
 */
public class ObjectUtil {
    public static void Reflect_Object(Object o,String classPath){
        try {
            Class userClass = Class.forName(classPath);//加载类
            Method[] methods = userClass.getDeclaredMethods();//获得类的方法集合
            //遍历方法集合
            for(int i =0 ;i<methods.length;i++){
                //获取所有getXX()的返回值
                //methods[i].getName()方法返回方法名
                if(methods[i].getName().startsWith("get")){
                    Object object = methods[i].invoke(o, null);
                    System.out.println(" "+methods[i].getName()+"="+object);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回类名
     * @param obj
     * @return
     */
    public static String className(Object obj){
        String name = obj.getClass().getName();
        name = name.substring(name.lastIndexOf(".")+1,name.length());
        return name;
    }


    private Map<String,String> map = new HashMap<>();

    /**
     * 创建带属性的节点  只能作用于有索引的节点
     * @param o
     * @param
     * @return
     */
    public String createNode(String name,Object o){
        Class _class = null;
        StringBuilder sb = new StringBuilder("(");
        try {
            System.out.println(o.getClass().getName());
            _class = Class.forName(o.getClass().getName());
            String classPath = _class.getName();
            String neo4jName = classPath.substring(classPath.lastIndexOf(".") + 1, classPath.length());
            sb.append(name+":");
            sb.append(neo4jName);
            recursive(o,_class);
            if(map == null || map.size() == 0) throw new RuntimeException("该对象并没有任何的属性");
            sb.append("{");
            for(String key: map.keySet()){
                sb.append(key);
                sb.append(":");
                if(map.get(key) instanceof String){
                    sb.append("'");
                    sb.append(map.get(key));
                    sb.append("'");
                    sb.append(",");
                }else{
                    sb.append(map.get(key));
                    sb.append(",");
                }
            }
            sb = sb.deleteCharAt(sb.length()-1  );
            sb.append("}");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * 抽取出被反射出的类
     * @param o
     * @return
     */
    public Map<String,String> objectMap(Object o){
        recursive(o,o.getClass());
        return map;

    }

    /**
     * 通过反射把类的所有属性赋值到map集合里面
     * @param o 对象属性
     * @param _class  类路径
     */
    public void recursive(Object o,Class _class){
        if(_class==null){

        }else{
            Method[] methods = _class.getDeclaredMethods();// 获得类的方法集合
            // 遍历方法集合
            for (int i = 0; i < methods.length; i++) {
                // 获取所有getXX()的返回值
                if (methods[i].getName().startsWith("get")) {// 方法返回方法名
                    methods[i].setAccessible(true);//允许private被访问(以避免private getXX())
                    Object object = null;
                    try {
                        try {
                            object = methods[i].invoke(o, null);
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        String key = methods[i].getName().substring(3, methods[i].getName().length());
                        
                        StringBuilder sb = new StringBuilder(key);
                        sb.setCharAt(0,Character.toLowerCase(sb.charAt(0)));
                        key = sb.toString();
                        //如果为obj自带的getClass则跳过
                        if(("class").equals(key)) continue;
                        if(null == object) continue;
                        map.put(key,object.toString());
//                        System.out.println(" " + methods[i].getName() + "=" + object);
                    } catch (IllegalArgumentException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            if(_class.getName().equals("java.lang.Object")){
//                System.out.println("222222====没有了e");
            }else{
//                System.out.println("11111===="+_class.getName());
                recursive(o,_class.getSuperclass());
            }
        }
    }
}
