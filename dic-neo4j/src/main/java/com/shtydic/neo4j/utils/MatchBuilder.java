package com.shtydic.neo4j.utils;



import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.shtydic.neo4j.model.MatchModel;
import com.shtydic.neo4j.model.WhereModel2;

/**
 * Created by dengyichao on 2016/11/25.
 */
public class MatchBuilder {
    /*
     为了不让程序员私自动用这里面的属性产生严重的操作失误或非法人员的注入破坏程序
     把这一些属性提取到工具类里面，
     并不提供get和set方法，不给外部人员调动
     */
    private Integer matchNumber = 0;
    private Map<Integer, LinkedList<String>> aliasOnes = new HashMap<Integer, LinkedList<String>>();
    private Map<Integer, LinkedList<String>> nexusJs = new HashMap<Integer, LinkedList<String>>();
    private MatchModel match = new MatchModel();
    private String matchStr;

    //存储创建的Node
    private static Map<String,Object> nodeMap = new HashMap<>();
    //保存一些特殊的条件
    private List<WhereModel2> wheres = new ArrayList<>();

    //添加复杂查询的条件
    public MatchBuilder addNote(String nexus, String note) {
        LinkedList<String> aliasOnesList = aliasOnes.get(matchNumber - 1);
        aliasOnesList.addLast(note);
        LinkedList<String> nexusJsList = nexusJs.get(matchNumber - 1);
        nexusJsList.addLast(nexus);
        return this;
    }

    //开始查询
    public MatchBuilder nexusStart(String varName, String nexus, String aliasOne, MatchModel.DirType dirType) {
        match.getVarNames().add(matchNumber, varName);
        match.getNexuss().add(matchNumber, nexus);
        match.getAliasOnes().add(matchNumber, aliasOne);
        match.getDirections().add(matchNumber, dirType);
        aliasOnes.put(matchNumber, new LinkedList<String>());
        nexusJs.put(matchNumber, new LinkedList<String>());
        matchNumber++;
        return this;
    }

    //创造字符串
    public String createComplexQueryStr() {
        StringBuilder sb = new StringBuilder("MATCH");
        sb.append(" ");
        if (null != aliasOnes && aliasOnes.size() > 0)
            for (int i = 0; i < aliasOnes.size(); i++) {
                StringBuilder sbStr = new StringBuilder();
                sbStr.append("(" + this.match.getVarNames().get(i) + ")");
                sbStr.append("-");
                //sbStr.append("[:" + this.match.getNexuss().get(i) + "]_(" + this.match.getAliasOnes().get(i) + ")");
                sbStr.append("[");
                if (MatchBuilder.isNexusSpecialStr(this.match.getNexuss().get(i))) {
                    sbStr.append("'" + this.match.getNexuss().get(i) + "'");
                } else {
                    sbStr.append(this.match.getNexuss().get(i));
                }

                sbStr.append("]");
                sbStr.append("_");
                sbStr.append("(" + this.match.getAliasOnes().get(i) + ")");
                for (int j = 0; j < nexusJs.get(i).size(); j++) {
                    sbStr.append("-");
                    sbStr.append("[" + nexusJs.get(i).get(j) + "]");
                    sbStr.append("_");
                    sbStr.append("(" + aliasOnes.get(i).get(j) + ")");
                }
                sbStr.append(",");
                String str = sbStr.toString();
                if (match.getDirections().get(i) == MatchModel.DirType.left) {
                    str = str.replaceAll("-", "<-");
                    str = str.replaceAll("_", "-");
                }
                if (match.getDirections().get(i) == MatchModel.DirType.right) {
                    str = str.replaceAll("_", "->");
                }
                if (match.getDirections().get(i) == MatchModel.DirType.eq) {
                    str = str.replaceAll("_", "-");
                }
                sb.append(str);
            }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * 是否需要加上路径查询
     *
     * @param pathName 随便起的变量名
     * @return
     */
    public String createComplexQueryStr(String pathName) {
        StringBuilder sb = new StringBuilder("MATCH");
        sb.append(" ");
        sb.append(pathName + "=");
        if (aliasOnes != null && aliasOnes.size() > 0)
            for (int i = 0; i < aliasOnes.size(); i++) {
                StringBuilder sbStr = new StringBuilder();
                sbStr.append("(" + this.match.getVarNames().get(i) + ")");
                sbStr.append("-");
                //sbStr.append("[:" + this.match.getNexuss().get(i) + "]_(" + this.match.getAliasOnes().get(i) + ")");
                sbStr.append("[");
                if (MatchBuilder.isNexusSpecialStr(this.match.getNexuss().get(i))) {
                    sbStr.append("'" + this.match.getNexuss().get(i) + "'");
                } else {
                    sbStr.append(this.match.getNexuss().get(i));
                }

                sbStr.append("]");
                sbStr.append("_");
                sbStr.append("(" + this.match.getAliasOnes().get(i) + ")");
                for (int j = 0; j < nexusJs.get(i).size(); j++) {
                    sbStr.append("-");
                    sbStr.append("[" + nexusJs.get(i).get(j) + "]");
                    sbStr.append("_");
                    sbStr.append("(" + aliasOnes.get(i).get(j) + ")");
                }
                sbStr.append(",");
                String str = sbStr.toString();
                if (match.getDirections().get(i) == MatchModel.DirType.left) {
                    str = str.replaceAll("-", "<-");
                    str = str.replaceAll("_", "-");
                }
                if (match.getDirections().get(i) == MatchModel.DirType.right) {
                    str = str.replaceAll("_", "->");
                }
                if (match.getDirections().get(i) == MatchModel.DirType.eq) {
                    str = str.replaceAll("_", "-");
                }
                sb.append(str);
            }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /*
        所有路径查询方法
     */
    public String macthPath(String firstPath, String endPath, Integer maxLeng, MatchModel.MathPath mathPath) {
        StringBuilder sb = new StringBuilder("MATCH");
        sb.append(" ");
        sb.append("p=");

        if (mathPath == MatchModel.MathPath.nomenclature) {    //命名路径
            sb.append("(" + firstPath + ")" + "-->" + "(" + endPath + ")");
            return sb.toString();
        }

        if (mathPath == MatchModel.MathPath.minimum) {      //最短路径
            sb.append("shortestPath");
        } else if (mathPath == MatchModel.MathPath.minimunAll) {     //所有最短路径
            sb.append("allShortestPaths");
        }
        sb.append("(");
        sb.append("(" + firstPath + ")" + "-");
        sb.append("[*.." + maxLeng);
        sb.append("]");
        sb.append("-");
        sb.append("(" + endPath + ")");
        sb.append(")");
        return sb.toString();
    }

    //单独的命名查询
    public String macthPath(String firstPath, String endPath) {
        StringBuilder sb = new StringBuilder("MATCH");
        sb.append(" ");
        sb.append("p=");
        sb.append("(" + firstPath + ")" + "-->" + "(" + endPath + ")");
        return sb.toString();
    }

    //查询可变长度的关系
    public static String findMatchNexusAll(MatchModel matchModel) {
        if (matchModel == null) return null;

        StringBuilder sb = new StringBuilder("MATCH ");
        if (1 == 1) {
            sb.append("(" + matchModel.getVarName() + ")");
            sb.append("-");
            if (null != matchModel.getNexus()) {
                sb.append("[r:");
                if (MatchBuilder.isNexusSpecialStr(matchModel.getNexus())) {
                    sb.append("'" + matchModel.getNexus() + "'");
                } else {
                    sb.append(matchModel.getNexus());
                }

                if (matchModel.getDepth() != null) {
                    sb.append("*1.." + matchModel.getDepth());
                }
                sb.append("]");
            } else {
                matchModel.setDirection(MatchModel.DirType.eq);
            }

            sb.append("_");
            sb.append("(b)");
        }
        String str = sb.toString();
        str = MatchBuilder.dirViewTool(matchModel, str);
        return str;
    }

    /**
     * 处理关系方向
     *
     * @param matchModel
     * @param str
     * @return
     */
    public static String dirViewTool(MatchModel matchModel, String str) {
        if (matchModel.getDirection() == MatchModel.DirType.left) {
            str = str.replaceFirst("-", "<-");
            str = str.replaceAll("_", "<-");
            int index = str.lastIndexOf("<");
            str = str.substring(0, index) + str.substring(index + 1, str.length());
        }
        if (matchModel.getDirection() == MatchModel.DirType.right) {
            str = str.replaceAll("_", "->");
        }
        if (matchModel.getDirection() == MatchModel.DirType.eq) {
            str = str.replaceAll("_", "-");
        }
        return str;
    }

    //用来判断特殊字符的关系
    public static boolean isNexusSpecialStr(String str) {
        if (str.trim().indexOf(" ") != -1) return true;
        return false;
    }



    public static String createNode(String name,Object o){
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
            if(nodeMap == null || nodeMap.size() == 0) throw new RuntimeException("该对象并没有任何的属性");
            sb.append("{");
            for(String key: nodeMap.keySet()){
                sb.append(key);
                sb.append(":");
                if(nodeMap.get(key) instanceof java.lang.String){
                    sb.append("'");
                    sb.append(nodeMap.get(key));
                    sb.append("'");
                    sb.append(",");
                }else{
                    sb.append(nodeMap.get(key));
                    sb.append(",");
                }
            }
            sb = sb.deleteCharAt(sb.length()-1);
            sb.append("}");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        sb.append(")");
        return sb.toString();
    }

    public static void recursive(Object o,Class _class){
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
                        nodeMap.put(key,object);
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
